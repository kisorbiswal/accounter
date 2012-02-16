package com.vimukti.accounter.web.client.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;

public class ImportView extends AbstractBaseView {

	private VerticalPanel importerMatchingPanel;

	private List<Importer<?>> importerLines = new ArrayList<Importer<?>>();

	private Map<String, Field<?>> fields = new HashMap<String, Field<?>>();

	private VerticalPanel matchedPanel;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	private void createControls() {
		HorizontalPanel hPanel = new HorizontalPanel();

		this.importerMatchingPanel = new VerticalPanel();
		createStatementMatchingBody();

		hPanel.add(importerMatchingPanel);
		// Prepare MatchedPanel
		hPanel.add(this.matchedPanel);
	}

	private void createStatementMatchingBody() {
		if (importerMatchingPanel == null) {
			importerMatchingPanel = new VerticalPanel();
		}
		FlexTable table = new FlexTable();
		Map<String, String> map = getFirstLine();

		int rowIndex = 0;
		for (Entry<String, String> entry : map.entrySet()) {
			Label columnName = new Label();
			Label columnValue = new Label();
			ListBox importerFields = new ListBox();
			for (Field f : fields.values()) {
				importerFields.addItem(f.getName());
			}
			importerFields.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {

				}
			});
			table.setWidget(rowIndex, 0, columnName);
			table.setWidget(rowIndex, 2, columnValue);
			table.setWidget(rowIndex, 3, importerFields);
			rowIndex++;
		}
		importerMatchingPanel.add(table);

	}

	private Map<String, String> getFirstLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
