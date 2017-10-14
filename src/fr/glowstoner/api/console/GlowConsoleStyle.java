package fr.glowstoner.api.console;

import java.awt.Color;
import java.awt.Font;

public class GlowConsoleStyle {
	
	private Font font;
	private Color backgroundColor;
	private Color textColor;
	private Color caretColor;
	
	public GlowConsoleStyle() {
		return;
	}
	
	public GlowConsoleStyle(Font font, Color backgroundColor, Color textColor, Color caretColor) {
		this.font = font;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
		this.caretColor = caretColor;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Color getCaretColor() {
		return caretColor;
	}

	public void setCaretColor(Color caretColor) {
		this.caretColor = caretColor;
	}
}
