package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author vimukti1
 * 
 */
public class CashPurchaseView extends
		AbstractVendorTransactionView<ClientCashPurchase> {

	protected DynamicForm vendorForm;
	protected DynamicForm termsForm;
	public List<String> selectedComboList;
	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;

	protected CashPurchaseView() {
		super(ClientTransaction.TYPE_CASH_PURCHASE, VENDOR_TRANSACTION_GRID);
		this.validationCount = 6;
	}

	protected CashPurchaseView(int type) {
		super(type, VENDOR_TRANSACTION_GRID);
	}

	@Override
	protected void createControls() {

		// setTitle(UIUtils.title(vendorConstants.cashPurchase()));

		titlelabel = new Label(FinanceApplication.getVendorsMessages()
				.cashPurchase());
		titlelabel.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());

		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							deliveryDateItem.setEnteredDate(date);
							setTransactionDate(date);
						}
					}
				});
		transactionDateItem.setWidth(100);

		transactionNumber = createTransactionNumberItem();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.setHorizontalAlignment(ALIGN_RIGHT);
		labeldateNoLayout.setCellHorizontalAlignment(datepanel, ALIGN_RIGHT);
		labeldateNoLayout.add(datepanel);

		if (this.transactionObject != null)
			// FIXME--the form need to be disabled here
			dateNoForm.setDisabled(true);

		forms.add(dateNoForm);
		formItems.add(transactionDateItem);
		formItems.add(transactionNumber);

		vendorCombo = createVendorComboItem(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierName(),
				FinanceApplication.getVendorsMessages().vendorName()));
		vendorCombo.setHelpInformation(true);
		vendorCombo.setWidth(100);
		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		contactCombo.setWidth(100);
		billToCombo = createBillToComboItem();
		billToCombo.setHelpInformation(true);
		billToCombo.setWidth(100);
		phoneSelect = new SelectCombo(vendorConstants.phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		if (transactionObject != null)
			phoneSelect.setDisabled(true);

		formItems.add(phoneSelect);

		vendorForm = UIUtils.form(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor()));

		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect,
				billToCombo);

		forms.add(vendorForm);
		formItems.add(contactCombo);
		formItems.add(billToCombo);

		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				paymentMethodSelected(paymentMethodCombo.getValue().toString());
				if (paymentMethod.equals(FinanceApplication
						.getVendorsMessages().check())
						&& transactionObject != null && payFromAccount != null) {
					ClientCashPurchase cashPurchase = (ClientCashPurchase) transactionObject;
					checkNo.setValue(cashPurchase.getCheckNumber());
				}
			}
		});
		paymentMethodCombo.setWidth(100);
		payFromCombo = createPayFromCombo(vendorConstants.Payfrom());
		payFromCombo.setWidth(100);
		payFromCombo.setPopupWidth("500px");
		checkNo = createCheckNumberItem(FinanceApplication.getCompany()
				.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? vendorConstants
				.chequeNo()
				: vendorConstants.checkno());
		checkNo.setDisabled(true);
		checkNo.setWidth(100);
		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);

		termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setFields(paymentMethodCombo, payFromCombo, checkNo,
				deliveryDateItem);

		forms.add(termsForm);
		formItems.add(checkNo);
		formItems.add(deliveryDateItem);

		Label lab2 = new Label(vendorConstants.itemsAndExpenses());
		menuButton = createAddNewButton();

		netAmount = new AmountLabel(FinanceApplication.getVendorsMessages()
				.netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);
		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		vendorTransactionGrid = getGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isEdit);
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		forms.add(memoForm);
		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("50%");

		totalForm.setFields(netAmount, vatTotalNonEditableText,
				transactionTotalNonEditableText);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.add(menuButton);
			vPanel.add(memoForm);
			vPanel.setWidth("100%");

			bottomLayout.add(vPanel);
			bottomLayout.add(vatCheckform);
			bottomLayout.setCellHorizontalAlignment(vatCheckform,
					HasHorizontalAlignment.ALIGN_RIGHT);
			bottomLayout.add(totalForm);
			bottomLayout.setCellHorizontalAlignment(totalForm,
					HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			memoForm.setStyleName("align-form");
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.add(menuButton);
			vPanel.add(memoForm);

			bottomLayout.add(vPanel);
		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");

		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);

		mainVLay.add(vendorTransactionGrid);

		mainVLay.add(bottomLayout);

		// setOverflow(Overflow.SCROLL);
		canvas.add(mainVLay);
		// addChild(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		initViewType();
	}

	@Override
	protected void accountSelected(ClientAccount account) {

		if (account == null)
			return;
		this.payFromAccount = account;
		payFromCombo.setComboItem(payFromAccount);
		if (account != null
				&& FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
				.equalsIgnoreCase(FinanceApplication.getVendorsMessages()
						.cheque())
				: paymentMethod.equalsIgnoreCase(FinanceApplication
						.getVendorsMessages().check())
						&& transactionObject != null) {
			ClientCashPurchase cashPurchase = (ClientCashPurchase) transactionObject;
			checkNo.setValue(cashPurchase.getCheckNumber());
			// setCheckNumber();
		} else if (account == null) {
			checkNo.setValue("");
		}
	}

	protected void setCheckNumber() {

		rpcUtilService.getNextCheckNumber(payFromAccount.getStringID(),
				new AsyncCallback<Long>() {

					public void onFailure(Throwable t) {
						// //UIUtils.logError(
						// "Failed to get the next check number!!", t);
						checkNo.setValue(FinanceApplication
								.getVendorsMessages().Tobeprinted());
						return;
					}

					public void onSuccess(Long result) {
						if (result == null)
							onFailure(null);

						checkNumber = String.valueOf(result);
						checkNo.setValue(result.toString());
					}

				});

	}

	@Override
	protected void initTransactionViewData() {
		super.initTransactionViewData();
		initTransactionNumber();
		if (transactionObject == null)
			initPayFromAccounts();

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		ClientCashPurchase cashPurchaseToBeEdited = (ClientCashPurchase) transactionObject;
		contactSelected(cashPurchaseToBeEdited.getContact());
		vendorSelected(FinanceApplication.getCompany().getVendor(
				cashPurchaseToBeEdited.getVendor()));
		paymentMethodSelected(cashPurchaseToBeEdited.getPaymentMethod() != null ? cashPurchaseToBeEdited
				.getPaymentMethod()
				: "");
		accountSelected(FinanceApplication.getCompany().getAccount(
				cashPurchaseToBeEdited.getPayFrom()));
		// transactionDateItem.setEnteredDate(cashPurchaseToBeEdited.get)
		initMemoAndReference();
		checkNo.setDisabled(true);
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmount.setAmount(cashPurchaseToBeEdited.getNetAmount());
			vatTotalNonEditableText.setAmount(cashPurchaseToBeEdited.getTotal()
					- cashPurchaseToBeEdited.getNetAmount());
		}
		transactionTotalNonEditableText.setAmount(cashPurchaseToBeEdited
				.getTotal());

		if (vatinclusiveCheck != null) {
			setAmountIncludeChkValue(transactionObject.isAmountsIncludeVAT());
		}
		vendorTransactionGrid.setCanEdit(false);

		initTransactionViewData();

	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (this.vendor != null && this.vendor != vendor) {
			ClientCashPurchase ent = (ClientCashPurchase) this.transactionObject;

			if (ent != null && ent.getVendor().equals(vendor.getStringID())) {
				this.vendorTransactionGrid.removeAllRecords();
				this.vendorTransactionGrid
						.setRecords(ent.getTransactionItems());
			} else if (ent != null
					&& !ent.getVendor().equals(vendor.getStringID())) {
				this.vendorTransactionGrid.removeAllRecords();
				this.vendorTransactionGrid.updateTotals();
			}
		}
		super.vendorSelected(vendor);
		if (vendor == null)
			return;
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setVendorTaxcodeToAccount();

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		super.paymentMethodSelected(paymentMethod);
		setDisableStateForCheckNo(paymentMethod);
		paymentMethodCombo.setValue(paymentMethod);
	}

	private void setDisableStateForCheckNo(String paymentMethod) {

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
				.equalsIgnoreCase(FinanceApplication.getVendorsMessages()
						.cheque())
				: paymentMethod.equalsIgnoreCase(FinanceApplication
						.getVendorsMessages().check())) {
			checkNo.setDisabled(false);
		} else {
			checkNo.setValue("");
			checkNo.setDisabled(true);

		}
		if (transactionObject != null) {
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
					.equalsIgnoreCase(FinanceApplication.getVendorsMessages()
							.cheque())
					: paymentMethod.equalsIgnoreCase(FinanceApplication
							.getVendorsMessages().check())) {
				checkNo.setDisabled(false);
			} else {
				checkNo.setDisabled(true);
			}
		}

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		ClientCashPurchase cashPurchase = prepareObject();
		transactionObject = cashPurchase;

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			cashPurchase.setNetAmount(netAmount.getAmount());
			// if (vatinclusiveCheck != null)
			// cashPurchase.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
			// .getValue());
		}

		super.saveAndUpdateView();

		createAlterObject();
	}

	protected ClientCashPurchase prepareObject() {
		ClientCashPurchase cashPurchase = transactionObject != null ? (ClientCashPurchase) transactionObject
				: new ClientCashPurchase();

		// Setting Type
		cashPurchase.setType(ClientTransaction.TYPE_CASH_PURCHASE);

		// Setting Vendor
		cashPurchase.setVendor(this.vendor.getStringID());

		// Setting Contact
		if (contact != null)
			cashPurchase.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			cashPurchase.setVendorAddress((billingAddress));

		// Setting Phone
		if (phoneNo != null)
			cashPurchase.setPhone(phoneNo);

		// Setting Payment Methods
		cashPurchase.setPaymentMethod(paymentMethod);

		// Setting Pay From Account
		cashPurchase.setPayFrom(payFromAccount.getStringID());

		// Setting Check number
		cashPurchase.setCheckNumber(checkNo.getValue().toString());
		// cashPurchase
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			cashPurchase.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getTime());

		// Setting Total
		cashPurchase.setTotal(vendorTransactionGrid.getTotal());

		// Setting Memo
		cashPurchase.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// cashPurchase.setReference(getRefText());
		return cashPurchase;
	}

	public void createAlterObject() {

		if (transactionObject.getStringID() != null
				&& transactionObject.getStringID().length() != 0)
			alterObject((ClientCashPurchase) transactionObject);

		else
			createObject((ClientCashPurchase) transactionObject);

	}

	@Override
	protected void initMemoAndReference() {
		setMemoTextAreaItem(((ClientCashPurchase) transactionObject).getMemo());
		// setRefText(((ClientCashPurchase) transactionObject).getReference());
	}

	@Override
	public void updateNonEditableItems() {
		netAmount.setAmount(vendorTransactionGrid.getGrandTotal());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			vatTotalNonEditableText.setAmount(vendorTransactionGrid.getTotal()
					- vendorTransactionGrid.getGrandTotal());
		}
		transactionTotalNonEditableText.setAmount(vendorTransactionGrid
				.getTotal());
	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {

		switch (this.validationCount) {
		case 6:
			return AccounterValidator.validateTransactionDate(transactionDate);
		case 5:
			return AccounterValidator.validateFormItem(payFromCombo);
		case 4:
			return AccounterValidator.validateForm(vendorForm);
		case 3:
			return AccounterValidator.validate_dueOrDelivaryDates(
					deliveryDateItem.getEnteredDate(), this.transactionDate,
					FinanceApplication.getVendorsMessages().deliverydate());
		case 2:
			return AccounterValidator.isBlankTransaction(vendorTransactionGrid);
		case 1:
			return vendorTransactionGrid.validateGrid();
		default:
			return true;
		}
	}

	public static CashPurchaseView getInstance() {
		return new CashPurchaseView();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	protected void onLoad() {
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR) {

				vendorCombo.addComboItem((ClientVendor) core);
			}
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				payFromCombo.addComboItem((ClientAccount) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR) {
				vendorCombo.updateComboItem((ClientVendor) core);
			}

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				payFromCombo.updateComboItem((ClientAccount) core);
			break;
		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.removeComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				payFromCombo.removeComboItem((ClientAccount) core);
			break;
		}
	}

	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError(((InvalidOperationException) (caught))
						.getDetailedMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.stringID,
				editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		vendorCombo.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		if (paymentMethod.equals(FinanceApplication.getVendorsMessages()
				.check())
				|| paymentMethod.equals(FinanceApplication.getVendorsMessages()
						.cheque())) {
			checkNo.setDisabled(isEdit);
		} else {
			checkNo.setDisabled(!isEdit);
		}
		deliveryDateItem.setDisabled(isEdit);
		vendorTransactionGrid.setDisabled(isEdit);
		vendorTransactionGrid.setCanEdit(true);

		super.onEdit();
	}

	protected void initViewType() {

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Double getTransactionTotal() {
		return this.transactionTotalNonEditableText.getAmount();
	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		vendorForm.setWidth("75%");
		// refText.setWidth("200px");

	}
}
