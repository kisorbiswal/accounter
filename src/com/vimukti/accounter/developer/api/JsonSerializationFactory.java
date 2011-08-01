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

public class JsonSerializationFactory implements ApiSerializationFactory {
	private static JsonSerializationFactory instance;
	private XStream stream;

	private JsonSerializationFactory() {
		stream = new XStream(new JettisonMappedXmlDriver());
		stream.setMode(XStream.NO_REFERENCES);
		initializeStream();
	}

	private void initializeStream() {
		// TODO Auto-generated method stub

	}

	public static JsonSerializationFactory getInstance() {
		if (instance == null) {
			instance = new JsonSerializationFactory();
		}
		return instance;
	}

	@Override
	public IAccounterCore deserialize(InputStream inputStream) throws Exception {
		return (IAccounterCore) stream.fromXML(inputStream);
	}

	@Override
	public String serialize(IAccounterCore str) throws Exception {
		return stream.toXML(str);
	}

	@Override
	public List<IAccounterCore> deserializeList(String str) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(AccounterException ex) throws Exception {
		return stream.toXML(ex);
	}

	@Override
	public String serializeResult(ApiResult apiResult) {
		return stream.toXML(apiResult);
	}

	@Override
	public String serializeList(List<? extends IAccounterCore> str)
			throws Exception {
		return stream.toXML(str);
	}

	@Override
	public String serializeReportsList(List<? extends BaseReport> list) {
		return stream.toXML(list);
	}

	@Override
	public String serializeTransacHistCustomerList(List<ClientCustomer> list) {
		return stream.toXML(list);
	}

	@Override
	public String serializeTransacHistVendorList(List<ClientVendor> list) {
		return stream.toXML(list);
	}

	@Override
	public String serializeMinAndMaxTrasacDate(List<ClientFinanceDate> list) {
		return stream.toXML(list);
	}

	@Override
	public String serializeDateList(List<ClientFinanceDate> list) {
		return stream.toXML(list);
	}

}
