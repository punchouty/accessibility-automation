package com.racloop.accessibility;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {

    private static final int TIMEOUT = 10;
    protected WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setupTest() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void waitToBeClickable(String xPath) {
        WebElement webElement = driver.findElement(By.xpath(xPath));
        WebDriverWait webDriverWait = new WebDriverWait(driver, TIMEOUT);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    @Test
    public void test() {
        //yet to implement
    }
}
