package co.com.micha3lvega.totp.server.services;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.com.micha3lvega.totp.server.model.User;
import co.com.micha3lvega.totp.server.repository.UserRepository;
import co.com.micha3lvega.totp.server.util.TotpSecretKeyGenerator;

/**
 * Servicio que maneja las operaciones relacionadas con los usuarios. Proporciona métodos para crear un nuevo usuario, iniciar sesión y generar códigos TOTP.
 */
@Service
public class UserServices {

	private UserRepository repository;
	private PasswordEncoder encoder;

	/**
	 * Constructor de la clase UserServices.
	 *
	 * @param repository el repositorio de usuarios para acceder a la base de datos.
	 * @param encoder    el codificador de contraseñas utilizado para encriptar las contraseñas de los usuarios.
	 */
	public UserServices(UserRepository repository, PasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}

	/**
	 * Crea un nuevo usuario en la base de datos.
	 *
	 * @param user el objeto User que contiene la información del nuevo usuario.
	 * @return el usuario creado con la clave secreta generada y la contraseña encriptada.
	 * @throws RuntimeException si ya existe un usuario con el mismo nombre de usuario.
	 */
	public User createUser(User user) {
		// Buscar que no exista el usuario
		if (repository.existsByUsername(user.getUsername())) {
			throw new RuntimeException("Ya existe el usuario: " + user.getUsername());
		}

		// Generar secret key
		var secretKey = TotpSecretKeyGenerator.generateSecretKey();
		user.setSecretkey(secretKey);

		// Encriptar password
		user.setPassword(encoder.encode(user.getPassword()));

		// Guardar usuario
		return repository.save(user);
	}

	/**
	 * Inicia sesión de un usuario con su nombre de usuario y contraseña.
	 *
	 * @param username el nombre de usuario del usuario que intenta iniciar sesión.
	 * @param password la contraseña del usuario que intenta iniciar sesión.
	 * @return el objeto User si las credenciales son válidas.
	 * @throws RuntimeException si el usuario no se encuentra o si la contraseña es incorrecta.
	 */
	public User login(String username, String password) {
		var user = repository.findByUsername(username);

		if (user == null) {
			throw new RuntimeException("Usuario no encontrado");
		}

		if (!encoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Contraseña incorrecta");
		}

		// actualizar secret key
		return updateSecretKey(user.getId());

	}

	/**
	 * Actualiza la clave secreta (secret key) del usuario.
	 *
	 * Este método verifica si el usuario existe en el repositorio. Si existe, genera una nueva clave secreta utilizando el generador de claves secretas y la guarda en el repositorio.
	 *
	 * @param userid el ID del usuario cuyo secret key será actualizado.
	 * @return el objeto User actualizado con la nueva clave secreta.
	 * @throws IllegalArgumentException si el usuario es nulo o si no existe en el repositorio.
	 */
	public User updateSecretKey(String userid) {

		if (userid == null || userid.isEmpty()) {
			throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío.");
		}

		// Buscar que el usuario exista
		var optionalUser = repository.findById(userid);

		if (optionalUser.isEmpty()) {
			throw new IllegalArgumentException("El usuario no existe.");
		}

		// Obtener el usuario
		var user = optionalUser.get();

		// Generar nuevo secret key
		user.setSecretkey(TotpSecretKeyGenerator.generateSecretKey());

		return repository.save(user);
	}

	/**
	 * Genera un código TOTP para el usuario.
	 *
	 * @param user el objeto User para el que se generará el código TOTP.
	 * @return el código TOTP actual generado a partir de la clave secreta del usuario.
	 */
	public String generateTotpCode(User user) {
		var totp = new Totp(user.getSecretkey());
		return totp.now(); // Devuelve el código TOTP actual
	}
}
