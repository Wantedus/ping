package com.arkea.oac.model;

import java.util.ArrayList;
import java.util.List;

public class Target {
	
	private boolean federation = true;
	private boolean agency = false;
	private List<String> clientList = new ArrayList<>();
	private String targetType;//permet de controller le type de cible
	private String villeAgence;
	
	
	//Posibilit� de g�n�rer des champs null, s'ils ne correspondent pas au target_type
	public Target(boolean federation, boolean agency, List<String> clientList, String villeAgence) {
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
	public List<String> getClientList() {
		return this.clientList;
	}
	public void setClientList(List<String> clientList) {
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
	

	public String defineTargetType (boolean federation, boolean agency, List<String> client_list) {
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
