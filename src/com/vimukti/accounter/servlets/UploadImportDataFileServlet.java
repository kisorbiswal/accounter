package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class UploadImportDataFileServlet extends BaseServlet {

	/**
	 * used to upload the Statement file and to store the statement records in
	 * that file
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Session session = null;
		JSONObject object = new JSONObject();
		String[] headers = null;
		boolean isHeader = true;
		int noOfLines = 0;
		try {
			HttpSession httpSession = request.getSession();
			Long companyID = (Long) httpSession.getAttribute(COMPANY_ID);
			if (companyID == null)
				return;

			session = HibernateUtil.getCurrentSession();
			MultipartRequest multi = new MultipartRequest(request,
					ServerConfiguration.getTmpDir(), 50 * 1024 * 1024,
					"ISO-8859-1", new DefaultFileRenamePolicy());

			Enumeration<?> files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String fileID = (String) files.nextElement();
				File file = multi.getFile(fileID);
				if (file != null) {
					DataInputStream in = new DataInputStream(
							new FileInputStream(file.getAbsolutePath()));
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));
					String strLine;
					Map<String, JSONArray> headerWithValues = new HashMap<String, JSONArray>();
					JSONObject allRecords = new JSONObject();
					while ((strLine = br.readLine()) != null) {
						String[] values = strLine.split(",");

						if (isHeader) {
							headers = values;
							isHeader = false;
						} else {
							if (values.length == headers.length) {
								noOfLines++;
								for (int i = 0; i < values.length; i++) {
									String value = values[i].trim().replaceAll(
											"\"", "");
									JSONArray array = (JSONArray) headerWithValues
											.get(headers[i]);
									if (array == null) {
										array = new JSONArray();
										array.put(value);
										headerWithValues.put(headers[i], array);
										allRecords.put(headers[i], array);
									} else {
										JSONArray array2 = (JSONArray) allRecords
												.get(headers[i]);
										array2.put(value);
									}
								}
							}
						}
					}
					object.put("fileID", file.getAbsolutePath());
					object.put("first20Records", headerWithValues);
					object.put("noOfRows", noOfLines);
				}
			}
			StringBuilder builder = new StringBuilder(object.toString());
			response.getWriter().print(builder);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
