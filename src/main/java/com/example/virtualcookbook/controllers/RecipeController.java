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
import java.util.List;


@Controller
@RequestMapping("recipe")
public class RecipeController {

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddRecipeForm(Model model){
        model.addAttribute("title", "Add Recipe");
        model.addAttribute(new Recipe());
        model.addAttribute("categories", categoryDao.findAll());
        return "recipe/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddRecipeForm(@ModelAttribute @Valid Recipe newRecipe,
                                       Errors errors, @RequestParam int categoryId, Model model) {

        Category selectedCategory = categoryDao.findOne(categoryId);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Recipe");
            model.addAttribute("categories", categoryDao.findAll());
            return "recipe/add";
        }

        newRecipe.setCategory(selectedCategory);
        selectedCategory.addRecipe(newRecipe);
        recipeDao.save(newRecipe);
        categoryDao.save(selectedCategory);
        return "redirect:/recipe/view/" + newRecipe.getId();
    }

    @RequestMapping(value = "view/{recipeId}", method = RequestMethod.GET)
    public String viewRecipe(Model model, @PathVariable int recipeId){
        Recipe viewRecipe = recipeDao.findOne(recipeId);
        model.addAttribute("recipe", viewRecipe);
        return "recipe/view";
    }

    @RequestMapping(value = "remove/{recipeId}", method = RequestMethod.POST)
    public String removeRecipe(Model model, @PathVariable int recipeId){
        //Recipe viewRecipe = recipeDao.findOne(recipeId);
        for(Category category : categoryDao.findAll()) {
            category.deleteRecipe(recipeDao.findOne(recipeId));
            }
        recipeDao.delete(recipeId);
        return "redirect:/category";
    }

    @RequestMapping(value = "edit/{recipeId}", method = RequestMethod.GET)
    public String displayEditRecipeForm(Model model, @PathVariable int recipeId){
        model.addAttribute("title", "Edit Recipe");
        model.addAttribute("recipe", recipeDao.findOne(recipeId));
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("categories", categoryDao.findAll());
        return "recipe/edit";
    }

    @RequestMapping(value = "edit/{recipeId}", method = RequestMethod.POST)
    public String processEditRecipeForm(Model model, @Valid Recipe editRecipe,
                                        Errors errors, @RequestParam int categoryId,
                                        @PathVariable int recipeId){

        String name = editRecipe.getName();
        List<String> ingredients = editRecipe.getIngredients();
        String directions = editRecipe.getDirections();
        Category selectedCategory = categoryDao.findOne(categoryId);

        Recipe revisedRecipe = recipeDao.findOne(recipeId);
        Category originalCategory = categoryDao.findOne(revisedRecipe.getCategory().getId());

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Recipe");
            model.addAttribute("recipe", recipeDao.findOne(recipeId));
            model.addAttribute("recipeId", recipeId);
            model.addAttribute("categories", categoryDao.findAll());
            return "recipe/edit";
        }

        if (selectedCategory != originalCategory){
            originalCategory.deleteRecipe(revisedRecipe);
            categoryDao.save(originalCategory);
            revisedRecipe.setCategory(selectedCategory);
            selectedCategory.addRecipe(revisedRecipe);
        }

        revisedRecipe.setName(name);
        revisedRecipe.setIngredients(ingredients);
        revisedRecipe.setDirections(directions);
        revisedRecipe.setCategory(selectedCategory);

        recipeDao.save(revisedRecipe);
        categoryDao.save(selectedCategory);

        return "redirect:/recipe/view/" + recipeId;
    }
}

