package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.IMenuFactory.IMenu;
import com.vimukti.accounter.web.client.ui.IMenuFactory.IMenuBar;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class AccounterMenuBar extends HorizontalPanel {

	private ClientCompanyPreferences preferences = Global.get().preferences();
	private final IMenuFactory factory;
	public static String oldToken;

	public AccounterMenuBar(IMenuFactory factory) {
		this.factory = factory;

		setStyleName("MENU_BAR_BG");

		ICountryPreferences countryPreferences = Accounter.getCompany()
				.getCountryPreferences();

		MenuBar accounterMenuBar = new MenuBar();
		accounterMenuBar.setPreferencesandPermissions(preferences,
				Accounter.getUser(), countryPreferences,
				Accounter.getFeatures());

		generateMenu(accounterMenuBar.getMenus());
	}

	private void generateMenu(List<Menu> menus) {

		IMenuBar menuBar = factory.createMenuBar();

		for (Menu menu : menus) {

			IMenu menuMain = factory.createMenu();

			for (MenuItem menuItem : menu.getMenuItems()) {

				String title = menuItem.getTitle();
				String urlToken = menuItem.getUrlToken();

				if (title == null) {
					menuMain.addSeparatorItem();
				} else if (menuItem instanceof Menu) {

					Menu subMenu = (Menu) menuItem;
					menuMain.addMenuItem(title, getSubMenu(subMenu));

				} else {
					UrlCommand cmd = new UrlCommand(urlToken);
					menuMain.addMenuItem(title, cmd);
				}
			}
			menuBar.addMenuItem(menu.getTitle(), menuMain);
		}
		add(menuBar);
	}

	private IMenu getSubMenu(Menu subMenu) {

		IMenu submenu = factory.createMenu();
		for (MenuItem subMenuItem : subMenu.getMenuItems()) {

			String subTitle = subMenuItem.getTitle();
			String subUrlToken = subMenuItem.getUrlToken();

			if (subTitle == null) {
				submenu.addSeparatorItem();
			} else if (subMenuItem instanceof Menu) {

				Menu anotherSubMenu = (Menu) subMenuItem;
				submenu.addMenuItem(subTitle, getSubMenu(anotherSubMenu));

			} else {
				UrlCommand cmd = new UrlCommand(subUrlToken);
				submenu.addMenuItem(subTitle, cmd);
			}
		}
		return submenu;
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

}
