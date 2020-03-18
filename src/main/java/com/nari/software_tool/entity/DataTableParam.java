package com.nari.software_tool.entity;

import java.io.Serializable;

/**
 * 
 * 简介：datatable接收参数的实体类<BR/>
 *
 * 描述：接收前台表格datatable发送的参数实体<BR/>
 
 */
public class DataTableParam implements Serializable 
{
	private static final long serialVersionUID = 6003791544948183660L;
	private String name;
	private String value;
	
	public DataTableParam(String name, String value) 
	{
		super();
		this.name = name;
		this.value = value;
	}

	public DataTableParam() 
	{
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
