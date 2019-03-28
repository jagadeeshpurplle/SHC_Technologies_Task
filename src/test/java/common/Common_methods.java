package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Common_methods {
	
	static Properties prop = new Properties();
	

	public static void waitForPageLoaded(WebDriver driver) { //function to wait till page loads completely 
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }
	
	public static ArrayList<String> readDataFromFile(String path) throws FileNotFoundException {
		File file = new File(path);
		ArrayList<String> values = new ArrayList<String>();
		Scanner sc = new Scanner(file);
		while(sc.hasNextLine()){
			values.add(sc.nextLine());
		}
		return values;
	}
	

    //get sql connection
    public static Connection getSqlConnection() throws ClassNotFoundException { //making connection with sql DB and return connection
    	Connection connn = null;
    	try { 
			
			System.out.println("Making connection with mysql");
			Class.forName("com.mysql.jdbc.Driver"); 
			connn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SHC_Technologies?user=root&password=chinna");
			System.out.println("Connection established with mysql"); 
			return connn;
    	}catch (SQLException e1) { 
		
			System.out.println(e1.getMessage());
		
		}
		return connn;
    }
    
    public static ResultSet execute_Query(Connection connn,String query) throws SQLException { //return result set
    	    	
		Statement st = connn.createStatement(); 
		
		ResultSet rs=st.executeQuery(query);
		
		return rs;
    }
    public static int execute(Connection connn,String query) throws SQLException { // to execute statement
    	
		Statement st = connn.createStatement();
		
		int rs=st.executeUpdate(query);
		
		return rs;
    }
    
	
	public static void insertIntoDB(Connection con,int s, String company, String skill, int pos) throws ClassNotFoundException, SQLException, IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/envProperties/env.properties");
		prop.load(fis); 
		PreparedStatement preparedStmt = con.prepareStatement(prop.getProperty("query_to_insert_jobs_data")); // query to insert data to DB with prepare statement
		preparedStmt.setInt (1, s);
		preparedStmt.setString (2, company);
		preparedStmt.setString (3, skill);
		preparedStmt.setInt(4, pos);

		// execute the preparedstatement
		preparedStmt.executeUpdate(); //executing query to insert data to DB
	}
	
	public static String capture(WebDriver driver, String screenshotName) throws IOException { //to capture screenshot
		
		TakesScreenshot ts = (TakesScreenshot)driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String dest = System.getProperty("user.dir")+"/screenshots/"+screenshotName+".png";
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);
		return dest;
		
	}
	
	public static String fileName() { // generating file with current time
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_hh:mm:ss");
		Date date= new Date();
		String file_name = sdf.format(date);		
		return file_name;
		
	}
	
	public static void printLine() { 
		System.out.println("----------------------------------------------");
	}
	
	public static void printEndLine() {
		System.out.println("=====================================================");
	}
	
}
