package driverManager;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utilities.ConfigReader;

public class DriverFactory {

	private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
	static ConfigReader configReader;

	private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
	public final static int TIMEOUT = 2;
	static String browserType = null;

	public static void setBrowserType(String browser) {
		browserType = browser;
	}

	public static String getBrowserType() {
		if (browserType == null) {
			try {
				configReader = new ConfigReader();
				browserType = configReader.getBrowser();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return browserType;
	}

	public WebDriver driverSetup(String browser) {
		if (browser.equalsIgnoreCase("Chrome")) {
			threadDriver.set(new ChromeDriver());
		} else if (browser.equalsIgnoreCase("Firefox")) {
			threadDriver.set(new FirefoxDriver());
		} else if (browser.equalsIgnoreCase("Edge")) {
			threadDriver.set(new EdgeDriver());
		} else {
			threadDriver.set(new ChromeDriver());
		}

		WebDriver driver = threadDriver.get();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
		driver.manage().window().maximize();
		logger.info("Driver initialised with browser as:  " + browser);
		return driver;

	}

	public static WebDriver getDriverInstance() {
		return threadDriver.get();
	}

	public String getTitle() {
		return threadDriver.get().getTitle();
	}

}
