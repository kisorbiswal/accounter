package com.vimukti.accounter.admin.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ToolBar;

public class AdminViewManger extends VerticalPanel {
	private ToolBar toolBar;
	VerticalPanel simplepanel;
	VerticalPanel vpanel;
	static AdminViewManger adminViewManger;

	public AdminViewManger() {
		createControls();
	}

	private void createControls() {
		vpanel = new VerticalPanel();
		vpanel.addStyleName("view_manager_body");
		vpanel.setWidth("100%");
		vpanel.setHeight("100%");
		simplepanel = new VerticalPanel();
		simplepanel.setHeight("100%");
		simplepanel.setWidth("100%");
		simplepanel.addStyleName("viewholder");
		this.toolBar = new ToolBar();
		vpanel.add(toolBar);
		vpanel.add(new Label("Accounter_Test"));
		vpanel.add(simplepanel);
		this.add(vpanel);

	}

	public void showView(final AdminAbstractView<?> view, final Action action,
			boolean shouldAskToSave) {
		Object input = action.getInput();
		if (view.getManager() == null) {
			view.setManager(this);
			if (input != null) {
				view.setData(input);
			}
			view.init();
			view.initData();
			simplepanel.add(view);
		}
	}

	public void closeCurrentView() {
		// TODO Auto-generated method stub

	}

}
