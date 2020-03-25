package com.nari.software_tool.entity.Communicate;


import java.util.List;

/**
 * 客户端通信基础类
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public class CommunicatePojo {
    //响应数据类型
    private int  resp;
    //列表元素数量
    private int total;
    //对象数组
    private List<Object> lst;

    public int getResp() {
        return resp;
    }

    public void setResp(int resp) {
        this.resp = resp;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Object> getLst() {
        return lst;
    }

    public void setLst(List<Object> lst) {
        this.lst = lst;
    }

    @Override
    public String toString() {
        return "CommunicatePojo{" +
                "resp=" + resp +
                ", total=" + total +
                ", lst=" + lst +
                '}';
    }
}
