package com.vimukti.accounter.taxreturn;

import java.io.File;
import java.io.FileOutputStream;

import com.vimukti.accounter.taxreturn.core.GovTalkMessage;

public class Test {
	public static void main(String[] args) throws Exception {
		GovTalkMessage message = GovTalkMessageGenerator.getRequestMessage();// BodyValue
		// fillPoll("");//Acknw
		// fillDelete("");//Response
		FileOutputStream stream = new FileOutputStream(
				new File(
						"C:/Users/vimukti04/Desktop/accounter/HMRC-VAT/LTS3.10/HMRCTools/TestData/New folder",
						"vatrequest.xml"));
		message.toXML(stream);
	}
}
