package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.JobCombo;

public class CreateJobIdToolBar extends CreateStatementToolBar{

	
	private JobCombo jobCombo;
	
	public CreateJobIdToolBar(boolean isVendor, AbstractReportView reportView) {
		super(isVendor, reportView);
	getViewSelect().setVisible(false);
		jobCombo = new JobCombo("Job", false);
		addItems(jobCombo);
		
	}

	@Override
	protected void createDisplayJobCombo() {

		jobCombo
		.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientJob>() {
			@Override
			public void selectedComboBoxItem(
					ClientJob selectItem) {
				setJobId(selectItem.getID());
			}
		});
		if(getPayeeId()!= 0){
		jobCombo.setCustomer(Accounter.getCompany().getCustomer(getPayeeId()));
		}
	}
	
	
}
