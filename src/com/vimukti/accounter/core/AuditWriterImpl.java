package com.vimukti.accounter.core;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuditWriterImpl implements AuditWriter {

	@Override
	public AuditWriter put(String key, String value) {

		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public AuditWriter put(String key, boolean value) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public AuditWriter put(String key, int value) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public AuditWriter put(String key, double value) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public AuditWriter put(String key, Double value) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public AuditWriter put(String key,
			Collection<? extends IAccounterServerCore> value) {
		JSONArray array = new JSONArray();
		for (IAccounterServerCore a : value) {
			AuditWriterImpl impl = new AuditWriterImpl();
			try {
				a.writeAudit(impl);
				array.put(impl.getJSON());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		this.put(key, array);
		return this;
	}

	private AuditWriter put(String key, JSONArray array) {
		// TODO Auto-generated method stub
		return this;
	}

	private JSONObject getJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuditWriter gap() {
		// TODO Auto-generated method stub
		return this;
	}

}
