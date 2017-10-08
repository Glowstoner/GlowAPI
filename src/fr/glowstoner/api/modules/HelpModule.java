package fr.glowstoner.api.modules;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.command.IGlowCommandExecutor;
import fr.glowstoner.api.console.Level;
import fr.glowstoner.api.files.GlowModule;

public class HelpModule implements IGlowCommandExecutor {
	
	private Instance main;

	public HelpModule(Instance main) {
		this.main = main;
	}

	@Override
	public String description() {
		return "Permet de voir l'aide pour tout les modules !";
	}

	@Override
	public void execute(String command, String[] args) {
		if(args.length == 0) {
			for(GlowModule mod : GlowModule.getAllModulesInstance()) {
				GlowAPI.getInstance().getLogger(main).log("Pour le module "+mod.getModuleName()+" : ", Level.INFO);
				
				GlowAPI.getInstance().getLogger(main).log("Nom : "+mod.getModuleName(), Level.INFO);
				GlowAPI.getInstance().getLogger(main).log("Version : "+mod.getModuleVersion(), Level.INFO);
			}
		}else {
			GlowModule sel = null;
			
			for(GlowModule mod : GlowModule.getAllModulesInstance()) {
				if(mod.getModuleName().equalsIgnoreCase(args[0])) {
					sel = mod;
				}
			}
			
			if(sel == null) {
				GlowAPI.getInstance().getLogger(main).log("Aucun module trouvé pour \""+args[0]+"\" !", Level.WARNING);
				
				return;
			}
			
			GlowAPI.getInstance().getLogger(main).log("Pour le module "+sel.getModuleName()+" : ", Level.INFO);
			
			GlowAPI.getInstance().getLogger(main).log("Nom : "+sel.getModuleName(), Level.INFO);
			GlowAPI.getInstance().getLogger(main).log("Version : "+sel.getModuleVersion(), Level.INFO);
		}
	}

}
