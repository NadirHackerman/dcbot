package com.github.lbam.dcBot;

import java.awt.Color;

import com.github.lbam.dcBot.Commands.Callback;
import com.github.lbam.dcBot.Commands.GameReceiver;
import com.github.lbam.dcBot.Database.DAO.DaoChampion;
import com.github.lbam.dcBot.Database.DAO.DaoPreferences;
import com.github.lbam.dcBot.Database.Models.Champion;
import com.github.lbam.dcBot.Handlers.MessageHandler;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Instance {
	
	IUser user;
	String playerId;
	IChannel channel;
	
	Champion actualChampion;
	DaoChampion database;
	int progress, maxChampionId;
	
	IMessage showingMessage;
	String lang;
	
	public Instance(IUser user, IChannel ch, DaoChampion db, int progress, int maxChampion) {
		this.user = user;
		this.progress = progress;
		maxChampionId = maxChampion;
		playerId = user.getID();
		channel = ch;
		database = db;
		
		lang = DaoPreferences.db.getLang(ch.getGuild().getID());
	
		if(progress == 0) {
			String welcomeTitle = DaoPreferences.db.getTitle("welcome", lang).getText();
			String welcomeText = DaoPreferences.db.getLocal("welcome", lang).getText();
			MessageHandler.threadedDesctrutiveMessage(welcomeTitle, welcomeText, Color.yellow, channel, 5000);
		}
		
		int championsLeft = maxChampionId - progress;
		String startText = String.format(DaoPreferences.db.getLocal("start", lang).getText(), database.getTries(playerId), progress, championsLeft);
		MessageHandler.threadedDesctrutiveMessage(user.getName(), startText, Color.cyan, channel, 6500);
		
		showingMessage = MessageHandler.sendMessage("Descubra o campeão", "Moldando respostas...", Color.black, channel);
		showNextChampion();
	}
	
	private void showNextChampion() {
		if(progress == maxChampionId) {
			CompletedGameMessage();
		}else {
			actualChampion = database.getRandomChampion(playerId);
			MessageHandler.editChampionMessage(user, actualChampion.getRepresentation(), showingMessage);
		}
	}
	
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String guess = message.getContent().toLowerCase();
		
		if(message.getAuthor().isBot() || !message.getAuthor().getID().equals(playerId) || guess.startsWith("%dc"))
			return;	
		else if(guess.equals("dica")) {
				if(database.getUsedHints(playerId) < 3) {
					String hintTitle = String.format(DaoPreferences.db.getTitle("hint", lang).getText(),user.getName());
					String hintText = DaoPreferences.db.getLocal(actualChampion.getName(), lang).getText();
					MessageHandler.threadedDesctrutiveMessage(hintTitle, hintText, Color.blue, channel, 8000);
					actualChampion.setUsedHint();
				}else {
					String noHintText = DaoPreferences.db.getLocal("noHint", lang).getText();
					MessageHandler.threadedDesctrutiveMessage("@"+user.getName(), noHintText, Color.ORANGE, channel, 5000);
				}
		}else if(guess.equals(actualChampion.getName())) {
			MessageHandler.sendCorrectAnswer(channel, user, lang);
			progress++;
			
			Runnable r = new Runnable() { 
				public void run() {
					database.registerCorrectAnswer(playerId, actualChampion.getId(), actualChampion.getUsedHint());
					showNextChampion();
				}
			};
			
			Thread t = new Thread(r);
			t.start();
		}
		else if(!guess.equals(actualChampion.getName())) {
			MessageHandler.sendWrongAnswer(channel, user, lang);
			
			Runnable r = new Runnable() {
				public void run() { 
					database.registerIncorrectGuess(playerId, actualChampion.getId(), actualChampion.getUsedHint());
				}
			};
			
			Thread t = new Thread(r);
			t.start();
		}
		
		MessageHandler.deleteMessage(message);
	}
	
	public void CompletedGameMessage() {
		MessageHandler.deleteMessage(showingMessage);
		String completeGameTitle = String.format(DaoPreferences.db.getTitle("completeGame", lang).getText(), user.getName());
		String completeGameText = String.format(DaoPreferences.db.getLocal("completeGame", lang).getText(), database.getTries(playerId));
		MessageHandler.sendMessage(completeGameTitle, completeGameText, Color.pink, channel);
		new Callback(new GameReceiver(user, channel), "sair", channel).execute();
	}
}
