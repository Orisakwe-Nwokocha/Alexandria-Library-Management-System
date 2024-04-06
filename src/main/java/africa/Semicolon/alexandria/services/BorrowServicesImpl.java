package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.LibraryLoans;
import africa.Semicolon.alexandria.dtos.requests.BorrowBookRequest;
import africa.Semicolon.alexandria.dtos.responses.BorrowBookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowServicesImpl implements BorrowServices {
    @Autowired
    private LibraryLoans libraryLoans;

    @Override
    public BorrowBookResponse borrowBook(BorrowBookRequest borrowBookRequest, User member) {
        return null;
    }
}
