package com.learn.salesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SalesserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesserviceApplication.class, args);
	}

}
