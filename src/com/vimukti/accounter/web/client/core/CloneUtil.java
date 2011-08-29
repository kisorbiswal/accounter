package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

class CloneUtil {

	public static <T extends Cloneable2<T>> List<T> copyCloneList(List<T> originalList){
		if(originalList==null){
			return null;
		}
		List<T> list = new ArrayList<T>();
		for (T o : originalList) {
			list.add(o.clone(o));
		}
		return list;
	}
}
