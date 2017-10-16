package fr.glowstoner.api.network.packets.control;

public interface IGlowPacketListener {
	
	public abstract void onPacketReceive(GlowPacket packet);

}
