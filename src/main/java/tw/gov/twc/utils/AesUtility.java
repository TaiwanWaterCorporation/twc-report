package tw.gov.twc.utils;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AesUtility {
	
	private static byte[] key;
	private final static int GCM_IV_LENGTH = 12;

	public AesUtility() {
	}

	public static String decrypt(String encryptData) {
		if(encryptData == null)
			return null;
		
		try {
			key = "/t0MxqpcyyQkB3lQcVhSVQ==".getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SecretKeySpec spec = new SecretKeySpec(key, "AES");
		Cipher cipher;
		byte[] original = new byte[0];
		byte[] deBase64 = Base64.decodeBase64(encryptData);
		AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, deBase64, 0, GCM_IV_LENGTH);
		
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, spec, gcmIv);
			original = cipher.doFinal(deBase64, GCM_IV_LENGTH, deBase64.length - GCM_IV_LENGTH);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(original, StandardCharsets.UTF_8);
	}

}