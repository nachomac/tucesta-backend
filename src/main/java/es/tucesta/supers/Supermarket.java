package es.tucesta.supers;

import org.openqa.selenium.WebDriver;

public interface Supermarket {
    WebDriver driver=null;
    String usr=null,pwd=null;
    public void login();
    public void emptyBasket();
    public boolean addIngredient(String url,int iAddTimes,String btnName);
}

