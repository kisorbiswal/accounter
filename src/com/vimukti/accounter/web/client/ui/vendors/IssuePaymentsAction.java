package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.IssuePaymentDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author kumar kasimala
 * @modified by Ravi kiran.G
 */

public class IssuePaymentsAction extends Action {

	public IssuePaymentsAction(String text) {
		super(text);
	}

	public IssuePaymentsAction(String text, String iconString) {
		super(text, iconString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		new IssuePaymentDialog(FinanceApplication.getVendorsMessages()
				.selectPaymentsToIssue(), FinanceApplication
				.getVendorsMessages().selectPaymentMethod()).show();
		// MainFinanceWindow.getViewManager().showView(view, data, isDependent,
		// IssuePaymentsAction.this);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().issuePayment();
	}
	
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/issue_payment.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "issuePayments";
	}

}
