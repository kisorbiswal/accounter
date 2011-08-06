package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ToolBar extends HorizontalPanel {

	private ButtonGroup backNextGroup, closeGroup, addGroup, titleGroup;
	private Label statusLabel;

	public ToolBar() {
		init();
	}

	private void init() {

		statusLabel = new Label();
		backNextGroup = new ButtonGroup(ButtonGroup.BACK_NEXT_BUTTON_GROUP);
		addGroup = new ButtonGroup(ButtonGroup.ADD_BUTTON_GROUP);
		closeGroup = new ButtonGroup(ButtonGroup.CLOSE_BUTTON_GROUP);
		this.add(backNextGroup);
		this.add(statusLabel);
		this.add(addGroup);
		this.add(closeGroup);
	}

	public void addButtonGroup(int buttonGroupType) {
		add(new ButtonGroup(buttonGroupType));
	}

	public void updateToolBar(ParentCanvas<?> existingView) {

	}
}
