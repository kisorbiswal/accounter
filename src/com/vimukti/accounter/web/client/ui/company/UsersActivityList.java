package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;

public class UsersActivityList extends CellTable<ClientActivity> {
	private ArrayList<ClientActivity> userActivities;
	private AsyncDataProvider<ClientActivity> listDataProvider;
	private ClientFinanceDate fromDate, endDate;

	public UsersActivityList() {
		createControls();
	}

	public UsersActivityList(ClientFinanceDate fromdate2,
			ClientFinanceDate endDate2) {
		this.fromDate = fromdate2;
		this.endDate = endDate2;
		createControls();
	}

	private void createControls() {

		// AsyncDataProvider<ClientActivity> listDataProvider = new
		// AsyncDataProvider<ClientActivity>() {
		//
		// @Override
		// protected void onRangeChanged(HasData<ClientActivity> display) {
		// int start = display.getVisibleRange().getStart();
		// int end = start + display.getVisibleRange().getLength();
		// end = end >= userActivities.size() ? userActivities.size()
		// : end;
		// updateRowData(start, userActivities);
		// }
		// };
		setVisibleRange(0, 50);
		listDataProvider = new AsyncDataProvider<ClientActivity>() {
			@Override
			protected void onRangeChanged(HasData<ClientActivity> display) {
				final int start = display.getVisibleRange().getStart();
				int length = display.getVisibleRange().getLength();
				Accounter.createHomeService().getUsersActivityLog(fromDate,
						endDate, start, length,
						new AsyncCallback<PaginationList<ClientActivity>>() {

							@Override
							public void onSuccess(
									PaginationList<ClientActivity> result) {
								updateRowData(start, result);
								setRowCount(result.getTotalCount());
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});
				// The remote service that should be implemented
				// remoteService.fetchPage(start, length, callback);
			}
		};
		listDataProvider.addDataDisplay(this);

		this.setWidth("100%", true);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		initTableColumns();
		updateGridData();
	}

	private void updateGridData() {

		// Accounter.createHomeService().getUsersActivityLog(fromDate, endDate,
		// new AsyncCallback<ArrayList<ClientActivity>>() {
		//
		// @Override
		// public void onSuccess(ArrayList<ClientActivity> result) {
		// listDataProvider.setList(result);
		// listDataProvider.refresh();
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		//
		// }
		// });

	}

	private void initTableColumns() {
		TextColumn<ClientActivity> dateColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return String.valueOf(object.getDate());
			}
		};

		TextColumn<ClientActivity> userNameColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return null;
			}
		};
		TextColumn<ClientActivity> activity = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return object.getActivity();
			}
		};
		TextColumn<ClientActivity> idColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return String.valueOf(object.getId());
			}
		};
		TextColumn<ClientActivity> nameColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return object.getActivity();
			}
		};

		TextColumn<ClientActivity> transactionDateColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return String.valueOf(object.getDate());
			}
		};
		TextColumn<ClientActivity> amountColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return String.valueOf(object.getAmount());
			}
		};
		this.addColumn(dateColumn, "Modified");
		this.addColumn(userNameColumn, "User Name");
		this.addColumn(activity, "Activity");
		this.addColumn(idColumn, "ID");
		this.addColumn(nameColumn, "Name");
		this.addColumn(transactionDateColumn, "Date");
		this.addColumn(amountColumn, "Amount");

	}
}