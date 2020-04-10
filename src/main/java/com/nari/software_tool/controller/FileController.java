package com.nari.software_tool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Controller
@RequestMapping
public class FileController {

    @GetMapping(value="/login")
    public String toLogin(){
        return "login";
    }

    @GetMapping(value="/index")
    public String toIndex(){
        return "index";
    }

    @GetMapping(value="/Info_UserInfo")
    public String toUserInfo(){
        return "Info_UserInfo";
    }

    @GetMapping(value="/Info_SoftwareInfo")
    public String toSoftwareInfo(){
        return "Info_SoftwareInfo";
    }

    @GetMapping(value="/Info_TypeInfo")
    public String toTypeInfo(){
        return "Info_TypeInfo";
    }

    @GetMapping(value="/Info_BranchInfo")
    public String toBranchInfo(){
        return "Info_BranchInfo";
    }

    @GetMapping(value="/Info_VersionInfo")
    public String toVersionInfo(){
        return "Info_VersionInfo";
    }

    @GetMapping(value="/Info_PluginInfo")
    public String toPluginInfo(){
        return "Info_PluginInfo";
    }
}
