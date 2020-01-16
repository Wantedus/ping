package com.arkea.oac.dao;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.arkea.oac.model.Message;

@Component
public interface MessageDAO {

	public int createMessage(Message m);
	
	public Message getMessage(int id);
	
	public int updateMessage(int id,Message m);
	
	public ArrayList<Message> getAllMessage();
	
	public int deleteMessage(int id);
}
