package com.nari.software_tool.service.serviceImpl;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.nari.software_tool.service.BranchService;
import com.nari.software_tool.dao.SoftwareBranchMapper;
import com.nari.software_tool.dao.SoftHistoryInfoMapper;
import com.nari.software_tool.dao.SoftwareInfoMapper;
import com.nari.software_tool.dao.SoftwareKindMapper;
import com.nari.software_tool.dao.SoftPluginMapper;
import com.nari.software_tool.entity.DataTableModel;

import util.aes.StringUtils;
import util.aes.TestProperties;

@Service
@Transactional
public class BranchServiceImple implements BranchService{
    @Autowired
    private SoftPluginMapper softPluginMapper;

    @Autowired
    private SoftwareBranchMapper branchMapper;

    @Autowired
    private SoftHistoryInfoMapper softHistoryInfoMapper;

    @Autowired
    private SoftwareInfoMapper softwareInfoMapper;

    @Autowired
    private SoftwareKindMapper softwareKindMapper;

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
    public void deleteDir(String rootPath, String branchId)
    {
        //查分支名称
        Map<String, Object> branchObj = branchMapper.getBranchById(branchId);
        if (branchObj == null)
        {
            return;
        }
        String softIdId = (String) branchObj.get("soft_id");
        String branchName = (String) branchObj.get("branch");
        if ((StringUtils.isEmpty(softIdId))||(StringUtils.isEmpty(branchName))){
            return;
        }

        //查软件名称
        Map<String, Object> softObj = softwareInfoMapper.querySoftwareById(softIdId);
        if (softObj == null)
        {
            return;
        }
        String kindId = (String) softObj.get("kind");
        String en_name = (String) softObj.get("name_en");
        if ((StringUtils.isEmpty(kindId))||(StringUtils.isEmpty(en_name))){
            return;
        }

        //查软件类别名称
        Map<String,Object> map = softwareKindMapper.getKindById(kindId);
        if (map == null)
        {
            return;
        }
        String softPath = rootPath+"/"+map.get("name_en")+"/"+ en_name+"/"+ branchName;
        File softDir = new File(softPath);
        String softDirAbsolutePath = softDir.getAbsolutePath();

        TestProperties.deleteFile(softDirAbsolutePath);
    }

    @Override
    public boolean deleteBranch(String branchId) {
        //删除分支相关的插件记录
        softPluginMapper.deletePluginsByBranchId(branchId);
        //删除分支下的所有版本
        softHistoryInfoMapper.deleteVersionsByBranchId(branchId);
        //删除分支
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

