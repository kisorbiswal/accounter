package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.core.Accounter;
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
	static final String ATTR_RATE = FinanceApplication.getCustomersMessages()
			.Rate();
	static final String ATTR_AS_OF = FinanceApplication.getCustomersMessages()
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
		info = FinanceApplication.getFinanceUIConstants().addorEdit() + " "
				+ title;
		validationCount = 1;
		// FinanceApplication.getFinanceUIConstants().codeToEnterNew()
		// + title
		// + FinanceApplication.getFinanceUIConstants()
		// .typeTheRateAndSelectDateIn() + title
		// + FinanceApplication.getFinanceUIConstants().settingsList();
	}

	protected void initCompany() {

		this.company = FinanceApplication.getCompany();

	}

	protected void updateCompany() {

		rpcGetService.getObjectById(AccounterCoreType.COMPANY, company
				.getStringID(), new AsyncCallback<ClientCompany>() {

			public void onFailure(Throwable caught) {
				Accounter.showError(FinanceApplication.getFinanceUIConstants()
						.failedtoUpdatetheCompany());

			}

			public void onSuccess(ClientCompany result) {

				if (result == null) {
					onFailure(new Exception());
					return;
				}

				FinanceApplication.setCompany(result);

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
		descriptionText.setTitle(FinanceApplication.getFinanceUIConstants()
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
		}, FinanceApplication.getFinanceUIConstants().taxable(),
				FinanceApplication.getFinanceUIConstants().nonTaxable());
		typeRadio.setDefaultValue(FinanceApplication.getFinanceUIConstants()
				.taxable());
		statusCheck = new CheckboxItem(FinanceApplication
				.getFinanceUIConstants().active());
		statusCheck.setValue(true);

		taxCodeForm = new DynamicForm();
		taxCodeForm.setWidth("100%");
		taxCodeForm = UIUtils.form(title
				+ FinanceApplication.getFinanceUIConstants().code());
		taxCodeForm.setFields(taxCodeText, descriptionText, typeRadio,
				statusCheck);

		Label taxRates = new Label(title + " "
				+ FinanceApplication.getFinanceUIConstants().rates());

		// initListGrid();

		HorizontalPanel buttonsLayout = new HorizontalPanel();
		buttonsLayout.setWidth("50%");

		Button button1 = new Button();
		button1.setWidth("80%");
		button1.setText(FinanceApplication.getFinanceUIConstants().ok());

		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// if (validForm()) {
				// saveAndClose = true;
				// createOrEditTaxCode();
				// }
			}
		});

		Button button2 = new Button();
		button2.setWidth("80%");
		button2.setTitle(FinanceApplication.getFinanceUIConstants().cancel());
		button2.setText(FinanceApplication.getFinanceUIConstants().cancel());
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				MainFinanceWindow.getViewManager().closeView(
						AddEditSalesTaxCodeView.this.getAction(), null);
			}
		});

		Button button3 = new Button();
		button3.setWidth("80%");
		button3.setTitle(FinanceApplication.getFinanceUIConstants().help());
		button3.setText(FinanceApplication.getFinanceUIConstants().help());
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// if (dialogButtonsHandler != null)
				// thirdButtonClick();
			}
		});

		buttonsLayout.add(button1);
		buttonsLayout.add(button2);
		buttonsLayout.add(button3);
		if (button1.isEnabled()) {
			button1.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(button1, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		if (button2.isEnabled()) {
			button2.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(button2, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		if (button3.isEnabled()) {
			button3.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(button3, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		VerticalPanel bodyLayout = new VerticalPanel();
		bodyLayout.setSize("100%", "100%");
		bodyLayout.add(infolabel);
		// bodyLayout.add(new Label(FinanceApplication.getFinanceUIConstants()
		// .enterTaxCode()));

		bodyLayout.add(taxCodeForm);
		// bodyLayout.add(taxRates);
		Button addButton = new Button(FinanceApplication
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
								Accounter.showError(FinanceApplication
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
				Accounter.showError(FinanceApplication.getFinanceUIConstants()
						.dateShouldNotBeNull());
				return false;

			}
		} catch (Exception e) {
			Accounter.showError(FinanceApplication.getFinanceUIConstants()
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
						+ FinanceApplication.getFinanceUIConstants()
								.rateRangeShould0to100();
				Accounter.showError(invalidRatealert);
				setValidRate(0.0);
				return;
			}
			setValidRate(rate);

		} catch (Exception e) {
			Accounter.showError(FinanceApplication.getFinanceUIConstants()
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
			typeRadio.setValue(takenTaxCode.isTaxable() ? FinanceApplication
					.getFinanceUIConstants().taxable() : FinanceApplication
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
		// taxCode.setTaxAgency(getSelectedTaxAgency().getStringID());

		// Setting status check
		taxCode.setTaxable(typeRadio.getValue().equals(
				FinanceApplication.getFinanceUIConstants().taxable()) ? true
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
		// if (core.getStringID().equals(
		// this.taxAgencyCombo.getSelectedValue().getStringID())) {
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

}
