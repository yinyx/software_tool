package com.nari.software_tool.entity.Communicate;

/**
 * @author yinyx
 * @version 1.0 2020/3/25
 */
public class AppPktPojo {

    private String Ver;

    private String Date;

    private String New;

    private String Path;

    private int Size;

    private String MD5;

    public String getVer() {
        return Ver;
    }

    public void setVer(String ver) {
        Ver = ver;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getNew() {
        return New;
    }

    public void setNew(String aNew) {
        New = aNew;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    @Override
    public String toString() {
        return "AppPktPojo{" +
                "Ver='" + Ver + '\'' +
                ", Date='" + Date + '\'' +
                ", New='" + New + '\'' +
                ", Path='" + Path + '\'' +
                ", Size=" + Size +
                ", MD5='" + MD5 + '\'' +
                '}';
    }
}
