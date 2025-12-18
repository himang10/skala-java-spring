package com.skala.springbootaopsample;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.skala.springbootaopsample.config.MyInfoProperties;
import com.skala.springbootaopsample.controller.AOPController;
import com.skala.springbootaopsample.service.AOPService;

@SpringBootApplication
public class SpringbootAopApplication implements CommandLineRunner {


	private final AOPService aopService;
	private final AOPController aopController;

	SpringbootAopApplication( 	AOPService aopService, AOPController aopController) {
		this.aopController = aopController;
		this.aopService = aopService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAopApplication.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
        aopService.doAction();
		//aopController.hello();
    }

}
