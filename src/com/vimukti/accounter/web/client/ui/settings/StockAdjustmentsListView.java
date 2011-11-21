package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class StockAdjustmentsListView extends BaseListView<StockAdjustmentList> {

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
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.addRecords(result);
						} else {
							grid.addEmptyMessage(messages.noRecordsToShow());
						}
					}
				});
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.messages().stockAdjustments();
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getStockAdjustmentAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.messages().addaNew(
				Accounter.messages().stockAdjustment());
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().stockAdjustments();
	}

}
