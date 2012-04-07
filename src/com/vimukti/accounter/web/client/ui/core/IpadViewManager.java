package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * 
 * 
 */

public class IpadViewManager extends ViewManager {

	@Override
	protected StyledPanel createRightPanel() {
		// We don't need right panel in iPad so return null
		return null;
	}

}
