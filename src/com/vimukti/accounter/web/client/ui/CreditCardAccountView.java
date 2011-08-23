package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CreditCardAccountView extends AbstractBaseView {

	TextItem accTypeText;

	public CreditCardAccountView() {
		createControls();
	}

	private void createControls() {

		accTypeText = new TextItem(Accounter.messages().accountType(
				Global.get().Account()));
		// accTypeText.setWidth("*");
		accTypeText.setDisabled(true);
		accTypeText.setValue(Accounter.constants().creditCardOrLineOfCredit());

		TextItem numText = new TextItem(Accounter.messages().accountNo(
				Global.get().Account()));
		// numText.setWidth("*");
		TextItem accNameText = new TextItem(Accounter.messages().accountName(
				Global.get().Account()));
		// accNameText.setWidth("*");
		accNameText.setRequired(true);
		CheckboxItem activeCheck = new CheckboxItem(Accounter.constants()
				.active());
		SelectItem cashFlowSelect = new SelectItem(Accounter.messages()
				.cashFlowCategory(Global.get().Account()));

		// cashFlowSelect.setWidth("*");
		cashFlowSelect.setValue(Accounter.constants().operating());
		AmountField opBalText = new AmountField(Accounter.constants()
				.openingBalance(), this);
		// opBalText.setWidth("*");
		DateItem asofDate = UIUtils.date(Accounter.constants().asOf());
		// asofDate.setWidth("*");

		DynamicForm chartForm = UIUtils.form(Accounter.messages()
				.chartOfAccountsInformation(Global.get().Account()));

		chartForm.setFields(accTypeText, numText, accNameText, activeCheck,
				cashFlowSelect, opBalText, asofDate);

		CheckboxItem basisCheck = new CheckboxItem(Accounter.messages()
				.thisIsConsideredACashAccount(Global.get().account()));

		DynamicForm basisForm = UIUtils.form(Accounter.constants()
				.cashBasisAccounting());
		basisForm.setFields(basisCheck);

		TextAreaItem commentsArea = new TextAreaItem();
		// commentsArea.setWidth("*");
		commentsArea.setShowTitle(false);

		DynamicForm commentsForm = UIUtils.form(Accounter.constants()
				.comments());
		commentsForm.setFields(commentsArea);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("50%");
		leftVLay.add(chartForm);
		leftVLay.add(basisForm);
		leftVLay.add(commentsForm);

		TextItem bankName = new TextItem(Accounter.constants().bankName());
		// bankName.setWidth("*");
		AmountField limitText = new AmountField(Accounter.constants().amount(),
				this);
		// limitText.setWidth("*");
		IntegerField cardNumText = new IntegerField(this, Accounter.constants()
				.cardOrLoadNumber());
		// cardNumText.setWidth("*");

		DynamicForm creditForm = UIUtils.form(Accounter.messages()
				.creditCardAccountInformation(Global.get().account()));
		// creditForm.setWidth("*");
		// creditForm.setAutoHeight();
		creditForm.setFields(bankName, limitText, cardNumText);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(creditForm);

		Button saveCloseButt = new Button(Accounter.constants().saveAndClose());
		// saveCloseButt.setAutoFit(true);
		// saveCloseButt.setLayoutAlign(Alignment.LEFT);

		Button saveNewButt = new Button(Accounter.constants().saveAndNew());
		// saveNewButt.setAutoFit(true);
		// saveNewButt.setLayoutAlign(Alignment.RIGHT);

		HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setAlign(Alignment.RIGHT);
		// buttHLay.setBackgroundColor("#ff00aa");
		buttHLay.setWidth("100%");
		// buttHLay.setAutoHeight();
		// buttHLay.setMembersMargin(20);
		buttHLay.add(saveCloseButt);
		buttHLay.add(saveNewButt);
		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setMargin(20);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(topHLay);
		mainVLay.add(buttHLay);
		// setAutoSize(true);
		add(mainVLay);
		setSize("750px", "500px");

		// show();
	}

	@Override
	public void init() {

	}

	@Override
	public void initData() {

	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {

		this.accTypeText.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().titleToGoHere();
	}

}
