package com.github.lbam.dcBot.Commands;

import java.lang.reflect.Method;


import com.github.lbam.dcBot.Handlers.MessageHandler;
import com.github.lbam.dcBot.Interfaces.Command;

import sx.blah.discord.handle.obj.IChannel;

public class Callback implements Command {
	Method targetMethod;
	GameReceiver receiver;
	
	public Callback(GameReceiver rec, String cmd, IChannel ch) {
		try {
			targetMethod = rec.getClass().getDeclaredMethod(cmd);
			receiver = rec;
		} catch (Exception e) {
			MessageHandler.sendInvalidCommand(ch);
		}
	}
	
	public void execute() {
		try {
			targetMethod.invoke(receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
