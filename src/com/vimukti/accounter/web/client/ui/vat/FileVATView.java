package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBox;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.VATBoxGrid;
import com.vimukti.accounter.web.client.ui.reports.VAT100Report;

public class FileVATView extends BaseView<ClientVATReturn> {

	private TAXAgencyCombo taxAgencyCombo;
	private DateItem fromDate;
	private DateItem toDate;
	private HorizontalPanel topLayout;
	private VerticalPanel mainLayout;
	private VATBoxGrid gridView;
	private Button adjustButton;
	private Button printButton;
	private ClientTAXAgency selectedVatAgency;
	private ArrayList<DynamicForm> listforms;
	private double amt;
	private Button updateButton;

	boolean canSaveFileVat = false;

	public FileVATView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	private void createControls() {
		Label infolabel = new Label(Accounter.constants().fileVAT());
		infolabel.removeStyleName("gwt-Label");
		infolabel.addStyleName(Accounter.constants().labelTitle());
		taxAgencyCombo = new TAXAgencyCombo(Accounter.constants().vatAgency());
		taxAgencyCombo.setHelpInformation(true);
		taxAgencyCombo.setRequired(true);

		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					public void selectedComboBoxItem(ClientTAXAgency selectItem) {

						vatAgencySelected(selectItem);

					}
				});
		taxAgencyCombo.setWidth("200px");
		fromDate = new DateItem(Accounter.constants().from());
		fromDate.setHelpInformation(true);
		fromDate.setWidth(100);
		fromDate.setEnteredDate(new ClientFinanceDate());
		toDate = new DateItem(Accounter.constants().to());
		toDate.setHelpInformation(true);
		toDate.setWidth(100);
		toDate.setEnteredDate(new ClientFinanceDate());
		listforms = new ArrayList<DynamicForm>();

		updateButton = new Button(Accounter.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				canSaveFileVat = true;
				reloadVatBoxes();
			}
		});

		DynamicForm topForm = new DynamicForm();
		topForm.setIsGroup(true);
		topForm.setGroupTitle(Accounter.constants().top());
		topForm.setNumCols(6);
		topForm.setFields(taxAgencyCombo, fromDate, toDate);
		topForm.setWidth("100%");

		initListGrid();

		HTML beforeLabel = new HTML("<string>"
				+ Accounter.constants().beforeYouFile());

		HTML adjustLabel = new HTML("<strong>"
				+ Accounter.constants().doYouNeedToMakeAnAdjustment()
				+ " </strong><br>" + Accounter.constants().useAdjustButton());

		adjustButton = new Button(Accounter.constants().adjustVATReturn());
		adjustButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedVatAgency != null) {
					AdjustTAXAction vatAdjustmentAction = ActionFactory
							.getVatAdjustmentAction();
					vatAdjustmentAction.setVatAgency(selectedVatAgency);
					vatAdjustmentAction.run(null, true);
				}

			}
		});

		VerticalPanel adjustForm = new VerticalPanel();
		adjustForm.setSpacing(3);
		// adjustForm.add(adjustLabel);
		adjustForm.add(adjustButton);
		// if (adjustButton.isEnabled()) {
		// adjustButton.getElement().getParentElement()
		// .setClassName("ibutton");
		// ThemesUtil.addDivToButton(adjustButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		HTML printLabel = new HTML("<strong>"
				+ Accounter.constants().doYouWantPrintYourVATReturn()
				+ "</strong><br>"
				+ Accounter.constants().youCanPrintVATReturn());

		printButton = new Button(Accounter.constants().printVATReturn());
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedVatAgency != null
						&& fromDate.getEnteredDate() != null
						&& toDate.getEnteredDate() != null)
					printVATReturn();
				else
					Accounter.showError(Accounter.constants()
							.pleaseselectvaliddateranges());

			}
		});

		disableButtons();

		VerticalPanel printForm = new VerticalPanel();
		printForm.setSpacing(3);
		printForm.add(printLabel);
		printForm.add(printButton);
		// if (printButton.isEnabled()) {
		// printButton.getElement().getParentElement().setClassName("ibutton");
		// ThemesUtil.addDivToButton(printButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		topLayout = new HorizontalPanel();
		topLayout.add(topForm);
		topLayout.add(updateButton);

		// if (updateButton.isEnabled()) {
		// updateButton.getElement().getParentElement()
		// .setClassName("ibutton");
		// ThemesUtil.addDivToButton(updateButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		mainLayout = new VerticalPanel();
		mainLayout.setHeight("100%");
		mainLayout.add(infolabel);
		mainLayout.add(topLayout);
		mainLayout.add(gridView);
		mainLayout.setWidth("100%");
		// mainLayout.add(beforeLabel);
		mainLayout.add(adjustForm);
		mainLayout.add(printForm);

		AccounterDOM.setParentElementHeight(infolabel.getElement(), 5);
		AccounterDOM.setParentElementHeight(topLayout.getElement(), 10);
		AccounterDOM.setParentElementHeight(gridView.getElement(), 60);

		this.add(mainLayout);

		/* Adding dynamic forms in list */
		listforms.add(topForm);

	}

	protected void vatAgencySelected(final ClientTAXAgency selectItem) {

		this.selectedVatAgency = selectItem;

		if (selectItem == null) {
			disableButtons();
			gridView.clear();
		} else {

			enableButtons();
			getVATReturnEndDate(selectItem);

		}

	}

	private void getVATReturnEndDate(ClientTAXAgency selectItem) {
		List<ClientVATReturn> vatReturns = getCompany().getVatReturns();
		ClientVATReturn lastVATReturn = null;
		for (ClientVATReturn vatReturn : vatReturns) {
			if (selectItem.getID() == vatReturn.getTAXAgency()) {
				lastVATReturn = vatReturn;
			}
		}
		if (lastVATReturn != null) {
			if (lastVATReturn.getVATperiodEndDate() != 0) {
				ClientFinanceDate date = new ClientFinanceDate(
						lastVATReturn.getVATperiodEndDate());
				int day = date.getDay();
				date.setDay(day + 1);
				fromDate.setDatethanFireEvent(date);
				// date.setDate(day + 1);
				toDate.setDatethanFireEvent(date);
			}
		} else {
			List<ClientFiscalYear> fiscalYears = Accounter.getCompany()
					.getFiscalYears();
			for (ClientFiscalYear fiscalYear : fiscalYears) {
				if (fiscalYear.getIsCurrentFiscalYear()) {
					fromDate.setDatethanFireEvent(fiscalYear.getStartDate());
					break;
				}
			}
		}

	}

	public void disableButtons() {
		this.adjustButton.setEnabled(false);
		this.printButton.setEnabled(false);

	}

	public void enableButtons() {
		this.adjustButton.setEnabled(true);

	}

	public void enableprintButton() {

		this.printButton.setEnabled(true);

	}

	public void disableprintButton() {

		this.printButton.setEnabled(false);

	}

	private void reloadVatBoxes() {
		gridView.removeAllRecords();
		ClientFinanceDate startDate = fromDate.getDate();
		ClientFinanceDate endDate = toDate.getDate();
		if (this.selectedVatAgency == null || startDate.getDate() == 0
				|| endDate.getDate() == 0) {
			gridView.removeLoadingImage();
			gridView.addEmptyMessage(Accounter.constants().selectVatAgency());
			disableprintButton();
			return;
		}
		gridView.addLoadingImagePanel();

		this.rpcUtilService.getTAXReturn(this.selectedVatAgency,
				fromDate.getDate(), toDate.getDate(),
				new AccounterAsyncCallback<ClientVATReturn>() {

					@Override
					public void onException(AccounterException caught) {
						// gridView.clear();
						gridView.addEmptyMessage(Accounter.constants()
								.norecordstoshowinbetweentheselecteddates());
						// UIUtils.err(FinanceApplication.constants()
						// .failedToRetrieveVatBoxesForVATAgency()
						// + FileVATView.this.selectedVatAgency.getName());
						disableprintButton();
					}

					@Override
					public void onResultSuccess(ClientVATReturn result) {
						gridView.removeLoadingImage();
						setData(result);

						getData().setTAXAgency(
								FileVATView.this.selectedVatAgency.getID());

						gridView.clear();

						double box3Amt = 0.0;
						double box5Amt = 0.0;
						double box4Amt = 0.0;
						for (ClientBox box : result.getBoxes()) {

							if (box.getBoxNumber() == 1
									|| box.getBoxNumber() == 2) {
								box3Amt = box3Amt + box.getAmount();
							}
							if (box.getBoxNumber() == 3) {
								box.setAmount(box3Amt);
							}
							if (box.getBoxNumber() == 4) {
								box4Amt = box4Amt + box.getAmount();
							}
							if (box.getBoxNumber() == 4) {
								box.setAmount(box4Amt);
							}
							if (box.getBoxNumber() == 4) {
								box5Amt = box3Amt - box.getAmount();
							}

							if (box.getBoxNumber() == 5) {
								box.setAmount(box5Amt);
								// gridView.addFooterValue("You Owe VAT of  " +
								// box5Amt
								// + " to  " + selectedVatAgency.getName(), 1);
							}
							gridView.addData(box);
						}
						enableprintButton();

					}
				});

	}

	private void initListGrid() {

		gridView = new VATBoxGrid(false);
		// gridView.setCurrentView(this);
		// gridView.setCanEdit(true);
		// gridView.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		gridView.isEnable = false;
		gridView.init();
		// gridView.setHeight("250px");
		gridView.addStyleName("file-vat");
		gridView.addEmptyMessage(Accounter.constants().selectVatAgency());

	}

	@Override
	public void saveAndUpdateView() {

		if (this.selectedVatAgency != null && !isInViewMode()) {
			data.setTransactionDate(new ClientFinanceDate().getDate());

		}
		saveOrUpdate(data);
		// else {
		//
		// UIUtils.err(FinanceApplication.constants()
		// .pleaseSelectValidVATAgency());
		// return;
		// }

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		// Accounter.showInformation(FinanceApplication.constants()
		// .fileVATCreated());
		super.saveSuccess(result);
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.taxAgencyCombo.setFocus();
	}

	@Override
	protected void onAttach() {
		super.onAttach();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	public void printVATReturn() {
		VAT100Report report = new VAT100Report();
		report.setAction(ActionFactory.getVAT100ReportAction());
		report.setStartAndEndDates(fromDate.getEnteredDate(),
				toDate.getEnteredDate());
		report.setVatAgency(selectedVatAgency.getID());
		report.print();

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void printPreview() {
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		// check whether vet agency is selected or not
		// can save file vat or not

		if (this.selectedVatAgency == null && this.isInViewMode()) {
			taxAgencyCombo.addStyleName("highlightedFormItem");
			result.addError(selectedVatAgency, Accounter.constants()
					.pleaseSelectValidVATAgency());
		} else {
			result.addWarning(selectedVatAgency, Accounter.constants()
					.sureToSaveFileVAT());
			// AccounterValidator.validate_FileVat(this);
		}
		if (!canSaveFileVat) {
			taxAgencyCombo.addStyleName("highlightedFormItem");
			fromDate.addStyleName("highlightedFormItem");
			toDate.addStyleName("highlightedFormItem");
			result.addError(this, Accounter.constants()
					.fileVATcantsavewithemptyvalues());
		}
		List<ClientBox> records = gridView.getRecords();
		if (records.isEmpty()) {
			result.addError(this, Accounter.constants()
					.thereIsNoTrasationsToFile());
		}
		return result;
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().fileVAT();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		this.saveAndCloseButton.setText(Accounter.constants().fileVATReturn());
		buttonBar.remove(this.saveAndNewButton);
	}
}
