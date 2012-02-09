package com.vimukti.accounter.web.client.ui.customers;

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
import com.vimukti.accounter.web.client.core.ClientEmailAccount;
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
import com.vimukti.accounter.web.client.ui.company.EmailAccountDialog;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
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

	private Button sendBtn;
	private Button cancelBtn;
	private Button smtpBtn;
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

		fromAddcombo = new EmailCombo(messages.from(), true);
		fromAddcombo.setRequired(true);
		fromAddcombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientEmailAccount>() {

					@Override
					public void selectedComboBoxItem(
							ClientEmailAccount selectItem) {
						if (selectItem != null) {
							smtpBtn.setEnabled(true);
						} else {
							smtpBtn.setEnabled(false);
						}
					}
				});

		smtpBtn = new Button(messages.smtpSettings());
		smtpBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showEmailAccountDialog();
			}
		});
		smtpBtn.setEnabled(fromAddcombo.getSelectedValue() != null ? true
				: false);

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

		subject = new TextItem(messages.subject());
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
		horPanel.add(smtpBtn);
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

	protected void showEmailAccountDialog() {
		EmailAccountDialog dialog = new EmailAccountDialog(
				fromAddcombo.getSelectedValue());
		dialog.setCallback(new ActionCallback<ClientEmailAccount>() {

			@Override
			public void actionResult(ClientEmailAccount result) {
				fromAddcombo.setComboItem(result);
			}
		});
		dialog.center();
	}

	private String getValidMail(String email) {
		if (email.trim().length() != 0) {
			if (!UIUtils.isValidMultipleEmailIds(email)) {
				Accounter.showError(messages.invalidEmail());
				return "";
			} else
				return email;
		}
		return email;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		result.add(FormItem.validate(fromAddcombo, toAddress));
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		if (UIUtils.isValidMultipleEmailIds(toAddress.getValue().toString())) {
			companyName = getPreferences().getTradingName();
			ToAdd = toAddress.getValue().toString() != null ? toAddress
					.getValue().toString() : "";
			getValidMail(toAddress.getValue().toString());
			ccAdd = ccAddress.getValue().toString() != null ? ccAddress
					.getValue().toString() : "";
			sub = subject.getValue().toString() != null ? subject.getValue()
					.toString() : "";
			body = emailBody.getValue().toString() != null ? emailBody
					.getValue().toString() : "";
			body = body.replaceAll("\n", "<br/>");

			Accounter.createHomeService().sendPdfInMail(fileName, sub, body,
					fromAddcombo.getSelectedValue(), ToAdd, ccAdd,
					new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(Void result) {
							MainFinanceWindow.getViewManager()
									.closeCurrentView();
						}
					});
		}
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
