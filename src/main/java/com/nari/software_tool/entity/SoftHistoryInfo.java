package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 程序包对象POJO
 * @author yinyx
 * @version 1.0 2020/3/25
 */
@Component
public class SoftHistoryInfo {
    //编号
    private int id;

    private String softId;

    private String branchId;

    private String historyVersion;

    private String historyPath;

    private Date uploadDate;

    private String operator;

    private Date appPktDate;

    private String appPktNew;

    private String appPktPath;

    private int appPktSize;

    private String appPktMd5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoftId() {
        return softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getHistoryVersion() {
        return historyVersion;
    }

    public void setHistoryVersion(String historyVersion) {
        this.historyVersion = historyVersion;
    }

    public String getHistoryPath() {
        return historyPath;
    }

    public void setHistoryPath(String historyPath) {
        this.historyPath = historyPath;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getAppPktDate() {
        return appPktDate;
    }

    public void setAppPktDate(Date appPktDate) {
        this.appPktDate = appPktDate;
    }

    public String getAppPktNew() {
        return appPktNew;
    }

    public void setAppPktNew(String appPktNew) {
        this.appPktNew = appPktNew;
    }

    public String getAppPktPath() {
        return appPktPath;
    }

    public void setAppPktPath(String appPktPath) {
        this.appPktPath = appPktPath;
    }

    public int getAppPktSize() {
        return appPktSize;
    }

    public void setAppPktSize(int appPktSize) {
        this.appPktSize = appPktSize;
    }

    public String getAppPktMd5() {
        return appPktMd5;
    }

    public void setAppPktMd5(String appPktMd5) {
        this.appPktMd5 = appPktMd5;
    }

    @Override
    public String toString() {
        return "SoftHistoryInfo{" +
                "id=" + id +
                ", softId='" + softId + '\'' +
                ", branchId='" + branchId + '\'' +
                ", historyVersion='" + historyVersion + '\'' +
                ", historyPath='" + historyPath + '\'' +
                ", uploadDate=" + uploadDate +
                ", operator='" + operator + '\'' +
                ", appPktDate=" + appPktDate +
                ", appPktNew='" + appPktNew + '\'' +
                ", appPktPath='" + appPktPath + '\'' +
                ", appPktSize=" + appPktSize +
                ", appPktMd5='" + appPktMd5 + '\'' +
                '}';
    }
}