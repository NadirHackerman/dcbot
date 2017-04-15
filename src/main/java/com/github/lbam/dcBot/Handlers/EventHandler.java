package com.github.lbam.dcBot.Handlers;

import java.awt.Color;

import com.github.lbam.dcBot.Commands.Callback;
import com.github.lbam.dcBot.Commands.GameReceiver;
import com.github.lbam.dcBot.Interfaces.Command;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class EventHandler {
	
	@EventSubscriber
	public void onReadyEvent(ReadyEvent event){
		System.out.println("Entrou");
	}
	
	@EventSubscriber
	public void GuildCreateEvent(IGuild guild) {
		for(IChannel ch : guild.getChannels()) {
			MessageHandler.sendMessage("Saudações, invocador", "Comandos: \n %dc jogar - inicia um jogo. \n %dc sair - encerra um jogo. \n %dc ajuda - reabre esse painel. \n Para pedir dicas, digite 'dica' enquanto em uma sessão. Mas lembre-se: cada jogador poderá utilizar apenas 3 dicas.", Color.yellow, ch);
		}
	}
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String[] args = message.getContent().split(" ");
		
		if(message.getAuthor().isBot())
			return;
		
		if(args[0].equals("send")) {
			IMessage x = MessageHandler.sendMessage("Oi", "OLá", Color.green, message.getChannel());
			MessageHandler.editChampionMessage(message.getAuthor(), "Hi", x);
		}
		if(args[0].equals("%dc") && args.length > 1) {
			Command cmd = new Callback(new GameReceiver(message), args[1], message.getChannel());
			cmd.execute();
		} 
		else
			return;
		
	}
}
