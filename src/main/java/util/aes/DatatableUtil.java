package util.aes;

import java.util.HashMap;

import com.nari.software_tool.entity.DataTableParam;

/**
 * 
 * 简介：处理DataTable的工具类<BR/>
 *
 * 描述：主要用于处理DataTable的实体类的工具类<BR/>
 *
 * @author  jixingwang
 * 
 * @see [com.bshinfo.web.model.dataTableUtil.DataTableParam]
 *
 * @since JDK1.7
 *
 * @version  V1.00
 *
 * @date 2017年3月20日上午10:54:36
 */
public class DatatableUtil 
{
	/**
	 * 
	 * 功能描述：将DataTableParam数组转化成HashMap
	 *
	 *
	 * @param [com.bshinfo.web.model.dataTableUtil.DataTableParam[]] dataTableParams <datatable数组>
	 *
	 * @return [java.util.HashMap<String, String>] <转化后Map集合>
	 * 
	 * @author jixingwang
	 *
	 * @date 2017年3月20日上午10:55:13
	 */
	public static HashMap<String, String> convertToMap(DataTableParam[] dataTableParams) 
	{
		HashMap<String, String> paramsMap = new HashMap<>();
		for (DataTableParam dataTableParam : dataTableParams) 
		{
			paramsMap.put(dataTableParam.getName(), dataTableParam.getValue());
		}

		return paramsMap;
	}
}
