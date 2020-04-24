package com.nari.software_tool.entity.Communicate;

import com.nari.software_tool.entity.ScreenShotInfo;
import com.nari.software_tool.entity.SoftInstallInfo;

import java.util.List;
import java.util.Map;

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
    private String Type;
    //允许按照的最低用户类型
    private int AllowUser;
    //MSI安装程序产品代码或InnoSetup 的程序名
    private String ProductCode;
    //程序描述
    private String Desc;
    //历史版本数量
    private int History;
    //历史版本列表请求URL字符串
    private String HistoryUrl;
    //安装配置对象
    private SoftInstallInfo Install;
    //程序包对象
    private AppPktPojo AppPkt;
    //截图列表
    private List<Map<String,Object>> ScreenShots;

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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public int getAllowUser() {
        return AllowUser;
    }

    public void setAllowUser(int allowUser) {
        AllowUser = allowUser;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public int getHistory() {
        return History;
    }

    public void setHistory(int history) {
        History = history;
    }

    public String getHistoryUrl() {
        return HistoryUrl;
    }

    public void setHistoryUrl(String historyUrl) {
        HistoryUrl = historyUrl;
    }

    public SoftInstallInfo getInstall() {
        return Install;
    }

    public void setInstall(SoftInstallInfo install) {
        Install = install;
    }

    public AppPktPojo getAppPkt() {
        return AppPkt;
    }

    public void setAppPkt(AppPktPojo appPkt) {
        AppPkt = appPkt;
    }

    public List<Map<String, Object>> getScreenShots() {
        return ScreenShots;
    }

    public void setScreenShots(List<Map<String, Object>> screenShots) {
        ScreenShots = screenShots;
    }

    @Override
    public String toString() {
        return "CommunicateSoftPojo{" +
                "Name='" + Name + '\'' +
                ", UUID='" + UUID + '\'' +
                ", Version='" + Version + '\'' +
                ", Icon='" + Icon + '\'' +
                ", Type='" + Type + '\'' +
                ", AllowUser=" + AllowUser +
                ", ProductCode='" + ProductCode + '\'' +
                ", Desc='" + Desc + '\'' +
                ", History=" + History +
                ", HistoryUrl='" + HistoryUrl + '\'' +
                ", Install=" + Install +
                ", AppPkt=" + AppPkt +
                ", ScreenShots=" + ScreenShots +
                '}';
    }
}
