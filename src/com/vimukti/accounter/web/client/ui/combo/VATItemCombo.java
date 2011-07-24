package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

/**
 * @author Murali.A
 * 
 */
public class VATItemCombo extends CustomCombo<ClientTAXItem> {

	/**
	 * @param title
	 */
	public VATItemCombo(String title) {
		super(title);
		initCombo(getVATItmes());
	}

	public VATItemCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		initCombo(getVATItmes());
	}

	public VATItemCombo(String title, ClientTAXAgency taxAgency) {
		super(title);
		initCombo(getVATItmes());

	}

	/* This method returns the VATItems for a VATAgency */
	public List<ClientTAXItem> getVATItmesByVATAgncy(ClientTAXAgency taxAgency) {
		List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		if (taxAgency != null) {
			for (ClientTAXItem vatItem : getCompany()
					.getTaxItems()) {
				if (vatItem.getTaxAgency().equalsIgnoreCase(
						taxAgency.getID())) {
					vatItmsList.add(vatItem);
				}
			}
		}
		return vatItmsList;
	}

	/* VATItmes whose 'isPercentage' is false, only allowed into the list */
	List<ClientTAXItem> getVATItmes() {
		List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : getCompany()
				.getTaxItems()) {
			if (!vatItem.isPercentage()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

	/* VATItmes whose 'isPercentage' is true, only allowed into the list */
	public List<ClientTAXItem> getFilteredVATItems() {
		List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : getCompany()
				.getTaxItems()) {
			if (vatItem.isPercentage()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

	/*
	 * VATItmes whose 'isPercentage' is true,and "Sales" type VATItems only
	 * allowed into the list
	 */
	public List<ClientTAXItem> getSalesWithPrcntVATItems() {
		List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : getFilteredVATItems()) {
			if (vatItem.isSalesType()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

	/*
	 * VATItmes whose 'isPercentage' is true,and "Purchase" type VATItems only
	 * allowed into the list
	 */
	public List<ClientTAXItem> getPurchaseWithPrcntVATItems() {
		List<ClientTAXItem> vatItmsList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : getFilteredVATItems()) {
			if (!vatItem.isSalesType()) {
				vatItmsList.add(vatItem);
			}
		}
		return vatItmsList;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.combo.CustomCombo#getDefaultAddNewCaption
	 * ()
	 */
	@Override
	public String getDefaultAddNewCaption() {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			return comboConstants.newTaxItem();
		else
			return comboConstants.newVATItem();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.combo.CustomCombo#getDisplayName(
	 * java.lang.Object)
	 */
	@Override
	protected String getDisplayName(ClientTAXItem object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.combo.CustomCombo#getSelectItemType()
	 */
	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.VAT_ITEM;
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.combo.CustomCombo#onAddNew()
	 */
	@Override
	public void onAddNew() {
		Action action = VatActionFactory.getNewVatItemAction();
		action.setActionSource(this);
		
		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientTAXItem object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
