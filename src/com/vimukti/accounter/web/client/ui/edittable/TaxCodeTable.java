package com.vimukti.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TaxCodeTable extends AbstractDropDownTable<ClientTAXCode> {

	public TaxCodeTable() {
		super(Accounter.getCompany().getTaxCodes());
	}

	@Override
	public void initColumns() {
		TextColumn<ClientTAXCode> column = new TextColumn<ClientTAXCode>() {

			@Override
			public String getValue(ClientTAXCode object) {
				// String displayName;
				// ClientTAXItemGroup vatGroup;
				// if (object != null) {
				// displayName = object.getName() != null ? object.getName()
				// : "";
				// if (isSales) {
				// vatGroup = ((ClientTAXItemGroup) Accounter
				// .getCompany()
				// .getTAXItemGroup(object.getTAXItemGrpForSales()));
				//
				// } else {
				// vatGroup = ((ClientTAXItemGroup) Accounter.getCompany()
				// .getTAXItemGroup(
				// object.getTAXItemGrpForPurchases()));
				// }
				//
				// if (vatGroup instanceof ClientTAXItem) {
				// // The selected one is VATItem,so get 'VATRate' from
				// // 'VATItem'
				// if (vatGroup != null)
				// displayName += " - "
				// + ((ClientTAXItem) vatGroup).getTaxRate();
				// } else {
				// // The selected one is VATGroup,so get 'GroupRate' from
				// // 'VATGroup'
				// if (vatGroup != null)
				// displayName += " - "
				// + ((ClientTAXGroup) vatGroup)
				// .getGroupRate();
				// }
				// if (vatGroup != null && vatGroup.isPercentage())
				// displayName += "%";
				// return displayName;
				// } else
				// return "";
				return object.getDisplayName();
			}
		};
		this.addColumn(column);
		this.setColumnWidth(column, "100px");
	}

	@Override
	protected boolean filter(ClientTAXCode t, String string) {
		return t.getDisplayName().toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientTAXCode value) {
		return value.getDisplayName();
	}

	@Override
	protected ClientTAXCode getAddNewRow() {
		ClientTAXCode code = new ClientTAXCode();
		code.setName("Add New Code");
		return code;
	}

	@Override
	public void addNewItem() {
		// TODO
	}

	@Override
	public List<ClientTAXCode> getTotalRowsData() {
		return Accounter.getCompany().getTaxCodes();
	}

}
