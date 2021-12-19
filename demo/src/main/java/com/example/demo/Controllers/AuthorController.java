package com.example.demo.Controllers;

import com.example.demo.Entities.Author;
import com.example.demo.Services.AuthorService;
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
        path = {"/author"}
)
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping({"/addauthor"})
    public String createauthor(@RequestParam(required = false) String name, @RequestParam boolean enable, RedirectAttributes redirectAttributes) {
        try {
            this.authorService.createAuthor(name, enable);
            redirectAttributes.addFlashAttribute("message", "Author " + name + " was added");
            return "redirect:/author/showall";
        } catch (Exception var5) {
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("enable", enable);
            redirectAttributes.addFlashAttribute("error", var5.getMessage());
            return "redirect:/author";
        }
    }

    @GetMapping({"/showall"})
    public String getAuthors(ModelMap model) {
        List<Author> authors = this.authorService.findAuthors();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping({"/delete/{id}"})
    public String delete(@PathVariable Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Author author = this.authorService.findAuthor(id);
        redirectAttributes.addFlashAttribute("message", "Author " + author.getName() + " was deleted");
        this.authorService.deleteAuthor(id);
        return "redirect:/author/showall";
    }

    @GetMapping({"/edit/{id}"})
    public String edit(@PathVariable("id") Long id, ModelMap model) {
        Author author = this.authorService.findAuthor(id);
        model.addAttribute("title", "Edit author");
        model.put("name", author.getName());
        model.put("enable", author.isEnable());
        return "editauthors";
    }

    @PostMapping({"/edit/{id}/author/saveedit"})
    public String saveedit(@PathVariable("id") Long id, @RequestParam String name, @RequestParam boolean enable, ModelMap model, RedirectAttributes redirectAttributes) {
        try {
            this.authorService.editAuthor(id, name, enable);
            redirectAttributes.addFlashAttribute("message", "Author " + name + " was edited");
            return "redirect:/author/showall";
        } catch (Exception var7) {
            redirectAttributes.addFlashAttribute("id", id);
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("enable", enable);
            redirectAttributes.addFlashAttribute("error", var7.getMessage());
            return "redirect:/author";
        }
    }
}