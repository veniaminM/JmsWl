package com;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringJmsActiveMqPubSubApp implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringJmsActiveMqPubSubApp.class, args);
	}

	@Override
	public void run(String... args) {
	}

}
