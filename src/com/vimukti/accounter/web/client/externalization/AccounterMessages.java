package com.vimukti.accounter.web.client.externalization;

import com.google.gwt.i18n.client.Messages;

public interface AccounterMessages extends Messages {
	public String userName(String loginUserName);

	public String failedTransaction(String transName);

	public String pleaseEnter(String itemName);

	public String pleaseEnterHTML(String title);

}
