package com.arkea.oac.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arkea.oac.dao.MessageDAOImpl;
import com.arkea.oac.model.Message;
import com.arkea.page.MessagePageEntity;

@RestController
public class MessageController {

	@Autowired
	private MessageDAOImpl messageDAOImpl;
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/message/{id}")
	public ResponseEntity<Message> getMessage(@PathVariable int id) {
		
		
		 Message m =messageDAOImpl.getMessage(id);
			System.out.println(m.getT().toString());	
		
		 if(m.getId()=="") {
			 return  new ResponseEntity<Message>(m, HttpStatus.NO_CONTENT);
		 }
		 else{
			return new ResponseEntity<Message>(m, HttpStatus.OK);
		 }
	 

	}
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/message")
	public ArrayList<Message> getAllMessage() {
		return messageDAOImpl.getAllMessage();
	}
	
	
	/**
	 * Get all message by page
	 * @param page la page actuelle, 1 par défaut
	 * @param size les rangs qu'ils vont afficher sur la page, 10 par défaut
	 * @return la page
	 */
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/message/")
	public MessagePageEntity getAllMessageByPage(
			@RequestParam(defaultValue = "1", required=false) Integer page,
			@RequestParam(defaultValue = "10", required=false) Integer size){
		
		return messageDAOImpl.getAllMessageByPage(page, size);
	}

	@CrossOrigin(origins = "*")
	@GetMapping(value="/search/libelle")
	public List<Message> getMessageByLibelle(
			String libelle,
			@RequestParam String type,
			@RequestParam(defaultValue = "0",required=false) Integer page,
			@RequestParam(defaultValue = "5",required=false) Integer size){
		return messageDAOImpl.getMessageByLibelle(libelle, type, page, size);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(value="/search/motcle")
	public List<Message> getMessageByMotCle(
			String motcle,
			@RequestParam String type,
			@RequestParam(defaultValue = "0",required=false) Integer page,
			@RequestParam(defaultValue = "5",required=false) Integer size){
		return messageDAOImpl.getMessageByMotCle(motcle, type, page, size);
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/message")
	public ResponseEntity<Integer> postMessage(@RequestBody Message m) {
		
		//System.out.println(m.getT().toString());
		int id =messageDAOImpl.createMessage(m);
				
		return new ResponseEntity<Integer>(id, HttpStatus.CREATED);
		
	}
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/message/{id}")
	public ResponseEntity<Integer> putMessage(@RequestBody Message m,@PathVariable int id) {
		
	
		/*
		Message updatedMessa ge =messageDAOImpl.getMessage(id);
		
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
		
		//return messageDAOImpl.getMessage(id);
	
		return new ResponseEntity<Integer>(id, HttpStatus.ACCEPTED);
		
	}
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = "/message/{id}")
	public ResponseEntity<Integer> deleteMessage(@PathVariable int id) {
		messageDAOImpl.deleteMessage(id);
		return new ResponseEntity<Integer>(id, HttpStatus.NO_CONTENT);
	}
	
	
	
}
