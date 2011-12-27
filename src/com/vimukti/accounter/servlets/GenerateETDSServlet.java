package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientETDSFilling;
import com.vimukti.accounter.web.server.FinanceTool;

public class GenerateETDSServlet extends BaseServlet {

	/**
	 * this servlet is used to generate the eTDs filling text file
	 */
	private static final long serialVersionUID = 1L;

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
			List<ClientETDSFilling> etdsList = financetool.getEtdsList(
					Integer.parseInt(formNo), Integer.parseInt(quater),
					Integer.parseInt(startYear), Integer.parseInt(endYear),
					getCompany(request).getId());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		generateTextFile(request, response, companyName);

	}

	private void generateTextFile(HttpServletRequest request,
			HttpServletResponse response, String companyName) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
