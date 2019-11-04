package com.laso.mybatis_generator.service;

import com.laso.mybatis_generator.model.FilePaths;
import org.apache.commons.io.FileUtils;
import org.mybatis.generator.api.MyBatisGenerator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @创建人 daishu
 * @创建时间 2019/11/2
 * @描述
 */
public class GenerateThread implements Runnable {

    private MyBatisGenerator myBatisGenerator;
    private List<String> checkedTable;
    private FilePaths sourcePaths;
    private FilePaths targetPaths;
    private boolean a;

    public GenerateThread(MyBatisGenerator myBatisGenerator, List<String> checkedTable, FilePaths sourcePaths, FilePaths targetPaths, boolean a) {
        this.myBatisGenerator = myBatisGenerator;
        this.checkedTable = checkedTable;
        this.sourcePaths = sourcePaths;
        this.targetPaths = targetPaths;
        this.a = a;
    }

    @Override
    public void run() {

        try {
            System.out.println("生成中，请稍等...");
            myBatisGenerator.generate(null);
            System.out.println("开始从临时文件夹覆盖到目标路径...");

            FileService fileService = new FileService();
            // 新文件操作
            fileService.coverFile(checkedTable, sourcePaths, targetPaths, a);

            // 删除临时文件夹
            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "mybatisGeneratorTmp");
            FileUtils.deleteDirectory(file);

            System.out.println("开始删除临时文件...");

            System.out.println("生成完毕！");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (IllegalStateException e){

        }

    }
}
