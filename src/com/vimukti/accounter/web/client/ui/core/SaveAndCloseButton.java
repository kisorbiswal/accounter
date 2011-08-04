/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public class SaveAndCloseButton extends Button {

	/**
	 * Creates new Instance
	 */
	public SaveAndCloseButton(AbstractBaseView view) {
		super(Accounter.constants().saveAndClose());
		this.addStyleName("saveAndClose-Btn");
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

			}
		});
	}

}
