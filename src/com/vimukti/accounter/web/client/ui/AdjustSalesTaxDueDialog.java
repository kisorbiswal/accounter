package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */
@SuppressWarnings("unchecked")
public class AdjustSalesTaxDueDialog extends BaseDialog {
	public AdjustSalesTaxDueDialog(String title, String description) {
		super(title, description);
		createControls();
		center();
	}

	private void createControls() {
		setTitle(FinanceApplication.getFinanceUIConstants().adjustSalesTax());

		Label lab1 = new Label(FinanceApplication.getFinanceUIConstants()
				.adjustSalesTax());
		// lab1.setWrap(false);
		// lab1.setAutoFit(true);

		Label lab2 = new Label(FinanceApplication.getFinanceUIConstants()
				.selectDate());
		lab2.setHeight("1");
		// lab2.setOverflow(Overflow.VISIBLE);
		// lab2.setWrap(false);
		lab2.setWidth("100%");

		DateItem effectDate = UIUtils.date(FinanceApplication
				.getFinanceUIConstants().dateEffective());
		effectDate.setRequired(true);

		IntegerField entryText = new IntegerField(FinanceApplication
				.getFinanceUIConstants().journalEntryNo());

		SelectItem incSelect = new SelectItem(FinanceApplication
				.getFinanceUIConstants().taxIncome());
		incSelect.setRequired(true);

		SelectItem codeSelect = new SelectItem(FinanceApplication
				.getFinanceUIConstants().taxCodeAdjust());
		codeSelect.setRequired(true);

		RadioGroupItem incDecRadio = new RadioGroupItem(FinanceApplication
				.getFinanceUIConstants().adjust());

		incDecRadio.setValueMap(FinanceApplication.getFinanceUIConstants()
				.increase(), FinanceApplication.getFinanceUIConstants()
				.decrease());

		AmountField amtText = new AmountField(FinanceApplication
				.getFinanceUIConstants().amount());
		TextItem memoText = new TextItem();
		memoText.setTitle(FinanceApplication.getFinanceUIConstants().memo());

		final DynamicForm taxForm = new DynamicForm();
		taxForm.setFields(effectDate, entryText, incSelect, codeSelect,
				incDecRadio, amtText, memoText);

		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				return taxForm.validate();
			}

		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		// mainVLay.setTop(30);
		mainVLay.add(lab1);
		mainVLay.add(lab2);
		mainVLay.add(taxForm);
		// setOverflow(Overflow.VISIBLE);
		setBodyLayout(mainVLay);

		setSize("450", "321");
		// show();
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
}
