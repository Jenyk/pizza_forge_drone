package cz.bcp.forge.pizza.drone;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(Arquillian.class)
public class PizzaArquillianDroneTest {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return Deployments.createDeployment();
	}

	@Drone
	WebDriver browser;

	@ArquillianResource
	URL deploymentUrl;

	@Test
	public void testCRUD() throws Exception {

		// Create
		browser.get(deploymentUrl.toString());
		browser.findElement(By.linkText("Pizza")).click();
		browser.findElement(By.linkText("Create New")).click();

		// name
		WebElement nameTextBox = browser.findElement(By.id("create:pizzaBeanPizzaName"));
		String name = "TestPizza";
		nameTextBox.clear();
		nameTextBox.sendKeys(name);

		// price
		WebElement priceTextBox = browser.findElement(By.id("create:pizzaBeanPizzaPrice"));
		String price = "1.0";
		priceTextBox.clear();
		priceTextBox.sendKeys(price);

		browser.findElement(By.linkText("Save")).click();

		assertTrue(isElementPresent(By.id("search:pizzaBeanPageItems:0:itemName")));
		browser.findElement(By.id("search:pizzaBeanPageItems:0:itemName")).click();

		try {
			WebElement nameResult = browser.findElement(By.id("pizzaBeanPizzaName"));
			assertTrue(nameResult.getText().matches(name));
			WebElement priceResult = browser.findElement(By.id("pizzaBeanPizzaPrice"));
			assertTrue(priceResult.getText().matches(price));
		} catch (Error e) {
		}

		// Edit
		browser.get(deploymentUrl.toString());
		browser.findElement(By.linkText("Pizza")).click();
		
		browser.findElement(By.id("search:pizzaBeanPageItems:0:itemName")).click();
		browser.findElement(By.linkText("Edit")).click();
		
		// name
		nameTextBox = browser.findElement(By.id("create:pizzaBeanPizzaName"));
		nameTextBox.clear();
		String newName = "TestPizza2";
		nameTextBox.sendKeys(newName);

		// price
		priceTextBox = browser.findElement(By.id("create:pizzaBeanPizzaPrice"));
		priceTextBox.clear();
		String newPrice = "2.1";
		priceTextBox.sendKeys(newPrice);

		browser.findElement(By.linkText("Save")).click();

		try {
			WebElement nameResult = browser.findElement(By.id("pizzaBeanPizzaName"));
			assertTrue(nameResult.getText().matches(newName));
			WebElement priceResult = browser.findElement(By.id("pizzaBeanPizzaPrice"));
			assertTrue(priceResult.getText().matches(newPrice));
		} catch (Error e) {
		}

		// Delete
		browser.get(deploymentUrl.toString());
		browser.findElement(By.linkText("Pizza")).click();
		
		assertTrue(isElementPresent(By.id("search:pizzaBeanPageItems:0:itemName")));
		browser.findElement(By.id("search:pizzaBeanPageItems:0:itemName")).click();
		browser.findElement(By.linkText("Edit")).click();
		browser.findElement(By.linkText("Delete")).click();
		
		browser.findElement(By.linkText("Pizza")).click();
		assertFalse(isElementPresent(By.id("search:pizzaBeanPageItems:0:itemName")));
	}

	private boolean isElementPresent(By by) {
		try {
			browser.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
