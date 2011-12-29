package com.vimukti.accounter.core;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.core.PrintCheque;
import com.vimukti.accounter.web.client.core.Utility;

public class ChequePdfGenerator {
	private static final int DPI = 72;
	private static int height = 40;// pixels
	private static int width = 7;// pixels

	public static String generate(long layoutId, String companyName,
			List<PrintCheque> printCheques) throws Exception {
		ChequeLayout layout = (ChequeLayout) HibernateUtil.getCurrentSession()
				.get(ChequeLayout.class, layoutId);
		if (layout == null) {
			return null;
		}
		Document document = new Document(new Rectangle(0.0f, 0.0f, 595.0f,
				842.0f));
		File file = new File(ServerConfiguration.getTmpDir(),
				SecureUtils.createID() + ".pdf");
		System.out.println("Cheque file " + file.getAbsolutePath());
		PdfWriter instance = PdfWriter.getInstance(document,
				new FileOutputStream(file));
		document.open();
		// document.addTitle("simple test pdf");
		PdfContentByte directContent = instance.getDirectContent();

		Rectangle pageSize = document.getPageSize();
		printAllCheques(document, directContent, pageSize, layout,
				printCheques, companyName);
		document.close();
		return file.getName();
	}

	private static void printAllCheques(Document document,
			PdfContentByte directContent, Rectangle pageSize,
			ChequeLayout layout, List<PrintCheque> printCheques,
			String companyName) throws DocumentException {
		int chequeHieght = (int) getPixel((float) layout.getChequeHeight());
		for (int i = 0; i < printCheques.size();) {
			document.newPage();
			int topPading = (int) pageSize.getHeight();
			for (; i < printCheques.size() && topPading > chequeHieght; i++) {
				PrintCheque printCheque = printCheques.get(i);
				// Name of Payee
				addString(directContent, topPading, printCheque.getPayeeName(),
						layout.getPayeeNameLeft(), layout.getPayeeNameTop(),
						layout.getPayeeNameWidth());

				String numberInWords = Utility.getNumberInWords(printCheque
						.getAmount());
				int a = getAmountLineIndex(numberInWords,
						layout.getAmountWordsLin1Width());
				String line1 = numberInWords.substring(0, a);
				numberInWords = numberInWords.substring(a);
				a = getAmountLineIndex(numberInWords,
						layout.getAmountWordsLin2Width());
				String line2 = numberInWords.substring(0, a);

				// Amount words line1
				addString(directContent, topPading, line1,
						layout.getAmountWordsLin1Left(),
						layout.getAmountWordsLin1Top(),
						layout.getAmountWordsLin1Width());

				// Amount words line2
				addString(directContent, topPading, line2,
						layout.getAmountWordsLin2Left(),
						layout.getAmountWordsLin2Top(),
						layout.getAmountWordsLin2Width());

				// Amount in figure
				addString(directContent, topPading,
						String.valueOf(printCheque.getAmount()),
						layout.getAmountFigLeft(), layout.getAmountFigTop(),
						layout.getAmountFigWidth());

				// Cheque date
				addString(directContent, topPading,
						new FinanceDate(printCheque.getDate()).toString(),
						layout.getChequeDateLeft(), layout.getChequeDateTop(),
						layout.getChequeDateWidth());

				// Company name
				addString(directContent, topPading, companyName,
						layout.getCompanyNameLeft(),
						layout.getCompanyNameTop(),
						layout.getCompanyNameWidth());

				// Signature
				addString(directContent, topPading,
						layout.getAuthorisedSignature(),
						layout.getSignatoryLeft(), layout.getSignatoryTop(),
						layout.getSignatoryWidth());
				topPading -= chequeHieght;
			}
		}
	}

	private static int getAmountLineIndex(String numberInWords,
			double amountWordsLin1Width) {
		String line1 = "";
		float pixel = getPixel((float) amountWordsLin1Width);
		int firstLine = (int) (pixel / width);
		String[] split = numberInWords.split(" ");
		for (int i = 0; i < split.length; i++) {
			String tryl = line1 + split[i];
			if (tryl.length() > firstLine) {
				break;
			}
			line1 = tryl + " ";
		}
		return line1.trim().length();
	}

	private static void addString(PdfContentByte directContent, int pageTop,
			String string, double left, double top, double width)
			throws DocumentException {
		ColumnText text = new ColumnText(directContent);
		setCoOrdinats(text, pageTop, (float) left, (float) top, (float) width);
		text.setAlignment(Element.ALIGN_JUSTIFIED);
		Chunk chunk = new Chunk(string);
		chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_CLIP, 1,
				null);
		text.addText(chunk);
		text.go();
	}

	private static void setCoOrdinats(ColumnText text, int top, float x,
			float y, float w) {
		float xPixel = getPixel(x);
		float yPixel = top - (getPixel(y) + height);
		text.setSimpleColumn(xPixel, yPixel, xPixel + getPixel(w), yPixel
				+ height);
	}

	private static float getPixel(float i) {
		return DPI * i * 100 / 254;
	}
}