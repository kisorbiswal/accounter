package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class TDSFiledDetailsView extends BaseView {

	int formType;
	String ackNo;
	int financialYearStart;
	int financialYearEnd;
	int quater;
	long dateOfFiled;

	LabelItem formNoLabel;
	LabelItem ackNoLabel;
	LabelItem dateOfFiledLabel;
	LabelItem financialYearLabel;
	LabelItem assesmentYearLabel;
	LabelItem quaterLabel;

	VerticalPanel mainPanel = new VerticalPanel();

	public TDSFiledDetailsView(int formType, String ackNo,
			int financialYearStart, int financialYearEnd, int quater,
			long dateOfFiled) {
		super();
		this.formType = formType;
		this.ackNo = ackNo;
		this.financialYearStart = financialYearStart;
		this.financialYearEnd = financialYearEnd;
		this.quater = quater;
		this.dateOfFiled = dateOfFiled;
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		Accounter.createHomeService().getTDSChallansForAckNo(ackNo,
				new AccounterAsyncCallback<ArrayList<ClientTDSChalanDetail>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientTDSChalanDetail> result) {
						if (result != null) {
							showItems(result);
						}
					}
				});
	}

	protected void showItems(ArrayList<ClientTDSChalanDetail> challans) {

		for (ClientTDSChalanDetail challan : challans) {
			VerticalPanel panel = new VerticalPanel();
			panel.add(new TDSChallanItem(challan));
			mainPanel.add(panel);
		}

	}

	private void createControls() {

		formNoLabel = new LabelItem();
		formNoLabel.setTitle(getFormTypes().get(formType - 1));
		ackNoLabel = new LabelItem();
		ackNoLabel.setTitle(messages.acknowledgmentNo());
		ackNoLabel.setValue(ackNo);
		dateOfFiledLabel = new LabelItem();
		dateOfFiledLabel.setTitle(messages.dateOfFiled());
		dateOfFiledLabel.setValue(UIUtils
				.getDateByCompanyType(new ClientFinanceDate(dateOfFiled)));
		financialYearLabel = new LabelItem();
		financialYearLabel.setTitle(messages.financialYear());
		financialYearLabel.setValue(Integer.toString(financialYearStart) + "-"
				+ Integer.toString(financialYearEnd));
		assesmentYearLabel = new LabelItem();
		assesmentYearLabel.setTitle(messages.assessmentYear());
		assesmentYearLabel.setValue(Integer.toString(financialYearStart + 1)
				+ "-" + Integer.toString(financialYearEnd + 1));
		quaterLabel = new LabelItem();
		quaterLabel.setTitle(messages.period());
		quaterLabel
				.setValue((String) getFinancialQuatersList().get(quater - 1));

		DynamicForm filedForm = new DynamicForm();
		filedForm.setFields(ackNoLabel, dateOfFiledLabel, financialYearLabel,
				assesmentYearLabel, quaterLabel);
		filedForm.setSize("30%", "100%");
		mainPanel.add(formNoLabel.getMainWidget());
		mainPanel.add(filedForm);

		mainPanel.setSize("100%", "100%");
		mainPanel.setHorizontalAlignment(ALIGN_LEFT);
		this.add(mainPanel);
	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("Form No.26Q");
		list.add("Form No.27Q");
		list.add("Form No.27EQ");
		return list;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.tdsFiledDetails();
	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		this.cancelButton = new CancelButton(this);
		buttonBar.add(cancelButton);
	}

}
