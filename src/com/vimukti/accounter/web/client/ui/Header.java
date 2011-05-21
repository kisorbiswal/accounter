package com.vimukti.accounter.web.client.ui;

import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class Header extends HorizontalPanel {

	private Label userName;
	public static Label companyName;

	private HTML logout, help, logo;
	private VerticalPanel panel1, panel2;
	private static VerticalPanel panel3;
	public static String gettingStartedStatus = "Hide Getting Started";
	static MenuBar helpBar;

	public Header() {
		createControls();
	}

	private void createControls() {

		companyName = new Label(FinanceApplication.getCompany().getName());
		companyName.addStyleName("companyName");
		Label welcome = new Label("Welcome ");
		userName = new Label();
		userName.setText(FinanceApplication.getCompanyMessages().userName(
				FinanceApplication.clientIdentity.getDisplayName()));
		userName.addStyleName("userName-style");
		userName.getElement().getStyle().setTextDecoration(
				TextDecoration.UNDERLINE);

		userName.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CompanyActionFactory.getUserDetailsAction().run(null, false);
			}
		});

		logout = new HTML("<a href='/do/logout'>Logout</a>");
		logout.addStyleName("logout-html");
		helpBar = new MenuBar();
		initializeHelpBar();
		helpBar.setStyleName("helpBar");
		help = new HTML(
				"<a href='http://help.accounter.com'><font color='#00A3D3'>Help</font></a>");
		help.addStyleName("help-style");
		help.addStyleName("helpBar");

		logo = new HTML(
				"<div class='logo'><img src='/images/Logo.jpg'><div class='vimutki-text' >Vimukti Technologies Pvt Ltd</div></div>");
		logo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CompanyActionFactory.getCompanyHomeAction().run(null, false);
			}
		});
		Image image = new Image();
		image.setUrl("images/Logo.jpg");
		image.setStyleName("logo");

		panel1 = new VerticalPanel();
		panel1.setWidth("100%");
		panel1.add(logo);

		panel2 = new VerticalPanel();
		panel2.setWidth("85%");
		panel2.add(companyName);

		panel3 = new VerticalPanel();
		panel3.setWidth("85%");
		panel3.addStyleName("logout-help-welcome");
		panel3.add(userName);
		panel3.add(help);
		panel3.add(logout);
		panel3.setCellHorizontalAlignment(panel3, ALIGN_RIGHT);

		this.add(panel1);
		this.setCellHorizontalAlignment(panel1, ALIGN_LEFT);
		this.setCellWidth(panel1, "25%");
		this.add(panel2);
		this.setCellHorizontalAlignment(panel2, ALIGN_CENTER);
		this.setCellWidth(panel2, "50%");
		this.add(panel3);
		this.setCellHorizontalAlignment(panel3, ALIGN_RIGHT);
		this.setCellWidth(panel3, "25%");

		// Element spanEle = DOM.createSpan();
		// spanEle.setInnerText("Vimukti Technologies Pvt Ltd");
		// spanEle.addClassName("vimutki-text");
		// DOM.appendChild(panel1.getElement(), spanEle);

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
		helpMenu.addItem("<a href='http://help.accounter.com'>Help Centre</a>",
				true, new Command() {

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
		panel3.remove(helpBar);
		helpBar = new MenuBar();
		initializeHelpBar();
		helpBar.setStyleName("helpBar");
		panel3.insert(helpBar, 1);
	}
}
