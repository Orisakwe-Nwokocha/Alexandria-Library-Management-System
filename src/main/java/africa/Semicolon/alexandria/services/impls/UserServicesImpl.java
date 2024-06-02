package africa.Semicolon.alexandria.services.impls;


import africa.Semicolon.alexandria.data.constants.Role;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.Users;
import africa.Semicolon.alexandria.dto.requests.*;
import africa.Semicolon.alexandria.dto.responses.*;
import africa.Semicolon.alexandria.exceptions.*;
import africa.Semicolon.alexandria.services.*;
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
    private LibraryLoanServices libraryLoanServices;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpService otpService;


    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        validate(registerRequest);
        User newUser = map(registerRequest);

        if (registerRequest.getOtp() == null || registerRequest.getOtp().isBlank())
            return otpService.generateAndSendOtp(registerRequest.getEmail(), newUser);
        otpService.validate(registerRequest.getOtp(), newUser);

        String template = "registration-template";
        emailService.sendEmail(registerRequest.getEmail(), "Registration Successful",
                template, registerRequest.getUsername());

        newUser = users.save(newUser);
//        User savedUser = users.save(newUser);
        return mapRegisterResponseWith(newUser);
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
        validateLibrarianWith(addBookRequest.getUsername());
        return bookServices.addBookWith(addBookRequest);
    }

    @Override
    public BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest) {
        User member = findUserBy(borrowBookRequest.getUsername());
        validate(member);
        return libraryLoanServices.loanBookWith(borrowBookRequest, member);
    }

    @Override
    public ReturnBookResponse returnBook(ReturnBookRequest returnBookRequest) {
        User member = findUserBy(returnBookRequest.getUsername());
        validate(member);
        return libraryLoanServices.returnBookWith(returnBookRequest, member);
    }

    @Override
    public RemoveBookResponse removeBook(RemoveBookRequest removeBookRequest) {
        validateLibrarianWith(removeBookRequest.getUsername());
        return bookServices.removeBookWith(removeBookRequest);
    }

    private void validate(User member) {
        validateLoginStatusOf(member);
        validate(member, MEMBER, "Only valid members are authorized to perform this action");
    }

    private void validateLibrarianWith(String username) {
        User librarian = findUserBy(username);
        validateLoginStatusOf(librarian);
        validate(librarian, LIBRARIAN, "Only valid librarians are authorized to perform this action");
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
