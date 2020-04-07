package com.nari.software_tool.entity.Communicate;

/**
 * 客户端请求历史版本列表
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public class CommunicateHistoryPojo {
    //程序包对象
    private AppPktPojo AppPkt;

    public AppPktPojo getAppPkt() {
        return AppPkt;
    }

    public void setAppPkt(AppPktPojo appPkt) {
        AppPkt = appPkt;
    }

    @Override
    public String toString() {
        return "CommunicateHistoryPojo{" +
                "AppPkt=" + AppPkt +
                '}';
    }
}
