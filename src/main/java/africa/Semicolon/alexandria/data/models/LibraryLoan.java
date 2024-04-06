package africa.Semicolon.alexandria.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Document("LibraryLoans")
public class LibraryLoan {
    @Id
    private String id;
    @DBRef
    private Borrower borrower;
    @DBRef
    private Book book;
    private final LocalDateTime borrowedAt = LocalDateTime.now();
    private LocalDateTime returnedAt;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yy 'at' hh:mm:ss a");
        String borrowedAtFormatted = borrowedAt.format(formatter);
        String returnedAtFormatted = (returnedAt != null) ? returnedAt.format(formatter) : "Not returned yet";

        String format = "LibraryLoan{Id='%s'\nbook='%s'\nborrowedAt='%s', returnedAt='%s'}";
        return String.format(format, id, book, borrowedAtFormatted, returnedAtFormatted);
    }
}
