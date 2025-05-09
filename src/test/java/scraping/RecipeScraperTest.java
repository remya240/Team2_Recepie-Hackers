package scraping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import data.Recipe;
import testBase.BaseClass;
import utilities.ExcelData;

public class RecipeScraperTest extends BaseClass {

	@Test
	public void GetRecipesOnPage() throws InterruptedException {
		List<Recipe> recipeList = new ArrayList<>();

		System.out.println("Current URL: " + driver.getCurrentUrl());
		Thread.sleep(3000);

		String recipeTab = driver.getWindowHandle();
		int recipePages = getNumOfPages();
		for (int j = 1; j <=recipePages ; j++) {
			try {

				List<WebElement> recipeBlocks = driver
						.findElements(By.xpath("//main//div[contains(@class,'recipe-block')]"));

				for (WebElement recipeCard : recipeBlocks) {
					Recipe recipe = new Recipe();

					WebElement recipeLink = recipeCard.findElement(By.tagName("a")); // recipe link

					((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');",
							recipeLink.getAttribute("href"));
					String recipeDetailTab = switchToTabByIndex(1);
					// Need to get data from page (scraping)

					String currentUrl = driver.getCurrentUrl();
					recipe.recipeId = RecipeDetails.getRecipeID(currentUrl);
					RecipeDetails.getRecipeName(currentUrl, recipe);

					// Wait for page to load (add appropriate wait here)
					Thread.sleep(2000); // Consider using explicit waits instead
					RecipeDetails.getCookingTime(recipe);
					RecipeDetails.getPreparationTime(recipe);
					RecipeDetails.getNoofserving(recipe);
					RecipeDetails.getRecipeIngrediants(recipe);
					RecipeDetails.getTags(recipe);
					recipe.recipeCategory = RecipeDetails.getRecipeCategory(recipe.recipeName, recipe.tag);
					System.out.println("Recipe Category----" + recipe.recipeCategory);
					recipe.foodCategory = RecipeDetails.getFoodCategory(recipe.recipeName, recipe.tag);
					System.out.println("Food Category----" + recipe.foodCategory);
					recipe.cuisineCategory = RecipeDetails.getCuisineCategory(recipe.tag);
					System.out.println("CuisineCategory---" + recipe.cuisineCategory);
					RecipeDetails.GetPreparationMethod(recipe);
					RecipeDetails.GetNuritientValue(recipe);
					RecipeDetails.getRecipieDescription(recipe);
					RecipeDetails.getUrl(recipe);
					recipeList.add(recipe);
					closeTab(recipeDetailTab);
					driver.switchTo().window(recipeTab);
				}

			} catch (Exception e) {
				System.out.println("Error from tab: " + recipeTab);
			}

			List<WebElement> nextRecipePageButton = driver
					.findElements(By.xpath("//ul[contains(@class,'pagination')]//a[text()='Next']"));

			if (!nextRecipePageButton.isEmpty()) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextRecipePageButton.get(0));
				Thread.sleep(2000); // Wait for page to load
			}
		}
		filterEliminateRecipes(recipeList);
		filterAddRecipes(recipeList);
		filterLFVAllergyNut(recipeList);
		filterLCHFAllergyNut(recipeList);
	}

	private int getNumOfPages() {
		// Get the total pages: find all page-number links and pick the highest
		List<WebElement> pageLinks = driver
				.findElements(By.xpath("//ul[contains(@class,'pagination')]//a[contains(@href,'?page=')]"));
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

	public void filterEliminateRecipes(List<Recipe> recipeList) {
		// Load Excel data
		ExcelData.LoadLFVData();
		DBConnection.initConnection();

		Set<Recipe> eliminateList = recipeList.stream().filter(recipe -> {
			boolean containsEliminate = ExcelData.LFVEliminate.stream()
					.anyMatch(value -> recipe.ingredients.contains(value));

			if (containsEliminate)
				return false;

			boolean containsAdd = ExcelData.LFVAdd.stream().anyMatch(value -> recipe.ingredients.contains(value));

			return !containsAdd;
		}).collect(Collectors.toSet());

		System.out.println("Number of recipes for Eliminate table: " + eliminateList.size());

		for (Recipe recipe : eliminateList) {
			DBConnection.saveRecipeToDatabase(recipe, "LFV_elemination");
		}
	}

	public void filterAddRecipes(List<Recipe> recipeList) {
		// Load Excel data
		ExcelData.LoadLFVData();
		DBConnection.initConnection();

		Set<Recipe> addList = recipeList.stream().filter(recipe -> {
			boolean containsEliminate = ExcelData.LFVEliminate.stream()
					.anyMatch(value -> recipe.ingredients.contains(value));

			if (containsEliminate)
				return false;

			boolean containsAdd = ExcelData.LFVAdd.stream().anyMatch(value -> recipe.ingredients.contains(value));

			return containsAdd; // Only difference is this return value
		}).collect(Collectors.toSet());

		System.out.println("Number of recipes for Add table: " + addList.size());

		for (Recipe recipe : addList) {
			DBConnection.saveRecipeToDatabase(recipe, "LFV_to_add");
		}

		System.out.println("\nTotal " + addList.size() + " recipes saved to database.");
	}

	public void filterLFVAllergyNut(List<Recipe> recipeList) {
		// Load Excel data
		ExcelData.LoadLFVData();
		DBConnection.initConnection();

		Set<Recipe> nutAllergyRecipes = recipeList.stream().filter(recipe -> {
			boolean containsNut = ExcelData.LFVAllergyNut.stream()
					.anyMatch(nut -> recipe.ingredients.toLowerCase().contains(nut));
			return containsNut;
		}).collect(Collectors.toSet());

		System.out.println("Number of recipes with nut allergens: " + nutAllergyRecipes.size());

		for (Recipe recipe : nutAllergyRecipes) {
			DBConnection.saveRecipeToDatabase(recipe, "LFV_Allergy_Nut");
		}

		System.out.println("\nTotal " + nutAllergyRecipes.size() + " recipes saved to database.");
	}

	public void filterLCHFAllergyNut(List<Recipe> recipeList) {
		// Load Excel data
		ExcelData.LoadLCHFData();
		DBConnection.initConnection();

		Set<Recipe> nutAllergyRecipes = recipeList.stream().filter(recipe -> {
			boolean containsNut = ExcelData.LCHFAllergyNut.stream()
					.anyMatch(nut -> recipe.ingredients.toLowerCase().contains(nut));
			return containsNut;
		}).collect(Collectors.toSet());

		System.out.println("Number of nut allergy recipes found: " + nutAllergyRecipes.size());

		for (Recipe recipe : nutAllergyRecipes) {
			DBConnection.saveRecipeToDatabase(recipe, "LCHF_Allergy_Nut");
		}

		System.out.println("\nTotal " + nutAllergyRecipes.size() + " recipes saved to database.");
	}

}
