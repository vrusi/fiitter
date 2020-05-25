package sk.fiitter.service;

import sk.fiitter.model.User;

public interface SecurityService {
    String findLoggedInUsername();

    User findLoggedInUser();

    void autoLogin(String username, String password);
}
