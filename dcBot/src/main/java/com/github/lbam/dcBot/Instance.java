package com.github.lbam.dcBot;

import java.awt.Color;

import com.github.lbam.dcBot.Commands.GameReceiver;
import com.github.lbam.dcBot.Database.DAO.DaoChampion;
import com.github.lbam.dcBot.Database.Models.Champion;
import com.github.lbam.dcBot.Handlers.MessageHandler;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class Instance {
	
	IUser user;
	String playerId;
	IChannel channel;
	GameReceiver gameReceiver;
	
	Champion actualChampion;
	DaoChampion database;
	boolean permissionWarning = false;
	
	IMessage showingMessage;
	
	public Instance(IUser user, IChannel ch, GameReceiver gr) {
		this.user = user;
		playerId = user.getID();
		channel = ch;
		database = new DaoChampion();
		gr = gameReceiver;
		
		if(database.getProgress(playerId) == database.getMaxChampionId()+1) {
			MessageHandler.sendMessage("Parabéns!", "Você já completou o jogo, com o total de "+database.getTries(playerId)+" tentativas", Color.pink, channel);
			gameReceiver.sair();
		}
		
		int championsLeft = database.getMaxChampionId() - database.getProgress(playerId);
		showingMessage = MessageHandler.sendMessage(user.getName(), database.getTries(playerId) + " tentativas para "+database.getProgress(playerId)+" acertos."+"\n"+championsLeft+" campeões restantes.", Color.DARK_GRAY, channel);
		showNextChampion();
	}
	
	private void showNextChampion() {
		actualChampion = database.getRandomChampion(playerId);
		database.registerChampion(playerId, actualChampion.getId());
		MessageHandler.editChampionMessage(user, actualChampion.getRepresentation(), showingMessage);
	}
	
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String guess = message.getContent().toLowerCase();
		
		if(message.getAuthor().isBot() || !message.getAuthor().getID().equals(playerId) || guess.startsWith("%dc"))
			return;	
		else if(guess.equals("dica")) {
				if(database.getUsedHints(playerId) < 3) {
					MessageHandler.threadedDesctrutiveMessage("DICA", actualChampion.getDica(), Color.blue, channel, 8000);
					database.useHint(playerId, actualChampion.getId());
				}else {
					MessageHandler.threadedDesctrutiveMessage("Suas dicas acabaram :(", "Infelizmente, você já utilizou suas 3 dicas.", Color.ORANGE, channel, 5000);
				}
		}else if(guess.equals(actualChampion.getName())) {
			MessageHandler.sendCorrectAnswer(channel, user);
			database.registerCorrectAnswer(playerId, actualChampion.getId());
			showNextChampion();
		}
		else {
			MessageHandler.sendWrongAnswer(channel, user);
		}
		
		try {
			message.delete();
		} catch (MissingPermissionsException e) {
			if(permissionWarning == false) {
				MessageHandler.sendMessage("Atenção!", "O bot não possui permissões necessárias no servidor para excluir os palpites. Habilite-as para assim ter uma melhor experiência.", Color.yellow, channel);
				permissionWarning = true;
			}
		} catch (RateLimitException e) {
			e.printStackTrace();
		} catch (DiscordException e) {
			e.printStackTrace();
		}
	}
}
