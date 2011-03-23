package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Mandeep Singh
 * @modified By kumar kasimala
 * @modified By Ravi Kiran.G
 */
@SuppressWarnings("unchecked")
public class CompanyInfoDialog extends BaseDialog {

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);

	TextItem websiteText, taxIDText, companyNameText, trandigNameText,
			registrationNumberText, bankAccountText, sortCodeText;
	EmailField emailText;
	IntegerField phoneText, faxText;

	DynamicForm companyDetailsForm, phoneAndFaxForm, taxIDForm,
			RegistrationNumberForm;

	@SuppressWarnings("unused")
	private TextAreaItem address;
	@SuppressWarnings("unused")
	private LinkedHashMap<String, ClientAddress> addresses = new LinkedHashMap<String, ClientAddress>();

	protected String str;

	@SuppressWarnings("unused")
	private String string;

	private TextAreaItem textareaItem, textareaItem2;

	private LinkedHashMap<Integer, ClientAddress> allAddresses;

	private CheckboxItem doupaySalesChecBox;

	private TextItem vatRegNumber;

	private Button taxgroupBtn;

	private RadioGroupItem paysalesTaxgroupItem;

	CompanyPreferencesView preferencesView = new CompanyPreferencesView();

	public CompanyInfoDialog(String title, String description) {
		super(title, description);
		createControls();
		center();
		// setHeight("250");
		initData();
	}

	protected void initData() {
		this.company = FinanceApplication.getCompany();
		if (this.company != null) {
			companyNameText.setValue(company.getName());
			trandigNameText.setValue(company.getTradingName());
			this.taxIDText.setValue(company.getTaxId());
			this.faxText.setValue(company.getFax());
			this.phoneText.setValue(company.getPhone());
			this.websiteText.setValue(company.getWebSite());
			this.emailText.setValue(company.getCompanyEmail());
			this.bankAccountText.setValue(company.getBankAccountNo());
			this.sortCodeText.setValue(company.getSortCode());
			List<ClientAddress> companyAddress = company.getAddresses();
			for (ClientAddress address : companyAddress) {
				if (address.getType() == ClientAddress.TYPE_COMPANY) {
					setAddressToTextItem(textareaItem, address);
					allAddresses
							.put(UIUtils.getAddressType("company"), address);
				}
				if (address.getType() == ClientAddress.TYPE_COMPANY_REGISTRATION) {
					setAddressToTextItem(textareaItem2, address);
					allAddresses.put(UIUtils
							.getAddressType("companyregistration"), address);
				}
			}
			registrationNumberText.setValue(company.getRegistrationNumber());

			doupaySalesChecBox.setValue(FinanceApplication.getCompany()
					.getPreferences().getDoYouPaySalesTax());

			if (doupaySalesChecBox.getValue() == Boolean.FALSE) {
				vatRegNumber.setDisabled(true);
			}
		}
	}

	public void setAddressToTextItem(TextAreaItem textItem,
			ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		textItem.setValue(toToSet);

	}

	private void createControls() {
		VerticalPanel mainVLay = new VerticalPanel();
		// setTitle(companyConstants.companyInformation());
		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		companyNameText = new TextItem(companyConstants.registeredName());
		companyNameText.setHelpInformation(true);
		companyNameText.setRequired(true);
		companyNameText.setWidth(100);
		string = "[a-zA-Z0-9 /s]";

		textareaItem = new TextAreaItem(companyConstants.registeredAddress());
		textareaItem.setHelpInformation(true);
		textareaItem.setRequired(false);
		textareaItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("title", "description", textareaItem,
						"company", allAddresses);
			}
		});

		trandigNameText = new TextItem(companyConstants.tradingName());
		trandigNameText.setHelpInformation(true);
		trandigNameText.setRequired(true);
		trandigNameText.setWidth(100);

		textareaItem2 = new TextAreaItem(companyConstants.tradingAddress());
		textareaItem2.setHelpInformation(true);

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		textareaItem2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("title", "description", textareaItem2,
						"companyregistration", allAddresses);
			}
		});
		textareaItem2.setRequired(false);

		TextItem emptyText = new TextItem();
		emptyText.setVisible(false);

		companyDetailsForm = UIUtils.form(companyConstants.companyDetails());

		VerticalPanel mainVLay2 = new VerticalPanel();

		phoneText = new IntegerField(companyConstants.businessPhone());
		phoneText.setHelpInformation(true);

		faxText = new IntegerField(companyConstants.businessFax());
		faxText.setHelpInformation(true);

		emailText = new EmailField(FinanceApplication.getCompanyMessages()
				.email());
		emailText.setHelpInformation(true);

		websiteText = new TextItem(companyConstants.webPageAddress());
		websiteText.setHelpInformation(true);

		registrationNumberText = new TextItem(companyConstants
				.companyRegistrationNumber());
		registrationNumberText.setHelpInformation(true);

		taxIDText = new TextItem(companyConstants.federalTaxId());
		taxIDText.setHelpInformation(true);

		bankAccountText = new TextItem();
		bankAccountText.setTitle(FinanceApplication.getCompanyMessages()
				.bankAccountNo());
		bankAccountText.setHelpInformation(true);

		sortCodeText = new TextItem();
		sortCodeText.setTitle(FinanceApplication.getCompanyMessages()
				.sortCode());
		sortCodeText.setHelpInformation(true);

		phoneAndFaxForm = UIUtils.form(companyConstants.phoneAndFaxNumbers());

		phoneAndFaxForm.setFields(phoneText, faxText, websiteText, emailText,
				registrationNumberText, taxIDText, bankAccountText,
				sortCodeText);
		phoneAndFaxForm.setNumCols(8);

		DynamicForm taxesForm = new DynamicForm();
		// taxesForm.setWidth100();
		// taxesForm.setHeight("*");
		taxesForm.setIsGroup(true);
		taxesForm.setGroupTitle(companyMessges.taxes());
		// taxesForm.setTitleOrientation(TitleOrientation.TOP);
		// taxesForm.setPadding(10);

		doupaySalesChecBox = new CheckboxItem();
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			doupaySalesChecBox.setTitle(companyMessges.doYoupaySalesTaxes());

		} else
			doupaySalesChecBox
					.setTitle(companyMessges.areYouRegisteredForVAT());
		vatRegNumber = new TextItem(UIUtils.getVendorString(companyMessges
				.vatRegNo(), companyMessges.taxRegNo()));
		vatRegNumber.setHelpInformation(true);
		vatRegNumber.setWidth(100);
		vatRegNumber.setDisabled(false);

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			doupaySalesChecBox.addChangeHandler(new ChangeHandler() {

				private FocusWidget taxgroupBtn;

				public void onChange(ChangeEvent event) {
					if ((Boolean) ((CheckboxItem) event.getSource()).getValue())
						taxgroupBtn.setEnabled(false);
					else
						taxgroupBtn.setEnabled(true);
				}
			});
			doupaySalesChecBox
					.addChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							vatRegNumber.setDisabled(!event.getValue());
							vatRegNumber.setValue(FinanceApplication
									.getCompany().getpreferences()
									.getVATregistrationNumber());
						}
					});
		} else {

			doupaySalesChecBox
					.addChangeHandler(new ValueChangeHandler<Boolean>() {

						@Override
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							vatRegNumber.setDisabled(!event.getValue());
							vatRegNumber.setValue(FinanceApplication
									.getCompany().getpreferences()
									.getVATregistrationNumber());
						}
					});
		}
		vatRegNumber.setValue(FinanceApplication.getCompany().getpreferences()
				.getVATregistrationNumber());
		taxgroupBtn = new Button(companyMessges.taxgroups());
		// taxgroupBtn.setColSpan("*");
		taxgroupBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				preferencesView.taxGroupButtonClick();
			}
		});
		paysalesTaxgroupItem = new RadioGroupItem();
		paysalesTaxgroupItem.setTitle(companyMessges
				.onWhatbasisdoUpaySalesTaxes());
		// paysalesTaxgroupItem.setColSpan("*");
		// paysalesTaxgroupItem.setVertical(false);
		// paysalesTaxgroupItem
		// .setValue(company.getPreferences() != null ? company
		// .getPreferences().getIsAccuralBasis() ? "1" : "2" : "1");

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("1", companyMessges.accrualBasis());
		map.put("2", companyMessges.cashBasis());
		paysalesTaxgroupItem.setValueMap(map);

		// if(!FinanceApplication.getCompany().getpreferences().getDoYouPaySalesTax())
		// vatRegNumber.setDisabled(true);
		
		companyDetailsForm.setFields(companyNameText, textareaItem,
				trandigNameText, textareaItem2, doupaySalesChecBox,
				vatRegNumber);
		companyDetailsForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		companyDetailsForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		companyDetailsForm.setNumCols(7);
		companyDetailsForm.setCellSpacing(5);
		mainVLay.add(companyDetailsForm);

		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US)
		// mainVLay.add(taxgroupBtn);

		mainVLay2.add(phoneAndFaxForm);
		HorizontalPanel mainHLay = new HorizontalPanel();
		mainHLay.add(mainVLay);
		mainHLay.add(mainVLay2);

		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {

				try {
					if (CompanyInfoDialog.this.validate())
						updatedCompany();
					return true;
				} catch (InvalidTransactionEntryException e) {
					e.printStackTrace();
				} catch (InvalidEntryException e) {
					Accounter.showError(e.getMessage());
				}
				return false;
			}

		});

		okbtn.setText(FinanceApplication.getCompanyMessages().update());

		mainHLay.setWidth("600");
		mainHLay.setHeight("250");

		setBodyLayout(mainHLay);

	}

	public void close() {
		// FIXME

		// destroy();
	}

	protected void updatedCompany() {

		/*
		 * Here we are creating a new ClientCompany Object, to avoid sending all
		 * the lists to server. Only the necessary fields which are required to
		 * be updated are only sent to server.
		 */
		ClientCompany clientCompany = new ClientCompany();
		clientCompany.stringID = company.stringID;
		clientCompany.setName(getStringValue(companyNameText));
		clientCompany.setTradingName(getStringValue(this.trandigNameText));
		clientCompany.setPhone(getStringValue(phoneText));
		clientCompany.setCompanyEmail(getStringValue(emailText));
		clientCompany.setTaxId(getStringValue(taxIDText));
		clientCompany.setFax(getStringValue(faxText));
		clientCompany.setWebSite(getStringValue(websiteText));
		clientCompany.setBankAccountNo(getStringValue(bankAccountText));
		clientCompany.setSortCode(getStringValue(sortCodeText));

		ClientCompanyPreferences companyPreferences = company.getPreferences();
		if (companyPreferences == null) {
			companyPreferences = new ClientCompanyPreferences();
		}
		companyPreferences
				.setDoYouPaySalesTax(getBooleanValue(doupaySalesChecBox));
		companyPreferences.setVATregistrationNumber(vatRegNumber.getValue()
				.toString());

		clientCompany.setpreferences(companyPreferences);

		List<ClientAddress> list2 = new ArrayList<ClientAddress>(allAddresses
				.values());
		if (!list2.isEmpty())
			clientCompany.setAddresses(list2);
		else
			clientCompany.setAddresses(FinanceApplication.getCompany()
					.getAddresses());

		clientCompany
				.setRegistrationNumber(getStringValue(registrationNumberText));

		ViewManager.getInstance().updateCompany(clientCompany, this);

	}

	public String getStringValue(FormItem item) {
		return item.getValue() != null ? item.getValue().toString() : "";

	}

	public boolean getBooleanValue(FormItem item) {
		return item.getValue() != null ? (Boolean) item.getValue() : false;

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		this.removeFromParent();
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		return validateCompanyDetailsForm(companyDetailsForm);

	}

	private boolean validateCompanyDetailsForm(DynamicForm companyDetailsForm)
			throws InvalidEntryException {
		if (!companyDetailsForm.validate()) {
			throw new InvalidEntryException(AccounterErrorType.REQUIRED_FIELDS);
		}
		return true;
	}

}
