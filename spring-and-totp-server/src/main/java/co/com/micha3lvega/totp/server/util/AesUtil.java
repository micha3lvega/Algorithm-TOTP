package co.com.micha3lvega.totp.server.util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utilidad para operaciones de cifrado y descifrado utilizando el algoritmo AES. Esta clase proporciona métodos para generar una clave secreta, cifrar y descifrar datos utilizando el modo AES con CBC y relleno PKCS5Padding. Además, soporta múltiples ciclos de cifrado o descifrado si se requiere mayor complejidad.
 */
public class AesUtil {

	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	/**
	 * Constructor privado para evitar la instanciación de esta clase utilitaria.
	 */
	private AesUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Cifra un valor utilizando AES con una clave secreta y un IV (vector de inicialización).
	 *
	 * @param value         el valor en texto plano a cifrar.
	 * @param secretKeyText la clave secreta en formato Base64.
	 * @param ivText        el IV en formato Base64.
	 * @return el valor cifrado en formato Base64.
	 * @throws RuntimeException si ocurre un error durante el cifrado.
	 */
	public static String encrypt(String value, String secretKeyText, String ivText) {
		try {
			SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKeyText),
					ALGORITHM);
			var iv = Base64.getDecoder().decode(ivText);
			var cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
			var encryptedBytes = cipher.doFinal(value.getBytes());
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Error al cifrar: " + e.getMessage(), e);
		}
	}

	/**
	 * Descifra un valor utilizando AES con una clave secreta y un IV (vector de inicialización).
	 *
	 * @param encryptedValue el valor cifrado en formato Base64.
	 * @param secretKeyText  la clave secreta en formato Base64.
	 * @param ivText         el IV en formato Base64.
	 * @return el valor descifrado en texto plano.
	 * @throws RuntimeException si ocurre un error durante el descifrado.
	 */
	public static String decrypt(String encryptedValue, String secretKeyText, String ivText) {
		try {
			SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKeyText),
					ALGORITHM);
			var iv = Base64.getDecoder().decode(ivText);
			var cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
			var decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
			return new String(decryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Error al descifrar: " + e.getMessage(), e);
		}
	}

	/**
	 * Cifra un valor utilizando múltiples ciclos de cifrado AES con la misma clave secreta e IV.
	 *
	 * @param valueToEncrypt el valor en texto plano a cifrar.
	 * @param secretKey      la clave secreta en formato Base64.
	 * @param iv             el IV en formato Base64.
	 * @param cycles         el número de ciclos de cifrado.
	 * @return el valor cifrado tras todos los ciclos.
	 */
	public static String encrypt(String valueToEncrypt, String secretKey, String iv, int cycles) {
		var encryptedValue = valueToEncrypt;
		for (var i = 0; i < cycles; i++) {
			encryptedValue = encrypt(encryptedValue, secretKey, iv);
		}
		return encryptedValue;
	}

	/**
	 * Descifra un valor utilizando múltiples ciclos de descifrado AES con la misma clave secreta e IV.
	 *
	 * @param valueToDecrypt el valor cifrado en formato Base64 a descifrar.
	 * @param secretKey      la clave secreta en formato Base64.
	 * @param iv             el IV en formato Base64.
	 * @param cycles         el número de ciclos de descifrado.
	 * @return el valor descifrado tras todos los ciclos.
	 */
	public static String decrypt(String valueToDecrypt, String secretKey, String iv, int cycles) {
		var decryptedValue = valueToDecrypt;
		for (var i = 0; i < cycles; i++) {
			decryptedValue = decrypt(decryptedValue, secretKey, iv);
		}
		return decryptedValue;
	}

	/**
	 * Genera una clave secreta AES de 256 bits codificada en Base64.
	 *
	 * @return la clave secreta generada en formato Base64.
	 * @throws Exception si ocurre un error durante la generación de la clave.
	 */
	public static String generateSecretKey() throws Exception {
		var keyGen = KeyGenerator.getInstance(ALGORITHM);
		keyGen.init(256);
		var secretKey = keyGen.generateKey();
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	/**
	 * Genera un vector de inicialización (IV) de 16 bytes y lo devuelve como una cadena hexadecimal.
	 *
	 * @return el IV generado en formato hexadecimal.
	 */
	public static String generateIVAsHexString() {
		var random = new SecureRandom();
		var iv = new byte[16];
		random.nextBytes(iv);
		return bytesToHex(iv);
	}

	/**
	 * Convierte un array de bytes en una cadena de texto hexadecimal.
	 *
	 * @param bytes el array de bytes a convertir.
	 * @return la cadena de texto en formato hexadecimal.
	 */
	private static String bytesToHex(byte[] bytes) {
		var hexString = new StringBuilder(2 * bytes.length);
		for (byte b : bytes) {
			var hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
