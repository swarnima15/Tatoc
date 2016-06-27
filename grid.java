package grid_gate;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Cookie;
import org.openqa.jetty.html.Input;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.server.handler.SwitchToFrame;
import org.openqa.selenium.interactions.Actions;

import java.util.Arrays;
import java.util.List;
import java.lang.*;
public class grid {

	public static void main(String[] args)
	{
		//TASK:1
	WebDriver driver=new FirefoxDriver();
	 driver.get("http://10.0.1.86/");
	driver.get("http://10.0.1.86/tatoc");
	driver.get("http://10.0.1.86/tatoc/basic/grid/gate");
	WebElement result_1=driver.findElement(By.className("greenbox"));
	driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
	result_1.click();
	driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
	
	//TASK:2	
   driver.switchTo().frame("main");
   String box1=driver.findElement(By.id("answer")).getAttribute("class");
	driver.switchTo().frame("child");
	String box2=driver.findElement(By.id("answer")).getAttribute("class");
   while(!box1.equals(box2))
   {
	   driver.switchTo().defaultContent();
	   driver.switchTo().frame("main");
	   driver.findElement(By.linkText("Repaint Box 2")).click();
	   driver.switchTo().frame("child");
	   box2=driver.findElement(By.id("answer")).getAttribute("class");
 }
   driver.switchTo().defaultContent();
   driver.switchTo().frame("main");
   driver.findElement(By.linkText("Proceed")).click();
   driver.switchTo().defaultContent();
   
	//TASK:3
	WebElement drag = driver.findElement(By.id("dragbox"));
	WebElement drop = driver.findElement(By.id("dropbox"));
	Actions action = new Actions(driver);
	action.dragAndDrop(drag,drop).build().perform();
	
	driver.findElement(By.linkText("Proceed")).click(); 
	
	//TASK:4
	String  handle= driver.getWindowHandle(); 
	driver.findElement(By.xpath("//a[@href='#']")).click();
	for(String winHandle : driver.getWindowHandles()){
	    driver.switchTo().window(winHandle);
	}
	driver.findElement(By.id("name")).sendKeys("swarnima");
	driver.findElement(By.id("submit")).click();
	driver.switchTo().window(handle);
	List<WebElement> ds= driver.findElements(By.xpath("//a[@href='#']"));
	ds.get(1).click(); 
	
	
	//TASK:5
	driver.findElement(By.linkText("Generate Token")).click();
    String val=driver.findElement(By.id("token")).getText();
	String[] val_1=val.split(": ");
	Cookie name = new Cookie("Token", val_1[1]);
	driver.manage().addCookie(name);
	try{
	Thread.sleep(5000);}
	catch (Exception ex){}
	driver.findElement(By.linkText("Proceed")).click(); 
	
	driver.quit();
	
	}

}
