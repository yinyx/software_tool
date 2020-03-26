package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

@Component
public class SoftwareInstall {
    private String soft_id;
    private int type;
	private int Is_multi;
	private String installer;
	private String uninstaller;
	private String KeyFile;

    //soft_id
    public String getSoft_id() {
        return soft_id;
    }
    public void setSoft_id(String soft_id) {
        this.soft_id = soft_id;
    }
	
	//type
	public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
	
	//Is_multi
	public int getIs_multi() {
        return Is_multi;
    }
    public void setIs_multi(int Is_multi) {
        this.Is_multi = Is_multi;
    }
	
	//installer
	public String getInstaller() {
        return installer;
    }
    public void setInstaller(String installer) {
        this.installer = installer;
    }
	
	//uninstaller
	public String getUninstaller() {
        return uninstaller;
    }
    public void setUninstaller(String uninstaller) {
        this.uninstaller = uninstaller;
    }

    //KeyFile
    public String getKeyFile() {
        return KeyFile;
    }
    public void setKeyFile(String KeyFile) {
        this.KeyFile = KeyFile;
    }

    @Override
    public String toString() {
        return "SoftwareInstall [soft_id=" + soft_id + ", installer=" + installer + "]";
    }
    
}


