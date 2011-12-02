package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.IDeleteCallback;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class DeleteButton extends ImageButton {

	private AbstractBaseView<?> view;
	private IAccounterCore obj;

	/**
	 * Creates new Instance
	 */
	public DeleteButton(AbstractBaseView<?> view, IAccounterCore obj) {
		super(Accounter.messages().delete(), Accounter.getFinanceImages()
				.delete());
		this.view = view;
		this.obj = obj;
		String name = obj != null ? obj.getName() : "";
		this.setTitle(Accounter.messages().clickThisTo(
				Accounter.messages().delete(), name));
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
				String name = obj.getName() != null ? obj.getName() : "";
				Accounter.showWarning(Accounter.messages().doyouwanttoDelete()
						+ name, AccounterType.WARNING,
						new ErrorDialogHandler() {

							@Override
							public boolean onYesClick() {
								executeDelete(obj);
								return true;
							}

							@Override
							public boolean onNoClick() {
								return true;
							}

							@Override
							public boolean onCancelClick() {
								return false;
							}
						});
			}
		});

	}

	private void executeDelete(IAccounterCore obj) {
		IDeleteCallback iDeleteCallback = new IDeleteCallback() {

			@Override
			public void deleteSuccess(IAccounterCore result) {
				view.cancel();
			}

			@Override
			public void deleteFailed(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				String errorString = AccounterExceptions
						.getErrorString(errorCode);
				Accounter.showError(errorString);
				caught.fillInStackTrace();

			}
		};

		if (obj instanceof ClientTransaction) {
			Accounter.deleteTransaction(iDeleteCallback, obj);
		} else {
			Accounter.deleteObject(iDeleteCallback, obj);
		}
	}

}
