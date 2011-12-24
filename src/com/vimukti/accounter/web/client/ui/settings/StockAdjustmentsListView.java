package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class StockAdjustmentsListView extends BaseListView<StockAdjustmentList> {

	private int start;

	@Override
	public void updateInGrid(StockAdjustmentList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new StockAdjustmentsListGrid(false);
		grid.init();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		reloadRecords();
	}

	public void reloadRecords() {
		Accounter.createHomeService().getStockAdjustments(
				new AccounterAsyncCallback<ArrayList<StockAdjustmentList>>() {

					@Override
					public void onException(AccounterException exception) {
						grid.addEmptyMessage(messages().noRecordsToShow());
					}

					@Override
					public void onResultSuccess(
							ArrayList<StockAdjustmentList> result) {
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.addRecords(result);
						} else {
							grid.addEmptyMessage(messages().noRecordsToShow());
						}
						grid.sort(10, false);
					}
				});
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.messages().stockAdjustments();
	}

	@Override
	protected Action getAddNewAction() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages().readOnly()))
			return ActionFactory.getStockAdjustmentAction();
		else
			return null;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("isActive", isActiveAccounts);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		// isActiveAccounts = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		// if (isActiveAccounts) {
		// viewSelect.setComboItem(messages().active());
		// } else {
		// viewSelect.setComboItem(messages().inActive());
		// }

	}

	@Override
	protected String getAddNewLabelString() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages().readOnly()))
			return Accounter.messages().addaNew(
					Accounter.messages().stockAdjustment());
		else
			return "";
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().stockAdjustments();
	}

}
