package scraping;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import driverManager.DriverFactory;
import testBase.BaseClass;


public class RecepieScrap extends BaseClass {

	@Test
	public void testRecipeScraping() throws InterruptedException {
		System.out.println("$$$$$$");
		WebDriver driver = DriverFactory.getDriverInstance();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		System.out.println("Current URL: " + driver.getCurrentUrl());
		Thread.sleep(3000);
		
		String recipeTab =  driver.getWindowHandle();
		int recipePages = getNumOfPages();
		for (int j = 1; j <= recipePages; j++) {
			try {

				
				List<WebElement> recipeBlocks = driver
						.findElements(By.xpath("//main//div[contains(@class,'recipe-block')]"));

				for (WebElement recipe : recipeBlocks) {
					String recipeName = recipe.findElement(By.tagName("h5")).getText();
					WebElement recipeLink = recipe.findElement(By.tagName("a")); // recipe link

					System.out.println("Recipe Name: " + recipeName);
					System.out.println("Recipe Link : " + recipeLink.getAttribute("href"));

					((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');",
							recipeLink.getAttribute("href"));
					String recipeDetailTab = switchToTabByIndex(1);

					// Need to get data from page (scraping)

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
				((JavascriptExecutor) driver).executeScript("arguments[0].click();",
						nextRecipePageButton.get(0));
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
