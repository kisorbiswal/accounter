package com.vimukti.accounter.core;

import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.vimukti.accounter.main.CipherThreadLocal;

public class EU {
	private static Map<String, ByteArrayWrapper> keys = new HashMap<String, EU.ByteArrayWrapper>();

	public synchronized static byte[] d(byte[] in) {
		CipherCouple cipherCouple = CipherThreadLocal.get();
		if (cipherCouple == null) {
			return in;
		}
		Cipher cipher = cipherCouple.dCipher;
		try {
			return cipher.doFinal(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized static byte[] e(byte[] in) {
		CipherCouple cipherCouple = CipherThreadLocal.get();
		if (cipherCouple == null) {
			return in;
		}
		Cipher cipher = cipherCouple.eCipher;
		try {
			return cipher.doFinal(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] generatePBS(String password) {
		byte[] salt = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1024,
					128);
			SecretKey tmp = factory.generateSecret(spec);
			return tmp.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] generateSymetric() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128);
		byte[] encoded = generator.generateKey().getEncoded();
		return encoded;
	}

	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] bytes = cipher.doFinal(data);
		return bytes;
	}

	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] bytes = cipher.doFinal(data);
		return bytes;
	}

	public static class ByteArrayWrapper {
		byte[] data;

		public ByteArrayWrapper(byte[] s2) {
			data = s2;
		}
	}

	public static class CipherCouple {
		Cipher eCipher;
		Cipher dCipher;
	}

	public static void storeKey(byte[] s2, String emailId) {
		ByteArrayWrapper byteArrayWrapper = new ByteArrayWrapper(s2);
		keys.put(emailId, byteArrayWrapper);
	}

	public static byte[] generateD2(String password, String emailId)
			throws Exception {
		byte[] s1 = EU.generatePBS(password);
		byte[] s2 = EU.generateSymetric();
		byte[] d2 = EU.encrypt(s1, s2);
		EU.storeKey(s2, emailId);
		return d2;
	}

	/**
	 * s2
	 * 
	 * @param emailID
	 * @return
	 */
	public static byte[] getKey(String emailID) {
		ByteArrayWrapper byteArrayWrapper = keys.get(emailID);
		if (byteArrayWrapper == null) {
			return null;
		}
		return byteArrayWrapper.data;
	}

	public static void removeCipher() {
		CipherThreadLocal.set(null);
	}

	public static void createCipher(byte[] userSecret, byte[] d2, String emailId)
			throws Exception {
		byte[] s2 = getKey(emailId);
		if (s2 == null) {
			return;
		}
		byte[] s1 = decrypt(d2, s2);
		byte[] key = decrypt(userSecret, s1);

		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher eCipher = Cipher.getInstance("AES");
		eCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		Cipher dCipher = Cipher.getInstance("AES");
		dCipher.init(Cipher.DECRYPT_MODE, skeySpec);
		CipherCouple cipherCouple = new CipherCouple();
		cipherCouple.dCipher = dCipher;
		cipherCouple.eCipher = eCipher;
		CipherThreadLocal.set(cipherCouple);
	}
}
