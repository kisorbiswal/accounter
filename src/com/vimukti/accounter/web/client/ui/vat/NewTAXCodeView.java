package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
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
	public long selectedVATPurchaseAcc;
	public long selectedVATSAlesAcc;
	protected boolean isComboDisabled = false;
	private String vatCode;

	private ArrayList<DynamicForm> listforms;

	public NewTAXCodeView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "");

	}

	@Override
	public void initData() {
		ClientTAXCode vat = (ClientTAXCode) getData();
		if (isInViewMode()) {
			vatCodeTxt.setValue(vat.getName() != null ? vat.getName() : "");
			vatCode = vat.getName() != null ? vat.getName() : "";
			description.setValue(vat.getDescription() != null ? vat
					.getDescription() : "");
			isActive.setValue(vat.isActive());
			taxableGroupRadio.setValue(vat.isTaxable() ? Accounter.constants()
					.taxable() : Accounter.constants().taxExempt());

			if (getCompany().getTaxItem(vat.getTAXItemGrpForPurchases()) != null) {
				selectedVATPurchaseAcc = vat.getTAXItemGrpForPurchases();
				vatItemComboForPurchases.setComboItem(Accounter.getCompany()
						.getTaxItem(vat.getTAXItemGrpForPurchases()));
			} else
				vatItemComboForPurchases.setSelected("");

			if (getCompany().getTaxItem(vat.getTAXItemGrpForSales()) != null) {
				selectedVATSAlesAcc = vat.getTAXItemGrpForSales();
				vatItemComboForSales.setComboItem(Accounter.getCompany()
						.getTaxItem(vat.getTAXItemGrpForSales()));
			} else
				vatItemComboForSales.setSelected("");

			if (!vat.isTaxable()) {
				// vatItemComboForPurchases.setValue("");
				// vatItemComboForSales.setValue("");
				vatItemComboForPurchases.setDisabled(true);
				vatItemComboForSales.setDisabled(true);
				vatItemComboForSales.setRequired(false);
				vatItemComboForPurchases.setRequired(false);
			}
		}
	}

	private void createControls() {
		Label infoLabel = new Label(Accounter.constants().newVATCode());
		infoLabel.setStyleName(Accounter.constants().labelTitle());
		// infoLabel.setHeight("35px");
		listforms = new ArrayList<DynamicForm>();

		AccounterConstants vatMessages = Accounter.constants();
		vatCodeTxt = new TextItem(vatMessages.vatCode());
		vatCodeTxt.setHelpInformation(true);
		vatCodeTxt.setRequired(true);
		vatCodeTxt.setWidth(100);
		vatCodeTxt.setDisabled(isInViewMode());
		description = new TextAreaItem();
		description.setToolTip(Accounter
				.messages()
				.writeCommentsForThis(this.getAction().getViewName())
				.replace(Accounter.constants().comments(),
						Accounter.constants().description()));
		description.setHelpInformation(true);
		description.setWidth(100);
		description.setTitle(Accounter.constants().description());
		description.setDisabled(isInViewMode());

		isActive = new CheckboxItem(Accounter.constants().isActive());
		isActive.setValue((Boolean) true);
		isActive.setDisabled(isInViewMode());

		taxableGroupRadio = new RadioGroupItem(Accounter.constants().tax());
		taxableGroupRadio.setWidth(100);
		taxableGroupRadio.setDisabled(!isInViewMode());
		taxableGroupRadio.setValues(getClickHandler(), Accounter.constants()
				.taxable(), Accounter.constants().taxExempt());

		taxableGroupRadio.setDefaultValue(Accounter.constants().taxable());

		vatItemComboForPurchases = new VATItemCombo(Accounter.constants()
				.vatItemForPurchases());
		vatItemComboForPurchases.setHelpInformation(true);
		vatItemComboForPurchases.initCombo(vatItemComboForPurchases
				.getPurchaseWithPrcntVATItems());
		vatItemComboForPurchases.setRequired(true);
		vatItemComboForPurchases.setDisabled(isInViewMode());
		// vatItemComboForPurchases.setWidth(100);
		vatItemComboForPurchases
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {
					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null)
							selectedVATPurchaseAcc = selectItem.getID();
					}
				});

		vatItemComboForSales = new VATItemCombo(Accounter.constants()
				.vatItemForSales());
		vatItemComboForSales.setHelpInformation(true);
		vatItemComboForSales.initCombo(vatItemComboForSales
				.getSalesWithPrcntVATItems());
		vatItemComboForSales.setRequired(true);
		vatItemComboForSales.setDisabled(isInViewMode());
		vatItemComboForSales
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null)
							selectedVATSAlesAcc = selectItem.getID();
					}
				});
		vatNameForm = new DynamicForm();
		vatNameForm.setWidth("80%");
		vatNameForm.getCellFormatter().setWidth(0, 0, "225px");
		vatNameForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		vatNameForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		vatNameForm.setFields(vatCodeTxt, description, taxableGroupRadio,
				isActive, vatItemComboForSales, vatItemComboForPurchases);

		if (getData() != null) {
			vatCodeTxt.setValue(data.getName() != null ? data.getName() : "");
			description.setValue(data.getDescription() != null ? data
					.getDescription() : "");
			isActive.setValue(data.isActive());
			taxableGroupRadio.setValue(data.isTaxable() ? Accounter.constants()
					.taxable() : Accounter.constants().taxExempt());
			vatItemComboForPurchases
					.setValue(data.getTAXItemGrpForPurchases() != 0 ? Accounter
							.getCompany()
							.getTAXItemGroup(data.getTAXItemGrpForPurchases())
							.getName() : "");
			vatItemComboForSales
					.setValue(data.getTAXItemGrpForSales() != 0 ? Accounter
							.getCompany()
							.getTAXItemGroup(data.getTAXItemGrpForSales())
							.getName() : "");
		} else {
			setData(new ClientTAXCode());
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
		this.add(mainVPanel);

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
				taxableGroupRadio.setValue(taxbl);
				if (taxbl.equalsIgnoreCase(Accounter.constants().taxable())) {
					isComboDisabled = false;
					vatItemComboForPurchases.setDisabled(false);
					vatItemComboForSales.setDisabled(false);
					vatItemComboForPurchases.setRequired(true);
					vatItemComboForSales.setRequired(true);
				} else {
					isComboDisabled = true;
					// vatItemComboForSales.setValue("");
					// vatItemComboForPurchases.setValue("");
					vatItemComboForSales.setDisabled(true);
					vatItemComboForPurchases.setDisabled(true);
					vatItemComboForPurchases.setRequired(false);
					vatItemComboForSales.setRequired(false);
				}
			}
		};
		return hanler;
	}

	@Override
	public void saveAndUpdateView() {

		updateVATCode();

		saveOrUpdate(getData());

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String exceptionMessage = exception.getMessage();
		// addError(this, exception.getMessage());
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
		updateVATCode();
		if (exceptionMessage.contains("name")) {
			data.setName(vatCode);

		}
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (editableVATCode == null) {
			// Accounter.showInformation(FinanceApplication.constants()
			// .newVATCodeCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication.constants()
			// .VATCodeUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	protected void updateVATCode() {

		data.setName(vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
				.toString() : "");
		data.setDescription(description.getValue() != null ? description
				.getValue().toString() : "");
		data.setActive((Boolean) isActive.getValue());
		if (taxableGroupRadio.getValue() != null) {
			if (taxableGroupRadio.getValue().toString()
					.equalsIgnoreCase("Taxable"))
				data.setTaxable(true);
			else
				data.setTaxable(false);
		} else
			data.setTaxable(false);

		data.setTAXItemGrpForPurchases(selectedVATPurchaseAcc);
		data.setTAXItemGrpForSales(selectedVATSAlesAcc);

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		// already exists?
		// validate form

		String name = vatCodeTxt.getValue() != null ? vatCodeTxt.getValue()
				.toString() : "";

		ClientTAXCode taxCodeByName = getCompany().getTAXCodeByName(name);

		if ((!isInViewMode() && taxCodeByName != null)) {
			result.addError(vatCodeTxt, Accounter.constants().alreadyExist());
			return result;
		}
		result.add(DynamicForm.validate(this.getForms().toArray(
				new DynamicForm[getForms().size()])));

		// if (!((!isEdit && taxCodeByName != null))
		// || (isEdit ? (data.getName().equalsIgnoreCase(name) ? true
		// : (taxCodeByName != null
		// || taxCodeByName.getID() == this.getData()
		// .getID() ? false : true)) : true)) {
		// result.addError(vatCodeTxt, Accounter.constants().alreadyExist());
		// }
		return result;
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
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}


	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.TAX_CODE, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vatCodeTxt.setDisabled(isInViewMode());
		description.setDisabled(isInViewMode());
		isActive.setDisabled(isInViewMode());
		taxableGroupRadio.setDisabled(isInViewMode());
		vatItemComboForPurchases.setDisabled(isInViewMode());
		vatItemComboForSales.setDisabled(isInViewMode());
		super.onEdit();

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			return Accounter.constants().vatCode();
		} else {
			return Accounter.constants().taxCode();
		}
	}
}
