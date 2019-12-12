package com.arkea.oac.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;

import org.springframework.stereotype.Component;

import com.arkea.oac.model.Message;
import com.mysql.jdbc.PreparedStatement;

@Component
public class  MessageDAOImpl implements MessageDAO {
	
private static java.sql.Connection con;
    
    private static String user = "root";
    private static String mdp = "";
    private static String url = "jdbc:mysql://localhost/ping?serverTimezone=UTC";
    
    public static java.sql.Connection getInstance(){

        if(con == null){
            try {
            	Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, user, mdp);

            } catch (Exception e)
            {
                e.printStackTrace();
            }
       }
        return con;
    }

	@Override
	public void createMessage(Message m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message getMessage(int id) {
		// Method to get a message based on its ID
		System.out.println("enter getMessage fonction");
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement("SELECT *	 FROM t90_msg WHERE IDT_MES_DWB = ?"))
		{
			System.out.println("inside the try");
            ps.setInt(1,id);
            ResultSet r =  ps.executeQuery();
            if(r.next() )
            	System.out.println("inside the next");
                return new Message(r.getString(3));
        }
        catch (Exception e)
		{
            e.printStackTrace();
        }
		return null;
	}

}
