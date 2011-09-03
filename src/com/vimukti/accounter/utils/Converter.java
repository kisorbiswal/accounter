package com.vimukti.accounter.utils;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;

import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.vimukti.accounter.core.ITemplate;

public class Converter {
	private Dimension dimension;

	// public static void main(String[] args) throws Exception {
	// Converter converter = new Converter();
	// String htmlFileName =
	// "/home/spider/Desktop/temporary/Editable Invoice.html";
	// File pdfFile = new File("/home/spider/Desktop/kumartest3333.pdf");
	// converter.generatePDF(htmlFileName, pdfFile, PD4Constants.A4,
	// args.length > 2 ? args[2] : null, args.length > 3 ? args[3]
	// : null);
	// System.out.println("done.");
	// }

	public Converter(Dimension dimension) {
		this.dimension = dimension;
	}

	public Converter() {
		this.dimension = PD4Constants.A4;
	}

	/**
	 * this method is used to generate Pdf documents for all reports
	 * 
	 * @param template
	 * @param outputStream
	 * @throws Exception
	 */
	public void generatePdfReports(ITemplate template, OutputStream outputStream)
			throws Exception {

		File pdfTempFile = File.createTempFile("crdedit", ".pdf");
		java.io.FileOutputStream fos = new java.io.FileOutputStream(pdfTempFile);
		try {

			PD4ML pd4ml = new PD4ML();
			System.err.println("PD4ML Obj created");
			pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
			pd4ml.setHtmlWidth(950);
			pd4ml.setPageSize(dimension);
			// page
			// orientation
			// if (fontsDir != null && fontsDir.length() > 0) {
			// pd4ml.useTTF(fontsDir, true);
			// }

			if (template.getHeader() != null
					&& template.getHeader().length() > 0) {
				PD4PageMark header = new PD4PageMark();
				header.setAreaHeight(-1); // autocompute
				header.setHtmlTemplate(template.getHeader()
				/* + template.getBody() */); // autocompute
				pd4ml.setPageHeader(header);
			}

			if (template.getFooter() != null
					&& template.getFooter().length() > 0) {
				PD4PageMark footer = new PD4PageMark();
				footer.setAreaHeight(-1); // autocompute
				footer.setHtmlTemplate(template.getFooter()); // autocompute
				pd4ml.setPageFooter(footer);
			}

			// File htmlFile = File.createTempFile("invioceHtml", ".html");
			// FileOutputStream foshtml = new FileOutputStream(htmlFile);
			// foshtml.write(htmlcontent.getBytes());
			// foshtml.close();
			pd4ml.enableTableBreaks(true);
			pd4ml.enableDebugInfo();
			System.err.println(template.getBody());
			// String templateBody = template.getBody();
			// int count = 0;
			// for (int i = 0; i < templateBody.length(); i++) {
			// if (templateBody.charAt(i) == '/'
			// && templateBody.charAt(i) == 't'
			// && templateBody.charAt(i) == 'r') {
			// count++;
			// if (count % 40 == 0) {
			// templateBody.replace("</tr>", ReportsGenerator
			// .getBodyColumns());
			// }
			// }
			// }
			// pd4ml
			// .render(
			// "file:/home/vimukti80/Desktop/invioceHtml7431158160782287771.html",
			// outputStream == null ? fos : outputStream);

			pd4ml.render(new StringReader(template.getBody()),
					outputStream == null ? fos : outputStream);

		} catch (Exception e) {
			System.err.println("error occured");
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

	}

	/**
	 * this method is used to generate PDF documents for Invoice and Credit Note
	 * Memo
	 * 
	 * @param fileName
	 * @param outputStream
	 * @param reader
	 * @throws Exception
	 */
	public void generatePdfDocuments(String fileName,
			OutputStream outputStream, InputStreamReader reader)
			throws Exception {
		File pdfTempFile = File.createTempFile(fileName.replace(" ", ""),
				".pdf");
		java.io.FileOutputStream fos = new java.io.FileOutputStream(pdfTempFile);
		try {

			PD4ML pd4ml = new PD4ML();
			System.err.println("PD4ML Obj created");
			pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
			pd4ml.setHtmlWidth(950);
			pd4ml.setPageSize(dimension);

			pd4ml.enableTableBreaks(true);
			pd4ml.enableDebugInfo();

			pd4ml.render(reader, outputStream == null ? fos : outputStream);
		} catch (Exception e) {
			System.err.println("error occured");
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

	}

	public File getPdfFile(String fileName, InputStreamReader reader)
			throws Exception {
		File pdfTempFile = File.createTempFile(fileName.replace(" ", ""),
				".pdf");
		java.io.FileOutputStream fos = new java.io.FileOutputStream(pdfTempFile);
		try {

			PD4ML pd4ml = new PD4ML();
			System.err.println("PD4ML Obj created");
			pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
			pd4ml.setHtmlWidth(950);
			pd4ml.setPageSize(dimension);

			pd4ml.enableTableBreaks(true);
			pd4ml.enableDebugInfo();

			pd4ml.render(reader, fos);
			return pdfTempFile;
		} catch (Exception e) {
			System.err.println("error occured");
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		return null;

	}
}