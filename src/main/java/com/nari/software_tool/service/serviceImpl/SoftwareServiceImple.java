package com.nari.software_tool.service.serviceImpl;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.nari.software_tool.service.SoftwareService;
import com.nari.software_tool.dao.SoftwareInfoMapper;
import com.nari.software_tool.entity.DataTableModel;
import com.nari.software_tool.entity.Page;

import util.aes.PaginationUtil;
import util.aes.StringUtils;

@Service
@Transactional
public class SoftwareServiceImple implements SoftwareService{
    // 注入软件类别Mapper
    @Autowired
    private SoftwareInfoMapper softwareInfoMapper;

    public DataTableModel querySoftwaresList(Map<String, String> dataTableMap)
    {
        DataTableModel dataTableModel = new DataTableModel();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        String sEcho = dataTableMap.get("sEcho");
		String Kind = dataTableMap.get("Kind");
		
		if ((Kind != null)&&(Kind.equals("0")))
		{
			Kind = null;
		}

        int start = Integer.parseInt(dataTableMap.get("iDisplayStart"));
        int length = Integer.parseInt(dataTableMap.get("iDisplayLength"));

        paramMap.put("start", start);
        paramMap.put("length", length);
		paramMap.put("Kind", Kind);

        List<Map<String, Object>> resList = softwareInfoMapper.querySoftwaresList(paramMap);
        Integer count = softwareInfoMapper.querySoftwaresCount(paramMap);

        dataTableModel.setiTotalDisplayRecords(count);
        dataTableModel.setiTotalRecords(count);
        dataTableModel.setsEcho(Integer.valueOf(sEcho));
        dataTableModel.setAaData(resList);

        return dataTableModel;
    }
}

