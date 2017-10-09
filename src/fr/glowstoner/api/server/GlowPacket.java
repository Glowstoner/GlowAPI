package fr.glowstoner.api.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
