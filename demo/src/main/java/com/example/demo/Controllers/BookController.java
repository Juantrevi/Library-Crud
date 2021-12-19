package com.example.demo.Controllers;

import com.example.demo.Entities.Author;
import com.example.demo.Entities.Book;
import com.example.demo.Entities.Editorial;
import com.example.demo.Repositories.AuthorRepository;
import com.example.demo.Repositories.EditorialRepository;
import com.example.demo.Services.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(
        path = {"/book"}
)
public class BookController {
    private final BookService bookService;
    private final AuthorRepository authorRepository;
    private final EditorialRepository editorialRepository;

    @Autowired
    public BookController(BookService bookService, AuthorRepository authorRepository, EditorialRepository editorialRepository) {
        this.bookService = bookService;
        this.authorRepository = authorRepository;
        this.editorialRepository = editorialRepository;
    }

    @PostMapping(
            path = {"/addbook"}
    )
    public String addBook(@RequestParam(required = false) Long isbn, @RequestParam(required = false) String title, @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer copies, @RequestParam(required = false) String author, @RequestParam(required = false) String editorial, RedirectAttributes redirectAttributes) {
        try {
            this.bookService.createBook(isbn, title, year, copies, author, editorial);
            redirectAttributes.addFlashAttribute("message", "Book " + title + " was added");
            return "redirect:/book";
        } catch (Exception var9) {
            redirectAttributes.addFlashAttribute("error", var9.getMessage());
            redirectAttributes.addFlashAttribute("isbn", isbn);
            redirectAttributes.addFlashAttribute("title", title);
            redirectAttributes.addFlashAttribute("year", year);
            redirectAttributes.addFlashAttribute("copies", copies);
            redirectAttributes.addFlashAttribute("author", author);
            redirectAttributes.addFlashAttribute("editorial", editorial);
            return "redirect:/book";
        }
    }

    @GetMapping({"/showbooks"})
    public String bringall(ModelMap model) {
        List<Book> books = this.bookService.findBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping({"/deletebook/{id}"})
    public String deletebook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Book book = this.bookService.findBook(id);
        redirectAttributes.addFlashAttribute("message", "Book " + book.getTitle() + " was deleted");
        this.bookService.deleteBook(id);
        return "redirect:/book/showbooks";
    }

    @GetMapping({"/getbook/{id}"})
    public String editbook(@PathVariable("id") Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        List<Author> authors = this.authorRepository.findAll();
        List<Editorial> editorials = this.editorialRepository.findAll();
        model.put("authors", authors);
        model.put("editorials", editorials);
        Book book = this.bookService.findBook(id);

        try {
            model.put("isbn", book.getIsbn());
            model.put("title", book.getTitle());
            model.put("year", book.getYear());
            model.put("copies", book.getCopies());
            model.put("author", book.getAuthor());
            model.put("editorial", book.getEditorial());
            return "editbook";
        } catch (Exception var8) {
            redirectAttributes.addFlashAttribute("error", var8.getMessage());
            redirectAttributes.addFlashAttribute("isbn", book.getIsbn());
            redirectAttributes.addFlashAttribute("title", book.getTitle());
            redirectAttributes.addFlashAttribute("year", book.getYear());
            redirectAttributes.addFlashAttribute("copies", book.getCopies());
            redirectAttributes.addFlashAttribute("author", book.getAuthor());
            redirectAttributes.addFlashAttribute("editorial", book.getEditorial());
            return "redirect:/editbook";
        }
    }

    @PostMapping({"/getbook/{id}/book/saveedit"})
    public String saveedit(@PathVariable("id") Long id, @RequestParam Long isbn, @RequestParam String title, @RequestParam Integer year, @RequestParam Integer copies, @RequestParam String author, @RequestParam String editorial, ModelMap model, RedirectAttributes redirectAttributes) {
        List<Author> authors = this.authorRepository.findAll();
        List editorials = this.editorialRepository.findAll();

        try {
            this.bookService.updateBook(id, isbn, title, year, copies, author, editorial);
            redirectAttributes.addFlashAttribute("message", "Book was edited");
            return "redirect:/book/showbooks";
        } catch (Exception var13) {
            redirectAttributes.addFlashAttribute("isbn", isbn);
            redirectAttributes.addFlashAttribute("title", title);
            redirectAttributes.addFlashAttribute("year", year);
            redirectAttributes.addFlashAttribute("authors", authors);
            redirectAttributes.addFlashAttribute("editorials", editorials);
            redirectAttributes.addFlashAttribute("error", var13.getMessage());
            return "redirect:/editbook";
        }
    }

    @GetMapping({"/rentbook/{id}"})
    public String rentbook(@PathVariable("id") Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Book book = this.bookService.findBook(id);

        try {
            model.put("isbn", book.getIsbn());
            model.put("title", book.getTitle());
            model.put("year", book.getYear());
            model.put("copies", book.getCopies());
            model.put("remainingCopies", book.getRemainingCopies());
            model.put("loanedCopies", book.getLoanedCopies());
            model.put("enable", book.getEnable());
            model.put("author", book.getAuthor());
            model.put("editorial", book.getEditorial());
            return "rentbook";
        } catch (Exception var6) {
            redirectAttributes.addFlashAttribute("error", var6.getMessage());
            redirectAttributes.addFlashAttribute("isbn", book.getIsbn());
            redirectAttributes.addFlashAttribute("title", book.getTitle());
            redirectAttributes.addFlashAttribute("year", book.getYear());
            redirectAttributes.addFlashAttribute("copies", book.getCopies());
            redirectAttributes.addFlashAttribute("remainingCopies", book.getRemainingCopies());
            redirectAttributes.addFlashAttribute("loanedCopies", book.getLoanedCopies());
            redirectAttributes.addFlashAttribute("enable", book.getEnable());
            redirectAttributes.addFlashAttribute("author", book.getAuthor());
            redirectAttributes.addFlashAttribute("editorial", book.getEditorial());
            return "redirect:/rentbook";
        }
    }

    @PostMapping({"/rentbook/{id}/book/saverent"})
    public String saverent(@PathVariable("id") Long id, @RequestParam Integer loanedCopies, ModelMap model, RedirectAttributes redirectAttributes) {
        Book book = this.bookService.findBook(id);

        try {
            this.bookService.rentbook(id, loanedCopies);
            redirectAttributes.addFlashAttribute("message", loanedCopies + " Book rented of " + book.getTitle() + ". " + book.getRemainingCopies() + " copies left.");
            return "redirect:/book/showbooks";
        } catch (Exception var7) {
            String var10002 = var7.getMessage();
            redirectAttributes.addFlashAttribute("error", var10002 + ", only " + book.getRemainingCopies() + " copies available");
            return "redirect:/book/showbooks";
        }
    }
}
