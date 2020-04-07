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
    private AppPktPojo AppPkt;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public AppPktPojo getAppPkt() {
        return AppPkt;
    }

    public void setAppPkt(AppPktPojo appPkt) {
        AppPkt = appPkt;
    }

    @Override
    public String toString() {
        return "CommunicatePacketPojo{" +
                "UUID='" + UUID + '\'' +
                ", AppPkt=" + AppPkt +
                '}';
    }
}
