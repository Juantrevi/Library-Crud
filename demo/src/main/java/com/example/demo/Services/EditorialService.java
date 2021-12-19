
package com.example.demo.Services;

import com.example.demo.Entities.Editorial;
import com.example.demo.Repositories.EditorialRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialService {
    private final EditorialRepository editorialRepository;

    @Autowired
    public EditorialService(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }

    @Transactional
    public void createEditorial(String name, boolean enable) {
        Optional<Editorial> answer = Optional.ofNullable(editorialRepository.findEditorialByName(name));
        if (answer.isPresent()) {
            throw new IllegalStateException("Editorial " + name + " already exists");
        } else if (name != null && name.length() > 0) {
            Editorial editorial = new Editorial();
            editorial.setName(name);
            editorial.setEnable(enable);
            editorialRepository.save(editorial);
        } else {
            throw new IllegalStateException("Editorial cant be empty or null");
        }
    }

    public Editorial findEditorial(Long id) {
        Optional<Editorial> answer = editorialRepository.findById(id);
        if (answer.isEmpty()) {
            throw new IllegalStateException("Editorial does not exist");
        } else {
            return answer.get();
        }
    }

    public List<Editorial> findEditorials() {
        return editorialRepository.findAll();
    }

    @Transactional
    public void editEditorial(Long idEditorial, String name, Boolean enable) {
        Optional<Editorial> answer = editorialRepository.findById(idEditorial);
        editorialRepository.findEditorialByName(name);
        if (answer.isEmpty()) {
            throw new IllegalStateException("Editorial does not exist");
        } else {
            Editorial editorial = answer.get();
            if (name != null && name.length() > 0 && !Objects.equals(editorial.getName(), name)) {
                editorial.setName(name);
                if (editorialRepository.findEditorialByName(name).equals(name)) {
                    throw new IllegalStateException("Editorial " + name + "already exists");
                } else {
                    editorial.setEnable(enable);
                    editorialRepository.save(editorial);
                }
            } else {
                throw new IllegalStateException("The editorial cannot be empty, null or the same");
            }
        }
    }

    @Transactional
    public void deleteEditorial(Long idEditorial) {
        boolean exists = editorialRepository.existsById(idEditorial);
        if (!exists) {
            throw new IllegalStateException("Editorial does not exists");
        } else {
            editorialRepository.deleteById(idEditorial);
        }
    }
}