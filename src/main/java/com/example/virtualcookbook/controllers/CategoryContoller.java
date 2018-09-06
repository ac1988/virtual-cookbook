package com.example.virtualcookbook.controllers;

import com.example.virtualcookbook.models.Category;
import com.example.virtualcookbook.models.Recipe;
import com.example.virtualcookbook.models.data.CategoryDao;
import com.example.virtualcookbook.models.data.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("category")
public class CategoryContoller {

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "My Virtual Cookbook");

        return "category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCategoryForm(Model model){
        model.addAttribute("title", "Add Category");
        model.addAttribute(new Category());
        return "category/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCategoryForm(@ModelAttribute @Valid Category category, Errors errors, Model model){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Category");
            return "category/add";
        }

        categoryDao.save(category);
        return "redirect:";
    }

    @RequestMapping(value = "view/{categoryId}", method = RequestMethod.GET)
    public String viewCategory(Model model, @PathVariable int categoryId){
        Category viewCategory = categoryDao.findOne(categoryId);
        model.addAttribute("category", viewCategory);
        return "category/view";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCategoryForm(Model model) {
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "Remove Category");
        return "category/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCategoryForm(Model model, @RequestParam int[] categoryIds) {

        for (int categoryId : categoryIds) {
            Category cat = categoryDao.findOne(categoryId);
            if (cat.getRecipes().size() > 0) {
                model.addAttribute("error", "One of your selected categories has" +
                        " recipes that need to be removed first.");
                model.addAttribute("categories", categoryDao.findAll());
                model.addAttribute("title", "Remove Category");
                return "category/remove";
            } else {
                categoryDao.delete(categoryId);
            }
        }
        return "redirect:";

    }
}
