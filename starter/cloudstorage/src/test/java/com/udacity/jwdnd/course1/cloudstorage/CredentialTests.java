package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseURL;
    private WebDriverWait wait;
    ExpectedCondition<Boolean> pageLoadCondition = driver -> {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        try {
            return jsExecutor.executeScript("return document.readyState").equals("complete") &&
                    (Integer) jsExecutor.executeScript("return jQuery.active") == 0;
        } catch (Exception e) {
            return true;
        }
    };

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 30);
        baseURL = "http://localhost:" + port;
        login();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void shouldDisplaySuccessAddMessageAndRedirectToCredentialTabWhenCredentialCreated() {
        String url = "url";
        String username = "admin";
        String password = "admin";
        driver.get(baseURL + "/home");
        wait.until(pageLoadCondition);
        CredentialPage credentialPage = new CredentialPage(driver);
        ResultPage resultPage = new ResultPage(driver);
        credentialPage.openCredentialTab();
        credentialPage.createCredential(url, username, password);
        wait.until(pageLoadCondition);
        assertEquals("Result", driver.getTitle());
        assertEquals("Your credential was successfully added.", resultPage.getSuccessMessage());
        resultPage.clickSuccess();
        assertEquals(baseURL + "/home?activeTab=credentials", driver.getCurrentUrl());
        wait.until(pageLoadCondition);
        assertTrue(credentialPage.hasCredentials());
        assertEquals(1,credentialPage.getCredentialsSize());
        assertEquals(url, credentialPage.getCredentialUrl(0));
        assertEquals(username, credentialPage.getCredentialUsername(0));
        assertNotEquals(password, credentialPage.getCredentialPassword(0));
    }

    @Test
    @Order(2)
    public void shouldDisplaySuccessUpdateMessageAndRedirectToCredentialsTableWhenCredentialUpdated() {
        String url = "url Update";
        String username = "admin Update";
        String password = "admin 123";

        driver.get(baseURL + "/home");
        wait.until(pageLoadCondition);
        CredentialPage credentialPage = new CredentialPage(driver);
        ResultPage resultPage = new ResultPage(driver);
        credentialPage.openCredentialTab();
        credentialPage.updateCredential( 0, url, username , password);
        wait.until(pageLoadCondition);

        assertEquals("Result", driver.getTitle());
        assertEquals("Your credential was successfully updated.", resultPage.getSuccessMessage());

        resultPage.clickSuccess();
        assertEquals(baseURL + "/home?activeTab=credentials", driver.getCurrentUrl());
        wait.until(pageLoadCondition);

        assertEquals(url, credentialPage.getCredentialUrl(0));
        assertEquals(username, credentialPage.getCredentialUsername(0));
    }

    @Test
    @Order(3)
    public void shouldDisplaySuccessDeleteMessageAndRedirectToCredentialsTableWhenCredentialDeleted() {
        driver.get(baseURL + "/home");
        wait.until(pageLoadCondition);
        CredentialPage credentialPage = new CredentialPage(driver);
        ResultPage resultPage = new ResultPage(driver);
        credentialPage.openCredentialTab();
        assertEquals(1, credentialPage.getCredentialsSize());
        credentialPage.deleteCredential( 0);
        wait.until(pageLoadCondition);

        assertEquals("Result", driver.getTitle());
        assertEquals("Your credential was successfully deleted.", resultPage.getSuccessMessage());
        resultPage.clickSuccess();
        assertEquals(baseURL + "/home?activeTab=credentials", driver.getCurrentUrl());
    }

    private void login() {
        String username = "username";
        String password = "password";

        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("FirstName", "LastName", username, password);

        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }
}
