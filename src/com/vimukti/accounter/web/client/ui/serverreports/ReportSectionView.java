package com.vimukti.accounter.web.client.ui.serverreports;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
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
		HorizontalPanel1.setSize("100%", "70px");
		// HorizontalPanel1.setMembersMargin(5);

		HorizontalPanel HorizontalPanel2 = new HorizontalPanel();
		HorizontalPanel2.setSize("100%", "70px");
		// HorizontalPanel2.setMembersMargin(5);

		// VLayout rightLayout= new VLayout();
		// rightLayout.setMembersMargin(5);

		// Form for Company & Financial type reports

		Label reportLabel = new Label("Reports");
		// reportLabel.setHeight(20);
		mainLayout.add(reportLabel);

		DynamicForm companyAndFinancialForm = UIUtils.form(Accounter
				.constants().companyAndFinancial());
		companyAndFinancialForm.setWidth("50%");
		companyAndFinancialForm.setHeight("40%");
		companyAndFinancialForm.setNumCols(1);

		LinkItem profitAndLossLink = new LinkItem();
		profitAndLossLink.setLinkTitle(Accounter.constants().profitAndLoss());
		profitAndLossLink.setShowTitle(false);
		profitAndLossLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getProfitAndLossAction());
			}

		});

		LinkItem balanceSheetLink = new LinkItem();
		balanceSheetLink.setLinkTitle(Accounter.constants().balanceSheet());
		balanceSheetLink.setShowTitle(false);
		balanceSheetLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getBalanceSheetAction());
			}

		});

		LinkItem cashFlowLink = new LinkItem();
		cashFlowLink.setLinkTitle(Accounter.constants().cashFlowStatement());
		cashFlowLink.setShowTitle(false);
		cashFlowLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getCashFlowStatementAction());
			}

		});

		LinkItem trailBalanceLink = new LinkItem();
		trailBalanceLink.setLinkTitle(Accounter.constants().trialBalance());
		trailBalanceLink.setShowTitle(false);
		trailBalanceLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getTrialBalanceAction());
			}

		});

		LinkItem transactionDetailsByAccountsLink = new LinkItem();
		transactionDetailsByAccountsLink.setLinkTitle(Accounter.messages()
				.transactionDetailsByAccount(Global.get().Account()));
		transactionDetailsByAccountsLink.setShowTitle(false);
		transactionDetailsByAccountsLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getTransactionDetailByAccountAction());
			}

		});

		companyAndFinancialForm.setFields(profitAndLossLink, balanceSheetLink,
				cashFlowLink, trailBalanceLink,
				transactionDetailsByAccountsLink);

		HorizontalPanel1.add(companyAndFinancialForm);

		// Form for Sales type reports

		DynamicForm salesForm = UIUtils.form(Accounter.constants().sales());
		salesForm.setWidth("50%");
		salesForm.setHeight("40%");
		salesForm.setNumCols(1);

		LinkItem salesByCustomerSummaryLink = new LinkItem();
		salesByCustomerSummaryLink.setLinkTitle(Accounter.messages()
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
		salesByCustomerDetailLink.setLinkTitle(Accounter.messages()
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
		salesByItemSummaryLink.setLinkTitle(Accounter.constants()
				.salesByItemSummary());
		salesByItemSummaryLink.setShowTitle(false);
		salesByItemSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByItemSummaryAction());
			}

		});

		LinkItem salesByItemDetailLink = new LinkItem();
		salesByItemDetailLink.setLinkTitle(Accounter.constants()
				.salesByItemDetail());
		salesByItemDetailLink.setShowTitle(false);
		salesByItemDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getSalesByItemDetailAction());
			}

		});

		salesForm.setFields(salesByCustomerSummaryLink,
				salesByCustomerDetailLink, salesByItemSummaryLink,
				salesByItemDetailLink);

		HorizontalPanel1.add(salesForm);

		// Form for purchase type reports

		DynamicForm purchaseForm = UIUtils.form(Accounter.constants()
				.purchase());
		purchaseForm.setWidth("50%");
		purchaseForm.setHeight("40%");
		purchaseForm.setNumCols(1);

		LinkItem purchaseBySupplierSummaryLink = new LinkItem();
		purchaseBySupplierSummaryLink.setLinkTitle(Accounter.messages()
				.purchaseByVendorSummary(Global.get().Vendor()));
		purchaseBySupplierSummaryLink.setShowTitle(false);
		purchaseBySupplierSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getPurchaseByVendorSummaryAction());
			}

		});

		LinkItem purchaseBySupplierDetailLink = new LinkItem();
		purchaseBySupplierDetailLink.setLinkTitle(Accounter.messages()
				.purchaseByVendorDetail(Global.get().vendor()));
		purchaseBySupplierDetailLink.setShowTitle(false);
		purchaseBySupplierDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getPurchaseByVendorDetailAction());
			}

		});

		LinkItem purchaseByProductSummaryLink = new LinkItem();
		purchaseByProductSummaryLink.setLinkTitle(Accounter.constants()
				.purchaseByProductSummary());
		purchaseByProductSummaryLink.setShowTitle(false);
		purchaseByProductSummaryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getPurchaseByItemSummaryAction());
			}

		});

		LinkItem purchaseByProductDetailLink = new LinkItem();
		purchaseByProductDetailLink.setLinkTitle(Accounter.constants()
				.purchaseByProductDetail());
		purchaseByProductDetailLink.setShowTitle(false);
		purchaseByProductDetailLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getPurchaseByItemAction());
			}

		});

		purchaseForm.setFields(purchaseBySupplierSummaryLink,
				purchaseBySupplierDetailLink, purchaseByProductSummaryLink,
				purchaseByProductDetailLink);

		HorizontalPanel2.add(purchaseForm);

		// Form for Other type reports

		DynamicForm otherForm = UIUtils.form(Accounter.constants().other());
		otherForm.setWidth("50%");
		otherForm.setHeight("40%");
		otherForm.setNumCols(1);

		LinkItem customerTransactionHistoryLink = new LinkItem();
		customerTransactionHistoryLink.setLinkTitle(Accounter.messages()
				.customerTransactionHistory(Global.get().customer()));
		customerTransactionHistoryLink.setShowTitle(false);
		customerTransactionHistoryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getCustomerTransactionHistoryAction());
			}

		});

		LinkItem arAgingLink = new LinkItem();
		arAgingLink.setLinkTitle(Accounter.constants().arAgeing());
		arAgingLink.setShowTitle(false);
		arAgingLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getArAgingDetailAction());
			}

		});

		LinkItem apAgingLink = new LinkItem();
		apAgingLink.setLinkTitle(Accounter.constants().apAging());
		apAgingLink.setShowTitle(false);
		apAgingLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc, ActionFactory.getAorpAgingDetailAction());
			}

		});

		LinkItem supplierTransactionHistoryLink = new LinkItem();
		supplierTransactionHistoryLink.setLinkTitle(Accounter.messages()
				.vendorTransactionHistory(Global.get().Vendor()));
		supplierTransactionHistoryLink.setShowTitle(false);
		supplierTransactionHistoryLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getVendorTransactionHistoryAction());
			}

		});

		LinkItem mostProfitableCustomerLink = new LinkItem();
		mostProfitableCustomerLink.setLinkTitle(Accounter.messages()
				.mostProfitableCustomer(Global.get().customer()));
		mostProfitableCustomerLink.setShowTitle(false);
		mostProfitableCustomerLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				ClientAccount acc = null;
				UIUtils.runAction(acc,
						ActionFactory.getMostProfitableCustomersAction());
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
