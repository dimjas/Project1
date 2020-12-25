package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {
    @FindBy(css = "#inputFirstName")
    private WebElement firstNameField;

    @FindBy(css = "#inputLastName")
    private WebElement lastNameField;

    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(tagName = "button")
    public WebElement submitButton;

    @FindBy(css = ".alert-danger")
    private WebElement errorMessage;

    private final JavascriptExecutor jsExecutor;

    public SignupPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        jsExecutor = (JavascriptExecutor) webDriver;
    }

    public void signup(String firstName, String lastName, String username, String password) {
        jsExecutor.executeScript("arguments[0].value='" + firstName + "';", firstNameField);
        jsExecutor.executeScript("arguments[0].value='" + lastName + "';", lastNameField);
        jsExecutor.executeScript("arguments[0].value='" + username + "';", usernameField);
        jsExecutor.executeScript("arguments[0].value='" + password + "';", passwordField);
        jsExecutor.executeScript("arguments[0].click();", submitButton);
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }
}
