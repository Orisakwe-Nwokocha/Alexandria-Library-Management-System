package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.dtos.requests.LoginRequest;
import africa.Semicolon.alexandria.dtos.requests.LogoutRequest;
import africa.Semicolon.alexandria.dtos.requests.RegisterRequest;
import africa.Semicolon.alexandria.dtos.responses.LoginResponse;
import africa.Semicolon.alexandria.dtos.responses.LogoutResponse;
import africa.Semicolon.alexandria.dtos.responses.RegisterResponse;

public interface UserServices {
    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    LogoutResponse logout(LogoutRequest logOutRequest);

}
