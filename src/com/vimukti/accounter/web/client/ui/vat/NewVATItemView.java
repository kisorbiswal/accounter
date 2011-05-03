package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.combo.VatReturnBoxCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Raj Vimal
 * 
 */

public class NewVATItemView extends BaseView<ClientTAXItem> {

	private TextItem vatItemNameText;
	private TextAreaItem descriptionText;
	private AmountField vatRateText;
	private PercentageField vatRateTextPerT;
	private TAXAgencyCombo vatAgencyCombo;
	private CheckboxItem statusCheck;
	private VatReturnBoxCombo vatReturnBoxCombo;
	private ClientTAXItem takenVATItem;
	protected ClientVATReturnBox selectedBox;
	protected ClientTAXAgency selectedVATAgency;
	private CheckboxItem isPercentatateAmtCheck;

	private ArrayList<DynamicForm> listforms;

	public NewVATItemView() {
		super();
		this.validationCount = 7;
	}

	private void createControls() {
		Label infoLabel = null;
		Label infolabel1 = null;
		final int accounttype = FinanceApplication.getCompany()
				.getAccountingType();
		if (accounttype == 1) {
			infoLabel = new Label(FinanceApplication.getVATMessages().VATItem());
			infoLabel.setStyleName(FinanceApplication.getCustomersMessages()
					.lableTitle());
			// infoLabel.setHeight("35px");
		}

		else {
			infolabel1 = new Label(FinanceApplication.getCompanyMessages()
					.taxItem());

			infolabel1.setStyleName(FinanceApplication.getCustomersMessages()
					.lableTitle());
			// infolabel1.setHeight("50px");
		}

		listforms = new ArrayList<DynamicForm>();

		vatItemNameText = new TextItem(FinanceApplication.getVATMessages()
				.VATItemName());
		vatItemNameText.setHelpInformation(true);
		vatItemNameText.setWidth(80);
		vatItemNameText.setRequired(true);

		descriptionText = new TextAreaItem(FinanceApplication.getVATMessages()
				.description());
		descriptionText.setHelpInformation(true);
		descriptionText.setWidth(80);

		vatRateText = new AmountField(FinanceApplication.getVATMessages()
				.VATAmount());
		vatRateText.setHelpInformation(true);
		vatRateText.setWidth(80);
		vatRateText.setRequired(true);

		vatRateTextPerT = new PercentageField(FinanceApplication
				.getVATMessages().VATRateInPerc());
		vatRateTextPerT.setHelpInformation(true);
		vatRateTextPerT.setWidth(80);
		vatRateTextPerT.setRequired(true);

		vatAgencyCombo = new TAXAgencyCombo(FinanceApplication.getVATMessages()
				.VATAgency());
		vatAgencyCombo.setHelpInformation(true);
		vatAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						selectedVATAgency = (ClientTAXAgency) selectItem;
						loadVATReturnBoxes(selectedVATAgency);
					}

				});
		vatAgencyCombo.setWidth(80);
		vatAgencyCombo.setRequired(true);

		// Label label = new Label(
		// "Assign This item to box of VAT agency's VAT Return");

		vatReturnBoxCombo = new VatReturnBoxCombo(FinanceApplication
				.getVATMessages().VATReturnBox());
		vatReturnBoxCombo.setHelpInformation(true);
		vatReturnBoxCombo.setRequired(true);
		vatReturnBoxCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVATReturnBox>() {

					@Override
					public void selectedComboBoxItem(
							ClientVATReturnBox selectItem) {
						selectedBox = (ClientVATReturnBox) selectItem;
					}

				});
		vatReturnBoxCombo.setWidth(80);

		statusCheck = new CheckboxItem(FinanceApplication.getVATMessages()
				.itemIsActive());
		statusCheck.setValue(true);

		final DynamicForm form1 = UIUtils.form(FinanceApplication
				.getVATMessages().type());
		form1.setWidth("80%");
		form1.setIsGroup(true);

		if (accounttype == 0) {

			vatItemNameText.setTitle(FinanceApplication.getCompanyMessages()
					.taxItemName());
			vatRateText.setTitle(FinanceApplication.getCompanyMessages()
					.taxAmount());

			vatRateTextPerT.setTitle(FinanceApplication.getCompanyMessages()
					.taxRateP());
			vatAgencyCombo.setTitle(FinanceApplication.getCompanyMessages()
					.taxAgency());

		}

		isPercentatateAmtCheck = new CheckboxItem(FinanceApplication
				.getVATMessages().isConsiderAsPercentange());
		isPercentatateAmtCheck.setValue(true);

		isPercentatateAmtCheck
				.addChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						form1.clear();
						if (event.getValue()) {
							vatRateTextPerT.setPercentage(0.0);
							if (accounttype == 0)
								form1.setFields(vatItemNameText,
										descriptionText,
										isPercentatateAmtCheck,
										vatRateTextPerT, vatAgencyCombo,
										statusCheck);
							else
								form1.setFields(vatItemNameText,
										descriptionText,
										isPercentatateAmtCheck,
										vatRateTextPerT, vatAgencyCombo,
										vatReturnBoxCombo, statusCheck);

						} else {
							vatRateText.setAmount(0.0);
							if (accounttype == 0)
								form1.setFields(vatItemNameText,
										descriptionText,
										isPercentatateAmtCheck, vatRateText,
										vatAgencyCombo, statusCheck);
							else
								form1.setFields(vatItemNameText,
										descriptionText,
										isPercentatateAmtCheck, vatRateText,
										vatAgencyCombo, vatReturnBoxCombo,
										statusCheck);
						}
					}
				});

		form1.getCellFormatter().setWidth(0, 0, "250px");
		form1.getCellFormatter().addStyleName(1, 0, "memoFormAlign");

		if (takenVATItem != null) {
			vatItemNameText
					.setValue(takenVATItem.getName() != null ? takenVATItem
							.getName() : "");
			descriptionText
					.setValue(takenVATItem.getDescription() != null ? takenVATItem
							.getDescription()
							: "");
			isPercentatateAmtCheck.setValue(takenVATItem.isPercentage());

			if (takenVATItem.getTaxAgency() != null) {
				selectedVATAgency = FinanceApplication.getCompany()
						.getTaxAgency(takenVATItem.getTaxAgency());
				vatAgencyCombo.setComboItem(selectedVATAgency);
				loadVATReturnBoxes(selectedVATAgency);
			}

			if (takenVATItem.getVatReturnBox() != null) {
				selectedBox = FinanceApplication.getCompany().getVatReturnBox(
						takenVATItem.getVatReturnBox());
				vatReturnBoxCombo.setComboItem(selectedBox);
			}
			statusCheck.setValue(takenVATItem.isActive());

			if (takenVATItem.isPercentage()) {
				vatRateTextPerT.setPercentage(takenVATItem.getTaxRate());
				if (accounttype == 0)
					form1.setFields(vatItemNameText, descriptionText,
							isPercentatateAmtCheck, vatRateTextPerT,
							vatAgencyCombo, statusCheck);
				else
					form1.setFields(vatItemNameText, descriptionText,
							isPercentatateAmtCheck, vatRateTextPerT,
							vatAgencyCombo, vatReturnBoxCombo, statusCheck);
			} else {

				vatRateText.setAmount(takenVATItem.getTaxRate());
				if (accounttype == 0)
					form1.setFields(vatItemNameText, descriptionText,
							isPercentatateAmtCheck, vatRateText,
							vatAgencyCombo, statusCheck);
				else
					form1.setFields(vatItemNameText, descriptionText,
							isPercentatateAmtCheck, vatRateText,
							vatAgencyCombo, vatReturnBoxCombo, statusCheck);
			}

		} else {
			if (accounttype == 0)
				form1.setFields(vatItemNameText, descriptionText,
						isPercentatateAmtCheck, vatRateTextPerT,
						vatAgencyCombo, statusCheck);
			else
				form1.setFields(vatItemNameText, descriptionText,
						isPercentatateAmtCheck, vatRateTextPerT,
						vatAgencyCombo, vatReturnBoxCombo, statusCheck);
		}

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSpacing(25);
		mainPanel.setWidth("100%");
		if (accounttype == 1)
			mainPanel.add(infoLabel);
		else
			mainPanel.add(infolabel1);
		mainPanel.add(form1);

		if (UIUtils.isMSIEBrowser()) {
			form1.getCellFormatter().setWidth(0, 1, "270px");
			form1.setWidth("50%");
		}

		canvas.add(mainPanel);

		/* Adding dynamic forms in list */
		listforms.add(form1);

	}

	protected void loadVATReturnBoxes(ClientTAXAgency vatAgency) {
		List<ClientVATReturnBox> vatBoxes = FinanceApplication.getCompany()
				.getVatReturnBoxes();
		List<ClientVATReturnBox> vatBoxes2 = new ArrayList<ClientVATReturnBox>();
		for (ClientVATReturnBox vatBox : vatBoxes) {
			if (vatAgency.getVATReturn() == vatBox.getVatReturnType())
				vatBoxes2.add(vatBox);
		}
		vatReturnBoxCombo.initCombo(vatBoxes2);
	}

	@Override
	public void init() {
		super.init();
		takenVATItem = (ClientTAXItem) this.data;
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public boolean validate() throws Exception {
		switch (this.validationCount) {

		case 7:
			List<DynamicForm> forms = this.getForms();
			boolean validate = true;
			for (DynamicForm form : forms) {
				if (form != null) {
					if (!form.validate(false)) {
						validate = false;
						// throw new InvalidEntryException(
						// AccounterErrorType.REQUIRED_FIELDS);
					}

				}
			}
			return validate;
		case 6:
			String name = vatItemNameText.getValue().toString() != null ? vatItemNameText
					.getValue().toString()
					: "";
			if (((takenVATItem == null && Utility.isObjectExist(
					FinanceApplication.getCompany().getTaxItems(), name)) ? false
					: true)
					|| (takenVATItem != null ? (takenVATItem.getName()
							.equalsIgnoreCase(name) ? true : (Utility
							.isObjectExist(FinanceApplication.getCompany()
									.getTaxItems(), name) ? false : true))
							: true)) {
				return true;
			} else
				throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);
		case 5:
			// if ((Boolean) isPercentatateAmtCheck.getValue() == false) {
			// if (!vatRateText.validate()) {
			// throw new InvalidEntryException(
			// AccounterErrorType.REQUIRED_FIELDS);
			// }
			// }
			return true;
		case 4:
			// if ((Boolean) isPercentatateAmtCheck.getValue() == true) {
			// if (!vatRateTextPerT.validate()) {
			// throw new InvalidEntryException(
			// AccounterErrorType.REQUIRED_FIELDS);
			// }
			// }
			return true;

		case 3:
			// if (!vatAgencyCombo.validate()) {
			// throw new InvalidEntryException(
			// AccounterErrorType.REQUIRED_FIELDS);
			// }
			return true;

		case 2:
			if (accountType == 0)
				return true;
			else {
				// if (!vatReturnBoxCombo.validate()) {
				// throw new InvalidEntryException(
				// AccounterErrorType.REQUIRED_FIELDS);
				// }
				return true;
			}
		case 1:
			if (takenVATItem == null) {
				if (Utility.isObjectExist(FinanceApplication.getCompany()
						.getTaxItems(), this.vatItemNameText.getValue()
						.toString())) {
					throw new InvalidEntryException(
							AccounterErrorType.ALREADYEXIST);
				}
			}
		default:
			return true;

		}
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		ClientTAXItem vatItem = getObject();
		if (takenVATItem == null)
			createObject(vatItem);
		else
			alterObject(vatItem);
	}

	@Override
	public void setData(ClientTAXItem data) {
		super.setData(data);
		if (data != null)
			takenVATItem = (ClientTAXItem) data;
		else
			takenVATItem = null;
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {

			// if (takenVATItem == null) {
			// Accounter.showInformation(FinanceApplication.getVATMessages()
			// .newVATItemCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication.getVATMessages()
			// .VATItemUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new Exception());
		}
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(exception.getMessage());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		MainFinanceWindow.getViewManager().showError(exception.getMessage());

	}

	private ClientTAXItem getObject() {
		ClientTAXItem vatItem;
		if (takenVATItem != null) {
			vatItem = takenVATItem;
		} else {
			vatItem = new ClientTAXItem();
		}

		vatItem
				.setName(vatItemNameText.getValue().toString() != null ? vatItemNameText
						.getValue().toString()
						: "");
		vatItem
				.setDescription(descriptionText.getValue().toString() != null ? descriptionText
						.getValue().toString()
						: "");
		vatItem.setVatReturnBox(selectedBox != null ? selectedBox.getStringID()
				: takenVATItem != null ? takenVATItem.getVatReturnBox() : null);
		vatItem.setTaxAgency(selectedVATAgency != null ? selectedVATAgency
				.getStringID() : takenVATItem != null ? takenVATItem
				.getTaxAgency() : null);
		vatItem.setActive((Boolean) statusCheck.getValue());

		vatItem
				.setTaxRate((Boolean) this.isPercentatateAmtCheck.getValue() ? vatRateTextPerT
						.getPercentage()
						: vatRateText.getAmount());
		vatItem.setPercentage((Boolean) this.isPercentatateAmtCheck.getValue());
		return vatItem;
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vatItemNameText.setFocus();
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
	public void setPrevoiusOutput(Object preObject) {
		super.setPrevoiusOutput(preObject);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.vatAgencyCombo.addComboItem((ClientTAXAgency) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.vatAgencyCombo.removeComboItem((ClientTAXAgency) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.vatAgencyCombo.updateComboItem((ClientTAXAgency) core);
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
