package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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

public class ExceptionsTest {
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
		// driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);

	}

	@Test(priority = 1)
	public void notVisibleTest() {
//Open url
		String url = "https://the-internet.herokuapp.com/dynamic_loading/1";
		driver.get(url);

// Push the Start button
		driver.findElement(By.xpath("//div[@id='start']/button")).click();

//Get string Hello World!
		WebElement actualMessage = driver.findElement(By.xpath("//div[@id='finish']/h4"));
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(actualMessage));
		String finishText = actualMessage.getText();
		String expectedMessage = "Hello World!";

		Assert.assertTrue(finishText.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Messga:  " + expectedMessage);
		System.out.println("Actual Message = Expected message.Test is success! ");
	}

	@Test(priority = 2)
	public void timeOutTest() {
//Open url
		String url = "https://the-internet.herokuapp.com/dynamic_loading/1";
		driver.get(url);

// Push the Start button
		driver.findElement(By.xpath("//div[@id='start']/button")).click();

//Get string Hello World!
		WebElement actualMessage = driver.findElement(By.xpath("//div[@id='finish']/h4"));
		wait = new WebDriverWait(driver, 2);
		try {
			wait.until(ExpectedConditions.visibilityOf(actualMessage));
		} catch (TimeoutException exception) {
			System.out.println("Exception catched: " + exception.getMessage());
			sleep(3);

		}

		String finishText = actualMessage.getText();
		String expectedMessage = "Hello World!";

		Assert.assertTrue(finishText.contains(expectedMessage),
				"Actual message does not contain expected message.\nActual Message: " + actualMessage
						+ "\nExpected Messga:  " + expectedMessage);
		System.out.println("Actual Message = Expected message.Test is success! ");
	}

	@Test(priority = 3)
	public void noSuchElementTest() {
//Open url
		String url = "https://the-internet.herokuapp.com/dynamic_loading/2";
		driver.get(url);

// Push the Start button
		driver.findElement(By.xpath("//div[@id='start']/button")).click();

//Get string Hello World!
		wait = new WebDriverWait(driver, 10);
		Assert.assertTrue(wait.until(
				ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@id='finish']/h4"), "Hello World!")),
				"Couldn't verify expected text 'Hello World!'");
	}

	@Test(priority = 4)
	public void staleElementTest() {

		String url = "https://the-internet.herokuapp.com/dynamic_controls";
		driver.get(url);

		WebElement checkBox = driver.findElement(By.xpath("//input[@type='checkbox']"));
		WebElement removeBut = driver.findElement(By.xpath("//button[contains(text(),'Remove')]"));

		removeBut.click();
		wait = new WebDriverWait(driver, 10);

		// wait.until(ExpectedConditions.invisibilityOf(checkBox));
		// Assert.assertFalse(checkBox.isDisplayed());

		// Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkBox)),
		// "Check box is visible, but should not be");

		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkBox)),
				"Check box is visible, but should not be");

		WebElement addBut = driver.findElement(By.xpath("//button[contains(text(),'Add')]"));
		addBut.click();
		WebElement checkBox2 = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='checkbox']")));
		Assert.assertTrue(checkBox2.isDisplayed(), "Check box is not visible, but it should be");

	}

	@Test(priority = 5)

	public void disabledElementTest() {
		wait = new WebDriverWait(driver, 10);
		driver.get("https://the-internet.herokuapp.com/dynamic_controls");
		String text = "I'm did that challenge!!!";
		
		WebElement enableBut = driver.findElement(By.xpath("//button[contains(text(),'Enable')]"));
		WebElement textField = driver.findElement(By.xpath("//input[@type='text']"));
		
		enableBut.click();
	
		wait.until(ExpectedConditions.elementToBeClickable(textField));
		textField.click();
		textField.sendKeys(text);
		
				
		//String expText = driver.findElement(By.xpath("//input[@type='text']")).getAttribute("value");
		//System.out.println("expText is: " + expText);
		
		Assert.assertEquals(textField.getAttribute("value"),text,"Text is not matched!");
		}
	

	private void sleep(long m) {
		try {
			Thread.sleep(m * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// Close browser
		driver.quit();

	}
}
