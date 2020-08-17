package com.laso.mybatis_generator.common;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建人 daishu
 * @创建时间 2019/10/26
 * @描述
 */
public class JDBCUtil {
    /**
     * 连接数据库,返回Result
     *
     * @return
     */
    public static List<String> jdbcGetTableList(String url, String user, String password, String sql) {

        Connection connection = null;
        ResultSet rs = null;
        List<String> tableList = new ArrayList<>();
        try {
            //加载驱动程序
            connection = DriverManager.getConnection(url, user, password);
            if (!connection.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
            }
            //2.创建statement类对象，用来执行SQL语句
            Statement statement = connection.createStatement();
            //3.ResultSet类，用来存放获取的结果集
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                tableList.add(rs.getString("TABLE_NAME"));
            }

            rs.close();
            connection.close();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            System.err.println("链接数据库失败");
            AlertUtil.showErrorAlert("数据库连接失败");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
        return tableList;

    }
}
