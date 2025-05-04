package scraping;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import driverManager.DriverFactory;
import testBase.BaseClass;
import utilities.LoggerLoad;

public class RecepieScrap extends BaseClass {

    @Test
    public void testRecipeScraping() throws Exception {
        System.out.println("$$$$$$");
        WebDriver driver = DriverFactory.getDriverInstance();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            navigateToCategories(driver, wait); 


        } catch (Exception e) {
            LoggerLoad.error("Error in recipe scraping: " + e.getMessage());
            throw e;
        }
    }

    private void navigateToCategories(WebDriver driver, WebDriverWait wait) throws Exception {
        try {
            LoggerLoad.info("Current URL: " + driver.getCurrentUrl());

            LoggerLoad.debug("Locating 'Categories' dropdown");
            WebElement categoriesDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[@class='navbar-nav d-flex flex-row mob-hide']//a[normalize-space()='Categories']")));
            categoriesDropdown.click();

            LoggerLoad.debug("Locating 'View All Category' link");
            WebElement viewAllCategory = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[@class='dropdown-menu show']//a[normalize-space()='View All Category']")));
            viewAllCategory.click();

            LoggerLoad.info("Landed on 'View All Category' page");

        } catch (Exception e) {
            LoggerLoad.error("Failed to navigate to categories: " + e.getMessage());
            throw e;
        }
    }


    

}
