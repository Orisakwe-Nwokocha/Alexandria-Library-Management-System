package africa.Semicolon.alexandria.utils;

import africa.Semicolon.alexandria.data.constants.Role;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.dtos.requests.RegisterRequest;
import africa.Semicolon.alexandria.dtos.responses.LoginResponse;
import africa.Semicolon.alexandria.dtos.responses.LogoutResponse;
import africa.Semicolon.alexandria.dtos.responses.RegisterResponse;
import africa.Semicolon.alexandria.exceptions.InvalidArgumentException;

import static africa.Semicolon.alexandria.utils.Cleaner.lowerCaseValueOf;
import static africa.Semicolon.alexandria.utils.Cleaner.upperCaseValueOf;
import static africa.Semicolon.alexandria.utils.Cryptography.encode;

public final class Mapper {
    public static User map(RegisterRequest registerRequest) {
        String username = lowerCaseValueOf(registerRequest.getUsername());
        String password = encode(registerRequest.getPassword());
        String role = upperCaseValueOf(registerRequest.getRole());

        User user = new User();
        try {
            user.setRole(Role.valueOf(role));
        }
        catch (IllegalArgumentException e) {
            throw new InvalidArgumentException("Invalid role: " + role);
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setName(registerRequest.getName());
        return user;
    }
    public static RegisterResponse mapRegisterResponseWith(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(user.getId());
        registerResponse.setUsername(user.getUsername());
        return registerResponse;
    }

    public static LoginResponse mapLoginResponseWith(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setLoggedIn(true);
        return loginResponse;
    }

    public static LogoutResponse mapLogoutResponseWith(User user) {
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setId(user.getId());
        logoutResponse.setUsername(user.getUsername());
        logoutResponse.setLoggedIn(false);
        return logoutResponse;
    }

}
