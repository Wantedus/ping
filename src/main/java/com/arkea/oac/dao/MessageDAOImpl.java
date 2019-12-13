package com.arkea.oac.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;

import org.springframework.stereotype.Component;

import com.arkea.oac.model.Message;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

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
	public int createMessage(Message m) {
		
		int generatedId = 0;
		
		
		String sql = "INSERT INTO t90_msg(CD_EFS,LIB_TY_MES,TXT_LIB_MES,"
 		+ "LIB_MES_CNS,CD_PRTY_MES,DUR_VIE_MES,IDT_UTI, TXT_MES_CTU) VALUES (?, ?, ?,?,?,?,?,?)";
		
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
	        {
			    ps.setInt(1,34);
			    //ps.setInt(2,null);
			    ps.setString(2,m.getType());
	        	ps.setString(3,m.getWording());
	        	if(m.getType().equals("BULLE_CONSEILLER")) {
	        		ps.setString(4,"BULLE_CONSEILLER");
	        	}
	        	else {
	        		ps.setString(4,null	);
	        	}
	        	ps.setInt(5,m.getPriority());
	        	ps.setInt(6,0);
	        	ps.setString(7,"thomas");
	        	ps.setString(8,m.getText());
	        	
	        	int rowAffected =ps.executeUpdate();
	            if(rowAffected == 1)
	            {
	            	
	            	ResultSet rs = ps.getGeneratedKeys();
	            	if(rs.next())
	            	   generatedId = rs.getInt(1);
	            }
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		return  generatedId;
	}

	@Override
	public Message getMessage(int id) {
		// Method to get a message based on its ID
		System.out.println("enter getMessage fonction");
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement("SELECT *	 FROM t90_msg WHERE IDT_MES_DWB = ?"))
		{
			
            ps.setInt(1,id);
            ResultSet r =  ps.executeQuery();
            if(r.next() )
            	
                return new Message(r.getString(3));
        }
        catch (Exception e)
		{
            e.printStackTrace();
        }
		return null;
	}

}
