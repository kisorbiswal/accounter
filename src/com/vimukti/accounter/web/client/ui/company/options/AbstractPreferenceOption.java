/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.user.client.ui.Composite;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class AbstractPreferenceOption extends Composite {

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
	}

	protected AccounterConstants constants = Accounter.constants();

	protected AccounterMessages messages = Accounter.messages();

	public abstract String getTitle();

	public abstract void onSave();

	public abstract String getAnchor();

}
