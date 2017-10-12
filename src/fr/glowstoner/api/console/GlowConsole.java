package fr.glowstoner.api.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import fr.glowstoner.api.GlowAPI;

public class GlowConsole {
	private JPanel panel;
	private JFrame frame;
	private JTextField text;
	private JTextPane textpane;
	private JScrollPane scroll;
	private StyledDocument doc;
	private GlowLoader loader;
	
	private String n;
	private String l;
  
	public void genConsole() {
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		//loader
		this.loader = new GlowLoader();
		this.loader.setLoadPanel();
		this.loader.packWindow();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e4) {
			e4.printStackTrace();
		}
		
		this.loader.setLoadText("Chargement logs ...");
		
		this.textpane = new JTextPane();
		this.textpane.setEditable(false);
		
		Font font = null;
		
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fr/glowstoner/api/ressources/light.ttf")).deriveFont(16f);
			
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (FontFormatException | IOException e4) {
			e4.printStackTrace();
		}
		
		this.textpane.setFont(font);
		this.textpane.setBackground(new Color(50, 50, 50));
		this.textpane.setForeground(Color.WHITE);
		this.textpane.setCaretColor(Color.WHITE);
    
		this.loader.setLoadText("Chargement emplacement text ...");
		
		this.text = new JTextField();
		this.text.setForeground(Color.WHITE);
		this.text.setCaretColor(Color.WHITE);
		this.text.setBackground(new Color(50, 50, 50));
		this.text.setFont(new Font("Serge UI", 0, 12));
    
		this.loader.setLoadText("Chargement scroll ...");
		
		this.scroll = new JScrollPane(this.textpane);
		this.scroll.setBorder(null);
		this.scroll.setOpaque(false);
		this.scroll.getViewport().setOpaque(false);
    
		this.loader.setLoadText("Chargement frame ...");
		
		this.frame = new JFrame();
		this.frame.setTitle("GlowAPI");
		this.frame.setSize(1200, 800);
		
		try {
			this.frame.setIconImage(ImageIO.read(getClass().getResource("/fr/glowstoner/api/ressources/glowstone.png")));
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		
		this.panel = new JPanel(new BorderLayout());
		
		this.panel.setPreferredSize(new Dimension(1200, 800));
		
		this.panel.add(this.text, BorderLayout.PAGE_END);
		this.panel.add(this.scroll, BorderLayout.CENTER);
		
		this.panel.setBackground(new Color(50, 50, 50));
		
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(true);
		this.frame.add(this.panel);
		
		this.loader.setLoadText("Chargement doc ...");
		
		this.doc = this.textpane.getStyledDocument();
		
		try {
			this.doc.insertString(this.doc.getLength(), "GlowAPI, by Glowstoner", null);
      
			this.textpane.setStyledDocument(this.doc);
		} catch (BadLocationException e2) {
			e2.printStackTrace();
		}
    
		this.loader.setLoadText("Chargement Events ...");
		
		this.text.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("")) {
					return;
				}
				
				String line = null;
				boolean exists = false;
				
				for(char cs : e.getActionCommand().toCharArray()) {
					if(!(cs == ' ')) {
						exists = true;

						break;
					}
				}
				
				if(!exists) {
					GlowAPI.getInstance().getConsole().text.setText("");
					
					return;
				}
				
				for(int index = 0 ; index <= e.getActionCommand().length() ; index++) {
					if(!(e.getActionCommand().charAt(index) == ' ')) {
						line = e.getActionCommand().substring(index);
						break;
					}
				}
				
				if(line == null) {
					return;
				}
				
				GlowAPI.getInstance().getConsole().l = line;
				
				String[] allargs = line.split(" ");
        
				StringBuilder builder = new StringBuilder();
				
				for (String arg : allargs) {					
					if (!arg.equals(allargs[0])) {
						builder.append(arg + " ");
					}
				}
				
				String[] args = builder.toString().split(" ");
				
				if(args[0].equals("")) {
					args = new String[0];
				}
        
				String command = allargs[0];
				
				GlowConsole.this.log("[CONSOLE] Console -> \"" +line + "\"", Level.INFO);
				if (!GlowAPI.getInstance().getCommand().hasCommand(command)) {
					GlowConsole.this.log("[CONSOLE] Commande inexistante !", Level.WARNING);
          
					return;
				}
				
				try {
					GlowAPI.getInstance().getCommand().executeCommand(command, args);
					
					GlowConsole.this.textpane.setCaretPosition(GlowConsole.this.doc.getLength());
				} catch (Exception e1) {
					GlowConsole.this.log("Erreur lors de l'execution de la commande \"" + e.getActionCommand() + "\" !", Level.SEVERE);
					GlowConsole.this.log(e1.toString(), Level.SEVERE);
					
					e1.printStackTrace();
				}
			}
		});
		
		this.text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				final GlowConsole c = GlowAPI.getInstance().getConsole();
				
				switch(e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						//down
						
						if(text.getText() != null && !text.getText().equals(l)) {
							n = c.text.getText();
						}
						
						c.text.setText(c.l);
						break;
					case KeyEvent.VK_UP:
						//up
						
						c.text.setText(c.n);
						break;
				}
			}
		});
		
		this.loader.setLoadText("Pret !");
		this.loader.setVisible(false);
	}
  
	public void log(String msg, Level lvl) {
		if (GlowAPI.getInstance() == null) {
			if (lvl.equals(Level.INFO)) {
				print("[BOOTSTRAP] [INFO] " + msg, Color.WHITE);
			} else if (lvl.equals(Level.SEVERE)) {
				print("[BOOTSTRAP] [ERREUR] " + msg, Color.RED);
			} else if (lvl.equals(Level.WARNING)) {
				print("[BOOTSTRAP] [ATTENTION] " + msg, Color.ORANGE);
			} else {
				print("[BOOTSTRAP] [ATTENTION] Type de level non-reconnu ! Msg -> " + msg, Color.RED);
			}
			
			return;
		}
		
		if (lvl.equals(Level.INFO)) {	
			print("[INFO] " + msg, Color.WHITE);
		} else if (lvl.equals(Level.SEVERE)) {
			print("[ERREUR] " + msg, Color.RED);
		} else if (lvl.equals(Level.WARNING)) {
			print("[ATTENTION] " + msg, Color.ORANGE);
		} else {
			print("[ATTENTION] Type de level non-reconnu ! Msg -> " + msg, Color.RED);
		}
	}
	
	public void logColor(String msg, Level lvl, Color c) {
		if (GlowAPI.getInstance() == null) {
			if (lvl.equals(Level.INFO)) {
				print("[BOOTSTRAP] [INFO] " + msg, c);
			} else if (lvl.equals(Level.SEVERE)) {
				print("[BOOTSTRAP] [ERREUR] " + msg, Color.RED);
			} else if (lvl.equals(Level.WARNING)) {
				print("[BOOTSTRAP] [ATTENTION] " + msg, Color.ORANGE);
			} else {
				print("[BOOTSTRAP] [ATTENTION] Type de level non-reconnu ! Msg -> " + msg, Color.RED);
			}
			
			return;
		}
		
		if (lvl.equals(Level.INFO)) {	
			print("[INFO] " + msg, c);
		} else if (lvl.equals(Level.SEVERE)) {
			print("[ERREUR] " + msg, Color.RED);
		} else if (lvl.equals(Level.WARNING)) {
			print("[ATTENTION] " + msg, Color.ORANGE);
		} else {
			print("[ATTENTION] Type de level non-reconnu ! Msg -> " + msg, Color.RED);
		}
	}
	
	public void logBoot(String msg, Level lvl) {
		if (lvl.equals(Level.INFO)) {
			print("[BOOTSTRAP] [INFO] " + msg, Color.WHITE);
		} else if (lvl.equals(Level.SEVERE)) {
			print("[BOOTSTRAP] [ERREUR] " + msg, Color.RED);
		} else if (lvl.equals(Level.WARNING)) {
			print("[BOOTSTRAP] [ATTENTION] " + msg, Color.ORANGE);
		} else {
			print("[BOOTSTRAP] [ATTENTION] Type de level non-reconnu ! Msg -> " + msg, Color.RED);
		}
	}
  
	private void print(String msg, Color c) {
		try {
			StyleContext context = new StyleContext();
			
			if (this.doc.getLength() == 0) {
				Style style = context.addStyle("style", null);
				
				StyleConstants.setForeground(style, c);
				
				this.doc.insertString(this.doc.getLength(), msg, style);
			} else {
				Style style = context.addStyle("style", null);
				
				StyleConstants.setForeground(style, c);
				
				this.doc.insertString(this.doc.getLength(), "\n" + msg, style);
			}
		} catch (BadLocationException e) {
			log("Erreur critique !", Level.SEVERE);
		}
		
		this.textpane.setStyledDocument(this.doc);
    
		this.text.setText(null);
	}
  
	public StyledDocument getLogs() {
		return this.doc;
	}
  
	public void clear() {
		try {
			this.doc.remove(0, this.doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public JTextPane getPane() {
		return this.textpane;
	}
	
	public JTextField getText() {
		return this.text;
	}
	
	public JScrollPane getEndPane() {
		return this.scroll;
	}
  
	public void destroy() {
		this.frame.dispatchEvent(new WindowEvent(this.frame, 201));
	}
}
