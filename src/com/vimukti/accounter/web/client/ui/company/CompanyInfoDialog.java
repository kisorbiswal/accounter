package com.vimukti.accounter.web.client.ui.company;

import java.util.LinkedHashMap;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
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

public class CompanyInfoDialog extends BaseDialog<ClientAddress> {

	TextItem websiteText, taxIDText, companyNameText, trandigNameText,
			registrationNumberText, bankAccountText, sortCodeText;
	EmailField emailText;
	IntegerField phoneText, faxText;

	DynamicForm companyDetailsForm, phoneAndFaxForm, taxIDForm,
			RegistrationNumberForm;

	private TextAreaItem address;

	private LinkedHashMap<String, ClientAddress> addresses = new LinkedHashMap<String, ClientAddress>();

	protected String str;

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
		this.company = getCompany();
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
			allAddresses.put(ClientAddress.TYPE_COMPANY, company
					.getTradingAddress());
			setAddressToTextItem(textareaItem, company.getTradingAddress());
			allAddresses.put(ClientAddress.TYPE_COMPANY_REGISTRATION, company
					.getRegisteredAddress());
			setAddressToTextItem(textareaItem2, company.getRegisteredAddress());
			registrationNumberText.setValue(company.getRegistrationNumber());

			doupaySalesChecBox.setValue(getCompany().getPreferences()
					.isChargeSalesTax());

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
		// setTitle(Accounter.constants().companyInformation());
		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		companyNameText = new TextItem(Accounter.constants().registeredName());
		companyNameText.setHelpInformation(true);
		companyNameText.setRequired(true);
		companyNameText.setWidth(100);
		string = "[a-zA-Z0-9 /s]";

		textareaItem = new TextAreaItem(Accounter.constants()
				.registeredAddress());
		textareaItem.setHelpInformation(true);
		textareaItem.setRequired(false);
		textareaItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("title", "description", textareaItem,
						"company", allAddresses);
			}
		});

		trandigNameText = new TextItem(Accounter.constants().tradingName());
		trandigNameText.setHelpInformation(true);
		trandigNameText.setRequired(true);
		trandigNameText.setWidth(100);

		textareaItem2 = new TextAreaItem(Accounter.constants().tradingAddress());
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

		companyDetailsForm = UIUtils.form(Accounter.constants()
				.companyDetails());

		VerticalPanel mainVLay2 = new VerticalPanel();

		phoneText = new IntegerField(this, Accounter.constants()
				.businessPhone());
		phoneText.setHelpInformation(true);

		faxText = new IntegerField(this, Accounter.constants().businessFax());
		faxText.setHelpInformation(true);

		emailText = new EmailField(Accounter.constants().email());
		emailText.setHelpInformation(true);

		websiteText = new TextItem(Accounter.constants().webPageAddress());
		websiteText.setHelpInformation(true);

		registrationNumberText = new TextItem(Accounter.constants()
				.companyRegistrationNumber());
		registrationNumberText.setHelpInformation(true);

		taxIDText = new TextItem(Accounter.constants().federalTaxId());
		taxIDText.setHelpInformation(true);

		bankAccountText = new TextItem();
		bankAccountText.setTitle(Accounter.messages().bankAccountNo(
				Global.get().Account()));
		bankAccountText.setHelpInformation(true);

		sortCodeText = new TextItem();
		sortCodeText.setTitle(Accounter.constants().sortCode());
		sortCodeText.setHelpInformation(true);

		phoneAndFaxForm = UIUtils.form(Accounter.constants()
				.phoneAndFaxNumbers());

		phoneAndFaxForm.setFields(phoneText, faxText, websiteText, emailText,
				registrationNumberText, taxIDText, bankAccountText,
				sortCodeText);
		phoneAndFaxForm.setNumCols(8);

		DynamicForm taxesForm = new DynamicForm();
		// taxesForm.setWidth100();
		// taxesForm.setHeight("*");
		taxesForm.setIsGroup(true);
		taxesForm.setGroupTitle(Accounter.constants().taxes());
		// taxesForm.setTitleOrientation(TitleOrientation.TOP);
		// taxesForm.setPadding(10);

		doupaySalesChecBox = new CheckboxItem();
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			doupaySalesChecBox.setTitle(Accounter.constants()
					.doYoupaySalesTaxes());

		} else
			doupaySalesChecBox.setTitle(Accounter.constants()
					.areYouRegisteredForVAT());
		vatRegNumber = new TextItem(
				getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? Accounter
						.constants().vatRegistrationNumber()
						: Accounter.constants().taxRegNo());
		vatRegNumber.setHelpInformation(true);
		vatRegNumber.setWidth(100);
		vatRegNumber.setDisabled(false);

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
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
							vatRegNumber.setValue(Accounter.getCompany()
									.getPreferences()
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
							vatRegNumber.setValue(Accounter.getCompany()
									.getPreferences()
									.getVATregistrationNumber());
						}
					});
		}
		vatRegNumber.setValue(getCompany().getPreferences()
				.getVATregistrationNumber());
		taxgroupBtn = new Button(Accounter.constants().taxgroups());
		// taxgroupBtn.setColSpan("*");
		taxgroupBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				preferencesView.taxGroupButtonClick();
			}
		});
		paysalesTaxgroupItem = new RadioGroupItem();
		paysalesTaxgroupItem.setTitle(Accounter.constants()
				.onWhatbasisdoUpaySalesTaxes());
		// paysalesTaxgroupItem.setColSpan("*");
		// paysalesTaxgroupItem.setVertical(false);
		// paysalesTaxgroupItem
		// .setValue(company.getPreferences() != null ? company
		// .getPreferences().getIsAccuralBasis() ? "1" : "2" : "1");

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map
				.put("1", Accounter.messages().accrualBasis(
						Global.get().customer()));
		map.put("2", Accounter.messages().cashBasis(Global.get().customer()));
		paysalesTaxgroupItem.setValueMap(map);

		// if(!FinanceApplication.getCompany().getpreferences().getDoYouPaySalesTax())
		// vatRegNumber.setDisabled(true);

		companyDetailsForm.setFields(companyNameText, textareaItem,
				trandigNameText, textareaItem2, doupaySalesChecBox,
				vatRegNumber);
		companyDetailsForm.getCellFormatter().addStyleName(1, 0,
				"memoFormAlign");
		companyDetailsForm.getCellFormatter().addStyleName(3, 0,
				"memoFormAlign");
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

		okbtn.setText(Accounter.constants().update());

		mainHLay.setWidth("600px");
		mainHLay.setHeight("250px");

		setBodyLayout(mainHLay);

	}

	public void close() {

	}

	protected void updatedCompany() {

		/*
		 * Here we are creating a new ClientCompany Object, to avoid sending all
		 * the lists to server. Only the necessary fields which are required to
		 * be updated are only sent to server.
		 */
		ClientCompany clientCompany = new ClientCompany();
		clientCompany.id = company.companyID;
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
				.setChargeSalesTax(getBooleanValue(doupaySalesChecBox));
		companyPreferences.setVATregistrationNumber(vatRegNumber.getValue()
				.toString());

		clientCompany.setPreferences(companyPreferences);

		if (!allAddresses.isEmpty()) {
			clientCompany.setTradingAddress(allAddresses
					.get(ClientAddress.TYPE_COMPANY));
			clientCompany.setRegisteredAddress(allAddresses
					.get(ClientAddress.TYPE_COMPANY_REGISTRATION));
		} else {
			clientCompany.setTradingAddress(getCompany().getTradingAddress());
			clientCompany.setRegisteredAddress(getCompany()
					.getRegisteredAddress());
		}

		clientCompany
				.setRegistrationNumber(getStringValue(registrationNumberText));

		Accounter.updateCompany(this, clientCompany);

	}

	public String getStringValue(FormItem item) {
		return item.getValue() != null ? item.getValue().toString() : "";

	}

	public boolean getBooleanValue(FormItem item) {
		return item.getValue() != null ? (Boolean) item.getValue() : false;

	}

	@Override
	public Object getGridColumnValue(ClientAddress obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		this.removeFromParent();
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	@Override
	protected ValidationResult validate() {
		return companyDetailsForm.validate();

	}

	@Override
	protected boolean onOK() {
		updatedCompany();
		return true;
	}

	@Override
	public void setFocus() {
		companyNameText.setFocus();

	}
}
