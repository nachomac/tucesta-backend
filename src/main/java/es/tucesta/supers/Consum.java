package es.tucesta.supers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;

public class Consum implements Supermarket {
    WebDriver driver;
    String usr;
    String pwd;
    public Consum(WebDriver driver,String usr,String pwd) {
        this.driver=driver;
        this.usr=usr;
        this.pwd=pwd;
    }
    public void login(){

        try {
            driver.get("https://tienda.consum.es/consum/#Login");
            Thread.sleep(1500);
            try {
                driver.findElement(By.id("onetrust-accept-btn-handler")).click();
            } catch (Exception e) {
                System.out.println("cookies accepted already");
            }
            Thread.sleep(1500);
            WebElement foo = new WebDriverWait(driver, 100)
                    .until(driver -> driver.findElement(By.id("userName")));
            driver.findElement(By.id("userName")).click();
            driver.findElement(By.id("userName")).sendKeys(usr);
            driver.findElement(By.id("password")).sendKeys(pwd);
            driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
            {
                WebDriverWait wait = new WebDriverWait(driver, 30);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".t-header-navbar--form > #txtSearch")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emptyBasket() {
        System.out.println("____empty basket_____");
        driver.get("https://tienda.consum.es/consum/producto/-tomate-en-rama/p-968953#DetalleProducto");

        (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".p-3:nth-child(4) .content-badge-component__badge div:nth-child(2)")));
        try {driver.findElement(By.cssSelector(".p-3:nth-child(4) .content-badge-component__badge div:nth-child(2)")).click();} catch (Exception e) {System.out.println(e.toString());}
        try{Thread.sleep(1500);} catch (Exception e) {}

        try{driver.findElement(By.cssSelector(".cart-products-list-component__trash > cmp-icon")).click();} catch (Exception e) {System.out.println(e.toString());}
        try{Thread.sleep(1500);} catch (Exception e) {}

        try{driver.findElement(By.cssSelector(".u-rhythm > p")).click();} catch (Exception e) {System.out.println(e.toString());}
        try{Thread.sleep(1500);} catch (Exception e) {}
    }

    public boolean addIngredient(String url,int iAddTimes,String btnName){
System.out.println("addIngre");
        driver.get(url);
        try{Thread.sleep(500);} catch (Exception e) {}
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".btn-primary")));
        }

        try{
            driver.findElement(By.cssSelector(".btn-primary")).click();
            Thread.sleep(500);
        } catch (Exception e) {
            return false;
        }
        for (int i=1;i<iAddTimes;i++) {
            driver.findElement(By.cssSelector(".iconAngular-plus")).click();
            try{Thread.sleep(700);} catch (Exception e) {}
        }
        return true;
    }
}
