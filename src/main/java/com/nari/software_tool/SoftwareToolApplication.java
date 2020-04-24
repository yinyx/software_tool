package com.nari.software_tool;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("com.nari.software_tool.dao")
@SpringBootApplication
public class SoftwareToolApplication extends SpringBootServletInitializer {

    @Override

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(SoftwareToolApplication.class);

    }

    public static void main(String[] args) {
        SpringApplication.run(SoftwareToolApplication.class, args);
    }

}
