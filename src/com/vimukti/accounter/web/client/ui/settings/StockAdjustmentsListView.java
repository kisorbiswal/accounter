package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;

public class StockAdjustmentsListView extends
		TransactionsListView<StockAdjustmentList> implements IPrintableView {

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
						grid.addEmptyMessage(messages.noRecordsToShow());
					}

					@Override
					public void onResultSuccess(
							ArrayList<StockAdjustmentList> result) {
						grid.removeAllRecords();
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.addRecords(result);
						} else {
							grid.addEmptyMessage(messages.noRecordsToShow());
						}
						grid.sort(10, false);
					}
				});
	}

	@Override
	protected String getListViewHeading() {
		return messages.stockAdjustments();
	}

	@Override
	protected Action getAddNewAction() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return new StockAdjustmentAction();
		else
			return null;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		start = (Integer) viewDate.get("start");
	}

	@Override
	protected String getAddNewLabelString() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return messages.addaNew(messages.stockAdjustment());
		else
			return "";
	}

	@Override
	protected String getViewTitle() {
		return messages.stockAdjustments();
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getStockAdjustmentsExportCsv(
				getExportCSVCallback(messages.stockAdjustments()));
	}
}
