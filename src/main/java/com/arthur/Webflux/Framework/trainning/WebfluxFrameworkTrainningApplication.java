package com.arthur.webflux.framework.trainning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class WebfluxFrameworkTrainningApplication {

	static{
		BlockHound.install();
	}

	public static void main(String[] args) {

		SpringApplication.run(WebfluxFrameworkTrainningApplication.class, args);
	}

}
