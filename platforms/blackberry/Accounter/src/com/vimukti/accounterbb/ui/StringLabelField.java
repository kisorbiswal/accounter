package com.vimukti.accounterbb.ui;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.LabelField;

public class StringLabelField extends LabelField {
	public StringLabelField(String title) {
		super(title);
	}

	protected void paint(Graphics graphics) {
		graphics.setFont(Font.getDefault().derive(Font.BOLD, 6, Ui.UNITS_pt));
		graphics.setColor(Color.BLUE);
		super.paint(graphics);
	}

	protected void layout(int width, int height) {
		super.layout(Display.getWidth() - 40, 20);
	}
}
