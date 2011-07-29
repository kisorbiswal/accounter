package com.vimukti.accounter.web.client.ui.vat;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.ManageVATGroupListGrid;

public class ManageVATGroupListView extends BaseListView<ClientTAXGroup> {

	private List<ClientTAXGroup> listOfVatGroups;

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getVatGroupAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.constants().addNewVATGroup();
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().VATGroupList();
	}

	@Override
	protected void initGrid() {
		grid = new ManageVATGroupListGrid();
		grid.addStyleName("listgrid-tl");
		grid.init();
		listOfVatGroups = getCompany().getVatGroups();
		filterList(true);
		getTotalLayout(grid);
	}

	
	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientTAXGroup vatGroup : listOfVatGroups) {
			if (isActive) {
				if (vatGroup.isActive() == true)
					grid.addData(vatGroup);
			} else if (vatGroup.isActive() == false) {
				grid.addData(vatGroup);
			}

		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
		}
	}

	@Override
	public void initListCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInGrid(ClientTAXGroup objectTobeModified) {
		// NOTHING TO DO.
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
	protected String getViewTitle() {
		return "Manage Vat Group";
	}

}
