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
public class SaveAndNewButtom extends Button {

	private AbstractBaseView<?> view;

	/**
	 * Creates new Instance
	 */
	public SaveAndNewButtom(AbstractBaseView<?> view) {
		super(Accounter.constants().saveAndNew());
		this.view = view;
		this.addStyleName("saveAndNew-Btn");
		addClichHandler();
	}

	/**
	 * 
	 */
	private void addClichHandler() {
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				view.onSave(true);
			}
		});

	}

}
