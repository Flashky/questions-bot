package com.xio91.bots.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.form.FormEncoder;

@Configuration
public class FeignConfig {
	
	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;

	@Bean
	FormEncoder feignFormEncoder() {
	      return new FormEncoder(new SpringEncoder(this.messageConverters));
	   }
}
