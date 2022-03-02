package es.tucesta.supers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;

public class Ulabox implements Supermarket {

    WebDriver driver;
    String usr;
    String pwd;
    public Ulabox(WebDriver driver,String usr,String pwd) {
        this.driver=driver;
        this.usr=usr;
        this.pwd=pwd;
    }
    public void login(){

        try {
            driver.get("https://www.ulabox.com/entrar");
            (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
            driver.findElement(By.name("email")).click();
            driver.findElement(By.name("email")).sendKeys("nacho.macias@gmail.com");
            driver.findElement(By.name("password")).sendKeys("a2adosA2");
            driver.findElement(By.name("password")).sendKeys(Keys.ENTER);
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void emptyBasket() {

        try{Thread.sleep(4000);} catch (Exception ee) {}
        driver.findElement(By.cssSelector(".MuiIconButton-root:nth-child(1)")).click();
        try {
            while (true) {
                Thread.sleep(1000);
                driver.findElement(By.cssSelector(".MuiGrid-root:nth-child(1) > .MuiPaper-root .MuiButtonBase-root:nth-child(3) .MuiSvgIcon-root")).click();
            }
        } catch (Exception e) {}
    }
    public boolean addIngredient(String url,int iAddTimes,String btnName){

        driver.get(url);
        try{Thread.sleep(1000);} catch (Exception ee) {}
        driver.findElement(By.cssSelector(".jss421 > .MuiButton-label")).click();
        for (int i=1;i<iAddTimes;i++) {
            try{Thread.sleep(700);} catch (Exception ee) {}
            driver.findElement(By.cssSelector(".MuiButtonBase-root:nth-child(3) path")).click();
        }
        try{Thread.sleep(1000);} catch (Exception ee) {}
        return true;
    }
}
