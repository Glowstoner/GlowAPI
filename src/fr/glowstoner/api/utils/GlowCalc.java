package fr.glowstoner.api.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class GlowCalc {
	
	private static final ScriptEngine se = new ScriptEngineManager().getEngineByName("JavaScript");
	
	public static Object calc(String text) throws ScriptException {
		return se.eval(text);
	}
}
