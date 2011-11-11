package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.countries.UnitedKingdom;
import com.vimukti.accounter.web.client.ui.IMenuFactory.IMenu;
import com.vimukti.accounter.web.client.ui.IMenuFactory.IMenuBar;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.ItemsAction;
import com.vimukti.accounter.web.client.ui.company.PaymentsAction;
import com.vimukti.accounter.web.client.ui.company.PreferencesAction;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class AccounterMenuBar extends HorizontalPanel {

	private ClientCompanyPreferences preferences = Global.get().preferences();
	private IMenuFactory factory;
	public static String oldToken;

	public AccounterMenuBar(IMenuFactory factory) {
		this.factory = factory;
		IMenuBar menuBar = getMenuBar();
		add(menuBar);

		setStyleName("MENU_BAR_BG");
	}

	private IMenuBar getMenuBar() {
		IMenuBar menuBar = factory.createMenuBar();

		menuBar.addMenuItem(Accounter.constants().company(), getCompanyMenu());

		if (Accounter.getCompany().getPreferences().isTrackTax()) {
			menuBar.addMenuItem(Accounter.constants().tax(), getVATMenu());
		}

		menuBar.addMenuItem(Global.get().Customer(), getCustomerMenu());

		menuBar.addMenuItem(Global.get().Vendor(), getVendorMenu());

		if (Accounter.getUser().canDoBanking()) {
			menuBar.addMenuItem(Accounter.constants().banking(),
					getBankingMenu());
		}

		if (getPreferences().isSalesOrderEnabled()) {
			menuBar.addMenuItem(Accounter.constants().sales(),
					getSalesSubMenu());

		}

		if (getPreferences().isPurchaseOrderEnabled()) {
			menuBar.addMenuItem(Accounter.constants().purchases(),
					getPurchaseSubMenu());
		}

		if (getPreferences().isInventoryEnabled()) {
			menuBar.addMenuItem(Accounter.constants().inventory(),
					getInventoryMenu());
		}

		// menuitem = menuBar.addItem(Accounter.constants().fixedAssets(),
		// getFixedAssetsMenu());
		// ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		if (Accounter.getUser().canViewReports()) {
			menuBar.addMenuItem(Accounter.constants().reports(),
					getReportMenu());
			// ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}
		// menuBar.addItem(Accounter.constants().help(),
		// getHelpMenu());
		if (Accounter.getUser().canChangeSettings()) {
			menuBar.addMenuItem(Accounter.constants().settings(),
					getSettingsMenu());
			// ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);
		}

		return menuBar;
	}

	private IMenu getInventoryMenu() {
		IMenu inventoryMenuBar = factory.createMenu();
		// inventoryMenuBar.addMenuItem(ActionFactory.getStockSettingsAction());
		inventoryMenuBar.addMenuItem(ActionFactory.getStockAdjustmentAction());
		inventoryMenuBar.addMenuItem(Accounter.constants().new1(),
				getNewInventoryMenu());
		inventoryMenuBar.addMenuItem(Accounter.constants().InventoryLists(),
				getInventoryListsMenu());
		return inventoryMenuBar;
	}

	private IMenu getInventoryListsMenu() {
		IMenu inventoryMenu = factory.createMenu();
		inventoryMenu.addMenuItem(Accounter.constants().inventoryItems(),
				ActionFactory.getInventoryItemsAction());
		if (getPreferences().iswareHouseEnabled()) {
			inventoryMenu.addMenuItem(Accounter.constants().warehouseList(),
					ActionFactory.getWarehouseListAction());
			inventoryMenu.addMenuItem(Accounter.constants()
					.warehouseTransferList(), ActionFactory
					.getWarehouseTransferListAction());
		}
		inventoryMenu.addMenuItem(Accounter.constants().stockAdjustments(),
				ActionFactory.getStockAdjustmentsListAction());
		inventoryMenu.addMenuItem(Accounter.constants().measurementList(),
				ActionFactory.getMeasurementsAction());
		return inventoryMenu;
	}

	private IMenu getNewInventoryMenu() {

		IMenu newMenuBar = factory.createMenu();
		if (getPreferences().iswareHouseEnabled()) {
			newMenuBar.addMenuItem(Accounter.constants().wareHouse(),
					ActionFactory.getWareHouseViewAction());
			newMenuBar.addMenuItem(Accounter.constants().wareHouseTransfer(),
					ActionFactory.getWareHouseTransferAction());
		}
		newMenuBar.addMenuItem(Accounter.constants().measurement(),
				ActionFactory.getAddMeasurementAction());
		return newMenuBar;

	}

	private IMenu getTaxMenu() {
		IMenu taxMenu = getSubMenu();

		taxMenu = getVATMenu();
		return taxMenu;
	}

	private IMenu getSettingsMenu() {
		IMenu settingsMenuBar = factory.createMenu();
		settingsMenuBar.addMenuItem(ActionFactory.getGeneralSettingsAction());

		// settingsMenuBar.addItem(ActionFactory.getChartOfAccountsAction());
		return settingsMenuBar;
	}

	private IMenu getFixedAssetsMenu() {
		IMenu fixedAssetMenu = factory.createMenu();
		fixedAssetMenu.addMenuItem(ActionFactory.getNewFixedAssetAction());
		fixedAssetMenu.addSeparatorItem();
		fixedAssetMenu.addMenuItem(ActionFactory.getDepriciationAction());

		fixedAssetMenu.addSeparatorItem();

		fixedAssetMenu.addMenuItem(Accounter.constants().fixedAssetsList(),
				getFixedAssetsListMenu());

		return fixedAssetMenu;
	}

	private IMenu getVATMenu() {
		IMenu vatmenu = getSubMenu();

		IMenu vatNews = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vatNews.addMenuItem(ActionFactory.getNewVatItemAction());
			// vatNews.addItem(ActionFactory.getVatGroupAction());
			vatNews.addMenuItem(ActionFactory.getNewTAXCodeAction());
			vatNews.addMenuItem(ActionFactory.getNewTAXAgencyAction());
			vatmenu.addMenuItem(Accounter.constants().new1(), vatNews);

			vatmenu.addSeparatorItem();
		}

		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vatmenu.addMenuItem(ActionFactory.getAdjustTaxAction());
			vatmenu.addMenuItem(ActionFactory.getFileTAXAction());
		}
		// vatmenu.addItem(ActionFactory.getCreateTaxesAction());
		if (Accounter.getUser().canDoBanking()) {
			vatmenu.addMenuItem(ActionFactory.getpayTAXAction());
			vatmenu.addMenuItem(ActionFactory.getreceiveVATAction());
			vatmenu.addMenuItem(ActionFactory.getTaxHistoryAction());
		}
		vatmenu.addSeparatorItem();
		if (preferences.isTDSEnabled()) {
			vatmenu.addMenuItem(ActionFactory.getpayTDSAction());
			vatmenu.addMenuItem(Accounter.constants().tds(), getTDSMenu());
			vatmenu.addSeparatorItem();
		}

		vatmenu.addMenuItem(Accounter.constants().vatList(), getVATsListMenu());

		return vatmenu;
	}

	private IMenu getTDSMenu() {
		IMenu tdsMenu = getSubMenu();
		tdsMenu.addMenuItem("TDS Return",
				ActionFactory.getTDSVendorsAction(true));
		tdsMenu.addMenuItem("Form-16A",
				ActionFactory.getTDSVendorsAction(false));
		return tdsMenu;
	}

	private IMenu getVATsListMenu() {
		IMenu vatmenus = getSubMenu();
		vatmenus.addMenuItem(ActionFactory.getVatItemListAction());
		vatmenus.addMenuItem(ActionFactory.getTAXCodeListAction());
		// vatmenus.addItem(ActionFactory.getManageVATGroupListAction());

		return vatmenus;
	}

	private IMenu getFixedAssetsListMenu() {
		IMenu fixedAssetListMenu = getSubMenu();
		fixedAssetListMenu.addMenuItem(ActionFactory
				.getPendingItemsListAction());
		fixedAssetListMenu.addMenuItem(ActionFactory
				.getRegisteredItemsListAction());

		fixedAssetListMenu.addMenuItem(ActionFactory
				.getSoldDisposedListAction());
		return fixedAssetListMenu;
	}

	private IMenu getHelpMenu() {
		IMenu helpMenuBar = getSubMenu();
		// helpMenuBar.addItem(Accounter.constants().help(),
		// new Command() {
		// public void execute() {
		// Window.alert("Not implemented yet!");
		// }
		// });
		// helpMenuBar.addItem(ActionFactory.getFinanceLogAction());
		return helpMenuBar;
	}

	private IMenu getReportMenu() {
		IMenu reportMenuBar = getSubMenu();
		reportMenuBar.addMenuItem(ActionFactory.getReportsHomeAction());
		reportMenuBar.addSeparatorItem();
		reportMenuBar.addMenuItem(Accounter.constants().companyAndFinancial(),
				getCompanyAndFinancialMenu());
		reportMenuBar.addMenuItem(
				Accounter.messages().customersAndReceivable(
						Global.get().Customer()),
				getCustomersAndReceivableMenu());

		reportMenuBar
				.addMenuItem(Accounter.constants().sales(), getSalesMenu());
		reportMenuBar.addMenuItem(
				Global.get().messages()
						.vendorsAndPayables(Global.get().Vendor()),
				getVendorAndPayablesMenu());
		reportMenuBar.addMenuItem(Accounter.constants().purchase(),
				getPurchaseMenu());
		// reportMenuBar.addItem(Accounter.constants().budget() + " "
		// + Accounter.constants().report(), getBudgetSubMenus());
		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// reportMenuBar.addItem(Accounter.constants()
		// .banking(), getBankingSubMenu());
		// }
		if (Accounter.getCompany().getPreferences().isTrackTax()) {
			reportMenuBar.addMenuItem(Accounter.constants().tax(),
					getVATReportMenu());
		}
		// reportMenuBar.addItem(Accounter.constants()
		// .salesAndPurchaseOrders(), getSalesAndPurchaseMenu());
		return reportMenuBar;
	}


	private IMenu getSalesSubMenu() {
		IMenu salesMenu = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesMenu.addMenuItem(ActionFactory.getSalesOrderAction());
		if (Accounter.getUser().canSeeInvoiceTransactions())
			salesMenu.addMenuItem(ActionFactory.getSalesOrderListAction());
		if (Accounter.getUser().canViewReports())
			salesMenu.addMenuItem(ActionFactory.getSalesOpenOrderAction());
		return salesMenu;
	}

	private IMenu getPurchaseSubMenu() {
		IMenu purchaseMenu = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions())
			purchaseMenu.addMenuItem(ActionFactory.getPurchaseOrderAction());
		if (Accounter.getUser().canSeeInvoiceTransactions())
			purchaseMenu
					.addMenuItem(ActionFactory.getPurchaseOrderListAction());
		if (Accounter.getUser().canViewReports())
			purchaseMenu.addMenuItem(ActionFactory
					.getPurchaseOpenOrderListAction());
		return purchaseMenu;
	}

	private IMenu getVendorAndPayablesMenu() {
		IMenu vendorAndPayableMenuBar = getSubMenu();
		vendorAndPayableMenuBar.addMenuItem(ActionFactory
				.getAorpAgingSummaryReportAction());
		vendorAndPayableMenuBar.addMenuItem(ActionFactory
				.getAorpAgingDetailAction());
		vendorAndPayableMenuBar.addMenuItem(ActionFactory
				.getVendorTransactionHistoryAction());
		// vendorAndPayableMenuBar.addItem(ActionFactory
		// .getAmountsDueToVendorsAction());
		return vendorAndPayableMenuBar;
	}

	private IMenu getBudgetSubMenus() {
		IMenu budgetMenu = getSubMenu();

		budgetMenu.addMenuItem(ActionFactory.getBudgetReportsAction(1));
		budgetMenu.addMenuItem(ActionFactory.getBudgetReportsAction(2));
		budgetMenu.addMenuItem(ActionFactory.getBudgetReportsAction(3));
		budgetMenu.addMenuItem(ActionFactory.getBudgetReportsAction(4));
		// vendorAndPayableMenuBar.addItem(ActionFactory
		// .getAmountsDueToVendorsAction());
		return budgetMenu;
	}

	private IMenu getPurchaseMenu() {
		IMenu purchaseMenuBar = getSubMenu();
		purchaseMenuBar.addMenuItem(ActionFactory
				.getPurchaseByVendorSummaryAction());
		purchaseMenuBar.addMenuItem(ActionFactory
				.getPurchaseByVendorDetailAction());
		purchaseMenuBar.addMenuItem(ActionFactory
				.getPurchaseByItemSummaryAction());
		purchaseMenuBar.addMenuItem(ActionFactory.getPurchaseByItemAction());

		if (getPreferences().isPurchaseOrderEnabled()) {
			purchaseMenuBar.addMenuItem(ActionFactory
					.getPurchaseOpenOrderAction());
		}
		return purchaseMenuBar;
	}

	private IMenu getVATReportMenu() {
		IMenu vatReportMenuBar = getSubMenu();
		ICountryPreferences countryPreferences = Accounter.getCompany()
				.getCountryPreferences();
		if (countryPreferences instanceof UnitedKingdom
				&& countryPreferences.isVatAvailable()) {
			vatReportMenuBar.addMenuItem(ActionFactory
					.getVATSummaryReportAction());
			vatReportMenuBar.addMenuItem(ActionFactory
					.getVATDetailsReportAction());
			vatReportMenuBar.addMenuItem(ActionFactory.getVAT100ReportAction());
			vatReportMenuBar.addMenuItem(ActionFactory
					.getVATExceptionDetailsReportAction());
			vatReportMenuBar.addMenuItem(ActionFactory
					.getVATUncategorisedAmountsReportAction());
			vatReportMenuBar.addMenuItem(ActionFactory.getECSalesListAction());
		} else {
			vatReportMenuBar.addMenuItem(ActionFactory
					.getTaxItemDetailReportAction());
			vatReportMenuBar.addMenuItem(ActionFactory
					.getTaxItemExceptionDetailReportAction());
		}

		vatReportMenuBar.addMenuItem(ActionFactory
				.getVATItemSummaryReportAction());

		return vatReportMenuBar;
	}

	private IMenu getSalesMenu() {
		IMenu salesMenuBar = getSubMenu();
		salesMenuBar.addMenuItem(ActionFactory
				.getSalesByCustomerSummaryAction());
		salesMenuBar
				.addMenuItem(ActionFactory.getSalesByCustomerDetailAction());
		salesMenuBar.addMenuItem(ActionFactory.getSalesByItemSummaryAction());
		salesMenuBar.addMenuItem(ActionFactory.getSalesByItemDetailAction());

		if (getPreferences().isSalesOrderEnabled()) {
			salesMenuBar.addMenuItem(ActionFactory.getSalesOpenOrderAction());
		}
		if (Accounter.getCompany().getPreferences().isLocationTrackingEnabled()) {
			salesMenuBar.addMenuItem(ActionFactory
					.getSalesByLocationDetailsAction(true));
			salesMenuBar.addMenuItem(ActionFactory
					.getSalesByLocationSummaryAction(true));
		}

		if (Accounter.getCompany().getPreferences().isClassTrackingEnabled()) {
			salesMenuBar.addMenuItem(ActionFactory
					.getSalesByLocationDetailsAction(false));
			salesMenuBar.addMenuItem(ActionFactory
					.getSalesByLocationSummaryAction(false));
		}
		return salesMenuBar;
	}

	private IMenu getCustomersAndReceivableMenu() {
		IMenu customersAndReceivableMenuBar = getSubMenu();
		customersAndReceivableMenuBar.addMenuItem(ActionFactory
				.getArAgingSummaryReportAction());
		customersAndReceivableMenuBar.addMenuItem(ActionFactory
				.getArAgingDetailAction());
		customersAndReceivableMenuBar.addMenuItem(ActionFactory
				.getStatementReport());
		customersAndReceivableMenuBar.addMenuItem(ActionFactory
				.getCustomerTransactionHistoryAction());
		// customersAndReceivableMenuBar.addItem(ActionFactory
		// .getMostProfitableCustomersAction());

		return customersAndReceivableMenuBar;
	}

	private IMenu getCompanyAndFinancialMenu() {
		IMenu companyAndFinancialMenuBar = getSubMenu();
		companyAndFinancialMenuBar.addMenuItem(ActionFactory
				.getProfitAndLossAction());
		companyAndFinancialMenuBar.addMenuItem(ActionFactory
				.getBalanceSheetAction());
		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		companyAndFinancialMenuBar.addMenuItem(ActionFactory
				.getCashFlowStatementAction());
		companyAndFinancialMenuBar.addMenuItem(ActionFactory
				.getTrialBalanceAction());
		companyAndFinancialMenuBar.addMenuItem(ActionFactory
				.getTransactionDetailByAccountAction());
		companyAndFinancialMenuBar.addMenuItem(ActionFactory
				.getGlReportAction());
		companyAndFinancialMenuBar.addMenuItem(ActionFactory
				.getExpenseReportAction());
		if (Accounter.getCompany().getPreferences().isTrackTax()) {
			companyAndFinancialMenuBar.addMenuItem(ActionFactory
					.getSalesTaxLiabilityAction());
		}
		if (Accounter.getCompany().getPreferences().isTrackTax()) {
			companyAndFinancialMenuBar.addMenuItem(ActionFactory
					.getTransactionDetailByTaxItemAction());
		}
		if (Accounter.getCompany().getPreferences().isLocationTrackingEnabled())
			companyAndFinancialMenuBar.addMenuItem(ActionFactory
					.getProfitAndLossByLocationAction(true));
		if (Accounter.getCompany().getPreferences().isClassTrackingEnabled())
			companyAndFinancialMenuBar.addMenuItem(ActionFactory
					.getProfitAndLossByLocationAction(false));
		return companyAndFinancialMenuBar;
	}

	private IMenu getBankingMenu() {
		IMenu bankingMenuBar = getSubMenu();
		// bankingMenuBar.addItem(ActionFactory.getBankingHomeAction());
		// bankingMenuBar.addSeparator();
		bankingMenuBar.addMenuItem(ActionFactory.getNewBankAccountAction());
		bankingMenuBar.addSeparatorItem();
		// bankingMenuBar.addItem(ActionFactory.getAccountRegisterAction());
		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		bankingMenuBar.addMenuItem(ActionFactory.getWriteChecksAction());

		bankingMenuBar.addMenuItem(ActionFactory.getMakeDepositAction());
		// bankingMenuBar.addItem(ActionFactory.getTransferFundsAction());
		if (Accounter.getCompany().getPreferences().isKeepTrackofBills())
			bankingMenuBar.addMenuItem(ActionFactory.getPayBillsAction());
		// bankingMenuBar.addItem(ActionFactory.getEnterPaymentsAction());
		bankingMenuBar.addSeparatorItem();
		bankingMenuBar.addMenuItem(ActionFactory.getCreditCardChargeAction());
		bankingMenuBar.addSeparatorItem();
		bankingMenuBar
				.addMenuItem(ActionFactory.getReconciliationsListAction());
		bankingMenuBar.addSeparatorItem();
		bankingMenuBar.addMenuItem(Accounter.constants().bankingList(),
				getBankingListMenu());

		return bankingMenuBar;
	}

	private IMenu getBankingListMenu() {
		IMenu bankingListMenuBar = getSubMenu();
		// bankingListMenuBar.addItem(ActionFactory
		// .getChartsOfAccountsAction());
		bankingListMenuBar.addMenuItem(ActionFactory
				.getPaymentsAction(PaymentsAction.BANKING));
		ChartOfAccountsAction chartOfAccountsAction = new ChartOfAccountsAction(
				Accounter.messages().bankAccounts(Global.get().Accounts()),
				ClientAccount.TYPE_BANK);
		bankingListMenuBar.addMenuItem(chartOfAccountsAction);
		return bankingListMenuBar;
	}

	private IMenu getVendorMenu() {
		IMenu vendorMenuBar = getSubMenu();
		vendorMenuBar.addMenuItem(ActionFactory.getVendorsHomeAction());
		vendorMenuBar.addSeparatorItem();
		vendorMenuBar.addMenuItem(Accounter.constants().new1(),
				getNewVendorMenu());
		vendorMenuBar.addSeparatorItem();
		if (Accounter.getUser().canDoInvoiceTransactions())
			if (Accounter.getCompany().getPreferences().isKeepTrackofBills())
				vendorMenuBar.addMenuItem(ActionFactory.getEnterBillsAction());
		if (Accounter.getUser().canDoBanking()) {
			if (Accounter.getCompany().getPreferences().isKeepTrackofBills())
				vendorMenuBar.addMenuItem(ActionFactory.getPayBillsAction());
			if (Accounter.getCompany().getPreferences().isKeepTrackofBills())
				vendorMenuBar.addMenuItem(ActionFactory
						.getIssuePaymentsAction());
			if (Accounter.getCompany().getPreferences().isKeepTrackofBills())
				vendorMenuBar.addMenuItem(ActionFactory
						.getNewVendorPaymentAction());
		}
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			vendorMenuBar.addMenuItem(ActionFactory.getRecordExpensesAction());

			// This should be enabled when user select to track employee
			// expenses.
			if (ClientCompanyPreferences.get().isHaveEpmloyees()
					&& ClientCompanyPreferences.get().isTrackEmployeeExpenses()) {
				vendorMenuBar.addMenuItem(ActionFactory
						.getExpenseClaimsAction(0));
			}
			// vendorMenuBar.addItem(ActionFactory.getItemReceiptAction());
			vendorMenuBar.addSeparatorItem();
		}
		vendorMenuBar.addMenuItem(
				Global.get().messages().payeeLists(Global.get().Vendor()),
				getVendorListMenu());
		return vendorMenuBar;
	}

	private IMenu getVendorListMenu() {
		IMenu vendorListMenuBar = getSubMenu();
		vendorListMenuBar.addMenuItem(ActionFactory.getVendorsAction());
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			ItemsAction itemsAction = ActionFactory.getItemsAction(false, true);
			itemsAction.setCatagory(Global.get().vendor());
			vendorListMenuBar.addMenuItem(itemsAction);
			if (Accounter.getCompany().getPreferences().isKeepTrackofBills())
				vendorListMenuBar.addMenuItem(ActionFactory.getBillsAction());
		}
		if (Accounter.getUser().canSeeBanking())
			vendorListMenuBar.addMenuItem(ActionFactory
					.getVendorPaymentsAction());

		return vendorListMenuBar;
	}

	private IMenu getNewVendorMenu() {
		IMenu newVendorMenuBar = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			newVendorMenuBar.addMenuItem(ActionFactory.getNewVendorAction());
			newVendorMenuBar.addMenuItem(ActionFactory.getNewItemAction(false));
		}
		if (Accounter.getUser().canDoBanking())
			newVendorMenuBar.addMenuItem(ActionFactory
					.getNewCashPurchaseAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			newVendorMenuBar
					.addMenuItem(ActionFactory.getNewCreditMemoAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			newVendorMenuBar.addMenuItem(ActionFactory.getNewCheckAction());

		return newVendorMenuBar;
	}

	private IMenu getCustomerMenu() {
		IMenu customerMenuBar = getSubMenu();
		customerMenuBar.addMenuItem(ActionFactory.getCustomersHomeAction());
		customerMenuBar.addSeparatorItem();
		customerMenuBar.addMenuItem(Accounter.constants().new1(),
				getNewCustomerMenu());
		customerMenuBar.addSeparatorItem();
		if (Accounter.getUser().canDoBanking()) {
			customerMenuBar.addMenuItem(ActionFactory
					.getNewCustomerPaymentAction());
			customerMenuBar
					.addMenuItem(ActionFactory.getReceivePaymentAction());
			customerMenuBar
					.addMenuItem(ActionFactory.getCustomerRefundAction());
			customerMenuBar.addSeparatorItem();
		}
		customerMenuBar.addMenuItem(
				Accounter.messages().payeeLists(Global.get().Customer()),
				getCustomerListMenu());
		return customerMenuBar;
	}

	private IMenu getCustomerListMenu() {
		IMenu customerListMenuBar = getSubMenu();
		customerListMenuBar.addMenuItem(ActionFactory.getCustomersAction());
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			ItemsAction itemsAction = ActionFactory.getItemsAction(true, false);
			itemsAction.setCatagory(Global.get().Customer());
			customerListMenuBar.addMenuItem(itemsAction);
			if (preferences.isDoyouwantEstimates()) {
				customerListMenuBar.addMenuItem(ActionFactory.getQuotesAction(
						Accounter.constants().quotes(), ClientEstimate.QUOTES));
			}
			if (preferences.isDelayedchargesEnabled())
				customerListMenuBar.addMenuItem(ActionFactory
						.getQuotesAction(Accounter.constants().Charges(),
								ClientEstimate.CHARGES));
			customerListMenuBar.addMenuItem(ActionFactory
					.getInvoicesAction(null));

		}
		if (Accounter.getUser().canSeeBanking()) {
			customerListMenuBar.addMenuItem(ActionFactory
					.getReceivedPaymentsAction());
			customerListMenuBar.addMenuItem(ActionFactory
					.getCustomerRefundsAction());
		}

		return customerListMenuBar;
	}

	private IMenu getNewCustomerMenu() {
		IMenu newCustomerMenuBar = getSubMenu();
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			newCustomerMenuBar
					.addMenuItem(ActionFactory.getNewCustomerAction());
			newCustomerMenuBar
					.addMenuItem(ActionFactory.getNewItemAction(true));
			if (preferences.isDoyouwantEstimates()) {
				newCustomerMenuBar.addMenuItem(ActionFactory
						.getNewQuoteAction(ClientEstimate.QUOTES, Accounter
								.constants().newQuote()));
			}
			if (preferences.isDelayedchargesEnabled()) {
				newCustomerMenuBar.addMenuItem(ActionFactory.getNewQuoteAction(
						ClientEstimate.CHARGES, Accounter.constants()
								.newCharge()));
				newCustomerMenuBar.addMenuItem(ActionFactory.getNewQuoteAction(
						ClientEstimate.CREDITS, Accounter.constants()
								.newCredit()));
			}
			newCustomerMenuBar.addMenuItem(ActionFactory.getNewInvoiceAction());
		}
		if (Accounter.getUser().canDoBanking())
			newCustomerMenuBar
					.addMenuItem(ActionFactory.getNewCashSaleAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			newCustomerMenuBar.addMenuItem(ActionFactory
					.getNewCreditsAndRefundsAction());

		return newCustomerMenuBar;
	}

	private IMenu getSubMenu() {
		return factory.createMenu();
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
								Global.get().Accounts()), Accounter.messages()
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

	private IMenu getCompanyMenu() {

		IMenu companyMenuBar = getSubMenu();

		companyMenuBar.addMenuItem(Accounter.constants().dashBoard(),
				getDashBoardCommand());
		companyMenuBar.addSeparatorItem();

		if (Accounter.getUser().canDoBanking())
			companyMenuBar
					.addMenuItem(ActionFactory.getNewJournalEntryAction());
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			companyMenuBar.addMenuItem(ActionFactory.getNewAccountAction());
			companyMenuBar.addSeparatorItem();
		}
		// companyMenuBar.addItem(ActionFactory
		// .getCompanyInformationAction());
		if (Accounter.getUser().canChangeSettings()) {
			PreferencesAction.CATEGORY = PreferencesAction.COMPANY;
			companyMenuBar.addMenuItem(ActionFactory.getPreferencesAction());
			companyMenuBar.addSeparatorItem();
		}
		// companyMenuBar.addItem(ActionFactory.getBudgetActions());
		// companyMenuBar.addSeparator();

		if (getPreferences().isTrackTax()) {
			companyMenuBar.addMenuItem(Accounter.constants().itemTax(),
					getSalesTaxSubmenu());
		}
		if (Accounter.getUser().canChangeSettings()) {
			companyMenuBar.addMenuItem(Accounter.constants()
					.manageSupportLists(), getManageSupportListSubmenu());
			companyMenuBar.addSeparatorItem();
		}
		// if (Accounter.getUser().canManageFiscalYears()) {
		// companyMenuBar.addItem(ActionFactory.getManageFiscalYearAction());
		// companyMenuBar.addSeparator();
		// }

		// FIXME > Need Complete merging functionality
		// companyMenuBar.addItem(
		// Accounter.messages().mergeAccounts(Global.get().Account()),
		// getMergeSubMenu());
		// companyMenuBar.addSeparator();
		companyMenuBar.addMenuItem(Accounter.constants().companyLists(),
				getCompanyListMenu());

		return companyMenuBar;
	}

	private IMenu getMergeSubMenu() {
		IMenu mergeAccountsMenuBar = getSubMenu();
		mergeAccountsMenuBar.addMenuItem(
				Accounter.messages().mergeCustomers(Global.get().Customer()),
				getMergeCustomerCommand());
		mergeAccountsMenuBar.addMenuItem(
				Accounter.messages().mergeVendors(Global.get().Vendor()),
				getMergeVendorCommand());
		mergeAccountsMenuBar.addMenuItem(
				Accounter.messages().mergeAccounts(Global.get().Accounts()),
				getMergeAccountCommand());
		mergeAccountsMenuBar.addMenuItem(Accounter.constants().mergeItems(),
				getMergeItemCommand());
		return mergeAccountsMenuBar;
	}

	private IMenu getCompanyListMenu() {
		IMenu companyListMenuBar = getSubMenu();
		if (Accounter.getUser().canSeeInvoiceTransactions())
			companyListMenuBar.addMenuItem(ActionFactory
					.getChartOfAccountsAction());
		if (Accounter.getUser().canSeeBanking())
			companyListMenuBar.addMenuItem(ActionFactory
					.getJournalEntriesAction());
		if (Accounter.getUser().canSeeInvoiceTransactions()) {
			ItemsAction itemsAction = ActionFactory.getItemsAction(true, true);
			itemsAction.setCatagory(Accounter.messages().bothCustomerAndVendor(
					Global.get().Customer(), Global.get().Vendor()));
			companyListMenuBar.addMenuItem(itemsAction);
		}
		companyListMenuBar.addMenuItem(ActionFactory.getCustomersAction());
		companyListMenuBar.addMenuItem(ActionFactory.getVendorsAction());
		if (Accounter.getUser().canSeeBanking())
			companyListMenuBar.addMenuItem(ActionFactory
					.getPaymentsAction(PaymentsAction.COMPANY));
		companyListMenuBar.addMenuItem(ActionFactory.getSalesPersonAction());
		// companyListMenuBar.addItem(ActionFactory.getWarehouseListAction());
		// companyListMenuBar.addItem(ActionFactory.getRecurringsListAction());
		companyListMenuBar.addMenuItem(ActionFactory
				.getUsersActivityListAction());
		return companyListMenuBar;
	}

	private IMenu getManageSupportListSubmenu() {
		IMenu manageSupportListMenuBar = getSubMenu();
		manageSupportListMenuBar.addMenuItem(ActionFactory
				.getCustomerGroupListAction());
		manageSupportListMenuBar.addMenuItem(ActionFactory
				.getVendorGroupListAction());
		manageSupportListMenuBar.addMenuItem(ActionFactory
				.getPaymentTermListAction());
		manageSupportListMenuBar.addMenuItem(ActionFactory
				.getShippingMethodListAction());
		manageSupportListMenuBar.addMenuItem(ActionFactory
				.getShippingTermListAction());
		/*
		 * manageSupportListMenuBar.addItem(ActionFactory
		 * .getPriceLevelListAction());
		 */
		manageSupportListMenuBar.addMenuItem(ActionFactory
				.getItemGroupListAction());
		manageSupportListMenuBar.addMenuItem(ActionFactory
				.getCreditRatingListAction());
		manageSupportListMenuBar.addMenuItem(ActionFactory
				.getCurrencyGroupListAction());
		if (Accounter.getCompany().getPreferences().isLocationTrackingEnabled())
			manageSupportListMenuBar.addMenuItem(ActionFactory
					.getLocationGroupListAction());
		if (Accounter.getCompany().getPreferences().isClassTrackingEnabled())
			manageSupportListMenuBar.addMenuItem(ActionFactory
					.getAccounterClassGroupListAction());
		// manageSupportListMenuBar.addItem(ActionFactory.getCountryRegionListAction());
		// manageSupportListMenuBar.addItem(ActionFactory
		// .getFormLayoutsListAction());
		// manageSupportListMenuBar.addItem(ActionFactory
		// .getPayTypeListAction());

		return manageSupportListMenuBar;
	}

	private IMenu getSalesTaxSubmenu() {
		IMenu salesTaxMenuBar = getSubMenu();
		salesTaxMenuBar.addMenuItem(ActionFactory
				.getManageSalesTaxGroupsAction());
		// salesTaxMenuBar.addItem(ActionFactory
		// .getManageSalesTaxCodesAction());
		salesTaxMenuBar.addMenuItem(ActionFactory
				.getManageSalesTaxItemsAction());
		if (Accounter.getUser().canDoBanking())
			salesTaxMenuBar.addMenuItem(ActionFactory.getPaySalesTaxAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesTaxMenuBar.addMenuItem(ActionFactory.getAdjustTaxAction());
		// salesTaxMenuBar.addItem(ActionFactory
		// .getViewSalesTaxLiabilityAction());
		// salesTaxMenuBar.addItem(ActionFactory.getTaxItemAction());
		if (Accounter.getUser().canDoInvoiceTransactions())
			salesTaxMenuBar.addMenuItem(ActionFactory.getNewTAXAgencyAction());
		return salesTaxMenuBar;
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

}
