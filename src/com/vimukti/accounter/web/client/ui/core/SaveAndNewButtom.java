/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public class SaveAndNewButtom extends Button {

	/**
	 * Creates new Instance
	 */
	public SaveAndNewButtom(AbstractBaseView view) {
		super(Accounter.constants().saveAndNew());
		this.addStyleName("saveAndNew-Btn");
	}

}
