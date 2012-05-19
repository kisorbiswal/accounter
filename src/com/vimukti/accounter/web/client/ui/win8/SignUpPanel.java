package com.vimukti.accounter.web.client.ui.win8;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.SignupDetails;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class SignUpPanel extends FlowPanel {
	TextItem firstname, lastname, emailid, confirmMailid, phone;
	PasswordItem password, confirmPassword;
	SelectCombo countrySelect;
	SignupDetails signupDetails;
	DynamicForm signupForm;
	CheckBox agreeterms, newsLetters;
	WebsocketAccounterInitialiser accounter;
	Label errorlLabel;

	public SignUpPanel(WebsocketAccounterInitialiser accounter) {
		getElement().setId("signUpPanel");
		this.accounter = accounter;
		createControls();
	}

	private void createControls() {
		errorlLabel = new Label();
		errorlLabel.setStyleName("errors");
		firstname = new TextItem(Accounter.getMessages().firstName(),
				"firstname");
		lastname = new TextItem(Accounter.getMessages().lastName(), "lastname");
		emailid = new TextItem(Accounter.getMessages().email(), "emailid");
		confirmMailid = new TextItem(Accounter.getMessages2().confirmEmail(),
				"confirmemailid");
		password = new PasswordItem(Accounter.getMessages().password());
		confirmPassword = new PasswordItem(Accounter.getMessages()
				.confirmPassword());
		phone = new TextItem(Accounter.getMessages().phone(), "phone");
		countrySelect = new SelectCombo(Accounter.getMessages().country());
		String[] countries = CoreUtils.getCountries();
		for (int i = 0; i < countries.length; i++) {
			countrySelect.setComboItem(countries[i]);
		}
		agreeterms = new CheckBox();
		Anchor termaAndConditionsAnchor = new Anchor(Accounter.getMessages()
				.termsConditions());
		termaAndConditionsAnchor.setHref("/site/termsandconditions");
		newsLetters = new CheckBox();
		newsLetters.setText(Accounter.getMessages().newsletter());
		newsLetters.setValue(true);
		final AccounterAsyncCallback<Boolean> accounterAsyncCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				errorlLabel.setText(exception.getMessage());

			}

			@Override
			public void onResultSuccess(Boolean result) {
				accounter.showView(new ActivationPanel(accounter));

			}

		};
		Button submitbutton = new Button(Accounter.getMessages().submit(),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						errorlLabel.setText("");
						if (!emailid.getValue()
								.equals(confirmMailid.getValue())) {
							errorlLabel.setText(Accounter.getMessages()
									.emailIdAndConfirmEmaildMustBeSame());
						}
						if (validate()) {
							signupDetails = new SignupDetails();
							signupDetails.setFirstName(firstname.getValue());
							signupDetails.setLastName(lastname.getValue());
							signupDetails.setEmailId(emailid.getValue());
							signupDetails.setPassword(password.getValue());
							signupDetails.setConfirmPassword(confirmPassword
									.getValue());
							signupDetails.setCountry(countrySelect
									.getSelectedValue());
							signupDetails.setAcceptedTerms(agreeterms
									.getValue());
							signupDetails.setSubscribeUpdates(newsLetters
									.getValue());
							Accounter.createWindowsRPCService().signup(
									signupDetails, accounterAsyncCallback);

						}
					}
				});
		submitbutton.setStyleName("success");
		Button cancelButton = new Button(Accounter.getMessages().cancel(),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						accounter.showView(new LoginPanel(accounter));
					}
				});
		cancelButton.setStyleName("cancel");

		StyledPanel stylPanel = new StyledPanel("headernFooter");

		StyledPanel centerPanel = new StyledPanel("centerPanel");

		centerPanel.add(new HTML("<h2>" + Accounter.getMessages().signUp()
				+ "</h2>"));
		centerPanel.add(errorlLabel);
		signupForm = new DynamicForm("signupForm");
		signupForm.add(firstname, lastname, emailid, confirmMailid, password,
				confirmPassword, phone, countrySelect);
		centerPanel.add(signupForm);
		centerPanel.add(agreeterms);
		centerPanel.add(termaAndConditionsAnchor);
		centerPanel.add(newsLetters);
		centerPanel.add(submitbutton);
		centerPanel.add(cancelButton);

		add(stylPanel);
		add(centerPanel);
		add(stylPanel);

	}

	private boolean validate() {
		if (firstname.getValue().isEmpty() || lastname.getValue().isEmpty()
				|| emailid.getValue().isEmpty()
				|| password.getValue().isEmpty()
				|| confirmPassword.getValue().isEmpty()
				|| agreeterms.getValue().equals(Boolean.FALSE)) {
			errorlLabel.setText(Accounter.getMessages().shouldNotEmpty());
			return false;
		} else {
			return true;
		}
	}
}
