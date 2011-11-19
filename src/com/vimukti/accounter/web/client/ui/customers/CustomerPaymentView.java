//package com.vimukti.accounter.web.client.ui.customers;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.vimukti.accounter.web.client.core.ClientCustomer;
//import com.vimukti.accounter.web.client.core.ClientPriceLevel;
//import com.vimukti.accounter.web.client.core.ClientSalesPerson;
//import com.vimukti.accounter.web.client.core.ClientTaxGroup;
//import com.vimukti.accounter.web.client.core.ClientTransaction;
//import com.vimukti.accounter.web.client.ui.AccounterListGrid;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.SelectItem;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//
//public class CustomerPaymentView extends AbstractCustomerTransactionView {
//	CustomersMessages messages = GWT.create(CustomersMessages.class);
//
//	public CustomerPaymentView() {
//		super(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
//	}
//
//	@Override
//	protected void createControls() {
//		setTitle(UIUtils.title(messages.newCustomerPayment()));
//		Label lab1 = new Label(
//				"<div style='font-size: 20px;margin-top: 10px'>Customer Payment "
//						+ getTransactionStatus() + "</div>");
//		// lab1.setWrap(false);
//		// lab1.setAutoFit(true);
//
//		transactionDateItem = createTransactionDateItem();
//
//		transactionNumber = createTransactionNumberItem();
//
//		DynamicForm dateNoForm = new DynamicForm();
//		dateNoForm.setNumCols(4);
//		dateNoForm.setWidth("25%");
//		// dateNoForm.setLayoutAlign(Alignment.RIGHT);
//		dateNoForm.setFields(transactionDateItem, transactionNumber);
//		forms.add(dateNoForm);
//
//		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
//		// labeldateNoLayout.setHeight(20);
//		labeldateNoLayout.setWidth("100%");
//		labeldateNoLayout.add(lab1);
//		labeldateNoLayout.add(dateNoForm);
//
//		SelectItem recvFrmSelect = new SelectItem(messages
//				.receivedFrom());
//		recvFrmSelect.setRequired(true);
//		// recvFrmSelect.setWidth("*");
//		TextItem amtText = new TextItem();
//		amtText.setTitle(messages.amount());
//		// amtText.setWidth("*");
//
//		SelectItem payMethSelect = new SelectItem(messages
//				.paymentMethod());
//		payMethSelect.setRequired(true);
//		// payMethSelect.setWidth("*");
//
//		TextItem refText = new TextItem();
//		refText.setTitle(messages.reference());
//		// refText.setWidth("*");
//
//		TextItem memoText = new TextItem();
//		memoText.setTitle(messages.memo());
//		// memoText.setWidth("*");
//
//		DynamicForm payForm = UIUtils.form(messages.payment());
//		payForm.setWidth("50%");
//		// payForm.setAutoHeight();
//		payForm.setFields(recvFrmSelect, amtText, payMethSelect, refText,
//				memoText);
//		forms.add(payForm);
//
//		TextItem custBalText = new TextItem();
//		custBalText.setTitle(messages.customerBalance());
//		// custBalText.setWidth("*");
//
//		DynamicForm balForm = UIUtils.form(messages.balances());
//		balForm.setFields(custBalText);
//		forms.add(balForm);
//
//		SelectItem depoSelect = new SelectItem(messages.depositIn());
//		// depoSelect.setWidth("*");
//		depoSelect.setRequired(true);
//		DynamicForm depoForm = UIUtils.form(messages.deposit());
//		depoForm.setFields(depoSelect);
//		forms.add(depoForm);
//
//		AccounterListGrid grid = new AccounterListGrid();
//		grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
//		grid.setSelectionType(SelectionStyle.SIMPLE);
//		ListGridField dueDateField = new ListGridField("due_date",
//				messages.dueDate());
//		ListGridField invoiceField = new ListGridField("invoice",
//				messages.invoice());
//		ListGridField invoiceAmtField = new ListGridField("invoice_amt",
//				messages.invoiceAmount());
//		ListGridField amtDueField = new ListGridField("amt_due",
//				messages.amountDue());
//		ListGridField discDateField = new ListGridField("disc_date",
//				messages.discountDate());
//		ListGridField cashDiscField = new ListGridField("cach_disc",
//				messages.cashDiscount());
//		ListGridField writeOffField = new ListGridField("write_off",
//				messages.writeOff());
//		ListGridField appCredField = new ListGridField("app_credit",
//				messages.appliedCredit());
//		ListGridField payField = new ListGridField("pay", messages
//				.payment());
//		grid.setFields(dueDateField, invoiceField, invoiceAmtField,
//				amtDueField, discDateField, cashDiscField, writeOffField,
//				appCredField, payField);
//
//		TextItem unusedCredText = new TextItem();
//		unusedCredText.setTitle(messages.unusedCredits());
//		unusedCredText.setColSpan(1);
//		unusedCredText.setWidth(100);
//		TextItem unusedPayText = new TextItem();
//		unusedPayText.setTitle(messages.unusedPayments());
//		unusedPayText.setColSpan(1);
//		unusedPayText.setWidth(100);
//		DynamicForm unusedsForm = new DynamicForm();
//		// unusedsForm.setLayoutAlign(Alignment.RIGHT);
//		unusedsForm.setWidth("*");
//		unusedsForm.setNumCols(4);
//		unusedsForm.setFields(unusedCredText, unusedPayText);
//		forms.add(unusedsForm);
//
//		VerticalPanel rightVLay = new VerticalPanel();
//		rightVLay.setWidth("*");
//		rightVLay.add(balForm);
//		rightVLay.add(depoForm);
//
//		HorizontalPanel topHLay = new HorizontalPanel();
//		topHLay.setWidth("100%");
//		topHLay.add(payForm);
//		topHLay.add(rightVLay);
//
//		Button saveCloseButt = new Button(messages.saveAndClose());
//		// saveCloseButt.setAutoFit(true);
//		// saveCloseButt.setLayoutAlign(Alignment.LEFT);
//
//		Button saveNewButt = new Button(messages.saveAndNew());
//		// saveNewButt.setAutoFit(true);
//		// saveNewButt.setLayoutAlign(Alignment.RIGHT);
//
//		HorizontalPanel buttHLay = new HorizontalPanel();
//		// buttHLay.setAlign(Alignment.RIGHT);
//		// buttHLay.setMembersMargin(20);
//		// buttHLay.setMargin(10);
//		// buttHLay.setAutoHeight();
//		buttHLay.add(saveCloseButt);
//		buttHLay.add(saveNewButt);
//
//		VerticalPanel mainVLay = new VerticalPanel();
//		mainVLay.setSize("100%", "100%");
//		// mainVLay.setMargin(10);
//		mainVLay.add(labeldateNoLayout);
//		mainVLay.add(topHLay);
//	//	mainVLay.add(grid);
//		mainVLay.add(unusedsForm);
//		mainVLay.add(buttHLay);
//		canvas.add(mainVLay);
//		setSize("100%", "100%");
//
//	}
//
//	@Override
//	protected void customerSelected(ClientCustomer customer) {
//		
//
//	}
//
//	@Override
//	protected void salesPersonSelected(ClientSalesPerson person) {
//		
//
//	}
//
//	@Override
//	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
//		
//
//	}
//
//	@Override
//	protected void taxGroupSelected(ClientTaxGroup taxGroup) {
//		
//
//	}
//
//	@Override
//	protected void initTransactionViewData(ClientTransaction transactionObject) {
//		
//
//	}
//
//	@Override
//	protected void initMemoAndReference() {
//		
//
//	}
//
//	@Override
//	protected void initSalesTaxNonEditableItem() {
//		
//
//	}
//
//	@Override
//	protected void initTransactionTotalNonEditableItem() {
//		
//
//	}
//
//	@Override
//	public boolean validate() throws InvalidTransactionEntryException {
//		return true;
//	}
//
//	@Override
//	public void init() {
//
//	}
//
//	@Override
//	public void initData() {
//
//	}
//
// }
