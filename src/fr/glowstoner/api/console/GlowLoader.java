package fr.glowstoner.api.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GlowLoader extends JFrame{

	private static final long serialVersionUID = -5215161524990337111L;

	private JPanel panel;
	private JLabel load, text;
	
	public GlowLoader() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		super.setTitle("GlowAPI Loader");
		super.setPreferredSize(new Dimension(600, 400));
		super.setUndecorated(true);
		
		try {
			super.setIconImage(ImageIO.read(getClass().getResource("/fr/glowstoner/api/ressources/glowstone.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		super.setLayout(null);
		
		this.panel = new JPanel();
		this.panel.setLayout(null);
		
		this.panel.setPreferredSize(new Dimension(600, 400));
	}
	
	public void setLoadPanel() {
		this.panel.setBackground(new Color(50, 50, 50));
		
		ImageIcon gif = new ImageIcon(getClass().getClassLoader().getResource("fr/glowstoner/api/ressources/loader.gif"));
		
		JLabel label = new JLabel();
		label.setIcon(gif);
		
		gif.setImageObserver(label);
		label.setLayout(null);
		label.setBounds(250, 100, 111, 47);
		
		this.panel.add(label);
		
		load = new JLabel("Chargement ...");
		
		Font font = null;
		
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fr/glowstoner/api/ressources/light.ttf")).deriveFont(30f);
			
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (FontFormatException | IOException e4) {
			e4.printStackTrace();
		}
		
		load.setFont(font);
		
		load.setForeground(Color.WHITE);
		
		load.setLayout(null);
		load.setBounds(230, 10, 500, 500);
		
		this.text = new JLabel("GlowAPI by Glowstoner, Version Alpha");
		text.setFont(font.deriveFont(15f));
		text.setForeground(Color.WHITE);
		text.setLayout(null);
		text.setBounds(190, 50, 500, 500);
		
		this.panel.add(text);
		this.panel.add(load);
		
		super.getContentPane().add(panel);
	}
	
	public void setStyle() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException |
				ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			
			e.printStackTrace();
		}
	}
	
	public void setLoadText(String text) {
		this.text.setText(text);
	}
	
	public void packWindow() {
		super.setContentPane(this.panel);
		
		SwingUtilities.updateComponentTreeUI(this.panel);
		
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);
	}
}