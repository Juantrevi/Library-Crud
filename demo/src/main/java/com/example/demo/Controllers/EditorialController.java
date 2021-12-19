package com.example.demo.Controllers;

import com.example.demo.Entities.Editorial;
import com.example.demo.Services.EditorialService;
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
@RequestMapping({"/editorial"})
public class EditorialController {
    private final EditorialService editorialService;

    @Autowired
    public EditorialController(EditorialService editorialService) {
        this.editorialService = editorialService;
    }

    @PostMapping({"/addeditorial"})
    public String createEditorial(ModelMap model, @RequestParam String name, @RequestParam boolean enable, RedirectAttributes redirectAttributes) {
        try {
            this.editorialService.createEditorial(name, enable);
            redirectAttributes.addFlashAttribute("message", "Editorial '" + name + "' was added");
            return "redirect:/editorial";
        } catch (Exception var6) {
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("enable", enable);
            redirectAttributes.addFlashAttribute("error", var6.getMessage());
            return "redirect:/editorial";
        }
    }

    @GetMapping({"/showall"})
    public String getEditorials(ModelMap model) {
        List<Editorial> editorials = this.editorialService.findEditorials();
        model.addAttribute("editorials", editorials);
        return "/editorials";
    }

    @GetMapping({"/delete/{id}"})
    public String delete(@PathVariable Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Editorial editorial = this.editorialService.findEditorial(id);
        redirectAttributes.addFlashAttribute("message", "Editorial " + editorial.getName() + " was deleted");
        this.editorialService.deleteEditorial(id);
        return "redirect:/editorial/showall";
    }

    @GetMapping({"/edit/{id}"})
    public String edit(@PathVariable("id") Long id, ModelMap model) {
        Editorial editorial = this.editorialService.findEditorial(id);
        model.addAttribute("title", "Edit editorial");
        model.put("name", editorial.getName());
        model.put("enable", editorial.isEnable());
        return "editeditorials";
    }

    @PostMapping({"/edit/{id}/editorial/saveedit"})
    public String saveedit(@PathVariable("id") Long id, @RequestParam String name, @RequestParam boolean enable, ModelMap model, RedirectAttributes redirectAttributes) {
        try {
            this.editorialService.editEditorial(id, name, enable);
            redirectAttributes.addFlashAttribute("message", "Editorial " + name + " was edited");
            return "redirect:/editorial/showall";
        } catch (Exception var7) {
            redirectAttributes.addFlashAttribute("id", id);
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("enable", enable);
            redirectAttributes.addFlashAttribute("error", var7.getMessage());
            return "redirect:/editorial/showall";
        }
    }
}
