package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
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
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AdjustmentVATItemCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AdjustTAXView extends BaseView<ClientTAXAdjustment> {

	private DateItem adjustDate;
	protected TextItem entryNo;
	private TAXAgencyCombo taxAgencyCombo;
	private AdjustmentVATItemCombo vatItemCombo;
	private OtherAccountsCombo adjustAccountCombo;
	private RadioGroupItem typeRadio;
	private AmountField amount;
	private TextAreaItem memo;
	private DynamicForm vatform;
	AccounterConstants accounterConstants = Accounter.constants();
	// private ClientTAXItem clientVATItem;
	private ClientTAXAgency clientTAXAgency;

	// private ClientTAXAdjustment taxAdjustment;

	// private AccounterAsyncCallback<Boolean> refreshFileVat;
	private ClientTAXAgency taxAgency;
	// private static TextItem vatLine, vatLinetxt, vatAccounttxt, vatAccount;
	private LabelItem vatLine, vatLinetxt, vatAccounttxt, vatAccount;
	private ArrayList<DynamicForm> listforms;

	public AdjustTAXView() {
		super();
		// this.validationCount = 4;
	}

	public AdjustTAXView(ClientTAXAgency taxAgency) {
		super();
		// this.validationCount = 4;
		this.taxAgency = taxAgency;
	}

	@Override
	public void init() {
		super.init();
		// vatAdjustment = (ClientVATAdjustment) this.data;
		createControls();
		setSize("100%", "100%");
		initEntryNumber();
	}

	private void createControls() {
		listforms = new ArrayList<DynamicForm>();
		Label infoLabel;
		infoLabel = new Label(Accounter.constants().taxAdjustment());
		infoLabel.removeStyleName("gwt-Label");

		infoLabel.setStyleName(Accounter.constants().labelTitle());
		// infoLabel.setHeight("35px");
		adjustDate = new DateItem(null);
		adjustDate.setHelpInformation(true);
		adjustDate.setDatethanFireEvent(new ClientFinanceDate());
		// adjustDate.setWidth(100);

		entryNo = new IntegerField(this, Accounter.constants().no());
		entryNo.setToolTip(Accounter.messages().giveNoTo(
				this.getAction().getViewName()));
		entryNo.setHelpInformation(true);
		entryNo.setWidth(100);

		taxAgencyCombo = new TAXAgencyCombo(Accounter.constants().taxAgency());
		taxAgencyCombo.setHelpInformation(true);
		// taxAgencyCombo.setWidth(100);
		taxAgencyCombo.setComboItem(taxAgency);

		vatItemCombo = new AdjustmentVATItemCombo(Accounter.constants()
				.taxItem(), taxAgency);
		vatItemCombo.setHelpInformation(true);
		vatItemCombo.initCombo(vatItemCombo.getVATItmesByVATAgncy(taxAgency));
		// vatItemCombo.setWidth(100);

		if (taxAgency == null) {
			vatItemCombo.setDisabled(true);
		}

		vatLine = new LabelItem();
		vatLine.setValue(Accounter.constants().taxLine());

		vatAccount = new LabelItem();
		vatAccount.setValue(Accounter.messages().taxAccount(
				Global.get().Account()));

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

						if (selectItem == null) {
							clientTAXAgency = selectItem;
							vatItemCombo.setDisabled(true);
							vatLine.setValue("");
							vatAccount.setValue("");

						} else {
							clientTAXAgency = selectItem;
							vatItemCombo.setDisabled(false);
							vatItemCombo.initCombo(Accounter.getCompany()
									.getTaxItems(selectItem));
							vatItemCombo.setValue("");
						}

					}
				});
		vatform.getCellFormatter().setWidth(0, 1, "182");

		taxAgencyCombo.setRequired(true);
		vatItemCombo.setRequired(true);

		adjustAccountCombo = new OtherAccountsCombo(Accounter.messages()
				.adjustmentAccount(Global.get().Account()));
		adjustAccountCombo.setHelpInformation(true);
		// adjustAccountCombo.setWidth(100);
		adjustAccountCombo.setPopupWidth("600px");
		adjustAccountCombo.setRequired(true);
		amount = new AmountField(Accounter.constants().amount(), this);
		amount.setHelpInformation(true);
		amount.setRequired(true);
		amount.setWidth(100);
		typeRadio = new RadioGroupItem("");
		// typeRadio.setRequired(true);
		typeRadio.setValueMap(Accounter.constants().increaseTAXLine(),
				Accounter.constants().decreaseTAXLine());
		typeRadio.setDefaultValue(Accounter.constants().increaseTAXLine());

		memo = new TextAreaItem(Accounter.constants().memo());
		memo.setMemo(false, this);
		memo.setHelpInformation(true);
		memo.setWidth(100);
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
		// if (getCompany().getPreferences().isChargeSalesTax()) {
		// topform.setFields(taxAgencyCombo);
		// } else {
		topform.setFields(taxAgencyCombo, vatItemCombo);
		// }

		topform.setWidth("50%");
		topform.getCellFormatter().setWidth(0, 0, "190");

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("50%");
		memoForm.setFields(adjustAccountCombo, amount, typeRadio, memo);
		memoForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		memoForm.getCellFormatter().setWidth(0, 0, "190");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.add(infoLabel);
		mainPanel.add(datepanel);
		mainPanel.add(topform);
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			mainPanel.add(vatform);
		}
		mainPanel.add(memoForm);
		mainPanel.setSpacing(10);

		this.add(mainPanel);
		listforms.add(memoForm);
		listforms.add(topform);
		settabIndexes();

	}

	private void initEntryNumber() {

		AccounterAsyncCallback<String> transactionNumberCallback = new AccounterAsyncCallback<String>() {

			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.constants()
						.failedToGetTransactionNumber());
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
		// addError(this, Accounter.constants().failedToApplyChanges());
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
			result.addError(amount,
					Accounter.messages().shouldNotbeZero(amount.getName()));
		} else if (AccounterValidator.isNegativeAmount(amount.getAmount())) {
			result.addError(amount,
					Accounter.messages().shouldBePositive(amount.getName()));
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

	private void updateData() {

		data.setNumber(entryNo.getValue().toString());

		data.setTransactionDate(adjustDate.getDate().getDate());

		// vatAdjustment.setVatAgency(clientVATAgency.getID());s
		data.setTaxItem(vatItemCombo.getSelectedValue().getID());
		data.setTaxAgency(clientTAXAgency.getID());

		data.setAdjustmentAccount(adjustAccountCombo.getSelectedValue().getID());

		data.setTotal(amount.getAmount());
		if (typeRadio.getValue()
				.equals(Accounter.constants().increaseVATLine()))
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
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().taxAdjustment();
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientTAXAdjustment());
		}
		super.initData();
	}

	private void settabIndexes() {
		taxAgencyCombo.setTabIndex(1);
		vatItemCombo.setTabIndex(2);
		adjustAccountCombo.setTabIndex(3);
		amount.setTabIndex(4);
		memo.setTabIndex(5);
		adjustDate.setTabIndex(6);
		entryNo.setTabIndex(7);
		saveAndCloseButton.setTabIndex(8);
		saveAndNewButton.setTabIndex(9);
		cancelButton.setTabIndex(10);

	}
}
