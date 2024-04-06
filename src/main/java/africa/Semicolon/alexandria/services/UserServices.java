package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.dto.requests.*;
import africa.Semicolon.alexandria.dto.responses.*;

public interface UserServices {
    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    LogoutResponse logout(LogoutRequest logOutRequest);
    AddBookResponse addBook(AddBookRequest addBookRequest);
    BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest);
    ReturnBookResponse returnBook(ReturnBookRequest returnBookRequest);
}
