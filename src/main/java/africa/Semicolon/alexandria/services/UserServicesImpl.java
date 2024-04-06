package africa.Semicolon.alexandria.services;


import africa.Semicolon.alexandria.data.constants.Role;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.Users;
import africa.Semicolon.alexandria.dtos.requests.*;
import africa.Semicolon.alexandria.dtos.responses.*;
import africa.Semicolon.alexandria.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static africa.Semicolon.alexandria.data.constants.Role.LIBRARIAN;
import static africa.Semicolon.alexandria.data.constants.Role.MEMBER;
import static africa.Semicolon.alexandria.utils.Cleaner.lowerCaseValueOf;
import static africa.Semicolon.alexandria.utils.Cryptography.isMatches;
import static africa.Semicolon.alexandria.utils.Mapper.*;

@Service
public class UserServicesImpl implements UserServices {
    @Autowired
    private Users users;
    @Autowired
    private BookServices bookServices;
    @Autowired
    private BorrowServices borrowServices;

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

    @Override
    public AddBookResponse addBook(AddBookRequest addBookRequest) {
        User librarian = findUserBy(addBookRequest.getUsername());
        validateLoginStatusOf(librarian);
        validate(librarian, LIBRARIAN, "Only valid librarians are authorized to perform this action");
        return bookServices.addBookWith(addBookRequest);
    }

    @Override
    public BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest) {
        User member = findUserBy(borrowBookRequest.getUsername());
        validateLoginStatusOf(member);
        validate(member, MEMBER, "Only valid members are authorized to perform this action");
        return borrowServices.borrowBookWith(borrowBookRequest, member);
    }


    private void validate(User user, Role authorizedRole, String errorMessage) {
        boolean isValidRole = user.getRole().equals(authorizedRole);
        if (!isValidRole) throw new UnauthorizedException(errorMessage);
    }

    private void validateLoginStatusOf(User user) {
        if (!user.isLoggedIn()) throw new IllegalUserStateException("User is not logged in");
    }

    private User findUserBy(String username) {
        return users.findByUsername(lowerCaseValueOf(username))
                .orElseThrow(() -> new UserNotFoundException(String.format("User with '%s' not found", username)));
    }

    private void validate(RegisterRequest registerRequest) {
        String username = lowerCaseValueOf(registerRequest.getUsername());
        boolean userExists = users.existsByUsername(username);
        if (userExists) throw new UserExistsException(String.format("%s already exists", username));
    }
}
