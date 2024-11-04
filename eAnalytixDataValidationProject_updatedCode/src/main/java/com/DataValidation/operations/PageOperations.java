package com.DataValidation.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil.Test;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.ui.Select;

import net.bytebuddy.description.type.TypeDescription.Generic;
import net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection;

public class PageOperations {

	private static WebDriver driver;

	private static Actions action;
	private static WebDriverWait wait;
	public static String startdate="";
	public static String enddate="";

	public static String startdate_time="T18:30:00.000Z";
	public static String enddate_time="T18:29:59.999Z";

	public static String startdate_time1="18:30:00";
	public static String enddate_time1="18:29:59";

	public static String start_date_inmilli="";
	public static String end_date_inmilli="";

	public static void cancelCaptcha() throws Exception {
		action = new Actions(driver);
		wait = new WebDriverWait(driver, 30);
		List<WebElement> captcha= driver.findElements(By.xpath("//iframe"));
		for(int i=0;i<=captcha.size(); i++)
		{
			Thread.sleep(1000);
			wait.until(ExpectedConditions.visibilityOf(captcha.get(i)));
			action.sendKeys(Keys.ESCAPE).build().perform();
		}
	}

	public WebElement find_text_by_normalize_space(String tag, String value)
	{
		WebElement element= driver.findElement(By.xpath("//"+tag+"[normalize-space()='"+value+"']"));
		return element;
	}

	public WebElement find_text_by_contains_text(String tag, String value)
	{
		WebElement element= driver.findElement(By.xpath("//"+tag+"[contains(text(),'"+value+"')]"));
		return element;
	}

	public WebElement find_text_by_contains_text_sibling(String tag, String value)
	{
		WebElement element= driver.findElement(By.xpath("//"+tag+"[contains(text(),'"+value+"')] //following-sibling::"+tag+""));
		return element;
	}

	public String formatting(float number)
	{
		String formattednumber=null;
		try {
			if(number!=0)
			{
				String pattern = "##,##,##,###.##";
				DecimalFormat numberFormat = new DecimalFormat(pattern);
				System.out.println(number);
				formattednumber = numberFormat.format(number);
				}
			else
			{
				formattednumber="0";
				}
		
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}

		return formattednumber;
	}

	public int roundoffunits(String units)
	{
		int roundedoffvalue=0;
		if(units!= null)
		{
			float stringfloat = Float.parseFloat(units);
			roundedoffvalue= (int)Math.ceil(stringfloat);
			return roundedoffvalue;

		}
		else {
			roundedoffvalue= 0;
			return roundedoffvalue; 

		}

	}

	public int roundoffunits(float units)
	{
		int roundedoffvalue=0;
		if(units!= 0.0)
		{
			float stringfloat = units;
			roundedoffvalue= (int)Math.ceil(stringfloat);
			return roundedoffvalue;

		}
		else {
			roundedoffvalue= 0;
			return roundedoffvalue; 

		}

	}

	
	
	public String roundoff_to_two_decimal(String units)
	{
		String roundedoffvalue="";
		if(units!= null)
		{
			DecimalFormat df = new DecimalFormat();
			double stringfloat = Double.parseDouble(units);
			df.setMaximumFractionDigits(2);
			roundedoffvalue = df.format(stringfloat);
			return roundedoffvalue;
					}
		else {
			roundedoffvalue= "0.00";
			return roundedoffvalue; 

		}
		
	}
	
	public String roundoff_to_two_decimal(double units)
	{
		String roundedoffvalue="";
		if(units!= 0.00)
		{
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			roundedoffvalue = df.format(units);
			return roundedoffvalue;
					}
		else {
			roundedoffvalue= "0.00";
			return roundedoffvalue; 

		}
		
	}
	
	
	
	public String roundoff_to_single_decimal(String units)
	{
		String roundedoffvalue="";
		if(units!= null)
		{
			DecimalFormat df = new DecimalFormat();
			double stringfloat = Double.parseDouble(units);
			df.setMaximumFractionDigits(1);
			roundedoffvalue = df.format(stringfloat);
			return roundedoffvalue;
					}
		else {
			roundedoffvalue= "0.00";
			return roundedoffvalue; 

		}
		
	}
	

	public PageOperations(WebDriver driver) {
		this.driver = driver;
	}


	public static WebDriver getDriver()
	{
		return driver;
	}

	

	public static void pagerefresh() {
		driver.navigate().refresh();
	}

	public static void highlightElement(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
				"background:#ADD8E6; border:2px solid red;");
		try {
			Thread.sleep(250);
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void moveToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		highlightElement(driver, element);

	}

	public static void scrollToTop() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight,0)");
	}

	public static void scrollToBottom() {
		((JavascriptExecutor)

				driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
	}


	public static void scrolltoMiddle()
	{
		((JavascriptExecutor)driver).executeScript("window.scrollBy(0,600)");
	}

	public void implicitwait() {
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}


	public static void dropDown(WebElement element, String text){

		Select WebElement = new Select(element);
		WebElement.selectByVisibleText(text);
	}

	public static void selectall_backspace_sendvalue(WebElement element, String value) throws Exception
	{

		try{
			element.sendKeys(Keys.CONTROL + "a");
			element.sendKeys(Keys.DELETE);
			element.sendKeys(value);
		}
		catch(Exception NoSuchElementException)
		{
			return;
		}
	}

	public static void hit_escape_key(WebElement element) throws Exception
	{

		try{
			element.sendKeys(Keys.TAB);
		}
		catch(Exception NoSuchElementException)
		{
			return;
		}
	}

	public static void sendTexttoElement(WebElement element, String text) {
		element.sendKeys(text);
	}


	public static void waitTillElementisVisible(WebElement elememnt)
	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5000);
			wait.until(ExpectedConditions.visibilityOf(elememnt));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Element is not visible");
		}
	}

	public static void waitTillElementisVisible1(WebElement elememnt)
	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5000);
			wait.until(ExpectedConditions.visibilityOf(elememnt));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Element is not visible");
		}
	}

	
	
	
	public static void waitTillElementisclickable(WebElement elememnt) throws Exception
	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5000);
			wait.until(ExpectedConditions.elementToBeClickable(elememnt));
		}
		catch(Exception e)
		{
			e.printStackTrace();

		}
	}


	public static void wait_and_switch_to_iframe(WebElement iframeid)
	{
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeid));
	}


	public static String getTextFromWebElement(WebElement element) {
		moveToElement(element);
		String text = element.getText();
		return text;
	}

	public static void clickOnElement(WebElement element) {
		moveToElement(element);
		element.click();
	}

	public static boolean isDisplayedElement(WebElement element) throws Exception {
		try{
			moveToElement(element);
			return element.isDisplayed();
		}
		catch(Exception NoSuchElementException)
		{
			return false;
		}
	}

	public static void sendTextElement(WebElement element, String text) {
		moveToElement(element);
		element.sendKeys(text);
	}

	public static void clearTextElement(WebElement element) {
		moveToElement(element);
		element.clear();
	}

	public static ArrayList<String> getTextFromListOfWebElements(List<WebElement> elements) {

		ArrayList<String> listOfText = new ArrayList<String>();
		Iterator iterator = elements.iterator();
		while (iterator.hasNext()) {
			WebElement ele = (WebElement) iterator.next();
			moveToElement(ele);
			String text = ele.getText();
			listOfText.add(text);
		}
		return listOfText;
	}

	public static String getProperty(String filePath, String key) {
		String value = "";

		try {
			FileInputStream fin = new FileInputStream(filePath);
			//			InputStream fin = Test.class.getClassLoader().getResourceAsStream(filePath);
			Properties pro = new Properties();
			pro.load(fin);
			value = pro.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void captureScreenshot(String fileName, String path) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmm");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println(dtf.format(now));

		TakesScreenshot ss = ((TakesScreenshot) PageOperations.driver);
		File sourceFile = ss.getScreenshotAs(OutputType.FILE);

		String filePath = path + fileName + dtf.format(now) + ".png";

		File destFile = new File(filePath);
		try {
			FileUtils.copyFile(sourceFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String gettoday()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		String date = dateFormat.format(today).substring(8, 10);
		System.out.println(date);
		if(date.startsWith("0"))
		{
			String selectdate = dateFormat.format(today).substring(9,10);

			return selectdate;
		}
		else
		{
			return date;		   
		}}

	public static String formatted_todaysdate()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		String date = dateFormat.format(today);
		return date;
	}





	public static String getpreviousyear()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.YEAR, -1);
		Date today = calendar.getTime();
		String date = dateFormat.format(today).substring(0, 4);
		System.out.println(date);
		return date;
	}

	public static String getpreviousmonth()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.MONTH, -1);
		Date today = calendar.getTime();
		String date = dateFormat.format(today).substring(5, 7);
		if(date.startsWith("0"))
		{
			String selectdate = dateFormat.format(today).substring(6,7);
			return selectdate;
		}
		else
		{
			return date;
		}
	}

	public static String getpagetitle()
	{
		return driver.getTitle();
	}

	public static String getmonthname(String date)
	{
		int monthnumber=Integer.parseInt(date);
		--monthnumber;
		String month[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
		return month[monthnumber];
	}


	public static String getcustomizeddate(int d, int m, int y)
	{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -d);
		calendar.add(calendar.MONTH, -m);
		calendar.add(calendar.YEAR, -y);
		Date yesterday = calendar.getTime();
		String selectdate = dateFormat.format(yesterday);
		return selectdate;
	}

	public static String getcustomizeddateformatted(int d, int m, int y)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -d);
		calendar.add(calendar.MONTH, -m);
		calendar.add(calendar.YEAR, -y);
		Date yesterday = calendar.getTime();
		String selectdate = dateFormat.format(yesterday);
		return selectdate;
	}


	public static String getLastdayofMonth(int n)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -n);
		String date1= format.format(cal.getTime());
		int lastday = cal.getActualMaximum(cal.DAY_OF_MONTH);
		String lastdayofmonth=date1.substring(0,8)+lastday;
		return lastdayofmonth;
	}


	public static String getpreviousday()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, -1);
		Date today = calendar.getTime();
		String date = dateFormat.format(today).substring(8, 10);
		System.out.println(date);
		if(date.startsWith("0"))
		{
			String selectdate = dateFormat.format(today).substring(9,10);

			return selectdate;
		}
		else
		{
			return date;		   
		}
	}

	public static String getcustomizeddatewithslash(int d, int m, int y)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -d);
		calendar.add(calendar.MONTH, -m);
		calendar.add(calendar.YEAR, -y);
		Date yesterday = calendar.getTime();
		String selectdate = dateFormat.format(yesterday);
		return selectdate;
	}



	public static String time_to_milliseconds(String myDate) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		//formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		//formatter.setTimeZone(TimeZone.getDefault());
		Date date = new Date();  
		long millis=0;
		String x="";
		try {
			date = formatter.parse(myDate);
			millis = date.getTime();
			x=Long.toString(millis);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return x.substring(0,10);
	}

	static void waitForAjaxToFinish() {

		WebDriverWait wait = new WebDriverWait(driver, 5000);
		wait.until(new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver wdriver) {
				return ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0").equals(true);
			}

		});
	}


	public void untilAngularFinishHttpCalls() {
		final String javaScriptToLoadAngular = "var injector = window.angular.element('body').injector();"
				+ "var $http = injector.get('$http');" + "return ($http.pendingRequests.length === 0)";

		ExpectedCondition<Boolean> pendingHttpCallsCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(javaScriptToLoadAngular).equals(true);
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 20); // timeout = 20 secs
		wait.until(pendingHttpCallsCondition);
	}

	public Sheet getSheet(String sheetName, String fileName, String filePath) {
		Workbook wb = null;
		Sheet sheet = null;
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// checking whether it is xls or xlsx file
		String extension[] = fileName.split("\\.");
		String tmp = extension[1];
		if(tmp.equalsIgnoreCase("xlsx")) {
			System.out.println("Given file is of xlsx format");
			try {
				wb = new XSSFWorkbook(fin);
				sheet = wb.getSheet(sheetName);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				wb = new HSSFWorkbook(fin);
				sheet = wb.getSheet(sheetName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sheet;
	}


	public static String meterid()
	{
		int random =( int )( Math. random() * 999999999);
		String meter = "RN"+random;
		return meter;
	}

	public static Float get_sum_of_list_elements(List jsondata)
	{ 
		Float total=0.0f;
		List<Float> is = new ArrayList<>();
		while(jsondata.remove(null))
		{
		}
		for(Object obj:jsondata)
		{
			is.add((float)((Number) obj).floatValue());

		}

		for(int i=0;i<is.size();i++)
		{
			total=total+is.get(i);
		}
		return total;
	}
	
	public static List<Float> get_list_of_elements(List jsondata)
	{ 
		List<Float> is = new ArrayList<>();
		for(Object obj:jsondata)
		{
			is.add((float)((Number) obj).floatValue());

		}

		for(int i=0;i<is.size();i++)
		{
			is.get(i);
		}
		return is;
	}
	
	
	
	

	public static void rename_test_report(String filename, String path)
	{
		LocalDateTime now = LocalDateTime.now();
		//System.out.println("Current DateTime Before Formatting: " + now);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		String formatedDateTime = now.format(formatter);
		System.out.println(formatedDateTime);
		String filepath=System.getProperty("user.dir") + "/test-output/emailable-report.html";
		Path source = Paths.get(filepath);
		try {
			Thread.sleep(10000);
			Files.move(source, source.resolveSibling(filename+formatedDateTime+".html"));
			System.out.println("Test Report Generated Successfully");

		}
		catch (Exception NoSuchFileException) {
			System.out.println("File rename failed due to no such file exists");
			// TODO: handle exception
		}
	}

	public static String startdate_in_milli(int day, int month,int year)
	{	
		startdate=PageOperations.getcustomizeddatewithslash(day, month, year)+" "+startdate_time1;
		start_date_inmilli=PageOperations.time_to_milliseconds(startdate);
		return start_date_inmilli;
		
	}

	public static String enddate_in_milli(int day, int month,int year)
	{
		enddate=PageOperations.getcustomizeddatewithslash(day, month, year)+" "+enddate_time1;
		end_date_inmilli=PageOperations.time_to_milliseconds(enddate);
		return end_date_inmilli;
	}

	public static String payload_body_in_milliseconds(int sday, int smonth,int syear, int eday, int emonth,int eyear) throws Exception
	{
		String payload_body = "{\"fromDate\":"+startdate_in_milli(sday, smonth, syear)+","+"\"toDate\":"+enddate_in_milli(eday, emonth, eyear)+"}";
		return payload_body;
		

	}
	
	

	public void waitForPagetoLoad() {

		WebDriverWait wait = new WebDriverWait(driver, 30);
		JavascriptExecutor executor = (JavascriptExecutor) driver;

		// Using below readyState will wait till page load
		ExpectedCondition<Boolean> pageLoadCondition = new
				ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
			}
		};
		wait.until(pageLoadCondition);

		// Below JQuery will wait till data has not been loaded
		int count = 0;
		if ((Boolean) executor.executeScript("return window.jQuery != undefined")) {
			while (!(Boolean) executor.executeScript("return jQuery.active == 0")) {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (count > 4)
					break;
				count++;
			}
		}
	}
}
