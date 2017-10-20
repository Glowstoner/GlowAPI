package fr.glowstoner.api.console;

import javax.swing.JOptionPane;

public class GlowExitPopup{

	public GlowExitPopup() {
		
	}
	
	public void showPopup() {
		int sel = JOptionPane.showConfirmDialog(null, "Êtes vous sûr de quitter ?\n"
				+ "/end est préférable pour fermer correctement l'API", "GlowAPI",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if(sel == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}
