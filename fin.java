import java.util.ArrayList;

import java.util.List;
import java.util.StringTokenizer;
import java.lang.*;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;

import java.io.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

public class fin {

public static void main(String[] args) {

	try
	{
	Connection con=Sqlconnect.dbConnector();
	  
	//OPENS WHATSAPP WEB
	System.setProperty("webdriver.chrome.driver","C:\\Users\\hariharn.r\\Chrome Driver\\chromedriver.exe");
	WebDriver driver = new ChromeDriver();	 
	driver.get("https://web.whatsapp.com/");
	Thread.sleep(40000);
	
	
	//MAP OF THE CHAT/CONTACT AND THE MESSAGES IN THAT CHAT
	LinkedHashMap<String, LinkedHashMap<Long, String>> map = new LinkedHashMap<String, LinkedHashMap<Long, String>>();

		while(true)
		{	
			//SEARCH FOR 'Search  or start new text'
			WebElement search =driver.findElement(By.className("ZP8RM"));
			
			//CLICKS ON THE FIRST CONTACT/CHAT
			Actions builder=new Actions(driver);
			Action down=builder.moveToElement(search).moveByOffset(15,30).doubleClick().build();
			down.perform();
			
			//CLICKS ON THE DROP DOWN MENU(for archive chat) for the FIRST CHAT
			Action mouse=builder.moveToElement(search).moveByOffset(160,80).click().build();
			mouse.perform();
	
			try
			{
				//FIND THE NAME OF THAT CONTACT
				String name = driver.findElement(By.className("_19vo_")).getText();
				
				
				/*List<WebElement> allImages = driver.findElements(new ByChained(By.xpath("//div[@class='_1_keJ']"),By.tagName("img")));
				for(WebElement imageFromList:allImages){
				     String ImageUrl=imageFromList.getAttribute("src");
				     System.out.println(ImageUrl); //will get you all the image urls on the page
				}*/
				//By byXpath1 = By.xpath("//div[@class='_1zGQT _2ugFP message-in' and (@class='_3fnHB')]");
			
				
				//MAKES A LIST OF THE MESSAGES TO BE READ
				List<WebElement> strip=driver.findElements(By.xpath("//span[@class='selectable-text invisible-space copyable-text']"));
				
				//List<WebElement> strip=driver.findElements(By.cssSelector("div._1zGQT _2ugFP message-in span.selectable-text invisible-space copyable-text"));
				//List<WebElement> strip=driver.findElements(byXpath);
				//List<WebElement> strip=new ArrayList<>();
				//for(int j=0;j<strip0.size();j++)
					//strip.add(strip0.get(j).findElement(By.xpath("//span[@class='selectable-text invisible-space copyable-text']")));
				List<WebElement> dates=driver.findElements(By.className("_3fnHB"));
				//List<WebElement> dates = driver.findElements(By.cssSelector("div._1zGQT _2ugFP message-in span._3fnHB"));

				List<String> strip2 = new ArrayList<>();
				List<String> datestrip = new ArrayList<>();
				
				for(int i = 0; i<strip.size(); i++)
				{
					strip2.add(strip.get(i).getText());
					
					//strip2.add(( strip.get(i).findElement(By.xpath("//span[@class='selectable-text invisible-space copyable-text']"))).getText());
					datestrip.add(dates.get(i).getText());
					//datestrip.add(( strip.get(i).findElement(By.xpath("//span[@class='_3fnHB']"))).getText());
				}
			
				//CLICKS THE ARCHIVE CHAT
				driver.findElement(By.className("_3zy-4")).click();
				Thread.sleep(5000);
				
				boolean b = false, b1 = false; 
				
				if(!(map.containsKey(name)))	
				{	
					LinkedHashMap<Long, String> m = new LinkedHashMap<Long, String>();
					map.put(name, m);
				}		
				for(int i=0;i<strip.size();i++)
				{
					try
					{
						Long c = (long)(strip2.get(i).hashCode());
						b = (map.get(name)).containsKey(c);
						
						//message alredy exists in database, so compare with time
						if(b)			
						{
							String q = "SELECT Date, Message from User";
							Statement p = con.createStatement();
							
							ResultSet rs = p.executeQuery(q);
							while(rs.next())
							{
								String d = rs.getString("Date");
								if(c == Long.parseLong(rs.getString("Message")) && d.equals(datestrip.get(i)))
								{
									b1 = true;
									break;
								}
							}
						}
						if(!b || !b1)
						{
							String query = "INSERT INTO User VALUES(?,?,?,?)";
							PreparedStatement pst=con.prepareStatement(query);
							pst.setString(1, name);
							pst.setInt(2, strip2.get(i).hashCode());
							pst.setString(3, strip2.get(i));
							pst.setString(4, datestrip.get(i));  
							
							//PUT THE NEW MESSAGE INTO THE DATABASE
							pst.executeUpdate();
							int temp = strip2.get(i).hashCode();
							Long l = new Long(temp);
							(map.get(name)).put(l , strip2.get(i));
						}	
					}
					catch(Exception f)
					{
						f.printStackTrace();
					}	
				}
				
				//TIME TO ARCHIVE THE CHAT
				Thread.sleep(5000);
			}
			catch(Exception f)
			{
				f.printStackTrace();
				driver.findElement(By.className("_3zy-4")).click();
				Thread.sleep(1000);
			}
		}
	}	
	catch(Exception e)
	{		
		e.printStackTrace();
	}
}	
}
