package ExtentReports;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.ReporterType;

public class MyTestNgListener implements ITestListener{
	ExtentReports report = new ExtentReports("SeleniumTestAutomationResult.html");;
	ExtentTest test;
	@Override
	public void onFinish(ITestContext arg0) {
		System.out.println("Inside OnFinish method..");
		report.flush();
		report.close();
	}

	@Override
	public void onStart(ITestContext arg0) {
		System.out.println("Inside OnStart method..");
    }

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		System.out.println("Inside OnTestFailure method..");
		test.log(LogStatus.ERROR, arg0.getMethod().getMethodName()+" has been failed.");
		test.log(LogStatus.FAIL, arg0.getThrowable());
		WebDriver driver = SigininTests.getDriver();
		File scrfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String destfilename = arg0.getMethod().getMethodName()+System.currentTimeMillis()+".jpg";
		try {
			FileUtils.copyFile(scrfile, new File(destfilename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String screencapture = test.addScreenCapture(destfilename);
		test.log(LogStatus.FAIL, screencapture);
		report.endTest(test);
		//write the screenshot capture code here...
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		System.out.println("Inside OnTestSkipped method..");
		//test.log(LogStatus.WARNING, arg0.getTestName()+" has been skipped.");
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		System.out.println("Inside OnTestStart method..");
		test = report.startTest(arg0.getMethod().getMethodName());
		System.out.println("Current test name:"+arg0.getMethod().getMethodName());
		
		test.log(LogStatus.INFO, arg0.getMethod().getMethodName()+ "has been started..");
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		System.out.println("Inside OnTestSuccess method..");
		test.log(LogStatus.INFO, arg0.getMethod().getMethodName()+ "has been started..");
		report.endTest(test);
	}

}
