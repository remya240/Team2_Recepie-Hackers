package scraping;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import data.Recipe;
import driverManager.DriverFactory;
import testBase.BaseClass;
import utilities.LoggerLoad;

public class RecipeScraperTest extends BaseClass {

	@Test
	public void GetRecipesOnPage() throws InterruptedException {
		List<Recipe> lstRecipe = new ArrayList<>();
		
		System.out.println("Current URL: " + driver.getCurrentUrl());
		Thread.sleep(3000);
		Recipe recipe = new Recipe();

		String recipeTab = driver.getWindowHandle();
		int recipePages = getNumOfPages();
		for (int j = 1; j <= recipePages; j++) {
			try {

				List<WebElement> recipeBlocks = driver
						.findElements(By.xpath("//main//div[contains(@class,'recipe-block')]"));

				for (WebElement recipeCard : recipeBlocks) {
					// String recipeName = recipeCard.findElement(By.tagName("h5")).getText();
					WebElement recipeLink = recipeCard.findElement(By.tagName("a")); // recipe link

					((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');",
							recipeLink.getAttribute("href"));
					String recipeDetailTab = switchToTabByIndex(1);
					// Need to get data from page (scraping)

					String currentUrl = driver.getCurrentUrl();
					recipe.recipeId = RecipeDetails.getRecipeID(currentUrl);
					String recipeName = RecipeDetails.getRecipeName(currentUrl, recipe);

					// Wait for page to load (add appropriate wait here)
					Thread.sleep(2000); // Consider using explicit waits instead
					recipe = RecipeDetails.getCookingTime(recipe);
					RecipeDetails.getPreparationTime(recipe);
					RecipeDetails.getNoofserving(recipe);
					RecipeDetails.getRecipeIngrediants(recipe);

					String recipeTag = RecipeDetails.getTags(recipe);
					recipe.recipeCategory = RecipeDetails.getRecipeCategory(recipeName, recipeTag);
					System.out.println("Recipe Category----" + recipe.recipeCategory);
					recipe.foodCategory = RecipeDetails.getFoodCategory(recipeName, recipeTag);
					System.out.println("Food Category----" + recipe.foodCategory);
					recipe.cuisineCategory = RecipeDetails.getCuisineCategory(recipeTag);
					System.out.println("CuisineCategory---" + recipe.cuisineCategory);
					RecipeDetails.GetPreparationMethod(recipe);
					RecipeDetails.GetNuritientValue(recipe);
					RecipeDetails.getRecipieDescription(recipe);
					RecipeDetails.getUrl(recipe);
					lstRecipe.add(recipe);
					// lstRecipe.add(recipe);
					closeTab(recipeDetailTab);
					driver.switchTo().window(recipeTab);
				}

			} catch (Exception e) {
				System.out.println("Error from tab: " + recipeTab);
			}

			List<WebElement> nextRecipePageButton = driver
					.findElements(By.xpath("//ul[contains(@class,'pagination')]//a[text()='Next']"));
			// System.out.println("Next Button: " + nextButton.size());

			if (!nextRecipePageButton.isEmpty()) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextRecipePageButton.get(0));
				// nextButton.get(0).click();
				Thread.sleep(2000); // Wait for page to load
			}
		}
	}

	private int getNumOfPages() {
		// Get the total pages: find all page-number links and pick the highest
		List<WebElement> pageLinks = driver
				.findElements(By.xpath("//ul[contains(@class,'pagination')]//a[contains(@href,'?page=')]"));
		// ul[contains(@class,'pagination')]//a[normalize-space(text()) != 'Next' and
		// normalize-space(text()) != 'Previous' and normalize-space(text()) != '…']

		int totalPages = 1;
		for (WebElement link : pageLinks) {
			String pageText = link.getText().trim();
			try {
				int pageNum = Integer.parseInt(pageText);
				if (pageNum > totalPages) {
					totalPages = pageNum;
				}
			} catch (NumberFormatException e) {
				// Skip if not a number, e.g. ellipsis (…)
			}
		}
		System.out.println("Total pages: " + totalPages);
		return totalPages;
	}

	private String switchToTabByIndex(int index) {
		Set<String> handles = driver.getWindowHandles();
		String target = handles.toArray(new String[0])[index];
		driver.switchTo().window(target);
		return target;
	}

	private void closeTab(String handleToClose) {
		Set<String> allWindows = driver.getWindowHandles();
		for (String handle : allWindows) {
			if (handle.equals(handleToClose)) {
				driver.switchTo().window(handle);
				driver.close(); // This closes the tab
				break;
			}
		}
	}

}
