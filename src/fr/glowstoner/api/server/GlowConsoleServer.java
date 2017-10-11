package fr.glowstoner.api.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.console.Level;
import fr.glowstoner.api.server.packets.PacketLogin;
import fr.glowstoner.api.server.packets.PacketMsg;
import fr.glowstoner.api.server.packets.PacketName;

public class GlowConsoleServer implements Runnable {

	private ServerSocket server;
	private Thread t;
	
	private static List<Socket> logged = new ArrayList<>();
	private static List<GlowClientConnection> connections = new ArrayList<>();
	
	public GlowConsoleServer(int port) throws IOException {
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
		for(GlowClientConnection c : GlowConsoleServer.connections) {
			if(c.getName().equals(clientname)) {
				return c.getClient();
			}
		}
		
		return null;
	}
	
	public List<GlowClientConnection> getAllConnections(){
		return GlowConsoleServer.connections;
	}

	@Override
	public void run() {
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
			
			GlowConsoleServer.connections.add(new GlowClientConnection("default", this));
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
					if(!GlowConsoleServer.logged.contains(socket)) {
						Object o = this.in.readObject();
						
						if(!(o instanceof PacketLogin)) {
							break;
						}
						
						if(o instanceof GlowPacket) {
							GlowPacket.getInstance().callEvent((GlowPacket) o);
						}
						
						PacketLogin p = (PacketLogin) o;
						
						if(p.getPass().equals(GlowAPI.getInstance().getConfig().getGlowServerPass())) {
							GlowConsoleServer.logged.add(socket);
							
							continue;
						}else {
							GlowAPI.getInstance().getBaseLogger().log("Mot de passe incorrect", Level.INFO);
							
							PacketMsg msg = new PacketMsg(PacketSource.SERVER);
							msg.writeMsg("Mot de passe incorrect");
							
							sendPacket(msg);
						}
					}else {
						Object o = this.in.readObject();
						
						if(o instanceof GlowPacket) {
							GlowPacket.getInstance().callEvent((GlowPacket) o);
							
							if(o instanceof PacketName) {
								PacketName n = (PacketName) o;

								if(n.getName() != null) {
									for(GlowClientConnection connection : GlowConsoleServer.connections) {
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