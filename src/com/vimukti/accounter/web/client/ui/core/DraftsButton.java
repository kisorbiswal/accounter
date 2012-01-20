package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class DraftsButton extends ImageButton {
	private AbstractBaseView<?> view;

	public DraftsButton(AbstractBaseView<?> view) {
		super("Save as drafts", Accounter.getFinanceImages().saveAndClose());
		this.view = view;
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DraftsButton.this.view.saveAsDrafts();
				DraftsButton.this.view.setSaveCliecked(true);
				DraftsButton.this.view.setCloseOnSave(true);
				DraftsButton.this.view.getManager().closeCurrentView();
			}
		});
	}
}
