package com.vimukti.accounter.web.client.ui.setup;

import java.util.LinkedHashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
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
	private TextItem companynameText, legalnameText, textId, phonenumberText,
			faxNumberText, webaddressText;
	private EmailField emailText;
	private TextAreaItem streetadressText;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	private DynamicForm dynamicForm;

	@Override
	public String getHeader() {
		return "Enter Your Company Information";
	}

	@Override
	public VerticalPanel getPageBody() {

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		Label label = new Label(
				"accounter uses this information on ypur tax forms and froms you use to communicate with ypur cusomers and vendors");

		companynameText = new TextItem(Accounter.constants().companyName());
		companynameText.setHelpInformation(true);
		companynameText.setRequired(true);
		companynameText.setWidth(100);

		legalnameText = new TextItem(Accounter.constants().legalName());
		legalnameText.setHelpInformation(true);
		legalnameText.setWidth(100);

		textId = new TextItem(Accounter.constants().textId());
		textId.setHelpInformation(true);
		textId.setRequired(true);
		textId.setWidth(100);

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

		dynamicForm.setFields(companynameText, legalnameText, textId,
				streetadressText, phonenumberText, faxNumberText, emailText,
				webaddressText);
		mainpanel.add(label);
		mainpanel.add(dynamicForm);
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
		// TODO Auto-generated method stub
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub
	}
}
