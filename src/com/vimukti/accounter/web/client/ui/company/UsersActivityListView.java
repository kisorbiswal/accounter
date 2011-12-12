package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.translation.AbstractPagerView;
import com.vimukti.accounter.web.client.ui.translation.Pager;

public class UsersActivityListView extends AbstractPagerView<ClientActivity> {

	private Label titleItem;
	private DateField fromdate, toDate;
	private VerticalPanel mainPanel;
	private DynamicForm dateForm, buttonForm;
	private UsersActivityList activityList;
	private Button updateButton;

	@Override
	protected void createControls() {
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		titleItem = new Label(Accounter.messages().usersActivityLogTitle());
		titleItem.setStyleName(Accounter.messages().labelTitle());

		fromdate = new DateField(Accounter.messages().fromDate());
		toDate = new DateField(Accounter.messages().endDate());
		fromdate.setEnteredDate(new ClientFinanceDate());
		toDate.setEnteredDate(new ClientFinanceDate());

		updateButton = new Button(Accounter.messages().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshPager();
				refreshActivityList();
			}
		});

		buttonForm = new DynamicForm();
		buttonForm.setNumCols(2);

		dateForm = new DynamicForm();
		dateForm.setNumCols(6);
		dateForm.setFields(fromdate, toDate);

		pager = new Pager(50, this) {
			@Override
			protected void initData() {
				super.initData();
			}
		};

		activityList = new UsersActivityList(fromdate.getValue(),
				toDate.getValue()) {
			@Override
			protected void setPagerData(PaginationList<ClientActivity> result) {
				super.setPagerData(result);
				pager.setTotalResultCount(result.getTotalCount());
				updateData(result);
			}
		};
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

		HorizontalPanel panel = new HorizontalPanel();
		panel.add(dateForm);
		panel.add(updateButton);
		panel.setCellHorizontalAlignment(updateButton, ALIGN_RIGHT);
		panel.setWidth("100%");
		panel.addStyleName("user_activity_dateform");

		mainPanel.add(titleItem);
		mainPanel.add(panel);
		mainPanel.add(activityList);
		activityList.setStyleName("user_activity_log");
		mainPanel.add(pager);
		add(mainPanel);
	}

	private void refreshActivityList() {
		ClientFinanceDate startDate = fromdate.getEnteredDate();
		ClientFinanceDate endDate = toDate.getEnteredDate();
		activityList.setFromDate(startDate);
		activityList.setEndDate(endDate);
		activityList.setVisibleRangeAndClearData(new Range(0, 50), true);
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return "Users Activity";
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateListData() {
		activityList.setVisibleRangeAndClearData(
				new Range(pager.getStartRange(), pager.getRange()), true);
	}

}
