/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.ClickableSafeHtmlCell;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class Prepare1099MISCView extends AbstractBaseView {
	private String[] boxes;
	private int[] boxNums = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14 };
	private int totalNoOf1099Forms;
	private double totalAll1099Payments;
	private ListDataProvider<Client1099Form> listDataProvider;

	private DisclosurePanel disclosurePanel;
	private VerticalPanel setupPanel, amountPanel, preview1099panel,
			companyAddressPanel, einNumPanel;
	private HorizontalPanel companyInfopanel, setVendorsPanel,
			setAccountsPanel;
	private Label setVendor, addAccount;
	private Label noOf1099FormsLabel;
	private Label companyInfoText, einInfoText;
	private AmountLabel total1099AmountLabel;
	private DynamicForm amountForm;
	private CellTable<Client1099Form> cellTable;
	private SelectCombo selectComboItem;
	private ArrayList<String> arrayList;
	private HTML changeVendorHtml, changeAccountsHtml, companyInfoHtml,
			einInfoHtml;
	VerticalPanel mainPanel;

	int horizontalValue;
	int verticalValue;

	@Override
	public void init() {
		AccounterConstants c = Accounter.constants();
		boxes = new String[] { c.box1() + "\n(600.00)", c.box2() + "\n(10.00)",
				c.box3() + "\n(600.00)", c.box4() + "\n(0.00)",
				c.box5() + "\n(0.00)", c.box6() + "\n(600.00)",
				c.box7() + "\n(600.00)", c.box8() + "\n(10.00)",
				c.box9() + "\n(5000.00)", c.box10() + "\n(600.00)",
				c.box13() + "\n(0.00)", c.box14() + "\n(0.00)" };

		this.createControl();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	public void createControl() {
		totalAll1099Payments = 0;

		mainPanel = new VerticalPanel();

		mainPanel.add(getSetupPanel());
		mainPanel.add(getPreview1099());
		mainPanel.add(getPrintSetUp());
		mainPanel.add(getEndButtons());

		this.add(mainPanel);

	}

	ClientVendor vendor = null;

	public CellTable<Client1099Form> get1099InformationGrid() {

		cellTable = new CellTable<Client1099Form>();
		cellTable.setWidth("100%");

		CheckboxCell checkboxCell = new CheckboxCell();
		Column<Client1099Form, Boolean> checkBoxColumn = new Column<Client1099Form, Boolean>(
				checkboxCell) {

			@Override
			public Boolean getValue(Client1099Form object) {
				return true;
			}
		};

		ClickableSafeHtmlCell informationLink = new ClickableSafeHtmlCell();

		Column<Client1099Form, SafeHtml> informationColumn = new Column<Client1099Form, SafeHtml>(
				informationLink) {

			@Override
			public SafeHtml getValue(Client1099Form object) {
				vendor = object.getVendor();
				return object.getVendorInformation();
			}
		};
		informationColumn
				.setFieldUpdater(new FieldUpdater<Client1099Form, SafeHtml>() {

					@Override
					public void update(int index, Client1099Form object,
							SafeHtml value) {
						ActionFactory.getNewVendorAction().run(vendor, false);
					}
				});

		Column<Client1099Form, String> total1099PaymentsCell = new Column<Client1099Form, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(Client1099Form object) {
				double total1099Payments = object.getTotal1099Payments();
				return total1099Payments != 0 ? "" + total1099Payments : "";
			}
		};

		Column<Client1099Form, String> totalAllPaymentsCell = new Column<Client1099Form, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(Client1099Form object) {
				double totalAllPayments = object.getTotalAllPayments();
				return totalAllPayments != 0 ? "" + totalAllPayments : "";
			}
		};

		cellTable.addColumn(checkBoxColumn, Accounter.constants().select());
		cellTable.addColumn(informationColumn, Accounter.messages()
				.vendorInformation(Global.get().Vendor()));
		addBoxColumnsToCellTable(cellTable);
		cellTable.addColumn(total1099PaymentsCell, Accounter.constants()
				.total1099Payments());
		cellTable.addColumn(totalAllPaymentsCell, Accounter.constants()
				.totalAllPayments());

		return cellTable;
	}

	private ClientAccount getAccountByBoxNum(int i) {
		ArrayList<ClientAccount> activeAccounts = getCompany()
				.getActiveAccounts();
		for (ClientAccount clientAccount : activeAccounts) {
			if (clientAccount.getBoxNumber() == i) {
				return clientAccount;
			}
		}
		return null;
	}

	private void addBoxColumnsToCellTable(CellTable<Client1099Form> cellTable) {
		for (int i = 0; i < boxNums.length; i++) {
			final int num = i;
			ClientAccount clientAccount = getAccountByBoxNum(boxNums[i]);
			if (clientAccount != null) {
				Column<Client1099Form, String> boxCell = new Column<Client1099Form, String>(
						new ClickableTextCell()) {

					@Override
					public String getValue(Client1099Form object) {
						double box = object.getBox(boxNums[num]);
						return box != 0 ? "" + box : "";
					}
				};
				cellTable.addColumn(boxCell, boxes[i]);
			}
		}
	}

	private DisclosurePanel getSetupPanel() {

		disclosurePanel = new DisclosurePanel(Accounter.messages()
				.setupVendorsAndAccounts(Global.get().Vendor(),
						Global.get().Account()));
		disclosurePanel.setOpen(true);

		setVendorsPanel = new HorizontalPanel();
		setAccountsPanel = new HorizontalPanel();

		setVendor = new Label(getSelectedVendorsNum() + " "
				+ Accounter.messages().vendorsSelected(Global.get().Vendor()));

		addAccount = new Label(getSelectedAccountsNum() + " "
				+ Accounter.messages().accountsSelected(Global.get().Account()));

		changeVendorHtml = new HTML(Accounter.messages().changeVendors(
				Global.get().Vendor()));
		changeVendorHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final SelectItemsTo1099Dialog<ClientVendor> selectVendorsTo1099Dialog = new SelectItemsTo1099Dialog<ClientVendor>(
						Accounter.messages().vendorsSelected(
								Global.get().Vendor()),
						Accounter.messages().SelectVendorsToTrack1099(
								Global.get().Vendor()));
				ArrayList<ClientVendor> vendors = getCompany().getVendors();
				ArrayList<ClientVendor> tempSelectedItemsList = new ArrayList<ClientVendor>();

				tempSelectedItemsList.addAll(Utility.filteredList(
						new ListFilter<ClientVendor>() {

							@Override
							public boolean filter(ClientVendor e) {
								return e.isTrackPaymentsFor1099();
							}
						}, vendors));
				selectVendorsTo1099Dialog
						.setSelectedItems(tempSelectedItemsList);

				selectVendorsTo1099Dialog.setAvailableItems(getCompany()
						.getVendors());

				selectVendorsTo1099Dialog
						.setCallBack(new ActionCallback<ArrayList<ClientVendor>>() {

							@Override
							public void actionResult(
									ArrayList<ClientVendor> result) {
								for (ClientVendor vendor : selectVendorsTo1099Dialog.tempSelectedItemsList) {
									vendor.setTrackPaymentsFor1099(true);
									saveOrUpdate(vendor);
								}
								for (ClientVendor vendor : selectVendorsTo1099Dialog.tempAvailItemsList) {
									vendor.setTrackPaymentsFor1099(false);
									saveOrUpdate(vendor);
								}

							}
						});
				selectVendorsTo1099Dialog.show();
			}
		});

		changeAccountsHtml = new HTML(Accounter.messages().changeAccounts(
				Global.get().Account()));
		changeAccountsHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String assignAccounts = Accounter.messages().assignAccounts(
						Global.get().Account());
				AssignAccountsTo1099Dialog assignAccountsTo1099Dialog = new AssignAccountsTo1099Dialog(
						assignAccounts, assignAccounts);
				assignAccountsTo1099Dialog
						.setCallback(new ActionCallback<int[]>() {

							@Override
							public void actionResult(int[] result) {
								refreshView();
							}
						});
				assignAccountsTo1099Dialog.show();
			}
		});

		setVendorsPanel.add(setVendor);
		setVendorsPanel.add(changeVendorHtml);

		setAccountsPanel.add(addAccount);
		setAccountsPanel.add(changeAccountsHtml);

		Label infoLable = new Label(
				"Before paying vendors this year, select vendors and assign accounts. These setup tasks ensure that the forms you file next year will be correct. If you have already made vendor payments this year, you may need to revise them to assign them to the proper accounts.");

		infoLable.setWordWrap(true);
		infoLable.setHorizontalAlignment(ALIGN_JUSTIFY);

		setupPanel = new VerticalPanel();
		setupPanel.setSize("100%", "100%");
		setupPanel.add(infoLable);
		setupPanel.add(setVendorsPanel);
		setupPanel.add(setAccountsPanel);
		disclosurePanel.add(setupPanel);

		return disclosurePanel;
	}

	private int getSelectedVendorsNum() {
		ArrayList<ClientVendor> list = new ArrayList<ClientVendor>();
		for (ClientVendor vendor : getCompany().getActiveVendors()) {
			if (vendor.isTrackPaymentsFor1099())
				list.add(vendor);
		}
		return list.size();
	}

	private int getSelectedAccountsNum() {
		ArrayList<ClientAccount> list = new ArrayList<ClientAccount>();
		for (ClientAccount clientAccount : getCompany().getAccounts()) {
			if (clientAccount.getBoxNumber() != 0)
				list.add(clientAccount);
		}
		return list.size();
	}

	private void refreshView() {
		this.remove(mainPanel);
		createControl();
	}

	private VerticalPanel getPreview1099() {
		Label label = new Label(Accounter.constants().preview1099Informaion());

		companyAddressPanel = new VerticalPanel();
		companyInfoText = new Label(Accounter.constants().companyInformation());
		final ClientAddress address = getCompany().getRegisteredAddress();
		if (address != null) {
			SafeHtml safeHtml = new SafeHtml() {

				@Override
				public String asString() {
					return address.getAddressString();
				}
			};
			companyInfoHtml = new HTML(safeHtml);
			companyAddressPanel.add(companyInfoText);
			companyAddressPanel.add(companyInfoHtml);
		}

		einNumPanel = new VerticalPanel();
		einInfoText = new Label(Accounter.constants().ein());
		String ein = getCompany().getEin();
		if (ein != null) {
			einInfoHtml = new HTML(ein);
			einNumPanel.add(einInfoText);
			einNumPanel.add(einInfoHtml);
		}

		companyInfopanel = new HorizontalPanel();
		companyInfopanel.setSize("100%", "100%");
		companyInfopanel.add(companyAddressPanel);
		companyInfopanel.add(einNumPanel);

		amountPanel = new VerticalPanel();
		amountPanel.addStyleName("tax-form");

		amountForm = new DynamicForm();
		noOf1099FormsLabel = new Label(Accounter.constants()
				.totalNoOf1099Forms() + totalNoOf1099Forms);

		total1099AmountLabel = new AmountLabel(Accounter.constants()
				.totalAll1099Payments());
		total1099AmountLabel.setAmount(totalAll1099Payments);

		amountForm.setFields(total1099AmountLabel);

		amountPanel.add(noOf1099FormsLabel);
		amountPanel.add(amountForm);

		arrayList = new ArrayList<String>();
		arrayList.add(Accounter.messages().venodrsThatMeetThreshold(
				Global.get().Vendor()));
		arrayList.add(Accounter.messages().vendorsBelowThreshold(
				Global.get().Vendor()));
		arrayList.add(Accounter.messages()
				.non1099Vendors(Global.get().Vendor()));

		selectComboItem = new SelectCombo(Accounter.constants().show());
		selectComboItem.initCombo(arrayList);
		selectComboItem.setSelected(arrayList.get(0));
		selectComboItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						initializeList(selectComboItem.getSelectedValue());

					}
				});

		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setFields(selectComboItem);

		preview1099panel = new VerticalPanel();
		preview1099panel.setWidth("100%");
		preview1099panel.add(label);
		preview1099panel.add(companyInfopanel);
		preview1099panel.setHorizontalAlignment(ALIGN_RIGHT);
		preview1099panel.add(dynamicForm);
		preview1099panel.setHorizontalAlignment(ALIGN_LEFT);
		preview1099panel.add(get1099InformationGrid());
		preview1099panel.setHorizontalAlignment(ALIGN_RIGHT);
		preview1099panel.add(amountPanel);
		initializeList(arrayList.get(0));
		return preview1099panel;

	}

	int selected;

	protected void initializeList(String selectedValue) {
		selected = -1;
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i) == selectedValue) {
				selected = i;
				break;
			}
		}
		listDataProvider = new ListDataProvider<Client1099Form>();

		Accounter.get1099FormInformation(
				new AsyncCallback<ArrayList<Client1099Form>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<Client1099Form> result) {
						listDataProvider.getList().addAll(result);
						setTotalAmountFields(result);
					}
				}, selected);

		listDataProvider.addDataDisplay(cellTable);

	}

	protected void setTotalAmountFields(ArrayList<Client1099Form> result) {
		totalAll1099Payments = 0;
		totalNoOf1099Forms = 0;
		if (selected == 0) {
			totalNoOf1099Forms = result.size();

			for (Client1099Form client1099Form : result) {
				totalAll1099Payments += client1099Form.getTotal1099Payments();
			}
		}
		noOf1099FormsLabel.setText(Accounter.constants().totalNoOf1099Forms()
				+ totalNoOf1099Forms);
		total1099AmountLabel.setAmount(totalAll1099Payments);
	}

	private DisclosurePanel getPrintSetUp() {

		DisclosurePanel disclosurePanel = new DisclosurePanel(Accounter
				.constants().printAlignmentAndSetup());

		Button printSample = new Button("Print Sample");
		printSample.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				long vendorId = 0000;
				long objectID = 1;
				long brandingThemeID = 1;
				UIUtils.downloadMISCForm(objectID,
						ClientTransaction.TYPE_MISC_SAMPLE_FORM,
						brandingThemeID, vendorId, horizontalValue,
						verticalValue);
			}

		});
		Label blankLabel = new Label("Load empty paper");
		Label adjustLabel = new Label(
				" Enter adjustments to move text 1/100th of an inch.");

		Label sampleInfoLabel = new Label(
				" This will always print a sample form for you. This one is not for your original form.");

		Label sampleInfoLabel2 = new Label(
				" Place the sample form printed on the 1099-Form. Hold them to the light and check the alignment. If they are not matching adjust the alignemnt and print the sample again.");

		HTML verLabel = new HTML("<B> Vertical </B>");
		HTML horLabel = new HTML("<B> Horizantal </B>");

		Label infoLabel = new Label(
				"	Alignment adjustment values are saved when you click Print Sample (above), or Print (below).If you print forms on more than one printer, write down the alignment values for each.");

		// HTML alignmentSelected = new HTML("<B> Horizantal :</B>"
		// + horizontalValue + "<B> Vertical :</B>" + verticalValue);

		Grid advancedOptions = new Grid(8, 2);
		advancedOptions.setCellSpacing(6);
		advancedOptions.setWidget(0, 0, blankLabel);
		advancedOptions.setHTML(0, 1, "");
		advancedOptions.setWidget(1, 0, printSample);
		advancedOptions.setHTML(1, 1, "");
		advancedOptions.setWidget(2, 0, sampleInfoLabel);
		advancedOptions.setHTML(2, 1, "");
		advancedOptions.setWidget(3, 0, adjustLabel);
		advancedOptions.setHTML(3, 1, "");
		advancedOptions.setWidget(4, 0, verLabel);
		advancedOptions.setWidget(4, 1, getListBox(true, 1));
		advancedOptions.setWidget(5, 0, horLabel);
		advancedOptions.setWidget(5, 1, getListBox(true, 2));
		advancedOptions.setWidget(6, 0, sampleInfoLabel2);
		advancedOptions.setHTML(6, 1, "");
		advancedOptions.setWidget(7, 0, infoLabel);
		advancedOptions.setHTML(7, 1, "");

		disclosurePanel.add(advancedOptions);
		return disclosurePanel;
	}

	private HorizontalPanel getEndButtons() {

		Button print1099 = new Button(Accounter.constants().printOn1099Form());
		Button printInfo = new Button(Accounter.constants()
				.printOnInformationSheet());
		Button cancel = new Button(Accounter.constants().cancel());

		HorizontalPanel panel = new HorizontalPanel();
		panel.add(print1099);
		panel.add(printInfo);
		panel.add(cancel);

		print1099.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				for (Client1099Form element : listDataProvider.getList()) {
					long vendorId = element.getVendor().getID();

					long objectID = 1;
					long brandingThemeID = 1;
					UIUtils.downloadMISCForm(objectID,
							ClientTransaction.TYPE_MISC_FORM, brandingThemeID,
							vendorId, horizontalValue, verticalValue);
				}

			}
		});
		printInfo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancel();
			}
		});

		return panel;
	}

	ListBox getListBox(boolean dropdown, final int box) {
		final ListBox widget = new ListBox();
		widget.addStyleName("demo-ListBox");
		widget.addItem("-60");
		widget.addItem("-50");
		widget.addItem("-40");
		widget.addItem("-30");
		widget.addItem("-20");
		widget.addItem("-10");
		widget.addItem("0");
		widget.addItem("10");
		widget.addItem("20");
		widget.addItem("30");
		widget.addItem("40");
		widget.addItem("50");
		widget.addItem("60");
		if (!dropdown)
			widget.setVisibleItemCount(3);

		widget.setSelectedIndex(7);
		widget.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(
					com.google.gwt.event.dom.client.ChangeEvent event) {
				int index = widget.getSelectedIndex();
				if (box == 1)
					horizontalValue = Integer.parseInt(widget
							.getItemText(index));
				else
					verticalValue = Integer.parseInt(widget.getItemText(index));

			}
		});

		return widget;
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

}
