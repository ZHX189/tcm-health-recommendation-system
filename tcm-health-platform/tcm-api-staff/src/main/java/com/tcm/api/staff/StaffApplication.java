package com.tcm.api.staff;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

/**
 * 员工端应用启动类
 * 
 * 员工端不需要AI聊天功能，排除相关服务和自动配置
 *
 * @author Ti
 * @since 2026-02-03
 */
@SpringBootApplication(exclude = {
        org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration.class,
        org.springframework.ai.autoconfigure.chat.client.ChatClientAutoConfiguration.class
})
@ComponentScan(
        basePackages = "com.tcm",
        excludeFilters = @Filter(
                type = FilterType.REGEX,
                pattern = "com\\.tcm\\.service\\.ai\\..*"
        )
)
@MapperScan("com.tcm.mapper")
public class StaffApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffApplication.class, args);
        System.out.println("============================================");
        System.out.println("   中医养生平台 - 员工端API 启动成功！");
        System.out.println("   端口: 8082");
        System.out.println("   API文档: http://localhost:8082/doc.html");
        System.out.println("============================================");
    }
}
