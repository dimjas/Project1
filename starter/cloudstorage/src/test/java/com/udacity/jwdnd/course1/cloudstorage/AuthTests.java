package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTests {

    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseURL;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        baseURL = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void shouldRedirectToLoginPageWhenUserIsNotAuthorized() {
        driver.get(baseURL + "/home");
        assertEquals("Login", driver.getTitle());
        driver.get(baseURL + "/result");
        assertEquals("Login", driver.getTitle());
        driver.get(baseURL + "/files");
        assertEquals("Login", driver.getTitle());
        driver.get(baseURL + "/notes");
        assertEquals("Login", driver.getTitle());
        driver.get(baseURL + "/credentials");
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void shouldDisplayErrorWhenUsernameExists() {
        signIn();
        signIn();
        assertEquals("The username already exists.", new SignupPage(driver).getErrorMessage());
    }

    @Test
    public void shouldRedirectToHomePageWhenCredentialsAreValid() {
        signIn();
        login();
        assertEquals(baseURL + "/home", driver.getCurrentUrl());
    }

    @Test
    public void shouldDisplayErrorWhenCredentialsAreInvalid() {
        signIn();
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, "wrong password");
        assertTrue(loginPage.errorMessage.isDisplayed());
    }

    @Test
    public void shouldRedirectToLoginPageAfterLogout() {
        signIn();
        login();

        assertEquals("Home", driver.getTitle());
        driver.get(baseURL + "/logout");
        assertEquals("You have been logged out", new LoginPage(driver).logoutMessage.getText());
        driver.get(baseURL + "/home");
        assertEquals("Login",driver.getTitle());
    }

    private void signIn() {
        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("FirstName", "LastName", USERNAME, PASSWORD);
    }

    private void login() {
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);
    }
}
