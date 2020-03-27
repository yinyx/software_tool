package com.nari.software_tool.service;

import java.util.Map;
import java.util.List;

import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.ScreenShotInfo;

public interface SoftwareService {
    DataTableModel querySoftwaresList(Map<String, String> dataTableMap);
	
	int querySoftwareCountByKind(String kindId);
	
	void saveSoftware(Map<String, Object> paramMap);

    Map<String, Object> getSoftwareById(String softwareId);
	
	boolean deleteSoftware(String SoftwareId);
	
	void updateSoftware(Map<String, Object> paramMap);
	
	void updateSoftwareIcon(Map<String, Object> paramMap);
	
	void deleteIcon(String softwareId);
	
	void deleteDir(String rootPath, String softwareId);

	void deleteFile(String path);
	
	Map<String, Object> getInstallConfigBySoftwareId(String softwareId);
	
	void updateInstallAttribute(Map<String, Object> paramMap);

	boolean queryScreenShot(ScreenShotInfo screenShotInfo);

	boolean batchInsertScreenShots(List<ScreenShotInfo> screenShotInfoLists);
}