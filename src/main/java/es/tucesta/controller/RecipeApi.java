package es.tucesta.controller;

import com.sun.mail.smtp.SMTPTransport;
import es.tucesta.response.OrderResponse;
import es.tucesta.supers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * RecipeApi controller provides Recipe endpoint.
 *
 * @author
 * @version 0.3
 * @since   2020-11-03
 */

@RequestMapping("/api")
@RestController
public class RecipeApi {
    private final Logger log = LoggerFactory.getLogger(RecipeApi.class);
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setJdbcTemplate(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/processOrder/{orderId}")
    String processOrder(@PathVariable Long orderId) throws MessagingException {

        log.info("Received startOrder request "+orderId);
        jdbcTemplate.update("update tbl_order set percent='0' where order_id=" + orderId);
        jdbcTemplate.update("update tbl_order_detail set added=0 where order_id="+orderId);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String sHtml="";
            log.info("Processing in separate thread");
            AtomicReference<String> usrSuper = new AtomicReference<>("");
            AtomicReference<String> pwdSuper = new AtomicReference<>("");
            AtomicReference<String> idTienda = new AtomicReference<>("");
            String sSql="select tbl_order.*,username,password from tbl_order left join tbl_user_shops on tbl_order.user_id=tbl_user_shops.user_id and	tbl_order.shop_id=tbl_user_shops.shop_id where order_id=" + orderId;
            jdbcTemplate.query(sSql, rst -> {
                if (rst.next()) {
                    usrSuper.set(rst.getString("username"));
                    pwdSuper.set(rst.getString("password"));
                    idTienda.set(rst.getString("shop_id"));
                }
                return null;
            });
            ChromeOptions chromeOptions = new ChromeOptions();
            if ((idTienda.get().equals("1")) || (idTienda.get().equals("4"))|| (idTienda.get().equals("5"))) {
                //si ulabox, little
                chromeOptions.addArguments("--start-maximized");
                //chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("force-device-scale-factor=0.75");
                chromeOptions.addArguments("high-dpi-support=0.75");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
            }

            System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver");
            WebDriver driver = new ChromeDriver(chromeOptions);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
            driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
            //-----login-----------
            Supermarket supermarket=null;
            switch (idTienda.get()) {
                case "1" :
                    supermarket=new Alcampo(driver, usrSuper.get(), pwdSuper.get());
                    break;
                case "2" :
                    supermarket=new Carrefour(driver, usrSuper.get(), pwdSuper.get());
                    break;
                case "3" :
                    supermarket=new Dia(driver, usrSuper.get(), pwdSuper.get());
                    break;
                case "4" :
                    supermarket=new Consum(driver, usrSuper.get(), pwdSuper.get());
                    break;
                case "5" :
                    supermarket=new Ulabox(driver, usrSuper.get(), pwdSuper.get());
                    break;
            };
            supermarket.login();
            jdbcTemplate.update("update tbl_order set percent='10' where order_id=" + orderId);
            supermarket.emptyBasket();
            jdbcTemplate.update("update tbl_order set percent='20' where order_id=" + orderId);
            sSql="select count(ingredient) from tbl_order_detail where order_id="+orderId;
            int size=jdbcTemplate.queryForObject(sSql, Integer.class);
            sSql="select sum(qty) as qty,ingredient from tbl_order_detail where order_id="+orderId+" group by ingredient";
            int increment=80/size;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sSql);
            int iNumProd=1;
            for (Map row : rows) {
                int i=1;
                List<Map<String, Object>> rows2 = jdbcTemplate.queryForList("select * from tbl_ingredients_shops where description='"+row.get("ingredient")+"' and shop_id="+idTienda);
                for (Map row2 : rows2) {
                    String url=row2.get("url").toString();
                    String btnName=row2.get("button").toString();
                    //int qty = ((BigDecimal) row.get("qty")).intValue();
                    int qty = ((BigDecimal) row.get("qty")).intValue();
                    int qty2 = ((Long)row2.get("qty")).intValue();
                    float fAddTimes = (float)qty / (float)qty2;
                    int iAddTimes = (int) Math.ceil(fAddTimes);
                    System.out.println("***********"+row.get("ingredient")+"::"+fAddTimes+"*****"+iAddTimes+"********"+btnName);
                    boolean bAdded= supermarket.addIngredient(url,iAddTimes,btnName); //false;
                    try {Thread.sleep(300);} catch (Exception e) {}
                    if (!bAdded) sHtml+=("<a href=\""+url+"\">"+row.get("ingredient")+"</a> no está disponible<br>");
                    i=i+1;
                }
                jdbcTemplate.update("update tbl_order set percent='"+(20+(increment*iNumProd))+"' where order_id=" + orderId);
                jdbcTemplate.update("update tbl_order_detail set added=1 where order_id="+orderId+" and ingredient='"+row.get("ingredient")+"'");
                iNumProd=iNumProd+1;
            }
            driver.close();
            jdbcTemplate.update("update tbl_order set percent='100' where order_id=" + orderId);

            try {
                sendMail(sHtml,orderId);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        return "ok";
    }

    @GetMapping("/progressOrder/{orderId}")
    OrderResponse progressOrder(@PathVariable Long orderId) {

        log.info("Received startOrder request "+orderId);
        String sql = "SELECT percent FROM tbl_order WHERE order_id="+orderId;
        int percent = jdbcTemplate.queryForObject(sql, Integer.class);
        sql="select * from tbl_order_detail where added=1 and order_id="+orderId;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        String ingredientsAdded="";
        for (Map row : rows) {
            ingredientsAdded+=(row.get("qty")+" "+row.get("ingredient")+"<br>");
        }
        return new OrderResponse(orderId,percent,ingredientsAdded);
    }

    public void sendMail(String sHtml,Long orderId) throws MessagingException {

        // Poner link a recetas en el email
        sHtml+="<br>";
        String sql="SELECT * FROM tbl_order_recipes left join tbl_recipe on tbl_order_recipes.recipe_id=tbl_recipe.id WHERE order_id="+orderId;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        String ingredientsAdded="";
        for (Map row : rows) {
            sHtml+=("<a href=\"http://tucesta.es/guid.php?id="+row.get("recipe_id")+"\">"+row.get("name")+"</a><br>");
        }
        sHtml+="<br>Que lo disfrutes!<br><b>tucesta.es</b>";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtppro.zoho.eu");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

        Session esession = Session.getInstance(props, null);
        Message msg = new MimeMessage(esession);
        msg.setFrom(new InternetAddress("info@tucesta.es"));;
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse("nacho.macias@gmail.com", false));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        msg.setSubject("Tu Cesta "+sdf.format(new java.util.Date()) );
        msg.setContent(sHtml, "text/html");
        msg.setSentDate(new java.util.Date());
        SMTPTransport t =
                (SMTPTransport)esession.getTransport("smtps");
        t.connect("smtppro.zoho.eu", "info@tucesta.es", "a2adosA2!");
        t.sendMessage(msg, msg.getAllRecipients());
        System.out.println("Response: " + t.getLastServerResponse());
        t.close();
    }

}
