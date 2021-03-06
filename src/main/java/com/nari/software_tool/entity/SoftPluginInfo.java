package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author yinyx
 * @version 1.0 2020/4/10
 */
@Component
public class SoftPluginInfo {

    private String pluginId;

    private String Type;

    private String softId;

    private String Branch;

    private String Version;

    private String pluginName;

    private String pluginMD5;

    private Date uploadTime;

    private String relativePath;

    private String absolutePath;

    private String operator;

    private String description;

    private String size;

    private String pluginIcon;

    private String pluginVersion;

    private String keyFile;

    private String keyFileMd5;

    private String appPktDate;

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSoftId() {
        return softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginMD5() {
        return pluginMD5;
    }

    public void setPluginMD5(String pluginMD5) {
        this.pluginMD5 = pluginMD5;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPluginIcon() {
        return pluginIcon;
    }

    public void setPluginIcon(String pluginIcon) {
        this.pluginIcon = pluginIcon;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public String getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public String getKeyFileMd5() {
        return keyFileMd5;
    }

    public void setKeyFileMd5(String keyFileMd5) {
        this.keyFileMd5 = keyFileMd5;
    }

    public String getAppPktDate() {
        return appPktDate;
    }

    public void setAppPktDate(String appPktDate) {
        this.appPktDate = appPktDate;
    }

    @Override
    public String toString() {
        return "SoftPluginInfo{" +
                "pluginId='" + pluginId + '\'' +
                ", Type='" + Type + '\'' +
                ", softId='" + softId + '\'' +
                ", Branch='" + Branch + '\'' +
                ", Version='" + Version + '\'' +
                ", pluginName='" + pluginName + '\'' +
                ", pluginMD5='" + pluginMD5 + '\'' +
                ", uploadTime=" + uploadTime +
                ", relativePath='" + relativePath + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", operator='" + operator + '\'' +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                ", pluginIcon='" + pluginIcon + '\'' +
                ", pluginVersion='" + pluginVersion + '\'' +
                ", keyFile='" + keyFile + '\'' +
                ", keyFileMd5='" + keyFileMd5 + '\'' +
                ", appPktDate='" + appPktDate + '\'' +
                '}';
    }
}
