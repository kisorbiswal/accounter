/**
 * 
 */
package com.vimukti.accounter.web.client.ui.combo;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetView;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;

/**
 * @author vimukti16
 * 
 */
public class DepreciationMethodCombo extends SelectItem {

	int selectedDeprciationMethod = 0;
	private NewFixedAssetView newFixedAssetView;

	/**
	 * @param title
	 */
	public DepreciationMethodCombo(String title) {
		super(title);
		this.addChangeHandler(getChangeHandler());
		initCombo();
	}

	protected ChangeHandler getChangeHandler() {
		ChangeHandler handler = new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				switch (getSelectedIndex()) {
				case 0:
					selectedDeprciationMethod = 0;
					break;
				case 1:
					selectedDeprciationMethod = ClientFixedAsset.METHOD_STRAIGHT_LINE;
					break;
				case 2:
					selectedDeprciationMethod = ClientFixedAsset.METHOD_REDUCING_BALANCE;
					break;
				}
				if (newFixedAssetView != null) {
					newFixedAssetView.getDepreciationAmount();
				}
			}
		};

		return handler;
	}

	public void initCombo() {
		setValueMap(new String[] {
				"",
				FinanceApplication.getAccounterComboConstants().straightLine(),
				FinanceApplication.getAccounterComboConstants()
						.reducingBalance() });
	}

	public int getSelectedValue() {

		return selectedDeprciationMethod;
	}
	
	public void setSelectedValue(int selectedDeprciationMethod) {

		this.selectedDeprciationMethod = selectedDeprciationMethod;
	}
	

	public String getNameByType(int depMethod) {
		switch (depMethod) {
		case 0:
			return "";
		case 1:
			return FinanceApplication.getAccounterComboConstants()
					.straightLine();
		case 2:
			return FinanceApplication.getAccounterComboConstants()
					.reducingBalance();
		}
		return "";

	}

	public void setView(NewFixedAssetView newFixedAssetView) {
		this.newFixedAssetView = newFixedAssetView;
	}
}
