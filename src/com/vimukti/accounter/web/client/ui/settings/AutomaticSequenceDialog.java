package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

@SuppressWarnings("unchecked")
public class AutomaticSequenceDialog extends BaseDialog {

	private HTML paraHTML;
	private FlexTable optionsTable;
	private Label invoicePrefixlabel, creditNoteLabel, nextNumberLabel;
	private TextBox invoiceBox, creditBox, nextBox;
	private VerticalPanel subLayoutPanel, invoicePanel, creditPanel, nextPanel;

	public AutomaticSequenceDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	private void createControls() {
		subLayoutPanel = new VerticalPanel();
		paraHTML = new HTML(FinanceApplication.getSettingsMessages()
				.automaticSequencingData());

		invoicePanel = new VerticalPanel();
		invoicePrefixlabel = new Label(FinanceApplication.getSettingsMessages()
				.invoicePrefix());
		invoiceBox = new TextBox();
		invoiceBox.setText(FinanceApplication.getSettingsMessages()
				.invoicePrefixValue());
		invoicePanel.add(invoicePrefixlabel);
		invoicePanel.add(invoiceBox);

		creditPanel = new VerticalPanel();
		creditNoteLabel = new Label(FinanceApplication.getSettingsMessages()
				.creditNotePrefix());
		creditBox = new TextBox();
		creditBox.setText(FinanceApplication.getSettingsMessages()
				.creditNotePrefixValue());
		creditPanel.add(creditNoteLabel);
		creditPanel.add(creditBox);

		nextPanel = new VerticalPanel();
		nextNumberLabel = new Label(FinanceApplication.getSettingsMessages()
				.nextNumber());
		nextBox = new TextBox();
		nextBox.setText(FinanceApplication.getSettingsMessages()
				.nextNumberValue());
		nextPanel.add(nextNumberLabel);
		nextPanel.add(nextBox);

		optionsTable = new FlexTable();
		optionsTable.setWidget(0, 0, invoicePanel);
		optionsTable.setWidget(0, 1, creditPanel);
		optionsTable.setWidget(0, 2, nextPanel);

		okbtn.setText(FinanceApplication.getSettingsMessages().saveButton());
		okbtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				okClicked();
			}
		});
		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelClicked();
			}
		});

		subLayoutPanel.add(paraHTML);
		subLayoutPanel.add(optionsTable);
		mainPanel.add(subLayoutPanel);
	}
}
