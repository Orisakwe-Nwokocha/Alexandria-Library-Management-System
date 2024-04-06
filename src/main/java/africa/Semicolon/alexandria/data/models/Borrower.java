package africa.Semicolon.alexandria.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("Borrowers")
public class Borrower {
    @Id
    private String id;
    @DBRef
    private User member;
    @DBRef
    private List<Book> books = new ArrayList<>();

}
