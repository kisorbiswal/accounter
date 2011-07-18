package com.vimukti.comet.server;

import javax.servlet.http.HttpServletResponse;

public class Constants {
	static final String RESPONSE_CONTENT_TYPE = "text/html; charset=utf-8";

	static final int STATUS_CODE = HttpServletResponse.SC_OK;

	final static String MAXIMUM_BYTES_WRITTEN_INIT_PARAMETER = "maximum-bytes-written";

	final static String CONNECTION_TIME_OUT_INIT_PARAMETER = "connection-timeout";

	final static String DOCUMENT_START_HTML = "<html>\n<head>\n<script>\nwindow.parent.__cometOnConnect();\n</script>\n";

	final static String DOCUMENT_END_HTML = "</body>\n</html>\n";

	final static int BUFFER_SIZE = 512;

	// final static String SCRIPT_TAG_OPEN =
	// "<script>try{window.parent.__cometDispatch('";
	// final static String SCRIPT_TAG_CLOSE = "');}catch(e){}</script>";

	public static final int TERMINATE_COMET_SESSION = 0;

	public static final int OBJECT_PAYLOAD = TERMINATE_COMET_SESSION + 1;

	public static final int EXCEPTION_PAYLOAD = OBJECT_PAYLOAD + 1;

	public static final String XHR_SCRIPT_TAG_OPEN = "\n<script>p(";
	public static final String XHR_SCRIPT_TAG_CLOSE = ");</script>";

	public static final String XHR_START = "\r\n\r\n<html><head><script type='text/javascript'>\r\n window.onError = null;\r\nvar old_domain=document.domain; if(old_domain.match(/^(\\d{1,3}\\.){3}\\d{1,3}$/)) document.domain= old_domain;else{var domain_pieces = old_domain.split('.');document.domain=domain_pieces.slice(-3, domain_pieces.length).join('.'); }\r\n\r\nparent.Meteor.register(this);\r\n</script>\r\n</head>\r\n<body onload='try { parent.Meteor.reset(this) } catch (e) {}'>\r\n<!--......................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................-->\r\n\r\n";
	// "<!--.........................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................-->\r\ndemo\r\n"
	// ;
	public static final String IFRAME_START = "\r\n\r\n<html><head><script type='text/javascript'>\r\n window.onError = null;\r\nvar old_domain=document.domain; if(old_domain.match(/^(\\d{1,3}\\.){3}\\d{1,3}$/)) document.domain= old_domain;else{var domain_pieces = old_domain.split('.');document.domain=domain_pieces.slice(-3, domain_pieces.length).join('.'); }\r\n\r\nparent.Meteor.register(this);\r\n</script>\r\n</head>\r\n<body onload='try { parent.Meteor.reset(this) } catch (e) {}'>\r\n<!--......................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................-->\r\n\r\n";
	// "<html><head><script type='text/javascript'>\r\nwindow.onError = null;\r\nvar domainparts = document.domain.split('.');\r\ndocument.domain = domainparts[domainparts.length-2]+'.'+domainparts[domainparts.length-1];\r\nparent.Meteor.register(this);\r\n</script>\r\n</head>\r\n<body onload='try { parent.Meteor.reset(this) } catch (e) {}'>\r\n<!--......................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................-->\r\ndemo\r\n"
	// ;
	public static final String SMARTPOLL_OPEN = "<script>parent.Meteor.process(";

	public static final String SMARTPOLL_CLOSE = ");</script>";

	public static final String GWT_STREAM = "GWTSTREAM";
	public static final String JSON_STREAM = "JSONSTREAM";

	public static final int JSON_TYPE = EXCEPTION_PAYLOAD + 1;
}
