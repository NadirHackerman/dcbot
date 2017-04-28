package com.github.lbam.dcBot.Database.Models;

public class Champion {
	private int id;
	private String name;
	private String representation;
	private int usedHint;
	private boolean correct = false;
	
	public Champion(int id, String name, String representation) {
		this.id = id;
		this.name = name;
		this.representation = representation;
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
	public void setUsedHint() {
		this.usedHint = 1;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect() {
		this.correct = true;
	}


	
}
