package com.vimukti.accounter.web.client.ui;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class Header extends HorizontalPanel {

	private Image userImage;
	private HorizontalPanel usernamePanel;

	public static Label companyName;

	public static HTML userName;

	private SimplePanel headerLinks;

	private HTML logout, help, logo;

	private VerticalPanel panel1, panel2;
	private VerticalPanel panel3;
	private String gettingStartedStatus = Accounter.constants()
			.hideGettingStarted();
	private MenuBar helpBar;

	/**
	 * Creates new Instance
	 */
	public Header() {
		addStyleName("page_header");
		createControls();
	}

	private void createControls() {

		companyName = new Label(Accounter.getCompany().getDisplayName());
		companyName.addStyleName("companyName");
		userImage = new Image("/images/User.png");
		userImage.getElement().getStyle().setPaddingBottom(4, Unit.PX);
		if (Accounter.getCompany().isConfigured()) {
			userName = new HTML("<a><font color=\"#3299A4\">"
					+ Accounter.messages().userName(
							Accounter.getUser().getFullName()) + "<font></a>");
		} else {
			userName = new HTML("<font color=\"#3299A4\">"
					+ Accounter.messages().userName(
							Accounter.getUser().getFullName()) + "<font>");
		}
		userName.addStyleName("userName-style");
		// userName.getElement().getStyle().setPaddingLeft(5, Unit.PX);

		if (!Accounter.isLoggedInFromDomain()
				&& Accounter.getCompany().isConfigured()) {
			userName.getElement().getStyle()
					.setTextDecoration(TextDecoration.UNDERLINE);
			userName.getElement().getStyle().setCursor(Cursor.POINTER);

			userName.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ActionFactory.getUserDetailsAction().run(null, false);
				}
			});
		}

		logout = new HTML(Accounter.messages().logoutHTML());
		logout.addStyleName("logout-html");
		// logout.setWidth(((Accounter.constants().logout().length() * 4) + 19)+
		// "px");
		helpBar = new MenuBar();
		initializeHelpBar();
		helpBar.setStyleName("helpBar");
		help = new HTML(Accounter.messages().helpHTML());
		// help.setWidth(((Accounter.constants().help().length() * 2) + 19) +
		// "px");
		help.addStyleName("help-style");
		help.addStyleName("helpBar");
		help.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ViewManager viewManager = MainFinanceWindow.getViewManager();
				if (viewManager != null) {
					viewManager.addRemoveHelpPanel();
				} else {
					Window.open("http://help.accounterlive.com", "_blank", "");
				}
			}
		});

		logo = new HTML(
				"<div class='logo'><img src='/images/Accounter_logo_title.png'></div>");
		logo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getCompanyHomeAction().run(null, false);
			}
		});
		Image image = new Image();
		image.setUrl("/images/Accounter_logo_title.png");
		image.setStyleName("logo");

		panel1 = new VerticalPanel();
		panel1.setWidth("100%");
		panel1.add(logo);

		panel2 = new VerticalPanel();
		panel2.add(companyName);
		panel2.setWidth("100%");

		headerLinks = new SimplePanel();
		headerLinks.addStyleName("header_links");

		panel3 = new VerticalPanel();
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
		headerLinks.add(panel3);
		this.add(headerLinks);
		this.setCellHorizontalAlignment(panel3, ALIGN_RIGHT);
		this.setCellWidth(panel3, "25%");

		// Element spanEle = DOM.createSpan();
		// spanEle.setInnerText("Vimukti Technologies Pvt Ltd");
		// spanEle.addClassName("vimutki-text");
		// DOM.appendChild(panel1.getElement(), spanEle);

		this.setWidth("100%");

	}

	public void initializeHelpBar() {
		MenuItem menuItem = helpBar.addItem(Accounter.constants().help(),
				getHelpMenuBar());
		menuItem.getElement().getStyle().setColor("#072027");
		Image child = new Image();
		child.addStyleName("menu_arrow");
		child.setUrl(Accounter.getThemeImages().drop_down_indicator().getURL());
		DOM.insertChild(menuItem.getElement(), child.getElement(), 0);
	}

	private CustomMenuBar getHelpMenuBar() {
		CustomMenuBar helpMenu = new CustomMenuBar();
		helpMenu.addItem(Accounter.messages().helpCenter().asString(), true,
				new Command() {

					@Override
					public void execute() {

					}
				});

		helpMenu.addItem(gettingStartedStatus, new Command() {

			@Override
			public void execute() {
				if (gettingStartedStatus.equals(Accounter.constants()
						.hideGettingStarted())) {
					// DashBoardView.hideGettingStarted();
					changeHelpBarContent(Accounter.constants()
							.showGettingStarted());
				} else {
					// DashBoardView.showGettingStarted();
					changeHelpBarContent(Accounter.constants()
							.hideGettingStarted());
				}
			}
		});
		return helpMenu;
	}

	public void changeHelpBarContent(String gettingStartedStatus) {
		this.gettingStartedStatus = gettingStartedStatus;
		panel3.remove(helpBar);
		helpBar = new MenuBar();
		initializeHelpBar();
		helpBar.setStyleName("helpBar");
		panel3.insert(helpBar, 1);
	}
}
