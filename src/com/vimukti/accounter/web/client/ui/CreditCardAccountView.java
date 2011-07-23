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

		accTypeText = new TextItem(Accounter.getFinanceUIConstants()
				.accountType());
		// accTypeText.setWidth("*");
		accTypeText.setDisabled(true);
		accTypeText.setValue(Accounter.getFinanceUIConstants()
				.creditCardOrLineOfCredit());

		TextItem numText = new TextItem(Accounter
				.getFinanceUIConstants().accountNo());
		// numText.setWidth("*");
		TextItem accNameText = new TextItem(Accounter
				.getFinanceUIConstants().accountName());
		// accNameText.setWidth("*");
		accNameText.setRequired(true);
		CheckboxItem activeCheck = new CheckboxItem(Accounter
				.getFinanceUIConstants().active());
		SelectItem cashFlowSelect = new SelectItem(Accounter
				.getFinanceUIConstants().cashFlowcategory());
		// cashFlowSelect.setWidth("*");
		cashFlowSelect.setValue(Accounter.getFinanceUIConstants()
				.operating());
		AmountField opBalText = new AmountField(Accounter
				.getFinanceUIConstants().openingBalance());
		// opBalText.setWidth("*");
		DateItem asofDate = UIUtils.date(Accounter
				.getFinanceUIConstants().asOf());
		// asofDate.setWidth("*");

		DynamicForm chartForm = UIUtils.form(Accounter
				.getFinanceUIConstants().chartOfAccountsInformation());
		chartForm.setFields(accTypeText, numText, accNameText, activeCheck,
				cashFlowSelect, opBalText, asofDate);

		CheckboxItem basisCheck = new CheckboxItem(Accounter
				.getFinanceUIConstants().thisIsConsideredACashAccount());

		DynamicForm basisForm = UIUtils.form(Accounter
				.getFinanceUIConstants().cashBasisAccounting());
		basisForm.setFields(basisCheck);

		TextAreaItem commentsArea = new TextAreaItem();
		// commentsArea.setWidth("*");
		commentsArea.setShowTitle(false);

		DynamicForm commentsForm = UIUtils.form(Accounter
				.getFinanceUIConstants().comments());
		commentsForm.setFields(commentsArea);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("50%");
		leftVLay.add(chartForm);
		leftVLay.add(basisForm);
		leftVLay.add(commentsForm);

		TextItem bankName = new TextItem(Accounter
				.getFinanceUIConstants().bankName());
		// bankName.setWidth("*");
		AmountField limitText = new AmountField(Accounter
				.getFinanceUIConstants().creditLimit());
		// limitText.setWidth("*");
		IntegerField cardNumText = new IntegerField(Accounter
				.getFinanceUIConstants().cardOrLoadNumber());
		// cardNumText.setWidth("*");

		DynamicForm creditForm = UIUtils.form(Accounter
				.getFinanceUIConstants().creditCardAccountInformation());
		// creditForm.setWidth("*");
		// creditForm.setAutoHeight();
		creditForm.setFields(bankName, limitText, cardNumText);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(creditForm);

		AccounterButton saveCloseButt = new AccounterButton(Accounter
				.getFinanceUIConstants().saveAndClose());
		// saveCloseButt.setAutoFit(true);
		// saveCloseButt.setLayoutAlign(Alignment.LEFT);

		AccounterButton saveNewButt = new AccounterButton(Accounter
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
		return Accounter.getFinanceUIConstants().titleToGoHere();
	}

}
