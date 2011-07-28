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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
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
 * 
 */

public class MainFinanceWindow extends VerticalPanel {

	private static ViewManager viewManager;
	private Header header;
	private int height;
	private int width;
	private HelpItem item;
	public Map<String, Action> actions;

	private static Accounter accounter;

	public static String oldToken;
	public static boolean shouldExecuteRun = true;

	public MainFinanceWindow() {
		initializeActionsWithTokens();
		createControls();
		sinkEvents(Event.ONMOUSEOVER);
		if (GWT.isScript())
			removeLoadingImage();

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

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


	
	private Widget getSalesMenuBar() {
		// currently not using anywhere
		return null;
	}

	private native void removeLoadingImage() /*-{
		var parent = $wnd.document.getElementById('loadingWrapper');
		parent.style.visibility = 'hidden';
	}-*/;

	private void createControls() {

		viewManager = ViewManager.getInstance();

		header = new Header();
		CompanyActionFactory.getCompanyHomeAction().run(null, false);

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
		addStyleName(Accounter.constants().financeWindow());

		if (UIUtils.isMSIEBrowser()) {
			this.getElement().getStyle().setPaddingTop(0, Unit.PX);
			this.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		}

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

		MenuItem menuitem = menuBar.addItem(Accounter.constants()
				.company(), getCompanyMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			menuitem = menuBar.addItem(Accounter.constants().VAT(),
					getVATMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		menuitem = menuBar
				.addItem(Accounter.constants().customer(),
						getCustomerMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem(UIUtils.getVendorString(Accounter
				.constants().supplier(), Accounter
				.constants().vendor()), getVendorMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (Accounter.getUser().canDoBanking()) {
			menuitem = menuBar.addItem(Accounter.constants()
					.banking(), getBankingMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		menuitem = menuBar.addItem("Sales", getSalesSubMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem("Purchases", getPurchaseSubMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		// menuBar.addItem(FinanceApplication.constants()
		// .fixedAssets(), getFixedAssetsMenu());
		if (Accounter.getUser().canViewReports()) {
			menuitem = menuBar.addItem(Accounter.constants()
					.reports(), getReportMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		// menuBar.addItem(FinanceApplication.constants().help(),
		// getHelpMenu());
		if (Accounter.getUser().canChangeSettings()) {
			menuitem = menuBar.addItem(Accounter.constants()
					.settings(), getSettingsMenu());
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
		settingsMenuBar.addItem(SettingsActionFactory
				.getGeneralSettingsAction());
		// settingsMenuBar.addItem(SettingsActionFactory.getInventoryItemsAction());
		// settingsMenuBar.addItem(SettingsActionFactory.getChartOfAccountsAction());
		return settingsMenuBar;
	}


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


	private CustomMenuBar getFixedAssetsMenu() {
		CustomMenuBar fixedAssetMenu = new CustomMenuBar();
		fixedAssetMenu.addItem(FixedAssetsActionFactory
				.getNewFixedAssetAction());
		fixedAssetMenu.addSeparator();
		fixedAssetMenu.addItem(CompanyActionFactory.getDepriciationAction());

		fixedAssetMenu.addSeparator();

		fixedAssetMenu.addItem(Accounter.constants()
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
		vatmenu.addItem(Accounter.constants().New(), vatNews);
		vatmenu.addSeparator();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vatmenu.addItem(VatActionFactory.getAdjustTaxAction());
			vatmenu.addItem(VatActionFactory.getFileVatAction());
		}
		// vatmenu.addItem(VatActionFactory.getCreateTaxesAction());
		if (Accounter.getUser().canDoBanking()) {
			vatmenu.addItem(VatActionFactory.getpayVATAction());
			vatmenu.addItem(VatActionFactory.getreceiveVATAction());
		}
		vatmenu.addSeparator();

		vatmenu.addItem(Accounter.constants().VATList(),
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
		// helpMenuBar.addItem(FinanceApplication.constants().help(),
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
		reportMenuBar.addItem(Accounter.constants()
				.companyAndFinancial(), getCompanyAndFinancialMenu());
		reportMenuBar.addItem(Accounter.constants()
				.customersAndReceivable(), getCustomersAndReceivableMenu());
		reportMenuBar.addItem(Accounter.constants().sales(),
				getSalesMenu());
		reportMenuBar.addItem(UIUtils.getVendorString(Accounter
				.constants().suppliersAndPayables(), Accounter
				.constants().vendorsAndPayables()),
				getVendorAndPayablesMenu());
		reportMenuBar.addItem(Accounter.constants().purchase(),
				getPurchaseMenu());
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// reportMenuBar.addItem(FinanceApplication.constants()
		// .banking(), getBankingSubMenu());
		// }
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			reportMenuBar.addItem(Accounter.constants().VAT(),
					getVATReportMenu());
		}
		// reportMenuBar.addItem(FinanceApplication.constants()
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
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesMenu.addItem(SalesOrderActionFactory.getSalesOrderAction());
		if (Accounter.getUser().canSeeInvoiceTransactions())
			salesMenu
					.addItem(SalesOrderActionFactory.getSalesOrderListAction());
		if (Accounter.getUser().canViewReports())
			salesMenu
					.addItem(SalesOrderActionFactory.getSalesOpenOrderAction());
		return salesMenu;
	}

	private CustomMenuBar getPurchaseSubMenu() {
		CustomMenuBar purchaseMenu = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions())
			purchaseMenu.addItem(PurchaseOrderActionFactory
					.getPurchaseOrderAction());
		if (Accounter.getUser().canSeeInvoiceTransactions())
			purchaseMenu.addItem(PurchaseOrderActionFactory
					.getPurchaseOrderListAction());
		if (Accounter.getUser().canViewReports())
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
		bankingMenuBar.addItem(Accounter.constants().bankingList(),
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
			vendorMenuBar.addItem(Accounter.constants().New(),
					getNewVendorMenu());
		}
		vendorMenuBar.addSeparator();
		if (Accounter.getUser().canDoInvoiceTransactions())
			vendorMenuBar.addItem(VendorsActionFactory.getEnterBillsAction());
		if (Accounter.getUser().canDoBanking()) {
			vendorMenuBar.addItem(VendorsActionFactory.getPayBillsAction());
			vendorMenuBar
					.addItem(VendorsActionFactory.getIssuePaymentsAction());
			vendorMenuBar.addItem(VendorsActionFactory
					.getNewVendorPaymentAction());
		}
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vendorMenuBar.addItem(VendorsActionFactory
					.getRecordExpensesAction());
			vendorMenuBar.addItem(VendorsActionFactory
					.getExpenseClaimsAction(0));
			// vendorMenuBar.addItem(VendorsActionFactory.getItemReceiptAction());
			vendorMenuBar.addSeparator();
		}
		vendorMenuBar.addItem(UIUtils.getVendorString(Accounter
				.constants().supplierLists(), Accounter
				.constants().vendorLists()), getVendorListMenu());
		return vendorMenuBar;
	}

	private CustomMenuBar getVendorListMenu() {
		CustomMenuBar vendorListMenuBar = getSubMenu();
		vendorListMenuBar.addItem(VendorsActionFactory.getVendorsAction());
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			vendorListMenuBar.addItem(VendorsActionFactory.getItemsAction());
			vendorListMenuBar.addItem(VendorsActionFactory.getBillsAction());
		}
		if (Accounter.getUser().canSeeBanking())
			vendorListMenuBar.addItem(VendorsActionFactory
					.getVendorPaymentsAction());

		return vendorListMenuBar;
	}

	private CustomMenuBar getNewVendorMenu() {
		CustomMenuBar newVendorMenuBar = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			newVendorMenuBar.addItem(VendorsActionFactory.getNewVendorAction());
			newVendorMenuBar.addItem(VendorsActionFactory.getNewItemAction());
		}
		if (Accounter.getUser().canDoBanking())
			newVendorMenuBar.addItem(VendorsActionFactory
					.getNewCashPurchaseAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			newVendorMenuBar.addItem(VendorsActionFactory
					.getNewCreditMemoAction());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			if (Accounter.getUser().canDoInvoiceTransactions())
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
			customerMenuBar.addItem(Accounter.constants().New(),
					getNewCustomerMenu());
		}
		customerMenuBar.addSeparator();
		if (Accounter.getUser().canDoBanking()) {
			customerMenuBar.addItem(CustomersActionFactory
					.getNewCustomerPaymentAction());
			customerMenuBar.addItem(CustomersActionFactory
					.getReceivePaymentAction());
			customerMenuBar.addItem(CustomersActionFactory
					.getCustomerRefundAction());
			customerMenuBar.addSeparator();
		}
		customerMenuBar.addItem(Accounter.constants()
				.customerLists(), getCustomerListMenu());
		return customerMenuBar;
	}

	private CustomMenuBar getCustomerListMenu() {
		CustomMenuBar customerListMenuBar = getSubMenu();
		customerListMenuBar
				.addItem(CustomersActionFactory.getCustomersAction());
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			customerListMenuBar
					.addItem(CustomersActionFactory.getItemsAction());
			customerListMenuBar.addItem(CustomersActionFactory
					.getQuotesAction());
			customerListMenuBar.addItem(CustomersActionFactory
					.getInvoicesAction(null));
		}
		if (Accounter.getUser().canSeeBanking()) {
			customerListMenuBar.addItem(CustomersActionFactory
					.getReceivedPaymentsAction());
			customerListMenuBar.addItem(CustomersActionFactory
					.getCustomerRefundsAction());
		}

		return customerListMenuBar;
	}

	private CustomMenuBar getNewCustomerMenu() {
		CustomMenuBar newCustomerMenuBar = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewCustomerAction());
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewItemAction());
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewQuoteAction());
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewInvoiceAction());
		}
		if (Accounter.getUser().canDoBanking())
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewCashSaleAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			newCustomerMenuBar.addItem(CustomersActionFactory
					.getNewCreditsAndRefundsAction());

		return newCustomerMenuBar;
	}

	private CustomMenuBar getSubMenu() {
		CustomMenuBar subMenu = new CustomMenuBar();
		

		return subMenu;
	}

	private CustomMenuBar getCompanyMenu() {

		CustomMenuBar companyMenuBar = getSubMenu();

		companyMenuBar.addItem("DashBoard", getDashBoardCommand());
		companyMenuBar.addSeparator();

		if (Accounter.getUser().canDoBanking())
			companyMenuBar.addItem(CompanyActionFactory
					.getNewJournalEntryAction());
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			companyMenuBar.addItem(CompanyActionFactory.getNewAccountAction());
			companyMenuBar.addSeparator();
		}
		// companyMenuBar.addItem(CompanyActionFactory
		// .getCompanyInformationAction());
		if (Accounter.getUser().canChangeSettings()) {
			companyMenuBar.addItem(CompanyActionFactory.getPreferencesAction());
			companyMenuBar.addSeparator();
		}
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyMenuBar.addItem(Accounter.constants().itemTax(),
					getSalesTaxSubmenu());
		companyMenuBar.addItem(Accounter.constants()
				.manageSupportLists(), getManageSupportListSubmenu());
		if (Accounter.getUser().canManageFiscalYears())
			companyMenuBar.addItem(CompanyActionFactory
					.getManageFiscalYearAction());
		companyMenuBar.addSeparator();
		companyMenuBar.addItem(
				Accounter.constants().companyLists(),
				getCompanyListMenu());

		return companyMenuBar;
	}

	private CustomMenuBar getCompanyListMenu() {
		CustomMenuBar companyListMenuBar = getSubMenu();
		if (Accounter.getUser().canSeeInvoiceTransactions())
			companyListMenuBar.addItem(CompanyActionFactory
					.getChartOfAccountsAction());
		if (Accounter.getUser().canSeeBanking())
			companyListMenuBar.addItem(CompanyActionFactory
					.getJournalEntriesAction());
		if (Accounter.getUser().canSeeInvoiceTransactions())
			companyListMenuBar.addItem(CustomersActionFactory.getItemsAction());
		companyListMenuBar.addItem(CustomersActionFactory.getCustomersAction());
		companyListMenuBar.addItem(VendorsActionFactory.getVendorsAction());
		if (Accounter.getUser().canSeeBanking())
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
		if (Accounter.getUser().canDoBanking())
			salesTaxMenuBar
					.addItem(CompanyActionFactory.getPaySalesTaxAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesTaxMenuBar.addItem(CompanyActionFactory.getAdjustTaxAction());
		// salesTaxMenuBar.addItem(CompanyActionFactory
		// .getViewSalesTaxLiabilityAction());
		// salesTaxMenuBar.addItem(CompanyActionFactory.getTaxItemAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesTaxMenuBar.addItem(CompanyActionFactory
					.getNewTAXAgencyAction());
		return salesTaxMenuBar;
	}

	public static ViewManager getViewManager() {
		return viewManager;
	}

	public void fitToSize(int height, int width) {
		
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
	}

	@Override
	protected void onDetach() {
		this.getParent().removeStyleName("noScroll");
		super.onDetach();


	}

	@Override
	protected void onUnload() {
		super.onUnload();
		AccounterCometClient.cometStop();
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

				Accounter.createGETService().getObjectById(type, id, callback);

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
				.getExpensesAction(Accounter.constants().cash()));
		actions.put("creditCardExpenses", VendorsActionFactory
				.getExpensesAction(Accounter.constants().creditCard()));
		actions.put("employeeExpenses", VendorsActionFactory
				.getExpensesAction(Accounter.constants().employee()));
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
