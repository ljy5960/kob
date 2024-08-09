package com.kob.matchingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
        //向后端发请求SpringBoot也有一个工具、如果我们需要在当前的component用到service的话
        //就把它先加个注解bean、想取得谁加一个bean注解然后获取就可以了
//        当我们需要用到某个东西的时候、就定一个它的configuration
//        加一个注解bean、返回它的实例然后就可以了
//        定义完之后、未来我们用它的时候就可以加一个Autowired就可以自动注入进来
//                加了Autowired的话
//        他就会去看一下他就会去看一下这个service这个接口是不是有唯一的加了注解bean的函数和它对应
//                如果有的话就调用一下这个函数把它的返回赋过来
    }
}
