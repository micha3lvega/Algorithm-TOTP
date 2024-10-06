package co.com.micha3lvega.totp.server.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utilidad para operaciones de cifrado y descifrado utilizando el algoritmo AES. Esta clase proporciona métodos para generar una clave secreta, cifrar y descifrar datos.
 */
public class AesUtil {

	/**
	 * Genera una clave secreta AES de 256 bits codificada en Base64.
	 *
	 * @return la clave secreta generada en formato Base64.
	 * @throws Exception si ocurre un error durante la generación de la clave.
	 */
	public static String generateSecretKey() throws Exception {
		var keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256);
		var secretKey = keyGen.generateKey();
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	/**
	 * Cifra los datos múltiples veces usando el algoritmo AES.
	 *
	 * @param data      los datos a cifrar.
	 * @param secretKey la clave secreta en formato Base64.
	 * @param cycles    el número de veces que se aplicará el cifrado.
	 * @return los datos cifrados en formato Base64.
	 * @throws Exception si ocurre un error durante el cifrado.
	 */
	public static String encrypt(String data, String secretKey, int cycles) throws Exception {
		for (var i = 0; i < cycles; i++) {
			data = encrypt(data, secretKey);
		}
		return data;
	}

	/**
	 * Cifra los datos usando el algoritmo AES.
	 *
	 * @param data      los datos a cifrar.
	 * @param secretKey la clave secreta en formato Base64.
	 * @return los datos cifrados en formato Base64.
	 * @throws Exception si ocurre un error durante el cifrado.
	 */
	public static String encrypt(String data, String secretKey) throws Exception {
		var keySpec = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");
		var cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		var encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	/**
	 * Descifra los datos múltiples veces usando el algoritmo AES.
	 *
	 * @param encryptedData los datos cifrados en formato Base64.
	 * @param secretKey     la clave secreta en formato Base64.
	 * @param cycles        el número de veces que se aplicará el descifrado.
	 * @return los datos descifrados como texto plano.
	 * @throws Exception si ocurre un error durante el descifrado.
	 */
	public static String decrypt(String encryptedData, String secretKey, int cycles)
			throws Exception {
		for (var i = 0; i < cycles; i++) {
			encryptedData = decrypt(encryptedData, secretKey);
		}
		return encryptedData;
	}

	/**
	 * Descifra los datos usando el algoritmo AES.
	 *
	 * @param encryptedData los datos cifrados en formato Base64.
	 * @param secretKey     la clave secreta en formato Base64.
	 * @return los datos descifrados como texto plano.
	 * @throws Exception si ocurre un error durante el descifrado.
	 */
	public static String decrypt(String encryptedData, String secretKey) throws Exception {
		var keySpec = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");
		var cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		var decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedData, "UTF-8");
	}
}
