package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.NewJobDialog;

public class JobDropDownTable extends AbstractDropDownTable<ClientJob> {
	private long customerId;

	public JobDropDownTable() {
		super(getjobs(0));
	}

	@Override
	public List<ClientJob> getTotalRowsData() {
		return getjobs(getCustomerId());
	}

	public static List<ClientJob> getjobs(long customerId) {
		final List<ClientJob> jobs = new ArrayList<ClientJob>();
		ClientCustomer customer = Accounter.getCompany()
				.getCustomer(customerId);
		if (customer != null) {
			jobs.addAll(customer.getJobs());
		}
		return jobs;
	}

	@Override
	protected ClientJob getAddNewRow() {
		ClientJob clientJob = new ClientJob();
		clientJob.setJobName("Add New Job");
		return clientJob;
	}

	@Override
	public void initColumns() {
		this.addColumn(new TextColumn<ClientJob>() {

			@Override
			public String getValue(ClientJob object) {
				return object.getJobName();
			}
		});

	}

	@Override
	protected boolean filter(ClientJob t, String string) {
		return t.getJobName().toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientJob value) {
		return value.getJobName();
	}

	@Override
	protected Class<?> getType() {
		return ClientJob.class;
	}

	@Override
	protected void addNewItem(String text) {
		final ClientCompany company = Accounter.getCompany();
		if (getCustomerId() != 0) {
			ClientCustomer customer = company.getCustomer(getCustomerId());
			NewJobDialog jobDialog = new NewJobDialog(null, messages.job(), "",
					customer);
			jobDialog.addSuccessCallback(new ValueCallBack<ClientJob>() {

				@Override
				public void execute(final ClientJob value) {
					Accounter.createCRUDService().create(value,
							new AsyncCallback<Long>() {

								@Override
								public void onSuccess(Long result) {
									value.setID(result);
									company.processUpdateOrCreateObject(value);
									selectRow(value);
								}

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}
							});
				}
			});
		}
	}

	@Override
	protected void addNewItem() {
		addNewItem("");

	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
		reInitData();
	}

}
