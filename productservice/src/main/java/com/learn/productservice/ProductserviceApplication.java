package com.learn.productservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
public class ProductserviceApplication {

	private static final Logger logger = LoggerFactory.getLogger(ProductserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProductserviceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(Environment environment) {
		return args -> {
			logger.info("====== Environment and Configuration ======");
			logger.info("Active profiles: {}", (Object) environment.getActiveProfiles());
			logger.info("RabbitMQ Host: {}", environment.getProperty("spring.rabbitmq.host"));
			logger.info("RabbitMQ Port: {}", environment.getProperty("spring.rabbitmq.port"));
			logger.info("RabbitMQ Username: {}", environment.getProperty("spring.rabbitmq.username"));
			logger.info("Service Name: {}", environment.getProperty("spring.application.name"));
			logger.info("graphql.path: {}", environment.getProperty("graphql.path"));
			logger.info("===========================================");
		};
	}
}
