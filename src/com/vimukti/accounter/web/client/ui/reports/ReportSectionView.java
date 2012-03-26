package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.countries.India;
import com.vimukti.accounter.web.client.countries.UnitedKingdom;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class ReportSectionView extends BaseHomeView {

	private Map<String, String> companyAndFinancialMap,
			customersAndRecievableMap, inventoryMap, budgetMap, taxMap,
			vendorAndPayableMap, salesMap, purchaseMap, fixedAssetMap,
			ukTaxMap, bankingMap, jobMap;
	private FlowPanel companyAndFinancialPanel, customersAndRecievablePanel,
			inventoryPanel, budgetPanel, taxPanel, vendorAndPayablePanel,
			salesPanel, purchasePanel, fixedAssetPanel, mainTaxPanel,
			ukTaxPanel, bankingPanel, jobPanel;

	private FlowPanel rightPanel, leftPanel;
	private StyledPanel mainPanel;

	@Override
	public void init() {
		getLeftLayout().add(createControl());
		// setSize("100%", "100%");
	}

	private StyledPanel createControl() {

		Label reportLabel = new Label(messages.reports());
		reportLabel.setStyleName("name-label");

		companyAndFinancialMap = new HashMap<String, String>();
		customersAndRecievableMap = new HashMap<String, String>();
		inventoryMap = new HashMap<String, String>();
		bankingMap = new HashMap<String, String>();
		jobMap = new HashMap<String, String>();
		budgetMap = new HashMap<String, String>();
		taxMap = new HashMap<String, String>();
		vendorAndPayableMap = new HashMap<String, String>();
		salesMap = new HashMap<String, String>();
		purchaseMap = new HashMap<String, String>();
		fixedAssetMap = new HashMap<String, String>();
		ukTaxMap = new HashMap<String, String>();

		Label companyAndFinancialHeader = new Label(
				messages.companyAndFinancial());
		Label customersAndRecievableHeader = new Label(
				messages.customersAndReceivable(Global.get().Customers()));
		Label inventoryHeader = new Label(messages.inventory());
		Label bankingHeader = new Label(messages.banking());
		Label jobHeader = new Label(messages.job());
		Label budgetHeader = new Label(messages.budget());
		Label vendorAndPayableHeader = new Label(
				messages.vendorsAndPayables(Global.get().Vendors()));
		Label salesHeader = new Label(messages.sales());
		Label purchaseHeader = new Label(messages.purchase());
		Label fixedAssetHeader = new Label(messages.fixedAsset());
		Label mainTaxHeader = new Label(messages.tax());

		companyAndFinancialPanel = new FlowPanel();
		companyAndFinancialPanel.addStyleName("section");
		customersAndRecievablePanel = new FlowPanel();
		customersAndRecievablePanel.addStyleName("section");
		inventoryPanel = new FlowPanel();
		inventoryPanel.addStyleName("section");
		bankingPanel = new FlowPanel();
		bankingPanel.addStyleName("section");
		jobPanel = new FlowPanel();
		jobPanel.addStyleName("section");
		budgetPanel = new FlowPanel();
		budgetPanel.addStyleName("section");
		taxPanel = new FlowPanel();
		taxPanel.addStyleName("section");
		vendorAndPayablePanel = new FlowPanel();
		vendorAndPayablePanel.addStyleName("section");
		salesPanel = new FlowPanel();
		salesPanel.addStyleName("section");
		purchasePanel = new FlowPanel();
		purchasePanel.addStyleName("section");
		fixedAssetPanel = new FlowPanel();
		fixedAssetPanel.addStyleName("section");
		mainTaxPanel = new FlowPanel();
		mainTaxPanel.addStyleName("section");
		ukTaxPanel = new FlowPanel();
		ukTaxPanel.addStyleName("section");

		leftPanel = new StyledPanel("leftPanel");
		rightPanel = new StyledPanel("rightPanel");

		mainPanel = new StyledPanel("mainPanel");

		companyAndFinancialMap.put(messages.profitAndLoss(), ActionFactory
				.getProfitAndLossAction().getHistoryToken());
		companyAndFinancialMap.put(messages.balanceSheet(), ActionFactory
				.getBalanceSheetAction().getHistoryToken());
		companyAndFinancialMap.put(messages.cashFlowStatement(), ActionFactory
				.getCashFlowStatementAction().getHistoryToken());
		companyAndFinancialMap.put(messages.transactionDetailsByAccount(),
				ActionFactory.getTransactionDetailByAccountAction()
						.getHistoryToken());
		companyAndFinancialMap.put(messages.trialBalance(), ActionFactory
				.getTrialBalanceAction().getHistoryToken());
		if (Global.get().preferences().isEnableMultiCurrency()) {
			companyAndFinancialMap.put(messages
					.realisedExchangeLossesAndGains(), ActionFactory
					.getRealisedExchangeLossesAndGainsAction()
					.getHistoryToken());
			companyAndFinancialMap.put(messages
					.unRealisedExchangeLossesAndGains(), ActionFactory
					.getEnterExchangeRatesAction().getHistoryToken());
		}
		companyAndFinancialMap.put(messages.generalLedgerReport(),
				ActionFactory.getGlReportAction().getHistoryToken());
		companyAndFinancialMap.put(messages.expenseReport(), ActionFactory
				.getExpenseReportAction().getHistoryToken());
		companyAndFinancialMap.put(messages.automaticTransactions(),
				ActionFactory.getAutomaticTransactionsAction()
						.getHistoryToken());
		if (Global.get().preferences().isTrackTax()) {
			companyAndFinancialMap.put(messages.salesTaxLiability(),
					ActionFactory.getSalesTaxLiabilityAction()
							.getHistoryToken());
			companyAndFinancialMap.put(messages.transactionDetailByTaxItem(),
					ActionFactory.getTransactionDetailByTaxItemAction()
							.getHistoryToken());
		}
		companyAndFinancialMap.put(messages.reconciliationsReport(),
				ActionFactory.getReconcilationsAction().getHistoryToken());
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			companyAndFinancialMap.put(messages.profitAndLossByLocation(Global
					.get().Location()), ActionFactory
					.getProfitAndLossByLocationAction(2).getHistoryToken());
		}
		if (Global.get().preferences().isClassTrackingEnabled()) {
			companyAndFinancialMap.put(messages.profitAndLossbyClass(),
					ActionFactory.getProfitAndLossByLocationAction(1)
							.getHistoryToken());
		}

		salesMap.put(messages.salesByCustomerSummary(Global.get().customer()),
				ActionFactory.getSalesByCustomerSummaryAction()
						.getHistoryToken());
		salesMap.put(messages.salesByCustomerDetail(Global.get().customer()),
				ActionFactory.getSalesByCustomerDetailAction()
						.getHistoryToken());
		salesMap.put(messages.salesByCustomerSummary(Global.get().customer()),
				ActionFactory.getSalesByCustomerSummaryAction()
						.getHistoryToken());
		salesMap.put(messages.salesByItemSummary(), ActionFactory
				.getSalesByItemSummaryAction().getHistoryToken());
		salesMap.put(messages.salesByItemDetail(), ActionFactory
				.getSalesByItemDetailAction().getHistoryToken());
		if (Global.get().preferences().isSalesOrderEnabled()) {
			salesMap.put(messages.salesOrderReport(),
					HistoryTokens.SALESORDERREPORT);
		}
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			salesMap.put(messages.getSalesByLocationDetails(Global.get()
					.Location()), ActionFactory
					.getSalesByLocationDetailsAction(true, true)
					.getHistoryToken());
			salesMap.put(messages.salesByLocationSummary(Global.get()
					.Location()), ActionFactory
					.getSalesByLocationSummaryAction(true, true)
					.getHistoryToken());
		}
		if (Global.get().preferences().isClassTrackingEnabled()) {
			salesMap.put(messages.salesByClassDetails(), ActionFactory
					.getSalesByLocationDetailsAction(false, true)
					.getHistoryToken());
			salesMap.put(messages.salesByClassSummary(), ActionFactory
					.getSalesByLocationSummaryAction(false, true)
					.getHistoryToken());

		}

		// Form for purchase type reports
		purchaseMap.put(
				messages.purchaseByVendorSummary(Global.get().vendor()),
				ActionFactory.getPurchaseByVendorSummaryAction()
						.getHistoryToken());
		purchaseMap.put(messages.purchaseByVendorDetail(Global.get().vendor()),
				ActionFactory.getPurchaseByVendorDetailAction()
						.getHistoryToken());
		purchaseMap.put(messages.purchaseByItemSummary(), ActionFactory
				.getPurchaseByItemSummaryAction().getHistoryToken());
		purchaseMap.put(messages.purchaseByItemDetail(), ActionFactory
				.getPurchaseByItemAction().getHistoryToken());
		if (Global.get().preferences().isPurchaseOrderEnabled()) {
			purchaseMap.put(messages.purchaseOrderReport(),
					HistoryTokens.PURCHASEORDERREPORT);
		}

		// Form for customer receivable type reports

		customersAndRecievableMap.put(messages.arAgeing(), ActionFactory
				.getArAgingDetailAction().getHistoryToken());
		customersAndRecievableMap.put(messages.arAgeingSummary(), ActionFactory
				.getArAgingSummaryReportAction().getHistoryToken());
		customersAndRecievableMap.put(
				messages.payeeStatement(Global.get().Customers()),
				ActionFactory.getStatementReport(false, 0).getHistoryToken());
		customersAndRecievableMap.put(messages.payeeTransactionHistory(Global
				.get().Customer()), ActionFactory
				.getCustomerTransactionHistoryAction().getHistoryToken());

		vendorAndPayableMap.put(messages.apAging(), ActionFactory
				.getAorpAgingDetailAction().getHistoryToken());
		vendorAndPayableMap.put(messages.apAgeingSummary(), ActionFactory
				.getAorpAgingSummaryReportAction().getHistoryToken());
		vendorAndPayableMap.put(messages.payeeStatement(Global.get().Vendor()),
				ActionFactory.getStatementReport(true, 0).getHistoryToken());
		vendorAndPayableMap.put(messages.payeeTransactionHistory(Global.get()
				.Vendor()), ActionFactory.getVendorTransactionHistoryAction()
				.getHistoryToken());

		budgetMap.put(messages.budgetOverview(), ActionFactory
				.getBudgetOverView().getHistoryToken());
		budgetMap.put(messages.budgetvsActuals(), ActionFactory
				.getBudgetVsActionReport().getHistoryToken());

		fixedAssetMap.put(messages.depreciationReport(), ActionFactory
				.getDepreciationSheduleAction().getHistoryToken());

		inventoryMap.put(messages.inventoryValutionSummary(), ActionFactory
				.getInventoryValutionSummaryReportAction().getHistoryToken());
		inventoryMap.put(messages.inventoryValuationDetails(), ActionFactory
				.getInventoryValuationDetailsAction().getHistoryToken());
		// inventoryMap.put(messages.inventoryStockStatusByItem(), ActionFactory
		// .getInventoryStockStatusByItemAction().getHistoryToken());
		// inventoryMap.put(messages.inventoryStockStatusByVendor(),
		// ActionFactory
		// .getInventoryStockStatusByVendorAction().getHistoryToken());

		// for banking
		// resolved issue 3544
		// bankingMap.put(messages.depositDetail(), ActionFactory
		// .getBankDepositDetailReportAction().getHistoryToken());
		bankingMap.put(messages.checkDetail(), ActionFactory
				.getBankCheckDetailReportAction().getHistoryToken());
		bankingMap.put(messages.missingchecks(), ActionFactory
				.getMissingChecksReportAction().getHistoryToken());
		bankingMap.put(messages.reconcilationDiscrepancyReport(), ActionFactory
				.getReconciliationDiscrepancyReportAction().getHistoryToken());
		bankingMap.put(messages.depositDetail(), ActionFactory
				.getBankDepositDetailReportAction().getHistoryToken());

		jobMap.put(messages.estimatesbyJob(), ActionFactory
				.getEstimatesByJobAction().getHistoryToken());
		jobMap.put(messages.unbilledCostsByJob(), ActionFactory
				.getUnbilledCostsByJobAction().getHistoryToken());
		jobMap.put(messages.jobProfitabilityDetail(), ActionFactory
				.getJobProfitabilityDetailReportAction().getHistoryToken());
		jobMap.put(messages.jobProfitabilitySummary(), ActionFactory
				.getJobProfitabilitySummaryReportAction().getHistoryToken());
		jobMap.put(messages.profitAndLossByJob(), ActionFactory
				.getProfitAndLossByLocationAction(3).getHistoryToken());
		// TAX tab for uk country
		ukTaxMap.put(messages.priorVATReturns(), ActionFactory
				.getVATSummaryReportAction().getHistoryToken());
		ukTaxMap.put(messages.vatDetail(), ActionFactory
				.getVATDetailsReportAction().getHistoryToken());
		ukTaxMap.put(messages.vat100(), ActionFactory.getVAT100ReportAction()
				.getHistoryToken());
		ukTaxMap.put(messages.ecSalesList(), ActionFactory
				.getECSalesListAction().getHistoryToken());
		ukTaxMap.put(messages.vatItemSummary(), ActionFactory
				.getVATItemSummaryReportAction().getHistoryToken());

		// TAX tab for all other countries
		taxMap.put(messages.taxItemDetailReport(), ActionFactory
				.getTaxItemDetailReportAction().getHistoryToken());
		taxMap.put(messages.taxItemExceptionDetailReport(), ActionFactory
				.getTaxItemExceptionDetailReportAction().getHistoryToken());
		taxMap.put(messages.vatItemSummary(), ActionFactory
				.getVATItemSummaryReportAction().getHistoryToken());
		if (Accounter.getCompany().getCountryPreferences() instanceof India) {
			if (Accounter.getCompany().getPreferences().isTDSEnabled()) {
				taxMap.put(messages.tdsAcknowledgmentsReport(), ActionFactory
						.getTDSAcknowledgmentsReportAction().getHistoryToken());
			}
		}

		addLinksToPanel(companyAndFinancialMap, companyAndFinancialPanel);
		addLinksToPanel(customersAndRecievableMap, customersAndRecievablePanel);
		if (Global.get().preferences().isInventoryEnabled()) {
			addLinksToPanel(inventoryMap, inventoryPanel);
		}
		addLinksToPanel(bankingMap, bankingPanel);
		// For Job Tracking Reports
		if (Global.get().preferences().isJobTrackingEnabled()) {
			addLinksToPanel(jobMap, jobPanel);
		}
		addLinksToPanel(budgetMap, budgetPanel);
		addLinksToPanel(ukTaxMap, ukTaxPanel);
		addLinksToPanel(taxMap, taxPanel);
		addLinksToPanel(salesMap, salesPanel);
		addLinksToPanel(vendorAndPayableMap, vendorAndPayablePanel);
		addLinksToPanel(purchaseMap, purchasePanel);
		addLinksToPanel(fixedAssetMap, fixedAssetPanel);

		ICountryPreferences company = Accounter.getCompany()
				.getCountryPreferences();
		if (Global.get().preferences().isTrackTax()) {
			if (company instanceof UnitedKingdom) {
				mainTaxPanel.add(ukTaxPanel);
			} else {
				mainTaxPanel.add(taxPanel);
			}
		}

		leftPanel.add(companyAndFinancialHeader);
		leftPanel.add(companyAndFinancialPanel);
		leftPanel.add(customersAndRecievableHeader);
		leftPanel.add(customersAndRecievablePanel);

		if (Global.get().preferences().isInventoryEnabled()
				&& hasPermission(Features.EXTRA_REPORTS)) {
			leftPanel.add(inventoryHeader);
			leftPanel.add(inventoryPanel);
		}

		if (hasPermission(Features.EXTRA_REPORTS)) {
			leftPanel.add(bankingHeader);
			leftPanel.add(bankingPanel);
		}

		if (Global.get().preferences().isJobTrackingEnabled()) {
			leftPanel.add(jobHeader);
			leftPanel.add(jobPanel);
		}

		if (hasPermission(Features.EXTRA_REPORTS)
				&& hasPermission(Features.BUDGET)) {
			rightPanel.add(budgetHeader);
			rightPanel.add(budgetPanel);
		}

		if (Global.get().preferences().isTrackTax()) {
			rightPanel.add(mainTaxHeader);
			rightPanel.add(mainTaxPanel);
		}

		if (hasPermission(Features.EXTRA_REPORTS)) {
			rightPanel.add(salesHeader);
			rightPanel.add(salesPanel);
		}
		rightPanel.add(vendorAndPayableHeader);
		rightPanel.add(vendorAndPayablePanel);

		if (hasPermission(Features.EXTRA_REPORTS)) {
			rightPanel.add(purchaseHeader);
			rightPanel.add(purchasePanel);
		}

		if (hasPermission(Features.EXTRA_REPORTS)
				&& hasPermission(Features.FIXED_ASSET)) {
			rightPanel.add(fixedAssetHeader);
			rightPanel.add(fixedAssetPanel);
		}

		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.addStyleName("report-section");
		leftPanel.addStyleName("report-left-panel");
		rightPanel.addStyleName("report-right-panel");
		return mainPanel;
	}

	private boolean hasPermission(String extraReports) {
		// TODO Auto-generated method stub
		return false;
	}

	private void addLinksToPanel(final Map<String, String> map, FlowPanel panel) {
		Set<String> keySet = map.keySet();
		for (final String linkName : keySet) {
			Anchor link = new Anchor(linkName);
			panel.add(link);
			link.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					historyChanged(map.get(linkName));
				}
			});
		}
	}

	protected void historyChanged(String value) {
		Action<?> action = MainFinanceWindow.actions.get(value);
		action.run();
	}
}
