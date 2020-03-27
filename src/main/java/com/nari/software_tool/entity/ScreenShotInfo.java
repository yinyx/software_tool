package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

/**
 * 截图列表
 * @author yinyx
 * @version 1.0 2020/3/25
 */
@Component
public class ScreenShotInfo {
    //软件记录编号
    private String Id;
    // 软件名
    private String softName;
    //文件名
    private String shotsName;
    //存储url
    private String url;
    //截图序号
    private int shotId;
    //创建时间
    private String  createTime;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getShotsName() {
        return shotsName;
    }

    public void setShotsName(String shotsName) {
        this.shotsName = shotsName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getShotId() {
        return shotId;
    }

    public void setShotId(int shotId) {
        this.shotId = shotId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ScreenShotInfo{" +
                "id='" + Id + '\'' +
                ", softName='" + softName + '\'' +
                ", shotsName='" + shotsName + '\'' +
                ", url='" + url + '\'' +
                ", shotId=" + shotId +
                ", createTime=" + createTime +
                '}';
    }
}
