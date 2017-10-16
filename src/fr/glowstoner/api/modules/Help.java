package fr.glowstoner.api.modules;

import java.lang.reflect.InvocationTargetException;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.command.IGlowCommandExecutor;
import fr.glowstoner.api.console.logger.Level;

public class Help implements IGlowCommandExecutor {

	@Override
	public void execute(String command, String [] args) {
		
		GlowAPI.getInstance().getBaseLogger().log("Voici les commandes disponibles :", Level.INFO);
		
		for(String cmds : GlowAPI.getInstance().getCommand().getCommandsList()) {
			try {
				GlowAPI.getInstance().getBaseLogger().log("- "+cmds+ " : "+GlowAPI.getInstance().getCommand().getDescription(cmds), Level.INFO);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				GlowAPI.getInstance().getBaseLogger().log("Erreur Critique, lors de l'execution de la commande !", Level.SEVERE);
				e.printStackTrace();
			}
		}
	}

	@Override
	public String description() {
		return "Donne la liste des commandes et leurs descriptions";
	}
}
