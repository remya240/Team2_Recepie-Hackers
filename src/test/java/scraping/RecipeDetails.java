package scraping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import data.Recipe;
import testBase.BaseClass;
import utilities.LoggerLoad;

public class RecipeDetails extends BaseClass {

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
		System.out.println("Preparation Time: " + recipe.prepTime);

		return recipe;
	}

	public static Recipe GetPreparationMethod(Recipe recipe) {
		try {
			WebElement noOfServing = driver.findElement(By.xpath("//div[@class='rsepc']//ol"));
			recipe.noOfServing = noOfServing.getText().trim();
		} catch (Exception Ex) {
			LoggerLoad.error("preparationMethod element not found: " + Ex.getMessage());
			recipe.noOfServing = "N/A";
		}

		System.out.println("NoOfServing : " + recipe.noOfServing);

		return recipe;
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

	public static Recipe getRecipieIdandUrl(Recipe recipe) {

		try {
			recipe.recipeURL = driver.getCurrentUrl();

			// Extract recipe ID from the URL
			String[] parts = recipe.recipeURL.split("-");
			String recipeId = parts[parts.length - 1];
			recipe.recipeId = recipeId; // assuming you have a field named 'recipeId' in the Recipe class

		} catch (Exception ex) {
			LoggerLoad.error("recipeURL element not found: " + ex.getMessage());
			recipe.cookingTime = "N/A";
		}

		// Print recipe URL and ID
		System.out.println("Recipe URL: " + recipe.recipeURL);
		System.out.println("Recipe ID: " + recipe.recipeId);

		return recipe;
	}

	public static Recipe getRecipieName(Recipe recipe) {

		try {
			WebElement headingElement = driver.findElement(By.xpath("//h4[@class='rec-heading']"));
			String fullHeading = headingElement.getText();

			// Extract first part before '|'
			String firstTitle = fullHeading.split("\\|")[0].trim();

			// Remove "recipe" at the end if present
			if (firstTitle.toLowerCase().endsWith("recipe")) {
				firstTitle = firstTitle.substring(0, firstTitle.toLowerCase().lastIndexOf("recipe")).trim();
			}

			recipe.recipeName = firstTitle;

		} catch (Exception ex) {
			LoggerLoad.error("Recipe heading element not found: " + ex.getMessage());
			recipe.recipeName = "N/A";
		}

		System.out.println("RecipeName: " + recipe.recipeName);

		return recipe;
	}

	public static Recipe getNoofserving(Recipe recipe) {

		try {
			WebElement noofsering = driver.findElement(By.xpath("//p[@class='mb-0 font-size-13 font-size-13']"));
			recipe.noOfServing = noofsering.getText().trim();
		} catch (Exception Ex) {
			LoggerLoad.error("preparationMethod element not found: " + Ex.getMessage());
			recipe.noOfServing = "N/A";
		}

		System.out.println("PreparationMethod : " + recipe.noOfServing);

		return recipe;
	}

	public static Recipe getRecipieDescription(Recipe recipe) {

		try {
			WebElement recipedescription = driver.findElement(By.xpath("//p[contains(text(),'|')]"));
			recipe.recipedescription = recipedescription.getText().trim();
		} catch (Exception Ex) {
			LoggerLoad.error("Description element not found: " + Ex.getMessage());
			recipe.recipedescription = "N/A";
		}

		System.out.println("Description : " + recipe.recipedescription);

		return recipe;
	}

}