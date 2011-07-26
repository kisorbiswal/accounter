package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.FixedAssetsActionFactory;
import com.vimukti.accounter.web.client.ui.core.PurchaseOrderActionFactory;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;
import com.vimukti.accounter.web.client.ui.core.SalesOrderActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.settings.SettingsActionFactory;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

public class HorizontalMenuBar extends HorizontalPanel {

	public static String oldToken;

	public HorizontalMenuBar() {
		MenuBar menuBar = getMenuBar();
		add(menuBar);
		setStyleName("MENU_BAR_BG");
		AccounterDOM.addStyleToparent(menuBar.getElement(), Accounter
				.getFinanceUIConstants().menuBarParent());
	}

	private MenuBar getMenuBar() {
		MenuBar menuBar = new MenuBar();
		// MenuItem dashBoardMenuitem = menuBar.addItem("DashBoard",
		// getDashBoardCommand());
		// ThemesUtil.insertEmptyChildToMenuBar(menuBar);
		// dashBoardMenuitem.getElement().setTitle(
		// "Click here to download this plugin");

		MenuItem menuitem = menuBar.addItem(Accounter
				.getFinanceUIConstants().company(), getCompanyMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			menuitem = menuBar.addItem(Accounter
					.getFinanceUIConstants().vat(), getVATMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		menuitem = menuBar.addItem(Accounter.getFinanceUIConstants()
				.customer(), getCustomerMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem(UIUtils.getVendorString(Accounter
				.getVendorsMessages().supplier(), Accounter
				.getVendorsMessages().vendor()), getVendorMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (Accounter.getUser().canDoBanking()) {
			menuitem = menuBar.addItem(Accounter
					.getFinanceUIConstants().banking(), getBankingMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		menuitem = menuBar.addItem("Sales", getSalesSubMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		menuitem = menuBar.addItem("Purchases", getPurchaseSubMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		// menuBar.addItem(Accounter.getFinanceUIConstants()
		// .fixedAssets(), getFixedAssetsMenu());
		if (Accounter.getUser().canViewReports()) {
			menuitem = menuBar.addItem(Accounter
					.getFinanceUIConstants().reports(), getReportMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		// menuBar.addItem(Accounter.getFinanceUIConstants().help(),
		// getHelpMenu());
		if (Accounter.getUser().canChangeSettings()) {
			menuitem = menuBar.addItem(Accounter.getSettingsMessages()
					.settings(), getSettingsMenu());
			ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		//
		// if (!GWT.isScript()) {
		// menuitem = menuBar.addItem(Accounter.getCompanyMessages()
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

//	private CustomMenuBar getTestMenu() {
//		final GUITest guiTest = new GUITest();
//		CustomMenuBar test = getSubMenu();
//
//		Command cmd1 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createSupportList();
//
//			}
//		};
//		CustomMenuItem menuItem1 = new CustomMenuItem(Accounter
//				.getFinanceMessages().supportLists(), cmd1);
//		test.addItem(menuItem1);
//
//		Command cmd2 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createAccounts();
//
//			}
//		};
//		CustomMenuItem menuItem2 = new CustomMenuItem(Accounter
//				.getFinanceMessages().accounts(), cmd2);
//		test.addItem(menuItem2);
//
//		CustomMenuBar customerMenu = getSubMenu();
//		Command cmd3 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createCustomers();
//
//			}
//		};
//
//		CustomMenuItem menuItem3 = new CustomMenuItem(Accounter
//				.getFinanceMessages().customers(), cmd3);
//		customerMenu.addItem(menuItem3);
//
//		Command cmd4 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createCustomerIems();
//
//			}
//		};
//		CustomMenuItem menuItem4 = new CustomMenuItem(Accounter
//				.getFinanceMessages().customerItems(), cmd4);
//		customerMenu.addItem(menuItem4);
//
//		Command cmd5 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createQuotes();
//
//			}
//		};
//		CustomMenuItem menuItem5 = new CustomMenuItem(Accounter
//				.getFinanceMessages().quotes(), cmd5);
//		customerMenu.addItem(menuItem5);
//
//		Command cmd6 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createInvoices();
//
//			}
//		};
//		CustomMenuItem menuItem6 = new CustomMenuItem(Accounter
//				.getFinanceMessages().invoices(), cmd6);
//		customerMenu.addItem(menuItem6);
//
//		Command cmd7 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createCashSales();
//
//			}
//		};
//		CustomMenuItem menuItem7 = new CustomMenuItem(Accounter
//				.getFinanceMessages().cashSales(), cmd7);
//		customerMenu.addItem(menuItem7);
//
//		Command cmd8 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createCustomerCredits();
//
//			}
//		};
//		CustomMenuItem menuItem8 = new CustomMenuItem(Accounter
//				.getFinanceMessages().customerCredit(), cmd8);
//		customerMenu.addItem(menuItem8);
//
//		Command cmd9 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createCustomerRefunds();
//
//			}
//		};
//		CustomMenuItem menuItem9 = new CustomMenuItem(Accounter
//				.getFinanceMessages().customerRefunds(), cmd9);
//		customerMenu.addItem(menuItem9);
//
//		Command cmd10 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createRecievePayments();
//
//			}
//		};
//		CustomMenuItem menuItem10 = new CustomMenuItem(Accounter
//				.getFinanceMessages().recievePayments(), cmd10);
//		customerMenu.addItem(menuItem10);
//
//		test.addItem(Accounter.getCustomersMessages().customer(),
//				customerMenu);
//
//		CustomMenuBar vendorMenu = new CustomMenuBar();
//
//		Command cmd11 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createVendors();
//
//			}
//		};
//
//		CustomMenuItem menuItem11 = new CustomMenuItem(UIUtils.getVendorString(
//				Accounter.getVendorsMessages().vendors(),
//				Accounter.getVendorsMessages().vendors()), cmd11);
//		vendorMenu.addItem(menuItem11);
//
//		Command cmd12 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createVendorIems();
//
//			}
//		};
//		CustomMenuItem menuItem12 = new CustomMenuItem(UIUtils.getVendorString(
//				Accounter.getFinanceMessages().supplierItems(),
//				Accounter.getFinanceMessages().vendorItems()), cmd12);
//		vendorMenu.addItem(menuItem12);
//
//		Command cmd13 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createCashPurchases();
//
//			}
//		};
//		CustomMenuItem menuItem13 = new CustomMenuItem(Accounter
//				.getFinanceMessages().cashPurchases(), cmd13);
//		vendorMenu.addItem(menuItem13);
//
//		Command cmd14 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createVendorCreditMemo();
//
//			}
//		};
//		CustomMenuItem menuItem14 = new CustomMenuItem(UIUtils.getVendorString(
//				Accounter.getFinanceMessages().supplierMemo(),
//				Accounter.getFinanceMessages().vendorMemo()), cmd14);
//		vendorMenu.addItem(menuItem14);
//
//		Command cmd15 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createEnterBill();
//			}
//		};
//		CustomMenuItem menuItem15 = new CustomMenuItem(Accounter
//				.getFinanceMessages().enterBills(), cmd15);
//		vendorMenu.addItem(menuItem15);
//
//		Command cmd16 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createItemReceipt();
//			}
//		};
//		CustomMenuItem menuItem16 = new CustomMenuItem(Accounter
//				.getFinanceMessages().itemReciepts(), cmd16);
//		vendorMenu.addItem(menuItem16);
//
//		Command cmd17 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createVendorPayment();
//			}
//		};
//		CustomMenuItem menuItem17 = new CustomMenuItem(UIUtils.getVendorString(
//				Accounter.getFinanceMessages().supplierPayment(),
//				Accounter.getFinanceMessages().vendorPayment()), cmd17);
//		vendorMenu.addItem(menuItem17);
//
//		Command cmd18 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.creatVendorPayBills();
//			}
//		};
//		CustomMenuItem menuItem18 = new CustomMenuItem(Accounter
//				.getFinanceMessages().payBills(), cmd18);
//		vendorMenu.addItem(menuItem18);
//
//		Command cmd19 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createIssuePayments();
//			}
//		};
//		CustomMenuItem menuItem19 = new CustomMenuItem(Accounter
//				.getFinanceMessages().issuePayments(), cmd19);
//		vendorMenu.addItem(menuItem19);
//
//		test.addItem(UIUtils.getVendorString(Accounter
//				.getVendorsMessages().supplier(), Accounter
//				.getVendorsMessages().vendor()), vendorMenu);
//
//		CustomMenuBar bankMenu = getSubMenu();
//
//		Command cmd20 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createTransferFunds();
//			}
//		};
//		CustomMenuItem menuItem20 = new CustomMenuItem(Accounter
//				.getFinanceMessages().transferFunds(), cmd20);
//		bankMenu.addItem(menuItem20);
//
//		Command cmd22 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createMakeDeposite();
//			}
//		};
//		CustomMenuItem menuItem22 = new CustomMenuItem(Accounter
//				.getFinanceMessages().makeDeposits(), cmd22);
//		bankMenu.addItem(menuItem22);
//
//		Command cmd23 = new Command() {
//
//			@Override
//			public void execute() {
//				guiTest.createCreditCardCharge();
//			}
//		};
//		CustomMenuItem menuItem23 = new CustomMenuItem(Accounter
//				.getFinanceMessages().creditCardCharge(), cmd23);
//		bankMenu.addItem(menuItem23);
//
//		test.addItem(Accounter.getFinanceMessages().bank(), bankMenu);
//
//		return test;
//	}

	private CustomMenuBar getFixedAssetsMenu() {
		CustomMenuBar fixedAssetMenu = new CustomMenuBar();
		fixedAssetMenu.addItem(FixedAssetsActionFactory
				.getNewFixedAssetAction());
		fixedAssetMenu.addSeparator();
		fixedAssetMenu.addItem(CompanyActionFactory.getDepriciationAction());

		fixedAssetMenu.addSeparator();

		fixedAssetMenu.addItem(Accounter.getFinanceUIConstants()
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
		vatmenu.addItem(Accounter.getCustomersMessages().New(),
				vatNews);
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

		vatmenu.addItem(Accounter.getFinanceUIConstants().vatList(),
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
		// helpMenuBar.addItem(Accounter.getFinanceUIConstants().help(),
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
		reportMenuBar.addItem(Accounter.getFinanceUIConstants()
				.companyAndFinancial(), getCompanyAndFinancialMenu());
		reportMenuBar.addItem(Accounter.getFinanceUIConstants()
				.customersAndReceivable(), getCustomersAndReceivableMenu());
		reportMenuBar.addItem(Accounter.getFinanceUIConstants()
				.sales(), getSalesMenu());
		reportMenuBar.addItem(UIUtils.getVendorString(Accounter
				.getVendorsMessages().suppliersAndPayables(),
				Accounter.getVendorsMessages().vendorsAndPayables()),
				getVendorAndPayablesMenu());
		reportMenuBar.addItem(Accounter.getFinanceUIConstants()
				.purchase(), getPurchaseMenu());
		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// reportMenuBar.addItem(Accounter.getFinanceUIConstants()
		// .banking(), getBankingSubMenu());
		// }
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			reportMenuBar.addItem(Accounter.getFinanceUIConstants()
					.vat(), getVATReportMenu());
		}
		// reportMenuBar.addItem(Accounter.getFinanceUIConstants()
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
		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getCashFlowStatementAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getTrialBalanceAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getTransactionDetailByAccountAction());
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ReportsActionFactory
					.getGlReportAction());
		companyAndFinancialMenuBar.addItem(ReportsActionFactory
				.getExpenseReportAction());
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyAndFinancialMenuBar.addItem(ReportsActionFactory
					.getSalesTaxLiabilityAction());
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
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
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			bankingMenuBar.addItem(BankingActionFactory.getWriteChecksAction());

		bankingMenuBar.addItem(BankingActionFactory.getMakeDepositAction());
		// bankingMenuBar.addItem(BankingActionFactory.getTransferFundsAction());
		bankingMenuBar.addItem(VendorsActionFactory.getPayBillsAction());
		// bankingMenuBar.addItem(BankingActionFactory.getEnterPaymentsAction());
		bankingMenuBar.addSeparator();
		bankingMenuBar
				.addItem(BankingActionFactory.getCreditCardChargeAction());
		bankingMenuBar.addSeparator();
		bankingMenuBar.addItem(Accounter.getFinanceUIConstants()
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
		if (getNewVendorMenu().menuItems.size() > 0) {
			vendorMenuBar.addSeparator();
			vendorMenuBar.addItem(Accounter.getFinanceUIConstants()
					.New(), getNewVendorMenu());
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
				.getVendorsMessages().supplierLists(), Accounter
				.getVendorsMessages().vendorLists()), getVendorListMenu());
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
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
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
			customerMenuBar.addItem(Accounter.getFinanceUIConstants()
					.New(), getNewCustomerMenu());
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
		customerMenuBar.addItem(Accounter.getFinanceUIConstants()
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

	private Command getDashBoardCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				String historyToken = CompanyActionFactory
						.getCompanyHomeAction().getHistoryToken();
				if (!History.getToken().equals(historyToken)) {
					MainFinanceWindow.oldToken = History.getToken();
					HistoryTokenUtils.setPresentToken(CompanyActionFactory
							.getCompanyHomeAction(), null);
				}
				CompanyActionFactory.getCompanyHomeAction().run(null, false);
			}
		};
		return dashBoardcmd;
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
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			companyMenuBar.addItem(Accounter.getFinanceUIConstants()
					.itemTax(), getSalesTaxSubmenu());
		companyMenuBar.addItem(Accounter.getFinanceUIConstants()
				.manageSupportLists(), getManageSupportListSubmenu());
		if (Accounter.getUser().canManageFiscalYears())
			companyMenuBar.addItem(CompanyActionFactory
					.getManageFiscalYearAction());
		companyMenuBar.addSeparator();
		companyMenuBar.addItem(Accounter.getFinanceUIConstants()
				.companyLists(), getCompanyListMenu());

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

}
