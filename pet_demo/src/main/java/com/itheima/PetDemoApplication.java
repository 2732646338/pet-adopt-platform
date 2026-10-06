package com.itheima;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itheima.Mapper") // 重点！对应你的mapper包名，大小写要一致
public class PetDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetDemoApplication.class, args);
    }
}
