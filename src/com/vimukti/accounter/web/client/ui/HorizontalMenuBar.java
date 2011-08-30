package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.company.ItemsAction;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class HorizontalMenuBar extends HorizontalPanel {

	private ClientCompanyPreferences preferences = Global.get().preferences();
	public static String oldToken;

	public HorizontalMenuBar() {
		MenuBar menuBar = getMenuBar();
		add(menuBar);
		setStyleName("MENU_BAR_BG");
		AccounterDOM.addStyleToparent(menuBar.getElement(), Accounter
				.constants().menuBarParent());
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

		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			if (Accounter.getCompany().getPreferences().getDoYouPaySalesTax()) {
				menuitem = menuBar.addItem(Accounter.constants().vat(),
						getVATMenu());
				ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
			}
		}
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {

			menuitem = menuBar.addItem(Accounter.constants().tax(),
					getTaxMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		}

		menuitem = menuBar.addItem(
				Accounter.messages().Customer(Global.get().Customer()),
				getCustomerMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem(Global.get().vendor(), getVendorMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (Accounter.getUser().canDoBanking()) {
			menuitem = menuBar.addItem(Accounter.constants().banking(),
					getBankingMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		if (getPreferences().isSalesOrderEnabled()) {
			menuitem = menuBar.addItem(Accounter.constants().sales(),
					getSalesSubMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		}

		if (getPreferences().isPurchaseOrderEnabled()) {
			menuitem = menuBar.addItem(Accounter.constants().purchases(),
					getPurchaseSubMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		// menuitem = menuBar.addItem(Accounter.constants().fixedAssets(),
		// getFixedAssetsMenu());
		// ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (Accounter.getUser().canViewReports()) {
			menuitem = menuBar.addItem(Accounter.constants().reports(),
					getReportMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		// menuBar.addItem(Accounter.constants().help(),
		// getHelpMenu());
		if (Accounter.getUser().canChangeSettings()) {
			menuitem = menuBar.addItem(Accounter.constants().settings(),
					getSettingsMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		//
		// if (!GWT.isScript()) {
		// menuitem = menuBar.addItem(Accounter.constants()
		// .test(), getTestMenu());
		// ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		// }

		menuBar.setAutoOpen(true);
		menuBar.setAnimationEnabled(true);
		return menuBar;
	}

	private CustomMenuBar getTaxMenu() {
		CustomMenuBar taxMenu = getSubMenu();

		taxMenu = getVATMenu();
		return taxMenu;
	}

	private CustomMenuBar getSettingsMenu() {
		CustomMenuBar settingsMenuBar = new CustomMenuBar();
		settingsMenuBar.addItem(ActionFactory.getGeneralSettingsAction());
		// settingsMenuBar.addItem(ActionFactory.getInventoryItemsAction());
		// settingsMenuBar.addItem(ActionFactory.getChartOfAccountsAction());
		return settingsMenuBar;
	}

	// private Command getSettingsCommand(final int i) {
	// Command settingsCommand = new Command() {
	//
	// @Override
	// public void execute() {
	// switch (i) {
	// case 1:
	// ActionFactory.getGeneralSettingsAction().run(null,
	// false);
	// break;
	// case 2:
	// ActionFactory.getInventoryItemsAction().run(null,
	// false);
	// break;
	// case 3:
	// ActionFactory.getChartOfAccountsAction().run(null,
	// false);
	// break;
	// }
	// }
	// };
	// return settingsCommand;
	// }

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
	// test.addItem(Accounter.constants().customer(),
	// customerMenu);
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
	// Accounter.constants().vendors(),
	// Accounter.constants().vendors()), cmd11);
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
	// Accounter.getFinanceMessages().supplierItems(),
	// Accounter.getFinanceMessages().vendorItems()), cmd12);
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
	// Accounter.getFinanceMessages().supplierMemo(),
	// Accounter.getFinanceMessages().vendorMemo()), cmd14);
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
	// Accounter.getFinanceMessages().supplierPayment(),
	// Accounter.getFinanceMessages().vendorPayment()), cmd17);
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
	// test.addItem(UIUtils.getVendorString(Accounter
	// .constants().supplier(), Accounter
	// .constants().vendor()), vendorMenu);
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
	// test.addItem(Accounter.getFinanceMessages().bank(), bankMenu);
	//
	// return test;
	// }

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
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vatNews.addItem(ActionFactory.getNewVatItemAction());
			// vatNews.addItem(ActionFactory.getVatGroupAction());
			vatNews.addItem(ActionFactory.getNewTAXCodeAction());
			vatNews.addItem(ActionFactory.getNewTAXAgencyAction());
			vatmenu.addItem(Accounter.constants().new1(), vatNews);
			vatmenu.addSeparator();
		}

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
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
			vatmenu.addItem("Pay TDS", ActionFactory.getpayVATAction());
			vatmenu.addItem(Accounter.constants().tds(), getTDSMenu());
			vatmenu.addSeparator();
		}

		vatmenu.addItem(Accounter.constants().vatList(), getVATsListMenu());

		return vatmenu;
	}

	private CustomMenuBar getTDSMenu() {
		CustomMenuBar tdsMenu = getSubMenu();
		tdsMenu.addItem("TDS Return", ActionFactory.getTDSVendorsAction());
		tdsMenu.addItem("Form-16A", ActionFactory.getTDSVendorsAction());
		return tdsMenu;
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
		// helpMenuBar.addItem(Accounter.constants().help(),
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
		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// reportMenuBar.addItem(Accounter.constants()
		// .banking(), getBankingSubMenu());
		// }
		if (Accounter.getCompany().getPreferences().getDoYouPaySalesTax()) {
			if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				reportMenuBar.addItem(Accounter.constants().vat(),
						getVATReportMenu());
			}
		}
		// reportMenuBar.addItem(Accounter.constants()
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

		if (getPreferences().isPurchaseOrderEnabled()) {
			purchaseMenuBar.addItem(ActionFactory.getPurchaseOpenOrderAction());
		}
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

		if (getPreferences().isSalesOrderEnabled()) {
			salesMenuBar.addItem(ActionFactory.getSalesOpenOrderAction());
		}
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
		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getCashFlowStatementAction());
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getTrialBalanceAction());
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getTransactionDetailByAccountAction());
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ActionFactory
					.getGlReportAction());
		companyAndFinancialMenuBar.addItem(ActionFactory
				.getExpenseReportAction());
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ActionFactory
					.getSalesTaxLiabilityAction());
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ActionFactory
					.getTransactionDetailByTaxItemAction());
		return companyAndFinancialMenuBar;
	}

	private CustomMenuBar getBankingMenu() {
		CustomMenuBar bankingMenuBar = getSubMenu();
		// bankingMenuBar.addItem(ActionFactory.getBankingHomeAction());
		// bankingMenuBar.addSeparator();
		bankingMenuBar.addItem(ActionFactory.getNewBankAccountAction());
		bankingMenuBar.addSeparator();
		// bankingMenuBar.addItem(ActionFactory.getAccountRegisterAction());
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
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

			// This should be enabled when user select to track employee
			// expenses.
			if (ClientCompanyPreferences.get().isHaveEpmloyees()
					&& ClientCompanyPreferences.get().isTrackEmployeeExpenses()) {
				vendorMenuBar.addItem(ActionFactory.getExpenseClaimsAction(0));
			}
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
			ItemsAction itemsAction = ActionFactory.getItemsAction();
			itemsAction.setCatagory(Global.get().vendor());
			vendorListMenuBar.addItem(itemsAction);
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
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			if (Accounter.getUser().canDoInvoiceTransactions())
				newVendorMenuBar.addItem(ActionFactory.getNewCheckAction());

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
			ItemsAction itemsAction = ActionFactory.getItemsAction();
			itemsAction.setCatagory(Global.get().Customer());
			customerListMenuBar.addItem(itemsAction);
			if (preferences.isDoyouwantEstimates()) {
				customerListMenuBar.addItem(ActionFactory.getQuotesAction());
			}
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
			if (preferences.isDoyouwantEstimates()) {
				newCustomerMenuBar.addItem(ActionFactory.getNewQuoteAction());
			}
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

	private Command getMergeCustomerCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				CustomerMergeDialog customerMergeDialog = new CustomerMergeDialog(
						Accounter.messages().mergeCustomers(
								Global.get().Customer()), Accounter.messages()
								.mergeDescription(Global.get().customer()));

				customerMergeDialog.show();
			}
		};
		return dashBoardcmd;
	}

	private Command getMergeAccountCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				AccountMergeDialog accountMergeDialog = new AccountMergeDialog(
						Accounter.messages().mergeAccounts(
								Global.get().Account()), Accounter.messages()
								.mergeDescription(Global.get().account()));
				accountMergeDialog.show();
			}
		};
		return dashBoardcmd;
	}

	private Command getMergeVendorCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				VendorMergeDialog vendorMergeDialog = new VendorMergeDialog(
						Accounter.messages()
								.mergeVendors(Global.get().Vendor()), Accounter
								.messages().mergeDescription(
										Global.get().vendor()));
				vendorMergeDialog.show();
			}
		};
		return dashBoardcmd;
	}

	private Command getMergeItemCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				ItemMergeDialog dialog = new ItemMergeDialog(Accounter
						.constants().mergeItems(), Accounter.constants()
						.itemDescription());

				dialog.show();
			}
		};
		return dashBoardcmd;
	}

	private Command getDashBoardCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				ActionFactory.getCompanyHomeAction().run(null, false);
			}
		};
		return dashBoardcmd;
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
			companyMenuBar.addItem(ActionFactory.getPreferencesAction());

		}
		// companyMenuBar.addItem(ActionFactory.getBudgetActions());
		companyMenuBar.addSeparator();
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {

			if (getPreferences().getDoYouPaySalesTax()) {
				companyMenuBar.addItem(Accounter.constants().itemTax(),
						getSalesTaxSubmenu());
			}
		}
		if (Accounter.getUser().canChangeSettings()) {
			companyMenuBar.addItem(Accounter.constants().manageSupportLists(),
					getManageSupportListSubmenu());
		}
		if (Accounter.getUser().canManageFiscalYears()) {
			companyMenuBar.addItem(ActionFactory.getManageFiscalYearAction());
			companyMenuBar.addSeparator();
		}

		companyMenuBar.addItem(
				Accounter.messages().mergeAccounts(Global.get().Account()),
				getMergeSubMenu());
		companyMenuBar.addSeparator();
		companyMenuBar.addItem(Accounter.constants().companyLists(),
				getCompanyListMenu());

		return companyMenuBar;
	}

	private CustomMenuBar getMergeSubMenu() {
		CustomMenuBar mergeAccountsMenuBar = getSubMenu();
		mergeAccountsMenuBar.addItem(
				Accounter.messages().mergeCustomers(Global.get().Customer()),
				getMergeCustomerCommand());
		mergeAccountsMenuBar.addItem(
				Accounter.messages().mergeVendors(Global.get().Vendor()),
				getMergeVendorCommand());
		mergeAccountsMenuBar.addItem(
				Accounter.messages().mergeAccounts(Global.get().Account()),
				getMergeAccountCommand());
		mergeAccountsMenuBar.addItem(Accounter.constants().mergeItems(),
				getMergeItemCommand());

		return mergeAccountsMenuBar;
	}

	private CustomMenuBar getCompanyListMenu() {
		CustomMenuBar companyListMenuBar = getSubMenu();
		if (Accounter.getUser().canSeeInvoiceTransactions())
			companyListMenuBar
					.addItem(ActionFactory.getChartOfAccountsAction());
		if (Accounter.getUser().canSeeBanking())
			companyListMenuBar.addItem(ActionFactory.getJournalEntriesAction());
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			ItemsAction itemsAction = ActionFactory.getItemsAction();
			itemsAction.setCatagory(Accounter.messages().bothCustomerAndVendor(
					Global.get().Customer(), Global.get().Vendor()));
			companyListMenuBar.addItem(itemsAction);
		}
		companyListMenuBar.addItem(ActionFactory.getCustomersAction());
		companyListMenuBar.addItem(ActionFactory.getVendorsAction());
		if (Accounter.getUser().canSeeBanking())
			companyListMenuBar.addItem(ActionFactory.getPaymentsAction());
		companyListMenuBar.addItem(ActionFactory.getSalesPersonAction());
		// companyListMenuBar.addItem(ActionFactory.getWarehouseListAction());
		companyListMenuBar.addItem(ActionFactory.getRecurringsListAction());
		companyListMenuBar.addItem(ActionFactory.getUsersActivityListAction());
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

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

}
