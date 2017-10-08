package fr.glowstoner.api.server.packets;

import java.io.Serializable;

import fr.glowstoner.api.server.GlowPacket;
import fr.glowstoner.api.server.PacketSource;
import fr.glowstoner.api.server.PacketType;

public class PacketLogin extends GlowPacket implements Serializable{
	
	private static final long serialVersionUID = 4616054627794646635L;
	
	private String pass;
	private PacketSource source;
	
	public PacketLogin(PacketSource source) {
		this.source = source;
	}

	public String getPass() {
		return pass;
	}
	
	public PacketSource getSource() {
		return this.source;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public PacketType state() {
		return PacketType.CLIENT;
	}
}
