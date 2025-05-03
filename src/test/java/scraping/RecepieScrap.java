package scraping;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import driverManager.DriverFactory;
import testBase.BaseClass;

public class RecepieScrap extends BaseClass { 
    
    @Test
    public void testRecipeScraping() {
    	System.out.println("$$$$$$");
        WebDriver driver =DriverFactory.getDriverInstance();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
            System.out.println("Current URL: " + driver.getCurrentUrl());
            
       
         
}
}