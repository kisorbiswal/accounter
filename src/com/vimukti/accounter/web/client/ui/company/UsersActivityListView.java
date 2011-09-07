package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientActivity;
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
	private HorizontalPanel dateButtonPanel;
	private DynamicForm dateForm, buttonForm;
	private UsersActivityList activityList;
	private Button updateButton, customizeButton;

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
		buttonForm.setNumCols(2);

		dateForm = new DynamicForm();
		dateForm.setNumCols(6);
		dateForm.setFields(fromdate, endDate);

		activityList = new UsersActivityList(fromdate.getValue(),
				endDate.getValue());
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		pager.setDisplay(activityList);
		pager.setStyleName("pager-alignment");
		activityList.addColumnSortHandler(new Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<?, ?> column = event.getColumn();
				int columnIndex = activityList
						.getColumnIndex((Column<ClientActivity, ?>) column);
				boolean isAscending = event.isSortAscending();
				activityList.sortRowData(columnIndex, isAscending);

			}
		});

		mainPanel.add(titleItem);
		mainPanel.add(dateForm);
		mainPanel.add(activityList);
		mainPanel.add(pager);
		add(mainPanel);
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return "Users Activity";
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}
}
