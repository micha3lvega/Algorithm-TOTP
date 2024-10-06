package co.com.micha3lvega.totp.server.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO implements Serializable {

	private static final long serialVersionUID = 163694715622952787L;

	@NotEmpty(message = "El username es obligatorio")
	private String username;

	@NotEmpty(message = "la contraseña es obligatoria")
	private String password;

}
