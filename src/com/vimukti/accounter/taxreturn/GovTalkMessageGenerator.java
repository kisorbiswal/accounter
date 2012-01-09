package com.vimukti.accounter.taxreturn;

import java.util.List;

import com.vimukti.accounter.taxreturn.core.Authentication;
import com.vimukti.accounter.taxreturn.core.Body;
import com.vimukti.accounter.taxreturn.core.Channel;
import com.vimukti.accounter.taxreturn.core.ChannelRouting;
import com.vimukti.accounter.taxreturn.core.GatewayAdditions;
import com.vimukti.accounter.taxreturn.core.GovTalkMessage;
import com.vimukti.accounter.taxreturn.core.GovtTalkDetails;
import com.vimukti.accounter.taxreturn.core.Header;
import com.vimukti.accounter.taxreturn.core.IDAuthentication;
import com.vimukti.accounter.taxreturn.core.MessageDetails;
import com.vimukti.accounter.taxreturn.core.SenderDetails;

public class GovTalkMessageGenerator {
	public static GovTalkMessage getDeleteMessage(String correlationId) {
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		Header header = message.getHeader();

		MessageDetails messageDatails = header.getMessageDatails();
		messageDatails.setClazz("HMRC-VAT-DEC-TEST");
		messageDatails.setQualifier("request");
		messageDatails.setFunction("delete");
		messageDatails.setCorrelationID(correlationId);
		messageDatails.setResponseEndPoint(null);
		messageDatails.setTransformation("XML");
		messageDatails.setGatewayTest(null);
		messageDatails.setGatewayTimestamp(null);

		header.setSenderDatails(null);

		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
		govtTalkDetails.setGatewayAdditions(null);
		govtTalkDetails.setChannelRoutings(null);

		Body body = message.getBody();
		body.setiRenvelope(null);
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
		body.setiRenvelope(null);
		return message;
	}

	public static GovTalkMessage getRequestMessage(Body body) {
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		Header header = message.getHeader();

		MessageDetails messageDatails = header.getMessageDatails();
		messageDatails.setClazz("HMRC-VAT-DEC-TIL");
		messageDatails.setQualifier("request");
		messageDatails.setFunction("submit");
		messageDatails.setCorrelationID("");
		messageDatails.setResponseEndPoint(null);
		messageDatails.setTransformation("XML");
		messageDatails.setGatewayTest(null);
		messageDatails.setGatewayTimestamp(null);

		SenderDetails senderDatails = header.getSenderDatails();
		senderDatails.setEmailAddress("***REMOVED***");
		IDAuthentication iDAuthentication = senderDatails.getiDAuthentication();
		iDAuthentication.setSenderId("VATDEC059a01");
		List<Authentication> authentications = iDAuthentication
				.getAuthentications();
		Authentication authentication = new Authentication();
		authentication.setMethod("clear");
		// authentication.setRole("principal");
		authentication.setValue("***REMOVED***");
		authentication.setSignature(null);
		authentications.add(authentication);

		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
		GatewayAdditions gatewayAdditions = govtTalkDetails
				.getGatewayAdditions();
		gatewayAdditions.setType(null);
		gatewayAdditions.setValue(null);
		List<ChannelRouting> channelRoutings = govtTalkDetails
				.getChannelRoutings();
		ChannelRouting routing = new ChannelRouting();
		Channel channel = routing.getChannel();
		channel.setuRI("0147");
		channel.setProduct("SDS");
		channel.setVersion("2.02");
		channelRoutings.add(routing);

		message.setBody(body);
		return message;
	}
}
