package fr.glowstoner.api.network.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import fr.glowstoner.api.GlowAPI;
import fr.glowstoner.api.console.logger.Level;

public class GlowServerFrame extends JFrame {

	private static final long serialVersionUID = 1823252141372937387L;
	
	private JPanel panel;
	private JScrollPane scroll;
	private JTextPane tpane;
	private StyledDocument doc;

	public GlowServerFrame(String title) {
		super(title);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}	
		
		try {
			super.setIconImage(ImageIO.read(getClass().getResource("/fr/glowstoner/api/ressources/glowstone.png")));
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}
	
	public void genWindow() {
		this.panel = new JPanel(new BorderLayout());
		
		this.panel.setPreferredSize(new Dimension(600, 400));
		
		this.tpane = new JTextPane();
		this.tpane.setEditable(false);
		
		Font font = GlowAPI.getInstance().getConsole().getActualStyle().getFont();
		
		this.tpane.setFont(font);
		this.tpane.setBackground(GlowAPI.getInstance().getConsole().getActualStyle().getBackgroundColor());
		this.tpane.setForeground(GlowAPI.getInstance().getConsole().getActualStyle().getTextColor());
		
		this.scroll = new JScrollPane(this.tpane);
		
		this.scroll.setBorder(null);
		this.scroll.setOpaque(false);
		this.scroll.getViewport().setOpaque(false);
		
		this.panel.add(this.scroll, BorderLayout.CENTER);
		
		this.doc = this.tpane.getStyledDocument();
		
		try {
			this.doc.insertString(this.doc.getLength(), "GlowAPI, by Glowstoner", null);
			
			this.tpane.setStyledDocument(this.doc);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		super.add(this.panel);
		
		super.setPreferredSize(new Dimension(600, 400));
		super.setLocationRelativeTo(null);
		
		super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		super.setVisible(true);
		
		super.pack();
	}
	
	public void log(String msg, Level lvl) {
		if (lvl.equals(Level.INFO)) {	
			print("[INFO] " + msg, GlowAPI.getInstance().getConsole().getActualStyle().getTextColor());
		} else if (lvl.equals(Level.SEVERE)) {
			print("[ERREUR] " + msg, Color.RED);
		} else if (lvl.equals(Level.WARNING)) {
			print("[ATTENTION] " + msg, Color.ORANGE);
		} else {
			print("[ATTENTION] Type de level non-reconnu ! Msg -> " + msg, Color.RED);
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
		
		this.tpane.setStyledDocument(this.doc);
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
}
