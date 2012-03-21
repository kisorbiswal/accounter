package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
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
import com.vimukti.accounter.web.client.ui.StyledPanel;
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
		this.getElement().setId("adjusttaxview");
	}

	public AdjustTAXView(ClientTAXAgency taxAgency) {
		super(ClientTransaction.TYPE_ADJUST_VAT_RETURN);
		this.getElement().setId("adjusttaxview");
		this.taxAgency = taxAgency;
	}

	@Override
	public void init() {
		super.init();
		// setSize("100%", "100%");
		if (!isInViewMode()) {
			initEntryNumber();
		}
	}

	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		Label infoLabel;
		infoLabel = new Label(messages.taxAdjustment());
		infoLabel.setStyleName("label-title-list");

		adjustDate = new DateItem(messages.date(), "adjustDate");
		adjustDate.setDatethanFireEvent(new ClientFinanceDate());
		adjustDate.setEnabled(isInViewMode());
		// adjustDate.setWidth(100);

		entryNo = new IntegerField(this, messages.no());
		entryNo.setToolTip(messages.giveNoTo(this.getAction().getViewName()));
		// entryNo.setWidth(100);
		entryNo.setEnabled(isInViewMode());

		taxAgencyCombo = new TAXAgencyCombo(messages.taxAgency());
		// taxAgencyCombo.setWidth(100);
		taxAgencyCombo.setComboItem(taxAgency);
		taxAgencyCombo.setEnabled(!isInViewMode());

		vatItemCombo = new AdjustmentVATItemCombo(messages.taxItem(), taxAgency);
		vatItemCombo.initCombo(vatItemCombo.getVATItmesByVATAgncy(taxAgency));
		vatItemCombo.setEnabled(!isInViewMode());
		// vatItemCombo.setWidth(100);

		vatLine = new LabelItem(messages.taxLine(), "vatLine");

		vatAccount = new LabelItem(messages.taxAccount(), "vatAccount");

		vatLinetxt = new LabelItem("", "vatLinetxt");
		vatAccounttxt = new LabelItem("", "vatAccounttxt");

		vatLinetxt.setValue(" ");
		vatAccounttxt.setValue("  ");

		vatform = new DynamicForm("vatform");
		// vatform.setWidth("64%");

		vatform.add(vatLine, vatLinetxt, vatAccount, vatAccounttxt);
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
							vatItemCombo.setEnabled(false);
							vatLine.setValue("");
							vatAccount.setValue("");

						} else {
							clientTAXAgency = selectItem;
							vatItemCombo.setEnabled(true);
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
		// adjustAccountCombo.setWidth(100);
		// adjustAccountCombo.setPopupWidth("600px");
		adjustAccountCombo.setRequired(true);
		adjustAccountCombo.setEnabled(!isInViewMode());
		amount = new AmountField(messages.amount(), this, getBaseCurrency(),
				"amount");
		amount.setRequired(true);
		amount.setEnabled(!isInViewMode());
		typeRadio = new RadioGroupItem("");
		// typeRadio.setRequired(true);
		typeRadio.setValueMap(messages.increaseTAXLine(),
				messages.decreaseTAXLine());
		typeRadio.setDefaultValue(messages.increaseTAXLine());
		typeRadio.setEnabled(isInViewMode());

		salesTypeRadio = new RadioGroupItem();
		salesTypeRadio.setGroupName(messages.type());
		salesTypeRadio.setValue(messages.salesType(), messages.purchaseType());
		salesTypeRadio.setDefaultValue(messages.salesType());
		salesTypeRadio.setEnabled(isInViewMode());

		memo = createMemoTextAreaItem();
		memo.setDisabled(isInViewMode());
		DynamicForm dateForm = new DynamicForm("dateForm");
		dateForm.setStyleName("datenumber-panel");
		dateForm.add(adjustDate, entryNo);
		// dateForm.getCellFormatter().setWidth(0, 0, "189");
		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateForm);
		// datepanel.setCellHorizontalAlignment(dateForm,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		DynamicForm topform = new DynamicForm("topform");
		topform.addStyleName("fields-panel");
		// if (getCompany().getPreferences().isChargeSalesTax()) {
		// topform.setFields(taxAgencyCombo);
		// } else {
		topform.add(taxAgencyCombo, vatItemCombo, salesTypeRadio);
		// }

		// topform.setWidth("50%");
		// topform.getCellFormatter().setWidth(0, 0, "190");

		DynamicForm memoForm = new DynamicForm("memoForm");
		memoForm.addStyleName("fields-panel");
		// memoForm.setWidth("50%");
		memoForm.add(adjustAccountCombo, amount, typeRadio, memo);
		// memoForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// memoForm.getCellFormatter().setWidth(0, 0, "190");

		StyledPanel mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(infoLabel);
		mainPanel.add(voidedPanel);
		mainPanel.add(datepanel);
		mainPanel.add(topform);
		// if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// mainPanel.add(vatform);
		// }
		mainPanel.add(memoForm);

		this.add(mainPanel);
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
			result.addError(amount, messages.shouldNotbeZero(amount.getTitle()));
		} else if (AccounterValidator.isNegativeAmount(amount.getAmount())) {
			result.addError(amount,
					messages.shouldBePositive(amount.getTitle()));
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
		adjustDate.setEnabled(isInViewMode());
		entryNo.setEnabled(isInViewMode());
		taxAgencyCombo.setEnabled(!isInViewMode());
		vatItemCombo.setEnabled(!isInViewMode());
		salesTypeRadio.setEnabled(isInViewMode());
		adjustAccountCombo.setEnabled(!isInViewMode());
		amount.setEnabled(isInViewMode());
		typeRadio.setEnabled(isInViewMode());
		memo.setDisabled(isInViewMode());
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
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {

	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}

}
