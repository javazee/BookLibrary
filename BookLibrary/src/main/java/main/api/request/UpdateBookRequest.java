package main.api.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateBookRequest {

    private String name;

    private String year;

    private String genre;

    private List<AuthorOfBook> authors;
}
