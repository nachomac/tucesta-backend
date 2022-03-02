package es.tucesta.supers;

import java.util.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;

public class Carrefour implements Supermarket {
    WebDriver driver;
    String usr;
    String pwd;
    public Carrefour(WebDriver driver,String usr,String pwd) {
        this.driver=driver;
        this.usr=usr;
        this.pwd=pwd;
    }
    public void login(){
        try {
            driver.get("https://www.carrefour.es/access?redirect=https%3A%2F%2Fwww.carrefour.es%2Fmyaccount%2F%23%2Farea-privada%2Fdashboard&back=https%3A%2F%2Fwww.carrefour.es%2F%3Fic_source%3Dfood&source=login_nonFood");
            {
                WebDriverWait wait = new WebDriverWait(driver, 30);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gigya-loginID-133272631659353340")));
            }
            Thread.sleep(500);
            driver.findElement(By.cssSelector(".btn-cookies")).click();
            Thread.sleep(500);
            driver.findElement(By.id("gigya-loginID-133272631659353340")).sendKeys(usr);
            driver.findElement(By.id("gigya-loginID-133272631659353340")).sendKeys(Keys.ENTER);
            {
                WebDriverWait wait = new WebDriverWait(driver, 30);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gigya-password-66067780736329300")));
            }
            driver.findElement(By.id("gigya-password-66067780736329300")).sendKeys(pwd);
            driver.findElement(By.id("gigya-password-66067780736329300")).sendKeys(Keys.ENTER);
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void emptyBasket() {
        System.out.println("___________empty basket_____________");

        try {
            try {
                Thread.sleep(1000);
                System.out.println("1_");
                driver.get("https://www.carrefour.es/supermercado/?DPSLogout=true&_requestid=10271");
                Thread.sleep(1000);
                System.out.println("2_________________");
                Thread.sleep(1700);
                try {
                    addIngredient("https://www.carrefour.es/supermercado/zumo-de-naranja-carrefour-brik-1-l-carrefour/R-745415924/p",1,"");
                } catch (Exception e) {}

                System.out.println("**33____");
                Thread.sleep(1000);
                try {
                    driver.findElement(By.cssSelector("input.sale-point__postal-code-input")).click();
                    driver.findElement(By.cssSelector("input.sale-point__postal-code-input")).sendKeys("28003");
                    Thread.sleep(700);
                    driver.findElement(By.cssSelector("input.sale-point__postal-code-input")).sendKeys(Keys.ENTER);
                    Thread.sleep(1000);
                    driver.findElement(By.cssSelector("button.options-buttons__accept options-buttons__accept--disabled")).click();
                    Thread.sleep(1000);
                    try {
                        {
                            WebDriverWait wait = new WebDriverWait(driver, 30);
                            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".options-buttons__accept")));
                        }
                        driver.findElement(By.cssSelector(".options-buttons__accept")).click();
                    } catch (Exception e) {
                        System.out.println("________");
                        e.printStackTrace();
                        Thread.sleep(500);
                        driver.findElement(By.cssSelector("span > button")).click();
                        Thread.sleep(500);
                        driver.findElement(By.cssSelector(".sale-point__postal-code-input")).click();
                        Thread.sleep(500);
                        driver.findElement(By.cssSelector(".sale-point__postal-code-input")).sendKeys("28003");
                        Thread.sleep(500);
                        driver.findElement(By.cssSelector(".options-buttons__accept")).click();
                        Thread.sleep(500);
                        driver.findElement(By.cssSelector(".options-buttons__accept")).click();
                        Thread.sleep(500);
                    }
                    Thread.sleep(700000);

                } catch (Exception e) {}
                System.out.println("2");
                Thread.sleep(10000);
            } catch (Exception e) {e.printStackTrace();}
            Thread.sleep(1000);
            System.out.println("4");
            driver.get("https://www.carrefour.es/supermercado/MiCarrito");
            System.out.println("_5");
            (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".icon-cart:nth-child(1) > .total-price")));
            System.out.println("6");
            driver.findElement(By.cssSelector(".icon-cart:nth-child(1) > .total-price")).click();
            System.out.println(".7");
            (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Vaciar carrito")));
            driver.findElement(By.linkText("Vaciar carrito")).click();
            Thread.sleep(700);
            driver.findElement(By.cssSelector(".btn-submit")).click();
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean addIngredient(String url,int iAddTimes,String btnName){

        driver.get(url);
        try {
            try{Thread.sleep(1000);} catch (Exception ee) {}
            List<WebElement> listOfElements = driver.findElements(By.cssSelector(".add-to-cart-button__full-button"));
            boolean isPresent = listOfElements.size()>0;
            if (isPresent) {
                System.out.println("__item found");
                driver.findElement(By.cssSelector(".add-to-cart-button__full-button")).click();
                for (int i=1;i<iAddTimes;i++) {
                    Thread.sleep(700);
                    driver.findElement(By.cssSelector(".add-to-cart-button__more-unit-button")).click();
                }
            } else {
                System.out.println("____item not found");
                return false;
            }
        } catch (Exception e){
            System.out.println("errorcino:"+e.toString());
            try{Thread.sleep(1000);} catch (Exception ee) {}
            return false;
        }
        try {Thread.sleep(700);} catch (Exception e) {}
        return true;
    }
}
