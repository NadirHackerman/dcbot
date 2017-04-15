package com.github.lbam.dcBot.Database.Models;

public class Champion {
	private int id;
	private String name;
	private String representation;
	private String dica;
	private int usedHint;
	
	public Champion(int id, String name, String representation, String dica) {
		this.id = id;
		this.name = name;
		this.representation = representation;
		this.dica = dica;
		usedHint = 0;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getRepresentation() {
		return representation;
	}
	public String getDica() {
		return dica;
	}
	public int getUsedHint() {
		return usedHint;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setRepresentation(String representation) {
		this.representation = representation;
	}
	public void setDica(String dica) {
		this.dica = dica;
	}
	public void setUsedHint() {
		this.usedHint = 1;
	}

	
}
