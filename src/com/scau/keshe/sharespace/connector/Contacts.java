package com.scau.keshe.sharespace.connector;


public class Contacts {

	private String name;
	
	private String portrat;
	
	public Contacts(String name, String img) {
		this.name = name;
		this.portrat = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPortrat() {
		return portrat;
	}

	public void setPortrat(String portrat) {
		this.portrat = portrat;
	}
	
	

}
