package fr.glowstoner.api.modules;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.command.IGlowCommandExecutor;
import fr.glowstoner.api.console.logger.Level;
import fr.glowstoner.api.files.GlowModule;

public class HelpModule implements IGlowCommandExecutor {

	@Override
	public String description() {
		return "Permet de voir l'aide pour tout les modules !";
	}

	@Override
	public void execute(String command, String[] args) {
		if(args.length == 0) {
			for(GlowModule mod : GlowModule.getAllModulesInstance()) {
				GlowAPI.getInstance().getBaseLogger().log("Pour le module "+mod.getModuleName()+" : ", Level.INFO);
				
				GlowAPI.getInstance().getBaseLogger().log("Nom : "+mod.getModuleName(), Level.INFO);
				GlowAPI.getInstance().getBaseLogger().log("Version : "+mod.getModuleVersion(), Level.INFO);
			}
		}else {
			GlowModule sel = null;
			
			for(GlowModule mod : GlowModule.getAllModulesInstance()) {
				if(mod.getModuleName().equalsIgnoreCase(args[0])) {
					sel = mod;
				}
			}
			
			if(sel == null) {
				GlowAPI.getInstance().getBaseLogger().log("Aucun module trouvé pour \""+args[0]+"\" !", Level.WARNING);
				
				return;
			}
			
			GlowAPI.getInstance().getBaseLogger().log("Pour le module "+sel.getModuleName()+" : ", Level.INFO);
			
			GlowAPI.getInstance().getBaseLogger().log("Nom : "+sel.getModuleName(), Level.INFO);
			GlowAPI.getInstance().getBaseLogger().log("Version : "+sel.getModuleVersion(), Level.INFO);
		}
	}

}
