package com.nari.software_tool.service;

import java.util.Map;
import java.util.List;

import com.nari.software_tool.entity.DataTableModel;

public interface BranchService {
    List<Map<String,Object>> queryAllBranchs(Map<String, Object> paramMap);

    DataTableModel queryBranchsList(Map<String, String> dataTableMap);

    Map<String, Object> getBranchById(String BranchId);

    void saveBranch(Map<String, Object> paramMap);

    boolean deleteBranch(String BranchId);
	
	boolean queryBranchNameIsRepeat(String BranchName);
}