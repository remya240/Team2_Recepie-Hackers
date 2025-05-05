package data;

import enums.FoodCategory;
import enums.RecipeCategory;

public class Recipe {

    public String recipeId;
    public String recipeName;
    public RecipeCategory recipeCategory;
    public FoodCategory foodCategory;
    public String ingredients;
    public String prepTime;
    public String cookingTime;
    public String preparationMethod;
    public String nutritionValue;
    public String targetCondition;
    public String recipeURL;
    public Boolean toAdd;
    public Boolean noAllergies;  // Use camelCase

    // Optionally, include constructors, getters, setters
}
