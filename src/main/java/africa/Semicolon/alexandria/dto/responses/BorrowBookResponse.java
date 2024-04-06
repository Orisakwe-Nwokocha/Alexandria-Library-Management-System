package africa.Semicolon.alexandria.dto.responses;

import lombok.Data;

@Data
public class BorrowBookResponse {
    private String username;
    private String libraryLoan;
    private String book;
}
