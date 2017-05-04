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
		IGuild server = event.getGuild();
		
		for(IChannel ch : server.getChannels()) {
			MessageHandler.showHelpPanel(ch);
		}
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
				MessageHandler.threadedDesctrutiveMessage("Changelog", "-Bard fixed\n -Tahm Kench fixed\n -Now you can skip the actual champion by typing ':dc skip' while playing. *(Agora você pode pular um campeão digitando :dc pular enquanto em jogo).*", Color.WHITE, message.getChannel(), 20000);
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
