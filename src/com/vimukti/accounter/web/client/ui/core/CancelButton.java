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
public class CancelButton extends Button {

	private AbstractBaseView<?> currentView;

	/**
	 * Creates new Instance
	 */
	public CancelButton(AbstractBaseView<?> view) {
		super(Accounter.constants().cancel());
		this.currentView = view;
		this.addStyleName("cancle-Btn");
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				currentView.close();
			}
		});
	}

}
