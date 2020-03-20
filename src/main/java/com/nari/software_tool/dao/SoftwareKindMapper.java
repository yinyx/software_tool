package com.nari.software_tool.dao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.nari.software_tool.entity.SoftwareKind;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SoftwareKindMapper {

    List<Map<String, Object>> queryAllKinds();

    List<Map<String, Object>> queryKindList(Map<String, Object> paramMap);

    int queryKindsCount();

    Map<String, Object> getKindById(String KindId);

    void addKind(Map<String, Object> paramMap);

    void updateKind(Map<String, Object> paramMap);

    void deleteKind(String userId);
	
	int queryKindNameIsRepeat(String name);

}
