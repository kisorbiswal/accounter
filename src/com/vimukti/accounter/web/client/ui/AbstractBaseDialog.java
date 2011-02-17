package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.IAccounterWidget;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

public abstract class AbstractBaseDialog<T> extends CustomDialog implements
		IAccounterWidget {

	protected AbstractBaseView<T> abstractParent;
	protected AbstractBaseView<T> parent;
	protected AsyncCallback<T> callBack;
	protected IAccounterGETServiceAsync rpcGetService;
	protected ClientCompany company;

	protected String stringID;

	protected IAccounterCRUDServiceAsync rpcDoSerivce;

	public AbstractBaseDialog(String title) {
		super(true);
		setText(title);
		setModal(true);
		initCompany();
		initRPCService();

	}

	public AbstractBaseDialog() {
		setModal(true);
	}

	public AbstractBaseDialog(AbstractBaseView<T> abstractParent) {
		super(true);
		this.abstractParent = abstractParent;
		initCompany();
		initRPCService();
	}

	public void addCallBack(AsyncCallback<T> callback) {
		this.callBack = callback;

	}

	protected void initCompany() {

		this.company = FinanceApplication.getCompany();

	}

	protected void initRPCService() {
		this.rpcGetService = FinanceApplication.createGETService();
		this.rpcDoSerivce = FinanceApplication.createCRUDService();

	}

	public Object getGridColumnValue(IsSerializable obj, int index) {
		return null;
	}

	public String getStringID() {
		return this.stringID;
	}

	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	// }

	protected boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		return true;
	}

}
