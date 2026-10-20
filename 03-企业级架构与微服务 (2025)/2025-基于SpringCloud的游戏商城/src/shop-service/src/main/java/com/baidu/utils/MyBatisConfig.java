package com.baidu.utils;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {
    @Bean
    public MybatisPlusInterceptor getInfo(){
        MybatisPlusInterceptor interceptor=new MybatisPlusInterceptor();
//创建PaginationInnerInterceptor
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();

        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }

}
