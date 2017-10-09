package fr.glowstoner.api;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import fr.glowstoner.api.boot.GlowBoot;
import fr.glowstoner.api.boot.IGlowAPIMethods;
import fr.glowstoner.api.command.GlowCommand;
import fr.glowstoner.api.console.GlowConsole;
import fr.glowstoner.api.console.GlowLogSource;
import fr.glowstoner.api.console.GlowLogger;
import fr.glowstoner.api.console.SourceType;
import fr.glowstoner.api.files.GlowModule;
import fr.glowstoner.api.server.GlowPacket;
import fr.glowstoner.api.server.GlowServer;
import fr.glowstoner.api.server.PacketSource;
import fr.glowstoner.api.server.packets.PacketMsg;

public abstract class GlowAPI implements IGlowAPIMethods{
	
	private static GlowAPI instance;
	
	private static GlowConsole console;
	private static GlowCommand cmd;
	private static GlowBoot boot;
	private static GlowServer server;
	
	private static GlowPacket packet;

	public GlowAPI() {
		instance = this;
	}

	public GlowConsole getConsole() {
		return console;
	}
	
	public GlowCommand getCommand() {
		return cmd;
	}
	
	public GlowPacket getPacket() {
		return packet;
	}
	
	public GlowServer getServer() {
		return server;
	}
	
	public void setServer(GlowServer server) {
		GlowAPI.server = server;
	}
	
	public GlowLogger getLogger(Object module) {
		for(GlowModule modules : GlowModule.getAllModulesInstance()) {	
			if(module.getClass().equals(modules.getModuleClass())) {
				return new GlowLogger(new GlowLogSource(SourceType.MODULE, modules));
			}
		}
		
		return null;
	}
	
	public GlowLogger getBaseLogger() {
		return new GlowLogger(new GlowLogSource(SourceType.BASE, null));
	}
	
	public static GlowAPI getInstance() {
		return instance;
	}
	
	public GlowBoot getBootLoader() {
		return boot;
	}
	
	public void stop() {
		try {
			boot.endAll();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//load all (Packets + Console + Command + Default Bootloader)
	public static void boot() {
		PacketMsg p = new PacketMsg(PacketSource.DEFAULT);
		p.writeMsg("default-boot");
		
		GlowAPI.packet = GlowPacket.getInstance();
		
		GlowBoot bootload = new GlowBoot();
		bootload.initConsole();
		GlowCommand cmdInstance = new GlowCommand();
		cmd = cmdInstance;
		
		console = bootload.getConsole();
		bootload.globalStart(true);
		
		boot = bootload.getBootLoader();
		
		try {
			GlowAPI.packet.registerAllPackets();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	//load Command + Packets
	public static void bootInstance() {
		PacketMsg p = new PacketMsg(PacketSource.DEFAULT);
		p.writeMsg("default-genI");
		
		GlowAPI.packet = GlowPacket.getInstance();
		
		GlowBoot bootload = new GlowBoot();
		GlowCommand cmdI = new GlowCommand();
		cmd = cmdI;
		boot = bootload;
		
		bootload.globalStart(false);
		
		try {
			GlowAPI.packet.registerAllPackets();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
}