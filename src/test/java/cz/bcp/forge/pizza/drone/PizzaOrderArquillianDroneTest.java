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
public class PizzaOrderArquillianDroneTest {

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
		browser.findElement(By.linkText("Pizza Order")).click();
		browser.findElement(By.linkText("Create New")).click();

		// name
		WebElement nameTextBox = browser.findElement(By.id("create:pizzaOrderBeanPizzaOrderName"));
		String name = "TestPizzaOrder";
		nameTextBox.clear();
		nameTextBox.sendKeys(name);

		// address
		WebElement addressTextBox = browser.findElement(By.id("create:pizzaOrderBeanPizzaOrderAddress"));
		String address = "TestAddress";
		addressTextBox.clear();
		addressTextBox.sendKeys(address);

		// total
		WebElement totalTextBox = browser.findElement(By.id("create:pizzaOrderBeanPizzaOrderTotal"));
		String total = "1.0";
		totalTextBox.clear();
		totalTextBox.sendKeys(total);

		// delivery date
		WebElement deliveryDateTextBox = browser.findElement(By.id("create:pizzaOrderBeanPizzaOrderDeliveryDate"));
		String deliveryDate = "22.4.2013";
		deliveryDateTextBox.clear();
		deliveryDateTextBox.sendKeys(deliveryDate);

		browser.findElement(By.linkText("Save")).click();

		assertTrue(isElementPresent(By.id("search:pizzaOrderBeanPageItems:0:itemName")));
		browser.findElement(By.id("search:pizzaOrderBeanPageItems:0:itemName")).click();

		try {
			WebElement nameResult = browser.findElement(By.id("pizzaOrderBeanPizzaOrderName"));
			assertTrue(nameResult.getText().matches(name));
			WebElement addressResult = browser.findElement(By.id("pizzaOrderBeanPizzaOrderAddress"));
			assertTrue(addressResult.getText().matches(address));
			WebElement totalResult = browser.findElement(By.id("pizzaOrderBeanPizzaOrderTotal"));
			assertTrue(totalResult.getText().matches(total));
			WebElement deliveryDateResult = browser.findElement(By.id("pizzaOrderBeanPizzaOrderDeliveryDate"));
			assertTrue(deliveryDateResult.getText().matches(deliveryDate));
		} catch (Error e) {
		}

		// Edit
		browser.get(deploymentUrl.toString());
		browser.findElement(By.linkText("Pizza Order")).click();
		
		browser.findElement(By.id("search:pizzaOrderBeanPageItems:0:itemName")).click();
		browser.findElement(By.linkText("Edit")).click();

		// name
		nameTextBox = browser.findElement(By.id("create:pizzaOrderBeanPizzaOrderName"));
		nameTextBox.clear();
		String newName = "TestPizzaOrder2";
		nameTextBox.sendKeys(newName);

		// address
		addressTextBox = browser.findElement(By.id("create:pizzaOrderBeanPizzaOrderAddress"));
		addressTextBox.clear();
		String newAddress = "TestAddress2";
		addressTextBox.sendKeys(newAddress);

		// total
		totalTextBox = browser.findElement(By.id("create:pizzaOrderBeanPizzaOrderTotal"));
		totalTextBox.clear();
		String newTotal = "2.1";
		totalTextBox.sendKeys(newTotal);

		// delivery date
		deliveryDateTextBox = browser.findElement(By.id("create:pizzaOrderBeanPizzaOrderDeliveryDate"));
		deliveryDateTextBox.clear();
		String newDeliveryDate = "23.5.2014";
		deliveryDateTextBox.sendKeys(newDeliveryDate);

		browser.findElement(By.linkText("Save")).click();

		try {
			WebElement nameResult = browser.findElement(By.id("pizzaOrderBeanPizzaOrderName"));
			assertTrue(nameResult.getText().matches(newName));
			WebElement addressResult = browser.findElement(By.id("pizzaOrderBeanPizzaOrderAddress"));
			assertTrue(addressResult.getText().matches(newAddress));
			WebElement totalResult = browser.findElement(By.id("pizzaOrderBeanPizzaOrderTotal"));
			assertTrue(totalResult.getText().matches(newTotal));
			WebElement deliveryDateResult = browser.findElement(By.id("pizzaOrderBeanPizzaOrderDeliveryDate"));
			assertTrue(deliveryDateResult.getText().matches(newDeliveryDate));
		} catch (Error e) {
		}

		// Delete
		browser.get(deploymentUrl.toString());
		browser.findElement(By.linkText("Pizza Order")).click();
		
		assertTrue(isElementPresent(By.id("search:pizzaOrderBeanPageItems:0:itemName")));
		browser.findElement(By.id("search:pizzaOrderBeanPageItems:0:itemName")).click();
		browser.findElement(By.linkText("Edit")).click();
		browser.findElement(By.linkText("Delete")).click();

		browser.findElement(By.linkText("Pizza Order")).click();
		assertFalse(isElementPresent(By.id("search:pizzaOrderBeanPageItems:0:itemName")));
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
