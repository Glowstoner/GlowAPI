package fr.glowstoner.api.server;

public interface IGlowPacketListener {
	
	public abstract void onPacketReceive(GlowPacket packet);

}
