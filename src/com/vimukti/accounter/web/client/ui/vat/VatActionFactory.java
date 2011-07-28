package com.vimukti.accounter.web.client.ui.vat;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.vimukti.accounter.web.client.ui.core.AbstractActionFactory;
import com.vimukti.accounter.web.client.ui.core.Action;

public class VatActionFactory extends AbstractActionFactory {
	public static NewVatItemAction getNewVatItemAction() {
		String flag;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			flag = actionsConstants.newVATItem();
		else
			flag = actionsConstants.newTaxItem();
		return new NewVatItemAction(flag);
	}

	public static FileVatAction getFileVatAction() {
		return new FileVatAction(actionsConstants.fileVAT());
	}

	public static AdjustTAXAction getAdjustTaxAction() {
		return new AdjustTAXAction(actionsConstants.VATAdjustment());
	}

	public static VatItemListAction getVatItemListAction() {
		return new VatItemListAction(actionsConstants.VATItemList());
	}

	public static VatGroupAction getVatGroupAction() {
		return new VatGroupAction(actionsConstants.newVATGroup());
	}

	public static Action getTAXCodeListAction() {
		return new ManageTAXCodesListAction(actionsConstants.VATCodeList());
	}

	public static Action getNewTAXCodeAction() {
		return new NewTAXCodeAction(actionsConstants.newVATCode());
	}

	public static Action getCreateTaxesAction() {
		return new CreateTaxesAction(actionsConstants.createTaxes());
	}

	// public static Action getManageVATCodeAction() {
	// return new ManageVATCodeAction("Manage VAT");
	// }

	public static NewTAXAgencyAction getNewTAXAgencyAction() {
		return new NewTAXAgencyAction(actionsConstants.newVATAgency());
	}

	public static ManageVATGroupListAction getManageVATGroupListAction() {
		return new ManageVATGroupListAction(actionsConstants.VATGroupList());
	}

	public static AdjustTAXAction getVatAdjustmentAction() {
		return new AdjustTAXAction(Accounter.constants().VATAdjustment());

	}

	public static PayVATAction getpayVATAction() {
		return new PayVATAction(Accounter.constants().payVAT());

	}

	public static Action getreceiveVATAction() {
		return new ReceiveVATAction(Accounter.constants().receiveVAT());
	}
}