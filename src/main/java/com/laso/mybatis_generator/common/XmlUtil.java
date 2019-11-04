package com.laso.mybatis_generator.common;

import com.laso.mybatis_generator.model.XmlInfo;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @创建人 daishu
 * @创建时间 2019/11/2
 * @描述
 */
public class XmlUtil {

    public static XmlInfo getXmlInfo(File file) {
        XmlInfo xmlInfo = new XmlInfo();

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);;

            Element rootElement = document.getRootElement();

            List skill1 = rootElement.elements("context");


            Element context = (Element) skill1.get(0);

            List skill2 = context.elements("jdbcConnection");
            Element jdbcConnection = (Element) skill2.get(0);

            for (Iterator<Attribute> it2 = jdbcConnection.attributeIterator(); it2.hasNext(); ) {
                Attribute attr = it2.next();
                if ("connectionURL".equals(attr.getName())) {
                    xmlInfo.setUrl(attr.getValue());
                }
                if ("userId".equals(attr.getName())) {
                    xmlInfo.setUser(attr.getValue());
                }
                if ("password".equals(attr.getName())) {
                    xmlInfo.setPassword(attr.getValue());
                }
            }

            List skill3 = context.elements("javaModelGenerator");

            Element javaModelGenerator = (Element) skill3.get(0);
            for (Iterator<Attribute> it3 = javaModelGenerator.attributeIterator(); it3.hasNext(); ) {
                Attribute attr3 = it3.next();
                if ("targetPackage".equals(attr3.getName())) {
                    xmlInfo.setModelPa(attr3.getValue());
                }
                if ("targetProject".equals(attr3.getName())) {
                    xmlInfo.setModelPr(attr3.getValue());
                }

            }

            List skill4 = context.elements("sqlMapGenerator");

            Element sqlMapGenerator = (Element) skill4.get(0);
            for (Iterator<Attribute> it4 = sqlMapGenerator.attributeIterator(); it4.hasNext(); ) {
                Attribute attr4 = it4.next();
                if ("targetPackage".equals(attr4.getName())) {
                    xmlInfo.setXmlPa(attr4.getValue());



                }
                if ("targetProject".equals(attr4.getName())) {
                    xmlInfo.setXmlPr(attr4.getValue());
                }

            }

            List skill5 = context.elements("javaClientGenerator");
            System.out.println(skill5.size());

            Element javaClientGenerator = (Element) skill5.get(0);
            for (Iterator<Attribute> it5 = javaClientGenerator.attributeIterator(); it5.hasNext(); ) {
                Attribute attr5 = it5.next();
                if ("targetPackage".equals(attr5.getName())) {
                    xmlInfo.setMapperPa(attr5.getValue());
                }
                if ("targetProject".equals(attr5.getName())) {
                    xmlInfo.setMapperPr(attr5.getValue());

                }

            }

        } catch (DocumentException e) {
            System.err.println("解析文件失败！");
            e.printStackTrace();
        }
        return xmlInfo;
    }


}
