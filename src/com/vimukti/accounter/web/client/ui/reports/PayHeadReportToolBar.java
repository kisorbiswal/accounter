package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayheadCombo;

public class PayHeadReportToolBar extends DateRangeReportToolbar {

	private PayheadCombo payheadCombo;
	private ClientPayHead selectedPayHead;

	public PayHeadReportToolBar(AbstractReportView reportView) {
		super(reportView);
	}

	@Override
	protected void createControls() {
		payheadCombo = new PayheadCombo(messages.payhead());
		payheadCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayHead>() {

					@Override
					public void selectedComboBoxItem(ClientPayHead selectItem) {
						setSelectedPayHead(selectItem);
					}
				});

		super.createControls();
	}

	protected void setSelectedPayHead(ClientPayHead selectItem) {
		this.selectedPayHead = selectItem;
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		ClientPayHead selectedValue = payheadCombo.getSelectedValue();
		if (selectedValue != null) {
			reportview.makeReportRequest(selectedValue.getID(),
					fromItem.getDate(), toItem.getDate());
		} else {
			reportview.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	protected void accountData() {
		if (selectedPayHead != null) {
			accData(selectedPayHead);
		}
	}

	protected com.vimukti.accounter.web.client.ui.forms.FormItem<?> getItem() {
		return payheadCombo;
	}

	protected void accData(ClientPayHead payHead) {
		if (payHead != null) {
			selectedPayHead = payHead;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectedPayHead.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
		}
	}

}
