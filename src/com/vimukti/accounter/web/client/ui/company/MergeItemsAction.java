package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.ItemMergeDialog;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MergeItemsAction extends Action {

	public MergeItemsAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		ItemMergeDialog dialog = new ItemMergeDialog(
				messages.mergeItems(), messages.itemDescription());

		dialog.show();
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
		return "merge_item";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
