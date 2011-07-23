/**
 * 
 */
package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PaymentTermListDialog;

/**
 * @author vimukti16
 * 
 */
public class PaymentTermsCombo extends CustomCombo<ClientPaymentTerms> {

	/**
	 * @param title
	 */
	public PaymentTermsCombo(String title) {
		super(title);
		initCombo(Accounter.getCompany().getPaymentsTerms());
	}

	public PaymentTermsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		initCombo(Accounter.getCompany().getPaymentsTerms());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newPaymentTerms();
	}

	@Override
	protected String getDisplayName(ClientPaymentTerms object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAddNew() {
		PaymentTermListDialog paymentTermsDialog = new PaymentTermListDialog();
		paymentTermsDialog.hide();
		paymentTermsDialog.addCallBack(createAddNewCallBack());
		paymentTermsDialog.showAddEditTermDialog(null);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.PAYMENT_TERMS;
	}

	@Override
	protected String getColumnData(ClientPaymentTerms object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
