package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewJobAction extends Action<ClientJob> {
	private JobView view;
	private ClientCustomer customer;

	public NewJobAction(ClientCustomer customer) {
		this.customer = customer;
	}

	@Override
	public String getText() {
		return messages.newJob();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new JobView(customer);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewJobAction.this);
				/*
				 * NewJobDialog jobDialog = new NewJobDialog(null,
				 * messages.job(), ""); final ClientCompany company =
				 * Accounter.getCompany(); jobDialog.addSuccessCallback(new
				 * ValueCallBack<ClientJob>() {
				 * 
				 * @Override public void execute(final ClientJob value) {
				 * Accounter.createCRUDService().create(value, new
				 * AsyncCallback<Long>() {
				 * 
				 * @Override public void onSuccess(Long result) {
				 * value.setID(result);
				 * company.processUpdateOrCreateObject(value); }
				 * 
				 * @Override public void onFailure(Throwable caught) {
				 * caught.printStackTrace(); } });
				 * 
				 * } });
				 */
			}

		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	@Override
	public String getHistoryToken() {
		return "newjob";
	}

	@Override
	public String getHelpToken() {
		return "new-job";
	}
}