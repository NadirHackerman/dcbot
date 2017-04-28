package com.github.lbam.dcBot.Runnables;

import java.awt.Color;

import com.github.lbam.dcBot.Handlers.MessageHandler;

import sx.blah.discord.handle.obj.IChannel;

public class SelfDestructiveMessage implements Runnable {
	String title, body;
	Color color;
	IChannel channel;
	int time;
	
	public SelfDestructiveMessage(String title, String body, Color color, IChannel ch, int time) {
		this.title = title;
		this.body = body;
		this.color = color;
		this.channel = ch;
		this.time = time;
	}
	
	public void run() {
		MessageHandler.sendDestructiveMessage(title, body, color, channel, time);
	}

}