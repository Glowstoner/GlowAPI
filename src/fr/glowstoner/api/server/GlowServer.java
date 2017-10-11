package fr.glowstoner.api.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.console.Level;
import fr.glowstoner.api.server.packets.PacketLogin;
import fr.glowstoner.api.server.packets.PacketMsg;
import fr.glowstoner.api.server.packets.PacketName;

public class GlowServer implements Runnable {

	private ServerSocket server;
	private Thread t;
	
	private static List<Socket> logged = new ArrayList<>();
	private static List<GlowClientConnection> connections = new ArrayList<>();
	
	public GlowServer(int port) throws IOException {
		this.server = new ServerSocket(port);
	}
	
	public void start() {
		this.t = new Thread(this);
		t.start();
	}
	
	public void addConnection(Socket socket) {
		GlowClientThread c = new GlowClientThread(socket);
		
		try {
			c.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		c.start();
	}
	
	public void close() {
		if(this.server != null) {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public GlowClientThread getConnection(String clientname){
		for(GlowClientConnection c : GlowServer.connections) {
			if(c.getName().equals(clientname)) {
				return c.getClient();
			}
		}
		
		return null;
	}
	
	public List<GlowClientConnection> getAllConnections(){
		return GlowServer.connections;
	}

	@Override
	public void run() {
		GlowAPI.getInstance().getBaseLogger().log("Serveur actif sur "+server.getLocalPort(), Level.INFO);
		
		while(t != null) {
			try {
				addConnection(this.server.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class GlowClientThread extends Thread {
		
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		private boolean active;

		public GlowClientThread(Socket socket) {
			this.socket = socket;
			
			GlowAPI.getInstance().getBaseLogger().log("Connection reçue ! IP : "+socket.getInetAddress().getHostAddress(), Level.INFO);
			
			GlowServer.connections.add(new GlowClientConnection("default", this));
		}
		
		public void open() throws IOException {
			this.in = new ObjectInputStream(socket.getInputStream());
			this.out = new ObjectOutputStream(socket.getOutputStream());
			
			this.out.flush();
			
			this.active = true;
		}
		
		public void sendPacket(GlowPacket p) throws IOException {
			this.out.writeObject(p);
			this.out.flush();
		}
		
		public void close() throws IOException {
			this.active = false;
			
			if(socket != null) socket.close();
			if(in != null) in.close();
			if(out != null) out.close();
		}
		
		@Override
		public void run() {
			while(active) {
				try {
					if(!GlowServer.logged.contains(socket)) {
						Object o = this.in.readObject();
						
						GlowAPI.getInstance().getBaseLogger().log("Packet reçu ! "+o.toString(), Level.INFO);
						
						if(!(o instanceof PacketLogin)) {
							break;
						}
						
						if(o instanceof GlowPacket) {
							GlowPacket.getInstance().callEvent((GlowPacket) o);
						}
						
						PacketLogin p = (PacketLogin) o;
						
						GlowAPI.getInstance().getBaseLogger().log("Protocole PacketLogin détécté : "+p.getPass(), Level.INFO);
						
						if(p.getPass().equals(GlowAPI.getInstance().getConfig().getGlowServerPass())) {
							GlowAPI.getInstance().getBaseLogger().log("Mot de passe correct !", Level.INFO);
							
							GlowServer.logged.add(socket);
							
							continue;
						}else {
							GlowAPI.getInstance().getBaseLogger().log("Mot de passe incorrect", Level.INFO);
							
							PacketMsg msg = new PacketMsg(PacketSource.SERVER);
							msg.writeMsg("Mot de passe incorrect");
							
							sendPacket(msg);
						}
					}else {
						Object o = this.in.readObject();
						
						GlowAPI.getInstance().getBaseLogger().log("Packet reçu : "+o, Level.INFO);
						
						if(o instanceof GlowPacket) {
							GlowPacket.getInstance().callEvent((GlowPacket) o);
							
							if(o instanceof PacketName) {
								PacketName n = (PacketName) o;

								if(n.getName() != null) {
									GlowAPI.getInstance().getBaseLogger().log("PacketName reçu : "+this.socket+" renommé en "+n.getName(), Level.INFO);
									
									for(GlowClientConnection connection : GlowServer.connections) {
										if(connection.getClient().equals(this)) {
											connection.setName(n.getName());
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					active = false;
					
					Iterator<GlowClientConnection> conn = GlowServer.connections.iterator();
					
					GlowClientConnection gconnect = null;
					
					while(conn.hasNext()) {
						GlowClientConnection gcc = conn.next();
						
						if(gcc.getClient().equals(this)) {
							gconnect = gcc;
						}
					}
					
					GlowServer.connections.remove(gconnect);
				}
			}
			
			try {
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public class GlowClientConnection {
		
		private String name;
		private GlowClientThread client;
		
		public GlowClientConnection(String name, GlowClientThread client) {
			this.name = name;
			this.client = client;
		}
		
		public GlowClientConnection() {
			return;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public GlowClientThread getClient() {
			return client;
		}

		public void setClient(GlowClientThread client) {
			this.client = client;
		}
	}
}