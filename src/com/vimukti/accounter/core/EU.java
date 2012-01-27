package com.vimukti.accounter.core;

import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EU {
	private static Map<Long, Cipher> eCiperMap = new HashMap<Long, Cipher>();
	private static Map<Long, Cipher> dCiperMap = new HashMap<Long, Cipher>();

	private static byte[] encryptCompanyKey(String password, byte[] key)
			throws InvalidKeyException, Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password));
		byte[] doFinal = cipher.doFinal(key);
		return doFinal;
	}

	private static byte[] decryptCompanyKey(String password, byte[] companyKey)
			throws Exception, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(password));
		byte[] bytes = cipher.doFinal(companyKey);
		return bytes;
	}

	private static SecretKeySpec getSecretKeySpec(String pass) throws Exception {
		byte[] salt = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 1024, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKeySpec skeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
		return skeySpec;
	}

	private static synchronized void setEncryptionCipher(String password,
			byte[] companyKey) throws Exception {
		Long id = AccounterThreadLocal.get().getID();
		if (eCiperMap.get(id) != null && dCiperMap.get(id) != null) {
			return;
		}
		init(decryptCompanyKey(password, companyKey));
	}

	private static void init(byte[] encryptCompanyKey) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(encryptCompanyKey, "AES");
		Cipher eCipher = Cipher.getInstance("AES");
		eCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		Cipher dCipher = Cipher.getInstance("AES");
		dCipher.init(Cipher.DECRYPT_MODE, skeySpec);
		Long id = AccounterThreadLocal.get().getID();
		eCiperMap.put(id, eCipher);
		dCiperMap.put(id, dCipher);
	}

	public byte[] changeCompanyKey(String oldPwd, String newPwd,
			byte[] oldCompanyKey) throws Exception {
		byte[] decryptCompanyKey = decryptCompanyKey(oldPwd, oldCompanyKey);
		byte[] encryptCompanyKey = encryptCompanyKey(newPwd, decryptCompanyKey);
		init(decryptCompanyKey);
		return encryptCompanyKey;
	}

	public static void initEncryption(Company company, String password) {
		try {
			byte[] key = company.getAesEncryptionKey();
			if (key == null) {
				KeyGenerator generator = KeyGenerator.getInstance("AES");
				generator.init(128);
				byte[] encoded = generator.generateKey().getEncoded();
				byte[] encryptCompanyKey = encryptCompanyKey(password, encoded);
				key = encryptCompanyKey;
			}
			setEncryptionCipher(password, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] d(byte[] in) {
		User user = AccounterThreadLocal.get();
		Cipher cipher = dCiperMap.get(user.getID());
		if (cipher == null) {
			return in;
		}
		try {
			return cipher.doFinal(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] e(byte[] in) {
		User user = AccounterThreadLocal.get();
		Cipher cipher = eCiperMap.get(user.getID());
		if (cipher == null) {
			return in;
		}
		try {
			return cipher.doFinal(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void removeEncryption() {
		User user = AccounterThreadLocal.get();
		if (user != null) {
			removeEncryption(user.getID());
		}
	}

	public static void removeEncryption(Long userId) {
		if (userId == null) {
			return;
		}

		eCiperMap.remove(userId);
		dCiperMap.remove(userId);
	}
}
