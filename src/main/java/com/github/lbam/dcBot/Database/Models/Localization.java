package com.github.lbam.dcBot.Database.Models;

public class Localization {
	private String lang;
	private String hash;
	private String text;

	public Localization(String lang, String hash, String text) {
		this.lang = lang;
		this.hash = hash;
		this.text = text;
	}
	
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
