package com.github.lbam.dcBot;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class Instance {
	
	String playerId;
	
	public Instance(String id) {
		playerId = id;
	}
	
	public int getRandomChampion() {
		return 0;
	}
	
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String guess = message.getContent();
		
		if(message.getAuthor().isBot() || message.getAuthor().getID() != playerId)
			return;	
		
	}
}
