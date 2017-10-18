package fr.glowstoner.api.basemodules;

import fr.glowstoner.api.command.IGlowCommandExecutor;

public class End implements IGlowCommandExecutor{
	
	private Instance main;

	public End(Instance instance) {
		this.main = instance;
	}

	@Override
	public void execute(String command, String [] args) {
		main.stop();
	}

	@Override
	public String description() {
		return "Ferme la console";
	}	
}
