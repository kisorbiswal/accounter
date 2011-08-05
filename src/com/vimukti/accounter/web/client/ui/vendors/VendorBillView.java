package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * modified by Ravi Kiran.G, Murali.A
 * 
 */
public class VendorBillView extends
		AbstractVendorTransactionView<ClientEnterBill> {

	private PaymentTermsCombo paymentTermsCombo;
	private ClientPaymentTerms selectedPaymentTerm;
	private DateField dueDateItem;
	private ClientEnterBill enterBillToBeEdited;

	private CheckboxItem euVATexempVendor;

	private CheckboxItem showPricesWithVAT;
	private AmountLabel netAmount;

	private AmountField total;

	private DynamicForm vendorForm, vatForm;
	private LinkItem purchaseLabel;
	private VendorBillListDialog dialog;
	private Double balanceDue = 0.0;

	private long selectedPurchaseOrder;
	private long selectedItemReceipt;

	private ArrayList<DynamicForm> listforms;
	private ArrayList<ClientTransaction> selectedOrdersAndItemReceipts;

	private VendorBillView() {
		super(ClientTransaction.TYPE_ENTER_BILL, VENDOR_TRANSACTION_GRID);
	}

	@Override
	protected void initTransactionViewData() {

		super.initTransactionViewData();
		// initTransactionNumber();
		initPaymentTerms();

	}

	private void resetGlobalVariables() {

		this.setVendor(null);
		this.billingAddress = null;
		this.contact = null;
		this.phoneNo = null;
		this.addressListOfVendor = null;
		this.contacts = null;
		List<ClientContact> list = new ArrayList<ClientContact>();
		list.addAll(contacts);
		contactCombo.initCombo(list);
		List<ClientAddress> adrsList = new ArrayList<ClientAddress>();
		adrsList.addAll(addressListOfVendor);
		billToCombo.initCombo(adrsList);
		contactCombo.setDisabled(isEdit);
		billToCombo.setDisabled(isEdit);
		// phoneSelect.setValueMap();
		setMemoTextAreaItem("");
		// setRefText("");

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {

		enterBillToBeEdited = (ClientEnterBill) transactionObject;
		ClientVendor vendor = getCompany().getVendor(
				enterBillToBeEdited.getVendor());
		contactSelected(enterBillToBeEdited.getContact());
		billToaddressSelected(enterBillToBeEdited.getVendorAddress());
		selectedVendor(vendor);
		transactionNumber.setValue(enterBillToBeEdited.getNumber());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmount.setAmount(enterBillToBeEdited.getNetAmount());
			vatTotalNonEditableText.setAmount(enterBillToBeEdited.getTotal()
					- enterBillToBeEdited.getNetAmount());
		}
		transactionTotalNonEditableText.setAmount(enterBillToBeEdited
				.getTotal());

		balanceDueNonEditableText
				.setAmount(enterBillToBeEdited.getBalanceDue());

		if (vatinclusiveCheck != null) {
			setAmountIncludeChkValue(transactionObject.isAmountsIncludeVAT());
		}
		this.dueDateItem
				.setValue(enterBillToBeEdited.getDueDate() != 0 ? new ClientFinanceDate(
						enterBillToBeEdited.getDueDate())
						: getTransactionDate());
		initMemoAndReference();
		initTransactionViewData();
		vendorTransactionGrid.setCanEdit(false);

	}

	private void initBalanceDue() {

		if (transactionObject != null) {

			setBalanceDue(((ClientEnterBill) transactionObject).getBalanceDue());

		}

	}

	public void setBalanceDue(Double balanceDue) {
		if (balanceDue == null)
			balanceDue = 0.0D;
		this.balanceDue = balanceDue;
		balanceDueNonEditableText.setAmount(balanceDue);
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	private void initPaymentTerms() {
		paymentTermsList = getCompany().getPaymentsTerms();

		paymentTermsCombo.initCombo(paymentTermsList);
		paymentTermsCombo.setDisabled(isEdit);

		if (transactionObject != null
				&& ((ClientEnterBill) transactionObject).getPaymentTerm() != 0) {
			ClientPaymentTerms paymentTerm = getCompany().getPaymentTerms(
					((ClientEnterBill) transactionObject).getPaymentTerm());
			paymentTermsCombo.setComboItem(paymentTerm);
			selectedPaymentTerm = paymentTerm;

		} else {
			for (ClientPaymentTerms paymentTerm : paymentTermsList) {
				if (paymentTerm.getName().equals("Due on Receipt")) {
					paymentTermsCombo.addItemThenfireEvent(paymentTerm);
					break;
				}
			}
			this.selectedPaymentTerm = paymentTermsCombo.getSelectedValue();
		}

	}

	public void selectedVendor(ClientVendor vendor) {
		updatePurchaseOrderOrItemReceipt(vendor);

		super.vendorSelected(vendor);
		if (transactionObject == null)
			vendorTransactionGrid.removeAllRecords();

		selectedOrdersAndItemReceipts = new ArrayList<ClientTransaction>();
		if (transactionObject != null && this.selectedPaymentTerm != null)
			paymentTermSelected(selectedPaymentTerm);
		if (transactionObject == null)
			getPurchaseOrdersAndItemReceipt();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setVendorTaxcodeToAccount();
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {

		updatePurchaseOrderOrItemReceipt(vendor);

		super.vendorSelected(vendor);
		if (transactionObject == null)
			vendorTransactionGrid.removeAllRecords();

		selectedOrdersAndItemReceipts = new ArrayList<ClientTransaction>();
		if (!(transactionObject != null && vendor.getID() == enterBillToBeEdited
				.getVendor()))
			setPaymentTermsCombo(vendor);
		if (transactionObject == null)
			getPurchaseOrdersAndItemReceipt();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setVendorTaxcodeToAccount();
		if (vendor.getPhoneNo() != null) {
			phoneSelect.setValue(vendor.getPhoneNo());
		} else {
			phoneSelect.setValue("");
		}
	}

	private void updatePurchaseOrderOrItemReceipt(ClientVendor vendor) {
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientEnterBill ent = (ClientEnterBill) this.transactionObject;

			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorTransactionGrid.removeAllRecords();
				this.vendorTransactionGrid
						.setRecords(ent.getTransactionItems());
				selectedPurchaseOrder = ent.getPurchaseOrder();
				selectedItemReceipt = ent.getItemReceipt();
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorTransactionGrid.removeAllRecords();
				this.vendorTransactionGrid.updateTotals();

				selectedPurchaseOrder = 0;
				selectedItemReceipt = 0;
			}
		}

	}

	private void setPaymentTermsCombo(ClientVendor vendor) {
		ClientPaymentTerms vendorPaymentTerm = getCompany().getPaymentTerms(
				vendor.getPaymentTermsId());
		// if (transactionObject != null && this.selectedPaymentTerm != null)
		// paymentTermSelected(selectedPaymentTerm);

		// else if (transactionObject == null) {
		if (vendorPaymentTerm != null) {
			paymentTermsCombo.setComboItem(vendorPaymentTerm);
			paymentTermSelected(vendorPaymentTerm);

		} else {
			paymentTermsList = getCompany().getPaymentsTerms();
			for (ClientPaymentTerms paymentTerm : paymentTermsList) {
				if (paymentTerm.getName().equals("Due on Receipt")) {
					paymentTermsCombo.addItemThenfireEvent(paymentTerm);
					break;
				}
			}
			this.selectedPaymentTerm = paymentTermsCombo.getSelectedValue();
			paymentTermSelected(this.selectedPaymentTerm);
		}
		// }

	}

	private void setDueDate(long date) {
		dueDateItem.setEnteredDate(new ClientFinanceDate(date));
	}

	private ClientFinanceDate getDueDate() {
		return dueDateItem.getEnteredDate();
	}

	@Override
	protected void createControls() {

		// setTitle(UIUtils.title(Accounter.constants().vendorBill()));
		Label lab1;
		// if (transactionObject == null
		// || transactionObject.getStatus() ==
		// ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
		lab1 = new Label(Accounter.constants().enterBill());

		// else
		// lab1 = new Label("Enter Bill(" + getTransactionStatus() + ")");

		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("50px");
		transactionDateItem = createTransactionDateItem();
		transactionDateItem.setTitle(Accounter.constants().billDate());
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							deliveryDateItem.setEnteredDate(date);
							dueDateItem.setEnteredDate(date);
							setTransactionDate(date);
						}
					}
				});
		transactionNumber = createTransactionNumberItem();
		// transactionNumber.setTitle(UIUtils.getVendorString("Supplier Bill no",
		// "Vendor Bill No"));
		transactionNumber.setTitle(Accounter.constants().invNo());
		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);
		forms.add(dateNoForm);

		vendorCombo = createVendorComboItem(UIUtils
				.getVendorString(Accounter.constants().supplierName(),
						Accounter.constants().vendorName()));
		// vendorCombo.setWidth(100);
		// purchaseLabel = new LinkItem();
		// purchaseLabel.setLinkTitle(FinanceApplication.constants()
		// .purchaseAndItemReceipt());
		// purchaseLabel.setShowTitle(false);
		// purchaseLabel.setDisabled(isEdit);
		// purchaseLabel.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// getPurchaseOrdersAndItemReceipt();
		// }
		// });
		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		contactCombo = createContactComboItem();
		// contactCombo.setWidth(100);
		billToCombo = createBillToComboItem();
		billToCombo.setWidth(100);
		if (this.transactionObject != null)
			billToCombo.setDisabled(true);

		vendorForm = UIUtils.form(UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor()));
		vendorForm.setWidth("100%");
		vendorForm.setNumCols(3);
		vendorForm.setFields(vendorCombo, emptylabel, contactCombo, emptylabel
		// billToCombo
				);

		forms.add(vendorForm);
		// formItems.add(vendorCombo);
		formItems.add(contactCombo);
		formItems.add(billToCombo);

		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(80);
		phoneSelect.setDisabled(false);
		formItems.add(phoneSelect);

		paymentTermsCombo = new PaymentTermsCombo(Accounter.constants()
				.paymentTerms());
		paymentTermsCombo.setHelpInformation(true);
		// paymentTermsCombo.setWidth(80);
		paymentTermsCombo.setDisabled(isEdit);
		paymentTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						paymentTermSelected(selectItem);

					}

				});

		dueDateItem = new DateField(Accounter.constants().dueDate());
		dueDateItem.setHelpInformation(true);
		dueDateItem.setEnteredDate(getTransactionDate());
		dueDateItem.setColSpan(1);
		dueDateItem.setTitle(Accounter.constants().dueDate());
		dueDateItem.setDisabled(isEdit);

		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);

		DynamicForm termsForm = UIUtils.form(Accounter.constants().terms());
		termsForm.setStyleName(Accounter.constants().venderForm());
		termsForm.setWidth("75%");
		// termsForm.setFields(phoneSelect, paymentTermsCombo);

		DynamicForm dateform = new DynamicForm();
		dateform.setWidth("100%");
		dateform.setNumCols(2);
		dateform.setItems(phoneSelect, paymentTermsCombo, dueDateItem,
				deliveryDateItem);
		dateform.getCellFormatter().setWidth(0, 0, "200px");
		forms.add(termsForm);
		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		balanceDueNonEditableText = new AmountField(Accounter.constants()
				.balanceDue(), this);
		balanceDueNonEditableText.setHelpInformation(true);
		balanceDueNonEditableText.setDisabled(true);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		menuButton = createAddNewButton();
		vendorTransactionGrid = getGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.isEnable = false;
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isEdit);
		vendorTransactionGrid.getElement().getStyle().setMarginTop(10, Unit.PX);
		memoTextAreaItem = createMemoTextAreaItem();
		// memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);

		// addLinksButton = new Button(FinanceApplication.constants()
		// /addLinks());
		// //FIXME--need to disable basing on the mode of the view being opened
		// addLinksButton.setEnabled(isEdit);
		// addLinksButton.setEnabled(true);
		// linksText = new TextItem();
		// linksText.setWidth(100);
		// linksText.setShowTitle(false);
		// linksText.setDisabled(isEdit);
		// formItems.add(linksText);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		// memoForm.setWidget(3, 0, addLinksButton);
		// memoForm.setWidget(3, 1, linksText.getMainWidget());

		forms.add(memoForm);

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setWidth("100%");
		totalForm.setStyleName("invoice-total");
		// netAmount.setWidth((netAmount.getMainWidget().getOffsetWidth() +
		// "102")
		// + "px");

		totalForm.setFields(netAmount, vatTotalNonEditableText,
				transactionTotalNonEditableText);

		if (this.transactionObject != null)
			totalForm.setFields(balanceDueNonEditableText);
		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);
		rightVLay.add(dateform);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "45%");

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");

		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setHorizontalAlignment(ALIGN_RIGHT);
		hpanel.add(createAddNewButton());
		hpanel.getElement().getStyle().setMarginTop(8, Unit.PX);

		menuButton.setType(AccounterButton.ADD_BUTTON);

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		if (getCompany().getAccountingType() == 1) {
			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setHorizontalAlignment(ALIGN_RIGHT);
			vpanel.setWidth("100%");
			vpanel.add(hpanel);
			vpanel.add(totalForm);

			bottomLayout.add(memoForm);
			bottomLayout.add(totalForm);
			bottomLayout.setCellWidth(totalForm, "30%");

			bottompanel.add(vpanel);
			bottompanel.add(bottomLayout);

			// VerticalPanel vPanel = new VerticalPanel();
			// vPanel.add(menuButton);
			// vPanel.add(memoForm);
			// vPanel.setWidth("100%");
			//
			// bottomLayout.add(vPanel);
			// bottomLayout.add(vatCheckform);
			// // bottomLayout.setHorizontalAlignment(align)
			// bottomLayout.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// bottomLayout.add(totalForm);
			// bottomLayout.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			memoForm.setStyleName("align-form");
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("100%");
			vPanel.add(hpanel);
			vPanel.setCellHorizontalAlignment(hpanel, ALIGN_RIGHT);
			vPanel.add(memoForm);

			bottompanel.add(vPanel);
		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.add(lab1);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(labeldateNoLayout);
		mainVLay.setCellHorizontalAlignment(topHLay, ALIGN_RIGHT);
		mainVLay.add(topHLay);
		mainVLay.add(vendorTransactionGrid);

		mainVLay.add(bottompanel);

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
			vendorForm.setWidth("68%");
			termsForm.setWidth("100%");
			dateform.setWidth("100%");
		}

		this.add(mainVLay);
		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);

		listforms.add(dateform);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);

	}

	private void paymentTermSelected(ClientPaymentTerms selectItem) {
		selectedPaymentTerm = selectItem;
		// paymentTermsCombo.setComboItem(selectedPaymentTerm);
		if (transactionObject != null) {
			// setDueDate(((ClientEnterBill) transactionObject).getDueDate());
			setDueDate(Utility.getCalculatedDueDate(getTransactionDate(),
					selectedPaymentTerm).getDate());
		} else {
			setDueDate(Utility.getCalculatedDueDate(getTransactionDate(),
					selectedPaymentTerm).getDate());
		}
	}

	@Override
	public void saveAndUpdateView() {

		ClientEnterBill enterBill = transactionObject != null ? (ClientEnterBill) transactionObject
				: new ClientEnterBill();

		// Setting Vendor
		enterBill.setVendor(getVendor());

		// Setting Contact
		if (contact != null)
			enterBill.setContact(contact);

		// Setting Address
		if (billingAddress != null)
			enterBill.setVendorAddress(billingAddress);

		// Setting Phone
		if (phoneNo != null)
			enterBill.setPhone(phoneNo);

		// Setting Payment Terms
		if (selectedPaymentTerm != null)
			enterBill.setPaymentTerm(selectedPaymentTerm);

		// Setting Due date
		if (dueDateItem.getEnteredDate() != null)
			enterBill.setDueDate((dueDateItem.getEnteredDate()).getDate());

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			enterBill.setDeliveryDate(deliveryDateItem.getEnteredDate());

		// Setting Total
		enterBill.setTotal(vendorTransactionGrid.getTotal());

		// Setting Memo
		enterBill.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// enterBill.setReference(getRefText());

		ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
				transactionDateItem.getEnteredDate(), selectedPaymentTerm);
		enterBill.setDiscountDate(discountDate.getDate());

		if (selectedItemReceipt != 0)
			enterBill.setItemReceipt(selectedItemReceipt);
		if (vatinclusiveCheck != null)
			enterBill.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());

		if (selectedPurchaseOrder != 0)
			enterBill.setPurchaseOrder(selectedPurchaseOrder);
		transactionObject = enterBill;

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			enterBill.setNetAmount(netAmount.getAmount());
		// enterBill.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		super.saveAndUpdateView();

		if (transactionObject.getID() != 0)
			alterObject((ClientEnterBill) transactionObject);

		else
			createObject((ClientEnterBill) transactionObject);

	}

	@Override
	public void updateNonEditableItems() {
		transactionTotalNonEditableText.setAmount(vendorTransactionGrid
				.getTotal());
		netAmount.setAmount(vendorTransactionGrid.getGrandTotal());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			vatTotalNonEditableText.setAmount(vendorTransactionGrid.getTotal()
					- vendorTransactionGrid.getGrandTotal());
		}
	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(true);
		setMemoTextAreaItem(((ClientEnterBill) transactionObject).getMemo());
		// setRefText(((ClientEnterBill) transactionObject).getReference());

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (!AccounterValidator.validateTransactionDate(transactionDate)) {
			result.addError(transactionDate,
					AccounterErrorType.InvalidTransactionDate);
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, AccounterErrorType.InvalidDate);
		}
		result.add(vendorForm.validate());

		if (!AccounterValidator.validate_dueOrDelivaryDates(
				dueDateItem.getEnteredDate(), this.transactionDate)) {
			result.addError(dueDateItem, Accounter.constants().the()
					+ " "
					+ Accounter.constants().dueDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());
		}
		if (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
			result.addError(vendorTransactionGrid,
					AccounterErrorType.blankTransaction);
		}
		result.add(vendorTransactionGrid.validateGrid());
		return result;
	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	// super.setViewConfiguration(viewConfiguration);
	//
	// if (isEdit && (!transactionObject.isEnterBill()))
	// throw new Exception("Unable to load the Required EnterBill....");
	//
	// if (viewConfiguration.isInitWithPayee()) {
	// ClientPayee payee = viewConfiguration.getPayeeObject();
	//
	// if (payee == null || (!payee.isVendor()))
	// throw new Exception("Required Vendor Could Not be Loaded...");
	// }
	//
	// }

	public static VendorBillView getInstance() {
		return new VendorBillView();

	}

	protected void getPurchaseOrdersAndItemReceipt() {

		if (this.rpcUtilService == null)
			return;
		if (getVendor() == null) {
			Accounter.showError(Accounter.constants().pleaseSelectTheVendor());
		} else {

			if (dialog != null && dialog.preVendor != null
					&& dialog.preVendor == this.getVendor()) {
				return;
			}
			AccounterAsyncCallback<List<PurchaseOrdersAndItemReceiptsList>> callback = new AccounterAsyncCallback<List<PurchaseOrdersAndItemReceiptsList>>() {

				@Override
				public void onException(AccounterException caught) {
					// Accounter.showError(FinanceApplication.constants()
					// .noPurchaseOrderAndItemReceiptForVendor()
					// + vendor.getName());
					return;

				}

				@Override
				public void onSuccess(
						List<PurchaseOrdersAndItemReceiptsList> result) {
					if (result == null)
						onFailure(new Exception());

					if (result.size() > 0) {
						showPurchaseDialog(result);
					} else {
						onFailure(new Exception());
					}

				}
			};

			this.rpcUtilService.getPurchasesAndItemReceiptsList(getVendor()
					.getID(), callback);
		}

		// if (vendor == null)
		// Accounter.showError("Please Select the Vendor");
		// else
		// new VendorBillListDialog(this).show();

	}

	protected void showPurchaseDialog(
			List<PurchaseOrdersAndItemReceiptsList> result) {
		if (result == null)
			return;
		List<PurchaseOrdersAndItemReceiptsList> filteredList = new ArrayList<PurchaseOrdersAndItemReceiptsList>();
		filteredList.addAll(result);

		for (PurchaseOrdersAndItemReceiptsList record : result) {
			for (ClientTransaction transaction : selectedOrdersAndItemReceipts) {
				if (transaction.getID() == record.getTransactionId())
					filteredList.remove(record);
			}
		}
		if (filteredList.size() > 0) {
			if (dialog != null) {
				dialog.setQuoteList(filteredList);
			} else
				dialog = new VendorBillListDialog(this, filteredList);

			dialog.show();
		}

	}

	public void selectedPurchaseOrder(ClientPurchaseOrder purchaseOrder) {
		if (purchaseOrder == null)
			return;
		for (ClientTransactionItem record : this.vendorTransactionGrid
				.getRecords()) {
			for (ClientTransactionItem salesRecord : purchaseOrder
					.getTransactionItems())
				if (record.getReferringTransactionItem() == salesRecord.getID())
					vendorTransactionGrid.deleteRecord(record);

		}
		// if (dialog.preCustomer == null || dialog.preCustomer !=
		// this.customer) {
		// dialog.preCustomer = this.customer;
		// } else {
		// return;
		// }

		if (selectedOrdersAndItemReceipts != null)
			selectedOrdersAndItemReceipts.add(purchaseOrder);

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		selectedOrdersAndItemReceipts.add(purchaseOrder);

		for (ClientTransactionItem item : purchaseOrder.getTransactionItems()) {
			if (item.getLineTotal() - item.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			clientItem.setType(item.getType());
			clientItem.setDescription(item.getDescription());
			clientItem.setTaxCode(item.getTaxCode());
			clientItem.setReferringTransactionItem(item.getID());
			clientItem.setAccount(item.getAccount());
			clientItem.setItem(item.getItem());
			clientItem.setQuantity(item.getQuantity());
			clientItem.setUnitPrice(item.getUnitPrice());
			clientItem.setDiscount(item.getDiscount());
			clientItem.setLineTotal(item.getLineTotal() - item.getInvoiced());
			clientItem.setVATfraction(item.getVATfraction());
			clientItem.setVatItem(item.getVatItem());
			clientItem.setTaxable(item.isTaxable());

			itemsList.add(clientItem);

		}

		selectedPurchaseOrder = purchaseOrder.getID();
		vendorTransactionGrid.isItemRecieptView = true;
		vendorTransactionGrid.setAllTransactions(itemsList);
	}

	public void selectedItemReceipt(ClientItemReceipt itemReceipt) {

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		selectedOrdersAndItemReceipts.add(itemReceipt);

		for (ClientTransactionItem item : itemReceipt.getTransactionItems()) {

			ClientTransactionItem clientItem = new ClientTransactionItem();

			clientItem.setType(item.getType());
			clientItem.setQuantity(item.getQuantity());
			clientItem.setDescription(item.getDescription());
			clientItem.setReferringTransactionItem(item.getID());
			clientItem.setUnitPrice(item.getUnitPrice());
			clientItem.setDiscount(item.getDiscount());
			clientItem.setLineTotal(item.getLineTotal() - item.getInvoiced());
			clientItem.setTaxable(item.isTaxable());
			clientItem.setAccount(item.getAccount());
			clientItem.setItem(item.getItem());
			clientItem.setVatItem(item.getVatItem());
			clientItem.setTaxCode(item.getTaxCode());
			clientItem.setVATfraction(item.getVATfraction());

			itemsList.add(clientItem);

		}

		selectedItemReceipt = itemReceipt.getID();

		vendorTransactionGrid.setAllTransactions(itemsList);
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorCombo.setFocus();
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {

		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.addComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				paymentTermsCombo.addComboItem((ClientPaymentTerms) core);
			break;
		case AccounterCommand.UPDATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.updateComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				paymentTermsCombo.updateComboItem((ClientPaymentTerms) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.removeComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				paymentTermsCombo.removeComboItem((ClientPaymentTerms) core);
			break;
		}
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.id, editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		vendorCombo.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		// purchaseLabel.setDisabled(isEdit);
		paymentTermsCombo.setDisabled(isEdit);
		dueDateItem.setDisabled(isEdit);
		deliveryDateItem.setDisabled(isEdit);
		vendorTransactionGrid.setDisabled(isEdit);
		vendorTransactionGrid.setCanEdit(true);
		balanceDueNonEditableText.setDisabled(true);
		memoTextAreaItem.setDisabled(isEdit);

		super.onEdit();
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return null;
		// return this.total.getAmount();
	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
		phoneSelect.setWidth("210px");
		paymentTermsCombo.setWidth("210px");
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().enterBills();
	}
}