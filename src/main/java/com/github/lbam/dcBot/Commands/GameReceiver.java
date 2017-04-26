package com.github.lbam.dcBot.Commands;

import java.awt.Color;

import com.github.lbam.dcBot.BotMain;
import com.github.lbam.dcBot.Instance;
import com.github.lbam.dcBot.Database.DAO.DaoChampion;
import com.github.lbam.dcBot.Database.DAO.DaoPreferences;
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
	
	//Checa se o player já está em um jogo, caso contrário, inicia um.
	public void jogar() {
		if (!gameExists){
			InstanceHandler.instances.put(playerId, null);
			DaoChampion db = new DaoChampion();
			int playerProgress = db.getProgress(playerId), maxChampion = db.getMaxChampionId()+1;
			if(playerProgress < maxChampion) {
				Instance instancia = new Instance(user, ch, db, playerProgress, maxChampion);
				InstanceHandler.instances.put(playerId, instancia);
				BotMain.Bot.getDispatcher().registerListener(instancia);
			}else {
				MessageHandler.sendMessage(String.format(DaoPreferences.getTitle("completeGame", DaoPreferences.getLang(ch.getGuild().getID())).getText(), user.getID()), String.format(DaoPreferences.getLocal("completeGame", DaoPreferences.getLang(ch.getGuild().getID())).getText(), db.getTries(playerId)), Color.pink, ch);
			}
		}else {
			MessageHandler.sendIngameError(ch);
			}
	}
	
	//Checa se existe um jogo em nome do player, caso exista, exclui.
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
	
	
}
