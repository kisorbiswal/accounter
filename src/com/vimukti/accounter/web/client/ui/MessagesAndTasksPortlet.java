package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;

public class MessagesAndTasksPortlet extends Portlet {

	public MessagesAndTasksPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.messagesAndTasks(), messages
				.gotoTasksAndMessages());
	}

}
