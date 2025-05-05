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
		System.out.println("Cooking Time: " + recipe.prepTime);

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
}