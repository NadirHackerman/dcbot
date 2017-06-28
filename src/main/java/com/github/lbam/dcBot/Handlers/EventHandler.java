package com.github.lbam.dcBot.Handlers;
import java.awt.Color;
import java.util.ArrayList;

import com.github.lbam.dcBot.Commands.Callback;
import com.github.lbam.dcBot.Commands.GameReceiver;
import com.github.lbam.dcBot.Database.DAO.DaoPreferences;
import com.github.lbam.dcBot.Interfaces.Command;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class EventHandler {
	public static ArrayList<String> changeLogged = new ArrayList<String>();
	@EventSubscriber
	public void onReadyEvent(ReadyEvent event){
		System.out.println("Entrou");
	}
	
	@EventSubscriber
	public void onGuildCreateEvent(GuildCreateEvent event) {
//		IGuild server = event.getGuild();
//		
//		for(IChannel ch : server.getChannels()) {
//			MessageHandler.showHelpPanel(ch);
//		}
	}
	
	public void register(IGuild server){
		String serverRegion = server.getRegion().getName();
		
		if(serverRegion.equals("Brazil")){
			DaoPreferences.createPreferences(server.getID(), "br");
		}else{
			DaoPreferences.createPreferences(server.getID(), "us");
		}
	}
	
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String[] args = message.getContent().split(" ");
		IGuild server = message.getChannel().getGuild();
		
		if(message.getAuthor().isBot())
			return;

		if(args[0].equals(":dc") && args.length > 1) {
			if(!changeLogged.contains(server.getID())){
				MessageHandler.sendMessage("Changelog", "- YOU CAN NOW SEE **PLAYERS STATISTICS** AND SEND **EMOJI SUGGESTIONS** ON OUR NEW WEBSITE: **http://wicbot.tk** \n - Now you won't skip to the same champion you're at. (sorry about that)\n - 3 **new** champions added.", Color.WHITE, message.getChannel());
				changeLogged.add(server.getID());
			}
			
			if(!DaoPreferences.existeRegistro(server.getID())){
				register(server);
			}
			
			Command cmd = new Callback(new GameReceiver(message.getAuthor(),message.getChannel()), args[1], message.getChannel());
			cmd.execute();
		} 
		else
			return;
		
	}
}
