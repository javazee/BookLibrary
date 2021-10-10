package main;

import main.api.request.AuthorOfBook;
import main.api.response.AddBookResponse;
import main.api.response.BooksListResponse;
import main.api.response.UpdateBookResponse;
import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public BooksListResponse getBooks(String column, String order, int page){
        Page<Book> books;
        BooksListResponse response = new BooksListResponse();
        if (order.equals("desc")) {
            if (column.equals("author")){
                books = bookRepository.findAllOrderedByAuthor(PageRequest.of(page, 10), Sort.Direction.DESC);
            } else {
                books = bookRepository.findAllOrderedBooks(PageRequest.of(page, 10, Sort.by(column).descending()));
            }
        } else {
            if (column.equals("author")) {
                books = bookRepository.findAllOrderedByAuthor(PageRequest.of(page, 10), Sort.Direction.ASC);
            } else {
                books = bookRepository.findAllOrderedBooks(PageRequest.of(page, 10, Sort.by(column).ascending()));
            }
        }
        if (books.isEmpty()) return response;
        response.setBooks(books.getContent().stream().distinct().collect(Collectors.toList()));
        response.setResult(true);
        return response;
    }

    public boolean deleteBook(int id){
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isPresent()){
            bookRepository.delete(optional.get());
            return true;
        }
        return false;
    }

    public UpdateBookResponse updateBook(int id, String name, String genre, List<AuthorOfBook> authors, String year) {
        Optional<Book> optional = bookRepository.findById(id);
        UpdateBookResponse response = new UpdateBookResponse();
        if (optional.isPresent()) {
            Book book = optional.get();
            HashMap<String, String> errors = validateDataForm(name, authors, genre, year);
            if (errors.isEmpty()) {
                book.setName(name);
                book.setGenre(Genre.valueOf(genre));
                book.setYear(Integer.parseInt(year));
                book.getAuthors().clear();
                for (AuthorOfBook authorItem : authors) {
                    Author author;
                    Optional<Author> optionalAuthor = authorRepository.findByFirstNameAndLastName(authorItem.getFirstName(), authorItem.getLastName());
                    author = optionalAuthor.orElseGet(() -> new Author(authorItem.getFirstName(), authorItem.getLastName()));
                    book.addAuthor(author);
                    authorRepository.save(author);
                }
                bookRepository.save(book);
                response.setResult(true);
                return response;
            }
            return response;
        }
        return response;
    }

    public AddBookResponse addBook(String name, List<AuthorOfBook> authors, String genre, String year){
        AddBookResponse response = new AddBookResponse();
        HashMap<String, String> errors = validateDataForm(name, authors, genre, year);
        if (bookRepository.existsByNameAndYear(name, Integer.parseInt(year))) {
            errors.put("book", "Книга уже существует");
        }
        if (errors.isEmpty()){
            Book book = new Book();
            book.setName(name);
            book.setGenre(Genre.valueOf(genre));
            book.setYear(Integer.parseInt(year));
            for (AuthorOfBook authorItem : authors) {
                Author author;
                Optional<Author> optional = authorRepository.findByFirstNameAndLastName(authorItem.getFirstName(), authorItem.getLastName());
                author = optional.orElseGet(() -> new Author(authorItem.getFirstName(), authorItem.getLastName()));
                book.addAuthor(author);
                authorRepository.save(author);
            }
            book.setCode(generateISBNCode());
            bookRepository.save(book);
            response.setResult(true);
            response.setId(book.getId());
            return response;
        }
        response.setErrors(errors);
        return response;
    }

    public BooksListResponse searchBooks(String query, int page){
        Page<Book> books;
        BooksListResponse response = new BooksListResponse();
        if (query == null || query.trim().isEmpty()){
            books = bookRepository.findAllOrderedBooks(PageRequest.of(page, 10, Sort.by("name").ascending()));
        } else {
            books = bookRepository.searchBooksByQuery(query, PageRequest.of(page, 10));
        }
        if (books.isEmpty()) return response;
        response.setBooks(books.getContent().stream().distinct().collect(Collectors.toList()));
        response.setResult(true);
        return response;
    }

    private HashMap<String, String> validateDataForm(String name, List<AuthorOfBook> authors, String genre, String year){
        HashMap <String, String> errors = new HashMap<>();
        if (name.length() == 0) {
            errors.put("name", "Введите имя");
        }
        if (!name.matches("[\\s0-9A-Za-zА-Яа-яЁё-]+")) {
            errors.put("name", "Наименование содержит некорректные символы");
        }
        for (AuthorOfBook author : authors) {
            if (author.getFirstName().length() < 2) {
                errors.put("authorFirstName", "Слишком короткое имя");
            }
            if (!author.getFirstName().matches("[A-Za-zА-Яа-яЁё]+")) {
                errors.put("authorFirstName", "Имя автора должно содержать только буквы");
            }
            if (author.getLastName().isEmpty()) {
                if (author.getLastName().length() < 2) {
                    errors.put("authorLastName", "Слишком короткое фамилия");
                }
                if (!author.getLastName().matches("[A-Za-zА-Яа-яЁё]+")) {
                    errors.put("authorLastName", "Фамилия автора должна содержать только буквы");
                }
            }
        }
        try {
            Genre.valueOf(genre);
        } catch (IllegalArgumentException ex){
            errors.put("genre", "Введите другой жанр");
            ex.printStackTrace();
        }
        if (!year.matches("[0-9]{1,4}") || Integer.parseInt(year) > LocalDate.now().getYear()) {
            errors.put("year", "Некорректный год публикации");
        }
        return errors;
    }

    private String generateISBNCode(){
        StringBuilder sb = new StringBuilder("ISBN 978-5-");
        Random r = new Random();
        sb.append(r.nextInt(99999));
        sb.append("-");
        sb.append(r.nextInt(999));
        sb.append("-");
        sb.append(r.nextInt(9));
        return sb.toString();
    }



}
