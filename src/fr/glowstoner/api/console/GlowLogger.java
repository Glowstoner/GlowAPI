package fr.glowstoner.api.console;

import fr.glowstoner.api.GlowAPI;

public class GlowLogger {
	
	private GlowLogSource source;

	public GlowLogger(GlowLogSource source) {
		this.source = source;
	}
	
	public void log(String msg, Level lvl) {
		if(source.getRender() == null) {
			GlowAPI.getInstance().getConsole().log("Erreur Critique", Level.SEVERE);
		}
		
		GlowAPI.getInstance().getConsole().log(source.getRender()+msg, lvl);
	}
}
