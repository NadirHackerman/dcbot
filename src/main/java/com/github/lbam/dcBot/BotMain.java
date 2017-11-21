package com.github.lbam.dcBot;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
        Bot = getClient(System.getenv("TOKEN"));
        Bot.getDispatcher().registerListener(new EventHandler());

        ServerSocket myServerSocket = new ServerSocket(43594);
        while (true) {
            Socket skt = myServerSocket.accept();
        }
    }
    
    public static IDiscordClient getClient(String Token) throws DiscordException{
		return new ClientBuilder().withToken(Token).login();
    }
}
