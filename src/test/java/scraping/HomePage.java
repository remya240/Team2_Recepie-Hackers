package scraping;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import testBase.BaseClass;
import utilities.LoggerLoad;

public class HomePage extends BaseClass {

	private Pagination pagination = new Pagination();

	@Test
	public void testRecipeScraping() throws Exception {
		System.out.println("InsideTest&&&");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		try {
			navigateToCategories(driver, wait);
//            String currentUrl = driver.getCurrentUrl();
//            String recipeId = commonMethods.getRecipeID(currentUrl);
//            String recipeName = commonMethods.getRecipeName(currentUrl);
//            LoggerLoad.info(String.format("Scraped Recipe - ID: %s, Name: %s", 
//                    recipeId, recipeName));
			pagination.GetAllRecipes(wait);
			// recipe = recipePage.getRecipeDetails(recipe);

		} catch (Exception e) {
			LoggerLoad.error("Error in recipe scraping: " + e.getMessage());
			throw e;
		}
		// return lstRecipe;
	}

	private void navigateToCategories(WebDriver driver, WebDriverWait wait) throws Exception {
		try {
			LoggerLoad.info("Current URL: " + driver.getCurrentUrl());

			LoggerLoad.debug("Locating 'More' dropdown");
			WebElement moreDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("More")));
			moreDropdown.click();
			System.out.println("completed moreDropdown dropdown click");

			LoggerLoad.debug("Clicking 'Recipe Category Search' under More");
			WebElement recipeCategory = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//nav/div/div/div[2]/ul/li[5]/ul/li[3]/a")));
			recipeCategory.click();
			System.out.println("completed recipeCategory click");

		} catch (Exception e) {
			LoggerLoad.error("Failed to navigate to categories: " + e.getMessage());
			throw e;
		}
	}
		
}