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

	private AbstractBaseView<?> view;

	/**
	 * Creates new Instance
	 */
	public SaveAndCloseButton(AbstractBaseView<?> baseView) {

		super(Accounter.constants().saveAndClose(), Accounter
				.getFinanceImages().saveAndClose());
		this.view = baseView;
		this.addStyleName("saveAndClose-Btn");
		this.setTitle(Accounter.messages().clickThisTo(this.getText(),
				view.getAction().getViewName()));
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
			
				view.onSave(false);
			}
		});
	}

}
