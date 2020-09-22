package com.laso.mybatis_generator.controller;

import com.alibaba.fastjson.JSON;
import com.laso.mybatis_generator.MainUI;
import com.laso.mybatis_generator.common.AlertUtil;
import com.laso.mybatis_generator.common.DbRemarksCommentGenerator;
import com.laso.mybatis_generator.model.FilePaths;
import com.laso.mybatis_generator.model.XmlInfo;
import com.laso.mybatis_generator.service.FileService;
import com.laso.mybatis_generator.service.GenService;
import com.laso.mybatis_generator.service.GenerateThread;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * @创建人 daishu
 * @创建时间 2019/10/26
 * @描述
 */
@Slf4j
public class ConfigController implements Initializable {

    @FXML
    private TextField modelTargetPackage;

    @FXML
    private TextField modelTargetProject;

    @FXML
    private TextField mapperTargetPackage;

    @FXML
    private TextField mapperTargetProject;

    @FXML
    private TextField xmlTargetPackage;

    @FXML
    private TextField xmlTargetProject;

    @FXML
    private VBox checkTables;

    @FXML
    private TextField projectFolderField;

    @FXML
    private CheckBox modelCover;

    @FXML
    private MenuBar meun;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextArea consolePrint;

    String modelPa;
    String modelPr;
    String mapperPa;
    String mapperPr;
    String xmlPa;
    String xmlPr;
    String url;
    String user;
    String password;

    List<String> tableNames;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        IndexController indexController = (IndexController) MainUI.controllers.get(IndexController.class.getSimpleName());

        XmlInfo xmlInfo = indexController.xmlInfo;

        // 控制台输出流更变
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                String text = String.valueOf((char) b);
                Platform.runLater(() -> {
                    consolePrint.appendText(text);
                });
            }

            @Override
            public void write(byte[] b, int off, int len) {
                String s = new String(b, off, len);
                Platform.runLater(() -> consolePrint.appendText(s));
            }
        }, true));
        System.setErr(System.out);

        meun.prefWidthProperty().bind(pane.widthProperty());

        // 创建菜单
        Menu menu1 = new Menu("关于我们");

        MenuItem menuItem1 = new MenuItem("基因宝介绍");
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage configStage = new Stage();
                configStage.setTitle("基因宝介绍");
                Parent config = null;
                try {
                    config = FXMLLoader.load(getClass().getResource("/fxml/aboutGenebox.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                configStage.setScene(new Scene(config));
                configStage.show();

            }
        });

//        MenuItem menuItem2 = new MenuItem("加入我们");
//        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                Stage configStage = new Stage();
//                configStage.setTitle("加入我们");
//                Parent config = null;
//                try {
//                    config = FXMLLoader.load(getClass().getResource("/fxml/joinUs.fxml"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                configStage.setTitle("index");
//                configStage.setScene(new Scene(config));
//                configStage.show();
//
//            }
//        });

        menu1.getItems().setAll(menuItem1);

        // 将菜单添加进菜单栏
        ObservableList<Menu> menus = meun.getMenus();
        menus.clear();
        menus.addAll(menu1);

        url = xmlInfo.getUrl();
        user = xmlInfo.getUser();
        password = xmlInfo.getPassword();

        // 若数据来自上传xml 则设置初始值

        modelPa = xmlInfo.getModelPa();
        modelPr = xmlInfo.getModelPr();
        mapperPa = xmlInfo.getMapperPa();
        mapperPr = xmlInfo.getMapperPr();
        xmlPa = xmlInfo.getXmlPa();
        xmlPr = xmlInfo.getXmlPr();

        //设置初始值
        modelTargetPackage.setText(modelPa);
        modelTargetProject.setText(modelPr);
        mapperTargetPackage.setText(mapperPa);
        mapperTargetProject.setText(mapperPr);
        xmlTargetPackage.setText(xmlPa);
        xmlTargetProject.setText(xmlPr);

        //路径初始值
        if (!indexController.path.isEmpty()) {
            projectFolderField.setText(indexController.path);
        }

        //获取数据库中的表
        GenService genService = new GenService();
        tableNames = genService.getTableNames(url, user, password);

        tableNames.forEach(name -> {
            CheckBox tableCheck = new CheckBox();
            tableCheck.setId(name);

            //TODO 超级无敌大坑 tableCheck.setText("_" + name); text 为页面展示的内容 但是它这个控件默认 把第一个下划线去掉了。不明白内部做了什么。所以 手动拼接一个下划线在前面解决问题了。😭

            tableCheck.setText("_" + name);

            log.info("vbox 控件对象：{}", tableCheck.toString());

            checkTables.getChildren().add(tableCheck);
        });
        modelCover.setSelected(true);
        System.out.println("请填写路径信息和选择要生成的表");

    }


    @FXML
    void generat(ActionEvent event) {

        FileService fileService = new FileService();
        GenService genService = new GenService();

        modelPa = modelTargetPackage.getText();
        modelPr = modelTargetProject.getText();
        mapperPa = mapperTargetPackage.getText();
        mapperPr = mapperTargetProject.getText();
        xmlPa = xmlTargetPackage.getText();
        xmlPr = xmlTargetProject.getText();

        ObservableList<Node> children = checkTables.getChildren();

        FilePaths sourcePaths = new FilePaths();
        sourcePaths.setModelPath(System.getProperty("java.io.tmpdir") + File.separator + "mybatisGeneratorTmp" + File.separator + modelPr + File.separator + this.getPaPath(modelPa));
        sourcePaths.setMapperPath(System.getProperty("java.io.tmpdir") + File.separator + "mybatisGeneratorTmp" + File.separator + mapperPr + File.separator + this.getPaPath(mapperPa));
        sourcePaths.setXmlPath(System.getProperty("java.io.tmpdir") + File.separator + "mybatisGeneratorTmp" + File.separator + xmlPr + File.separator + this.getPaPath(xmlPa));

        FilePaths targetPaths = new FilePaths();
        targetPaths.setModelPath(projectFolderField.getText() + File.separator + modelPr + File.separator + this.getPaPath(modelPa));
        targetPaths.setMapperPath(projectFolderField.getText() + File.separator + mapperPr + File.separator + this.getPaPath(mapperPa));
        targetPaths.setXmlPath(projectFolderField.getText() + File.separator + xmlPr + File.separator + this.getPaPath(xmlPa));


        List<String> checkedTable = new ArrayList<>();
        CheckBox checkBox;
        for (Node box : children) {
            checkBox = (CheckBox) box;
            System.out.println("表名：" + checkBox.getId() + "  是否生成：" + checkBox.selectedProperty().get());
            if (checkBox.selectedProperty().get()) {
                checkedTable.add(checkBox.getId());
            }
        }
        if (modelPa.isEmpty() || modelPr.isEmpty() || mapperPr.isEmpty() || mapperPa.isEmpty() || xmlPa.isEmpty() || xmlPr.isEmpty()) {
            AlertUtil.showWarnAlert("数据填完整");
            return;
        } else if (checkedTable.isEmpty()) {
            AlertUtil.showWarnAlert("至少选一个表");
            return;
        } else if (projectFolderField.getText().isEmpty()) {
            AlertUtil.showWarnAlert("选择项目路径");
            return;
        }

        try {

            // 构建生成配置类
            Configuration configuration = new Configuration();
            org.mybatis.generator.config.Context context = new org.mybatis.generator.config.Context(ModelType.FLAT);
            configuration.addContext(context);

            context.setTargetRuntime("MyBatis3");


            PluginConfiguration pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.addProperty("type", "com.laso.mybatis_generator.common.MapperPlugin");
            pluginConfiguration.setConfigurationType("com.laso.mybatis_generator.common.MapperPlugin");
            context.addPluginConfiguration(pluginConfiguration);

            // Comment 注释相关
            CommentGeneratorConfiguration commentConfig = new CommentGeneratorConfiguration();
            commentConfig.setConfigurationType(DbRemarksCommentGenerator.class.getName());
            commentConfig.addProperty("suppressDate", "true");
            commentConfig.addProperty("suppressAllComments", "true");
            context.setCommentGeneratorConfiguration(commentConfig);


            context.addProperty("javaFileEncoding", "UTF-8");
            context.addProperty("beginningDelimiter", "`");
            context.addProperty("endingDelimiter", "`");
            context.addProperty("autoDelimitKeywords", "true");


            //表配置
            for (String tablename : checkedTable) {

                TableConfiguration tableConfig = new TableConfiguration(context);

                tableConfig.setDomainObjectName(genService.lineToHump(tablename));
                tableConfig.setTableName(tablename);
                tableConfig.setCountByExampleStatementEnabled(false);
                tableConfig.setDeleteByExampleStatementEnabled(false);
                tableConfig.setSelectByExampleStatementEnabled(false);
                tableConfig.setUpdateByExampleStatementEnabled(false);
                tableConfig.getProperties().setProperty("useActualColumnNames", "false");
                GeneratedKey generatedKey = new GeneratedKey("id", "MY", true, null);//设置主键列和生成方式
                tableConfig.setGeneratedKey(generatedKey);

                context.addTableConfiguration(tableConfig);

            }

            // 链接信息
            JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
            jdbcConfig.setConnectionURL(url);
            jdbcConfig.setPassword(password);
            jdbcConfig.setUserId(user);
            jdbcConfig.setDriverClass("com.mysql.jdbc.Driver");
            jdbcConfig.getProperties().setProperty("nullCatalogMeansCurrent", "true");

            // java model
            JavaModelGeneratorConfiguration modelConfig = new JavaModelGeneratorConfiguration();
            modelConfig.setTargetPackage(modelPa);
            modelConfig.setTargetProject(System.getProperty("java.io.tmpdir") + File.separator + "mybatisGeneratorTmp" + File.separator + modelPr);

            // xml
            SqlMapGeneratorConfiguration mapperConfig = new SqlMapGeneratorConfiguration();
            mapperConfig.setTargetPackage(xmlPa);
            mapperConfig.setTargetProject(System.getProperty("java.io.tmpdir") + File.separator + "mybatisGeneratorTmp" + File.separator + xmlPr);

            // mapper
            JavaClientGeneratorConfiguration daoConfig = new JavaClientGeneratorConfiguration();
            daoConfig.setConfigurationType("XMLMAPPER");
            daoConfig.setTargetPackage(mapperPa);
            daoConfig.setTargetProject(System.getProperty("java.io.tmpdir") + File.separator + "mybatisGeneratorTmp" + File.separator + mapperPr);

            context.setId("myid");
            context.setJdbcConnectionConfiguration(jdbcConfig);
            context.setJavaModelGeneratorConfiguration(modelConfig);
            context.setSqlMapGeneratorConfiguration(mapperConfig);
            context.setJavaClientGeneratorConfiguration(daoConfig);

            if (!(fileService.checkDirs(sourcePaths) && fileService.checkDirs(targetPaths))) {
                System.err.println("创建目录失败");
                return;
            }


            DefaultShellCallback callback = new DefaultShellCallback(true);

            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, new ArrayList<>());

            System.out.println("开始生成文件到临时文件夹...");

            Thread thread = new Thread(new GenerateThread(myBatisGenerator, checkedTable, sourcePaths, targetPaths, modelCover.isSelected()));

            System.out.println(JSON.toJSON(configuration));
            thread.start();

        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("连接超时");
        }

    }

    @FXML
    public void chooseProjectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = new Stage();
        File selectedFolder = directoryChooser.showDialog(stage);
        if (selectedFolder != null) {
            projectFolderField.setText(selectedFolder.getAbsolutePath());
        }
    }

    private String getPaPath(String pa) {
        String replace = pa.replace(".", File.separator);
        return replace;
    }

    @FXML
    void aboutUs(ActionEvent event) {

    }

    /**
     * 全选
     *
     * @param event
     */
    @FXML
    void checkAll(ActionEvent event) {
        System.out.println("表格全选");

        ObservableList<Node> children = checkTables.getChildren();
        List<String> checkedTable = new ArrayList<>();
        CheckBox checkBox;
        for (Node box : children) {
            checkBox = (CheckBox) box;
            checkBox.setSelected(true);
        }

    }

    /**
     * 反选
     *
     * @param event
     */
    @FXML
    void reversalCheck(ActionEvent event) {
        System.out.println("表格反选");

        ObservableList<Node> children = checkTables.getChildren();
        CheckBox checkBox;
        for (Node box : children) {
            checkBox = (CheckBox) box;
            checkBox.setSelected(!checkBox.selectedProperty().getValue());
        }

    }


}
