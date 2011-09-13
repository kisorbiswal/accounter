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

	public UsersActivityList(ClientFinanceDate fromdate,
			ClientFinanceDate endDate) {
		this.setFromDate(fromdate);
		this.setEndDate(endDate);
		createControls();
	}

	private void createControls() {
		listDataProvider = new AsyncDataProvider<ClientActivity>() {
			@Override
			protected void onRangeChanged(HasData<ClientActivity> display) {
				final int start = display.getVisibleRange().getStart();
				int length = display.getVisibleRange().getLength();
				Accounter.createHomeService().getUsersActivityLog(
						getFromDate(), getEndDate(), start, length,
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
		setPageSize(50);
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
				return getActivityDataType(object);
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
				return String.valueOf(object.getDate() != null ? object
						.getDate() : "");
			}
		};
		TextColumn<ClientActivity> amountColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return String.valueOf(object.getAmount() != 0.0 ? object
						.getAmount() : "");
			}
		};
		this.addColumn(dateColumn, Accounter.constants().modifiedTime());
		this.addColumn(userNameColumn, Accounter.constants().userName());
		this.addColumn(activity, Accounter.constants().activity());
		this.addColumn(nameColumn, Accounter.constants().name());
		this.addColumn(transactionDateColumn, Accounter.constants().date());
		this.addColumn(amountColumn, Accounter.constants().amount());
		this.setColumnWidth(dateColumn, "250px");

	}

	protected String getActivityDataType(ClientActivity activity) {
		String dataType = "";
		StringBuffer buffer = new StringBuffer();
		int type = activity.getActivityType();
		switch (type) {
		case 0:
			return dataType = Accounter.constants().loggedIn();
		case 1:
			return dataType = Accounter.constants().loggedOut();
		case 2:
			buffer.append(Accounter.constants().added());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 3:
			buffer.append(Accounter.constants().edited());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 4:
			buffer.append(Accounter.constants().deleted());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 5:
			return dataType = Accounter.constants().updatedPreferences();
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

	public ClientFinanceDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(ClientFinanceDate fromDate) {
		this.fromDate = fromDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}
}