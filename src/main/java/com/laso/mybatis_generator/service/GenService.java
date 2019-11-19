package com.laso.mybatis_generator.service;

import com.laso.mybatis_generator.common.JDBCUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @创建人 daishu
 * @创建时间 2019/10/26
 * @描述
 */

public class GenService {


    /**
     * 从url中匹配表名的正则(?<=\w/)[a-z_]+(?=\??)
     */
    static Pattern pattern = Pattern.compile("(?<=\\w/)[a-z_]+(?=\\??)");

    public List<String> getTableNames(String url, String user, String password) {

        String dbName = "";

        //匹配表名的正则
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            dbName = matcher.group(0);
            System.out.println("数据库名为:"+matcher.group(0));
        }

        //查询表名sql
        String sql = "select table_name from information_schema.tables where table_schema='"+dbName+"'";

        List<String> list = JDBCUtil.jdbcGetTableList(url, user, password, sql);

        return list;



    }



    public static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰 获得表的别名
     *
     * @param str
     * @return
     */
    public String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);

        // 将首字母大写
        char[] ch = sb.toString().toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

}
