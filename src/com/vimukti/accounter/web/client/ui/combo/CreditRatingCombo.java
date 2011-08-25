package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.ui.CreditRatingListDialog;

public class CreditRatingCombo extends CustomCombo<ClientCreditRating> {

	public CreditRatingCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newCreditRating();
	}

	@Override
	protected String getDisplayName(ClientCreditRating object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		CreditRatingListDialog creditRatingDialog = new CreditRatingListDialog(
				"", "");
		creditRatingDialog.hide();
		creditRatingDialog.addCallBack(createAddNewCallBack());
		creditRatingDialog.showAddEditGroupDialog(null);
	}

	@Override
	protected String getColumnData(ClientCreditRating object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
