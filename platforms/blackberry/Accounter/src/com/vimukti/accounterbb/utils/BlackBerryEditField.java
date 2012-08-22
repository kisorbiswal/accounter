package com.vimukti.accounterbb.utils;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.EditField;

import com.vimukti.accounterbb.ui.InputSelectionListener;

/**
 * 
 * This is Custom EditField for BlackBerry.
 * 
 * @author Architect by Jitendra.B
 * 
 * 
 */

public class BlackBerryEditField extends BasicEditField {
	/**
	 * No arg Constructor with default padding values
	 */

	private InputSelectionListener listener;

	public BlackBerryEditField() {
		this(0);
	}

	public BlackBerryEditField(long style) {
		this(new XYEdges(5, 10, 5, 20), EditField.FIELD_VCENTER
				| EditField.USE_ALL_WIDTH | EditField.NON_SPELLCHECKABLE
				| CONSUME_INPUT | FOCUSABLE | style);
	}

	public BlackBerryEditField(XYEdges points, long style) {
		super(style);
		setPadding(points);
	}

	/**
	 * Paints EditField Background with specified Format values
	 */
	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 20, 20);
		graphics.setColor(0x686868);
		graphics.drawRoundRect(10, 0, getWidth() - 20, getHeight(), 20, 20);
		graphics.setColor(Color.BLACK);
		graphics.drawRoundRect(11, 1, getWidth() - 22, getHeight() - 2, 20, 20);
		graphics.setColor(Color.BLACK);

	}

	protected boolean keyChar(char key, int status, int time) {
		if (Characters.ENTER == key) {
			listener.inputSelected(this.getText().trim());
			return true;
		} 
//		else {
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

	public int getPreferredHeight() {
		return 100;
	}

	public int getPreferredWidth() {
		return 150;
	}

	public void setSelectionListener(InputSelectionListener listener) {
		this.listener = listener;
	}
	
}
