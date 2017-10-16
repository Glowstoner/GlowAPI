package fr.glowstoner.api.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.yaml.snakeyaml.Yaml;

import fr.glowstoner.api.boot.GlowBoot;
import fr.glowstoner.api.console.GlowConsole;
import fr.glowstoner.api.console.logger.Level;

public class GlowModule {
	
	private static List<GlowModule> modules = new ArrayList<>();
	
	private File module;
	private Class<?> main;
	private Map<String, Object> config;
	private String version, name;
	private GlowConsole console;
	private GlowBoot boot;

	public GlowModule(File module, GlowConsole console, GlowBoot boot) {
		this.module = module;
		this.console = console;
		this.boot = boot;
		
	}
	
	public GlowModule(Class<?> main, String version, GlowConsole console, GlowBoot boot) {
		this.main = main;
		this.console = console;
		this.boot = boot;
	}
	
	public boolean isGlowModule() throws ZipException, IOException {
		if(!module.getName().endsWith(".jar")) {
			return false;
		}

		ZipFile zip = new ZipFile(module);
		
		Enumeration<? extends ZipEntry> entries = zip.entries();
		
		if(entries != null) {
			while(entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				
				if(entry.getName().equals("glowmodule.yml")) {
					InputStream inputStream = zip.getInputStream(entry);
					
					@SuppressWarnings("unchecked")
					Map<String, Object> config = 
							new HashMap<String, Object>((Map<? extends String, ? extends Object>) new Yaml().load(inputStream));
					
					if(config.containsKey("main")) {
						if(config.get("main") != null) {
							inputStream.close();
							
							zip.close();
							
							return true;
						}
					}
					
					inputStream.close();
				}
			}
		}
		
		zip.close();
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void setConfig() throws IOException {
		ZipFile zip = new ZipFile(module);
		
		Enumeration<? extends ZipEntry> entries = zip.entries();
		
		if(entries != null) {
			while(entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				
				if(entry.getName().equals("glowmodule.yml")) {
					InputStream inputStream = zip.getInputStream(entry);
					
					this.config = new HashMap<String, Object>((Map<? extends String, ? extends Object>) new Yaml().load(inputStream));
					
					inputStream.close();
				}
			}
		}
		
		zip.close();
	}
	
	public void setMain() throws IOException, ClassNotFoundException {
		ZipFile zip = new ZipFile(module);
		
		Enumeration<? extends ZipEntry> entries = zip.entries();
		
		if(entries != null) {
			while(entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				
				if(entry.getName().equals("glowmodule.yml")) {
					InputStream inputStream = zip.getInputStream(entry);
					
					@SuppressWarnings({ "unchecked", "rawtypes" })
					Map<String, Object> config = new HashMap<String, Object>((Map) new Yaml().load(inputStream));
					
					if(config.containsKey("main")) {
						if(config.get("main") != null) {
							URL[] url = new URL[] {module.toURI().toURL()};
							
							@SuppressWarnings("resource")
							ClassLoader loader = new URLClassLoader(url);
							
							console.log("Main détécté ! main value = "+config.get("main"), Level.INFO);
							
							this.main = loader.loadClass((String) config.get("main"));
							
							modules.add(this);
							
							inputStream.close();
						}
					}
					
					inputStream.close();
				}
			}
		}
		
		zip.close();
	}
	
	public void setVersion() {
		console.log("Tentative de getVersion pour "+this.module.getName(), Level.INFO);
		
		if(this.config.get("version") != null) {
			this.version = (String) this.config.get("version");
		}else {
			console.log("Version non-trouvée !", Level.SEVERE);
			
			this.version = "vBASE";
			
			return;
		}
	}
	
	public void setName() {
		console.log("Tentative de getName pour "+this.module.getName(), Level.INFO);
		
		if(this.config.get("name") != null) {
			this.name = (String) this.config.get("name");
		}else {
			console.log("Name non-trouvé !", Level.SEVERE);
			
			this.name = "MODULE";
			
			return;
		}
	}
	
	public void load() {
		boot.setClassByModule(this);
	}
	
	public Class<?> getModuleClass(){
		return this.main;
	}
	
	public String getModuleVersion() {
		return this.version;
	}
	
	public String getModuleName() {
		return this.name;
	}
	
	public File getFileModule() {
		return this.module;
	}
	
	public Map<String, Object> getModuleConfig(){
		return this.config;
	}
	
	public static List<GlowModule> getAllModulesInstance(){
		return modules;
	}
}
