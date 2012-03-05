package com.vimukti.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreateClassDialog;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ClassTable extends AbstractDropDownTable<ClientAccounterClass> {

	public ClassTable() {
		super(getClasses());
	}

	private static List<ClientAccounterClass> getClasses() {
		return Accounter.getCompany().getAccounterClasses();
	}

	@Override
	public List<ClientAccounterClass> getTotalRowsData() {
		return getClasses();
	}

	@Override
	protected ClientAccounterClass getAddNewRow() {
		ClientAccounterClass clientAccounterClass = new ClientAccounterClass();
		clientAccounterClass.setClassName(messages.comboDefaultAddNew("Class"));
		return clientAccounterClass;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientAccounterClass> column = new TextColumn<ClientAccounterClass>() {

			@Override
			public String getValue(ClientAccounterClass object) {
				return getDisplayName(object);
			}
		};
		this.addColumn(column);
		this.setColumnWidth(column, "100px");
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	private String getDisplayName(ClientAccounterClass code) {
		String name = code.getName();
		if (name == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(name);
		if (name.equals(messages.comboDefaultAddNew("Class"))) {
			return result.toString();
		}
		return name;
	}

	@Override
	protected boolean filter(ClientAccounterClass t, String string) {
		return getDisplayName(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientAccounterClass value) {
		return getDisplayName(value);
	}

	@Override
	protected void addNewItem(String text) {
		addNewItem("");
	}

	@Override
	protected void addNewItem() {
		if (Accounter.getCompany().getPreferences().isClassTrackingEnabled()) {
			CreateClassDialog classDialog = new CreateClassDialog(null,
					messages.createClass(), "");
			classDialog
					.addSuccessCallback(new ValueCallBack<ClientAccounterClass>() {

						@Override
						public void execute(
								final ClientAccounterClass accounterClass) {
							Accounter.createCRUDService().create(
									accounterClass, new AsyncCallback<Long>() {

										@Override
										public void onSuccess(Long result) {
											accounterClass.setID(result);
											Accounter
													.getCompany()
													.processUpdateOrCreateObject(
															accounterClass);
											selectRow(accounterClass);
										}

										@Override
										public void onFailure(Throwable caught) {
											caught.printStackTrace();
										}
									});
						}
					});
		}
	}

	@Override
	protected Class<?> getType() {
		return ClientAccounterClass.class;
	}
}
