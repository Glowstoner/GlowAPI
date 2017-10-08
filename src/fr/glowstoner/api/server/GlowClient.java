package fr.glowstoner.api.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.console.Level;

public class GlowClient {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public GlowClient(String ip, int port) throws IOException{
		GlowAPI.getInstance().getBaseLogger().log("Connection en cours à "+ip+" via "+port+" ...", Level.INFO);
		
		socket = new Socket(ip, port);
		GlowAPI.getInstance().getBaseLogger().log("Vous êtes connecté ! -> "+socket, Level.INFO);
		start();
	}
	
	public void sendPacket(GlowPacket packet) throws IOException {
		out.writeObject(packet);
		out.flush();
	}

   public void start() throws IOException {  
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
      
      GlowClientReceiver re = new GlowClientReceiver(this.in);
      re.start();
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
