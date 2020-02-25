package com.racloop.accessibility;

import com.racloop.accessibility.util.ContrastChecker;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import java.util.HashSet;
import java.util.List;

public class SampleTest extends BaseTest {

    public static final String BASER_URL = "http://qaregression.com";
    public static final String ERROR_PAGE = BASER_URL + "/inaccessible-page/error.html";
    public static final String NO_ERROR_PAGE = BASER_URL + "/index.html";
    public static final String FORM_ERROR_PAGE = BASER_URL + "/inaccessible-page/form-error.html";

    @Test
    public void blinkingTest() {
        driver.get(NO_ERROR_PAGE);
        String htmlSource = driver.getPageSource();
        if(htmlSource.contains("<blink>") || htmlSource.contains("<marquee>")) {
            Assert.fail("Blinking Test Present");
        }
    }

    @Test
    public void titleTest() {
        driver.get(NO_ERROR_PAGE);
        List<WebElement> elements = driver.findElements(By.xpath("//title"));
        Assert.assertEquals(1, elements.size());
    }

    @Test
    public void visualFormattingTest() {
        driver.get(NO_ERROR_PAGE);
        String htmlSource = driver.getPageSource();
        if(htmlSource.contains("<b>") || htmlSource.contains("<u>") || htmlSource.contains("<font>")|| htmlSource.contains("<center>")) {
            Assert.fail("Visual formatting present");
        }
    }

    @Test
    public void languageAttributeTest() {
        driver.get(NO_ERROR_PAGE);
        WebElement element = driver.findElement(By.xpath("//html"));
        String language = element.getAttribute("lang");
        Assert.assertFalse(language.isEmpty());
    }

    @Test
    public void blinkingTestNegative() {
        driver.get(ERROR_PAGE);
        String htmlSource = driver.getPageSource();
        if(htmlSource.contains("<blink>") || htmlSource.contains("<marquee>")) {
            Assert.fail("Blinking Test Present");
        }
    }

    @Test
    public void languageAttributeTestNegative() {
        driver.get(ERROR_PAGE);
        WebElement element = driver.findElement(By.xpath("//html"));
        String language = element.getAttribute("lang");
        Assert.assertFalse(language.isEmpty());
    }

    @Test
    public void titleTestNegative() {
        driver.get(ERROR_PAGE);
        List<WebElement> elements = driver.findElements(By.xpath("//title"));
        Assert.assertEquals(1, elements.size());
    }

    @Test
    public void hyperLinkTextTest() {
        driver.get(NO_ERROR_PAGE);
        List<WebElement> elements = driver.findElements(By.xpath("//a"));
        for (WebElement element : elements) {
            String text = element.getText();
            String href = element.getAttribute("href");
            Assert.assertNotNull(text);
            Assert.assertFalse(text.isEmpty());
            Assert.assertNotEquals("#", href);
        }
    }

    @Test
    public void hyperLinkTextTestNegative() {
        driver.get(ERROR_PAGE);
        List<WebElement> elements = driver.findElements(By.xpath("//a"));
        for (WebElement element : elements) {
            String text = element.getText();
            String href = element.getAttribute("href");
            Assert.assertNotNull(text);
            Assert.assertFalse(text.isEmpty());
            Assert.assertNotEquals("#", href);
        }
    }

    @Test
    public void altTextOnImage() {
        String xpath = "//img[@height='140']";
        driver.get(ERROR_PAGE);
        List<WebElement> imageElements = driver.findElements(By.xpath(xpath));
        if(imageElements.size() == 0) {
            Assert.fail("No image found with xpath : " + xpath);
        }
        for (WebElement imageElement : imageElements) {
            String altAttributeText = imageElement.getAttribute("alt");
            Assert.assertFalse(altAttributeText.isEmpty());
        }
    }

    @Test
    public void tableTest() {
        driver.get(ERROR_PAGE);
        WebElement table = driver.findElement(By.xpath("//table[@id='table-correct']"));
        List<WebElement> elements = table.findElements(By.tagName("caption"));
        System.out.println("Size : " + elements.size());
        boolean isPresent = elements.size() == 1;
        String summary = table.getAttribute("summary");
        if (!isPresent && summary.isEmpty()) {
            Assert.fail("Either caption or table need to be there on table");
        }

    }

    @Test
    public void tableHeadersTest() {
        driver.get(ERROR_PAGE);
        List<WebElement> tableHeaders = driver.findElements(By.xpath("//table[@id='table-correct']//th"));
        if(tableHeaders.size() == 0) {
            Assert.fail("Table Headers should have attributes to specify row and column headers");
        }
        for (WebElement tableHeader : tableHeaders) {
            String headerAttributeText = tableHeader.getAttribute("header");
            String roleAttributeText = tableHeader.getAttribute("role");
            String scopeAttributeText = tableHeader.getAttribute("scope");
            if( (headerAttributeText == null) && (roleAttributeText == null) && (scopeAttributeText == null)) {
                Assert.fail("Table Headers should have attributes to specify row and column headers");
            }
        }
    }

    @Test
    public void tableTestNegative() {
        driver.get(ERROR_PAGE);
        WebElement table = driver.findElement(By.xpath("//table[@id='table-incorrect']"));
        List<WebElement> elements = table.findElements(By.tagName("caption"));
        System.out.println("Size : " + elements.size());
        boolean isPresent = elements.size() == 1;
        String summary = table.getAttribute("summary");
        if (!isPresent && summary.isEmpty()) {
            Assert.fail("Either caption or table need to be there on table");
        }
    }

    @Test
    public void tableHeadersTestNegative() {
        driver.get(ERROR_PAGE);
        List<WebElement> tableHeaders = driver.findElements(By.xpath("//table[@id='table-incorrect']//th"));
        if(tableHeaders.size() == 0) {
            Assert.fail("Table Headers should have attributes to specify row and column headers");
        }
        for (WebElement tableHeader : tableHeaders) {
            String headerAttributeText = tableHeader.getAttribute("header");
            String roleAttributeText = tableHeader.getAttribute("role");
            String scopeAttributeText = tableHeader.getAttribute("scope");
            if( (headerAttributeText == null) && (roleAttributeText == null) && (scopeAttributeText == null)) {
                Assert.fail("Table Headers should have attributes to specify row and column headers");
            }
        }
    }

    @Test
    public void pageZoomInTest() throws InterruptedException {
        driver.get(ERROR_PAGE);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.style.zoom='200%'");
        Thread.sleep(3000);
        //check all important elements for their visibility
    }

    @Test
    public void keyboardTrapTest() {
        driver.get(FORM_ERROR_PAGE);
        HashSet<String> identifiers = new HashSet<>();
        WebElement focusedElement = driver.findElement(By.xpath("//input"));
        String current = null;
        String previous = null;
        String first = null;
        String last = null;
        while(focusedElement != null && !identifiers.contains(current)) {
            System.out.println(focusedElement);
            current = generateXpath(focusedElement, "");
            System.out.println(current);
            if(previous == null) {
                first = current;
                identifiers.add(current);
                focusedElement.sendKeys(Keys.TAB);
                focusedElement = driver.switchTo().activeElement();
                previous = current;
                current = generateXpath(focusedElement, "");
                continue;
            }
            identifiers.add(current);
            focusedElement.sendKeys(Keys.TAB);
            focusedElement = driver.switchTo().activeElement();
            previous = current;
            current = generateXpath(focusedElement, "");
            last = current;
        }
        Assert.assertEquals(first, last);
    }

    private String generateXpath(WebElement childElement, String current) {
        String childTag = childElement.getTagName();
        if(childTag.equals("html")) {
            return "/html[1]"+current;
        }
        WebElement parentElement = childElement.findElement(By.xpath(".."));
        List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
        int count = 0;
        for(int i=0;i<childrenElements.size(); i++) {
            WebElement childrenElement = childrenElements.get(i);
            String childrenElementTag = childrenElement.getTagName();
            if(childTag.equals(childrenElementTag)) {
                count++;
            }
            if(childElement.equals(childrenElement)) {
                return generateXpath(parentElement, "/" + childTag + "[" + count + "]"+current);
            }
        }
        return null;
    }

    @Test
    public void contrastRatioTest() {
        driver.get(ERROR_PAGE);
        List<WebElement> childElements = driver.findElements(By.xpath("//p"));
        for (WebElement childElement : childElements) {
            String text = getTextNode(childElement);
            if(!text.isEmpty()) {
                String hexColorForeground = Color.fromString(childElement.getCssValue("color")).asHex();
                String hexColorBackground = getParentBackgroundColor(childElement);
                System.out.println(hexColorForeground + " | " + hexColorBackground);
                java.awt.Color foreground = java.awt.Color.decode(hexColorForeground);
                java.awt.Color background = java.awt.Color.decode(hexColorBackground);
                double contrastRatio = ContrastChecker.getContrastRatio(foreground, background);
                System.out.println(contrastRatio + " | " + text);
                Assert.assertTrue(contrastRatio > 4.5);
            }
        }
    }

    public static String getTextNode(WebElement e) {
        String text = e.getText().trim();
        List<WebElement> children = e.findElements(By.xpath("./*"));
        for (WebElement child : children) {
            text = text.replaceFirst(child.getText(), "").trim();
        }
        return text;
    }

    public String getParentBackgroundColor(WebElement element) {
        WebElement current = element;
        String hexColorBackground = Color.fromString(current.getCssValue("background-color")).asHex();
        while(true) {
            WebElement parent = current.findElement(By.xpath(".."));
            if (parent == null) {
                return hexColorBackground;
            } else {
                hexColorBackground = Color.fromString(parent.getCssValue("background-color")).asHex();
                if (hexColorBackground.equals("#000000")) {
                    current = parent;
                } else {
                    return hexColorBackground;
                }
            }
        }
    }

    @Test
    public void contrastRatioTestNegative() {
        driver.get(ERROR_PAGE);
        List<WebElement> childElements = driver.findElements(By.xpath("//div"));
        for (WebElement childElement : childElements) {
            String text = getTextNode(childElement);
            if(!text.isEmpty()) {
                String hexColorForeground = Color.fromString(childElement.getCssValue("color")).asHex();
                String hexColorBackground = getParentBackgroundColor(childElement);
                System.out.println(hexColorForeground + " | " + hexColorBackground);
                java.awt.Color foreground = java.awt.Color.decode(hexColorForeground);
                java.awt.Color background = java.awt.Color.decode(hexColorBackground);
                double contrastRatio = ContrastChecker.getContrastRatio(foreground, background);
                System.out.println(contrastRatio + " | " + text);
                Assert.assertTrue(contrastRatio > 4.5);
            }
        }
    }
}
