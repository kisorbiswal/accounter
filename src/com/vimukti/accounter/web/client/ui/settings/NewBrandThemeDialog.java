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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

@SuppressWarnings("unchecked")
public class NewBrandThemeDialog extends BaseDialog {

	private Label nameLabel, pageSizeLabel, topMarginLabel, bottomMarginLabel,
			addressPadLabel, fontLabel, fontSizeLabel, draftLabel,
			approvedLabel, overdueLabel, creditNoteLabel, statementLabel,
			logoLabel, taxesLabel, termsLabel;
	private RadioButton a4Button, usLetterButton, leftRadioButton,
			rightRadioButton, exclusiveButton, inclusiveButton, cmButton,
			inchButton;
	private VerticalPanel checkBoxPanel, radioButtonPanel,
			check_radio_textAreaPanel, button_textBoxPanel;
	private HorizontalPanel mainLayoutPanel, check_radioPanel, buttonPanel;
	private Button saveButton, cancelButton;
	private CheckBox taxNumItem, headingItem, unitPriceItem, paymentItem,
			columnItem, addressItem, logoItem;
	private TextBox nameBox, topMarginBox, bottomMarginBox, addressPadBox,
			draftBox, approvedBox, overdueBox, creditNoteBox, statementBox,
			paypalTextBox;
	private String[] fontNameArray, fontSizeArray;
	private ListBox fontNameBox, fontSizeBox;
	private HTML paypalEmailHtml, contactDetailHtml, measureHtml;
	private TextArea contactDetailsArea, termsPaymentArea;
	// private DynamicForm titlesForm, subMarginForm, name_sizeForm,
	// measureForm,
	// radioForm;
	private Label measureLabel;
	private FlexTable textBoxTable;

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
				okClicked();
			}
		});
		cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancelClicked();
			}
		});

		addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {
				ClientBrandingTheme brandingTheme = new ClientBrandingTheme();
				saveValues(brandingTheme);
				ViewManager.getInstance().createObject(brandingTheme,
						NewBrandThemeDialog.this);
				return true;
			}

			private int getPageSize() {
				if (a4Button.isEnabled()) {
					return 1;
				} else {
					return 2;
				}
			}

			private int getLogoType() {
				if (leftRadioButton.isEnabled()) {
					return 1;
				} else {
					return 2;
				}
			}

			private int getTaxType() {
				if (exclusiveButton.isEnabled()) {
					return 1;
				} else {
					return 2;
				}
			}

			private String getFont(int index) {
				switch (index) {
				case 0:
					return "Arial";
				case 1:
					return "Calibri";
				case 2:
					return "Cambria";
				case 3:
					return "Georgia";
				case 4:
					return "Myriad";
				case 5:
					return "Tahoma";
				case 6:
					return "Times New Roman";
				case 7:
					return "Trebuchet";

				default:
					return "Arial";
				}

			}

			private String getFontSize(int size) {

				switch (size) {
				case 0:
					return "8pt";
				case 1:
					return "9pt";
				case 2:
					return "10pt";
				case 3:
					return "11pt";
				case 4:
					return "12pt";
				case 5:
					return "13pt";
				case 6:
					return "14pt";
				case 7:
					return "15pt";

				default:
					return "8pt";
				}

			}

			private void saveValues(ClientBrandingTheme brandingTheme) {
				brandingTheme.setThemeName(String.valueOf(nameBox.getValue()));
				brandingTheme.setPageSizeType(getPageSize());
				brandingTheme.setTopMargin(Double.parseDouble(String
						.valueOf(topMarginBox.getValue())));
				brandingTheme.setBottomMargin(Double.parseDouble(String
						.valueOf(bottomMarginBox.getValue())));
				brandingTheme.setAddressPadding(Double.parseDouble(String
						.valueOf(addressPadBox.getValue())));
				brandingTheme.setFont(getFont(fontNameBox.getSelectedIndex()));
				brandingTheme.setFontSize(getFontSize(fontSizeBox
						.getSelectedIndex()));
				;

				brandingTheme.setCreditMemoTitle(String.valueOf(creditNoteBox
						.getValue()));
				brandingTheme.setOverDueInvoiceTitle(String.valueOf(overdueBox
						.getValue()));
				brandingTheme.setStatementTitle(String.valueOf(statementBox
						.getValue()));
				brandingTheme.setShowTaxNumber(taxNumItem.isEnabled());
				brandingTheme.setShowColumnHeadings(headingItem.isEnabled());
				brandingTheme.setShowPaymentAdviceCut_Away(paymentItem
						.isEnabled());
				brandingTheme.setShowUnitPrice_And_Quantity(unitPriceItem
						.isEnabled());
				brandingTheme.setShowTaxColumn(columnItem.isEnabled());
				brandingTheme.setShowRegisteredAddress(addressItem.isEnabled());
				brandingTheme.setShowLogo(logoItem.isEnabled());
				brandingTheme.setPayPalEmailID(String.valueOf(paypalTextBox
						.getValue()));
				brandingTheme.setTerms_And_Payment_Advice(String
						.valueOf(termsPaymentArea.getValue()));
				brandingTheme.setContactDetails(String
						.valueOf(contactDetailsArea.getValue()));
				brandingTheme.setLogoAlignmentType(getLogoType());
				brandingTheme.setShowTaxesAsType(getTaxType());
			}

			@Override
			public void onCancelClick() {
				hide();
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

		measureLabel = new Label("Measure in");
		logoLabel = new Label("Logo alignment");
		leftRadioButton = new RadioButton("group2", "Left");
		rightRadioButton = new RadioButton("group2", "Right");
		leftRadioButton.setChecked(true);
		taxesLabel = new Label("Show taxes as");
		exclusiveButton = new RadioButton("group3", "Exclusive");
		inclusiveButton = new RadioButton("group3", "Inclusive");
		inclusiveButton.setChecked(true);

		contactDetailHtml = new HTML(
				"<p>Enter your contact details as they should<br>appear at the top of all PDFs you print or<br>send</p>");
		contactDetailsArea = new TextArea();
		contactDetailsArea
				.setText("Vimukti Technologies Private Limited,2nd floor, LIG-B 11&26, Beside TMC,Dr. A.S. Rao Nagar,ECIL Post, Kapra,Hyderabad, Andhra Pradesh,India, PIN: 500062");

		radioButtonPanel.add(logoLabel);
		radioButtonPanel.add(leftRadioButton);
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
		taxNumItem.setChecked(true);
		headingItem = new CheckBox("Show column headings");
		headingItem.setChecked(true);
		unitPriceItem = new CheckBox("Show unit price & quantity");
		unitPriceItem.setChecked(true);
		paymentItem = new CheckBox("Show payment advice cut-away");
		paymentItem.setChecked(true);
		columnItem = new CheckBox("Show tax column");
		columnItem.setChecked(true);
		addressItem = new CheckBox("Show registered address");
		addressItem.setChecked(true);
		logoItem = new CheckBox("Show logo");
		logoItem.setChecked(true);
		paypalEmailHtml = new HTML(
				"<p>Enter your <b>PayPal</b> email to have<br>payment links automatically appear<br>on all your invoices</p>");
		paypalTextBox = new TextBox();
		paypalTextBox.setWidth("150px");

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
		measureHtml = new HTML("<p><b>Measure in</b></p>");
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

		nameBox = new TextBox();
		nameBox.setWidth("100px");
		topMarginBox = new TextBox();
		topMarginBox.setWidth("100px");
		topMarginBox.setText("1.35");
		bottomMarginBox = new TextBox();
		bottomMarginBox.setWidth("100px");
		bottomMarginBox.setText("1.0");
		addressPadBox = new TextBox();
		addressPadBox.setWidth("100px");
		addressPadBox.setText("1.0");
		draftBox = new TextBox();
		draftBox.setWidth("100px");
		draftBox.setText("DRAFT INVOICE");
		approvedBox = new TextBox();
		approvedBox.setWidth("100px");
		approvedBox.setText("INVOICE");
		overdueBox = new TextBox();
		overdueBox.setWidth("100px");
		overdueBox.setText("INVOICE");
		creditNoteBox = new TextBox();
		creditNoteBox.setWidth("100px");
		creditNoteBox.setText("CREDIT");
		statementBox = new TextBox();
		statementBox.setWidth("100px");
		statementBox.setText("STATEMENT");

		a4Button = new RadioButton("group1", "A4");
		usLetterButton = new RadioButton("group1", "US Letter");
		a4Button.setChecked(true);

		cmButton = new RadioButton("group4", "cm");
		inchButton = new RadioButton("group4", "inch");
		cmButton.setChecked(true);

		fontNameArray = new String[] { "Arial", "Calibri", "Cambria",
				"Georgia", "Myriad", "Tahoma", "Times New Roman", "Trebuchet" };
		fontSizeArray = new String[] { "8pt", "9pt", "10pt", "11pt", "12pt",
				"13pt", "14pt", "15pt" };

		fontNameBox = new ListBox();
		fontNameBox.setWidth("100px");
		for (int i = 0; i < fontNameArray.length; i++) {
			fontNameBox.addItem(fontNameArray[i]);
		}
		fontNameBox.setSelectedIndex(0);
		fontSizeBox = new ListBox();
		fontSizeBox.setWidth("100px");
		for (int i = 0; i < fontSizeArray.length; i++) {
			fontSizeBox.addItem(fontSizeArray[i]);
		}
		fontSizeBox.setSelectedIndex(0);
		HorizontalPanel measurePanel = new HorizontalPanel();
		measurePanel.add(topMarginBox);
		measurePanel.add(measureLabel);

		HorizontalPanel unitsPanel = new HorizontalPanel();
		unitsPanel.add(bottomMarginBox);
		unitsPanel.add(cmButton);
		unitsPanel.add(inchButton);

		textBoxTable = new FlexTable();
		textBoxTable.setWidget(0, 0, nameLabel);
		textBoxTable.setWidget(0, 1, nameBox);
		textBoxTable.setWidget(1, 0, pageSizeLabel);
		textBoxTable.setWidget(1, 1, a4Button);
		textBoxTable.setWidget(2, 1, usLetterButton);
		textBoxTable.setWidget(3, 0, topMarginLabel);
		textBoxTable.setWidget(3, 1, measurePanel);
		textBoxTable.setWidget(4, 0, bottomMarginLabel);
		textBoxTable.setWidget(4, 1, unitsPanel);
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
