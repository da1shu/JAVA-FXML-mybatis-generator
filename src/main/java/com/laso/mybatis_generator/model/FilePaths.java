package com.laso.mybatis_generator.model;

/**
 * @创建人 daishu
 * @创建时间 2019/10/30
 * @描述
 */
public class FilePaths {
    String modelPath;
    String mapperPath;
    String xmlPath;

    public FilePaths() {
    }

    public FilePaths(String modelPath, String mapperPath, String xmlPath) {
        this.modelPath = modelPath;
        this.mapperPath = mapperPath;
        this.xmlPath = xmlPath;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPatt) {
        this.mapperPath = mapperPatt;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }


    @Override
    public String toString() {
        return "FilePaths{" +
                "modelPath='" + modelPath + '\'' +
                ", mapperPath='" + mapperPath + '\'' +
                ", xmlPath='" + xmlPath + '\'' +
                '}';
    }
}
