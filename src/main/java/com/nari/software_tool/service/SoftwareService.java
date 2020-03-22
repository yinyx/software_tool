package com.nari.software_tool.service;

import java.util.Map;
import java.util.List;

import com.nari.software_tool.entity.DataTableModel;

public interface SoftwareService {
    DataTableModel querySoftwaresList(Map<String, String> dataTableMap);
}