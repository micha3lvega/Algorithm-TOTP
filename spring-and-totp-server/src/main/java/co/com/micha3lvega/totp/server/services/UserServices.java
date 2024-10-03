package co.com.micha3lvega.totp.server.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.com.micha3lvega.totp.server.model.User;
import co.com.micha3lvega.totp.server.repository.UserRepository;
import co.com.micha3lvega.totp.server.util.TotpSecretKeyGenerator;

@Service
public class UserServices {

	private UserRepository repository;
	private PasswordEncoder encoder; // Cambia aquí a PasswordEncoder

	public UserServices(UserRepository repository, PasswordEncoder encoder) { // Cambia aquí también
		this.repository = repository;
		this.encoder = encoder;
	}

	/**
	 * Metodo para crear un usuario
	 *
	 * @param user datos del usuario a crear
	 * @return Objecto con los datos del usuario
	 */
	public User createUser(User user) {

		// Buscar que no exxista el usuario
		if (repository.existsByUsername(user.getUsername())) {
			throw new RuntimeException("Ya existe el usuario: " + user.getUsername());
		}

		// Generar secretkey
		var secretKey = TotpSecretKeyGenerator.generateSecretKey();
		user.setSecretkey(secretKey);

		// Encriptar password
		user.setPassword(encoder.encode(user.getPassword()));

		// Guardar usuario
		return repository.save(user);
	}

	/**
	 * Metodo para iniciar sesión con el usuario
	 *
	 * @param username nombre de usuario
	 * @param password contraseña del usuario
	 *
	 */
	public User login(String username, String password) {

		var user = repository.findByUsername(username);

		if (user == null) {
			throw new RuntimeException("Usuario no encontrado");
		}

		if (!encoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Contraseña incorrecta");
		}

		return user;
	}

}
