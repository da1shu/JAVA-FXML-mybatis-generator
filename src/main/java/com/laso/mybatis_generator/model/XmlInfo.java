package com.laso.mybatis_generator.model;

/**
 * @创建人 daishu
 * @创建时间 2019/11/3
 * @描述
 */
public class XmlInfo {
    String modelPa;
    String modelPr;
    String mapperPa;
    String mapperPr;
    String xmlPa;
    String xmlPr;
    String url;
    String user;
    String password;

    public String getModelPa() {
        return modelPa;
    }

    public void setModelPa(String modelPa) {
        this.modelPa = modelPa;
    }

    public String getModelPr() {
        return modelPr;
    }

    public void setModelPr(String modelPr) {
        this.modelPr = modelPr;
    }

    public String getMapperPa() {
        return mapperPa;
    }

    public void setMapperPa(String mapperPa) {
        this.mapperPa = mapperPa;
    }

    public String getMapperPr() {
        return mapperPr;
    }

    public void setMapperPr(String mapperPr) {
        this.mapperPr = mapperPr;
    }

    public String getXmlPa() {
        return xmlPa;
    }

    public void setXmlPa(String xmlPa) {
        this.xmlPa = xmlPa;
    }

    public String getXmlPr() {
        return xmlPr;
    }

    public void setXmlPr(String xmlPr) {
        this.xmlPr = xmlPr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "XmlInfo{" +
                "modelPa='" + modelPa + '\'' +
                ", modelPr='" + modelPr + '\'' +
                ", mapperPa='" + mapperPa + '\'' +
                ", mapperPr='" + mapperPr + '\'' +
                ", xmlPa='" + xmlPa + '\'' +
                ", xmlPr='" + xmlPr + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
