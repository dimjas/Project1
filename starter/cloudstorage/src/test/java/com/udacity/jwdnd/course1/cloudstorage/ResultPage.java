package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {
    @FindBy(css = "#successMessage")
    private WebElement successMessage;

    @FindBy(css = "#errorMessage")
    private WebElement errorMessage;

    @FindBy(css = "div.alert-success a")
    private WebElement successLink;

    private final JavascriptExecutor jsExecutor;

    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        jsExecutor = (JavascriptExecutor) webDriver;
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public void clickSuccess() {
        jsExecutor.executeScript("arguments[0].click();", successLink);
    }
}
