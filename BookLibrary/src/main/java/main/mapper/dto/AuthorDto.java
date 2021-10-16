package main.mapper.dto;

import lombok.Data;
import main.model.Book;

import java.util.HashSet;
import java.util.Set;

@Data
public class AuthorDto {

    private int id;

    private String firstName;

    private String lastName;

    private Set<Book> books = new HashSet<>();
}
