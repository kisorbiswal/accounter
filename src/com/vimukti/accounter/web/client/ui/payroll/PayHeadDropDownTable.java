package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;

public class PayHeadDropDownTable extends AbstractDropDownTable<ClientPayHead> {

	private static List<ClientPayHead> list = new ArrayList<ClientPayHead>();

	public PayHeadDropDownTable(boolean isAddNewRequired) {
		super(list, isAddNewRequired);
		initList();
	}

	private void initList() {
		Accounter.createPayrollService().getPayheads(0, -1,
				new AsyncCallback<PaginationList<ClientPayHead>>() {

					@Override
					public void onSuccess(PaginationList<ClientPayHead> result) {
						list = result;
						reInitData();
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public List<ClientPayHead> getTotalRowsData() {
		return list;
	}

	@Override
	protected ClientPayHead getAddNewRow() {
		ClientPayHead payhead = new ClientPayHead();
		payhead.setName(messages.comboDefaultAddNew(messages.payhead()));
		return payhead;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientPayHead> textColumn = new TextColumn<ClientPayHead>() {

			@Override
			public String getValue(ClientPayHead object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected boolean filter(ClientPayHead t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientPayHead value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem(String text) {
		NewPayHeadAction action;
		action = ActionFactory.getNewPayHeadAction();
		action.setCallback(new ActionCallback<ClientPayHead>() {

			@Override
			public void actionResult(ClientPayHead result) {
				selectRow(result);

			}
		});
		action.run(null, true);
	}

	@Override
	protected void addNewItem() {
		addNewItem("");

	}

}
