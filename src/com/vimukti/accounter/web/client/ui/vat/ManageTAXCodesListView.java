/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vat;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
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
		return VatActionFactory.getNewTAXCodeAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return FinanceApplication.getVATMessages().addNewVATCode();
	}

	@Override
	protected String getListViewHeading() {
		return FinanceApplication.getVATMessages().VATCodeList();
	}

	@Override
	protected void initGrid() {
		grid = new ManageTAXCodeListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();

		listOfTaxCodes = FinanceApplication.getCompany().getTaxCodes();
		filterList(true);

	}

	@SuppressWarnings("unchecked")
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
			if (grid.getRecords().isEmpty()) {
				grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
			}
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
		// TODO Auto-generated method stub

	}

}
