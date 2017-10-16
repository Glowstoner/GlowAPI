package fr.glowstoner.api.network.packets;

import java.io.Serializable;

import fr.glowstoner.api.network.packets.control.GlowPacket;
import fr.glowstoner.api.network.packets.control.enums.PacketSource;
import fr.glowstoner.api.network.packets.control.enums.PacketType;

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
