package com.vimukti.accounter.utils;

import java.io.UnsupportedEncodingException;

import org.hibernate.dialect.Encryptor;

import com.vimukti.accounter.core.EU;

public class AccounterEncryptor implements Encryptor {

	private static AccounterEncryptor instance;

	@Override
	public byte[] encrypt(String value) {
		if (value == null) {
			return null;
		}
		if (isActive()) {
			return doEncrypt(value);
		}
		return fromUTF16String(value);
	}

	private byte[] doEncrypt(String value) {
		byte[] in = fromUTF16String(value);
		in = EU.e(in);
		return in;
	}

	private boolean isActive() {
		return true;
	}

	@Override
	public String decrypt(byte[] value) {
		if (value == null) {
			return null;
		}
		if (isActive()) {
			return doDecrypt(value);
		}
		return toUTF16String(value);
	}

	private String toUTF16String(byte[] value) {
		try {
			String string = new String(value, "UTF-16");
			return string;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] fromUTF16String(String value) {
		try {
			byte[] bytes = value.getBytes("UTF-16");
			return bytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String doDecrypt(byte[] value) {
		byte[] in = value;
		in = EU.d(value);
		return toUTF16String(in);
	}

	public static AccounterEncryptor getInstance() {
		if (instance == null) {
			instance = new AccounterEncryptor();
		}
		return instance;
	}

}
