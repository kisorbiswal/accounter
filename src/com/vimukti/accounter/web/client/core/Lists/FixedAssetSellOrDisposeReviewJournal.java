package com.vimukti.accounter.web.client.core.Lists;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FixedAssetSellOrDisposeReviewJournal implements IsSerializable {

	Map<String, Double> disposalSummary;

	Map<String, Double> disposalJournal;

	private long id;

	public long getID() {
		return this.id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public Map<String, Double> getDisposalSummary() {
		return disposalSummary;
	}

	public void setDisposalSummary(Map<String, Double> disposalSummary) {
		this.disposalSummary = disposalSummary;
	}

	public Map<String, Double> getDisposalJournal() {
		return disposalJournal;
	}

	public void setDisposalJournal(Map<String, Double> disposalJournal) {
		this.disposalJournal = disposalJournal;
	}

}
