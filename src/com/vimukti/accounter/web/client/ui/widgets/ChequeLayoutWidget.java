package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientChequeLayout;

public class ChequeLayoutWidget extends SimplePanel {
	private ClientChequeLayout chequeLayout;
	private String payeeName;
	private String amountWordLin1;
	private String amountWordLin2;
	private String amountFigr;
	private String checkDate;
	private String companyName;
	private Label payeeNameLbl;
	private Label amountWordLin1Lbl;
	private Label amountWordLin2Lbl;
	private Label amountFigrLbl;
	private Label chequeDateLbl;
	private Label companyNameLbl;
	private Label authoritySignatoryLbl;

	private AbsolutePanel chequeBody;

	public ChequeLayoutWidget(ClientChequeLayout chequeLayout) {
		this.setChequeLayout(chequeLayout);
		payeeName = "Nagaraju Palla";
		amountWordLin1 = "Fiteen Thousad Eight Hundred Sixty";
		amountWordLin2 = "Eight Only";
		amountFigr = "15868.00";
		checkDate = "12/15/2011";
		companyName = "vimukti";
		createControls();
	}

	private void createControls() {
		createChequeLayout();

		payeeNameLbl = new Label(payeeName);
		createParameter(payeeNameLbl, chequeLayout.getPayeeNameTop(),
				chequeLayout.getPayeeNameLeft(),
				chequeLayout.getPayeeNameWidth());

		amountWordLin1Lbl = new Label(amountWordLin1);
		createParameter(amountWordLin1Lbl,
				chequeLayout.getAmountWordsLin1Top(),
				chequeLayout.getAmountWordsLin1Left(),
				chequeLayout.getAmountWordsLin1Width());

		amountWordLin2Lbl = new Label(amountWordLin2);
		createParameter(amountWordLin2Lbl,
				chequeLayout.getAmountWordsLin2Top(),
				chequeLayout.getAmountWordsLin2Left(),
				chequeLayout.getAmountWordsLin2Width());

		amountFigrLbl = new Label(amountFigr);
		createParameter(amountFigrLbl, chequeLayout.getAmountFigTop(),
				chequeLayout.getAmountFigLeft(),
				chequeLayout.getAmountFigWidth());

		chequeDateLbl = new Label(checkDate);
		createParameter(chequeDateLbl, chequeLayout.getChequeDateTop(),
				chequeLayout.getChequeDateLeft(),
				chequeLayout.getChequeDateWidth());

		companyNameLbl = new Label(companyName);
		createParameter(companyNameLbl, chequeLayout.getCompanyNameTop(),
				chequeLayout.getCompanyNameLeft(),
				chequeLayout.getCompanyNameWidth());

		authoritySignatoryLbl = new Label(chequeLayout.getAuthorisedSignature());
		createParameter(authoritySignatoryLbl, chequeLayout.getSignatoryTop(),
				chequeLayout.getSignatoryLeft(),
				chequeLayout.getSignatoryWidth());
	}

	private void createChequeLayout() {
		chequeBody = new AbsolutePanel();
		chequeBody.setStyleName("chequeBody");

		chequeBody.setSize(chequeLayout.getChequeWidth() + "cm",
				chequeLayout.getChequeHeight() + "cm");

		HorizontalPanel topScale = new HorizontalPanel();
		Label top = new Label(
				" | | | | 1 | | | | 2 | | | | 3 | | | | 4 | | | | 5 | | | | 6 | | | | 7 | | | | 8 | | | | 9 | | | | 10");
		topScale.add(top);
		topScale.addStyleName("chequeTopScal");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		VerticalPanel vertScale = new VerticalPanel();
		Label left = new Label(
				"_ _ _ _ 1 _ _ _ _ 2 _ _ _ _ 3 _ _ _ _ 4 _ _ _ _ 5");
		vertScale.add(left);
		vertScale.setStyleName("chequeLeftScal");
		SimplePanel chequeBg = new SimplePanel();
		chequeBg.add(chequeBody);
		chequeBg.setStyleName("chequeBackground");
		horizontalPanel.add(vertScale);
		horizontalPanel.add(chequeBg);
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(topScale);
		verticalPanel.add(horizontalPanel);
		this.add(verticalPanel);
	}

	private void createParameter(Label label, double top, double left,
			double width) {
		label.setWidth(width + "cm");
		label.setStyleName("chequeParameter");
		DOM.setStyleAttribute(label.getElement(), "position", "relative");
		chequeBody.add(label, getPixel(left), getPixel(top));
	}

	private int getPixel(double cm) {
		return (int) (72 * cm * 100 / 254);// ScreenResolution.centimeterAsPixel(cm);
	}

	private void updateParameter(Label label, double left, double top) {
		chequeBody.setWidgetPosition(label, getPixel(left), getPixel(top));
	}

	public ClientChequeLayout getChequeLayout() {
		return chequeLayout;
	}

	public void setChequeLayout(ClientChequeLayout chequeLayout) {
		this.chequeLayout = chequeLayout;

		// Have to do re-inialize the labels
		updateParameter(payeeNameLbl, chequeLayout.getPayeeNameLeft(),
				chequeLayout.getPayeeNameTop());
		payeeNameLbl.setWidth(chequeLayout.getPayeeNameWidth() + "cm");

		updateParameter(amountWordLin1Lbl,
				chequeLayout.getAmountWordsLin1Left(),
				chequeLayout.getAmountWordsLin1Top());
		amountWordLin1Lbl.setWidth(chequeLayout.getAmountWordsLin1Width()
				+ "cm");

		updateParameter(amountWordLin2Lbl,
				chequeLayout.getAmountWordsLin2Left(),
				chequeLayout.getAmountWordsLin2Top());
		amountWordLin2Lbl.setWidth(chequeLayout.getAmountWordsLin2Width()
				+ "cm");

		updateParameter(amountFigrLbl, chequeLayout.getAmountFigLeft(),
				chequeLayout.getAmountFigTop());
		amountFigrLbl.setWidth(chequeLayout.getAmountFigWidth() + "cm");

		updateParameter(chequeDateLbl, chequeLayout.getChequeDateLeft(),
				chequeLayout.getChequeDateTop());
		chequeDateLbl.setWidth(chequeLayout.getChequeDateWidth() + "cm");

		updateParameter(companyNameLbl, chequeLayout.getCompanyNameLeft(),
				chequeLayout.getCompanyNameTop());
		companyNameLbl.setWidth(chequeLayout.getCompanyNameWidth() + "cm");

		updateParameter(authoritySignatoryLbl, chequeLayout.getSignatoryLeft(),
				chequeLayout.getSignatoryTop());
		authoritySignatoryLbl.setWidth(chequeLayout.getSignatoryWidth() + "cm");
		authoritySignatoryLbl.setText(chequeLayout.getAuthorisedSignature());

		chequeBody.setHeight(chequeLayout.getChequeHeight() + "cm");
		chequeBody.setWidth(chequeLayout.getChequeHeight() + "cm");
	}

	public void setPayeeNameTop(double payeeNameTop) {
		chequeLayout.setPayeeNameTop(payeeNameTop);
		updateParameter(payeeNameLbl, chequeLayout.getPayeeNameLeft(),
				chequeLayout.getPayeeNameTop());
	}

	public void setPayeeNameLeft(double payeeNameLeft) {
		chequeLayout.setPayeeNameLeft(payeeNameLeft);
		updateParameter(payeeNameLbl, chequeLayout.getPayeeNameLeft(),
				chequeLayout.getPayeeNameTop());
	}

	public void setPayeeNameWidth(double payeeNameWidth) {
		chequeLayout.setPayeeNameWidth(payeeNameWidth);
		payeeNameLbl.setWidth(payeeNameWidth + "cm");
	}

	public void setAmountWordsLin1Top(double amountWordsLin1Top) {
		chequeLayout.setAmountWordsLin1Top(amountWordsLin1Top);
		updateParameter(amountWordLin1Lbl,
				chequeLayout.getAmountWordsLin1Left(),
				chequeLayout.getAmountWordsLin1Top());
	}

	public void setAmountWordsLin1Left(double amountWordsLin1Left) {
		chequeLayout.setAmountWordsLin1Left(amountWordsLin1Left);
		updateParameter(amountWordLin1Lbl,
				chequeLayout.getAmountWordsLin1Left(),
				chequeLayout.getAmountWordsLin1Top());
	}

	public void setAmountWordsLin1Width(double amountWordsLin1Width) {
		chequeLayout.setAmountWordsLin1Width(amountWordsLin1Width);
		amountWordLin1Lbl.setWidth(amountWordsLin1Width + "cm");
	}

	public void setAmountWordsLin2Top(double amountWordsLin2Top) {
		chequeLayout.setAmountWordsLin2Top(amountWordsLin2Top);
		updateParameter(amountWordLin2Lbl,
				chequeLayout.getAmountWordsLin2Left(),
				chequeLayout.getAmountWordsLin2Top());
	}

	public void setAmountWordsLin2Left(double amountWordsLin2Left) {
		chequeLayout.setAmountWordsLin2Left(amountWordsLin2Left);
		updateParameter(amountWordLin2Lbl,
				chequeLayout.getAmountWordsLin2Left(),
				chequeLayout.getAmountWordsLin2Top());
	}

	public void setAmountWordsLin2Width(double amountWordsLin2Width) {
		chequeLayout.setAmountWordsLin2Width(amountWordsLin2Width);
		amountWordLin2Lbl.setWidth(amountWordsLin2Width + "cm");
	}

	public void setAmountFigTop(double amountFigTop) {
		chequeLayout.setAmountFigTop(amountFigTop);
		updateParameter(amountFigrLbl, chequeLayout.getAmountFigLeft(),
				chequeLayout.getAmountFigTop());
	}

	public void setAmountFigLeft(double amountFigLeft) {
		chequeLayout.setAmountFigLeft(amountFigLeft);
		updateParameter(amountFigrLbl, chequeLayout.getAmountFigLeft(),
				chequeLayout.getAmountFigTop());
	}

	public void setAmountFigWidth(double amountFigWidth) {
		chequeLayout.setAmountFigWidth(amountFigWidth);
		amountFigrLbl.setWidth(amountFigWidth + "cm");
	}

	public void setChequeDateTop(double chequeDateTop) {
		chequeLayout.setChequeDateTop(chequeDateTop);
		updateParameter(chequeDateLbl, chequeLayout.getChequeDateLeft(),
				chequeLayout.getChequeDateTop());
	}

	public void setChequeDateLeft(double chequeDateLeft) {
		chequeLayout.setChequeDateLeft(chequeDateLeft);
		updateParameter(chequeDateLbl, chequeLayout.getChequeDateLeft(),
				chequeLayout.getChequeDateTop());
	}

	public void setChequeDateWidth(double chequeDateWidth) {
		chequeLayout.setChequeDateWidth(chequeDateWidth);
		chequeDateLbl.setWidth(chequeDateWidth + "cm");
	}

	public void setCompanyNameTop(double companyNameTop) {
		chequeLayout.setCompanyNameTop(companyNameTop);
		updateParameter(companyNameLbl, chequeLayout.getCompanyNameLeft(),
				chequeLayout.getCompanyNameTop());
	}

	public void setCompanyNameLeft(double companyNameLeft) {
		chequeLayout.setCompanyNameLeft(companyNameLeft);
		updateParameter(companyNameLbl, chequeLayout.getCompanyNameLeft(),
				chequeLayout.getCompanyNameTop());
	}

	public void setCompanyNameWidth(double companyNameWidth) {
		chequeLayout.setCompanyNameWidth(companyNameWidth);
		companyNameLbl.setWidth(companyNameWidth + "cm");
	}

	public void setSignatoryTop(double signatoryTop) {
		chequeLayout.setSignatoryTop(signatoryTop);
		updateParameter(authoritySignatoryLbl, chequeLayout.getSignatoryLeft(),
				chequeLayout.getSignatoryTop());
	}

	public void setSignatoryLeft(double signatoryLeft) {
		chequeLayout.setSignatoryLeft(signatoryLeft);
		updateParameter(authoritySignatoryLbl, chequeLayout.getSignatoryLeft(),
				chequeLayout.getSignatoryTop());
	}

	public void setSignatoryWidth(double signatoryWidth) {
		chequeLayout.setSignatoryWidth(signatoryWidth);
		authoritySignatoryLbl.setWidth(signatoryWidth + "cm");
	}

	public void setChequeHeight(double chequeHeight) {
		chequeLayout.setChequeHeight(chequeHeight);
		chequeBody.setHeight(chequeHeight + "cm");
	}

	public void setChequeWidth(double chequeWidth) {
		chequeLayout.setChequeWidth(chequeWidth);
		chequeBody.setWidth(chequeWidth + "cm");
	}

	public void setAuthoritySignatory(String authoritySignatory) {
		authoritySignatoryLbl.setText(authoritySignatory);
	}
}
