package com.github.lbam.dcBot;

import java.awt.Color;

import com.github.lbam.dcBot.Commands.Callback;
import com.github.lbam.dcBot.Commands.GameReceiver;
import com.github.lbam.dcBot.Database.DAO.DaoChampion;
import com.github.lbam.dcBot.Database.DAO.DaoPlayer;
import com.github.lbam.dcBot.Database.DAO.DaoPreferences;
import com.github.lbam.dcBot.Database.Models.Champion;
import com.github.lbam.dcBot.Database.Models.Player;
import com.github.lbam.dcBot.Handlers.MessageHandler;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.MissingPermissionsException;

public class Instance {
	Player player;
	Champion actualChampion;
	
	String lang;
	DaoChampion database;
	int progress, maxChampion;
	
	boolean showPermissionWarning = false;
	IMessage showingMessage;
	
	public Instance(Player player) {
		this.player = player;
		lang = player.getLang();
		progress = player.getProgress();
		
		database = new DaoChampion();
		maxChampion = database.getMaxChampion();
		
		if(progress == 0) {
			String welcomeTitle = DaoPreferences.getTitle("welcome", lang).getText();
			String welcomeText = DaoPreferences.getLocal("welcome", lang).getText();
			MessageHandler.threadedDesctrutiveMessage(welcomeTitle, welcomeText, Color.yellow, player.getChannel(), 5000);
		}
		
		showStartText();
		
		showingMessage = MessageHandler.sendMessage("Descubra o campeão", "Moldando respostas...", Color.black, player.getChannel());
		showNextChampion();
	}
	
	private void showStartText() {
		int championsLeft = maxChampion - progress;
		int tries = DaoPlayer.getTries(player.getPlayerId());
		String start = DaoPreferences.getLocal("start", lang).getText();
		String startText = String.format(start, tries, progress, championsLeft);
		MessageHandler.threadedDesctrutiveMessage(player.getName(), startText, Color.cyan, player.getChannel(), 6500);
	}

	private void showNextChampion() {
		if(progress == maxChampion) {
			CompletedGameMessage();
		}else {
			actualChampion = database.getRandomChampion(player.getPlayerId());
			MessageHandler.editChampionMessage(player.getUser(), actualChampion.getRepresentation(), showingMessage);
		}
	}
	
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String guess = message.getContent().toLowerCase();
		
		if(message.getAuthor().isBot() || !message.getAuthor().getID().equals(player.getPlayerId()) || guess.startsWith(":dc") || !message.getChannel().equals(player.getChannel()))
			return;	
		else if(guess.equals("dica")) {
				if(database.getUsedHints(player.getPlayerId()) < 3) {
					new Thread(() -> {useHint();}).start();
				}else {
					String noHintText = DaoPreferences.getLocal("noHint", lang).getText();
					MessageHandler.threadedDesctrutiveMessage("@"+player.getName(), noHintText, Color.ORANGE, player.getChannel(), 5000);
				}
		}else if(guess.equals(actualChampion.getName())) {
			progress++;
			MessageHandler.sendCorrectAnswer(player.getChannel(), player.getUser(), lang);
			database.registerCorrectAnswer(player.getPlayerId(), actualChampion.getId(), actualChampion.getUsedHint()); 
			showNextChampion();
		}
		else if(!guess.equals(actualChampion.getName())) {
			MessageHandler.sendWrongAnswer(player.getChannel(), player.getUser(), lang);
			database.registerIncorrectGuess(player.getPlayerId(), actualChampion.getId(), actualChampion.getUsedHint());
		}
		

		new Thread( () -> {
			try {
				MessageHandler.deleteMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
		}}).start();
	}
	
	public void useHint(){
		String hintTitle = String.format(DaoPreferences.getTitle("hint", lang).getText(),player.getName());
		String hintText = DaoPreferences.getLocal(actualChampion.getName(), lang).getText();
		MessageHandler.threadedDesctrutiveMessage(hintTitle, hintText, Color.blue, player.getChannel(), 8000);
		actualChampion.setUsedHint();
	}
	
	public void CompletedGameMessage() {
		try {
			MessageHandler.deleteMessage(showingMessage);
		} catch (MissingPermissionsException e) {
			e.printStackTrace();
		}
		String completeGameTitle = String.format(DaoPreferences.getTitle("completeGame", lang).getText(), player.getName());
		String completeGameText = String.format(DaoPreferences.getLocal("completeGame", lang).getText(), DaoPlayer.getTries(player.getPlayerId()));
		MessageHandler.sendMessage(completeGameTitle, completeGameText, Color.pink, player.getChannel());
		new Callback(new GameReceiver(player.getUser(), player.getChannel()), "sair", player.getChannel()).execute();
	}
}
