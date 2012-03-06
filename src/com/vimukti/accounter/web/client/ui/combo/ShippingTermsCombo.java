package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShippingTermListDialog;

public class ShippingTermsCombo extends CustomCombo<ClientShippingTerms> {

	public ShippingTermsCombo(String title) {
		super(title,"ShippingTermsCombo");
		initCombo(getCompany().getShippingTerms());
	}

	public ShippingTermsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1,"ShippingTermsCombo");
		initCombo(getCompany().getShippingTerms());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.shippingTerms();
	}

	@Override
	protected String getDisplayName(ClientShippingTerms object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ShippingTermListDialog shippingTermDialog = new ShippingTermListDialog(
				"", "");
		// shippingTermDialog.addCallBack(createAddNewCallBack());
		shippingTermDialog.removeFromParent();
		shippingTermDialog
				.addCallBack(new AccounterAsyncCallback<ClientShippingTerms>() {
					@Override
					public void onException(AccounterException exception) {
						exception.printStackTrace();
						Accounter.showError(exception.getMessage());
					}

					@Override
					public void onResultSuccess(ClientShippingTerms result) {
						addItemThenfireEvent(result);
					}
				});
		shippingTermDialog.showAddEditTermDialog(null);
	}

	@Override
	protected String getColumnData(ClientShippingTerms object,  int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
