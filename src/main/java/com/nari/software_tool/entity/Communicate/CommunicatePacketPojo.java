package com.nari.software_tool.entity.Communicate;

import com.nari.software_tool.entity.ScreenShotInfo;

import java.util.List;

/**
 * 客户端请求程序包
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public class CommunicatePacketPojo {
    //响应数据类型
    private int RESP;
    //程序包UUID
    private String UUID;
    //结果
    private int Rslt;

    //程序包对象
    private AppPktPojo AppPkt;

    public int getRESP() {
        return RESP;
    }

    public void setRESP(int RESP) {
        this.RESP = RESP;
    }

    public int getRslt() {
        return Rslt;
    }

    public void setRslt(int rslt) {
        Rslt = rslt;
    }

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
                "RESP='" + RESP + '\'' +
                ", UUID='" + UUID + '\'' +
                ", Rslt=" + Rslt +
                ", AppPkt=" + AppPkt +
                '}';
    }
}
