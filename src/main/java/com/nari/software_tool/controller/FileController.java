package com.nari.software_tool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yinyx
 * @version 1.0 2020/3/16
 */
@Controller
@RequestMapping
public class FileController {

    @GetMapping(value="/index")
    public String toLogin(){
        return "index";
    }

}
