
package com.example.demo.Services;

import com.example.demo.Entities.Author;
import com.example.demo.Repositories.AuthorRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public AuthorService() {
    }

    @Transactional
    public void createAuthor(String name, boolean enable) {
        Optional<Author> answer = Optional.ofNullable(this.authorRepository.findAuthorByName(name));
        if (name.isEmpty()) {
            throw new IllegalStateException("Author cannot be empty");
        } else if (answer.isPresent()) {
            throw new IllegalStateException("Author already exists");
        } else {
            Author author = new Author();
            author.setName(name);
            author.setEnable(enable);
            authorRepository.save(author);
        }
    }

    public Author findAuthor(Long id) {
        Optional<Author> answer = authorRepository.findById(id);
        if (answer.isEmpty()) {
            throw new IllegalStateException("Author does not exist");
        } else {
            return answer.get();
        }
    }

    public List<Author> findAuthors() {
        return authorRepository.findAll();
    }

    @Transactional
    public void editAuthor(Long idAuthor, String name, Boolean enable) {
        Optional<Author> answer = authorRepository.findById(idAuthor);
        if (answer.isEmpty()) {
            throw new IllegalStateException("Author does not exist");
        } else {
            Author author = answer.get();
            if (name != null && name.length() > 0 && !Objects.equals(author.getName(), name)) {
                author.setName(name);
                author.setEnable(enable);
                authorRepository.save(author);
            } else {
                throw new IllegalStateException("The Author cannot be empty or null or the same");
            }
        }
    }

    @Transactional
    public void deleteAuthor(Long idAuthor) {
        boolean exists = authorRepository.existsById(idAuthor);
        if (!exists) {
            throw new IllegalStateException("Author does not exists");
        } else {
            authorRepository.deleteById(idAuthor);
        }
    }
}
