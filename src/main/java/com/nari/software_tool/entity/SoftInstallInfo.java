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

    @Override
    public String toString() {
        return "SoftInstallInfo{" +
                "softId='" + softId + '\'' +
                ", Type=" + Type +
                ", Multi=" + Multi +
                ", Installer='" + Installer + '\'' +
                ", Uninstaller='" + Uninstaller + '\'' +
                ", KeyFile='" + KeyFile + '\'' +
                '}';
    }
}
