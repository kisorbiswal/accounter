/**
 * 
 */
package com.vimukti.accounter.mobile;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileApplicationAdaptor implements MobileAdaptor {

	public static MobileAdaptor INSTANCE = new MobileApplicationAdaptor();

	@Override
	public UserMessage preProcess(MobileSession session, String userMessage,
			String networkId, int networkType) throws AccounterMobileException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String postProcess(Result userMessage) {
		// TODO Auto-generated method stub
		return null;
	}

}
