package com.springboot.scsms;

import org.springframework.boot.SpringApplication;

import com.springboot.application.ScsmsApplication;

public class TestScsmsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ScsmsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
