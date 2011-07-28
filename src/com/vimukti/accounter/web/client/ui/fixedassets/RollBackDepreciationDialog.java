package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

@SuppressWarnings("unchecked")
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
					.lastDepreciation() + "  " + lastDepreciationDateString);
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
		setWidth("300");
		mainPanel.setSpacing(3);

	}

	private void getLastDepreciationDate() {
		AsyncCallback<ClientFinanceDate> callBack = new AsyncCallback<ClientFinanceDate>() {

			public void onFailure(Throwable caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onSuccess(ClientFinanceDate result) {
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
				getCompany().getpreferences().getDepreciationStartDate());
		ClientFinanceDate tempDate = new ClientFinanceDate(
				depreciationStartDate.getTime());

		depreciationDate = new ArrayList<ClientFinanceDate>();

		if (lastDepreciationDate != null) {

			ClientFinanceDate tempLastDate = new ClientFinanceDate(
					lastDepreciationDate.getTime());
			tempLastDate.setDate(1);

			while (!(tempDate.getDate() == tempLastDate.getDate()
					&& tempDate.getMonth() == tempLastDate.getMonth() && tempDate
						.getYear() == tempLastDate.getYear())) {

				depreciationDate.add(new ClientFinanceDate(tempDate.getTime()));
				int month = tempDate.getMonth();
				tempDate.setMonth(month + 1);

			}
			depreciationDate.add(new ClientFinanceDate(tempDate.getTime()));
		}

	}

	protected void cancelClicked() {
		removeFromParent();
	}

	/**
	 * Called when Ok button clicked
	 */
	protected void okClicked() {
		rollBackDepreciation();
		removeFromParent();

	}

	private void rollBackDepreciation() {
		String dateString = dateBox.getValue(dateBox.getSelectedIndex());
		ClientFinanceDate date = UIUtils.stringToDate(dateString,
				(Accounter.constants().ddMMMyyyy()));

		AsyncCallback callBack = new AsyncCallback() {

			public void onFailure(Throwable caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onSuccess(Object result) {
				History.newItem(CompanyActionFactory.getDepriciationAction()
						.getHistoryToken());
				// CompanyActionFactory.getDepriciationAction().run(null, true);
			}

		};
		Accounter.createHomeService().rollBackDepreciation(date.getTime(),
				callBack);
	}

	protected void helpClick() {

		Accounter.showInformation(AccounterWarningType.NOT_YET_IMPLEMENTED);

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
		// currently not using this method anywhere.

	}

	// FinanceApplication.constants()
	// .rollBackDepreciation()

	@Override
	protected String getViewTitle() {
		return Accounter.constants().rollBackDepreciation();
	}
}
