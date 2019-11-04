package com.laso.mybatis_generator.service;

import com.laso.mybatis_generator.model.FilePaths;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.xml.ws.Service;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.concurrent.CountDownLatch;

/**
 * @创建人 daishu
 * @创建时间 2019/10/30
 * @描述
 */
public class CoverFileThread implements Runnable {

    String tableName;
    FilePaths tmpPath;
    FilePaths targetPath;
    private CountDownLatch countDownLatch;
    boolean model;

    public CoverFileThread(String tableName, FilePaths tmpPath, FilePaths targetPath, CountDownLatch countDownLatch, boolean model) {
        this.tableName = tableName;
        this.tmpPath = tmpPath;
        this.targetPath = targetPath;
        this.countDownLatch = countDownLatch;
        this.model = model;
    }

    @Override
    public void run() {
        System.out.println(tableName + "表文件开始覆盖");
        GenService genService = new GenService();

        String tableFileName = genService.lineToHump(tableName);
        String modelTmpPath = FilenameUtils.normalize(tmpPath.getModelPath() + File.separator + tableFileName + ".java");
        String mapperTmpPath = FilenameUtils.normalize(tmpPath.getMapperPath() + File.separator + tableFileName + "Mapper.java");
        String xmlTmpPath = FilenameUtils.normalize(tmpPath.getXmlPath() + File.separator + tableFileName + "Mapper.xml");


        String modelTargetPath = FilenameUtils.normalize(targetPath.getModelPath() + File.separator + tableFileName + ".java");
        String mapperTargetPath = FilenameUtils.normalize(targetPath.getMapperPath() + File.separator + tableFileName + "Mapper.java");
        String xmlTargetPath = FilenameUtils.normalize(targetPath.getXmlPath() + File.separator + tableFileName + "Mapper.xml");

        FileService fileService = new FileService();

        try {
            // xml
            StringBuffer stringBuffer = fileService.getStringBuffer(xmlTargetPath);
            fileService.addCustomized(xmlTmpPath, stringBuffer);
            this.replacementFile(xmlTmpPath, xmlTargetPath);

            //mapper
            StringBuffer stringBuffer1 = fileService.getStringBuffer(mapperTargetPath);
            fileService.addCustomized(mapperTmpPath, stringBuffer1);
            this.replacementFile(mapperTmpPath, mapperTargetPath);

            //dao
            if (model) {
                replacementFile(modelTmpPath, modelTargetPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(tableName + "表文件覆盖完成");
            countDownLatch.countDown();
        }
    }

    private void replacementFile(String tmp, String target) throws IOException {
        File oldfile = new File(tmp);
        File newfile = new File(target);
        FileUtils.copyFile(oldfile, newfile);
    }
}
