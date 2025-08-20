package com.aegis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.aegis.**.mapper")
@SpringBootApplication
public class AegisApplication {

    public static void main(String[] args) {
        SpringApplication.run(AegisApplication.class, args);
    }

}
