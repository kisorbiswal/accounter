package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.Timer;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;

/**
 * AccounterExecute is sub class of Timer, which is used to execute view after
 * save and close button clicked.or display Error if it is throws Execption
 * 
 * @author kumar kasimala
 * 
 */
public class AccounterExecute extends Timer {

	private CustomButton button;
	@SuppressWarnings("unchecked")
	private AbstractBaseView view;
	private int validationCount;
	private int actualCount;

	@SuppressWarnings("unchecked")
	public AccounterExecute(AbstractBaseView view, CustomButton button) {
		this.button = button;
		this.view = view;
		this.validationCount = view.validationCount;
		this.actualCount = view.validationCount;
		this.scheduleRepeating(200);
	}

	@Override
	public void run() {
		if (view.validationCount == validationCount) {
			try {
				if (view.validate()) {
					if (view.validationCount != 0) {
						view.validationCount--;
						validationCount--;
					}
				} else {
					if (view.validationCount != 0) {
						if (!view.warnOccured)
							view.validationCount--;
						validationCount--;
					}
				}

				if (!view.errorOccured && view.validationCount == 0) {
					view.saveAndUpdateView();
					view.validationCount--;
				} else if (view.errorOccured && view.validationCount == 0) {
					stop();
				}

			} catch (Exception e) {
				if (e instanceof JavaScriptException) {
					JavaScriptException scriptEx = (JavaScriptException) e;
					Accounter.showError(scriptEx.getDescription() == null ? e
							.toString() : scriptEx.getDescription());
				} else {
					view.validationCount--;
					validationCount--;
					BaseView.errordata.setHTML(BaseView.errordata.getHTML()
							+ "<li> "
							+ (e.getMessage() == null ? e.toString() : e
									.getMessage()) + ".");
					BaseView.commentPanel.setVisible(true);
					view.errorOccured = true;
					if (view.validationCount <= 0)
						stop();
					// Accounter.showError(e.getMessage() == null ? e.toString()
					// : e.getMessage());
				}
				// stop();
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void stop() {
		// button.getParent().setVisible(true);
		button.setEnabled(true);
		view.validationCount = this.actualCount;
		this.cancel();
	}

}
