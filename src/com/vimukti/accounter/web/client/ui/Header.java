package com.vimukti.accounter.web.client.ui;

import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Header extends HorizontalPanel {

	private HorizontalPanel hpanel1;
	private static HorizontalPanel hpanel2;
	private Label l1, userName, companyName;
	private HTML logout, help;
	private VerticalPanel vpanel;
	public static String gettingStartedStatus = "Hide Getting Started";
	static MenuBar helpBar;

	public Header() {
		createControls();
	}

	private void createControls() {
		hpanel1 = new HorizontalPanel();
		hpanel2 = new HorizontalPanel();
		hpanel2.setWidth("100%");
		hpanel1.addStyleName("main-color1");
		hpanel1.setWidth("100%");
		l1 = new Label();
		l1.setText("Demo Company (Global)");
		l1.addStyleName("label2-style");
		companyName = new Label(FinanceApplication.getCompany().getName());
		companyName.addStyleName("companyName");
		Label welcome = new Label("Welcome ");
		userName = new Label();
		userName.setText(FinanceApplication.getCompanyMessages().userName(
				FinanceApplication.getClientIdentity().getDisplayName()));
		userName.addStyleName("userName-style");
		userName.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});
		userName.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				userName.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		userName.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				userName.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
			}
		});

		logout = new HTML("<a href='/do/logout'>Logout</a>");
		logout.addStyleName("logout-html");
		helpBar = new MenuBar();
		initializeHelpBar();
		helpBar.setStyleName("helpBar");
		help = new HTML("<span>Help</span><div class='help-content'></div>");
		help.addStyleName("help-style");
		hpanel1.add(l1);
		hpanel2.setStyleName("help-logout-sep");
		// hpanel2.add(welcome);
		hpanel2.add(userName);
		hpanel2.add(helpBar);
		hpanel2.add(logout);
		HorizontalPanel hPanel3 = new HorizontalPanel();
		hPanel3.add(companyName);
		hPanel3.add(hpanel2);
		hPanel3.setWidth("100%");
		hPanel3.setCellHorizontalAlignment(hpanel2,
				HasHorizontalAlignment.ALIGN_RIGHT);
		vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		vpanel.add(hPanel3);
		vpanel.add(hpanel1);
		this.add(vpanel);
		this.setWidth("100%");

	}

	public static void initializeHelpBar() {
		MenuItem menuItem = helpBar.addItem("Help", getHelpMenuBar());
		menuItem.getElement().getStyle().setColor("#00A3D3");
		Image child = new Image();
		child.addStyleName("menu_arrow");
		child.setUrl(FinanceApplication.getThemeImages().drop_down_indicator()
				.getURL());
		DOM.insertChild(menuItem.getElement(), child.getElement(), 0);
	}

	private static CustomMenuBar getHelpMenuBar() {
		CustomMenuBar helpMenu = new CustomMenuBar();
		helpMenu.addItem("Help Centre", new Command() {

			@Override
			public void execute() {

			}
		});

		helpMenu.addItem(gettingStartedStatus, new Command() {

			@Override
			public void execute() {
				if (gettingStartedStatus.equals("Hide Getting Started")) {
					DashBoard.hideGettingStarted();
					Header.changeHelpBarContent("Show Getting Started");
				} else {
					DashBoard.showGettingStarted();
					Header.changeHelpBarContent("Hide Getting Started");
				}
			}
		});
		return helpMenu;
	}

	public static void changeHelpBarContent(String gettingStartedStatus) {
		Header.gettingStartedStatus = gettingStartedStatus;
		hpanel2.remove(helpBar);
		helpBar = new MenuBar();
		initializeHelpBar();
		helpBar.setStyleName("helpBar");
		hpanel2.insert(helpBar, 1);
	}
}
