package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class ToolBar extends HorizontalPanel {

	public ToolBar() {

	}

	/**
	 * Aligns the Tools to the Left
	 * 
	 * @param positions
	 */
	public void leftAlign(ImageButton... btns) {
		for (ImageButton imageButton : btns) {
			this.add(imageButton);
			this.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
		}
	}

	/**
	 * Aligns the Tools to the Right
	 * 
	 * @param positions
	 */
	public void rightAlign(ImageButton... btns) {
		for (ImageButton imageButton : btns) {
			this.add(imageButton);
			this.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
		}
	}
}
