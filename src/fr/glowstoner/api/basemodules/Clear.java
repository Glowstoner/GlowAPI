package fr.glowstoner.api.basemodules;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.command.IGlowCommandExecutor;
import fr.glowstoner.api.console.logger.Level;

public class Clear implements IGlowCommandExecutor {

	@Override
	public void execute(String command, String [] args) {
		GlowAPI.getInstance().getConsole().clear();
		GlowAPI.getInstance().getBaseLogger().log("Les logs ont bien été clear !", Level.INFO);
	}

	@Override
	public String description() {
		return "Permet de clear la console !";
	}

}