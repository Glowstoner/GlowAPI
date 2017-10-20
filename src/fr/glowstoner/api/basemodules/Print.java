package fr.glowstoner.api.basemodules;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.command.IGlowCommandExecutor;
import fr.glowstoner.api.console.logger.enums.Level;

public class Print implements IGlowCommandExecutor {

	@Override
	public void execute(String command, String[] args) {
		StringBuilder builder = new StringBuilder();
		
		for(String arg : args) {
			builder.append(arg + " ");
		}
		
		GlowAPI.getInstance().getBaseLogger().log(builder.toString(), Level.INFO);
	}

	@Override
	public String description() {
		return "Ecrit un message dans la console";
	}

}
