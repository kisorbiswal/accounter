package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class InviteUserAction extends Action {

	InviteUserView view;

	public InviteUserAction(String text) {
		super(text);
		this.catagory = Accounter.constants().settings() + " > "
				+ Accounter.constants().users();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

//	@Override
//	public ParentCanvas getView() {
//		// NOTHING TO DO.
//		return null;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			view = new InviteUserView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, InviteUserAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHistoryToken() {
		return "inviteUser";
	}

}
