package fr.glowstoner.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class GlowConfig {
	
	private File folder;
	private File config;
	private Map<String, Object> configMap;
	private String glowserverpass;
	
	public GlowConfig() {
		
	}
	
	public boolean hasFolder() {
		Path path = null;
		
		try {
			path = Paths.get(ClassLoader.getSystemResource("").toURI());
			
			for(File files : path.toFile().listFiles()) {
				if(files.getName().equals("config")){
					if(files.isDirectory()) {
						this.folder = files;
						
						return true;
					}
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public void createFolder() {
		if(!hasFolder()) {
			Path path = null;
			
			try {
				path = Paths.get(ClassLoader.getSystemResource("").toURI());
				
				File file = new File(path.toString() + "\\config\\");
				file.mkdirs();
				
				this.folder = file;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean hasConfig() {
		for(File files : this.folder.listFiles()) {
			if(files.getName().equals("glowconfig.yml")) {
				 return true;
			}
		}
		
		return false;
	}
	
	public void createConfig() {
		try {
			File config = new File(this.folder.getPath() + "/glowconfig.yml/");
			config.createNewFile();
			
			InputStream in = getClass().getResourceAsStream("/fr/glowstoner/api/config/file/glowconfig.yml");
			
			Files.copy(in, config.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			this.config = new File(this.folder.getPath() + "/glowconfig.yml/");
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setConfig() {
		this.config = new File(this.folder.getPath() + "/glowconfig.yml/");
	}
	
	public void loadConfig() throws FileNotFoundException {
		InputStream in = new FileInputStream(this.config);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> confmap = new HashMap<>((Map<? extends String, ? extends Object>) new Yaml().load(in));
		
		this.configMap = confmap;
	}

	public void setGlowServerPass() {
		this.glowserverpass = (String) this.configMap.get("glowserver-pass");
	}
	
	public String getGlowServerPass() {
		return this.glowserverpass;
	}
}