package com.arkea.oac.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	/**
	 * récupérer un message en fonction de son id 
	 * if id n'existe pas,
	 * @return un fichier vide avec NO_CONTENT 
	 * @return sinon retourner un fichier avec le message correspondant à l'id. 
	 */
	@GetMapping(value = "/message/{id}")
	public ResponseEntity<Message> getMessage(@PathVariable int id) {
		
		 Message m =messageDAOImpl.getMessage(id);
		
		 if(m.getId()=="") {
			 return  new ResponseEntity<Message>(m, HttpStatus.NO_CONTENT);
		 }
		 else{
			return new ResponseEntity<Message>(m, HttpStatus.OK);
		 }
	 

	}
	
	/**
	 * recuperer tous les messages
	 * return list message
	 */
	@GetMapping(value = "/message")
	public ArrayList<Message> getAllMessage() {
		return messageDAOImpl.getAllMessage();
	}
	
	
	/**
	 * Get all message by page
	 * @param page la page actuelle, 1 par defaut
	 * @param size les rangs qu'ils vont afficher sur la page, 10 par defaut
	 * @return la page
	 */
	@GetMapping(value = "/message/")
	public MessagePageEntity getAllMessageByPage(
			@RequestParam(defaultValue = "1", required=false) Integer page,
			@RequestParam(defaultValue = "10", required=false) Integer size){
		
		return messageDAOImpl.getAllMessageByPage(page, size);
	}

    /**
     * Récupérer les messages en fonction du libellé	
     * libelle declaration
     * @param, recupere parametre de la requete Http
     * @param page actuel, 0 par defaut
     * @param size les rans qui seront afficher sur la page, 5 par defaut
     * @return une page en fonction du libelle et type 
     */
	@GetMapping(value="/search/libelle")
	public List<Message> getMessageByLibelle(
			String libelle,
			@RequestParam String type,
			@RequestParam(defaultValue = "0",required=false) Integer page,
			@RequestParam(defaultValue = "5",required=false) Integer size){
		return messageDAOImpl.getMessageByLibelle(libelle, type, page, size);
	}
	
	/** 
	 * get message en fonction du mot clé
	 * @param motcle
	 * @param type
	 * @param page actuel, 0 par defaut
	 * @param size
	 * @return une page en fonction du mot cle et du type 
	 */
	@GetMapping(value="/search/motcle")
	public List<Message> getMessageByMotCle(
			String motcle,
			@RequestParam String type,
			@RequestParam(defaultValue = "0",required=false) Integer page,
			@RequestParam(defaultValue = "5",required=false) Integer size){
		return messageDAOImpl.getMessageByMotCle(motcle, type, page, size);
	}
	
	/**
	 * Créer un nouveau message avec la requete Post 
	 * @param m, affecter un id au nouveau message m crée
	 * requete réussie, nouveau message crée
	 */
	@PostMapping(value = "/message")
	public ResponseEntity<Integer> postMessage(@RequestBody Message m) {
		
		int id =messageDAOImpl.createMessage(m);
		
		
		return new ResponseEntity<Integer>(id, HttpStatus.CREATED);
		
	}
    /**
     * Put un nouveau message pour substituer un ancien message
     * @param m nouveau message crée
     * @param id du message m
     * @return requete a ete reçue
     */
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
	/**
	 * Supprimer message en fonction d'un id
	 * @param id du message à supprimer
	 * @return une page vide 
	 */
	
	@DeleteMapping(value = "/message/{id}")
	public ResponseEntity<Integer> deleteMessage(@PathVariable int id) {
		messageDAOImpl.deleteMessage(id);
		return new ResponseEntity<Integer>(id, HttpStatus.NO_CONTENT);
	}
	
	
	
}
