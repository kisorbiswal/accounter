package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.PurchaseOrderUKGrid;
import com.vimukti.accounter.web.client.ui.grids.PurchaseOrderUSGrid;

public class PurchaseOrderView extends
		AbstractVendorTransactionView<ClientPurchaseOrder> {

	private PaymentTermsCombo payTermsSelect;
	private ShippingTermsCombo shippingTermsCombo;
	private ShippingMethodsCombo shippingMethodsCombo;
	private AddressCombo shipToCombo;
	private AccounterButton addLinksButton;
	private TextItem linksText;
	private DynamicForm vendorForm;
	private DynamicForm termsForm;
	private ClientAddress shippingAddress;
	protected ClientAddress vendorAddress;
	protected ClientPaymentTerms paymentTerms;
	protected ClientShippingTerms shippingTerms;
	protected ClientShippingMethod shippingMethod;
	private TextAreaItem billtoAreaItem;
	private ShipToForm shipToAddress;
	private DateField dueDateItem;

	private long dueDate;
	private long despatchDate;

	private long deliveryDate;
	DynamicForm amountsForm;

	private ArrayList<DynamicForm> listforms;
	private TextItem purchaseOrderText;
	private HTML lab1;
	private List<String> listOfTypes;
	private String OPEN = Accounter.constants().open();
	private String COMPLETED = Accounter.constants().completed();
	private String CANCELLED = Accounter.constants().cancelled();
	private DateField despatchDateItem;

	public PurchaseOrderView() {
		super(ClientTransaction.TYPE_PURCHASE_ORDER, VENDOR_TRANSACTION_GRID);
		validationCount = 5;
	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .purchaseOrder()));
		lab1 = new HTML(Accounter.constants().purchaseOrder());
		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");

		statusSelect = new SelectCombo(Accounter.constants().status());
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(OPEN);
		listOfTypes.add(COMPLETED);
		listOfTypes.add(CANCELLED);
		statusSelect.initCombo(listOfTypes);
		statusSelect.setComboItem(OPEN);
		statusSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (statusSelect.getSelectedValue() != null)
							statusSelect.setComboItem(selectItem);

					}
				});
		statusSelect.setRequired(true);
		statusSelect.setDisabled(isEdit);
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(Accounter.constants().orderNo());
		transactionNumber.setWidth(50);

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.addStyleName("date-number");
		dateNoForm.setFields(statusSelect, transactionDateItem);
		forms.add(dateNoForm);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("98%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);
		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);
		amountsForm = new DynamicForm();
		amountsForm.setWidth("100%");

		netAmount = createNetAmountLabel();

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabelforPurchase();

		vatTotalNonEditableText = createVATTotalNonEditableLabelforPurchase();

		// vendorCombo =
		// createVendorComboItem(Accounter.constants().vendorName());

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");
		prodAndServiceHLay.add(amountsForm);
		prodAndServiceHLay.setCellHorizontalAlignment(amountsForm, ALIGN_RIGHT);

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {

			DynamicForm priceLevelForm = new DynamicForm();
			// priceLevelForm.setCellSpacing(4);
			// priceLevelForm.setWidth("70%");
			// priceLevelForm.setFields(priceLevelSelect);
			amountsForm.setFields(netAmount, vatTotalNonEditableText,
					transactionTotalNonEditableText);
			amountsForm.setStyleName("invoice-total");
			// forms.add(priceLevelForm);
			forms.add(amountsForm);
			// prodAndServiceHLay.add(priceLevelForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(priceLevelForm,
			// ALIGN_RIGHT);
			// prodAndServiceHLay.add(amountsForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
			// ALIGN_RIGHT);
			// listforms.add(priceLevelForm);

		} else {

			// prodAndServiceForm2.setFields(salesTaxTextNonEditable,
			// transactionTotalNonEditableText, paymentsNonEditableText,
			// balanceDueNonEditableText, taxCodeSelect, priceLevelSelect);
			amountsForm.setNumCols(4);
			amountsForm.addStyleName("tax-form");
			amountsForm.setFields(taxCodeSelect, salesTaxTextNonEditable,
					disabletextbox, transactionTotalNonEditableText,
					disabletextbox, paymentsNonEditableText, disabletextbox,
					balanceDueNonEditableText);

			prodAndServiceHLay.add(amountsForm);
			prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
					ALIGN_RIGHT);
		}

		vendorCombo = new VendorCombo(UIUtils.getVendorString(Accounter
				.constants().supplier(), Accounter.constants().vendor()), true);
		vendorCombo.setRequired(true);
		vendorCombo.setHelpInformation(true);

		vendorCombo.setDisabled(isEdit);
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);

					}

				});
		// vendorCombo.setWidth(100);
		contactCombo = createContactComboItem();
		contactCombo.setTitle(Accounter.constants().contact());
		// contactCombo.setWidth(100);
		// billToCombo = createVendorAddressComboItem();
		// billToCombo.setTitle(FinanceApplication.constants().billTo());
		billtoAreaItem = new TextAreaItem(Accounter.constants().billTo());
		billtoAreaItem.setWidth("100%");
		billtoAreaItem.setDisabled(true);
		// shipToCombo = createShipToComboItem();
		shipToAddress = new ShipToForm(null);
		shipToAddress.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		shipToAddress.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "40px");
		shipToAddress.getCellFormatter().addStyleName(0, 1, "memoFormAlign");
		shipToAddress.businessSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						shippingAddress = shipToAddress.getAddress();
						if (shippingAddress != null)
							shipToAddress.setAddres(shippingAddress);
						else
							shipToAddress.addrArea.setValue("");
					}
				});

		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setDisabled(false);

		formItems.add(phoneSelect);

		vendorForm = UIUtils.form(UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor()));
		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect,
				billtoAreaItem);
		vendorForm.getCellFormatter().setWidth(0, 0, "226px");
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");

		forms.add(vendorForm);
		formItems.add(contactCombo);
		formItems.add(billToCombo);

		purchaseOrderText = new TextItem(UIUtils.getVendorString(Accounter
				.constants().supplierOrderNo(), Accounter.constants()
				.vendorOrderNo()));
		purchaseOrderText.setWidth(50);
		purchaseOrderText.setColSpan(1);
		purchaseOrderText.setDisabled(isEdit);

		payTermsSelect = createPaymentTermsSelectItem();

		shippingTermsCombo = createShippingTermsCombo();

		shippingMethodsCombo = createShippingMethodCombo();

		dueDateItem = new DateField(Accounter.constants().dueDate());
		dueDateItem.setDisabled(isEdit);
		// dueDateItem.setWidth(100);
		if (transactionObject != null) {
			// setDueDate(((ClientEnterBill) transactionObject).getDueDate());
		} else
			setDueDate(new ClientFinanceDate().getDate());
		dueDateItem.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {
					ClientFinanceDate newDate = ((DateField) event.getSource())
							.getValue();
					setDueDate(newDate.getDate());
				} catch (Exception e) {
					Accounter.showError(Accounter.constants().invalidDueDate());
				}

			}

		});
		despatchDateItem = new DateField(Accounter.constants().despatchDate());
		despatchDateItem.setDisabled(isEdit);
		if (transactionObject != null) {
		} else
			setDespatchDate(new ClientFinanceDate().getDate());
		despatchDateItem.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {
					ClientFinanceDate newDate = ((DateField) event.getSource())
							.getValue();
					setDespatchDate(newDate.getDate());
				} catch (Exception e) {
					Accounter.showError(Accounter.constants()
							.invalidDispatchDate());
				}

			}

		});

		deliveryDateItem = createTransactionDeliveryDateItem();
		deliveryDateItem.setTitle(Accounter.constants().receivedDate());

		DynamicForm dateform = new DynamicForm();
		dateform.setWidth("100%");
		dateform.setNumCols(2);
		dateform.setItems(dueDateItem, despatchDateItem, deliveryDateItem);

		termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setFields(transactionNumber, purchaseOrderText,
				payTermsSelect, shippingTermsCombo, shippingMethodsCombo);
		termsForm.getCellFormatter().setWidth(0, 0, "230px");
		dateform.getCellFormatter().setWidth(0, 0, "230px");

		forms.add(termsForm);
		formItems.add(checkNo);
		formItems.add(dueDateItem);
		formItems.add(despatchDateItem);
		formItems.add(deliveryDateItem);

		// Label lab2 = new Label(Accounter.constants().itemsAndExpenses());
		vendorTransactionGrid = getGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.isEnable = false;
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isEdit);
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);
		addLinksButton = new AccounterButton(Accounter.constants().addLinks());
		// FIXME--need to disable basing on the mode of the view being opened

		// addLinksButton.setEnabled(true);
		linksText = new TextItem();
		linksText.setWidth(100);
		linksText.setShowTitle(false);
		linksText.setDisabled(isEdit);
		formItems.add(linksText);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("80%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		forms.add(memoForm);
		DynamicForm linksform = new DynamicForm();
		linksform.setWidth("100%");
		linksform.setItems(linksText);
		HorizontalPanel linkspanel = new HorizontalPanel();
		linkspanel.setWidth("70%");
		linkspanel.add(addLinksButton);
		addLinksButton.setEnabled(isEdit);
		linkspanel.add(linksform);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);
		leftVLay.add(shipToAddress);

		VerticalPanel rightVLay = new VerticalPanel();
		// rightVLay.setWidth("93%");
		rightVLay.add(termsForm);
		rightVLay.add(dateform);
		// rightVLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		// rightVLay.setCellHorizontalAlignment(dateform, ALIGN_RIGHT);

		HorizontalPanel topHLay = new HorizontalPanel();
		// topHLay.setStyleName("toplayout");
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "52%");
		topHLay.setCellWidth(rightVLay, "47%");
		// topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		VerticalPanel panel = new VerticalPanel();
		panel.add(createAddNewButton());
		panel.add(amountsForm);

		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");
		bottomLayout.setHorizontalAlignment(ALIGN_RIGHT);

		menuButton.setType(AccounterButton.ADD_BUTTON);

		bottomLayout.add(memoForm);
		bottomLayout.add(panel);
		bottomLayout.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		// bottomLayout.add(linkspanel);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);

		mainVLay.add(vendorTransactionGrid);
		// mainVLay.add(menuButton);
		mainVLay.add(bottomLayout);

		if (UIUtils.isMSIEBrowser()) {
			vendorForm.getCellFormatter().setWidth(0, 1, "200px");
			vendorForm.setWidth("75%");
			termsForm.getCellFormatter().setWidth(0, 1, "68%");
			memoForm.getCellFormatter().setWidth(0, 1, "300px");
			memoForm.setWidth("40%");
			statusSelect.setWidth("150px");
		}
		// setOverflow(Overflow.SCROLL);
		canvas.add(mainVLay);
		// addChild(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(dateform);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(linksform);

	}

	public AbstractTransactionGrid<ClientTransactionItem> getGrid() {

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			return new PurchaseOrderUSGrid();
		else
			return new PurchaseOrderUKGrid();

	}

	private PaymentTermsCombo createPaymentTermsSelectItem() {

		PaymentTermsCombo comboItem = new PaymentTermsCombo(Accounter
				.constants().paymentTerms());

		comboItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						if (selectItem != null) {
							paymentTerms = selectItem;
							paymentTermsSelected(paymentTerms);
						}

					}

				});
		comboItem.setDisabled(isEdit);
		// comboItem.setShowDisabled(false);
		//
		return comboItem;
	}

	private ShippingTermsCombo createShippingTermsCombo() {

		ShippingTermsCombo shippingTermsCombo = new ShippingTermsCombo(
				Accounter.constants().shippingTerms());

		shippingTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {

					public void selectedComboBoxItem(
							ClientShippingTerms selectItem) {
						if (selectItem != null)
							shippingTerms = selectItem;
					}

				});

		shippingTermsCombo.setDisabled(isEdit);

		formItems.add(shippingTermsCombo);

		return shippingTermsCombo;
	}

	protected ShippingMethodsCombo createShippingMethodCombo() {

		ShippingMethodsCombo shippingMethodsCombo = new ShippingMethodsCombo(
				Accounter.constants().shippingMethod());

		shippingMethodsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {

					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						if (selectItem != null)
							shippingMethod = selectItem;
					}

				});

		shippingMethodsCombo.setDisabled(isEdit);

		formItems.add(shippingMethodsCombo);

		return shippingMethodsCombo;

	}

	protected AddressCombo createShipToComboItem() {

		AddressCombo shipToCombo = new AddressCombo(Accounter.constants()
				.shipTo());

		shipToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {
						shipToAddressSelected(selectItem);
					}

				});

		shipToCombo.setDisabled(isEdit);
		// shipToCombo.setShowDisabled(false);

		formItems.add(shipToCombo);

		return shipToCombo;

	}

	public AddressCombo createVendorAddressComboItem() {

		AddressCombo addressCombo = new AddressCombo(UIUtils.getVendorString(
				Accounter.constants().supplierAddress(), Accounter.constants()
						.vendorAddress()));

		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						if (selectItem != null)
							vendoraddressSelected(selectItem);
					}

				});

		addressCombo.setDisabled(isEdit);
		// addressCombo.setShowDisabled(false);

		return addressCombo;

	}

	@Override
	protected void initTransactionViewData() {
		super.initTransactionViewData();

		// initTransactionNumber();

		initVendorAddressCombo();

		initShipToCombo();

		initPaymentTerms();

		initShippingTerms();

		initShippingMethod();
	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		ClientPurchaseOrder purchaseOrderToBeEdited = (ClientPurchaseOrder) transactionObject;

		ClientCompany company = getCompany();

		// String status;
		// if (purchaseOrderToBeEdited.getStatus() ==
		// ClientPurchaseOrder.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
		// status = "Un-Applied";
		// else if (purchaseOrderToBeEdited.getStatus() ==
		// ClientPurchaseOrder.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
		// status = "Partially Applied";
		// else
		// status = "Applied";
		// lab1.setText(FinanceApplication.constants().purchaseOrder()
		// + " (" + status + ")");
		this.dueDate = purchaseOrderToBeEdited.getDueDate();
		this.despatchDate = purchaseOrderToBeEdited.getDespatchDate();
		this.deliveryDate = purchaseOrderToBeEdited.getDeliveryDate();

		this.transactionItems = purchaseOrderToBeEdited.getTransactionItems();

		initTransactionNumber();
		vendorSelected(company.getVendor(purchaseOrderToBeEdited.getVendor()));
		contactSelected(purchaseOrderToBeEdited.getContact());
		phoneSelect.setValue(purchaseOrderToBeEdited.getPhone());
		// vendoraddressSelected(purchaseOrderToBeEdited.getVendorAddress());
		// shipToAddressSelected(purchaseOrderToBeEdited.getShippingAddress());

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		if (vendor != null)
			addresses.addAll(vendor.getAddress());
		shipToAddress.setListOfCustomerAdress(addresses);
		if (shippingAddress != null) {
			shipToAddress.businessSelect.setValue(shippingAddress
					.getAddressTypes().get(shippingAddress.getType()));
			shipToAddress.setAddres(shippingAddress);
		}
		shipToAddress.businessSelect.setDisabled(true);

		this.addressListOfVendor = vendor.getAddress();

		if (billingAddress != null) {

			billtoAreaItem.setValue(getValidAddress(billingAddress));

		} else
			billtoAreaItem.setValue("");

		purchaseOrderText.setValue(purchaseOrderToBeEdited
				.getPurchaseOrderNumber());

		paymentTermsSelected(company.getPaymentTerms(purchaseOrderToBeEdited
				.getPaymentTerm()));
		shippingTermsSelected(company.getShippingTerms(purchaseOrderToBeEdited
				.getShippingTerms()));
		shippingMethodSelected(company
				.getShippingMethod(purchaseOrderToBeEdited.getShippingMethod()));
		dueDateItem.setEnteredDate(new ClientFinanceDate(
				purchaseOrderToBeEdited.getDueDate()));
		despatchDateItem.setEnteredDate(new ClientFinanceDate(
				purchaseOrderToBeEdited.getDespatchDate()));
		deliveryDateItem.setEnteredDate(new ClientFinanceDate(
				purchaseOrderToBeEdited.getDeliveryDate()));
		memoTextAreaItem.setValue(purchaseOrderToBeEdited.getMemo());
		// refText.setValue(purchaseOrderToBeEdited.getReference());
		vendorTransactionGrid.setCanEdit(false);

		int status = purchaseOrderToBeEdited.getStatus();
		switch (status) {
		case ClientTransaction.STATUS_OPEN:
			statusSelect.setComboItem(OPEN);
			break;
		case ClientTransaction.STATUS_COMPLETED:
			statusSelect.setComboItem(COMPLETED);
			break;
		case ClientTransaction.STATUS_CANCELLED:
			statusSelect.setComboItem(CANCELLED);
		default:
			break;
		}

	}

	private void initDeliveryDate() {

		if (transactionObject != null) {
			ClientPurchaseOrder purchaseOrder = (ClientPurchaseOrder) transactionObject;
			deliveryDateItem.setEnteredDate(new ClientFinanceDate(purchaseOrder
					.getDeliveryDate()));

		}

	}

	private void initVendorAddressCombo() {

		if (billToCombo == null || addressListOfVendor == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_BILL_TO) {

				tempSet.add(address);
				clientAddress = address;
				break;
			}

		}
		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		billToCombo.initCombo(adressList);
		billToCombo.setDisabled(isEdit);
		// billToCombo.setShowDisabled(false);

		if (isEdit && billingAddress != null) {
			billToCombo.setComboItem(billingAddress);
			return;
		}
		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		} else {
			billToCombo.setComboItem(null);
			billToaddressSelected(clientAddress);
		}
	}

	private void initShipToCombo() {

		if (shipToCombo == null || addressListOfVendor == null)
			return;
		// ClientCompany company = FinanceApplication.getCompany();
		//
		// List<ClientAddress> listOfAddress = company.getAddresses();

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_SHIP_TO) {

				tempSet.add(address);
				clientAddress = address;
				break;
			}

		}

		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		shipToCombo.initCombo(adressList);
		shipToCombo.setDisabled(isEdit);

		if (isEdit && shippingAddress != null) {
			shipToCombo.setComboItem(shippingAddress);
			return;
		}
		if (clientAddress != null) {
			shipToCombo.setComboItem(clientAddress);
			shipToAddressSelected(clientAddress);

		} else {
			shipToCombo.setComboItem(null);
			shipToAddressSelected(clientAddress);
		}

	}

	private void initPaymentTerms() {

		payTermsSelect.initCombo(getCompany().getPaymentsTerms());

	}

	private void initShippingTerms() {

		shippingTermsCombo.initCombo(getCompany().getShippingTerms());

	}

	private void initShippingMethod() {

		List<ClientShippingMethod> result = getCompany().getShippingMethods();
		if (shippingMethodsCombo != null) {
			shippingMethodsCombo.initCombo(result);

		}

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transactionObject != null) {

			ClientSalesOrder salesOrder = (ClientSalesOrder) transactionObject;

			if (salesOrder != null) {

				memoTextAreaItem.setValue(salesOrder.getMemo());
				// refText.setValue(salesOrder.getReference());

			}

		}

	}

	@Override
	public void updateNonEditableItems() {
		transactionTotalNonEditableText.setAmount(vendorTransactionGrid
				.getTotal());
		netAmount.setAmount(vendorTransactionGrid.getGrandTotal());
		// vatTotalNonEditableText.setValue(vendorTransactionGrid.getVatTotal());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			vatTotalNonEditableText.setAmount(vendorTransactionGrid.getTotal()
					- vendorTransactionGrid.getGrandTotal());
		}

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		try {
			ClientPurchaseOrder purchaseOrder = transactionObject != null ? (ClientPurchaseOrder) transactionObject
					: new ClientPurchaseOrder();
			purchaseOrder.setVendor(vendor.getID());

			if (statusSelect.getSelectedValue().equals(OPEN))
				purchaseOrder.setStatus(ClientTransaction.STATUS_OPEN);
			else if (statusSelect.getSelectedValue().equals(COMPLETED))
				purchaseOrder.setStatus(ClientTransaction.STATUS_COMPLETED);
			else if (statusSelect.getSelectedValue().equals(CANCELLED))
				purchaseOrder.setStatus(ClientTransaction.STATUS_CANCELLED);

			if (contact != null)
				purchaseOrder.setContact(contact);
			if (phoneSelect.getValue() != null)
				purchaseOrder.setPhone(phoneSelect.getValue().toString());
			if (billingAddress != null)
				purchaseOrder.setVendorAddress(billingAddress);
			if (shippingAddress != null)
				purchaseOrder.setShippingAddress(shippingAddress);

			if (purchaseOrderText.getValue() != null)
				purchaseOrder.setPurchaseOrderNumber(purchaseOrderText
						.getValue().toString());
			if (paymentTerms != null)
				purchaseOrder.setPaymentTerm(paymentTerms.getID());
			if (shippingTerms != null)
				purchaseOrder.setShippingTerms(shippingTerms.getID());
			if (shippingMethod != null)
				purchaseOrder.setShippingMethod(shippingMethod.getID());
			if (dueDateItem.getEnteredDate() != null) {
				purchaseOrder
						.setDueDate(dueDateItem.getEnteredDate().getDate());
			}
			if (despatchDateItem.getEnteredDate() != null) {
				purchaseOrder.setDespatchDate(despatchDateItem.getEnteredDate()
						.getDate());
			}
			if (deliveryDateItem.getEnteredDate() != null)
				purchaseOrder.setDeliveryDate((deliveryDateItem
						.getEnteredDate().getDate()));

			purchaseOrder.setMemo(getMemoTextAreaItem());
			purchaseOrder.setNetAmount(vendorTransactionGrid.getGrandTotal());
			purchaseOrder.setTotal(vendorTransactionGrid.getTotal());
			// purchaseOrder.setReference(getRefText());

			transactionObject = purchaseOrder;
			super.saveAndUpdateView();

			if (transactionObject.getID() != 0) {
				alterObject((ClientPurchaseOrder) transactionObject);

			} else {
				createObject((ClientPurchaseOrder) transactionObject);
			}

			if (accountType == ClientCompany.ACCOUNTING_TYPE_US
					|| accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				netAmount.setAmount(purchaseOrder.getNetAmount());
				vatTotalNonEditableText.setAmount(purchaseOrder.getTotal()
						- purchaseOrder.getNetAmount());
				transactionTotalNonEditableText.setAmount(purchaseOrder
						.getTotal());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		this.vendor = vendor;
		if (vendor == null)
			return;

		super.vendorSelected(vendor);
		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billtoAreaItem.setValue(getValidAddress(billingAddress));
		} else
			billtoAreaItem.setValue("");

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(vendor.getAddress());
		shipToAddress.setAddress(addresses);

		initVendorAddressCombo();
		initShipToCombo();

		ClientCompany company = getCompany();
		paymentTerms = company.getPaymentTerms(vendor.getPaymentTerms());
		shippingMethod = company.getShippingMethod(vendor.getShippingMethod());
		if (paymentTerms != null) {
			payTermsSelect.setComboItem(paymentTerms);
			paymentTermsSelected(paymentTerms);
		}

		shippingMethodsCombo.setComboItem(shippingMethod);
		vendorCombo.setComboItem(vendor);
	}

	private void vendoraddressSelected(ClientAddress selectedAddress) {
		if (selectedAddress == null)
			return;
		this.vendorAddress = selectedAddress;
		billToCombo.setComboItem(selectedAddress);

	}

	protected void shipToAddressSelected(ClientAddress selectedAddress) {
		if (selectedAddress == null)
			return;
		this.shippingAddress = selectedAddress;
		shipToCombo.setComboItem(selectedAddress);

	}

	private void paymentTermsSelected(ClientPaymentTerms paymentTerms) {
		if (paymentTerms != null) {
			this.paymentTerms = paymentTerms;
			payTermsSelect.setComboItem(paymentTerms);
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(
					getTransactionDate(), paymentTerms);
			if (dueDate != null) {
				dueDateItem.setValue(dueDate);
			}
		}
	}

	private void shippingTermsSelected(ClientShippingTerms shippingTerms) {
		if (shippingTerms != null) {
			this.shippingTerms = shippingTerms;
			shippingTermsCombo.setComboItem(shippingTerms);
		}
	}

	private void shippingMethodSelected(ClientShippingMethod shippingMethod) {
		if (shippingMethod != null) {
			this.shippingMethod = shippingMethod;
			shippingMethodsCombo.setComboItem(shippingMethod);
		}
	}

	private void setDueDate(long date) {
		dueDateItem.setEnteredDate(new ClientFinanceDate(date));
	}

	private void setDespatchDate(long date) {
		despatchDateItem.setEnteredDate(new ClientFinanceDate(date));
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

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.addComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_TERM)
				this.shippingTermsCombo
						.addComboItem((ClientShippingTerms) core);
			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shippingMethodsCombo
						.addComboItem((ClientShippingMethod) core);
			if (core.getObjectType() == AccounterCoreType.ADDRESS)
				this.shipToCombo.addComboItem((ClientAddress) core);
			if (core.getObjectType() == AccounterCoreType.ADDRESS)
				this.billToCombo.addComboItem((ClientAddress) core);

			break;

		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.removeComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_TERM)
				this.shippingTermsCombo
						.removeComboItem((ClientShippingTerms) core);
			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shippingMethodsCombo
						.removeComboItem((ClientShippingMethod) core);
			if (core.getObjectType() == AccounterCoreType.ADDRESS)
				this.shipToCombo.removeComboItem((ClientAddress) core);
			if (core.getObjectType() == AccounterCoreType.ADDRESS)
				this.billToCombo.removeComboItem((ClientAddress) core);

			break;

		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}

	}

	public boolean validate() throws Exception {
		switch (validationCount) {
		case 5:
			return AccounterValidator.validateFormItem(false, statusSelect);
		case 4:
			return AccounterValidator.validate_dueOrDelivaryDates(
					dueDateItem.getDate(), transactionDateItem.getDate(),
					Accounter.constants().dueDate());
		case 3:
			return AccounterValidator.validateForm(vendorForm, false);
		case 2:
			return AccounterValidator.isBlankTransaction(vendorTransactionGrid);
		case 1:
			return vendorTransactionGrid.validateGrid();
		default:
			return true;

		}
	}

	public void onEdit() {
		if (transactionObject.getStatus() == ClientTransaction.STATUS_COMPLETED)
			Accounter.showError("Completed purchase order can't be edited");
		else {
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

			AccounterCoreType type = UIUtils
					.getAccounterCoreType(transactionObject.getType());
			this.rpcDoSerivce.canEdit(type, transactionObject.id, editCallBack);

		}

	}

	protected void enableFormItems() {
		isEdit = false;
		statusSelect.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		shipToAddress.businessSelect.setDisabled(isEdit);
		// shipToCombo.setDisabled(isEdit);
		ClientTransactionItem item = new ClientTransactionItem();
		if (!DecimalUtil.isEquals(item.getInvoiced(), 0)) {
			vendorCombo.setDisabled(isEdit);
		} else {
			vendorCombo.setDisabled(true);
		}

		// billToCombo.setDisabled(isEdit);
		purchaseOrderText.setDisabled(isEdit);
		deliveryDateItem.setDisabled(isEdit);
		payTermsSelect.setDisabled(isEdit);

		shippingTermsCombo.setDisabled(isEdit);
		shippingMethodsCombo.setDisabled(isEdit);

		dueDateItem.setDisabled(isEdit);
		despatchDateItem.setDisabled(isEdit);

		vendorTransactionGrid.setDisabled(isEdit);
		vendorTransactionGrid.setCanEdit(true);
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
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().purchaseOrder();
	}
}
