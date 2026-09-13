package com.tcm.api.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户端应用启动类
 *
 * @author Ti
 * @since 2026-02-03
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.tcm")
@MapperScan("com.tcm.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        System.out.println("============================================");
        System.out.println("   中医养生平台 - 用户端API 启动成功！");
        System.out.println("   端口: 8081");
        System.out.println("   API文档: http://localhost:8081/doc.html");
        System.out.println("============================================");
    }
}
