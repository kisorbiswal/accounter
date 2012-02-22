package com.vimukti.accounter.web.client.imports;

import java.util.List;
import java.util.Map;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.Field;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ImportAction extends Action {

	private Map<String, List<String>> columnData;
	private int importType;
	private List<Field<?>> importerFields;
	private String fileID;

	public ImportAction(List<Field<?>> importerFields,
			Map<String, List<String>> data, int type, String fileID) {
		this.columnData = data;
		this.importType = type;
		this.importerFields = importerFields;
		this.fileID = fileID;
		this.catagory = messages.importFile();
	}

	@Override
	public String getText() {
		return messages.importFile();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				ImportView view = new ImportView(importType, fileID,
						importerFields, columnData);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ImportAction.this);
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "import";
	}

	@Override
	public String getHelpToken() {
		return "import";
	}

}
