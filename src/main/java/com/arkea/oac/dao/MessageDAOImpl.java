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

	//Informations sur la base de données, modifiable dans le fichier de conf
	@Value( "${username}" )  
	private String username;
	@Value( "${mdp}" )  
	private  String mdp;
	@Value( "${url}" )  
	private  String url;
	
	//IDT_UTI prends la valeur userName, vu que nous avons pas accès à  la conexion ni aux utilisateurs, nous remplaçons la valeur
	private String userName="";


	public java.sql.Connection getInstance(){
		
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


	/*
	 * Creer un message 
	 * @param Messsage
	 * @return Id du message cree
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


		String sqlTy = "INSERT INTO t90_ty_cli (CD_EFS, CD_ESA, IDT_MES_DWB, IDT_UTI, TM_STP)"
				+"VALUES (34, ?, ?, ?, ?)";
		
		String sqlVar = "INSERT INTO t90_var (CD_EFS_PUB, CD_EFS_PSE, NO_PSE_MAL, IDT_PUB, NO_PSE,IDT_CTB_CTU,CD_CHP,LIB_CHP)"
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


		//Ajouter dans t90_pub
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
			
			
			ps.setString(8,m.getVision360()); 
			
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


		//Ajouter dans t90_efs
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlEfs))
		{

			if(m.getEntity()!=null) {	
				for (String temp : m.getEntity()) {
					ps.setInt(1,34); //CD_EFS

					ps.setInt(2,generatedIdPub); //CD_EFS_MES
					ps.setString(3,temp ); //IDT_MES_DWB
					ps.executeUpdate();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//Ajouter dans t90_mot_cle
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
		//Ajouter dans t90_cnl
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
		//Ajouter dans t90_var seulement si les cibles sont les clients
		if(m.getT().getTargetType()=="C") {
			try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlVar))
			{
				String clients=m.getT().getClientList();
				clients = clients.replace(" ", "");
				String[] listeClients =clients.split("\\r?\\n");
				
				String client=null;
						
				for(int i=0;i<listeClients.length;i++) {
					client=listeClients[i];
					String[] clientChamps = client.split(","); 
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
		}
		
		
		return  generatedId;
	}

	/**
	 * Mettre a jour message 
	 * @param id du message, Messsage
	 * @return Id du message modifie
	 * @exception exception while compiling SQL
	 * @author ThomasCLISSON
	 */
	public int updateMessage(int id, Message m) { 
		//Fonctionne sauf pour le target)

		//ID du message cree, renvoye a la fin
		int generatedId = 0;
		//ID de la publication cree, utile pour le sql intermediaaire
		int generatedIdPub=0;
		int rowAffected=0;
		ResultSet rs;
		

		String idPub = "SELECT IDT_PUB from t90_pub WHERE IDT_MES_DWB= ?" ;

		String sqlMsg = "UPDATE t90_msg SET CD_EFS = ? ,LIB_TY_MES =?,TXT_LIB_MES=?,"
				+ "LIB_MES_CNS=?,CD_PRTY_MES=?,DUR_VIE_MES=?,DA_MOD=?,NB_AFG_MX=?, TXT_MES_CTU=? WHERE IDT_MES_DWB = ? " ;


		String sqlPub  ="UPDATE t90_pub SET DA_DBT_AFG = ? ,DA_FIN_AFG =?  WHERE IDT_MES_DWB = ? ";

		String sqlMc = "INSERT INTO t90_mot_cle (CD_EFS,TXT_CLE,IDT_MES_DWB,IDT_UTI,TM_STP) values (?,?,?,?,?)  ";
		String sqlMcD = "DELETE from t90_mot_cle WHERE IDT_MES_DWB = ? ";		

	
		String sqlEfsD = "DELETE from t90_efs WHERE IDT_PUB = ? ";		
		String sqlEfs = "INSERT into t90_efs (CD_EFS_PUB, IDT_PUB,CD_EFS) values (?,?,?)  ";	
		
		String sqlCnlD = "DELETE from t90_cnl WHERE IDT_PUB = ? ";		
		String sqlCnl = "INSERT into t90_cnl (CD_EFS_PUB, IDT_PUB,CD_CNL,NO_PRTY) values (?,?,?,?)  ";	
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(idPub))
		{
			ps.setInt(1,id);
			ResultSet r =  ps.executeQuery();
			while(r.next() ) {
				generatedIdPub= r.getInt(1);
				
				
			}
			r.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMsg, Statement.RETURN_GENERATED_KEYS))
		{
			//CD_EFS
			ps.setInt(1,34); 
			//LIB_TY_MES
			ps.setString(2,m.getType()); 
			//TXT_LIB_MES
			ps.setString(3,m.getLibelle()); 
			if(m.getType().equals("BULLE_CONSEILLER")) { 
				//LIB_MES_CNS
				ps.setString(4,"Votre conseiller vous informe");  
			}
			else {
				
				ps.setString(4,""); 
			}
			//CD_PRTY_MES
			ps.setInt(5,m.getPriority()); 
			//DUR_VIE_MES
			ps.setInt(6,0); 
			ps.setString(7,userName); //IDT_UTI
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


		//update t90_pub
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
			//TXT_LIB_MES
			ps.setInt(3,id); 
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
		//update t90_mot_cle
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
			
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMc))
			{	

				for(String k : m.getKeywords()) {
					ps.setInt(1,  34); 
					//Mot cles
					ps.setString(2,k);
					//TXT_LIB_MES
					ps.setInt(3,id);
					ps.setString(4,userName);
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

		
		//DELETE  si l'update comprend 0 entity
		
			try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlEfsD))
			{	
				
				ps.setInt(1, generatedIdPub);
				ps.executeUpdate();
				ps.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
	
			try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlEfs))
			{	


				for(String k : m.getEntity()) {
					ps.setInt(1,  34); 
					//Mot cles
					ps.setInt(2,generatedIdPub);
					//TXT_LIB_MES
					ps.setString(3,k);
					
					ps.executeUpdate();
				}

				ps.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		
		 
			try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlCnlD))
			{	
				
				ps.setInt(1, generatedIdPub);
				ps.executeUpdate();
				ps.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlCnl))
			{	
				for(String k : m.getCanals()) {
					ps.setInt(1,  34); 
					//Mot cles
					ps.setInt(2,generatedIdPub);
					//TXT_LIB_MES
					ps.setString(3,k);
					if(k.equals("GAB")) {
						ps.setInt(4,m.getPriorityGAB());
					}
					else {
						ps.setInt(4,0);
					}
						
					
					
					ps.executeUpdate();
				}

				ps.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		
		
		return  generatedId;

	}
	
	
	/**
	 * Get message by mot cle
	 * @param motCle Le mot cle du message
	 * @param type Le type du message
	 * @param page La page actuelle
	 * @param size Les elements max sur la page
	 * @return liste des messages filtres
	 * @author YinjieZHAO
	 */
	public MessagePageEntity getMessageByMotCle (String motCle, String type, Integer page, Integer size) {
		int identity=0;
		long total=0;
		String containMot = "%"+motCle+"%";
		
		String sqlMessage = "FROM t90_msg "
				+ "LEFT JOIN t90_mot_cle ON t90_mot_cle.IDT_MES_DWB = t90_msg.IDT_MES_DWB "
				+ "WHERE t90_mot_cle.TXT_CLE LIKE ? AND t90_msg.LIB_TY_MES = ? ";
		
		String sqlCount = "SELECT count(*) "+ sqlMessage;
		
		String sqlRangeMessage="SELECT * "
				+ sqlMessage
				+ "LIMIT ?,?;";
		
		ArrayList<Message> mMessageList = new ArrayList<>();
		Message m;
		MessagePageEntity mpe = new MessagePageEntity();
		
		if (motCle == null)
			containMot = "%%";
		
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
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlCount))
		{
			ps.setString(1,containMot);
			ps.setString(2, type);
			
			ResultSet r = ps.executeQuery();

			while (r.next()) {
				total = r.getLong(1);
			}

			r.close();
			ps.close();

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		mpe.setData(mMessageList);
		mpe.setTotal(total);
		
		return mpe;
	}
	
	
	/**
	 * Get Message by Libelle
	 * @param libelle Le libelle du message
	 * @param type Le type du message
	 * @param page La page actuelle
	 * @param size Les elements max
	 * @return liste des messages filtrÃƒÂ©e
	 * @author YinjieZHAO
	 */
	public MessagePageEntity getMessageByLibelle (String libelle, String type, Integer page, Integer size){
		int identity=0;
		long total=0;
		String containLib = "%"+libelle+"%";
		
		String sqlMessage = "FROM t90_msg "
				+ "WHERE t90_msg.TXT_LIB_MES LIKE ? AND t90_msg.LIB_TY_MES = ? ";
		
		String sqlRangeMessage="SELECT * "
				+ sqlMessage
				+ "LIMIT ?,?;";
		
		String sqlCount = "SELECT count(*) " + sqlMessage;
		
		ArrayList<Message> mMessageList = new ArrayList<>();
		Message m;
		MessagePageEntity mpe = new MessagePageEntity();
		
		if (libelle == null)
			containLib = "%%";
		
		if (page != null && size != null) {
			page = (page-1)*size;
		}

		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlRangeMessage))
		{
			ps.setString(1, containLib);
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
		
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlCount))
		{
			ps.setString(1,containLib);
			ps.setString(2, type);
			
			ResultSet r = ps.executeQuery();

			while (r.next()) {
				total = r.getLong(1);
			}

			r.close();
			ps.close();

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		mpe.setData(mMessageList);
		mpe.setTotal(total);
		
		return mpe;
	}
	
	
	/**
	 * Get all message by page
	 * @param page la page actuelle
	 * @param size les elements max sur la page
	 * @return l'entite du message, c'est une liste des messages et un nombre total
	 * @author YinjieZHAO
	 */
	public MessagePageEntity getAllMessageByPage (Integer page, Integer size) {
		MessagePageEntity pageEntity = new MessagePageEntity();
		
		if (page != null && size != null) {
			page = (page-1)*size;
		} else {
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
	 * @param id du message
	 * @return le resultat du message
	 * @author YinjieZHAO, Thomas Clisson
	 */
	@Override
	public Message getMessage(int id) {
		//In the target section
		boolean federation=true;
		boolean agency=true;

		// Lists
		ArrayList <String> canaux = new ArrayList<>();
		List<String> entities = new ArrayList<>();
		ArrayList <String> keywords = new ArrayList<>();
		String clients = "";
		ArrayList <String> NO_PSE = new ArrayList<>();

		//Target
		Target target;

		// Elements
		String identity="";
		String type="";
		String libelle="";
		//Pas besoin de recuperer ce champ "votre conseiller vous informe"
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
				// Libelle
				libelle = r.getString(4);
				// Texte du message !
				textMessage = r.getString(11);
				// Texte pour Message Bulle
				//Pas besoin de recuperer"votre conseiller vous informe"
				//textBulle = r.getString(5);
				// Vision360
				vision360 = r.getString(25);
				// Le type de la cible
				targetType = r.getString(22);
				// La date debut d'affichage du message
				start = r.getDate(23);
				
				// La date fin d'affichage du message
				end = r.getDate(24);


				// La valeur temporaire de la priorite
				tmp = r.getString(6);
				// Verifier si la valeur temporaire est nulle
				if (tmp != null)
					priority = Integer.parseInt(tmp);

				// verifier si la liste entites contient un une valeur identique
				if ( !entities.contains(r.getString(32)) ) 
					entities.add(r.getString(32));

				// Verifier si la liste canal contient un une valeur identique
				
				if ( !canaux.contains(r.getString(35)) ) 
					canaux.add(r.getString(35));

				// La valeur temporaire du canal
				tmp = r.getString(35);

				// Vevifier si le canal est null
				if (tmp != null) {
					// verifier si le canal est GAB
					if (tmp.equals("GAB")) {
						tmp = r.getString(36);
						priorityGAB = r.getInt(36);
					}
				}

				tmp = "";

				// Verifier si la liste des mots cles contient un renregistrement identique
				if ( !keywords.contains(r.getString(38)) )
					keywords.add(r.getString(38));

				// Verifier si le type de la cible est null
				if(r.getString(22) !=null) {

					// Vefirifer si le type de la cible est de la liste des clients
					if (targetType.equals("C")) {
						// Mettre la federation en false
						federation=false;
						// Mettre l'agence en false
						agency=false;
					
						// Add list of clients
						if ( NO_PSE.contains(r.getString(46)) && !tmp.equals(r.getString(49))) {
							

						} else if ( !NO_PSE.contains(r.getString(46))){
							// Add client PP
							NO_PSE.add(r.getString(46));
							
							// La valeur temporaire du Client PP
							tmp = r.getString(46);
							client = tmp + ",";
						
							clients+=client;
							client="";
							
						}
						
					} else if (targetType.equals("F")) {
						// FÃƒÂ©dÃƒÂ©ration
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

			target = new Target(federation, agency, clients, ville);

			r.close();

			ps.close();
			
			return new Message(identity, type, vision360, libelle, textMessage, keywords, start, end, entities, canaux, priority, priorityGAB, target );

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
	 * @return l'id du message supprime
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

		//Selectionner l'id de la publication liee au message
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
		//Delete from Mot clé
		try(java.sql.PreparedStatement ps = getInstance().prepareStatement(sqlMc))
		{
			//CD_EFS
			ps.setInt(1,id); 
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
