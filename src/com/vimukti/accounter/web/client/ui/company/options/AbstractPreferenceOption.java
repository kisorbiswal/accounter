/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.user.client.ui.Composite;

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

	public abstract String getTitle();

	public abstract void onSave();

	public abstract String getAnchor();
	
}
