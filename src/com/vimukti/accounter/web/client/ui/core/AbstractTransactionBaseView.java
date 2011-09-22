/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomMenuBar;
import com.vimukti.accounter.web.client.ui.CustomMenuItem;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.WriteChequeView;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.ClassListCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.LocationCombo;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundView;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerPaymentView;
import com.vimukti.accounter.web.client.ui.customers.RecurringTransactionDialog;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorPaymentView;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyWidget;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author Fernandez This Class serves as the Base Class for All Transactions
 * 
 */
public abstract class AbstractTransactionBaseView<T extends ClientTransaction>
		extends BaseView<T> {
	protected AccounterConstants customerConstants = Accounter.constants();

	protected int transactionType;

	protected T transaction;

	// public static final int CUSTOMER_TRANSACTION_GRID = 1;
	// public static final int VENDOR_TRANSACTION_GRID = 2;
	// public static final int BANKING_TRANSACTION_GRID = 3;
	// public static final int MAKEDEPOSIT_TRANSACTION_GRID = 4;
	// public static final int PAYBILL_TRANSACTION_GRID = 5;
	// public static final int RECIEVEPAYMENT_TRANSACTION_GRID = 6;
	// public static final int TAXAGENCY_TRANSACTION_GRID = 7;
	// public static final int PAYSALESTAX_TRANSACTION_GRID = 8;
	// public static final int JOURNALENTRY_TRANSACTION_GRID = 9;
	// public static final int PAYVAT_TRANSACTION_GRID = 10;

	protected String checkNumber = ClientWriteCheck.IS_TO_BE_PRINTED;

	protected ClientFinanceDate transactionDate;
	protected TextItem transactionNumber;
	protected TextItem checkNo;
	protected DateField transactionDateItem;
	protected TextAreaItem memoTextAreaItem;
	protected TextItem phoneSelect;
	// protected TextItem refText;
	// protected AddNewButton menuButton;
	private PopupPanel popupPanel;
	private CustomMenuBar popupMenuBar;

	protected Set<ClientAddress> addressListOfVendor;
	protected Set<ClientContact> contacts;
	protected ClientContact contact;

	protected ClientAccount payFromAccount;
	protected ClientAddress billingAddress;
	protected AddressCombo billToCombo;
	protected ContactCombo contactCombo;
	protected VendorCombo vendorCombo;
	protected PayFromAccountsCombo payFromCombo;

	private Event event;
	private boolean isMenuRequired = true;

	protected List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();

	private List<String> payVatMethodList;

	protected String paymentMethod;
	protected String phoneNo;

	protected SelectCombo paymentMethodCombo;

	// /**
	// * // * The Transaction Grid meant to Serve in all Transactions //
	// */
	// protected AbstractTransactionGrid<ClientTransactionItem>
	// vendorTransactionGrid,
	// customerTransactionGrid;
	// /**
	// * // * The Transaction Grid meant to Serve in all Transactions //
	// */
	// protected AbstractTransactionGrid<ClientTransactionItem>
	// vendorTransactionGrid,

	protected boolean showPriceWithVat;

	protected boolean EUVatExempt = false;

	protected boolean isVATInclusive;

	public CheckboxItem vatinclusiveCheck;

	protected ClientVendor vendor;

	// protected CurrencyWidget currencyWidget;
	private ClientLocation location;

	protected LocationCombo locationCombo;
	protected int gridType;

	protected Button recurringButton;

	// Class Tracking
	protected ClassListCombo classListCombo;

	protected ClientAccounterClass clientAccounterClass;

	private ArrayList<ClientAccounterClass> clientAccounterClasses = new ArrayList<ClientAccounterClass>();

	public boolean isVatInclusive() {
		return isVATInclusive;
	}

	/**
	 * @param transactionType
	 */
	public AbstractTransactionBaseView(int transactionType) {
		super();
		this.transactionType = transactionType;
		// this.gridType = transactionViewType;

	}

	protected abstract void createControls();

	// protected abstract void initTransactionViewData(
	// ClientTransaction transactionObject);

	protected abstract void initTransactionViewData();

	public abstract void updateNonEditableItems();

	protected final String getTransactionStatus() {
		return Utility.getStatus(transactionType,
				transaction != null ? transaction.getStatus() : 0);
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

	// public abstract AbstractTransactionGrid<ClientTransactionItem> getGrid();
	// public AbstractTransactionGrid<ClientTransactionItem> getGrid() {
	// // if (getCompany().getAccountingType() ==
	// // ClientCompany.ACCOUNTING_TYPE_US) {
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
	// case VENDOR_TRANSACTION_GRID:
	// return new VendorTransactionGrid();
	// case CUSTOMER_TRANSACTION_GRID:
	// return new CustomerTransactionGrid();
	// }
	// // } else {
	// // switch (gridType) {
	// // case JOURNALENTRY_TRANSACTION_GRID:
	// // break;
	// // case PAYSALESTAX_TRANSACTION_GRID:
	// // break;
	// // case TAXAGENCY_TRANSACTION_GRID:
	// // break;
	// // case RECIEVEPAYMENT_TRANSACTION_GRID:
	// // break;
	// // case PAYBILL_TRANSACTION_GRID:
	// // break;
	// // case MAKEDEPOSIT_TRANSACTION_GRID:
	// // break;
	// // case VENDOR_TRANSACTION_GRID:
	// // return new VendorTransactionUKGrid();
	// // case CUSTOMER_TRANSACTION_GRID:
	// // return new CustomerTransactionUKGrid();
	// // }
	// // }
	// return null;
	// }

	public void setAmountIncludeChkValue(boolean isAmountIncludedVAT) {
		if (vatinclusiveCheck != null) {
			vatinclusiveCheck.setValue(isAmountIncludedVAT);
			isVATInclusive = isAmountIncludedVAT;
		}
	}

	public CheckboxItem getVATInclusiveCheckBox() {
		vatinclusiveCheck = new CheckboxItem(Accounter.constants()
				.amountIncludesVat());
		vatinclusiveCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isVATInclusive = (Boolean) event.getValue();
				refreshTransactionGrid();
			}
		});
		vatinclusiveCheck.setDisabled(isInViewMode());
		return vatinclusiveCheck;
	}

	protected abstract void refreshTransactionGrid();

	protected void initTransactionNumber() {

		if (transactionNumber == null)
			return;

		if (transaction != null && transaction.getID() != 0) {

			transactionNumber.setValue(transaction.getNumber());
			return;
		}

		AccounterAsyncCallback<String> transactionNumberCallback = new AccounterAsyncCallback<String>() {

			public void onException(AccounterException caught) {
				Accounter.showError("Failed to Get the Transaction Number..");

			}

			public void onResultSuccess(String result) {
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
		dateItem.setToolTip(Accounter
				.messages()
				.selectDateWhenTransactioCreated(this.getAction().getViewName()));
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

		if (transaction != null && transaction.getDate() != null) {
			dateItem.setEnteredDate(transaction.getDate());
			setTransactionDate(transaction.getDate());

		} else {
			setTransactionDate(new ClientFinanceDate());
			dateItem.setEnteredDate(new ClientFinanceDate());

		}

		dateItem.setDisabled(isInViewMode());

		// formItems.add(dateItem);

		return dateItem;

	}

	public void setTransactionDate(long date) {

	}

	protected TextItem createTransactionNumberItem() {

		final TextItem item = new TextItem(Accounter.constants().no());
		item.setToolTip(Accounter.messages().giveNoTo(
				this.getAction().getViewName()));
		item.setHelpInformation(true);
		item.setWidth(100);
		item.setColSpan(1);

		item.setDisabled(isInViewMode());

		// formItems.add(item);

		if (UIUtils.isMSIEBrowser())
			item.setWidth("150px");

		return item;

	}

	protected TextItem createRefereceText() {

		TextItem refText = new TextItem(Accounter.constants().reference());
		refText.setHelpInformation(true);
		// formItems.add(refText);

		return refText;

	}

	protected AmountField createNetAmountField() {
		AmountField netAmountField = new AmountField(Accounter.constants()
				.netAmount(), this);
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
			memoArea.setMemo(true, this);
		memoArea.setHelpInformation(true);

		memoArea.setTitle(Accounter.constants().memo());
		// memoArea.setRowSpan(2);
		// memoArea.setColSpan(3);

		// formItems.add(memoArea);

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

				getManager().closeCurrentView();
			}

		} catch (Exception e) {
			// SC.logWarn(String.valueOf(result));
			e.printStackTrace();
		}

	}

	protected void reload() {
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

	public void saveAndUpdateView() {

	}

	// public AddNewButton createAddNewButton() {
	// // TODO make this button to Image button
	// menuButton = new AddNewButton(this);
	// return menuButton;
	// }

	private Button createMakeRecurringButton() {
		Button recurringButton = new Button();
		recurringButton.setText(Accounter.constants().makeItRecurring());
		recurringButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (transaction.getRecurringTransaction() == 0) {
					// create new recurring for this transaction
					openRecurringDialog();
				} else {
					// open existing recurring transaction.
					Accounter.createGETService().getObjectById(
							AccounterCoreType.RECURRING_TRANSACTION,
							transaction.getRecurringTransaction(),
							new AsyncCallback<ClientRecurringTransaction>() {

								@Override
								public void onFailure(Throwable caught) {
									Accounter
											.showError("Unable to open recurring transaction "
													+ caught);
								}

								@Override
								public void onSuccess(
										ClientRecurringTransaction result) {
									openRecurringDialog(result);
								}
							});
				}

			}
		});

		return recurringButton;
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		// FIXME > Need to complete Recurring transaction feature.
		// if (canRecur()) {
		// recurringButton = createMakeRecurringButton();
		// buttonBar.add(recurringButton);
		// }
		super.createButtons(buttonBar);
	}

	// for new recurring
	private void openRecurringDialog() {
		openRecurringDialog(null);
	}

	// for editing existing recurring
	private void openRecurringDialog(ClientRecurringTransaction result) {

		RecurringTransactionDialog dialog = null;
		if (result == null) {
			dialog = new RecurringTransactionDialog(this);
		} else {
			dialog = new RecurringTransactionDialog(result);
		}

		dialog.setCallback(new ActionCallback<ClientRecurringTransaction>() {

			@Override
			public void actionResult(ClientRecurringTransaction result) {
				System.out.println("Recurring result" + result);
			}
		});
		dialog.show();
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub
		super.onAddNew();
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public ClientTransaction getTransactionObject() {
		return transaction;
	}

	public void setTransactionObject(T transactionObject) {
		this.transaction = transactionObject;
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
		paymentMethodSelect.setDisabled(isInViewMode());
		// formItems.add(paymentMethodSelect);

		return paymentMethodSelect;

	}

	protected void paymentMethodSelected(String paymentmethod) {
		this.paymentMethod = paymentmethod;
	}

	@Override
	public void init() {
		super.init();
		createControls();
		createHistoryView();
		setSize("100%", "100%");
	}

	/**
	 * Creates HistoryView
	 */
	private void createHistoryView() {
		VerticalPanel transactionHistoryPanel = new VerticalPanel();
		// TODO Create History View Here
		this.add(transactionHistoryPanel);
	}

	@Override
	public void initData() {

		initTransactionViewData();

		super.initData();

	}

	@Override
	public void setData(T data) {
		super.setData(data);
		if (data != null)
			transaction = data;
		else
			transaction = null;

	}

	@Override
	public void disableUserEntry() {

		// if (this.formItems != null) {
		//
		// for (FormItem item : formItems) {
		// // item.disable();
		// }
		// }
	}

	// public void setShowPriceWithVat(boolean showPriceWithVat) {
	// this.showPriceWithVat = showPriceWithVat;
	// }

	public boolean isShowPriceWithVat() {
		return isVATInclusive;
	}

	public void setMenuItems(Widget button, Map<String, ImageResource> items) {
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

	public void setMenuItems(Widget button, String... items) {
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
			if (itm.equalsIgnoreCase(Accounter.messages().accounts(
					Global.get().Account()))) {
				image = Accounter.getFinanceMenuImages().Accounts();
			} else if (itm.equals(Accounter.constants().productOrServiceItem())) {
				if (sellProducts) {
					image = Accounter.getFinanceMenuImages().items();
				} else {
					continue;
				}
			} else if (itm.equals(Accounter.constants().comment())) {
				image = Accounter.getFinanceMenuImages().comments();
			} else if (itm.equals("Sales Tax") || (itm.equals("Service Item"))
					|| (itm.equals("Service"))) {
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

	private void createPopupMenu(Widget button) {
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

	protected void addAccount() {

	}

	protected void addItem() {

	}

	@Override
	public void showMenu(Event event) {
		// int menuTop = menuButton.getAbsoluteTop()
		// - (popupMenuBar.numberOfItems * 43);
		//
		// int left = menuButton.getAbsoluteLeft() - 5;

		// popupPanel.setPopupPosition(left, menuTop + 1);
		// popupPanel.show();
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
	public void saveFailed(AccounterException exception) {
		transactionFailed(exception);
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {
		if (vatinclusiveCheck != null)
			vatinclusiveCheck.setDisabled(isInViewMode());
		// if (menuButton != null)
		// menuButton.setEnabled(!isInViewMode());
		setMode(EditMode.EDIT);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()
				&& classListCombo != null) {
			classListCombo.setDisabled(isInViewMode());
		}
	}

	public boolean isEdit() {
		return isInViewMode();
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		// if (menuButton != null) {
		// menuButton.setEnabled(!isInViewMode());
		// }
	}

	@Override
	public ValidationResult validate() {

		updateTransaction();

		ValidationResult result = new ValidationResult();
		// Validations
		// 1. Transaction total <= 0 ERROR
		// 2. TransactionItem line total <= 0 ERROR
		// 3. If(accountingType == UK)
		// if(taxCodeName == "New S" && transactionDate is before 4 Jan 2011)
		// ERROR
		if (transaction != null)
			if (this.transaction.getTotal() <= 0) {
				if (transaction instanceof ClientPayBill) {
					result.addError(
							this,
							Accounter.messages().valueCannotBe0orlessthan0(
									Accounter.constants().amount()));
				} else {
					if (!(this instanceof CustomerRefundView)
							&& !(this instanceof WriteChequeView))
						result.addError(this, Accounter.constants()
								.transactiontotalcannotbe0orlessthan0());
				}
			}
		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()
				&& getPreferences().isWarnOnEmptyClass()
				&& this.transaction.getAccounterClass() == null) {
			result.addWarning(classListCombo, AccounterWarningType.EMPTY_CLASS);
		}

		if (transactionItems != null) {
			for (ClientTransactionItem transactionItem : transactionItems) {
				if (transactionItem.getLineTotal() <= 0) {
					result.addError(
							"TransactionItem" + transactionItem.getAccount()
									+ transactionItem.getAccount(), Accounter
									.constants()
									.transactionitemtotalcannotbe0orlessthan0());
				}

				if (getPreferences().isClassTrackingEnabled()
						&& !getPreferences().isClassOnePerTransaction()
						&& getPreferences().isWarnOnEmptyClass()
						&& transactionItem.getClientAccounterClass() == null) {
					// TODO
				}
			}
		}

		return result;

	}

	/**
	 * For Location Combo
	 * 
	 * @return
	 */
	protected LocationCombo createLocationCombo() {
		LocationCombo locationCombo = new LocationCombo(Accounter.messages()
				.location(Global.get().Location()));
		locationCombo.setHelpInformation(true);
		locationCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLocation>() {

					@Override
					public void selectedComboBoxItem(ClientLocation selectItem) {
						locationSelected(selectItem);
					}
				});

		locationCombo
				.addNewLocationHandler(new ValueCallBack<ClientLocation>() {
					@Override
					public void execute(ClientLocation value) {
						addLoactionToTransaction(value);

					}

				});
		locationCombo.setDisabled(isInViewMode());
		return locationCombo;

	}

	/**
	 * add location to transaction.
	 * 
	 * @param location
	 */
	private void addLoactionToTransaction(final ClientLocation location) {

		// transaction.setLocation(location.getID());
		AccounterAsyncCallback<Long> asyncallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				caught.printStackTrace();
			}

			public void onResultSuccess(Long result) {
				Utility.updateClientList(location, getCompany().getLocations());
				location.setID(result);
				locationSelected(location);
			}

		};
		Accounter.createCRUDService().create(location, asyncallBack);
	}

	/**
	 * 
	 * @param selectItem
	 */
	protected void locationSelected(ClientLocation selectItem) {
		this.location = selectItem;
		if (location != null)
			locationCombo.setComboItem(this.location);
	}

	/**
	 * Updates the Transaction Obejct from the GUI Fields before saving.
	 */
	protected void updateTransaction() {

		if (transaction != null) {
			transaction.setDate(transactionDate.getDate());
			transaction.setType(transactionType);

			if (transactionNumber != null)
				transaction.setNumber(transactionNumber.getValue().toString());
			transactionItems.clear();
			List<ClientTransactionItem> list = getAllTransactionItems();
			if (list != null) {
				for (ClientTransactionItem item : list) {
					if (!item.isEmpty()) {
						transactionItems.add(item);
					}
				}
				transaction.setTransactionItems(transactionItems);
			}
			if (location != null)
				transaction.setLocation(location.getID());

			if (getPreferences().isClassTrackingEnabled()
					&& getPreferences().isClassOnePerTransaction()
					&& clientAccounterClass != null) {
				transaction.setAccounterClass(clientAccounterClass);
			}
		}
	}

	public List<ClientTransactionItem> getAllTransactionItems() {
		return null;
	}

	protected CurrencyWidget createCurrencyWidget() {
		// FIXME test only.

		List<String> currencies = new ArrayList<String>();
		String baseCurrency = null;
		for (int i = 0; i < 10; i++) {
			String currency = "CU" + i;
			currencies.add(currency);
			if (i == 5) {
				baseCurrency = currency;
			}
		}
		return new CurrencyWidget(currencies, baseCurrency);
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	public ClientVendor getVendor() {
		return vendor;
	}

	protected void billToaddressSelected(ClientAddress selectItem) {

		this.billingAddress = selectItem;
		if (this.billingAddress != null && billToCombo != null)
			billToCombo.setComboItem(this.billingAddress);
		else
			billToCombo.setValue("");

	}

	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		payFromCombo.setAccounts();
		payFromCombo.setDisabled(isInViewMode());
		payFromAccount = payFromCombo.getSelectedValue();
		if (payFromAccount != null)
			payFromCombo.setComboItem(payFromAccount);
	}

	public VendorCombo createVendorComboItem(String title) {

		VendorCombo vendorCombo = new VendorCombo(title != null ? title
				: Global.get().Vendor());
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(true);
		vendorCombo.setDisabled(isInViewMode());
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);

					}

				});

		// vendorCombo.setShowDisabled(false);
		return vendorCombo;

	}

	protected void vendorSelected(ClientVendor vendor) {

		if (vendor == null)
			return;
		this.setVendor(vendor);
		initContacts(vendor);
		// initPhones(vendor);
		paymentMethodSelected(vendor.getPaymentMethod());
		addressListOfVendor = vendor.getAddress();
		initBillToCombo();

	}

	protected void initBillToCombo() {

		if (billToCombo == null || addressListOfVendor == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_BILL_TO) {
				if (address != null) {
					tempSet.add(address);
					clientAddress = address;
					break;
				}
			}

		}
		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		billToCombo.initCombo(adressList);
		if (adressList == null || adressList.size() == 0)
			billToCombo.setDisabled(true);
		else
			billToCombo.setDisabled(isInViewMode());
		billToCombo.setDefaultToFirstOption(false);

		if (isInViewMode() && billingAddress != null) {
			billToCombo.setComboItem(billingAddress);
			return;
		}
		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		} else {
			billToCombo.setValue(null);
			// billToaddressSelected(clientAddress);
		}
	}

	public void initContacts(ClientVendor vendor) {
		if (contactCombo == null)
			return;
		this.contacts = vendor.getContacts();

		this.contact = vendor.getPrimaryContact();

		if (contacts != null && contacts.size() > 0) {
			List<ClientContact> contactList = new ArrayList<ClientContact>();
			contactList.addAll(contacts);
			contactCombo.initCombo(contactList);
			contactCombo.setDisabled(isInViewMode());

			if (contact != null && contacts.contains(contact)) {
				contactCombo.setComboItem(contact);
				contactSelected(contact);

			}
		} else {
			contactCombo.initCombo(null);
			contactCombo.setValue("");
		}
	}

	protected void contactSelected(ClientContact contact) {
		if (contact == null)
			return;
		this.contact = contact;
		this.phoneNo = contact.getBusinessPhone();
		if (this.phoneNo != null && phoneSelect != null) {
			phoneSelect.setValue(this.phoneNo);

		}
		contactCombo.setComboItem(contact);
		// contactCombo.setDisabled(isEdit);

	}

	public AddressCombo createBillToComboItem() {

		AddressCombo addressCombo = new AddressCombo(Accounter.constants()
				.billTo(), false);
		addressCombo.setDefaultToFirstOption(false);
		addressCombo.setHelpInformation(true);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setDisabled(isInViewMode());
		// addressCombo.setShowDisabled(false);

		return addressCombo;

	}

	public PayFromAccountsCombo createPayFromCombo(String title) {

		PayFromAccountsCombo payFromCombo = new PayFromAccountsCombo(title);
		payFromCombo.setHelpInformation(true);
		payFromCombo.setRequired(true);
		payFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountSelected(selectItem);
						// selectedAccount = (Account) selectItem;
						// adjustBalance();

					}

				});
		payFromCombo.setDisabled(isInViewMode());
		// payFromCombo.setShowDisabled(false);
		// formItems.add(payFromCombo);
		return payFromCombo;
	}

	private void accountSelected(ClientAccount account) {
		if (account == null)
			return;
		this.payFromAccount = account;
	}

	protected TextItem createCheckNumberItem(String title) {

		final TextItem checkNo = new TextItem(title);
		checkNo.setHelpInformation(true);
		checkNo.setDisabled(isInViewMode());
		// checkNo.setShowDisabled(false);
		if (transaction != null) {
			if (transactionType == ClientTransaction.TYPE_CASH_PURCHASE) {
				ClientCashPurchase clientCashPurchase = (ClientCashPurchase) transaction;
				checkNo.setValue(clientCashPurchase.getCheckNumber());
			}
		}
		return checkNo;

	}

	protected void initMemoAndReference() {

		if (this.isInViewMode()) {

			ClientPayBill payBill = (ClientPayBill) transaction;

			if (payBill != null) {
				memoTextAreaItem.setDisabled(true);
				setMemoTextAreaItem(payBill.getMemo());
				// setRefText(payBill.getReference());

			}
		}
	}

	/**
	 * Decides whether to add the "make it recurring" button to this view.
	 * Default value is <b>true</b>.
	 * 
	 * @return
	 */
	protected boolean canRecur() {
		return transaction != null && transaction.getID() != 0;
	}

	public ClassListCombo createAccounterClassListCombo() {
		classListCombo = new ClassListCombo("Class", true);
		classListCombo.initCombo(getCompany().getAccounterClasses());
		classListCombo.setHelpInformation(true);
		classListCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccounterClass>() {

					@Override
					public void selectedComboBoxItem(
							ClientAccounterClass selectItem) {
						classSelected(selectItem);
					}
				});

		classListCombo
				.addNewAccounterClassHandler(new ValueCallBack<ClientAccounterClass>() {

					@Override
					public void execute(
							final ClientAccounterClass accounterClass) {
						Accounter.createCRUDService().create(accounterClass,
								new AsyncCallback<Long>() {

									@Override
									public void onSuccess(Long result) {
										accounterClass.setID(result);
										classSelected(accounterClass);
										getCompany().getAccounterClasses().add(
												accounterClass);
									}

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
									}
								});
					}
				});

		classListCombo.setDisabled(isInViewMode());

		return classListCombo;
	}

	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		if (clientAccounterClass != null) {
			this.clientAccounterClass = clientAccounterClass;
			classListCombo.setComboItem(clientAccounterClass);
		}
	}

	public ArrayList<ClientAccounterClass> getClientAccounterClasses() {
		return clientAccounterClasses;
	}

	public void setClientAccounterClasses(
			ArrayList<ClientAccounterClass> clientAccounterClasses) {
		this.clientAccounterClasses = clientAccounterClasses;
	}

	public ClientAccounterClass getClientAccounterClass() {
		return clientAccounterClass;
	}

	public void setClientAccounterClass(
			ClientAccounterClass clientAccounterClass) {
		this.clientAccounterClass = clientAccounterClass;
	}

	public List<ClientTransactionItem> getAccountTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : transactionItems) {
			if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				list.add(item);
			}
		}
		return list;
	}

	public List<ClientTransactionItem> getItemTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : transactionItems) {
			if (item.getType() == ClientTransactionItem.TYPE_ITEM) {
				list.add(item);
			}
		}
		return list;
	}

	public boolean isTrackTax() {
		if (transaction != null && transaction.haveTax()) {
			return true;
		} else {
			return getPreferences().isTrackTax();
		}
	}

	public boolean isTaxPerDetailLine() {
		if (transaction != null && transaction.usesDifferentTaxCodes()) {
			return true;
		} else {
			return getPreferences().isTaxPerDetailLine();
		}
	}

	public boolean isTrackPaidTax() {
		if (transaction != null && transaction.haveTax()) {
			return true;
		} else {
			return getPreferences().isTrackPaidTax();
		}
	}

}