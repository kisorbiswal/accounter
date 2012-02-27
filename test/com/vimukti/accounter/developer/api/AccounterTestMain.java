package com.vimukti.accounter.developer.api;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.developer.api.core.ITest;
import com.vimukti.accounter.developer.api.report.ListsTest;

public class AccounterTestMain {
	private static List<ITest> tests = new ArrayList<ITest>();

	public static void main(String[] args) {
		loadTestCases();
		test();
	}

	private static void test() {
		for (ITest test : tests) {
			try {
				test.before();
				test.test();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void loadTestCases() {
		tests.add(new ListsTest());
	}
}
