package com.arkea.oac.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {

	private String id;
	//bulle ou message
	private String type;
	private String vision360;
	private String libelle;
	//private String textBulle;
	private String textMessage;
	public String getTextMessage() {
		return textMessage;
	}


	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	private List<String> keywords = new ArrayList<>();
	private Date start;
	private Date end;
	private List<String> entity = new ArrayList<String>();
	private List<String> canals = new ArrayList<>();
	private int priority;
	private int priorityGAB;

	private Target target;
	


	public Message() {

	}

/*
	public Message(String id, String type, String vision360, String lib, String textBulle,String textMessage, List<String> keywords,
			Date start, Date end, List<Integer> entity, List<String> canals, int priority, int priorityGAB, Target t) {
		super();
		this.id = id;
		this.type = type;
		this.vision360 = vision360;
		this.libelle = lib;
		this.textBulle = textBulle;
		this.textMessage=textMessage;
		this.keywords = keywords;
		this.start = start;
		this.end = end;
		this.entity = entity;
		this.canals = canals;
		this.priority = priority;
		this.priorityGAB = priorityGAB;
		this.target = t;
	}
	*/
	//Constructeur sans texte bulle
	public Message(String id, String type, String vision360, String lib,String textMessage, List<String> keywords,
			Date start, Date end, List<String> entity, List<String> canals, int priority, int priorityGAB, Target t) {
		super();
		this.id = id;
		this.type = type;
		this.vision360 = vision360;
		
		this.libelle = lib;
		
		this.textMessage=textMessage;
		this.keywords = keywords;
		this.start = start;
		this.end = end;
		this.entity = entity;
		this.canals = canals;
		this.priority = priority;
		this.priorityGAB = priorityGAB;
		this.target = t;
	}


	public Message(String id, String text) {

		//returns a tests message 
		this.id=id;
		this.type = text;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setVision360(String vision360) {
		this.vision360 = vision360;
	}

	public String getLibelle() {
		return libelle;
	}


	public void setTextLibelle(String lib) {
		this.libelle = lib;
	}

/*
	public String getTextBulle() {
		return textBulle;
	}


	public void setTextMes(String textMes) {
		this.textBulle = textMes;
	}
*/
	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}


	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public List<String> getEntity() {
		return entity;
	}

	public void setEntity(List<String> entity) {
		this.entity = entity;
	}

	public List<String> getCanals() {
		return canals;
	}

	public void setCanals(List<String> canals) {
		this.canals = canals;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriorityGAB() {
		return priorityGAB;
	}

	public void setPriorityGAB(int priorityGAB) {
		this.priorityGAB = priorityGAB;
	}

	public Target getT() {
		return target;
	}

	public void setT(Target t) {
		this.target = t;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getVision360() {
		return vision360;
	}


	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}





}
