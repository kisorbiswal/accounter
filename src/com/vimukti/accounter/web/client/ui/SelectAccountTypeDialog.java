package com.vimukti.accounter.web.client.ui;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.ViewConfiguration;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * 
 * @author Mandeep Singh
 * @modified by Ravi kiran.G
 * 
 */
@SuppressWarnings("unchecked")
public class SelectAccountTypeDialog extends BaseDialog {

	DynamicForm incomeAndExpenseForm;
	RadioGroupItem incomeAndExpenseRadioGroup;

	private LinkedHashMap<String, String> accountTypes;
	private List<Integer> options;
	@SuppressWarnings("unused")
	private ViewConfiguration configuration;
	private String defaultId;

	public SelectAccountTypeDialog(List<Integer> options,
			ViewConfiguration configuration) {
		super("Select Account Type", "Select an Account Type");
		this.options = options;
		this.configuration = configuration;
		createControls();
		center();
	}

	private void createControls() {
		setWidth("420");
		// setAutoSize(Boolean.TRUE);

		incomeAndExpenseForm = new DynamicForm();
		incomeAndExpenseForm.setIsGroup(true);
		incomeAndExpenseForm.setGroupTitle(Accounter.constants()
				.incomeAndExpenseAccounts());
		// incomeAndExpenseForm.setWrapItemTitles(false);
		// incomeAndExpenseForm.setItemLayout(FormLayoutType.ABSOLUTE);
		// incomeAndExpenseForm.setMargin(10);
		incomeAndExpenseForm.setWidth("100%");

		incomeAndExpenseRadioGroup = new RadioGroupItem();
		incomeAndExpenseRadioGroup.setShowTitle(false);
		// incomeAndExpenseRadioGroup.setTop(10);
		// incomeAndExpenseRadioGroup.setLeft(70);
		// incomeAndExpenseRadioGroup.setWidth("*");

		accountTypes = new LinkedHashMap<String, String>();
		if (options != null && options.size() != 0) {
			for (int type : options) {
				accountTypes.put(String.valueOf(type),
						" " + UIUtils.nbsp(Utility.getAccountTypeString(type))
								+ " ");
			}
			defaultId = String.valueOf(options.get(0));

		} else {
			for (int i = 0; i < UIUtils.accountTypes.length - 2; i++) {
				accountTypes
						.put(String.valueOf(UIUtils.accountTypes[i]),
								" "
										+ UIUtils.nbsp(Utility
												.getAccountTypeString(UIUtils.accountTypes[i]))
										+ " ");

			}
			defaultId = String.valueOf(UIUtils.accountTypes[0]);
		}

		incomeAndExpenseRadioGroup.setValueMap(accountTypes);
		incomeAndExpenseRadioGroup.setDefaultValue(defaultId);
		incomeAndExpenseForm.setFields(incomeAndExpenseRadioGroup);

		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				String val = incomeAndExpenseRadioGroup.getValue().toString();
				int type = Integer.parseInt(val);
				String typeName = Utility.getAccountTypeString(type);
				typeName = UIUtils.unbsp(typeName);

				@SuppressWarnings("unused")
				NewAccountView accountView = new NewAccountView();
				try {
					// FIX ME
					// UIUtils.setCanvas(accountView, configuration);
					return true;
				} catch (Throwable e) {
					Accounter.showError("Failed!!");
					e.printStackTrace();
				}
				return false;
			}

		});

		VerticalPanel MVLay = new VerticalPanel();
		MVLay.setWidth("420");
		MVLay.add(incomeAndExpenseForm);

		setBodyLayout(MVLay);

		headerLayout.setWidth("420");
		headerLayout.setHeight("100px");
		footerLayout.setWidth("420");
		// setAutoHeight();
		show();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected String getViewTitle() {
		return "Select Account Type";
	}
}
