package fr.glowstoner.api.modules;

import java.awt.Color;

import fr.glowstoner.api.command.IGlowCommandExecutor;
import fr.glowstoner.api.console.Level;

public class BColor implements IGlowCommandExecutor {
	
	private Instance main;

	public BColor(Instance instance) {
		this.main = instance;
	}

	@Override
	public void execute(String command, String[] args) {
		if(args.length == 0) {
			main.getBaseLogger().log("Usage : /bcolor <COULEUR>", Level.WARNING);
			return;
		}else if(args.length == 1) {
			try {
				String[] colorset = args[0].split("-");
				
				int r = Integer.valueOf(colorset[0]);
				int g = Integer.valueOf(colorset[1]);
				int b = Integer.valueOf(colorset[2]);
				
				main.getConsole().getPane().setBackground(new Color(r, g, b));
				
				main.getBaseLogger().log("La couleur du background a été fixée sur "+r+", "+g+" et "+b, Level.INFO);
			}catch(Exception e) {
				main.getBaseLogger().log("Une erreur lors du chargement de la couleur pour " + args[0] + " !", Level.SEVERE);
			}
		}else {
			main.getBaseLogger().log("Usage : /bcolor <COULEUR>", Level.WARNING);
		}
	}

	@Override
	public String description() {
		return "Change la coleur du background de la console";
	}

}
