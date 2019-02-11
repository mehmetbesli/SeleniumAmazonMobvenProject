package com.amazon;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumMovbenProject {

	private Logger logger = Logger.getLogger("SeleniumMovbenProject");

	private WebDriver driver;
	public String checkProductExist;

	private String secondProductPriceString;

	public static void main(String[] args) {
		SeleniumMovbenProject movbenProject = new SeleniumMovbenProject();
		movbenProject.runAllSteps();
	}

	private void runAllSteps() {
		openChromeBrowser();
		gotoAmazonWebPage();
		enterProductName();
		selectRequiredProductInSuggestionPopup();
		selectFirstProduct();
		checkSecondProductPriceAtOtherSeller();
		putLowestProductInBasket();
		checkBasketStatus();
		closeBrowser();
	}

	public void openChromeBrowser() {
		String path = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", path + "\\libs\\chromedriver.exe");
		driver = new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	/**
	 * will go to amazon web page
	 */
	public void gotoAmazonWebPage() {
		driver.get("https://www.amazon.com.tr/");
	}

	/**
	 * will enter product name in searh box
	 */
	public void enterProductName() {
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(MovbenProjectConstant.iphoneName);
	}

	/**
	 * select 'iphone x' from suggestion box
	 */
	public void selectRequiredProductInSuggestionPopup() {
		// get all suggested products
		List<WebElement> list = driver.findElements(By.xpath(".//*[contains(@id,'issDiv')]"));
		// select iphone x
		for (WebElement webElement : list) {
			if (webElement.getText().equals(MovbenProjectConstant.iphoneXName)) {
				webElement.click();
				break;
			}
		}
	}

	/**
	 * select first product
	 */
	public void selectFirstProduct() {
		driver.findElement(By.xpath("//div[@id='atfResults']/ul/li//div[@class='a-fixed-left-grid-col a-col-left']")).click();
	}

	/**
	 * get price of second product of other seller section which is shown at the
	 * right panel.
	 */
	public void checkSecondProductPriceAtOtherSeller() {
		secondProductPriceString = driver.findElement(By.id("mbc-price-2")).getText();
	}

	/**
	 * put lowest price of product in basket
	 */
	public void putLowestProductInBasket() {
		String selectedProductPriceString = driver.findElement(By.id("priceblock_ourprice")).getText();
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("tr", "TR"));
		try {
			double seconProductPrice = numberFormat.parse(secondProductPriceString).doubleValue();
			double selectedProductPrice = numberFormat.parse(selectedProductPriceString).doubleValue();
			if (selectedProductPrice > seconProductPrice) {
				driver.findElement(By.id("add-to-cart-button")).click();
				logger.info("Selected product added to basket");
			} else {
				driver.findElement(By.id("mbc-buybutton-addtocart-2-announce")).click();
				logger.info("Second product of other seller added to basket");
			}
		} catch (ParseException e) {
			logger.error("Error occured when price compared", e);
		}
	}

	/**
	 * Did product add to basket or not?
	 */
	public void checkBasketStatus() {
		checkProductExist = driver.findElement(By.xpath("//h1[@class='a-size-medium a-text-bold']")).getText();
		//check product exist or not
		if (checkProductExist != null) {
			logger.info("Product added to basket.");
		} else {
			logger.error("Product could not add to basket");
		}
	}

	/**
	 * close active tested browser
	 */
	public void closeBrowser() {
		try {
			driver.close();
			logger.info("Browser closed");
		} catch (Exception e) {
			logger.error("Exception occured when browser closed", e);
		}
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public String getSecondProductPriceString() {
		return secondProductPriceString;
	}

	public void setSecondProductPriceString(String secondProductPriceString) {
		this.secondProductPriceString = secondProductPriceString;
	}

	public String getCheckProductExist() {
		return checkProductExist;
	}

	public void setCheckProductExist(String checkProductExist) {
		this.checkProductExist = checkProductExist;
	}
}
