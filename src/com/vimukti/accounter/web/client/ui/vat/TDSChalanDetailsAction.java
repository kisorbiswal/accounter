package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SelectChallanTypeDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class TDSChalanDetailsAction extends Action<ClientTDSChalanDetail> {

	private int type;

	public TDSChalanDetailsAction() {
		super();
		this.catagory = messages.tds();

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vatAdjustment();
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final ClientTDSChalanDetail data,
			final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				if (type == 0 && data == null) {
					SelectChallanTypeDialog dialog = new SelectChallanTypeDialog();
					dialog.setDependent(isDependent);
					dialog.setCallback(getCallback());
					dialog.show();
				} else {
					if (data != null) {
						type = data.getFormType();
					}
					TDSChalanDetailsView view = new TDSChalanDetailsView(type);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, TDSChalanDetailsAction.this);

				}

			}

		});
	}

	@Override
	public String getHistoryToken() {
		return "chalanDetails";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

	@Override
	public String getText() {
		return messages.challanDetails();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
