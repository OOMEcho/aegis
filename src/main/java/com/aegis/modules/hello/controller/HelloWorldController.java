package com.aegis.modules.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/20 15:52
 * @Description: 测试接口
 */
@RestController
public class HelloWorldController {

    /**
     * 测试接口
     *
     * @return 返回 "Hello, World!"
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
