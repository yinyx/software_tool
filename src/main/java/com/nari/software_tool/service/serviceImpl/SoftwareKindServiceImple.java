package com.nari.software_tool.service.serviceImpl;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.nari.software_tool.service.SoftwareKindService;
import com.nari.software_tool.dao.SoftwareKindMapper;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.Page;

import util.aes.PaginationUtil;
import util.aes.StringUtils;

@Service
@Transactional(rollbackFor = { Exception.class })
public class SoftwareKindServiceImple implements SoftwareKindService{
    // 注入软件类别Mapper
    @Autowired
    private SoftwareKindMapper softwareKindMapper;

    @Override
    public List<Map<String,Object>> queryAllKinds()
    {
        List<Map<String, Object>> allKinds = softwareKindMapper.queryAllKinds();
        return allKinds;
    }

    @Override
    public DataTableModel queryKindsList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        String sEcho = dataTableMap.get("sEcho");
        String KindName = dataTableMap.get("KindName");

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("start", start);
        paramMap.put("length", length);
        paramMap.put("KindName", KindName);

        List<Map<String, Object>> resList = softwareKindMapper.queryKindList(paramMap);
        Integer count = softwareKindMapper.queryKindsCount(paramMap);

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }

    @Override
    public Map<String, Object> getKindById(String kindId) {
        return softwareKindMapper.getKindById(kindId);
    }

    @Override
    public void saveKind(Map<String, Object> paramMap) {
        String kindId = (String) paramMap.get("kindId");
        if (StringUtils.isEmpty(kindId)) {
            paramMap.put("kindId", StringUtils.getUUId());
            softwareKindMapper.addKind(paramMap);
        } else {
            softwareKindMapper.updateKind(paramMap);
        }
    }

    @Override
    public boolean deleteKind(String kindId) {
        softwareKindMapper.deleteKind(kindId);
        return true;
    }
	
	@Override
    public boolean queryKindNameIsRepeat(String KindName) {
        Integer count = softwareKindMapper.queryKindNameIsRepeat(KindName);
        if (count == 0) {
            return true;
        } else {
            return false;
        }

    }
}

