package com.vimukti.accounter.developer.api;

import java.io.InputStream;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.vimukti.accounter.api.core.ApiResult;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class ApiSerializationFactory {
	private static XStream stream;

	public ApiSerializationFactory(boolean isJson) {
		if (isJson) {
			stream = new XStream(new JettisonMappedXmlDriver());
			stream.setMode(XStream.NO_REFERENCES);
		} else {
			stream = new XStream();
		}
		initializeStream();
	}

	private void initializeStream() {
		// TODO Auto-generated method stub

	}

	IAccounterCore deserialize(InputStream inputStream) throws Exception {
		return (IAccounterCore) stream.fromXML(inputStream);
	}

	String serialize(IAccounterCore str) throws Exception {
		return stream.toXML(str);
	}

	List<IAccounterCore> deserializeList(String str) throws Exception {
		// TODO
		return null;
	}

	String serialize(AccounterException ex) throws Exception {
		return stream.toXML(ex);
	}

	String serializeResult(ApiResult apiResult) {
		return stream.toXML(apiResult);
	}

	String serializeList(List<? extends IAccounterCore> str) throws Exception {
		return stream.toXML(str);
	}

	String serializeReportsList(List<? extends BaseReport> list) {
		return stream.toXML(list);
	}

	String serializeTransacHistCustomerList(List<ClientCustomer> list) {
		return stream.toXML(list);
	}

	String serializeTransacHistVendorList(List<ClientVendor> list) {
		return stream.toXML(list);
	}

	String serializeMinAndMaxTrasacDate(List<ClientFinanceDate> list) {
		return stream.toXML(list);
	}

	String serializeDateList(List<ClientFinanceDate> list) {
		return stream.toXML(list);
	}

}
