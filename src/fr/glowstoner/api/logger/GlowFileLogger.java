package fr.glowstoner.api.logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GlowFileLogger {
	
	private File folder;
	private File logfile;

	public GlowFileLogger() {
		
	}
	
	public boolean hasFolder() {
		Path path = null;
		
		try {
			path = Paths.get(ClassLoader.getSystemResource("").toURI());
			
			for(File files : path.toFile().listFiles()) {
				if(files.getName().equals("logs")){
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
				
				File file = new File(path.toString() + "\\logs\\");
				file.mkdirs();
				
				this.folder = file;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean ifFileWithSameNameExists(String name) {
		for(File fs : this.folder.listFiles()) {
			if(fs.getName().equals(name+".glowlog")) {
				return true;
			}
		}
		
		return false;
	}
	
	public void createLogFile() throws IOException {
		String name = null;
		
		Date d = new Date();
		
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(d);
		 
		name = "consolelog-"+c.get(Calendar.YEAR)+"-"+c.get(Calendar.DAY_OF_MONTH)+"-"
				+c.get(Calendar.MONTH)+"-"+c.get(Calendar.MINUTE);
		
		int nameerr = 2;
		
		while(ifFileWithSameNameExists(name)) {
			if(name.contains("_")) {
				name = name.substring(0, name.indexOf("_")) + "_";
				
				name = name + nameerr;
			}else {
				name = name + "_" + nameerr;
			}
			
			nameerr++;
		}
		
		name = name + ".glowlog";
		
		File f = new File(this.folder.getPath() + "\\" + name + "\\");
		f.createNewFile();
		
		this.logfile = f;
	}
	
	public File getLogFile() {
		return logfile;
	}
}
