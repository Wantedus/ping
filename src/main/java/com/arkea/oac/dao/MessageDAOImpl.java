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

    /**
	 * Cr�er un message 
	 * @param Messsage
	 * @return Id du message cr��
	 * @exception exception while compiling SQL
	 * @author ThomasCLISSON
	 */
	@Override
	public int createMessage(Message m) {
		
		int generatedId = 0;
		int generatedIdPub=0;
		ResultSet rs;
		int rowAffected;
		
		//ajout dans t90_msg
		String sql = "INSERT INTO t90_msg(CD_EFS,LIB_TY_MES,TXT_LIB_MES,"
 		+ "LIB_MES_CNS,CD_PRTY_MES,DUR_VIE_MES,IDT_UTI, TXT_MES_CTU) VALUES (?, ?, ?,?,?,?,?,?)";
		//dans t90_pub
		 String sqlPub = "INSERT INTO t90_pub(CD_EFS,CD_EFS_MES,"
			 		+ "IDT_MES_DWB,CD_ETA_PUB,CD_TY_PUB,DA_DBT_AFG,DA_FIN_AFG, IDC_BIC,IDT_UTI,TM_STP)"
			 		+ " VALUES (?, ?,?,?,?,?,?,?,?,?)";
		 //dans t90_efs
		 String sqlEfs = "INSERT INTO t90_efs(CD_EFS_PUB,IDT_PUB,CD_EFS)"
			 		+ " VALUES (?,?,?)";
		 //t90_cnl
		 String sqlCnl = "INSERT INTO t90_cnl(CD_EFS_PUB,IDT_PUB,CD_CNL,NO_PRTY)"
			 		+ " VALUES (?,?,?,?)";
		 //t90_mc
		 String sqlMc = "INSERT INTO t90_mot_cle(CD_EFS,TXT_CLE,IDT_MES_DWB,IDT_UTI,TM_STP)"
			 		+ " VALUES (?,?,?,?,?)";
		 
		 String sqlMcDelete = "delete from t90_mot_cle where IDT_MES_DWB=?";
		 String sqlTy = "INSERT INTO oac.T90_TY_CLI (CD_EFS, CD_ESA, IDT_MES_DWB, IDT_UTI, TM_STP)"
		 +"VALUES (34, ?, ?, ?, ?)";
		
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
	        {
			    ps.setInt(1,34); //CD_EFS
			   
			    ps.setString(2,m.getType()); //LIB_TY_MES
	        	ps.setString(3,m.getWording()); //TXT_LIB_MES
	        	if(m.getType().equals("BULLE_CONSEILLER")) { 
	        		ps.setString(4,"Votre conseiller vous informe");  //LIB_MES_CNS
	        	}
	        	else {
	        		ps.setString(4,m.getTextLib()); 
	        	}
	        	ps.setInt(5,m.getPriority()); //CD_PRTY_MES
	        	ps.setInt(6,0); //DUR_VIE_MES
	        	ps.setString(7,"thomas"); //IDT_UTI
	        	
	        	ps.setString(8,m.getTextMes()); //TXT_MES_CTU
	        	
	        	rowAffected =ps.executeUpdate();
	            if(rowAffected == 1)
	            {
	            	
	            	rs = ps.getGeneratedKeys();
	            	if(rs.next())
	            	   generatedId = rs.getInt(1);
	            }
	            rowAffected=0;
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 
		
					
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
	        	
	        	
	        	rowAffected =ps.executeUpdate();
	            if(rowAffected == 1)
	            {
	            	
	            	 rs = ps.getGeneratedKeys();
	            	if(rs.next())
	            	   generatedIdPub = rs.getInt(1);
	            }
	            rowAffected=0;
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 
		
					
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
		 /*
		 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlTy))
	        {
			 
			 if(m.getKeywords() !=null) {
			 		for (String temp : m.getKeywords()) {
			 			 
			 			   //AG, AS, PA,PR -- d�pend du fichier � l'upload ? 
						    ps.setString(1,temp);
						    //IDT_MES_DWB
				        	ps.setInt(3,generatedId);
				        	//IDT_UTI
				        	ps.setString(4, "thomas");
				        	//TP_STP (current timestamp)
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
	        
		 //Ajouter les diff�rents clients en aprsant le fichier envoy�
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

	/**
	 * Mettre un jour message 
	 * @param id du message, Messsage
	 * @return Id du message modifi�
	 * @exception exception while compiling SQL
	 * @author ThomasCLISSON
	 */
	public int updateMessage(int id, Message m) { //Fonctionne pour les champs simple et keywords (pas les liste, ni le target)
		
		int generatedId = 0;
		int generatedIdPub=0;
		int rowAffected=0;
		ResultSet rs;
		
		String sqlMsg = "UPDATE t90_msg SET CD_EFS = ? ,LIB_TY_MES =?,TXT_LIB_MES=?,"
				+ "LIB_MES_CNS=?,CD_PRTY_MES=?,DUR_VIE_MES=?,DA_MOD=?,NB_AFG_MX=?, TXT_MES_CTU=? WHERE IDT_MES_DWB = ? " ;
		
		
		String sqlPub  ="UPDATE t90_pub SET DA_DBT_AFG = ? ,DA_FIN_AFG =?  WHERE IDT_MES_DWB = ? ";
		
		String sqlMc = "INSERT INTO t90_mot_cle (CD_EFS,TXT_CLE,IDT_MES_DWB,IDT_UTI,TM_STP) values (?,?,?,?,?)  ";
		String sqlMcD = "DELETE from t90_mot_cle WHERE IDT_MES_DWB = ? ";		
		
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
	        		System.out.println(m.getTextLib());
	        		ps.setString(4,m.getTextLib()); 
	        	}
	        	ps.setInt(5,m.getPriority()); //CD_PRTY_MES
	        	ps.setInt(6,0); //DUR_VIE_MES
	        	//ps.setString(7,"thomas"); //IDT_UTI
	        	java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
		 		 ps.setTimestamp(7,  date ); 
	        	ps.setInt(8,0); //NB_AFG_MAX 
	        	System.out.println(m.getTextMes());
	        	ps.setString(9,m.getTextMes()); //TXT_MES_CTU
	        	
	        	ps.setInt(10, id);
	        	
	        	 rowAffected =ps.executeUpdate();
	            if(rowAffected == 1)
	            {
	            	
	            	 rs = ps.getGeneratedKeys();
	            	 if(rs.next())
	            	   generatedId = rs.getInt(1);
	            }
	            rs=null;
	            rowAffected=0;
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
			     	rowAffected =ps.executeUpdate();
		            if(rowAffected == 1)
		            {
		            	
		            	rs = ps.getGeneratedKeys();
		            	if(rs.next())
		            		generatedIdPub = rs.getInt(1);
		            }
		            rs=null;
		            rowAffected=0;
	        }
		  catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 
		 if(m.getKeywords().size()>0) {
			 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMc))
		        {	
				
				 	
				 		for(String k : m.getKeywords()) {
				 			 ps.setInt(1,  34); 
					 		 //Mot cl�
					 		 ps.setString(2,k);
					 		//TXT_LIB_MES
					 		 ps.setInt(3,id);
					 		 ps.setString(4, "thomas");
					 	 	java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
					 		 ps.setTimestamp(5,  date ); 
					 		ps.executeUpdate();
				 		}
				 		
						ps.close();
		        }
			  catch (Exception e)
		        {
		            e.printStackTrace();
		        }
		 }
		 else {
			 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMcD))
		        {	
				 ps.setInt(1, id);
						ps.executeUpdate();
						ps.close();
		        }
			  catch (Exception e)
		        {
		            e.printStackTrace();
		        }
			 
		 	}
	        
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

	/**
	 * Get all messages in database
	 * @param null
	 * @return a list of messages
	 * @exception exception while compiling SQL
	 * @author YinjieZHAO
	 */
	@Override 
	public ArrayList<Message> getAllMessage() {
		int identity;
		Message m = new Message();
		ArrayList<Message> mMessageList = new ArrayList<>(); 
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement("SELECT IDT_MES_DWB FROM t90_msg "))
		{
			ResultSet r = ps.executeQuery();
			
			while (r.next()) {
				identity = r.getInt(1);
				m = getMessage(identity);
				mMessageList.add(m);
			}
			
			ps.close();
			
			return mMessageList;
			
		}catch (Exception e)
		{
            e.printStackTrace();
        }
		return null;
	}
	
	
	/**
	 * Get message by id
	 * @param id id du message
	 * @return le resultat du message
	 * @author YinjieZHAO
	 */
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
        
        //Target
        Target t;
        
        // Elements
        String identity="";
        String type="";
        String wording="";
        String textLib="";
        String textMes="";
        Date start=null;
        Date end=null;
        String targetType="";
        String client="";
        String vision360="";
        
        int priority=0;
        int priorityGAB=0;
        
        //Temporary value
        String tmp="";
        
		// Method to get a message based on its ID
		System.out.println("Get message with id : " +id);
		
		String SELECT = "SELECT * FROM t90_msg  ";
		String JOIN_t90_pub = "LEFT JOIN t90_pub ON t90_msg.IDT_MES_DWB = t90_pub.IDT_MES_DWB ";
		String JOIN_t90_efs = "LEFT JOIN t90_efs ON t90_pub.IDT_PUB = t90_efs.IDT_PUB ";
		String JOIN_t90_cnl = "LEFT JOIN t90_cnl ON t90_pub.IDT_PUB = t90_cnl.IDT_PUB ";
		String JOIN_t90_mot_cle = "LEFT JOIN t90_mot_cle ON t90_msg.IDT_MES_DWB = t90_mot_cle.IDT_MES_DWB ";
		String JOIN_t90_var = "LEFT JOIN t90_var ON t90_pub.IDT_PUB = t90_var.IDT_PUB ";
		String WHERE_t90_var = "WHERE t90_msg.IDT_MES_DWB = ?";
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(SELECT
																		+ JOIN_t90_pub
																		+ JOIN_t90_efs
																		+ JOIN_t90_cnl
																		+ JOIN_t90_mot_cle
																		+ JOIN_t90_var
																		+ WHERE_t90_var))
		{
			ps.setInt(1,id);
            ResultSet r =  ps.executeQuery();
            
            while(r.next() ) {
            	//Identity
            	identity = r.getString(2);
            	//Type of message
            	type = r.getString(3);
            	// Libellé
            	wording = r.getString(4);
            	// Texte
<<<<<<< HEAD
            	text = r.getString(11);
=======
            	textLib = r.getString(5);
>>>>>>> 4089b3a615a2f23460ef02340e79ee3fc28bd01f
            	// Vision360
            	vision360 = r.getString(9);
            	// Le type de la cible
            	targetType = r.getString(22);
            	// La date début d'affichage du message
            	start = r.getDate(23);
            	// La date fin d'affichage du message
            	end = r.getDate(24);
            	textMes=r.getString(11);
            	
            	// La valeur temporaire de la priorité
            	tmp = r.getString(6);
            	// Vérifier si la valeur temporaire est nulle
            	if (tmp != null)
            		priority = Integer.parseInt(tmp);
            	
            	// Vérifier si la liste entité contient un élément pareil
            	if ( !entities.contains(r.getInt(32)) ) 
            		entities.add(r.getInt(32));
            	
            	// Vérifier si la liste canal contient un élément pareil
            	if ( !canaux.contains(r.getString(35)) ) 
            		canaux.add(r.getString(35));
            	
            	// La valeur temporaire du canal
            	tmp = r.getString(35);
            	
            	// Vérifier si le canal est null
            	if (tmp != null) {
            		// Vérifier si le canal est GAB
            		if (tmp.equals("GAB")) {
                		tmp = r.getString(36);
            			priorityGAB = r.getInt(36);
                	}
            	}
            	System.out.println(r.getString(38));
            	// Vérifier si la liste des mots clés contient un élément pareil
            	if ( !keywords.contains(r.getString(38)) )
            		keywords.add(r.getString(38));
            	
            	// Vérifier si le type de la cible est null
            	if(r.getString(22) !=null) {
            		
            		// Vérifier si le type de la cible est de la liste des clients
            		if (targetType.equals("C")) {
            			// Mettre la federation en false
                		federation=false;
                		// Mettre l'agence en false
                		agency=false;
                		
                		// Add list of clients
                		if ( NO_PSE.contains(r.getString(46)) && !tmp.equals(r.getString(49))) {
                			//la valeur temporaire du client
                    		tmp = r.getString(49);
                    		// Ajouter le PP
                    		client += tmp;
                    		// Ajouter le PP/PM dans la liste de client
                    		clients.add(client);
                    		
                    	} else if ( !NO_PSE.contains(r.getString(46))){
                    		// Add client PP
                    		NO_PSE.add(r.getString(46));
                    		// La valeur temporaire du Client PP
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
            
            t = new Target(federation, agency, clients);

            ps.close();
            
            return new Message(identity, type, wording, vision360, textLib, keywords, start, end, entities, canaux, priority, priorityGAB, t,textMes );
        
		}

        catch (Exception e)
		{
            e.printStackTrace();
        }
		return null;
	}

	public int deleteMessage(int id) {
		
			
			String sql ="delete from t90_pub where IDT_MES_DWB=?";
			String sql2 ="delete from t90_msg where IDT_MES_DWB=?";
			
			 try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sql))
		        {
				   ps.setInt(1,id); //CD_EFS
				   ps.executeUpdate();
				   System.out.println("after execute");
		           ps.close();
		        }
		        catch (Exception e)
		        {
		            e.printStackTrace();
		        }
			
			try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sql2))
	        {
			   ps.setInt(1,id); //CD_EFS
			   ps.executeUpdate();
			   ps.close();
	           
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		
		return id;
	}

}
