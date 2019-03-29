package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Angel_profile_page {


	private static WebElement element = null;
	
	
	public static String profileMainTocheckWhetherProfilePageOpenedOrNot(WebDriver driver) {
		
		return "//div[@class='g-lockup-subheader']";
	}
	
	public static WebElement skills_section(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='s-grid']//div[3]"));
		
		return element;

	}
	

	

	
	
}
