package com.baidu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@ServletComponentScan("com.baidu.filter")
public class ShopWebApplication8008 {
    public static void main(String[] args) {
        SpringApplication.run(ShopWebApplication8008.class,args);
    }
}

