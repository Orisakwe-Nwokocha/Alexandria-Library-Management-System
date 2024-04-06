package africa.Semicolon.alexandria.data.repositories;

import africa.Semicolon.alexandria.data.models.Borrower;
import africa.Semicolon.alexandria.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Borrowers extends MongoRepository<Borrower, String> {

    boolean existsByMember(User member);

    Borrower findByMember(User member);
}
