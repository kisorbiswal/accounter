package com.vimukti.accounter.web.client.ui.payroll;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class AttendanceOrProductionTypeListView extends
		BaseListView<ClientAttendanceOrProductionType> {

	public AttendanceOrProductionTypeListView() {
		this.getElement().setId("AttendanceOrProductionTypeListView");
		isViewSelectRequired = false;
	}

	@Override
	public void updateInGrid(ClientAttendanceOrProductionType objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new AttendanceOrProductionTypeListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.attendanceOrProductionTypeList();
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getNewAttendanceProductionTypeAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addNew(messages.attendanceOrProductionType());
	}

	@Override
	protected String getViewTitle() {
		return messages.attendanceOrProductionTypeList();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);

		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

	}

	@Override
	protected void filterList(boolean isActive) {
		this.isActive = isActive;
		onPageChange(0, getPageSize());
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedValue = viewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.active())) {
			isActive = true;
		} else {
			isActive = false;
		}
		map.put("isActive", isActive);
		map.put("start", start);
		return map;
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		isActive = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		if (isActive) {
			viewSelect.setComboItem(messages.active());
		} else {
			viewSelect.setComboItem(messages.inActive());
		}

	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createPayrollService().getAttendanceProductionTypes(start,
				length, this);
	}

	@Override
	public void onSuccess(
			PaginationList<ClientAttendanceOrProductionType> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

}
