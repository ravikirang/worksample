package ExtentReports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class SigininTests {
	static WebDriver driver;
	
	@DataProvider(name="getusernameandpassword")
	public Object[][] getUserNameAndPassword() throws FileNotFoundException, IOException{
		Properties props = new Properties();
		props.load(new FileInputStream(new File("Datadriventest.properties")));
		int size = props.size();
		String[][] str =  new String[size/2][2];
		for(int i=0;i<size/2;i++){
			str[i][0] = props.getProperty("UserID"+i);
			str[i][1] = props.getProperty("Password"+i);
		}
		return str;
	}
	
	/*@DataProvider(name="getusernameandpassword")
	public Object[][] getUserNameAndPasswordFromExcel() throws FileNotFoundException, IOException{
		Properties props = new Properties();
		props.load(new FileInputStream(new File("Datadriventest.properties")));
		Xls_Reader reader = new Xls_Reader("E:\\TestData.xlsx");
		return reader.getAllValuesofTestCaseID("validsignIn", 0);
		reader.
		int size = props.size();
		String[][] str =  new String[size/2][2];
		for(int i=0;i<size/2;i++){
			str[i][0] = props.getProperty("UserID"+i);
			str[i][1] = props.getProperty("Password"+i);
		}
		return str;
	}*/
	
	@BeforeMethod(groups={"Regression","Smoke"})
	public void setup(){
		driver = new FirefoxDriver();
		driver.get("http://www.gmail.com");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	@Test(groups={"Regression","Smoke"}, dataProvider="getusernameandpassword")
	public void validsignIn(String username, String password){
		SigninPage sigin = new SigninPage(driver);
		sigin.signIn(username, password);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='COMPOSE']")));
	}

	@Test(groups={"Regression"}, dataProvider="getusernameandpassword")
	public void InvalidsignIn(String username, String password) throws Exception{
		SigninPage sigin = new SigninPage(driver);
		sigin.signIn(username,password);
		try{
			driver.findElement(By.xpath("//div[text()='COMPOSE']"));
			//make the test fail
			throw new Exception("Login supposed to be failed. But we've got a success here...");
		}
		catch(NoSuchElementException e){
			System.out.println("Successfully verified invalid login..");
		}
	}
	@AfterMethod
	public void teardown(){
		driver.quit();
	}
	
	public static WebDriver getDriver(){
		return driver;
	}
}
