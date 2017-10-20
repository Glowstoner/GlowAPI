package fr.glowstoner.api.network.packets;

import java.io.Serializable;

import fr.glowstoner.api.network.packets.control.GlowPacket;
import fr.glowstoner.api.network.packets.control.enums.PacketSource;
import fr.glowstoner.api.network.packets.control.enums.PacketType;

public class PacketLogin extends GlowPacket implements Serializable{
	
	private static final long serialVersionUID = 4616054627794646635L;
	
	public PacketLogin(PacketSource source) {
		super(source);
	}
	
	private String pass;
	

	public String getPass() {
		return pass;
	}

	public void writePass(String pass) {
		this.pass = pass;
	}

	@Override
	public PacketType state() {
		return PacketType.CLIENT;
	}

	@Override
	public boolean isEncrypted() {
		return true;
	}
}
