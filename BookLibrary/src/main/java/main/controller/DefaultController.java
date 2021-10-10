package main.controller;

import main.model.Book;
import main.model.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
public class DefaultController {

    private final BookRepository bookRepository;

    @Autowired
    public DefaultController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RequestMapping("/")
    public String index(Model model) {
        List<Book> books = bookRepository.findAll();
        HashMap<Integer, Book> booksMap = new HashMap<>();
        for (int i = 0; i < books.size(); i++){
            booksMap.put(i + 1, books.get(i));
        }
        model.addAttribute("books", booksMap);
        return "index";
    }

}
