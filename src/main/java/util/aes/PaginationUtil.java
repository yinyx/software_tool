package util.aes;

import java.util.Map;

import com.nari.software_tool.entity.Page;



/**
 * 分页实工具类<br/>
 * 
 * 功能描述: MyBatis 分页操作工具类 支持MySql And  Oracle<BR/>
 * 
 * 〈 Description: <br/>	 
 * 	
 *	
 * 〉
 *
 * @author 	zhangChao
 * 
 * @see 	[Page<T>]
 * 
 * @since 	[JDK 1.7] 
 * 
 * @version [1.0]
 * 
 * @Date    [2016-12-16 15:21:51]
 */
public class PaginationUtil
{
	/**
	 * 设置分页信息
	 * 
	 * @param params  分页查询参数
	 * 
	 * @param pageNo  当前页
	 * 
	 * @param pageSize 页大小
	 * 
	 * @return {Page<Map<String, Object>>}
	 */
	public static Page<Map<String, Object>> setPageInfoStart(Map<String, Object> params,Integer pageNo,Integer pageSize)
	{
		Page<Map<String, Object>> page = new Page<Map<String, Object>>();
		// 当前页
        int pg = pageNo == null? 1 : pageNo;
        // 当前页显示条数
        int pgs = pageSize == null ? 5 : pageSize;
        page.setPageNo(pg);
        page.setPageSize(pgs);
        page.setParams(params);
        return page;
	}
	
	
	/**
	 * 结果集中封装分页信息
	 * 
	 * @param resMap
	 * 
	 * @param page
	 * 
	 */
	public static void setPageInfoEnd(Map<String,Object> resMap,Page<Map<String, Object>> page)
	{
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("pageSize", page.getPageSize());
		resMap.put("totalCount", page.getTotalRecord());
		resMap.put("pageNo", page.getPageNo());
	}
}
