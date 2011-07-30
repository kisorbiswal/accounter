package com.vimukti.accounter.developer.api;

import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class XmlSerializationFactory implements ApiSerializationFactory {

	private XmlSerializationFactory instance;
	private XStream stream;

	private XmlSerializationFactory() {
		stream = new XStream();
		initializeStream();
	}

	private void initializeStream() {
		// TODO Auto-generated method stub

	}

	public XmlSerializationFactory getInstance() {
		if (instance == null) {
			instance = new XmlSerializationFactory();
		}
		return instance;
	}

	@Override
	public IAccounterCore deserialize(String str) throws Exception {
		return (IAccounterCore) stream.fromXML(str);
	}

	@Override
	public String serialize(IAccounterCore str) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IAccounterCore> deserializeList(String str) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serializeList(List<IAccounterCore> str) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
