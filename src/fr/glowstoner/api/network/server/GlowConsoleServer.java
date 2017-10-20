package fr.glowstoner.api.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.network.packets.PacketLogin;
import fr.glowstoner.api.network.packets.PacketName;
import fr.glowstoner.api.network.packets.PacketText;
import fr.glowstoner.api.network.packets.control.GlowPacket;
import fr.glowstoner.api.network.packets.control.IGlowPacketListener;
import fr.glowstoner.api.network.packets.control.enums.PacketSource;
import fr.glowstoner.api.network.security.GlowNetworkSecurity;

public class GlowConsoleServer implements Runnable {

	private ServerSocket server;
	private Thread t;
	private GlowServerFrame f;
	private String key = "U2FsdGVkX1/MmvdUcO15WYc6OWHQqWkF6K9edkfBRW4=";
	
	private static List<Socket> logged = new ArrayList<>();
	private static List<GlowClientConnection> connections = new ArrayList<>();
	
	public GlowConsoleServer(int port) throws IOException {
		this.server = new ServerSocket(port);
		
		setKey(GlowAPI.getInstance().getConfig().getGlowServerSecrurityKey());
	}
	
	public void setKey(String key) {
		this.key  = key;
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
	
	public void openWindow() {
		this.f = new GlowServerFrame("GlowServer");
		
		this.f.genWindow();
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
			
			crypt();
		}
		
		public void sendPacket(GlowPacket p) throws IOException {
			GlowPacket gp = GlowAPI.getInstance().getPacket().callEventSending(p);
			
			this.out.writeObject(gp);
			this.out.flush();
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
							login.writePass(s.encrypt(pass));
						} catch (UnsupportedEncodingException | NoSuchAlgorithmException |
								InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
								BadPaddingException e) {
							
							e.printStackTrace();
						}
						
						return login; 
					}else if(packet instanceof PacketText) {
						PacketText text = (PacketText) packet;
						
						String msg = text.getText();
						
						GlowNetworkSecurity s = new GlowNetworkSecurity();
						
						try {
							s.setKey(key);
							text.writeText(s.encrypt(msg));
						} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
								| IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						
						return text;
					}else if(packet instanceof PacketName) {
						PacketName name = (PacketName) packet;
						
						String sname = name.getName();
						
						GlowNetworkSecurity s = new GlowNetworkSecurity();
						
						try {
							s.setKey(key);
							name.writeName(s.encrypt(sname));
						} catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException |
								NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
							e.printStackTrace();
						}
						
						return name;
					}
					
					return packet;
				}
				
				@Override
				public void onPacketReceive(GlowPacket packet) {}
			});
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
						
						GlowNetworkSecurity sec = new GlowNetworkSecurity();
						sec.setKey(key);
						
						String pass = sec.decrypt(p.getPass());
						
						if(pass.equals(GlowAPI.getInstance().getConfig().getGlowServerPass())) {
							GlowConsoleServer.logged.add(socket);
							
							continue;
						}else {
							PacketText msg = new PacketText(PacketSource.SERVER);
							msg.writeText("Mot de passe incorrect");
							
							sendPacket(msg);
						}
					}else {
						Object o = this.in.readObject();
						
						if(o instanceof GlowPacket) {
							GlowPacket.getInstance().callEvent((GlowPacket) o);
							
							GlowPacket gp = (GlowPacket) o;
							
							if(o instanceof PacketName) {
								PacketName n = (PacketName) gp;

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
					
					Iterator<GlowClientConnection> conn = GlowConsoleServer.connections.iterator();
					
					GlowClientConnection gconnect = null;
					
					while(conn.hasNext()) {
						GlowClientConnection gcc = conn.next();
						
						if(gcc.getClient().equals(this)) {
							gconnect = gcc;
						}
					}
					
					GlowConsoleServer.connections.remove(gconnect);
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