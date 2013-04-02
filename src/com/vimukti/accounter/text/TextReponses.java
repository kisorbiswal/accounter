package com.vimukti.accounter.text;

public class TextReponses {

	public static String alreadySignedUpResponse(String uniqueId) {
		return "You have already signup !! \n Your unique indentiy is "
				+ uniqueId;
	}

	public static String signupSuccssfullResponse(String uniqueId) {
		return "You have been signed up successfully, please use this email "
				+ uniqueId + " for furthus request";
	}

	public static String donnotHaveCompaniesYet() {
		return "You don't have companies yet, please create a company";
	}

	public static String notYetSignedUp() {
		return "You are not yet signed up. Please signup bu sending an email to "
				+ TextRequestProcessor.SIGNUP_EMAIL;
	}

	public static String internalErrorOccured() {
		return "Internal Error occured while processing your request";
	}
}
