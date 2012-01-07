package com.vimukti.accounter.taxreturn;

import java.io.FileOutputStream;

import com.vimukti.accounter.taxreturn.core.GovTalkMessage;
import com.vimukti.accounter.taxreturn.core.GovtTalkDetails;
import com.vimukti.accounter.taxreturn.core.Header;
import com.vimukti.accounter.taxreturn.core.MessageDetails;
import com.vimukti.accounter.taxreturn.core.SenderDetails;

public class Test {
	public static void main(String[] args) throws Exception {
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		fill(govtTalkDetails);
		Header header = message.getHeader();
		fill(header);
		FileOutputStream stream = new FileOutputStream("example.xml");
		message.toXML(stream, false);
	}

	private static void fill(Header header) {
		MessageDetails messageDatails = header.getMessageDatails();
		SenderDetails senderDatails = header.getSenderDatails();
	}

	private static void fill(GovtTalkDetails govtTalkDetails) {
		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
	}
}
