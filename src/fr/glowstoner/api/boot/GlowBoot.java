package fr.glowstoner.api.boot;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.console.GlowConsole;
import fr.glowstoner.api.console.Level;
import fr.glowstoner.api.files.GlowFilesLoader;
import fr.glowstoner.api.files.GlowModule;

public class GlowBoot {
	
	private List<Class<?>> clazzs = new ArrayList<>();
	private static GlowConsole console;
	public static boolean status;
	
	public GlowFilesLoader filesloader;

	public GlowBoot() {
		return;
	}
	
	public void setClass(String strclass, boolean withconsole) {
		if(withconsole == true) {
			Class<?> clazz = null;
			
			console.log("Ajout de la classe "+strclass+" ...", Level.INFO);

			try {
				clazz = Class.forName(strclass);
			} catch (ClassNotFoundException e) {
				console.log("La classe n'a pas été trouvée : "+e.getMessage(), Level.SEVERE);
			}

			if (clazz != null) {
				if (clazzs.contains(clazz)) {
					console.log("La classe a déjà été instanciée !", Level.WARNING);

					return;
				}

				clazzs.add(clazz);
				
				console.log("Classe "+clazz.getCanonicalName()+" ajoutée !", Level.INFO);
			} else {
				console.log("La classe est nulle !", Level.SEVERE);
			}
		}else {
			Class<?> clazz = null;

			try {
				clazz = Class.forName(strclass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			if (clazz != null) {
				if (clazzs.contains(clazz)) {
					return;
				}

				clazzs.add(clazz);
			}
		}
	}
	
	public void setClass(Class<?> clazz) {
		console.log("Ajout de la classe "+clazz.getName()+" ...", Level.INFO);

		if (clazzs.contains(clazz)) {
			console.log("La classe a déjà été instanciée !", Level.WARNING);

			return;
		}

		clazzs.add(clazz);
		
		console.log("Classe "+clazz.getCanonicalName()+" ajoutée !", Level.INFO);
	}
	
	public void setClassByModule(GlowModule module) {
		setClass(module.getModuleClass());
	}
	
	public void startAll(boolean withconsole) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
		NoSuchMethodException, SecurityException, InstantiationException {
		
		if(withconsole == true) {
			console.log("Lancement classes ...", Level.INFO);
			
			for (Class<?> clazz : clazzs) {
				console.logBoot("Lancement de la class "+clazz.getName(), Level.INFO);
				
				Object o = clazz.newInstance();

				clazz.getDeclaredMethod("start").invoke(o);
			}

			console.logBoot("Démarré !", Level.INFO);
		}else {
			for (Class<?> clazz : clazzs) {		
				Object o = clazz.newInstance();

				clazz.getDeclaredMethod("start").invoke(o);
			}
		}
	}
	
	public void endAll() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, NoSuchMethodException, SecurityException, InterruptedException {

		console.log("Ending ...", Level.INFO);

		status = false;

		for (Class<?> clazz : clazzs) {
			Object o = clazz.newInstance();

			clazz.getDeclaredMethod("end").invoke(o);
		}

		console.log("Terminé !", Level.INFO);

		GlowAPI.getInstance().getConsole().destroy();
	}
	
	public void globalStart(boolean withconsole) {
		if(withconsole == true) {
			console.log("Lancement Global ...", Level.INFO);
			
			loadFiles(true);
			
			setClass("fr.glowstoner.api.modules.Instance", true);
			
			try {
				startAll(true);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException | InstantiationException e) {
				console.log("Erreur critique lors du lancement des files :'(", Level.SEVERE);
				e.printStackTrace();
			}
			
			status = true;
		}else {
			loadFiles(false);
			
			setClass("fr.glowstoner.api.modules.Instance", false);
			
			try {
				startAll(false);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException | InstantiationException e) {
				e.printStackTrace();
			}
			
			status = true;
		}
	}
	
	public void initConsole() {
		console = new GlowConsole();
		console.genConsole();
	}
	
	public GlowConsole getConsole() {
		return console;
	}
	
	public GlowBoot getBootLoader() {
		return this;
	}
	
	public void loadFiles(boolean withconsole) {
		if(withconsole == true) {
			console.log("Lancement des fichiers ...", Level.INFO);
			
			this.filesloader = new GlowFilesLoader(console);
			
			if(!filesloader.hasFolder()) {
				console.log("Impossible de trouver le dossier \"modules\" ! Création ...", Level.WARNING);
				
				filesloader.createFolder();
				
				console.log("Dossier \"modules\" créé !", Level.INFO);
			}
			
			filesloader.setFolderAuto();
			console.log("Dossier \"modules\" trouvé !", Level.INFO);
			
			console.log("Lancement procédure de chargement des modules !", Level.INFO);
			filesloader.loadAllModules(this);
		}else {
			this.filesloader = new GlowFilesLoader(console);
			
			if(!filesloader.hasFolder()) {
				filesloader.createFolder();
			}
			
			filesloader.setFolderAuto();
			
			filesloader.loadAllModules(this);
		}
	}
	
	public void restart() {
		
	}
}
