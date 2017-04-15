package com.github.lbam.dcBot;

import java.awt.Color;

import com.github.lbam.dcBot.Commands.Callback;
import com.github.lbam.dcBot.Commands.GameReceiver;
import com.github.lbam.dcBot.Database.DAO.DaoChampion;
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
	
	public Instance(IUser user, IChannel ch, DaoChampion db, int progress, int maxChampion) {
		this.user = user;
		this.progress = progress;
		maxChampionId = maxChampion;
		playerId = user.getID();
		channel = ch;
		database = db;

		if(progress == 0) {
			MessageHandler.threadedDesctrutiveMessage("Saudações, invocador!", "Seu objetivo é adivinhar qual o personagem do League of Legends o bot quis representar a partir de emojis padrões do Discord.", Color.yellow, channel, 5000);
		}
		
		int championsLeft = maxChampionId - progress;
		MessageHandler.threadedDesctrutiveMessage(user.getName(), database.getTries(playerId) + " tentativas para "+progress+" acertos."+"\n"+championsLeft+" campeões restantes.", Color.cyan, channel, 6500);
		
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
					MessageHandler.threadedDesctrutiveMessage("DICA para o jogador "+user.getName(), actualChampion.getDica(), Color.blue, channel, 8000);
					actualChampion.setUsedHint();
				}else {
					MessageHandler.threadedDesctrutiveMessage("Jogador "+user.getName()+", suas dicas acabaram :(", "Infelizmente, você já utilizou suas 3 dicas.", Color.ORANGE, channel, 5000);
				}
		}else if(guess.equals(actualChampion.getName())) {
			MessageHandler.sendCorrectAnswer(channel, user);
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
		else {
			MessageHandler.sendWrongAnswer(channel, user);
			
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
		MessageHandler.sendMessage("Parabéns," + user.getName() + "!", "Você já completou o jogo, com o total de "+database.getTries(playerId)+" tentativas. Aguarde mais atualizações.", Color.pink, channel);
		new Callback(new GameReceiver(user, channel), "sair", channel).execute();
	}
}
