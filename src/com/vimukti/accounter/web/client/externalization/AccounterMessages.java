package com.vimukti.accounter.web.client.externalization;

import java.awt.Stroke;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface AccounterMessages extends Messages {
	public String userName(String loginUserName);

	public String failedTransaction(String transName);

	public String pleaseEnter(String itemName);

	public String pleaseEnterHTML(String title);

	public String failedTogetCreditsListAndPayments(String name);

	public SafeHtml addComparativeButton();

	public SafeHtml addNewLine();

	public String contactDetailsHtml();

	SafeHtml companyCommentHtml();

	SafeHtml companySettingsTitle();

	public SafeHtml allHTML();

	public SafeHtml aboutThisFieldHelp();

	public SafeHtml changePasswordHTML();

	SafeHtml conversionBalanaceHeader();

	SafeHtml conversionCommet();

	SafeHtml conversionDateButton();

	SafeHtml conversionHTML();

	public SafeHtml creaditHTML();

	public SafeHtml removeHTML();

	public SafeHtml previousHTML();

	public SafeHtml nextHTML();

	public SafeHtml logoutHTML();

	SafeHtml deleteHtml();

	public String paypalEmailHtml();

	public String startFiscalHTML();

	public String endFiscalHTML();

	SafeHtml footerComment();

	SafeHtml generalSettingsHeading();

	public String fiscalStartEndCompreHTML();

	SafeHtml helpContent();

	public SafeHtml helpCenter();

	public SafeHtml helpHTML();

	SafeHtml invoiceBrandingHTML();

	SafeHtml invoiceComment();

	SafeHtml logoComment();

	public SafeHtml upload();

	public SafeHtml undoneHtml();

	SafeHtml uploadLogo();

	SafeHtml userHTML();

	SafeHtml usersComment();

	public SafeHtml labelHTML();

	public SafeHtml conversationDateSelectionHTML();

	public SafeHtml bodyFooter();

	public SafeHtml sureToDelete(String string);

	public String wecantDeleteThisTheme(String themeName);

	public String pleaseselectvalidtransactionGrid(String string);

	public String noRecordsToShow();

	public String selectIndustryInfoHTML();

	public String selectIndustry();

	public String userGuidelinesMessage();

	public String accounterUseYourForms();

	public String industrySelectNote();

	public String journalEntryVendor(String name);

	public String invalidAccountNumber(String name);

	public String startSetupInfo();

	public String skipSetupInfo();
	
}
