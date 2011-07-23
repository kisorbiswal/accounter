package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

@SuppressWarnings("unchecked")
public class AddEditFiscalYearDialog extends BaseDialog {

	public AddEditFiscalYearDialog(String title, String desc,
			ClientFiscalYear fiscalYear) {
		super(title, desc);
		// TODO Auto-generated constructor stub
		createControls(fiscalYear);
		center();
	}

	private void createControls(ClientFiscalYear fiscalYear) {

		// setHeight(250);
		// setWidth(400);

		DateItem startDate = new DateItem();
		startDate.setTitle(Accounter.getFinanceUIConstants()
				.startofFiscalYear());
		// startDate.setUseTextField(true);
		// int firstMonth =
		// FinanceApplication.getCompany().getFirstMonthOfFiscalYear();

		DateItem closeDate = new DateItem();
		closeDate.setTitle(Accounter.getFinanceUIConstants()
				.closeofFiscalYear());
		// closeDate.setUseTextField(true);
		VerticalPanel bodyLayout = new VerticalPanel();
		DynamicForm form = new DynamicForm();
		// form.setWidth(250);
		form.setFields(startDate, closeDate);
		bodyLayout.add(form);
		setBodyLayout(bodyLayout);

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
		return Accounter.getCompanyMessages().fiscalYear();
	}
}
