package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.dtos.requests.BorrowBookRequest;
import africa.Semicolon.alexandria.dtos.responses.BorrowBookResponse;

public interface BorrowServices {
    BorrowBookResponse borrowBookWith(BorrowBookRequest borrowBookRequest, User borrower);
}
