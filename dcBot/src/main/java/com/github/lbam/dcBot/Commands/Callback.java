package com.github.lbam.dcBot.Commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.lbam.dcBot.Instance;
import com.github.lbam.dcBot.Interfaces.Command;

import sx.blah.discord.handle.obj.IMessage;

public class Callback implements Command {
	Method targetMethod;
	GameReceiver receiver;
	
	public Callback(GameReceiver rec, String cmd) {
		try {
			targetMethod = rec.getClass().getMethod(cmd);
		} catch (Exception e) {
			//Comando inválido.
		}
	}
	
	public void execute() {
		try {
			targetMethod.invoke(receiver, null);
		} catch (Exception e) {
			
		}
	}

}
