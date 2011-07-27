package com.vimukti.accounter.web.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.vimukti.accounter.web.client.commet.AccounterCometClient;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.company.HelpItem;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.FixedAssetsActionFactory;
import com.vimukti.accounter.web.client.ui.core.PurchaseOrderActionFactory;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;
import com.vimukti.accounter.web.client.ui.core.SalesOrderActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;
import com.vimukti.accounter.web.client.ui.settings.SettingsActionFactory;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

/**
 * 
 * @author Raj Vimal
 * 
 */

public class MainFinanceWindow extends VerticalPanel {

	public static int index;
	private static ScrollPanel rightCanvas;
	private static MainFinanceWindow financeWindow;
	private static ViewManager viewManager;
	private Header header;
	private static boolean isNotDetachableTab;
	private int height;
	private int width;
	public static boolean iscancelEvent;
	private HelpItem item;
	private HorizontalPanel downpanel;
	public Map<String, Action> actions;

	private static Accounter accounter;

	public static String oldToken;
	public static boolean shouldExecuteRun = true;

	public MainFinanceWindow(BaseFinanceWindow parent) {
		initializeActionsWithTokens();
		createControls();
		financeWindow = this;
		sinkEvents(Event.ONMOUSEOVER);
		if (GWT.isScript())
			removeLoadingImage();

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				// TODO Auto-generated method stub
				if (shouldExecuteRun)
					historyChanged(event.getValue());
			}
		});
		oldToken = CompanyActionFactory.getCompanyHomeAction()
				.getHistoryToken();
		HistoryTokenUtils.setPresentToken(
				CompanyActionFactory.getCompanyHomeAction(), null);
		shouldExecuteRun = true;
		handleBackSpaceEvent();
	}

	public MainFinanceWindow(boolean isSales) {
		if (isSales) {
			createSalesControls();
		} else {
			createPurchasesControls();
		}
		if (GWT.isScript())
			removeLoadingImage();
		financeWindow = this;
	}

	private void createPurchasesControls() {
		// setTitle(FinanceApplication.getFinanceUIConstants().bizantraPurchases());

		VerticalPanel vlay = new VerticalPanel();
		// vlay.setSize("100%", "100%");
		vlay.setWidth("100%");

		viewManager = ViewManager.getInstance(true);

		// CompanyActionFactory.getCompanyHomeAction().run(null, false);
		VendorsActionFactory.getPurchaseOrderListAction().run(null, true);
		/**
		 * commented for defiz
		 */
		// RootPanel.get().add(vlay);

		// add(getSalesMenuBar());
		add(viewManager);

		setSize("100%", "100%");
		addStyleName("financeWindow");

	}

	private void createSalesControls() {

		// setTitle(FinanceApplication.getFinanceUIConstants().bizantraSales());


		VerticalPanel vlay = new VerticalPanel();
		vlay.setSize("100%", "100%");

		viewManager = ViewManager.getInstance(true);

		// CompanyActionFactory.getCompanyHomeAction().run(null, false);
		CustomersActionFactory.getSalesOrderListAction().run(null, true);
		/**
		 * commented for defiz
		 */
		// RootPanel.get().add(vlay);

		// add(getSalesMenuBar());
		add(viewManager);

		setSize("100%", "100%");
		addStyleName("financeWindow");

	}

	@SuppressWarnings("unused")
	private Widget getSalesMenuBar() {
		// TODO Auto-generated method stub
		return null;
	}

	private native void removeLoadingImage() /*-{
		var parent = $wnd.document.getElementById('loadingWrapper');
		parent.style.visibility = 'hidden';
	}-*/;

	private void createControls() {
		// setTitle(FinanceApplication.getFinanceUIConstants().bizantraFinance());

		VerticalPanel vlay = new VerticalPanel();
		vlay.setSize("100%", "100%");

		viewManager = ViewManager.getInstance();

		header = new Header();
		CompanyActionFactory.getCompanyHomeAction().run(null, false);
		/**
		 * // * commented for defiz
		 */
		// RootPanel.get().add(vlay);

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.addStyleName("header");
		vpanel.setSize("100%", "100%");
		HorizontalMenuBar hMenuBar = new HorizontalMenuBar();
		vpanel.add(header);
		vpanel.add(hMenuBar);
		add(vpanel);
		add(viewManager);
		Label help = new Label("Help Links");
		help.addStyleName("down-panel");
		if (item == null) {
			item = new HelpItem();
		}
		if (downpanel == null) {
			downpanel = new HorizontalPanel();
			downpanel.setWidth("100%");
			downpanel.getElement().getStyle().setMarginTop(25, Unit.PX);
			Image image = new Image("images/Copyright_synbol.png");
			image.setStyleName("copy-img");
			// Label copyrightLabel = new Label("Copyright");
			HTML companyName = new HTML(
					"<a href='http://www.vimukti.com' target='_new'>Vimukti Technologies Pvt. Ltd.</font></a>");
			// Anchor companyName = new
			// Anchor(" Vimukti Technologies Pvt. Ltd.",
			// true, "www.vimukti.com");
			Label allrightsLabel = new Label("All rights are reserved.");
			// copyrightLabel.setStyleName("down-panel1");
			allrightsLabel.setStyleName("down-panel1");
			companyName.addStyleName("down-panel1");
			Anchor support = new Anchor("Support", true, "/site/support",
					"_blank");
			support.getElement().getStyle().setMarginLeft(6, Unit.PX);
			// downpanel.add(copyrightLabel);
			downpanel.add(image);
			downpanel.add(companyName);
			downpanel.add(allrightsLabel);
			downpanel.add(support);

			// downpanel.setCellWidth(copyrightLabel, "1%");
			// downpanel.setCellWidth(image, "1%");
			// downpanel.setCellWidth(companyNameLabel, "13%");
			downpanel.setCellHorizontalAlignment(companyName, ALIGN_CENTER);
			// downpanel.setCellWidth(allrightsLabel, "10%");
			downpanel.setCellHorizontalAlignment(allrightsLabel, ALIGN_CENTER);
			// downpanel.setCellWidth(support, "1%");
			downpanel.setCellHorizontalAlignment(support, ALIGN_CENTER);
			// downpanel.setCellWidth(feedback, "8%");
			downpanel.setWidth("535px");
		}
		VerticalPanel helppanel = new VerticalPanel();
		helppanel.setWidth("100%");
		helppanel.setStyleName("help-panel");
		// helppanel.add(help);
		// helppanel.add(item);
		helppanel.setSpacing(10);
		helppanel.add(downpanel);
		helppanel.setCellHorizontalAlignment(downpanel, ALIGN_CENTER);
		add(helppanel);
		setCellHorizontalAlignment(helppanel, ALIGN_CENTER);
		// setSize("100%", "100%");
		addStyleName(accounter.getFinanceUIConstants().financeWindow());

		if (UIUtils.isMSIEBrowser()) {
			this.getElement().getStyle().setPaddingTop(0, Unit.PX);
			this.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		}

	}

	public HelpItem getHelpItem() {
		return item;

	}

	public static MainFinanceWindow getInstance() {
		return financeWindow;
	}

	private MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();
		// MenuItem dashBoardMenuitem = menuBar.addItem("DashBoard",
		// getDashBoardCommand());
		// ThemesUtil.insertEmptyChildToMenuBar(menuBar);
		// dashBoardMenuitem.getElement().setTitle(
		// "Click here to download this plugin");

		MenuItem menuitem = menuBar.addItem(Accounter.getFinanceUIConstants()
				.company(), getCompanyMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			menuitem = menuBar.addItem(Accounter.getFinanceUIConstants().vat(),
					getVATMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		menuitem = menuBar
				.addItem(accounter.getFinanceUIConstants().customer(),
						getCustomerMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem(UIUtils.getVendorString(Accounter
				.getVendorsMessages().supplier(), Accounter
				.getVendorsMessages().vendor()), getVendorMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (accounter.getUser().canDoBanking()) {
			menuitem = menuBar.addItem(Accounter.getFinanceUIConstants()
					.banking(), getBankingMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		menuitem = menuBar.addItem("Sales", getSalesSubMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem("Purchases", getPurchaseSubMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		// menuBar.addItem(FinanceApplication.getFinanceUIConstants()
		// .fixedAssets(), getFixedAssetsMenu());
		if (accounter.getUser().canViewReports()) {
			menuitem = menuBar.addItem(Accounter.getFinanceUIConstants()
					.reports(), getReportMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		// menuBar.addItem(FinanceApplication.getFinanceUIConstants().help(),
		// getHelpMenu());
		if (accounter.getUser().canChangeSettings()) {
			menuitem = menuBar.addItem(accounter.getSettingsMessages()
					.settings(), getSettingsMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		//
		// if (!GWT.isScript()) {
		// menuitem = menuBar.addItem(FinanceApplication.getCompanyMessages()
		// .test(), getTestMenu());
		// ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		// }

		menuBar.setAutoOpen(true);
		menuBar.setAnimationEnabled(true);
		return menuBar;
	}

	private CustomMenuBar getSettingsMenu() {
		CustomMenuBar settingsMenuBar = new CustomMenuBar();
		settingsMenuBar.addItem(SettingsActionFactory
				.getGeneralSettingsAction());
		// settingsMenuBar.addItem(SettingsActionFactory.getInventoryItemsAction());
		// settingsMenuBar.addItem(SettingsActionFactory.getChartOfAccountsAction());
		return settingsMenuBar;
	}

	// private Command getSettingsCommand(final int i) {
	// Command settingsCommand = new Command() {
	//
	// @Override
	// public void execute() {
	// switch (i) {
	// case 1:
	// SettingsActionFactory.getGeneralSettingsAction().run(null,
	// false);
	// break;
	// case 2:
	// SettingsActionFactory.getInventoryItemsAction().run(null,
	// false);
	// break;
	// case 3:
	// SettingsActionFactory.getChartOfAccountsAction().run(null,
	// false);
	// break;
	// }
	// }
	// };
	// return settingsCommand;
	// }

	private Command getDashBoardCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				String historyToken = CompanyActionFactory
						.getCompanyHomeAction().getHistoryToken();
				if (!History.getToken().equals(historyToken)) {
					oldToken = History.getToken();
					VendorsActionFactory.getExpensesAction(null)
							.run(null, true);
				}
				CompanyActionFactory.getCompanyHomeAction().run(null, false);
			}
		};
		return dashBoardcmd;
	}

	// private CustomMenuBar getTestMenu() {
	// final GUITest guiTest = new GUITest();
	// CustomMenuBar test = getSubMenu();
	//
	// Command cmd1 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createSupportList();
	//
	// }
	// };
	// CustomMenuItem menuItem1 = new CustomMenuItem(Accounter
	// .getFinanceMessages().supportLists(), cmd1);
	// test.addItem(menuItem1);
	//
	// Command cmd2 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createAccounts();
	//
	// }
	// };
	// CustomMenuItem menuItem2 = new CustomMenuItem(Accounter
	// .getFinanceMessages().accounts(), cmd2);
	// test.addItem(menuItem2);
	//
	// CustomMenuBar customerMenu = getSubMenu();
	// Command cmd3 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createCustomers();
	//
	// }
	// };
	//
	// CustomMenuItem menuItem3 = new CustomMenuItem(Accounter
	// .getFinanceMessages().customers(), cmd3);
	// customerMenu.addItem(menuItem3);
	//
	// Command cmd4 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createCustomerIems();
	//
	// }
	// };
	// CustomMenuItem menuItem4 = new CustomMenuItem(Accounter
	// .getFinanceMessages().customerItems(), cmd4);
	// customerMenu.addItem(menuItem4);
	//
	// Command cmd5 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createQuotes();
	//
	// }
	// };
	// CustomMenuItem menuItem5 = new CustomMenuItem(Accounter
	// .getFinanceMessages().quotes(), cmd5);
	// customerMenu.addItem(menuItem5);
	//
	// Command cmd6 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createInvoices();
	//
	// }
	// };
	// CustomMenuItem menuItem6 = new CustomMenuItem(Accounter
	// .getFinanceMessages().invoices(), cmd6);
	// customerMenu.addItem(menuItem6);
	//
	// Command cmd7 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createCashSales();
	//
	// }
	// };
	// CustomMenuItem menuItem7 = new CustomMenuItem(Accounter
	// .getFinanceMessages().cashSales(), cmd7);
	// customerMenu.addItem(menuItem7);
	//
	// Command cmd8 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createCustomerCredits();
	//
	// }
	// };
	// CustomMenuItem menuItem8 = new CustomMenuItem(Accounter
	// .getFinanceMessages().customerCredit(), cmd8);
	// customerMenu.addItem(menuItem8);
	//
	// Command cmd9 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createCustomerRefunds();
	//
	// }
	// };
	// CustomMenuItem menuItem9 = new CustomMenuItem(Accounter
	// .getFinanceMessages().customerRefunds(), cmd9);
	// customerMenu.addItem(menuItem9);
	//
	// Command cmd10 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createRecievePayments();
	//
	// }
	// };
	// CustomMenuItem menuItem10 = new CustomMenuItem(Accounter
	// .getFinanceMessages().recievePayments(), cmd10);
	// customerMenu.addItem(menuItem10);
	//
	// test.addItem(accounter.getCustomersMessages().customer(), customerMenu);
	//
	// CustomMenuBar vendorMenu = new CustomMenuBar();
	//
	// Command cmd11 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createVendors();
	//
	// }
	// };
	//
	// CustomMenuItem menuItem11 = new CustomMenuItem(UIUtils.getVendorString(
	// accounter.getVendorsMessages().vendors(), accounter
	// .getVendorsMessages().vendors()), cmd11);
	// vendorMenu.addItem(menuItem11);
	//
	// Command cmd12 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createVendorIems();
	//
	// }
	// };
	// CustomMenuItem menuItem12 = new CustomMenuItem(UIUtils.getVendorString(
	// accounter.getFinanceMessages().supplierItems(), accounter
	// .getFinanceMessages().vendorItems()), cmd12);
	// vendorMenu.addItem(menuItem12);
	//
	// Command cmd13 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createCashPurchases();
	//
	// }
	// };
	// CustomMenuItem menuItem13 = new CustomMenuItem(Accounter
	// .getFinanceMessages().cashPurchases(), cmd13);
	// vendorMenu.addItem(menuItem13);
	//
	// Command cmd14 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createVendorCreditMemo();
	//
	// }
	// };
	// CustomMenuItem menuItem14 = new CustomMenuItem(UIUtils.getVendorString(
	// accounter.getFinanceMessages().supplierMemo(), accounter
	// .getFinanceMessages().vendorMemo()), cmd14);
	// vendorMenu.addItem(menuItem14);
	//
	// Command cmd15 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createEnterBill();
	// }
	// };
	// CustomMenuItem menuItem15 = new CustomMenuItem(Accounter
	// .getFinanceMessages().enterBills(), cmd15);
	// vendorMenu.addItem(menuItem15);
	//
	// Command cmd16 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createItemReceipt();
	// }
	// };
	// CustomMenuItem menuItem16 = new CustomMenuItem(Accounter
	// .getFinanceMessages().itemReciepts(), cmd16);
	// vendorMenu.addItem(menuItem16);
	//
	// Command cmd17 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createVendorPayment();
	// }
	// };
	// CustomMenuItem menuItem17 = new CustomMenuItem(UIUtils.getVendorString(
	// accounter.getFinanceMessages().supplierPayment(), accounter
	// .getFinanceMessages().vendorPayment()), cmd17);
	// vendorMenu.addItem(menuItem17);
	//
	// Command cmd18 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.creatVendorPayBills();
	// }
	// };
	// CustomMenuItem menuItem18 = new CustomMenuItem(Accounter
	// .getFinanceMessages().payBills(), cmd18);
	// vendorMenu.addItem(menuItem18);
	//
	// Command cmd19 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createIssuePayments();
	// }
	// };
	// CustomMenuItem menuItem19 = new CustomMenuItem(Accounter
	// .getFinanceMessages().issuePayments(), cmd19);
	// vendorMenu.addItem(menuItem19);
	//
	// test.addItem(UIUtils.getVendorString(Accounter.getVendorsMessages()
	// .supplier(), Accounter.getVendorsMessages().vendor()),
	// vendorMenu);
	//
	// CustomMenuBar bankMenu = getSubMenu();
	//
	// Command cmd20 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createTransferFunds();
	// }
	// };
	// CustomMenuItem menuItem20 = new CustomMenuItem(Accounter
	// .getFinanceMessages().transferFunds(), cmd20);
	// bankMenu.addItem(menuItem20);
	//
	// Command cmd22 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createMakeDeposite();
	// }
	// };
	// CustomMenuItem menuItem22 = new CustomMenuItem(Accounter
	// .getFinanceMessages().makeDeposits(), cmd22);
	// bankMenu.addItem(menuItem22);
	//
	// Command cmd23 = new Command() {
	//
	// @Override
	// public void execute() {
	// guiTest.createCreditCardCharge();
	// }
	// };
	// CustomMenuItem menuItem23 = new CustomMenuItem(Accounter
	// .getFinanceMessages().creditCardCharge(), cmd23);
	// bankMenu.addItem(menuItem23);
	//
	// test.addItem(accounter.getFinanceMessages().bank(), bankMenu);
	//
	// return test;
	// }

	private CustomMenuBar getFixedAssetsMenu() {
		CustomMenuBar fixedAssetMenu = new CustomMenuBar();
		fixedAssetMenu.addItem(FixedAssetsActionFactory
				.getNewFixedAssetAction());
		fixedAssetMenu.addSeparator();
		fixedAssetMenu.addItem(CompanyActionFactory.getDepriciationAction());

		fixedAssetMenu.addSeparator();

		fixedAssetMenu.addItem(accounter.getFinanceUIConstants()
				.fixedAssetsList(), getFixedAssetsListMenu());

		return fixedAssetMenu;
	}

	private CustomMenuBar getVATMenu() {
		CustomMenuBar vatmenu = getSubMenu();

		CustomMenuBar vatNews = getSubMenu();
		vatNews.addItem(VatActionFactory.getNewVatItemAction());
		// vatNews.addItem(VatActionFactory.getVatGroupAction());
		vatNews.addItem(VatActionFactory.getNewTAXCodeAction());
		vatNews.addItem(VatActionFactory.getNewTAXAgencyAction());
		vatmenu.addItem(accounter.getCustomersMessages().New(), vatNews);
		vatmenu.addSeparator();
		if (accounter.getUser().canDoInvoiceTransactions()) {
			vatmenu.addItem(VatActionFactory.getAdjustTaxAction());
			vatmenu.addItem(VatActionFactory.getFileVatAction());
		}
		// vatmenu.addItem(VatActionFactory.getCreateTaxesAction());
		if (accounter.getUser().canDoBanking()) {
			vatmenu.addItem(VatActionFactory.getpayVATAction());
			vatmenu.addItem(VatActionFactory.getreceiveVATAction());
		}
		vatmenu.addSeparator();

		vatmenu.addItem(accounter.getFinanceUIConstants().vatList(),
				getVATsListMenu());

		return vatmenu;
	}

	private CustomMenuBar getVATsListMenu() {
		CustomMenuBar vatmenus = getSubMenu();
		vatmenus.addItem(VatActionFactory.getVatItemListAction());
		vatmenus.addItem(VatActionFactory.getTAXCodeListAction());
		// vatmenus.addItem(VatActionFactory.getManageVATGroupListAction());

		return vatmenus;
	}

	private CustomMenuBar getFixedAssetsListMenu() {
		CustomMenuBar fixedAssetListMenu = getSubMenu();
		fixedAssetListMenu.addItem(FixedAssetsActionFactory
				.getPendingItemsListAction());
		fixedAssetListMenu.addItem(FixedAssetsActionFactory
				.getRegisteredItemsListAction());

		fixedAssetListMenu.addItem(FixedAssetsActionFactory
				.getSoldDisposedListAction());
		return fixedAssetListMenu;
	}

	private CustomMenuBar getHelpMenu() {
		CustomMenuBar helpMenuBar = getSubMenu();
		// helpMenuBar.addItem(FinanceApplication.getFinanceUIConstants().help(),
		// new Command() {
		// public void execute() {
		// Window.alert("Not implemented yet!");
		// }
		// });
		// helpMenuBar.addItem(CompanyActionFactory.getFinanceLogAction());
		helpMenuBar.setAnimationEnabled(false);
		return helpMenuBar;
	}

	private CustomMenuBar getReportMenu() {
		CustomMenuBar reportMenuBar = getSubMenu();
		// reportMenuBar.addItem(ReportsActionFactory.getReportsHomeAction());
		// reportMenuBar.addSeparator();
		reportMenuBar.addItem(accounter.getFinanceUIConstants()
				.companyAndFinancial(), getCompanyAndFinancialMenu());
		reportMenuBar.addItem(accounter.getFinanceUIConstants()
				.customersAndReceivable(), getCustomersAndReceivableMenu());
		reportMenuBar.addItem(accounter.getFinanceUIConstants().sales(),
				getSalesMenu());
		reportMenuBar.addItem(UIUtils.getVendorString(Accounter
				.getVendorsMessages().suppliersAndPayables(), accounter
				.getVendorsMessages().vendorsAndPayables()),
				getVendorAndPayablesMenu());
		reportMenuBar.addItem(accounter.getFinanceUIConstants().purchase(),
				getPurchaseMenu());
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
		// .banking(), getBankingSubMenu());
		// }
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			reportMenuBar.addItem(accounter.getFinanceUIConstants().vat(),
					getVATReportMenu());
		}
		// reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
		// .salesAndPurchaseOrders(), getSalesAndPurchaseMenu());
		return reportMenuBar;
	}

	// private CustomMenuBar getBankingSubMenu() {
	// CustomMenuBar SubMenuBar = getSubMenu();
	// SubMenuBar.addItem(ReportsActionFactory.getDetailReportAction());
	// SubMenuBar.addItem(ReportsActionFactory.getCheckDetailReport());
	// return SubMenuBar;
	// }

	// private CustomMenuBar getSalesAndPurchaseMenu() {
	// CustomMenuBar salesAndPurchaseMenuBar = new CustomMenuBar();
	// salesAndPurchaseMenuBar.addItem(ReportsActionFactory
	// .getSalesOpenOrderAction());
	// salesAndPurchaseMenuBar.addItem(ReportsActionFactory
	// .getPurchaseOpenOrderAction());
	// return salesAndPurchaseMenuBar;
	// }

	private CustomMenuBar getSalesSubMenu() {
		CustomMenuBar salesMenu = getSubMenu();
		if (accounter.getUser().canDoInvoiceTransactions())
			salesMenu.addItem(SalesOrderActionFactory.getSalesOrderAction());
		if (accounter.getUser().canSeeInvoiceTransactions())
			salesMenu
					.addItem(SalesOrderActionFactory.getSalesOrderListAction());
		if (accounter.getUser().canViewReports())
			salesMenu
					.addItem(SalesOrderActionFactory.getSalesOpenOrderAction());
		return salesMenu;
	}

	private CustomMenuBar getPurchaseSubMenu() {
		CustomMenuBar purchaseMenu = getSubMenu();
		if (accounter.getUser().canDoInvoiceTransactions())
			purchaseMenu.addItem(PurchaseOrderActionFactory
					.getPurchaseOrderAction());
		if (accounter.getUser().canSeeInvoiceTransactions())
			purchaseMenu.addItem(PurchaseOrderActionFactory
					.getPurchaseOrderListAction());
		if (accounter.getUser().canViewReports())
			purchaseMenu.addItem(PurchaseOrderActionFactory
					.getPurchaseOpenOrderListAction());
		return purchaseMenu;
	}

	private CustomMenuBar getVendorAndPayablesMenu() {
		CustomMenuBar vendorAndPayableMenuBar = getSubMenu();
		vendorAndPayableMenuBar.addItem(ReportsActionFactory
				.getAorpAgingSummaryReportAction());
		vendorAndPayableMenuBar.addItem(ReportsActionFactory
				.getAorpAgingDetailAction());
		vendorAndPayableMenuBar.addItem(ReportsActionFactory
				.getVendorTransactionHistoryAction());
		// vendorAndPayableMenuBar.addItem(ReportsActionFactory
		// .getAmountsDueToVendorsAction());
		return vendorAndPayableMenuBar;
	}

	private CustomMenuBar getPurchaseMenu() {
		CustomMenuBar purchaseMenuBar = getSubMenu();
		purchaseMenuBar.addItem(ReportsActionFactory
				.getPurchaseByVendorSummaryAction());
		purchaseMenuBar.addItem(ReportsActionFactory
				.getPurchaseByVendorDetailAction());
		purchaseMenuBar.addItem(ReportsActionFactory
				.getPurchaseByItemSummaryAction());
		purchaseMenuBar.addItem(ReportsActionFactory.getPurchaseByItemAction());
		purchaseMenuBar.addItem(ReportsActionFactory
				.getPurchaseOpenOrderAction());
		return purchaseMenuBar;
	}

	private CustomMenuBar getVATReportMenu() {
		CustomMenuBar vatReportMenuBar = getSubMenu();
		vatReportMenuBar.addItem(ReportsActionFactory
				.getVATSummaryReportAction());
		vatReportMenuBar.addItem(ReportsActionFactory
				.getVATDetailsReportAction());
		vatReportMenuBar.addItem(ReportsActionFactory.getVAT100ReportAction());
		vatReportMenuBar.addItem(ReportsActionFactory
				.getVATUncategorisedAmountsReportAction());
		vatReportMenuBar.addItem(ReportsActionFactory
				.getVATItemSummaryReportAction());
		vatReportMenuBar.addItem(ReportsActionFactory.getECSalesListAction());
		// vatReportMenuBar.addItem(ReportsActionFactory
		// .getReverseChargeListAction());
		return vatReportMenuBar;
	}

	private CustomMenuBar getSalesMenu() {
		CustomMenuBar salesMenuBar = getSubMenu();
		salesMenuBar.addItem(ReportsActionFactory
				.getSalesByCustomerSummaryAction());
		salesMenuBar.addItem(ReportsActionFactory
				.getSalesByCustomerDetailAction());
		salesMenuBar
				.addItem(ReportsActionFactory.getSalesByItemSummaryAction());
		salesMenuBar.addItem(ReportsActionFactory.getSalesByItemDetailAction());
		salesMenuBar.addItem(ReportsActionFactory.getSalesOpenOrderAction());

		return salesMenuBar;
	}

	private CustomMenuBar getCustomersAndReceivableMenu() {
		CustomMenuBar customersAndReceivableMenuBar = getSubMenu();
		customersAndReceivableMenuBar.addItem(ReportsActionFactory
				.getArAgingSummaryReportAction());
		customersAndReceivableMenuBar.addItem(ReportsActionFactory
				.getArAgingDetailAction());
		customersAndReceivableMenuBar.addItem(ReportsActionFactory
				.getStatementReport());
		customersAndReceivableMenuBar.addItem(ReportsActionFactory
				.getCustomerTransactionHistoryAction());
		// customersAndReceivableMenuBar.addItem(ReportsActionFactory
		// .getMostProfitableCustomersAction());

		return customersAndReceivableMenuBar;
	}

	private CustomMenuBar getCompanyAndFinancialMenu() {
		CustomMenuBar companyAndFinancialMenuBar = getSubMenu();
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getProfitAndLossAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getBalanceSheetAction());
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getCashFlowStatementAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getTrialBalanceAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getTransactionDetailByAccountAction());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ReportsActionFactory
					.getGlReportAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getExpenseReportAction());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ReportsActionFactory
					.getSalesTaxLiabilityAction());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ReportsActionFactory
					.getTransactionDetailByTaxItemAction());
		return companyAndFinancialMenuBar;
	}

	private CustomMenuBar getBankingMenu() {
		CustomMenuBar bankingMenuBar = getSubMenu();
		// bankingMenuBar.addItem(BankingActionFactory.getBankingHomeAction());
		// bankingMenuBar.addSeparator();
		bankingMenuBar.addItem(BankingActionFactory.getNewBankAccountAction());
		bankingMenuBar.addSeparator();
		// bankingMenuBar.addItem(BankingActionFactory.getAccountRegisterAction());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			bankingMenuBar.addItem(BankingActionFactory.getWriteChecksAction());

		bankingMenuBar.addItem(BankingActionFactory.getMakeDepositAction());
		// bankingMenuBar.addItem(BankingActionFactory.getTransferFundsAction());
		bankingMenuBar.addItem(VendorsActionFactory.getPayBillsAction());
		// bankingMenuBar.addItem(BankingActionFactory.getEnterPaymentsAction());
		bankingMenuBar.addSeparator();
		bankingMenuBar
				.addItem(BankingActionFactory.getCreditCardChargeAction());
		bankingMenuBar.addSeparator();
		bankingMenuBar.addItem(accounter.getFinanceUIConstants().bankingList(),
				getBankingListMenu());

		return bankingMenuBar;
	}

	private CustomMenuBar getBankingListMenu() {
		CustomMenuBar bankingListMenuBar = getSubMenu();
		// bankingListMenuBar.addItem(BankingActionFactory
		// .getChartsOfAccountsAction());
		bankingListMenuBar.addItem(BankingActionFactory.getPaymentsAction());

		return bankingListMenuBar;
	}

	private CustomMenuBar getVendorMenu() {
		CustomMenuBar vendorMenuBar = getSubMenu();
		vendorMenuBar.addItem(VendorsActionFactory.getVendorsHomeAction());
		if (getNewVendorMenu().menuItems.size() > 0) {
			vendorMenuBar.addSeparator();
			vendorMenuBar.addItem(accounter.getFinanceUIConstants().New(),
					getNewVendorMenu());
		}
		vendorMenuBar.addSeparator();
		if (accounter.getUser().canDoInvoiceTransactions())
			vendorMenuBar.addItem(VendorsActionFactory.getEnterBillsAction());
		if (accounter.getUser().canDoBanking()) {
			vendorMenuBar.addItem(VendorsActionFactory.getPayBillsAction());
			vendorMenuBar
					.addItem(VendorsActionFactory.getIssuePaymentsAction());
			vendorMenuBar.addItem(VendorsActionFactory
					.getNewVendorPaymentAction());
		}
		if (accounter.getUser().canDoInvoiceTransactions()) {
			vendorMenuBar.addItem(VendorsActionFactory
					.getRecordExpensesAction());
			vendorMenuBar.addItem(VendorsActionFactory
					.getExpenseClaimsAction(0));
			// vendorMenuBar.addItem(VendorsActionFactory.getItemReceiptAction());
			vendorMenuBar.addSeparator();
		}
		vendorMenuBar.addItem(UIUtils.getVendorString(Accounter
				.getVendorsMessages().supplierLists(), Accounter
				.getVendorsMessages().vendorLists()), getVendorListMenu());
		return vendorMenuBar;
	}

	private CustomMenuBar getVendorListMenu() {
		CustomMenuBar vendorListMenuBar = getSubMenu();
		vendorListMenuBar.addItem(VendorsActionFactory.getVendorsAction());
		if (accounter.getUser().canSeeInvoiceTransactions()) {
			vendorListMenuBar.addItem(VendorsActionFactory.getItemsAction());
			vendorListMenuBar.addItem(VendorsActionFactory.getBillsAction());
		}
		if (accounter.getUser().canSeeBanking())
			vendorListMenuBar.addItem(VendorsActionFactory
					.getVendorPaymentsAction());

		return vendorListMenuBar;
	}

	private CustomMenuBar getNewVendorMenu() {
		CustomMenuBar newVendorMenuBar = getSubMenu();
		if (accounter.getUser().canDoInvoiceTransactions()) {
			newVendorMenuBar.addItem(VendorsActionFactory.getNewVendorAction());
			newVendorMenuBar.addItem(VendorsActionFactory.getNewItemAction());
		}
		if (accounter.getUser().canDoBanking())
			newVendorMenuBar.addItem(VendorsActionFactory
					.getNewCashPurchaseAction());
		if (accounter.getUser().canDoInvoiceTransactions())
			newVendorMenuBar.addItem(VendorsActionFactory
					.getNewCreditMemoAction());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			if (accounter.getUser().canDoInvoiceTransactions())
				newVendorMenuBar.addItem(VendorsActionFactory
						.getNewCheckAction());

		return newVendorMenuBar;
	}

	private CustomMenuBar getCustomerMenu() {
		CustomMenuBar customerMenuBar = getSubMenu();
		customerMenuBar
				.addItem(CustomersActionFactory.getCustomersHomeAction());
		if (getNewCustomerMenu().menuItems.size() > 0) {
			customerMenuBar.addSeparator();
			customerMenuBar.addItem(accounter.getFinanceUIConstants().New(),
					getNewCustomerMenu());
		}
		customerMenuBar.addSeparator();
		if (accounter.getUser().canDoBanking()) {
			customerMenuBar.addItem(CustomersActionFactory
					.getNewCustomerPaymentAction());
			customerMenuBar.addItem(CustomersActionFactory
					.getReceivePaymentAction());
			customerMenuBar.addItem(CustomersActionFactory
					.getCustomerRefundAction());
			customerMenuBar.addSeparator();
		}
		customerMenuBar.addItem(accounter.getFinanceUIConstants()
				.customerLists(), getCustomerListMenu());
		return customerMenuBar;
	}

	private CustomMenuBar getCustomerListMenu() {
		CustomMenuBar customerListMenuBar = getSubMenu();
		customerListMenuBar
				.addItem(CustomersActionFactory.getCustomersAction());
		if (accounter.getUser().canSeeInvoiceTransactions()) {
			customerListMenuBar
					.addItem(CustomersActionFactory.getItemsAction());
			customerListMenuBar.addItem(CustomersActionFactory
					.getQuotesAction());
			customerListMenuBar.addItem(CustomersActionFactory
					.getInvoicesAction(null));
		}
		if (accounter.getUser().canSeeBanking()) {
			customerListMenuBar.addItem(CustomersActionFactory
					.getReceivedPaymentsAction());
			customerListMenuBar.addItem(CustomersActionFactory
					.getCustomerRefundsAction());
		}

		return customerListMenuBar;
	}

	private CustomMenuBar getNewCustomerMenu() {
		CustomMenuBar newCustomerMenuBar = getSubMenu();
		if (accounter.getUser().canDoInvoiceTransactions()) {
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewCustomerAction());
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewItemAction());
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewQuoteAction());
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewInvoiceAction());
		}
		if (accounter.getUser().canDoBanking())
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewCashSaleAction());
		if (accounter.getUser().canDoInvoiceTransactions())
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewCreditsAndRefundsAction());

		return newCustomerMenuBar;
	}

	private CustomMenuBar getSubMenu() {
		CustomMenuBar subMenu = new CustomMenuBar();
		// {
		// private boolean isCanHide = true;
		//
		// public void onBrowserEvent(Event event) {
		// super.onBrowserEvent(event);
		// switch (DOM.eventGetType(event)) {
		//
		// case Event.ONMOUSEOVER: {
		// MenuItem item = findItem(DOM.eventGetTarget(event));
		// if (item != null) {
		// itemOver(item, true);
		// if (item.getSubMenu() != null
		// && item.getSubMenu() == getShownChildMenu()) {
		// isCanHide = false;
		// } else {
		// isCanHide = true;
		// }
		// }
		//
		// break;
		// }
		// case Event.ONMOUSEOUT:
		// MenuItem item = findItem(DOM.eventGetTarget(event));
		// if ((item != null && item.getSubMenu() != null)) {
		// return;
		// }
		// if (item == null || !isCanHide) {
		// return;
		// }
		// if (DOM.eventGetClientX(event) <= getAbsoluteLeft()) {
		// closeAll();
		// } else if (DOM.eventGetClientX(event) >= getAbsoluteLeft()
		// + getOffsetWidth()) {
		// closeAll();
		// } else if (DOM.eventGetClientY(event) >= getAbsoluteTop()
		// + getOffsetHeight()) {
		// closeAll();
		// }
		// isCanHide = true;
		// break;
		// }
		// }
		//
		// };

		return subMenu;
	}

	private CustomMenuBar getCompanyMenu() {

		CustomMenuBar companyMenuBar = getSubMenu();

		companyMenuBar.addItem("DashBoard", getDashBoardCommand());
		companyMenuBar.addSeparator();

		if (accounter.getUser().canDoBanking())
			companyMenuBar.addItem(CompanyActionFactory
					.getNewJournalEntryAction());
		if (accounter.getUser().canDoInvoiceTransactions()) {
			companyMenuBar.addItem(CompanyActionFactory.getNewAccountAction());
			companyMenuBar.addSeparator();
		}
		// companyMenuBar.addItem(CompanyActionFactory
		// .getCompanyInformationAction());
		if (accounter.getUser().canChangeSettings()) {
			companyMenuBar.addItem(CompanyActionFactory.getPreferencesAction());
			companyMenuBar.addSeparator();
		}
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyMenuBar.addItem(accounter.getFinanceUIConstants().itemTax(),
					getSalesTaxSubmenu());
		companyMenuBar.addItem(accounter.getFinanceUIConstants()
				.manageSupportLists(), getManageSupportListSubmenu());
		if (accounter.getUser().canManageFiscalYears())
			companyMenuBar.addItem(CompanyActionFactory
					.getManageFiscalYearAction());
		companyMenuBar.addSeparator();
		companyMenuBar.addItem(
				accounter.getFinanceUIConstants().companyLists(),
				getCompanyListMenu());

		return companyMenuBar;
	}

	private CustomMenuBar getCompanyListMenu() {
		CustomMenuBar companyListMenuBar = getSubMenu();
		if (accounter.getUser().canSeeInvoiceTransactions())
			companyListMenuBar.addItem(CompanyActionFactory
					.getChartOfAccountsAction());
		if (accounter.getUser().canSeeBanking())
			companyListMenuBar.addItem(CompanyActionFactory
					.getJournalEntriesAction());
		if (accounter.getUser().canSeeInvoiceTransactions())
			companyListMenuBar.addItem(CustomersActionFactory.getItemsAction());
		companyListMenuBar.addItem(CustomersActionFactory.getCustomersAction());
		companyListMenuBar.addItem(VendorsActionFactory.getVendorsAction());
		if (accounter.getUser().canSeeBanking())
			companyListMenuBar
					.addItem(CompanyActionFactory.getPaymentsAction());
		companyListMenuBar.addItem(CustomersActionFactory
				.getSalesPersonAction());
		return companyListMenuBar;
	}

	private CustomMenuBar getManageSupportListSubmenu() {
		CustomMenuBar manageSupportListMenuBar = getSubMenu();
		manageSupportListMenuBar.addItem(CompanyActionFactory
				.getCustomerGroupListAction());
		manageSupportListMenuBar.addItem(CompanyActionFactory
				.getVendorGroupListAction());
		manageSupportListMenuBar.addItem(CompanyActionFactory
				.getPaymentTermListAction());
		manageSupportListMenuBar.addItem(CompanyActionFactory
				.getShippingMethodListAction());
		manageSupportListMenuBar.addItem(CompanyActionFactory
				.getShippingTermListAction());
		manageSupportListMenuBar.addItem(CompanyActionFactory
				.getPriceLevelListAction());
		manageSupportListMenuBar.addItem(CompanyActionFactory
				.getItemGroupListAction());
		manageSupportListMenuBar.addItem(CompanyActionFactory
				.getCreditRatingListAction());
		// manageSupportListMenuBar.addItem(CompanyActionFactory.getCountryRegionListAction());
		// manageSupportListMenuBar.addItem(CompanyActionFactory
		// .getFormLayoutsListAction());
		// manageSupportListMenuBar.addItem(CompanyActionFactory
		// .getPayTypeListAction());

		return manageSupportListMenuBar;
	}

	private CustomMenuBar getSalesTaxSubmenu() {
		CustomMenuBar salesTaxMenuBar = getSubMenu();
		salesTaxMenuBar.addItem(CompanyActionFactory
				.getManageSalesTaxGroupsAction());
		// salesTaxMenuBar.addItem(CompanyActionFactory
		// .getManageSalesTaxCodesAction());
		salesTaxMenuBar.addItem(CompanyActionFactory
				.getManageSalesTaxItemsAction());
		if (accounter.getUser().canDoBanking())
			salesTaxMenuBar
					.addItem(CompanyActionFactory.getPaySalesTaxAction());
		if (accounter.getUser().canDoInvoiceTransactions())
			salesTaxMenuBar.addItem(CompanyActionFactory.getAdjustTaxAction());
		// salesTaxMenuBar.addItem(CompanyActionFactory
		// .getViewSalesTaxLiabilityAction());
		// salesTaxMenuBar.addItem(CompanyActionFactory.getTaxItemAction());
		if (accounter.getUser().canDoInvoiceTransactions())
			salesTaxMenuBar.addItem(CompanyActionFactory
					.getNewTAXAgencyAction());
		return salesTaxMenuBar;
	}

	public static ViewManager getViewManager() {
		return viewManager;
	}

	public void fitToSize(int height, int width) {
		@SuppressWarnings("unused")
		BaseView<?> view = viewManager.getContentPanel();

		this.height = height;
		this.width = width - 20;

		// this.setWidth(width - 10 + "px");

		// if (view == null)
		viewManager.fitToSize(height, width - 10 - 15);
		// else {
		// view.setHeightForCanvas(height + "");
		// view.getButtonPanel().setHeight("30px");
		// }

	}

	@Override
	public void onLoad() {
		// ClientCometManager.getInstance().initComet();
		// @SuppressWarnings("unused")
		// BaseView<?> view = viewManager.getContentPanel();
		// viewManager.fitToSize(height, width);

		// setHeight(this.height + "px");
		// if (view == null)
		// viewManager.fitToSize(width - 10, height);
		// else {
		// view.setHeightForCanvas((height * 80.6 / 100) + "");
		// view.getButtonPanel().setHeight("30px");
		// }
		super.onLoad();
		viewManager.fitToSize(this.getOffsetHeight(), 960);
		// if (GWT.isScript())
		AccounterCometClient.start();
		this.getElement().getParentElement()
				.addClassName("main-finance-window");
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:

			break;

		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	/**
	 * Give this MenuBar focus.
	 */
	public void focus() {
		FocusImpl.getFocusImplForPanel().focus(getElement());
	}

	@Override
	protected void onAttach() {
		this.getParent().addStyleName("noScroll");
		super.onAttach();
		isNotDetachableTab = false;
	}

	@Override
	protected void onDetach() {
		this.getParent().removeStyleName("noScroll");
		super.onDetach();

		if (!isNotDetachableTab) {
			// make all static instances to null in finance
			ViewManager.makeAllStaticInstancesNull();
			MainFinanceWindow.makeAllStaticInstancesNull();
			accounter.makeAllStaticInstancesNull();
		}
		isNotDetachableTab = false;

	}

	@Override
	protected void onUnload() {
		super.onUnload();
		AccounterCometClient.cometStop();
		MainFinanceWindow.makeAllViewsStaticstoNull();
	}

	public static void makeAllStaticInstancesNull() {
		financeWindow = null;
		viewManager = null;
		rightCanvas = null;
	}

	public static void setIsnotDetachableTab(boolean b) {
		isNotDetachableTab = b;
	}

	public static void makeAllViewsStaticstoNull() {
		ViewManager.makeAllStaticInstancesNull();
		MainFinanceWindow.makeAllStaticInstancesNull();
		accounter.makeAllStaticInstancesNull();
		MainFinanceWindow.setIsnotDetachableTab(true);
	}

	public void cancelonMouseoverevent(boolean iscancelEvent) {
		MainFinanceWindow.iscancelEvent = iscancelEvent;
	}

	private void handleBackSpaceEvent() {
		Event.addNativePreviewHandler(new NativePreviewHandler() {
			@Override
			public void onPreviewNativeEvent(final NativePreviewEvent event) {
				Event e = Event.as(event.getNativeEvent());

				if (e.getKeyCode() == KeyCodes.KEY_BACKSPACE) {
					if (!defaultPresumtion(e.getEventTarget().toString())) {
						e.preventDefault();
						// viewManager.closeCurrentView();
					}
					return;
				}
			}
		});
	}

	protected boolean defaultPresumtion(String eventTarget) {
		return eventTarget.contains("HTMLInputElement")
				|| eventTarget.contains("HTMLSelectElement")
				|| eventTarget.contains("HTMLTextAreaElement");
	}

	public <T extends IAccounterCore> void historyChanged(String value) {
		if (actions == null || value == null)
			return;


		String historyToken = null;
		long id = 0;

		AccounterCoreType type = null;

		List<Object> list = HistoryTokenUtils.getObject(value);
		historyToken = (String) list.get(0);
		if (list.size() == 3) {
			type = (AccounterCoreType) list.get(1);
			id = Long.parseLong((String) list.get(2));

			// historyToken = value.substring(0, value.indexOf("?"));
			// stringID = value.substring(value.indexOf("?") + 1);
		}

		final Action action = actions.get(historyToken);
		if (action != null) {

			if (type != null && id != 0) {
				AsyncCallback<T> callback = new AsyncCallback<T>() {

					public void onFailure(Throwable caught) {
						if (caught instanceof InvocationException) {
							Accounter
									.showMessage("Your session expired, Please login again to continue");
						} else {
							Accounter.showError("Unable To show the view");
						}
					}

					public void onSuccess(T result) {
						if (result != null) {
							action.run(result, false);
						}
					}

				};

				accounter.createGETService().getObjectById(type, id, callback);


			} else {
				action.run(null, false);
			}
		}
	}

	private void initializeActionsWithTokens() {
		actions = new HashMap<String, Action>();
		actions.put(CompanyActionFactory.getCompanyHomeAction()
				.getHistoryToken(), CompanyActionFactory.getCompanyHomeAction());
		actions.put(CompanyActionFactory.getNewJournalEntryAction()
				.getHistoryToken(), CompanyActionFactory
				.getNewJournalEntryAction());
		actions.put(CompanyActionFactory.getNewAccountAction()
				.getHistoryToken(), CompanyActionFactory.getNewAccountAction());
		actions.put(CompanyActionFactory.getPreferencesAction()
				.getHistoryToken(), CompanyActionFactory.getPreferencesAction());
		actions.put(CompanyActionFactory.getManageSalesTaxGroupsAction()
				.getHistoryToken(), CompanyActionFactory
				.getManageSalesTaxGroupsAction());
		actions.put(CompanyActionFactory.getManageSalesTaxItemsAction()
				.getHistoryToken(), CompanyActionFactory
				.getManageSalesTaxItemsAction());
		actions.put(CompanyActionFactory.getPaySalesTaxAction()
				.getHistoryToken(), CompanyActionFactory.getPaySalesTaxAction());
		actions.put(
				CompanyActionFactory.getAdjustTaxAction().getHistoryToken(),
				CompanyActionFactory.getAdjustTaxAction());
		actions.put(CompanyActionFactory.getNewTAXAgencyAction()
				.getHistoryToken(), CompanyActionFactory
				.getNewTAXAgencyAction());
		actions.put(CompanyActionFactory.getCustomerGroupListAction()
				.getHistoryToken(), CompanyActionFactory
				.getCustomerGroupListAction());
		actions.put(CompanyActionFactory.getVendorGroupListAction()
				.getHistoryToken(), CompanyActionFactory
				.getVendorGroupListAction());
		actions.put(CompanyActionFactory.getPaymentTermListAction()
				.getHistoryToken(), CompanyActionFactory
				.getPaymentTermListAction());
		actions.put(CompanyActionFactory.getShippingMethodListAction()
				.getHistoryToken(), CompanyActionFactory
				.getShippingMethodListAction());
		actions.put(CompanyActionFactory.getShippingTermListAction()
				.getHistoryToken(), CompanyActionFactory
				.getShippingTermListAction());
		actions.put(CompanyActionFactory.getPriceLevelListAction()
				.getHistoryToken(), CompanyActionFactory
				.getPriceLevelListAction());
		actions.put(CompanyActionFactory.getItemGroupListAction()
				.getHistoryToken(), CompanyActionFactory
				.getItemGroupListAction());
		actions.put(CompanyActionFactory.getCreditRatingListAction()
				.getHistoryToken(), CompanyActionFactory
				.getCreditRatingListAction());
		actions.put(CompanyActionFactory.getManageFiscalYearAction()
				.getHistoryToken(), CompanyActionFactory
				.getManageFiscalYearAction());
		actions.put(CompanyActionFactory.getChartOfAccountsAction()
				.getHistoryToken(), CompanyActionFactory
				.getChartOfAccountsAction());
		actions.put(CompanyActionFactory.getJournalEntriesAction()
				.getHistoryToken(), CompanyActionFactory
				.getJournalEntriesAction());

		actions.put(CustomersActionFactory.getItemsAction().getHistoryToken(),
				CustomersActionFactory.getItemsAction());
		actions.put(CustomersActionFactory.getCustomersAction()
				.getHistoryToken(), CustomersActionFactory.getCustomersAction());
		actions.put(VendorsActionFactory.getVendorsAction().getHistoryToken(),
				VendorsActionFactory.getVendorsAction());
		actions.put(CompanyActionFactory.getPaymentsAction().getHistoryToken(),
				CompanyActionFactory.getPaymentsAction());

		actions.put(CustomersActionFactory.getSalesPersonAction()
				.getHistoryToken(), CustomersActionFactory
				.getSalesPersonAction());
		actions.put(VatActionFactory.getNewVatItemAction().getHistoryToken(),
				VatActionFactory.getNewVatItemAction());
		actions.put(VatActionFactory.getNewTAXCodeAction().getHistoryToken(),
				VatActionFactory.getNewTAXCodeAction());
		actions.put(VatActionFactory.getNewTAXAgencyAction().getHistoryToken(),
				VatActionFactory.getNewTAXAgencyAction());
		actions.put(VatActionFactory.getAdjustTaxAction().getHistoryToken(),
				VatActionFactory.getAdjustTaxAction());
		actions.put(VatActionFactory.getFileVatAction().getHistoryToken(),
				VatActionFactory.getFileVatAction());
		actions.put(VatActionFactory.getpayVATAction().getHistoryToken(),
				VatActionFactory.getpayVATAction());
		actions.put(VatActionFactory.getreceiveVATAction().getHistoryToken(),
				VatActionFactory.getreceiveVATAction());
		actions.put(VatActionFactory.getVatItemListAction().getHistoryToken(),
				VatActionFactory.getVatItemListAction());

		actions.put(VatActionFactory.getTAXCodeListAction().getHistoryToken(),
				VatActionFactory.getTAXCodeListAction());

		actions.put(CustomersActionFactory.getCustomersHomeAction()
				.getHistoryToken(), CustomersActionFactory
				.getCustomersHomeAction());
		actions.put(CustomersActionFactory.getNewCustomerAction()
				.getHistoryToken(), CustomersActionFactory
				.getNewCustomerAction());
		actions.put(
				CustomersActionFactory.getNewItemAction().getHistoryToken(),
				CustomersActionFactory.getNewItemAction());
		actions.put(CustomersActionFactory.getNewQuoteAction()
				.getHistoryToken(), CustomersActionFactory.getNewQuoteAction());
		actions.put(CustomersActionFactory.getNewInvoiceAction()
				.getHistoryToken(), CustomersActionFactory
				.getNewInvoiceAction());

		actions.put(CustomersActionFactory.getNewCashSaleAction()
				.getHistoryToken(), CustomersActionFactory
				.getNewCashSaleAction());
		actions.put(CustomersActionFactory.getNewCreditsAndRefundsAction()
				.getHistoryToken(), CustomersActionFactory
				.getNewCreditsAndRefundsAction());
		actions.put(CustomersActionFactory.getNewCustomerPaymentAction()
				.getHistoryToken(), CustomersActionFactory
				.getNewCustomerPaymentAction());
		actions.put(CustomersActionFactory.getReceivePaymentAction()
				.getHistoryToken(), CustomersActionFactory
				.getReceivePaymentAction());
		actions.put(CustomersActionFactory.getCustomerRefundAction()
				.getHistoryToken(), CustomersActionFactory
				.getCustomerRefundAction());
		actions.put(CustomersActionFactory.getCustomersAction()
				.getHistoryToken(), CustomersActionFactory.getCustomersAction());
		actions.put(CustomersActionFactory.getItemsAction().getHistoryToken(),
				CustomersActionFactory.getItemsAction());
		actions.put(CustomersActionFactory.getQuotesAction().getHistoryToken(),
				CustomersActionFactory.getQuotesAction());
		actions.put(CustomersActionFactory.getInvoicesAction(null)
				.getHistoryToken(), CustomersActionFactory
				.getInvoicesAction(null));
		actions.put(CustomersActionFactory.getReceivedPaymentsAction()
				.getHistoryToken(), CustomersActionFactory
				.getReceivedPaymentsAction());
		actions.put(CustomersActionFactory.getCustomerRefundsAction()
				.getHistoryToken(), CustomersActionFactory
				.getCustomerRefundsAction());

		actions.put(VendorsActionFactory.getVendorsHomeAction()
				.getHistoryToken(), VendorsActionFactory.getVendorsHomeAction());
		actions.put(
				VendorsActionFactory.getNewVendorAction().getHistoryToken(),
				VendorsActionFactory.getNewVendorAction());
		actions.put(VendorsActionFactory.getNewItemAction().getHistoryToken(),
				VendorsActionFactory.getNewItemAction());
		actions.put(VendorsActionFactory.getNewCashPurchaseAction()
				.getHistoryToken(), VendorsActionFactory
				.getNewCashPurchaseAction());
		actions.put(VendorsActionFactory.getNewCreditMemoAction()
				.getHistoryToken(), VendorsActionFactory
				.getNewCreditMemoAction());
		actions.put(VendorsActionFactory.getNewCheckAction().getHistoryToken(),
				VendorsActionFactory.getNewCheckAction());
		actions.put(VendorsActionFactory.getEnterBillsAction()
				.getHistoryToken(), VendorsActionFactory.getEnterBillsAction());
		actions.put(VendorsActionFactory.getPayBillsAction().getHistoryToken(),
				VendorsActionFactory.getPayBillsAction());
		actions.put(VendorsActionFactory.getIssuePaymentsAction()
				.getHistoryToken(), VendorsActionFactory
				.getIssuePaymentsAction());
		actions.put(VendorsActionFactory.getNewVendorPaymentAction()
				.getHistoryToken(), VendorsActionFactory
				.getNewVendorPaymentAction());
		actions.put(VendorsActionFactory.getRecordExpensesAction()
				.getHistoryToken(), VendorsActionFactory
				.getRecordExpensesAction());
		actions.put(VendorsActionFactory.getExpenseClaimsAction(0)
				.getHistoryToken(), VendorsActionFactory
				.getExpenseClaimsAction(0));

		actions.put(VendorsActionFactory.getVendorsAction().getHistoryToken(),
				VendorsActionFactory.getVendorsAction());
		actions.put(VendorsActionFactory.getItemsAction().getHistoryToken(),
				VendorsActionFactory.getItemsAction());
		actions.put(VendorsActionFactory.getBillsAction().getHistoryToken(),
				VendorsActionFactory.getBillsAction());
		actions.put(VendorsActionFactory.getVendorPaymentsAction()
				.getHistoryToken(), VendorsActionFactory
				.getVendorPaymentsAction());

		actions.put(BankingActionFactory.getNewBankAccountAction()
				.getHistoryToken(), BankingActionFactory
				.getNewBankAccountAction());
		actions.put(BankingActionFactory.getWriteChecksAction()
				.getHistoryToken(), BankingActionFactory.getWriteChecksAction());
		actions.put(BankingActionFactory.getMakeDepositAction()
				.getHistoryToken(), BankingActionFactory.getMakeDepositAction());
		actions.put(VendorsActionFactory.getPayBillsAction().getHistoryToken(),
				VendorsActionFactory.getPayBillsAction());
		actions.put(BankingActionFactory.getCreditCardChargeAction()
				.getHistoryToken(), BankingActionFactory
				.getCreditCardChargeAction());
		actions.put(BankingActionFactory.getPaymentsAction().getHistoryToken(),
				BankingActionFactory.getPaymentsAction());

		actions.put(SalesOrderActionFactory.getSalesOrderAction()
				.getHistoryToken(), SalesOrderActionFactory
				.getSalesOrderAction());
		actions.put(SalesOrderActionFactory.getSalesOrderListAction()
				.getHistoryToken(), SalesOrderActionFactory
				.getSalesOrderListAction());
		actions.put(SalesOrderActionFactory.getSalesOpenOrderAction()
				.getHistoryToken(), SalesOrderActionFactory
				.getSalesOpenOrderAction());

		actions.put(PurchaseOrderActionFactory.getPurchaseOrderAction()
				.getHistoryToken(), PurchaseOrderActionFactory
				.getPurchaseOrderAction());
		actions.put(PurchaseOrderActionFactory.getPurchaseOrderListAction()
				.getHistoryToken(), PurchaseOrderActionFactory
				.getPurchaseOrderListAction());
		actions.put(PurchaseOrderActionFactory.getPurchaseOpenOrderListAction()
				.getHistoryToken(), PurchaseOrderActionFactory
				.getPurchaseOpenOrderListAction());

		actions.put(ReportsActionFactory.getProfitAndLossAction()
				.getHistoryToken(), ReportsActionFactory
				.getProfitAndLossAction());
		actions.put(ReportsActionFactory.getBalanceSheetAction()
				.getHistoryToken(), ReportsActionFactory
				.getBalanceSheetAction());
		actions.put(ReportsActionFactory.getCashFlowStatementAction()
				.getHistoryToken(), ReportsActionFactory
				.getCashFlowStatementAction());
		actions.put(ReportsActionFactory.getTrialBalanceAction()
				.getHistoryToken(), ReportsActionFactory
				.getTrialBalanceAction());

		actions.put(ReportsActionFactory.getTransactionDetailByAccountAction()
				.getHistoryToken(), ReportsActionFactory
				.getTransactionDetailByAccountAction());
		actions.put(ReportsActionFactory.getGlReportAction().getHistoryToken(),
				ReportsActionFactory.getGlReportAction());
		actions.put(ReportsActionFactory.getExpenseReportAction()
				.getHistoryToken(), ReportsActionFactory
				.getExpenseReportAction());

		actions.put(ReportsActionFactory.getSalesTaxLiabilityAction()
				.getHistoryToken(), ReportsActionFactory
				.getSalesTaxLiabilityAction());
		actions.put(ReportsActionFactory.getTransactionDetailByTaxItemAction()
				.getHistoryToken(), ReportsActionFactory
				.getTransactionDetailByTaxItemAction());
		actions.put(ReportsActionFactory.getArAgingSummaryReportAction()
				.getHistoryToken(), ReportsActionFactory
				.getArAgingSummaryReportAction());
		actions.put(ReportsActionFactory.getArAgingDetailAction()
				.getHistoryToken(), ReportsActionFactory
				.getArAgingDetailAction());
		actions.put(
				ReportsActionFactory.getStatementReport().getHistoryToken(),
				ReportsActionFactory.getStatementReport());
		actions.put(ReportsActionFactory.getCustomerTransactionHistoryAction()
				.getHistoryToken(), ReportsActionFactory
				.getCustomerTransactionHistoryAction());
		actions.put(ReportsActionFactory.getSalesByCustomerSummaryAction()
				.getHistoryToken(), ReportsActionFactory
				.getSalesByCustomerSummaryAction());
		actions.put(ReportsActionFactory.getSalesByCustomerDetailAction()
				.getHistoryToken(), ReportsActionFactory
				.getSalesByCustomerDetailAction());
		actions.put(ReportsActionFactory.getSalesByItemSummaryAction()
				.getHistoryToken(), ReportsActionFactory
				.getSalesByItemSummaryAction());
		actions.put(ReportsActionFactory.getSalesByItemDetailAction()
				.getHistoryToken(), ReportsActionFactory
				.getSalesByItemDetailAction());
		actions.put(ReportsActionFactory.getSalesOpenOrderAction()
				.getHistoryToken(), ReportsActionFactory
				.getSalesOpenOrderAction());

		actions.put(ReportsActionFactory.getAorpAgingSummaryReportAction()
				.getHistoryToken(), ReportsActionFactory
				.getAorpAgingSummaryReportAction());
		actions.put(ReportsActionFactory.getAorpAgingDetailAction()
				.getHistoryToken(), ReportsActionFactory
				.getAorpAgingDetailAction());
		actions.put(ReportsActionFactory.getVendorTransactionHistoryAction()
				.getHistoryToken(), ReportsActionFactory
				.getVendorTransactionHistoryAction());

		actions.put(ReportsActionFactory.getPurchaseByVendorSummaryAction()
				.getHistoryToken(), ReportsActionFactory
				.getPurchaseByVendorSummaryAction());
		actions.put(ReportsActionFactory.getPurchaseByVendorDetailAction()
				.getHistoryToken(), ReportsActionFactory
				.getPurchaseByVendorDetailAction());
		actions.put(ReportsActionFactory.getPurchaseByItemSummaryAction()
				.getHistoryToken(), ReportsActionFactory
				.getPurchaseByItemSummaryAction());
		actions.put(ReportsActionFactory.getPurchaseByItemAction()
				.getHistoryToken(), ReportsActionFactory
				.getPurchaseByItemAction());
		actions.put(ReportsActionFactory.getPurchaseOpenOrderAction()
				.getHistoryToken(), ReportsActionFactory
				.getPurchaseOpenOrderAction());

		actions.put(ReportsActionFactory.getVATSummaryReportAction()
				.getHistoryToken(), ReportsActionFactory
				.getVATSummaryReportAction());
		actions.put(ReportsActionFactory.getVATDetailsReportAction()
				.getHistoryToken(), ReportsActionFactory
				.getVATDetailsReportAction());
		actions.put(ReportsActionFactory.getVAT100ReportAction()
				.getHistoryToken(), ReportsActionFactory
				.getVAT100ReportAction());

		actions.put(ReportsActionFactory
				.getVATUncategorisedAmountsReportAction().getHistoryToken(),
				ReportsActionFactory.getVATUncategorisedAmountsReportAction());
		actions.put(ReportsActionFactory.getVATItemSummaryReportAction()
				.getHistoryToken(), ReportsActionFactory
				.getVATItemSummaryReportAction());
		actions.put(ReportsActionFactory.getECSalesListAction()
				.getHistoryToken(), ReportsActionFactory.getECSalesListAction());

		actions.put(SettingsActionFactory.getGeneralSettingsAction()
				.getHistoryToken(), SettingsActionFactory
				.getGeneralSettingsAction());

		actions.put("bankAccounts", CompanyActionFactory
				.getChartOfAccountsAction(ClientAccount.TYPE_BANK));
		actions.put("cashExpenses", VendorsActionFactory
				.getExpensesAction(accounter.getVendorsMessages().cash()));
		actions.put("creditCardExpenses", VendorsActionFactory
				.getExpensesAction(accounter.getVendorsMessages().creditCard()));
		actions.put("employeeExpenses", VendorsActionFactory
				.getExpensesAction(accounter.getVendorsMessages().employee()));
		actions.put(BankingActionFactory.getAccountRegisterAction()
				.getHistoryToken(), BankingActionFactory
				.getAccountRegisterAction());
		actions.put("overDueInvoices", CustomersActionFactory
				.getInvoicesAction(InvoiceListView.OVER_DUE));
		actions.put(CompanyActionFactory.getUserDetailsAction()
				.getHistoryToken(), CompanyActionFactory.getUserDetailsAction());

	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}
}
