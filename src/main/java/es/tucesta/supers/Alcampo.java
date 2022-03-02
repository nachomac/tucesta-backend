package es.tucesta.supers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;

public class Alcampo implements Supermarket {
    WebDriver driver;
    String usr;
    String pwd;
    public Alcampo(WebDriver driver,String usr,String pwd) {
        this.driver=driver;
        this.usr=usr;
        this.pwd=pwd;
    }
    public void login(){
        driver.get("https://ar-customerdiamond-es--prod.force.com/authorization?language=es");
        try {Thread.sleep(1000);} catch (Exception e) {}
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id("uname1")));
        System.out.println("supuestamente cargado");
        driver.findElement(By.id("uname1")).click();
        driver.findElement(By.id("uname1")).sendKeys("nacho.macias@gmail.com");
        driver.findElement(By.id("pwd1")).click();
        try {Thread.sleep(1000);} catch (Exception e) {}
        driver.findElement(By.id("pwd1")).sendKeys("snkxjb6gA");
        driver.findElement(By.id("pwd1")).sendKeys(Keys.ENTER);
        driver.findElement(By.id("btnSubmit_login")).click();
        try {Thread.sleep(3000);} catch (Exception e) {}
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.cc_b_ok.auxOKButton")));
        driver.findElement(By.cssSelector("button.cc_b_ok.auxOKButton")).click();
        try {Thread.sleep(3000);} catch (Exception e) {}
    }
    public void emptyBasket() {
        System.out.println("empty basket");
        try {Thread.sleep(1000);} catch (Exception e) {}
        driver.get("https://www.alcampo.es/compra-online/cart/emptyCart");
        try {Thread.sleep(1000);} catch (Exception e) {}
    }
    public boolean addIngredient(String url,int iAddTimes,String btnName){
        boolean bProv=false;
        driver.get(url);
        try {
            Thread.sleep(1000);
            WebElement ele = driver.findElement(By.id(btnName));
            JavascriptExecutor executor = (JavascriptExecutor)driver;
            executor.executeScript("arguments[0].click();", ele);
            Thread.sleep(1000);
            for (int i=1;i<iAddTimes;i++) {
                Thread.sleep(700);
                driver.findElement(By.cssSelector(".yCmsContentSlot:nth-child(3) .imagen-mas")).click();
            }
            bProv=ele.isEnabled();
        } catch (Exception e){
            e.printStackTrace();
        }
        return bProv;
    }
}
