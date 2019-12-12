package com.arkea.oac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.arkea.oac.dao.MessageDAOImpl;
import com.arkea.oac.model.Message;

@RestController
public class MessageController {

	@Autowired
	private MessageDAOImpl messageDAOImpl;
	
	@GetMapping(value = "/message/{id}")
	public Message getMessage(@PathVariable int id) {
		
		return messageDAOImpl.getMessage(id);
		
	}
}