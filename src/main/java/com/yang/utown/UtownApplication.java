package com.yang.utown;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yang.utown.mapper")
public class UtownApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtownApplication.class, args);
	}

}
