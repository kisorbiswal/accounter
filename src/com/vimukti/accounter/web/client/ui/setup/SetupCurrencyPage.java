package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.CustomLabel;

public class SetupCurrencyPage extends AbstractSetupPage{
	public static final String []currencyList = new String[]{};
	private ListBox baseCurrencyList;
	
	@Override
	public String getHeader() {
		return accounterConstants.setSupportedCurrencies();
	}

	@Override
	public VerticalPanel getPageBody() {
		VerticalPanel container = new VerticalPanel();
		HorizontalPanel baseCurrencyHorPanel = new HorizontalPanel();
		container.add(baseCurrencyHorPanel);
		
		CustomLabel baseCurrencyLabel = new CustomLabel(accounterConstants.baseCurrency());
		baseCurrencyHorPanel.add(baseCurrencyLabel);
		
		 baseCurrencyList = new ListBox();
		for(int i = 0; i <currencyList.length; i++ )
			baseCurrencyList.addItem(currencyList[i]);
		
		baseCurrencyHorPanel.add(baseCurrencyList);
		//add currencies grid
		
		return null;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub
		
	}

}
