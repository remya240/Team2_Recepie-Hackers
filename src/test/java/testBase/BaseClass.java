package testBase;

import driverManager.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utilities.ConfigReader;

public class BaseClass {
    String url;
    ConfigReader configReader;
    DriverFactory driverFactory;

    public BaseClass() {
        this.driverFactory = new DriverFactory();
        this.configReader = new ConfigReader();

    }

    protected static WebDriver driver;

    @BeforeSuite
    public void initializeDriver() {

        String browser = DriverFactory.getBrowserType();
        driver = driverFactory.driverSetup(browser);
        url = this.configReader.getUrl();
        driver.get(url);
        driver.manage().window().maximize();
    }

    @AfterSuite
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}