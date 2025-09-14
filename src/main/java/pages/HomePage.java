package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.LoggerUtil;
import utils.ValidationUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HomePage extends BasePage {

    private WaitUtils waitUtils;
    private final By navLinks = By.cssSelector("header a[href]");
    private By activeBanner = By.cssSelector("div.slick-slide.slick-current:not(.slick-cloned), div.slick-slide.slick-active:not(.slick-cloned)");

    private By carousel = By.cssSelector("div.slick-slider.slick-initialized");
    private By dotsItem = By.cssSelector("div.slick-slider.slick-initialized ul[class*='dots'] li");
    private By images = By.cssSelector("img[alt]");

    public HomePage(WebDriver driver, int timeout) {
        super(driver, timeout);
        this.waitUtils = new WaitUtils(driver); // Initialize wait utility
        LoggerUtil.info(this.getClass(), "HomePage initialized");
    }

    @Override
    public boolean isLoaded() {
        LoggerUtil.info(this.getClass(), "Waiting for dashboard heading to be visible");
        return true;
    }

    public List<String> getNavTexts() {
        LoggerUtil.info(this.getClass(), "Fetching navigation link texts...");
        List<String> texts = driver.findElements(navLinks).stream()
                .map(el -> el.getText().trim())
                .filter(txt -> !txt.isEmpty())
                .collect(Collectors.toList());
        LoggerUtil.info(this.getClass(), "Navigation texts found: " + texts);
        return texts;
    }

    public List<String> getNavHrefs() {
        LoggerUtil.info(this.getClass(), "Fetching navigation link hrefs...");
        List<String> hrefs = driver.findElements(navLinks).stream()
                .map(el -> el.getAttribute("href"))
                .collect(Collectors.toList());
        LoggerUtil.info(this.getClass(), "Navigation hrefs found: " + hrefs);
        return hrefs;
    }


    public List<String> getAllBannerContents() {
        ValidationUtils.scrollToBottom(driver);
        WebElement carousel = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.slick-slider.slick-initialized")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", carousel);

        wait.until(ExpectedConditions.visibilityOfElementLocated(activeBanner));

        Set<String> collected = new LinkedHashSet<>();
        List<WebElement> dots = driver.findElements(dotsItem);
        System.out.println("Total dots found: " + dots.size());

        for (int i = 0; i < dots.size(); i++) {
            WebElement dot = dots.get(i);

            // click via JS to avoid overlay issues
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dot);

            // 🔹 Wait for this dot to become active (robust instead of previousContent text check)
            int index = i; // capture loop index
            wait.until(driver -> {
                List<WebElement> freshDots = driver.findElements(dotsItem);
                return freshDots.get(index).getAttribute("class").contains("slick-active");
            });

            // 🔹 Now fetch the active banner
            WebElement slide = wait.until(ExpectedConditions.visibilityOfElementLocated(activeBanner));

            // Build content
            String content = slide.getText().trim();
            for (WebElement img : slide.findElements(images)) {
                String alt = img.getAttribute("alt");
                if (alt != null && !alt.isBlank() && !"Banner image".equalsIgnoreCase(alt)) {
                    content += " | " + alt.trim();
                }
            }
            String aria = slide.getAttribute("aria-label");
            if (aria != null && !aria.isBlank()) {
                content += " | " + aria.trim();
            }

            collected.add(content);
            System.out.println("Banner " + (i + 1) + ": " + content);
        }


        return new ArrayList<>(collected);
    }

    public List<String> getAllBannerContentsNew() {
        ValidationUtils.scrollToBottom(driver);

        WebElement carousel = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.slick-slider.slick-initialized")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", carousel);

        wait.until(ExpectedConditions.visibilityOfElementLocated(activeBanner));

        Set<String> collected = new LinkedHashSet<>();
        List<WebElement> dots = driver.findElements(dotsItem);
        System.out.println("Total dots found: " + dots.size());

        String previousContent = "";

        for (int i = 0; i < dots.size(); i++) {
            WebElement dot = dots.get(i);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dot);

            // 🔹 Wait until banner content is different from previousContent
            String finalPreviousContent = previousContent;
            WebElement slide = wait.until(driver -> {
                WebElement s = driver.findElement(activeBanner);
                String txt = s.getText().trim();

                for (WebElement img : s.findElements(images)) {
                    String alt = img.getAttribute("alt");
                    if (alt != null && !alt.isBlank()) {
                        txt += " | " + alt.trim();
                    }
                }
                String aria = s.getAttribute("aria-label");
                if (aria != null && !aria.isBlank()) {
                    txt += " | " + aria.trim();
                }

                return (!txt.isEmpty() && !txt.equals(finalPreviousContent)) ? s : null;
            });

            // 🔹 Extract fresh content after transition
            String content = slide.getText().trim();
            for (WebElement img : slide.findElements(images)) {
                String alt = img.getAttribute("alt");
                if (alt != null && !alt.isBlank()) {
                    content += " | " + alt.trim();
                }
            }
            String aria = slide.getAttribute("aria-label");
            if (aria != null && !aria.isBlank()) {
                content += " | " + aria.trim();
            }

            collected.add(content);
            previousContent = content;

            System.out.println("Banner " + (i + 1) + ": " + content);
        }

        return new ArrayList<>(collected);
    }



}
