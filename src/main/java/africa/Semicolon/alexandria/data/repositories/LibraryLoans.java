package africa.Semicolon.alexandria.data.repositories;

import africa.Semicolon.alexandria.data.models.LibraryLoan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LibraryLoans extends MongoRepository<LibraryLoan, String> {
}
