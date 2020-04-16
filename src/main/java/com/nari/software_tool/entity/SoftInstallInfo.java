package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

/**
 * 安装配置对象POJO
 * @author yinyx
 * @version 1.0 2020/3/25
 */
@Component
public class SoftInstallInfo {

    private String softId;

    private int Type;

    private int Multi;

    private String Installer;

    private String Uninstaller;

    private String KeyFile;
    //插件相对目录（插件组件）
    private String PluginDir;
    //可执行程序相对路径(相对于安装目录)
    private String ExecPath;
    //插件或组件类型的宿主程序UUID(仅用于插件组件)
    private String HostAppId;

    public String getSoftId() {
        return softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getMulti() {
        return Multi;
    }

    public void setMulti(int multi) {
        Multi = multi;
    }

    public String getInstaller() {
        return Installer;
    }

    public void setInstaller(String installer) {
        Installer = installer;
    }

    public String getUninstaller() {
        return Uninstaller;
    }

    public void setUninstaller(String uninstaller) {
        Uninstaller = uninstaller;
    }

    public String getKeyFile() {
        return KeyFile;
    }

    public void setKeyFile(String keyFile) {
        KeyFile = keyFile;
    }

    public String getPluginDir() {
        return PluginDir;
    }

    public void setPluginDir(String pluginDir) {
        PluginDir = pluginDir;
    }

    public String getExecPath() {
        return ExecPath;
    }

    public void setExecPath(String execPath) {
        ExecPath = execPath;
    }

    public String getHostAppId() {
        return HostAppId;
    }

    public void setHostAppId(String hostAppId) {
        HostAppId = hostAppId;
    }

    @Override
    public String toString() {
        return "SoftInstallInfo{" +
                "softId='" + softId + '\'' +
                ", Type=" + Type +
                ", Multi=" + Multi +
                ", Installer='" + Installer + '\'' +
                ", Uninstaller='" + Uninstaller + '\'' +
                ", KeyFile='" + KeyFile + '\'' +
                ", PluginDir='" + PluginDir + '\'' +
                ", ExecPath='" + ExecPath + '\'' +
                ", HostAppId='" + HostAppId + '\'' +
                '}';
    }
}
