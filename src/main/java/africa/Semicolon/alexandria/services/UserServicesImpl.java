package africa.Semicolon.alexandria.services;


import africa.Semicolon.alexandria.data.constants.Role;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.Users;
import africa.Semicolon.alexandria.dtos.requests.LoginRequest;
import africa.Semicolon.alexandria.dtos.requests.LogoutRequest;
import africa.Semicolon.alexandria.dtos.requests.RegisterRequest;
import africa.Semicolon.alexandria.dtos.responses.LoginResponse;
import africa.Semicolon.alexandria.dtos.responses.LogoutResponse;
import africa.Semicolon.alexandria.dtos.responses.RegisterResponse;
import africa.Semicolon.alexandria.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static africa.Semicolon.alexandria.data.constants.Role.LIBRARIAN;
import static africa.Semicolon.alexandria.utils.Cleaner.lowerCaseValueOf;
import static africa.Semicolon.alexandria.utils.Cryptography.isMatches;
import static africa.Semicolon.alexandria.utils.Mapper.*;

@Service
public class UserServicesImpl implements UserServices {
    @Autowired
    private Users users;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        validate(registerRequest);
        User newUser = map(registerRequest);
        User savedUser = users.save(newUser);
        return mapRegisterResponseWith(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User foundUser = findUserBy(loginRequest.getUsername());
        if (!isMatches(loginRequest, foundUser)) throw new IncorrectPasswordException("Password is not correct");
        foundUser.setLoggedIn(true);
        User savedUser = users.save(foundUser);
        return mapLoginResponseWith(savedUser);
    }

    @Override
    public LogoutResponse logout(LogoutRequest logOutRequest) {
        User foundUser = findUserBy(logOutRequest.getUsername());
        foundUser.setLoggedIn(false);
        User savedUser = users.save(foundUser);
        return mapLogoutResponseWith(savedUser);
    }


//    private void validate(User user) {
//        boolean isAdmin = user.getRole().equals(LIBRARIAN);
//        if (!isAdmin) throw new InvalidUserRoleException("User is not a valid admin");
//    }

    private void validateLoginStatusOf(User user) {
        if (!user.isLoggedIn()) throw new IllegalUserStateException("User is not logged in");
    }

    private User findUserBy(String username) {
        username = lowerCaseValueOf(username);
        User foundUser = users.findByUsername(username);
        if (foundUser == null) throw new UserNotFoundException(String.format("User with '%s' not found", username));
        return foundUser;
    }

    private void validateUniqueUsername(RegisterRequest registerRequest) {
        String username = lowerCaseValueOf(registerRequest.getUsername());
        boolean userExists = users.existsByUsername(username);
        if (userExists) throw new UserExistsException(String.format("%s already exists", username));
    }

    private void validateBlank(RegisterRequest registerRequest) {
        boolean isBlank = registerRequest.getUsername().isBlank()
                || registerRequest.getPassword().isBlank()
                || registerRequest.getRole().isBlank();
        if (isBlank) throw new InvalidArgumentException("Registration details cannot be blank");
    }

    private void validate(RegisterRequest registerRequest) {
        validateBlank(registerRequest);
        validateUniqueUsername(registerRequest);
    }
}
