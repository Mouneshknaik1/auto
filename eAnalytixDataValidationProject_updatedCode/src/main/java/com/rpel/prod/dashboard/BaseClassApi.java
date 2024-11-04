package com.rpel.prod.dashboard;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.DataValidation.operations.PageOperations;


public class BaseClassApi {
	public static String eid="rpel";
	//	public static String env_prd="prd";
	public static String env_sit="sit";
	//	public static String env_ppd="ppd";

	public static String assettype="";
	public static String start_date="2024/09/22";
	public static String end_date="2024/09/23";
	
	public static String userid="andanagouda.patil@ril.com";
	public static String password="Rpel@1234";

	public static String adminuserid="jiotadmin@jio.io";
	public static String adminpassword="jeVaaPPaDm@41";
	public static String assetModelName="";
	public static String pfThresholdValue="";
	public static String areaOfStore="";
	public static String storename="";
	public static String storeDatpath="";
	public static String pricePerUnitist="";
	public static String store_dat_path="";
	public static String BearerToken="";
	public static String adminBearerToken="";
	public static String user_id="";
	public static String adminuser_id="";
	public static String get_current_time="";



	public static List<Double> import_value = null;
	public static Float totalimportValue=0.00000f;	
	public static Float totalexportValue=0.00000f;


	public static String sitename="RPEL";

	public static String sheet1_name="test";
	public static String sheet2_name="test";


	public static String apiKey="42e94c60-e79e-49b1-81dd-67f044550735";
	public static String prod_uri="https://energenie.jio.com/";
	public static String sit_uri="https://sit.energenie.jio.com/";
	//	public static String ppd_uri="https://pp.eanalytix.jvts.net/";

	public static String exec_uri=sit_uri;

	public static String base_uri=exec_uri+"accounts/api/users/";
	public static String login_api="v2/login";
	public static String uri_events=exec_uri+"accounts/api/events/v2/stats/billing/";
	public static String uri_generic_api=exec_uri+"accounts/api/";
	public static String uri_devicestatus=uri_generic_api+"devicestatuses/search";
	public static String base_event_uri=exec_uri+"accounts/api/events/v2/stats/";
	//	public static String generic_events_uri=exec_uri+"accounts/api/events/search";
	public static String generic_events_uri=exec_uri+"accounts/api/events/search";

	public static String generic_get_device=exec_uri+"accounts/api/devices/";


	public static String admin_login_uri = exec_uri+"accounts/api/adminusers/v2/login";
	public static String admin_assetModel_uri = exec_uri+"accounts/api/assettypemodels/search";
	public static String admin_assetModelBycode_uri = exec_uri+"accounts/api/assettypemodels/byCode";

	public static String assetCode="0";

	public SoftAssert sa; 
	public static String uri;
	public static String uri2;

	public static String start_time="18:30:00";
	public static String end_time="18:29:59";
	public static String from_date=start_date+" "+start_time;
	public static String to_date=end_date+" "+end_time;
	public static String to_date_energyCal="";
	public static String to_date_kpi=end_date+" "+start_time;
	public static String granularity="day";
	public static String from_date_in_milli="";
	public static String to_date_in_milli="";
	public static String to_date_in_milli1="";
	public static String from_date_in_milli_energyCalc="";
	public static String to_date_in_milli_energyCalc="";

	public static String kpi_to_date_in_milli="";
	public static String device_mac_id="";
	public static String device_asset_code="";
	public static List device_mac_id_list;
	public static List device_asset_code_list;
	public static List device_asset_imei_list;
	public static List device_deviceid_list;
	public static String deviceOnboarded="";



	public static float last_event_value=0.00000f;
	public static float first_event_value=0.00000f;
	public static List<Float> filteredVoltage;

	public static Workbook workbook;
	public static Workbook workbook1;
	public static Workbook workbook2;
	public static Sheet	sheet;
	public static Sheet	sheet1;
	public static Sheet	sheet2;

	public static String ec_ops_outputfile_rpel="rpel_output_file";

	public static String getDate="";
	public static String getTime="";
	public static String yesterdayDate="";
	public static String todaysDate="";
	public static String timeInMillis="";

	public static String yesterdayDate_start="";
	public static String yesterdayDate_end="";

	public static String day_90day_from="";

	public static String todaysDate_start="";
	public static String todaysDate_end="";
	public static String from_date_today=todaysDate_start+" "+start_time;
	public static String to_date_today=todaysDate_end+" "+end_time;
	public static String from_date_yesterday=yesterdayDate_start+" "+start_time;
	public static String to_date_yesterday=yesterdayDate_end+" "+end_time;

	public static String yesterdayDate_start_millis="";
	public static String yesterdayDate_end_millis="";

	public static String todaysDate_start_millis="";
	public static String todaysDate_end_millis="";

	public static String from_date_90day_avg_millis=todaysDate_start+" "+start_time;
	public static String to_date_90day_avg_millis=todaysDate_end+" "+end_time;;



	@BeforeSuite
	public void beforesuite()
	{
		create_xlsx_newtest(ec_ops_outputfile_rpel);
		enddate_in_milli(to_date);
		from_date_in_milli(from_date);
		kpi_enddate_in_milli(to_date_kpi);
		login();

	}



	//@Test
	//	public void test()
	//	{
	//		create_xlsx(ec_ops_outputfile);
	//	}

	public void setTimeout()
	{
		RestAssured.config = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().
				setParam("http.connection.timeout",300000).
				setParam("http.socket.timeout",300000).
				setParam("http.connection-manager.timeout",300000));
	}



	public void login()
	{

		String requestpayload = "{\"eid\":\""+eid+"\",\"email\":\""+userid+"\",\"type\":\"supervisor\",\"password\":\""+password+"\"}";
		System.out.println(requestpayload);
		System.out.println(base_uri+login_api);
		sa = new SoftAssert();
		Response response = given()

				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.body(requestpayload)
				.when()
				.post(base_uri+login_api)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("User login successful.", response.jsonPath().getString("message"));
		String token = response.jsonPath().getString("data.token");
		user_id = response.jsonPath().getString("data._id");
		///System.out.println(user_id);
		/*
		 * System.out.println("user_id:"+user_id);
		 * System.out.println(response.jsonPath().getString("message"));
		 * System.out.println(token);
		 */
		BearerToken= "Bearer "+ token;
		//System.out.println(BearerToken);
		sa.assertAll();  

	}



	public void get_path(String imei_no)
	{
		uri = base_uri+user_id+"/acls";
		sa = new SoftAssert();
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Eid",eid)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.when()
				.get(uri)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("ACLs retrieved successfully for user.", response.jsonPath().getString("message"));
		List lpath= response.jsonPath().getList("data.dat.path");
		List lname= response.jsonPath().getList("data.dat.name");
		List<String> currentDat= response.jsonPath().getList("data.currentDat");
		for(String s:currentDat)
		{
			System.out.println(s);
		}
		//currentDat
		for(int i =0;i<lname.size();i++)
		{
			String storename=lname.get(i).toString();
			if(imei_no.equalsIgnoreCase(storename) )
			{
				store_dat_path= lpath.get(i).toString();
				System.out.println(store_dat_path);
				break;
			}
		}
		sa.assertAll();
	}

	void from_date_in_milli(String from_date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();  
		long millis=0;
		String x="";
		try {
			date = formatter.parse(from_date);
			millis = date.getTime();
			x=Long.toString(millis);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		from_date_in_milli=x.substring(0,10).concat("000");
	}

	void enddate_in_milli(String to_date)
	{

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = new Date();  
		long millis=0;
		String x="";
		try {
			date = formatter.parse(to_date);
			millis = date.getTime();
			x=Long.toString(millis);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		to_date_in_milli=x.substring(0,10).concat("999");

	}

	void from_date_in_milli_energyCalc(String from_date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();  
		long millis=0;
		String x="";
		try {
			date = formatter.parse(from_date);
			millis = date.getTime();
			x=Long.toString(millis);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		from_date_in_milli_energyCalc=x.substring(0,10).concat("000");
		System.out.println(from_date_in_milli_energyCalc);
	}




	void kpi_enddate_in_milli(String date_kpi)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();  
		long millis=0;
		String x="";
		try {
			date = formatter.parse(to_date_kpi);
			millis = date.getTime();
			x=Long.toString(millis);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		kpi_to_date_in_milli=x.substring(0,10).concat("001");
		//System.out.println(kpi_to_date_in_milli);
	}


	public String get_device_id(String imei_id, String asset_type)
	{
		sa= new SoftAssert();
		//		String payload="{\"device\":{\"model\":\"\",\"name\":null,\"mac\":\""+imei_id+"\",\"tagId\":null,\"assetCode\":\""+asset_type+"\",\"created\":{\"from\":null,\"to\":null}},\"assetType\":{\"code\":\""+asset_type+"\"},\"flags\":{\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetType\":true},\"startsWith\":{\"datRegex\":\""+user_id+"\"}}";
		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true,\"isSmartBatteryStatus\":true},\"startsWith\":{\"datRegex\":\""+user_id+"\"}}";

		System.out.println(payload); 
		//System.out.println(uri_generic_api+uri_devicestatus);
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("Eid",eid)
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.body(payload)
				.when()
				.post(uri_generic_api+uri_devicestatus)
				.then()
				.extract().response();
		//System.out.println(response.prettyPrint());
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("Devicestatuses retrieved successfully.", response.jsonPath().getString("message"));
		List imei= response.jsonPath().getList("data.device.imei");
		List mac= response.jsonPath().getList("data.device.mac");
		System.out.println("imei : " +imei);
		System.out.println("mac : " +mac);
		for(int i =0;i<mac.size();i++)
		{
			String towername=imei.get(i).toString();
			System.out.println("towername : " +towername);
			if(towername.equalsIgnoreCase(imei_id))

			{
				device_mac_id= mac.get(i).toString();
				System.out.println("device_mac_id :"+device_mac_id);
				break;
			}
		}

		sa.assertAll();
		return device_mac_id;
	}



	public void renamereport() throws IOException
	{

		String replace=to_date.replace("/", "");
		try {
			String dir = System.getProperty("user.dir")+"\\RPELData\\uidir.bat";
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start","/B /wait", dir}); 
			System.out.println("Report is successfully generated.");
		}
		catch (Exception e) {
			{
				System.out.println("Report is not generated"); 

			}
			// TODO: handle exception
		}

	}



	public static String get_device_id(String imei_id)
	{
		SoftAssert sa= new SoftAssert();

		String device_id=null;
		//		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true},\"startsWith\":{\"datRegex\":\"\"},\"device\":{\"model\":\"\",\"name\":\"\",\"mac\":\""+imei_id+"\",\"assetCode\":\"\",\"created\":{\"from\":null,\"to\":null}},\"assetType\":{\"code\":\"\"}}";
		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true,\"isSmartBatteryStatus\":true},\"startsWith\":{\"datRegex\":\""+user_id+"\"}}";

		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("Eid",eid)
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.body(payload)
				.when()
				.post(uri_devicestatus)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		//System.out.println(response.statusCode());
		int count= response.jsonPath().getInt("count");
		if(count==0)
		{
			deviceOnboarded="No";
			System.out.println("No device found");

		}

		else
		{


			deviceOnboarded="Yes";
			sa.assertEquals("Devicestatuses retrieved successfully.", response.jsonPath().getString("message"));
			device_asset_imei_list= response.jsonPath().getList("data.device.imei");
			device_mac_id_list= response.jsonPath().getList("data.device.mac");
			device_asset_code_list= response.jsonPath().getList("data.assetType.code");
			//device_deviceid_list= response.jsonPath().getList("data.assetType.code");
			device_deviceid_list=response.jsonPath().getList("data.device._id");

			//System.out.println(imei);
			//System.out.println(mac);
			for(int i =0;i<device_mac_id_list.size();i++)
			{
				String towername=device_asset_imei_list.get(i).toString();
				if(towername.equalsIgnoreCase(imei_id))
				{
					device_mac_id= device_mac_id_list.get(i).toString();
					device_asset_code=device_asset_code_list.get(i).toString();
					device_id=device_deviceid_list.get(i).toString();
					//System.out.println(device_id);
					//System.out.println("device_mac_id :"+device_mac_id);
					//System.out.println("device_asset_code :"+device_asset_code);
					//break;
				}
			}


		}
		sa.assertAll();
		return device_id;
	}


	public void admin_login()
	{

		String requestpayload = "{\"email\":\""+adminuserid+"\",\"password\":\""+adminpassword+"\"}";
		//System.out.println(requestpayload);
		//System.out.println(admin_login_uri);
		sa = new SoftAssert();
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.body(requestpayload)
				.when()
				.post(admin_login_uri)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals(response.jsonPath().getString("message"),"User authentication successful.");
		String token = response.jsonPath().getString("data.admin_token");
		adminuser_id = response.jsonPath().getString("data._id");
		//System.out.println(response.jsonPath().getString("message"));
		//System.out.println(token);
		adminBearerToken= "Bearer "+ token;
		sa.assertAll();  

	}



	public static void create_xlsx_newtest(String opfile_name)
	{

		String excelPath=  System.getProperty("user.dir") + "/RPELData/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sitename);

		//		sheet1 = workbook.createSheet(sitename+"--Energy Dispensed");


		sheet.createRow(0);
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue("Site");

		sheet.getRow(0).createCell(1).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(1).setCellValue("Mac ID");

		sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(2).setCellValue("Total Imported energy");


		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue("Total Exported Energy");

		try {
			File file = new File(excelPath);
			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	


	public static void create_xlsx_energy_dispensed(String opfile_name)
	{

		String excelPath=  System.getProperty("user.dir") + "/EnergenieData/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sitename);

		sheet.createRow(0);
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue("Site");

		sheet.getRow(0).createCell(1).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(1).setCellValue("MeterId");

		sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(2).setCellValue("Total_Energy_Consumed(wh)");

		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue("Total_Energy_Consumed(kwh)");

		sheet.getRow(0).createCell(4).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(4).setCellValue("Energy_consumed_for_Discharging");


		try {
			File file = new File(excelPath);
			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	



	public static String millitohms(long millis)
	{
		/*
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
			    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
			    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
		 */
		Duration timeLeft = Duration.ofMillis(millis);
		String hms = String.format("%02d:%02d:%02d", 
				timeLeft.toHours(), timeLeft.toMinutesPart(), timeLeft.toSecondsPart());
		//System.out.println(hms);
		return hms;
	}



	@AfterSuite
	public void aftersuite() throws IOException
	{

		String OS =System.getProperty("os.name");
		ChromeOptions options = new ChromeOptions();
		if (OS.toLowerCase().contains("win"))
		{
			this.renamereport();
		}
		else
			PageOperations.rename_test_report("Out","");
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
		return x.substring(0,10).concat("999");
	}



	public void  getDeviceAssetCode(String macid)
	{
		String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+macid+"\"}";
		///String payload ="{\"from\":1692210600000,\"to\":1692296999999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\"PAOZtSlYysEfZgK4_1\"}";
		//System.out.println(payload);
		//System.out.println(jsonpath);
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("Eid",eid)
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.body(payload)
				.when()
				.post(generic_events_uri)
				.then()
				.extract().response();
		sa.assertEquals(response.statusCode(),200);
		List<Object> deviceList=response.getBody().jsonPath().getList("data.device");
		String deviceId="";
		try {
			System.out.println("deviceId :"+deviceId);
			deviceId=deviceList.get(0).toString();
			System.out.println("Device Id : "+deviceId);
			System.out.println(generic_get_device+deviceId);
		}
		catch(Exception e)
		{

		}
		getAssetCode(deviceId);
	}
	public void getAssetCode(String id)
	{


		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("Eid",eid)
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.get(generic_get_device+id)
				.then()
				.extract().response();
		sa.assertEquals(response.statusCode(),200);

		try {
			assetCode=response.getBody().jsonPath().getString("data.assetTypeModel.code");
			System.out.println("Asset code : "+assetCode);

		}
		catch(Exception e)
		{

		}
		sa.assertAll();		

	}





	public static String timetomillisecondsforEnergyCalc(String myDate) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();  
		long millis=0;
		String x="";
		try {
			date = formatter.parse(myDate);
			//System.out.println("date");
			millis = date.getTime();
			//System.out.println("millis:"+millis);
			x=Long.toString(millis);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return x.substring(0,10);
	}

	public static String istUtc(String date)
	{
		String formattedDate="";
		try {

			String strDate=date;
			String timeZone="GMT+05:30";
			String format = "yyyy/MM/dd HH:mm:ss";
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date dateStr = formatter.parse(strDate);
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			formattedDate = formatter.format(dateStr);
			//System.out.println("UTC datetime is: "+formattedDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return formattedDate;
	}


	public static void time(int days,int hour)
	{

		Date currentDate = new Date();

		//currentDate.setMinutes(0);
		//currentDate.setSeconds(0);
		Calendar cal = Calendar.getInstance();
		// remove next line if you're always using the current time.
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, days);
		cal.add(Calendar.HOUR, hour);
		Date currenTime = cal.getTime();
		//System.out.println(currenTime);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String strDate = formatter.format(currenTime);
		getTime=strDate.substring(strDate.length()-8, strDate.length());
		getDate=strDate.substring(0,strDate.length()-8);
		//System.out.println(strDate);
		//System.out.println(getTime);
		//System.out.println(getDate);
		//System.out.println(getCurrentUtcTime());
		//istUtc(strDate);
		//timeInMillis = time_to_milliseconds(istUtc(strDate));
		//System.out.println(timeInMillis);

	}


	public static void time(int days,int hour,int setmin)
	{
		Date currentDate = new Date();
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		Calendar cal = Calendar.getInstance();
		// remove next line if you're always using the current time.
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, days);
		cal.add(Calendar.HOUR, hour);
		Date currenTime = cal.getTime();
		//System.out.println(currenTime);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String strDate = formatter.format(currenTime);
		getTime=strDate.substring(strDate.length()-8, strDate.length());
		getDate=strDate.substring(0,strDate.length()-8);

	}



	// Method to convert epoch timestamps to IST format
	public static ArrayList<String> convertToIST(List<Long> filteredtimelist) {
		// Create a list to store the converted timestamps
		ArrayList<String> istTimestamps = new ArrayList<>();

		// Define the date format for IST
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// Set the timezone to IST (Indian Standard Time)
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

		// Convert each epoch timestamp to IST format
		for (Object epochObj  : filteredtimelist) {
			// Convert epoch to Date object
			long epoch = (long) epochObj;

			Date date = new Date(epoch);
			// Format the date to IST string and add to the list
			istTimestamps.add(sdf.format(date));
		}

		return istTimestamps;
	}

}


