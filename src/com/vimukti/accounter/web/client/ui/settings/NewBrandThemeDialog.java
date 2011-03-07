package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem;

@SuppressWarnings("unchecked")
public class NewBrandThemeDialog extends BaseDialog {

	private Label nameLabel, pageSizeLabel, topMarginLabel, bottomMarginLabel,
			addressPadLabel, fontLabel, fontSizeLabel, draftLabel,
			approvedLabel, overdueLabel, creditNoteLabel, statementLabel,
			logoLabel, taxesLabel, termsLabel;
	private RadioButton a4Button, usLetterButton, lettRadioButton,
			rightRadioButton, exclusiveButton, inclusiveButton;
	private VerticalPanel checkBoxPanel, radioButtonPanel,
			check_radio_textAreaPanel, button_textBoxPanel;
	private HorizontalPanel mainLayoutPanel, check_radioPanel, buttonPanel;
	private Button saveButton, cancelButton;
	private FlexTable textBoxTable;
	private CheckBox taxNumItem, headingItem, unitPriceItem, paymentItem,
			columnItem, addressItem, logoItem;
	private TextBoxItem nameBox, topMarginBox, bottomMarginBox, addressPadBox,
			draftBox, approvedBox, overdueBox, creditNoteBox, statementBox,
			paypalTextBox;
	private String[] fontNameArray, fontSizeArray;
	private ListBox fontNameBox, fontSizeBox;
	private HTML paypalEmailHtml, contactDetailHtml;
	private TextArea contactDetailsArea, termsPaymentArea;

	public NewBrandThemeDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	private void createControls() {
		mainLayoutPanel = new HorizontalPanel();
		check_radioPanel = new HorizontalPanel();
		check_radio_textAreaPanel = new VerticalPanel();
		button_textBoxPanel = new VerticalPanel();

		check_radioPanel.add(addCheckBoxTableControls());
		check_radioPanel.add(addRadioBoxTableControls());

		check_radio_textAreaPanel.add(check_radioPanel);
		termsLabel = new Label("Terms & Payment Advice");
		termsPaymentArea = new TextArea();
		termsPaymentArea.setWidth("450px");
		termsPaymentArea.setHeight("75px");
		check_radio_textAreaPanel.add(termsLabel);
		check_radio_textAreaPanel.add(termsPaymentArea);

		buttonPanel = new HorizontalPanel();
		saveButton = new Button("Save");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				NewBrandThemeDialog.this.hide();
			}
		});
		cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				NewBrandThemeDialog.this.hide();
				removeFromParent();
			}
		});
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		mainLayoutPanel.add(addTextBoxTableControl());
		mainLayoutPanel.add(check_radio_textAreaPanel);
		button_textBoxPanel.add(mainLayoutPanel);
		button_textBoxPanel.add(buttonPanel);

		okbtn.setVisible(false);
		cancelBtn.setVisible(false);
		mainPanel.add(button_textBoxPanel);

	}

	private VerticalPanel addRadioBoxTableControls() {
		radioButtonPanel = new VerticalPanel();

		logoLabel = new Label("Logo alignment");
		lettRadioButton = new RadioButton("group2", "Left");
		rightRadioButton = new RadioButton("group2", "Right");
		taxesLabel = new Label("Show taxes as");
		exclusiveButton = new RadioButton("group3", "Exclusive");
		inclusiveButton = new RadioButton("group3", "Inclusive");

		contactDetailHtml = new HTML(
				"<p>Enter your contact details as they should<br>appear at the top of all PDFs you print or<br>send</p>");
		contactDetailsArea = new TextArea();

		radioButtonPanel.add(logoLabel);
		radioButtonPanel.add(lettRadioButton);
		radioButtonPanel.add(rightRadioButton);
		radioButtonPanel.add(taxesLabel);
		radioButtonPanel.add(exclusiveButton);
		radioButtonPanel.add(inclusiveButton);
		radioButtonPanel.add(contactDetailHtml);
		radioButtonPanel.add(contactDetailsArea);

		return radioButtonPanel;
	}

	private VerticalPanel addCheckBoxTableControls() {

		taxNumItem = new CheckBox("Show tax Number");
		headingItem = new CheckBox("Show column headings");
		unitPriceItem = new CheckBox("Show unit price & quantity");
		paymentItem = new CheckBox("Show payment advice cut-away");
		columnItem = new CheckBox("Show tax column");
		addressItem = new CheckBox("Show registered address");
		logoItem = new CheckBox("Show logo");

		paypalEmailHtml = new HTML(
				"<p>Enter your <b>PayPal</b> email to have<br>payment links automatically appear<br>on all your invoices</p>");
		paypalTextBox = new TextBoxItem();
		paypalTextBox.setWidth("2");

		checkBoxPanel = new VerticalPanel();
		checkBoxPanel.add(taxNumItem);
		checkBoxPanel.add(headingItem);
		checkBoxPanel.add(unitPriceItem);
		checkBoxPanel.add(paymentItem);
		checkBoxPanel.add(columnItem);
		checkBoxPanel.add(addressItem);
		checkBoxPanel.add(logoItem);
		checkBoxPanel.add(paypalEmailHtml);
		checkBoxPanel.add(paypalTextBox);

		return checkBoxPanel;

	}

	private FlexTable addTextBoxTableControl() {
		// measureHtml=new HTML("<p><b>Measure in</b></p>");
		nameLabel = new Label("Name");
		pageSizeLabel = new Label("Page size");
		topMarginLabel = new Label("Top margin");
		bottomMarginLabel = new Label("Bottom margin");
		addressPadLabel = new Label("Address padding");
		fontLabel = new Label("Font");
		fontSizeLabel = new Label("Font size");
		draftLabel = new Label("Draft Invoice title");
		approvedLabel = new Label("Approved Invoice title");
		overdueLabel = new Label("Overdue Invoice title");
		creditNoteLabel = new Label("Credit Note title");
		statementLabel = new Label("Statement title");

		nameBox = new TextBoxItem();
		nameBox.setWidth("150px");
		topMarginBox = new TextBoxItem();
		bottomMarginBox = new TextBoxItem();
		addressPadBox = new TextBoxItem();
		draftBox = new TextBoxItem();
		approvedBox = new TextBoxItem();
		overdueBox = new TextBoxItem();
		creditNoteBox = new TextBoxItem();
		statementBox = new TextBoxItem();

		a4Button = new RadioButton("group1", "A4");
		usLetterButton = new RadioButton("group1", "US Letter");

		fontNameArray = new String[] { "Arial", "Calibri", "Cambria",
				"Georgia", "Myriad", "Tahoma", "Times New Roman", "Trebuchet" };
		fontSizeArray = new String[] { "8pt", "9pt", "10pt", "11pt", "12pt",
				"13pt", "14pt", "15pt" };

		fontNameBox = new ListBox();
		for (int i = 0; i < fontNameArray.length; i++) {
			fontNameBox.addItem(fontNameArray[i]);
		}
		fontSizeBox = new ListBox();
		fontSizeBox.setWidth("150px");
		for (int i = 0; i < fontSizeArray.length; i++) {
			fontSizeBox.addItem(fontSizeArray[i]);
		}

		textBoxTable = new FlexTable();
		textBoxTable.setWidget(0, 0, nameLabel);
		textBoxTable.setWidget(0, 1, nameBox);
		textBoxTable.setWidget(1, 0, pageSizeLabel);
		textBoxTable.setWidget(1, 1, a4Button);
		textBoxTable.setWidget(2, 1, usLetterButton);
		textBoxTable.setWidget(3, 0, topMarginLabel);
		textBoxTable.setWidget(3, 1, topMarginBox);
		// textBoxTable.setWidget(3, 2, measureHtml);
		textBoxTable.setWidget(4, 0, bottomMarginLabel);
		textBoxTable.setWidget(4, 1, bottomMarginBox);
		textBoxTable.setWidget(5, 0, addressPadLabel);
		textBoxTable.setWidget(5, 1, addressPadBox);
		textBoxTable.setWidget(6, 0, fontLabel);
		textBoxTable.setWidget(6, 1, fontNameBox);
		textBoxTable.setWidget(7, 0, fontSizeLabel);
		textBoxTable.setWidget(7, 1, fontSizeBox);
		textBoxTable.setWidget(8, 0, draftLabel);
		textBoxTable.setWidget(8, 1, draftBox);
		textBoxTable.setWidget(9, 0, approvedLabel);
		textBoxTable.setWidget(9, 1, approvedBox);
		textBoxTable.setWidget(10, 0, overdueLabel);
		textBoxTable.setWidget(10, 1, overdueBox);
		textBoxTable.setWidget(11, 0, creditNoteLabel);
		textBoxTable.setWidget(11, 1, creditNoteBox);
		textBoxTable.setWidget(12, 0, statementLabel);
		textBoxTable.setWidget(12, 1, statementBox);

		return textBoxTable;
	}
}
