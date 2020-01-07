package com.arkea.oac.model;

import java.util.ArrayList;
import java.util.List;

public class Target {
	
	private boolean federation = true;
	private boolean agency = false;
	private List<String> client_list = new ArrayList<>();
	private String target_type;//permet de controller le type de cible
	
	public Target() {
	}
	
	
	//Posibilit� de g�n�rer des champs null, s'ils ne correspondent pas au target_type
	public Target(boolean federation, boolean agency, List<String> client_list) {
		this.federation = federation;
		this.agency = agency;
		this.client_list = client_list;
		this.target_type = defineTargetType(federation, agency, client_list);
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
	public List<String> getClient_list() {
		return this.client_list;
	}
	public void setClient_list(List<String> client_list) {
		this.client_list = client_list;
	}

	public String getTarget_type() {
		return this.target_type;
	}

	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}
	

	public String defineTargetType (boolean federation, boolean agency, List<String> client_list) {
		if (federation)
			return "federation";
		else if (agency)
			return "agency";
		else 
			return "clients";
	}
	
}
