package co.com.micha3lvega.totp.server.util;

import java.security.SecureRandom;

import org.jboss.aerogear.security.otp.api.Base32;

/**
 * Generador de claves secretas para TOTP (Time-based One-Time Password). Esta clase proporciona un método para generar una clave secreta aleatoria que puede ser utilizada en el proceso de autenticación TOTP.
 */
public class TotpSecretKeyGenerator {

	private static final int SECRET_KEY_LENGTH = 16;

	/**
	 * Constructor privado para evitar la instanciación de la clase. Lanza una excepción si se intenta instanciar.
	 */
	private TotpSecretKeyGenerator() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Genera una clave secreta aleatoria de 16 bytes.
	 *
	 * @return una cadena que representa la clave secreta en formato Base32.
	 */
	public static String generateSecretKey() {
		var secureRandom = new SecureRandom();
		var randomBytes = new byte[SECRET_KEY_LENGTH];
		secureRandom.nextBytes(randomBytes);
		return Base32.encode(randomBytes);
	}

}
