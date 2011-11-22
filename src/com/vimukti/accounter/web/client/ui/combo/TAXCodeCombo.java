package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.vat.NewTAXCodeAction;

public class TAXCodeCombo extends CustomCombo<ClientTAXCode> {
	private boolean isSales;

	public TAXCodeCombo(String title, boolean isSales) {
		super(title);
		this.isSales = isSales;
		initCombo(getTAXCodesForSalesOrPurchase(getCompany()
				.getActiveTaxCodes()));
	}

	public TAXCodeCombo(String title, boolean isAddNewRequired, boolean isSales) {
		super(title, isAddNewRequired, 1);
		this.isSales = isSales;
		initCombo(getTAXCodesForSalesOrPurchase(getCompany()
				.getActiveTaxCodes()));
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.item();
	}

	@Override
	public String getDisplayName(ClientTAXCode object) {
		if (object != null) {
			return object.getName() != null ? object.getName() : "";
		}
		return "";
	}

	@Override
	public void onAddNew() {
		if (getCompany().getPreferences().isTrackTax()) {
			NewTAXCodeAction action = ActionFactory.getNewTAXCodeAction();
			action.setCallback(new ActionCallback<ClientTAXCode>() {

				@Override
				public void actionResult(ClientTAXCode result) {

					addItemThenfireEvent(result);

				}
			});

			action.run(null, true);
		}/*
		 * else { TaxDialog dialog = new TaxDialog(); dialog.setCallback(new
		 * ActionCallback<ClientTAXCode>() {
		 * 
		 * @Override public void actionResult(ClientTAXCode result) {
		 * addItemThenfireEvent(result);
		 * 
		 * } }); dialog.show(); }
		 */
	}

	@Override
	protected String getColumnData(ClientTAXCode object, int col) {
		switch (col) {
		case 0:
			return getDisplayName(object);
		case 1:
			if (isSales) {
				ClientTAXItem item = getCompany().getTaxItem(
						object.getTAXItemGrpForSales());
				return DataUtils.getAmountAsString(item.getTaxRate()) + "%";
			} else {
				ClientTAXItem item = getCompany().getTaxItem(
						object.getTAXItemGrpForPurchases());
				return DataUtils.getAmountAsString(item.getTaxRate()) + "%";
			}
		}
		return null;
	}

	protected List<ClientTAXCode> getTAXCodesForSalesOrPurchase(
			List<ClientTAXCode> activeTaxCodes) {

		List<ClientTAXCode> taxCodeList = new ArrayList<ClientTAXCode>();
		for (ClientTAXCode taxCode : activeTaxCodes) {
			if (isSales) {
				if (taxCode.getTAXItemGrpForSales() != 0) {
					taxCodeList.add(taxCode);
				}
			} else {
				if (taxCode.getTAXItemGrpForPurchases() != 0) {
					taxCodeList.add(taxCode);
				}
			}
		}
		Collections.sort(taxCodeList, new Comparator<ClientTAXCode>() {

			@Override
			public int compare(ClientTAXCode o1, ClientTAXCode o2) {
				return Long.valueOf(o1.getID()).compareTo(
						Long.valueOf(o2.getID()));
			}
		});
		return taxCodeList;
	}

	public void setSelectedObj(long taxCodeID) {
		for (ClientTAXCode taxCode : getComboItems()) {
			if (taxCode.getID() == taxCodeID) {
				this.selectedObject = taxCode;
				this.setSelectedItem(comboItems.indexOf(taxCode));
				break;
			}
		}
	}
}
