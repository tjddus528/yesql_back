package com.appletantam.yesql_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/security.properties")
public class YesqlBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(YesqlBackApplication.class, args);
	}

}
