package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Angel_Jobs {


	private static WebElement element = null;
	
	
	public static WebElement login(WebDriver driver) {
	
		element = driver.findElement(By.xpath("//a[@class='auth login u-fontWeight300']"));
		
		return element;
	}
	
	public static String CheckloginPageIsOpenedOrNot(WebDriver driver) {
		
		return "//div[@class='s-grid0-colSm24']/h1";
	}
	
	public static WebElement user_email(WebDriver driver) {
		
		element = driver.findElement(By.id("user_email"));
		
		return element;
	}
	
	public static WebElement aceeptCookie(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='c-button c-button--blue js-accept']"));
		
		return element;
	}
	public static WebElement user_pass(WebDriver driver) {
		
		element = driver.findElement(By.id("user_password"));
		
		return element;
	}
	
	public static WebElement submit_button(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//input[@type='submit']"));
		
		return element;
	}
	
	public static String loginCheckWithProfileExistOrNot(WebDriver driver) {
		
		return "//div[@class='text']/a[@title='View Profile']";
	}
	
	
	public static WebElement loginCheckWithProfileExistOrNotToSayStatus(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='text']/a[@title='View Profile']"));
		
		return element;
	}
	
	public static WebElement mainNavPage(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='mainNav respondToNavbarHeight u-inlineBlock u-nowrap u-uppercase']"));
		
		return element;
	}
	public static String mainNavPageToScrollUp(WebDriver driver) {
		
		return "//div[@class='mainNav respondToNavbarHeight u-inlineBlock u-nowrap u-uppercase']";
	
	}
	
	
	public static String currentlyShowingSearchKeywords(WebDriver driver) {
		
		return "//div[@class='currently-showing']";
	}
	
	
	public static WebElement currentlyShowingSearchKeywordsElement(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='currently-showing']"));
		
		return element;
	}
	
	public static String removeCurrentlyShowing(WebDriver driver) {
		
		return "//img[@title='Remove']";
	}

	public static WebElement searchBox(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='search-box']"));
		
		return element;
	}
	
	public static String inputFieldOfSearch(WebDriver driver) {
		
		return "//input[@class='input keyword-input']";
	}

	public static WebElement inputFieldOfSearchElement(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//input[@class='input keyword-input']"));
		
		return element;
	}
	
	public static String dataOfJobs(WebDriver driver) {
		
		return "//div[@class='find g-module gray hidden shadow_no_border startup-container']//div[@data-tab='find']";
	}			
	
	public static WebElement noOfJobsFound(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='label-container u-floatLeft']"));
		
		return element;
	}
	
	
	public static WebElement tagsOfJob(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='djl87 job_listings fbw9 browse_startups_table_row _a _jm expanded']//div[@class='details-row jobs']//div[@class='tags']"));
		
		return element;
	}
	
	public static WebElement closeExpandedJob(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='djl87 job_listings fbw9 browse_startups_table_row _a _jm expanded']//div[@class='prompt']"));
		
		return element;
	}
	
	public static WebElement dataOfJobsElement(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='find g-module gray hidden shadow_no_border startup-container']//div[@data-tab='find']"));
		
		return element;
	}
	
	public static String jobsThatNotExpanded(WebDriver driver) {
		
		return "//div[@class=' djl87 job_listings fbw9 browse_startups_table_row _a _jm']";
	}			
	
	public static WebElement companyName(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='djl87 job_listings fbw9 browse_startups_table_row _a _jm expanded']//a[@class='startup-link']"));
		
		return element;
	}
	
	public static WebElement applyButtonExpanded(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='djl87 job_listings fbw9 browse_startups_table_row _a _jm expanded']//a[@class='g-button blue apply-now-button']"));
		
		return element;
	}
	public static WebElement sendApplicationButton(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//button[@class='c-button c-button--blue']"));
		
		return element;
	}
	
	
	
	public static WebElement successMessage(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//div[@class='success-message']"));
		
		return element;
	}
	
	public static String successMessageWait(WebDriver driver) {
		
		return "//div[@class='success-message']";
		
	}
	public static WebElement undoApplication(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//button[@class='c-button c-button--gray']"));
		
		return element;
	}
	
	
	
	
	public static WebElement noteToRecruiter(WebDriver driver) {
		
		element = driver.findElement(By.xpath("//textarea[@name='note']"));
		
		return element;
	}
	
	public static String noteToRecruiterWait(WebDriver driver) {
		
		return "//textarea[@name='note']";
		
		
	}
	
	
	
	
	

	
	
}
