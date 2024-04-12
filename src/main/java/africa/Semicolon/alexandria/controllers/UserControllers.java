package africa.Semicolon.alexandria.controllers;

import africa.Semicolon.alexandria.dto.requests.*;
import africa.Semicolon.alexandria.dto.responses.*;
import africa.Semicolon.alexandria.exceptions.AlexandriaAppException;
import africa.Semicolon.alexandria.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/user")
public class UserControllers {
    @Autowired
    private UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, Errors errors) {
        if (errors.hasErrors()) return getValidationErrorMessageOf(errors);
        try {
            RegisterResponse result = userServices.register(registerRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PatchMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, Errors errors) {
        if (errors.hasErrors()) return getValidationErrorMessageOf(errors);
        try {
            LoginResponse result = userServices.login(loginRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PatchMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequest logoutRequest, Errors errors) {
        if (errors.hasErrors()) return getValidationErrorMessageOf(errors);
        try {
            LogoutResponse result = userServices.logout(logoutRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/add-book")
    public ResponseEntity<?> addBook(@Valid @RequestBody AddBookRequest addBookRequest, Errors errors) {
        if (errors.hasErrors()) return getValidationErrorMessageOf(errors);
        try {
            AddBookResponse result = userServices.addBook(addBookRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove-book")
    public ResponseEntity<?> removeBook(@Valid @RequestBody RemoveBookRequest removeBookRequest, Errors errors) {
        if (errors.hasErrors()) return getValidationErrorMessageOf(errors);
        try {
            RemoveBookResponse result = userServices.removeBook(removeBookRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PatchMapping("/borrow-book")
    public ResponseEntity<?> borrowBook(@Valid @RequestBody BorrowBookRequest borrowBookRequest, Errors errors) {
        if (errors.hasErrors()) return getValidationErrorMessageOf(errors);
        try {
            BorrowBookResponse result = userServices.borrowBook(borrowBookRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PatchMapping("/return-book")
    public ResponseEntity<?> returnBook(@Valid @RequestBody ReturnBookRequest returnBookRequest, Errors errors) {
        if (errors.hasErrors()) return getValidationErrorMessageOf(errors);
        try {
            ReturnBookResponse result = userServices.returnBook(returnBookRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    private static ResponseEntity<String> getValidationErrorMessageOf(Errors errors) {
        String defaultMessage = errors.getAllErrors().getFirst().getDefaultMessage();
        return new ResponseEntity<>(String.format("Operation failed: %s", defaultMessage), BAD_REQUEST);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("POST", "PATCH", "DELETE", "GET")
                        .allowedHeaders("*");
            }
        };
    }
}
