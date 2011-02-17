package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section;

public interface ISectionHandler {
	@SuppressWarnings("unchecked")
	public void OnSectionAdd(Section section);

	@SuppressWarnings("unchecked")
	public void OnSectionEnd(Section section);

}
