package main.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b")
    Page<Book> findAllOrderedBooks(PageRequest pageRequest);

    boolean existsByNameAndYear(String name, int year);

    @Query(value = "SELECT * FROM books AS b LEFT JOIN author_books AS ab ON b.id = ab.book_id " +
            "LEFT JOIN authors AS a ON ab.author_id = a.id order by a.first_name and a.last_name ",
    nativeQuery = true)
    Page<Book> findAllOrderedByAuthor(PageRequest request, Sort.Direction direction);
}
