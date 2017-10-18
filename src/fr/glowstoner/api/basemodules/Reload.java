package fr.glowstoner.api.basemodules;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.command.IGlowCommandExecutor;

public class Reload implements IGlowCommandExecutor {

	@Override
	public void execute(String command, String[] args) {
		GlowAPI.getInstance().reload();
	}

	@Override
	public String description() {
		return "Recrée les instances de l'API";
	}

}
