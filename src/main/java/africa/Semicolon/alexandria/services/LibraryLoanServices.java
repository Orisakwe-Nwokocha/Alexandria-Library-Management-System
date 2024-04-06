package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.dto.requests.BorrowBookRequest;
import africa.Semicolon.alexandria.dto.requests.ReturnBookRequest;
import africa.Semicolon.alexandria.dto.responses.BorrowBookResponse;
import africa.Semicolon.alexandria.dto.responses.ReturnBookResponse;

public interface LibraryLoanServices {
    BorrowBookResponse loanBookWith(BorrowBookRequest borrowBookRequest, User member);
    ReturnBookResponse returnBookWith(ReturnBookRequest returnBookRequest, User member);
}
