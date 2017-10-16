package fr.glowstoner.api.network.packets.control;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.api.files.GlowModule;
import fr.glowstoner.api.network.packets.control.enums.PacketSource;
import fr.glowstoner.api.network.packets.control.enums.PacketType;

public abstract class GlowPacket implements Serializable{

	private static final long serialVersionUID = 5353130595968876758L;
	
	private static GlowPacket instance;
	
	private static List<IGlowPacketListener> listeners = new ArrayList<>();
	
	private PacketSource source;
	
	public GlowPacket(PacketSource source) {
		GlowPacket.instance = this;
		this.source = source;
	}
	
	public void addPacketListener(IGlowPacketListener packetlistener) {
		listeners.add(packetlistener);
	}
	
	public void callEvent(GlowPacket packet) {
		for(IGlowPacketListener listener : listeners) {
			listener.onPacketReceive(packet);
		}
	}
	
	public void registerAllPackets() throws NoSuchMethodException, SecurityException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
		
		for(GlowModule mod : GlowModule.getAllModulesInstance()) {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
		    method.setAccessible(true);
		    method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{mod.getFileModule().toURI().toURL()});
		}
	}

	public abstract PacketType state();

	public static GlowPacket getInstance() {
		return instance;
	}

	public static void setInstance(GlowPacket instance) {
		GlowPacket.instance = instance;
	}
	
	public PacketType getType() {
		return state();
	}
	
	public PacketSource getSource() {
		return this.source;
	}
}
