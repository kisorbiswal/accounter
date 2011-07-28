package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.vat.TAXAgencyView;

/**
 * 
 * @author Raj Vimal
 * 
 */
public class NewTAXAgencyAction extends Action {

	protected TAXAgencyView view;

	public NewTAXAgencyAction(String text) {
		super(text);
		String flag;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			flag = Accounter.constants().company();
		else
			flag = Accounter.constants().VAT();
		this.catagory = flag;
	}

	public NewTAXAgencyAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = TAXAgencyView.getInstance();

					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewTAXAgencyAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Tax Agencies..", t);
			}
		});

	}

	@Override
	public ParentCanvas<?> getView() {
		// NOTHING TO DO.
		return view;
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newTaxAgency();
	}

	@Override
	public String getImageUrl() {
		return "/images/New_Tax_Agency.png";
	}

	@Override
	public String getHistoryToken() {
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			return "newVatAgency";
		else
			return "newTaxAgency";
	}
}
