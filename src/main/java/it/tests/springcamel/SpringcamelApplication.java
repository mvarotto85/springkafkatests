package it.tests.springcamel;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringcamelApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SpringcamelApplication.class, args);
		Thread.sleep(100000);

	}
}
