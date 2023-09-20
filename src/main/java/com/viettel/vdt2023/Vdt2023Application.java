package com.viettel.vdt2023;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.security.DenyAll;

@SpringBootApplication
@EnableSwagger2
public class Vdt2023Application {

	public static void main(String[] args) {
		SpringApplication.run(Vdt2023Application.class, args);
	}

}
