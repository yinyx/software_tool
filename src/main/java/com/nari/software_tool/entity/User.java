package com.nari.software_tool.entity;

import org.springframework.stereotype.Component;

@Component
public class User {
    private String id;
    private String user_name;
    private String password;
    
    public String getUsername() {
        return user_name;
    }
    public void setUsername(String user_name) {
        this.user_name = user_name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + user_name + ", password="
                + password + "]";
    }
    
}


