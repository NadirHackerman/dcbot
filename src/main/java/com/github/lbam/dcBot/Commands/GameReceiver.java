package com.github.lbam.dcBot.Commands;

import java.awt.Color;

import com.github.lbam.dcBot.BotMain;
import com.github.lbam.dcBot.Instance;
import com.github.lbam.dcBot.Database.DAO.DaoChampion;
import com.github.lbam.dcBot.Database.DAO.DaoPlayer;
import com.github.lbam.dcBot.Database.DAO.DaoPreferences;
import com.github.lbam.dcBot.Database.Models.Player;
import com.github.lbam.dcBot.Handlers.InstanceHandler;
import com.github.lbam.dcBot.Handlers.MessageHandler;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class GameReceiver {
	
	IUser user;
	String playerId;
	String guildLang;
	IChannel ch;
	boolean gameExists;
	
	public GameReceiver(IUser user, IChannel ch) {
		this.user = user;
		this.ch = ch;
		playerId = user.getID();
		gameExists = InstanceHandler.instances.containsKey(playerId);
		guildLang = DaoPreferences.getLang(ch.getGuild().getID());
	}
	
	public void jogar() {
		if (!gameExists){
			InstanceHandler.instances.put(playerId, null);
			Player player = new Player(ch.getGuild().getID(), guildLang, ch, user, DaoPlayer.getProgress(playerId));
			Instance instancia = new Instance(player);
			InstanceHandler.instances.put(playerId, instancia);
			BotMain.Bot.getDispatcher().registerListener(instancia);
		} else {
			MessageHandler.sendIngameError(ch);
		}
	}
	
	public void pular(){
		if(gameExists){
			InstanceHandler.instances.get(playerId).skipChampion();
		}
	}
	
	public void sair() {
		if(gameExists) {
			BotMain.Bot.getDispatcher().unregisterListener(InstanceHandler.instances.get(playerId));
			InstanceHandler.instances.remove(playerId);
		}
	}
	
	public void ajuda() {
		if(!gameExists) {
			MessageHandler.showHelpPanel(ch);
		}else {
			String lang = DaoPreferences.getLang(ch.getGuild().getID());
			MessageHandler.sendMessage(DaoPreferences.getLocal("ingameHelp", lang).getText(), ch);
		}
	}
	
	public void play(){
		jogar();
	}
	
	public void quit(){
		sair();
	}
	
	public void help(){
		ajuda();
	}
	
	public void skip(){
		pular();
	}
	
	
}
