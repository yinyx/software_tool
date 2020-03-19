package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

@Component
public class SoftwareKind {
    private String id;
    private String kind_name;

    public String getKindname() {
        return kind_name;
    }
    public void setKindname(String kind_name) {
        this.kind_name = kind_name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SoftwareKind [id=" + id + ", kindname=" + kind_name + "]";
    }
    
}


