package com.example.demo.Repositories;

import com.example.demo.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookByIsbn(Long isbn);
}