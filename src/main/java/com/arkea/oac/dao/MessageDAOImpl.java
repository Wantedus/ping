package com.arkea.oac.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.arkea.oac.model.Message;
import com.arkea.oac.model.Target;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@Component
public class  MessageDAOImpl implements MessageDAO {
	
private static java.sql.Connection con;
    
    private static String user = "root";
    private static String mdp = "root";
    private static String url = "jdbc:mysql://localhost:8889/ping?serverTimezone=UTC";
    
    
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
 		+ "LIB_MES_CNS,CD_PRTY_MES,DUR_VIE_MES,IDT_UTI,NB_AFG_MAX, TXT_MES_CTU) VALUES (?, ?, ?,?,?,?,?,?,?)";
		
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
	        {
			    ps.setInt(1,34); //CD_EFS
			   
			    ps.setString(2,m.getType()); //LIB_TY_MES
	        	ps.setString(3,m.getWording()); //TXT_LIB_MES
	        	if(m.getType().equals("BULLE_CONSEILLER")) { 
	        		ps.setString(4,"Votre conseiller vous informe");  //LIB_MES_CNS
	        	}
	        	else {
	        		ps.setString(4,null	); 
	        	}
	        	ps.setInt(5,m.getPriority()); //CD_PRTY_MES
	        	ps.setInt(6,0); //DUR_VIE_MES
	        	ps.setString(7,"thomas"); //IDT_UTI
	        	ps.setInt(8,0); //NB_AFG_MAX 
	        	ps.setString(9,m.getText()); //TXT_MES_CTU
	        	
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
		//In the target section
		boolean federation=true;
		boolean agency=true;
		
		// Lists
        ArrayList <String> canaux = new ArrayList<>();
        ArrayList <Integer> entities = new ArrayList<>();
        ArrayList <String> keywords = new ArrayList<>();
        ArrayList <String> clients = new ArrayList<>();
        ArrayList <String> NO_PSE = new ArrayList<>();
        
        // Elements
        String identity="";
        String type="";
        String wording="";
        String text="";
        Date start=null;
        Date end=null;
        String targetType="";
        String client="";
        
        //Temporary value
        String tmp="";
        
		// Method to get a message based on its ID
		System.out.println("Get message with id : " +id);
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement("SELECT * FROM t90_msg  "
																		+ "INNER JOIN t90_pub ON t90_msg.IDT_MES_DWB = t90_pub.IDT_MES_DWB "
																		+ "LEFT JOIN t90_efs ON t90_pub.IDT_PUB = t90_efs.IDT_PUB "
																		+ "LEFT JOIN t90_cnl ON t90_pub.IDT_PUB = t90_cnl.IDT_PUB "
																		+ "LEFT JOIN t90_mot_cle ON t90_pub.IDT_MES_DWB = t90_mot_cle.IDT_MES_DWB "
																		+ "LEFT JOIN t90_var ON t90_pub.IDT_PUB = t90_var.IDT_PUB "
																		+ "WHERE t90_msg.IDT_MES_DWB = ?"))
		{
			
            ps.setInt(1,id);
            ResultSet r =  ps.executeQuery();
            
            while(r.next() ) {

            	identity = r.getString(2);
            	type = r.getString(3);
            	wording = r.getString(3);
            	text = r.getString(4);
            	targetType = r.getString(22);
            	start = r.getDate(23);
            	end = r.getDate(24);
            	
            	if ( !entities.contains(r.getInt(32)) ) 
            		entities.add(r.getInt(32));
            	
            	if ( !canaux.contains(r.getString(35)) ) 
            		canaux.add(r.getString(35));
            	
            	if ( !keywords.contains(r.getString(38)) )
            		keywords.add(r.getString(38));
            	
            	
            	if (targetType.equals("C")) {
            		federation=false;
            		agency=false;
            		// Add list of clients PM, PP
            		if ( NO_PSE.contains(r.getString(46)) && !tmp.equals(r.getString(49))) {
                		tmp = r.getString(49);
                		client += tmp;
                		clients.add(client);
                		
                	} else if ( !NO_PSE.contains(r.getString(46))){
                		NO_PSE.add(r.getString(46));
                		tmp = r.getString(49);
                		client = tmp + ",";
                	}
            	} else if (targetType.equals("F")) {
            		federation=true;
            		agency=false;
            	} else {
            		federation=false;
            		agency=true;
            	}
            
            } 
            
            Target t = new Target(federation, agency, clients);
            
//            System.out.println("id : " + identity);
//        	System.out.println("vision360 : ???");
//        	System.out.println("type : " + type);
//        	System.out.println("wording : " + wording);
//        	System.out.println("text : " + text);
//        	System.out.println("start : " + start);
//        	System.out.println("end : " + end);
//        	System.out.println("priority : ???");
//        	System.out.println("targetType : " + targetType);
//        	
//            for (int mEntity : entities) {
//            	System.out.println("Entity : " + mEntity);
//            }
//            
//            for (String mCanal : canaux) {
//            	System.out.println("canal : " + mCanal);
//            }
//            
//            for (String mKeyword : keywords) {
//            	System.out.println("Keyword : " + mKeyword);
//            }
//            
//            for (String mClient : clients) {
//            	System.out.println("Client : " + mClient);
//            }
            
            boolean vision360=true;
            int priority=0;
            
            return new Message(identity, type, wording, vision360, text, keywords, start, end, entities, canaux, priority, t);
        }
        catch (Exception e)
		{
            e.printStackTrace();
        }
		return null;
	}

}
