package com.nari.software_tool.service.serviceImpl;

import com.nari.software_tool.dao.SoftHistoryInfoMapper;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VersionServiceImple implements VersionService {
    // 注入软件分支Mapper
    @Autowired
    private SoftHistoryInfoMapper softHistoryInfoMapper;

    @Override
    public List<Map<String, Object>> queryAllBranchs(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public DataTableModel queryHistoryList(Map<String, String> dataTableMap)
    {
        DataTableModel<Map<String, Object>> dataTableModel = new DataTableModel<>();
        Map<String,Object> paramMap = new HashMap<>();
        String sEcho = dataTableMap.get("sEcho");
        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));
        String Kind = dataTableMap.get("Kind");
        String softId = dataTableMap.get("softId");
        String branchId = dataTableMap.get("branchId");

        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("Kind",Kind);
        paramMap.put("softId",softId);
        paramMap.put("branchId",branchId);

        List<Map<String, Object>> resList = softHistoryInfoMapper.queryHistoryList(paramMap);
        int count = softHistoryInfoMapper.queryHistoryCount(paramMap);
        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);
        return dataTableModel;
    }

    @Override
    public Map<String, Object> getBranchById(String BranchId) {
        return null;
    }

    @Override
    public Map<String, Object> getHistoryById(String historyId) {
        Map<String, Object> obj = softHistoryInfoMapper.queryHistoryById(historyId);
        return obj;
    }

    @Override
    public void saveBranch(Map<String, Object> paramMap) {

    }

    @Override
    public boolean deleteBranch(String BranchId) {
        return false;
    }

    @Override
    public boolean queryBranchNameIsRepeat(String BranchName) {
        return false;
    }


}

