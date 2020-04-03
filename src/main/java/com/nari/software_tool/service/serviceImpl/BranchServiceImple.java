package com.nari.software_tool.service.serviceImpl;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.nari.software_tool.service.BranchService;
import com.nari.software_tool.dao.SoftwareBranchMapper;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.Page;

import util.aes.PaginationUtil;
import util.aes.StringUtils;

@Service
@Transactional
public class BranchServiceImple implements BranchService{
    // 注入软件分支Mapper
    @Autowired
    private SoftwareBranchMapper branchMapper;

    @Override
    public List<Map<String,Object>> queryAllBranchs(Map<String, Object> paramMap)
    {
		String SoftwareId = (String) paramMap.get("SoftwareId");
        List<Map<String, Object>> allBranchs = branchMapper.queryBranchListBySoftwareId(SoftwareId);
        return allBranchs;
    }

    @Override
    public DataTableModel queryBranchsList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        String sEcho = dataTableMap.get("sEcho");
		String SoftwareId = dataTableMap.get("SoftId");
		String branch_name = dataTableMap.get("branch_name");

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("start", start);
        paramMap.put("length", length);
		paramMap.put("SoftId", SoftwareId);
		paramMap.put("branch_name", branch_name);

        List<Map<String, Object>> resList = branchMapper.queryBranchList(paramMap);
        Integer count = branchMapper.queryBranchCount(paramMap);

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }

    @Override
    public Map<String, Object> getBranchById(String branchId) {
        return branchMapper.getBranchById(branchId);
    }

    @Override
    public void saveBranch(Map<String, Object> paramMap) {
        String branchId = (String) paramMap.get("branchId");
        if (StringUtils.isEmpty(branchId)) {
            paramMap.put("branchId", StringUtils.getUUId());
            branchMapper.addBranch(paramMap);
        } else {
            branchMapper.updateBranch(paramMap);
        }
    }

    @Override
    public boolean deleteBranch(String branchId) {
        branchMapper.deleteBranchByBranchId(branchId);
        return true;
    }
	
	@Override
    public boolean queryBranchNameIsRepeat(Map<String, Object> paramMap) {
        Integer count = branchMapper.queryBranchNameIsRepeat(paramMap);
        if (count == 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<Map<String, Object>> queryBranchList(String softId) {
        return branchMapper.queryBranchListBySoftwareId(softId);
    }
}

