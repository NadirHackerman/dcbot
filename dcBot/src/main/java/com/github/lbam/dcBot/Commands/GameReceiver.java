package com.github.lbam.dcBot.Commands;

import com.github.lbam.dcBot.BotMain;
import com.github.lbam.dcBot.Instance;
import com.github.lbam.dcBot.Handlers.InstanceHandler;

import sx.blah.discord.handle.obj.IMessage;

public class GameReceiver {
	String playerId;
	boolean existeJogo;
	
	public GameReceiver(IMessage message) {
		playerId = message.getAuthor().getID();
		existeJogo = InstanceHandler.instances.containsKey(playerId);
	}
	
	//Checa se o player já está em um jogo, caso contrário, inicia um.
	public void jogar() {
		if (!existeJogo){
			Instance instancia = new Instance(playerId);
			InstanceHandler.instances.put(playerId, instancia);
			BotMain.Bot.getDispatcher().registerListener(instancia);
		}else {
			///instancia já existe
		}
	}
	
	//Checa se existe um jogo em nome do player, caso exista, exclui.
	public void sair() {
		if(existeJogo) {
			BotMain.Bot.getDispatcher().unregisterListener(InstanceHandler.instances.get(playerId));
			InstanceHandler.instances.remove(playerId);
		}
	}
	
	public void dica() {
		if(existeJogo){
			
		}
	}
	
	public void ajuda() {
		
	}
	
	
}
