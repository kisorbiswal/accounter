package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTaxRates;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.SaleTaxCodeGrid;

public class AddEditSalesTaxCodeView extends BaseView<ClientTAXCode> {
	static final String ATTR_RATE = Accounter.constants().rate();
	static final String ATTR_AS_OF = Accounter.constants().asOf();

	TextItem taxCodeText;
	TextItem descriptionText;

	CheckboxItem statusCheck;
	DynamicForm taxCodeForm;
	SaleTaxCodeGrid gridView;
	AccounterConstants accounterConstants = Accounter.constants();
	// private ClientTAXCode selectedTaxCode;
	protected ClientCompany company;
	// private ClientTAXCode takenTaxCode;
	protected String changedValue;
	boolean isRateInValid;
	private String title;
	private String info;
	private double rateValue = 0.0;
	private ClientFinanceDate validDate;
	private RadioGroupItem typeRadio;

	public AddEditSalesTaxCodeView(String title) {
		this.title = title;
		info = Accounter.constants().addOrEdit() + " " + title;
	}

	protected void initCompany() {

		this.company = getCompany();

	}

	protected void updateCompany() {

		rpcGetService.getObjectById(AccounterCoreType.COMPANY, company.getID(),
				Accounter.getCompany().getID(),
				new AccounterAsyncCallback<ClientCompany>() {

					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants()
								.failedToUpdateCompany());

					}

					public void onResultSuccess(ClientCompany result) {

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
		descriptionText.setTitle(Accounter.constants().description());
		// taxAgencyCombo = new TaxAgencyCombo(FinanceApplication
		// .constants().taxAgency());
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
		}, Accounter.constants().taxable(), Accounter.constants().nonTaxable());
		typeRadio.setDefaultValue(Accounter.constants().taxable());
		statusCheck = new CheckboxItem(Accounter.constants().active());
		statusCheck.setValue(true);

		taxCodeForm = new DynamicForm();
		taxCodeForm.setWidth("100%");
		taxCodeForm = UIUtils.form(title + Accounter.constants().code());
		taxCodeForm.setFields(taxCodeText, descriptionText, typeRadio,
				statusCheck);

		Label taxRates = new Label(title + " " + Accounter.constants().rates());

		// initListGrid();

		VerticalPanel bodyLayout = new VerticalPanel();
		bodyLayout.setSize("100%", "100%");
		bodyLayout.add(infolabel);
		// bodyLayout.add(new Label(FinanceApplication.constants()
		// .enterTaxCode()));

		bodyLayout.add(taxCodeForm);
		// bodyLayout.add(taxRates);
		Button addButton = new Button(Accounter.constants().addNew());
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientTaxRates taxRates = new ClientTaxRates();
				taxRates.setAsOf(new ClientFinanceDate().getDate());
				validateDateField(new ClientFinanceDate(),
						gridView.getRecords());
				gridView.addData(taxRates);

			}
		});
		// bodyLayout.add(addButton);
		// bodyLayout.add(gridView);
		// bodyLayout.add(buttonsLayout);
		setSize("100%", "100%");

		add(bodyLayout);

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
								Accounter.showError(Accounter.constants()
										.dateShouldBeUnique());
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
				Accounter
						.showError(Accounter.constants().dateShouldNotBeNull());
				return false;

			}
		} catch (Exception e) {
			Accounter.showError(Accounter.constants().invalidDate());
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
						+ Accounter.constants().rateRangeShould0to100();
				Accounter.showError(invalidRatealert);
				setValidRate(0.0);
				return;
			}
			setValidRate(rate);

		} catch (Exception e) {
			Accounter.showError(Accounter.constants().invalidInput());
			setValidRate(0.0);
		}

	}

	// public boolean validForm() throws InvalidEntryException {
	//
	// if (!taxCodeForm.validate())
	// // throw new
	// // InvalidEntryException(AccounterErrorType.REQUIRED_FIELDS);
	// return false;
	//
	// if ((takenTaxCode == null
	// && Utility.isObjectExist(company.getTaxCodes(),
	// UIUtils.toStr(taxCodeText.getValue())) ? true : false)
	// || (takenTaxCode != null && !(takenTaxCode
	// .getName()
	// .equalsIgnoreCase(UIUtils.toStr(taxCodeText.getValue())) ? true
	// : (Utility.isObjectExist(company.getTaxCodes(),
	// UIUtils.toStr(taxCodeText.getValue())) ? false
	// : true)))) {
	// Accounter.showError(AccounterErrorType.ALREADYEXIST);
	// return false;
	// }
	// // if (gridView.getRecords().size() == 0) {
	// // Accounter.showError(FinanceApplication.constants()
	// // .provideAtleastOne()
	// // + title + "Rate!");
	// // return false;
	// // }
	// return true;
	//
	// }

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
		if (data == null) {
			setData(new ClientTAXCode());
		} else {
			taxCodeText.setValue(data.getName());
			if (data.getDescription() != null)
				descriptionText.setValue(data.getDescription());
			// selectedTaxAgency = FinanceApplication.getCompany().getTaxAgency(
			// takenTaxCode.getTaxAgency());
			// taxAgencyCombo.setComboItem(selectedTaxAgency);
			statusCheck.setValue(data.isActive());
			typeRadio.setValue(data.isTaxable() ? Accounter.constants()
					.taxable() : Accounter.constants().nonTaxable());
			// Set<ClientTaxRates> clientTaxRates = (Set<ClientTaxRates>)
			// takenTaxCode
			// .getTaxRates();
			// List<ClientTaxRates> listRecord = new ArrayList<ClientTaxRates>(
			// clientTaxRates);

			// gridView.setRecords(listRecord);
		}

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		result.add(taxCodeForm.validate());
		String taxCodeName = taxCodeText.getValue();
		ClientTAXCode taxCode = company.getTaxCodeByName(taxCodeName);
		if (taxCode != null && taxCode.getID() != this.getData().getID()) {
			result.addError(taxCodeText, accounterConstants.alreadyExist());
		}
		return result;
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
	public void saveAndUpdateView() {

		updateData();
		saveOrUpdate(getData());

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (object == null)
			saveFailed(new AccounterException());
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML("Duplication of TaxCode is not allowed");
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		// addError(this,
		// Accounter.constants().duplicationOfTaxCodeIsNotAllowed());
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

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

	@SuppressWarnings({ "deprecation" })
	private boolean compareDate(ClientFinanceDate date,
			ClientFinanceDate selectedRecordDate) {
		if ((date.getYear() == selectedRecordDate.getYear())
				&& date.getMonth() == selectedRecordDate.getMonth()
				&& date.getDay() == selectedRecordDate.getDay()) {
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
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		String constant;
		if (getCompany().getAccountingType() == 1)
			constant = Accounter.constants().newVATCode();
		else
			constant = Accounter.constants().newTaxCode();
		return constant;
	}

	private void updateData() {

		// Setting Tax Code
		data.setName(UIUtils.toStr(taxCodeText.getValue()));

		// Setting description
		if (descriptionText.getValue() != null)
			data.setDescription(descriptionText.getValue().toString());

		// Setting Tax Agency
		// taxCode.setTaxAgency(getSelectedTaxAgency().getID());

		// Setting status check
		data.setTaxable(typeRadio.getValue().equals(
				Accounter.constants().taxable()) ? true : false);
		boolean isActive = (Boolean) statusCheck.getValue();
		data.setActive(isActive);

		// Setting Tax Rates

		// List<ClientTaxRates> records = gridView.getRecords();
		// Set<ClientTaxRates> allTaxRates = new
		// HashSet<ClientTaxRates>(records);

		// taxCode.setTaxRates(allTaxRates);
	}
}
