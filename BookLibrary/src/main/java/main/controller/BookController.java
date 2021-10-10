package main.controller;

import main.BookService;
import main.api.request.AddBookRequest;
import main.api.request.UpdateBookRequest;
import main.api.response.AddBookResponse;
import main.api.response.BooksListResponse;
import main.api.response.UpdateBookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<BooksListResponse> getBooks(@RequestParam (defaultValue = "name") String column,
                                                      @RequestParam (defaultValue = "desc") String order ,
                                                      @RequestParam (defaultValue = "0") int page ){
        BooksListResponse response = bookService.getBooks(column, order, page);
        if (response.isResult()) return ResponseEntity.status(HttpStatus.OK).body(response);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    @DeleteMapping("/{id}") HttpStatus deleteBook(@PathVariable int id){
        if (bookService.deleteBook(id)) return HttpStatus.OK;
        return HttpStatus.NOT_FOUND;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateBookResponse> updateBook(@RequestBody UpdateBookRequest request,
                                         @PathVariable int id){
        UpdateBookResponse response = bookService.updateBook(id,
                request.getName(),
                request.getGenre(),
                request.getAuthors(),
                request.getYear());
        if (response.isResult()) return ResponseEntity.status(HttpStatus.OK).body(response);
        if (!response.isResult() && !response.getErrors().isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<AddBookResponse> addBook(@RequestBody AddBookRequest request){
        System.out.println(request);
        AddBookResponse response = bookService.addBook(request.getName(),
                request.getAuthors(),
                request.getGenre(),
                request.getYear());
        if (response.getErrors().isEmpty()) return ResponseEntity.status(HttpStatus.OK).body(response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
