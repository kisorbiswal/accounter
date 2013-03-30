package com.vimukti.accounter.text;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.vimukti.accounter.servlets.BaseServlet;

public class MailHookeServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JSONObject jsonObj = requestDataToJSON(req);
		// TODO
		super.doPost(req, resp);
	}

	/**
	 * Parsing Request data to Json Object
	 * 
	 * @param HttpServletRequest
	 */
	private JSONObject requestDataToJSON(HttpServletRequest req) {
		JSONObject jsonObject = null;
		try {
			String line = null;
			StringBuffer jb = new StringBuffer();
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
			jsonObject = JSONObject.fromObject(jb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
