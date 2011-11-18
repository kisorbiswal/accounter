package com.vimukti.accounter.web.client.ui.company;


import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;

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
				DateTimeFormat datefmt = DateTimeFormat.getFormat(Accounter.getCompany().getPreferences().getDateFormat());
				String dateformat = datefmt.format(new Date(object.getTime()));
				 DateTimeFormat timefmt = DateTimeFormat.getFormat("h:mm a");
				 String timeFormat = timefmt.format(new Date(object.getTime()));
				return dateformat+" "+timeFormat;
			}
		};
	//	  dateColumn.setSortable(false);

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
				return DataUtils.getAmountAsString(object.getAmount());
			}
		};
		this.addColumn(dateColumn, Accounter.messages().modifiedTime());
		this.addColumn(userNameColumn, Accounter.messages().userName());
		this.addColumn(activity, Accounter.messages().activity());
		this.addColumn(nameColumn, Accounter.messages().name());
		this.addColumn(transactionDateColumn, Accounter.messages().date());
		this.addColumn(amountColumn, Accounter.messages().amount());
		this.setColumnWidth(dateColumn, "170px");
		this.setColumnWidth(userNameColumn, "160px");
		this.setColumnWidth(activity, "200px");

	}

	protected String getActivityDataType(ClientActivity activity) {
		String dataType = "";
		StringBuffer buffer = new StringBuffer();
		int type = activity.getActivityType();
		switch (type) {
		case 0:
			return dataType = Accounter.messages().loggedIn();
		case 1:
			return dataType = Accounter.messages().loggedOut();
		case 2:
			buffer.append(Accounter.messages().added());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 3:
			buffer.append(Accounter.messages().edited());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 4:
			buffer.append(Accounter.messages().deleted());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 5:
			return dataType = Accounter.messages().updatedPreferences();
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