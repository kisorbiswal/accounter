package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class BaseHomeView extends AbstractBaseView {
	private VerticalPanel widgetLayout;

	// private FinanceMenuImages images = GWT.create(FinanceMenuImages.class);

	// private boolean wait;
	// private TextItem custNameText;
	// private TextItem emailText;
	// private TextItem contactNoText;

	public BaseHomeView() {
		createView();
	}

	private void createView() {

		HorizontalPanel mainLayout = new HorizontalPanel();
		mainLayout.setSize("100%", "100%");
		// mainLayout.setSize("100%", "100%");
		// mainLayout.setMembersMargin(5);

		// HorizontalPanel firstImagePanel = getFirstImagePanel();
		// HorizontalPanel secondImagePanel = getSecondImagePanel();
		// firstImagePanel.setStyleName(FinanceApplication.constants()
		// .imageAction());
		// secondImagePanel.setStyleName(FinanceApplication
		// .constants().imageAction());
		VerticalPanel imagePanel = new VerticalPanel();
		imagePanel.setStyleName(Accounter.constants().imageActionContainer());
		imagePanel.setSpacing(5);
		// imagePanel.setWidth("100%");
		// imagePanel.add(firstImagePanel);
		// imagePanel.add(secondImagePanel);
		widgetLayout = new VerticalPanel();
		widgetLayout.setStyleName("finance-portlet");
		widgetLayout.setWidth("100%");
		// widgetLayout.add(imagePanel);
		mainLayout.add(widgetLayout);

		// VerticalPanel rightLayout = new VerticalPanel();
		// // rightLayout.setMargin(15);
		// // rightLayout.setWidth("30%");
		// // rightLayout.setHeight("100px");
		//
		// DecoratedTabPanel tabSet = new DecoratedTabPanel();
		// // tabSet.setEdgeMarginSize(10);
		// // tabSet.setTabBarPosition(Side.TOP);
		// // tabSet.setTop(15);
		// // tabSet.setWidth("100%");
		// // tabSet.setHeight("100%");
		//
		// tabSet.add(getAddTab(), FinanceApplication.constants()
		// .add());
		// tabSet.add(getFindTab(), FinanceApplication.constants()
		// .find());
		// tabSet.selectTab(0);
		// rightLayout.add(tabSet);
		//
		// mainLayout.add(rightLayout);

		add(mainLayout);
	}

	private HorizontalPanel getFirstImagePanel() {
		HorizontalPanel firstRowPanel = new HorizontalPanel();
		firstRowPanel.setWidth("100%");

		ImageButton invoiceButton = new ImageButton(Accounter.constants()
				.enterInvoice(), Accounter.getFinanceImages().newInvoice());
		invoiceButton.setAction(ActionFactory.getNewInvoiceAction());

		ImageButton receivePaymentButton = new ImageButton(Accounter
				.constants().receivePayments(), Accounter.getFinanceImages()
				.receivedPaymentList());
		receivePaymentButton.setAction(ActionFactory.getReceivePaymentAction());

		ImageButton refundAnsCredits = new ImageButton(Accounter.constants()
				.customerCredit(), Accounter.getFinanceImages()
				.customerRefundsList());
		refundAnsCredits.setAction(ActionFactory
				.getNewCreditsAndRefundsAction());

		ImageButton bankDeposit = new ImageButton(Accounter.constants()
				.bankDeposite(), Accounter.getFinanceImages().makeDeposits());
		bankDeposit.setAction(ActionFactory.getMakeDepositAction());
		ImageButton fileVat = new ImageButton(Accounter.constants().fileVAT(),
				Accounter.getFinanceImages().fileVat());
		fileVat.setAction(ActionFactory.getFileVatAction());

		// ImageButton vat = new ImageButton(FinanceApplication
		// .constants().vatItem(), images.Accounts());
		// vat.setAction(ActionFactory.getNewVatItemAction());
		// ImageButton newitem = new ImageButton(FinanceApplication
		// .constants().newItem(), images.newItem());
		// newitem.setAction(ActionFactory.getNewItemAction());

		// statementButton.setAction(ActionFactory.getNewInvoiceAction());

		// ImageButton chartofAccounts = new ImageButton(FinanceApplication
		// .constants().chartOfAccount(), images
		// .cahrtOfAccounts());
		// chartofAccounts.setAction(ActionFactory
		// .getChartOfAccountsAction());

		// ImageButton statementButton = new ImageButton(FinanceApplication
		// .constants().statements(), images.newBankAccount());

		firstRowPanel.add(invoiceButton.getImagePanel());
		firstRowPanel.add(receivePaymentButton.getImagePanel());
		firstRowPanel.add(refundAnsCredits.getImagePanel());

		firstRowPanel.add(bankDeposit.getImagePanel());
		firstRowPanel.add(fileVat.getImagePanel());
		// firstRowPanel.add(statementButton.getImagePanel());
		// firstRowPanel.add(chartofAccounts.getImagePanel());
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK)
		// firstRowPanel.add(vat.getImagePanel());
		// else
		// firstRowPanel.add(newitem.getImagePanel());
		return firstRowPanel;
	}

	private HorizontalPanel getSecondImagePanel() {
		HorizontalPanel secondRowPanel = new HorizontalPanel();
		secondRowPanel.setWidth("100%");

		ImageButton enterBillButton = new ImageButton(Accounter.constants()
				.enterBill(), Accounter.getFinanceImages().enterBills());
		enterBillButton.setAction(ActionFactory.getEnterBillsAction());

		ImageButton payBill = new ImageButton(Accounter.constants().payBill(),
				Accounter.getFinanceImages().payBills());
		payBill.setAction(ActionFactory.getPayBillsAction());

		ImageButton vendorcredit = new ImageButton(UIUtils.getVendorString(
				Accounter.constants().supplierCredit(), Accounter.constants()
						.vendorCreditMemo()), Accounter.getFinanceImages()
				.newCreditMemo());
		vendorcredit.setAction(ActionFactory.getNewCreditMemoAction());

		ImageButton expenses = new ImageButton(Accounter.constants()
				.enterExpenses(), Accounter.getFinanceImages().recordExpenses());
		expenses.setAction(ActionFactory.getRecordExpensesAction());
		ImageButton creditCardCharge = new ImageButton(Accounter.constants()
				.creaditCardCharges(), Accounter.getFinanceImages()
				.creditChargeExpense());
		creditCardCharge.setAction(ActionFactory.getCreditCardChargeAction());

		// ImageButton recoincile = new ImageButton(" Reconcile", images
		// .newCheck());
		// recoincile.setAction(ActionFactory.getMakeDepositAction());

		secondRowPanel.add(enterBillButton.getImagePanel());
		secondRowPanel.add(payBill.getImagePanel());
		secondRowPanel.add(vendorcredit.getImagePanel());
		secondRowPanel.add(expenses.getImagePanel());

		// secondRowPanel.add(recoincile.getImagePanel());
		secondRowPanel.add(creditCardCharge.getImagePanel());

		return secondRowPanel;
	}

	// private VerticalPanel getFindTab() {
	// DynamicForm form = new DynamicForm();
	// form.setNumCols(1);
	//
	// TextItem invoicetext = new TextItem(FinanceApplication
	// .constants().invoice());
	// // invoicetext.setWidth("100%");
	// // invoicetext.setTitleOrientation(TitleOrientation.TOP);
	//
	// // CanvasItem item1= new CanvasItem();
	// // Button goButton1= new Button("Go");
	// // goButton1.setWidth("*");
	//
	// TextItem contactText = new TextItem(FinanceApplication
	// .constants().contacts());
	// // contactText.setWidth("100%");
	// // contactText.setTitleOrientation(TitleOrientation.TOP);
	// // Button goButton2= new Button("Go");
	// // goButton2.setWidth("*");
	//
	// TextItem accountstext = new TextItem();
	// accountstext.setTitle(FinanceApplication.constants()
	// .account());
	// // accountstext.setColSpan(2);
	// // accountstext.setWidth("100%");
	// // accountstext.setTitleOrientation(TitleOrientation.TOP);
	// // Button goButton3= new Button("Go");
	// // goButton3.setWidth("*");
	//
	// VerticalPanel vLayout = new VerticalPanel();
	// form.setFields(invoicetext, contactText, accountstext);
	//
	// // VLayout innerLayout=new VLayout();
	// // innerLayout.setTop(5);
	// // innerLayout.add(goButton1);
	// // innerLayout.add(goButton2);
	// // innerLayout.add(goButton3);
	// //
	// // HorizontalPanel outerLayout = new HorizontalPanel();
	// // outerLayout.add(form);
	// // outerLayout.add(innerLayout);
	//
	// vLayout.add(form);
	//
	// // Tab tab = new Tab("Find");
	// // tab.setPane(vLayout);
	//
	// return vLayout;
	// }
	//
	// private VerticalPanel getAddTab() {
	// VerticalPanel vLay = new VerticalPanel();
	//
	// Label newInvoiceButton = new Label();
	// newInvoiceButton.setStyleName("newInvoice");
	// newInvoiceButton.setText(FinanceApplication.constants()
	// .newInvoice());
	// // newInvoiceButton.setSize("100%", "*");
	// // newInvoiceButton.setCursor(Cursor.HAND);
	// // newInvoiceButton.setAlign(Alignment.CENTER);
	// newInvoiceButton.addClickHandler(new ClickHandler() {
	//
	// public void onClick(ClickEvent event) {
	// try {
	//
	// ActionFactory.getNewInvoiceAction().run(null,
	// false);
	//
	// } catch (Throwable e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// });
	//
	// Label newQuoteButton = new Label();
	// newQuoteButton.setStyleName("newquote");
	// newQuoteButton.setText(FinanceApplication.constants()
	// .newQuote());
	// // newQuoteButton.setAlign(Alignment.CENTER);
	// // newQuoteButton.setSize("100%", "*");
	// // newQuoteButton.setCursor(Cursor.HAND);
	// newQuoteButton.addClickHandler(new ClickHandler() {
	//
	// public void onClick(ClickEvent event) {
	// try {
	// ActionFactory.getNewQuoteAction().run(null, false);
	// } catch (Throwable e) {
	//
	// e.printStackTrace();
	// }
	// }
	//
	// });
	//
	// Label newBillButton = new Label();
	// newBillButton.setStyleName("newBill");
	// newBillButton.setText(FinanceApplication.constants()
	// .newBill());
	// // newBillButton.setAlign(Alignment.CENTER);
	// // newBillButton.setSize("100%", "*");
	// // newBillButton.setCursor(Cursor.HAND);
	// newBillButton.addClickHandler(new ClickHandler() {
	//
	// public void onClick(ClickEvent event) {
	// try {
	// ActionFactory.getEnterBillsAction().run(null, false);
	// } catch (Throwable e) {
	//
	// e.printStackTrace();
	// }
	// }
	//
	// });
	//
	// VerticalPanel vLayout = new VerticalPanel();
	// // vLayout.setWidth("100%");
	//
	// DynamicForm form = new DynamicForm();
	// // form.setColWidths(1);
	// form.setNumCols(1);
	// form.setIsGroup(true);
	// form.setGroupTitle(FinanceApplication.constants()
	// .addNewCustomer());
	// // form.setBottom(1);
	// // form.setWidth("100%");
	//
	// custNameText = new TextItem();
	// custNameText.setTitle(FinanceApplication.constants().name());
	// // custNameText.setTitleOrientation(TitleOrientation.TOP);
	// // custNameText.setWidth("100%");
	// custNameText.setRequired(true);
	// // TextItem lastNameText = new TextItem("");
	// // lastNameText.setWidth("50%");
	// // lastNameText.setTitleOrientation(TitleOrientation.TOP);
	// emailText = new TextItem();
	// emailText.setTitle(FinanceApplication.constants().email());
	// // emailText.setWidth("100%");
	// // emailText.setColSpan(2);
	// // emailText.setTitleOrientation(TitleOrientation.TOP);
	// contactNoText = new TextItem();
	// contactNoText.setTitle(FinanceApplication.constants().contactNumber());
	// // contactNoText.setWidth("100%");
	// // contactText.setColSpan(2);
	// // contactNoText.setTitleOrientation(TitleOrientation.TOP);
	//
	// HorizontalPanel layout = new HorizontalPanel();
	// Button addButton = new
	// Button(FinanceApplication.constants().add());
	// // addButton.setRight(5);
	// addButton.addClickHandler(new ClickHandler() {
	//
	// public void onClick(ClickEvent event) {
	// try {
	// saveAndUpdateView();
	// saveSuccess(null);
	// } catch (Exception e) {
	// //
	// }
	//
	// }
	// });
	// layout.add(addButton);
	// // layout.setAlign(Alignment.RIGHT);
	//
	// form.setFields(custNameText, emailText, contactNoText);
	// vLayout.add(form);
	// vLayout.add(layout);
	// // vLayout.setMembersMargin(1);
	// vLay.add(newInvoiceButton);
	// vLay.add(newQuoteButton);
	// vLay.add(newBillButton);
	// vLay.add(vLayout);
	// // vLay.setMembersMargin(1);
	// // Tab tab = new Tab("Add");
	// // tab.setPane(vLay);
	// return vLay;
	//
	// }
	//
	public VerticalPanel getLeftLayout() {
		return widgetLayout;
	}

	//
	// @Override
	// public void init() {
	//
	// }
	//
	// @Override
	// public void initData() {
	//
	// }
	//
	// private ClientCustomer getCustomerObject() {
	//
	// customer = new ClientCustomer();
	//
	// customer.setName((String) custNameText.getValue());
	// customer.setFileAs((String) custNameText.getValue());
	//
	// Set<ClientEmail> emailSet = new HashSet<ClientEmail>();
	// ClientEmail email = new ClientEmail();
	// email.setEmail((String) emailText.getValue());
	// emailSet.add(email);
	//
	// customer.setEmails(emailSet);
	//
	// Set<ClientPhone> phoneNumbers = new HashSet<ClientPhone>();
	// ClientPhone clientPhone = new ClientPhone();
	// clientPhone.setNumber((String) contactNoText.getValue());
	// phoneNumbers.add(clientPhone);
	// customer.setPhoneNumbers(phoneNumbers);
	//
	// for (ClientTaxGroup clientTaxGroup : FinanceApplication.getCompany()
	// .getTaxGroups()) {
	// if (clientTaxGroup.getName().equals("None")) {
	// customer.setTaxGroup(clientTaxGroup.getID());
	// }
	// }
	//
	// return customer;
	//
	// }
	//
	// @Override
	// public void saveAndUpdateView() throws Exception {
	//
	// if (!wait) {
	// try {
	// ClientCustomer customer = getCustomerObject();
	// if (Utility.isObjectExist(FinanceApplication.getCompany()
	// .getCustomers(), customer.getName())) {
	// throw new InvalidEntryException(
	// AccounterErrorType.ALREADYEXIST);
	// }
	// createObject(customer);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw e;
	// }
	// }
	//
	// }

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	// @Override
	// protected void saveSuccess(IsSerializable result) {
	// if (result != null) {
	// Accounter.showInformation("New Customer Created!");
	// super.saveSuccess(result);
	//
	// } else {
	// saveFailed(new Exception());
	// }
	//
	// }
	@Override
	public void fitToSize(int height, int width) {
		this.setHeight(height + "px");

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}
}
