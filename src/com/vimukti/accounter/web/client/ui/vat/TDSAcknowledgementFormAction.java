package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class TDSAcknowledgementFormAction extends Action {

	public TDSAcknowledgementFormAction() {
		super();
		this.catagory = "TDS";

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vatAdjustment();
	}

	@Override
	public void run() {
		TdsForm16ACreationDialogue dialog = new TdsForm16ACreationDialogue();
		dialog.show();
	}

	@Override
	public String getHistoryToken() {
		return "acknowledgementFormTDS";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

	@Override
	public String getText() {
		return "Chalan Details";
	}

}
