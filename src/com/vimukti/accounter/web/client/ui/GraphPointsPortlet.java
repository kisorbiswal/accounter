package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;

public abstract class GraphPointsPortlet extends Portlet {

	public GraphPointsPortlet(ClientPortletConfiguration configuration,
			String title, String gotoString) {
		super(configuration, title, gotoString);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if (isInitializing) {
			return;
		}
		isInitializing = true;
		refreshWidget();
	}

}
