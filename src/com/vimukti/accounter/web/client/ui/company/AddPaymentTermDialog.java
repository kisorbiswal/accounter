package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PaymentTermListDialog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddPaymentTermDialog extends BaseDialog<ClientPaymentTerms> {

	public TextItem payTermText;
	public TextItem descText;
	// public SelectCombo dueSelect;
	// public PercentageField discText;
	// public IntegerField discDayText;
	private DynamicForm discForm;
	public DynamicForm nameDescForm;
	public IntegerField dayText;
	// public String[] dueValues = { Accounter.constants().currentMonth(),
	// Accounter.constants().currentQuarter(),
	// Accounter.constants().currentHalfYear(),
	// Accounter.constants().currentYear() };
	// private List<String> listOfDueValues;
	// private Label dayLabel;

	private IntegerRangeValidator integerRangeValidator;
	private DynamicForm dueForm;

	private PaymentTermListDialog parent;

	public AddPaymentTermDialog(PaymentTermListDialog parent, String title,
			String desc) {
		super(title, desc);
		initiliase();
		this.parent = parent;
		center();
	}

	private void initiliase() {

		integerRangeValidator = new IntegerRangeValidator();

		payTermText = new TextItem(Accounter.constants().paymentTerm());
		payTermText.setHelpInformation(true);
		payTermText.setRequired(true);

		descText = new TextItem(Accounter.constants().description());
		descText.setHelpInformation(true);

		dayText = new IntegerField(this, Accounter.constants().dueDays());
		dayText.setHelpInformation(true);
		// dayText.setWidth(20);
		dayText.setValidators(integerRangeValidator);

		// dueSelect = new SelectCombo(Accounter.constants().due());
		// dueSelect.setHelpInformation(true);
		// dueSelect.setWidth(90);
		// listOfDueValues = new ArrayList<String>();
		// for (int i = 0; i < dueValues.length; i++) {
		// listOfDueValues.add(dueValues[i]);
		// }
		// dueSelect.initCombo(listOfDueValues);
		// dueSelect
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		// if (selectItem != null)
		// dueSelect.setComboItem(selectItem);
		//
		// }
		// });

		// dayText = new IntegerField(this, Accounter.constants().and());
		// dayText = new IntegerField(this, Accounter.constants().dueDays());
		// dayText.setHelpInformation(true);
		// // dayText.setWidth(20);
		// dayText.setValidators(integerRangeValidator);

		// dayLabel = new Label();
		// dayLabel.setText(Accounter.constants().days());

		nameDescForm = new DynamicForm();
		nameDescForm.setFields(payTermText, descText, dayText);
		nameDescForm.setSize("100%", "100%");

		// discText = new PercentageField(this,
		// Accounter.constants().discount());
		// discText.setHelpInformation(true);
		// discText.setColSpan(1);
		// discText.setWidth(90);
		// discText.setHint(Accounter.constants().ifpaidwithin());
		//
		// discDayText = new IntegerField(this, Accounter.constants()
		// .ifpaidwithin());
		// discDayText.setHelpInformation(true);
		// discDayText.setColSpan(1);
		// discDayText.setHint(Accounter.constants().days());
		// discDayText.setWidth(20);

		dueForm = new DynamicForm();
		// dueForm.setWidth("100%");
		// dueForm.setNumCols(4);
		// dueForm.setFields(dueSelect, dayText);

		// dueForm.setFields(dayText);

		dueForm.setWidth("100%");
		dueForm.setStyleName("due_form_table");

		// dayLabel = new Label();
		// dayLabel.setText(Accounter.constants().days());

		HorizontalPanel duePanel = new HorizontalPanel();
		// duePanel.setSize("100%", "100%");
		duePanel.add(dueForm);
		// duePanel.add(dayLabel);
		duePanel.setWidth("100%");
		// duePanel.setCellVerticalAlignment(dayLabel,
		// HasVerticalAlignment.ALIGN_MIDDLE);

		discForm = new DynamicForm();
		discForm.setWidth("100%");
		discForm.setNumCols(4);
		discForm.setIsGroup(true);
		// discForm.setGroupTitle(Accounter.constants().cashDiscount());
		// discForm.setFields(discText, discDayText);
		discForm.setSize("100%", "100%");

		// Label label2 = new Label();
		// label2.setText(Accounter.constants().days());

		HorizontalPanel discountPanel = new HorizontalPanel();
		// discountPanel.setSize("100%", "100%");
		discountPanel.add(discForm);
		// discountPanel.add(label2);
		// discountPanel.setCellVerticalAlignment(label2,
		// HasVerticalAlignment.ALIGN_MIDDLE);

		okbtn.setWidth("60px");
		cancelBtn.setWidth("60px");

		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setTop(25);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(nameDescForm);
		// mainVLay.add(duePanel);
		// mainVLay.add(discountPanel);

		setBodyLayout(mainVLay);
		setWidth("450px");

	}

	public void setObject(ClientPaymentTerms paymentTerm) {

		paymentTerm
				.setName(this.payTermText.getValue() != null ? this.payTermText
						.getValue().toString() : "");
		paymentTerm
				.setDescription(this.descText.getValue() != null ? this.descText
						.getValue().toString() : "");
		// paymentTerm.setIfPaidWithIn(UIUtils
		// .toInt(this.discText.getValue() != null ? this.discText
		// .getValue().toString() : "0"));
		// paymentTerm.setDiscountPercent(UIUtils.toDbl(this.discDayText
		// .getValue() != null ? this.discDayText.getValue().toString()
		// : "0"));
		paymentTerm.setDueDays(UIUtils.toInt(this.dayText.getValue()));
	}

	public ClientPaymentTerms getObject() {
		final ClientPaymentTerms paymentTerm = new ClientPaymentTerms();

		paymentTerm
				.setName(this.payTermText.getValue() != null ? this.payTermText
						.getValue().toString() : "");
		paymentTerm
				.setDescription(this.descText.getValue() != null ? this.descText
						.getValue().toString() : "");
		// paymentTerm.setIfPaidWithIn(UIUtils
		// .toInt(this.discText.getValue() != null ? this.discText
		// .getValue().toString() : "0"));
		// paymentTerm.setDiscountPercent(UIUtils.toDbl(this.discDayText
		// .getValue() != null ? this.discDayText.getValue().toString()
		// : "0"));
		paymentTerm
				.setDueDays(UIUtils.toInt(this.dayText.getValue() != null ? this.dayText
						.getValue().toString() : "0"));

		return paymentTerm;
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (payTermText.getValue().trim() == null
				|| payTermText.getValue().trim().length() == 0) {
			result.addError(this, Accounter.constants().pleaseEnterPayTerm());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		return parent.onOK();
	}

	@Override
	public void setFocus() {
		payTermText.setFocus();

	}

}
