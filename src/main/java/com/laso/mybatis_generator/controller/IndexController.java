package com.laso.mybatis_generator.controller;

import com.laso.mybatis_generator.MainUI;
import com.laso.mybatis_generator.common.AlertUtil;
import com.laso.mybatis_generator.common.XmlUtil;
import com.laso.mybatis_generator.model.XmlInfo;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * @创建人 daishu
 * @创建时间 2019/10/25
 * @描述
 */
public class IndexController implements Initializable {

    @FXML
    private TextField url;

    @FXML
    private TextField user;

    @FXML
    private TextField password;

    @FXML
    private MenuBar meun;

    @FXML
    private AnchorPane pane;


    XmlInfo xmlInfo;
    String path;

    @FXML
    private TextArea consolePrint;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //添加到MainUi的controller容器中 便于其他controller获取该controller对象
        MainUI.controllers.put(this.getClass().getSimpleName(), this);

        //绑定为新的输出流
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
                Platform. runLater(() -> consolePrint. appendText(s));
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


        menu1.getItems().setAll(menuItem1);

        // 将菜单添加进菜单栏
        ObservableList<Menu> menus = meun.getMenus();
        menus.clear();
        menus.addAll(menu1);

        System.out.println("欢迎使用");
        System.out.println("可选择上传配置文件或者手动填写连接信息");





    }

    /**
     * 上传xml
     *
     * @param event
     */
    @FXML
    void loadXML(ActionEvent event) {

        try {

            System.out.println("选择xml文件");

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("选择需要的打开的文件");

            /*初始路径*/
            fileChooser.setInitialDirectory(new File("."));
            Stage prStage = new Stage(StageStyle.DECORATED);
            File xmlFile = fileChooser.showOpenDialog(prStage);

            if(xmlFile == null || !xmlFile.exists()){
                return;
            }

            path = xmlFile.getParentFile().getParentFile().getParentFile().getParent();

            System.out.println("获得xml文件，文件名为：" + xmlFile.getName() + "项目路径为：" + path);

            /*解析xml*/
            xmlInfo = XmlUtil.getXmlInfo(xmlFile);
            if (xmlInfo.getPassword().isEmpty()||xmlInfo.getUrl().isEmpty()||xmlInfo.getUser().isEmpty()){
                System.err.println("解析失败，链接信息缺失");
                return;
            }

            System.out.println("解析完成");
            System.out.println(xmlInfo.toString());

            /*创建新舞台*/
            Stage configStage = new Stage();
            configStage.setTitle("配置选择");
            Parent config = FXMLLoader.load(getClass().getResource("/fxml/config.fxml"));
            configStage.setScene(new Scene(config));
            configStage.show();

        } catch (Exception e) {
            System.err.println("文件解析错误");
            e.printStackTrace();
            AlertUtil.showErrorAlert("解析文件失败");
        }

    }


    /**
     * 手动配置
     *
     * @param event
     */
    @FXML
    void config(ActionEvent event) {

        System.out.println("手动配置数据源");

        if (url.getText().isEmpty() || user.getText().isEmpty()) {
            AlertUtil.showWarnAlert("数据填完整");
            return;
        }
        try {

            xmlInfo.setUrl(url.getText());
            xmlInfo.setPassword(password.getText());
            xmlInfo.setUser(user.getText());

            /*创建新舞台*/
            Stage configStage = new Stage();
            configStage.setTitle("配置选择");
            Parent config = FXMLLoader.load(getClass().getResource("/fxml/config.fxml"));
            configStage.setTitle("index");
            configStage.setScene(new Scene(config));
            configStage.show();

        } catch (Exception e) {
            System.err.println("数据源错误");
            AlertUtil.showErrorAlert("数据源错误");
        }

    }


}
