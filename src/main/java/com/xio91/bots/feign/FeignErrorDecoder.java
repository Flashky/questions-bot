package com.xio91.bots.feign;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException.Unauthorized;
import com.xio91.bots.feign.dto.ErrorResponse;
import com.xio91.bots.service.TokenFeignService;

import feign.Response;
import feign.codec.ErrorDecoder;

// https://www.appsdeveloperblog.com/feign-error-handling-with-errordecoder/

//@Component
public class FeignErrorDecoder implements ErrorDecoder {

	private static final Logger LOG = LoggerFactory.getLogger(TokenFeignService.class);
	
    @Override
    public Exception decode(String methodKey, Response response) {
    	
    	HttpStatus httpStatus = HttpStatus.resolve(response.status());
    	
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		ErrorResponse errorMessage = mapper.readValue(response.body().asInputStream(), ErrorResponse.class);
    		LOG.error("[" + httpStatus.value() + "] " +response.reason() + ": " +errorMessage.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	    	switch(httpStatus) {
	    		case BAD_REQUEST:
	    			// TODO prepare exception
	    			break;
	    		case UNAUTHORIZED:
	    			break;
	    		case FORBIDDEN:
	    			// TODO prepare exception
	    		break;
	    		case NOT_FOUND:
	    			// TODO prepare exception
	    		break;
	    		default:
	    			return new Exception(response.reason());
	    	}
	    	

    	
    	return new Exception(response.reason());
    }
    

}
