package com.arkea.oac.model;

import java.util.ArrayList;
import java.util.List;

public class Target {
	
	private boolean federation = true;
	private boolean agency = false;
	private String clientList;
	private String targetType;//permet de controller le type de cible
	private String villeAgence;
	
	public Target() {
	}
	
	
	//Posibilit� de g�n�rer des champs null, s'ils ne correspondent pas au target_type
	public Target(boolean federation, boolean agency, String clientList, String villeAgence) {
		this.federation = federation;
		this.agency = agency;
		this.clientList = clientList;
		this.targetType = defineTargetType(federation, agency, clientList);
		this.villeAgence = villeAgence;
	}

	public Target(String targetType) {
		this.targetType = targetType;
	}

	public boolean isFederation() {
		return this.federation;
	}
	public void setFederation(boolean federation) {
		this.federation = federation;
	}
	public boolean getAgency() {
		return agency;
	}
	public void setAgency(boolean agency) {
		this.agency = agency;
	}
	public String getClientList() {
		return this.clientList;
	}
	public void setClientList(String clientList) {
		this.clientList = clientList;
	}

	public String getTargetType() {
		return this.targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	
	public String getVilleAgence() {
		return this.villeAgence;
	}
	
	public void setVilleAgence(String villeAgence) {
		this.villeAgence = villeAgence;
	}
	

	public String defineTargetType (boolean federation, boolean agency,String client_list) {
		if (federation)
//			Federation
			return "F"; 
		else if (agency)
//			Agency
			return "S";
		else 
//			Clients
			return "C";
	}
	
}
