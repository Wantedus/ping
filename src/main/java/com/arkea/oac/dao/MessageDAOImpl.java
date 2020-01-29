package com.arkea.oac.dao;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.arkea.oac.model.Message;
import com.arkea.oac.model.Target;
import com.arkea.page.MessagePageEntity;
import com.mysql.jdbc.Statement;

@Component
@PropertySource("classpath:application.properties")
public class  MessageDAOImpl implements MessageDAO {

	private static java.sql.Connection con;

	@Value( "${username}" )  
	private String username;
	@Value( "${mdp}" )  
	private  String mdp;
	@Value( "${url}" )  
	private  String url ;
	//IDT_UTI prends la valeur userName, vu que nous avons pas accès à la conexion ni aux utilisateurs, nous rempla
	private String userName="";


	public java.sql.Connection getInstance(){
		System.out.println(username);
		System.out.println(mdp);
		System.out.println(url);

		if(con == null){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(url, username, mdp);

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return con;
	}



	//    public static java.sql.Connection getInstance(){
	//		
	//    	private static java.sql.Connection con;
	//
	//		@Value( "${user}" )  
	//		private String user;
	//		@Value( "${mdp}" )  
	//		private  String mdp;
	//		@Value( "${url}" )  
	//		private  String url ;
	//		
	//	
	//    	System.out.println(user);
	//    	System.out.println(mdp);
	//    	System.out.println(url);
	//    	
	//        if(con == null){
	//            try {
	//            	Class.forName("com.mysql.jdbc.Driver");
	//                con = DriverManager.getConnection(url, user, mdp);
	//
	//            } catch (Exception e)
	//            {
	//                e.printStackTrace();
	//            }
	//       }
	//        return con;
	//    }


/*
	 * Crï¿½er un message 
	 * @param Messsage
	 * @return Id du message crÃ©Ã©
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


		String sqlTy = "INSERT INTO oac.T90_TY_CLI (CD_EFS, CD_ESA, IDT_MES_DWB, IDT_UTI, TM_STP)"
				+"VALUES (34, ?, ?, ?, ?)";
		
		String sqlVar = "INSERT INTO oac.T90_VAR (CD_EFS_PUB, CD_EFS_PSE, NO_PSE_MAIL, IDT_PUB, NO_PSE_IDT_CTB_CTU,CD_CHP_LIB_CHP)"
				+"VALUES (?, ?, ?, ?, ?,?,?,?)";

		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setInt(1,34); //CD_EFS

			ps.setString(2,m.getType()); //LIB_TY_MES
			ps.setString(3,m.getLibelle()); //TXT_LIB_MES
			if(m.getType().equals("BULLE_CONSEILLER")) { 
				ps.setString(4,"Votre conseiller vous informe");  //LIB_MES_CNS
			}
			else {
				ps.setString(4,""); 
			}
			//CD_PRTY_MES
			ps.setInt(5,m.getPriority()); 
			//DUR_VIE_MES
			ps.setInt(6,0);
			//IDT_UTI
			ps.setString(7,userName); 
			//TXT_MES_CTU
			ps.setString(8,m.getTextMessage()); 

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
			//CD_EFS
			ps.setInt(1,34); 
			//CD_EFS_MES
			ps.setInt(2,34); 
			//IDT_MES_DWB
			ps.setInt(3,generatedId ); 
			//CD_ETA_PUB
			ps.setString(4,"O");  
			//CD_TY_PUB
			ps.setString(5,m.getT().getTargetType()); 

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
			//IDC_BIC
			if(m.isVision360()=="true") {
				ps.setString(8,"O"); 
			}
			else if(m.isVision360()=="false") {
				ps.setString(8,"N"); 
			}
			else {
				ps.setString(8,null);
			}
			//IDT_UTI
			ps.setString(9,userName); 
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


		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMc))
		{

			if(m.getKeywords().size()>0) {
				for (String temp : m.getKeywords()) {

					ps.setInt(1,34); 

					ps.setString(2,temp);
					ps.setInt(3,generatedId);
					ps.setString(4, userName);
					java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
					ps.setTimestamp(5, date);

					ps.executeUpdate();
				}rowAffected=0;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


		/*
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
			ps.setString(9,userName); //IDT_UTI
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
		*/


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
					ps.setString(4, userName);
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
		//String sqlVar = "INSERT INTO oac.T90_VAR (CD_EFS_PUB, CD_EFS_PSE, NO_PSE_MAIL, IDT_PUB, NO_PSE_IDT_CTB_CTU,CD_CHP_LIB_CHP)"
			//	+"VALUES (34, ?, ?, ?, ?,?,?)";
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlVar))
		{
			String clients=m.getT().getClientList();
			System.out.println("Voici les clients: "+clients);
			String[] listeClients =clients.split("\\r?\\n");
			int i=0;
			String client=null;
			System.out.println("taille de la liste:" +listeClients.length);
			
			for(i=0;i<listeClients.length;i++) {
				client=listeClients[i];
				System.out.println(client);
				
				String[] clientChamps = client.split(","); 
				
				System.out.println(clientChamps[0]);
				
					ps.setInt(1,34); 

					ps.setInt(2,01);
					ps.setString(3,"");
					ps.setInt(4, generatedIdPub);
					ps.setInt(5,Integer.parseInt(clientChamps[0])); 

					ps.setString(6,"c_54246");
					ps.setString(7,"PP");
					//Si PP remettre le code
					ps.setInt(8,Integer.parseInt(clientChamps[0]));
					

					ps.executeUpdate();
					
					ps.setInt(1,34); 

					ps.setInt(2,01);
					ps.setString(3,"");
					ps.setInt(4, generatedIdPub);
					ps.setInt(5,Integer.parseInt(clientChamps[1])); 

					ps.setString(6,"c_54246");
					ps.setString(7,"PM");
					//Si PP remettre le code
					ps.setInt(8,Integer.parseInt(clientChamps[1]));
					

					ps.executeUpdate();
				
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

			 			   //AG, AS, PA,PR -- dï¿½pend du fichier ï¿½ l'upload ? 
						    ps.setString(1,temp);
						    //IDT_MES_DWB
				        	ps.setInt(3,generatedId);
				        	//IDT_UTI
				        	ps.setString(4, userName);
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

		//Ajouter les diffï¿½rents clients en aprsant le fichier envoyï¿½
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
				        	ps.setString(4, userName);
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
	 * @return Id du message modifiÃ©
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

			ps.setString(3,m.getLibelle()); //TXT_LIB_MES
			if(m.getType().equals("BULLE_CONSEILLER")) { 
				ps.setString(4,"Votre conseiller vous informe");  //LIB_MES_CNS
			}
			else {
				
				ps.setString(4,""); 
			}
			ps.setInt(5,m.getPriority()); //CD_PRTY_MES
			ps.setInt(6,0); //DUR_VIE_MES
			//ps.setString(7,"thomas"); //IDT_UTI
			java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
			ps.setTimestamp(7,  date ); 
			ps.setInt(8,0); //NB_AFG_MAX 
			
			ps.setString(9,m.getTextMessage()); //TXT_MES_CTU

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
					//Mot clï¿½
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
	 * Get message by mot cle
	 * @param motCle Le mot cle du message
	 * @param type Le type du message
	 * @param page La page actuelle
	 * @param size Les elements max sur la page
	 * @return liste des messages filtrÃ©e
	 * @author YinjieZHAO
	 */
	public List<Message> getMessageByMotCle (String motCle, String type, Integer page, Integer size) {
		int identity=0;
		String containMot = "%"+motCle+"%";
		if (motCle == null)
			containMot = "%%";
		String sqlRangeMessage="SELECT * "
				+ "FROM t90_msg "
				+ "LEFT JOIN t90_mot_cle ON t90_mot_cle.IDT_MES_DWB = t90_msg.IDT_MES_DWB "
				+ "WHERE t90_mot_cle.TXT_CLE LIKE ? AND t90_msg.LIB_TY_MES = ? "
				+ "LIMIT ?,?;";
		ArrayList<Message> mMessageList = new ArrayList<>();
		Message m;
		
		if (page != null && size != null) {
			page = (page-1)*size;
		}
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlRangeMessage))
		{
			ps.setString(1,containMot);
			ps.setString(2, type);
			ps.setInt(3, page);
			ps.setInt(4, size);
			
			ResultSet r = ps.executeQuery();

			while (r.next()) {
				identity = r.getInt(2);
				m = getMessage(identity);
				mMessageList.add(m);
			}

			r.close();
			ps.close();

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return mMessageList;
	}
	
	
	/**
	 * Get Message by Libelle
	 * @param libelle Le libelle du message
	 * @param type Le type du message
	 * @param page La page actuelle
	 * @param size Les elements max
	 * @return liste des messages filtrÃ©e
	 * @author YinjieZHAO
	 */
	public List<Message> getMessageByLibelle (String libelle, String type, Integer page, Integer size){
		int identity=0;
		String containLib = "%"+libelle+"%";
		String sqlRangeMessage="SELECT * "
				+ "FROM t90_msg "
				+ "WHERE t90_msg.TXT_LIB_MES LIKE ? AND t90_msg.LIB_TY_MES = ? "
				+ "LIMIT ?,?;";
		ArrayList<Message> mMessageList = new ArrayList<>();
		Message m;
		
		if (libelle == null)
			containLib = "%%";
		
		if (page != null && size != null) {
			page = (page-1)*size;
		}
		
		System.out.println("In function getMessage by libelle");
		System.out.println("Libelle : " + containLib);
		System.out.println("Type : " + type);
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlRangeMessage))
		{
			ps.setString(1, containLib);
			ps.setString(2, type);
			ps.setInt(3, page);
			ps.setInt(4, size);
			
			System.out.println("In SQL getMessage by libelle");
			
			ResultSet r = ps.executeQuery();

			while (r.next()) {
				identity = r.getInt(2);
				m = getMessage(identity);
				System.out.println("Identity : " + identity);
				mMessageList.add(m);
			}

			r.close();
			ps.close();

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return mMessageList;
	}
	
	
	/**
	 * Get all message by page
	 * @param page la page actuelle
	 * @param size les elements max sur la page
	 * @return l'entitÃ© du message, c'est une liste des messages et un nombre total
	 * @author YinjieZHAO
	 */
	public MessagePageEntity getAllMessageByPage (Integer page, Integer size) {
		MessagePageEntity pageEntity = new MessagePageEntity();
		
		if (page != null && size != null) {
			page = (page-1)*size;
		} else {
			System.out.println("Syntaxe error, MessagePageEntity");
			return null;
		}
		
		List<Message> messages = getRangedMessage(page, size);
		pageEntity.setData(messages);
		Long total = getTotal();
		pageEntity.setTotal(total);
		return pageEntity;
	}

	/**
	 * Get le nombre total du message
	 * @return le nombre total
	 * @author YinjieZHAO
	 */
	public Long getTotal() {
		long total=0;
		String sqlGetTotal="SELECT count(*) "
				+ "FROM t90_msg;";
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlGetTotal))
		{
			ResultSet r = ps.executeQuery();

			if (r.next()) {
				total = r.getLong(1);
			}
			System.out.println("Total : " + total);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return total;
			
	}
	
	/**
	 * Get a range of message
	 * @param range the range
	 * @return list of messages
	 * @author YinjieZHAO
	 */
	@Override
	public ArrayList<Message> getRangedMessage(Integer page, Integer size ) { 
		int identity=0;
		String sqlRangeMessage="SELECT * "
				+ "FROM t90_msg "
				+ "LEFT JOIN t90_pub ON t90_pub.IDT_MES_DWB = t90_msg.IDT_MES_DWB "
				+ "LEFT JOIN t90_cnl ON t90_pub.IDT_PUB = t90_cnl.IDT_PUB "
				+ "ORDER BY t90_cnl.NO_PRTY, t90_msg.CD_PRTY_MES "
				+ "LIMIT ?,?;";

		ArrayList<Message> mMessageList = new ArrayList<>();
		Message m;
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlRangeMessage))
		{
			ps.setInt(1,page);
			ps.setInt(2, size);
			
			ResultSet r = ps.executeQuery();

			while (r.next()) {
				identity = r.getInt(2);
				m = getMessage(identity);
				mMessageList.add(m);
			}

			r.close();
			ps.close();

			return mMessageList;

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return mMessageList;
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
		String sqlGetAll = "SELECT * "
				+ "FROM t90_msg "
				+ "LEFT JOIN t90_pub ON t90_pub.IDT_MES_DWB = t90_msg.IDT_MES_DWB "
				+ "LEFT JOIN t90_cnl ON t90_pub.IDT_PUB = t90_cnl.IDT_PUB "
				+ "ORDER BY t90_cnl.NO_PRTY, t90_msg.CD_PRTY_MES ";

		ArrayList<Message> mMessageList = new ArrayList<>(); 

		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlGetAll))
		{
			ResultSet r = ps.executeQuery();

			while (r.next()) {
				identity = r.getInt(2);
				m = getMessage(identity);
				mMessageList.add(m);
			}

			r.close();

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
		String clients = null;
		ArrayList <String> NO_PSE = new ArrayList<>();

		//Target
		Target t;

		// Elements
		String identity="";
		String type="";
		String libelle="";
		//Pas besoin de récupérer ce champ "votre conseiller vous informe"
		//String textBulle="";
		String ville="";
		Date start=null;
		Date end=null;
		String targetType="";
		String client="";
		String vision360="";

		//Le texte du message
		String textMessage="";

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
		String JOIN_t90_str = "LEFT JOIN t90_str ON t90_pub.IDT_PUB = t90_str.IDT_PUB ";
		String WHERE_IDT_MES_DWB = "WHERE t90_msg.IDT_MES_DWB = ? ";

		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(SELECT
				+ JOIN_t90_pub
				+ JOIN_t90_efs
				+ JOIN_t90_cnl
				+ JOIN_t90_mot_cle
				+ JOIN_t90_var
				+ JOIN_t90_str
				+ WHERE_IDT_MES_DWB))
		{
			ps.setInt(1,id);
			ResultSet r =  ps.executeQuery();
			while(r.next() ) {

				//Identity
				identity = r.getString(2);
				//Type of message
				type = r.getString(3);
				// LibellÃ©
				libelle = r.getString(4);
				// Texte du message !
				textMessage = r.getString(11);
				// Texte pour Message Bulle
				//Pas besoin de récupérer "votre conseiller vous informe"
				//textBulle = r.getString(5);
				// Vision360
				vision360 = r.getString(9);
				// Le type de la cible
				targetType = r.getString(22);
				// La date dÃ©but d'affichage du message
				start = r.getDate(23);
				// La date fin d'affichage du message
				end = r.getDate(24);




				// La valeur temporaire de la prioritÃ©
				tmp = r.getString(6);
				// VÃ©rifier si la valeur temporaire est nulle
				if (tmp != null)
					priority = Integer.parseInt(tmp);

				// VÃ©rifier si la liste entitÃ© contient un Ã©lÃ©ment pareil
				if ( !entities.contains(r.getInt(32)) ) 
					entities.add(r.getInt(32));

				// VÃ©rifier si la liste canal contient un Ã©lÃ©ment pareil
				if ( !canaux.contains(r.getString(35)) ) 
					canaux.add(r.getString(35));

				// La valeur temporaire du canal
				tmp = r.getString(35);

				// VÃ©rifier si le canal est null
				if (tmp != null) {
					// VÃ©rifier si le canal est GAB
					if (tmp.equals("GAB")) {
						tmp = r.getString(36);
						priorityGAB = r.getInt(36);
					}
				}

				tmp = "";

				// VÃ©rifier si la liste des mots clÃ©s contient un Ã©lÃ©ment pareil
				if ( !keywords.contains(r.getString(38)) )
					keywords.add(r.getString(38));

				// VÃ©rifier si le type de la cible est null
				if(r.getString(22) !=null) {

					// VÃ©rifier si le type de la cible est de la liste des clients
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
							client +=","+tmp;
							// Ajouter le PP/PM dans la liste de client
							clients+=client+"\n";
							System.out.println(clients);

						} else if ( !NO_PSE.contains(r.getString(46))){
							// Add client PP
							NO_PSE.add(r.getString(46));
							// La valeur temporaire du Client PP
							tmp = r.getString(49);
							client = tmp + ",";
						}

					} else if (targetType.equals("F")) {
						// FÃ©dÃ©ration
						federation=true;
						agency=false;
					} else {
						// Agence
						federation=false;
						agency=true;
						ville = r.getString(52);

					}

				}
				tmp = "";
			} 

			t = new Target(federation, agency, clients, ville);

			r.close();

			ps.close();

			return new Message(identity, type, vision360, libelle, textMessage, keywords, start, end, entities, canaux, priority, priorityGAB, t );

		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Delete message by id
	 * @param id id du message
	 * @return l'id du message supprimï¿½
	 * @author Abdoul Leadi - Thomas Clisson
	 */
	public int deleteMessage(int id) {


		String sqlPubId ="Select IDT_PUB from t90_pub where IDT_MES_DWB=?";

		String sqlPub ="delete from t90_pub where IDT_MES_DWB=?";
		String sqlMc ="delete from t90_mot_cle where IDT_MES_DWB=?";
		String sqlTy ="delete from t90_ty_cli where IDT_MES_DWB=?";

		String sqlEfs ="delete from t90_efs where IDT_PUB=?";
		String sqlCnl ="delete from t90_cnl where IDT_PUB=?";
		String sqlAprl ="delete from t90_aprl where IDT_PUB=?";
		String sqlCli ="delete from t90_cli where IDT_PUB=?";
		String sqlStr ="delete from t90_str where IDT_PUB=?";

		String sqlMsg ="delete from t90_msg where IDT_MES_DWB=?";

		int idPub=0;

		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlPubId))
		{
			ps.setInt(1,id);
			ResultSet r = ps.executeQuery();

			while (r.next()) {
				idPub = r.getInt(1);
			}
			r.close();
			ps.close();

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Identifiant de la pub = "+ idPub);
		
		
	
		//Delete from aprl
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlAprl))
		{
			//IDT_PUB
			ps.setInt(1,idPub); 
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from cli
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlCli))
		{
			//IDT_PUB
			ps.setInt(1,idPub); 
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from str
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlStr))
		{
			//IDT_PUB
			ps.setInt(1,idPub); 
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}



		ResultSet r;

		//Selectionner l'id de la publication liï¿½e au message
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlPubId))
		{
			ps.setInt(1,id);
			r = ps.executeQuery();

			while (r.next()) {
				idPub = r.getInt(1);
			}
			r.close();
			ps.close();

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		// Delete from Pub
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlPub))
		{
			ps.setInt(1,id); 
			ps.executeUpdate();
			ps.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from msg
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMsg))
		{
			ps.setInt(1,id); //CD_EFS
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from Mot Clï¿½
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMc))
		{
			ps.setInt(1,id); //CD_EFS
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from ty_cli
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlTy))
		{
			//IDT_PUB
			ps.setInt(1,id);
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from efs
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlEfs))
		{
			//IDT_PUB
			ps.setInt(1,idPub); 
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from cnl
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlCnl))
		{
			//IDT_PUB
			ps.setInt(1,idPub); 
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from aprl
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlAprl))
		{
			//IDT_PUB
			ps.setInt(1,idPub); 
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from cli
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlCli))
		{
			//IDT_PUB
			ps.setInt(1,idPub); 
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Delete from str
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlStr))
		{
			//IDT_PUB
			ps.setInt(1,idPub); 
			ps.executeUpdate();
			ps.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// Delete from Pub
				try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlPub))
				{
					ps.setInt(1,id); 
					ps.executeUpdate();
					ps.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				//Delete from msg
				try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMsg))
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
