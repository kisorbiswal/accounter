/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.ManageTAXCodeListGrid;

/**
 * @author gwt
 * 
 */
public class ManageTAXCodesListView extends BaseListView<ClientTAXCode> {

	private List<ClientTAXCode> listOfTaxCodes;

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return ActionFactory.getNewTAXCodeAction();
		} else {
			return null;
		}
	}

	@Override
	public void onSuccess(PaginationList<ClientTAXCode> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return messages.addaNewTaxCode();
		} else {
			return "";
		}
	}

	@Override
	protected String getListViewHeading() {
		return messages.taxCodesList();
	}

	@Override
	protected void initGrid() {
		grid = new ManageTAXCodeListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();

		listOfTaxCodes = getCompany().getTaxCodes();
		filterList(true);

	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientTAXCode taxCode : listOfTaxCodes) {
			if (isActive) {
				if (taxCode.isActive() == true)
					grid.addData(taxCode);
			} else if (taxCode.isActive() == false) {
				grid.addData(taxCode);
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	public void initListCallback() {

	}

	@Override
	public void updateInGrid(ClientTAXCode objectTobeModified) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
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
		// viewSelect.setComboItem(messages.active());
		// } else {
		// viewSelect.setComboItem(messages.inActive());
		// }

	}

	@Override
	protected String getViewTitle() {
		return messages.vatCodeList();
	}

}
