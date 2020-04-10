package com.nari.software_tool.service;

import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.SoftHistoryInfo;

import java.util.List;
import java.util.Map;

public interface VersionService {

    List<Map<String,Object>> queryAllBranchs(Map<String, Object> paramMap);

	List<Map<String,Object>> queryVersionByHistoryId(String historyId);

    DataTableModel queryHistoryList(Map<String, String> dataTableMap);

    Map<String, Object> getBranchById(String BranchId);

    Map<String, Object> getHistoryById(String historyId);
	
	Map<String, Object> getVersionPkgCfgById(String historyId);

    void saveBranch(Map<String, Object> paramMap);
	
	void setVersionInstllconfigById(Map<String, Object> paramMap);

    boolean deleteBranch(String BranchId);
	
	boolean queryBranchNameIsRepeat(String BranchName);

	int updateVersion(Map<String,Object> paramMap);

	int addVersion(Map<String,Object> paramMap);

	List<SoftHistoryInfo> queryHisList(String softId);

	SoftHistoryInfo queryPktInfo(String historyId,String MD5);
}