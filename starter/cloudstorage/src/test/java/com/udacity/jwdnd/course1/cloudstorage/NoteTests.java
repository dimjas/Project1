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
public class NoteTests {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseURL;
    private WebDriverWait wait;
    ExpectedCondition<Boolean> pageLoadCondition = driver -> {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        try {
            return jsExecutor.executeScript("return document.readyState").equals("complete") && (Integer) jsExecutor.executeScript("return jQuery.active") == 0;
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
    public void shouldDisplaySuccessAddMessageAndRedirectToNotesTableWhenNoteCreated() {
        driver.get(baseURL + "/home");
        wait.until(pageLoadCondition);
        NotePage notePage = new NotePage(driver);
        ResultPage resultPage = new ResultPage(driver);
        notePage.openNoteTab();
        notePage.createNote("Title", "Description");
        wait.until(pageLoadCondition);
        assertEquals("Result", driver.getTitle());
        assertEquals("Your note was successfully added.", resultPage.getSuccessMessage());
        resultPage.clickSuccess();
        assertEquals(baseURL + "/home?activeTab=notes", driver.getCurrentUrl());
        wait.until(pageLoadCondition);
        assertTrue(notePage.hasNotes());
        assertEquals(1, notePage.getNotesSize());
        assertEquals("Title", notePage.getNoteText(0));
        assertEquals("Description", notePage.getNoteDescription(0));
    }

    @Test
    @Order(2)
    public void shouldDisplaySuccessUpdateMessageAndRedirectToNotesTableWhenNoteUpdated() {
        driver.get(baseURL + "/home");
        wait.until(pageLoadCondition);
        NotePage notePage = new NotePage(driver);
        ResultPage resultPage = new ResultPage(driver);
        notePage.openNoteTab();
        notePage.updateNote(0, "Title Update", "Description Update");
        wait.until(pageLoadCondition);

        assertEquals("Result", driver.getTitle());
        assertEquals("Your note was successfully updated.", resultPage.getSuccessMessage());

        resultPage.clickSuccess();
        assertEquals(baseURL + "/home?activeTab=notes", driver.getCurrentUrl());
        wait.until(pageLoadCondition);

        assertEquals("Title Update", notePage.getNoteText(0));
        assertEquals("Description Update", notePage.getNoteDescription(0));
    }

    @Test
    @Order(3)
    public void shouldDisplaySuccessDeleteMessageAndRedirectToNotesTableWhenNoteDeleted() {
        driver.get(baseURL + "/home");
        wait.until(pageLoadCondition);
        NotePage notePage = new NotePage(driver);
        ResultPage resultPage = new ResultPage(driver);
        notePage.openNoteTab();
        assertEquals(1, notePage.getNotesSize());
        notePage.deleteNote(0);
        wait.until(pageLoadCondition);

        assertEquals("Result", driver.getTitle());
        assertEquals("Your note was successfully deleted.", resultPage.getSuccessMessage());

        resultPage.clickSuccess();
        assertEquals(baseURL + "/home?activeTab=notes", driver.getCurrentUrl());
    }

    private void login() {
        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("FirstName", "LastName", USERNAME, PASSWORD);

        if (driver.getCurrentUrl().endsWith("/signup")) {
            driver.get(baseURL + "/login");
        }
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);
    }
}
