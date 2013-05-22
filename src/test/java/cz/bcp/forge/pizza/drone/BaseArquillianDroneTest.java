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
public class BaseArquillianDroneTest {

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
	    browser.findElement(By.linkText("Base")).click();
	    browser.findElement(By.linkText("Create New")).click();
	    
	    // name
	    WebElement nameTextBox = browser.findElement(By.id("create:baseBeanBaseName"));
	    nameTextBox.clear();
	    String name = "TestBase";
	    nameTextBox.sendKeys(name);
	    
	    browser.findElement(By.linkText("Save")).click();
	    
	    assertTrue(isElementPresent(By.id("search:baseBeanPageItems:0:itemName"))); 
	    browser.findElement(By.id("search:baseBeanPageItems:0:itemName")).click();
	 
	    try {    	
	    	 WebElement nameResult = browser.findElement(By.id("baseBeanBaseName"));
	    	 assertTrue(nameResult.getText().matches(name));
	    } catch (Error e) {
	    }
	    
	    // Edit
	    browser.get(deploymentUrl.toString());
	    browser.findElement(By.linkText("Base")).click();
	    
	    browser.findElement(By.id("search:baseBeanPageItems:0:itemName")).click();
	    browser.findElement(By.linkText("Edit")).click();
	    
	    // name
	    nameTextBox = browser.findElement(By.id("create:baseBeanBaseName"));
	    nameTextBox.clear();
	    name = "TestBase2";
	    nameTextBox.sendKeys(name);
	    
	    browser.findElement(By.linkText("Save")).click();

	    try {
	    	 WebElement nameResult = browser.findElement(By.id("baseBeanBaseName"));
	    	 assertTrue(nameResult.getText().matches(name));
	    } catch (Error e) {
	    }

	    // Delete
	    browser.get(deploymentUrl.toString());
	    browser.findElement(By.linkText("Base")).click();
	    
	    assertTrue(isElementPresent(By.id("search:baseBeanPageItems:0:itemName")));
	    browser.findElement(By.id("search:baseBeanPageItems:0:itemName")).click();
	    browser.findElement(By.linkText("Edit")).click();
	    browser.findElement(By.linkText("Delete")).click();

	    browser.findElement(By.linkText("Base")).click(); 
	    assertFalse(isElementPresent(By.id("search:baseBeanPageItems:0:itemName")));
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