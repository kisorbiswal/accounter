package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewBrandingThemeCommand extends AbstractTransactionCommand {

	private static final String THEME_NAME = "Name";
	private static final String PAGE_SIZE = "Page Size";
	private static final String TOP_MARGIN = "Top Margin";
	private static final String BOTTOM_MARGIN = "Bottom Margin";
	private static final String ADDRESS_PADDING = "Address Padding";
	private static final String FONT = "Font";
	private static final String FONT_SIZE = "font Size";
	private static final String OVERDUE_INVOICE_TITLE = "Overdue Invoice Title";
	private static final String CREDIT_TITLE = "Creditnote Title";
	private static final String STATEMENT_TITLE = "Statement Title";
	private static final String LOGO = "Add logo";
	private static final String MEASURE_IN = "Measure in";
	private static final String SHOW_TAX_NUMBER = "Show Tax Number";
	private static final String SHOW_COLUMN_HEADINGS = "Show Column Headings";
	private static final String SHOW_UNITPRICE_N_QUANTITY = "Show Unit Price &amp; Quantity";
	private static final String SHOW_TAX = "Show Tax Column";
	private static final String SHOW_VAT = "Show Vat Column";
	private static final String SHOW_REG_ADDRESS = "Show Registered Address";
	private static final String SHOW_LOGO = "Show Logo";
	private static final String PAYPALID = "Paypal Id";
	private static final String LOGO_ALLIGNMENT = "Logo Alignment";
	private static final String TERMS_N_PAYMENT_ADVICE = "Terms &amp; Payment Advice";
	private static final String INVOICE_TEMPLATE = "Invoice Template";
	private static final String CREDIT_TEMPLATE = "Credit Template";
	private static final String CONTACT_DETAILS = "Contact Details";

	public static final int MEASURES_IN_CM = 1;
	public static final int MEASURES_IN_INCHES = 2;

	public static final int LOGO_ALIGNMENT_LEFT = 1;
	public static final int LOGO_ALIGNMENT_RIGHT = 2;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(THEME_NAME, false, false));

		list.add(new Requirement(PAGE_SIZE, true, false));
		list.add(new Requirement(TOP_MARGIN, true, false));
		list.add(new Requirement(BOTTOM_MARGIN, true, false));
		list.add(new Requirement(ADDRESS_PADDING, true, false));
		list.add(new Requirement(FONT, true, false));
		list.add(new Requirement(FONT_SIZE, true, false));
		list.add(new Requirement(OVERDUE_INVOICE_TITLE, true, false));
		list.add(new Requirement(CREDIT_TITLE, true, false));
		list.add(new Requirement(STATEMENT_TITLE, true, false));
		list.add(new Requirement(LOGO, true, false));
		list.add(new Requirement(MEASURE_IN, true, false));
		list.add(new Requirement(SHOW_TAX_NUMBER, true, false));
		list.add(new Requirement(SHOW_COLUMN_HEADINGS, true, false));
		list.add(new Requirement(SHOW_UNITPRICE_N_QUANTITY, true, false));
		list.add(new Requirement(SHOW_TAX, true, false));
		list.add(new Requirement(SHOW_VAT, true, false));
		list.add(new Requirement(SHOW_REG_ADDRESS, true, false));
		list.add(new Requirement(SHOW_LOGO, true, false));
		list.add(new Requirement(PAYPALID, true, false));
		list.add(new Requirement(LOGO_ALLIGNMENT, true, false));
		list.add(new Requirement(TERMS_N_PAYMENT_ADVICE, true, false));
		list.add(new Requirement(CONTACT_DETAILS, true, false));

		list.add(new Requirement(INVOICE_TEMPLATE, true, false));
		list.add(new Requirement(CREDIT_TEMPLATE, true, false));

	}

	@Override
	public Result run(Context context) {

		Result result = null;

		result = getThemeNameRequirement(context);
		if (result != null) {
			return result;
		}

		result = optionalRequirements(context);
		if (result != null) {
			return result;
		}

		return createNewTheme(context);
	}

	private Result createNewTheme(Context context) {

		BrandingTheme brandingTheme = new BrandingTheme();
		Integer pageSize = get(PAGE_SIZE).getValue();
		double topMargin = (Double) get(TOP_MARGIN).getValue();
		double bottomMargin = (Double) get(BOTTOM_MARGIN).getValue();
		double addressPadding = (Double) get(ADDRESS_PADDING).getValue();

		boolean showTaxNumber = (Boolean) get(SHOW_TAX_NUMBER).getValue();
		boolean showLogo = (Boolean) get(SHOW_LOGO).getValue();
		boolean showColumnHeadings = (Boolean) get(SHOW_COLUMN_HEADINGS)
				.getValue();
		boolean showUnitPrice = (Boolean) get(SHOW_UNITPRICE_N_QUANTITY)
				.getValue();
		boolean showTax = (Boolean) get(SHOW_TAX).getValue();
		boolean showVat = (Boolean) get(SHOW_VAT).getValue();
		boolean showRegAddress = (Boolean) get(SHOW_REG_ADDRESS).getValue();

		int measuresIn = get(MEASURE_IN).getValue();
		int logoAllignmentType = get(LOGO_ALLIGNMENT).getValue();

		brandingTheme.setThemeName((String) get(THEME_NAME).getValue());
		brandingTheme.setPageSizeType(pageSize.intValue());
		brandingTheme.setTopMargin(topMargin);
		brandingTheme.setMarginsMeasurementType(measuresIn);
		brandingTheme.setBottomMargin(bottomMargin);
		brandingTheme.setAddressPadding(addressPadding);
		brandingTheme.setFont((String) get(FONT).getValue());
		brandingTheme.setFontSize((String) get(FONT_SIZE).getValue());
		brandingTheme
				.setOverDueInvoiceTitle((String) get(OVERDUE_INVOICE_TITLE)
						.getValue());
		brandingTheme.setCreditMemoTitle((String) get(CREDIT_TITLE).getValue());
		brandingTheme.setStatementTitle((String) get(STATEMENT_TITLE)
				.getValue());
		brandingTheme.setFileName((String) get(LOGO).getValue());

		brandingTheme.setShowTaxNumber(showTaxNumber);
		brandingTheme.setShowColumnHeadings(showColumnHeadings);
		brandingTheme.setShowUnitPrice_And_Quantity(showUnitPrice);
		brandingTheme.setShowTaxColumn(showTax);
		brandingTheme.setShowVatColumn(showVat);
		brandingTheme.setShowRegisteredAddress(showRegAddress);
		brandingTheme.setShowLogo(showLogo);

		brandingTheme.setPayPalEmailID((String) get(PAYPALID).getValue());
		brandingTheme.setLogoAlignmentType(logoAllignmentType);
		brandingTheme.setContactDetails((String) get(CONTACT_DETAILS)
				.getValue());
		brandingTheme.setTerms_And_Payment_Advice((String) get(
				TERMS_N_PAYMENT_ADVICE).getValue());
		brandingTheme.setInvoiceTempleteName((String) get(INVOICE_TEMPLATE)
				.getValue());
		brandingTheme.setCreditNoteTempleteName((String) get(CREDIT_TEMPLATE)
				.getValue());

		Session session = context.getHibernateSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(brandingTheme);
		transaction.commit();

		markDone();

		Result result = new Result();
		result.add(" Branding theme was created successfully.");

		return result;
	}

	private Result getThemeNameRequirement(Context context) {

		Requirement requirement = get(THEME_NAME);
		if (!requirement.isDone()) {
			String themeName = context.getSelection(TEXT);
			if (themeName != null) {
				requirement.setValue(themeName);
			} else {
				return text(context, "Please enter the theme name", null);
			}
		}
		String name = (String) context.getAttribute(INPUT_ATTR);
		if (name.equals(THEME_NAME)) {
			requirement.setValue(name);
		}

		return null;
	}

	private Result optionalRequirements(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);

		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Result result = getpageSizeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getTopMarginRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getMeasuresInRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = getBottomMarginRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getAddressPaddingRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getOverDueInvoiceTitleRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getCreditNoteTitleRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getStatementTitleRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getPaypalIdRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getLogoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getTermsnPaymentAdviceRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getLogoAllignmentRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = getContactDetailsRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getInvoiceTemplateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getCreditTemplateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getFontRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = getFontSizeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = showTaxNumberRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = showColumnHeadingsRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = showUnitPriceQuantityRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = showTaxRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = showVatRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = showRegisteredAddressRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = showLogoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		return null;
	}

	private Result getOverDueInvoiceTitleRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(OVERDUE_INVOICE_TITLE);
		String title = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(OVERDUE_INVOICE_TITLE)) {
			String order = context.getSelection(OVERDUE_INVOICE_TITLE);
			if (order == null) {
				order = context.getString();
			}
			title = order;
			req.setValue(title);
		}

		if (selection == title) {
			context.setAttribute(INPUT_ATTR, OVERDUE_INVOICE_TITLE);
			return text(context, "Enter over due invoice title ",
					title.toString());
		}

		Record balanceRecord = new Record(title);
		balanceRecord.add("Name", OVERDUE_INVOICE_TITLE);
		balanceRecord.add("Value", title);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getStatementTitleRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(STATEMENT_TITLE);
		String title = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(STATEMENT_TITLE)) {
			String order = context.getSelection(STATEMENT_TITLE);
			if (order == null) {
				order = context.getString();
			}
			title = order;
			req.setValue(title);
		}

		if (selection == title) {
			context.setAttribute(INPUT_ATTR, STATEMENT_TITLE);
			return text(context, "Enter Statement title ", title.toString());
		}

		Record balanceRecord = new Record(title);
		balanceRecord.add("Name", STATEMENT_TITLE);
		balanceRecord.add("Value", title);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getFontRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(FONT);
		String font = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(FONT)) {
			String order = context.getSelection(FONT);
			if (order == null) {
				order = context.getString();
			}
			font = order;
			req.setValue(font);
		}

		if (selection == font) {
			context.setAttribute(INPUT_ATTR, FONT);
			return text(context, "Enter Font ", font.toString());
		}

		Record balanceRecord = new Record(font);
		balanceRecord.add("Name", FONT);
		balanceRecord.add("Value", font);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getFontSizeRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(FONT_SIZE);
		String fontSize = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(FONT_SIZE)) {
			String order = context.getSelection(FONT_SIZE);
			if (order == null) {
				order = context.getString();
			}
			fontSize = order;
			req.setValue(fontSize);
		}

		if (selection == fontSize) {
			context.setAttribute(INPUT_ATTR, FONT_SIZE);
			return text(context, "Enter Font Size ", fontSize.toString());
		}

		Record balanceRecord = new Record(fontSize);
		balanceRecord.add("Name", FONT_SIZE);
		balanceRecord.add("Value", fontSize);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getCreditNoteTitleRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(CREDIT_TITLE);
		String title = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(CREDIT_TITLE)) {
			String order = context.getSelection(CREDIT_TITLE);
			if (order == null) {
				order = context.getString();
			}
			title = order;
			req.setValue(title);
		}

		if (selection == title) {
			context.setAttribute(INPUT_ATTR, CREDIT_TITLE);
			return text(context, "Enter Credit note title ", title.toString());
		}

		Record balanceRecord = new Record(title);
		balanceRecord.add("Name", CREDIT_TITLE);
		balanceRecord.add("Value", title);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getpageSizeRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(PAGE_SIZE);
		Integer pageSize = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(PAGE_SIZE)) {
			Integer order = context.getSelection(PAGE_SIZE);
			if (order == null) {
				order = context.getInteger();
			}
			pageSize = order;
			req.setValue(pageSize);
		}

		if (selection == pageSize) {
			context.setAttribute(INPUT_ATTR, PAGE_SIZE);
			return number(context, "Enter page size ", pageSize.toString());
		}

		Record balanceRecord = new Record(pageSize);
		balanceRecord.add("Name", PAGE_SIZE);
		balanceRecord.add("Value", pageSize.intValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getTopMarginRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(TOP_MARGIN);
		Double topMargin = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(TOP_MARGIN)) {
			Double order = context.getSelection(TOP_MARGIN);
			if (order == null) {
				order = context.getDouble();
			}
			topMargin = order;
			req.setValue(topMargin);
		}

		if (selection == topMargin) {
			context.setAttribute(INPUT_ATTR, TOP_MARGIN);
			return amount(context, "Enter top margin ", topMargin);
		}

		Record balanceRecord = new Record(topMargin);
		balanceRecord.add("Name", TOP_MARGIN);
		balanceRecord.add("Value", topMargin.doubleValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getMeasuresInRequirement(Context context, ResultList list,
			Object selection) {

		Requirement measuresInReq = get(MEASURE_IN);
		Boolean measuresin = (Boolean) measuresInReq.getValue();
		if (selection == measuresin) {
			context.setAttribute(INPUT_ATTR, MEASURE_IN);
			measuresin = !measuresin;
			measuresInReq.setValue(measuresin);
		}
		int measures;
		if (measuresin) {
			measures = MEASURES_IN_CM;
		} else {
			measures = MEASURES_IN_INCHES;
		}
		Record isTaxableRecord = new Record(measuresin);
		isTaxableRecord.add("Name", MEASURE_IN);
		isTaxableRecord.add("Value", measures);
		list.add(isTaxableRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result getBottomMarginRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(BOTTOM_MARGIN);
		Double bottomMargin = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(BOTTOM_MARGIN)) {
			Double order = context.getSelection(BOTTOM_MARGIN);
			if (order == null) {
				order = context.getDouble();
			}
			bottomMargin = order;
			req.setValue(bottomMargin);
		}

		if (selection == bottomMargin) {
			context.setAttribute(INPUT_ATTR, BOTTOM_MARGIN);
			return amount(context, "Enter bottom margin ", bottomMargin);
		}

		Record balanceRecord = new Record(bottomMargin);
		balanceRecord.add("Name", BOTTOM_MARGIN);
		balanceRecord.add("Value", bottomMargin.doubleValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getAddressPaddingRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(ADDRESS_PADDING);
		Double addressPadding = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(ADDRESS_PADDING)) {
			Double order = context.getSelection(ADDRESS_PADDING);
			if (order == null) {
				order = context.getDouble();
			}
			addressPadding = order;
			req.setValue(addressPadding);
		}

		if (selection == addressPadding) {
			context.setAttribute(INPUT_ATTR, ADDRESS_PADDING);
			return amount(context, "Enter address padding ", addressPadding);
		}

		Record balanceRecord = new Record(addressPadding);
		balanceRecord.add("Name", ADDRESS_PADDING);
		balanceRecord.add("Value", addressPadding.doubleValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getPaypalIdRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(PAYPALID);
		String paypalId = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(PAYPALID)) {
			String order = context.getSelection(PAYPALID);
			if (order == null) {
				order = context.getString();
			}
			paypalId = order;
			req.setValue(paypalId);
		}

		if (selection == paypalId) {
			context.setAttribute(INPUT_ATTR, PAYPALID);
			return text(context, "Enter paypal email ", paypalId.toString());
		}

		Record balanceRecord = new Record(paypalId);
		balanceRecord.add("Name", PAYPALID);
		balanceRecord.add("Value", paypalId);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getTermsnPaymentAdviceRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(TERMS_N_PAYMENT_ADVICE);
		String paymentAdvice = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(TERMS_N_PAYMENT_ADVICE)) {
			String order = context.getSelection(TERMS_N_PAYMENT_ADVICE);
			if (order == null) {
				order = context.getString();
			}
			paymentAdvice = order;
			req.setValue(paymentAdvice);
		}

		if (selection == paymentAdvice) {
			context.setAttribute(INPUT_ATTR, TERMS_N_PAYMENT_ADVICE);
			return text(context, "Enter Terms and Payment advice ",
					paymentAdvice.toString());
		}

		Record balanceRecord = new Record(paymentAdvice);
		balanceRecord.add("Name", TERMS_N_PAYMENT_ADVICE);
		balanceRecord.add("Value", paymentAdvice);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getLogoAllignmentRequirement(Context context,
			ResultList list, Object selection) {

		Requirement measuresInReq = get(LOGO_ALLIGNMENT);
		Boolean logoAllign = (Boolean) measuresInReq.getValue();
		if (selection == logoAllign) {
			context.setAttribute(INPUT_ATTR, LOGO_ALLIGNMENT);
			logoAllign = !logoAllign;
			measuresInReq.setValue(logoAllign);
		}
		int measures;
		if (logoAllign) {
			measures = LOGO_ALIGNMENT_LEFT;
		} else {
			measures = LOGO_ALIGNMENT_RIGHT;
		}
		Record logo = new Record(logoAllign);
		logo.add("Name", LOGO_ALLIGNMENT);
		logo.add("Value", measures);
		list.add(logo);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result getContactDetailsRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(CONTACT_DETAILS);
		String contactDetails = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(CONTACT_DETAILS)) {
			String order = context.getSelection(CONTACT_DETAILS);
			if (order == null) {
				order = context.getString();
			}
			contactDetails = order;
			req.setValue(contactDetails);
		}

		if (selection == contactDetails) {
			context.setAttribute(INPUT_ATTR, CONTACT_DETAILS);
			return text(context, "Enter your contact details ",
					contactDetails.toString());
		}

		Record balanceRecord = new Record(contactDetails);
		balanceRecord.add("Name", CONTACT_DETAILS);
		balanceRecord.add("Value", contactDetails);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getLogoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(LOGO);
		String logo = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(LOGO)) {
			String order = context.getSelection(LOGO);
			if (order == null) {
				order = context.getString();
			}
			logo = order;
			req.setValue(logo);
		}

		if (selection == logo) {
			context.setAttribute(INPUT_ATTR, LOGO);
			return text(context, "Add logo ", logo.toString());
		}

		Record balanceRecord = new Record(logo);
		balanceRecord.add("Name", LOGO);
		balanceRecord.add("Value", logo);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getInvoiceTemplateRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(INVOICE_TEMPLATE);
		String invoiceTemplate = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(INVOICE_TEMPLATE)) {
			String order = context.getSelection(INVOICE_TEMPLATE);
			if (order == null) {
				order = context.getString();
			}
			invoiceTemplate = order;
			req.setValue(invoiceTemplate);
		}

		if (selection == invoiceTemplate) {
			context.setAttribute(INPUT_ATTR, INVOICE_TEMPLATE);
			return text(context, "Add logo ", invoiceTemplate.toString());
		}

		Record balanceRecord = new Record(invoiceTemplate);
		balanceRecord.add("Name", INVOICE_TEMPLATE);
		balanceRecord.add("Value", invoiceTemplate);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result getCreditTemplateRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(CREDIT_TEMPLATE);
		String creditTemplate = req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(CREDIT_TEMPLATE)) {
			String order = context.getSelection(CREDIT_TEMPLATE);
			if (order == null) {
				order = context.getString();
			}
			creditTemplate = order;
			req.setValue(creditTemplate);
		}

		if (selection == creditTemplate) {
			context.setAttribute(INPUT_ATTR, CREDIT_TEMPLATE);
			return text(context, "Credit Note Template ",
					creditTemplate.toString());
		}

		Record balanceRecord = new Record(creditTemplate);
		balanceRecord.add("Name", CREDIT_TEMPLATE);
		balanceRecord.add("Value", creditTemplate);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

	private Result showTaxNumberRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(SHOW_TAX);
		Boolean creditTemplate = req.getValue();

		if (selection == creditTemplate) {
			context.setAttribute(INPUT_ATTR, SHOW_TAX);
			creditTemplate = !creditTemplate;
			req.setValue(creditTemplate);
		}

		Record balanceRecord = new Record(creditTemplate);
		balanceRecord.add("Name", SHOW_TAX);
		balanceRecord.add("Value", creditTemplate.booleanValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result showColumnHeadingsRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(SHOW_COLUMN_HEADINGS);
		Boolean columnHeadings = req.getValue();

		if (selection == columnHeadings) {
			context.setAttribute(INPUT_ATTR, SHOW_COLUMN_HEADINGS);
			columnHeadings = !columnHeadings;
			req.setValue(columnHeadings);
		}

		Record balanceRecord = new Record(columnHeadings);
		balanceRecord.add("Name", SHOW_COLUMN_HEADINGS);
		balanceRecord.add("Value", columnHeadings.booleanValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result showUnitPriceQuantityRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(SHOW_UNITPRICE_N_QUANTITY);
		Boolean unitPrice = req.getValue();

		if (selection == unitPrice) {
			context.setAttribute(INPUT_ATTR, SHOW_UNITPRICE_N_QUANTITY);
			unitPrice = !unitPrice;
			req.setValue(unitPrice);
		}

		Record balanceRecord = new Record(unitPrice);
		balanceRecord.add("Name", SHOW_UNITPRICE_N_QUANTITY);
		balanceRecord.add("Value", unitPrice.booleanValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result showTaxRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(SHOW_TAX);
		Boolean showTax = req.getValue();

		if (selection == showTax) {
			context.setAttribute(INPUT_ATTR, SHOW_TAX);
			showTax = !showTax;
			req.setValue(showTax);
		}

		Record balanceRecord = new Record(showTax);
		balanceRecord.add("Name", SHOW_TAX);
		balanceRecord.add("Value", showTax.booleanValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result showVatRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(SHOW_VAT);
		Boolean showVat = req.getValue();

		if (selection == showVat) {
			context.setAttribute(INPUT_ATTR, SHOW_VAT);
			showVat = !showVat;
			req.setValue(showVat);
		}

		Record balanceRecord = new Record(showVat);
		balanceRecord.add("Name", SHOW_VAT);
		balanceRecord.add("Value", showVat.booleanValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result showRegisteredAddressRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(SHOW_REG_ADDRESS);
		Boolean regAddress = req.getValue();

		if (selection == regAddress) {
			context.setAttribute(INPUT_ATTR, SHOW_REG_ADDRESS);
			regAddress = !regAddress;
			req.setValue(regAddress);
		}

		Record balanceRecord = new Record(regAddress);
		balanceRecord.add("Name", SHOW_REG_ADDRESS);
		balanceRecord.add("Value", regAddress.booleanValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result showLogoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(SHOW_LOGO);
		Boolean showLogo = req.getValue();

		if (selection == showLogo) {
			context.setAttribute(INPUT_ATTR, SHOW_LOGO);
			showLogo = !showLogo;
			req.setValue(showLogo);
		}

		Record balanceRecord = new Record(showLogo);
		balanceRecord.add("Name", SHOW_LOGO);
		balanceRecord.add("Value", showLogo.booleanValue());
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;
	}

}
