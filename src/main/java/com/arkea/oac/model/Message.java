package com.arkea.oac.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
	
	private String id;
	private String type;//bulle ou message
	private String wording ;
	private String vision360;
	private String textLib;
	private String textBulle;
	private String textMes;
	private List<String> keywords = new ArrayList<>();
	private Date start;
	private Date end;
	private List<Integer> entity = new ArrayList<Integer>();
	private List<String> canals = new ArrayList<>();
	private int priority;
	private int priorityGAB;
	
	private Target t;
	

	public Message() {
	
	}


	


	public Message(String id, String type, String wording, String vision360, String textLib, String textBulle, List<String> keywords,
		Date start, Date end, List<Integer> entity, List<String> canals, int priority, int priorityGAB, Target t, String textMes) {
		super();
		this.id = id;
		this.type = type;
		this.wording = wording;
		this.vision360 = vision360;
		this.textLib = textLib;
		this.textBulle = textBulle;
		this.textMes = textMes;
		this.keywords = keywords;
		this.start = start;
		this.end = end;
		this.entity = entity;
		this.canals = canals;
		this.priority = priority;
		this.priorityGAB = priorityGAB;
		this.t = t;
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

	public String getWording() {
		return wording;
	}

	public void setWording(String wording) {
		this.wording = wording;
	}

	public String isVision360() {
		return vision360;
	}

	public void setVision360(String vision360) {
		this.vision360 = vision360;
	}

	public String getTextLib() {
		return textLib;
	}


	public void setTextLib(String textLib) {
		this.textLib = textLib;
	}


	public String getTextMes() {
		return textMes;
	}


	public void setTextMes(String textMes) {
		this.textMes = textMes;
	}

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

	public List<Integer> getEntity() {
		return entity;
	}

	public void setEntity(List<Integer> entity) {
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
		return t;
	}

	public void setT(Target t) {
		this.t = t;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
