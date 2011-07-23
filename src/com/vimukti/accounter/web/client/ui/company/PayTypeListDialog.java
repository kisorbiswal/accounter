package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

@SuppressWarnings("unchecked")
public class PayTypeListDialog extends GroupDialog {

	protected GroupDialogButtonsHandler groupDialogButtonHandler;

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);

	public PayTypeListDialog(String title, String descript) {
		super(title, descript);
		// TODO Auto-generated constructor stub
		initialise();
		center();
	}

	private void initialise() {

		setSize("30%", "50%");

		@SuppressWarnings("unused")
		final String title;
		@SuppressWarnings("unused")
		final String description;

		title = Accounter.getCompanyMessages().addOrEditPayType();
		description = Accounter.getCompanyMessages().toAddPayType();
		DialogGrid grid = getGrid();
		grid.addColumn(ListGrid.COLUMN_TYPE_TEXT, companyConstants.active());
		grid.addColumn(ListGrid.COLUMN_TYPE_TEXT, companyConstants
				.description());

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {
				// TODO Auto-generated method stub

			}

			public void onFirstButtonClick() {
				// TODO Auto-generated method stub
				// new AddOrEditPayTypeDialog(title, description).show();
			}

			public void onSecondButtonClick() {
				// TODO Auto-generated method stub
				// new AddOrEditPayTypeDialog(title, description).show();
			}

			public void onThirdButtonClick() {
				// TODO Auto-generated method stub

			}

		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] setColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List getRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteCallBack() {
	}

	public void addCallBack() {
	}

	public void editCallBack() {
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getCompanyMessages().addOrEditPayType();
	}

}
