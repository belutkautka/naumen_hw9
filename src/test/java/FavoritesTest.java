import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FavoritesTest {
    private static WebDriver driver;

    private static String username = "belutka";
    private static String password = "123";
    private static String link = "https://test-m.sd.nau.run/sd/";
    private static String cardText = "Тест1234567";
    private static Integer timeout = 2000;

    public void login() {
        type("username", username);
        type("password", password);
        clickWithWait("submit-button");
    }

    public void logout() {
        clickWithWait("gwt-debug-logout");
    }

    @BeforeEach
    void beforeEach() {
        driver.get(link);
        login();
    }

    @AfterEach
    void afterEach() {
        logout();
    }

    @AfterAll
    static void afterAll() {
        driver.close();
    }

    public FavoritesTest() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1280, 720));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Test
    public void addCardToFavorites() throws InterruptedException {
        wait("gwt-debug-logout");
        driver.get(link + "operator/#uuid:employee$46901!%7B%22tab%22:%22f2e9ff44-1336-00dd-0000-0000004904bd%22%7D");

        clickWithWait("favorite");
        type("gwt-debug-itemTitle-value", cardText);
        clickWithWait("gwt-debug-apply");

        Thread.sleep(timeout);
        clickWithWait("gwt-debug-12c338ac-168c-348b-a88c-b9594aed4be9");

        var element = driver.findElement(By.linkText(cardText));
        var actual = element.getText();
        var expected = cardText;

        Assertions.assertEquals(
                actual,
                expected,
                "Название объекта не совпало c ожидаемым");

    }

    @Test
    public void removeCardFromFavorites() throws InterruptedException {

        clickWithWait("gwt-debug-12c338ac-168c-348b-a88c-b9594aed4be9");

        clickBy(By.linkText(cardText));
        clickBy(By.xpath("//div[@id='gwt-debug-NTreeItemContent.uuid:employee$46901!%7B%22tab%22:%22f2e9ff44-1336-00dd-0000-0000004904bd%22%7D']/div/span"));
        clickWithWait("gwt-debug-YES");

        Thread.sleep(timeout);
        clickWithWait("gwt-debug-12c338ac-168c-348b-a88c-b9594aed4be9");
        var actual = driver.findElements(By.linkText(cardText)).isEmpty();
        Assertions.assertTrue(
                actual,
                "Карточка не была удалена");
    }

    private WebElement wait(String id) {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        var element = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        return element;
    }

    private void clickWithWait(String id) {
        wait(id).click();
    }

    private void clickBy(By by) {
        var wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        var element = wait.until(ExpectedConditions.elementToBeClickable(by));
        element.click();
    }

    private void type(String id, String text) {
        var element = wait(id);
        element.clear();
        element.sendKeys(text);
    }

}
