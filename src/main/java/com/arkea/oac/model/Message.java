package com.arkea.oac.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
	
	private String type;//bulle ou message
	private String wording ;
	private boolean vision360;
	private String text;
	private List<String> keywords = new ArrayList<>();
	private List<String> images = new ArrayList<>();
	private Date start;
	private Date end;
	private List<Integer> entity = new ArrayList<Integer>();
	private List<String> canals = new ArrayList<>();
	private int priority;
	
	private Target t;
	
	

	public Message(String type, String wording, boolean vision360, String text, List<String> keywords,
			List<String> images, Date start, Date end, List<Integer> entity, List<String> canals, int priority,
			Target t) {
		super();
		this.type = type;
		this.wording = wording;
		this.vision360 = vision360;
		this.text = text;
		this.keywords = keywords;
		this.images = images;
		this.start = start;
		this.end = end;
		this.entity = entity;
		this.canals = canals;
		this.priority = priority;
		this.t = t;
	}
	
	public Message() {
				
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

	public boolean isVision360() {
		return vision360;
	}

	public void setVision360(boolean vision360) {
		this.vision360 = vision360;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
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

	public Target getT() {
		return t;
	}

	public void setT(Target t) {
		this.t = t;
	}
	
	

}
