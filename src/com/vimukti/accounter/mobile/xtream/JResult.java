package com.vimukti.accounter.mobile.xtream;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.ResultList;

public class JResult {
	List<Object> resultParts = new ArrayList<Object>();

	public void addAll(List<Object> resultParts) {
		for (Object object : resultParts) {
			if (object instanceof String) {
				this.resultParts.add(object);
			} else if (object instanceof ResultList) {
				JResultList jResultList = new JResultList();
				jResultList.addAll((ResultList) object);
				this.resultParts.add(jResultList);
			} else if (object instanceof CommandList) {
				JCommandList jResultList = new JCommandList();
				jResultList.addAll((CommandList) object);
				this.resultParts.add(jResultList);
			}
		}
	}
}
