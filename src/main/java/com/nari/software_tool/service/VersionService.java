package com.nari.software_tool.service;

import com.nari.software_tool.entity.DataTableModel;

import java.util.List;
import java.util.Map;

public interface VersionService {

    List<Map<String,Object>> queryAllBranchs(Map<String, Object> paramMap);

    DataTableModel queryHistoryList(Map<String, String> dataTableMap);

    Map<String, Object> getBranchById(String BranchId);

    Map<String, Object> getHistoryById(String historyId);

    void saveBranch(Map<String, Object> paramMap);

    boolean deleteBranch(String BranchId);
	
	boolean queryBranchNameIsRepeat(String BranchName);
}