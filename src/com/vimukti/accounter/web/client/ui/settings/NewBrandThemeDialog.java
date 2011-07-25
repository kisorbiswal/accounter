package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.FileUploadDilaog;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Uday Kumar
 * 
 */
@SuppressWarnings({ "deprecation", "unchecked" })
public class NewBrandThemeDialog extends BaseDialog {

	private Label pageSizeLabel, topMarginLabel, bottomMarginLabel,
			addressPadLabel, fontLabel, fontSizeLabel, overdueLabel,
			creditNoteLabel, statementLabel, logoLabel, termsLabel;

	private RadioButton a4Button, usLetterButton, leftRadioButton,
			rightRadioButton, cmButton, inchButton;
	private VerticalPanel checkBoxPanel, radioButtonPanel,
			check_radio_textAreaPanel, button_textBoxPanel;
	private HorizontalPanel mainLayoutPanel, check_radioPanel;
	private CheckBox taxNumItem, headingItem, unitPriceItem,// paymentItem,
			columnItem, addressItem, logoItem;
	private TextBox topMarginBox, bottomMarginBox, addressPadBox, overdueBox,
			creditNoteBox, statementBox, paypalTextBox, logoNameBox;

	private TextItem nameItem;
	private String[] fontNameArray, fontSizeArray;
	private SelectCombo fontNameBox, fontSizeBox;
	private HTML paypalEmailHtml, contactDetailHtml;
	private TextArea contactDetailsArea, termsPaymentArea;
	private Label measureLabel;
	private FlexTable textBoxTable;
	private List<String> listOfFontNames, listOfFontSizes;
	private ClientBrandingTheme takenTheme;
	private SettingsMessages messages;
	private Label addLogoLabel;
	private ValueCallBack<ClientBrandingTheme> callback;
	private String[] fileTypes;
	private String filename;

	private DynamicForm nameForm;

	public NewBrandThemeDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}

	@Override
	protected void initConstants() {
		super.initConstants();
		messages = GWT.create(SettingsMessages.class);
	}

	public NewBrandThemeDialog(String title, String desc,
			ClientBrandingTheme brandingTheme) {
		super(title, desc);
		createControls();
		setBrandingTheme(brandingTheme);
	}

	private void setBrandingTheme(ClientBrandingTheme brandingTheme) {
		takenTheme = brandingTheme;
		nameItem.setValue(brandingTheme.getThemeName());
		topMarginBox.setValue(String.valueOf(brandingTheme.getTopMargin()));
		bottomMarginBox
				.setValue(String.valueOf(brandingTheme.getBottomMargin()));
		addressPadBox
				.setValue(String.valueOf(brandingTheme.getAddressPadding()));
		setPazeSize(brandingTheme.getPageSizeType());
		fontNameBox.setComboItem(brandingTheme.getFont());
		fontSizeBox.setComboItem(brandingTheme.getFontSize());
		setLogoType(brandingTheme.getLogoAlignmentType());
		setMeasurementType(brandingTheme.getMarginsMeasurementType());
		creditNoteBox.setValue(brandingTheme.getCreditMemoTitle());
		overdueBox.setValue(brandingTheme.getOverDueInvoiceTitle());
		statementBox.setValue(brandingTheme.getStatementTitle());
		taxNumItem.setValue(brandingTheme.isShowTaxNumber());
		headingItem.setValue(brandingTheme.isShowColumnHeadings());
		unitPriceItem.setValue(brandingTheme.isShowUnitPrice_And_Quantity());
		columnItem.setValue(brandingTheme.isShowTaxColumn());
		addressItem.setValue(brandingTheme.isShowRegisteredAddress());
		logoItem.setValue(brandingTheme.isShowLogo());
		paypalTextBox.setValue(brandingTheme.getPayPalEmailID());
		termsPaymentArea.setValue(brandingTheme.getTerms_And_Payment_Advice());
		contactDetailsArea.setValue(brandingTheme.getContactDetails());
		logoNameBox.setValue(brandingTheme.getFileName());

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
		check_radioPanel.setSpacing(10);
		termsLabel = new Label(messages.termsLabel());
		termsPaymentArea = new TextArea();
		termsPaymentArea.setStyleName("terms-payment-area");
		check_radio_textAreaPanel.add(termsLabel);
		check_radio_textAreaPanel.add(termsPaymentArea);

		okbtn.setText(messages.saveButton());

		addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {
				try {
					if (NewBrandThemeDialog.this.validate()) {
						ClientBrandingTheme brandingTheme = saveValues();
						if (takenTheme == null) {
							if (!Utility.isObjectExist(Accounter.getCompany()
									.getBrandingTheme(), brandingTheme
									.getThemeName())) {
								ViewManager.getInstance()
										.createObject(brandingTheme,
												NewBrandThemeDialog.this);
								HistoryTokenUtils.setPresentToken(
										MainFinanceWindow.getViewManager()
												.getCurrentView().getAction(),
										MainFinanceWindow.getViewManager()
												.getCurrentView().getData());
							} else {
								MainFinanceWindow.getViewManager()
										.showErrorInCurrectDialog(
												"Theme Name already exists");
							}
						} else
							ViewManager.getInstance().alterObject(
									brandingTheme, NewBrandThemeDialog.this);
					}

				} catch (Exception e) {
					System.err.println(e.toString());
				}
				return false;

			}

			@Override
			public void onCancelClick() {
				removeFromParent();
				Action.cancle();
			}

		});

		check_radio_textAreaPanel.setSpacing(10);

		mainLayoutPanel.add(addTextBoxTableControl());
		mainLayoutPanel.add(check_radio_textAreaPanel);

		button_textBoxPanel.add(mainLayoutPanel);

		setBodyLayout(button_textBoxPanel);

	}

	private int getPageSize() {
		if (a4Button.isChecked()) {
			return 1;
		} else {
			return 2;
		}
	}

	private void setMeasurementType(int i) {
		if (i == 1) {
			cmButton.setChecked(true);
		} else {
			inchButton.setChecked(true);
		}
	}

	private void setPazeSize(int i) {
		if (i == 1) {
			a4Button.setChecked(true);
		} else {
			usLetterButton.setChecked(true);
		}
	}

	private ClientBrandingTheme saveValues() {

		ClientBrandingTheme brandingTheme = takenTheme != null ? takenTheme
				: new ClientBrandingTheme();
		brandingTheme.setThemeName(String.valueOf(nameItem.getValue()));
		brandingTheme.setPageSizeType(getPageSize());
		brandingTheme.setTopMargin(Double.parseDouble(String
				.valueOf(topMarginBox.getValue())));
		brandingTheme.setBottomMargin(Double.parseDouble(String
				.valueOf(bottomMarginBox.getValue())));
		brandingTheme.setAddressPadding(Double.parseDouble(String
				.valueOf(addressPadBox.getValue())));
		brandingTheme.setFont(fontNameBox.getSelectedValue());
		brandingTheme.setFontSize(fontSizeBox.getSelectedValue());

		brandingTheme.setCreditMemoTitle(String.valueOf(creditNoteBox
				.getValue()));
		brandingTheme.setOverDueInvoiceTitle(String.valueOf(overdueBox
				.getValue()));
		brandingTheme
				.setStatementTitle(String.valueOf(statementBox.getValue()));
		brandingTheme.setShowTaxNumber(taxNumItem.isChecked());
		brandingTheme.setShowColumnHeadings(headingItem.isChecked());
		brandingTheme.setShowUnitPrice_And_Quantity(unitPriceItem.isChecked());
		brandingTheme.setShowTaxColumn(columnItem.isChecked());
		brandingTheme.setShowRegisteredAddress(addressItem.isChecked());
		brandingTheme.setShowLogo(logoItem.isChecked());
		brandingTheme
				.setPayPalEmailID(String.valueOf(paypalTextBox.getValue()));
		brandingTheme.setTerms_And_Payment_Advice(String
				.valueOf(termsPaymentArea.getValue()));
		brandingTheme.setContactDetails(String.valueOf(contactDetailsArea
				.getValue()));
		brandingTheme.setLogoAlignmentType(getLogoType());

		if (logoNameBox.getValue().toString().isEmpty()) {
			brandingTheme.setFileName(null);
			brandingTheme.setLogoAdded(false);
		} else {
			brandingTheme.setFileName(String.valueOf(logoNameBox.getText()
					.toString()));
			brandingTheme.setLogoAdded(true);
		}

		return brandingTheme;
	}

	private int getLogoType() {
		if (leftRadioButton.isChecked()) {
			return 1;
		} else {
			return 2;
		}
	}

	private void setLogoType(int i) {
		if (i == 1) {
			leftRadioButton.setChecked(true);
		} else {
			rightRadioButton.setChecked(true);
		}
	}

	// private int getTaxType() {
	// if (exclusiveButton.isChecked()) {
	// return 1;
	// } else {
	// return 2;
	// }
	// }
	//
	// private void setTaxType(int i) {
	// if (i == 1) {
	// exclusiveButton.setChecked(true);
	// } else {
	// inclusiveButton.setChecked(true);
	// }
	// }

	// private String getFont(int index) {
	// switch (index) {
	// case 0:
	// return messages.arial();
	// case 1:
	// return messages.calibri();
	// case 2:
	// return messages.cambria();
	// case 3:
	// return messages.georgia();
	// case 4:
	// return messages.myriad();
	// case 5:
	// return messages.tahoma();
	// case 6:
	// return messages.timesNewRoman();
	// case 7:
	// return messages.trebuchet();
	//
	// default:
	// return messages.arial();
	// }
	//
	// }
	//
	// private int setFont(String font) {
	// int i = 0;
	// if (font.equals(messages.arial())) {
	// i = 0;
	// } else if (font.equals(messages
	// .calibri())) {
	// i = 1;
	// } else if (font.equals(messages
	// .cambria())) {
	// i = 2;
	// } else if (font.equals(messages
	// .georgia())) {
	// i = 3;
	// } else if (font.equals(messages
	// .tahoma())) {
	// i = 4;
	// } else if (font.equals(messages
	// .timesNewRoman())) {
	// i = 5;
	// } else if (font.equals(messages
	// .trebuchet())) {
	// i = 6;
	// } else if (font.equals(messages
	// .trebuchet())) {
	// i = 7;
	// }
	// return i;
	// }
	//
	// private String getFontSize(int size) {
	//
	// switch (size) {
	// case 0:
	// return messages.point8();
	// case 1:
	// return messages.point9();
	// case 2:
	// return messages.point10();
	// case 3:
	// return messages.point11();
	// case 4:
	// return messages.point12();
	// case 5:
	// return messages.point13();
	// case 6:
	// return messages.point14();
	// case 7:
	// return messages.point15();
	//
	// default:
	// return messages.point8();
	// }
	//
	// }
	//
	// private int setFontSize(String size) {
	// int i = 0;
	// if (size.equals(messages.point8())) {
	// i = 0;
	// } else if (size.equals(messages
	// .point8())) {
	// i = 1;
	// } else if (size.equals(messages
	// .point9())) {
	// i = 2;
	// } else if (size.equals(messages
	// .point10())) {
	// i = 3;
	// } else if (size.equals(messages
	// .point11())) {
	// i = 4;
	// } else if (size.equals(messages
	// .point12())) {
	// i = 5;
	// } else if (size.equals(messages
	// .point13())) {
	// i = 6;
	// } else if (size.equals(messages
	// .point14())) {
	// i = 7;
	// } else if (size.equals(messages
	// .point15())) {
	// i = 8;
	// }
	// return i;
	// }

	private VerticalPanel addRadioBoxTableControls() {
		radioButtonPanel = new VerticalPanel();

		measureLabel = new Label(messages.measure());
		logoLabel = new Label(messages.logoAlignment());
		leftRadioButton = new RadioButton(messages.logoType(), messages.left());
		rightRadioButton = new RadioButton(messages.logoType(),
				messages.right());
		leftRadioButton.setChecked(true);
		// taxesLabel = new Label(messages.showTaxesAs());
		// exclusiveButton = new RadioButton(messages.taxType(), messages
		// .exclusive());
		// inclusiveButton = new RadioButton(messages.taxType(), messages
		// .inclusive());
		// inclusiveButton.setChecked(true);

		contactDetailHtml = new HTML(messages.contactDetailsHtml());
		contactDetailsArea = new TextArea();
		contactDetailsArea.setStyleName("contact-deatils-area");
		contactDetailsArea.setText(getCompany().getDisplayName());

		radioButtonPanel.add(logoLabel);
		radioButtonPanel.add(leftRadioButton);
		radioButtonPanel.add(rightRadioButton);
		// radioButtonPanel.add(taxesLabel);
		// radioButtonPanel.add(exclusiveButton);
		// radioButtonPanel.add(inclusiveButton);
		radioButtonPanel.add(contactDetailHtml);
		radioButtonPanel.add(contactDetailsArea);
		radioButtonPanel.setSpacing(5);

		return radioButtonPanel;
	}

	private VerticalPanel addCheckBoxTableControls() {

		taxNumItem = new CheckBox(messages.showTaxNumber());
		taxNumItem.setChecked(true);
		headingItem = new CheckBox(messages.showColumnHeadings());
		headingItem.setChecked(true);
		unitPriceItem = new CheckBox(messages.showUnitPrice());
		unitPriceItem.setChecked(true);
		// paymentItem = new CheckBox(messages
		// .showPaymentAdvice());
		// paymentItem.setChecked(true);
		columnItem = new CheckBox(messages.showTaxColumn());
		columnItem.setChecked(true);
		addressItem = new CheckBox(messages.showRegisteredAddress());
		addressItem.setChecked(true);
		logoItem = new CheckBox(messages.showLogo());
		logoItem.setChecked(true);
		paypalEmailHtml = new HTML(messages.paypalEmailHtml());
		paypalTextBox = new TextBox();

		checkBoxPanel = new VerticalPanel();
		checkBoxPanel.add(taxNumItem);
		checkBoxPanel.add(headingItem);
		checkBoxPanel.add(unitPriceItem);
		// checkBoxPanel.add(paymentItem);
		checkBoxPanel.add(columnItem);
		checkBoxPanel.add(addressItem);
		checkBoxPanel.add(logoItem);
		checkBoxPanel.add(paypalEmailHtml);
		checkBoxPanel.add(paypalTextBox);

		checkBoxPanel.setStyleName("rightBorder");
		checkBoxPanel.setSpacing(5);
		return checkBoxPanel;

	}

	private HorizontalPanel addTextBoxTableControl() {

		pageSizeLabel = new Label(messages.pageSize());
		topMarginLabel = new Label(messages.topMargin());
		bottomMarginLabel = new Label(messages.bottomMargin());
		addressPadLabel = new Label(messages.addressPadding());
		fontLabel = new Label(messages.font());
		fontSizeLabel = new Label(messages.fontSize());
		// draftLabel = new Label(messages
		// .draftInvoiceTitle());
		// approvedLabel = new Label(messages
		// .approvedInvoiceTitle());
		overdueLabel = new Label(messages.overdueInvoiceTitle());
		creditNoteLabel = new Label(messages.creditNoteTitle());
		statementLabel = new Label(messages.statementTitle());

		nameItem = new TextItem("Name");
		nameItem.addStyleName("name-item");
		topMarginBox = new TextBox();
		topMarginBox.setText(messages.topMarginValue());
		bottomMarginBox = new TextBox();
		bottomMarginBox.setText(messages.bottomMarginValue());
		addressPadBox = new TextBox();
		addressPadBox.setText(messages.addressPaddingValue());
		// draftBox = new TextBox();
		// draftBox.setText(messages
		// .draftBoxValue());
		// approvedBox = new TextBox();
		// approvedBox.setText(messages
		// .approvedValue());
		overdueBox = new TextBox();
		overdueBox.setText(messages.overdueValue());
		creditNoteBox = new TextBox();
		creditNoteBox.setText(messages.creditNoteValue());
		statementBox = new TextBox();
		statementBox.setText(messages.statement());

		a4Button = new RadioButton(messages.pageType(), messages.a4());
		usLetterButton = new RadioButton(messages.pageType(),
				messages.usLetter());
		a4Button.setChecked(true);

		cmButton = new RadioButton(messages.measureType(), messages.cm());
		inchButton = new RadioButton(messages.measureType(), messages.inch());
		cmButton.setChecked(true);

		fontNameArray = new String[] { messages.arial(), messages.calibri(),
				messages.cambria(), messages.georgia(), messages.myriad(),
				messages.tahoma(), messages.timesNewRoman(),
				messages.trebuchet() };
		fontSizeArray = new String[] { messages.point8(), messages.point9(),
				messages.point10(), messages.point11(),
		// messages.point13(),
		// messages.point14(),
		// messages.point15()
		};

		fontNameBox = new SelectCombo(null);
		// fontNameBox.setWidth(100);
		listOfFontNames = new ArrayList<String>();
		for (int i = 0; i < fontNameArray.length; i++) {
			listOfFontNames.add(fontNameArray[i]);
		}
		fontNameBox.initCombo(listOfFontNames);
		fontNameBox.setComboItem(fontNameArray[0]);
		fontNameBox
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null)
							fontNameBox.setComboItem(selectItem);

					}
				});

		fontSizeBox = new SelectCombo(null);
		fontSizeBox.setWidth(100);
		listOfFontSizes = new ArrayList<String>();
		for (int i = 0; i < fontSizeArray.length; i++) {
			listOfFontSizes.add(fontSizeArray[i]);
		}
		fontSizeBox.initCombo(listOfFontSizes);
		fontSizeBox.setComboItem(fontSizeArray[0]);
		fontSizeBox
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null)
							fontSizeBox.setComboItem(selectItem);
					}
				});

		DynamicForm fontNameForm = new DynamicForm();
		fontNameForm.setCellSpacing(0);
		fontNameForm.setNumCols(1);
		fontNameForm.setFields(fontNameBox);
		DynamicForm fontSizeForm = new DynamicForm();
		fontSizeForm.setCellSpacing(0);
		fontSizeForm.setNumCols(1);
		fontSizeForm.setFields(fontSizeBox);

		HorizontalPanel unitsPanel = new HorizontalPanel();
		unitsPanel.add(cmButton);
		unitsPanel.add(inchButton);

		VerticalPanel measurePanel = new VerticalPanel();
		measurePanel.add(measureLabel);
		// measurePanel.setCellHorizontalAlignment(measureLabel,
		// HasAlignment.ALIGN_CENTER);
		measurePanel.add(unitsPanel);
		measurePanel.setStyleName("measurePanel");

		nameForm = new DynamicForm();
		// nameForm.setCellSpacing(0);
		nameForm.setNumCols(2);
		nameItem.setRequired(true);
		nameForm.setFields(nameItem);
		nameForm.setWidth("110px");

		addLogoLabel = new Label("Add Logo");
		logoNameBox = new TextBox();
		logoNameBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
					@Override
					public void execute(ClientBrandingTheme value) {
						logoNameBox.setText(value.getFileName());
					}
				};
				FileUploadDilaog dilaog = new FileUploadDilaog("Upload Logo",
						"uday", callback, fileTypes, takenTheme);

			}
		});

		textBoxTable = new FlexTable();
		textBoxTable.setWidget(0, 0, pageSizeLabel);
		textBoxTable.setWidget(0, 1, a4Button);
		textBoxTable.setWidget(1, 1, usLetterButton);
		textBoxTable.setWidget(2, 0, topMarginLabel);
		textBoxTable.setWidget(2, 1, topMarginBox);
		textBoxTable.setWidget(3, 0, bottomMarginLabel);
		textBoxTable.setWidget(3, 1, bottomMarginBox);
		textBoxTable.setWidget(4, 0, addressPadLabel);
		textBoxTable.setWidget(4, 1, addressPadBox);
		textBoxTable.setWidget(5, 0, fontLabel);
		textBoxTable.setWidget(5, 1, fontNameForm);
		textBoxTable.setWidget(6, 0, fontSizeLabel);
		textBoxTable.setWidget(6, 1, fontSizeForm);
		// textBoxTable.setWidget(8, 0, draftLabel);
		// textBoxTable.setWidget(8, 1, draftBox);
		// textBoxTable.setWidget(9, 0, approvedLabel);
		// textBoxTable.setWidget(9, 1, approvedBox);
		textBoxTable.setWidget(7, 0, overdueLabel);
		textBoxTable.setWidget(7, 1, overdueBox);
		textBoxTable.setWidget(8, 0, creditNoteLabel);
		textBoxTable.setWidget(8, 1, creditNoteBox);
		textBoxTable.setWidget(9, 0, statementLabel);
		textBoxTable.setWidget(9, 1, statementBox);
		textBoxTable.setWidget(10, 0, addLogoLabel);
		textBoxTable.setWidget(10, 1, logoNameBox);

		HorizontalPanel textBoxHorizontalPanel = new HorizontalPanel();

		VerticalPanel textBoxPanel = new VerticalPanel();
		textBoxPanel.add(nameForm);
		textBoxPanel.add(textBoxTable);
		textBoxHorizontalPanel.add(textBoxPanel);
		textBoxHorizontalPanel.add(measurePanel);
		textBoxHorizontalPanel.setSpacing(10);
		textBoxHorizontalPanel.setStyleName("rightBorder");

		return textBoxHorizontalPanel;
	}

	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		return nameForm.validate(true);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		NewBrandThemeDialog.this.removeFromParent();
		super.saveSuccess(object);
		SettingsActionFactory.getInvoiceBrandingAction().run(null, true);
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getSettingsMessages().editBrandThemeLabel();
	}
}
