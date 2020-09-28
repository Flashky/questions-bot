package com.xio91.bots.rest.questions.model;

import lombok.Data;

@Data
public class Question {

	public Question(String text, String author) {
		
		this.text = text;
		this.author = new Author();
		this.author.setName(author);
	}
	
	private String id;
	private String text;
	private Author author;
	private Answer answer;
	
}
