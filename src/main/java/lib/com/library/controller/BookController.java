
package lib.com.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lib.com.library.model.BookModel;
import lib.com.library.repository.BookRepository;

@RestController
@RequestMapping("/api/usecure")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping({"/books", "/getbookss"})
    public List<BookModel> getBookList() {
        return bookRepository.findAll();
    }
@PutMapping("/books/{id}")
public BookModel updateBook(
        @PathVariable Long id,
        @RequestBody BookModel request) {

    BookModel book = bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Book not found with id: " + id));

    book.setIsFavorite(request.getIsFavorite());

    return bookRepository.save(book);
}
}

