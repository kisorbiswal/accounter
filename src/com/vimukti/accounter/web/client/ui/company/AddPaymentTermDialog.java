package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class AddPaymentTermDialog extends BaseDialog {

	public TextItem payTermText;
	public TextItem descText;
	public SelectCombo dueSelect;
	public PercentageField discText;
	public IntegerField discDayText;
	private DynamicForm discForm;
	public DynamicForm nameDescForm;
	public IntegerField dayText;
	public String[] dueValues = {
			Accounter.getCompanyMessages().currentMonth(),
			Accounter.getCompanyMessages().currentQuarter(),
			Accounter.getCompanyMessages().currentHalfYear(),
			Accounter.getCompanyMessages().currentYear() };
	private List<String> listOfDueValues;
	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
	private Label dayLabel;
	@SuppressWarnings("unused")
	private DynamicForm paymentForm;
	private IntegerRangeValidator integerRangeValidator;
	private DynamicForm dueForm;

	public AddPaymentTermDialog(String title, String desc) {
		super(title, desc);
		initiliase();
		center();
	}

	private void initiliase() {

		integerRangeValidator = new IntegerRangeValidator();

		payTermText = new TextItem(companyConstants.paymentTerm());
		payTermText.setHelpInformation(true);
		payTermText.setRequired(true);

		descText = new TextItem(companyConstants.description());
		descText.setHelpInformation(true);
		dueSelect = new SelectCombo(companyConstants.due());
		dueSelect.setHelpInformation(true);
		// dueSelect.setWidth(90);
		listOfDueValues = new ArrayList<String>();
		for (int i = 0; i < dueValues.length; i++) {
			listOfDueValues.add(dueValues[i]);
		}
		dueSelect.initCombo(listOfDueValues);
		dueSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null)
							dueSelect.setComboItem(selectItem);

					}
				});

		dayText = new IntegerField(Accounter.getCompanyMessages()
				.and());
		dayText.setHelpInformation(true);
		// dayText.setWidth(20);
		dayText.setValidators(integerRangeValidator);

		dayLabel = new Label();
		dayLabel.setText(Accounter.getCompanyMessages().days());

		nameDescForm = new DynamicForm();
		nameDescForm.setFields(payTermText, descText);
		nameDescForm.setSize("100%", "100%");

		discText = new PercentageField(companyConstants.discount());
		discText.setHelpInformation(true);
		discText.setColSpan(1);
		discText.setWidth(90);
		discText.setHint(" if paid within ");

		discDayText = new IntegerField("If paid within");
		discDayText.setHelpInformation(true);
		discDayText.setColSpan(1);
		discDayText.setHint(Accounter.getCompanyMessages().days());
		// discDayText.setWidth(20);

		dueForm = new DynamicForm();
		// dueForm.setWidth("100%");
		dueForm.setNumCols(4);
		dueForm.setFields(dueSelect, dayText);
		dueForm.setWidth("100%");
		dueForm.setStyleName("due_form_table");

		dayLabel = new Label();
		dayLabel.setText(Accounter.getCompanyMessages().days());

		HorizontalPanel duePanel = new HorizontalPanel();
		// duePanel.setSize("100%", "100%");
		duePanel.add(dueForm);
		duePanel.add(dayLabel);
		duePanel.setWidth("100%");
		duePanel.setCellVerticalAlignment(dayLabel,
				HasVerticalAlignment.ALIGN_MIDDLE);

		discForm = new DynamicForm();
		discForm.setWidth("100%");
		discForm.setNumCols(4);
		discForm.setIsGroup(true);
		discForm.setGroupTitle(companyConstants.cashDiscount());
		discForm.setFields(discText, discDayText);
		discForm.setSize("100%", "100%");

		Label label2 = new Label();
		label2.setText(Accounter.getCompanyMessages().days());

		HorizontalPanel discountPanel = new HorizontalPanel();
		// discountPanel.setSize("100%", "100%");
		discountPanel.add(discForm);
		discountPanel.add(label2);
		discountPanel.setCellVerticalAlignment(label2,
				HasVerticalAlignment.ALIGN_MIDDLE);

		okbtn.setWidth("60px");
		cancelBtn.setWidth("60px");

		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setTop(25);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(nameDescForm);
		mainVLay.add(duePanel);
		mainVLay.add(discountPanel);

		setBodyLayout(mainVLay);
		setWidth("450px");
	}

	public void setObject(ClientPaymentTerms paymentTerm) {

		paymentTerm
				.setName(this.payTermText.getValue() != null ? this.payTermText
						.getValue().toString() : "");
		paymentTerm
				.setDescription(this.descText.getValue() != null ? this.descText
						.getValue().toString()
						: "");
		paymentTerm.setIfPaidWithIn(UIUtils
				.toInt(this.discText.getValue() != null ? this.discText
						.getValue().toString() : "0"));
		paymentTerm.setDiscountPercent(UIUtils.toDbl(this.discDayText
				.getValue() != null ? this.discDayText.getValue().toString()
				: "0"));
		paymentTerm.setDueDays(UIUtils.toInt(this.dayText.getValue()));
	}

	public ClientPaymentTerms getObject() {
		final ClientPaymentTerms paymentTerm = new ClientPaymentTerms();

		paymentTerm
				.setName(this.payTermText.getValue() != null ? this.payTermText
						.getValue().toString() : "");
		paymentTerm
				.setDescription(this.descText.getValue() != null ? this.descText
						.getValue().toString()
						: "");
		paymentTerm.setIfPaidWithIn(UIUtils
				.toInt(this.discText.getValue() != null ? this.discText
						.getValue().toString() : "0"));
		paymentTerm.setDiscountPercent(UIUtils.toDbl(this.discDayText
				.getValue() != null ? this.discDayText.getValue().toString()
				: "0"));
		paymentTerm.setDueDays(UIUtils
				.toInt(this.dayText.getValue() != null ? this.dayText
						.getValue().toString() : "0"));

		return paymentTerm;
	}

	@Override
	public void addInputDialogHandler(InputDialogHandler handler) {
		// TODO Auto-generated method stub
		super.addInputDialogHandler(handler);

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getCompanyMessages().paymentTerm();
	}

}
