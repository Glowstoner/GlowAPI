package fr.glowstoner.api.console;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GlowWelcome extends JPanel implements Runnable{

	private static final long serialVersionUID = 4008524287249266056L;
	
	private JLabel text, name;
	private boolean active;
	
	public GlowWelcome() {
		super(null);
		
		active = true;
		
		Thread t = new Thread(this);
		t.start();
		
		text = new JLabel();
		name = new JLabel();
		
		text.setLayout(null);
		name.setLayout(null);
		
		Font font = null;
		
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fr/glowstoner/api/ressources/light.ttf"));
			
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (FontFormatException | IOException e4) {
			e4.printStackTrace();
		}
		
		text.setFont(font.deriveFont(130.0f));
		text.setForeground(Color.WHITE);
		
		name.setFont(font.deriveFont(40.0f));
		name.setForeground(Color.WHITE);
		
		name.setText("Bienvenue "+System.getProperty("user.name"));
		
		text.setBounds(440, 100, 500, 500);
		name.setBounds(300, -10, 500, 500);
		
		super.setBackground(new Color(50, 50, 50));
		
		super.add(this.name);
		super.add(this.text);
	}
	
	public JLabel getText() {
		return this.text;
	}
	
	public GlowWelcome get() {
		return this;
	}

	@Override
	public void run() {
		while(this.active) {
			Date date = new Date();
			Calendar c =  GregorianCalendar.getInstance();
			c.setTime(date);
			
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int min = c.get(Calendar.MINUTE);
			int sec = c.get(Calendar.SECOND);
			
			if(min < 10) {
				if(sec < 10) {
					text.setText(hour+":0"+min+":0"+sec);
				}else {
					text.setText(hour+":0"+min+":"+sec);
				}
			}else {
				if(sec < 10) {
					text.setText(hour+":"+min+":0"+sec);
				}else {
					text.setText(hour+":"+min+":"+sec);
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
