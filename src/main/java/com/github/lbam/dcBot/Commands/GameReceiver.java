package com.github.lbam.dcBot.Commands;

import java.awt.Color;

import com.github.lbam.dcBot.BotMain;
import com.github.lbam.dcBot.Instance;
import com.github.lbam.dcBot.Database.DAO.DaoChampion;
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
	
	public GameReceiver(IUser user, IChannel ch) {
		this.user = user;
		this.ch = ch;
		playerId = user.getID();
		gameExists = InstanceHandler.instances.containsKey(playerId);
	}
	
	//Checa se o player já está em um jogo, caso contrário, inicia um.
	public void jogar() {
		if (!gameExists){
			DaoChampion db = new DaoChampion();
			int playerProgress = db.getProgress(playerId), maxChampion = db.getMaxChampionId()+1;
			if(playerProgress < maxChampion) {
				MessageHandler.threadedDesctrutiveMessage("Carregando...", "Bem-vindo, invocador! Lembre-se de escrever %dc sair quando terminar!", Color.BLACK, ch, 2000);
				Instance instancia = new Instance(user, ch, db, playerProgress, maxChampion);
				InstanceHandler.instances.put(playerId, instancia);
				BotMain.Bot.getDispatcher().registerListener(instancia);
			}else {
				MessageHandler.sendMessage("Parabéns, " + user.getName() + "!", "Você já completou o jogo, com o total de "+db.getTries(playerId)+" tentativas. Aguarde mais atualizações.", Color.pink, ch);
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
			MessageHandler.sendMessage("AJUDA", "Lista de comandos: \n %dc jogar - inicia um jogo\n%dc sair - encerra o jogo\n Para pedir dicas, digite 'dica' enquanto numa partida.", Color.pink, ch);
		}else {
			MessageHandler.sendMessage("Antes de pedir ajuda, encerre o jogo atual usando o comando %dc sair", ch);
		}
	}
	
	
}
