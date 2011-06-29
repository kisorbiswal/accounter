package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class CreditCardAccountView extends AbstractBaseView {

	TextItem accTypeText;

	public CreditCardAccountView() {
		createControls();
	}

	private void createControls() {

		accTypeText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.accountType());
		// accTypeText.setWidth("*");
		accTypeText.setDisabled(true);
		accTypeText.setValue(FinanceApplication.getFinanceUIConstants()
				.creditCardOrLineOfCredit());

		TextItem numText = new TextItem(FinanceApplication
				.getFinanceUIConstants().accountNo());
		// numText.setWidth("*");
		TextItem accNameText = new TextItem(FinanceApplication
				.getFinanceUIConstants().accountName());
		// accNameText.setWidth("*");
		accNameText.setRequired(true);
		CheckboxItem activeCheck = new CheckboxItem(FinanceApplication
				.getFinanceUIConstants().active());
		SelectItem cashFlowSelect = new SelectItem(FinanceApplication
				.getFinanceUIConstants().cashFlowcategory());
		// cashFlowSelect.setWidth("*");
		cashFlowSelect.setValue(FinanceApplication.getFinanceUIConstants()
				.operating());
		AmountField opBalText = new AmountField(FinanceApplication
				.getFinanceUIConstants().openingBalance());
		// opBalText.setWidth("*");
		DateItem asofDate = UIUtils.date(FinanceApplication
				.getFinanceUIConstants().asOf());
		// asofDate.setWidth("*");

		DynamicForm chartForm = UIUtils.form(FinanceApplication
				.getFinanceUIConstants().chartOfAccountsInformation());
		chartForm.setFields(accTypeText, numText, accNameText, activeCheck,
				cashFlowSelect, opBalText, asofDate);

		CheckboxItem basisCheck = new CheckboxItem(FinanceApplication
				.getFinanceUIConstants().thisIsConsideredACashAccount());

		DynamicForm basisForm = UIUtils.form(FinanceApplication
				.getFinanceUIConstants().cashBasisAccounting());
		basisForm.setFields(basisCheck);

		TextAreaItem commentsArea = new TextAreaItem();
		// commentsArea.setWidth("*");
		commentsArea.setShowTitle(false);

		DynamicForm commentsForm = UIUtils.form(FinanceApplication
				.getFinanceUIConstants().comments());
		commentsForm.setFields(commentsArea);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("50%");
		leftVLay.add(chartForm);
		leftVLay.add(basisForm);
		leftVLay.add(commentsForm);

		TextItem bankName = new TextItem(FinanceApplication
				.getFinanceUIConstants().bankName());
		// bankName.setWidth("*");
		AmountField limitText = new AmountField(FinanceApplication
				.getFinanceUIConstants().creditLimit());
		// limitText.setWidth("*");
		IntegerField cardNumText = new IntegerField(FinanceApplication
				.getFinanceUIConstants().cardOrLoadNumber());
		// cardNumText.setWidth("*");

		DynamicForm creditForm = UIUtils.form(FinanceApplication
				.getFinanceUIConstants().creditCardAccountInformation());
		// creditForm.setWidth("*");
		// creditForm.setAutoHeight();
		creditForm.setFields(bankName, limitText, cardNumText);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(creditForm);

		AccounterButton saveCloseButt = new AccounterButton(FinanceApplication
				.getFinanceUIConstants().saveAndClose());
		// saveCloseButt.setAutoFit(true);
		// saveCloseButt.setLayoutAlign(Alignment.LEFT);

		AccounterButton saveNewButt = new AccounterButton(FinanceApplication
				.getFinanceUIConstants().saveAndNew());
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
		saveCloseButt.enabledButton();
		saveNewButt.enabledButton();
		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setMargin(20);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(topHLay);
		mainVLay.add(buttHLay);
		// setAutoSize(true);
		add(mainVLay);
		setSize("750", "500");

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
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return FinanceApplication.getFinanceUIConstants().titleToGoHere();
	}

}
