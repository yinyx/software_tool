package com.nari.software_tool.dao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SoftwareBranchMapper {
	List<Map<String, Object>> queryBranchListBySoftwareId(String SoftwareId);
	
	List<Map<String, Object>> queryBranchList(Map<String, Object> paramMap);
	
	int queryBranchCount(Map<String, Object> paramMap);
	
    Map<String, Object> getBranchById(String BranchId);

    void addBranch(Map<String, Object> paramMap);

    void updateBranch(Map<String, Object> paramMap);

    void deleteBranchByBranchId(String BranchId);
	
	void deleteBranchBySoftwareId(String SoftwareId);
	
	int queryBranchNameIsRepeat(Map<String, Object> paramMap);
}
