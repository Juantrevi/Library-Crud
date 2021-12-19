package com.example.demo.Repositories;

import com.example.demo.Entities.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long> {
    Editorial findEditorialByName(String name);
}
