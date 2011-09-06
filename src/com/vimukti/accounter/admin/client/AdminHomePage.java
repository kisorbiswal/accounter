package com.vimukti.accounter.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AdminHomePage extends HorizontalPanel implements EntryPoint {
	static AdminViewManger viewmanger;
	Button mybutton;
	VerticalPanel vpPanel;
	private static AdminConstants adminconstants;

	@Override
	public void onModuleLoad() {
		vpPanel = new VerticalPanel();
		createControls();

		RootPanel.get().add(vpPanel);

	}

	private void createControls() {
		Button dummybutton = new Button("Gwt button");
		vpPanel.add(dummybutton);
		vpPanel.setWidth("100%");
		vpPanel.setHeight("100%");
		AdminHorizantalMenuBar adminHMenubar = new AdminHorizantalMenuBar();
		vpPanel.add(adminHMenubar);
		viewmanger = new AdminViewManger();
		vpPanel.add(viewmanger);

	}

	public static AdminViewManger getViewManager() {
		return viewmanger;
	}

	public static AdminConstants getAdminConstants() {
		if (adminconstants == null) {
			adminconstants = (AdminConstants) GWT.create(AdminConstants.class);
		}
		return adminconstants;
	}

	public static AdminUserListAction getAdminUserListAction() {
		return new AdminUserListAction(AdminHomePage.getAdminConstants()
				.usersList());
	}

	public static AddNewAdminuserAction getNewAdminUserAction() {
		return new AddNewAdminuserAction(AdminHomePage.getAdminConstants()
				.addNewUser());
	}
}
