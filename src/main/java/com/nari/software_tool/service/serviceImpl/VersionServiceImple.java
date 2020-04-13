package com.nari.software_tool.service.serviceImpl;

import com.nari.software_tool.dao.SoftHistoryInfoMapper;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.SoftHistoryInfo;
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
    public List<Map<String, Object>> queryVersionByBranchId(String branchId) {
        return softHistoryInfoMapper.queryVersionByBranchId(branchId);
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
        String version_name = dataTableMap.get("version_name");

        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("Kind",Kind);
        paramMap.put("softId",softId);
        paramMap.put("branchId",branchId);
        paramMap.put("version_name",version_name);

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
    public Map<String, Object> getVersionPkgCfgById(String historyId) {
        Map<String, Object> obj = softHistoryInfoMapper.getVersionPkgCfgById(historyId);
        return obj;
    }

	@Override
	public void setVersionInstllconfigById(Map<String, Object> paramMap){
		softHistoryInfoMapper.setVersionInstllconfig(paramMap);
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

    @Override
    public int updateVersion(Map<String, Object> paramMap) {
        return softHistoryInfoMapper.updateVersion(paramMap);
    }

    @Override
    public int addVersion(Map<String, Object> paramMap) {
        return softHistoryInfoMapper.addVersion(paramMap);
    }

    @Override
    public List<SoftHistoryInfo> queryHisList(String softId) {
        return softHistoryInfoMapper.queryHisList(softId);
    }

    @Override
    public SoftHistoryInfo queryPktInfo(String historyId, String MD5) {
        return softHistoryInfoMapper.queryPktInfo(historyId,MD5);
    }

    @Override
    public boolean queryVersionNameIsRepeat(Map<String, Object> paramMap) {
        Integer count = softHistoryInfoMapper.queryVersionNameIsRepeat(paramMap);
        if (count == 0) {
            return true;
        } else {
            return false;
        }

    }

}

