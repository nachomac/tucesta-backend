package es.tucesta.supers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;

public class Dia implements Supermarket {
    WebDriver driver;
    String usr;
    String pwd;
    public Dia(WebDriver driver,String usr,String pwd) {
        this.driver=driver;
        this.usr=usr;
        this.pwd=pwd;
    }
    public void login(){
        driver.get("https://www.dia.es/compra-online/login/singlelogin");
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("onetrust-accept-btn-handler")));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        driver.findElement(By.id("userName")).click();
        driver.findElement(By.id("userName")).sendKeys(usr);
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        }
        driver.findElement(By.id("password")).sendKeys(pwd);
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            try{
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));
            } catch (Exception e) {}
        }
    }
    public void emptyBasket() {
        //vaciar la cesta
        try {
            driver.get("https://www.dia.es/compra-online/productos/lacteos-y-huevos/leche/semidesnatada/p/173903");
            //"https://www.dia.es/compra-online/productos/frescos/verdura-y-hortalizas/ajo-cebolla-y-puerro/p/20851"
            Thread.sleep(2000);
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();",
                    driver.findElement(By.cssSelector(".ico-tablet-cart")));
            Thread.sleep(2000);
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();",
                    driver.findElement(By.id("doEmptyCartButton")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean addIngredient(String url,int iAddTimes,String btnName){
        driver.get(url);
        try {
            (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id(btnName)));
            try {
                driver.findElement(By.id(btnName)).click();
            } catch (Exception e) {
                System.out.println("ingrediente no esta mas!!!");
            }
            (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".productGrid-product-form-quantitySelect")));
            try{
                driver.findElement(By.cssSelector(".productGrid-product-form-quantitySelect")).click();
            } catch (Exception e) {}
            Thread.sleep(500);
            driver.findElement(By.cssSelector(".productGrid-product-form-quantitySelect")).click();
            {
                WebElement dropdown = driver.findElement(By.cssSelector(".productGrid-product-form-quantitySelect"));
                Thread.sleep(500);
                if (iAddTimes>10) iAddTimes=10;
                for (int i=1;i<iAddTimes;i++) {
                    dropdown.sendKeys(Keys.DOWN);
                    Thread.sleep(700);
                }
            }
            Thread.sleep(700);
            driver.findElement(By.cssSelector(".productGrid-product-form-quantitySelect")).click();
            Thread.sleep(700);
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
