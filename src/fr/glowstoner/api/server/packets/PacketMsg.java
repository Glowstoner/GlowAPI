package fr.glowstoner.api.server.packets;

import java.io.Serializable;

import fr.glowstoner.api.server.GlowPacket;
import fr.glowstoner.api.server.PacketSource;
import fr.glowstoner.api.server.PacketType;

public class PacketMsg extends GlowPacket implements Serializable{
	
	private static final long serialVersionUID = -7679587219427702316L;
	
	private String msg;
	
	public PacketMsg(PacketSource source) {
		super(source);
	}
	
	public void writeMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public PacketType state() {
		return PacketType.DOUBLE;
	}
}
