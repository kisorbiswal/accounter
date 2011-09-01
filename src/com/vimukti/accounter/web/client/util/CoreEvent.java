package com.vimukti.accounter.web.client.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;

public class CoreEvent<T> extends GwtEvent<CoreEventHandler<T>> {

	@SuppressWarnings("rawtypes")
	private static final Map map = new HashMap();

	private ChangeType type;
	private T obj;

	public CoreEvent(ChangeType type, T core) {
		this.type = type;
		obj = core;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Type<CoreEventHandler<T>> getAssociatedType() {
		Type<CoreEventHandler<T>> type = (Type<CoreEventHandler<T>>) map
				.get(obj.getClass());
		if (type == null) {
			type = new Type<CoreEventHandler<T>>();
			map.put(obj.getClass(), type);
		}
		return type;
	}

	@Override
	protected void dispatch(CoreEventHandler<T> handler) {
		switch (type) {
		case ADD:
			handler.onAdd(obj);
		case CHANGE:
			handler.onChange(obj);
			break;
		case DELETE:
			handler.onDelete(obj);
			break;
		}
	}
}
