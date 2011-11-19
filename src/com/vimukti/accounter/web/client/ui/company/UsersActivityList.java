package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.forms.ClickableSafeHtmlCell;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

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
				DateTimeFormat datefmt = DateTimeFormat.getFormat(Accounter
						.getCompany().getPreferences().getDateFormat());
				String dateformat = datefmt.format(new Date(object.getTime()));
				DateTimeFormat timefmt = DateTimeFormat.getFormat("h:mm a");
				String timeFormat = timefmt.format(new Date(object.getTime()));
				return dateformat + " " + timeFormat;
			}
		};
		// dateColumn.setSortable(false);

		TextColumn<ClientActivity> userNameColumn = new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return object.getUserName();
			}
		};

		Column<ClientActivity, ClientActivity> activityColumn = getActivityColumn();

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
		this.addColumn(dateColumn, Accounter.constants().modifiedTime());
		this.addColumn(userNameColumn, Accounter.constants().userName());
		this.addColumn(activityColumn, Accounter.constants().activity());
		this.addColumn(nameColumn, Accounter.constants().name());
		this.addColumn(transactionDateColumn, Accounter.constants().date());
		this.addColumn(amountColumn, Accounter.constants().amount());
		this.setColumnWidth(dateColumn, "170px");
		this.setColumnWidth(userNameColumn, "160px");
		this.setColumnWidth(activityColumn, "200px");

	}

	private Column<ClientActivity, ClientActivity> getActivityColumn() {

		List<HasCell<ClientActivity, ?>> cells = new ArrayList<HasCell<ClientActivity, ?>>();
		cells.add(new TextColumn<ClientActivity>() {

			@Override
			public String getValue(ClientActivity object) {
				return getActivityType(object);
			}
		});
		Column<ClientActivity, SafeHtml> linkColumn = new Column<ClientActivity, SafeHtml>(
				new ClickableSafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ClientActivity object) {
				final String value = object.getDataType() != null ? object
						.getDataType() : "";
				SafeHtmlBuilder shb = new SafeHtmlBuilder();
				shb.appendHtmlConstant("<a href='#'>");
				shb.appendEscaped(value);
				shb.appendHtmlConstant("</a>");
				return shb.toSafeHtml();
			}
		};
		linkColumn
				.setFieldUpdater(new FieldUpdater<ClientActivity, SafeHtml>() {

					@Override
					public void update(int index, ClientActivity object,
							SafeHtml safeHtml) {
						openLinkAction(object);
					}
				});
		cells.add(linkColumn);

		Column<ClientActivity, ClientActivity> column = new Column<ClientActivity, ClientActivity>(
				new CompositeCell<ClientActivity>(cells)) {

			@Override
			public ClientActivity getValue(ClientActivity object) {
				return object;
			}
		};
		return column;

	}

	protected String getActivityType(ClientActivity activity) {
		StringBuffer buffer = new StringBuffer();
		int type = activity.getActivityType();
		switch (type) {
		case 0:
			return Accounter.constants().loggedIn();
		case 1:
			return Accounter.constants().loggedOut();
		case 2:
			buffer.append(Accounter.constants().added());
			buffer.append(" : ");
			return buffer.toString();
		case 3:
			buffer.append(Accounter.constants().edited());
			buffer.append(" : ");
			return buffer.toString();
		case 4:
			buffer.append(Accounter.constants().deleted());
			buffer.append(" : ");
			return buffer.toString();
		case 5:
			return Accounter.constants().updatedPreferences();
		default:
			break;
		}
		return null;
	}

	public void sortRowData(int columnIndex, boolean isAscending) {
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

	private void openLinkAction(ClientActivity object) {
		ReportsRPC.openTransactionView(object.getObjType(),
				object.getObjectID());
	}
}