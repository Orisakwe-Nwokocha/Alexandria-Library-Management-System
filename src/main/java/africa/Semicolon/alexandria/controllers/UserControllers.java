package africa.Semicolon.alexandria.controllers;

import africa.Semicolon.alexandria.dto.requests.*;
import africa.Semicolon.alexandria.dto.responses.*;
import africa.Semicolon.alexandria.exceptions.AlexandriaAppException;
import africa.Semicolon.alexandria.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/user")
public class UserControllers {
    @Autowired
    private UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            RegisterResponse result = userServices.register(registerRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PatchMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse result = userServices.login(loginRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PatchMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        try {
            LogoutResponse result = userServices.logout(logoutRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/add-book")
    public ResponseEntity<ApiResponse> addBook(@Valid @RequestBody AddBookRequest addBookRequest) {
        try {
            AddBookResponse result = userServices.addBook(addBookRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PatchMapping("/borrow-book")
    public ResponseEntity<ApiResponse> borrowBook(@Valid @RequestBody BorrowBookRequest borrowBookRequest) {
        try {
            BorrowBookResponse result = userServices.borrowBook(borrowBookRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PatchMapping("/return-book")
    public ResponseEntity<ApiResponse> returnBook(@Valid @RequestBody ReturnBookRequest returnBookRequest) {
        try {
            ReturnBookResponse result = userServices.returnBook(returnBookRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
}
