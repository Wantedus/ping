package com.arkea.oac.model;

import java.util.ArrayList;
import java.util.List;

public class Target {
	
	private boolean federation;
	private String agency;
	private List<String> client_list = new ArrayList<>();
	
	public Target(boolean federation, String agency, List<String> client_list) {
		super();
		this.federation = federation;
		this.agency = agency;
		this.client_list = client_list;
	}
	public Target() {
		super();
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

	
}
