package com.vimukti.accounter.web.client.ui.company;


import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ClassMergeDialog;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MergeClassAction extends Action {

	public MergeClassAction() {
		super();
	}

	@Override
	public void run() {
		if (Accounter.hasPermission(Features.MERGING)) {
			ClassMergeDialog classMergeDialog = new ClassMergeDialog(
					messages.mergeClasses(),
					messages.mergeClassDescription());

			classMergeDialog.show();
		} else {
			if (!isCalledFromHistory) {
				Accounter.showSubscriptionWarning();
			}
		}

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.MERGECLASS;
	}

	@Override
	public String getHelpToken() {
		return null;
	}

	@Override
	public String getText() {
		return messages.mergeClass();
	}

}
