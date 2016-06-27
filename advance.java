package grid_gate;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import jdk.nashorn.internal.parser.JSONParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import  java.sql.SQLException;

public class advance {

	public static void main(String[] args) throws SQLException, InterruptedException, JSONException, IOException 
	{
		WebDriver driver=new FirefoxDriver();
		driver.get("http://10.0.1.86/");
		driver.get("http://10.0.1.86/tatoc");
		driver.get("http://10.0.1.86/tatoc/advanced/hover/menu");
		
		//TASK:1
		Thread.sleep(2000);
		WebElement menu=driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/span[1]"));
		Actions builder = new Actions(driver);  
		builder.moveToElement(menu).build().perform();
		WebDriverWait wait = new WebDriverWait(driver, 5); 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[2]/div[2]/span[5]")));  
		WebElement menuOption = driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/span[5]"));
		menuOption.click();
		
		//TASK:2
		Thread.sleep(2000);
		String symbol=null, name=null, passkey=null,id=null;
		PreparedStatement stmt=null;
		Connection con=null;
		ResultSet rs=null;
		symbol= driver.findElement(By.cssSelector("#symboldisplay")).getText();

		try{
	    con=DriverManager.getConnection("jdbc:mysql://10.0.1.86:3306/tatoc","tatocuser","tatoc01");
	    stmt= con.prepareStatement("select id from identity where symbol=?;");
		stmt.setString(1, symbol);
		 rs= stmt.executeQuery();
			while( rs.next()){
				id=  rs.getString("id");
			}
			int identity= Integer.parseInt(id);
		    rs.close();
			stmt.close();	
			stmt= con.prepareStatement("select name,passkey from credentials where id=?;");
			stmt.setInt(1, identity);
			rs= stmt.executeQuery();
			if(( rs).next()){
				name= rs.getString("name");
				passkey= rs.getString("passkey");
			}
			rs.close();
			stmt.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
       {
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
			
			if(con!=null){
				con.close();
			}
		}
		
		driver.findElement(By.cssSelector("#name")).sendKeys(name);
		driver.findElement(By.cssSelector("#passkey")).sendKeys(passkey);
		driver.findElement(By.cssSelector("#submit")).click();
		
		//TASK 3:
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 js .executeScript("player.play()");
		 Thread.sleep(40000);
		 driver.findElement(By.linkText("Proceed")).click();
		
	    //TASK:4
	     String string=driver.findElement(By.id("session_id")).getText();
		 string=string.substring(12, string.length());
	     URL url = new URL("http://10.0.1.86/tatoc/advanced/rest/service/token/"+string);
		 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	   	conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
				}
		in.close();
		System.out.println(response.toString());
		String ssss=new String(response);
		JSONObject obj=new JSONObject(ssss);
		ssss=(String) obj.get("token");
        System.out.println(ssss);
				
		//post
				
		URL url1 = new URL("http://10.0.1.86/tatoc/advanced/rest/service/register");
		HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
		conn1.setRequestMethod("POST");
		conn1.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String urlParameters = "id="+string+"&signature="+ssss+"&allow_access=1";
				
		// Send post request
		conn1.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn1.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
        int responseCode = conn1.getResponseCode();
		conn1.disconnect();
		driver.findElement(By.cssSelector(".page a")).click();

	    //TASK:5	
		 FirefoxProfile profile = new FirefoxProfile();

			String path = "/home/swarnimagupta/Downloads";
		    profile.setPreference("browser.download.folderList", 2);
		    profile.setPreference("browser.download.dir", path);
		    profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		    profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/Gzip, application/csv, application/ris, text/csv,text/dat, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, applicaion/Gzip-archive, application/download, application/octet-stream");
		    profile.setPreference("browser.download.manager.showWhenStarting", false);
		    profile.setPreference("browser.download.manager.focusWhenStarting", false);  
		    profile.setPreference("browser.download.useDownloadDir", true);
		    profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		    profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		    profile.setPreference("browser.download.manager.closeWhenDone", true);
		    profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		    profile.setPreference("browser.download.manager.useWindow", false);
		    profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
		    profile.setPreference("pdfjs.disabled", true);

		    driver = new FirefoxDriver(profile);
		    driver.get("http://10.0.1.86/tatoc/advanced/file/handle");
		    driver.findElement(By.linkText("Download File")).click();
		  
		    
		   
		    BufferedReader br = null;
	        String strng=null, sCurrentLine;
	        try 
	        {
	            int i=0;
	            br = new BufferedReader(new FileReader("/home/swarnimagupta/Downloads/file_handle_test.dat"));
	            while ((sCurrentLine = br.readLine()) != null) 
	            {
	                if(i==2)
	                    strng = sCurrentLine;
	                i++;
	            }
	        }
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        } 

		  String strng1 = strng.substring(11,strng.length());
	        driver.findElement(By.id("signature")).sendKeys(strng1);
	        driver.findElement(By.className("submit")).click();
	}
}
