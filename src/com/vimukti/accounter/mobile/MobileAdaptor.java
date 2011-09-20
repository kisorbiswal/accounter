/**
 * 
 */
package com.vimukti.accounter.mobile;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface MobileAdaptor {

	enum AdaptorType {
		CHAT, APPLICATION
	};

	public UserMessage preProcess(MobileSession session, String userMessage)
			throws AccounterMobileException;

	public String postProcess(Result userMessage);
}
