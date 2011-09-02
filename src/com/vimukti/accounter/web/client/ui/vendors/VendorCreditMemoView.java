package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorTransactionGrid;

public class VendorCreditMemoView extends
		AbstractVendorTransactionView<ClientVendorCreditMemo> {
	private DynamicForm vendorForm;
	private ArrayList<DynamicForm> listforms;
	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();
	private VendorTransactionGrid vendorTransactionGrid;
	private boolean locationTrackingEnabled;

	private VendorCreditMemoView() {
		super(ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {

		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientVendorCreditMemo ent = (ClientVendorCreditMemo) this.transaction;

			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorTransactionGrid.removeAllRecords();
				this.vendorTransactionGrid
						.setRecords(ent.getTransactionItems());
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
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
	public void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientVendorCreditMemo());
		} else {

			super.vendorSelected(getCompany()
					.getVendor(transaction.getVendor()));
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			transactionNumber.setValue(transaction.getNumber());
			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				netAmount.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
			}
			transactionTotalNonEditableText.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}
			vendorTransactionGrid.setCanEdit(false);
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		super.initTransactionViewData();

	}

	public void resetElements() {
		this.setVendor(null);
		this.contact = null;
		this.phoneNo = null;
		setMemoTextAreaItem("");
		// setRefText("");

	}

	@Override
	public void createControls() {

		Label lab1 = new Label(messages.vendorCredit(Global.get().Vendor())
				+ "(" + getTransactionStatus() + ")");

		lab1.setStyleName(Accounter.constants().labelTitle());
		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab1 = new Label(messages.vendorCredit(Global.get().Vendor()));

		else
			lab1 = new Label(messages.vendorCredit(Global.get().Vendor()) + "("
					+ getTransactionStatus() + ")");

		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("50px");
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(Accounter.constants().creditNoteNo());

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		if (locationTrackingEnabled)
			dateNoForm.setFields(locationCombo);
		VerticalPanel datepanel = new VerticalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		vendorCombo = createVendorComboItem(messages.vendorName(Global.get()
				.Vendor()));

		contactCombo = createContactComboItem();

		// if (this.isEdit)
		// FIXME--need to disable the form
		// vendorForm.setDisabled(true);

		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);

		DynamicForm phoneForm = UIUtils.form(Accounter.constants()
				.phoneNumber());
		phoneForm.setFields(phoneSelect);
		if (this.isInViewMode()) {
			// FiXME--The form need to be disabled
			// phoneForm.setDisabled(true);
		}

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		Label lab2 = new Label(Accounter.constants().itemsAndExpenses());
		menuButton = createAddNewButton();
		vendorTransactionGrid = new VendorTransactionGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.isEnable = false;
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isInViewMode());
		vendorTransactionGrid.getElement().getStyle().setMarginTop(10, Unit.PX);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");

		vendorForm = UIUtils.form(Global.get().vendor());
		vendorForm.setWidth("50%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect);
		vendorForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "190px");

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

		int accountType = getCompany().getAccountingType();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {

			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("100%");
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.add(panel);
			vPanel.add(totalForm);

			bottomLayout1.add(memoForm);
			bottomLayout1.add(totalForm);
			bottomLayout1.setCellWidth(totalForm, "30%");

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
			vPanel.setWidth("100%");
			vPanel.add(panel);
			vPanel.setCellHorizontalAlignment(panel, ALIGN_RIGHT);
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

		this.add(mainVLay);

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

		if (this.isInViewMode()) {

			ClientVendorCreditMemo vendorCreditMemo = (ClientVendorCreditMemo) transaction;

			if (vendorCreditMemo != null) {
				memoTextAreaItem.setDisabled(true);
				setMemoTextAreaItem(vendorCreditMemo.getMemo());
				// setRefText(vendorCreditMemo.getReference());

			}
		}
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();

		super.saveAndUpdateView();

		saveOrUpdate(transaction);

	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Vendor
		transaction.setVendor(getVendor().getID());

		// Setting Contact
		if (contact != null)
			transaction.setContact(contact);

		// Setting Phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting Total
		transaction.setTotal(vendorTransactionGrid.getTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// transaction.setReference(getRefText());

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			transaction.setNetAmount(netAmount.getAmount());
		// itemReceipt.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		if (vatinclusiveCheck != null)
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. is Valid transaction date?
		// 2. is in prevent posting before date?
		// 3. vendorForm valid?
		// 4. isBlank transaction?
		// 5. is vendor transaction grid valid?
		if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateTransactionDate());
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}
		result.add(vendorForm.validate());
		if (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
			result.addError(vendorTransactionGrid,
					accounterConstants.blankTransaction());
		} else
			result.add(vendorTransactionGrid.validateGrid());
		return result;
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
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vendorCombo.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		vendorTransactionGrid.setDisabled(isInViewMode());
		vendorTransactionGrid.setCanEdit(true);
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());

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
		return this.transactionTotalNonEditableText.getAmount();
	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		vendorForm.setWidth("40%");
	}

	@Override
	protected String getViewTitle() {
		return messages.vendorCredit(Global.get().Vendor());
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractTransactionGrid<ClientTransactionItem> getTransactionGrid() {
		return vendorTransactionGrid;
	}
}
