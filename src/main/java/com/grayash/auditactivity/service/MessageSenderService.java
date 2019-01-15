package com.grayash.auditactivity.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grayash.auditactivity.config.RabbitConfig;
import com.grayash.auditactivity.model.ActivityData;


@Service
public class MessageSenderService {
	
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	 
	public void send(ActivityData data) {
	    try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        String orderJson = objectMapper.writeValueAsString(data);
	        Message message = MessageBuilder
	                            .withBody(orderJson.getBytes())
	                            .setContentType(MessageProperties.CONTENT_TYPE_JSON)
	                            .build();
	        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE, message);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	    }
	}


}
