package data;

import enums.CuisineCategory;
import enums.FoodCategory;
import enums.RecipeCategory;

public class Recipe {

	public String recipeId;
	public String recipeName;
	public RecipeCategory recipeCategory;
	public FoodCategory foodCategory;

	public CuisineCategory cuisineCategory;

	public String ingredients;
	public String prepTime;
	public String cookingTime;
	public String tag;
	public String noOfServings;

	public String recipeDescription;
	public String preparationMethod;
	public String nutritionValue;
	public String recipeURL;

	public String getRecipeId() {
		return recipeId;
	}
	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}
	public String getRecipeName() {
		return recipeName;
	}
	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
	public RecipeCategory getRecipeCategory() {
		return recipeCategory;
	}
	public void setRecipeCategory(RecipeCategory recipeCategory) {
		this.recipeCategory = recipeCategory;
	}
	public FoodCategory getFoodCategory() {
		return foodCategory;
	}
	public void setFoodCategory(FoodCategory foodCategory) {
		this.foodCategory = foodCategory;
	}
	public CuisineCategory getCuisineCategory() {
		return cuisineCategory;
	}
	public void setCuisineCategory(CuisineCategory cuisineCategory) {
		this.cuisineCategory = cuisineCategory;
	}
	public String getIngredients() {
		return ingredients;
	}
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	public String getPrepTime() {
		return prepTime;
	}
	public void setPrepTime(String prepTime) {
		this.prepTime = prepTime;
	}
	public String getCookingTime() {
		return cookingTime;
	}
	public void setCookingTime(String cookingTime) {
		this.cookingTime = cookingTime;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getNoOfServings() {
		return noOfServings;
	}
	public void setNoOfServings(String noOfServings) {
		this.noOfServings = noOfServings;
	}
	public String getRecipeDescription() {
		return recipeDescription;
	}
	public void setRecipeDescription(String recipeDescription) {
		this.recipeDescription = recipeDescription;
	}
	public String getPreparationMethod() {
		return preparationMethod;
	}
	public void setPreparationMethod(String preparationMethod) {
		this.preparationMethod = preparationMethod;
	}
	public String getNutritionValue() {
		return nutritionValue;
	}
	public void setNutritionValue(String nutritionValue) {
		this.nutritionValue = nutritionValue;
	}
	public String getRecipeURL() {
		return recipeURL;
	}
	public void setRecipeURL(String recipeURL) {
		this.recipeURL = recipeURL;
	}

}
