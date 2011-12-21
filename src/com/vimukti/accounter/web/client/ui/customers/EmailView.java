package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.EmailCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class EmailView extends AbstractBaseView implements AsyncCallback<Void> {
	private ClientInvoice invoice;
	// private EmailField fromAddress;
	private EmailField toAddress;
	private EmailField ccAddress;
	private TextItem subject;
	private TextAreaItem emailBody;
	private Label attachmentLabel;
	private long brandingThemeId;
	private String ToAdd, ccAdd, sub, body, companyName, fileName;

	private EmailCombo fromAddcombo;

	String from = "";
	private Button sendBtn;
	private Button cancelBtn;
	final DynamicForm form1 = new DynamicForm();
	final DynamicForm form2 = new DynamicForm();
	final DynamicForm form3 = new DynamicForm();

	public EmailView(ClientInvoice inovoice) {
		this.invoice = inovoice;
	}

	public EmailView() {
	}

	@Override
	public void init() {
		super.init();
		preparePdfFile();
		createControls();

	}

	private void preparePdfFile() {

		AsyncCallback callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(String result) {
				fileName = result.toString();
			};
		};

		Accounter.createHomeService().createPdfFile(
				((ClientInvoice) invoice).getID(),
				ClientTransaction.TYPE_INVOICE, brandingThemeId, callback);

	}

	@SuppressWarnings("unchecked")
	public void createControls() {

		AccounterMessages messages = Global.get().messages();

		fromAddcombo = new EmailCombo(Accounter.messages().from(), false);
		ArrayList<String> toAdd = fromAddcombo.getToAddress();

		from = toAdd.get(0);
		fromAddcombo.setComboItem(toAdd.get(0));

		ClientContact contact = invoice.getContact();
		String toemail = contact != null ? contact.getEmail() : "";
		toAddress = new EmailField(messages.to());
		toAddress.setText(toemail.trim());
		toAddress.setHelpInformation(true);
		toAddress.setRequired(true);

		ccAddress = new EmailField(messages.cc());
		ccAddress.setHelpInformation(true);
		ccAddress.setRequired(false);
		ccAddress.setWidth(80);

		subject = new TextItem(Accounter.messages().subject());
		subject.setWidth(80);

		emailBody = new TextAreaItem();
		emailBody.setMemo(true, this);
		emailBody.setValue(Global
				.get()
				.messages()
				.invoiceMailMessage(Global.get().Customer(),
						this.invoice.getNumber(), invoice.getDate()));
		emailBody.setWidth("100%");
		emailBody.setHeight(200);

		VerticalPanel vPanel = new VerticalPanel();
		attachmentLabel = new Label(messages.attachments());

		TextAreaItem attachmentItem = new TextAreaItem();
		attachmentItem.setValue("Invoice_" + invoice.getNumber() + ".pdf");
		attachmentItem.setDisabled(true);
		attachmentItem.setWidth(60);

		form2.setFields(attachmentItem);
		vPanel.add(attachmentLabel);
		vPanel.add(form2);

		form1.setFields(fromAddcombo, toAddress, ccAddress, subject);
		HorizontalPanel horPanel = new HorizontalPanel();
		horPanel.add(form1);
		horPanel.add(vPanel);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(horPanel);

		form3.setFields(emailBody);
		form3.setWidth("100%");
		form3.addStyleName("email_textarea");
		mainPanel.add(form3);

		sendBtn = new Button(messages.send());
		sendBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// for checking the From and To email address

				onSave(false);

			}

		});

		cancelBtn = new Button(messages.cancel());
		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainFinanceWindow.getViewManager().closeCurrentView();

			}
		});

		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(sendBtn);
		buttonsPanel.add(cancelBtn);

		mainPanel.add(buttonsPanel);
		mainPanel.setWidth("100%");

		this.add(mainPanel);

		fromAddcombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						fromAddcombo.setComboItem(selectItem);
						from = (String) selectItem;
					}

				});

		toAddress.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (toAddress.getValue() != null)

					toAddress.setText(getValidMail(toAddress.getValue()));
			}
		});
		ccAddress.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (ccAddress.getValue() != null)

					ccAddress.setText(getValidMail(ccAddress.getValue()));
			}
		});
	}

	private String getValidMail(String email) {
		if (email.trim().length() != 0) {
			if (!UIUtils.isValidMultipleEmailIds(email)) {
				Accounter.showError(Accounter.messages().invalidEmail());
				return "";
			} else
				return email;
		}
		return email;
	}

	private void updateControls() {
		companyName = getPreferences().getTradingName();
		ToAdd = toAddress.getValue().toString() != null ? toAddress.getValue()
				.toString() : "";
		getValidMail(toAddress.getValue().toString());
		ccAdd = ccAddress.getValue().toString() != null ? ccAddress.getValue()
				.toString() : "";
		sub = subject.getValue().toString() != null ? subject.getValue()
				.toString() : "";
		body = emailBody.getValue().toString() != null ? emailBody.getValue()
				.toString() : "";
		body = body.replaceAll("\n", "<br/>");

		Accounter.createHomeService().sendPdfInMail(fileName, sub, body, from,
				ToAdd, ccAdd, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void result) {
						MainFinanceWindow.getViewManager().closeCurrentView();
					}
				});

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		if (from.trim().length() == 0
				&& toAddress.getValue().trim().length() == 0) {
			result.addError(
					fromAddcombo,
					Accounter.messages().pleaseEnter(
							Accounter.messages().to() + " & "
									+ Accounter.messages().from()));
		} else if (from.trim().length() == 0) {
			result.addError(
					fromAddcombo,
					Accounter.messages().pleaseEnter(
							Accounter.messages().from()));
		} else if (toAddress.getValue().trim().length() == 0) {
			result.addError(toAddress,
					Accounter.messages().pleaseEnter(Accounter.messages().to()));
		} else if (UIUtils.isValidEmail(from)
				&& UIUtils.isValidMultipleEmailIds(toAddress.getValue()
						.toString())) {
			updateControls();

		}

		return result;
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
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return messages.email();
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setThemeId(long themeId) {
		this.brandingThemeId = themeId;

	}
}
