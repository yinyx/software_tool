package com.nari.software_tool.entity.Communicate;

import com.nari.software_tool.entity.ScreenShotInfo;

import java.util.List;

/**
 * 客户端请求程序包
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public class CommunicatePacketPojo {

    //程序包UUID
    private String UUID;

    //程序包对象
    private AppPktPojo softPktInfo;
    //截图列表
    private List<ScreenShotInfo> screenShotInfoList;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public AppPktPojo getSoftPktInfo() {
        return softPktInfo;
    }

    public void setSoftPktInfo(AppPktPojo softPktInfo) {
        this.softPktInfo = softPktInfo;
    }

    public List<ScreenShotInfo> getScreenShotInfoList() {
        return screenShotInfoList;
    }

    public void setScreenShotInfoList(List<ScreenShotInfo> screenShotInfoList) {
        this.screenShotInfoList = screenShotInfoList;
    }

    @Override
    public String toString() {
        return "CommunicatePacketPojo{" +
                "UUID='" + UUID + '\'' +
                ", softPktInfo=" + softPktInfo +
                ", screenShotInfoList=" + screenShotInfoList +
                '}';
    }
}
