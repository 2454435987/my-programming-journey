package com.water.delivery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.water.delivery.mapper")
public class WaterDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterDeliveryApplication.class, args);
        System.out.println("=========================================================");
        System.out.println(">>> 桶装水配送管理系统后端服务启动成功! <<<");
        System.out.println(">>> 服务端口: 8088");
        System.out.println(">>> API 接口文档地址: http://localhost:8088/doc.html");
        System.out.println("=========================================================");
    }
}
