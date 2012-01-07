package com.vimukti.accounter.taxreturn;

import java.io.FileOutputStream;

import com.vimukti.accounter.taxreturn.core.Body;
import com.vimukti.accounter.taxreturn.core.GatewayAdditions;
import com.vimukti.accounter.taxreturn.core.GovTalkMessage;
import com.vimukti.accounter.taxreturn.core.GovtTalkDetails;
import com.vimukti.accounter.taxreturn.core.Header;
import com.vimukti.accounter.taxreturn.core.MessageDetails;
import com.vimukti.accounter.taxreturn.core.SenderDetails;

public class Test {
	public static void main(String[] args) throws Exception {
		GovTalkMessage message = getRequestMessage("Will see later");// BodyValue
		// fillPoll("");//Acknw
		// fillDelete("");//Response
		FileOutputStream stream = new FileOutputStream("vatrequest.xml");
		message.toXML(stream);
	}

	public static GovTalkMessage getDeleteMessage(String correlationId) {
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		Header header = message.getHeader();

		MessageDetails messageDatails = header.getMessageDatails();
		messageDatails.setClazz("MOSWTSC2");
		messageDatails.setQualifier("request");
		messageDatails.setFunction("delete");
		messageDatails.setCorrelationID(correlationId);
		messageDatails.setResponseEndPoint(null);
		messageDatails.setTransformation("XML");
		messageDatails.setGatewayTest(null);
		messageDatails.setGatewayTimestamp("");

		header.setSenderDatails(null);

		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
		govtTalkDetails.setGatewayAdditions(null);

		Body body = message.getBody();
		body.setValue(null);
		return message;
	}

	public static GovTalkMessage getPollMessage(String correlationId) {
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		Header header = message.getHeader();

		MessageDetails messageDatails = header.getMessageDatails();
		messageDatails.setClazz("MOSWTSC2");
		messageDatails.setQualifier("poll");
		messageDatails.setFunction("submit");
		messageDatails.setCorrelationID(correlationId);
		messageDatails.setResponseEndPoint(null);
		messageDatails.setTransformation("XML");
		messageDatails.setGatewayTest(null);
		messageDatails.setGatewayTimestamp("");

		header.setSenderDatails(null);

		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
		govtTalkDetails.setGatewayAdditions(null);

		Body body = message.getBody();
		body.setValue(null);
		return message;
	}

	public static GovTalkMessage getRequestMessage(String bodyValue) {
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		Header header = message.getHeader();

		MessageDetails messageDatails = header.getMessageDatails();
		messageDatails.setClazz("MOSWTSC2");
		messageDatails.setQualifier("request");
		messageDatails.setFunction("submit");
		messageDatails.setCorrelationID("");
		messageDatails.setResponseEndPoint(null);
		messageDatails.setTransformation("XML");
		messageDatails.setGatewayTest(1);
		messageDatails.setGatewayTimestamp("");

		SenderDetails senderDatails = header.getSenderDatails();

		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
		GatewayAdditions gatewayAdditions = govtTalkDetails
				.getGatewayAdditions();
		gatewayAdditions.setType(null);
		gatewayAdditions.setValue(null);

		Body body = message.getBody();
		body.setValue(bodyValue);
		return message;
	}
}
