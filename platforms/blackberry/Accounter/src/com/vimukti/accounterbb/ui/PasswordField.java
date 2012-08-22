package com.vimukti.accounterbb.ui;

import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.PasswordEditField;

public class PasswordField extends PasswordEditField {

	private InputSelectionListener listener;

	public PasswordField() {
		setPadding(new XYEdges(5, 10, 5, 20));
	}

	public Locale getPreferredInputLocale() {
		return null;
	}

	public int getTextInputStyle() {
		return 0;
	}

	public boolean isUnicodeInputAllowed() {
		return false;
	}

	public void updateInputStyle() {

	}

	protected void paintBackground(Graphics graphics) {

		graphics.setColor(Color.WHITE);
		graphics.fillRoundRect(10, 0, getWidth() - 20, getHeight() - 5, 20, 20);
		graphics.setColor(0x686868);
		graphics.drawRoundRect(10, 0, getWidth() - 20, getHeight() - 5, 20, 20);
		graphics.setColor(Color.BLACK);
		graphics.drawRoundRect(11, 1, getWidth() - 22, getHeight() - 5, 20, 20);
		graphics.setColor(Color.BLACK);
	}

	public int getPreferredHeight() {
		return 100;
	}

	public int getPreferredWidth() {
		return 150;
	}

	protected boolean keyChar(char key, int status, int time) {
		if (Characters.ENTER == key) {
			listener.inputSelected(this.getText().trim());
			return true;
		}
//		else {
//
//			StringBuffer text = new StringBuffer(this.getText());
//			if (key == '\b') {
//				String string = new String(text);
//				int length = string.length();
//				if (length > 1) {
//					string = string.substring(0, length - 1);
//				} else if (length == 0 || length == 1) {
//					string = "";
//				}
//				this.setText(string);
//			} else if(Characters.ESCAPE != key){
//				this.setText(text.append(key).toString());
//			}
//		}
		return super.keyChar(key, status, time);
	}

	public void setSelectionListener(InputSelectionListener listener) {
		this.listener = listener;
	}

}
