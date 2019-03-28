package test_scenarios;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class SearchJobsAndApply {

	ExtentReports extent;
	ExtentTest logger;
	WebDriver driver;
	Properties prop = new Properties();
	WebDriverWait wait;
	JavascriptExecutor js;
	Actions action;
	Connection con;
	int removeStatus;
	
	LinkedHashMap<LinkedHashMap<ArrayList<String>,String>, Integer> companyRedSkillWithDivPos = new LinkedHashMap<LinkedHashMap<ArrayList<String>,String>, Integer>();
	ArrayList<String> userSkillsData;
	
	@BeforeTest
	public void before() throws ClassNotFoundException, SQLException, IOException {
		
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/STMExtentReport.html",true);
		extent.addSystemInfo("Host Name", "Jagadeesh_Automation")
		.addSystemInfo("Environment", "AngelList Production")
		.addSystemInfo("Author", "Mr. Jagadeesh");
		extent.loadConfig(new File(System.getProperty("user.dir")+"/src/test/resources/extent-config.xml"));

		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/envProperties/env.properties");
		prop.load(fis); 
		
		Map<String, Object> prefs = new HashMap<String, Object>();
    	prefs.put("profile.default_content_setting_values.notifications", 2);
    	prefs.put("profile.default_content_settings.cookies", 2);
    	ChromeOptions options = new ChromeOptions();
    	options.setPageLoadStrategy(PageLoadStrategy.NONE);
    	options.addArguments("--disable-gpu");
    	options.setExperimentalOption("useAutomationExtension", false);
    	options.setExperimentalOption("prefs", prefs);
    	DesiredCapabilities capabilities = DesiredCapabilities.chrome();
    	capabilities.setCapability("chrome.switches", Arrays.asList("--disable-local-storage"));
    	capabilities.setCapability("disable-restore-session-state", true);
    	driver = new ChromeDriver(capabilities);
		driver.manage().window().maximize();
		
		
		con = common.Common_methods.getSqlConnection();
		if(con == null) {
			System.out.println("connection failure");
			System.exit(0);
		}
		removeStatus = common.Common_methods.execute(con, "DELETE FROM `jobs`");
		
		
		wait = new WebDriverWait(driver, 50);
		action = new Actions(driver);
	}
	
	@AfterTest
	public void tearDown() throws SQLException {
		extent.flush();
		extent.close();
		driver.close();
		con.close();
	}
	
	
	@AfterMethod
	public void after_method(ITestResult result) throws IOException {
		
		if(result.getStatus() == ITestResult.FAILURE) {
			logger.log(LogStatus.FAIL, " Test case failed : "+result.getName());
			logger.log(LogStatus.FAIL, "Reason is : "+result.getThrowable());
			String path = common.Common_methods.capture(driver, common.Common_methods.fileName());
			logger.log(LogStatus.FAIL, "Screenshot--->"+logger.addScreenCapture(path));
		}
		
		extent.endTest(logger);		
	}
	
	@Test(priority=1)
	public void user_skills() throws InterruptedException {
		common.Common_methods.printEndLine();
		logger = extent.startTest("user_skills");
		if(removeStatus>0) {
			logger.log(LogStatus.INFO, "deleted data from `jobs` on starting of TEST execution");
		}
		login();
		
		
		pages.Angel_Jobs.loginCheckWithProfileExistOrNotToSayStatus(driver).click();
		
		common.Common_methods.waitForPageLoaded(driver);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pages.Angel_profile_page.profileMainTocheckWhetherProfilePageOpenedOrNot(driver))));
		
		js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView();", pages.Angel_profile_page.skills_section(driver));
		
		List<WebElement> userSkills = pages.Angel_profile_page.skills_section(driver).findElements(By.tagName("span"));
		
		userSkillsData = new ArrayList<String>();
		for(int i=0;i<userSkills.size();i++) {
			userSkillsData.add(userSkills.get(i).getText());
		}
		
		logger.log(LogStatus.INFO, "user skills : \n"+Arrays.toString(userSkillsData.toArray()));
		System.out.println("user skills : ");
		for(int j=0;j<userSkillsData.size();j++) {
			System.out.print(userSkillsData.get(j)+", ");
		}
		System.out.println();
		

		Thread.sleep(3000);
		logger.log(LogStatus.PASS, "user_skills test case passed");

		
	}
	
	
	@Test(priority=3)
	public void applyJobWithSkillMatch() throws InterruptedException, SQLException {
		common.Common_methods.printEndLine();
		logger = extent.startTest("applyJobWithSkillMatch");
		js = (JavascriptExecutor) driver;
//		js.executeScript("window.scrollBy(0,-900)", "");
		js.executeScript("arguments[0].scrollIntoView();", pages.Angel_Jobs.mainNavPage(driver));

		Set<String> userSkillSet = new HashSet<String>(userSkillsData);
		
		
		LinkedHashMap<String,String> data = getJobsDataFromDB();
		
		for(String comapny : data.keySet()) {
			String companyName = comapny;
			String skills = data.get(comapny);
			ArrayList<String> requiredSkills = new ArrayList<String>(Arrays.asList(skills.split(",")));
			ArrayList<String> skillsWithTrim = new ArrayList<String>();
			for(int i =0;i<requiredSkills.size();i++) {
//				System.out.println(skills.get(i).trim());
				skillsWithTrim.add(requiredSkills.get(i).trim());
				
			}
			Set<String> requiredSkillSet = new HashSet<String>(skillsWithTrim);

			Set<String> commonSkills = new HashSet<String>(requiredSkillSet);
			commonSkills.retainAll(userSkillSet);
			Iterator<String> iter = commonSkills.iterator();
			System.out.println("Matched skills with `"+companyName+"` :--> ");
			String status;
			
			if(!iter.hasNext()) {
				System.out.println(prop.getProperty("no_skill_mentioned"));
				status = "company name :  `"+companyName+"` </br> \t"+prop.getProperty("no_skill_mentioned")+"</br>";
//				logger.log(LogStatus.INFO, "For `"+companyName+"`:--> "+prop.getProperty("no_skill_mentioned"));

			}else {
				String matchedSkills = "";
				while(iter.hasNext()) {
					String s =iter.next()+", ";
					System.out.print(s);
					matchedSkills = matchedSkills+s;
				}
				status = "company name : `"+companyName+"`</br> Matched skills: \t"+matchedSkills+" </br>";
//				logger.log(LogStatus.INFO, "Matched skills with `"+companyName+"`:--> "+matchedSkills);
				System.out.println();
			}
			
			
			double match_percentage = (100.0 * commonSkills.size() / requiredSkillSet.size());
			System.out.println("Match percentage  is --> " + (100.0 * commonSkills.size() / requiredSkillSet.size()));			
			String match = "Match percentage is : "+match_percentage+"</br>";
			
			if(match_percentage>Integer.parseInt(prop.getProperty("match_percentage"))) {
				System.out.println("Yeah.. job matched with user skill");
				String yeah = "Yeah.. job matched with user skill"+"</br>";
				System.out.println("company name is : "+companyName);
				action.moveToElement(driver.findElement(By.linkText(companyName))).click().build().perform();
				js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollBy(0,200)", "");
				pages.Angel_Jobs.applyButtonExpanded(driver).click();
				String applyButton  = "clicked on APPLY button"+"</br>";
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pages.Angel_Jobs.noteToRecruiterWait(driver))));
				pages.Angel_Jobs.noteToRecruiter(driver).sendKeys(prop.getProperty("note_to_recruiter")+Keys.ESCAPE);
				action.moveToElement(pages.Angel_Jobs.closeExpandedJob(driver)).click().build().perform();
				logger.log(LogStatus.PASS, status+match+yeah+applyButton);
				Thread.sleep(5000);
				
			}
			common.Common_methods.printLine();			
		}
	
		logger.log(LogStatus.PASS, "applyJobWithSkillMatch test case passed");
		
	}
	
	
	@Test(priority=2)
	public void searchJobsStoreInDB() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
		common.Common_methods.printEndLine();
		logger = extent.startTest("searchJobsStoreInDB");
		
		List<WebElement> mainNavigation = pages.Angel_Jobs.mainNavPage(driver).findElements(By.tagName("li"));
		
//		System.out.println(mainNavigation.size());
		
		for(int i = 0;i<mainNavigation.size();i++) {
			if(mainNavigation.get(i).getText().equalsIgnoreCase("Jobs")) {
				mainNavigation.get(i).click();
				common.Common_methods.waitForPageLoaded(driver);
				logger.log(LogStatus.PASS, "opened jobs page");
				break;
			}
		}
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pages.Angel_Jobs.currentlyShowingSearchKeywords(driver))));
		List<WebElement> currentlyShowing = pages.Angel_Jobs.currentlyShowingSearchKeywordsElement(driver).findElements(By.xpath(pages.Angel_Jobs.removeCurrentlyShowing(driver)));
		for(int remove = 0;remove<currentlyShowing.size();) {
//			System.out.println(remove);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(pages.Angel_Jobs.removeCurrentlyShowing(driver))));
			currentlyShowing.get(remove).click();
			currentlyShowing = pages.Angel_Jobs.currentlyShowingSearchKeywordsElement(driver).findElements(By.xpath(pages.Angel_Jobs.removeCurrentlyShowing(driver)));
		}
		
		ArrayList<String> values = common.Common_methods.readDataFromFile(System.getProperty("user.dir")+"/inputs/input_keywords_search.txt");
		
		pages.Angel_Jobs.searchBox(driver).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(pages.Angel_Jobs.inputFieldOfSearch(driver))));
		
		String searched="";
		for(int i=0;i<values.size();i++) {
			System.out.println(values.get(i));
			pages.Angel_Jobs.inputFieldOfSearchElement(driver).sendKeys(values.get(i)+Keys.ENTER);
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pages.Angel_Jobs.dataOfJobs(driver))));
				searched = searched+ ", "+values.get(i);
			} catch (Exception e) {
				System.out.println(e.getMessage()+"\nNo jobs found");
				logger.log(LogStatus.INFO, "No jobs found fo this search :"+searched);
				if(i==values.size()-1) {
					logger.log(LogStatus.FAIL, "No jobs found with : "+searched+"</br> Terminatng program, try with different search keywords");
					System.exit(0);	
				}
				
			}
			
		}
		logger.log(LogStatus.PASS,"Search with : "+ searched);
		
		
		common.Common_methods.printLine();
		
		WebElement jobsFound = pages.Angel_Jobs.noOfJobsFound(driver);
		
		System.out.println(jobsFound.getText()+ "For the provided search keywords");
		logger.log(LogStatus.PASS, jobsFound.getText()+ " For the provided search keywords");
		
		getReqSkillsFromJobAndClose();
		
		
		common.Common_methods.waitForPageLoaded(driver);
		
		WebElement jobsParent = pages.Angel_Jobs.dataOfJobsElement(driver);
		
		List<WebElement> eachJob = jobsParent.findElements(By.xpath(pages.Angel_Jobs.jobsThatNotExpanded(driver)));
			
		for(int i=0;i<eachJob.size();i++) {
			if(eachJob.get(i).isDisplayed()) {
				expandAndTakeDetailsThenClose(eachJob,i);
			}else {
				js = (JavascriptExecutor)driver;
				js.executeScript("arguments[0].scrollIntoView();", eachJob.get(i));
				expandAndTakeDetailsThenClose(eachJob,i);
			}
			
		}
		
		
		int sno=0;
		for(LinkedHashMap<ArrayList<String>,String> skills : companyRedSkillWithDivPos.keySet()) {
			sno+=1;
			int jobPosition = companyRedSkillWithDivPos.get(skills);
			String companyName = "";
			String Allskills = "";
			for(ArrayList<String> skillsWithCompanyName : skills.keySet()) {
//				System.out.println("Company name is : "+skills.get(skillsWithCompanyName));
				companyName = skills.get(skillsWithCompanyName);
//				System.out.println("Required Skills are : ");
				if(skillsWithCompanyName.size()==0) {
//					System.out.println(skills.get(skillsWithCompanyName)+" : "+prop.getProperty("no_skill_mentioned"));
					common.Common_methods.insertIntoDB(con,sno,companyName,prop.getProperty("no_skill_mentioned"),jobPosition);
				}else {
					for(int i =0;i<skillsWithCompanyName.size();i++) {
//						System.out.println(skillsWithCompanyName.get(i));
						Allskills = Allskills +"," +skillsWithCompanyName.get(i);
						
					}
//					System.out.println(Allskills.substring(1));
					common.Common_methods.insertIntoDB(con,sno,companyName,Allskills.substring(1),jobPosition);
				}
			}
		}
		
		ResultSet rs2 = common.Common_methods.execute_Query(con, "select COUNT(*) from jobs");
		String count = null;
		while(rs2.next()) {
			count = rs2.getString("COUNT(*)");
		}
		System.out.println(count);
		System.out.println(companyRedSkillWithDivPos.size());
		if(Integer.parseInt(count)==companyRedSkillWithDivPos.size()) {
			logger.log(LogStatus.PASS, "successfully inserted data to `jobs` table");
		}else {
			logger.log(LogStatus.FAIL, "Error on inserting data");
		}
		
		ResultSet rs = common.Common_methods.execute_Query(con, prop.getProperty("query_to_fetch_data"));
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		System.out.println("Data that stored in DB-->");
		while(rs.next()) {
			for(int i=1;i<=columnCount;i++) {
				System.out.print("["+rs.getString(i)+ "] ");
			}
			System.out.println();
			
		}
		


		
		Thread.sleep(5000);
		logger.log(LogStatus.PASS, "searchJobsStoreInDB test case passed");
		
	}
	
	public void expandAndTakeDetailsThenClose(List<WebElement> eachJob, int i) throws InterruptedException {
		action.moveToElement(eachJob.get(i)).click().build().perform();
		Thread.sleep(2000);
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", pages.Angel_Jobs.tagsOfJob(driver));
		getReqSkillsFromJobAndClose();
	}
	
	public void getReqSkillsFromJobAndClose() throws InterruptedException {
		
		System.out.println("Company Name : "+pages.Angel_Jobs.companyName(driver).getText());
		String JobReqSkills = pages.Angel_Jobs.tagsOfJob(driver).getText();
		String JobcompanyName = pages.Angel_Jobs.companyName(driver).getText();
		String[] comapanyTags = JobReqSkills.split("·");
		ArrayList<String> compnyReqSkills = new ArrayList<String>();
		LinkedHashMap<ArrayList<String>, String> FirstcompanyReqSkillsWithCompName = new LinkedHashMap<ArrayList<String>,String>();
		for(int j=3;j<comapanyTags.length;j++) {
//			System.out.println(FirstcomapanyTags[j]);
			compnyReqSkills.add(comapanyTags[j]);
		}
		FirstcompanyReqSkillsWithCompName.put(compnyReqSkills, JobcompanyName);
		companyRedSkillWithDivPos.put(FirstcompanyReqSkillsWithCompName, 0);
		System.out.println("skills-->");
		if(compnyReqSkills.size()==0) {
			System.out.println(prop.getProperty("no_skill_mentioned"));
			logger.log(LogStatus.PASS, "Company name : "+JobcompanyName+" </br> \t"+prop.getProperty("no_skill_mentioned"));

		}else {
			for(int k= 0;k<compnyReqSkills.size();k++) {
				System.out.print(compnyReqSkills.get(k)+", ");
			}	
			System.out.println();
			common.Common_methods.printLine();
			logger.log(LogStatus.PASS, "Company name : "+JobcompanyName+" </br> Required-skills : </br> \t"+Arrays.toString(compnyReqSkills.toArray()));
		}
		action.moveToElement(pages.Angel_Jobs.closeExpandedJob(driver)).click().build().perform();
		Thread.sleep(2000);
	}
	
	public LinkedHashMap<String,String> getJobsDataFromDB() throws SQLException {
		LinkedHashMap<String,String> jobsFromDB = new LinkedHashMap<String, String>();
		ResultSet rs = common.Common_methods.execute_Query(con, prop.getProperty("query_to_fetch_data"));
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		System.out.println(columnCount);
		while(rs.next()) {
			String skills = rs.getString("req_skills");
			String companyName = rs.getString("company_name");
			jobsFromDB.put(companyName,skills);
		}
		return jobsFromDB; 
	}
	
	public void login() {

		driver.get(prop.getProperty("URL"));
		
		common.Common_methods.waitForPageLoaded(driver); //to wait till javascript DOM returns readystate true
		
		pages.Angel_Jobs.login(driver).click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pages.Angel_Jobs.CheckloginPageIsOpenedOrNot(driver))));
		
		pages.Angel_Jobs.user_email(driver).sendKeys(prop.getProperty("user_email"));
		
		pages.Angel_Jobs.user_pass(driver).sendKeys(prop.getProperty("password"));
		
		pages.Angel_Jobs.submit_button(driver).click();

		common.Common_methods.waitForPageLoaded(driver); //to wait till javascript DOM returns readystate true
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pages.Angel_Jobs.loginCheckWithProfileExistOrNot(driver))));
		
		if(pages.Angel_Jobs.loginCheckWithProfileExistOrNotToSayStatus(driver).isDisplayed()) {
			System.out.println("Successfully logged in");
			logger.log(LogStatus.PASS, "Successfully Logged in");
		}
		
	}

	
}

