package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class RollBackDepreciationDialog extends BaseDialog {

	protected ClientFinanceDate lastDepreciationDate;
	protected List<ClientFinanceDate> depreciationDate;
	private ListBox dateBox;

	public RollBackDepreciationDialog() {
		super(Accounter.constants().rollBackDepreciation(), "");
		getLastDepreciationDate();
		// getAllDepreciationDates();
		Timer timer = new Timer() {
			@Override
			public void run() {
				createControl();
				center();
			}
		};
		timer.schedule(500);
	}

	private void createControl() {

		HorizontalPanel typeForm = new HorizontalPanel();
		typeForm.setWidth("100%");

		VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.setStyleName("margin-b");
		if (lastDepreciationDate != null) {
			String lastDepreciationDateString = UIUtils
					.getDateByCompanyType(lastDepreciationDate);
			Label introLabel = new Label(Accounter.constants()
					.lastDepreciation()
					+ "  " + lastDepreciationDateString);
			contentPanel.add(introLabel);
		}
		HTML prefixText = new HTML(Accounter.constants()
				.rollbackDepreciationTo());

		dateBox = new ListBox();
		if (depreciationDate != null) {
			for (ClientFinanceDate date : depreciationDate) {
				String dateString = UIUtils.getDateByCompanyType(date);
				dateBox.addItem(dateString);
			}
		}

		okbtn.setWidth("50px");
		cancelBtn.setWidth("80px");

		footerLayout.setCellHorizontalAlignment(okbtn,
				HasHorizontalAlignment.ALIGN_RIGHT);
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(contentPanel);
		mainVLay.add(prefixText);
		mainVLay.add(dateBox);

		setBodyLayout(mainVLay);
		footerLayout.getElement().getStyle().setMarginLeft(29, Unit.PCT);
		setWidth("300px");
		mainPanel.setSpacing(3);

	}

	private void getLastDepreciationDate() {
		AccounterAsyncCallback<ClientFinanceDate> callBack = new AccounterAsyncCallback<ClientFinanceDate>() {

			public void onException(AccounterException caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onResultSuccess(ClientFinanceDate result) {
				lastDepreciationDate = result;
				getAllDepreciationDates();
			}

		};
		Accounter.createHomeService().getDepreciationLastDate(callBack);

	}

	/*
	 * This method will calculate the months from depreciation start date to
	 * depreciation last date
	 */

	private void getAllDepreciationDates() {

		ClientFinanceDate depreciationStartDate = new ClientFinanceDate(
				getCompany().getPreferences().getDepreciationStartDate());
		ClientFinanceDate tempDate = new ClientFinanceDate(
				depreciationStartDate.getDate());

		depreciationDate = new ArrayList<ClientFinanceDate>();

		if (lastDepreciationDate != null) {

			ClientFinanceDate tempLastDate = new ClientFinanceDate(
					lastDepreciationDate.getDate());
			tempLastDate.setDay(1);

			while (!(tempDate.getDay() == tempLastDate.getDay()
					&& tempDate.getMonth() == tempLastDate.getMonth() && tempDate
					.getYear() == tempLastDate.getYear())) {

				depreciationDate.add(new ClientFinanceDate(tempDate.getDate()));
				int month = tempDate.getMonth();
				tempDate.setMonth(month + 1);

			}
			depreciationDate.add(new ClientFinanceDate(tempDate.getDate()));
		}

	}

	protected void processCancel() {
		removeFromParent();
	}

	/**
	 * Called when Ok button clicked
	 */
	protected void processOK() {
		rollBackDepreciation();
		removeFromParent();

	}

	private void rollBackDepreciation() {
		String dateString = dateBox.getValue(dateBox.getSelectedIndex());
		ClientFinanceDate date = UIUtils.stringToDate(dateString, (Accounter
				.constants().ddMMyyyy()));

		AccounterAsyncCallback callBack = new AccounterAsyncCallback() {

			public void onException(AccounterException caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onResultSuccess(Object result) {
				History.newItem(ActionFactory.getDepriciationAction()
						.getHistoryToken());
				// ActionFactory.getDepriciationAction().run(null, true);
			}

		};
		Accounter.createHomeService().rollBackDepreciation(date.getDate(),
				callBack);
	}

	protected void helpClick() {

		Accounter.showInformation(AccounterWarningType.NOT_YET_IMPLEMENTED);

	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
