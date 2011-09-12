package com.vimukti.accounter.web.client.externalization;

import com.google.gwt.i18n.client.Messages;

public interface AccounterErrorMessages extends Messages {
	String vendorInUse(String vendorString);

	String customerInUse(String customerString);
}
