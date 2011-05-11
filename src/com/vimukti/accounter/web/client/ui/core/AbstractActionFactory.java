package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.externalization.ActionsConstants;

public abstract class AbstractActionFactory {
	public static ActionsConstants actionsConstants = GWT
			.create(ActionsConstants.class);
}
