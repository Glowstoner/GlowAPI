package fr.glowstoner.api.modules;

import java.io.IOException;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.command.IGlowCommandExecutor;
import fr.glowstoner.api.console.Level;
import fr.glowstoner.api.server.GlowServer;

public class Server implements IGlowCommandExecutor {

	@Override
	public void execute(String command, String[] args) {
		
		if(!(args.length == 0 || args.length > 1)) {
			try {
				 GlowAPI.getInstance().setServer(new GlowServer(Integer.valueOf(args[0])));
				
				 GlowServer s = GlowAPI.getInstance().getServer();
				 
				 s.openWindow();
				 s.start();
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}else {
			GlowAPI.getInstance().getBaseLogger().log("Usage : /server <port>", Level.INFO);
			
			return;
		}
	}

	@Override
	public String description() {
		return "Démarre le serveur !";
	}

}
