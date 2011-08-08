package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolBar extends HorizontalPanel {

	public ToolBar() {

	}

	/**
	 * Aligns the Tools to the Left
	 * 
	 * @param positions
	 */
	public void add(HorizontalAlignmentConstant alignment,Widget... btns) {
		for (Widget button : btns) {
			this.add(button);
			this.setCellHorizontalAlignment(button,alignment);
		}
	}
	public void add(HorizontalAlignmentConstant alignment,Widget widget) {
			this.add(widget);
			this.setCellHorizontalAlignment(widget,alignment);
	}

}
