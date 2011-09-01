package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class UsersActivityListView extends BaseView {

	private Label titleItem;
	private DateField fromdate, endDate;
	private VerticalPanel mainPanel;
	private DynamicForm buttonForm;
	private UsersActivityList activityList;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		titleItem = new Label("User's Activity Log");

		fromdate = new DateField("From Date");
		fromdate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {

			}
		});
		endDate = new DateField("End Date");
		endDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {

			}
		});

		buttonForm = new DynamicForm();
		buttonForm.setNumCols(4);
		buttonForm.setFields(fromdate, endDate);

		activityList = new UsersActivityList(fromdate.getValue(),
				endDate.getValue());
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		pager.setDisplay(activityList);
		// activityList.addColumnSortHandler(new Handler() {
		//
		// @Override
		// public void onColumnSort(ColumnSortEvent event) {
		// Column<?, ?> column = event.getColumn();
		// int columnIndex = activityList
		// .getColumnIndex((Column<ClientActivity, ?>) column);
		// boolean isAscending = event.isSortAscending();
		// activityList.sortRowData(columnIndex, isAscending);
		// }
		// });

		mainPanel.add(titleItem);
		mainPanel.add(buttonForm);
		mainPanel.add(activityList);
		mainPanel.add(pager);
		add(mainPanel);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return "Users Activity";
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}
}
