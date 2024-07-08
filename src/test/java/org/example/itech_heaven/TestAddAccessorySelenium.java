package org.example.itech_heaven;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TestAddAccessorySelenium {

    private static ChromeDriver driver;
    private static WebDriverWait wait;

    @BeforeEach
    public void openBrowser() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();

        driver.get("http://localhost:8080/login");

        WebElement username = driver.findElement(By.name("username"));
        WebElement password = driver.findElement(By.name("password"));

        username.sendKeys("TuanNM");
        password.sendKeys("Minhtuan@2811");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-login")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", loginButton);

        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/dashboard"));
        driver.get("http://localhost:8080/manage-accessory/form-add-accessory");
        Thread.sleep(1000);
    }

    @AfterEach
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testTitle(){
        driver.get("http://localhost:8080/manage-accessory/form-add-accessory");
        String actualTitle = driver.getTitle();
        Assertions.assertEquals("Thêm phụ kiện | Quản trị Admin", actualTitle);
    }

    @ParameterizedTest()
    @CsvFileSource(resources = "/testdata.csv", numLinesToSkip = 1)
    void testAddAccessory(String accessoryName, String price, String color,
                        String description, String quantity, String typeDevice, String mainImageFile, String secondaryImageFile, String status) throws InterruptedException {

        WebElement fileInput = driver.findElement(By.id("mainImageInput"));
        WebElement accessoryNameInput = driver.findElement(By.xpath("//*[@id=\"accessory-name\"]"));
        WebElement priceInput = driver.findElement(By.xpath("//*[@id=\"myForm\"]/div/div[2]/input"));
        WebElement colorInput = driver.findElement(By.xpath("//*[@id=\"myForm\"]/div/div[6]/input"));
        WebElement descriptionInput = driver.findElement(By.xpath("//*[@id=\"myForm\"]/div/div[11]/input"));
        WebElement secondaryImage = driver.findElement(By.xpath("//*[@id=\"imageInput\"]"));
        WebElement quantityInput = driver.findElement(By.xpath("//*[@id=\"myForm\"]/div/div[8]/input"));
        List<WebElement> checkboxes = driver.findElements(By.className("typeDevCheckbox"));
        JavascriptExecutor js = driver;
        accessoryNameInput.sendKeys(accessoryName);
        Thread.sleep(1000);
        priceInput.clear();
        priceInput.sendKeys(price);
        Thread.sleep(1000);

        for (WebElement checkbox : checkboxes) {
            if (checkbox.isSelected()){
                checkbox.click();
            }
        }
        try {
            if (typeDevice.contains("iPhone")) {
                WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxes.get(0)));
                js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
                checkbox.click();
            }
            if (typeDevice.contains("Mac")) {
                WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxes.get(1)));
                js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
                checkbox.click();
            }
            if (typeDevice.contains("Apple Watch")) {
                WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxes.get(2)));
                js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
                checkbox.click();
            }
            if (typeDevice.contains("iPad")) {
                WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxes.get(3)));
                js.executeScript("arguments[0].scrollIntoView(true);", checkbox);
                checkbox.click();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        colorInput.sendKeys(color);
        Thread.sleep(1000);
        quantityInput.sendKeys(quantity);
        Thread.sleep(1000);
        descriptionInput.sendKeys(description);
        Thread.sleep(1000);
        fileInput.sendKeys(mainImageFile);
        Thread.sleep(1000);


        if (!secondaryImageFile.isEmpty()) {
            String[] secondaryImages = secondaryImageFile.split("\n");
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < secondaryImages.length; i++){
                input.append(secondaryImages[i].trim());
                if (i < secondaryImages.length - 1) {
                    input.append("\n");
                }
            }
            secondaryImage.sendKeys(input);

            Thread.sleep(1000);
        }

        WebElement submit = driver.findElement(By.xpath("//*[@id=\"myForm\"]/button"));
        js.executeScript("arguments[0].click();", submit);
        Thread.sleep(1000);
        WebElement success = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]"));
        String successText = success.getText();
        Thread.sleep(1000);
        Assertions.assertEquals(status,successText);

    }
}
