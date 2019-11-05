package com.laso.mybatis_generator.service;

import com.laso.mybatis_generator.common.AlertUtil;
import com.laso.mybatis_generator.model.FilePaths;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @创建人 daishu
 * @创建时间 2019/10/29
 * @描述
 */

public class FileService {

    private String TYPE_XML = ".xml";
    private String TYPE_MAPPER = ".java";

    private String XML_END = "  <!-- customized sql end-->";
    private String XML_BENIN = "  <!-- customized sql begin-->";
    private String XML_SING = "</mapper>";

    private String MAPPER_END = "    /* customized interface end */";
    private String MAPPER_BENIN = "    /* customized interface begin */";
    private String MAPPER_SING = "}";



    /**
     * 覆盖源文件
     *
     * @param tables
     * @param sourcePaths
     * @param targetPaths
     * @param modelCover
     */
    public void coverFile(List<String> tables, FilePaths sourcePaths, FilePaths targetPaths, boolean modelCover) {

        int tableNum = tables.size();
        CountDownLatch countDownLatch = new CountDownLatch(tableNum);

        CoverFileThread[] threads = new CoverFileThread[tableNum];
        for (int i = 0; i < tableNum; i++) {
            threads[i] = new CoverFileThread(tables.get(i), sourcePaths, targetPaths, countDownLatch, modelCover);
        }

        //设置特定的线程池
        ExecutorService exe = Executors.newFixedThreadPool(tableNum);
        try {
            for (CoverFileThread thread : threads) {
                exe.execute(thread);
            }
            System.out.println("线程启动完成");

            //主线程等待countDownLatch归零
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("覆盖完成");
        return;
    }

    /**
     * 获得自定义sql段
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public StringBuffer getStringBuffer(String filePath) throws IOException {

        File file = new File(filePath);

        StringBuffer sb = new StringBuffer();

        if (!file.exists()) {
            return sb;
        }

        String begin = "";
        String end = "";
        String type = this.getType(file);

        if (TYPE_XML.equals(type)) {
            begin = XML_BENIN;
            end = XML_END;
        } else if (TYPE_MAPPER.equals(type)) {
            begin = MAPPER_BENIN;
            end = MAPPER_END;
        } else {
            System.err.println("文件格式不对");
            return sb;

        }


        boolean sql = false;
        final LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        try {
            while (it.hasNext()) {
                final String line = it.nextLine();
                if (line.indexOf(begin) >= 0) {
                    sql = true;
                }
                if (sql) {
                    sb.append("\r\n");
                    sb.append(line);
                }
                if (line.indexOf(end) >= 0) {
                    sql = false;
                }
            }

            return sb;

        } finally {
            it.close();
        }
    }


    /**修改生成的文件 俺自己都看不懂了 QAQ
     * @param path
     * @throws IOException
     */
    public void addCustomized(String path, StringBuffer sb) throws IOException {
        boolean a = sb == null || sb.length() == 0;

        File file = new File(path);
        String type = this.getType(file);
        String sing = "";
        if (TYPE_XML.equals(type)) {
            sing = XML_SING;
            if (a) {
                sb.append(XML_BENIN).append("\r\n").append(XML_END);
            }
        } else if (TYPE_MAPPER.equals(type)) {
            sing = MAPPER_SING;
            if (a) {
                sb.append("\r\n").append(MAPPER_BENIN).append("\r\n").append(MAPPER_END);
            }
        } else {
            System.err.println("文件格式不对");
            return;
        }

        String temFilePath = file.getParent() + File.separator + "tem" + file.getName();
        File temFile = new File(temFilePath);
        file.renameTo(temFile);
        file.createNewFile();
        // in
        FileReader fr = new FileReader(temFilePath);
        BufferedReader br = new BufferedReader(fr);
        // out
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path)),"utf-8"));

        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (line.indexOf(sing) >= 0) {
                    bw.write(sb.toString());
                    bw.write("\r\n");
                }
                bw.write(line);
                bw.write("\r\n");
            }

        } catch (Exception e) {
            System.err.println("read errors :" + e);
        } finally {
            bw.flush();
            bw.close();
            fr.close();
            br.close();
        }
    }

    /**
     * 获得文件后缀名
     */
    private String getType(File file) {
        String fileName = file.getName();
        String type = fileName.substring(fileName.lastIndexOf("."));
        return type;
    }


    /**
     * 检查并创建不存在的文件夹
     *
     * @return
     */
    public boolean checkDirs(FilePaths filePaths) {
        List<String> dirs = new ArrayList<>();
        dirs.add(FilenameUtils.normalize(filePaths.getModelPath()));
        dirs.add(FilenameUtils.normalize(filePaths.getMapperPath()));
        dirs.add(FilenameUtils.normalize(filePaths.getXmlPath()));
        boolean haveNotExistFolder = false;
        for (String dir : dirs) {
            File file = new File(dir);
            if (!file.exists()) {
                haveNotExistFolder = true;
            }
        }
        if (haveNotExistFolder) {
            try {
                for (String dir : dirs) {
                    FileUtils.forceMkdir(new File(dir));
                }
                return true;
            } catch (Exception e) {
                AlertUtil.showErrorAlert("创建临时文件目录失败");
            }
        }
        return true;
    }

}
