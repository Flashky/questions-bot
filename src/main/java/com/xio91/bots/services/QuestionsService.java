package com.xio91.bots.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.xio91.bots.rest.questions.model.Question;

import reactor.core.publisher.Mono;

@Service
public class QuestionsService {

	@Autowired
	private WebClient webClient;
	
	public void createQuestion(Question question) {

		String result = this.webClient.post()
				.uri("http://localhost:8081/xio91/v0/questions")
				.body(Mono.just(question), Question.class)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		
		System.out.println(result);
	}
}
