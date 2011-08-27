package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.EmailCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class EmailView extends AbstractBaseView implements AsyncCallback<Void> {
	private ClientInvoice invoice;
	private TextItem fromAddress;
	private TextItem toAddress;
	private TextItem ccAddress;
	private TextItem subject;
	private TextAreaItem emailBody;
	private Label attachmentLabel;

	private EmailCombo fromAddcombo;

	String from;
	private Button sendBtn;
	private Button cancelBtn;
	final DynamicForm form1 = UIUtils.form(Accounter.constants().type());
	final DynamicForm form2 = UIUtils.form(Accounter.constants().type());
	final DynamicForm form3 = UIUtils.form(Accounter.constants().type());

	public EmailView(ClientInvoice inovoice) {
		this.invoice = inovoice;
	}

	public EmailView() {

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		createControls();
	}

	public void createControls() {

		fromAddcombo = new EmailCombo("From");

		String message = "Dear Customer,"
				+ "\n\n"
				+ " May we remind you that the Invoice_"
				+ this.invoice.getNumber()
				+ " issued on"
				+ invoice.getDate()
				+ " is due for payment."
				+ "\n"
				+ " If you have already paid for this invoice, accept our apologies and ignore this reminder."
				+ "\n"
				+ " Feel free to get in touch with us for any clarifications."
				+ "\n" + " Thanks in advance for the payment." + "\n\n\n"
				+ "Regards";

		fromAddress = new TextItem("From");
		fromAddress.setWidth(80);
		fromAddress.setRequired(true);

		toAddress = new TextItem("To");
		toAddress.setWidth(80);
		toAddress.setRequired(true);

		ccAddress = new TextItem("Cc");
		ccAddress.setWidth(80);

		subject = new TextItem("Subject");
		subject.setWidth(80);

		emailBody = new TextAreaItem();
		emailBody.setMemo(true, this);
		emailBody.setValue(message);
		emailBody.setWidth("100%");
		emailBody.setHeight(200);

		VerticalPanel vPanel = new VerticalPanel();
		attachmentLabel = new Label(constants.attachments());

		TextAreaItem attachmentItem = new TextAreaItem();
		attachmentItem.setValue("Invoice_" + invoice.getNumber());
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

		AccounterConstants constants = Global.get().constants();

		sendBtn = new Button(constants.send());
		sendBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				updateControls();

			}

		});

		cancelBtn = new Button(constants.cancel());
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
						from = (String) selectItem;
						System.err.println("............." + from);
					}

				});
	}

	private void updateControls() {

		String fromAdd = fromAddress.getValue().toString() != null ? fromAddress
				.getValue().toString() : "";
		String ToAdd = toAddress.getValue().toString() != null ? toAddress
				.getValue().toString() : "";
		String ccAdd = ccAddress.getValue().toString() != null ? ccAddress
				.getValue().toString() : "";
		String sub = subject.getValue().toString() != null ? subject.getValue()
				.toString() : "";
		String body = emailBody.getValue().toString() != null ? fromAddress
				.getValue().toString() : "";

		long themeId = 0;
		// UIUtils.sendPdfAttachment(((ClientInvoice) transaction).getID(),
		// ClientTransaction.TYPE_INVOICE, themeId, "application/pdf",
		// sub, body, from, ToAdd, ccAdd);
		Accounter.createHomeService().sendPdfInMail(
				((ClientInvoice) invoice).getID(),
				ClientTransaction.TYPE_INVOICE, 1, "application/pdf", sub,
				body, from, ToAdd, ccAdd, this);

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
		return constants.email();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub
		super.processupdateView(core, command);
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}
}
