package com.example.demo.Controllers;

import com.example.demo.Entities.Author;
import com.example.demo.Entities.Editorial;
import com.example.demo.Repositories.AuthorRepository;
import com.example.demo.Repositories.EditorialRepository;
import com.example.demo.Services.EditorialService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/"})
public class PortalController {
    private EditorialService editorialService;
    private AuthorRepository authorRepository;
    private EditorialRepository editorialRepository;

    @Autowired
    public PortalController(EditorialService editorialService, AuthorRepository authorRepository, EditorialRepository editorialRepository) {
        this.editorialService = editorialService;
        this.authorRepository = authorRepository;
        this.editorialRepository = editorialRepository;
    }

    @GetMapping({"/"})
    public String index() {
        return "index";
    }

    @GetMapping({"/editorial"})
    public String editorial() {
        return "editorial";
    }

    @GetMapping({"/book"})
    public String book(ModelMap model) {
        List<Author> authors = this.authorRepository.findAll();
        List<Editorial> editorials = this.editorialRepository.findAll();
        model.put("authors", authors);
        model.put("editorials", editorials);
        return "book";
    }

    @GetMapping({"/author"})
    public String author() {
        return "author";
    }
}
