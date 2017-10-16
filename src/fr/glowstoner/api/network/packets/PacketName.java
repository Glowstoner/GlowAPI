package fr.glowstoner.api.network.packets;

import java.io.Serializable;

import fr.glowstoner.api.network.packets.control.GlowPacket;
import fr.glowstoner.api.network.packets.control.enums.PacketSource;
import fr.glowstoner.api.network.packets.control.enums.PacketType;

public class PacketName extends GlowPacket implements Serializable{

	private static final long serialVersionUID = 3403427205589374397L;

	private String name;
	
	public PacketName(PacketSource source) {
		super(source);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public PacketType state() {
		return PacketType.SERVER;
	}
}
