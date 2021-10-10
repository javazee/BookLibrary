package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import main.model.Book;

import java.util.List;

@Data
public class BooksListResponse {

    private boolean result;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Book> books;


}
