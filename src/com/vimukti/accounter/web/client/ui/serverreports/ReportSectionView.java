package com.vimukti.accounter.web.client.ui.serverreports;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;

public class ReportSectionView extends BaseHomeView {

	@Override
	public void init() {
		getLeftLayout().add(createControl());
		setSize("100%", "100%");

	}

	private VerticalPanel createControl() {
		VerticalPanel mainLayout = new VerticalPanel();
		
		BalanceSheetServerReport report = new BalanceSheetServerReport(0, 0, 0);
		// report.setSize("80%", "80%");
		// report.setParentLayout(mainLayout);
		// report.initReportType();
		// report.generateReport(FinanceApplication.getStartDate(), new Date());

		// mainLayout.setSize("100%", "100%");
		// mainLayout.setMembersMargin(10);
		// mainLayout.setTop(5);
		// mainLayout.setLeft(5);

		HorizontalPanel HorizontalPanel1 = new HorizontalPanel();
		HorizontalPanel1.setSize("100%", "70");
		// HorizontalPanel1.setMembersMargin(5);

		HorizontalPanel HorizontalPanel2 = new HorizontalPanel();
		HorizontalPanel2.setSize("100%", "70");
		// HorizontalPanel2.setMembersMargin(5);

		// VLayout rightLayout= new VLayout();
		// rightLayout.setMembersMargin(5);

		// Form for Company & Financial type reports

		Label reportLabel = new Label("Reports");
		// reportLabel.setHeight(20);
		mainLayout.add(reportLabel);

		DynamicForm companyAndFinancialForm = UIUtils
				.form("Company And Financial");
		companyAndFinancialForm.setWidth("50%");
		companyAndFinancialForm.setHeight("40%");
		companyAndFinancialForm.setNumCols(1);

		LinkItem profitAndLossLink = new LinkItem();
		profitAndLossLink.setLinkTitle("Profit And Loss");
		profitAndLossLink.setShowTitle(false);
		profitAndLossLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getProfitAndLossAction());
			}

		});

		LinkItem balanceSheetLink = new LinkItem();
		balanceSheetLink.setLinkTitle("Balance Sheet");
		balanceSheetLink.setShowTitle(false);
		balanceSheetLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getBalanceSheetAction());
			}

		});

		LinkItem cashFlowLink = new LinkItem();
		cashFlowLink.setLinkTitle("Cash Flow Statement");
		cashFlowLink.setShowTitle(false);
		cashFlowLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getCashFlowStatementAction());
			}

		});

		LinkItem trailBalanceLink = new LinkItem();
		trailBalanceLink.setLinkTitle("Trial Balance");
		trailBalanceLink.setShowTitle(false);
		trailBalanceLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getTrialBalanceAction());
			}

		});

		LinkItem transactionDetailsByAccountsLink = new LinkItem();
		transactionDetailsByAccountsLink
				.setLinkTitle("Transaction Details By Account");
		transactionDetailsByAccountsLink.setShowTitle(false);
		transactionDetailsByAccountsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getTransactionDetailByAccountAction());
			}

		});

		companyAndFinancialForm.setFields(profitAndLossLink, balanceSheetLink,
				cashFlowLink, trailBalanceLink,
				transactionDetailsByAccountsLink);

		HorizontalPanel1.add(companyAndFinancialForm);

		// Form for Sales type reports

		DynamicForm salesForm = UIUtils.form("Sales");
		salesForm.setWidth("50%");
		salesForm.setHeight("40%");
		salesForm.setNumCols(1);

		LinkItem salesByCustomerSummaryLink = new LinkItem();
		salesByCustomerSummaryLink.setLinkTitle("Sales By Customer Summary");
		salesByCustomerSummaryLink.setShowTitle(false);
		salesByCustomerSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getSalesByCustomerSummaryAction());
			}

		});

		LinkItem salesByCustomerDetailLink = new LinkItem();
		salesByCustomerDetailLink.setLinkTitle("Sales By Customer Detail");
		salesByCustomerDetailLink.setShowTitle(false);
		salesByCustomerDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getSalesByCustomerDetailAction());
			}

		});

		LinkItem salesByItemSummaryLink = new LinkItem();
		salesByItemSummaryLink.setLinkTitle("Sales By Item Summary");
		salesByItemSummaryLink.setShowTitle(false);
		salesByItemSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getSalesByItemSummaryAction());
			}

		});

		LinkItem salesByItemDetailLink = new LinkItem();
		salesByItemDetailLink.setLinkTitle("Sales By Item Detail");
		salesByItemDetailLink.setShowTitle(false);
		salesByItemDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getSalesByItemDetailAction());
			}

		});

		salesForm.setFields(salesByCustomerSummaryLink,
				salesByCustomerDetailLink, salesByItemSummaryLink,
				salesByItemDetailLink);

		HorizontalPanel1.add(salesForm);

		// Form for purchase type reports

		DynamicForm purchaseForm = UIUtils.form("Purchase");
		purchaseForm.setWidth("50%");
		purchaseForm.setHeight("40%");
		purchaseForm.setNumCols(1);

		LinkItem purchaseBySupplierSummaryLink = new LinkItem();
		purchaseBySupplierSummaryLink
				.setLinkTitle("Purchase By Supplier Summary");
		purchaseBySupplierSummaryLink.setShowTitle(false);
		purchaseBySupplierSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getPurchaseByVendorSummaryAction());
			}

		});

		LinkItem purchaseBySupplierDetailLink = new LinkItem();
		purchaseBySupplierDetailLink
				.setLinkTitle("Purchase By Supplier Detail");
		purchaseBySupplierDetailLink.setShowTitle(false);
		purchaseBySupplierDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getPurchaseByVendorDetailAction());
			}

		});

		LinkItem purchaseByProductSummaryLink = new LinkItem();
		purchaseByProductSummaryLink
				.setLinkTitle("Purchase By Product Summary");
		purchaseByProductSummaryLink.setShowTitle(false);
		purchaseByProductSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getPurchaseByItemSummaryAction());
			}

		});

		LinkItem purchaseByProductDetailLink = new LinkItem();
		purchaseByProductDetailLink.setLinkTitle("Purchase By Product Detail");
		purchaseByProductDetailLink.setShowTitle(false);
		purchaseByProductDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getPurchaseByItemAction());
			}

		});

		purchaseForm.setFields(purchaseBySupplierSummaryLink,
				purchaseBySupplierDetailLink, purchaseByProductSummaryLink,
				purchaseByProductDetailLink);

		HorizontalPanel2.add(purchaseForm);

		// Form for Other type reports

		DynamicForm otherForm = UIUtils.form("Other");
		otherForm.setWidth("50%");
		otherForm.setHeight("40%");
		otherForm.setNumCols(1);

		LinkItem customerTransactionHistoryLink = new LinkItem();
		customerTransactionHistoryLink
				.setLinkTitle("Customer Transaction History");
		customerTransactionHistoryLink.setShowTitle(false);
		customerTransactionHistoryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getCustomerTransactionHistoryAction());
			}

		});

		LinkItem arAgingLink = new LinkItem();
		arAgingLink.setLinkTitle("A/R Ageing");
		arAgingLink.setShowTitle(false);
		arAgingLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getArAgingDetailAction());
			}

		});

		LinkItem apAgingLink = new LinkItem();
		apAgingLink.setLinkTitle("A/P Aging");
		apAgingLink.setShowTitle(false);
		apAgingLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getAorpAgingDetailAction());
			}

		});

		LinkItem supplierTransactionHistoryLink = new LinkItem();
		supplierTransactionHistoryLink
				.setLinkTitle("Supplier Transaction History");
		supplierTransactionHistoryLink.setShowTitle(false);
		supplierTransactionHistoryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getVendorTransactionHistoryAction());
			}

		});

		LinkItem mostProfitableCustomerLink = new LinkItem();
		mostProfitableCustomerLink.setLinkTitle("Most Profitable Customer");
		mostProfitableCustomerLink.setShowTitle(false);
		mostProfitableCustomerLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ReportsActionFactory
						.getMostProfitableCustomersAction());
			}

		});

		otherForm.setFields(customerTransactionHistoryLink, arAgingLink,
				apAgingLink, supplierTransactionHistoryLink,
				mostProfitableCustomerLink);

		HorizontalPanel2.add(otherForm);

		mainLayout.add(HorizontalPanel1);
		mainLayout.add(HorizontalPanel2);

		return mainLayout;

	}
}
