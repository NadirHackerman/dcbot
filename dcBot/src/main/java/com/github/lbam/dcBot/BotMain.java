package com.github.lbam.dcBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.github.lbam.dcBot.Handlers.EventHandler;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

/**
 * Classe de inicialização
 *
 */
public class BotMain{
	
	public static IDiscordClient Bot;
	
    public static void main(String[] args) throws IOException, DiscordException{
    	Properties prop = new Properties();
    	prop.load(new FileInputStream("config.properties"));

    	Bot = getClient(prop.getProperty("token"));
    	Bot.getDispatcher().registerListener(new EventHandler());
    }
    
    public static IDiscordClient getClient(String Token) throws DiscordException{
		return new ClientBuilder().withToken(Token).login();
    }
}
