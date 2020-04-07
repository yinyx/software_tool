package com.nari.software_tool.entity.Communicate;

/**
 * @author yinyx
 * @version 1.0 2020/4/7
 */
public class UserPojo {

    private int Resp;

    private int Rslt;

    private int Type;

    public int getResp() {
        return Resp;
    }

    public void setResp(int resp) {
        Resp = resp;
    }

    public int getRslt() {
        return Rslt;
    }

    public void setRslt(int rslt) {
        Rslt = rslt;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "UserPojo{" +
                "Resp=" + Resp +
                ", Rslt=" + Rslt +
                ", Type=" + Type +
                '}';
    }
}
