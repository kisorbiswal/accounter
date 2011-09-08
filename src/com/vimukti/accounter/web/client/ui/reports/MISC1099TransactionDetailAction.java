package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MISC1099TransactionDetailAction extends Action {

	private MISC1099TransactionDetailReport report;
	private int boxNo;
	private long vendorId;

	public MISC1099TransactionDetailAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				report = new MISC1099TransactionDetailReport(vendorId, boxNo);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, MISC1099TransactionDetailAction.this);

			}

			@Override
			public void onFailure(Throwable reason) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		return "MISC1099TransactionDetail";
	}

	@Override
	public String getHelpToken() {
		return "MISC-1099-transaction-detail";
	}

	public void setBoxNo(int boxNo) {
		this.boxNo = boxNo;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
}
