package com.github.lbam.dcBot.Commands;

import java.awt.Color;

import com.github.lbam.dcBot.BotMain;
import com.github.lbam.dcBot.Instance;
import com.github.lbam.dcBot.Handlers.InstanceHandler;
import com.github.lbam.dcBot.Handlers.MessageHandler;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class GameReceiver {
	
	IUser user;
	String playerId;
	IChannel ch;
	boolean gameExists;
	
	public GameReceiver(IMessage message) {
		user = message.getAuthor();
		playerId = user.getID();
		ch = message.getChannel();
		gameExists = InstanceHandler.instances.containsKey(playerId);
	}
	
	//Checa se o player já está em um jogo, caso contrário, inicia um.
	public void jogar() {
		if (!gameExists){
			Instance instancia = new Instance(user, ch, this);
			InstanceHandler.instances.put(playerId, instancia);
			BotMain.Bot.getDispatcher().registerListener(instancia);
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
			MessageHandler.sendMessage(user.getName() + " - AJUDA", "Lista de comandos: \n %dc jogar - inicia um jogo\n%dc sair - encerra o jogo\n Para pedir dicas, digite 'dica' enquanto numa partida.", Color.pink, ch);
		}else {
			MessageHandler.sendMessage("Antes de pedir ajuda, encerre o jogo atual usando o comando %dc sair", ch);
		}
	}
	
	
}
