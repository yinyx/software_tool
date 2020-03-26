package com.nari.software_tool.entity.Communicate;

import com.nari.software_tool.entity.ScreenShotInfo;

import java.util.List;

/**
 * 客户端请求历史版本列表
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public class CommunicateHistoryPojo {
    //程序包对象
    private AppPktPojo softPktInfo;
    //截图列表
    private List<ScreenShotInfo> screenShotInfoList;

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
        return "CommunicateHistoryPojo{" +
                "softPktInfo=" + softPktInfo +
                ", screenShotInfoList=" + screenShotInfoList +
                '}';
    }
}
