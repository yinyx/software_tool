package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Component
public class SoftPathInfo {

    //目录编号
    private int pathId;
    //软件ID
    private String softId;
    //根目录
    private String routePath;
    //类别目录
    private String typePath;
    //名称目录
    private String namePath;
    //分支目录
    private String branchPath;
    //版本目录
    private String editionPath;

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public String getSoftId() {
        return softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getTypePath() {
        return typePath;
    }

    public void setTypePath(String typePath) {
        this.typePath = typePath;
    }

    public String getNamePath() {
        return namePath;
    }

    public void setNamePath(String namePath) {
        this.namePath = namePath;
    }

    public String getBranchPath() {
        return branchPath;
    }

    public void setBranchPath(String branchPath) {
        this.branchPath = branchPath;
    }

    public String getEditionPath() {
        return editionPath;
    }

    public void setEditionPath(String editionPath) {
        this.editionPath = editionPath;
    }

    @Override
    public String toString() {
        return "SoftPathInfo{" +
                "pathId=" + pathId +
                ", softId=" + softId +
                ", routePath='" + routePath + '\'' +
                ", typePath='" + typePath + '\'' +
                ", namePath='" + namePath + '\'' +
                ", branchPath='" + branchPath + '\'' +
                ", editionPath='" + editionPath + '\'' +
                '}';
    }
}
