package main.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int year;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "author_books",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('ADVENTURE', " +
                    "'ANTHOLOGY', " +
                    "'CLASSIC', " +
                    "'COMIC', " +
                    "'DETECTIVE', " +
                    "'DRAMA', " +
                    "'FABLE', " +
                    "'FAIRYTALE', " +
                    "'FANTASY', " +
                    "'HISTORICAL_FICTION', " +
                    "'HORROR', " +
                    "'HUMOR', " +
                    "'LEGEND', " +
                    "'MAGICAL_REALISM', " +
                    "'MYSTERY', " +
                    "'MYTHOLOGY', " +
                    "'REALISTIC_FICTION', " +
                    "'ROMANCE', " +
                    "'SATIRE', " +
                    "'SCIENCE_FICTION', " +
                    "'SHORT_STORY', " +
                    "'DYSTOPIA', " +
                    "'THRILLER') NOT NULL")
    private Genre genre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getCode() {
        return code;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author){
        this.authors.add(author);
        author.getBooks().add(this);
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Book{" +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", authors=" + authors +
                ", code='" + code + '\'' +
                ", genre=" + genre +
                '}';
    }
}

