package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTaxRates;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.SaleTaxCodeGrid;

public class AddEditSalesTaxCodeView extends BaseView<ClientTAXCode> {
	static final String ATTR_RATE = Accounter.getCustomersMessages()
			.Rate();
	static final String ATTR_AS_OF = Accounter.getCustomersMessages()
			.Asof();

	TextItem taxCodeText;
	TextItem descriptionText;
	// TaxAgencyCombo taxAgencyCombo;
	// TaxAgencyCombo taxAgencyCombo;
	// TaxCode takenTaxCode;

	CheckboxItem statusCheck;
	DynamicForm taxCodeForm;
	SaleTaxCodeGrid gridView;
	// private ClientTaxAgency selectedTaxAgency;
	private ClientTAXCode selectedTaxCode;
	protected ClientCompany company;
	private ClientTAXCode takenTaxCode;
	protected String changedValue;
	boolean isRateInValid;
	private String title;
	private String info;
	private double rateValue = 0.0;
	private ClientFinanceDate validDate;
	private RadioGroupItem typeRadio;

	public AddEditSalesTaxCodeView(String title) {
		this.title = title;
		info = Accounter.getFinanceUIConstants().addorEdit() + " "
				+ title;
		validationCount = 1;
		// FinanceApplication.getFinanceUIConstants().codeToEnterNew()
		// + title
		// + FinanceApplication.getFinanceUIConstants()
		// .typeTheRateAndSelectDateIn() + title
		// + FinanceApplication.getFinanceUIConstants().settingsList();
	}

	protected void initCompany() {

		this.company = Accounter.getCompany();

	}

	protected void updateCompany() {

		rpcGetService.getObjectById(AccounterCoreType.COMPANY, company
				.getID(), new AsyncCallback<ClientCompany>() {

			public void onFailure(Throwable caught) {
				Accounter.showError(Accounter.getFinanceUIConstants()
						.failedtoUpdatetheCompany());

			}

			public void onSuccess(ClientCompany result) {

				if (result == null) {
					onFailure(new Exception());
					return;
				}

				Accounter.setCompany(result);

			}
		});
	}

	private void initTaxAgencyCombo() {
		// List<ClientTaxAgency> list = FinanceApplication.getCompany()
		// .getActiveTaxAgencies();
		// if (taxAgencyCombo != null)
		// taxAgencyCombo.initCombo(list);
	}

	private void createControls() {

		Label infolabel = new Label(info);
		taxCodeText = new TextItem();
		taxCodeText.setWidth(100);
		taxCodeText.setTitle(title);
		taxCodeText.setRequired(true);
		descriptionText = new TextItem();
		descriptionText.setWidth(100);
		descriptionText.setTitle(Accounter.getFinanceUIConstants()
				.description());
		// taxAgencyCombo = new TaxAgencyCombo(FinanceApplication
		// .getFinanceUIConstants().taxAgency());
		// taxAgencyCombo.setWidth(100);
		// taxAgencyCombo.setRequired(true);

		// taxAgencyCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientTaxAgency>() {
		//
		// public void selectedComboBoxItem(ClientTaxAgency selectItem) {
		//
		// selectedTaxAgency = selectItem;
		//
		// }
		// });
		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);
		typeRadio.setValues(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changedValue = typeRadio.getValue().toString();
			}
		}, Accounter.getFinanceUIConstants().taxable(),
				Accounter.getFinanceUIConstants().nonTaxable());
		typeRadio.setDefaultValue(Accounter.getFinanceUIConstants()
				.taxable());
		statusCheck = new CheckboxItem(Accounter
				.getFinanceUIConstants().active());
		statusCheck.setValue(true);

		taxCodeForm = new DynamicForm();
		taxCodeForm.setWidth("100%");
		taxCodeForm = UIUtils.form(title
				+ Accounter.getFinanceUIConstants().code());
		taxCodeForm.setFields(taxCodeText, descriptionText, typeRadio,
				statusCheck);

		Label taxRates = new Label(title + " "
				+ Accounter.getFinanceUIConstants().rates());

		// initListGrid();

		HorizontalPanel buttonsLayout = new HorizontalPanel();
		buttonsLayout.setWidth("50%");

		AccounterButton button1 = new AccounterButton();
		button1.setWidth("80%");
		button1.setText(Accounter.getFinanceUIConstants().ok());

		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// if (validForm()) {
				// saveAndClose = true;
				// createOrEditTaxCode();
				// }
			}
		});

		AccounterButton button2 = new AccounterButton();
		button2.setWidth("80%");
		button2.setTitle(Accounter.getFinanceUIConstants().cancel());
		button2.setText(Accounter.getFinanceUIConstants().cancel());
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				MainFinanceWindow.getViewManager().closeView(
						AddEditSalesTaxCodeView.this.getAction(), null);
			}
		});

		AccounterButton button3 = new AccounterButton();
		button3.setWidth("80%");
		button3.setTitle(Accounter.getFinanceUIConstants().help());
		button3.setText(Accounter.getFinanceUIConstants().help());
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// if (dialogButtonsHandler != null)
				// thirdButtonClick();
			}
		});

		buttonsLayout.add(button1);
		buttonsLayout.add(button2);
		buttonsLayout.add(button3);
		button1.enabledButton();
		button2.enabledButton();
		button3.enabledButton();
		VerticalPanel bodyLayout = new VerticalPanel();
		bodyLayout.setSize("100%", "100%");
		bodyLayout.add(infolabel);
		// bodyLayout.add(new Label(FinanceApplication.getFinanceUIConstants()
		// .enterTaxCode()));

		bodyLayout.add(taxCodeForm);
		// bodyLayout.add(taxRates);
		AccounterButton addButton = new AccounterButton(Accounter
				.getFinanceUIConstants().addNew());
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientTaxRates taxRates = new ClientTaxRates();
				taxRates.setAsOf(new ClientFinanceDate().getTime());
				validateDateField(new ClientFinanceDate(), gridView
						.getRecords());
				gridView.addData(taxRates);

			}
		});
		// bodyLayout.add(addButton);
		// bodyLayout.add(gridView);
		// bodyLayout.add(buttonsLayout);
		setSize("100%", "100%");

		canvas.add(bodyLayout);

	}

	public void initListGrid() {
		gridView = new SaleTaxCodeGrid(false);
		gridView.setCurrentView(this);
		gridView.setCanEdit(true);
		gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		gridView.isEnable = false;
		gridView.init();
		gridView.setHeight("200px");
		gridView.setWidth("100%");

	}

	// validating the "as of" field for each record
	public boolean validateDateField(ClientFinanceDate selectedRecord,
			List<ClientTaxRates> records) {

		try {
			ClientFinanceDate selectedRecordDate = selectedRecord;

			if (selectedRecordDate != null) {
				// List<ClientTaxRates> records = gridView.getRecords();
				if (records.size() > 0) {

					for (ClientTaxRates obj : records) {
						ClientTaxRates taxRates = (ClientTaxRates) obj;
						if (!selectedRecord.equals(taxRates.getAsOf())) {
							ClientFinanceDate date = new ClientFinanceDate(
									taxRates.getAsOf());
							if (compareDate(date, selectedRecordDate)) {
								Accounter.showError(Accounter
										.getFinanceUIConstants()
										.dateShouldbeUnique());
								// setValidDate(new Date());
								return false;
							}

						}

					}
					setValidDate(selectedRecordDate);
					return true;
				} else {
					setValidDate(selectedRecordDate);
					return true;
				}
			} else {
				Accounter.showError(Accounter.getFinanceUIConstants()
						.dateShouldNotBeNull());
				return false;

			}
		} catch (Exception e) {
			Accounter.showError(Accounter.getFinanceUIConstants()
					.invalidDate());
			// setValidDate(new Date());
			return false;
		}
	}

	public void validateRateField(String selectedvalue) {
		String selectedRecordRate = selectedvalue;
		String invalidRatealert = " ";
		if (selectedRecordRate.endsWith("%"))
			selectedRecordRate = selectedRecordRate.substring(0,
					selectedRecordRate.length() - 1);
		try {
			Double rate = UIUtils.toDbl(selectedRecordRate);
			if (DecimalUtil.isLessThan(rate, 0.00)
					|| DecimalUtil.isGreaterThan(rate, 100.00)) {
				invalidRatealert = title
						+ Accounter.getFinanceUIConstants()
								.rateRangeShould0to100();
				Accounter.showError(invalidRatealert);
				setValidRate(0.0);
				return;
			}
			setValidRate(rate);

		} catch (Exception e) {
			Accounter.showError(Accounter.getFinanceUIConstants()
					.invalidInput());
			setValidRate(0.0);
		}

	}

	public boolean validForm() throws InvalidEntryException {

		if (!taxCodeForm.validate(false))
			// throw new
			// InvalidEntryException(AccounterErrorType.REQUIRED_FIELDS);
			return false;

		if ((takenTaxCode == null
				&& Utility.isObjectExist(company.getTaxCodes(), UIUtils
						.toStr(taxCodeText.getValue())) ? true : false)
				|| (takenTaxCode != null && !(takenTaxCode
						.getName()
						.equalsIgnoreCase(UIUtils.toStr(taxCodeText.getValue())) ? true
						: (Utility.isObjectExist(company.getTaxCodes(), UIUtils
								.toStr(taxCodeText.getValue())) ? false : true)))) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
			return false;
		}
		// if (gridView.getRecords().size() == 0) {
		// Accounter.showError(FinanceApplication.getFinanceUIConstants()
		// .provideAtleastOne()
		// + title + "Rate!");
		// return false;
		// }
		return true;

	}

	// public ClientTaxAgency getSelectedTaxAgency() {
	// return selectedTaxAgency;
	// }

	@Override
	public void init() {
		super.init();
		initCompany();
		createControls();
		initTaxAgencyCombo();

	}

	@Override
	public void initData() {
		this.takenTaxCode = (ClientTAXCode) this.getData();
		if (takenTaxCode != null) {
			taxCodeText.setValue(takenTaxCode.getName());
			if (takenTaxCode.getDescription() != null)
				descriptionText.setValue(takenTaxCode.getDescription());
			// selectedTaxAgency = FinanceApplication.getCompany().getTaxAgency(
			// takenTaxCode.getTaxAgency());
			// taxAgencyCombo.setComboItem(selectedTaxAgency);
			statusCheck.setValue(takenTaxCode.isActive());
			typeRadio.setValue(takenTaxCode.isTaxable() ? Accounter
					.getFinanceUIConstants().taxable() : Accounter
					.getFinanceUIConstants().nonTaxable());
			// Set<ClientTaxRates> clientTaxRates = (Set<ClientTaxRates>)
			// takenTaxCode
			// .getTaxRates();
			// List<ClientTaxRates> listRecord = new ArrayList<ClientTaxRates>(
			// clientTaxRates);

			// gridView.setRecords(listRecord);
		}

	}

	// creates a new tax code OR edits an existing tax code.
	protected void createOrEditTaxCode() {

		ClientTAXCode taxCode = getTaxCode();

		if (takenTaxCode == null)
			createObject(taxCode);
		else
			alterObject(taxCode);

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		switch (this.validationCount) {
		case 1:
			return validForm();
			// case 1:
			// return validDate();
		default:
			return true;
		}
	}

	private boolean validDate() {

		for (ClientTaxRates taxRate : gridView.getRecords()) {
			List<ClientTaxRates> records = gridView.getRecords();
			records.remove(taxRate);
			if (!validateDateField(new ClientFinanceDate(taxRate.getAsOf()),
					records))
				return false;
		}
		return true;
	}

	@Override
	public void saveAndUpdateView() throws Exception {

		ClientTAXCode taxCode = getTaxCode();

		if (takenTaxCode == null)
			createObject(taxCode);
		else
			alterObject(taxCode);

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (object == null)
			saveFailed(new Exception());
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML("Duplication of TaxCode is not allowed");
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		MainFinanceWindow.getViewManager().showError(
				"Duplication of TaxCode is not allowed");

	}

	private ClientTAXCode getTaxCode() {

		ClientTAXCode taxCode;

		if (takenTaxCode != null)
			taxCode = takenTaxCode;
		else
			taxCode = new ClientTAXCode();

		// Setting Tax Code
		taxCode.setName(UIUtils.toStr(taxCodeText.getValue()));

		// Setting description
		if (descriptionText.getValue() != null)
			taxCode.setDescription(descriptionText.getValue().toString());

		// Setting Tax Agency
		// taxCode.setTaxAgency(getSelectedTaxAgency().getID());

		// Setting status check
		taxCode.setTaxable(typeRadio.getValue().equals(
				Accounter.getFinanceUIConstants().taxable()) ? true
				: false);
		boolean isActive = (Boolean) statusCheck.getValue();
		taxCode.setActive(isActive);

		// Setting Tax Rates

		// List<ClientTaxRates> records = gridView.getRecords();
		// Set<ClientTaxRates> allTaxRates = new
		// HashSet<ClientTaxRates>(records);

		// taxCode.setTaxRates(allTaxRates);

		return taxCode;
	}

	public void setValidRate(double rate) {
		this.rateValue = rate;

	}

	public double getValidRate() {
		return this.rateValue;

	}

	public ClientFinanceDate getValidDate() {
		return validDate;
	}

	public void setValidDate(ClientFinanceDate validDate) {
		this.validDate = validDate;
	}

	@SuppressWarnings( { "deprecation" })
	private boolean compareDate(ClientFinanceDate date,
			ClientFinanceDate selectedRecordDate) {
		if ((date.getYear() == selectedRecordDate.getYear())
				&& date.getMonth() == selectedRecordDate.getMonth()
				&& date.getDate() == selectedRecordDate.getDate()) {
			return true;
		} else
			return false;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.taxCodeText.setFocus();
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
		// if (core.getID().equals(
		// this.taxAgencyCombo.getSelectedValue().getID())) {
		// this.taxAgencyCombo.addItemThenfireEvent((ClientTaxAgency) core);
		// }
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	protected String getViewTitle() {
		String constant;
		if (Accounter.getCompany().getAccountingType() == 1)
			constant = Accounter.getActionsConstants().newVatCode();
		else
			constant = Accounter.getActionsConstants().newTaxCode();
		return constant;
	}

}
