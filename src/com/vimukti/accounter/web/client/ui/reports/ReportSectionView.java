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
import com.vimukti.accounter.web.client.core.CountryPreferences;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.vat.TDSAcknowledgmentsReportAction;
import com.vimukti.accounter.web.client.util.Countries;

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

	boolean hasExtraReportsPerm = hasPermission(Features.EXTRA_REPORTS);

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

		companyAndFinancialMap.put(messages.profitAndLoss(),
				new ProfitAndLossAction().getHistoryToken());
		companyAndFinancialMap.put(messages.balanceSheet(),
				new BalanceSheetAction().getHistoryToken());
		companyAndFinancialMap.put(messages.cashFlowStatement(),
				new CashFlowStatementAction().getHistoryToken());
		companyAndFinancialMap.put(messages.transactionDetailsByAccount(),
				new TransactionDetailByAccountAction().getHistoryToken());
		companyAndFinancialMap.put(messages.trialBalance(),
				new TrialBalanceAction().getHistoryToken());
		if (Global.get().preferences().isEnableMultiCurrency()) {
			companyAndFinancialMap.put(messages
					.realisedExchangeLossesAndGains(),
					new RealisedExchangeLossesAndGainsAction()
							.getHistoryToken());
			companyAndFinancialMap.put(
					messages.unRealisedExchangeLossesAndGains(),
					new EnterExchangeRatesAction().getHistoryToken());
		}
		companyAndFinancialMap.put(messages.generalLedgerReport(),
				new GLReportAction().getHistoryToken());
		companyAndFinancialMap.put(messages.expenseReport(),
				new ExpenseReportAction().getHistoryToken());
		if (hasExtraReportsPerm
				&& hasPermission(Features.RECURRING_TRANSACTIONS)) {
			companyAndFinancialMap.put(messages.automaticTransactions(),
					new AutomaticTransactionsAction().getHistoryToken());
		}
		if (Global.get().preferences().isTrackTax()) {
			companyAndFinancialMap.put(messages.salesTaxLiability(),
					new SalesTaxLiabilityAction().getHistoryToken());
			companyAndFinancialMap.put(messages.transactionDetailByTaxItem(),
					new TransactionDetailByTaxItemAction().getHistoryToken());
		}
		companyAndFinancialMap.put(messages.reconciliationsReport(),
				new ReconcilationsAction().getHistoryToken());
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			companyAndFinancialMap.put(
					messages.profitAndLossByLocation(Global.get().Location()),
					new ProfitAndLossByLocationAction(2).getHistoryToken());
		}
		if (Global.get().preferences().isClassTrackingEnabled()) {
			companyAndFinancialMap.put(messages.profitAndLossbyClass(),
					new ProfitAndLossByLocationAction(1).getHistoryToken());
		}

		salesMap.put(messages.salesByCustomerSummary(Global.get().customer()),
				new SalesByCustomerSummaryAction().getHistoryToken());
		salesMap.put(messages.salesByCustomerDetail(Global.get().customer()),
				new SalesByCustomerDetailAction().getHistoryToken());
		salesMap.put(messages.salesByCustomerSummary(Global.get().customer()),
				new SalesByCustomerSummaryAction().getHistoryToken());
		salesMap.put(messages.salesByItemSummary(),
				new SalesByItemSummaryAction().getHistoryToken());
		salesMap.put(messages.salesByItemDetail(),
				new SalesByItemDetailAction().getHistoryToken());
		if (Global.get().preferences().isSalesOrderEnabled()) {
			salesMap.put(messages.salesOrderReport(),
					HistoryTokens.SALESORDERREPORT);
		}
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			salesMap.put(messages.getSalesByLocationDetails(Global.get()
					.Location()), new SalesByLocationDetailsAction(true, true)
					.getHistoryToken());
			salesMap.put(messages.salesByLocationSummary(Global.get()
					.Location()), new SalesByLocationSummaryAction(true, true)
					.getHistoryToken());
		}
		if (Global.get().preferences().isClassTrackingEnabled()) {
			salesMap.put(messages.salesByClassDetails(),
					new SalesByLocationDetailsAction(false, true)
							.getHistoryToken());
			salesMap.put(messages.salesByClassSummary(),
					new SalesByLocationSummaryAction(false, true)
							.getHistoryToken());

		}

		// Form for purchase type reports
		if (Global.get().preferences().isLocationTrackingEnabled()) {
			purchaseMap
					.put(messages.purchasesbyLocationDetail(Global.get()
							.Location()), new PurchaseByVendorDetailsAction()
							.getHistoryToken());
			purchaseMap.put(messages.purchasesbyLocationSummary(Global.get()
					.Location()), new PurchaseByVendorSummaryAction()
					.getHistoryToken());
		}
		purchaseMap.put(
				messages.purchaseByVendorSummary(Global.get().vendor()),
				new PurchaseByVendorSummaryAction().getHistoryToken());
		purchaseMap.put(messages.purchaseByVendorDetail(Global.get().vendor()),
				new PurchaseByVendorDetailsAction().getHistoryToken());
		purchaseMap.put(messages.purchaseByItemSummary(),
				new PurchaseByItemSummaryAction().getHistoryToken());
		purchaseMap.put(messages.purchaseByItemDetail(),
				new PurchaseByItemDetailsAction().getHistoryToken());
		if (Global.get().preferences().isPurchaseOrderEnabled()) {
			purchaseMap.put(messages.purchaseOrderReport(),
					HistoryTokens.PURCHASEORDERREPORT);
		}

		if (Global.get().preferences().isClassTrackingEnabled()) {
			purchaseMap.put(messages.purchasesbyClassDetail(),
					new SalesByLocationDetailsAction(false, false)
							.getHistoryToken());
			purchaseMap.put(messages.purchasesbyClassSummary(),
					new SalesByLocationSummaryAction(false, false)
							.getHistoryToken());

		}
		// Form for customer receivable type reports

		customersAndRecievableMap.put(messages.arAgeing(),
				new ARAgingDetailAction().getHistoryToken());
		customersAndRecievableMap.put(messages.arAgeingSummary(),
				new ARAgingSummaryReportAction().getHistoryToken());
		if (hasExtraReportsPerm) {
			customersAndRecievableMap.put(
					messages.payeeStatement(Global.get().Customers()),
					new StatementReportAction(0, false).getHistoryToken());
		}
		customersAndRecievableMap.put(
				messages.payeeTransactionHistory(Global.get().Customer()),
				new CustomerTransactionHistoryAction().getHistoryToken());

		vendorAndPayableMap.put(messages.apAging(),
				new APAgingDetailAction().getHistoryToken());
		vendorAndPayableMap.put(messages.apAgeingSummary(),
				new APAgingSummaryReportAction().getHistoryToken());
		if (hasExtraReportsPerm) {
			vendorAndPayableMap.put(
					messages.payeeStatement(Global.get().Vendor()),
					new StatementReportAction(0, true).getHistoryToken());
		}
		vendorAndPayableMap.put(
				messages.payeeTransactionHistory(Global.get().Vendor()),
				new VendorTransactionHistoryAction().getHistoryToken());

		budgetMap.put(messages.budgetOverview(),
				new BudgetOverviewReportAction().getHistoryToken());
		budgetMap.put(messages.budgetvsActuals(),
				new BudgetvsActualsAction().getHistoryToken());

		fixedAssetMap.put(messages.depreciationReport(),
				new DepreciationSheduleAction().getHistoryToken());

		inventoryMap.put(messages.inventoryValutionSummary(),
				new InventoryValutionSummaryReportAction().getHistoryToken());
		inventoryMap.put(messages.inventoryValuationDetails(),
				new InventoryValuationDetailsAction().getHistoryToken());
		inventoryMap.put(messages.inventoryStockStatusByItem(),
				HistoryTokens.INVENTORY_STOCK_STATUS_BY_ITEM_REPORT);
		inventoryMap.put(messages.inventoryStockStatusByVendor(),
				HistoryTokens.INVENTORY_STOCK_STATUS_BY_VENDOR_REPORT);
		// inventoryMap.put(messages.inventoryStockStatusByItem(), ActionFactory
		// .getInventoryStockStatusByItemAction().getHistoryToken());
		// inventoryMap.put(messages.inventoryStockStatusByVendor(),
		// ActionFactory
		// .getInventoryStockStatusByVendorAction().getHistoryToken());

		// for banking
		// resolved issue 3544
		// bankingMap.put(messages.depositDetail(), ActionFactory
		// .getBankDepositDetailReportAction().getHistoryToken());
		bankingMap.put(messages.checkDetail(),
				new BankCheckDetailReportAction().getHistoryToken());
		bankingMap.put(messages.missingchecks(),
				new MissingChecksReportAction().getHistoryToken());
		bankingMap.put(messages.reconcilationDiscrepancyReport(),
				new ReconciliationDiscrepancyReportAction().getHistoryToken());
		bankingMap.put(messages.depositDetail(),
				new BankDepositDetailReportAction().getHistoryToken());

		jobMap.put(messages.estimatesbyJob(),
				new EstimatesByJobAction().getHistoryToken());
		jobMap.put(messages.unbilledCostsByJob(),
				new UnbilledCostsByJobAction().getHistoryToken());
		jobMap.put(messages.jobProfitabilityDetail(),
				new JobProfitabilityDetailReportAction().getHistoryToken());
		jobMap.put(messages.jobProfitabilitySummary(),
				new JobProfitabilitySummaryReportAction().getHistoryToken());
		jobMap.put(messages.profitAndLossByJob(),
				new ProfitAndLossByLocationAction(3).getHistoryToken());
		// TAX tab for uk country
		ukTaxMap.put(messages.priorVATReturns(),
				new VATSummaryReportAction().getHistoryToken());
		ukTaxMap.put(messages.vatDetail(),
				new VATDetailsReportAction().getHistoryToken());
		ukTaxMap.put(messages.vat100(),
				new VAT100ReportAction().getHistoryToken());
		ukTaxMap.put(messages.ecSalesList(),
				new ECSalesListAction().getHistoryToken());
		ukTaxMap.put(messages.vatItemSummary(),
				new VATItemSummaryReportAction().getHistoryToken());

		// TAX tab for all other countries
		taxMap.put(messages.taxItemDetailReport(),
				new TaxItemDetailReportAction().getHistoryToken());
		taxMap.put(messages.taxItemExceptionDetailReport(),
				new TAXItemExceptionDetailReport().getHistoryToken());
		taxMap.put(messages.vatItemSummary(),
				new VATItemSummaryReportAction().getHistoryToken());
		if (Accounter.getCompany().getCountry().equals(Countries.INDIA)) {
			if (Accounter.getCompany().getPreferences().isTDSEnabled()) {
				taxMap.put(messages.tdsAcknowledgmentsReport(),
						new TDSAcknowledgmentsReportAction().getHistoryToken());
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

		CountryPreferences company = Accounter.getCompany()
				.getCountryPreferences();
		if (Global.get().preferences().isTrackTax()) {
			if (Accounter.getCompany().getCountry()
					.equals(Countries.UNITED_KINGDOM)) {
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
				&& hasExtraReportsPerm) {
			leftPanel.add(inventoryHeader);
			leftPanel.add(inventoryPanel);
		}

		if (hasExtraReportsPerm) {
			leftPanel.add(bankingHeader);
			leftPanel.add(bankingPanel);
		}

		if (Global.get().preferences().isJobTrackingEnabled()) {
			leftPanel.add(jobHeader);
			leftPanel.add(jobPanel);
		}

		if (hasExtraReportsPerm && hasPermission(Features.BUDGET)) {
			rightPanel.add(budgetHeader);
			rightPanel.add(budgetPanel);
		}

		if (Global.get().preferences().isTrackTax()) {
			rightPanel.add(mainTaxHeader);
			rightPanel.add(mainTaxPanel);
		}

		if (hasExtraReportsPerm) {
			rightPanel.add(salesHeader);
			rightPanel.add(salesPanel);
		}
		rightPanel.add(vendorAndPayableHeader);
		rightPanel.add(vendorAndPayablePanel);

		if (hasExtraReportsPerm) {
			rightPanel.add(purchaseHeader);
			rightPanel.add(purchasePanel);
		}

		if (hasExtraReportsPerm && hasPermission(Features.FIXED_ASSET)) {
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
		return Accounter.hasPermission(extraReports);
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
