package com.arkea.oac.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	/**
	 * récupérer un message en fonction de son id 
	 * if id n'existe pas,
	 * @return un fichier vide avec NO_CONTENT 
	 * @return sinon retourner un fichier avec le message correspondant à l'id. 
	 */
	@CrossOrigin(origins = "*")
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
	@CrossOrigin(origins = "*")
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
	@CrossOrigin(origins = "*")
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
	@CrossOrigin(origins = "*")
	@GetMapping(value="/search/libelle")
	public MessagePageEntity getMessageByLibelle(
			String libelle,
			@RequestParam String type,
			@RequestParam(defaultValue = "1",required=false) Integer page,
			@RequestParam(defaultValue = "5",required=false) Integer size){
		return messageDAOImpl.getMessageByLibelle(libelle, type, page, size);
	}
	

	
	/** 
	 * get message en fonction du mot clé
	 * @param motcle
	 * @param type
	 * @param page actuel, 1 par defaut
	 * @param size
	 * @return une page en fonction du mot cle et du type 
	 */
	@CrossOrigin(origins = "*")
	@GetMapping(value="/search/motcle")
	public MessagePageEntity getMessageByMotCle(
			String motcle,
			@RequestParam String type,
			@RequestParam(defaultValue = "1",required=false) Integer page,
			@RequestParam(defaultValue = "5",required=false) Integer size){
		return messageDAOImpl.getMessageByMotCle(motcle, type, page, size);
	}
	

	

	/**
	 * Créer un nouveau message avec la requete Post 
	 * @param m, affecter un id au nouveau message m crée
	 * requete réussie, nouveau message crée
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/message")
	public ResponseEntity<Integer> postMessage(@RequestBody Message m) {
		
		int id; 
		try {
			id=messageDAOImpl.createMessage(m);
			return new ResponseEntity<Integer>(id, HttpStatus.CREATED);
		}
		catch(Exception e){
			return new ResponseEntity<Integer>(0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	

    /**
     * Put un nouveau message pour substituer un ancien message
     * @param m nouveau message crée
     * @param id du message m
     * @return requete a ete reçue
     */
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/message/{id}")
	public ResponseEntity<Integer> putMessage(@RequestBody Message m,@PathVariable int id) {
		
		try {
			messageDAOImpl.updateMessage(id, m);
			return new ResponseEntity<Integer>(id, HttpStatus.ACCEPTED);
		}
		catch(Exception e){
			return new ResponseEntity<Integer>(0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}

	

	/**
	 * Supprimer message en fonction d'un id
	 * @param id du message à supprimer
	 * @return une page vide 
	 */
	
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = "/message/{id}")
	public ResponseEntity<Integer> deleteMessage(@PathVariable int id) {
		try {
			messageDAOImpl.deleteMessage(id);
			return new ResponseEntity<Integer>(id, HttpStatus.NO_CONTENT);
		}
		catch(Exception e){
			return new ResponseEntity<Integer>(0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	
}
