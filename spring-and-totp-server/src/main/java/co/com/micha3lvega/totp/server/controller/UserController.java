package co.com.micha3lvega.totp.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.com.micha3lvega.totp.server.dto.SignUpDTO;
import co.com.micha3lvega.totp.server.model.User;
import co.com.micha3lvega.totp.server.services.UserServices;
import jakarta.validation.Valid;

@RestController
public class UserController {

	private UserServices userServices;

	public UserController(UserServices userServices) {
		this.userServices = userServices;
	}

	@PostMapping("/signup")
	public User signup(@RequestBody @Valid SignUpDTO signUpDto) {

		var user = User.builder().username(signUpDto.getUsername())
				.password(signUpDto.getPassword()).build();
		user = userServices.createUser(user);
		user.setPassword(null); // no retornar el password
		return user;

	}

}
