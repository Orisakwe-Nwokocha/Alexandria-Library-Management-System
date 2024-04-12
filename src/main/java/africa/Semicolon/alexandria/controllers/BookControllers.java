package africa.Semicolon.alexandria.controllers;

import africa.Semicolon.alexandria.dto.responses.ApiResponse;
import africa.Semicolon.alexandria.dto.responses.GetAllBooksResponse;
import africa.Semicolon.alexandria.dto.responses.GetBookResponse;
import africa.Semicolon.alexandria.exceptions.AlexandriaAppException;
import africa.Semicolon.alexandria.services.BookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/book")
public class BookControllers {
    @Autowired
    private BookServices bookServices;

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable("id") String id) {
        try {
            GetBookResponse result = bookServices.getBookBy(id);
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBooks() {
        try {
            GetAllBooksResponse result = bookServices.getAllBooks();
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        } catch (AlexandriaAppException e) {
            return new ResponseEntity<>(new ApiResponse(true, e.getMessage()), BAD_REQUEST);
        }
    }
}
