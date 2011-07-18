package com.vimukti.comet.client;

public class Base64Decoder {
	
	public static native String base64decode(final String data) /*-{

	var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	var out = "", c1, c2, c3, e1, e2, e3, e4;
	for (var i = 0; i < data.length; ) {
	e1 = tab.indexOf(data.charAt(i++));
	e2 = tab.indexOf(data.charAt(i++));
	e3 = tab.indexOf(data.charAt(i++));
	e4 = tab.indexOf(data.charAt(i++));
	c1 = (e1 << 2) + (e2 >> 4);
	c2 = ((e2 & 15) << 4) + (e3 >> 2);
	c3 = ((e3 & 3) << 6) + e4;
	out += String.fromCharCode(c1);
	if (e3 != 64)
	out += String.fromCharCode(c2);
	if (e4 != 64)
	out += String.fromCharCode(c3);
	}
	return out;

	}-*/;

}
