package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class EmailView extends AbstractBaseView {

	private TextItem fromAddress;
	private TextItem toAddress;
	private TextItem ccAddress;
	private TextItem subject;
	private TextAreaItem emailBody;
	private Label attachmentLabel;

	private Button sendBtn;
	private Button cancelBtn;
	final DynamicForm form1 = UIUtils.form(Accounter.constants().type());
	final DynamicForm form2 = UIUtils.form(Accounter.constants().type());
	final DynamicForm form3 = UIUtils.form(Accounter.constants().type());

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		createControls();
	}

	public void createControls() {
		String fileName = "";
		String date = "";
		String message = "Dear Customer,"
				+ "\n\n"
				+ " May we remind you that the"
				+ fileName
				+ "issued on"
				+ date
				+ "is due for payment."
				+ "\n"
				+ "If you have already paid for this invoice, accept our apologies and ignore this reminder."
				+ "\n"
				+ "Feel free to get in touch with us for any clarifications."
				+ "\n" + "Thanks in advance for the payment.";

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
		attachmentItem.setWidth(60);

		form2.setFields(attachmentItem);
		vPanel.add(attachmentLabel);
		vPanel.add(form2);

		form1.setFields(fromAddress, toAddress, ccAddress, subject);
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
				// TODO Auto-generated method stub

			}
		});

		cancelBtn = new Button(constants.cancel());
		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});

		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(sendBtn);
		buttonsPanel.add(cancelBtn);

		mainPanel.add(buttonsPanel);
		mainPanel.setWidth("100%");

		this.add(mainPanel);
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
		return null;
	}

}
