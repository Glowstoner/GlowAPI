package fr.glowstoner.api.server.packets;

import java.io.Serializable;

import fr.glowstoner.api.server.GlowPacket;
import fr.glowstoner.api.server.PacketSource;
import fr.glowstoner.api.server.PacketType;

public class PacketLogin extends GlowPacket implements Serializable{
	
	private static final long serialVersionUID = 4616054627794646635L;
	
	public PacketLogin(PacketSource source) {
		super(source);
	}
	
	private String pass;
	

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public PacketType state() {
		return PacketType.CLIENT;
	}
}
