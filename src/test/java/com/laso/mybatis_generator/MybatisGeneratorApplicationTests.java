package com.laso.mybatis_generator;

import com.alibaba.fastjson.JSON;
import com.laso.mybatis_generator.common.JDBCUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest(classes = MainUI.class)
class MybatisGeneratorApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("???");
    }

    @Test
    void DOMTest() throws Exception {

    }

    @Test
    void XstreamTest() throws Exception {

        String path = "C:\\lasoProject\\mybatis_generator\\mybatis_generator\\src\\main\\resources\\mybatis-generator-base.xml";


        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(path));
        Element rootElement = document.getRootElement();

        List skill1 = rootElement.elements("context");


        Element context = (Element) skill1.get(0);

        List skill2 = context.elements("jdbcConnection");
        Element jdbcConnection = (Element) skill2.get(0);
        System.out.println(skill2.size());
        for (Iterator<Attribute> it2 = jdbcConnection.attributeIterator(); it2.hasNext(); ) {
            Attribute attr = it2.next();
            if ("driverClass".equals(attr.getName())) {
                System.out.println("jdbc的" + "driverClass是");
                System.out.println(attr.getValue());
            }
            if ("connectionURL".equals(attr.getName())) {
                System.out.println("jdbc的" + "connectionURL是");

                System.out.println(attr.getValue());

            }
            if ("userId".equals(attr.getName())) {
                System.out.println("jdbc的" + "userId是");

                System.out.println(attr.getValue());

            }
            if ("password".equals(attr.getName())) {
                System.out.println("jdbc的" + "password是");

                System.out.println(attr.getValue());

            }
        }

        List skill3 = context.elements("javaModelGenerator");
        System.out.println(skill3.size());

        Element javaModelGenerator = (Element) skill3.get(0);
        for (Iterator<Attribute> it3 = javaModelGenerator.attributeIterator(); it3.hasNext(); ) {
            Attribute attr3 = it3.next();
            if ("targetPackage".equals(attr3.getName())) {
                System.out.println("model" + "targetPackage");
                System.out.println(attr3.getValue());

            }
            if ("targetProject".equals(attr3.getName())) {
                System.out.println(attr3.getValue());

            }

        }

        List skill4 = context.elements("javaModelGenerator");
        System.out.println(skill4.size());

        Element sqlMapGenerator = (Element) skill4.get(0);
        for (Iterator<Attribute> it4 = sqlMapGenerator.attributeIterator(); it4.hasNext(); ) {
            Attribute attr4 = it4.next();
            if ("targetPackage".equals(attr4.getName())) {
                System.out.println(attr4.getValue());

            }
            if ("targetProject".equals(attr4.getName())) {
                System.out.println(attr4.getValue());

            }

        }

        List skill5 = context.elements("javaClientGenerator");
        System.out.println(skill5.size());

        Element javaClientGenerator = (Element) skill5.get(0);
        for (Iterator<Attribute> it5 = javaClientGenerator.attributeIterator(); it5.hasNext(); ) {
            Attribute attr5 = it5.next();
            if ("targetPackage".equals(attr5.getName())) {
                System.out.println(attr5.getValue());

            }
            if ("targetProject".equals(attr5.getName())) {
                System.out.println(attr5.getValue());

            }

        }


    }


    /*
     解析xml失败的问题：[org.xml.sax.SAXParseException; lineNumber: 4; columnNumber: 67;
     外部 DTD: 无法读取外部 DTD 'mybatis-generator-config_1_0.dtd',
     因为 accessExternalDTD 属性设置的限制导致不允许 'http' 访问。]

     问题原因：jdk1.8的bug
     问题解决：
     在jdk的安装路径下的jre\lib下，添加一个属性文件jaxp.properties,
     并写上如下内容javax.xml.accessExternalDTD = all
     或者升级jdk
     他麻辣隔壁的
     */
    @Test
    public void JAXBXmlFileToObject() throws Exception {
        File xml = new File("C:\\lasoProject\\mybatis_generator\\mybatis_generator\\src\\main\\resources\\mybatis-generator-base.xml");
        System.out.println(xml.isFile());

//            // 解析方法（旧）
//            JAXBContext context = JAXBContext.newInstance(GeneratorConfiguration.class);
//            Unmarshaller um = context.createUnmarshaller();
//            InputStream inputStream = GeneratorConfiguration.class.getClassLoader().getResourceAsStream("mybatis-generator1.xml");//在classpath下读取xml的文件流
//            GeneratorConfiguration bean = (GeneratorConfiguration) um.unmarshal(inputStream);//将xml转换成实体对象

//            // 新
//           GeneratorConfiguration generatorConfiguration = JAXB.unmarshal(xml, GeneratorConfiguration.class);
//            System.out .println(JSON.toJSON(generatorConfiguration));


    }

    /**
     * 正则：          (?<=\d{4}/)[a-z_]+(?=\??)
     * 新正则：(?<=\w/)[a-z_]+(?=\??)
     * jdbc:mysql:\/\/(1w+).(x1w+)*(:\d+)?[a-z_]+(?=\??)
     */
    @Test
    public void zhengZeTest() {
        String text = "jdbc:mysql://mysql232.dev.genebox.cn/laso_activity?useAffectedRows=true&characterEncoding=UTF-8&autoReconnect=true";
        //匹配表名的正则
        Pattern pattern = Pattern.compile("(?<=\\w/)[a-z_]+(?=\\??)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }


    /**
     * 测试数据库连接和sql
     */
    @Test
    public void getTableTest() {
        String url = "jdbc:mysql://39.104.175.96:3306/ms_admin";
        String user = "root";
        String passWord = "lasomysql";

        String dbName = "";
        //匹配表名的正则
        Pattern pattern = Pattern.compile("(?<=\\d{4}/)[a-z_]+(?=\\??)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            dbName = matcher.group(0);
            System.out.println("正则匹配结果:" + matcher.group(0));
        }

        String sql = "select table_name from information_schema.tables where table_schema='" + dbName + "'";

        List<String> list = JDBCUtil.jdbcGetTableList(url, user, passWord, sql);

        System.out.println(JSON.toJSON(list));
    }

    @Test
    public void sbTest() throws IOException {

        StringBuffer sb = new StringBuffer();
        boolean sql = false;
        final LineIterator it = FileUtils.lineIterator(new File("C:\\\\Users\\\\daishu\\\\Desktop\\\\new.txt"), "UTF-8");
        try {
            while (it.hasNext()) {
                final String line = it.nextLine();
                if (line.indexOf("<!-- customized sql end-->") >= 0) {
                    sql = false;
                }
                if (sql) {
                    sb.append("\r\n");
                    sb.append(line);
                }
                if (line.indexOf("<!-- customized sql begin-->") >= 0) {
                    sql = true;
                }
            }
        } finally {
            it.close();
        }
        System.out.println(sb);
    }

    @Test
    public void fileTest() throws IOException {

        StringBuffer sb = new StringBuffer();
        sb.append("这是自定义sql段落");

        // in
        FileReader fr = new FileReader("C:\\\\Users\\\\daishu\\\\Desktop\\\\authorityUserMapper.xml");
        BufferedReader br = new BufferedReader(fr);

        // out
        FileWriter fw = new FileWriter("C:\\\\Users\\\\daishu\\\\Desktop\\\\authorityUserMapper.xml");
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                bw.write(line);
                bw.write("\r\n");
                if (line.indexOf("<!-- customized sql begin-->") >= 0) {
                    bw.write(sb.toString());
                    bw.write("\r\n");
                }
            }
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        } finally {
            bw.flush();
            fw.close();
            bw.close();
            fr.close();
            br.close();
        }
    }


    @Test
    public void getName() throws IOException {
        String path = "C:\\\\Users\\\\daishu\\\\Desktop\\\\temnew.txt";

        File inFile = new File(path);
        FileUtils.deleteQuietly(inFile);

    }

    @Test
    public void add() throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(" <!-- customized sql end-->");
        String path = "C:\\\\Users\\\\daishu\\\\Desktop\\\\new.txt";

        File file = new File(path);
        String temFilePath = file.getParent() + File.separator + "tem" + file.getName();
        File temFile = new File(temFilePath);

        file.renameTo(temFile);

        file.createNewFile();

        // in
        FileReader fr = new FileReader(temFilePath);
        BufferedReader br = new BufferedReader(fr);

        // out
        FileWriter fw = new FileWriter(path);
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("</mapper>") >= 0) {
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
            fw.close();
            bw.close();
            fr.close();
            br.close();
            boolean delete = temFile.delete();
            System.out.println(delete);
        }
    }



    @Test
    public void testService() throws IOException {
        String target = "C:\\\\Users\\\\daishu\\\\Desktop\\\\src\\\\new.txt";
        String tmp = "C:\\\\Users\\\\daishu\\\\Desktop\\\\new.txt";

        File newfile = new File(target);
//        if (newfile.exists()){
//            newfile.deleteOnExit();
//        }

        File oldfile = new File(tmp);
        FileUtils.copyFile(oldfile ,newfile);



    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰 获得表的别名
     *
     * @return
     */
    @Test
    public void lineToHump() throws IOException {
        String oldPath = "C:\\Users\\daishu\\Desktop\\new.java";
        File file = new File(oldPath);

        String temFilePath = file.getParent() + File.separator + "tem" + file.getName();
        File temFile = new File(temFilePath);
        file.renameTo(temFile);
        file.createNewFile();
    }

}
