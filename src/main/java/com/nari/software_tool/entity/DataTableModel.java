package com.nari.software_tool.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 简介：datatable返回数据的实体类<BR/>
 *
 * 描述：封装后台查询出的数据返回给前端datatable表格解析的实体<BR/>
 *
 */
public class DataTableModel<T> implements Serializable
{
	private static final long serialVersionUID = -4134300147853888287L;
	//页面传递过来的操作次数,必须设置的值
	private int sEcho = 0;
	//总数量
    private int iTotalRecords = 0;
    //table展示的数量
    private int iTotalDisplayRecords = 0;
    //页面数据
    private List<T> aaData = new ArrayList<T>();
    private double sum=0;
    
	public DataTableModel() 
	{
		super();
	}
	
	public DataTableModel(int sEcho, int iTotalRecords,
			int iTotalDisplayRecords, List<T> aaData) 
	{
		super();
		this.sEcho = sEcho;
		this.iTotalRecords = iTotalRecords;
		this.iTotalDisplayRecords = iTotalDisplayRecords;
		this.aaData = aaData;
	}
	
	public int getsEcho() {
		return sEcho;
	}
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public List<T> getAaData() {
		return aaData;
	}

	public void setAaData(List<T> aaData) {
		this.aaData = aaData;
	}

	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}

	   @Override
	    public String toString() {
	        return "DataTableModel [sEcho=" + sEcho + ", iTotalRecords=" + iTotalRecords + ", iTotalDisplayRecords="
	                + iTotalDisplayRecords + "]"+ "  "+aaData;
	    }
}

