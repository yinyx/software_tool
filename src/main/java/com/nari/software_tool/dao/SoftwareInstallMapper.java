package com.nari.software_tool.dao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.software_tool.entity.SoftwareKind;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SoftwareInstallMapper {
    Map<String, Object> getInstallById(String SoftwareId);

    void addInstall(Map<String, Object> paramMap);

    void updateInstall(Map<String, Object> paramMap);

    void deleteInstall(String SoftwareId);
}
