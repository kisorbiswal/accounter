package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * @author Murali.A
 * 
 */
public class NewTAXCodeView extends BaseView<ClientTAXCode> {

	private TextItem vatCodeTxt;
	private TextAreaItem description;
	private CheckboxItem isActive;
	private RadioGroupItem taxableGroupRadio;
	private VATItemCombo vatItemComboForPurchases;
	private VATItemCombo vatItemComboForSales;
	private DynamicForm vatNameForm;
	public String selectedVATPurchaseAcc = "";
	public String selectedVATSAlesAcc = "";
	private ClientTAXCode editableTAXCode;
	protected boolean isComboDisabled = false;

	private ArrayList<DynamicForm> listforms;

	public NewTAXCodeView() {
		super();
		this.validationCount = 4;
	}

	@Override
	public void init() {
		super.init();
		editableTAXCode = (ClientTAXCode) this.data;
		createControls();
		setSize("100%", "");

	}

	@Override
	public void initData() {
		ClientTAXCode vat = (ClientTAXCode) getData();
		if (vat != null) {
			vatCodeTxt.setValue(vat.getName() != null ? vat.getName() : "");
			description.setValue(vat.getDescription() != null ? vat
					.getDescription() : "");
			isActive.setValue(vat.isActive());
			taxableGroupRadio.setValue(vat.isTaxable() ? FinanceApplication
					.getVATMessages().taxable() : FinanceApplication
					.getVATMessages().taxExempt());

			if (FinanceApplication.getCompany().getTaxItem(
					vat.getTAXItemGrpForPurchases()) != null) {
				selectedVATPurchaseAcc = vat.getTAXItemGrpForPurchases();
				vatItemComboForPurchases.setComboItem(FinanceApplication
						.getCompany().getTaxItem(
								vat.getTAXItemGrpForPurchases()));
			} else
				vatItemComboForPurchases.setSelected("");

			if (FinanceApplication.getCompany().getTaxItem(
					vat.getTAXItemGrpForSales()) != null) {
				selectedVATSAlesAcc = vat.getTAXItemGrpForSales();
				vatItemComboForSales.setComboItem(FinanceApplication
						.getCompany().getTaxItem(vat.getTAXItemGrpForSales()));
			} else
				vatItemComboForSales.setSelected("");

			if (!vat.isTaxable()) {
				vatItemComboForPurchases.setValue("");
				vatItemComboForSales.setValue("");
				vatItemComboForPurchases.setDisabled(true);
				vatItemComboForSales.setDisabled(true);
			}
		}
	}

	private void createControls() {
		Label infoLabel = new Label(FinanceApplication.getVATMessages()
				.newVATCode());
		infoLabel.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());
//		infoLabel.setHeight("35px");
		listforms = new ArrayList<DynamicForm>();

		vatCodeTxt = new TextItem(vatMessages.vatCode());
		vatCodeTxt.setHelpInformation(true);
		vatCodeTxt.setRequired(true);
		vatCodeTxt.setWidth(100);
		description = new TextAreaItem();
		description.setHelpInformation(true);
		description.setWidth(100);
		description.setTitle(FinanceApplication.getVATMessages().description());

		isActive = new CheckboxItem(FinanceApplication.getVATMessages()
				.isActive());
		isActive.setValue((Boolean) true);

		taxableGroupRadio = new RadioGroupItem(FinanceApplication
				.getVATMessages().tax());
		taxableGroupRadio.setWidth(100);
		taxableGroupRadio.setValues(getClickHandler(), FinanceApplication
				.getVATMessages().taxable(), FinanceApplication
				.getVATMessages().taxExempt());
		taxableGroupRadio.setDefaultValue(FinanceApplication.getVATMessages()
				.taxable());

		vatItemComboForPurchases = new VATItemCombo(FinanceApplication
				.getVATMessages().VATItemForPurchases());
		vatItemComboForPurchases.setHelpInformation(true);
		vatItemComboForPurchases.initCombo(vatItemComboForPurchases
				.getPurchaseWithPrcntVATItems());
		vatItemComboForPurchases.setRequired(true);
		vatItemComboForPurchases.setWidth(100);
		vatItemComboForPurchases
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {
					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null)
							selectedVATPurchaseAcc = selectItem.getStringID();
					}
				});

		vatItemComboForSales = new VATItemCombo(FinanceApplication
				.getVATMessages().VATItemForSales());
		vatItemComboForSales.setHelpInformation(true);
		vatItemComboForSales.initCombo(vatItemComboForSales
				.getSalesWithPrcntVATItems());
		vatItemComboForSales.setRequired(true);
		vatItemComboForSales
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null)
							selectedVATSAlesAcc = selectItem.getStringID();
					}
				});
		vatNameForm = new DynamicForm();
		vatNameForm.setWidth("80%");
		vatNameForm.getCellFormatter().setWidth(0, 0, "225px");
		vatNameForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		vatNameForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		vatNameForm.setFields(vatCodeTxt, description, taxableGroupRadio,
				isActive, vatItemComboForSales, vatItemComboForPurchases);

		if (editableTAXCode != null) {
			vatCodeTxt
					.setValue(editableTAXCode.getName() != null ? editableTAXCode
							.getName()
							: "");
			description
					.setValue(editableTAXCode.getDescription() != null ? editableTAXCode
							.getDescription()
							: "");
			isActive.setValue(editableTAXCode.isActive());
			taxableGroupRadio
					.setValue(editableTAXCode.isTaxable() ? FinanceApplication
							.getVATMessages().taxable() : FinanceApplication
							.getVATMessages().taxExempt());
			vatItemComboForPurchases.setValue(editableTAXCode
					.getTAXItemGrpForPurchases() != null ? FinanceApplication
					.getCompany().getTAXItemGroup(
							editableTAXCode.getTAXItemGrpForPurchases())
					.getName() : "");
			vatItemComboForSales.setValue(editableTAXCode
					.getTAXItemGrpForSales() != null ? FinanceApplication
					.getCompany().getTAXItemGroup(
							editableTAXCode.getTAXItemGrpForSales()).getName()
					: "");
		}

		VerticalPanel mainVPanel = new VerticalPanel();
		mainVPanel.setSpacing(25);
		mainVPanel.setWidth("100%");
		mainVPanel.add(infoLabel);
		mainVPanel.add(vatNameForm);

		if (UIUtils.isMSIEBrowser()) {
			vatNameForm.getCellFormatter().setWidth(0, 1, "270px");
			vatNameForm.setWidth("50%");
		}
		canvas.add(mainVPanel);

		/* Adding dynamic forms in list */
		listforms.add(vatNameForm);

	}

	private ClickHandler getClickHandler() {
		ClickHandler hanler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				vatCodeTxt.getMainWidget().removeStyleName(
						"highlightedFormItem");
				vatItemComboForPurchases.getMainWidget().removeStyleName(
						"highlightedFormItem");
				vatItemComboForSales.getMainWidget().removeStyleName(
						"highlightedFormItem");
				String taxbl = taxableGroupRadio.getValue().toString();
				if (taxbl.equalsIgnoreCase(FinanceApplication.getVATMessages()
						.taxable())) {
					isComboDisabled = false;
					vatItemComboForPurchases.setDisabled(false);
					vatItemComboForSales.setDisabled(false);
					vatItemComboForPurchases.setRequired(true);
					vatItemComboForSales.setRequired(true);
				} else {
					isComboDisabled = true;
					vatItemComboForSales.setValue("");
					vatItemComboForPurchases.setValue("");
					vatItemComboForPurchases.setRequired(false);
					vatItemComboForSales.setRequired(false);
					vatItemComboForSales.setDisabled(true);
					vatItemComboForPurchases.setDisabled(true);
				}
			}
		};
		return hanler;
	}

	@Override
	public void saveAndUpdateView() throws Exception {

		ClientTAXCode vatCode = getVATCode();

		if (editableTAXCode == null)
			createObject(vatCode);
		else
			alterObject(vatCode);

	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		Accounter.showError(exception.getMessage());
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (editableVATCode == null) {
			// Accounter.showInformation(FinanceApplication.getVATMessages()
			// .newVATCodeCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication.getVATMessages()
			// .VATCodeUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new Exception());
		}
	}

	protected ClientTAXCode getVATCode() {
		ClientTAXCode vatCode;
		if (editableTAXCode != null) {
			vatCode = editableTAXCode;
		} else
			vatCode = new ClientTAXCode();

		vatCode.setName(vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
				.toString() : "");
		vatCode.setDescription(description.getValue() != null ? description
				.getValue().toString() : "");
		vatCode.setActive((Boolean) isActive.getValue());
		if (taxableGroupRadio.getValue() != null) {
			if (taxableGroupRadio.getValue().toString().equalsIgnoreCase(
					"Taxable"))
				vatCode.setTaxable(true);
			else
				vatCode.setTaxable(false);
		} else
			vatCode.setTaxable(false);

		vatCode.setTAXItemGrpForPurchases(selectedVATPurchaseAcc);
		vatCode.setTAXItemGrpForSales(selectedVATSAlesAcc);

		return vatCode;
	}

	@Override
	public boolean validate() throws Exception {

		switch (this.validationCount) {
		case 4:
			List<DynamicForm> forms = this.getForms();
			for (DynamicForm form : forms) {
				if (form != null) {
					form.validate();
				}
			}
			return true;
		case 3:
			String name = vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
					.toString() : "";
			// if (name == null || name.equals(""))
			// throw new InvalidEntryException(
			// AccounterErrorType.REQUIRED_FIELDS);
			if (((editableTAXCode == null && Utility.isObjectExist(
					FinanceApplication.getCompany().getTaxCodes(), name)) ? false
					: true)
					|| (editableTAXCode != null ? (editableTAXCode.getName()
							.equalsIgnoreCase(name) ? true : (Utility
							.isObjectExist(FinanceApplication.getCompany()
									.getTaxCodes(), name) ? false : true))
							: true)) {
				return true;
			} else
				throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);
		case 2:
			// if (!isComboDisabled && !vatItemComboForPurchases.validate()) {
			// if (!vatItemComboForSales.validate()) {
			// throw new InvalidEntryException(
			// AccounterErrorType.REQUIRED_FIELDS
			// // + " AnyOne Of This Sales Or Purchase"
			// );
			// }
			// }
			return true;

		case 1:
			// if (!isComboDisabled && !vatItemComboForSales.validate()) {
			// if (!vatItemComboForPurchases.validate()) {
			// throw new InvalidEntryException(
			// AccounterErrorType.REQUIRED_FIELDS
			// // + " AnyOne Of This Sales Or Purchase"
			// );
			// }
			// }
			return true;

		default:
			return false;

		}
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vatCodeTxt.setFocus();
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

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForPurchases
						.addComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForSales.addComboItem((ClientTAXItem) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForPurchases
						.removeComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForSales.removeComboItem((ClientTAXItem) core);
			break;

		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForPurchases
						.updateComboItem((ClientTAXItem) core);

			if (core.getObjectType() == AccounterCoreType.TAXITEM)
				this.vatItemComboForSales.updateComboItem((ClientTAXItem) core);
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
