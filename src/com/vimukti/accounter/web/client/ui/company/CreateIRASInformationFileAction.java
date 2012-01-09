package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class CreateIRASInformationFileAction extends Action {

	@Override
	public String getText() {
		return messages.generateIrasAuditFile();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				CreateIRASInformationFileDialog dialog = new CreateIRASInformationFileDialog();
				dialog.show();
			}

		});
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "createGSTIncormationFile";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
