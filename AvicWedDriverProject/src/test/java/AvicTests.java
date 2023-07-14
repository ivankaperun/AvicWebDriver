import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;


import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class AvicTests {
    private WebDriver driver;

    @BeforeTest
    public void profileSetUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/ivankalozynska-perun/IdeaProjects/AvicWedDriverProject/src/main/resources/chromedriver");
    }

    @BeforeMethod
    public void testSetUp() {
        // create our new driver
        driver = new ChromeDriver();
        // open browser on full screen
        driver.manage().window().maximize();
        // open main page
        driver.get("https://avic.ua/");
    }

    @Test(priority = 1)
    public void checkThatUrlContainsSearchWord() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone 13 Pro Max");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        assertTrue(driver.getCurrentUrl().contains("query=iPhone+13+Pro+Max"));
    }


    @Test(priority = 2)
    public void checkSearchFilterAndAddToCartFunctionality() {
        driver.findElement(By.xpath("//span[@class='sidebar-item']")).click();//каталог товаров
        driver.findElement(By.xpath("//ul[contains(@class,'sidebar-list')]//a[contains(@href, 'apple-store')]")).click();//Go to Apple Store
        driver.findElement(By.xpath("//div[@class='brand-box__title']/a[contains(@href,'macbook')]")).click();// macbook
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));//wait for page loading
        driver.findElement(By.xpath("//label[@for='fltr-seriya-air']/a[contains(text(), 'MacBook Air')]")).click(); // select filter Macbook Air
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.xpath("//a[@class='prod-cart__buy'][contains(@data-ecomm-cart,'Apple MacBook Air 13 Gold Late 2020 (MGND3)')]")).click(); //add macbook to cart
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
        driver.findElement(By.xpath("//button[@class='fancybox-button fancybox-close-small']")).click();// Close modal window
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'header-bottom__cart')]//div[@class='active-cart-item js_cart_count']")));
        String productsCountInCart =
                driver.findElement(By.xpath("//div[contains(@class, 'header-bottom__cart')]/div[@class='active-cart-item js_cart_count']")).getText(); //Get count of products from cart
        assertEquals(productsCountInCart, "1");
    }


    @Test(priority = 3)
    public void checkSortFunctionality() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("MacBook Air");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.xpath("//div[@class='two-column-wrapper ']//div[@class='sort-holder']/span[starts-with(@class, 'select2')]//span[starts-with(@id,'select2-sort-')]")).click();
        driver.findElement(By.xpath("//span[@class='select2-results']/ul/li[contains(text(), 'Від дорогих до дешевих')]")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        assertTrue(driver.getCurrentUrl().contains("sort--pricedesc?query=MacBook+Air"));
    }

    @Test(priority = 4)
    public void checkAbilityToOpenAppleBrandPageByClickOnAppleIcon() {
        driver.findElement(By.xpath("//a[@href='/ua/brand-apple']//img[contains(@alt, 'Apple')]")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='page-title']")));
        assertTrue(driver.getCurrentUrl().contains("brand-apple"));
    }

    @AfterMethod
    public void tearDown() {
        //Close our opened recently browser
        driver.quit();
    }
}
