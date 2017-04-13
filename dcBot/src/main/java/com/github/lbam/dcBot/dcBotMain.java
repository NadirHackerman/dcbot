package com.github.lbam.dcBot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

/**
 * Classe de inicialização
 *
 */
public class dcBotMain{
	public static IDiscordClient Bot;
    
	public static void main(String[] args){
    	Bot = getClient(Token);
    }
    
    public static IDiscordClient getClient(String Token) throws DiscordException{
		return new ClientBuilder().withToken(Token).login();
    }
}
