package com.example.virtualcookbook.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=1, message = "Name must not be empty")
    private String name;

    //TODO: Determine a better way to store/build list of ingredients
    @ElementCollection
    private List<String> ingredients;

    @NotNull
    @Size(min=1, message = "Directions must not be empty")
    private String directions;

    @ManyToOne
    private Category category;

    public Recipe(){
    }

    public Recipe(String name, List<String> ingredients, String directions, Category category){
        this.name = name;
        this.ingredients = ingredients;
        this.directions = directions;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
