package com.github.lbam.dcBot.Database.Models;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class Player {
	String name;
	String playerId;
	String guildId;
	String lang;
	IChannel channel;
	IUser user;
	int progress;
	
	public Player(String guildId, String lang, IChannel channel, IUser user, int progress) {
		this.name = user.getName();
		this.playerId = user.getID();
		this.guildId = guildId;
		this.lang = lang;
		this.channel = channel;
		this.user = user;
		this.progress = progress;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGuildId() {
		return guildId;
	}
	public void setGuildId(String guildId) {
		this.guildId = guildId;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public IChannel getChannel() {
		return channel;
	}
	public void setChannel(IChannel channel) {
		this.channel = channel;
	}
	public IUser getUser() {
		return user;
	}
	public void setUser(IUser user) {
		this.user = user;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
}
