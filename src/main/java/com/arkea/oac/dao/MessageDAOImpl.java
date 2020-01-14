package com.arkea.oac.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
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
		int generatedIdPub=0;
		
		
		String sql = "INSERT INTO t90_msg(CD_EFS,LIB_TY_MES,TXT_LIB_MES,"
 		+ "LIB_MES_CNS,CD_PRTY_MES,DUR_VIE_MES,IDT_UTI, TXT_MES_CTU) VALUES (?, ?, ?,?,?,?,?,?)";
		/*
		"SELECT * FROM t90_msg  "
		+ "LEFT JOIN t90_pub ON t90_msg.IDT_MES_DWB = t90_pub.IDT_MES_DWB "
		+ "LEFT JOIN t90_efs ON t90_pub.IDT_PUB = t90_efs.IDT_PUB "
		+ "LEFT JOIN t90_cnl ON t90_pub.IDT_PUB = t90_cnl.IDT_PUB "
		+ "LEFT JOIN t90_mot_cle ON t90_pub.IDT_MES_DWB = t90_mot_cle.IDT_MES_DWB "
		+ "LEFT JOIN t90_var ON t90_pub.IDT_PUB = t90_var.IDT_PUB "
		+ "WHERE t90_msg.IDT_MES_DWB = ?"
		*/
		
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
	        {
			    ps.setInt(1,34); //CD_EFS
			   
			    ps.setString(2,m.getType()); //LIB_TY_MES
	        	ps.setString(3,m.getWording()); //TXT_LIB_MES
	        	if(m.getType().equals("BULLE_CONSEILLER")) { 
	        		ps.setString(4,"Votre conseiller vous informe");  //LIB_MES_CNS
	        	}
	        	else {
	        		ps.setString(4,""); 
	        	}
	        	ps.setInt(5,m.getPriority()); //CD_PRTY_MES
	        	ps.setInt(6,0); //DUR_VIE_MES
	        	ps.setString(7,"thomas"); //IDT_UTI
	        	
	        	ps.setString(8,m.getText()); //TXT_MES_CTU
	        	
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
		 
		 String sqlPub = "INSERT INTO t90_pub(CD_EFS,CD_EFS_MES,"
			 		+ "IDT_MES_DWB,CD_ETA_PUB,CD_TY_PUB,DA_DBT_AFG,DA_FIN_AFG, IDC_BIC,IDT_UTI,TM_STP)"
			 		+ " VALUES (?, ?,?,?,?,?,?,?,?,?)";
					/*
					"SELECT * FROM t90_msg  "
					+ "LEFT JOIN t90_efs ON t90_pub.IDT_PUB = t90_efs.IDT_PUB "
					+ "LEFT JOIN t90_cnl ON t90_pub.IDT_PUB = t90_cnl.IDT_PUB "
					+ "LEFT JOIN t90_mot_cle ON t90_pub.IDT_MES_DWB = t90_mot_cle.IDT_MES_DWB "
					+ "LEFT JOIN t90_var ON t90_pub.IDT_PUB = t90_var.IDT_PUB "
					+ "WHERE t90_msg.IDT_MES_DWB = ?"
					*/
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlPub, Statement.RETURN_GENERATED_KEYS))
	        {
			    ps.setInt(1,34); //CD_EFS
			   
			    ps.setInt(2,34); //CD_EFS_MES
	        	ps.setInt(3,generatedId ); //IDT_MES_DWB
	        	
	        	ps.setString(4,"O");  //CD_ETA_PUB
	        	
	        	ps.setString(5,"F"); //CD_TY_PUB
	        	if(m.getStart()!=null) {
			 		 ps.setDate(6,  new java.sql.Date(m.getStart().getTime() ) ); 
			 	}
			 	else {
			 		ps.setDate(6, null);
			 	}
	        	if(m.getEnd()!=null) {
			 		 ps.setDate(7,  new java.sql.Date(m.getEnd().getTime() ) ); 
			 	}
			 	else {
			 		ps.setDate(7, null);
			 	}
	        	
	        	ps.setString(8,"N"); //IDC_BIC
	        	ps.setString(9,"thomas"); //IDT_UTI
	        	java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
	        	ps.setTimestamp(10, date);
	        	
	        	
	        	int rowAffected =ps.executeUpdate();
	            if(rowAffected == 1)
	            {
	            	
	            	ResultSet rs = ps.getGeneratedKeys();
	            	if(rs.next())
	            	   generatedIdPub = rs.getInt(1);
	            }
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 
		 String sqlEfs = "INSERT INTO t90_efs(CD_EFS_PUB,IDT_PUB,CD_EFS)"
			 		+ " VALUES (?,?,?)";
					/*
					"SELECT * FROM t90_msg  "
					+ "LEFT JOIN t90_cnl ON t90_pub.IDT_PUB = t90_cnl.IDT_PUB "
					+ "LEFT JOIN t90_mot_cle ON t90_pub.IDT_MES_DWB = t90_mot_cle.IDT_MES_DWB "
					+ "LEFT JOIN t90_var ON t90_pub.IDT_PUB = t90_var.IDT_PUB "
					+ "WHERE t90_msg.IDT_MES_DWB = ?"*/
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlEfs))
	        {
			 
			 if(m.getEntity()!=null) {
			 		for (Integer temp : m.getEntity()) {
			 			 ps.setInt(1,34); //CD_EFS
						   
						    ps.setInt(2,generatedIdPub); //CD_EFS_MES
				        	ps.setInt(3,temp ); //IDT_MES_DWB
			 			ps.executeUpdate();
					}
			}
			 	
			     
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 
		 String sqlCnl = "INSERT INTO t90_cnl(CD_EFS_PUB,IDT_PUB,CD_CNL,NO_PRTY)"
			 		+ " VALUES (?,?,?,?)";
					/*
					"SELECT * FROM t90_msg  "
					+ "LEFT JOIN t90_mot_cle ON t90_pub.IDT_MES_DWB = t90_mot_cle.IDT_MES_DWB "
					+ "LEFT JOIN t90_var ON t90_pub.IDT_PUB = t90_var.IDT_PUB "
					+ "WHERE t90_msg.IDT_MES_DWB = ?"*/
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlCnl))
	        {
			 
			 if(m.getCanals() !=null) {
			 		for (String temp : m.getCanals()) {
			 			 ps.setInt(1,34); 
						   
						    ps.setInt(2,generatedIdPub);
				        	ps.setString(3,temp);
				        	ps.setInt(4, m.getPriority());
			 			ps.executeUpdate();
					}
			}
			 	
			     
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 String sqlMc = "INSERT INTO t90_mot_cle(CD_EFS,TXT_CLE,IDT_MES_DWB,IDT_UTI,TM_STP)"
			 		+ " VALUES (?,?,?,?,?)";
					/*
					"SELECT * FROM t90_msg  "
					+ "LEFT JOIN t90_var ON t90_pub.IDT_PUB = t90_var.IDT_PUB "
					+ "WHERE t90_msg.IDT_MES_DWB = ?"*/
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMc))
	        {
			 
			 if(m.getKeywords() !=null) {
			 		for (String temp : m.getKeywords()) {
			 			 
			 			ps.setInt(1,34); 
						 
						    ps.setString(2,temp);
				        	ps.setInt(3,generatedId);
				        	ps.setString(4, "thomas");
				        	java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
				        	ps.setTimestamp(5, date);
				        	
			 			ps.executeUpdate();
					}
			}
			 	
			     
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 //Ajouter les différents clients en aprsant le fichier envoyé
		 /*
		 String sqlVar = "INSERT INTO t90_var(CD_EFS_PUB,CD_EFS_PSE,NO_PSE_MAL,IDT_PUB,NO_PSE,IDT_CTB_CTU,CD_CHP,LIB_CHP)"
			 		+ " VALUES (?,?,?,?,?)";
					
					
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMc))
	        {
			 
			 if(m.getKeywords() !=null) {
			 		for (String temp : m.getKeywords()) {
			 			 
			 			ps.setInt(1,34); 
						 
						    ps.setString(2,temp);
				        	ps.setInt(3,generatedId);
				        	ps.setString(4, "thomas");
				        	java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
				        	ps.setTimestamp(5, date);
				        	
			 			ps.executeUpdate();
					}
			}
			 	
			     
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 */
		 
		return  generatedId;
	}

	public int updateMessage(int id, Message m) { //Fonctionne pour les champs simple (pas les lise, ni le target)
		
		int generatedId = 0;
		int generatedIdPub=0;
		
		String sqlMsg = "UPDATE t90_msg SET CD_EFS = ? ,LIB_TY_MES =?,TXT_LIB_MES=?,"
				+ "LIB_MES_CNS=?,CD_PRTY_MES=?,DUR_VIE_MES=?,NB_AFG_MX=?, TXT_MES_CTU=?"
				+ " WHERE IDT_MES_DWB = ?";
		//"INSERT INTO t90_msg(CD_EFS,LIB_TY_MES,TXT_LIB_MES,"
 		//+ "LIB_MES_CNS,CD_PRTY_MES,DUR_VIE_MES,IDT_UTI,NB_AFG_MAX, TXT_MES_CTU) VALUES (?, ?, ?,?,?,?,?,?,?)";
		//to do
		
		String sqlPub  ="UPDATE t90_pub SET DA_DBT_AFG = ? ,DA_FIN_AFG =?  WHERE IDT_MES_DWB = ? ";
		String sqlMc = "UPDATE t90_mot_cle SET TXT_CLE = ?   WHERE IDT_MES_DWB = ? ";
		//"LEFT JOIN t90_efs ON t90_pub.IDT_PUB = t90_efs.IDT_PUB "
		String sqlEfsD = "DELETE from t90_efs WHERE IDT_PUB = ? ";		
		String sqlEfsI = "INSERT into t90_efs (CD_EFS_PUB, IDT_PUB,CD_EFS) values (?,?,?)  ";		
		
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMsg, Statement.RETURN_GENERATED_KEYS))
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
	        	//ps.setString(7,"thomas"); //IDT_UTI
	        	ps.setInt(7,0); //NB_AFG_MAX 
	        	ps.setString(8,m.getText()); //TXT_MES_CTU
	        	
	        	ps.setInt(9,id); //IDT_MES_DWB
	        	
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
		 
		 
		
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlPub, Statement.RETURN_GENERATED_KEYS))
	        {
			
			 	if(m.getStart()!=null) {
			 		 ps.setDate(1,  new java.sql.Date(m.getStart().getTime() ) ); 
			 	}
			 	else {
			 		ps.setDate(1, null);
			 	}
				if(m.getEnd()!=null) {
					ps.setDate(2,new java.sql.Date(m.getEnd().getTime() ) ); 
				}
				else {
			 		ps.setDate(2, null);
			 	}
					   
					
			     	ps.setInt(3,id); //TXT_LIB_MES
			     	int rowAffected =ps.executeUpdate();
		            if(rowAffected == 1)
		            {
		            	
		            	ResultSet rs = ps.getGeneratedKeys();
		            	if(rs.next())
		            		generatedIdPub = rs.getInt(1);
		            }
	        }
		  catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 
		 /*
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMc))
	        {
			
			 	if(m.getKeywords()!=null) {
			 		 ps.setString(1,  m.getKeywords().get(0) ); 
			 	}
			 	else {
			 		ps.setDate(1, null);
			 	}
						   
					
			     	ps.setInt(2,id); //TXT_LIB_MES
			     	ps.executeUpdate();
	        }
		  catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        */
		/* 
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlEfsD))
	        {
			
			 
			 	ps.setInt(1,  generatedIdPub ); 
			 	ps.executeUpdate();
	        }
		  catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlEfsI))
	        {//String sqlEfsI = "Insert info t90_efs CD_EFSPUB, IDT_PUB,CD_EFS values (?,?,?)  ";	
			
			 	if(m.getEntity()!=null) {
			 		for (Integer temp : m.getEntity()) {
			 			 ps.setInt(1, 34 ); 
			 			 ps.setInt(2, generatedIdPub);
			 			 ps.setInt(3, temp);
			 			ps.executeUpdate();
					}
			 		
			 	}
			 	else {
			 		ps.setDate(1, null);
			 	}
			     	
	        }
		  catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        
	        */
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
																		+ "LEFT JOIN t90_pub ON t90_msg.IDT_MES_DWB = t90_pub.IDT_MES_DWB "
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
            	
            	if(r.getString(22) !=null) {
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
            	
            
            } 
            
            Target target = new Target(federation, agency, clients);
/*            
            System.out.println("id : " + identity);
        	System.out.println("vision360 : ???");
        	System.out.println("type : " + type);
        	System.out.println("wording : " + wording);
        	System.out.println("text : " + text);
        	System.out.println("start : " + start);
        	System.out.println("end : " + end);
        	System.out.println("priority : ???");
        	System.out.println("targetType : " + targetType);
       	
            for (int mEntity : entities) {
            	System.out.println("Entity : " + mEntity);
            }
            
            for (String mCanal : canaux) {
            	System.out.println("canal : " + mCanal);
            }
            
            for (String mKeyword : keywords) {
            	System.out.println("Keyword : " + mKeyword);
           }
            
            for (String mClient : clients) {
            	System.out.println("Client : " + mClient);
            }
 */           
            boolean vision360=true;
            int priority=0;
            
            return new Message(identity, type, wording, vision360, text, keywords, start, end, entities, canaux, priority, target);
        }
        catch (Exception e)
		{
            e.printStackTrace();
        }
		return null;
	}

	
	

}
