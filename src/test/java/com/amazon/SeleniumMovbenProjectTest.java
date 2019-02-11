package com.amazon;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.junit.After;
import org.junit.Assert;

public class SeleniumMovbenProjectTest {

	SeleniumMovbenProject movbenProject;
	
	@Before
	public void startBrowser() {
		movbenProject = new SeleniumMovbenProject();
		movbenProject.openChromeBrowser();
	}
	
	@After
	public void closeBrowser() {
		movbenProject.closeBrowser();
	}
	
	@Test
	public void gotoAmazonWebPageTest() {
		movbenProject.gotoAmazonWebPage();
		Assert.assertTrue("You are not in amazon web site", movbenProject.getDriver().getCurrentUrl().contains("amazon.com.tr"));
	}
	
	@Test
	public void searchProductTest() {
		movbenProject.gotoAmazonWebPage();
		movbenProject.enterProductName();
		String actualProductName = movbenProject.getDriver().findElement(By.id("twotabsearchtextbox")).getAttribute("value");
		Assert.assertEquals("Could not enter keyword", MovbenProjectConstant.iphoneName, actualProductName);
	}
	
	@Test
	public void selectRequiredProductInSuggestionPopupTest() {
		movbenProject.gotoAmazonWebPage();
		movbenProject.enterProductName();
		movbenProject.selectRequiredProductInSuggestionPopup();
		String actualProductName = movbenProject.getDriver().findElement(By.id("twotabsearchtextbox")).getAttribute("value");
		Assert.assertEquals("Could not select wanted product", MovbenProjectConstant.iphoneXName, actualProductName);
	}
	
	@Test
	public void selectFirstProductTest() {
		movbenProject.gotoAmazonWebPage();
		movbenProject.enterProductName();
		movbenProject.selectRequiredProductInSuggestionPopup();
		movbenProject.selectFirstProduct();
		Assert.assertTrue("Not selected first product", movbenProject.getDriver().getCurrentUrl().contains("sr_1_1"));
	}
	
	@Test
	public void checkSecondProductPriceAtOtherSellerTest() {
		movbenProject.gotoAmazonWebPage();
		movbenProject.enterProductName();
		movbenProject.selectRequiredProductInSuggestionPopup();
		movbenProject.selectFirstProduct();
		movbenProject.checkSecondProductPriceAtOtherSeller();
		Assert.assertTrue("Could not select price os second product of other seller", movbenProject.getSecondProductPriceString() != null);
	}
	
	@Test
	public void putLowestProductInBasketTest() {
		movbenProject.gotoAmazonWebPage();
		movbenProject.enterProductName();
		movbenProject.selectRequiredProductInSuggestionPopup();
		movbenProject.selectFirstProduct();
		movbenProject.checkSecondProductPriceAtOtherSeller();
		movbenProject.putLowestProductInBasket();
		movbenProject.checkBasketStatus();
		Assert.assertTrue("", movbenProject.getCheckProductExist() != null);
	}
	
}
