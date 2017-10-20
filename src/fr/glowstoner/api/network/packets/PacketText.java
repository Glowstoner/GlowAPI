package fr.glowstoner.api.network.packets;

import java.io.Serializable;

import fr.glowstoner.api.network.packets.control.GlowPacket;
import fr.glowstoner.api.network.packets.control.enums.PacketSource;
import fr.glowstoner.api.network.packets.control.enums.PacketType;

public class PacketText extends GlowPacket implements Serializable{
	
	private static final long serialVersionUID = -7679587219427702316L;
	
	private String text;
	
	public PacketText(PacketSource source) {
		super(source);
	}
	
	public void writeText(String msg) {
		this.text = msg;
	}

	public String getText() {
		return text;
	}

	@Override
	public PacketType state() {
		return PacketType.DOUBLE;
	}

	@Override
	public boolean isEncrypted() {
		return true;
	}
}
