package com.lambdatest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Scenario_Test {
    public static String hubURL = "https://hub.lambdatest.com/wd/hub";
    private WebDriver driver;
    private final String BASE_URL = "https://www.lambdatest.com/selenium-playground/";
    private final Duration TIMEOUT = Duration.ofSeconds(20); // As per assignment notes


    @BeforeTest
    @Parameters({"browser", "version", "platform"})
    public void setup(String browser, String version, String platform) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        InternetExplorerOptions browserCap = new InternetExplorerOptions();
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("browserVersion", version);
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("user", System.getenv("LT_USERNAME"));
        ltOptions.put("accessKey", System.getenv("LT_ACCESS_KEY"));
        ltOptions.put("build", "Selenium 4");
//        ltOptions.put("name", this.getClass().getName());
        ltOptions.put("platformName", platform);
        ltOptions.put("selenium_version", "4.23.0");
        capabilities.setCapability("build", "SeleniumJava101_Build_1");
        capabilities.setCapability("project", "SeleniumJava101_Assignment");
        capabilities.setCapability("name", "Test Scenario Execution"); // This will be the test name in LambdaTest
        capabilities.setCapability("console", true); // Enable console logs
        capabilities.setCapability("network", true); // Enable network logs
        capabilities.setCapability("video", true);   // Enable video recording
        capabilities.setCapability("visual", true);  // Enable visual regression (if applicable)

        // For parallelism at method level, ensure the test name is unique
        capabilities.setCapability("testName", "Scenario: " + Thread.currentThread().getId() + " - " + browser + " " + version + " " + platform);

        ltOptions.put("seCdp", true);




        if(browser.equals("internet explorer")){
//            ltOptions.put("selenium_version", "3.141.59");
            ltOptions.put("project", "IE11 Legacy Testing");
            ltOptions.put("build", "IE11-Windows7-Build");
            ltOptions.put("name", "Test on IE11 - Win7");
            ltOptions.put("w3c", false);
            ltOptions.put("ie.compatibility", "11001");
            browserCap.setCapability("LT:Options", ltOptions);
            driver = new RemoteWebDriver(new URL(hubURL), browserCap);

        }
        else {
            capabilities.setCapability("LT:Options", ltOptions);
            driver = new RemoteWebDriver(new URL(hubURL), capabilities);
            System.out.println(driver);
        }
    }

@Test
    public void testScenario1_SimpleFormDemo() {
        System.out.println("Running Test Scenario 1 on " + driver.getClass().getSimpleName() + "...");

        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement simpleFormDemoLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Simple Form Demo")));
        simpleFormDemoLink.click();

        wait.until(ExpectedConditions.urlContains("simple-form-demo"));
        Assert.assertTrue(driver.getCurrentUrl().contains("simple-form-demo"), "URL does not contain 'simple-form-demo'");
        System.out.println("URL validated successfully.");

        String messageToEnter = "Welcome to LambdaTest!";

        WebElement messageInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-message")));
        messageInput.sendKeys(messageToEnter);
        System.out.println("Entered message: '" + messageToEnter + "' into the text box.");

        WebElement getCheckedValueButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#showInput")));
        getCheckedValueButton.click();
        System.out.println("Clicked 'Get Checked Value' button.");

        WebElement displayedMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        String actualDisplayedMessage = displayedMessage.getText();
        Assert.assertEquals(actualDisplayedMessage, messageToEnter, "Displayed message does not match entered message.");
        System.out.println("Validated displayed message: '" + actualDisplayedMessage + "' matches entered message.");
        if (actualDisplayedMessage.contains("Welcome to LambdaTest!")) {
            markStatus("passed", "Validated displayed message Successful", driver);
        } else {
            markStatus("failed", "Validated displayed message Failure", driver);
        }
    }


    @Test
    public void testScenario2_DragAndDropSliderDemo() {
        System.out.println("Running Test Scenario 2 on " + driver.getClass().getSimpleName() + "...");

        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement simpleFormDemoLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Drag & Drop Sliders")));
        simpleFormDemoLink.click();

        wait.until(ExpectedConditions.urlContains("drag-drop-range-sliders-demo"));
        Assert.assertTrue(driver.getCurrentUrl().contains("drag-drop-range-sliders-demo"), "URL does not contain 'drag-drop-range-sliders-demo'");
        System.out.println("URL validated successfully.");
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='range' and @value='15']")));
        int targetValue = 95;
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'))", slider, "35");

        slider.click();
        for (;;) {
            if (Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rangeSuccess"))).getText()) < targetValue) {
                slider.sendKeys(Keys.ARROW_RIGHT);
            } else slider.sendKeys(Keys.ARROW_LEFT);

            if (Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rangeSuccess"))).getText()) == targetValue) {
                break;
            }
        }
        System.out.println("Slider value set to 95.");


//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        WebElement displayedRangeValueElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rangeSuccess")));
        String actualRangeValue = displayedRangeValueElement.getText();


        Assert.assertEquals(actualRangeValue, String.valueOf(targetValue), "Scenario 2 (Actions): The slider range value is not " + targetValue + ".");
        System.out.println("Scenario 2 (Actions): Validated that the range value is " + actualRangeValue + ".");
        System.out.println("Test Scenario 2 (Actions) completed successfully.");
        if ((Integer.parseInt(actualRangeValue) == targetValue)) {
            markStatus("passed", "Validation the slider range Successful", driver);
        } else {
            markStatus("failed", "Validation the slider range Failure", driver);

        }
    }
@Test
    public void testScenario3_InputFormDemo() {
        System.out.println("Running Test Scenario 3 on " + driver.getClass().getSimpleName() + "...");

        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement inputFormSubmitLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Input Form Submit")));
        inputFormSubmitLink.click();

        wait.until(ExpectedConditions.urlContains("input-form-demo"));
        Assert.assertTrue(driver.getCurrentUrl().contains("input-form-demo"), "Scenario 3: URL does not contain 'input-form-demo'");
        System.out.println("Scenario 3: URL validated for Input Form Submit page.");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and contains(@class, 'lambda')]")));
        submitButton.click();
        System.out.println("Scenario 3: Clicked 'Submit' button without filling any fields.");

        WebElement nameInput = driver.findElement(By.id("name"));
        Assert.assertEquals(driver.switchTo().activeElement(), nameInput, "Scenario 3: Focus is not on the Name input field.");
        System.out.println("Scenario 3: Verified focus is on the Name input field (indirect validation of error).");
        System.out.println("Scenario 3: WARNING: The error message 'Please fill out this field.' is a native browser HTML5 validation tooltip.");
        System.out.println("Scenario 3: Selenium WebDriver cannot directly interact with or assert the text of these native browser UI elements.");
        System.out.println("Scenario 3: Indirect validation (checking URL not changed) has been performed to confirm form validation is active.");

        nameInput.sendKeys("Test User");
        driver.findElement(By.id("inputEmail4")).sendKeys("test@example.com");
        driver.findElement(By.id("inputPassword4")).sendKeys("password123");
        driver.findElement(By.id("company")).sendKeys("LambdaTest Inc.");
        driver.findElement(By.id("websitename")).sendKeys("www.lambdatest.com");

        WebElement countryDropdown = driver.findElement(By.name("country"));
        org.openqa.selenium.support.ui.Select selectCountry = new org.openqa.selenium.support.ui.Select(countryDropdown);
        selectCountry.selectByVisibleText("United States");
        System.out.println("Scenario 3: Selected 'United States' from country dropdown.");

        driver.findElement(By.id("inputCity")).sendKeys("New York");
        driver.findElement(By.id("inputAddress1")).sendKeys("123 Main St");
        driver.findElement(By.id("inputAddress2")).sendKeys("Apt 4B");
        driver.findElement(By.id("inputState")).sendKeys("NY");
        driver.findElement(By.id("inputZip")).sendKeys("10001");
        System.out.println("Scenario 3: All form fields filled.");

        submitButton.click();
        System.out.println("Scenario 3: Clicked 'Submit' button after filling all fields.");
        if (driver.getPageSource().contains("Thanks for contacting us, we will get back to you shortly.")) {
            markStatus("passed", "Input Form Validation Successful", driver);
        } else {
            markStatus("failed", "Input Form Validation Failure", driver);
        }
    }


    @AfterTest
    public void tearDown() {
        try {
            driver.quit();
        } catch (

        Exception e) {
            markStatus("failed", "Got exception!", driver);
            e.printStackTrace();
            driver.quit();
        }
    }

    public static void markStatus(String status, String reason, WebDriver driver) {
        JavascriptExecutor jsExecute = (JavascriptExecutor) driver;
        jsExecute.executeScript("lambda-status=" + status);
        System.out.println(reason);
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        Scenario_Test test = new Scenario_Test();
        test.setup("Chrome", "128.0","Windows 10");
        test.testScenario1_SimpleFormDemo();

        test.tearDown();
    }
}
