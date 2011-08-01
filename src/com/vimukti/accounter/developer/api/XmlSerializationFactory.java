package com.vimukti.accounter.developer.api;

import java.io.InputStream;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.vimukti.accounter.api.core.ApiResult;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class XmlSerializationFactory implements ApiSerializationFactory {

	private static XmlSerializationFactory instance;
	private XStream stream;

	private XmlSerializationFactory() {
		stream = new XStream();
		initializeStream();
	}

	private void initializeStream() {
		// TODO Auto-generated method stub

	}

	public static XmlSerializationFactory getInstance() {
		if (instance == null) {
			instance = new XmlSerializationFactory();
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
		return (List<IAccounterCore>) stream.fromXML(str);
	}

	@Override
	public String serializeList(List<? extends IAccounterCore> str)
			throws Exception {
		return stream.toXML(str);
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
