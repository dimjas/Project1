package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(tagName = "button")
    private WebElement submitButton;

    @FindBy(css = ".alert-danger")
    private WebElement errorMessage;

    @FindBy(css = ".alert-dark")
    private WebElement logoutMessage;

    private final JavascriptExecutor jsExecutor;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        jsExecutor = (JavascriptExecutor) webDriver;
    }

    public void login(String username, String password) {
        jsExecutor.executeScript("arguments[0].value='" + username + "';", usernameField);
        jsExecutor.executeScript("arguments[0].value='" + password + "';", passwordField);
        jsExecutor.executeScript("arguments[0].click();", submitButton);
    }

    public boolean isErrorDisplayed() {
        return errorMessage.isDisplayed();
    }

    public String getLogoutMessage() {
        return logoutMessage.getText();
    }

}
