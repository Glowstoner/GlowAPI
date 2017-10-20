package fr.glowstoner.api.boot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.config.GlowConfig;
import fr.glowstoner.api.console.GlowConsole;
import fr.glowstoner.api.console.logger.enums.Level;
import fr.glowstoner.api.logger.GlowFileLogger;
import fr.glowstoner.api.module.GlowModule;
import fr.glowstoner.api.module.GlowModulesLoader;

public class GlowBoot {
	
	private List<Class<?>> clazzs = new ArrayList<>();
	private static GlowConsole console;
	
	public static boolean status;
	
	private GlowModulesLoader filesloader;
	private GlowConfig config;
	private GlowFileLogger log;
	
	private boolean withConsole;

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
				console.log("La classe n'a pas �t� trouv�e : "+e.getMessage(), Level.SEVERE);
			}

			if (clazz != null) {
				if (clazzs.contains(clazz)) {
					console.log("La classe a d�j� �t� instanci�e !", Level.WARNING);

					return;
				}

				clazzs.add(clazz);
				
				console.log("Classe "+clazz.getCanonicalName()+" ajout�e !", Level.INFO);
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
			console.log("La classe a d�j� �t� instanci�e !", Level.WARNING);

			return;
		}

		clazzs.add(clazz);
		
		console.log("Classe "+clazz.getCanonicalName()+" ajout�e !", Level.INFO);
	}
	
	public void setClassByModule(GlowModule module) {
		setClass(module.getModuleClass());
	}
	
	public List<Class<?>> getAllClasses(){
		return clazzs;
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
			
			GlowAPI.getInstance().getConsole().addConsoleListener(new fr.glowstoner.api.basemodules.ConsoleTextHandler());

			console.logBoot("D�marr� !", Level.INFO);
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

		console.log("Termin� !", Level.INFO);

		GlowAPI.getInstance().getConsole().destroy();
	}
	
	public void globalStart(boolean withconsole) {
		this.withConsole = withconsole;
		
		if(withconsole == true) {
			loadFiles(true);
			
			setClass("fr.glowstoner.api.basemodules.Instance", true);
			
			try {
				startAll(true);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException | InstantiationException e) {
				console.log("Erreur critique lors du lancement de l'API :'(", Level.SEVERE);
				e.printStackTrace();
			}
			
			status = true;
		}else {
			loadFiles(false);
			
			setClass("fr.glowstoner.api.basemodules.Instance", false);
			
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
	
	public GlowConfig getConfig() {
		return this.config;
	}
	
	public GlowModulesLoader getFilesLoader() {
		return this.filesloader;
	}
	
	public GlowFileLogger getLog() {
		return this.log;
	}
	
	public boolean withConsole() {
		return this.withConsole;
	}
	
	public void loadFiles(boolean withconsole) {
		if(withconsole == true) {
			this.log = new GlowFileLogger();
			
			boolean haslogFolder = log.hasFolder();
			
			if(!haslogFolder) {
				log.createFolder();
			}
			
			try {
				log.createLogFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			GlowBoot.console.setLog(this.log);
			GlowBoot.console.initLogStream();
			
			console.log("Lancement global ...", Level.INFO);
			
			console.log("Lancement des logs ...", Level.INFO);
			
			if(!haslogFolder) {
				console.log("Dossier des logs non-trouv� ! Cr�ation ...", Level.WARNING);
			}else {
				console.log("Dossier des logs trouv� !", Level.INFO);
			}
			
			console.log("Proc�dure de cr�ation des logs ...", Level.INFO);
			
			console.log("Fichier de log mis � " + log.getLogFile().getPath() + " !", Level.INFO);
			
			console.log("Lancement des fichiers ...", Level.INFO);
			
			this.filesloader = new GlowModulesLoader(console);
			
			if(!filesloader.hasFolder()) {
				console.log("Impossible de trouver le dossier \"modules\" ! Cr�ation ...", Level.WARNING);
				
				filesloader.createFolder();
				
				console.log("Dossier \"modules\" cr�� !", Level.INFO);
			}
			
			filesloader.setFolderAuto();
			console.log("Dossier \"modules\" trouv� !", Level.INFO);
			
			console.log("Lancement proc�dure de chargement des modules !", Level.INFO);
			filesloader.loadAllModules(this);
			
			console.log("Lancement des configs ...", Level.INFO);
			
			this.config = new GlowConfig();
			
			console.log("V�rification du dossier de configuration ...", Level.INFO);
			
			if(!config.hasFolder()) {
				console.log("Dossier de configuration non-trouv� ! Cr�ation ...", Level.WARNING);
				
				config.createFolder();
			}else {
				console.log("Dossier de configuration trouv� !", Level.INFO);
			}
			
			console.log("V�rification du fichier de configuration ...", Level.INFO);
			
			if(!config.hasConfig()) {
				console.log("Fichier de configuration non-trouv� ! Cr�ation ...", Level.WARNING);
				
				config.createConfig();
			}else {
				console.log("Fichier de configuration trouv� !", Level.INFO);
				config.setConfig();
			}
			
			console.log("Lancement de la proc�dure de chargement de la configuration ...", Level.INFO);
			
			try {
				config.loadConfig();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			config.setGlowServerPass();
			
			console.log("glowserver-pass = "+config.getGlowServerPass(), Level.INFO);
		}else {
			GlowFileLogger log = new GlowFileLogger();
			
			if(!log.hasFolder()) {
				log.createFolder();
			}
			
			try {
				log.createLogFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.filesloader = new GlowModulesLoader(console);
			
			if(!filesloader.hasFolder()) {
				filesloader.createFolder();
			}
			
			filesloader.setFolderAuto();
			
			filesloader.loadAllModules(this);
			
			this.config = new GlowConfig();
			
			if(!config.hasFolder()) {
				config.createFolder();
			}
			
			if(!config.hasConfig()) {
				config.createConfig();
			}else {
				config.setConfig();
			}
			
			try {
				config.loadConfig();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			config.setGlowServerPass();
		}
	}
}
