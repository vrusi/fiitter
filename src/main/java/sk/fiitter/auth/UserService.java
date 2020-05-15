package sk.fiitter.auth;

import sk.fiitter.model.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
