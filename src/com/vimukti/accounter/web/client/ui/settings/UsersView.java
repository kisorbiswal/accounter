package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.data.ClientUser;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class UsersView extends BaseView<ClientUser> {

	private HTML generalSettingsHTML, usersHtml, helpHtml;
	private VerticalPanel mainLayPanel, usersPanel, recentActivityPanel;
	private DecoratedTabPanel tabPanel;
	private FlexTable flexTable;
	private UsersListGrid usersListGrid;
	private RecentActivityListGrid activityListGrid;

	// private ScrollPanel panel;

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
	}

	private void createControls() {

		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void onUncaughtException(Throwable e) {
				e.printStackTrace();
			}
		});

		mainLayPanel = new VerticalPanel();
		flexTable = new FlexTable();
		// panel = new ScrollPanel();
		generalSettingsHTML = new HTML(
				"<a><font size='1px', color='green'>General Settings</font></a> >");
		generalSettingsHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getGeneralSettingsAction().run(null,
						false);
			}
		});
		usersHtml = new HTML("<b><font size='4px'>Users</font></b>");
		helpHtml = new HTML(
				"<a><font size='1px',color='D26001'>What's this?</font></a>");
		tabPanel = new DecoratedTabPanel();
		tabPanel.add(getUsersPanel(), "Users");
		tabPanel.add(getRecentActivityPanel(), "Recent Activity");
		tabPanel.selectTab(0);

		flexTable.setWidget(0, 0, generalSettingsHTML);
		flexTable.setWidget(1, 0, usersHtml);

		mainLayPanel.add(flexTable);
		mainLayPanel.add(helpHtml);
		mainLayPanel.add(tabPanel);

		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(false);
		cancelButton.setVisible(false);

		add(mainLayPanel);
		// panel.add(mainLayPanel);
	}

	private VerticalPanel getRecentActivityPanel() {
		recentActivityPanel = new VerticalPanel();

		activityListGrid = new RecentActivityListGrid(false);
		activityListGrid.setRecentActivityGridView(this);
		activityListGrid.init();

		recentActivityPanel.add(activityListGrid);

		return recentActivityPanel;
	}

	private VerticalPanel getUsersPanel() {
		usersPanel = new VerticalPanel();

		usersListGrid = new UsersListGrid(false);
		usersListGrid.setUsersView(this);
		usersListGrid.init();
		// ClientIdentity clientIdentity = new ClientIdentity();
		// Map<String, ClientUser> clientMap = clientIdentity.getClientUsers();
		// List<ClientUser> clientList = new ArrayList<ClientUser>();
		// for (int i = 0; i < clientMap.size(); i++) {
		// clientList.add(clientMap.get(i));
		// }
		// usersListGrid.addRecords(clientList);
		usersPanel.add(usersListGrid);

		return usersPanel;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
