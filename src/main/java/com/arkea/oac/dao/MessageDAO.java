package com.arkea.oac.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.arkea.oac.model.Message;
import com.arkea.page.MessagePageEntity;

@Component
public interface MessageDAO {

	public int createMessage(Message m);
	
	public Message getMessage(int id);
	
	public int updateMessage(int id,Message m);
	
	public ArrayList<Message> getAllMessage();
	
	public int deleteMessage(int id);

	public ArrayList<Message> getRangedMessage(Integer page, Integer size);
	
	public MessagePageEntity getAllMessageByPage (Integer page, Integer size);
	
	public List<Message> getMessageByMotCle (String motCle, String type, Integer page, Integer size);
	
	public List<Message> getMessageByLibelle (String libelle, String type, Integer page, Integer size);
}
