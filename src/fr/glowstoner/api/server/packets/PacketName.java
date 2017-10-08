package fr.glowstoner.api.server.packets;

import java.io.Serializable;

import fr.glowstoner.api.server.GlowPacket;
import fr.glowstoner.api.server.PacketSource;
import fr.glowstoner.api.server.PacketType;

public class PacketName extends GlowPacket implements Serializable{

	private static final long serialVersionUID = 3403427205589374397L;

	private String name;
	private PacketSource source;
	
	public PacketName(PacketSource source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public PacketSource getSource() {
		return this.source;
	}

	@Override
	public PacketType state() {
		return PacketType.SERVER;
	}
}
