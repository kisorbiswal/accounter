package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class ManageCountryRegionDialog extends GroupDialog<ClientCustomer> {
	protected GroupDialogButtonsHandler groupDialogButtonHandler;

	public ManageCountryRegionDialog(String title, String description) {
		super(title, description);
		initialise();
		center();
	}// end of constructor

	public void initialise() {

		getGrid().addColumn(ListGrid.COLUMN_TYPE_TEXT,
				Accounter.constants().countryRegion());
		// ListGridField country_or_RegionField=new ListGridField("country",);
		// country_or_RegionField.setWidth(220);
		// addField(country_or_RegionField);
		setSize("40%", "40%");

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {
				// TODO Auto-generated method stub

			}

			public void onFirstButtonClick() {
				new CountryRegionDialog(Accounter.constants().addCountry(),
						Accounter.constants().enterNameOfCountry()).show();

			}

			public void onSecondButtonClick() {
				new CountryRegionDialog(Accounter.constants().editCountry(),
						Accounter.constants().enterNameOfCountry()).show();

			}

			public void onThirdButtonClick() {
				// TODO Auto-generated method stub

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}// end of initialise

	@Override
	public Object getGridColumnValue(ClientCustomer obj, int index) {
		// NOTHING TO DO.
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
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}// end of ManageCountryRegionDialog
