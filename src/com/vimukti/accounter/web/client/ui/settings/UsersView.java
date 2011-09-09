package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class UsersView extends BaseView<ClientUserInfo> {

	private HTML generalSettingsHTML;
	private Label titleLabel;
	private VerticalPanel mainLayPanel, usersPanel, recentActivityPanel;
	private DecoratedTabPanel tabPanel;
	private FlexTable flexTable;
	private UsersListGrid usersListGrid;
	private RecentActivityListGrid activityListGrid;
	Button inviteUserButton;

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		super.initData();
		Accounter.createHomeService().getAllUsers(
				new AccounterAsyncCallback<ArrayList<ClientUserInfo>>() {

					@Override
					public void onResultSuccess(ArrayList<ClientUserInfo> result) {
						usersListGrid.removeLoadingImage();
						usersListGrid.removeAllRecords();
						usersListGrid.setRecords(result);
					}

					@Override
					public void onException(AccounterException caught) {
						usersListGrid.removeLoadingImage();
						Accounter.showError(Accounter.constants()
								.failedtoloadusersList());
					}
				});
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
		generalSettingsHTML = new HTML(Accounter.constants()
				.generalSettingsLabel());
		generalSettingsHTML.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				generalSettingsHTML.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				generalSettingsHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		generalSettingsHTML.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				generalSettingsHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		generalSettingsHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getGeneralSettingsAction().run(null, false);
			}
		});
		titleLabel = new Label(Accounter.constants().usersTitle());

		titleLabel.removeStyleName("gwt-Label");
		titleLabel.setStyleName(Accounter.constants().labelTitle());

		inviteUserButton = new Button(Accounter.constants().inviteUser());
		inviteUserButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getInviteUserAction().run(null, false);
			}
		});
		tabPanel = new DecoratedTabPanel();
		tabPanel.add(getUsersPanel(), Accounter.constants().users());
		tabPanel.add(getRecentActivityPanel(), Accounter.constants()
				.recentActivity());
		tabPanel.selectTab(0);

		// flexTable.setWidget(0, 0, generalSettingsHTML);
		flexTable.setWidget(1, 0, titleLabel);

		mainLayPanel.add(flexTable);
		if (Accounter.getUser().isCanDoUserManagement()) {
			mainLayPanel.add(inviteUserButton);
		}

		mainLayPanel.add(getUsersPanel());
		mainLayPanel.setWidth("100%");

		// saveAndCloseButton.setVisible(false);
		// saveAndNewButton.setVisible(false);
		// cancelButton.setVisible(false);

		add(mainLayPanel);
	}

	private VerticalPanel getRecentActivityPanel() {
		recentActivityPanel = new VerticalPanel();

		activityListGrid = new RecentActivityListGrid(false);
		activityListGrid.setRecentActivityGridView(this);
		activityListGrid.init();

		recentActivityPanel.add(activityListGrid);
		recentActivityPanel.setCellHorizontalAlignment(activityListGrid,
				HasHorizontalAlignment.ALIGN_CENTER);
		activityListGrid.setWidth("90%");
		activityListGrid.getElement().getStyle().setMarginTop(20, Unit.PX);

		return recentActivityPanel;
	}

	private VerticalPanel getUsersPanel() {
		usersPanel = new VerticalPanel();

		usersListGrid = new UsersListGrid(false);
		usersListGrid.setUsersView(this);
		usersListGrid.init();
		usersListGrid.addLoadingImagePanel();
		// usersListGrid
		// .setRecords(FinanceApplication.getCompany().getUsersList());
		usersPanel.add(usersListGrid);
		usersPanel.setCellHorizontalAlignment(usersListGrid,
				HasHorizontalAlignment.ALIGN_CENTER);
		usersListGrid.setWidth("100%");
		usersListGrid.getElement().getStyle().setMarginTop(10, Unit.PX);
		usersPanel.setWidth("100%");

		return usersPanel;
	}

	@Override
	public void print() {
	}

	@Override
	public void printPreview() {
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
	}

	@Override
	public void fitToSize(int height, int width) {
		this.setHeight(height + "");
		usersListGrid.setHeight(height + "");
		activityListGrid.setHeight(height + "");
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().users();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}

	@Override
	public void setFocus() {
		this.inviteUserButton.setFocus(true);

	}

}
