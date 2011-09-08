package com.vimukti.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.admin.client.ClientAdminUser;

public interface AdminHomeViewServiceAsync {
	public void getAdminUsersList(
			AsyncCallback<ArrayList<ClientAdminUser>> callBack);
}
