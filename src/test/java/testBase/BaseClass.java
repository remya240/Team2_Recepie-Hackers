package testBase;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;

import driverManager.DriverFactory;
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

		System.out.println("*******");
		url = this.configReader.getUrl();
		driver.get(url);
		driver.manage().window().maximize();
		// driver.get("https://www.tarladalal.com/");
		
		
	}
	
//	@AfterSuite
//	public static void quitDriver() {
//		if (driver != null) {
//			driver.quit();
//		}
//	}

	public static WebDriver getDriver() {
		return driver;
	}
}