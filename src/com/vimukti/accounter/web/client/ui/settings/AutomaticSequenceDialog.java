package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class AutomaticSequenceDialog extends BaseDialog {

	private HTML paraHTML;
	private FlexTable optionsTable;
	private Label invoicePrefixlabel, creditNoteLabel, nextNumberLabel;
	private TextBox invoiceBox, creditBox, nextBox;
	private VerticalPanel subLayoutPanel, invoicePanel, creditPanel, nextPanel;
	private Button saveButton;
	private HorizontalPanel buttonPanel;

	public AutomaticSequenceDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	private void createControls() {
		subLayoutPanel = new VerticalPanel();
		paraHTML = new HTML(
				"<p><font size='1px'>Define the number to be used when creating your next invoice or credit note. The number<br>will automatically increment with each new invoice or credit note you create</font></p>");

		invoicePanel = new VerticalPanel();
		invoicePrefixlabel = new Label("Invoice Prefix");
		invoiceBox = new TextBox();
		invoiceBox.setText("INV-");
		invoicePanel.add(invoicePrefixlabel);
		invoicePanel.add(invoiceBox);

		creditPanel = new VerticalPanel();
		creditNoteLabel = new Label("Credit Note Prefix");
		creditBox = new TextBox();
		creditBox.setText("CN-");
		creditPanel.add(creditNoteLabel);
		creditPanel.add(creditBox);

		nextPanel = new VerticalPanel();
		nextNumberLabel = new Label("Next Number");
		nextBox = new TextBox();
		nextBox.setText("0036");
		nextPanel.add(nextNumberLabel);
		nextPanel.add(nextBox);

		optionsTable = new FlexTable();
		optionsTable.setWidget(0, 0, invoicePanel);
		optionsTable.setWidget(0, 1, creditPanel);
		optionsTable.setWidget(0, 2, nextPanel);

		buttonPanel = new HorizontalPanel();
		saveButton = new Button("Save");
		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});
		cancelBtn = new Button("Cancel");
		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelClicked();
			}
		});
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelBtn);

		subLayoutPanel.add(paraHTML);
		subLayoutPanel.add(optionsTable);
		subLayoutPanel.add(buttonPanel);
		mainPanel.add(subLayoutPanel);
	}
}
