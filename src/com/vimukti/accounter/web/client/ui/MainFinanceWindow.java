package com.vimukti.accounter.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.FixedAssetsActionFactory;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

/**
 * 
 * @author Raj Vimal
 * 
 */

public class MainFinanceWindow extends VerticalPanel {

	public static int index;
	@SuppressWarnings("unused")
	private static ScrollPanel rightCanvas;
	private static MainFinanceWindow financeWindow;
	private static ViewManager viewManager;
	private Header header;
	private static boolean isNotDetachableTab;
	private int height;
	private int width;
	public static boolean iscancelEvent;

	public MainFinanceWindow(BaseFinanceWindow parent) {
		createControls();
		financeWindow = this;
		sinkEvents(Event.ONMOUSEOVER);
		 removeLoadingImage();
	}

	public MainFinanceWindow(boolean isSales) {
		if (isSales) {
			createSalesControls();
		} else {
			createPurchasesControls();
		}
		 removeLoadingImage();
		financeWindow = this;
	}

	private void createPurchasesControls() {
		// setTitle(FinanceApplication.getFinanceUIConstants().bizantraPurchases());

		VerticalPanel vlay = new VerticalPanel();
		vlay.setSize("100%", "100%");

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

		FinanceApplication.setSales(true);

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
	 var parent=$wnd.document.getElementById('loadingWrapper');
	 parent.style.visibility='hidden';
	 }-*/;

	private void createControls() {
		// setTitle(FinanceApplication.getFinanceUIConstants().bizantraFinance());

		VerticalPanel vlay = new VerticalPanel();
		vlay.setSize("100%", "100%");

		viewManager = ViewManager.getInstance();
		FinanceApplication.setPurchases(true);
		header = new Header();
		CompanyActionFactory.getCompanyHomeAction().run(null, false);
		/**
		 * // * commented for defiz
		 */
		// RootPanel.get().add(vlay);
		MenuBar menuBar = getMenuBar();
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setSize("100%", "100%");
		HorizontalPanel hpanel1 = new HorizontalPanel();
		hpanel1.add(header);
		HorizontalPanel hpanel2 = new HorizontalPanel();
		hpanel2.add(menuBar);
		vpanel.add(hpanel1);
		vpanel.add(hpanel2);
		add(vpanel);
		add(viewManager);

		// setSize("100%", "100%");
		addStyleName(FinanceApplication.getFinanceUIConstants().financeWindow());

		if (UIUtils.isMSIEBrowser()) {
			this.getElement().getStyle().setPaddingTop(0, Unit.PX);
			this.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		}

		AccounterDOM.addStyleToparent(menuBar.getElement(), FinanceApplication
				.getFinanceUIConstants().menuBarParent());

	}

	public static MainFinanceWindow getInstance() {
		return financeWindow;
	}

	private MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();
		menuBar.addItem(FinanceApplication.getFinanceUIConstants().company(),
				getCompanyMenu());
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			menuBar.addItem(FinanceApplication.getFinanceUIConstants().vat(),
					getVATMenu());
		}

		menuBar.addItem(FinanceApplication.getFinanceUIConstants().customer(),
				getCustomerMenu());
		menuBar.addItem(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor()), getVendorMenu());
		menuBar.addItem(FinanceApplication.getFinanceUIConstants().banking(),
				getBankingMenu());
		// menuBar.addItem(FinanceApplication.getFinanceUIConstants()
		// .fixedAssets(), getFixedAssetsMenu());
		menuBar.addItem(FinanceApplication.getFinanceUIConstants().reports(),
				getReportMenu());
		// menuBar.addItem(FinanceApplication.getFinanceUIConstants().help(),
		// getHelpMenu());

		if (!GWT.isScript())
			menuBar.addItem(FinanceApplication.getCompanyMessages().test(),
					getTestMenu());

		menuBar.setAutoOpen(true);
		menuBar.setAnimationEnabled(true);
		return menuBar;
	}

	private CustomMenuBar getTestMenu() {
		final GUITest guiTest = new GUITest();
		CustomMenuBar test = getSubMenu();

		Command cmd1 = new Command() {

			@Override
			public void execute() {
				guiTest.createSupportList();

			}
		};
		CustomMenuItem menuItem1 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().supportLists(), cmd1);
		test.addItem(menuItem1);

		Command cmd2 = new Command() {

			@Override
			public void execute() {
				guiTest.createAccounts();

			}
		};
		CustomMenuItem menuItem2 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().accounts(), cmd2);
		test.addItem(menuItem2);

		CustomMenuBar customerMenu = getSubMenu();
		Command cmd3 = new Command() {

			@Override
			public void execute() {
				guiTest.createCustomers();

			}
		};

		CustomMenuItem menuItem3 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().customers(), cmd3);
		customerMenu.addItem(menuItem3);

		Command cmd4 = new Command() {

			@Override
			public void execute() {
				guiTest.createCustomerIems();

			}
		};
		CustomMenuItem menuItem4 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().customerItems(), cmd4);
		customerMenu.addItem(menuItem4);

		Command cmd5 = new Command() {

			@Override
			public void execute() {
				guiTest.createQuotes();

			}
		};
		CustomMenuItem menuItem5 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().quotes(), cmd5);
		customerMenu.addItem(menuItem5);

		Command cmd6 = new Command() {

			@Override
			public void execute() {
				guiTest.createInvoices();

			}
		};
		CustomMenuItem menuItem6 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().invoices(), cmd6);
		customerMenu.addItem(menuItem6);

		Command cmd7 = new Command() {

			@Override
			public void execute() {
				guiTest.createCashSales();

			}
		};
		CustomMenuItem menuItem7 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().cashSales(), cmd7);
		customerMenu.addItem(menuItem7);

		Command cmd8 = new Command() {

			@Override
			public void execute() {
				guiTest.createCustomerCredits();

			}
		};
		CustomMenuItem menuItem8 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().customerCredit(), cmd8);
		customerMenu.addItem(menuItem8);

		Command cmd9 = new Command() {

			@Override
			public void execute() {
				guiTest.createCustomerRefunds();

			}
		};
		CustomMenuItem menuItem9 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().customerRefunds(), cmd9);
		customerMenu.addItem(menuItem9);

		Command cmd10 = new Command() {

			@Override
			public void execute() {
				guiTest.createRecievePayments();

			}
		};
		CustomMenuItem menuItem10 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().recievePayments(), cmd10);
		customerMenu.addItem(menuItem10);

		test.addItem(FinanceApplication.getCustomersMessages().customer(),
				customerMenu);

		CustomMenuBar vendorMenu = new CustomMenuBar();

		Command cmd11 = new Command() {

			@Override
			public void execute() {
				guiTest.createVendors();

			}
		};

		CustomMenuItem menuItem11 = new CustomMenuItem(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().vendors(),
				FinanceApplication.getVendorsMessages().vendors()), cmd11);
		vendorMenu.addItem(menuItem11);

		Command cmd12 = new Command() {

			@Override
			public void execute() {
				guiTest.createVendorIems();

			}
		};
		CustomMenuItem menuItem12 = new CustomMenuItem(UIUtils.getVendorString(
				FinanceApplication.getFinanceMessages().supplierItems(),
				FinanceApplication.getFinanceMessages().vendorItems()), cmd12);
		vendorMenu.addItem(menuItem12);

		Command cmd13 = new Command() {

			@Override
			public void execute() {
				guiTest.createCashPurchases();

			}
		};
		CustomMenuItem menuItem13 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().cashPurchases(), cmd13);
		vendorMenu.addItem(menuItem13);

		Command cmd14 = new Command() {

			@Override
			public void execute() {
				guiTest.createVendorCreditMemo();

			}
		};
		CustomMenuItem menuItem14 = new CustomMenuItem(UIUtils.getVendorString(
				FinanceApplication.getFinanceMessages().supplierMemo(),
				FinanceApplication.getFinanceMessages().vendorMemo()), cmd14);
		vendorMenu.addItem(menuItem14);

		Command cmd15 = new Command() {

			@Override
			public void execute() {
				guiTest.createEnterBill();
			}
		};
		CustomMenuItem menuItem15 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().enterBills(), cmd15);
		vendorMenu.addItem(menuItem15);

		Command cmd16 = new Command() {

			@Override
			public void execute() {
				guiTest.createItemReceipt();
			}
		};
		CustomMenuItem menuItem16 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().itemReciepts(), cmd16);
		vendorMenu.addItem(menuItem16);

		Command cmd17 = new Command() {

			@Override
			public void execute() {
				guiTest.createVendorPayment();
			}
		};
		CustomMenuItem menuItem17 = new CustomMenuItem(UIUtils.getVendorString(
				FinanceApplication.getFinanceMessages().supplierPayment(),
				FinanceApplication.getFinanceMessages().vendorPayment()), cmd17);
		vendorMenu.addItem(menuItem17);

		Command cmd18 = new Command() {

			@Override
			public void execute() {
				guiTest.creatVendorPayBills();
			}
		};
		CustomMenuItem menuItem18 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().payBills(), cmd18);
		vendorMenu.addItem(menuItem18);

		Command cmd19 = new Command() {

			@Override
			public void execute() {
				guiTest.createIssuePayments();
			}
		};
		CustomMenuItem menuItem19 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().issuePayments(), cmd19);
		vendorMenu.addItem(menuItem19);

		test.addItem(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor()), vendorMenu);

		CustomMenuBar bankMenu = getSubMenu();

		Command cmd20 = new Command() {

			@Override
			public void execute() {
				guiTest.createTransferFunds();
			}
		};
		CustomMenuItem menuItem20 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().transferFunds(), cmd20);
		bankMenu.addItem(menuItem20);

		Command cmd22 = new Command() {

			@Override
			public void execute() {
				guiTest.createMakeDeposite();
			}
		};
		CustomMenuItem menuItem22 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().makeDeposits(), cmd22);
		bankMenu.addItem(menuItem22);

		Command cmd23 = new Command() {

			@Override
			public void execute() {
				guiTest.createCreditCardCharge();
			}
		};
		CustomMenuItem menuItem23 = new CustomMenuItem(FinanceApplication
				.getFinanceMessages().creditCardCharge(), cmd23);
		bankMenu.addItem(menuItem23);

		test.addItem(FinanceApplication.getFinanceMessages().bank(), bankMenu);

		return test;
	}

	private CustomMenuBar getFixedAssetsMenu() {
		CustomMenuBar fixedAssetMenu = new CustomMenuBar();
		fixedAssetMenu.addItem(FixedAssetsActionFactory
				.getNewFixedAssetAction());
		fixedAssetMenu.addSeparator();
		fixedAssetMenu.addItem(CompanyActionFactory.getDepriciationAction());

		fixedAssetMenu.addSeparator();

		fixedAssetMenu.addItem(FinanceApplication.getFinanceUIConstants()
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
		vatmenu.addItem(FinanceApplication.getCustomersMessages().New(),
				vatNews);
		vatmenu.addSeparator();
		vatmenu.addItem(VatActionFactory.getAdjustTaxAction());
		vatmenu.addItem(VatActionFactory.getFileVatAction());
		// vatmenu.addItem(VatActionFactory.getCreateTaxesAction());
		vatmenu.addItem(VatActionFactory.getpayVATAction());
		vatmenu.addItem(VatActionFactory.getreceiveVATAction());
		vatmenu.addSeparator();

		vatmenu.addItem(FinanceApplication.getFinanceUIConstants().vatList(),
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
		reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.companyAndFinancial(), getCompanyAndFinancialMenu());
		reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.customersAndReceivable(), getCustomersAndReceivableMenu());
		reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.sales(), getSalesMenu());
		reportMenuBar.addItem(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().suppliersAndPayables(),
				FinanceApplication.getVendorsMessages().vendorsAndPayables()),
				getVendorAndPayablesMenu());
		reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.purchase(), getPurchaseMenu());
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
					.banking(), getBankingSubMenu());
		}
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
					.vat(), getVATReportMenu());
		}
		// reportMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
		// .salesAndPurchaseOrders(), getSalesAndPurchaseMenu());
		return reportMenuBar;
	}

	private CustomMenuBar getBankingSubMenu() {
		CustomMenuBar SubMenuBar = getSubMenu();
		SubMenuBar.addItem(ReportsActionFactory.getDetailReportAction());
		SubMenuBar.addItem(ReportsActionFactory.getCheckDetailReport());
		return SubMenuBar;
	}

	// private CustomMenuBar getSalesAndPurchaseMenu() {
	// CustomMenuBar salesAndPurchaseMenuBar = new CustomMenuBar();
	// salesAndPurchaseMenuBar.addItem(ReportsActionFactory
	// .getSalesOpenOrderAction());
	// salesAndPurchaseMenuBar.addItem(ReportsActionFactory
	// .getPurchaseOpenOrderAction());
	// return salesAndPurchaseMenuBar;
	// }

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
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ReportsActionFactory
					.getCashFlowStatementAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getTrialBalanceAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getTransactionDetailByAccountAction());
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ReportsActionFactory
					.getGlReportAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getExpenseReportAction());
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ReportsActionFactory
					.getSalesTaxLiabilityAction());
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
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
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			bankingMenuBar.addItem(BankingActionFactory.getWriteChecksAction());

		bankingMenuBar.addItem(BankingActionFactory.getMakeDepositAction());
		// bankingMenuBar.addItem(BankingActionFactory.getTransferFundsAction());
		bankingMenuBar.addItem(VendorsActionFactory.getPayBillsAction());
		// bankingMenuBar.addItem(BankingActionFactory.getEnterPaymentsAction());
		bankingMenuBar.addSeparator();
		bankingMenuBar
				.addItem(BankingActionFactory.getCreditCardChargeAction());
		bankingMenuBar.addSeparator();
		bankingMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.bankingList(), getBankingListMenu());

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
		vendorMenuBar.addSeparator();
		vendorMenuBar.addItem(FinanceApplication.getFinanceUIConstants().New(),
				getNewVendorMenu());
		vendorMenuBar.addSeparator();
		vendorMenuBar.addItem(VendorsActionFactory.getEnterBillsAction());
		vendorMenuBar.addItem(VendorsActionFactory.getPayBillsAction());
		vendorMenuBar.addItem(VendorsActionFactory.getIssuePaymentsAction());
		vendorMenuBar.addItem(VendorsActionFactory.getNewVendorPaymentAction());
		vendorMenuBar.addItem(VendorsActionFactory.getRecordExpensesAction());
		// vendorMenuBar.addItem(VendorsActionFactory.getItemReceiptAction());
		vendorMenuBar.addSeparator();
		vendorMenuBar.addItem(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplierLists(), FinanceApplication
				.getVendorsMessages().vendorLists()), getVendorListMenu());
		return vendorMenuBar;
	}

	private CustomMenuBar getVendorListMenu() {
		CustomMenuBar vendorListMenuBar = getSubMenu();
		vendorListMenuBar.addItem(VendorsActionFactory.getVendorsAction());
		vendorListMenuBar.addItem(VendorsActionFactory.getItemsAction());
		vendorListMenuBar.addItem(VendorsActionFactory.getBillsAction());
		vendorListMenuBar.addItem(VendorsActionFactory
				.getVendorPaymentsAction());

		return vendorListMenuBar;
	}

	private CustomMenuBar getNewVendorMenu() {
		CustomMenuBar newVendorMenuBar = getSubMenu();
		newVendorMenuBar.addItem(VendorsActionFactory.getNewVendorAction());
		newVendorMenuBar.addItem(CompanyActionFactory.getNewItemAction());
		newVendorMenuBar.addItem(VendorsActionFactory
				.getNewCashPurchaseAction());
		newVendorMenuBar.addItem(VendorsActionFactory.getNewCreditMemoAction());
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			newVendorMenuBar.addItem(VendorsActionFactory.getNewCheckAction());

		return newVendorMenuBar;
	}

	private CustomMenuBar getCustomerMenu() {
		CustomMenuBar customerMenuBar = getSubMenu();
		customerMenuBar
				.addItem(CustomersActionFactory.getCustomersHomeAction());
		customerMenuBar.addSeparator();
		customerMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.New(), getNewCustomerMenu());
		customerMenuBar.addSeparator();
		customerMenuBar.addItem(CustomersActionFactory
				.getNewCustomerPaymentAction());
		customerMenuBar.addItem(CustomersActionFactory
				.getReceivePaymentAction());
		customerMenuBar.addItem(CustomersActionFactory
				.getCustomerRefundAction());
		customerMenuBar.addSeparator();
		customerMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.customerLists(), getCustomerListMenu());
		return customerMenuBar;
	}

	private CustomMenuBar getCustomerListMenu() {
		CustomMenuBar customerListMenuBar = getSubMenu();
		customerListMenuBar
				.addItem(CustomersActionFactory.getCustomersAction());
		customerListMenuBar.addItem(CustomersActionFactory.getItemsAction());
		customerListMenuBar.addItem(CustomersActionFactory.getQuotesAction());
		customerListMenuBar.addItem(CustomersActionFactory.getInvoicesAction());
		customerListMenuBar.addItem(CustomersActionFactory
				.getReceivedPaymentsAction());
		customerListMenuBar.addItem(CustomersActionFactory
				.getCustomerRefundsAction());

		return customerListMenuBar;
	}

	private CustomMenuBar getNewCustomerMenu() {
		CustomMenuBar newCustomerMenuBar = getSubMenu();
		newCustomerMenuBar.addItem(CustomersActionFactory
				.getNewCustomerAction());
		newCustomerMenuBar.addItem(CustomersActionFactory.getNewItemAction());
		newCustomerMenuBar.addItem(CustomersActionFactory.getNewQuoteAction());
		newCustomerMenuBar
				.addItem(CustomersActionFactory.getNewInvoiceAction());
		newCustomerMenuBar.addItem(CustomersActionFactory
				.getNewCashSaleAction());
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

		companyMenuBar.addItem(CompanyActionFactory.getNewJournalEntryAction());
		companyMenuBar.addItem(CompanyActionFactory.getNewAccountAction());
		companyMenuBar.addSeparator();
		companyMenuBar.addItem(CompanyActionFactory
				.getCompanyInformationAction());
		companyMenuBar.addItem(CompanyActionFactory.getPreferencesAction());
		companyMenuBar.addSeparator();
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
					.itemTax(), getSalesTaxSubmenu());
		companyMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.manageSupportLists(), getManageSupportListSubmenu());
		companyMenuBar
				.addItem(CompanyActionFactory.getManageFiscalYearAction());
		companyMenuBar.addSeparator();
		companyMenuBar.addItem(FinanceApplication.getFinanceUIConstants()
				.companyLists(), getCompanyListMenu());

		return companyMenuBar;
	}

	private CustomMenuBar getCompanyListMenu() {
		CustomMenuBar companyListMenuBar = getSubMenu();
		companyListMenuBar.addItem(CompanyActionFactory
				.getChartOfAccountsAction());
		companyListMenuBar.addItem(CompanyActionFactory
				.getJournalEntriesAction());
		companyListMenuBar.addItem(CustomersActionFactory.getItemsAction());
		companyListMenuBar.addItem(CustomersActionFactory.getCustomersAction());
		companyListMenuBar.addItem(VendorsActionFactory.getVendorsAction());
		companyListMenuBar.addItem(CompanyActionFactory.getPaymentsAction());
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
		salesTaxMenuBar.addItem(CompanyActionFactory.getPaySalesTaxAction());
		salesTaxMenuBar.addItem(CompanyActionFactory.getAdjustTaxAction());
		// salesTaxMenuBar.addItem(CompanyActionFactory
		// .getViewSalesTaxLiabilityAction());
		// salesTaxMenuBar.addItem(CompanyActionFactory.getTaxItemAction());

		salesTaxMenuBar.addItem(CompanyActionFactory.getNewTAXAgencyAction());
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
//	  Window.addResizeHandler(new ResizeHandler() {
//
//	   @Override
//	   public void onResize(ResizeEvent event) {
//	    MainFinanceWindow.this.fitToSize(event.getHeight() - 20, 960);
//	   }
//	  });

	  // setHeight(this.height + "px");
	  // if (view == null)
	  // viewManager.fitToSize(width - 10, height);
	  // else {
	  // view.setHeightForCanvas((height * 80.6 / 100) + "");
	  // view.getButtonPanel().setHeight("30px");
	  // }
	  super.onLoad();
	  viewManager.fitToSize(this.getOffsetHeight(), 960);
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
			FinanceApplication.makeAllStaticInstancesNull();
		}
		isNotDetachableTab = false;

	}

	@Override
	protected void onUnload() {
		super.onUnload();

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
		FinanceApplication.makeAllStaticInstancesNull();
		MainFinanceWindow.setIsnotDetachableTab(true);
	}

	public void cancelonMouseoverevent(boolean iscancelEvent) {
		MainFinanceWindow.iscancelEvent = iscancelEvent;
	}
}