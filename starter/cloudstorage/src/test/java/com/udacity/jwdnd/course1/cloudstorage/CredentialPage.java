package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialPage {

    @FindBy(css = "#nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(css = "#credentialSubmit")
    private WebElement credentialSubmit;

    @FindBy(css = "#credentialAdd")
    private WebElement credentialAdd;

    @FindBy(css = "#credential-url")
    private WebElement credentialUrl;

    @FindBy(css = "#credential-username")
    private WebElement credentialUsername;

    @FindBy(css = "#credential-password")
    private WebElement credentialPassword;

    @FindBy(css = "#credentialTable")
    private WebElement credentialsTable;

    private final JavascriptExecutor jsExecutor;
    private final WebDriverWait wait;


    public CredentialPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        jsExecutor = (JavascriptExecutor) webDriver;
        wait = new WebDriverWait(webDriver, 5);
    }

    public void openCredentialTab() {
        wait.until(ExpectedConditions.elementToBeClickable(credentialsTab));
        credentialsTab.click();
    }

    public void createCredential(String url, String username, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(credentialAdd));
        jsExecutor.executeScript("arguments[0].click();", credentialAdd);
        wait.until(ExpectedConditions.elementToBeClickable(credentialUrl));
        jsExecutor.executeScript("arguments[0].value='" + url + "';", credentialUrl);
        jsExecutor.executeScript("arguments[0].value='" + username + "';", credentialUsername);
        jsExecutor.executeScript("arguments[0].value='" + password + "';", credentialPassword);
        jsExecutor.executeScript("arguments[0].click();", credentialSubmit);
    }

    public void updateCredential(int row, String url, String username, String password) {
        wait.until(ExpectedConditions.visibilityOf(credentialsTable));
        WebElement el = credentialsTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement editButton = el.findElement(By.tagName("button"));
        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        jsExecutor.executeScript("arguments[0].click();", editButton);
        wait.until(ExpectedConditions.elementToBeClickable(credentialUrl));
        jsExecutor.executeScript("arguments[0].value='" + url + "';", credentialUrl);
        jsExecutor.executeScript("arguments[0].value='" + username + "';", credentialUsername);
        jsExecutor.executeScript("arguments[0].value='" + password + "';", credentialPassword);
        jsExecutor.executeScript("arguments[0].click();", credentialSubmit);
    }

    public void deleteCredential(int row) {
        WebElement el = credentialsTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement deleteLink = el.findElement(By.linkText("Delete"));
        jsExecutor.executeScript("arguments[0].click();", deleteLink);
    }

    public String getCredentialUrl(int row) {
        wait.until(ExpectedConditions.visibilityOf(credentialsTable));
        WebElement el = credentialsTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement urlElement = el.findElements(By.tagName("th")).get(0);
        return urlElement.getText();
    }

    public String getCredentialUsername(int row) {
        wait.until(ExpectedConditions.visibilityOf(credentialsTable));
        WebElement el = credentialsTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement usernameElement = el.findElements(By.tagName("td")).get(1);
        return usernameElement.getText();
    }

    public String getCredentialPassword(int row) {
        wait.until(ExpectedConditions.visibilityOf(credentialsTable));
        WebElement el = credentialsTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement passwordElement = el.findElements(By.tagName("td")).get(2);
        return passwordElement.getText();
    }

    public boolean hasCredentials() {
        wait.until(ExpectedConditions.visibilityOf(credentialsTable));
        return credentialsTable.isDisplayed();
    }

    public int getCredentialsSize() {
        wait.until(ExpectedConditions.visibilityOf(credentialsTable));
        return credentialsTable.findElements(By.tagName("tr")).size() - 1;
    }
}
