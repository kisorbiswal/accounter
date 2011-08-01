package com.vimukti.accounter.developer.api;

import java.io.InputStream;
import java.util.List;

import com.vimukti.accounter.api.core.ApiResult;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public interface ApiSerializationFactory {
	IAccounterCore deserialize(InputStream inputStream) throws Exception;

	String serialize(IAccounterCore str) throws Exception;

	List<IAccounterCore> deserializeList(String str) throws Exception;

//	String serializeList(List<IAccounterCore> str) throws Exception;

	String serialize(AccounterException ex) throws Exception;

	String serializeResult(ApiResult apiResult);

	String serializeList(List<? extends IAccounterCore> str) throws Exception;
	
	
}
