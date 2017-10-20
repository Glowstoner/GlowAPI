package fr.glowstoner.api.basemodules;

import javax.script.ScriptException;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.console.IGlowConsoleListener;
import fr.glowstoner.api.console.enums.EventResult;
import fr.glowstoner.api.console.logger.enums.Level;
import fr.glowstoner.api.utils.GlowCalc;

public class ConsoleTextHandler implements IGlowConsoleListener {

	@Override
	public EventResult onCommandSending(String rawcommand) {
		if(!rawcommand.startsWith("=")) {
			return EventResult.DO_NOTHING;
		}
		
		try {
			GlowAPI.getInstance().getBaseLogger().log("Le résulat est " + GlowCalc.calc(rawcommand.substring(1)), Level.INFO);
			
			return EventResult.OVERRIDE_COMMAND;
		} catch (ScriptException e) {
			GlowAPI.getInstance().getBaseLogger().log("Invalide !", Level.SEVERE);
			
			return EventResult.SUPPRESS_WARNING;
		}
	}

}
