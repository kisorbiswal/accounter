package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
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

public class AdjustSalesTaxDueDialog extends BaseDialog {
	public AdjustSalesTaxDueDialog(String title, String description) {
		super(title, description);
		createControls();
		center();
	}

	private void createControls() {
		Label lab1 = new Label(Accounter.constants().adjustSalesTax());
		// lab1.setWrap(false);
		// lab1.setAutoFit(true);

		Label lab2 = new Label(Accounter.constants().selectDate());
		lab2.setHeight("1px");
		// lab2.setOverflow(Overflow.VISIBLE);
		// lab2.setWrap(false);
		lab2.setWidth("100%");

		DateItem effectDate = UIUtils.date(Accounter.constants()
				.dateEffective(), null);
		effectDate.setRequired(true);

		IntegerField entryText = new IntegerField(this, Accounter.constants()
				.journalEntryNo());

		SelectItem incSelect = new SelectItem(Accounter.constants().taxIncome());
		incSelect.setRequired(true);

		SelectItem codeSelect = new SelectItem(Accounter.constants()
				.taxCodeAdjust());
		codeSelect.setRequired(true);

		RadioGroupItem incDecRadio = new RadioGroupItem(Accounter.constants()
				.adjust());

		incDecRadio.setValueMap(Accounter.constants().increase(), Accounter
				.constants().decrease());

		AmountField amtText = new AmountField(Accounter.constants().amount(),
				this);
		TextItem memoText = new TextItem();
		memoText.setTitle(Accounter.constants().memo());

		final DynamicForm taxForm = new DynamicForm();
		taxForm.setFields(effectDate, entryText, incSelect, codeSelect,
				incDecRadio, amtText, memoText);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		// mainVLay.setTop(30);
		mainVLay.add(lab1);
		mainVLay.add(lab2);
		mainVLay.add(taxForm);
		// setOverflow(Overflow.VISIBLE);
		setBodyLayout(mainVLay);

		setSize("450px", "321px");
		// show();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}


	@Override
	protected boolean onOK() {
		return true;
	}
}
