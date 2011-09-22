package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

public class AccounterClassListDialog extends GroupDialog<ClientAccounterClass> {

	private GroupDialogButtonsHandler classGroupDialogButtonsHandler;

	private CreateClassDialog createClassDialog;

	private ClientAccounterClass clientAccounterClass;

	public AccounterClassListDialog(String title, String descript) {
		super(title, descript);
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.ACCOUNTER_CLASS);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {
			@Override
			public boolean onRecordClick(Object core, int column) {
				ClientAccounterClass accounterClass = (ClientAccounterClass) core;
				if (accounterClass != null) {
					enableEditRemoveButtons(true);
				} else {
					enableEditRemoveButtons(false);
				}
				return false;
			}
		});
		classGroupDialogButtonsHandler = new GroupDialogButtonsHandler() {

			@Override
			public void onThirdButtonClick() {
				deleteObject((IAccounterCore) listGridView.getSelection());
				if (listGridView.getRowCount() == 0) {

				}
				enableEditRemoveButtons(false);
			}

			@Override
			public void onSecondButtonClick() {
				showAddEditAccounterClassDialog((ClientAccounterClass) listGridView
						.getSelection());
				
			}

			@Override
			public void onFirstButtonClick() {
				showAddEditAccounterClassDialog(null);
			}

			@Override
			public void onCloseButtonClick() {
			}
		};

		addGroupButtonsHandler(classGroupDialogButtonsHandler);
		this.okbtn.setVisible(false);
	}

	protected void showAddEditAccounterClassDialog(
			ClientAccounterClass clientAccounterClass) {
		this.clientAccounterClass = clientAccounterClass;
		createClassDialog = new CreateClassDialog(this.clientAccounterClass,
				Accounter.constants().createClass(), "");
		createClassDialog
				.addSuccessCallback(new ValueCallBack<ClientAccounterClass>() {

					@Override
					public void execute(ClientAccounterClass accounterclass) {
						saveOrUpdate(accounterclass);
						enableEditRemoveButtons(false);
						
					}
				});
		createClassDialog.show();
	}

	@Override
	public String[] setColumns() {
		return new String[] { Accounter.constants().accounterClass() };
	}

	@Override
	protected List<ClientAccounterClass> getRecords() {
		return getCompany().getAccounterClasses();
	}

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	public Object getGridColumnValue(ClientAccounterClass accounterClass,
			int index) {
		switch (index) {
		case 0:
			if (accounterClass != null) {
				return accounterClass.getClassName();
			}
		}
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
