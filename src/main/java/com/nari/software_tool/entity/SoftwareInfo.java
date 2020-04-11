package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Component
public class SoftwareInfo {
    //编号
    private String id;
    //软件ID
    private String softId;
    //名称
    private String name;
    //英文名称
    private String nameEn;
    //类别
    private String kind;
    //软件图标名称
    private String icon;
    //简介
    private String briefIntroduction;
    //大小
    private String size;
    //最新版本
    private String latestVersion;
    //软件介绍
    private String introduction;
    //软件目录
    private String filePath;
    //安装类型
    private int install_type;
    //操作员
    private String userId;
    //使用权限
    private int AllowUser;
    //产品代码
    private String ProductCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoftId() {
        return softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getInstallType() {
        return install_type;
    }

    public void setInstallType(int install_type) {
        this.install_type = install_type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAllowUser() {
        return AllowUser;
    }

    public void setAllowUser(int AllowUser) {
        this.AllowUser = AllowUser;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String ProductCode) {
        this.ProductCode = ProductCode;
    }

    @Override
    public String toString() {
        return "SoftPathInfo{" +
                "id=" + id +
                ", softId=" + softId +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", kind='" + kind + '\'' +
                ", icon='" + icon + '\'' +
                ", briefIntroduction='" + briefIntroduction + '\'' +
                ", size='" + size + '\'' +
                ", latestVersion='" + latestVersion + '\'' +
                ", introduction='" + introduction + '\'' +
                ", filePath='" + filePath + '\'' +
                ", InstallType='" + install_type + '\'' +
                ", userId='" + userId + '\'' +
                ", AllowUser='" + AllowUser + '\'' +
                ", ProductCode='" + ProductCode + '\'' +
                '}';
    }

}
