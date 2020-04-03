package com.nari.software_tool.entity.Communicate;

import com.nari.software_tool.entity.ScreenShotInfo;
import com.nari.software_tool.entity.SoftInstallInfo;

import java.util.List;

/**
 * 客户端请求软件列表
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public class CommunicateSoftPojo{
    //软件名称
    private String Name;
    //软件的UUID，首次上传生成
    private String UUID;
    //最新版本号
    private String Version;
    //ICON文件的URL路径
    private String Icon;
    //软件分类
    private int Type;
    //程序描述
    private String Desc;
    //历史版本数量
    private int History;
    //历史版本列表请求URL字符串
    private List<String> HistoryUrl;
    //安装配置对象
    private SoftInstallInfo softwareInstallInfo;
    //程序包对象
    private AppPktPojo softPktInfo;
    //截图列表
    private List<ScreenShotInfo> screenShotInfoList;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public int getHistory() {
        return History;
    }

    public void setHistory(int history) {
        History = history;
    }

    public List<String> getHistoryUrl() {
        return HistoryUrl;
    }

    public void setHistoryUrl(List<String> historyUrl) {
        HistoryUrl = historyUrl;
    }

    public SoftInstallInfo getSoftwareInstallInfo() {
        return softwareInstallInfo;
    }

    public void setSoftwareInstallInfo(SoftInstallInfo softwareInstallInfo) {
        this.softwareInstallInfo = softwareInstallInfo;
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
        return "CommunicateSoftPojo{" +
                "Name='" + Name + '\'' +
                ", UUID='" + UUID + '\'' +
                ", Version='" + Version + '\'' +
                ", Icon='" + Icon + '\'' +
                ", Type=" + Type +
                ", Desc='" + Desc + '\'' +
                ", History=" + History +
                ", HistoryUrl='" + HistoryUrl + '\'' +
                ", softwareInstallInfo=" + softwareInstallInfo +
                ", softPktInfo=" + softPktInfo +
                ", screenShotInfoList=" + screenShotInfoList +
                '}';
    }
}
