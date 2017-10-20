package fr.glowstoner.api.network.packets.control;

public interface IGlowPacketListener {
	
	public abstract void onPacketReceive(GlowPacket packet);
	
	public abstract GlowPacket onPacketSending(GlowPacket packet);

}
