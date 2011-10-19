package com.vimukti.accounter.web.client.ui.setup;

import java.util.LinkedHashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * about Company information
 * 
 * @author vimukti2
 * 
 */
public class SetupCompanyInfoPage extends AbstractSetupPage {
	private TextItem companynameText, legalnameText, taxId, phonenumberText,
			faxNumberText, webaddressText;
	private EmailField emailText;
	private TextAreaItem streetadressText;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	private DynamicForm dynamicForm;
	private ClientCompany company;

	public SetupCompanyInfoPage() {
		super();
	}

	@Override
	public String getHeader() {
		return this.accounterConstants.enterYourCompanyInfo();
	}

	@Override
	public VerticalPanel getPageBody() {

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		HTML label = new HTML(Accounter.messages().accounterUseYourForms(
				Global.get().customer(), Global.get().vendor()));

		companynameText = new TextItem(Accounter.constants().companyName());
		companynameText.setHelpInformation(true);
		companynameText.setRequired(true);
		companynameText.setWidth(100);

		legalnameText = new TextItem(Accounter.constants().legalName());
		legalnameText.setHelpInformation(true);
		legalnameText.setWidth(100);

		taxId = new TextItem(Accounter.constants().taxId());
		taxId.setHelpInformation(true);
		taxId.setWidth(100);

		streetadressText = new TextAreaItem(Accounter.constants().address());
		streetadressText.setHelpInformation(true);
		streetadressText.setWidth(100);
		streetadressText.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				createaddressDialog();
			}
		});
		streetadressText.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent arg0) {
				createaddressDialog();
			}
		});

		phonenumberText = new TextItem(Accounter.constants().phoneNumber());
		phonenumberText.setHelpInformation(true);
		phonenumberText.setWidth(100);

		faxNumberText = new TextItem(Accounter.constants().fax());
		faxNumberText.setHelpInformation(true);
		faxNumberText.setWidth(100);

		emailText = new EmailField(Accounter.constants().email());
		emailText.setHelpInformation(true);
		emailText.setWidth(100);

		webaddressText = new TextItem(Accounter.constants().webSite());
		webaddressText.setHelpInformation(true);
		webaddressText.setWidth(100);

		VerticalPanel mainpanel = new VerticalPanel();

		dynamicForm = UIUtils.form(Accounter.constants().customer());

		dynamicForm.setFields(companynameText, legalnameText, taxId,
				streetadressText, phonenumberText, faxNumberText, emailText,
				webaddressText);
		mainpanel.add(label);
		mainpanel.add(dynamicForm);
		mainpanel.addStyleName("setuppage_body");
		return mainpanel;
	}

	/**
	 * create address dialog
	 */
	private void createaddressDialog() {
		new AddressDialog("", "", streetadressText, " ", allAddresses);
	}

	@Override
	public void onLoad() {

		this.company = Accounter.getCompany();
		if (this.company != null) {
			companynameText.setValue(company.getName());
			legalnameText.setValue(company.getTradingName());
			this.taxId.setValue(company.getTaxId());
			this.faxNumberText.setValue(company.getFax());
			this.phonenumberText.setValue(company.getPhone());
			this.webaddressText.setValue(company.getWebSite());
			this.emailText.setValue(company.getCompanyEmail());
			allAddresses.put(ClientAddress.TYPE_COMPANY,
					company.getTradingAddress());
			setAddressToTextItem(streetadressText, company.getTradingAddress());
		}
	}

	/**
	 * client address values will be set to textItem
	 * 
	 * @param textItem
	 * @param address
	 */
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
		streetadressText.setValue(toToSet);
	}

	@Override
	public void onSave() {

		ClientCompany clientCompany = new ClientCompany();
		clientCompany.id = company.id;
		clientCompany.setTradingName(companynameText.getValue().toString());
		clientCompany.setLegalName(legalnameText.getValue().toString());
		clientCompany.setPhone(phonenumberText.getValue().toString());
		clientCompany.setCompanyEmail(emailText.getValue().toString());
		clientCompany.setTaxId(taxId.getValue().toString());
		clientCompany.setFax(faxNumberText.getValue().toString());
		clientCompany.setWebSite(webaddressText.getValue().toString());
		if (!allAddresses.isEmpty()) {
			clientCompany.setTradingAddress(allAddresses
					.get(ClientAddress.TYPE_COMPANY));
		} else {
			clientCompany.setTradingAddress(Accounter.getCompany()
					.getTradingAddress());
		}
		Accounter.setCompany(clientCompany);
	}

	// not required for this page

	@Override
	public boolean doShow() {
		return true;
	}

	@Override
	public boolean validate() {
		return false;
	}

}
