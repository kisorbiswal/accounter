/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

/**
 * @author Prasanna Kumar G
 * 
 */
public class SaveAndNewButtom extends ImageButton {

	private AbstractBaseView<?> view;

	/**
	 * Creates new Instance
	 */
	public SaveAndNewButtom(AbstractBaseView<?> view) {
		super(messages.saveAndNew(), Accounter.getFinanceImages()
				.saveAndNew());
		this.view = view;
		this.setTitle(messages.clickThisToOpenNew(
				view.getAction().getViewName()));
		// this.addStyleName("saveAndNew-Btn");
		addClichHandler();
	}

	/**
	 * 
	 */
	private void addClichHandler() {
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// Need to clarify after implemented approval process.
				if (view instanceof AbstractTransactionBaseView) {
					((AbstractTransactionBaseView<?>) view)
							.getTransactionObject().setSaveStatus(
									ClientTransaction.STATUS_APPROVE);
				}
				view.onSave(true);
			}
		});

	}

}
