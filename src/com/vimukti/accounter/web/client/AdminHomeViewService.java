package com.vimukti.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.admin.client.ClientAdminUser;

public interface AdminHomeViewService extends RemoteService {

	public ArrayList<ClientAdminUser> getAdminUsersList();

}
