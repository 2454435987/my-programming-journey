package com.tea;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 茶文化主题App后端启动类
 */
@SpringBootApplication
@MapperScan("com.tea.mapper")
public class TeaCultureApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaCultureApplication.class, args);
        System.out.println("=== 茶文化主题App后端启动成功 http://127.0.0.1:8080 ===");
    }
}
