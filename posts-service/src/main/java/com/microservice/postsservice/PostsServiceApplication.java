package com.microservice.postsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class PostsServiceApplication {

	@GetMapping("/post-service/status")
	public String status(){
		return "This is post-service";
	}

	public static void main(String[] args) {
		SpringApplication.run(PostsServiceApplication.class, args);
	}

}
