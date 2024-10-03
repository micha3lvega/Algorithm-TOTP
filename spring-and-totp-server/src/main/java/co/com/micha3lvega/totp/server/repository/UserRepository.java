package co.com.micha3lvega.totp.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.com.micha3lvega.totp.server.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	boolean existsByUsername(String username);

}
