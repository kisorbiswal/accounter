package com.vimukti.accounter.web.client.ui.win8;

import com.vimukti.accounter.web.client.ui.core.URLLauncher;

public class Win8URLLauncher extends URLLauncher {

	public void launch(String url, boolean newTab) {
		launchURL(url);
	}

	protected native void launchURL(String url) /*-{
		var uri = new Windows.Foundation.Uri(url);
		Windows.System.Launcher.launchUriAsync(uri)
	}-*/;

}
