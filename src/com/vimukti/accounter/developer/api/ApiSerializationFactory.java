package com.vimukti.accounter.developer.api;

import java.util.List;

import com.vimukti.accounter.web.client.core.IAccounterCore;

public interface ApiSerializationFactory {
	IAccounterCore deserialize(String str) throws Exception;

	String serialize(IAccounterCore str) throws Exception;

	List<IAccounterCore> deserializeList(String str) throws Exception;

	String serializeList(List<IAccounterCore> str) throws Exception;
}
