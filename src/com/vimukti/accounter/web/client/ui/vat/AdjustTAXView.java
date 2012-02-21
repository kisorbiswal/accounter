package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AdjustmentVATItemCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AdjustTAXView extends
		AbstractTransactionBaseView<ClientTAXAdjustment> {

	private DateItem adjustDate;
	protected TextItem entryNo;
	private TAXAgencyCombo taxAgencyCombo;
	private AdjustmentVATItemCombo vatItemCombo;
	private OtherAccountsCombo adjustAccountCombo;
	private RadioGroupItem typeRadio;
	private RadioGroupItem salesTypeRadio;
	private AmountField amount;
	private TextAreaItem memo;
	private DynamicForm vatform;
	// private ClientTAXItem clientVATItem;
	private ClientTAXAgency clientTAXAgency;

	// private AccounterAsyncCallback<Boolean> refreshFileVat;
	private ClientTAXAgency taxAgency;
	// private static TextItem vatLine, vatLinetxt, vatAccounttxt, vatAccount;
	private LabelItem vatLine, vatLinetxt, vatAccounttxt, vatAccount;
	private ArrayList<DynamicForm> listforms;

	public AdjustTAXView() {
		super(ClientTransaction.TYPE_ADJUST_VAT_RETURN);
	}

	public AdjustTAXView(ClientTAXAgency taxAgency) {
		super(ClientTransaction.TYPE_ADJUST_VAT_RETURN);
		this.taxAgency = taxAgency;
	}

	@Override
	public void init() {
		super.init();
		setSize("100%", "100%");
		if (!isInViewMode()) {
			initEntryNumber();
		}
	}

	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		Label infoLabel;
		infoLabel = new Label(messages.taxAdjustment());
		infoLabel.removeStyleName("gwt-Label");

		infoLabel.setStyleName("label-title");
		// infoLabel.setHeight("35px");
		adjustDate = new DateItem(null);
		adjustDate.setHelpInformation(true);
		adjustDate.setDatethanFireEvent(new ClientFinanceDate());
		adjustDate.setDisabled(isInViewMode());
		// adjustDate.setWidth(100);

		entryNo = new IntegerField(this, messages.no());
		entryNo.setToolTip(messages.giveNoTo(this.getAction().getViewName()));
		entryNo.setHelpInformation(true);
		entryNo.setWidth(100);
		entryNo.setDisabled(isInViewMode());

		taxAgencyCombo = new TAXAgencyCombo(messages.taxAgency());
		taxAgencyCombo.setHelpInformation(true);
		// taxAgencyCombo.setWidth(100);
		taxAgencyCombo.setComboItem(taxAgency);
		taxAgencyCombo.setDisabled(isInViewMode());

		vatItemCombo = new AdjustmentVATItemCombo(messages.taxItem(), taxAgency);
		vatItemCombo.setHelpInformation(true);
		vatItemCombo.initCombo(vatItemCombo.getVATItmesByVATAgncy(taxAgency));
		vatItemCombo.setDisabled(isInViewMode());
		// vatItemCombo.setWidth(100);

		vatLine = new LabelItem();
		vatLine.setValue(messages.taxLine());

		vatAccount = new LabelItem();
		vatAccount.setValue(messages.taxAccount());

		vatLinetxt = new LabelItem();
		vatAccounttxt = new LabelItem();

		vatLinetxt.setValue(" ");
		vatAccounttxt.setValue("  ");

		vatform = new DynamicForm();
		// vatform.setWidth("64%");

		vatform.setNumCols(4);

		vatform.setFields(vatLine, vatLinetxt, vatAccount, vatAccounttxt);
		vatItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						// clientVATItem = selectItem;
						refreshVatLineLabel(vatLinetxt,
								selectItem.getVatReturnBox());

						refreshVatAccountLabel(vatAccounttxt, selectItem);

					}
				});

		// if (taxAgencyCombo.getSelectedValue().equals("")) {
		// clientT = taxAgencyCombo.ge;
		// } else {
		clientTAXAgency = taxAgencyCombo.getSelectedValue();
		// }
		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						amount.setCurrency(getCompany().getCurrency(
								selectItem.getCurrency()));
						if (selectItem == null) {
							clientTAXAgency = selectItem;
							vatItemCombo.setDisabled(true);
							vatLine.setValue("");
							vatAccount.setValue("");

						} else {
							clientTAXAgency = selectItem;
							vatItemCombo.setDisabled(false);
							vatItemCombo.initCombo(vatItemCombo
									.getVATItmesByVATAgncy(selectItem));
							vatItemCombo.setValue("");
						}
						if (selectItem != null) {
							salesTypeRadio.setVisible(selectItem
									.getSalesLiabilityAccount() != 0
									&& selectItem.getPurchaseLiabilityAccount() != 0);
						}
						vatItemCombo.setTaxAgency(selectItem);
					}
				});
		// vatform.getCellFormatter().setWidth(0, 1, "182");

		taxAgencyCombo.setRequired(true);
		vatItemCombo.setRequired(true);

		adjustAccountCombo = new OtherAccountsCombo(
				messages.adjustmentAccount());
		adjustAccountCombo.setHelpInformation(true);
		// adjustAccountCombo.setWidth(100);
		adjustAccountCombo.setPopupWidth("600px");
		adjustAccountCombo.setRequired(true);
		adjustAccountCombo.setDisabled(isInViewMode());
		adjustAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						ClientCurrency currency = getCompany().getCurrency(
								selectItem.getCurrency());
						currencyWidget.setSelectedCurrencyFactorInWidget(
								currency, adjustDate.getDate().getDate());
						if (isMultiCurrencyEnabled()) {
							setCurrency(currency);
							setCurrencyFactor(currencyWidget
									.getCurrencyFactor());
							updateAmountsFromGUI();
						}
					}
				});

		currencyWidget = createCurrencyFactorWidget();

		amount = new AmountField(messages.amount(), this, getBaseCurrency());
		amount.setHelpInformation(true);
		amount.setRequired(true);
		amount.setWidth(100);
		amount.setDisabled(isInViewMode());
		typeRadio = new RadioGroupItem("");
		// typeRadio.setRequired(true);
		typeRadio.setValueMap(messages.increaseTAXLine(),
				messages.decreaseTAXLine());
		typeRadio.setDefaultValue(messages.increaseTAXLine());
		typeRadio.setDisabled(isInViewMode());

		salesTypeRadio = new RadioGroupItem();
		salesTypeRadio.setGroupName(messages.type());
		salesTypeRadio.setValue(messages.salesType(), messages.purchaseType());
		salesTypeRadio.setDefaultValue(messages.salesType());
		salesTypeRadio.setDisabled(isInViewMode());

		memo = new TextAreaItem(messages.memo());
		memo.setMemo(false, this);
		memo.setHelpInformation(true);
		memo.setWidth(100);
		memo.setDisabled(isInViewMode());
		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(4);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(adjustDate, entryNo);
		// dateForm.getCellFormatter().setWidth(0, 0, "189");
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		datepanel.setCellHorizontalAlignment(dateForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		DynamicForm topform = new DynamicForm();
		topform.addStyleName("fields-panel");
		// if (getCompany().getPreferences().isChargeSalesTax()) {
		// topform.setFields(taxAgencyCombo);
		// } else {
		topform.setFields(taxAgencyCombo, vatItemCombo, salesTypeRadio);
		// }

		// topform.setWidth("50%");
		// topform.getCellFormatter().setWidth(0, 0, "190");

		DynamicForm memoForm = new DynamicForm();
		memoForm.addStyleName("fields-panel");
		// memoForm.setWidth("50%");
		memoForm.setFields(adjustAccountCombo, amount, typeRadio, memo);
		memoForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(currencyWidget);
		// memoForm.getCellFormatter().setWidth(0, 0, "190");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.add(infoLabel);
		mainPanel.add(voidedPanel);
		mainPanel.add(datepanel);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(topform);
		// if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// mainPanel.add(vatform);
		// }
		mainPanel.add(horizontalPanel);
		mainPanel.add(memoForm);
		mainPanel.setSpacing(10);

		if (isMultiCurrencyEnabled()) {
			horizontalPanel.add(currencyWidget);
			horizontalPanel.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			horizontalPanel.setCellWidth(currencyWidget, "50%");
		}
		this.add(mainPanel);
		horizontalPanel.setWidth("100%");
		horizontalPanel.setCellHorizontalAlignment(topform,
				HasHorizontalAlignment.ALIGN_LEFT);
		horizontalPanel.setCellWidth(topform, "50%");

		listforms.add(memoForm);
		listforms.add(topform);
		settabIndexes();

	}

	private void initEntryNumber() {

		AccounterAsyncCallback<String> transactionNumberCallback = new AccounterAsyncCallback<String>() {

			public void onException(AccounterException caught) {
				Accounter.showError(messages.failedToGetTransactionNumber());
			}

			public void onResultSuccess(String result) {
				if (result == null) {
					onFailure(new Exception());
				}

				// transactionNumber.setValue(String.valueOf(result));
				entryNo.setValue(result);

			}

		};

		this.rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_ADJUST_VAT_RETURN,
				transactionNumberCallback);

	}

	@Override
	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public void setFocus() {
		this.taxAgencyCombo.setFocus();
	}

	protected void refreshVatAccountLabel(LabelItem vatAccountLabel,
			ClientTAXItem selectItem) {

		if (this.clientTAXAgency != null) {

			if (selectItem.isSalesType()) {

				ClientAccount salesAccount = Accounter.getCompany().getAccount(
						this.clientTAXAgency.getSalesLiabilityAccount());

				vatAccountLabel.setValue(salesAccount != null ? salesAccount
						.getName() : "");

			} else {

				ClientAccount purchaseAccount = getCompany().getAccount(
						this.clientTAXAgency.getPurchaseLiabilityAccount());

				vatAccountLabel
						.setValue(purchaseAccount != null ? purchaseAccount
								.getName() : "");

			}

		}

	}

	protected void refreshVatLineLabel(LabelItem vatLineLabel,
			long vatReturnBoxID) {

		ClientVATReturnBox box = Accounter.getCompany().getVatReturnBoxByID(
				vatReturnBoxID);

		vatLineLabel.setValue(box != null ? box.getVatBox() : "");

	}

	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .failedToApplyChanges());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		// addError(this, messages.failedToApplyChanges());
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		// switch (this.validationCount) {
		//
		// case 4:
		List<DynamicForm> forms = this.getForms();
		for (DynamicForm form : forms) {
			if (form != null) {
				result.add(form.validate());
			}
		}
		// return true;
		// case 3:
		if (AccounterValidator.isZeroAmount(amount.getAmount())) {
			result.addError(amount, messages.shouldNotbeZero(amount.getName()));
		} else if (AccounterValidator.isNegativeAmount(amount.getAmount())) {
			result.addError(amount, messages.shouldBePositive(amount.getName()));
		}

		// case 2:
		// if (!adjustAccountCombo.validate()) {
		// throw new InvalidEntryException(
		// AccounterErrorType.REQUIRED_FIELDS);
		// }
		// return true;

		// case 1:
		// if (accountType == 0)
		// return true;
		// else {
		// if (!vatItemCombo.validate()) {
		// throw new InvalidEntryException(
		// AccounterErrorType.REQUIRED_FIELDS);
		// }
		// return true;
		// }

		// default:
		return result;

		// }
	}

	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	@Override
	public ClientTAXAdjustment saveView() {
		ClientTAXAdjustment saveView = super.saveView();
		if (saveView != null) {
			updateData();
		}
		return saveView;
	}

	private void updateData() {

		data.setNumber(entryNo.getValue().toString());

		data.setTransactionDate(adjustDate.getDate().getDate());

		// vatAdjustment.setVatAgency(clientVATAgency.getID());s
		if (vatItemCombo.getSelectedValue() != null) {
			data.setTaxItem(vatItemCombo.getSelectedValue().getID());
		}
		if (clientTAXAgency != null) {
			data.setTaxAgency(clientTAXAgency.getID());
			if (clientTAXAgency.getSalesLiabilityAccount() != 0
					&& clientTAXAgency.getPurchaseLiabilityAccount() != 0) {
				data.setSales(salesTypeRadio.getValue().equals(
						messages.salesType()));
			} else {
				data.setSales(clientTAXAgency.getSalesLiabilityAccount() != 0);
			}
		}
		if (adjustAccountCombo.getSelectedValue() != null) {
			data.setAdjustmentAccount(adjustAccountCombo.getSelectedValue()
					.getID());
		}

		data.setTotal(amount.getAmount());
		if (typeRadio.getValue() != null ? typeRadio.getValue().equals(
				messages.increaseTAXLine()) : false)
			data.setIncreaseVATLine(true);
		else
			data.setIncreaseVATLine(false);
		data.setMemo(String.valueOf(memo.getValue()));
		if (currency != null)
			data.setCurrency(currency.getID());
		data.setCurrencyFactor(currencyWidget.getCurrencyFactor());
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

	@Override
	public void onEdit() {
		enableFormItems();
	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		adjustDate.setDisabled(isInViewMode());
		entryNo.setDisabled(isInViewMode());
		taxAgencyCombo.setDisabled(isInViewMode());
		vatItemCombo.setDisabled(isInViewMode());
		salesTypeRadio.setDisabled(isInViewMode());
		adjustAccountCombo.setDisabled(isInViewMode());
		amount.setDisabled(isInViewMode());
		typeRadio.setDisabled(isInViewMode());
		memo.setDisabled(isInViewMode());
		currencyWidget.setDisabled(isInViewMode());
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.taxAdjustment();
	}

	private void settabIndexes() {
		taxAgencyCombo.setTabIndex(1);
		vatItemCombo.setTabIndex(2);
		adjustAccountCombo.setTabIndex(3);
		amount.setTabIndex(4);
		memo.setTabIndex(5);
		adjustDate.setTabIndex(6);
		entryNo.setTabIndex(7);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(8);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(9);
		cancelButton.setTabIndex(10);

	}

	@Override
	protected void initTransactionViewData() {
		if (data == null) {
			setData(new ClientTAXAdjustment());
		} else {
			adjustDate
					.setValue(new ClientFinanceDate(data.getTransactionDate()));
			entryNo.setValue(data.getNumber());
			clientTAXAgency = getCompany().getTaxAgency(data.getTaxAgency());
			taxAgencyCombo.setComboItem(clientTAXAgency);
			vatItemCombo.setComboItem(getCompany()
					.getTAXItem(data.getTaxItem()));
			if (data.isSales()) {
				salesTypeRadio.setDefaultValue(messages.salesType());
			} else {
				salesTypeRadio.setDefaultValue(messages.purchaseType());
			}
			adjustAccountCombo.setComboItem(getCompany().getAccount(
					data.getAdjustmentAccount()));
			amount.setAmount(data.getTotal());
			if (data.isIncreaseVATLine()) {
				typeRadio.setDefaultValue(messages.increaseTAXLine());
			} else {
				typeRadio.setDefaultValue(messages.decreaseTAXLine());
			}
			memo.setValue(data.getMemo());
			if (isMultiCurrencyEnabled()) {
				if (transaction.getCurrency() > 0) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPreferences()
							.getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				if (this.currency != null) {
					currencyWidget.setSelectedCurrency(this.currency);
				}
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}
		}
	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmountsFromGUI() {
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

}
