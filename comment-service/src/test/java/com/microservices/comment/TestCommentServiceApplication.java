package com.microservices.comment;

import org.springframework.boot.SpringApplication;

public class TestCommentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(CommentServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
