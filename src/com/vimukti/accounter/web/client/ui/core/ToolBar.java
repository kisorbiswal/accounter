package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ToolBar extends HorizontalPanel {

	private HorizontalPanel mainPanel;
	private ButtonGroup backNextGroup, closeGroup, addGroup, titleGroup;
	private Label statusLabel;

	public ToolBar() {
		add(defaultToolBar());
	}

	private Widget defaultToolBar() {

		mainPanel = new HorizontalPanel();
		statusLabel = new Label();
		backNextGroup = new ButtonGroup(ButtonGroup.BACK_NEXT_BUTTON_GROUP);
		addGroup = new ButtonGroup(ButtonGroup.ADD_BUTTON_GROUP);
		closeGroup = new ButtonGroup(ButtonGroup.CLOSE_BUTTON_GROUP);
		mainPanel.add(backNextGroup);
		mainPanel.add(statusLabel);
		mainPanel.add(addGroup);
		mainPanel.add(closeGroup);
		return mainPanel;
	}

	public void addButtonGroup(int buttonGroupType) {
		add(new ButtonGroup(buttonGroupType));
	}

	public void updateToolBar(ParentCanvas<?> existingView) {

	}
}
