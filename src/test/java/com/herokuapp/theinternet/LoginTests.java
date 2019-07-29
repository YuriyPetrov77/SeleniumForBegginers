package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginTests {
	WebDriver driver;
	WebDriverWait wait;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional("chrome") String browser) {
// Create driver
		switch (browser) {

		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			wait = new WebDriverWait(driver, 3);
			break;

		case "firefox":
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
			driver = new FirefoxDriver();
			wait = new WebDriverWait(driver, 3);
			break;

		default:
			System.out.println("Do not know how to start " + browser + " , starting chrome instead");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			wait = new WebDriverWait(driver, 3);
			break;
		}

// maximize browser window
		driver.manage().window().maximize();
		
// implicitWait
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		
	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLoginTest(String username, String password, String expectedMessage) {

		System.out.println("Starting Positive loginTest with correct username: " + username + " and correct Password: "
				+ password);

// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Testing page is opened.");

// enter user name
		driver.findElement(By.id("username")).sendKeys(username);
// enter password
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
// click login button
		//driver.findElement(By.className("radius")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.className("radius"))).click();

		sleep(1);
// verification
// new url
		String expectedUrl = "http://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected!");
		System.out.println("Right URL was open.");

// logout button is visible
		WebElement logOutButton = driver.findElement(By.xpath("//*[@class='button secondary radius']"));
		Assert.assertTrue(logOutButton.isDisplayed(), "Log out button is not visible!");
		System.out.println("LogOut Button is visible.");

// succesful login message
		WebElement successMessage = driver.findElement(By.cssSelector("#flash"));
		String actualMessage = successMessage.getText();
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Messga:  " + expectedMessage);
		System.out.println("Right LogIn and Password working properly! ");
		sleep(1);

	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })

	public void negativeLoginTest(String username, String password, String expectedMessage) {
		System.out.println("Starting the negativeLoginTest with Username: " + username + " and Password: " + password);

// Open the start page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);

// Input Incorrect Username (random)
		driver.findElement(By.id("username")).sendKeys(username);

// Input positivePass
		driver.findElement(By.id("password")).sendKeys(password);

// Click LoginButton
		driver.findElement(By.xpath("//button[@class='radius']")).click();

// Verification the same URL
		String expectedUrl = "http://the-internet.herokuapp.com/login";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected!");

// logIn button still visible
		WebElement logInButton = driver.findElement(By.xpath("//button[@class='radius']"));
		Assert.assertTrue(logInButton.isDisplayed(), "Login button still visible!");

// Verification the error message
		WebElement unsuccessMessage = driver.findElement(By.cssSelector("#flash"));
		String actualMessage = unsuccessMessage.getText();

		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Messga:  " + expectedMessage);

	}

	private void sleep(long m) {
		try {
			Thread.sleep(m * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// Close browser
		driver.quit();

	}
}
