/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

/**
 * @author Prasanna Kumar G
 * 
 */
public class SaveAndCloseButton extends ImageButton {

	private AbstractBaseView<?> baseView;

	/**
	 * Creates new Instance
	 */
	public SaveAndCloseButton(AbstractBaseView<?> view) {

		super(Accounter.constants().saveAndClose(), Accounter
				.getFinanceImages().saveAndClose());
		this.baseView = view;
		this.addStyleName("saveAndClose-Btn");
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				baseView.setCloseOnSave(true);
				baseView.saveAndUpdateView();
			}
		});
	}

}
