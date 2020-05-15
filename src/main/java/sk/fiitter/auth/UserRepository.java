package sk.fiitter.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.fiitter.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);
}
