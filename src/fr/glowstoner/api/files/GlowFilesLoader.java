package fr.glowstoner.api.files;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.glowstoner.api.boot.GlowBoot;
import fr.glowstoner.api.console.GlowConsole;
import fr.glowstoner.api.console.logger.Level;

public class GlowFilesLoader {
	
	private File folder;
	private GlowConsole console;

	public GlowFilesLoader(GlowConsole console) {
		this.console = console;
	}
	
	public boolean hasFolder() {
		Path path = null;
		try {
			path = Paths.get(ClassLoader.getSystemResource("").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		for(File file : path.toFile().listFiles()) {
			if(file.isDirectory()) {
				if(file.getName().equals("modules")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void createFolder() {
		if(!hasFolder()) {
			Path path = null;
			
			try {
				path = Paths.get(ClassLoader.getSystemResource("").toURI());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			File folder = new File(path.toString() + "\\modules\\");
			folder.mkdir();
			
			setFolder(folder);
		}
	}
	
	public void setFolder(File folder) {
		this.folder = folder;
	}
	
	public void loadAllModules(GlowBoot boot) {
		for(File mod : getAllModules()) {
			GlowModule module = new GlowModule(mod, console, boot);
			
			try {
				console.log("Chargement du module "+module.getFileModule().getName() +" ...", Level.INFO);
				console.log("- GlowModule -> "+module.isGlowModule(), Level.INFO);
				
				if(!module.isGlowModule()) {
					return;
				}
				
				module.setConfig();
				
				try {
					module.setMain();
					
					module.setName();
					module.setVersion();
					
					module.load();
				} catch (ClassNotFoundException e) {
					console.log("Erreur lors du chargement d'un module ! (setMain()) module -> "+mod.getName(), Level.SEVERE);
				}
			} catch (IOException e) {
				console.log("Erreur lors du chargement du module "+ module.getFileModule().getName()+e.getMessage(), Level.SEVERE);
			}
		}
	}
	
	public void setFolderAuto() {
		Path path = null;
		try {
			path = Paths.get(ClassLoader.getSystemResource("").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		for(File file : path.toFile().listFiles()) {
			if(file.isDirectory()) {
				if(file.getName().equals("modules")) {
					setFolder(file);
				}
			}
		}
	}
	
	public File getFolder() {
		return folder;
	}
	
	public File[] getAllModules() {
		File file = new File(folder.getPath());
		
		return file.listFiles();
	}
}