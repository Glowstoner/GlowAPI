package fr.glowstoner.api.modules;

import fr.glowstoner.api.GlowAPI;

public class Instance extends GlowAPI{
	
	@Override
	public void start() {
		getCommand().registerCommand("end", new End(this));
		getCommand().registerCommand("modules", new Modules());
		getCommand().registerCommand("clear", new Clear());
		getCommand().registerCommand("help", new Help());
		getCommand().registerCommand("helpmodule", new HelpModule(this));
		getCommand().registerCommand("server", new Server());
	}

	@Override
	public void end() {
		
	}
}