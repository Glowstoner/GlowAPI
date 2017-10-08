package fr.glowstoner.api.modules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.command.IGlowCommandExecutor;
import fr.glowstoner.api.console.Level;
import fr.glowstoner.api.files.GlowModule;

public class Modules implements IGlowCommandExecutor {

	@Override
	public void execute(String command, String[] args) {
		List<String> modules = new ArrayList<>();
		
		for(File files : GlowAPI.getInstance().getBootLoader().filesloader.getAllModules()) {
			GlowModule mod = new GlowModule(files, GlowAPI.getInstance().getConsole(), GlowAPI.getInstance().getBootLoader());
			
			try {
				if(mod.isGlowModule()) {
					mod.setConfig();
					mod.setName();
					
					modules.add(mod.getModuleName());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(modules.size() == 0) {
			GlowAPI.getInstance().getBaseLogger().log("Il n'y a aucun module !", Level.INFO);
			
			return;
		}
		
		GlowAPI.getInstance().getBaseLogger().log("Il y a "+modules.size()+" module(s) : ", Level.INFO);
		
		for(String modn : modules) {
			GlowAPI.getInstance().getBaseLogger().log("- "+modn, Level.INFO);
		}
	}

	@Override
	public String description() {
		return "Permet de voir tout les modules présents";
	}
}
