package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.countries.UnitedKingdom;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class ReportSectionView extends BaseHomeView {

	@Override
	public void init() {
		getLeftLayout().add(createControl());
		setSize("100%", "100%");

	}

	private VerticalPanel createControl() {

		VerticalPanel mainLayout = new VerticalPanel();
		BalanceSheetReport report = new BalanceSheetReport();
		// report.setSize("80%", "80%");
		// report.setParentLayout(mainLayout);
		// report.initReportType();
		// report.generateReport(FinanceApplication.getStartDate(), new Date());

		// mainLayout.setSize("100%", "100%");
		// mainLayout.setMembersMargin(10);
		// mainLayout.setTop(5);
		// mainLayout.setLeft(5);

		HorizontalPanel HorizontalPanel1 = new HorizontalPanel();
		HorizontalPanel1.setSize("100%", "70px");
		// HorizontalPanel1.setMembersMargin(5);

		HorizontalPanel HorizontalPanel2 = new HorizontalPanel();
		HorizontalPanel2.setSize("100%", "70px");

		HorizontalPanel HorizontalPanel3 = new HorizontalPanel();
		HorizontalPanel3.setSize("100%", "70px");

		HorizontalPanel HorizontalPanel4 = new HorizontalPanel();
		HorizontalPanel4.setSize("100%", "70px");

		HorizontalPanel HorizontalPanel5 = new HorizontalPanel();
		HorizontalPanel5.setSize("100%", "70px");
		// HorizontalPanel2.setMembersMargin(5);

		// VLayout rightLayout= new VLayout();
		// rightLayout.setMembersMargin(5);

		// Form for Company & Financial type reports

		Label reportLabel = new Label(messages.reports());
		reportLabel.setStyleName("name-label");
		// reportLabel.setHeight(20);
		mainLayout.add(reportLabel);

		Label companyAndFinancialLabel = new Label(
				messages.companyAndFinancial());
		companyAndFinancialLabel.setStyleName("name-label");
		DynamicForm companyAndFinancialForm = UIUtils.form(messages
				.companyAndFinancial());
		// companyAndFinancialForm.setWidth("50%");
		companyAndFinancialForm.setHeight("40%");
		companyAndFinancialForm.setNumCols(1);

		LinkItem profitAndLossLink = new LinkItem();
		profitAndLossLink.setLinkTitle(messages.profitAndLoss());
		profitAndLossLink.setShowTitle(false);
		profitAndLossLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getProfitAndLossAction());
			}

		});

		LinkItem balanceSheetLink = new LinkItem();
		balanceSheetLink.setLinkTitle(messages.balanceSheet());
		balanceSheetLink.setShowTitle(false);
		balanceSheetLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getBalanceSheetAction());
			}

		});

		LinkItem cashFlowLink = new LinkItem();
		cashFlowLink.setLinkTitle(messages.cashFlowStatement());
		cashFlowLink.setShowTitle(false);
		cashFlowLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getCashFlowStatementAction());
			}

		});

		LinkItem trailBalanceLink = new LinkItem();
		trailBalanceLink.setLinkTitle(messages.trialBalance());
		trailBalanceLink.setShowTitle(false);
		trailBalanceLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getTrialBalanceAction());
			}

		});

		LinkItem transactionDetailsByAccountsLink = new LinkItem();
		transactionDetailsByAccountsLink.setLinkTitle(messages
				.transactionDetailsByAccount());
		transactionDetailsByAccountsLink.setShowTitle(false);
		transactionDetailsByAccountsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getTransactionDetailByAccountAction());
			}

		});

		LinkItem realisedExchangeLossesAndGains = new LinkItem();
		realisedExchangeLossesAndGains.setLinkTitle(messages
				.realisedExchangeLossesAndGains());
		realisedExchangeLossesAndGains.setShowTitle(false);
		realisedExchangeLossesAndGains.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getRealisedExchangeLossesAndGainsAction());
			}

		});

		LinkItem unRealisedExchangeLossesAndGains = new LinkItem();
		unRealisedExchangeLossesAndGains.setLinkTitle(messages
				.unRealisedExchangeLossesAndGains());
		unRealisedExchangeLossesAndGains.setShowTitle(false);
		unRealisedExchangeLossesAndGains.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getEnterExchangeRatesAction());
			}

		});

		LinkItem generalLedgerLink = new LinkItem();
		generalLedgerLink.setLinkTitle(messages.generalLedgerReport());
		generalLedgerLink.setShowTitle(false);
		generalLedgerLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getGlReportAction());
			}

		});

		LinkItem expenseReportLink = new LinkItem();
		expenseReportLink.setLinkTitle(messages.expenseReport());
		expenseReportLink.setShowTitle(false);
		expenseReportLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getExpenseReportAction());
			}

		});

		LinkItem automaticTransactionsLink = new LinkItem();
		automaticTransactionsLink
				.setLinkTitle(messages.automaticTransactions());
		automaticTransactionsLink.setShowTitle(false);
		automaticTransactionsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getAutomaticTransactionsAction());
			}

		});

		LinkItem salesTaxLiabilityLink = new LinkItem();
		salesTaxLiabilityLink.setLinkTitle(messages.salesTaxLiability());
		salesTaxLiabilityLink.setShowTitle(false);
		salesTaxLiabilityLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesTaxLiabilityAction());
			}

		});

		LinkItem transactionDetailByTaxItemLink = new LinkItem();
		transactionDetailByTaxItemLink.setLinkTitle(messages
				.transactionDetailByTaxItem());
		transactionDetailByTaxItemLink.setShowTitle(false);
		transactionDetailByTaxItemLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getTransactionDetailByTaxItemAction());
			}

		});

		LinkItem reconciliationsReportLink = new LinkItem();
		reconciliationsReportLink
				.setLinkTitle(messages.reconciliationsReport());
		reconciliationsReportLink.setShowTitle(false);
		reconciliationsReportLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getReconcilationsAction());
			}

		});

		LinkItem profitandLossByLocationLink = new LinkItem();
		profitandLossByLocationLink.setLinkTitle(messages
				.profitAndLossByLocation(Global.get().Location()));
		profitandLossByLocationLink.setShowTitle(false);
		profitandLossByLocationLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getProfitAndLossByLocationAction(true));
			}

		});

		LinkItem profitandLossByClassLink = new LinkItem();
		profitandLossByClassLink.setLinkTitle(messages.profitAndLossbyClass());
		profitandLossByClassLink.setShowTitle(false);
		profitandLossByClassLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getProfitAndLossByLocationAction(true));
			}

		});

		companyAndFinancialForm
				.setFields(profitAndLossLink, balanceSheetLink,
						trailBalanceLink, cashFlowLink,
						transactionDetailsByAccountsLink,
						reconciliationsReportLink, automaticTransactionsLink,
						expenseReportLink, generalLedgerLink);
		if (Global.get().preferences().isEnableMultiCurrency()) {
			companyAndFinancialForm.setFields(realisedExchangeLossesAndGains,
					unRealisedExchangeLossesAndGains);
		}

		if (Global.get().preferences().isTrackTax()) {
			companyAndFinancialForm.setFields(salesTaxLiabilityLink,
					transactionDetailByTaxItemLink);
		}

		if (Global.get().preferences().isLocationTrackingEnabled()) {
			companyAndFinancialForm.setFields(profitandLossByLocationLink);
		}

		if (Global.get().preferences().isClassTrackingEnabled()) {
			companyAndFinancialForm.setFields(profitandLossByClassLink);
		}

		// Form for Sales type reports
		Label salesLabel = new Label(messages.sales());
		salesLabel.setStyleName("name-label");
		DynamicForm salesForm = UIUtils.form(messages.sales());
		// salesForm.setWidth("50%");
		salesForm.setHeight("40%");
		salesForm.setNumCols(1);

		LinkItem salesByCustomerSummaryLink = new LinkItem();
		salesByCustomerSummaryLink.setLinkTitle(messages
				.salesByCustomerSummary(Global.get().customer()));
		salesByCustomerSummaryLink.setShowTitle(false);
		salesByCustomerSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByCustomerSummaryAction());
			}

		});

		LinkItem salesByCustomerDetailLink = new LinkItem();
		salesByCustomerDetailLink.setLinkTitle(messages
				.salesByCustomerDetail(Global.get().Customer()));
		salesByCustomerDetailLink.setShowTitle(false);
		salesByCustomerDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByCustomerDetailAction());
			}

		});

		LinkItem salesByItemSummaryLink = new LinkItem();
		salesByItemSummaryLink.setLinkTitle(messages.salesByItemSummary());
		salesByItemSummaryLink.setShowTitle(false);
		salesByItemSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByItemSummaryAction());
			}

		});

		LinkItem salesByItemDetailLink = new LinkItem();
		salesByItemDetailLink.setLinkTitle(messages.salesByItemDetail());
		salesByItemDetailLink.setShowTitle(false);
		salesByItemDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByItemDetailAction());
			}

		});

		LinkItem salesOrderReportLink = new LinkItem();
		salesOrderReportLink.setLinkTitle(messages.salesOrderReport());
		salesOrderReportLink.setShowTitle(false);
		salesOrderReportLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getSalesOrderAction());
			}

		});

		LinkItem salesByLocationDetailLink = new LinkItem();
		salesByLocationDetailLink.setLinkTitle(messages
				.getSalesByLocationDetails(Global.get().Location()));
		salesByLocationDetailLink.setShowTitle(false);
		salesByLocationDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByLocationDetailsAction(true));
			}

		});

		LinkItem salesByLocationSummaryLink = new LinkItem();
		salesByLocationSummaryLink.setLinkTitle(messages
				.salesByLocationSummary(Global.get().Location()));
		salesByLocationSummaryLink.setShowTitle(false);
		salesByLocationSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByLocationSummaryAction(true));
			}

		});

		LinkItem salesByClassDetailsLink = new LinkItem();
		salesByClassDetailsLink.setLinkTitle(messages.salesByClassDetails());
		salesByClassDetailsLink.setShowTitle(false);
		salesByClassDetailsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByLocationDetailsAction(false));
			}

		});

		LinkItem salesByClassSummaryLink = new LinkItem();
		salesByClassSummaryLink.setLinkTitle(messages.salesByClassSummary());
		salesByClassSummaryLink.setShowTitle(false);
		salesByClassSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByLocationSummaryAction(false));
			}

		});

		salesForm.setFields(salesByCustomerSummaryLink,
				salesByCustomerDetailLink, salesByItemSummaryLink,
				salesByItemDetailLink);

		if (Global.get().preferences().isSalesOrderEnabled()) {
			salesForm.setFields(salesOrderReportLink);
		}
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			salesForm.setFields(salesByLocationDetailLink,
					salesByLocationSummaryLink);
		}

		if (Global.get().preferences().isClassTrackingEnabled()) {
			salesForm.setFields(salesByClassDetailsLink,
					salesByClassSummaryLink);
		}

		// Form for purchase type reports
		Label purchaseLabel = new Label(messages.purchase());
		purchaseLabel.setStyleName("name-label");
		DynamicForm purchaseForm = UIUtils.form(messages.purchase());
		// purchaseForm.setWidth("50%");
		purchaseForm.setHeight("40%");
		purchaseForm.setNumCols(1);

		LinkItem purchaseBySupplierSummaryLink = new LinkItem();
		purchaseBySupplierSummaryLink.setLinkTitle(messages
				.purchaseByVendorSummary(Global.get().vendor()));
		purchaseBySupplierSummaryLink.setShowTitle(false);
		purchaseBySupplierSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getPurchaseByVendorSummaryAction());
			}

		});

		LinkItem purchaseBySupplierDetailLink = new LinkItem();
		purchaseBySupplierDetailLink.setLinkTitle(messages
				.purchaseByVendorDetail(Global.get().vendor()));
		purchaseBySupplierDetailLink.setShowTitle(false);
		purchaseBySupplierDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getPurchaseByVendorDetailAction());
			}

		});

		LinkItem purchaseByItemSummaryLink = new LinkItem();
		purchaseByItemSummaryLink
				.setLinkTitle(messages.purchaseByItemSummary());
		purchaseByItemSummaryLink.setShowTitle(false);
		purchaseByItemSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getPurchaseByItemSummaryAction());
			}

		});

		LinkItem purchaseByItemDetailLink = new LinkItem();
		purchaseByItemDetailLink.setLinkTitle(messages.purchaseByItemDetail());
		purchaseByItemDetailLink.setShowTitle(false);
		purchaseByItemDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getPurchaseByItemAction());
			}

		});

		LinkItem purchaseOrderReportLink = new LinkItem();
		purchaseOrderReportLink.setLinkTitle(messages.purchaseOrderReport());
		purchaseOrderReportLink.setShowTitle(false);
		purchaseOrderReportLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getPurchaseOrderAction());
			}

		});

		purchaseForm.setFields(purchaseBySupplierSummaryLink,
				purchaseBySupplierDetailLink, purchaseByItemSummaryLink,
				purchaseByItemDetailLink);
		if (Global.get().preferences().isPurchaseOrderEnabled()) {
			purchaseForm.setFields(purchaseOrderReportLink);
		}

		// Form for customer receivable type reports
		Label customersLabel = new Label(messages.customersAndReceivable(Global
				.get().Customers()));
		customersLabel.setStyleName("name-label");
		DynamicForm customerForm = UIUtils.form(messages
				.customersAndReceivable(Global.get().Customers()));
		// otherForm.setWidth("50%");
		customerForm.setHeight("40%");
		customerForm.setNumCols(1);

		// Form for vendor receivable type reports
		Label vendorLabel = new Label(messages.vendorsAndPayables(Global.get()
				.Vendors()));
		vendorLabel.setStyleName("name-label");
		DynamicForm vendorForm = UIUtils.form(messages
				.vendorsAndPayables(Global.get().Vendors()));
		// otherForm.setWidth("50%");
		vendorForm.setHeight("40%");
		vendorForm.setNumCols(1);

		LinkItem customerTransactionHistoryLink = new LinkItem();
		customerTransactionHistoryLink.setLinkTitle(messages
				.payeeTransactionHistory(Global.get().Customer()));
		customerTransactionHistoryLink.setShowTitle(false);
		customerTransactionHistoryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getCustomerTransactionHistoryAction());
			}

		});

		LinkItem arAgingDetailLink = new LinkItem();
		arAgingDetailLink.setLinkTitle(messages.arAgeing());
		arAgingDetailLink.setShowTitle(false);
		arAgingDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getArAgingDetailAction());
			}

		});

		LinkItem apAgingDetailLink = new LinkItem();
		apAgingDetailLink.setLinkTitle(messages.apAging());
		apAgingDetailLink.setShowTitle(false);
		apAgingDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getAorpAgingDetailAction());
			}

		});

		LinkItem arAgingSummaryLink = new LinkItem();
		arAgingSummaryLink.setLinkTitle(messages.arAgeingSummary());
		arAgingSummaryLink.setShowTitle(false);
		arAgingSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getArAgingSummaryReportAction());
			}

		});

		LinkItem apAgingSummaryLink = new LinkItem();
		apAgingSummaryLink.setLinkTitle(messages.apAgeingSummary());
		apAgingSummaryLink.setShowTitle(false);
		apAgingSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getAorpAgingSummaryReportAction());
			}

		});

		LinkItem customerStatementLink = new LinkItem();
		customerStatementLink.setLinkTitle(messages.payeeStatement(Global.get()
				.Customers()));
		customerStatementLink.setShowTitle(false);
		customerStatementLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getStatementReport(false, 0));
			}

		});
		LinkItem vendorStatementLink = new LinkItem();
		vendorStatementLink.setLinkTitle(messages.apAgeingSummary());
		vendorStatementLink.setShowTitle(false);
		vendorStatementLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getStatementReport(true, 0));
			}

		});

		LinkItem supplierTransactionHistoryLink = new LinkItem();
		supplierTransactionHistoryLink.setLinkTitle(messages
				.payeeTransactionHistory(Global.get().Vendor()));
		supplierTransactionHistoryLink.setShowTitle(false);
		supplierTransactionHistoryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getVendorTransactionHistoryAction());
			}

		});

		LinkItem mostProfitableCustomerLink = new LinkItem();
		mostProfitableCustomerLink.setLinkTitle(messages
				.mostProfitableCustomer(Global.get().Customer()));
		mostProfitableCustomerLink.setShowTitle(false);
		mostProfitableCustomerLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getMostProfitableCustomersAction());
			}

		});

		customerForm.setFields(arAgingDetailLink, arAgingSummaryLink,
				customerStatementLink, mostProfitableCustomerLink,
				customerTransactionHistoryLink);
		vendorForm.setFields(apAgingDetailLink, apAgingSummaryLink,
				vendorStatementLink, supplierTransactionHistoryLink);

		Label budgetLabel = new Label(messages.budget());
		budgetLabel.setStyleName("name-label");
		DynamicForm budgetform = UIUtils.form(messages.budget());
		budgetform.setHeight("40%");
		budgetform.setNumCols(1);

		LinkItem budgetOverviewLink = new LinkItem();
		budgetOverviewLink.setLinkTitle(messages.budgetOverview());
		budgetOverviewLink.setShowTitle(false);
		budgetOverviewLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getBudgetOverView());
			}

		});

		LinkItem budgetVsActualsLink = new LinkItem();
		budgetVsActualsLink.setLinkTitle(messages.budgetvsActuals());
		budgetVsActualsLink.setShowTitle(false);
		budgetVsActualsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getBudgetVsActionReport());
			}

		});
		budgetform.setFields(budgetOverviewLink, budgetVsActualsLink);

		Label fixedAssestLabel = new Label(messages.fixedAssest());
		fixedAssestLabel.setStyleName("name-label");
		DynamicForm fixedAssetform = UIUtils.form(messages.fixedAssest());
		fixedAssetform.setHeight("40%");
		fixedAssetform.setNumCols(1);

		LinkItem depriciationLink = new LinkItem();
		depriciationLink.setLinkTitle(messages.depreciationReport());
		depriciationLink.setShowTitle(false);
		depriciationLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getDepreciationSheduleAction());
			}

		});
		fixedAssetform.setFields(depriciationLink);
		Label inventoryLabel = new Label(messages.inventory());
		inventoryLabel.setStyleName("name-label");
		DynamicForm inventoryform = UIUtils.form(messages.inventory());
		inventoryform.setHeight("40%");
		inventoryform.setNumCols(1);

		LinkItem invValuationSummaryLink = new LinkItem();
		invValuationSummaryLink.setLinkTitle(messages
				.inventoryValutionSummary());
		invValuationSummaryLink.setShowTitle(false);
		invValuationSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getInventoryValutionSummaryReportAction());
			}

		});

		LinkItem invValuationDetailsLink = new LinkItem();
		invValuationDetailsLink.setLinkTitle(messages
				.inventoryValuationDetails());
		invValuationDetailsLink.setShowTitle(false);
		invValuationDetailsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getInventoryValuationDetailsAction());
			}

		});
		LinkItem invValuationStockStatusByItemLink = new LinkItem();
		invValuationStockStatusByItemLink.setLinkTitle(messages
				.inventoryStockStatusByItem());
		invValuationStockStatusByItemLink.setShowTitle(false);
		invValuationStockStatusByItemLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getInventoryStockStatusByItemAction());
			}

		});
		LinkItem invValuationStockStatusByVendorLink = new LinkItem();
		invValuationStockStatusByVendorLink.setLinkTitle(messages
				.inventoryStockStatusByVendor());
		invValuationStockStatusByVendorLink.setShowTitle(false);
		invValuationStockStatusByVendorLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getInventoryStockStatusByVendorAction());
			}

		});

		inventoryform.setFields(invValuationSummaryLink,
				invValuationDetailsLink, invValuationStockStatusByItemLink,
				invValuationStockStatusByVendorLink);

		// TAX tab for uk country
		Label taxLabel = new Label(messages.tax());
		taxLabel.setStyleName("name-label");
		DynamicForm ukTaxForm = UIUtils.form(messages.inventory());
		ukTaxForm.setHeight("40%");
		ukTaxForm.setNumCols(1);

		LinkItem priorVatReturnsLink = new LinkItem();
		priorVatReturnsLink.setLinkTitle(messages.priorVATReturns());
		priorVatReturnsLink.setShowTitle(false);
		priorVatReturnsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getVATSummaryReportAction());
			}

		});

		LinkItem vatDetailLink = new LinkItem();
		vatDetailLink.setLinkTitle(messages.vatDetail());
		vatDetailLink.setShowTitle(false);
		vatDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getVATDetailsReportAction());
			}

		});
		LinkItem vat100Link = new LinkItem();
		vat100Link.setLinkTitle(messages.vat100());
		vat100Link.setShowTitle(false);
		vat100Link.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getVAT100ReportAction());
			}

		});
		LinkItem unCategorisedVatAmountsLink = new LinkItem();
		unCategorisedVatAmountsLink.setLinkTitle(messages
				.uncategorisedVATAmounts());
		unCategorisedVatAmountsLink.setShowTitle(false);
		unCategorisedVatAmountsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getVATUncategorisedAmountsReportAction());
			}

		});

		LinkItem ecSalesListLink = new LinkItem();
		ecSalesListLink.setLinkTitle(messages.ecSalesList());
		ecSalesListLink.setShowTitle(false);
		ecSalesListLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getECSalesListAction());
			}

		});

		LinkItem taxItemSummaryLink = new LinkItem();
		taxItemSummaryLink.setLinkTitle(messages.vatItemSummary());
		taxItemSummaryLink.setShowTitle(false);
		taxItemSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getVATItemSummaryReportAction());
			}

		});
		ukTaxForm.setFields(priorVatReturnsLink, vatDetailLink, vat100Link,
				unCategorisedVatAmountsLink, ecSalesListLink,
				taxItemSummaryLink);

		// TAX tab for all other countries
		DynamicForm otherCountriesTaxForm = UIUtils.form(messages.tax());
		otherCountriesTaxForm.setHeight("40%");
		otherCountriesTaxForm.setNumCols(1);

		LinkItem taxItemDetailLink = new LinkItem();
		taxItemDetailLink.setLinkTitle(messages.taxItemDetailReport());
		taxItemDetailLink.setShowTitle(false);
		taxItemDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getTaxItemDetailReportAction());
			}

		});

		LinkItem taxItemExceptionLink = new LinkItem();
		taxItemExceptionLink.setLinkTitle(messages
				.taxItemExceptionDetailReport());
		taxItemExceptionLink.setShowTitle(false);
		taxItemExceptionLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getTaxItemExceptionDetailReportAction());
			}

		});

		otherCountriesTaxForm.setFields(taxItemDetailLink,
				taxItemExceptionLink, taxItemSummaryLink);

		VerticalPanel companyAndFinancialPanel = new VerticalPanel();
		companyAndFinancialPanel.add(companyAndFinancialLabel);
		companyAndFinancialPanel.add(companyAndFinancialForm);

		VerticalPanel salesPanel = new VerticalPanel();
		salesPanel.add(salesLabel);
		salesPanel.add(salesForm);

		VerticalPanel customerPanel = new VerticalPanel();
		customerPanel.add(customersLabel);
		customerPanel.add(customerForm);

		VerticalPanel vendorPanel = new VerticalPanel();
		vendorPanel.add(vendorLabel);
		vendorPanel.add(vendorForm);

		VerticalPanel purchasePanel = new VerticalPanel();
		purchasePanel.add(purchaseLabel);
		purchasePanel.add(purchaseForm);

		VerticalPanel inventoryPanel = new VerticalPanel();
		inventoryPanel.add(inventoryLabel);
		inventoryPanel.add(inventoryform);

		VerticalPanel budgetPanel = new VerticalPanel();
		budgetPanel.add(budgetLabel);
		budgetPanel.add(budgetform);

		VerticalPanel fixedAssestPanel = new VerticalPanel();
		fixedAssestPanel.add(fixedAssestLabel);
		fixedAssestPanel.add(fixedAssetform);

		HorizontalPanel1.add(companyAndFinancialPanel);
		HorizontalPanel1.add(salesPanel);
		HorizontalPanel1.setCellWidth(companyAndFinancialPanel, "50%");
		HorizontalPanel1.setCellWidth(salesPanel, "50%");

		HorizontalPanel2.add(customerPanel);
		HorizontalPanel2.add(vendorPanel);
		HorizontalPanel2.setCellWidth(customerPanel, "50%");
		HorizontalPanel2.setCellWidth(vendorPanel, "50%");

		HorizontalPanel3.add(inventoryPanel);
		HorizontalPanel3.add(purchasePanel);
		HorizontalPanel3.setCellWidth(inventoryPanel, "50%");
		HorizontalPanel3.setCellWidth(purchasePanel, "50%");

		HorizontalPanel4.add(budgetPanel);
		HorizontalPanel4.add(fixedAssestPanel);
		HorizontalPanel4.setCellWidth(budgetPanel, "50%");
		HorizontalPanel4.setCellWidth(fixedAssestPanel, "50%");

		VerticalPanel taxPanel = new VerticalPanel();
		taxPanel.add(taxLabel);

		ICountryPreferences company = Accounter.getCompany()
				.getCountryPreferences();
		if (Global.get().preferences().isTrackTax()) {
			if (company instanceof UnitedKingdom) {
				taxPanel.add(ukTaxForm);
			} else {
				taxPanel.add(otherCountriesTaxForm);
			}
			HorizontalPanel5.add(taxPanel);
			HorizontalPanel5.setCellWidth(taxPanel, "50%");
		}

		mainLayout.add(HorizontalPanel1);
		mainLayout.add(HorizontalPanel2);
		mainLayout.add(HorizontalPanel3);
		mainLayout.add(HorizontalPanel4);
		mainLayout.add(HorizontalPanel5);

		HorizontalPanel2.setStyleName("reports_top_align");
		HorizontalPanel3.setStyleName("reports_top_align");
		HorizontalPanel4.setStyleName("reports_top_align");
		HorizontalPanel5.setStyleName("reports_top_align");
		mainLayout.setStyleName("reports_home_align");

		return mainLayout;

	}

}
