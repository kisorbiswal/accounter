package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.ui.Accounter;

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
		clientJob.setJobName("");
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
