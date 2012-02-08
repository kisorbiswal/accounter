package com.vimukti.accounter.web.client.ui;

public class JNSI {
	public static native boolean getIsAdmin(String jsVar)/*-{

		var isAdmin = eval('$wnd.' + jsVar);

		if (isAdmin != null) {
			return isAdmin;
		} else {
			return false;
		}
	}-*/;

	public static native void log(String message)/*-{
		if (window.console != undefined) {
			console.log(message);
		}
	}-*/;

	public static native String getCalcultedAmount(String str)/*-{
		str = str.replace(/[^0-9\+\-\.x\*\/]/gi, '');
		str = str.replace(/^[^0-9\.]+/gi, '');
		str = str.replace(/[^0-9\.]+$/gi, '');
		str = str.replace(/x/gi, '*');
		str = str.replace(/([\-\+\*\/])+/gi, '$1');
		return String(eval(str));
	}-*/;

	public static native Object getFeatures()/*-{
		return eval('$wnd.features');
	}-*/;

}
