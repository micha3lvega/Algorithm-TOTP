package co.com.micha3lvega.totp.server.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TotpSecretKeyGenerator {

	private static final int SECRET_KEY_LENGTH = 40;

	private TotpSecretKeyGenerator() {
		throw new IllegalStateException("Utility class");
	}

	public static String generateSecretKey() {
		var secureRandom = new SecureRandom();
		var randomBytes = new byte[SECRET_KEY_LENGTH];
		secureRandom.nextBytes(randomBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	}

}
