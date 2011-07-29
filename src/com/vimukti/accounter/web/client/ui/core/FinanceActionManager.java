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
//		fileMenu.add(ActionFactory.getNewCompanyAction());
//		fileMenu.add(ActionFactory.getOpenCompanyAction());
//		fileMenu.add(ActionFactory.getCloseCompanyAction());
//		fileMenu.addSeparator();
//		// fileMenu.add(openSelectedItemsAction);
//		fileMenu.add(ActionFactory.getQuickStartAction());
//		fileMenu.addSeparator();
//		fileMenu.add(ActionFactory.getPageSetupAction());
//		fileMenu.add(ActionFactory.getPrintPreviewAction());
//		fileMenu.add(ActionFactory.getPrintAction());
//		ExitAction exit = ActionFactory.getExitAction();
//		exit.isEdit(window, null);
//		fileMenu.add(exit);
//		return fileMenu;
//	}
//
//	private MenuManager getEditMenu() {
//		MenuManager editMenu = new MenuManager(actionsConstants.edit());
//		editMenu.setWidth("10%");
//		editMenu.add(ActionFactory.getDeleteAction());
//		editMenu.add(ActionFactory.getSelectAllAction());
//		editMenu.add(ActionFactory.getMakeActiveAction());
//		editMenu.add(ActionFactory.getMakeInActiveAction());
//
//		return editMenu;
//
//	}
//
//	private MenuManager getViewMenu() {
//		MenuManager viewMenu = new MenuManager(actionsConstants.view());
//		viewMenu.setWidth("10%");
//		viewMenu.add(ActionFactory.getArrangeByAction());
//		viewMenu.add(ActionFactory.getFilterByAction());
//		viewMenu.add(ActionFactory.getRefreshAction());
//		viewMenu.add(ActionFactory.getSwitchViewAction());
//		viewMenu.add(ActionFactory.getAddRemoveContentAction());
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
//		Action[] newAction = { ActionFactory.getNewCustomerAction(),
//				ActionFactory.getNewItemAction(),
//				ActionFactory.getNewQuoteAction(),
//				ActionFactory.getNewInvoiceAction(),
//				ActionFactory.getNewCashSaleAction(),
//				ActionFactory.getNewCreditsAndRefundsAction() };
//
//		// Action[] creditCardAction = { signupForCreditcardProcessingAction,
//		// manageCreditCardProcessingAction };
//
//		Action[] customerListsAction = {
//				ActionFactory.getCustomersAction(),
//				ActionFactory.getItemsAction(),
//				ActionFactory.getQuotesAction(),
//				ActionFactory.getInvoicesAction(),
//				ActionFactory.getReceivedPaymentsAction(),
//				ActionFactory.getCustomerRefundsAction() };
//		customerMenu.add(ActionFactory.getCustomersHomeAction());
//		// customerMenu.addSeparator();
//		// newMenu.add(newAction);
//		// customerMenu.addSubMenu(newMenu);
//		// customerMenu.addSeparator();
//		customerMenu.add(ActionFactory.getReceivedPaymentsAction());
//		customerMenu.add(ActionFactory.getCustomerRefundAction());
//		customerMenu.add(ActionFactory.getCreateStatementAction());
//		// customerMenu.addSeparator();
//		// creditcardMenu.add(creditCardAction);
//		// customerMenu.addSubMenu(creditcardMenu);
//		// customerMenu.addSeparator();
//		customerMenu.add(ActionFactory.getPriceLevelListAction());
//		// customerMenu.add(ActionFactory.get);
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
//		// businessServicesMenu.add(ActionFactory
//		// .getServicesOverviewAction());
//		// businessServicesMenu.add(ActionFactory
//		// .getBuyChecksAndFormsAction());
//		// businessServicesMenu.add(ActionFactory.
//		// getacceptCreditCardsAction);
//		// businessServicesMenu.add(ActionFactory.getBankOnlineAction());
//		//
//		//mergerAccountsMenu.add(ActionFactory.getMergeCustomersAction())
//		// ;
//		// mergerAccountsMenu.add(ActionFactory.getMergeVendorsAction());
//		// mergerAccountsMenu.add(ActionFactory.getMergeItemsAction());
//		// mergerAccountsMenu.add(ActionFactory
//		// .getMergeFinancialAccountsAction());
//
//		salesTaxMenu.add(ActionFactory.getManageSalesTaxGroupsAction());
//		salesTaxMenu.add(ActionFactory.getManageSalesTaxCodesAction());
//		salesTaxMenu.add(ActionFactory.getManageItemTaxAction());
//		salesTaxMenu.add(ActionFactory.getPaySalesTaxAction());
//		salesTaxMenu.add(ActionFactory.getViewSalesTaxLiabilityAction());
//		salesTaxMenu.add(ActionFactory.getNewTaxAgencyAction());
//
//		manageSupportListsMenu.add(ActionFactory
//				.getCustomerGroupListAction());
//		manageSupportListsMenu.add(ActionFactory
//				.getVendorGroupListAction());
//		manageSupportListsMenu.add(ActionFactory
//				.getPaymentTermListAction());
//		manageSupportListsMenu.add(ActionFactory
//				.getShippingMethodListAction());
//		manageSupportListsMenu.add(ActionFactory
//				.getShippingTermListAction());
//		manageSupportListsMenu.add(ActionFactory
//				.getPriceLevelListAction());
//		manageSupportListsMenu.add(ActionFactory
//				.getItemGroupListAction());
//		manageSupportListsMenu.add(ActionFactory
//				.getCreditRatingListAction());
//		manageSupportListsMenu.add(ActionFactory
//				.getCountryRegionListAction());
//		// manageSupportListsMenu.add(ActionFactory
//		// .getFormLayoutsListAction());
//		//manageSupportListsMenu.add(ActionFactory.getPayTypeListAction()
//		// );
//
//		companyListsMenu.add(ActionFactory.getChartOfAccountsAction());
//		companyListsMenu.add(ActionFactory.getJournalEntriesAction());
//		companyListsMenu.add(ActionFactory.getItemsAction());
//		companyListsMenu.add(ActionFactory.getCustomersAction());
//		companyListsMenu.add(ActionFactory.getVendorsAction());
//		companyListsMenu.add(ActionFactory.getPaymentsAction());
//		companyListsMenu
//				.add(ActionFactory.getNewSalesPersonListAction());
//
//		// companyMenu.addSubMenu(businessServicesMenu);
//		companyMenu.add(ActionFactory.getCompanyInformationAction());
//		companyMenu.add(ActionFactory.getPreferencesAction());
//		companyMenu.add(ActionFactory
//				.getIntegrateWithBusinessContactManagerAction());
//		companyMenu.addSeparator();
//		companyMenu.add(ActionFactory.getNewJournalEntryAction());
//		companyMenu.add(ActionFactory
//				.getNewCashBasisJournalEntryAction());
//		companyMenu.add(ActionFactory.getNewAccountAction());
//		// companyMenu.addSubMenu(mergerAccountsMenu);
//		companyMenu.addSeparator();
//
//		companyMenu.addSubMenu(salesTaxMenu);
//		companyMenu.addSubMenu(manageSupportListsMenu);
//		companyMenu.add(ActionFactory.getManageFiscalYearAction());
//		companyMenu.addSeparator();
//		// companyMenu.add(ActionFactory.getWriteLettersAction());
//		// companyMenu.add(ActionFactory.getManageWordTemplatesAction());
//		// companyMenu
//		// .add(ActionFactory.getManageEMailCoverLettersAction());
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
//		newMenu.add(ActionFactory.getNewVendorAction());
//		newMenu.add(ActionFactory.getNewItemAction());
//		newMenu.add(ActionFactory.getNewCashPurchaseAction());
//		newMenu.add(ActionFactory.getNewCreditMemoAction());
//		newMenu.add(ActionFactory.getNewCheckAction());
//
//		// vendorServicesMenu
//		// .add(ActionFactory.getServicesOverviewAction());
//		// vendorServicesMenu.add(ActionFactory
//		// .getBuyChecksAndFormsAction());
//		// vendorServicesMenu.add(ActionFactory.getBankOnlineAction());
//
//		vendorListsMenu.add(ActionFactory.getVendorsAction());
//		vendorListsMenu.add(ActionFactory.getItemsAction());
//		vendorListsMenu.add(ActionFactory.getBillsAction());
//		vendorListsMenu.add(ActionFactory.getVendorPaymentsAction());
//
//		vendorMenu.add(ActionFactory.getVendorsHomeAction());
//		// vendorMenu.addSeparator();
//		// vendorMenu.addSubMenu(newMenu);
//		// vendorMenu.addSeparator();
//		vendorMenu.add(ActionFactory.getEnterBillsAction());
//		vendorMenu.add(ActionFactory.getPayBillsAction());
//		vendorMenu.add(ActionFactory.getIssuePaymentsAction());
//		// vendorMenu.add(ActionFactory.getNewVendorPaymentAction());
//		vendorMenu.add(ActionFactory.getRecordExpensesAction());
//		// vendorMenu.addSeparator();
//		// vendorMenu.add(ActionFactory.getWriteLettersAction());
//		// vendorMenu.add(ActionFactory.getManageWordTemplatesAction());
//		//vendorMenu.add(ActionFactory.getManageEMailCoverLettersAction()
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
//		// ActionFactory.getServicesOverviewAction(),
//		// ActionFactory.getBuyChecksAndFormsAction() };
//		Action[] bankingLists = {
//				ActionFactory.getChartOfAccountsAction(),
//				ActionFactory.getPaymentsAction() };
//
//		bankingMenu.add(ActionFactory.getBankingHomeAction());
//		// bankingMenu.addSeparator();
//		bankingMenu.add(ActionFactory.getNewBankAccountAction());
//		// bankingMenu.addSeparator();
//
//		bankingMenu.add(ActionFactory.getAccountRegisterAction());
//		bankingMenu.add(ActionFactory.getWriteChecksAction());
//		bankingMenu.add(ActionFactory.getMakeDepositAction());
//		bankingMenu.add(ActionFactory.getPayBillsAction());
//		bankingMenu.add(ActionFactory.getEnterPaymentsAction());
//		// // bankingMenu.addSeparator();
//		// // bankingMenu.addSubMenu(bankOnlineMenu);
//		// // bankingMenu.addSeparator();
//		// // bankingMenu.addSubMenu(creditcardProcessingMenu);
//		// bankingMenu.add(ActionFactory.getMatchTrasactionsAction());
//		// bankingMenu.add(ActionFactory.getCreditCardChargeAction());
//		// bankingMenu.add(ActionFactory.getReconcileAccountAction);
//		// bankingMenu.addSeparator();
//		bankingMenu.add(ActionFactory.getPrintChecksAction());
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
//		// ActionFactory.getSalesAnalysisAction(),
//		// ActionFactory.getPurchaseAnalysisAction(),
//		// ActionFactory.getVendorListAction(),
//		// ActionFactory.getPaymentListAction(),
//		// ActionFactory.getItemListAction(),
//		// ActionFactory.getInvoiceListAction(),
//		// ActionFactory.getCustomersAction(),
//		// ActionFactory.getProfitAndLossAction() };
//
//		Action[] companyandFinancial = {
//				ActionFactory.getProfitAndLossAction(),
//				ActionFactory.getBalanceSheetAction(),
//				ActionFactory.getCashFlowStatementAction(),
//				ActionFactory.getTrialBalanceAction(),
//				ActionFactory.getTransactionDetailByAccountAction(),
//				ActionFactory.getGlReportAction(),
//				ActionFactory.getSalesTaxLiabilityAction(),
//				ActionFactory.getTransactionDetailByTaxCodeAction(), };
//		// ActionFactory.getSalesTaxCollectedThisQuarterAction() };
//
//		Action[] companiesReceivable = {
//				ActionFactory.getArAgingDetailAction(),
//				ActionFactory.getCustomerTransactionHistoryAction(),
//				// ActionFactory.getOpenInvoicesbyDueDateAction(),
//				ActionFactory.getMostProfitableCustomersAction() };
//		Action[] sales = {
//				ActionFactory.getSalesByCustomerSummaryAction(),
//				ActionFactory.getSalesByCustomerDetailAction(),
//				ActionFactory.getSalesByItemSummmaryAction(),
//				ActionFactory.getSalesByItemDetailAction(),
//		// ActionFactory.getOnlineSalesByCustomerSummaryAction(),
//		// ActionFactory.getOnlineSalesByItemSummaryAction(),
//		// ActionFactory.getTodaysSalesByCustomerAction(),
//		// ActionFactory.getYtdSalesComparedToLastYearAction()
//		};
//
//		Action[] purchases = {
//				ActionFactory.getPurchaseByVendorSummaryAction(),
//				ActionFactory.getPurchaseByVendorDetailAction(),
//				ActionFactory.getPurchaseByItemSummaryAction(),
//				ActionFactory.getPurchaseByItemAction(), };
//		Action[] vendorpayable = {
//				ActionFactory.getAorpAgingDetailAction(),
//				ActionFactory.getVendorTransactionHistoryAction(),
//				// ActionFactory.getTodaysVendorPaymentsAction(),
//				ActionFactory.getAmountsDueToVendorsAction() };
//		Action[] banking = { ActionFactory.getPaymentsAction(),
//		// ActionFactory.getFailedPaymentsAction(),
//		// ActionFactory.getUndepositedFundsCollectedTodayAction()
//		};
//
//		// Action[] cashBasis = { ActionFactory.getBalanceSheetAction(),
//		// ActionFactory.getProfitAndLossAction(),
//		// ActionFactory.getTransactionDetailByAccountAction(),
//		// ActionFactory.getGlReportAction(),
//		// ActionFactory.getSalesTaxLiabilityAction(),
//		// ActionFactory.getTransactionDetailByTaxCodeAction(),
//		// ActionFactory.getSalesByCustomerSummaryAction(),
//		// ActionFactory.getSalesByCustomerDetailAction(),
//		// ActionFactory.getSalesByItemSummaryAction(),
//		// ActionFactory.getSalesByItemDetailAction(),
//		// ActionFactory.getOnlineSalesByCustomerSummaryAction(),
//		// ActionFactory.getOnlineSalesByItemSummaryAction()
//		//
//		// };
//
//		reportMenu.add(ActionFactory.getReportsHomeAction());
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
//		// employeepayrollMenu.add(ActionFactory.getTimeReportAction());
//		// reportMenu.addSubMenu(employeepayrollMenu);
//
//		// cashBasisMenu.add(cashBasis);
//		// reportMenu.addSubMenu(cashBasisMenu);
//		//
//		// incomeTaxMenu.add(ActionFactory.getTaxSummaryAction());
//		//incomeTaxMenu.add(ActionFactory.getTaxSummaryCashBasisAction())
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
//						// XXX
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
//						// XXX
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
//						// XXX
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
//		Action[] menuActions = { ActionFactory.getNewCustomerAction(),
//				ActionFactory.getNewVendorAction(),
//				ActionFactory.getNewQuoteAction(),
//				ActionFactory.getNewInvoiceAction(),
//				ActionFactory.getNewItemAction(),
//				ActionFactory.getNewCheckAction(),
//				ActionFactory.getPayBillsAction(),
//				ActionFactory.getJournalEntriesAction()
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
//		toolBarManager.add(ActionFactory.getPrintAction());
//		toolBarManager.add(ActionFactory.getDeleteAction());
//		toolBarManager.add(ActionFactory.getRefreshAction());
//		toolBarManager.addSeparator();
//		toolBarManager.add(ActionFactory.getQuickStartAction());
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
