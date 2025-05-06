
package scraping;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import data.Recipe;
import testBase.BaseClass;
import utilities.LoggerLoad;

public class Pagination extends BaseClass {

	public List<Recipe> GetRecipesOnPage(List<Recipe> lstRecipe) {
		// Get all recipes on the page
		List<WebElement> recipeCards = driver
				.findElements(By.xpath("//div[@class='col-md-12']//div[@class='row recipe-list d-flex flex-wrap']"));

		// Get recipes for each recipe card on page
		for (int i = 1; i <= recipeCards.size(); i++) {
			// New recipe object
			Recipe recipe = new Recipe();

			try {
				WebElement recipeCard = driver.findElement(
						By.xpath("//body/main/section[@class='py-4 mt-5']/div[@class='container']/div[@class='row']"
								+ "/div[@class='col-md-12']/div[@class='row recipe-list d-flex flex-wrap']/div[" + i
								+ "]"));

				// Get the recipe name element (not just text)
//	            WebElement recipeNameElement = recipeCard.findElement(By.xpath(".//h2 | .//h3 | .//a[contains(@class,'recipe-name')]"));
//	            String recipeName = recipeNameElement.getText();
//	            recipe.recipeName = recipeName;
//	            LoggerLoad.info(recipe.recipeName);

				// Click on the recipe name/link element
				recipeCard.click();
				
				// Wait for page to load (add appropriate wait here)
				Thread.sleep(2000); // Consider using explicit waits instead
				String currentUrl = driver.getCurrentUrl();
				recipe.recipeId = RecipeDetails.getRecipeID(currentUrl);
				String recipeName = RecipeDetails.getRecipeName(currentUrl,recipe);
				RecipeDetails.getRecipeIngrediants(recipe);
				String recipeTag = RecipeDetails.getTags(recipe);
				
				recipe.recipeCategory = RecipeDetails.getRecipeCategory(recipeName, recipeTag);
				System.out.println("Recipe Category----"+recipe.recipeCategory);
				
				recipe.foodCategory = RecipeDetails.getFoodCategory(recipeName, recipeTag);
				System.out.println("Food Category----" +recipe.foodCategory);
				
				recipe.cuisineCategory = RecipeDetails.getCuisineCategory(recipeTag);
				System.out.println("recipe.cuisineCategory---"+recipe.cuisineCategory);
				
				recipe = RecipeDetails.getCookingTime(recipe);
				RecipeDetails.getPreparationTime(recipe);
				RecipeDetails.GetPreparationMethod(recipe);
				RecipeDetails.GetNuritientValue(recipe);
				// Add recipe to the list
				lstRecipe.add(recipe);

				// Get count of recipes scraped from website without filters
				LoggerLoad.info("No. of recipes found so far " + lstRecipe.size());

				// Go back to recipe list
				// driver.navigate().back();
				Thread.sleep(2000); // Wait for list to reload

			} catch (Exception ex) {
				LoggerLoad.info(ex.getMessage());
				LoggerLoad.info("page " + i + "catch block - " + driver.getTitle());
			}
		}
		return lstRecipe;
	}

	public void GetAllRecipes(WebDriverWait wait) {
		List<Recipe> lstRecipe = new ArrayList<>();

		for (int categoryPageCount = 1; categoryPageCount <= 3; categoryPageCount++) {
			try {
				if (categoryPageCount != 1) {
					LoggerLoad.info("Navigating to category page " + categoryPageCount);
					WebElement pageLink = driver
							.findElement(By.xpath("//a[@class='page-link' and text()='" + categoryPageCount + "']"));
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", pageLink);
					Thread.sleep(2000);
				}

				// Click on the "View All" button (generic)
				try {
					WebElement viewAllButton = wait.until(ExpectedConditions.elementToBeClickable(
							By.xpath("//a[contains(@href, '/recipes/category/') and contains(text(), 'View All')]")));
					viewAllButton.click();
					LoggerLoad.info("Clicked 'View All' button.");
					Thread.sleep(2000);
				} catch (Exception ex) {
					LoggerLoad.warn("View All button not found or not clickable: " + ex.getMessage());
				}

				// Get actual page numbers (pagination buttons only)
				List<WebElement> paginationLinks = driver.findElements(By.xpath(
						"//ul[@class='pagination justify-content-center align-items-center']//a[@class='page-link' and not(contains(text(),'Next')) and not(contains(text(),'Previous'))]"));

				LoggerLoad.info("Total sub-pages found: " + paginationLinks.size());

				for (int i = 1; i <= paginationLinks.size(); i++) {
					try {
						if (i != 1) {
							LoggerLoad.info("Navigating to sub-page " + i);

							WebElement subPageLink = driver.findElement(By.xpath(
									"//ul[@class='pagination justify-content-center align-items-center']//a[@class='page-link' and text()='"
											+ i + "']"));
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", subPageLink);
							Thread.sleep(2000);
						}

						lstRecipe = GetRecipesOnPage(lstRecipe);

					} catch (Exception e) {
						LoggerLoad.warn("Error on sub-page " + i + ": " + e.getMessage());
					}
				}

			} catch (Exception e) {
				LoggerLoad.warn("Error navigating to category page " + categoryPageCount + ": " + e.getMessage());
			}
		}
	}

}
