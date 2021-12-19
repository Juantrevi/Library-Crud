
package com.example.demo.Services;

import com.example.demo.Entities.Book;
import com.example.demo.Repositories.AuthorRepository;
import com.example.demo.Repositories.BookRepository;
import com.example.demo.Repositories.EditorialRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final EditorialRepository editorialRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, EditorialRepository editorialRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.editorialRepository = editorialRepository;
    }

    public void validate(Long isbn, String title, Integer year, Integer copies, String author, String editorial) {
        if (isbn == null) {
            throw new IllegalStateException("ISBN cant be null");
        } else if (title != null && !title.isEmpty()) {
            if (year == null) {
                throw new IllegalStateException("year cant be null");
            } else if (copies == null) {
                throw new IllegalStateException("copies cant be null");
            } else if (author == null) {
                throw new IllegalStateException("Author cant be null");
            } else if (editorial == null) {
                throw new IllegalStateException("editorial cant be null");
            }
        } else {
            throw new IllegalStateException("tittle cant be null or empty");
        }
    }

    @Transactional
    public void createBook(Long isbn, String title, Integer year, Integer copies, String author, String editorial) {
        this.validate(isbn, title, year, copies, author, editorial);
        Optional<Book> answer = Optional.ofNullable(bookRepository.findBookByIsbn(isbn));
        if (answer.isPresent()) {
            throw new IllegalStateException("Book already exists");
        } else {
            Book book = new Book();
            book.setIsbn(isbn);
            book.setTitle(title);
            book.setYear(year);
            book.setCopies(copies);
            book.setLoanedCopies(0);
            book.setRemainingCopies(copies);
            book.setEnable(true);
            book.setAuthor(authorRepository.findAuthorByName(author));
            book.setEditorial(editorialRepository.findEditorialByName(editorial));
            this.bookRepository.save(book);
        }
    }

    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    public Book findBook(Long id) {
        Optional<Book> answer = bookRepository.findById(id);
        if (answer.isEmpty()) {
            throw new IllegalStateException("book does not exists");
        } else {
            Book book1 = answer.get();
            return book1;
        }
    }

    @Transactional
    public void updateBook(Long id, Long isbn, String title, Integer year, Integer copies, String author, String editorial) {
        this.validate(isbn, title, year, copies, author, editorial);
        Optional<Book> answer = bookRepository.findById(id);
        if (answer.isPresent()) {
            Book book = answer.get();
            book.setIsbn(isbn);
            book.setTitle(title);
            book.setYear(year);
            book.setCopies(copies);
            book.setLoanedCopies((answer.get()).getLoanedCopies());
            book.setRemainingCopies((answer.get()).getCopies() - (answer.get()).getLoanedCopies());
            if (answer.get().getRemainingCopies() <= 0) {
                (answer.get()).setEnable(false);
            }

            book.setAuthor(authorRepository.findAuthorByName(author));
            book.setEditorial(editorialRepository.findEditorialByName(editorial));
            this.bookRepository.save(book);
        } else {
            throw new IllegalStateException("book does not exist");
        }
    }

    @Transactional
    public void deleteBook(Long id) {
        Optional<Book> answer = bookRepository.findById(id);
        if (answer.isPresent()) {
            this.bookRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Book does not exist");
        }
    }

    @Transactional
    public void rentbook(Long id, Integer num) {
        Optional<Book> answer = bookRepository.findById(id);
        if (answer.isPresent()) {
            Book book = answer.get();
            if (book.getRemainingCopies() < num) {
                throw new IllegalStateException("Not enough books to borrow");
            }

            book.setRemainingCopies(book.getRemainingCopies() - num);
            book.setLoanedCopies(book.getLoanedCopies() + num);
            if (book.getRemainingCopies() == 0) {
                book.setEnable(false);
            } else {
                book.setEnable(true);
            }
        }

    }
}