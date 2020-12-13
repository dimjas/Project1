package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotePage {

    @FindBy(css = "#nav-notes-tab")
    private WebElement notesTab;

    @FindBy(css = "#noteSubmit")
    private WebElement noteSubmit;

    @FindBy(css = "#noteAdd")
    private WebElement noteAdd;

    @FindBy(css = "#note-title")
    private WebElement noteTitle;

    @FindBy(css = "#note-description")
    private WebElement noteDescription;

    @FindBy(css = "#noteTable")
    private WebElement notesTable;

    private final JavascriptExecutor jsExecutor;
    private final WebDriverWait wait;

    public NotePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        jsExecutor = (JavascriptExecutor) webDriver;
        wait = new WebDriverWait(webDriver, 5);
    }

    public void openNoteTab() {
        wait.until(ExpectedConditions.elementToBeClickable(notesTab));
        notesTab.click();
    }

    public void createNote(String title, String description) {
        wait.until(ExpectedConditions.elementToBeClickable(noteAdd));
        jsExecutor.executeScript("arguments[0].click();", noteAdd);
        wait.until(ExpectedConditions.elementToBeClickable(noteTitle));
        jsExecutor.executeScript("arguments[0].value='" + title + "';", noteTitle);
        jsExecutor.executeScript("arguments[0].value='" + description + "';", noteDescription);
        jsExecutor.executeScript("arguments[0].click();", noteSubmit);
    }

    public void updateNote(int row, String title, String description) {
        wait.until(ExpectedConditions.visibilityOf(notesTable));
        WebElement el = notesTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement editButton = el.findElement(By.tagName("button"));
        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        jsExecutor.executeScript("arguments[0].click();", editButton);
        wait.until(ExpectedConditions.elementToBeClickable(noteTitle));
        jsExecutor.executeScript("arguments[0].value='" + title + "';", noteTitle);
        jsExecutor.executeScript("arguments[0].value='" + description + "';", noteDescription);
        jsExecutor.executeScript("arguments[0].click();", noteSubmit);
    }

    public void deleteNote(int row) {
        wait.until(ExpectedConditions.visibilityOf(notesTable));
        WebElement el = notesTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement deleteLink = el.findElement(By.linkText("Delete"));
        jsExecutor.executeScript("arguments[0].click();", deleteLink);
    }

    public String getNoteText(int row) {
        wait.until(ExpectedConditions.visibilityOf(notesTable));
        WebElement el = notesTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement titleElement = el.findElement(By.tagName("th"));
        return titleElement.getText();
    }

    public String getNoteDescription(int row) {
        wait.until(ExpectedConditions.visibilityOf(notesTable));
        WebElement el = notesTable.findElements(By.tagName("tr")).get(row + 1);
        WebElement descriptionElement = el.findElements(By.tagName("td")).get(1);
        return descriptionElement.getText();
    }

    public boolean hasNotes() {
        wait.until(ExpectedConditions.visibilityOf(notesTable));
        return notesTable.isDisplayed();
    }

    public int getNotesSize() {
        wait.until(ExpectedConditions.visibilityOf(notesTable));
        return notesTable.findElements(By.tagName("tr")).size() - 1;
    }
}
