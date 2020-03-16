package com.nari.software_tool;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.nari.software_tool.dao")
@SpringBootApplication
public class SoftwareToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwareToolApplication.class, args);
    }

}
