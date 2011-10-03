package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterErrors;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TaxItemCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * modified by Ravi Kiran.G, Murali.A
 * 
 */
public class VendorBillView extends
		AbstractVendorTransactionView<ClientEnterBill> {
	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();
	private PaymentTermsCombo paymentTermsCombo;
	private ClientPaymentTerms selectedPaymentTerm;
	private DateField dueDateItem;

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
	private TaxItemCombo vendorTDSTaxCode;

	private ArrayList<DynamicForm> listforms;
	private ArrayList<ClientTransaction> selectedOrdersAndItemReceipts;
	private boolean locationTrackingEnabled;
	protected VendorAccountTransactionTable vendorAccountTransactionTable;
	protected VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private DynamicForm totalForm = new DynamicForm();

	private VendorBillView() {
		super(ClientTransaction.TYPE_ENTER_BILL);
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
		contactCombo.setDisabled(isInViewMode());
		billToCombo.setDisabled(isInViewMode());
		// phoneSelect.setValueMap();
		setMemoTextAreaItem("");
		// setRefText("");

	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientEnterBill());
		} else {

			paymentTermsCombo.setValue(transaction.getPaymentTerm());
			dueDateItem
					.setValue(new ClientFinanceDate(transaction.getDueDate()));
			deliveryDateItem.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));

			ClientVendor vendor = getCompany().getVendor(
					transaction.getVendor());
			vendorCombo.setValue(vendor);
			billToaddressSelected(transaction.getVendorAddress());
			selectedVendor(vendor);
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			phoneSelect.setDisabled(true);
			transactionNumber.setValue(transaction.getNumber());
			// if (isTrackTax()) {
			// netAmount.setAmount(transaction.getNetAmount());
			// vatTotalNonEditableText.setAmount(transaction.getTotal()
			// - transaction.getNetAmount());
			// }

			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					netAmount.setAmount(transaction.getNetAmount());
					vatTotalNonEditableText
							.setAmount(getAmountInTransactionCurrency(transaction
									.getTotal() - transaction.getNetAmount()));
				} else {
					this.taxCode = getTaxCodeForTransactionItems(transaction
							.getTransactionItems());
					if (taxCode != null) {
						this.taxCodeSelect.setComboItem(taxCode);
					}
				}
			}

			transactionTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(transaction
							.getTotal()));

			balanceDueNonEditableText
					.setAmount(getAmountInTransactionCurrency(transaction
							.getBalanceDue()));

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}
			this.dueDateItem
					.setValue(transaction.getDueDate() != 0 ? new ClientFinanceDate(
							transaction.getDueDate())
							: getTransactionDate());
			initMemoAndReference();
			initAccounterClass();
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));

		super.initTransactionViewData();
		initPaymentTerms();

	}

	private void initBalanceDue() {

		if (isInViewMode()) {

			setBalanceDue(((ClientEnterBill) transaction).getBalanceDue());

		}

	}

	public void setBalanceDue(Double balanceDue) {
		if (balanceDue == null)
			balanceDue = 0.0D;
		this.balanceDue = balanceDue;
		balanceDueNonEditableText
				.setAmount(getAmountInTransactionCurrency(balanceDue));
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	private void initPaymentTerms() {
		paymentTermsList = getCompany().getPaymentsTerms();

		paymentTermsCombo.initCombo(paymentTermsList);
		paymentTermsCombo.setDisabled(isInViewMode());

		if (isInViewMode()
				&& ((ClientEnterBill) transaction).getPaymentTerm() != 0) {
			ClientPaymentTerms paymentTerm = getCompany().getPaymentTerms(
					((ClientEnterBill) transaction).getPaymentTerm());
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

		selectedOrdersAndItemReceipts = new ArrayList<ClientTransaction>();
		if (isInViewMode() && this.selectedPaymentTerm != null)
			paymentTermSelected(selectedPaymentTerm);
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {

		updatePurchaseOrderOrItemReceipt(vendor);

		if (vendor.isTdsApplicable()) {
			vendorTDSTaxCode.setSelected(vendorTDSTaxCode
					.getDisplayName(getCompany().getTAXItem(
							vendor.getTaxItemCode())));
			vendorTDSTaxCode.setVisible(true);
		} else {
			vendorTDSTaxCode.setValue(null);
			vendorTDSTaxCode.setVisible(false);
		}

		super.vendorSelected(vendor);
		if (transaction == null)
			vendorAccountTransactionTable.resetRecords();

		selectedOrdersAndItemReceipts = new ArrayList<ClientTransaction>();
		if (!(isInViewMode() && vendor.getID() == transaction.getVendor()))
			setPaymentTermsCombo(vendor);
		if (transaction.getID() == 0)
			getPurchaseOrdersAndItemReceipt();
		long code = vendor.getTAXCode();
		if (code == 0) {
			code = Accounter.getCompany().getDefaultTaxCode();
		}
		vendorAccountTransactionTable.setTaxCode(code, false);
		vendorItemTransactionTable.setTaxCode(code, false);
		if (vendor.getPhoneNo() != null) {
			phoneSelect.setValue(vendor.getPhoneNo());
		} else {
			phoneSelect.setValue("");
		}
	}

	private void updatePurchaseOrderOrItemReceipt(ClientVendor vendor) {
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientEnterBill ent = (ClientEnterBill) this.transaction;

			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorAccountTransactionTable
						.setRecords(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setRecords(getItemTransactionItems(ent
								.getTransactionItems()));
				selectedPurchaseOrder = ent.getPurchaseOrder();
				selectedItemReceipt = ent.getItemReceipt();
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorAccountTransactionTable.resetRecords();
				this.vendorAccountTransactionTable.updateTotals();
				this.vendorItemTransactionTable.updateTotals();

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
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
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

		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(true);

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
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
		vendorTDSTaxCode = new TaxItemCombo(messages.vendorTDSCode(Global.get()
				.Vendor()), 1);
		vendorTDSTaxCode.setHelpInformation(true);

		vendorCombo = createVendorComboItem(messages.vendorName(Global.get()
				.Vendor()));
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
		if (this.isInViewMode())
			billToCombo.setDisabled(true);

		vendorForm = UIUtils.form(Global.get().Vendor());
		vendorForm.setWidth("100%");
		vendorForm.setNumCols(3);
		if (getCompany().getPreferences().isTDSEnabled()) {
			vendorForm.setFields(vendorCombo, emptylabel, contactCombo,
					emptylabel, vendorTDSTaxCode);
		} else {
			vendorForm.setFields(vendorCombo, emptylabel, contactCombo,
					emptylabel);
		}

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			vendorForm.setFields(classListCombo);
		}

		vendorTDSTaxCode.setVisible(false);
		// formItems.add(vendorCombo);
		// formItems.add(contactCombo);
		// formItems.add(billToCombo);

		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(80);
		phoneSelect.setDisabled(false);
		// formItems.add(phoneSelect);

		dueDateItem = new DateField(Accounter.constants().dueDate());
		dueDateItem.setToolTip(Accounter.messages().selectDateUntilDue(
				this.getAction().getViewName()));
		dueDateItem.setHelpInformation(true);
		dueDateItem.setEnteredDate(getTransactionDate());
		dueDateItem.setColSpan(1);
		dueDateItem.setTitle(Accounter.constants().dueDate());
		dueDateItem.setDisabled(isInViewMode());

		paymentTermsCombo = new PaymentTermsCombo(Accounter.constants()
				.paymentTerms());
		paymentTermsCombo.setHelpInformation(true);
		// paymentTermsCombo.setWidth(80);
		paymentTermsCombo.setDisabled(isInViewMode());
		paymentTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {

						paymentTermSelected(selectItem);

					}

				});

		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);
		taxCodeSelect = createTaxCodeSelectItem();

		DynamicForm termsForm = UIUtils.form(Accounter.constants().terms());
		termsForm.setStyleName(Accounter.constants().venderForm());
		termsForm.setWidth("75%");
		// termsForm.setFields(phoneSelect, paymentTermsCombo);

		DynamicForm dateform = new DynamicForm();
		dateform.setWidth("100%");
		dateform.setNumCols(2);
		if (locationTrackingEnabled)
			dateform.setFields(locationCombo);
		dateform.setItems(phoneSelect, paymentTermsCombo, dueDateItem,
				deliveryDateItem);
		dateform.getCellFormatter().setWidth(0, 0, "200px");
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

		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax() && isTrackPaidTax(), isTaxPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorBillView.this.isShowPriceWithVat();
			}
		};

		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorAccountTransactionTable.getElement().getStyle().setMarginTop(10,
				Unit.PX);

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});
		FlowPanel accountFlowPanel = new FlowPanel();
		DisclosurePanel accountsDisclosurePanel = new DisclosurePanel(
				"Itemize by Account");
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");

		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorBillView.this.isShowPriceWithVat();
			}
		};

		vendorItemTransactionTable.setDisabled(isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		FlowPanel itemsFlowPanel = new FlowPanel();
		DisclosurePanel itemsDisclosurePanel = new DisclosurePanel(
				"Itemize by Product/Service");
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");
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

		DynamicForm tdsForm = new DynamicForm();
		tdsForm.setWidth("100%");
		tdsForm.setFields();

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		// memoForm.setWidget(3, 0, addLinksButton);
		// memoForm.setWidget(3, 1, linksText.getMainWidget());

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);

		totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");
		// netAmount.setWidth((netAmount.getMainWidget().getOffsetWidth() +
		// "102")
		// + "px");
		HorizontalPanel taxPanel = new HorizontalPanel();
		taxPanel.setWidth("100%");
		if (isTrackTax() && isTrackPaidTax()) {
			if (!isTaxPerDetailLine()) {
				DynamicForm form = new DynamicForm();
				form.setFields(taxCodeSelect);
				taxPanel.add(form);
				taxPanel.setCellHorizontalAlignment(form,
						HasHorizontalAlignment.ALIGN_LEFT);
			}
			totalForm.setFields(netAmount, vatTotalNonEditableText,
					transactionTotalNonEditableText);

		} else {
			totalForm.setFields(transactionTotalNonEditableText);
		}
		taxPanel.add(totalForm);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(tdsForm);

		if (this.isInViewMode())
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

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");
		//

		// if (isTrackTax()) {

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setWidth("100%");
		verticalPanel.setHorizontalAlignment(ALIGN_RIGHT);
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setHorizontalAlignment(ALIGN_RIGHT);
		// vpanel.setWidth("100%");

		// vpanel.add(hpanel);
		bottomLayout.add(memoForm);
		bottomLayout.add(taxPanel);
		bottomLayout.setCellWidth(totalForm, "30%");
		bottompanel.add(vpanel);
		bottompanel.add(verticalPanel);

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
		// } else if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_INDIA) {
		// bottomLayout.add(horizontalPanel);
		// bottomLayout.add(totalForm);
		// bottomLayout.setCellWidth(totalForm, "30%");
		//
		// memoForm.setStyleName("align-form");
		// VerticalPanel vPanel = new VerticalPanel();
		// vPanel.add(hpanel);
		// vPanel.setWidth("100%");
		//
		// vPanel.setCellHorizontalAlignment(hpanel, ALIGN_RIGHT);
		// vPanel.add(horizontalPanel);
		// vPanel.add(memoForm);
		//
		// bottompanel.add(vPanel);
		// bottompanel.add(bottomLayout);

		// } else {
		// memoForm.setStyleName("align-form");
		// VerticalPanel vPanel = new VerticalPanel();
		// vPanel.setWidth("100%");
		//
		// vPanel.add(memoForm);
		//
		// bottompanel.add(vPanel);
		// }

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.add(lab1);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(labeldateNoLayout);
		mainVLay.setCellHorizontalAlignment(topHLay, ALIGN_RIGHT);
		mainVLay.add(topHLay);
		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		// mainVLay.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
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

		settabIndexes();
	}

	private void paymentTermSelected(ClientPaymentTerms selectItem) {
		if (selectItem == null) {
			return;
		}
		selectedPaymentTerm = selectItem;

		// paymentTermsCombo.setComboItem(selectedPaymentTerm);
		// if (isInViewMode()) {
		// // setDueDate(((ClientEnterBill) transactionObject).getDueDate());
		// setDueDate(Utility.getCalculatedDueDate(getTransactionDate(),
		// selectedPaymentTerm).getDate());
		// } else {
		// setDueDate(Utility.getCalculatedDueDate(getTransactionDate(),
		// selectedPaymentTerm).getDate());
		// }

		setDueDate(Utility.getPaymentTermsDate(selectedPaymentTerm).getDate());
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();

		super.saveAndUpdateView();

		saveOrUpdate((ClientEnterBill) transaction);

	}

	protected void updateTransaction() {
		if (transaction == null)
			return;
		super.updateTransaction();
		// Setting Vendor
		if (getVendor() != null)
			transaction.setVendor(getVendor());

		// Setting Contact
		if (contact != null)
			transaction.setContact(contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// Setting Phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());
		// else
		// transaction.setPhone(phoneNo);

		// Setting Payment Terms
		if (selectedPaymentTerm != null)
			transaction.setPaymentTerm(selectedPaymentTerm);

		// Setting Due date
		if (dueDateItem.getEnteredDate() != null)
			transaction.setDueDate((dueDateItem.getEnteredDate()).getDate());

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// transaction.setReference(getRefText());

		ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
				transactionDateItem.getEnteredDate(), selectedPaymentTerm);
		transaction.setDiscountDate(discountDate.getDate());

		if (selectedItemReceipt != 0)
			transaction.setItemReceipt(selectedItemReceipt);
		if (vatinclusiveCheck != null)
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());

		if (selectedPurchaseOrder != 0)
			transaction.setPurchaseOrder(selectedPurchaseOrder);

		if (isTrackTax()) {
			transaction.setNetAmount(getAmountInBaseCurrency(netAmount
					.getAmount()));
			if (vatinclusiveCheck != null)
				transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
						.getValue());

		}

		// enterBill.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());
	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		transactionTotalNonEditableText
				.setAmount(getAmountInTransactionCurrency(grandTotal));
		netAmount.setAmount(getAmountInTransactionCurrency(lineTotal));
		if (isTrackTax()) {
			vatTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(grandTotal
							- lineTotal));
		}
	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(true);

		setMemoTextAreaItem(((ClientEnterBill) transaction).getMemo());
		// setRefText(((ClientEnterBill) transactionObject).getReference());

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. is Valid transaction date?
		// 2. is in prevent posting before date?
		// 3. vendorForm valid?
		// 4. is valid due date?
		// 5. isBlank transaction?
		// 6. is vendor transaction grid valid?
		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// accounterConstants.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, accounterConstants
					.invalidateDate());
		}
		result.add(vendorForm.validate());

		if (!AccounterValidator.isValidDueOrDelivaryDates(dueDateItem
				.getEnteredDate(), this.transactionDate)) {
			result.addError(dueDateItem, Accounter.constants().the()
					+ " "
					+ Accounter.constants().dueDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());
		}
		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable, accounterConstants
					.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}
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
			Accounter.showError(Accounter.messages().pleaseSelectTheVendor(
					Global.get().Vendor()));
		} else {

			if (dialog != null && dialog.preVendor != null
					&& dialog.preVendor == this.getVendor()) {
				return;
			}
			AccounterAsyncCallback<ArrayList<PurchaseOrdersAndItemReceiptsList>> callback = new AccounterAsyncCallback<ArrayList<PurchaseOrdersAndItemReceiptsList>>() {

				@Override
				public void onException(AccounterException caught) {
					// Accounter.showError(FinanceApplication.constants()
					// .noPurchaseOrderAndItemReceiptForVendor()
					// + vendor.getName());
					return;

				}

				@Override
				public void onResultSuccess(
						ArrayList<PurchaseOrdersAndItemReceiptsList> result) {
					if (result == null)
						onFailure(new Exception());

					if (result.size() > 0) {
						showPurchaseDialog(result);
					} else {
						onException(new AccounterException());
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
		for (ClientTransactionItem record : this.vendorAccountTransactionTable
				.getRecords()) {
			for (ClientTransactionItem salesRecord : purchaseOrder
					.getTransactionItems())
				if (record.getReferringTransactionItem() == salesRecord.getID())
					vendorAccountTransactionTable.delete(record);

		}
		for (ClientTransactionItem record : this.vendorItemTransactionTable
				.getRecords()) {
			for (ClientTransactionItem salesRecord : purchaseOrder
					.getTransactionItems())
				if (record.getReferringTransactionItem() == salesRecord.getID())
					vendorItemTransactionTable.delete(record);

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
		vendorAccountTransactionTable
				.setAllRows(getAccountTransactionItems(itemsList));
		vendorItemTransactionTable
				.setAllRows(getItemTransactionItems(itemsList));
		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
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

		vendorAccountTransactionTable
				.setAllRows(getAccountTransactionItems(itemsList));
		vendorItemTransactionTable
				.setAllRows(getItemTransactionItems(itemsList));
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

		balanceDueNonEditableText.setVisible(false);
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				String errorString = null;
				if (errorCode == AccounterException.ERROR_CANT_EDIT) {
					AccounterErrors accounterErrors = (AccounterErrors) GWT
							.create(AccounterErrors.class);
					errorString = accounterErrors.billPaidSoYouCantEdit();
				} else {
					errorString = AccounterExceptions.getErrorString(errorCode);
				}
				Accounter.showError(errorString);
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
		phoneSelect.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		// purchaseLabel.setDisabled(isEdit);
		paymentTermsCombo.setDisabled(isInViewMode());
		dueDateItem.setDisabled(isInViewMode());
		deliveryDateItem.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		balanceDueNonEditableText.setDisabled(true);
		memoTextAreaItem.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		vendorTDSTaxCode.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());
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

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
	}

	@Override
	protected void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems) {
		vendorAccountTransactionTable
				.setRecords(getAccountTransactionItems(transactionItems));
		vendorItemTransactionTable
				.setRecords(getItemTransactionItems(transactionItems));
	}

	@Override
	protected void removeAllRecordsFromGrid() {
		vendorAccountTransactionTable.removeAllRecords();
		vendorItemTransactionTable.removeAllRecords();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		transactionDateItem.setTabIndex(3);
		transactionNumber.setTabIndex(4);
		phoneSelect.setTabIndex(5);
		paymentTermsCombo.setTabIndex(6);
		dueDateItem.setTabIndex(7);
		deliveryDateItem.setTabIndex(8);
		memoTextAreaItem.setTabIndex(9);
		// menuButton.setTabIndex(10);
		saveAndCloseButton.setTabIndex(11);
		saveAndNewButton.setTabIndex(12);
		cancelButton.setTabIndex(13);
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
	}
}