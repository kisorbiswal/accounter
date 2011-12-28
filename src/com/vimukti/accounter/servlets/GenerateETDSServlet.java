package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.Form26QAnnexureGenerator;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientETDSFilling;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.server.FinanceTool;

public class GenerateETDSServlet extends BaseServlet {

	/**
	 * this servlet is used to generate the eTDs filling text file
	 */
	private static final long serialVersionUID = 1L;
	List<ClientETDSFilling> etdsList = null;
	private List<ClientTDSDeductorMasters> tdsDeductorMasterDetails;
	private List<ClientTDSResponsiblePerson> responsiblePersonDetails;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String companyName = getCompanyName(request);
		if (companyName == null)
			return;

		String formNo = request.getParameter("formNo");
		String quater = request.getParameter("quater");
		String startYear = request.getParameter("startYear");
		String endYear = request.getParameter("endYear");

		FinanceTool financetool = new FinanceTool();

		try {
			etdsList = financetool.getEtdsList(Integer.parseInt(formNo),
					Integer.parseInt(quater), Integer.parseInt(startYear),
					Integer.parseInt(endYear), getCompany(request).getId());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}

		try {
			tdsDeductorMasterDetails = financetool
					.getTDSDeductorMasterDetails(getCompany(request).getId());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		try {
			responsiblePersonDetails = financetool
					.getResponsiblePersonDetails(getCompany(request).getId());
		} catch (DAOException e) {
			e.printStackTrace();
		}

		generateTextFile(request, response, companyName, etdsList,
				tdsDeductorMasterDetails, responsiblePersonDetails);

	}

	private void generateTextFile(HttpServletRequest request,
			HttpServletResponse response, String companyName,
			List<ClientETDSFilling> etdsList,
			List<ClientTDSDeductorMasters> tdsDeductorMasterDetails2,
			List<ClientTDSResponsiblePerson> responsiblePersonDetails2)
			throws IOException {

		Form26QAnnexureGenerator form26Q = new Form26QAnnexureGenerator(
				tdsDeductorMasterDetails2, responsiblePersonDetails2);

		String generateFile = form26Q.generateFile();

		ServletOutputStream op = response.getOutputStream();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ "write.txt" + "\"");
		byte[] bytes = generateFile.getBytes();
		op.write(bytes);
		op.flush();

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
