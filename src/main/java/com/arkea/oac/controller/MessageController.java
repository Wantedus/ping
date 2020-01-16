package com.arkea.oac.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		
		 Message m =messageDAOImpl.getMessage(id);
		
		 if(m.getId()=="") {
			 return null;
		 }
		 else{
			 return m; 
		 }
		
	}
	
	@GetMapping(value = "/message")
	public ArrayList<Message> getAllMessage() {
		
		return messageDAOImpl.getAllMessage();
	}

	
	@PostMapping(value = "/message")
	public ResponseEntity<Integer> postMessage(@RequestBody Message m) {
		
		int id =messageDAOImpl.createMessage(m);
		
		
		return new ResponseEntity<Integer>(id, HttpStatus.CREATED);
		
	}

	@PutMapping(value = "/message/{id}")
	public Message putMessage(@RequestBody Message m,@PathVariable int id) {
		
	
		/*
		Message updatedMessage =messageDAOImpl.getMessage(id);
		
		updatedMessage.setCanals(m.getCanals());
		updatedMessage.setEnd(m.getEnd());
		updatedMessage.setEntity(m.getEntity());
		updatedMessage.setKeywords(m.getKeywords());
		updatedMessage.setPriority(m.getPriority());
		updatedMessage.setStart(m.getStart());
		updatedMessage.setT(m.getT());
		updatedMessage.setText(m.getText());
		updatedMessage.setType(m.getType());
		updatedMessage.setVision360(m.isVision360());
		updatedMessage.setWording(m.getWording());
		*/
		;
		messageDAOImpl.updateMessage(id, m);
		
		return messageDAOImpl.getMessage(id);
		
	}
	
	@DeleteMapping(value = "/message/{id}")
	public ResponseEntity<Integer> deleteMessage(@PathVariable int id) {
		messageDAOImpl.deleteMessage(id);
		return new ResponseEntity<Integer>(id, HttpStatus.ACCEPTED);
	}
	
	
	
}
