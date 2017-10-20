package fr.glowstoner.api.network.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.network.packets.PacketLogin;
import fr.glowstoner.api.network.packets.control.GlowPacket;
import fr.glowstoner.api.network.packets.control.IGlowPacketListener;
import fr.glowstoner.api.network.security.GlowNetworkSecurity;

public class GlowConsoleClient {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private String key = "U2FsdGVkX1/MmvdUcO15WYc6OWHQqWkF6K9edkfBRW4=";

	public GlowConsoleClient(String ip, int port) throws IOException{
		socket = new Socket(ip, port);
		start();
	}
	
	public void setSecurityKey(String key) {
		this.key = key;
	}
	
	public void sendPacket(GlowPacket packet) throws IOException {
		out.writeObject(GlowAPI.getInstance().getPacket().callEventSending(packet));
		out.flush();
	}
	
	public void crypt() {
		   GlowAPI.getInstance().getPacket().addPacketListener(new IGlowPacketListener() {
			
			@Override
			public GlowPacket onPacketSending(GlowPacket packet) {
				if(packet instanceof PacketLogin) {
					PacketLogin login = (PacketLogin) packet;
					
					String pass = login.getPass();
					
					GlowNetworkSecurity s = new GlowNetworkSecurity();
					try {
						s.setKey(key);
						login.setPass(s.encrypt(pass));
					} catch (UnsupportedEncodingException | NoSuchAlgorithmException |
							InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
							BadPaddingException e) {
						
						e.printStackTrace();
					}
					
					return login; 
				}
				
				return packet;
			}
			
			@Override
			public void onPacketReceive(GlowPacket packet) {}
		});
	   }

   public void start() throws IOException {  
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
      
      GlowClientReceiver re = new GlowClientReceiver(this.in);
      re.start();
      
      crypt();
   }
   
   public void stop() throws IOException {
	   if(socket != null)  socket.close();
	   if(in != null) socket.close();
   }
   
   public class GlowClientReceiver extends Thread {
	   
	   private ObjectInputStream in;
	   
	   public GlowClientReceiver(ObjectInputStream in) {
		   this.in = in;
	   }
	   
	   @Override
	   public void run() {
		   while(!super.isInterrupted()) {
			   try {
				   Object o = null;
				   
				   try {
					   o = in.readObject();
				   }catch(EOFException e) {
					   o = this.in.readObject();
				   }
				   
				   if(o instanceof GlowPacket) {
					   GlowPacket s = (GlowPacket) o;
					   
					   s.callEvent(s);
				   }
			   } catch (Exception e) {
				   e.printStackTrace();
				   
				   break;
			   }
		   	}
		   
		   this.interrupt();
		   
		   return;
	   	}
   	}
}
