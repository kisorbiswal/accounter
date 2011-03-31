package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class VendorCreditMemoView extends
		AbstractVendorTransactionView<ClientVendorCreditMemo> {
	private DynamicForm vendorForm;
	private ArrayList<DynamicForm> listforms;

	private VendorCreditMemoView() {
		super(ClientTransaction.TYPE_VENDOR_CREDIT_MEMO,
				VENDOR_TRANSACTION_GRID);
		this.validationCount = 4;

	}

	@Override
	protected void initTransactionViewData() {
		// if (transactionObject == null)
		// resetElements();
		super.initTransactionViewData();
		// initTransactionNumber();

	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (this.vendor != null && this.vendor != vendor) {
			ClientVendorCreditMemo ent = (ClientVendorCreditMemo) this.transactionObject;

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
		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		super.vendorSelected(vendor);
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setVendorTaxcodeToAccount();

	}

	@Override
	public void initTransactionViewData(ClientTransaction transactionObject) {

		ClientVendorCreditMemo vendorCreditMemo = (ClientVendorCreditMemo) transactionObject;
		vendorSelected(FinanceApplication.getCompany().getVendor(
				vendorCreditMemo.getVendor()));
		contactSelected(vendorCreditMemo.getContact());
		phoneSelect.setValue(vendorCreditMemo.getPhone());
		transactionNumber.setValue(vendorCreditMemo.getNumber());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmount.setAmount(vendorCreditMemo.getNetAmount());
			vatTotalNonEditableText.setAmount(vendorCreditMemo.getTotal()
					- vendorCreditMemo.getNetAmount());
		}
		transactionTotalNonEditableText.setAmount(vendorCreditMemo.getTotal());

		if (vatinclusiveCheck != null) {
			setAmountIncludeChkValue(transactionObject.isAmountsIncludeVAT());
		}
		vendorTransactionGrid.setCanEdit(false);
		initMemoAndReference();
		initTransactionViewData();

	}

	public void resetElements() {
		this.vendor = null;
		this.contact = null;
		this.phoneNo = null;
		setMemoTextAreaItem("");
		// setRefText("");

	}

	@Override
	public void createControls() {

		Label lab1 = new Label(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplierCredit(), FinanceApplication
				.getVendorsMessages().vendorCredit())
				+ "(" + getTransactionStatus() + ")");

		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());
		if (transactionObject == null
				|| transactionObject.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab1 = new Label(UIUtils.getVendorString(FinanceApplication
					.getVendorsMessages().supplierCredit(), FinanceApplication
					.getVendorsMessages().vendorCredit()));

		else
			lab1 = new Label(UIUtils.getVendorString(FinanceApplication
					.getVendorsMessages().supplierCredit(), FinanceApplication
					.getVendorsMessages().vendorCredit())
					+ "(" + getTransactionStatus() + ")");

		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());
		lab1.setHeight("50px");
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(FinanceApplication.getVendorsMessages()
				.creditNoteNo());

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		VerticalPanel datepanel = new VerticalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		forms.add(dateNoForm);

		vendorCombo = createVendorComboItem(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierName(),
				FinanceApplication.getVendorsMessages().vendorName()));

		contactCombo = createContactComboItem();

		if (this.transactionObject != null)
			// FIXME--need to disable the form
			// vendorForm.setDisabled(true);

			forms.add(vendorForm);

		phoneSelect = new TextItem(vendorConstants.phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);

		DynamicForm phoneForm = UIUtils.form(vendorConstants.phonenumber());
		phoneForm.setFields(phoneSelect);
		if (this.transactionObject != null) {
			// FiXME--The form need to be disabled
			// phoneForm.setDisabled(true);
		}

		forms.add(phoneForm);
		netAmount = new AmountLabel(FinanceApplication.getVendorsMessages()
				.netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		Label lab2 = new Label(vendorConstants.itemsAndExpenses());
		menuButton = createAddNewButton();
		vendorTransactionGrid = getGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isEdit);
		vendorTransactionGrid.getElement().getStyle().setMarginTop(10, Unit.PX);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");

		vendorForm = UIUtils.form(vendorConstants.supplier());
		vendorForm.setWidth("50%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect);
		vendorForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getCustomersMessages().width(), "190px");

		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);
		vatinclusiveCheck = getVATInclusiveCheckBox();
		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("invoice-total");
		// netAmount.setWidth((netAmount.getMainWidget().getOffsetWidth() + 100)
		// + "px");

		totalForm.setFields(netAmount, vatTotalNonEditableText,
				transactionTotalNonEditableText);
		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");
		leftVLay.add(vendorForm);
		VerticalPanel rightVLay1 = new VerticalPanel();
		rightVLay1.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay1.setWidth("100%");
		HorizontalPanel topHLay1 = new HorizontalPanel();
		topHLay1.setWidth("100%");
		topHLay1.add(leftVLay);
		topHLay1.add(rightVLay);

		HorizontalPanel bottomLayout1 = new HorizontalPanel();
		bottomLayout1.setWidth("100%");

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(createAddNewButton());
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		VerticalPanel bottomPanel = new VerticalPanel();
		bottomPanel.setWidth("100%");

		int accountType = FinanceApplication.getCompany().getAccountingType();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {

			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("100%");
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.add(panel);
			vPanel.add(totalForm);

			bottomLayout1.add(memoForm);
			bottomLayout1.add(totalForm);

			bottomPanel.add(vPanel);
			bottomPanel.add(bottomLayout1);
			//			
			// VerticalPanel vPanel = new VerticalPanel();
			//
			// vPanel.add(menuButton);
			// vPanel.add(memoForm);
			// vPanel.setWidth("100%");
			//
			// bottomLayout1.add(vPanel);
			// bottomLayout1.add(vatCheckform);
			// bottomLayout1.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// bottomLayout1.add(totalForm);
			// bottomLayout1.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
			memoForm.setStyleName("align-form");
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.add(panel);
			vPanel.add(memoForm);
			bottomPanel.add(vPanel);
		}
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay1);
		// mainVLay.add(lab2);

		mainVLay.add(vendorTransactionGrid);

		mainVLay.add(bottomPanel);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		canvas.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(vendorForm);

		listforms.add(memoForm);

		listforms.add(vatCheckform);
		listforms.add(totalForm);

	}

	@Override
	public void initMemoAndReference() {

		if (this.transactionObject != null) {

			ClientVendorCreditMemo vendorCreditMemo = (ClientVendorCreditMemo) transactionObject;

			if (vendorCreditMemo != null) {

				setMemoTextAreaItem(vendorCreditMemo.getMemo());
				// setRefText(vendorCreditMemo.getReference());

			}
		}
	}

	@Override
	public void saveAndUpdateView() throws Exception {

		ClientVendorCreditMemo vendorCreditMemo;
		if (transactionObject != null)
			vendorCreditMemo = (ClientVendorCreditMemo) transactionObject;
		else
			vendorCreditMemo = new ClientVendorCreditMemo();

		// Setting Vendor
		vendorCreditMemo.setVendor(vendor.getStringID());

		// Setting Contact
		if (contact != null)
			vendorCreditMemo.setContact(contact);

		// Setting Phone
		if (phoneSelect.getValue() != null)
			vendorCreditMemo.setPhone(phoneSelect.getValue().toString());

		// Setting Total
		vendorCreditMemo.setTotal(vendorTransactionGrid.getTotal());

		// Setting Memo
		vendorCreditMemo.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// vendorCreditMemo.setReference(getRefText());

		transactionObject = vendorCreditMemo;
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			vendorCreditMemo.setNetAmount(netAmount.getAmount());
		// itemReceipt.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		if (vatinclusiveCheck != null)
			vendorCreditMemo.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		super.saveAndUpdateView();

		if (transactionObject.getStringID() != null
				&& transactionObject.getStringID().length() != 0)
			alterObject(vendorCreditMemo);
		else
			createObject(vendorCreditMemo);

	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		switch (validationCount) {
		case 4:
			return AccounterValidator.validateTransactionDate(transactionDate);
		case 3:
			return AccounterValidator.validateForm(vendorForm);
		case 2:
			return AccounterValidator.isBlankTransaction(vendorTransactionGrid);
		case 1:
			return vendorTransactionGrid.validateGrid();
		default:
			return true;
		}
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

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	//
	// super.setViewConfiguration(viewConfiguration);
	//
	// if (isEdit && (!transactionObject.isVendorCreditMemo()))
	// throw new Exception("Unable to load the Required CashPurchase....");
	//
	// if (viewConfiguration.isInitWithPayee()) {
	// ClientPayee payee = viewConfiguration.getPayeeObject();
	//
	// if (payee == null || (!payee.isVendor()))
	// throw new Exception("Required Vendor Could Not be Loaded...");
	// }
	// }

	public static VendorCreditMemoView getInstance() {

		return new VendorCreditMemoView();

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
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.addComboItem((ClientVendor) core);
			break;
		case AccounterCommand.UPDATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.updateComboItem((ClientVendor) core);
			break;
		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.removeComboItem((ClientVendor) core);
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
		vendorTransactionGrid.setDisabled(isEdit);
		vendorTransactionGrid.setCanEdit(true);

		super.onEdit();
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
		vendorForm.setWidth("40%");
	}
}
