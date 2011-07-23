package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

@SuppressWarnings("unchecked")
public class StartDateDialog extends BaseDialog {

	protected List<ClientFinanceDate> startDateList;
	private ListBox dateBox;
	private String initialDate;
	protected boolean isDateChanged;

	public StartDateDialog() {
		super(Accounter.getFixedAssetConstants().startDate(), "");
		getStartdates();
		Timer timer = new Timer() {
			@Override
			public void run() {
				createControl();
				center();
			}
		};
		timer.schedule(300);
	}

	private String getStartDateString() {
		ClientFinanceDate startDate = new ClientFinanceDate(Accounter
				.getCompany().getPreferences().getDepreciationStartDate());
		String startDateString = startDate.toString();

		return startDateString;
	}

	private void createControl() {
		Label introLabel = new Label(Accounter
				.getFixedAssetConstants().currentStartDateIs()
				+ getStartDateString());

		Label infoLabel = new Label();
		infoLabel.setText(Accounter.getFixedAssetConstants()
				.startDateForManagingFixedAsset());
		HTML prefixText = new HTML(Accounter.getFixedAssetConstants()
				.newStartDate());
		dateBox = new ListBox();
		dateBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String changedDate = dateBox.getValue(dateBox
						.getSelectedIndex());
				if (initialDate != null && changedDate != null
						&& initialDate.equals(changedDate)) {
					isDateChanged = false;
				} else {
					isDateChanged = true;
					initialDate = changedDate;
				}
			}
		});
		fillDateCombo();
		initialDate = dateBox.getValue(dateBox.getSelectedIndex());

		okbtn.setWidth("50px");
		cancelBtn.setWidth("80px");
		// footerLayout.setCellWidth(okbtn, "80%");

		VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.setSpacing(3);
		contentPanel.add(introLabel);
		contentPanel.add(infoLabel);
		contentPanel.add(prefixText);
		contentPanel.add(dateBox);
		bodyLayout.add(contentPanel);
		setWidth("350");
		// mainPanel.setSpacing(20);
	}

	private void fillDateCombo() {
		for (ClientFinanceDate date : startDateList) {
			dateBox.addItem(date.toString());
		}

		setDefaultStartDate();
	}

	public void setDefaultStartDate() {
		String startDate = getStartDateString();
		for (int i = 0; i < dateBox.getItemCount(); i++) {
			if (dateBox.getItemText(i).equalsIgnoreCase(startDate)) {
				dateBox.setItemSelected(i, true);
				return;
			}
		}
	}

	protected void cancelClicked() {
		removeFromParent();
	}

	/**
	 * Called when Ok button clicked
	 */
	protected void okClicked() {
		if (isDateChanged)
			changeStartDate();
		removeFromParent();

	}

	private void changeStartDate() {
		String dateString = dateBox.getValue(dateBox.getSelectedIndex());
		ClientFinanceDate date = UIUtils.stringToDate(dateString,
				(Accounter.getFixedAssetConstants().ddMMMyyyy()));

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
		Accounter.createHomeService().changeDepreciationStartDateTo(
				date.getTime(), callBack);
	}

	protected void helpClick() {

		Accounter.showInformation(AccounterWarningType.NOT_YET_IMPLEMENTED);

	}

	private void getStartdates() {
		AsyncCallback<List<ClientFinanceDate>> callBack = new AsyncCallback<List<ClientFinanceDate>>() {

			public void onFailure(Throwable caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onSuccess(List<ClientFinanceDate> result) {
				startDateList = result;
			}

		};
		Accounter.createHomeService().getFinancialYearStartDates(
				callBack);

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
		return Accounter.getFixedAssetConstants().startDate();
	}

}
