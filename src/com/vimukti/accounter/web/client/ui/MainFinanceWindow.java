package com.vimukti.accounter.web.client.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zschech.gwt.comet.client.CometClient;
import net.zschech.gwt.comet.client.CometListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.comet.AccounterCometSerializer;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.company.HelpItem;
import com.vimukti.accounter.web.client.ui.company.PreferencesAction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;

/**
 * 
 * 
 */

public class MainFinanceWindow extends VerticalPanel {

	private static ViewManager viewManager;
	private Header header;
	private int height;
	private int width;
	private HelpItem item;
	public Map<String, Action> actions;

	public MainFinanceWindow() {
		initializeActionsWithTokens();
		createControls();
		sinkEvents(Event.ONMOUSEOVER);
	}

	private Widget getSalesMenuBar() {
		// currently not using anywhere
		return null;
	}

	private void createControls() {
		viewManager = new ViewManager(this);
		header = new Header();

		ClientCompany company = Accounter.getCompany();
		add(header);
		// If company is configured then show the dashboard
		// if (company.isConfigured()) {
		HorizontalMenuBar hMenuBar = new HorizontalMenuBar();
		if (!Accounter.isMacApp()) {
			add(hMenuBar);
		}

		add(viewManager);
		Label help = new Label(Accounter.constants().helpLinks());
		help.addStyleName("down-panel");
		if (item == null) {
			item = new HelpItem();
		}
		addStyleName(Accounter.constants().financeWindow());

		if (UIUtils.isMSIEBrowser()) {
			this.getElement().getStyle().setPaddingTop(0, Unit.PX);
			this.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		}

		ActionFactory.getCompanyHomeAction().run(null, false);
		// } else {
		// // if company is not configured then show the setupwizard
		// SetupWizard setupWizard = new SetupWizard();
		// add(setupWizard);
		// }

	}

	public HelpItem getHelpItem() {
		return item;

		
	}

	private MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();
		// MenuItem dashBoardMenuitem = menuBar.addItem("DashBoard",
		// getDashBoardCommand());
		// ThemesUtil.insertEmptyChildToMenuBar(menuBar);
		// dashBoardMenuitem.getElement().setTitle(
		// "Click here to download this plugin");

		MenuItem menuitem = menuBar.addItem(Accounter.constants().company(),
				getCompanyMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (getCompany().getPreferences().isRegisteredForVAT()) {
			menuitem = menuBar.addItem(Accounter.constants().vat(),
					getVATMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		menuitem = menuBar.addItem(Global.get().customer(), getCustomerMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem(Global.get().Vendor(), getVendorMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (Accounter.getUser().canDoBanking()) {
			menuitem = menuBar.addItem(Accounter.constants().banking(),
					getBankingMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		menuitem = menuBar.addItem(Accounter.constants().sales(),
				getSalesSubMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem(Accounter.constants().purchases(),
				getPurchaseSubMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		// menuBar.addItem(FinanceApplication.constants()
		// .fixedAssets(), getFixedAssetsMenu());
		if (Accounter.getUser().canViewReports()) {
			menuitem = menuBar.addItem(Accounter.constants().reports(),
					getReportMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		// menuBar.addItem(FinanceApplication.constants().help(),
		// getHelpMenu());
		if (Accounter.getUser().canChangeSettings()) {
			menuitem = menuBar.addItem(Accounter.constants().settings(),
					getSettingsMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		//
		// if (!GWT.isScript()) {
		// menuitem = menuBar.addItem(FinanceApplication.constants()
		// .test(), getTestMenu());
		// ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		// }

		menuBar.setAutoOpen(true);
		menuBar.setAnimationEnabled(true);
		return menuBar;
	}

	private CustomMenuBar getSettingsMenu() {
		CustomMenuBar settingsMenuBar = new CustomMenuBar();
		settingsMenuBar.addItem(ActionFactory.getGeneralSettingsAction());
		// settingsMenuBar.addItem(ActionFactory.getInventoryItemsAction());
		// settingsMenuBar.addItem(ActionFactory.getChartOfAccountsAction());
		return settingsMenuBar;
	}

	private Command getDashBoardCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				String historyToken = ActionFactory.getCompanyHomeAction()
						.getHistoryToken();
				if (!History.getToken().equals(historyToken)) {
					ActionFactory.getExpensesAction(null).run(null, true);
				}
				ActionFactory.getCompanyHomeAction().run(null, false);
			}
		};
		return dashBoardcmd;
	}

	private CustomMenuBar getFixedAssetsMenu() {
		CustomMenuBar fixedAssetMenu = new CustomMenuBar();
		fixedAssetMenu.addItem(ActionFactory.getNewFixedAssetAction());
		fixedAssetMenu.addSeparator();
		fixedAssetMenu.addItem(ActionFactory.getDepriciationAction());

		fixedAssetMenu.addSeparator();

		fixedAssetMenu.addItem(Accounter.constants().fixedAssetsList(),
				getFixedAssetsListMenu());

		return fixedAssetMenu;
	}

	private CustomMenuBar getVATMenu() {
		CustomMenuBar vatmenu = getSubMenu();

		CustomMenuBar vatNews = getSubMenu();
		vatNews.addItem(ActionFactory.getNewVatItemAction());
		// vatNews.addItem(ActionFactory.getVatGroupAction());
		vatNews.addItem(ActionFactory.getNewTAXCodeAction());
		vatNews.addItem(ActionFactory.getNewTAXAgencyAction());
		vatmenu.addItem(Accounter.constants().new1(), vatNews);
		vatmenu.addSeparator();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vatmenu.addItem(ActionFactory.getAdjustTaxAction());
			vatmenu.addItem(ActionFactory.getFileVatAction());
		}
		// vatmenu.addItem(ActionFactory.getCreateTaxesAction());
		if (Accounter.getUser().canDoBanking()) {
			vatmenu.addItem(ActionFactory.getpayVATAction());
			vatmenu.addItem(ActionFactory.getreceiveVATAction());
		}
		vatmenu.addSeparator();

		vatmenu.addItem(Accounter.constants().vatList(), getVATsListMenu());

		return vatmenu;
	}

	private CustomMenuBar getVATsListMenu() {
		CustomMenuBar vatmenus = getSubMenu();
		vatmenus.addItem(ActionFactory.getVatItemListAction());
		vatmenus.addItem(ActionFactory.getTAXCodeListAction());
		// vatmenus.addItem(ActionFactory.getManageVATGroupListAction());

		return vatmenus;
	}

	private CustomMenuBar getFixedAssetsListMenu() {
		CustomMenuBar fixedAssetListMenu = getSubMenu();
		fixedAssetListMenu.addItem(ActionFactory.getPendingItemsListAction());
		fixedAssetListMenu
				.addItem(ActionFactory.getRegisteredItemsListAction());

		fixedAssetListMenu.addItem(ActionFactory.getSoldDisposedListAction());
		return fixedAssetListMenu;
	}

	private CustomMenuBar getHelpMenu() {
		CustomMenuBar helpMenuBar = getSubMenu();
		// helpMenuBar.addItem(FinanceApplication.constants().help(),
		// new Command() {
		// public void execute() {
		// Window.alert("Not implemented yet!");
		// }
		// });
		// helpMenuBar.addItem(ActionFactory.getFinanceLogAction());
		helpMenuBar.setAnimationEnabled(false);
		return helpMenuBar;
	}

	private CustomMenuBar getReportMenu() {
		CustomMenuBar reportMenuBar = getSubMenu();
		// reportMenuBar.addItem(ActionFactory.getReportsHomeAction());
		// reportMenuBar.addSeparator();
		reportMenuBar.addItem(Accounter.constants().companyAndFinancial(),
				getCompanyAndFinancialMenu());
		reportMenuBar.addItem(
				Accounter.messages().customersAndReceivable(
						Global.get().Customer()),
				getCustomersAndReceivableMenu());
		reportMenuBar.addItem(Accounter.constants().sales(), getSalesMenu());
		reportMenuBar.addItem(
				Global.get().messages()
						.vendorsAndPayables(Global.get().Vendor()),
				getVendorAndPayablesMenu());
		reportMenuBar.addItem(Accounter.constants().purchase(),
				getPurchaseMenu());
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// reportMenuBar.addItem(FinanceApplication.constants()
		// .banking(), getBankingSubMenu());
		// }
		if (getCompany().getPreferences().isRegisteredForVAT()) {
			reportMenuBar.addItem(Accounter.constants().vat(),
					getVATReportMenu());
		}
		// reportMenuBar.addItem(FinanceApplication.constants()
		// .salesAndPurchaseOrders(), getSalesAndPurchaseMenu());
		return reportMenuBar;
	}

	// private CustomMenuBar getBankingSubMenu() {
	// CustomMenuBar SubMenuBar = getSubMenu();
	// SubMenuBar.addItem(ActionFactory.getDetailReportAction());
	// SubMenuBar.addItem(ActionFactory.getCheckDetailReport());
	// return SubMenuBar;
	// }

	// private CustomMenuBar getSalesAndPurchaseMenu() {
	// CustomMenuBar salesAndPurchaseMenuBar = new CustomMenuBar();
	// salesAndPurchaseMenuBar.addItem(ActionFactory
	// .getSalesOpenOrderAction());
	// salesAndPurchaseMenuBar.addItem(ActionFactory
	// .getPurchaseOpenOrderAction());
	// return salesAndPurchaseMenuBar;
	// }

	private CustomMenuBar getSalesSubMenu() {
		CustomMenuBar salesMenu = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesMenu.addItem(ActionFactory.getSalesOrderAction());
		if (Accounter.getUser().canSeeInvoiceTransactions())
			salesMenu.addItem(ActionFactory.getSalesOrderListAction());
		if (Accounter.getUser().canViewReports())
			salesMenu.addItem(ActionFactory.getSalesOpenOrderAction());
		return salesMenu;
	}

	private CustomMenuBar getPurchaseSubMenu() {
		CustomMenuBar purchaseMenu = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions())
			purchaseMenu.addItem(ActionFactory.getPurchaseOrderAction());
		if (Accounter.getUser().canSeeInvoiceTransactions())
			purchaseMenu.addItem(ActionFactory.getPurchaseOrderListAction());
		if (Accounter.getUser().canViewReports())
			purchaseMenu
					.addItem(ActionFactory.getPurchaseOpenOrderListAction());
		return purchaseMenu;
	}

	private CustomMenuBar getVendorAndPayablesMenu() {
		CustomMenuBar vendorAndPayableMenuBar = getSubMenu();
		vendorAndPayableMenuBar.addItem(ActionFactory
				.getAorpAgingSummaryReportAction());
		vendorAndPayableMenuBar.addItem(ActionFactory
				.getAorpAgingDetailAction());
		vendorAndPayableMenuBar.addItem(ActionFactory
				.getVendorTransactionHistoryAction());
		// vendorAndPayableMenuBar.addItem(ActionFactory
		// .getAmountsDueToVendorsAction());
		return vendorAndPayableMenuBar;
	}

	private CustomMenuBar getPurchaseMenu() {
		CustomMenuBar purchaseMenuBar = getSubMenu();
		purchaseMenuBar.addItem(ActionFactory
				.getPurchaseByVendorSummaryAction());
		purchaseMenuBar
				.addItem(ActionFactory.getPurchaseByVendorDetailAction());
		purchaseMenuBar.addItem(ActionFactory.getPurchaseByItemSummaryAction());
		purchaseMenuBar.addItem(ActionFactory.getPurchaseByItemAction());
		purchaseMenuBar.addItem(ActionFactory.getPurchaseOpenOrderAction());
		return purchaseMenuBar;
	}

	private CustomMenuBar getVATReportMenu() {
		CustomMenuBar vatReportMenuBar = getSubMenu();
		vatReportMenuBar.addItem(ActionFactory.getVATSummaryReportAction());
		vatReportMenuBar.addItem(ActionFactory.getVATDetailsReportAction());
		vatReportMenuBar.addItem(ActionFactory.getVAT100ReportAction());
		vatReportMenuBar.addItem(ActionFactory
				.getVATUncategorisedAmountsReportAction());
		vatReportMenuBar.addItem(ActionFactory.getVATItemSummaryReportAction());
		vatReportMenuBar.addItem(ActionFactory.getECSalesListAction());
		// vatReportMenuBar.addItem(ActionFactory
		// .getReverseChargeListAction());
		return vatReportMenuBar;
	}

	private CustomMenuBar getSalesMenu() {
		CustomMenuBar salesMenuBar = getSubMenu();
		salesMenuBar.addItem(ActionFactory.getSalesByCustomerSummaryAction());
		salesMenuBar.addItem(ActionFactory.getSalesByCustomerDetailAction());
		salesMenuBar.addItem(ActionFactory.getSalesByItemSummaryAction());
		salesMenuBar.addItem(ActionFactory.getSalesByItemDetailAction());
		salesMenuBar.addItem(ActionFactory.getSalesOpenOrderAction());

		return salesMenuBar;
	}

	private CustomMenuBar getCustomersAndReceivableMenu() {
		CustomMenuBar customersAndReceivableMenuBar = getSubMenu();
		customersAndReceivableMenuBar.addItem(ActionFactory
				.getArAgingSummaryReportAction());
		customersAndReceivableMenuBar.addItem(ActionFactory
				.getArAgingDetailAction());
		customersAndReceivableMenuBar.addItem(ActionFactory
				.getStatementReport());
		customersAndReceivableMenuBar.addItem(ActionFactory
				.getCustomerTransactionHistoryAction());
		// customersAndReceivableMenuBar.addItem(ActionFactory
		// .getMostProfitableCustomersAction());

		return customersAndReceivableMenuBar;
	}

	private CustomMenuBar getCompanyAndFinancialMenu() {
		CustomMenuBar companyAndFinancialMenuBar = getSubMenu();
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getProfitAndLossAction());
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getBalanceSheetAction());
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getCashFlowStatementAction());
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getTrialBalanceAction());
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getTransactionDetailByAccountAction());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			companyAndFinancialMenuBar.addItem(ActionFactory
					.getGlReportAction());
		}
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getExpenseReportAction());
		if (getCompany().getPreferences().isChargeSalesTax()) {
			companyAndFinancialMenuBar.addItem(ActionFactory
					.getSalesTaxLiabilityAction());
		}
		if (getCompany().getPreferences().isChargeSalesTax()) {
			companyAndFinancialMenuBar.addItem(ActionFactory
					.getTransactionDetailByTaxItemAction());
		}
		return companyAndFinancialMenuBar;
	}

	private CustomMenuBar getBankingMenu() {
		CustomMenuBar bankingMenuBar = getSubMenu();
		// bankingMenuBar.addItem(ActionFactory.getBankingHomeAction());
		// bankingMenuBar.addSeparator();
		bankingMenuBar.addItem(ActionFactory.getNewBankAccountAction());
		bankingMenuBar.addSeparator();
		// bankingMenuBar.addItem(ActionFactory.getAccountRegisterAction());
		bankingMenuBar.addItem(ActionFactory.getWriteChecksAction());

		bankingMenuBar.addItem(ActionFactory.getMakeDepositAction());
		// bankingMenuBar.addItem(ActionFactory.getTransferFundsAction());
		bankingMenuBar.addItem(ActionFactory.getPayBillsAction());
		// bankingMenuBar.addItem(ActionFactory.getEnterPaymentsAction());
		bankingMenuBar.addSeparator();
		bankingMenuBar.addItem(ActionFactory.getCreditCardChargeAction());
		bankingMenuBar.addSeparator();
		bankingMenuBar.addItem(Accounter.constants().bankingList(),
				getBankingListMenu());

		return bankingMenuBar;
	}

	private CustomMenuBar getBankingListMenu() {
		CustomMenuBar bankingListMenuBar = getSubMenu();
		// bankingListMenuBar.addItem(ActionFactory
		// .getChartsOfAccountsAction());
		bankingListMenuBar.addItem(ActionFactory.getPaymentsAction());

		return bankingListMenuBar;
	}

	private CustomMenuBar getVendorMenu() {
		CustomMenuBar vendorMenuBar = getSubMenu();
		vendorMenuBar.addItem(ActionFactory.getVendorsHomeAction());
		if (getNewVendorMenu().menuItems.size() > 0) {
			vendorMenuBar.addSeparator();
			vendorMenuBar.addItem(Accounter.constants().new1(),
					getNewVendorMenu());
		}
		vendorMenuBar.addSeparator();
		if (Accounter.getUser().canDoInvoiceTransactions())
			vendorMenuBar.addItem(ActionFactory.getEnterBillsAction());
		if (Accounter.getUser().canDoBanking()) {
			vendorMenuBar.addItem(ActionFactory.getPayBillsAction());
			vendorMenuBar.addItem(ActionFactory.getIssuePaymentsAction());
			vendorMenuBar.addItem(ActionFactory.getNewVendorPaymentAction());
		}
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vendorMenuBar.addItem(ActionFactory.getRecordExpensesAction());
			vendorMenuBar.addItem(ActionFactory.getExpenseClaimsAction(0));
			// vendorMenuBar.addItem(ActionFactory.getItemReceiptAction());
			vendorMenuBar.addSeparator();
		}
		vendorMenuBar.addItem(
				Global.get().messages().vendorLists(Global.get().Vendor()),
				getVendorListMenu());
		return vendorMenuBar;
	}

	private CustomMenuBar getVendorListMenu() {
		CustomMenuBar vendorListMenuBar = getSubMenu();
		vendorListMenuBar.addItem(ActionFactory.getVendorsAction());
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			vendorListMenuBar.addItem(ActionFactory.getItemsAction());
			vendorListMenuBar.addItem(ActionFactory.getBillsAction());
		}
		if (Accounter.getUser().canSeeBanking())
			vendorListMenuBar.addItem(ActionFactory.getVendorPaymentsAction());

		return vendorListMenuBar;
	}

	private CustomMenuBar getNewVendorMenu() {
		CustomMenuBar newVendorMenuBar = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			newVendorMenuBar.addItem(ActionFactory.getNewVendorAction());
			newVendorMenuBar.addItem(ActionFactory.getNewItemAction(false));
		}
		if (Accounter.getUser().canDoBanking())
			newVendorMenuBar.addItem(ActionFactory.getNewCashPurchaseAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			newVendorMenuBar.addItem(ActionFactory.getNewCreditMemoAction());
		// if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		// if (Accounter.getUser().canDoInvoiceTransactions())
		// newVendorMenuBar.addItem(ActionFactory.getNewCheckAction());

		return newVendorMenuBar;
	}

	private CustomMenuBar getCustomerMenu() {
		CustomMenuBar customerMenuBar = getSubMenu();
		customerMenuBar.addItem(ActionFactory.getCustomersHomeAction());
		if (getNewCustomerMenu().menuItems.size() > 0) {
			customerMenuBar.addSeparator();
			customerMenuBar.addItem(Accounter.constants().new1(),
					getNewCustomerMenu());
		}
		customerMenuBar.addSeparator();
		if (Accounter.getUser().canDoBanking()) {
			customerMenuBar
					.addItem(ActionFactory.getNewCustomerPaymentAction());
			customerMenuBar.addItem(ActionFactory.getReceivePaymentAction());
			customerMenuBar.addItem(ActionFactory.getCustomerRefundAction());
			customerMenuBar.addSeparator();
		}
		customerMenuBar.addItem(
				Accounter.messages().customerLists(Global.get().Customer()),
				getCustomerListMenu());
		return customerMenuBar;
	}

	private CustomMenuBar getCustomerListMenu() {
		CustomMenuBar customerListMenuBar = getSubMenu();
		customerListMenuBar.addItem(ActionFactory.getCustomersAction());
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			customerListMenuBar.addItem(ActionFactory.getItemsAction());
			customerListMenuBar.addItem(ActionFactory.getQuotesAction());
			customerListMenuBar.addItem(ActionFactory.getInvoicesAction(null));
		}
		if (Accounter.getUser().canSeeBanking()) {
			customerListMenuBar.addItem(ActionFactory
					.getReceivedPaymentsAction());
			customerListMenuBar.addItem(ActionFactory
					.getCustomerRefundsAction());
		}

		return customerListMenuBar;
	}

	private CustomMenuBar getNewCustomerMenu() {
		CustomMenuBar newCustomerMenuBar = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			newCustomerMenuBar.addItem(ActionFactory.getNewCustomerAction());
			newCustomerMenuBar.addItem(ActionFactory.getNewItemAction(true));
			newCustomerMenuBar.addItem(ActionFactory.getNewQuoteAction());
			newCustomerMenuBar.addItem(ActionFactory.getNewInvoiceAction());
		}
		if (Accounter.getUser().canDoBanking())
			newCustomerMenuBar.addItem(ActionFactory.getNewCashSaleAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			newCustomerMenuBar.addItem(ActionFactory
					.getNewCreditsAndRefundsAction());

		return newCustomerMenuBar;
	}

	private CustomMenuBar getSubMenu() {
		CustomMenuBar subMenu = new CustomMenuBar();

		return subMenu;
	}

	private CustomMenuBar getCompanyMenu() {

		CustomMenuBar companyMenuBar = getSubMenu();

		companyMenuBar.addItem(Accounter.constants().dashBoard(),
				getDashBoardCommand());
		companyMenuBar.addSeparator();

		if (Accounter.getUser().canDoBanking())
			companyMenuBar.addItem(ActionFactory.getNewJournalEntryAction());
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			companyMenuBar.addItem(ActionFactory.getNewAccountAction());
			companyMenuBar.addSeparator();
		}
		// companyMenuBar.addItem(ActionFactory
		// .getCompanyInformationAction());
		if (Accounter.getUser().canChangeSettings()) {
			PreferencesAction.CATEGORY = PreferencesAction.COMPANY;
			companyMenuBar.addItem(ActionFactory.getPreferencesAction());
			companyMenuBar.addSeparator();
		}
		if (getCompany().getPreferences().isChargeSalesTax()) {
			companyMenuBar.addItem(Accounter.constants().itemTax(),
					getSalesTaxSubmenu());
		}
		companyMenuBar.addItem(Accounter.constants().manageSupportLists(),
				getManageSupportListSubmenu());
		if (Accounter.getUser().canManageFiscalYears())
			companyMenuBar.addItem(ActionFactory.getManageFiscalYearAction());
		companyMenuBar.addSeparator();
		companyMenuBar.addItem(Accounter.constants().companyLists(),
				getCompanyListMenu());

		return companyMenuBar;
	}

	private CustomMenuBar getCompanyListMenu() {
		CustomMenuBar companyListMenuBar = getSubMenu();
		if (Accounter.getUser().canSeeInvoiceTransactions())
			companyListMenuBar
					.addItem(ActionFactory.getChartOfAccountsAction());
		if (Accounter.getUser().canSeeBanking())
			companyListMenuBar.addItem(ActionFactory.getJournalEntriesAction());
		if (Accounter.getUser().canSeeInvoiceTransactions())
			companyListMenuBar.addItem(ActionFactory.getItemsAction());
		companyListMenuBar.addItem(ActionFactory.getCustomersAction());
		companyListMenuBar.addItem(ActionFactory.getVendorsAction());
		if (Accounter.getUser().canSeeBanking())
			companyListMenuBar.addItem(ActionFactory.getPaymentsAction());
		companyListMenuBar.addItem(ActionFactory.getSalesPersonAction());
		return companyListMenuBar;
	}

	private CustomMenuBar getManageSupportListSubmenu() {
		CustomMenuBar manageSupportListMenuBar = getSubMenu();
		manageSupportListMenuBar.addItem(ActionFactory
				.getCustomerGroupListAction());
		manageSupportListMenuBar.addItem(ActionFactory
				.getVendorGroupListAction());
		manageSupportListMenuBar.addItem(ActionFactory
				.getPaymentTermListAction());
		manageSupportListMenuBar.addItem(ActionFactory
				.getShippingMethodListAction());
		manageSupportListMenuBar.addItem(ActionFactory
				.getShippingTermListAction());
		manageSupportListMenuBar.addItem(ActionFactory
				.getPriceLevelListAction());
		manageSupportListMenuBar
				.addItem(ActionFactory.getItemGroupListAction());
		manageSupportListMenuBar.addItem(ActionFactory
				.getCreditRatingListAction());
		if (getCompany().getPreferences().isClassTrackingEnabled()) {
			manageSupportListMenuBar.addItem(ActionFactory
					.getAccounterClassGroupListAction());
		}
		// manageSupportListMenuBar.addItem(ActionFactory.getCountryRegionListAction());
		// manageSupportListMenuBar.addItem(ActionFactory
		// .getFormLayoutsListAction());
		// manageSupportListMenuBar.addItem(ActionFactory
		// .getPayTypeListAction());

		return manageSupportListMenuBar;
	}

	private CustomMenuBar getSalesTaxSubmenu() {
		CustomMenuBar salesTaxMenuBar = getSubMenu();
		salesTaxMenuBar.addItem(ActionFactory.getManageSalesTaxGroupsAction());
		// salesTaxMenuBar.addItem(ActionFactory
		// .getManageSalesTaxCodesAction());
		salesTaxMenuBar.addItem(ActionFactory.getManageSalesTaxItemsAction());
		if (Accounter.getUser().canDoBanking())
			salesTaxMenuBar.addItem(ActionFactory.getPaySalesTaxAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesTaxMenuBar.addItem(ActionFactory.getAdjustTaxAction());
		// salesTaxMenuBar.addItem(ActionFactory
		// .getViewSalesTaxLiabilityAction());
		// salesTaxMenuBar.addItem(ActionFactory.getTaxItemAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesTaxMenuBar.addItem(ActionFactory.getNewTAXAgencyAction());
		return salesTaxMenuBar;
	}

	public static ViewManager getViewManager() {
		return viewManager;
	}

	public void fitToSize(int height, int width) {

		this.height = height;
		this.width = width - 20;

		viewManager.fitToSize(height, width - 10 - 15);

	}

	@Override
	public void onLoad() {
		// ClientCometManager.getInstance().initComet();
		//
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
		startCometService();
		this.getElement().getParentElement()
				.addClassName("main-finance-window");
	}

	private void startCometService() {
		AccounterCometSerializer serializer = GWT
				.create(AccounterCometSerializer.class);
		CometClient cometClient = new CometClient("/do/comet", serializer,
				new CometListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onMessage(List<? extends Serializable> messages) {
						for (Serializable serializableObj : messages) {
							Accounter.getCompany().processCommand(
									(IAccounterCore) serializableObj);
						}
					}

					@Override
					public void onHeartbeat() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(Throwable exception, boolean connected) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onDisconnected() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onConnected(int heartbeat) {
						// TODO Auto-generated method stub

					}
				});
		cometClient.start();
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
		this.setStyleName("finance_window");
		super.onAttach();
	}

	@Override
	protected void onDetach() {
		this.getParent().removeStyleName("noScroll");
		super.onDetach();

	}

	@Override
	protected void onUnload() {
		super.onUnload();
		// TODO
		// AccounterCometClient.cometStop();
	}

	public <T extends IAccounterCore> void historyChanged(String value) {
		if (actions == null || value == null)
			return;

		HistoryToken historyToken;
		try {
			historyToken = new HistoryToken(value);
		} catch (Exception e) {
			return;
		}

		final Action action = actions.get(historyToken.getToken());
		if (action != null) {

			if (historyToken.getType() != null && historyToken.getValue() != 0) {
				AccounterAsyncCallback<T> callback = new AccounterAsyncCallback<T>() {

					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants()
								.unableToshowtheview());
					}

					public void onResultSuccess(T result) {
						if (result != null) {
							action.setInput(result);
							action.execute();
						}
					}

				};

				Accounter.createGETService().getObjectById(
						historyToken.getType(), historyToken.getValue(),
						callback);

			} else {
				action.run(null, false);
			}
		}
	}

	private void initializeActionsWithTokens() {
		actions = new HashMap<String, Action>();
		actions.put(ActionFactory.getCompanyHomeAction().getHistoryToken(),
				ActionFactory.getCompanyHomeAction());
		actions.put(ActionFactory.getNewJournalEntryAction().getHistoryToken(),
				ActionFactory.getNewJournalEntryAction());
		actions.put(ActionFactory.getNewAccountAction().getHistoryToken(),
				ActionFactory.getNewAccountAction());
		actions.put(ActionFactory.getPreferencesAction().getHistoryToken(),
				ActionFactory.getPreferencesAction());
		actions.put(ActionFactory.getManageSalesTaxGroupsAction()
				.getHistoryToken(), ActionFactory
				.getManageSalesTaxGroupsAction());
		actions.put(ActionFactory.getManageSalesTaxItemsAction()
				.getHistoryToken(), ActionFactory
				.getManageSalesTaxItemsAction());
		actions.put(ActionFactory.getPaySalesTaxAction().getHistoryToken(),
				ActionFactory.getPaySalesTaxAction());
		actions.put(ActionFactory.getAdjustTaxAction().getHistoryToken(),
				ActionFactory.getAdjustTaxAction());
		actions.put(ActionFactory.getNewTAXAgencyAction().getHistoryToken(),
				ActionFactory.getNewTAXAgencyAction());
		actions.put(ActionFactory.getCustomerGroupListAction()
				.getHistoryToken(), ActionFactory.getCustomerGroupListAction());
		actions.put(ActionFactory.getVendorGroupListAction().getHistoryToken(),
				ActionFactory.getVendorGroupListAction());
		actions.put(ActionFactory.getPaymentTermListAction().getHistoryToken(),
				ActionFactory.getPaymentTermListAction());
		actions.put(ActionFactory.getShippingMethodListAction()
				.getHistoryToken(), ActionFactory.getShippingMethodListAction());
		actions.put(
				ActionFactory.getShippingTermListAction().getHistoryToken(),
				ActionFactory.getShippingTermListAction());
		actions.put(ActionFactory.getPriceLevelListAction().getHistoryToken(),
				ActionFactory.getPriceLevelListAction());
		actions.put(ActionFactory.getItemGroupListAction().getHistoryToken(),
				ActionFactory.getItemGroupListAction());
		actions.put(ActionFactory.getAccounterClassGroupListAction()
				.getHistoryToken(), ActionFactory
				.getAccounterClassGroupListAction());
		actions.put(
				ActionFactory.getCreditRatingListAction().getHistoryToken(),
				ActionFactory.getCreditRatingListAction());
		actions.put(
				ActionFactory.getManageFiscalYearAction().getHistoryToken(),
				ActionFactory.getManageFiscalYearAction());
		actions.put(ActionFactory.getChartOfAccountsAction().getHistoryToken(),
				ActionFactory.getChartOfAccountsAction());
		actions.put(ActionFactory.getJournalEntriesAction().getHistoryToken(),
				ActionFactory.getJournalEntriesAction());

		actions.put(ActionFactory.getItemsAction().getHistoryToken(),
				ActionFactory.getItemsAction());
		actions.put(ActionFactory.getCustomersAction().getHistoryToken(),
				ActionFactory.getCustomersAction());
		actions.put(ActionFactory.getVendorsAction().getHistoryToken(),
				ActionFactory.getVendorsAction());
		actions.put(ActionFactory.getPaymentsAction().getHistoryToken(),
				ActionFactory.getPaymentsAction());

		actions.put(ActionFactory.getSalesPersonAction().getHistoryToken(),
				ActionFactory.getSalesPersonAction());
		actions.put(ActionFactory.getNewVatItemAction().getHistoryToken(),
				ActionFactory.getNewVatItemAction());
		actions.put(ActionFactory.getNewTAXCodeAction().getHistoryToken(),
				ActionFactory.getNewTAXCodeAction());
		actions.put(ActionFactory.getNewTAXAgencyAction().getHistoryToken(),
				ActionFactory.getNewTAXAgencyAction());
		actions.put(ActionFactory.getAdjustTaxAction().getHistoryToken(),
				ActionFactory.getAdjustTaxAction());
		actions.put(ActionFactory.getFileVatAction().getHistoryToken(),
				ActionFactory.getFileVatAction());
		actions.put(ActionFactory.getpayVATAction().getHistoryToken(),
				ActionFactory.getpayVATAction());
		actions.put(ActionFactory.getreceiveVATAction().getHistoryToken(),
				ActionFactory.getreceiveVATAction());
		actions.put(ActionFactory.getVatItemListAction().getHistoryToken(),
				ActionFactory.getVatItemListAction());

		actions.put(ActionFactory.getTAXCodeListAction().getHistoryToken(),
				ActionFactory.getTAXCodeListAction());

		actions.put(ActionFactory.getCustomersHomeAction().getHistoryToken(),
				ActionFactory.getCustomersHomeAction());
		actions.put(ActionFactory.getNewCustomerAction().getHistoryToken(),
				ActionFactory.getNewCustomerAction());
		actions.put(ActionFactory.getNewItemAction(true).getHistoryToken(),
				ActionFactory.getNewItemAction(true));
		actions.put(ActionFactory.getNewQuoteAction().getHistoryToken(),
				ActionFactory.getNewQuoteAction());
		actions.put(ActionFactory.getNewInvoiceAction().getHistoryToken(),
				ActionFactory.getNewInvoiceAction());
		actions.put(ActionFactory.getInvoiceBrandingAction().getHistoryToken(),
				ActionFactory.getInvoiceBrandingAction());
		actions.put(ActionFactory.getNewCashSaleAction().getHistoryToken(),
				ActionFactory.getNewCashSaleAction());
		actions.put(ActionFactory.getNewCreditsAndRefundsAction()
				.getHistoryToken(), ActionFactory
				.getNewCreditsAndRefundsAction());
		actions.put(ActionFactory.getNewCustomerPaymentAction()
				.getHistoryToken(), ActionFactory.getNewCustomerPaymentAction());
		actions.put(ActionFactory.getReceivePaymentAction().getHistoryToken(),
				ActionFactory.getReceivePaymentAction());
		actions.put(ActionFactory.getCustomerRefundAction().getHistoryToken(),
				ActionFactory.getCustomerRefundAction());
		actions.put(ActionFactory.getCustomersAction().getHistoryToken(),
				ActionFactory.getCustomersAction());
		actions.put(ActionFactory.getItemsAction().getHistoryToken(),
				ActionFactory.getItemsAction());
		actions.put(ActionFactory.getQuotesAction().getHistoryToken(),
				ActionFactory.getQuotesAction());
		actions.put(ActionFactory.getInvoicesAction(null).getHistoryToken(),
				ActionFactory.getInvoicesAction(null));
		actions.put(
				ActionFactory.getReceivedPaymentsAction().getHistoryToken(),
				ActionFactory.getReceivedPaymentsAction());
		actions.put(ActionFactory.getCustomerRefundsAction().getHistoryToken(),
				ActionFactory.getCustomerRefundsAction());

		actions.put(ActionFactory.getVendorsHomeAction().getHistoryToken(),
				ActionFactory.getVendorsHomeAction());
		actions.put(ActionFactory.getNewVendorAction().getHistoryToken(),
				ActionFactory.getNewVendorAction());
		actions.put(ActionFactory.getNewCashPurchaseAction().getHistoryToken(),
				ActionFactory.getNewCashPurchaseAction());
		actions.put(ActionFactory.getNewCreditMemoAction().getHistoryToken(),
				ActionFactory.getNewCreditMemoAction());
		actions.put(ActionFactory.getNewCheckAction().getHistoryToken(),
				ActionFactory.getNewCheckAction());
		actions.put(ActionFactory.getEnterBillsAction().getHistoryToken(),
				ActionFactory.getEnterBillsAction());
		actions.put(ActionFactory.getPayBillsAction().getHistoryToken(),
				ActionFactory.getPayBillsAction());
		actions.put(ActionFactory.getIssuePaymentsAction().getHistoryToken(),
				ActionFactory.getIssuePaymentsAction());
		actions.put(
				ActionFactory.getNewVendorPaymentAction().getHistoryToken(),
				ActionFactory.getNewVendorPaymentAction());
		actions.put(ActionFactory.getRecordExpensesAction().getHistoryToken(),
				ActionFactory.getRecordExpensesAction());
		actions.put(ActionFactory.getExpenseClaimsAction(0).getHistoryToken(),
				ActionFactory.getExpenseClaimsAction(0));

		actions.put(ActionFactory.getVendorsAction().getHistoryToken(),
				ActionFactory.getVendorsAction());
		actions.put(ActionFactory.getItemsAction().getHistoryToken(),
				ActionFactory.getItemsAction());
		actions.put(ActionFactory.getBillsAction().getHistoryToken(),
				ActionFactory.getBillsAction());
		actions.put(ActionFactory.getVendorPaymentsAction().getHistoryToken(),
				ActionFactory.getVendorPaymentsAction());

		actions.put(ActionFactory.getNewBankAccountAction().getHistoryToken(),
				ActionFactory.getNewBankAccountAction());
		actions.put(ActionFactory.getWriteChecksAction().getHistoryToken(),
				ActionFactory.getWriteChecksAction());
		actions.put(ActionFactory.getMakeDepositAction().getHistoryToken(),
				ActionFactory.getMakeDepositAction());
		actions.put(ActionFactory.getPayBillsAction().getHistoryToken(),
				ActionFactory.getPayBillsAction());
		actions.put(
				ActionFactory.getCreditCardChargeAction().getHistoryToken(),
				ActionFactory.getCreditCardChargeAction());
		actions.put(ActionFactory.getPaymentsAction().getHistoryToken(),
				ActionFactory.getPaymentsAction());

		actions.put(ActionFactory.getSalesOrderAction().getHistoryToken(),
				ActionFactory.getSalesOrderAction());
		actions.put(ActionFactory.getSalesOrderListAction().getHistoryToken(),
				ActionFactory.getSalesOrderListAction());
		actions.put(ActionFactory.getSalesOpenOrderAction().getHistoryToken(),
				ActionFactory.getSalesOpenOrderAction());

		actions.put(ActionFactory.getPurchaseOrderAction().getHistoryToken(),
				ActionFactory.getPurchaseOrderAction());
		actions.put(ActionFactory.getPurchaseOrderListAction()
				.getHistoryToken(), ActionFactory.getPurchaseOrderListAction());
		actions.put(ActionFactory.getPurchaseOpenOrderListAction()
				.getHistoryToken(), ActionFactory
				.getPurchaseOpenOrderListAction());

		actions.put(ActionFactory.getProfitAndLossAction().getHistoryToken(),
				ActionFactory.getProfitAndLossAction());
		actions.put(ActionFactory.getBalanceSheetAction().getHistoryToken(),
				ActionFactory.getBalanceSheetAction());
		actions.put(ActionFactory.getCashFlowStatementAction()
				.getHistoryToken(), ActionFactory.getCashFlowStatementAction());
		actions.put(ActionFactory.getTrialBalanceAction().getHistoryToken(),
				ActionFactory.getTrialBalanceAction());

		actions.put(ActionFactory.getTransactionDetailByAccountAction()
				.getHistoryToken(), ActionFactory
				.getTransactionDetailByAccountAction());
		actions.put(ActionFactory.getGlReportAction().getHistoryToken(),
				ActionFactory.getGlReportAction());
		actions.put(ActionFactory.getExpenseReportAction().getHistoryToken(),
				ActionFactory.getExpenseReportAction());

		actions.put(ActionFactory.getSalesTaxLiabilityAction()
				.getHistoryToken(), ActionFactory.getSalesTaxLiabilityAction());
		actions.put(ActionFactory.getTransactionDetailByTaxItemAction()
				.getHistoryToken(), ActionFactory
				.getTransactionDetailByTaxItemAction());
		actions.put(ActionFactory.getArAgingSummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getArAgingSummaryReportAction());
		actions.put(ActionFactory.getArAgingDetailAction().getHistoryToken(),
				ActionFactory.getArAgingDetailAction());
		actions.put(ActionFactory.getStatementReport().getHistoryToken(),
				ActionFactory.getStatementReport());
		actions.put(ActionFactory.getCustomerTransactionHistoryAction()
				.getHistoryToken(), ActionFactory
				.getCustomerTransactionHistoryAction());
		actions.put(ActionFactory.getSalesByCustomerSummaryAction()
				.getHistoryToken(), ActionFactory
				.getSalesByCustomerSummaryAction());
		actions.put(ActionFactory.getSalesByCustomerDetailAction()
				.getHistoryToken(), ActionFactory
				.getSalesByCustomerDetailAction());
		actions.put(ActionFactory.getSalesByItemSummaryAction()
				.getHistoryToken(), ActionFactory.getSalesByItemSummaryAction());
		actions.put(ActionFactory.getSalesByItemDetailAction()
				.getHistoryToken(), ActionFactory.getSalesByItemDetailAction());
		actions.put(ActionFactory.getSalesOpenOrderAction().getHistoryToken(),
				ActionFactory.getSalesOpenOrderAction());

		actions.put(ActionFactory.getAorpAgingSummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getAorpAgingSummaryReportAction());
		actions.put(ActionFactory.getAorpAgingDetailAction().getHistoryToken(),
				ActionFactory.getAorpAgingDetailAction());
		actions.put(ActionFactory.getVendorTransactionHistoryAction()
				.getHistoryToken(), ActionFactory
				.getVendorTransactionHistoryAction());

		actions.put(ActionFactory.getPurchaseByVendorSummaryAction()
				.getHistoryToken(), ActionFactory
				.getPurchaseByVendorSummaryAction());
		actions.put(ActionFactory.getPurchaseByVendorDetailAction()
				.getHistoryToken(), ActionFactory
				.getPurchaseByVendorDetailAction());
		actions.put(ActionFactory.getPurchaseByItemSummaryAction()
				.getHistoryToken(), ActionFactory
				.getPurchaseByItemSummaryAction());
		actions.put(ActionFactory.getPurchaseByItemAction().getHistoryToken(),
				ActionFactory.getPurchaseByItemAction());
		actions.put(ActionFactory.getPurchaseOpenOrderAction()
				.getHistoryToken(), ActionFactory.getPurchaseOpenOrderAction());

		actions.put(
				ActionFactory.getVATSummaryReportAction().getHistoryToken(),
				ActionFactory.getVATSummaryReportAction());
		actions.put(
				ActionFactory.getVATDetailsReportAction().getHistoryToken(),
				ActionFactory.getVATDetailsReportAction());
		actions.put(ActionFactory.getVAT100ReportAction().getHistoryToken(),
				ActionFactory.getVAT100ReportAction());

		actions.put(ActionFactory.getVATUncategorisedAmountsReportAction()
				.getHistoryToken(), ActionFactory
				.getVATUncategorisedAmountsReportAction());
		actions.put(ActionFactory.getVATItemSummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getVATItemSummaryReportAction());
		actions.put(ActionFactory.getECSalesListAction().getHistoryToken(),
				ActionFactory.getECSalesListAction());

		actions.put(ActionFactory.getGeneralSettingsAction().getHistoryToken(),
				ActionFactory.getGeneralSettingsAction());

		actions.put("bankAccounts",
				ActionFactory.getChartOfAccountsAction(ClientAccount.TYPE_BANK));
		actions.put("cashExpenses",
				ActionFactory.getExpensesAction(Accounter.constants().cash()));
		actions.put("creditCardExpenses", ActionFactory
				.getExpensesAction(Accounter.constants().creditCard()));
		actions.put("employeeExpenses", ActionFactory
				.getExpensesAction(Accounter.constants().employee()));
		actions.put(ActionFactory.getAccountRegisterAction().getHistoryToken(),
				ActionFactory.getAccountRegisterAction());
		actions.put("overDueInvoices",
				ActionFactory.getInvoicesAction(InvoiceListView.OVER_DUE));
		actions.put(ActionFactory.getUserDetailsAction().getHistoryToken(),
				ActionFactory.getUserDetailsAction());
		actions.put(ActionFactory.getPrepare1099MISCAction().getHistoryToken(),
				ActionFactory.getPrepare1099MISCAction());

	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}
}
