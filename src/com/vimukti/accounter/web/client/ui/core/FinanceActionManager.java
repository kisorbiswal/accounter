package com.vimukti.accounter.web.client.ui.core;
//
//import com.google.gwt.core.client.GWT;
//import com.smartgwt.client.widgets.events.ClickEvent;
//import com.smartgwt.client.widgets.menu.MenuBar;
//import com.smartgwt.client.widgets.menu.events.ClickHandler;
//import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
//import com.vimukti.accounter.web.client.externalization.ActionsConstants;
//import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
//import com.vimukti.accounter.web.client.ui.company.ExitAction;
//import com.vimukti.accounter.web.client.ui.company.HelpAction;
//
//public class FinanceActionManager {
//	private MainFinanceWindow window;
//	private ActionsConstants actionsConstants;
//
//	public FinanceActionManager(MainFinanceWindow winFinanceWindow) {
//		this.actionsConstants = GWT.create(ActionsConstants.class);
//		this.window = winFinanceWindow;
//	}
//
//	public MenuBar getMenuBar() {
//		MenuBar menuBar = new MenuBar();
//
//		menuBar.setMenus(getFileMenu(), getEditMenu(), getViewMenu(),
//				getCompanyMenu(), getCustomerMenu(), getVendorsMenu(),
//				getBankingMenu(), getReportsMenu(), getHelpMenu());
//		// getPaypalPaymentsMenu(),
//		// getCreditProfileMenu(), getFixedAssetsMenu(),
//		// getIncomeTaxMenu()
//		return menuBar;
//
//	}
//
//	private MenuManager getFileMenu() {
//		MenuManager fileMenu = new MenuManager(actionsConstants.file());
//		fileMenu.setWidth("10%");
//		fileMenu.add(CompanyActionFactory.getNewCompanyAction());
//		fileMenu.add(CompanyActionFactory.getOpenCompanyAction());
//		fileMenu.add(CompanyActionFactory.getCloseCompanyAction());
//		fileMenu.addSeparator();
//		// fileMenu.add(openSelectedItemsAction);
//		fileMenu.add(CompanyActionFactory.getQuickStartAction());
//		fileMenu.addSeparator();
//		fileMenu.add(CompanyActionFactory.getPageSetupAction());
//		fileMenu.add(CompanyActionFactory.getPrintPreviewAction());
//		fileMenu.add(CompanyActionFactory.getPrintAction());
//		ExitAction exit = CompanyActionFactory.getExitAction();
//		exit.isEdit(window, null);
//		fileMenu.add(exit);
//		return fileMenu;
//	}
//
//	private MenuManager getEditMenu() {
//		MenuManager editMenu = new MenuManager(actionsConstants.edit());
//		editMenu.setWidth("10%");
//		editMenu.add(CompanyActionFactory.getDeleteAction());
//		editMenu.add(CompanyActionFactory.getSelectAllAction());
//		editMenu.add(CompanyActionFactory.getMakeActiveAction());
//		editMenu.add(CompanyActionFactory.getMakeInActiveAction());
//
//		return editMenu;
//
//	}
//
//	private MenuManager getViewMenu() {
//		MenuManager viewMenu = new MenuManager(actionsConstants.view());
//		viewMenu.setWidth("10%");
//		viewMenu.add(CompanyActionFactory.getArrangeByAction());
//		viewMenu.add(CompanyActionFactory.getFilterByAction());
//		viewMenu.add(CompanyActionFactory.getRefreshAction());
//		viewMenu.add(CompanyActionFactory.getSwitchViewAction());
//		viewMenu.add(CompanyActionFactory.getAddRemoveContentAction());
//
//		return viewMenu;
//
//	}
//
//	private MenuManager getCustomerMenu() {
//		MenuManager customerMenu = new MenuManager(actionsConstants.customers());
//		customerMenu.setWidth("10%");
//		// MenuManager newMenu = new MenuManager(actionsConstants.New());
//		// MenuManager creditcardMenu = new MenuManager(actionsConstants
//		// .creditCardProcessing());
//		MenuManager customerListsMenu = new MenuManager(actionsConstants
//				.cutomerList());
//
//		Action[] newAction = { CustomersActionFactory.getNewCustomerAction(),
//				CustomersActionFactory.getNewItemAction(),
//				CustomersActionFactory.getNewQuoteAction(),
//				CustomersActionFactory.getNewInvoiceAction(),
//				CustomersActionFactory.getNewCashSaleAction(),
//				CustomersActionFactory.getNewCreditsAndRefundsAction() };
//
//		// Action[] creditCardAction = { signupForCreditcardProcessingAction,
//		// manageCreditCardProcessingAction };
//
//		Action[] customerListsAction = {
//				CustomersActionFactory.getCustomersAction(),
//				CustomersActionFactory.getItemsAction(),
//				CustomersActionFactory.getQuotesAction(),
//				CustomersActionFactory.getInvoicesAction(),
//				CustomersActionFactory.getReceivedPaymentsAction(),
//				CustomersActionFactory.getCustomerRefundsAction() };
//		customerMenu.add(CustomersActionFactory.getCustomersHomeAction());
//		// customerMenu.addSeparator();
//		// newMenu.add(newAction);
//		// customerMenu.addSubMenu(newMenu);
//		// customerMenu.addSeparator();
//		customerMenu.add(CustomersActionFactory.getReceivedPaymentsAction());
//		customerMenu.add(CustomersActionFactory.getCustomerRefundAction());
//		customerMenu.add(CustomersActionFactory.getCreateStatementAction());
//		// customerMenu.addSeparator();
//		// creditcardMenu.add(creditCardAction);
//		// customerMenu.addSubMenu(creditcardMenu);
//		// customerMenu.addSeparator();
//		customerMenu.add(CompanyActionFactory.getPriceLevelListAction());
//		// customerMenu.add(CustomersActionFactory.get);
//		// customerMenu.add(manageWordTemplatesAction);
//		// customerMenu.add(manageEMailCoverLettersAction);
//		// customerMenu.addSeparator();
//		// customerListsMenu.add(customerListsAction);
//		//
//		// customerMenu.addSubMenu(customerListsMenu);
//
//		return customerMenu;
//
//	}
//
//	private MenuManager getCompanyMenu() {
//		MenuManager companyMenu = new MenuManager(actionsConstants.company());
//		companyMenu.setWidth("10%");
//		// MenuManager businessServicesMenu = new MenuManager(actionsConstants
//		// .businessServices());
//		// MenuManager mergerAccountsMenu = new MenuManager(actionsConstants
//		// .mergeAccounts());
//		MenuManager salesTaxMenu = new MenuManager(actionsConstants.salesTax());
//		MenuManager manageSupportListsMenu = new MenuManager(actionsConstants
//				.managerSupportLists());
//		MenuManager companyListsMenu = new MenuManager(actionsConstants
//				.companyLists());
//
//		// businessServicesMenu.add(VendorsActionFactory
//		// .getServicesOverviewAction());
//		// businessServicesMenu.add(VendorsActionFactory
//		// .getBuyChecksAndFormsAction());
//		// businessServicesMenu.add(VendorsActionFactory.
//		// getacceptCreditCardsAction);
//		// businessServicesMenu.add(VendorsActionFactory.getBankOnlineAction());
//		//
//		//mergerAccountsMenu.add(CompanyActionFactory.getMergeCustomersAction())
//		// ;
//		// mergerAccountsMenu.add(CompanyActionFactory.getMergeVendorsAction());
//		// mergerAccountsMenu.add(CompanyActionFactory.getMergeItemsAction());
//		// mergerAccountsMenu.add(CompanyActionFactory
//		// .getMergeFinancialAccountsAction());
//
//		salesTaxMenu.add(CompanyActionFactory.getManageSalesTaxGroupsAction());
//		salesTaxMenu.add(CompanyActionFactory.getManageSalesTaxCodesAction());
//		salesTaxMenu.add(CompanyActionFactory.getManageItemTaxAction());
//		salesTaxMenu.add(CompanyActionFactory.getPaySalesTaxAction());
//		salesTaxMenu.add(CompanyActionFactory.getViewSalesTaxLiabilityAction());
//		salesTaxMenu.add(CompanyActionFactory.getNewTaxAgencyAction());
//
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getCustomerGroupListAction());
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getVendorGroupListAction());
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getPaymentTermListAction());
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getShippingMethodListAction());
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getShippingTermListAction());
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getPriceLevelListAction());
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getItemGroupListAction());
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getCreditRatingListAction());
//		manageSupportListsMenu.add(CompanyActionFactory
//				.getCountryRegionListAction());
//		// manageSupportListsMenu.add(CompanyActionFactory
//		// .getFormLayoutsListAction());
//		//manageSupportListsMenu.add(CompanyActionFactory.getPayTypeListAction()
//		// );
//
//		companyListsMenu.add(CompanyActionFactory.getChartOfAccountsAction());
//		companyListsMenu.add(CompanyActionFactory.getJournalEntriesAction());
//		companyListsMenu.add(CustomersActionFactory.getItemsAction());
//		companyListsMenu.add(CustomersActionFactory.getCustomersAction());
//		companyListsMenu.add(VendorsActionFactory.getVendorsAction());
//		companyListsMenu.add(BankingActionFactory.getPaymentsAction());
//		companyListsMenu
//				.add(CompanyActionFactory.getNewSalesPersonListAction());
//
//		// companyMenu.addSubMenu(businessServicesMenu);
//		companyMenu.add(CompanyActionFactory.getCompanyInformationAction());
//		companyMenu.add(CompanyActionFactory.getPreferencesAction());
//		companyMenu.add(CompanyActionFactory
//				.getIntegrateWithBusinessContactManagerAction());
//		companyMenu.addSeparator();
//		companyMenu.add(CompanyActionFactory.getNewJournalEntryAction());
//		companyMenu.add(CompanyActionFactory
//				.getNewCashBasisJournalEntryAction());
//		companyMenu.add(CompanyActionFactory.getNewAccountAction());
//		// companyMenu.addSubMenu(mergerAccountsMenu);
//		companyMenu.addSeparator();
//
//		companyMenu.addSubMenu(salesTaxMenu);
//		companyMenu.addSubMenu(manageSupportListsMenu);
//		companyMenu.add(CompanyActionFactory.getManageFiscalYearAction());
//		companyMenu.addSeparator();
//		// companyMenu.add(VendorsActionFactory.getWriteLettersAction());
//		// companyMenu.add(VendorsActionFactory.getManageWordTemplatesAction());
//		// companyMenu
//		// .add(VendorsActionFactory.getManageEMailCoverLettersAction());
//		companyMenu.addSeparator();
//		companyMenu.addSubMenu(companyListsMenu);
//
//		return companyMenu;
//
//	}
//
//	private MenuManager getVendorsMenu() {
//
//		MenuManager vendorMenu = new MenuManager(actionsConstants.vendors());
//		vendorMenu.setWidth("10%");
//		MenuManager newMenu = new MenuManager(actionsConstants.New());
//		// MenuManager vendorServicesMenu = new MenuManager(actionsConstants
//		// .vendorServices());
//		MenuManager vendorListsMenu = new MenuManager(actionsConstants
//				.vendorLists());
//
//		newMenu.add(VendorsActionFactory.getNewVendorAction());
//		newMenu.add(VendorsActionFactory.getNewItemAction());
//		newMenu.add(VendorsActionFactory.getNewCashPurchaseAction());
//		newMenu.add(VendorsActionFactory.getNewCreditMemoAction());
//		newMenu.add(VendorsActionFactory.getNewCheckAction());
//
//		// vendorServicesMenu
//		// .add(VendorsActionFactory.getServicesOverviewAction());
//		// vendorServicesMenu.add(VendorsActionFactory
//		// .getBuyChecksAndFormsAction());
//		// vendorServicesMenu.add(VendorsActionFactory.getBankOnlineAction());
//
//		vendorListsMenu.add(VendorsActionFactory.getVendorsAction());
//		vendorListsMenu.add(CustomersActionFactory.getItemsAction());
//		vendorListsMenu.add(VendorsActionFactory.getBillsAction());
//		vendorListsMenu.add(VendorsActionFactory.getVendorPaymentsAction());
//
//		vendorMenu.add(VendorsActionFactory.getVendorsHomeAction());
//		// vendorMenu.addSeparator();
//		// vendorMenu.addSubMenu(newMenu);
//		// vendorMenu.addSeparator();
//		vendorMenu.add(VendorsActionFactory.getEnterBillsAction());
//		vendorMenu.add(VendorsActionFactory.getPayBillsAction());
//		vendorMenu.add(VendorsActionFactory.getIssuePaymentsAction());
//		// vendorMenu.add(VendorsActionFactory.getNewVendorPaymentAction());
//		vendorMenu.add(VendorsActionFactory.getRecordExpensesAction());
//		// vendorMenu.addSeparator();
//		// vendorMenu.add(VendorsActionFactory.getWriteLettersAction());
//		// vendorMenu.add(VendorsActionFactory.getManageWordTemplatesAction());
//		//vendorMenu.add(VendorsActionFactory.getManageEMailCoverLettersAction()
//		// );
//		// vendorMenu.addSeparator();
//		// vendorMenu.addSubMenu(vendorServicesMenu);
//		// vendorMenu.addSeparator();
//		// vendorMenu.addSubMenu(vendorListsMenu);
//
//		return vendorMenu;
//
//	}
//
//	private MenuManager getBankingMenu() {
//		MenuManager bankingMenu = new MenuManager(actionsConstants.banking());
//		bankingMenu.setWidth("10%");
//		// MenuManager bankOnlineMenu = new MenuManager(actionsConstants
//		// .bankOnline());
//		// MenuManager creditcardProcessingMenu = new
//		// MenuManager(actionsConstants
//		// .creditCardProcessing());
//		// MenuManager bankingServicesMenu = new MenuManager(actionsConstants
//		// .bankingServices());
//		MenuManager bankingListsMenu = new MenuManager(actionsConstants
//				.bankingLists());
//
//		// Action[] bankingServices = {
//		// VendorsActionFactory.getServicesOverviewAction(),
//		// VendorsActionFactory.getBuyChecksAndFormsAction() };
//		Action[] bankingLists = {
//				CompanyActionFactory.getChartOfAccountsAction(),
//				BankingActionFactory.getPaymentsAction() };
//
//		bankingMenu.add(BankingActionFactory.getBankingHomeAction());
//		// bankingMenu.addSeparator();
//		bankingMenu.add(BankingActionFactory.getNewBankAccountAction());
//		// bankingMenu.addSeparator();
//
//		bankingMenu.add(BankingActionFactory.getAccountRegisterAction());
//		bankingMenu.add(BankingActionFactory.getWriteChecksAction());
//		bankingMenu.add(BankingActionFactory.getMakeDepositAction());
//		bankingMenu.add(VendorsActionFactory.getPayBillsAction());
//		bankingMenu.add(BankingActionFactory.getEnterPaymentsAction());
//		// // bankingMenu.addSeparator();
//		// // bankingMenu.addSubMenu(bankOnlineMenu);
//		// // bankingMenu.addSeparator();
//		// // bankingMenu.addSubMenu(creditcardProcessingMenu);
//		// bankingMenu.add(BankingActionFactory.getMatchTrasactionsAction());
//		// bankingMenu.add(BankingActionFactory.getCreditCardChargeAction());
//		// bankingMenu.add(BankingActionFactory.getReconcileAccountAction);
//		// bankingMenu.addSeparator();
//		bankingMenu.add(BankingActionFactory.getPrintChecksAction());
//		// bankingMenu.addSeparator();
//		// bankingServicesMenu.add(bankingServices);
//		// // bankingMenu.addSubMenu(bankingServicesMenu);
//		// bankingListsMenu.add(bankingLists);
//		// bankingMenu.addSubMenu(bankingListsMenu);
//
//		return bankingMenu;
//
//	}
//
//	private MenuManager getReportsMenu() {
//		MenuManager reportMenu = new MenuManager(actionsConstants.reports());
//		reportMenu.setWidth("10%");
//		// MenuManager analysisToolsMenu = new MenuManager(actionsConstants
//		// .analysisTools());
//		MenuManager companyandFinancialMenu = new MenuManager(actionsConstants
//				.companyandFinancial());
//		MenuManager companyandReceivableMenu = new MenuManager(actionsConstants
//				.companiesAndReceivable());
//		MenuManager salesMenu = new MenuManager(actionsConstants.sales());
//		MenuManager purchaseMenu = new MenuManager(actionsConstants.purchase());
//		MenuManager vendorPayablesMenu = new MenuManager(actionsConstants
//				.vendorsAndPayables());
//		MenuManager bankingMenu = new MenuManager(actionsConstants.banking());
//		// MenuManager employeepayrollMenu = new
//		// MenuManager("Employee payroll");
//		MenuManager cashBasisMenu = new MenuManager(actionsConstants
//				.cashBasis());
//		MenuManager incomeTaxMenu = new MenuManager(actionsConstants
//				.incomeTax());
//
//		// Action[] analysisTools = {
//		// ReportsActionFactory.getSalesAnalysisAction(),
//		// ReportsActionFactory.getPurchaseAnalysisAction(),
//		// ReportsActionFactory.getVendorListAction(),
//		// ReportsActionFactory.getPaymentListAction(),
//		// ReportsActionFactory.getItemListAction(),
//		// ReportsActionFactory.getInvoiceListAction(),
//		// CustomersActionFactory.getCustomersAction(),
//		// ReportsActionFactory.getProfitAndLossAction() };
//
//		Action[] companyandFinancial = {
//				ReportsActionFactory.getProfitAndLossAction(),
//				ReportsActionFactory.getBalanceSheetAction(),
//				ReportsActionFactory.getCashFlowStatementAction(),
//				ReportsActionFactory.getTrialBalanceAction(),
//				ReportsActionFactory.getTransactionDetailByAccountAction(),
//				ReportsActionFactory.getGlReportAction(),
//				ReportsActionFactory.getSalesTaxLiabilityAction(),
//				ReportsActionFactory.getTransactionDetailByTaxCodeAction(), };
//		// ReportsActionFactory.getSalesTaxCollectedThisQuarterAction() };
//
//		Action[] companiesReceivable = {
//				ReportsActionFactory.getArAgingDetailAction(),
//				ReportsActionFactory.getCustomerTransactionHistoryAction(),
//				// ReportsActionFactory.getOpenInvoicesbyDueDateAction(),
//				ReportsActionFactory.getMostProfitableCustomersAction() };
//		Action[] sales = {
//				ReportsActionFactory.getSalesByCustomerSummaryAction(),
//				ReportsActionFactory.getSalesByCustomerDetailAction(),
//				ReportsActionFactory.getSalesByItemSummmaryAction(),
//				ReportsActionFactory.getSalesByItemDetailAction(),
//		// ReportsActionFactory.getOnlineSalesByCustomerSummaryAction(),
//		// ReportsActionFactory.getOnlineSalesByItemSummaryAction(),
//		// ReportsActionFactory.getTodaysSalesByCustomerAction(),
//		// ReportsActionFactory.getYtdSalesComparedToLastYearAction()
//		};
//
//		Action[] purchases = {
//				ReportsActionFactory.getPurchaseByVendorSummaryAction(),
//				ReportsActionFactory.getPurchaseByVendorDetailAction(),
//				ReportsActionFactory.getPurchaseByItemSummaryAction(),
//				ReportsActionFactory.getPurchaseByItemAction(), };
//		Action[] vendorpayable = {
//				ReportsActionFactory.getAorpAgingDetailAction(),
//				ReportsActionFactory.getVendorTransactionHistoryAction(),
//				// ReportsActionFactory.getTodaysVendorPaymentsAction(),
//				ReportsActionFactory.getAmountsDueToVendorsAction() };
//		Action[] banking = { BankingActionFactory.getPaymentsAction(),
//		// ReportsActionFactory.getFailedPaymentsAction(),
//		// ReportsActionFactory.getUndepositedFundsCollectedTodayAction()
//		};
//
//		// Action[] cashBasis = { ReportsActionFactory.getBalanceSheetAction(),
//		// ReportsActionFactory.getProfitAndLossAction(),
//		// ReportsActionFactory.getTransactionDetailByAccountAction(),
//		// ReportsActionFactory.getGlReportAction(),
//		// ReportsActionFactory.getSalesTaxLiabilityAction(),
//		// ReportsActionFactory.getTransactionDetailByTaxCodeAction(),
//		// ReportsActionFactory.getSalesByCustomerSummaryAction(),
//		// ReportsActionFactory.getSalesByCustomerDetailAction(),
//		// ReportsActionFactory.getSalesByItemSummaryAction(),
//		// ReportsActionFactory.getSalesByItemDetailAction(),
//		// ReportsActionFactory.getOnlineSalesByCustomerSummaryAction(),
//		// ReportsActionFactory.getOnlineSalesByItemSummaryAction()
//		//
//		// };
//
//		reportMenu.add(ReportsActionFactory.getReportsHomeAction());
//
//		// analysisToolsMenu.add(analysisTools);
//		// reportMenu.addSubMenu(analysisToolsMenu);
//
//		// companyandFinancialMenu.add(companyandFinancial);
//		// reportMenu.addSubMenu(companyandFinancialMenu);
//		//
//		// companyandReceivableMenu.add(companiesReceivable);
//		// reportMenu.addSubMenu(companyandReceivableMenu);
//		//
//		// salesMenu.add(sales);
//		// reportMenu.addSubMenu(salesMenu);
//		//
//		// purchaseMenu.add(purchases);
//		// reportMenu.addSubMenu(purchaseMenu);
//		//
//		// vendorPayablesMenu.add(vendorpayable);
//		// reportMenu.addSubMenu(vendorPayablesMenu);
//		//
//		// bankingMenu.add(banking);
//		// reportMenu.addSubMenu(bankingMenu);
//
//		// employeepayrollMenu.add(ReportsActionFactory.getTimeReportAction());
//		// reportMenu.addSubMenu(employeepayrollMenu);
//
//		// cashBasisMenu.add(cashBasis);
//		// reportMenu.addSubMenu(cashBasisMenu);
//		//
//		// incomeTaxMenu.add(ReportsActionFactory.getTaxSummaryAction());
//		//incomeTaxMenu.add(ReportsActionFactory.getTaxSummaryCashBasisAction())
//		// ;
//		// reportMenu.addSubMenu(incomeTaxMenu);
//
//		return reportMenu;
//
//	}
//
//	private MenuManager getPaypalPaymentsMenu() {
//		MenuManager payPalMenu = new MenuManager(actionsConstants
//				.paypalPayments());
//		payPalMenu.setWidth("10%");
//		payPalMenu.add(actionsConstants.payBalSettings(),
//				"/images/icons/paybal.png", new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//						// XXX
//					}
//
//				});
//		payPalMenu.add(actionsConstants.importPayments(),
//				"/images/icons/import_payments.png", new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//
//					}
//
//				});
//		payPalMenu.add(actionsConstants.payBalHelp(),
//				"/images/icons/paybal_help.png", new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//
//					}
//
//				});
//
//		return payPalMenu;
//
//	}
//
//	private MenuManager getCreditProfileMenu() {
//		MenuManager creditprofileMenu = new MenuManager(actionsConstants
//				.creditProfile());
//		creditprofileMenu.setWidth("10%");
//		return creditprofileMenu;
//
//	}
//
//	private MenuManager getFixedAssetsMenu() {
//		MenuManager fixedAssetsMenu = new MenuManager(actionsConstants
//				.fixedAssets());
//		fixedAssetsMenu.setWidth("10%");
//		fixedAssetsMenu.add(actionsConstants.fixedAssetManager(),
//				"fixed_asset_manager.png", new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//						// FIXME
//					}
//
//				});
//		fixedAssetsMenu.addSeparator();
//		fixedAssetsMenu.add(actionsConstants.fixedAssetshelp(),
//				"/images/icons/fixed_assets_help.png", new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//
//					}
//
//				});
//		return fixedAssetsMenu;
//
//	}
//
//	private MenuManager getIncomeTaxMenu() {
//		MenuManager incomeTaxMenu = new MenuManager(actionsConstants
//				.incomeTax());
//		incomeTaxMenu.add(actionsConstants.downloadTaxLines(), "",
//				new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//						// FIXME
//					}
//
//				});
//		incomeTaxMenu.add(actionsConstants.maxTaxLines(),
//				"/images/icons/import_payments.png", new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//
//					}
//
//				});
//		incomeTaxMenu.add(actionsConstants.help(), "", new ClickHandler() {
//
//			public void onClick(MenuItemClickEvent event) {
//
//			}
//
//		});
//
//		return incomeTaxMenu;
//
//	}
//
//	private MenuManager getHelpMenu() {
//		MenuManager helpMenu = new MenuManager(actionsConstants.help());
//		helpMenu.setWidth("10%");
//		helpMenu.add(actionsConstants.HelpwiththisWindow(), "",
//				new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//						// FIXME
//					}
//
//				});
//		helpMenu.addSeparator();
//		helpMenu.add(actionsConstants.support(), "", new ClickHandler() {
//
//			public void onClick(MenuItemClickEvent event) {
//
//			}
//
//		});
//		helpMenu.addSeparator();
//		helpMenu.add(actionsConstants.aboutAccounting(), "",
//				new ClickHandler() {
//
//					public void onClick(MenuItemClickEvent event) {
//
//					}
//
//				});
//
//		return helpMenu;
//
//	}
//
//	public ToolBarManager getToolar() {
//		ToolBarManager toolBarManager = new ToolBarManager();
//		// toolBarManager.setBackgroundImage("");
//
//		Action[] menuActions = { CustomersActionFactory.getNewCustomerAction(),
//				VendorsActionFactory.getNewVendorAction(),
//				CustomersActionFactory.getNewQuoteAction(),
//				CustomersActionFactory.getNewInvoiceAction(),
//				CustomersActionFactory.getNewItemAction(),
//				VendorsActionFactory.getNewCheckAction(),
//				VendorsActionFactory.getPayBillsAction(),
//				CompanyActionFactory.getJournalEntriesAction()
//
//		};
//		toolBarManager.addMenuItem(menuActions, actionsConstants.New(),
//				"/images/icons/customers/new_customer.png");
//		toolBarManager.addSeparator();
//		// toolBarManager.add("", "/images/icons/print.png","25",
//		// new com.smartgwt.client.widgets.events.ClickHandler() {
//		//
//		// public void onClick(ClickEvent event) {
//		//
//		// }
//		//
//		// });
//
//		toolBarManager.add(CompanyActionFactory.getPrintAction());
//		toolBarManager.add(CompanyActionFactory.getDeleteAction());
//		toolBarManager.add(CompanyActionFactory.getRefreshAction());
//		toolBarManager.addSeparator();
//		toolBarManager.add(CompanyActionFactory.getQuickStartAction());
//		toolBarManager.addSeparator();
//		toolBarManager.add("", "/images/icons/find.png", "25",
//				new com.smartgwt.client.widgets.events.ClickHandler() {
//
//					public void onClick(ClickEvent event) {
//
//					}
//
//				});
//		toolBarManager.addSeparator();
//		toolBarManager.add(new HelpAction("", "/images/icons/help.png"));
//		toolBarManager.addSeparator();
//		toolBarManager.add(actionsConstants.mixedTaxLines(), "", "",
//				new com.smartgwt.client.widgets.events.ClickHandler() {
//
//					public void onClick(ClickEvent event) {
//
//					}
//
//				});
//		toolBarManager.add(actionsConstants.fixedAssets(), "", "",
//				new com.smartgwt.client.widgets.events.ClickHandler() {
//
//					public void onClick(ClickEvent event) {
//
//					}
//
//				});
//
//		return toolBarManager;
//	}
//}
