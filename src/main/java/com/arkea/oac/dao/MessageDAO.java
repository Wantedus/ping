package com.arkea.oac.dao;

import org.springframework.stereotype.Component;

import com.arkea.oac.model.Message;

@Component
public interface MessageDAO {

	public void createMessage(Message m);
	
	public Message getMessage(int id);
}
