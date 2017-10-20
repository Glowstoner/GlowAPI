package fr.glowstoner.api.console;

import fr.glowstoner.api.console.enums.EventResult;

public interface IGlowConsoleListener {
	
	public abstract EventResult onCommandSending(String rawcommand);

}
