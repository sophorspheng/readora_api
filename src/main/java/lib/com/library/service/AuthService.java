package lib.com.library.service;

import java.util.Map;

import lib.com.library.model.UserModel;

public interface AuthService {
    Map<String, Object> login(String loginIdentifier, String password);
    UserModel register(String name, String email, String password, String role);
}
