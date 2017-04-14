package com.github.lbam.dcBot.Handlers;

import com.github.lbam.dcBot.Commands.Callback;
import com.github.lbam.dcBot.Commands.GameReceiver;
import com.github.lbam.dcBot.Interfaces.Command;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IMessage;

public class EventHandler {
	
	@EventSubscriber
	public void onReadyEvent(ReadyEvent event){
		System.out.println("Entrou");
	}
	
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String[] args = message.getContent().split(" ");
		
		if(message.getAuthor().isBot())
			return;
		
		if(args[0].equals("%dc") && args.length > 1) {
			Command cmd = new Callback(new GameReceiver(message), args[1]);
			cmd.execute();
		} 
		else
			return;
		
	}
}
