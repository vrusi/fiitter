package sk.fiitter.service;

import sk.fiitter.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
