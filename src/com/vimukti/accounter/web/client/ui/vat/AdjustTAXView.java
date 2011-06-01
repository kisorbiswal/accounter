package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
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
	private VATItemCombo vatItemCombo;
	private OtherAccountsCombo adjustAccountCombo;
	private RadioGroupItem typeRadio;
	private AmountField amount;
	private TextAreaItem memo;
	private DynamicForm vatform;

	private ClientTAXItem clientVATItem;
	private ClientTAXAgency clientTAXAgency;

	private ClientTAXAdjustment taxAdjustment;
	@SuppressWarnings("unused")
	private AsyncCallback<Boolean> refreshFileVat;
	private ClientTAXAgency taxAgency;
	// private static TextItem vatLine, vatLinetxt, vatAccounttxt, vatAccount;
	private LabelItem vatLine, vatLinetxt, vatAccounttxt, vatAccount;
	private ArrayList<DynamicForm> listforms;

	public AdjustTAXView() {
		super();
		this.validationCount = 4;
	}

	public AdjustTAXView(ClientTAXAgency taxAgency) {
		super();
		this.validationCount = 4;
		this.taxAgency = taxAgency;
	}

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
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			infoLabel = new Label(FinanceApplication.getVATMessages()
					.taxAdjustment());
		else
			infoLabel = new Label(FinanceApplication.getVATMessages()
					.VATAdjustment());

		infoLabel.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());
		// infoLabel.setHeight("35px");
		adjustDate = new DateItem(null);
		adjustDate.setHelpInformation(true);
		adjustDate.setDatethanFireEvent(new ClientFinanceDate());
		// adjustDate.setWidth(100);

		entryNo = new IntegerField(FinanceApplication.getCustomersMessages()
				.no());
		entryNo.setHelpInformation(true);
		entryNo.setWidth(100);

		taxAgencyCombo = new TAXAgencyCombo(FinanceApplication.getVATMessages()
				.VATAgency());
		taxAgencyCombo.setHelpInformation(true);
		// taxAgencyCombo.setWidth(100);
		taxAgencyCombo.setComboItem(taxAgency);

		vatItemCombo = new VATItemCombo(FinanceApplication.getVATMessages()
				.VATItem(), taxAgency);
		vatItemCombo.setHelpInformation(true);
		vatItemCombo.initCombo(vatItemCombo.getVATItmesByVATAgncy(taxAgency));
		// vatItemCombo.setWidth(100);

		if (taxAgency == null) {
			vatItemCombo.setDisabled(true);
		}

		vatLine = new LabelItem();
		vatLine.setValue(FinanceApplication.getVATMessages().VATLine());

		vatAccount = new LabelItem();
		vatAccount.setValue(FinanceApplication.getVATMessages().VATAccount());

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
						clientVATItem = selectItem;
						refreshVatLineLabel(vatLinetxt, selectItem
								.getVatReturnBox());

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
							vatItemCombo.initCombo(FinanceApplication
									.getCompany().getTaxItems(selectItem));
						}

					}
				});
		vatform.getCellFormatter().setWidth(0, 1, "182");

		taxAgencyCombo.setRequired(true);
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			vatItemCombo.setRequired(false);
		else
			vatItemCombo.setRequired(true);

		adjustAccountCombo = new OtherAccountsCombo(FinanceApplication
				.getVATMessages().adjustmentAccount());
		adjustAccountCombo.setHelpInformation(true);
		// adjustAccountCombo.setWidth(100);
		adjustAccountCombo.setPopupWidth("600px");
		adjustAccountCombo.setRequired(true);
		amount = new AmountField(FinanceApplication.getVATMessages().amount());
		amount.setHelpInformation(true);
		amount.setRequired(true);
		amount.setWidth(100);
		typeRadio = new RadioGroupItem("");
		// typeRadio.setRequired(true);
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			typeRadio.setValueMap(FinanceApplication.getVATMessages()
					.increaseVATLine(), FinanceApplication.getVATMessages()
					.decreaseVATLine());
			typeRadio.setDefaultValue(FinanceApplication.getVATMessages()
					.increaseVATLine());
		} else {
			typeRadio.setValueMap(FinanceApplication.getCompanyMessages()
					.increaseTAXLine(), FinanceApplication.getCompanyMessages()
					.decreaseTAXLine());
			typeRadio.setDefaultValue(FinanceApplication.getCompanyMessages()
					.increaseTAXLine());

		}

		memo = new TextAreaItem(FinanceApplication.getVATMessages().memo());
		memo.setMemo(false);
		memo.setHelpInformation(true);
		memo.setWidth(100);
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			taxAgencyCombo.setTitle(FinanceApplication.getVATMessages()
					.taxAgency());
			vatItemCombo
					.setTitle(FinanceApplication.getVATMessages().taxItem());
			vatLine.setValue(FinanceApplication.getVATMessages().taxLine());
			vatAccount.setValue(FinanceApplication.getVATMessages()
					.taxAccount());
		}
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
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			topform.setFields(taxAgencyCombo);
		else
			topform.setFields(taxAgencyCombo, vatItemCombo);

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
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			mainPanel.add(vatform);
		mainPanel.add(memoForm);
		mainPanel.setSpacing(10);

		canvas.add(mainPanel);
		listforms.add(memoForm);
		listforms.add(topform);

	}

	private void initEntryNumber() {

		AsyncCallback<String> transactionNumberCallback = new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				Accounter.showError(FinanceApplication.getVATMessages()
						.FailedToGetTransactionNumber());
			}

			public void onSuccess(String result) {
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
		// TODO Auto-generated method stub

	}

	protected void refreshVatAccountLabel(LabelItem vatAccountLabel,
			ClientTAXItem selectItem) {

		@SuppressWarnings("unused")
		String str = "";
		if (this.clientTAXAgency != null) {

			if (selectItem.isSalesType()) {

				ClientAccount salesAccount = FinanceApplication.getCompany()
						.getAccount(this.clientTAXAgency.getSalesLiabilityAccount());

				vatAccountLabel.setValue(salesAccount != null ? salesAccount
						.getName() : "");

			} else {

				ClientAccount purchaseAccount = FinanceApplication.getCompany()
						.getAccount(
								this.clientTAXAgency.getPurchaseLiabilityAccount());

				vatAccountLabel
						.setValue(purchaseAccount != null ? purchaseAccount
								.getName() : "");

			}

		}

	}

	protected void refreshVatLineLabel(LabelItem vatLineLabel,
			String vatReturnBoxID) {

		ClientVATReturnBox box = FinanceApplication.getCompany()
				.getVatReturnBoxByID(vatReturnBoxID);

		vatLineLabel.setValue(box != null ? box.getVatBox() : "");

	}

	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(FinanceApplication.getVATMessages()
		// .failedToApplyChanges());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		MainFinanceWindow.getViewManager().showError(
				FinanceApplication.getVATMessages().failedToApplyChanges());

	}

	public void saveSuccess(IAccounterCore result) {
		if (taxAdjustment == null) {
			// Accounter.showInformation(FinanceApplication.getVATMessages()
			// .VATAdjustmentSuccessfull());
			removeFromParent();

		} else {
			// Accounter.showInformation(FinanceApplication.getVATMessages()
			// .VATItemUpdatedSuccessfully());
		}
		super.saveSuccess(result);

	}

	public boolean validate() throws Exception {
		switch (this.validationCount) {

		case 4:
			List<DynamicForm> forms = this.getForms();
			for (DynamicForm form : forms) {
				if (form != null) {
					form.validate(false);
				}
			}
			return true;
		case 3:
			return AccounterValidator.validateAmount(amount.getAmount());

		case 2:
			// if (!adjustAccountCombo.validate()) {
			// throw new InvalidEntryException(
			// AccounterErrorType.REQUIRED_FIELDS);
			// }
			return true;

		case 1:
			if (accountType == 0)
				return true;
			else {
				// if (!vatItemCombo.validate()) {
				// throw new InvalidEntryException(
				// AccounterErrorType.REQUIRED_FIELDS);
				// }
				return true;
			}

		default:
			return true;

		}
	}

	public void saveAndUpdateView() throws Exception {
		ClientTAXAdjustment vatItem = getObject();
		if (taxAdjustment == null)
			createObject(vatItem);
		else
			alterObject(vatItem);
	}

	public void setData(ClientTAXAdjustment data) {
		super.setData(data);
		if (data != null)
			taxAdjustment = (ClientTAXAdjustment) data;
		else
			taxAdjustment = null;
	}

	private ClientTAXAdjustment getObject() {

		ClientTAXAdjustment TAXadjust;

		if (taxAdjustment != null) {
			TAXadjust = taxAdjustment;
		} else {
			TAXadjust = new ClientTAXAdjustment();
		}

		TAXadjust.setNumber(entryNo.getValue().toString());

		TAXadjust.setTransactionDate(adjustDate.getDate().getTime());

		// vatAdjustment.setVatAgency(clientVATAgency.getStringID());s
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			TAXadjust.setTaxItem(null);
		else
			TAXadjust.setTaxItem(clientVATItem.getStringID());
		TAXadjust.setTaxAgency(clientTAXAgency.getStringID());

		TAXadjust.setAdjustmentAccount(adjustAccountCombo.getSelectedValue()
				.getStringID());

		TAXadjust.setTotal(amount.getAmount());
		if (typeRadio.getValue().equals(
				FinanceApplication.getVATMessages().increaseVATLine()))
			TAXadjust.setIncreaseVATLine(true);
		else
			TAXadjust.setIncreaseVATLine(false);
		TAXadjust.setMemo(String.valueOf(memo.getValue()));

		return TAXadjust;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.taxAgencyCombo.addComboItem((ClientTAXAgency) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemCombo.addComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.adjustAccountCombo.addComboItem((ClientAccount) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.taxAgencyCombo.removeComboItem((ClientTAXAgency) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemCombo.removeComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.adjustAccountCombo.removeComboItem((ClientAccount) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.taxAgencyCombo.updateComboItem((ClientTAXAgency) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemCombo.updateComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.adjustAccountCombo.updateComboItem((ClientAccount) core);
			break;
		}

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

}
