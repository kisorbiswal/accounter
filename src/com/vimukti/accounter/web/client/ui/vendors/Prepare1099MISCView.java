/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

public class Prepare1099MISCView extends AbstractBaseView {
	private String[] boxes;
	private int[] boxNums = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14 };
	private int totalNoOf1099Forms;
	private double totalAll1099Payments;
	private ListDataProvider<Client1099Form> listDataProvider;

	private DisclosurePanel disclosurePanel;
	private VerticalPanel setVendorpanel, amountPanel, preview1099panel;
	private HorizontalPanel companyInfopanel;
	private Button setVendor, addAccount;
	private Label companyInfo, einInfo, noOf1099FormsLabel;
	private TextAreaItem companyInfoText, einInfoText;
	private AmountLabel total1099AmountLabel;
	private DynamicForm amountForm, panelR, panelL;
	private CellTable<Client1099Form> cellTable;

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

		VerticalPanel mainPanel = new VerticalPanel();

		mainPanel.add(getSetupPanel());
		mainPanel.add(getPreview1099());
		mainPanel.add(getPrintSetUp());
		mainPanel.add(getEndButtons());

		this.add(mainPanel);

	}

	public CellTable<Client1099Form> get1099InformationGrid() {

		cellTable = new CellTable<Client1099Form>();

		CheckboxCell checkboxCell = new CheckboxCell();
		Column<Client1099Form, Boolean> checkBoxColumn = new Column<Client1099Form, Boolean>(
				checkboxCell) {

			@Override
			public Boolean getValue(Client1099Form object) {
				return object.isSelected();
			}
		};

		ClickableTextCell informationLink = new ClickableTextCell();

		Column<Client1099Form, String> informationColumn = new Column<Client1099Form, String>(
				informationLink) {

			@Override
			public String getValue(Client1099Form object) {
				return object.getVendorInformation();
			}
		};
		informationColumn.setFieldUpdater(new FieldUpdater() {

			@Override
			public void update(int index, Object object, Object value) {
				ActionFactory.getNewVendorAction().run(null, false);
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
		setVendor = new Button(Accounter.messages().selectVendor(
				Global.get().Vendor()));
		addAccount = new Button(Accounter.messages().assignAccounts(
				Global.get().Account()));
		Label infoLable = new Label(
				"Before paying vendors this year, select vendors and assign accounts. These setup tasks ensure that the forms you file next year will be correct. If you have already made vendor payments this year, you may need to revise them to assign them to the proper accounts.");

		infoLable.setWordWrap(true);
		infoLable.setHorizontalAlignment(ALIGN_JUSTIFY);

		setVendorpanel = new VerticalPanel();
		setVendorpanel.setSize("1000px", "200px");
		setVendorpanel.add(infoLable);
		setVendorpanel.add(setVendor);

		setVendorpanel.add(addAccount);

		disclosurePanel.add(setVendorpanel);

		setVendor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SelectVendorsTo1099Dialog selectVendorsTo1099Dialog = new SelectVendorsTo1099Dialog(
						Accounter.messages()
								.selectVendor(Global.get().Vendor()), Accounter
								.messages().SelectVendorsToTrack1099(
										Global.get().Vendor()));
				selectVendorsTo1099Dialog.show();
			}
		});
		addAccount.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String assignAccounts = Accounter.messages().assignAccounts(
						Global.get().Account());
				AssignAccountsTo1099Dialog assignAccountsTo1099Dialog = new AssignAccountsTo1099Dialog(
						assignAccounts, assignAccounts);
				assignAccountsTo1099Dialog.show();
			}
		});

		return disclosurePanel;
	}

	private VerticalPanel getPreview1099() {

		companyInfo = new Label(Accounter.constants().companyInformation());
		einInfo = new Label(Accounter.constants().ein());

		companyInfoText = new TextAreaItem(Accounter.constants()
				.companyInformation());
		ClientAddress address = getCompany().getRegisteredAddress();
		if (address != null) {
			companyInfoText.setValue(address.getAddressString());
		}
		einInfoText = new TextAreaItem(Accounter.constants().ein());
		String ein = getCompany().getEin();
		if (ein != null) {
			einInfoText.setValue(ein);
		}

		companyInfoText.setDisabled(true);
		einInfoText.setDisabled(true);

		panelR = new DynamicForm();
		panelL = new DynamicForm();
		// panelL.add(companyInfo);
		panelL.setFields(companyInfoText);

		// panelR.add(einInfo);
		panelR.setFields(einInfoText);

		companyInfopanel = new HorizontalPanel();
		companyInfopanel.setSize("1000px", "100px");
		companyInfopanel.add(panelL);
		companyInfopanel.add(panelR);

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

		CellTable<Client1099Form> table = get1099InformationGrid();
		preview1099panel = new VerticalPanel();
		preview1099panel.add(companyInfopanel);
		preview1099panel.add(table);
		preview1099panel.setHorizontalAlignment(ALIGN_RIGHT);
		preview1099panel.add(amountPanel);

		listDataProvider = new ListDataProvider<Client1099Form>();

		Accounter
				.get1099FormInformation(new AsyncCallback<ArrayList<Client1099Form>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<Client1099Form> result) {
						listDataProvider.getList().addAll(result);
						setTotalAmountFields(result);
					}
				});

		listDataProvider.addDataDisplay(table);

		return preview1099panel;

	}

	protected void setTotalAmountFields(ArrayList<Client1099Form> result) {
		totalNoOf1099Forms = result.size();
		noOf1099FormsLabel.setText(Accounter.constants().totalNoOf1099Forms()
				+ totalNoOf1099Forms);
		for (Client1099Form client1099Form : result) {
			totalAll1099Payments += client1099Form.getTotal1099Payments();
		}

		total1099AmountLabel.setAmount(totalAll1099Payments);
	}

	private DisclosurePanel getPrintSetUp() {

		DisclosurePanel disclosurePanel = new DisclosurePanel(Accounter
				.constants().printAlignmentAndSetup());

		Button printSample = new Button("Print Sample");
		Label blankLabel = new Label("Load empty paper");
		Label adjustLabel = new Label(
				"	Enter adjustments to move text 1/100th of an inch.");

		Label verLabel = new Label("Vertical");
		Label horLabel = new Label("Horizantal");

		Label infoLabel = new Label(
				"	Alignment adjustment values are saved when you click Print Sample (above), or Print (below).If you print forms on more than one printer, write down the alignment values for each.");

		// Place the check above the text box using a vertical panel.

		// HorizontalPanel panel1 = new HorizontalPanel();
		// panel1.add(verLabel);
		// panel1.add(getListBox(true));
		//
		// HorizontalPanel panel2 = new HorizontalPanel();
		// panel2.add(horLabel);
		// panel2.add(getListBox(true));
		//
		// VerticalPanel panel = new VerticalPanel();
		// panel.setHeight("400px");
		// panel.add(blankLabel);
		// panel.add(printSample);
		// panel.add(adjustLabel);
		// panel.add(panel1);
		// panel.add(panel2);
		// panel.add(infoLabel);

		Grid advancedOptions = new Grid(6, 2);
		advancedOptions.setCellSpacing(6);
		advancedOptions.setWidget(0, 0, blankLabel);
		advancedOptions.setHTML(0, 1, "");
		advancedOptions.setWidget(1, 0, printSample);
		advancedOptions.setHTML(1, 1, "");
		advancedOptions.setWidget(2, 0, adjustLabel);
		advancedOptions.setHTML(2, 1, "");
		advancedOptions.setWidget(3, 0, verLabel);
		advancedOptions.setWidget(3, 1, getListBox(true));
		advancedOptions.setWidget(4, 0, horLabel);
		advancedOptions.setWidget(4, 1, getListBox(true));
		advancedOptions.setWidget(5, 0, infoLabel);
		advancedOptions.setHTML(5, 1, "");

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
				// TODO Auto-generated method stub

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

	static ListBox getListBox(boolean dropdown) {
		ListBox widget = new ListBox();
		widget.addStyleName("demo-ListBox");
		widget.addItem("One");
		widget.addItem("Two");
		widget.addItem("Three");
		widget.addItem("Four");
		widget.addItem("Five");
		if (!dropdown)
			widget.setVisibleItemCount(3);
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
