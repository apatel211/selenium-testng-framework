package pages;

import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LoggerUtil;
import utils.ValidationUtils;
import utils.WaitUtils;

import java.util.Set;

public class AppPage extends  BasePage{


    private WebDriver driver;
    private WaitUtils waitUtils;

    private ValidationUtils uiWait;
    private int timeout;

    // Selectors defined in Page Object
    private By dashboardText = By.xpath("//*[contains(text(),'Dashboard')]");

    private By appleAppStoreLink = By.cssSelector("a[href*='apps.apple.com']");
    private By googlePlayStoreLink = By.cssSelector("a[href*='play.google.com']");

    public AppPage(WebDriver driver, int timeout) {
        super(driver, timeout);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.timeout = timeout;
    }

    @Override
    public boolean isLoaded() {
        LoggerUtil.info(this.getClass(), "Navigate towards app store link");
        return waitUtils.isElementVisible(dashboardText, timeout);
    }



    public String getDashboardTextAndScroll() {
        waitUtils.waitForVisible(dashboardText, timeout);
        LoggerUtil.info(this.getClass(), "Bottom of dashboard");

        waitUtils.isElementVisible(dashboardText, timeout);
        driver.findElement(dashboardText).click();

        ValidationUtils.scrollToBottom(driver);
        // Return the visible text
        return driver.findElement(dashboardText).getText().trim();
    }

    // Validate Apple App Store link
    public void validateAppleAppStore(JsonNode node) {
        LoggerUtil.info(this.getClass(), "Validate apple app store");
        ValidationUtils.validateLink(driver, appleAppStoreLink, node, timeout);
    }

    // Validate Google Play Store link
    public void validateGooglePlayStore(JsonNode node) {
        LoggerUtil.info(this.getClass(), "Validate google play store");
        ValidationUtils.validateLink(driver, googlePlayStoreLink, node, timeout);
    }


}
