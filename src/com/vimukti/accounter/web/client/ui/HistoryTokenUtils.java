/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.History;

/**
 * @author K.Sandeep Sagar
 * 
 */
public class HistoryTokenUtils {


	public static String getTokenWithID(String historyToken,
			IAccounterCore object) {
		StringBuffer buffer = new StringBuffer(historyToken);
		if (object != null) {
			buffer.append('?');
			buffer.append(object.getObjectType().toString().toLowerCase());
			buffer.append(':');
			buffer.append(object.getID());
		}
		return buffer.toString();
	}

	public static void setPresentToken(Action action, IAccounterCore obj) {
		MainFinanceWindow.oldToken = com.google.gwt.user.client.History
				.getToken();
		com.google.gwt.user.client.History.newItem(getTokenWithID(
				action.getHistoryToken(), obj));
	}
}
