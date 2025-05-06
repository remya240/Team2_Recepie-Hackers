package scraping;

import enums.CuisineCategory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import data.Recipe;
import enums.FoodCategory;
import enums.RecipeCategory;
import testBase.BaseClass;
import utilities.LoggerLoad;

public class RecipeDetails extends BaseClass{
	
	public static String getRecipeID(String url) {
	
		String[] parts = url.split("-");
		String lastPart = parts[parts.length - 1];
		String number = lastPart.substring(0, lastPart.length() - 1);
		System.out.println("RECIPE ID" + number); // Output:
		return number;

	}
	
	public static String getRecipeName(String url,Recipe recipe) {

		String baseName = url.substring(url.lastIndexOf('/') + 1); // "paneer-masala-2404r"
		String namePart = baseName.substring(0, baseName.lastIndexOf('-')); // "paneer-masala"

		// Replace hyphens with spaces
		recipe.recipeName = namePart.replace("-", " ");
		System.out.println("Recipe Name+++ " + recipe.recipeName);
		return recipe.recipeName;
	}
	
	public static Recipe getCookingTime(Recipe recipe) {

		try {
			WebElement cookTime = driver.findElement(By.xpath(
					"//body[1]/main[1]/section[1]/div[1]/div[1]/div[1]/div[10]/div[1]/div[1]/div[1]/div[1]/div[2]/div[2]/p[1]"));
			recipe.cookingTime = cookTime.getText().trim();
		} catch (Exception Ex) {
			LoggerLoad.error("Cooking time element not found: " + Ex.getMessage());
			recipe.cookingTime = "N/A";
		}

		// Print cooking time
		System.out.println("Cooking Time: " + recipe.cookingTime);

		return recipe;
	}

	public static Recipe getPreparationTime(Recipe recipe) {

		try {
			WebElement prepTime = driver.findElement(By.xpath("//div[@class='box-time']//div[3]//p[1]"));
			recipe.prepTime = prepTime.getText().trim();
		} catch (Exception Ex) {
			LoggerLoad.error("Preparation time element not found: " + Ex.getMessage());
			recipe.prepTime = "N/A";
		}

// Print cooking time
		System.out.println("Cooking Time: " + recipe.prepTime);

		return recipe;
	}
	
	public static Recipe getRecipeIngrediants(Recipe recipe) {
		try {
		  WebElement ingredientSection = driver.findElement(By.id("ingredients"));
		  recipe.ingredients = ingredientSection.getText().trim();
		  System.out.println("Ingrediants List---"+recipe.ingredients);
		}catch(Exception e) {
			LoggerLoad.error("ingredients element not found: " + e.getMessage());   
		  }  
		  return recipe;
	}

	public static Recipe GetPreparationMethod(Recipe recipe) {
		try {
			WebElement prepMethod = driver.findElement(By.xpath("//div[@class='rsepc']//ol"));
			recipe.preparationMethod = prepMethod.getText().trim();
		} catch (Exception Ex) {
			LoggerLoad.error("preparationMethod element not found: " + Ex.getMessage());
			recipe.preparationMethod = "N/A";
		}

		System.out.println("PreparationMethod : " + recipe.preparationMethod);

		return recipe;
	}
	
	public static String getTags(Recipe recipe) {
		try {
		WebElement tagElements =driver.findElement(By.className("tags-list"));
		recipe.tag = tagElements.getText();
		System.out.println("Tags--"+recipe.tag);
		}
		catch(Exception e) {
			LoggerLoad.error("Tags element not found: " + e.getMessage());   
		  }  
		return recipe.tag;
	}
	
	public static RecipeCategory getRecipeCategory(String recipeName, String recipeTag) {
		{
			if (recipeName.contains("Vegan") || recipeTag.contains("Vegan"))
				return RecipeCategory.VEGAN;
			else if (recipeName.contains("Jain") || recipeTag.contains("Jain"))
				return RecipeCategory.JAIN;
			else if (recipeName.contains("Egg ") || recipeTag.contains("Egg "))
				return RecipeCategory.EGGITARIAN;
			else if (recipeName.contains("NonVeg") || recipeTag.contains("NonVeg"))
				return RecipeCategory.NONVEGETARIAN;
			else
				return RecipeCategory.VEGETARIAN;
		}
	}
	
	public static FoodCategory getFoodCategory(String recipeName, String recipeTags) 
	{
		if (recipeName.contains("Breakfast") || recipeTags.contains("Breakfast"))
			return FoodCategory.BREAKFAST;
		else if (recipeName.contains("Lunch") || recipeTags.contains("Lunch"))
			return FoodCategory.LUNCH;
		else if (recipeName.contains("Dinner") || recipeTags.contains("Dinner"))
			return FoodCategory.DINNER;
		else
			return FoodCategory.SNACKS;
	}

	public static Recipe GetNuritientValue(Recipe recipe) {
		try {
			WebElement nutrValue = driver.findElement(By.xpath("//table[@id='rcpnutrients']"));
			recipe.nutritionValue = nutrValue.getText().trim();
		} catch (Exception Ex) {
			LoggerLoad.error("NutritionValue element not found: " + Ex.getMessage());
			recipe.nutritionValue = "N/A";
		}

		System.out.println("NutritionValue : " + recipe.nutritionValue);

		return recipe;
	}

	public static CuisineCategory getCuisineCategory(String tag){
		String clearedTag = tag.replaceAll("\\s+", "_");
		for (CuisineCategory cuisineCategory : CuisineCategory.values()) {
			if (clearedTag.toUpperCase().contains(cuisineCategory.name())) {
				return cuisineCategory;
			}
		}
		return CuisineCategory.INDIAN;

	}

}

