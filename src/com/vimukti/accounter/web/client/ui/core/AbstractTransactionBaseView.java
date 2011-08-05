/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.PopupPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomMenuBar;
import com.vimukti.accounter.web.client.ui.CustomMenuItem;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.WriteChequeView;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundView;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerPaymentView;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorTransactionGrid;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseView;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseView;
import com.vimukti.accounter.web.client.ui.vendors.EmployeeExpenseView;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorPaymentView;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyWidget;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author Fernandez This Class serves as the Base Class for All Transactions
 * 
 */
public abstract class AbstractTransactionBaseView<T extends IAccounterCore> extends BaseView<T> {

	protected int transactionType;

	protected ClientTransaction transactionObject;

	public static final int CUSTOMER_TRANSACTION_GRID = 1;
	public static final int VENDOR_TRANSACTION_GRID = 2;
	public static final int BANKING_TRANSACTION_GRID = 3;
	public static final int MAKEDEPOSIT_TRANSACTION_GRID = 4;
	public static final int PAYBILL_TRANSACTION_GRID = 5;
	public static final int RECIEVEPAYMENT_TRANSACTION_GRID = 6;
	public static final int TAXAGENCY_TRANSACTION_GRID = 7;
	public static final int PAYSALESTAX_TRANSACTION_GRID = 8;
	public static final int JOURNALENTRY_TRANSACTION_GRID = 9;
	public static final int PAYVAT_TRANSACTION_GRID = 10;

	protected ClientFinanceDate transactionDate;
	protected TextItem transactionNumber;
	protected DateField transactionDateItem;
	protected TextAreaItem memoTextAreaItem;
	// protected TextItem refText;
	protected AccounterButton menuButton;
	private PopupPanel popupPanel;
	private CustomMenuBar popupMenuBar;

	private Event event;
	private boolean isMenuRequired = true;

	protected List<ClientTransactionItem> transactionItems;

	private List<String> payVatMethodList;

	protected String paymentMethod;

	protected SelectCombo paymentMethodCombo;

	/**
	 * The Transaction Grid meant to Serve in all Transactions
	 */
	protected AbstractTransactionGrid<ClientTransactionItem> vendorTransactionGrid,
			customerTransactionGrid;

	protected boolean showPriceWithVat;

	protected boolean EUVatExempt = false;

	protected boolean isVATInclusive;

	public CheckboxItem vatinclusiveCheck;

	protected CurrencyWidget currencyWidget;

	protected int gridType;

	public boolean isVatInclusive() {
		return isVATInclusive;
	}

	/**
	 * @param transactionType
	 */
	public AbstractTransactionBaseView(int transactionType,
			int transactionViewType) {
		super();
		this.transactionType = transactionType;
		this.gridType = transactionViewType;

	}

	protected abstract void createControls();

	protected abstract void initTransactionViewData(
			ClientTransaction transactionObject);

	protected abstract void initTransactionViewData();

	public abstract void updateNonEditableItems();

	protected final String getTransactionStatus() {
		return Utility.getStatus(transactionType,
				transactionObject != null ? transactionObject.getStatus() : 0);
	}

	protected ClientTAXCode getTaxCodeForTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		ClientTAXCode taxCode = null;

		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getTaxCode() != 0) {

				taxCode = getCompany().getTAXCode(
						clientTransactionItem.getTaxCode());

				// if (clientTransactionItem.getTaxItem() != null
				// || clientTransactionItem.getTaxGroup() != null) {
				// if (clientTransactionItem.getTaxItem() instanceof
				// ClientTAXItem)
				// taxItemGroup = clientTransactionItem.getTaxItem();
				// if (clientTransactionItem.getTaxGroup() instanceof
				// ClientTAXGroup)
				// taxItemGroup = clientTransactionItem.getTaxGroup();

				if (taxCode != null)
					break;
				else
					continue;
			}

		}
		return taxCode;
	}

	public double getVATRate(long VATCodeID) {
		return 0.0;
	}

	public AbstractTransactionGrid<ClientTransactionItem> getGrid() {
		// if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		switch (gridType) {
		case JOURNALENTRY_TRANSACTION_GRID:
			break;
		case PAYSALESTAX_TRANSACTION_GRID:
			break;
		case TAXAGENCY_TRANSACTION_GRID:
			break;
		case RECIEVEPAYMENT_TRANSACTION_GRID:
			break;
		case PAYBILL_TRANSACTION_GRID:
			break;
		case MAKEDEPOSIT_TRANSACTION_GRID:
		case VENDOR_TRANSACTION_GRID:
			return new VendorTransactionGrid();
		case CUSTOMER_TRANSACTION_GRID:
			return new CustomerTransactionGrid();
		}
		// } else {
		// switch (gridType) {
		// case JOURNALENTRY_TRANSACTION_GRID:
		// break;
		// case PAYSALESTAX_TRANSACTION_GRID:
		// break;
		// case TAXAGENCY_TRANSACTION_GRID:
		// break;
		// case RECIEVEPAYMENT_TRANSACTION_GRID:
		// break;
		// case PAYBILL_TRANSACTION_GRID:
		// break;
		// case MAKEDEPOSIT_TRANSACTION_GRID:
		// break;
		// case VENDOR_TRANSACTION_GRID:
		// return new VendorTransactionUKGrid();
		// case CUSTOMER_TRANSACTION_GRID:
		// return new CustomerTransactionUKGrid();
		// }
		// }
		return null;
	}

	public void setAmountIncludeChkValue(boolean isAmountIncludedVAT) {
		if (vatinclusiveCheck != null) {
			vatinclusiveCheck.setValue(isAmountIncludedVAT);
		}
	}

	public CheckboxItem getVATInclusiveCheckBox() {
		vatinclusiveCheck = new CheckboxItem(Accounter.constants()
				.amountIncludesVat());
		vatinclusiveCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isVATInclusive = (Boolean) event.getValue();
				if (vendorTransactionGrid != null) {
					vendorTransactionGrid.refreshVatValue();
				} else if (customerTransactionGrid != null) {
					customerTransactionGrid.refreshVatValue();
				}

			}
		});
		vatinclusiveCheck.setDisabled(isEdit);
		return vatinclusiveCheck;
	}

	public void setGridType(int gridType) {
		this.gridType = gridType;
	}

	protected void initTransactionNumber() {

		if (transactionNumber == null)
			return;

		if (transactionObject != null) {

			transactionNumber.setValue(transactionObject.getNumber());
			return;
		}

		AccounterAsyncCallback<String> transactionNumberCallback = new AccounterAsyncCallback<String>() {

			public void onException(AccounterException caught) {
				Accounter.showError("Failed to Get the Transaction Number..");

			}

			public void onSuccess(String result) {
				if (result == null) {
					onException(null);
				}

				// transactionNumber.setValue(String.valueOf(result));
				transactionNumber.setValue(result);

			}

		};

		this.rpcUtilService.getNextTransactionNumber(transactionType,
				transactionNumberCallback);

	}

	protected void resetForms() {
		// // FIX ME
		//
		// // for (DynamicForm form : forms) {
		// // form.reset();
		// // }
		// //
		// // for (FormItem item : formItems) {
		// // item.setDisabled(isEdit);
		// // }

	}

	protected DateField createTransactionDateItem() {

		final DateField dateItem = new DateField(Accounter.constants().date());
		dateItem.setHelpInformation(true);
		// if (this instanceof VendorBillView)
		// dateItem.setShowTitle(true);
		// else
		dateItem.setShowTitle(false);

		dateItem.setWidth(100);
		dateItem.setColSpan(2);
		dateItem.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (date != null) {
					try {
						ClientFinanceDate newDate = dateItem.getValue();
						if (newDate != null)
							setTransactionDate(newDate);
					} catch (Exception e) {
						Accounter.showError(Accounter.constants()
								.invalidTransactionDate());
						setTransactionDate(new ClientFinanceDate());
						dateItem.setEnteredDate(getTransactionDate());
					}

				}
			}
		});

		if (transactionObject != null && transactionObject.getDate() != null) {
			dateItem.setEnteredDate(transactionObject.getDate());
			setTransactionDate(transactionObject.getDate());

		} else {
			setTransactionDate(new ClientFinanceDate());
			dateItem.setEnteredDate(new ClientFinanceDate());

		}

		dateItem.setDisabled(isEdit);

		formItems.add(dateItem);

		return dateItem;

	}

	public void setTransactionDate(long date) {

	}

	protected TextItem createTransactionNumberItem() {

		final TextItem item = new TextItem(Accounter.constants().no());
		item.setHelpInformation(true);
		item.setWidth(100);
		item.setColSpan(1);

		item.setDisabled(isEdit);

		formItems.add(item);

		if (UIUtils.isMSIEBrowser())
			item.setWidth("150px");

		return item;

	}

	protected TextItem createRefereceText() {

		TextItem refText = new TextItem(Accounter.constants().reference());
		refText.setHelpInformation(true);
		formItems.add(refText);

		return refText;

	}

	protected AmountField createNetAmountField() {
		AmountField netAmountField = new AmountField(Accounter.constants()
				.netAmount());
		netAmountField.setHelpInformation(true);
		netAmountField.setDefaultValue("£0.00");
		netAmountField.setDisabled(true);
		return netAmountField;
	}

	protected AmountLabel createNetAmountLabel() {
		AmountLabel netAmountLabel = new AmountLabel(Accounter.constants()
				.netAmount());
		netAmountLabel.setTitle(Accounter.constants().netAmount());
		netAmountLabel.setDefaultValue("£0.00");
		return netAmountLabel;
	}

	protected AmountLabel createTransactionTotalNonEditableLabelforPurchase() {

		AmountLabel amountLabel = new AmountLabel(Accounter.constants().total());

		return amountLabel;

	}

	protected AmountLabel createVATTotalNonEditableLabelforPurchase() {
		AmountLabel amountLabel = new AmountLabel(Accounter.constants().vat());

		return amountLabel;
	}

	protected TextAreaItem createMemoTextAreaItem() {

		TextAreaItem memoArea = new TextAreaItem();
		if (!(this instanceof NewCustomerPaymentView
				|| this instanceof NewVendorPaymentView || this instanceof CustomerRefundView))
			memoArea.setMemo(true);
		memoArea.setHelpInformation(true);

		memoArea.setTitle(Accounter.constants().memo());
		// memoArea.setRowSpan(2);
		// memoArea.setColSpan(3);

		formItems.add(memoArea);

		return memoArea;

	}

	protected final void transactionSuccess(Object result) {

		try {
			if (result == null)
				throw new Exception();
			//
			// StringBuffer buffer = new StringBuffer();
			//
			// buffer.append("Saved Transaction "
			// + Utility.getTransactionName(transactionType));
			// if (transactionNumber != null) {
			// buffer
			// .append(" With Number "
			// + String.valueOf(" # "
			// + transactionNumber.getNumber()));
			// }
			//
			// Accounter.showInformation(buffer.toString());

			if (!saveAndClose) {
				// resetForms();
				// reload();
				if (!History.getToken().equals(getAction().getHistoryToken())) {

				}
				getAction().run(null, true);

			} else {

				MainFinanceWindow.getViewManager().closeView(this.getAction(),
						result);
			}

		} catch (Exception e) {
			// SC.logWarn(String.valueOf(result));
			e.printStackTrace();
		}

	}

	protected void reload() {
		transactionObject = null;
		if (customerTransactionGrid != null) {
			customerTransactionGrid.canDeleteRecord(true);
			// FIXME ::: no need of this statement
			// transactionGrid.setEnableMenu(true);
			customerTransactionGrid.canDeleteRecord(true);
			// customerTransactionGrid.setShowMenu(true);
			customerTransactionGrid.resetGridEditEvent();
			customerTransactionGrid.removeAllRecords();
			customerTransactionGrid.updateTotals();
		}
		initTransactionViewData();
	}

	public final void transactionFailed(Throwable caught) {
		String transName = Utility.getTransactionName(transactionType);
		if (caught.getMessage() != null)
			Accounter.showError(caught.getMessage());
		else
			Accounter.showError(Accounter.messages().failedTransaction(
					transName));
		// SC
		// .logWarn("Failed Transaction" + transName + " " + caught != null ?
		// caught
		// .getMessage()
		// : "Cause UnKnown");
		if (caught != null)
			caught.printStackTrace();

	}

	public void saveAndUpdateView() throws Exception {
		try {
			if (this.transactionObject.getTotal() <= 0) {
				throw new InvalidOperationException(Accounter.constants()
						.transactiontotalcannotbe0orlessthan0());
			}
			transactionObject.setType(transactionType);
			if (transactionDate != null)
				transactionObject.setDate(transactionDate.getDate());
			if (transactionNumber != null)
				transactionObject.setNumber(transactionNumber.getValue()
						.toString());
			processTransactionItems();
			if (transactionItems != null)
				validateTransactionItems(transactionItems);
			transactionObject.setTransactionItems(transactionItems);
			// transactionObject.setModifiedOn(new Date());
			if (transactionItems != null)
				validateVATCODEBaseOnTransactionDate();
		} catch (Exception e) {

			// SC.logWarn("Exception While Saving"
			// + String.valueOf(e.getMessage()));
			throw e;
		}

	}

	private void validateTransactionItems(
			List<ClientTransactionItem> transactionItems2)
			throws InvalidOperationException {
		if (transactionItems == null)
			return;
		for (ClientTransactionItem transactionItem : transactionItems)
			if (transactionItem.getLineTotal() <= 0)
				throw new InvalidOperationException(Accounter.constants()
						.transactionitemtotalcannotbe0orlessthan0());
	}

	public AccounterButton createAddNewButton() {
		menuButton = new AccounterButton(Accounter.constants().addNewItem());
		menuButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (isMenuRequired()) {
					showMenu(menuButton);
					showMenu((Event) event.getNativeEvent());
				} else {
					onAddNew();
				}
			}
		});

		return menuButton;
	}

	protected void onAddNew() {
		// TODO Auto-generated method stub

	}

	private void processTransactionItems() throws InvalidEntryException {
		if (customerTransactionGrid != null)
			this.transactionItems = customerTransactionGrid
					.getallTransactions(transactionObject);
		if (vendorTransactionGrid != null)
			this.transactionItems = vendorTransactionGrid
					.getallTransactions(transactionObject);

	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public ClientTransaction getTransactionObject() {
		return transactionObject;
	}

	public void setTransactionObject(ClientTransaction transactionObject) {
		this.transactionObject = transactionObject;
	}

	// public String getRefText() {
	// return refText != null && refText.getValue().toString() != null ? refText
	// .getValue().toString()
	// : "";
	// }
	//
	// public void setRefText(String reference) {
	// this.refText.setValue(reference != null ? reference : "");
	// }

	public String getMemoTextAreaItem() {
		return memoTextAreaItem != null
				&& memoTextAreaItem.getValue().toString() != null ? memoTextAreaItem
				.getValue().toString() : "";
	}

	public void setMemoTextAreaItem(String memo) {
		this.memoTextAreaItem.setValue(memo != null ? memo : "");
	}

	public SelectCombo createPaymentMethodSelectItem() {
		String paymentType = null;
		payVatMethodList = new ArrayList<String>();
		paymentType = UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
				.constants().check());
		String payVatMethodArray[] = new String[] {
				Accounter.constants().cash(), paymentType,
				Accounter.constants().creditCard(),
				Accounter.constants().directDebit(),
				Accounter.constants().masterCard(),
				Accounter.constants().onlineBanking(),
				Accounter.constants().standingOrder(),
				Accounter.constants().switchMaestro() };

		for (int i = 0; i < payVatMethodArray.length; i++) {
			payVatMethodList.add(payVatMethodArray[i]);
		}

		final SelectCombo paymentMethodSelect = new SelectCombo(Accounter
				.constants().paymentMethod());
		paymentMethodSelect.setHelpInformation(true);

		paymentMethodSelect.setRequired(true);
		paymentMethodSelect.initCombo(payVatMethodList);
		paymentMethodSelect.setDefaultToFirstOption(true);
		paymentMethod = Accounter.constants().cash();

		paymentMethodSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentMethodSelected(paymentMethodSelect
								.getSelectedValue());

					}
				});
		paymentMethodSelect.setDisabled(isEdit);
		formItems.add(paymentMethodSelect);

		return paymentMethodSelect;

	}

	protected void paymentMethodSelected(String paymentmethod) {
		this.paymentMethod = paymentmethod;
	}

	@Override
	public void init(ViewManager manager) {
		super.init(manager);
		createControls();
		// if (this.data != null) {
		// UIUtils.disableView(this.canvas);
		// }
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (transactionObject != null)
			initTransactionViewData(transactionObject);
		else
			initTransactionViewData();

		super.initData();

	}

	@Override
	public void setData(T data) {
		super.setData(data);
		if (data != null)
			transactionObject = (ClientTransaction) data;
		else
			transactionObject = null;

	}

	@Override
	public void disableUserEntry() {

		if (this.formItems != null) {

			for (FormItem item : formItems) {
				// item.disable();
			}
		}
	}

	// public void setShowPriceWithVat(boolean showPriceWithVat) {
	// this.showPriceWithVat = showPriceWithVat;
	// }

	public boolean isShowPriceWithVat() {
		return isVATInclusive;
	}

	public void setMenuItems(AccounterButton button,
			Map<String, ImageResource> items) {
		createPopupMenu(button);
		popupMenuBar.clearItems();
		for (final String itm : items.keySet()) {
			ImageResource imgSrc = items.get(itm);
			Command cmd = new Command() {

				@Override
				public void execute() {
					menuItemClicked(itm);
					popupPanel.removeFromParent();
				}
			};
			CustomMenuItem item = new CustomMenuItem(itm, cmd);
			item.addStyleName(itm);
			item.setIcon(imgSrc);
			popupMenuBar.addItem(item);
		}
	}

	public void setMenuItems(AccounterButton button, String... items) {
		createPopupMenu(button);
		popupMenuBar.clearItems();

		ClientCompanyPreferences preferences = getCompany().getPreferences();
		boolean sellProducts = preferences.isSellProducts();
		boolean sellServices = preferences.isSellServices();

		for (final String itm : items) {
			Command cmd = new Command() {

				@Override
				public void execute() {
					menuItemClicked(itm);
					popupPanel.removeFromParent();
				}
			};

			CustomMenuItem item = new CustomMenuItem(itm, cmd);
			item.addStyleName(itm);
			ImageResource image = null;
			if (itm.equals("Accounts")) {
				image = Accounter.getFinanceMenuImages().Accounts();
			} else if (itm.equals("Product Item")) {
				if (sellProducts) {
					image = Accounter.getFinanceMenuImages().items();
				} else {
					continue;
				}
			} else if (itm.equals("Comment")) {
				image = Accounter.getFinanceMenuImages().comments();
			} else if (itm.equals("Sales Tax") || (itm.equals("Service Item"))) {
				if (sellServices) {
					image = Accounter.getFinanceMenuImages().salestax();
				} else {
					continue;
				}
			}

			item.setIcon(image);

			// item.getElement().getStyle().setProperty("background",
			// "url(" + image + ") no-repeat scroll 0 0 transparent");

			popupMenuBar.addItem(item);
		}
	}

	private void createPopupMenu(AccounterButton button) {
		if (popupPanel == null) {
			popupPanel = new PopupPanel(true);
			popupMenuBar = new CustomMenuBar(Accounter.getFinanceMenuImages());
			popupMenuBar.getElement().setAttribute("id", "addnewpopumenu");

			popupPanel.setStyleName("popup");
			popupPanel.getElement().setAttribute("id", "addnewpopuppanel");
			popupMenuBar.setVisible(true);
			popupPanel.add(popupMenuBar);
			// popupPanel.setPopupPosition(button.getAbsoluteLeft(),
			// button.getAbsoluteTop() + button.getOffsetHeight());
		}
	}

	protected void menuItemClicked(String item) {
		onAddNew(item);
	}

	/**
	 * Override this method to do anything you want when add new button is
	 * clicked. Default implementation will show a
	 */
	protected void onAddNew(String item) {

	}

	public void showMenu(Event event) {
		// int x = DOM.eventGetClientX(event);
		// int y = DOM.eventGetClientY(event);
		// popupPanel.setPopupPosition(x, y);
		ClientCompanyPreferences preferences = getCompany().getPreferences();
		boolean sellProducts = preferences.isSellProducts();
		boolean sellServices = preferences.isSellServices();
		int childCount = 0;
		if (this instanceof EmployeeExpenseView) {
			if (sellServices) {
				childCount += 1;
			}
		} else if (this instanceof CreditCardExpenseView
				|| this instanceof CashExpenseView) {
			childCount = 1;
			if (sellServices) {
				childCount += 1;
			}
		} else if (this instanceof WriteChequeView) {
			childCount = 1;
			if (sellProducts) {
				childCount += 1;
			}
		} else {
			childCount = 1;
			if (sellProducts) {
				childCount += 1;
			}
			if (sellServices) {
				childCount += 1;
			}
		}

		if (childCount == 1) {
			popupPanel.setPopupPosition(
					menuButton.getAbsoluteLeft() - 5,
					menuButton.getAbsoluteTop()
							- (popupMenuBar.getOffsetHeight() + 41));
		} else if (childCount == 2) {
			// if (this instanceof CashExpenseView || this instanceof
			// WriteChequeView)
			popupPanel.setPopupPosition(
					menuButton.getAbsoluteLeft() - 5,
					menuButton.getAbsoluteTop()
							- (popupMenuBar.getOffsetHeight() + 85));
		} else if (childCount == 3) {
			// if (this instanceof EmployeeExpenseView) {
			popupPanel.setPopupPosition(
					menuButton.getAbsoluteLeft() - 5,
					menuButton.getAbsoluteTop()
							- (popupMenuBar.getOffsetHeight() + 127));
			// }
		}
		popupPanel.show();
	}

	public boolean isMenuRequired() {
		return isMenuRequired;
	}

	public void setMenuRequired(boolean isMenuRequired) {
		this.isMenuRequired = isMenuRequired;
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		transactionSuccess(result);
	}

	@Override
	public void saveFailed(Throwable exception) {
		transactionFailed(exception);
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {
		if (vatinclusiveCheck != null)
			vatinclusiveCheck.setDisabled(isEdit);
		if (menuButton != null)
			menuButton.setEnabled(!isEdit);
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void validateVATCODEBaseOnTransactionDate()
			throws InvalidEntryException {

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			for (ClientTransactionItem selectItem : this.transactionItems) {
				if (selectItem.getTaxCode() != 0) {
					ClientTAXCode code = getCompany().getTAXCode(
							selectItem.getTaxCode());

					if (code.getName().equalsIgnoreCase("New S")
							&& getTransactionDate().before(
									new ClientFinanceDate(2011 - 1900, 01 - 1,
											04))) {
						throw new InvalidEntryException(Accounter.constants()
								.vat4thJanError());
					}
				}
			}
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if (menuButton != null) {
			menuButton.setType(AccounterButton.ADD_BUTTON);
			menuButton.setEnabled(!isEdit);
		}
	}

	protected void showMenu(AccounterButton button) {

	}

	public CurrencyWidget createCurrencyWidget(List<ClientCurrency> currencies,
			ClientCurrency baseCurrency) {
		/*
		 * if (!((currencies == null) && (baseCurrency == null))) {
		 * getCurrencyWidget(); }
		 */
		return new CurrencyWidget(currencies, baseCurrency);

	}

	/**
	 * creates currency widgets from the existing currencies.
	 * 
	 * @return
	 */
	public CurrencyWidget bulidCurrencyWidget() {
		List<ClientCurrency> currencies = new ArrayList<ClientCurrency>(
				Accounter.getCompany().getCurrencies());

		CurrencyWidget currencyWidget = createCurrencyWidget(currencies,
				Accounter.getCompany().getPreferences().getBaseCurrency());

		return currencyWidget;
	}

	public CurrencyWidget getCurrencyWidget() {
		return currencyWidget;
	}
}
