package com.vimukti.accounter.web.client.ui.company;

import java.util.Date;

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
			}
		};
		listDataProvider.addDataDisplay(this);
		setPageSize(50);

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
				return new Date(object.getTime()).toString();
			}
		};
		dateColumn.setSortable(true);

		TextColumn<ClientActivity> userNameColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return object.getUserName();
			}
		};

		TextColumn<ClientActivity> activity = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return getActivityType(object.getActivityType());
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
				return object.getName();
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
		// this.addColumn(idColumn, "ID");
		this.addColumn(nameColumn, "Name");
		this.addColumn(transactionDateColumn, "Date");
		this.addColumn(amountColumn, "Amount");

	}

	protected String getActivityType(int activityType) {
		switch (activityType) {
		case 0:
			return "Logged in";
		case 1:
			return "Logged Out";
		case 2:
			return "Added ";
		case 3:
			return "Edited ";
		case 4:
			return "Deleted ";
		default:
			break;
		}
		return null;
	}

	private int columnIndex;
	private boolean isAscending;

	public void sortRowData(int columnIndex, boolean isAscending) {
		this.columnIndex = columnIndex;
		this.isAscending = isAscending;
		redraw();
	}
}