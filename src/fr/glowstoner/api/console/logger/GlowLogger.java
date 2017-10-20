package fr.glowstoner.api.console.logger;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.console.logger.enums.Level;

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
