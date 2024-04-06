package africa.Semicolon.alexandria.data.repositories;

import africa.Semicolon.alexandria.data.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Books extends MongoRepository<Book, String> {
}
