package com.github.lbam.dcBot.Handlers;

import java.awt.Color;

import com.github.lbam.dcBot.BotMain;
import com.github.lbam.dcBot.Runnables.SelfDestructiveMessage;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;



public class MessageHandler {
	
	static boolean permissionWarning = false;
	
	public static IMessage sendMessage(String msg, IChannel ch) {
		try {
			return ch.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}return null;
	}
	
	public static IMessage sendMessage(String title, String body, Color color, IChannel ch) {
		EmbedObject eb = new EmbedBuilder().withColor(color).withTitle(title).withDesc(body).build();
		try {
			return new MessageBuilder(BotMain.Bot).withChannel(ch).withEmbed(eb).build();
		} catch (Exception e) {
			e.printStackTrace();
		} return null;
	}
	
	
	public static IMessage sendDestructiveMessage(String title, String body, Color color, IChannel ch, int delay) {
		EmbedObject eb = new EmbedBuilder().withColor(color).withTitle(title).withDesc(body).build();
		try {
			IMessage m = new MessageBuilder(BotMain.Bot).withChannel(ch).withEmbed(eb).build();
			Thread.sleep(delay);
			m.delete();
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void sendInvalidCommand(IChannel ch) {
		sendMessage("Comando inválido!", "Digite %dc ajuda para saber mais.", Color.red, ch);
	}
	
	public static void sendIngameError(IChannel ch) {
		sendMessage("Você já está em jogo!", "Digite %dc sair antes de iniciar um novo.", Color.red, ch);
	}
	
	public static void editChampionMessage(IUser player, String representation, IMessage msg) {
		EmbedObject eb = new EmbedBuilder().withColor(Color.LIGHT_GRAY).withDesc(representation).build();
		try {
			msg.edit(player.mention(), eb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendWrongAnswer(IChannel ch, IUser user) {
		threadedDesctrutiveMessage(user.getName(), "Oops! Resposta errada.", Color.red, ch, 3000);
	}
	
	public static void sendCorrectAnswer(IChannel ch, IUser user) {
		threadedDesctrutiveMessage(user.getName(), "Correto!", Color.green, ch, 3000);
	}
	
	public static void threadedDesctrutiveMessage(String title, String body, Color color, IChannel ch, int delay) {
		Thread t = new Thread(new SelfDestructiveMessage(title, body, color, ch, delay));
		t.start();
	}
	
	public static void deleteMessage(IMessage msg) {
		try {
			msg.delete();
		} catch (MissingPermissionsException e) {
			if(permissionWarning == false) {
				MessageHandler.sendMessage("Atenção!", "O bot não possui permissões necessárias no servidor para excluir os palpites. Habilite-as para assim ter uma melhor experiência.", Color.yellow, msg.getChannel());
				permissionWarning = true;
			}
		} catch (RateLimitException e) {
			e.printStackTrace();
		} catch (DiscordException e) {
			e.printStackTrace();
		}
	}
	
	
}
