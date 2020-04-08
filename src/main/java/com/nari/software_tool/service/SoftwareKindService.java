package com.nari.software_tool.service;

import java.util.Map;
import java.util.List;

import com.nari.software_tool.entity.DataTableModel;

public interface SoftwareKindService {
    List<Map<String,Object>> queryAllKinds();

    DataTableModel queryKindsList(Map<String, String> dataTableMap);

    Map<String, Object> getKindById(String KindId);

    void saveKind(Map<String, Object> paramMap);

    boolean deleteKind(String KindId);
	
	boolean queryKindNameIsRepeat(String KindName);

    void deleteIcon(String iconPath, String KindId);

    void deleteDir(String rootPath, String KindId);
}