package com.arkea.oac.model;

import java.util.ArrayList;
import java.util.List;

public class Target {
	
	private String target_type;//permet de controller le type de cible
	private boolean federation;
	private String agency;
	private List<String> client_list = new ArrayList<>();
	
	public Target() {
		super();
	}
	
	
	//Posibilité de générer des champs null, s'ils ne correspondent pas au target_type
	public Target(String target_type, boolean federation, String agency, List<String> client_list) {
		super();
		this.target_type = target_type;
		this.federation = federation;
		this.agency = agency;
		this.client_list = client_list;
	}



	public boolean isFederation() {
		return federation;
	}
	public void setFederation(boolean federation) {
		this.federation = federation;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public List<String> getClient_list() {
		return client_list;
	}
	public void setClient_list(List<String> client_list) {
		this.client_list = client_list;
	}

	public String getTarget_type() {
		return target_type;
	}

	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}

	
}
