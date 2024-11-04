package com.rrlems.prd.dashboards;


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

public class RrlBaseClass {
	//public static String eid="rrlems";
	public static String eid="ems";
//	public static String env_prd="prd";
	public static String env_sit="sit";
//	public static String env_ppd="ppd";
	public static String assettype="";
	public static String start_date="2024/08/28";
	public static String end_date="2024/08/29";
	public static String start_date_budget="2023/08/28";
	public static String end_date_budget="2024/08/29";
	
	
	//public static String userid="madhu.hb@ril.com";
	//public static String password="Jio@1234";
	public static String userid="Madhusudhana.sv@ril.com";
	public static String password="Jio@1234";
	public static String adminuserid="jiotadmin@jio.io";
	public static String adminpassword="jeVaaPPaDm@41";
	public static String assetModelName="";
	public static String pfThresholdValue="";
	public static String areaOfStore="45000";
	public static String storename="";
	public static String storeDatpath="";
	public static String fuelTankCapacity="";
	public static String fuelPrice="";
	public static String pricePerUnitist="";
	public static String store_dat_path="";
	public static String BearerToken="";
	public static String adminBearerToken="";
	public static String user_id="";
	public static String adminuser_id="";
	public static String get_current_time="";

	public static String sitename="Powai";
	//public static String sitename="Ludo Mall";
	//public static String sitename="Mumbai";

	public static double avgTariff=8;
	public static String sheet1_name="test";
	public static String sheet2_name="test";
	
	public static int ac_count=20;

	public static String ac_filename="ac_outputfile";
	public static String dc_filename="dc_outputfile";
	public static String apiKey="42e94c60-e79e-49b1-81dd-67f044550735";
	public static String prod_uri="https://eanalytix.jvts.net/";
	public static String sit_uri="https://sit.jevaa.jvts.net/";
	public static String ppd_uri="https://pp.eanalytix.jvts.net/";

	public static String exec_uri=prod_uri;
	public static String base_uri=exec_uri+"accounts/api/users/";
	public static String login_api="v2/login";
	public static String uri_events=exec_uri+"accounts/api/events/v2/stats/billing/";
	public static String uri_generic_api=exec_uri+"accounts/api/";
	public static String uri_devicestatus=uri_generic_api+"devicestatuses/search";
	public static String base_event_uri=exec_uri+"accounts/api/events/v2/stats/";
	public static String generic_events_uri=exec_uri+"accounts/api/events/search/?limit=999";
	public static String generic_dashboard_budget=uri_generic_api+"events/v3/kpi/budget";

	public static String generic_get_device=exec_uri+"accounts/api/devices/";


	public static String admin_login_uri = exec_uri+"accounts/api/adminusers/v2/login";
	public static String admin_assetModel_uri = exec_uri+"accounts/api/assettypemodels/search";
	public static String admin_assetModelBycode_uri = exec_uri+"accounts/api/assettypemodels/byCode";

	public static String assetCode="0";

	public SoftAssert sa;
	public static String uri;
	public static String start_time="18:30:00";
	public static String end_time="18:29:59";
	public static String from_date=start_date+" "+start_time;
	public static String to_date=end_date+" "+end_time;
	public static String from_date_budget=start_date_budget+" "+start_time;
	public static String to_date_budget=end_date_budget+" "+end_time;
	
	public static String to_date_energyCal="";
	public static String to_date_kpi=end_date+" "+start_time;
	public static String granularity="day";
	public static String from_date_in_milli="";
	public static String to_date_in_milli="";
	public static String budget_from_date_in_milli="";
	public static String budget_to_date_in_milli="";
	
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

	public static Workbook workbook;
	public static Workbook workbook1;
	public static Workbook workbook2;
	public static Sheet	sheet;
	public static Sheet	sheet1;
	public static Sheet	sheet2;

	public static String ec_ops_outputfile_submeter="rrlems_submeter_outputfile";
	public static String ec_ops_outputfile_energyanalysis="rrlems_energyanalysis_outputfile";
	public static String ec_ops_outputfile_dg="rrlems_dg_outputfile";
	public static String ec_ops_kpioutputfile="kpi_ipcolo_outputfile";
	public static String firmware_outputfile="Firmwareverion_outputfile";
	public static double opshours = 0;	

	public static String admin_assetModelcode="";
	
	
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
		create_xlsx(ec_ops_outputfile_submeter);
		create_xlsx_energy_analysis(ec_ops_outputfile_energyanalysis);
		create_xlsx_dgMaintenance(ec_ops_outputfile_dg);
		//create_xlsx_avg(ec_ops_outputfile_avg);
		enddate_in_milli(to_date);
		from_date_in_milli(from_date);
		kpi_enddate_in_milli(to_date_kpi);
		
		budget_from_date_in_milli(from_date_budget);
		budget_enddate_in_milli(to_date_budget);
		energyComparissionDates();
		login();

	}

	//@Test
	public void test()
	{
	//	create_xlsx(ec_ops_outputfile);
	}
	
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
		//System.out.println(requestpayload);
		//System.out.println(base_uri+login_api);
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
				//System.out.println(store_dat_path);
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

	void budget_from_date_in_milli(String from_date)
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

		budget_from_date_in_milli=x.substring(0,10).concat("000");
	}

	void budget_enddate_in_milli(String to_date)
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

		budget_to_date_in_milli=x.substring(0,10).concat("999");

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
		String payload="{\"device\":{\"model\":\"\",\"name\":null,\"mac\":\""+imei_id+"\",\"tagId\":null,\"assetCode\":\""+asset_type+"\",\"created\":{\"from\":null,\"to\":null}},\"assetType\":{\"code\":\""+asset_type+"\"},\"flags\":{\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetType\":true},\"startsWith\":{\"datRegex\":\""+user_id+"\"}}";
		//System.out.println(payload); 
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
		//System.out.println(imei);
		//System.out.println(mac);
		for(int i =0;i<mac.size();i++)
		{
			String towername=imei.get(i).toString();
			if(towername.equalsIgnoreCase(imei_id))
			{
				device_mac_id= mac.get(i).toString();
				//System.out.println("device_mac_id :"+device_mac_id);
				break;
			}
		}

		sa.assertAll();
		return device_mac_id;
	}

	public static void get_event_values(List getlist)
	{

		if(getlist.isEmpty())
		{
			System.out.println("List is empty");
			last_event_value=0.00f;
			first_event_value=0.00f;

		}
		else
			try
		{
				List<Float> is = new ArrayList<>();
				while(getlist.remove(null))
				{
				}
				for(Object obj:getlist)
				{
					is.add((float)((Number) obj).floatValue());

				}

				if(is.isEmpty())
				{
					last_event_value=0.00f;
					first_event_value=0.00f;

				}
				else
				{
					last_event_value=is.get(0);
					first_event_value=is.get(is.size()-1);
				}
				//System.out.println(last_event_value);
				//System.out.println(first_event_value);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}


	public static void update_to_excel(String opfile_name,int rownum,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,String value1,String value2,String value3,double value4,double value5,double value6,String value7)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/MdgData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);

			//int rowCount = sheet.getLastRowNum();
			//System.out.println("rowCount:"+rowCount);
			//sheet.getRow(rownum).createCell(cellnum1).setCellType(CellType.STRING);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(value1);
			//sheet.getRow(rownum).createCell(cellnum2).setCellType(CellType.STRING);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(value2);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(value3);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(value4);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(value5);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(value6);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(value7);
			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void renamereport() throws IOException
	{

		String replace=to_date.replace("/", "");
		try {
			String dir = System.getProperty("user.dir")+"\\RRLEMSData\\uidir.bat";
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
	
	//@Test
	public void test111()
	{
	String s=get_device_id("I-AS-BGGN-JCO-0001");
	System.out.println(s);
	}
	
	public static String get_device_id(String imei_id)
	{
		SoftAssert sa= new SoftAssert();
	
		String device_id=null;
		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true},\"startsWith\":{\"datRegex\":\"\"},\"device\":{\"model\":\"\",\"name\":\"\",\"mac\":\""+imei_id+"\",\"assetCode\":\"\",\"created\":{\"from\":null,\"to\":null}},\"assetType\":{\"code\":\"\"}}";
		//System.out.println(payload);
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
		//System.out.println(response.jsonPath().getInt("count"));
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
			//System.out.println(device_asset_imei_list);
			//System.out.println(device_mac_id_list);
			for(int i =0;i<device_mac_id_list.size();i++)
			{
				String name=device_mac_id_list.get(i).toString();
				if(name.equalsIgnoreCase(imei_id))
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


	public static void create_xlsx(String opfile_name)
	{

		String excelPath=  System.getProperty("user.dir") + "/RRLEMSData/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sitename);
		sheet.createRow(0);
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue("StoreName");
		
		sheet.getRow(0).createCell(1).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(1).setCellValue("main_energy");

		sheet.getRow(0).createCell(2).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(2).setCellValue("main_avg_powerfactor");

		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue("mains_avg_PowerLoad");

		sheet.getRow(0).createCell(4).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(4).setCellValue("mains_apparentEnergy ");

		sheet.getRow(0).createCell(5).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(5).setCellValue("hvac_energy");

		sheet.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(6).setCellValue("hvac_avg_powerfactor");

		sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(7).setCellValue("hvac_avg_PowerLoad");

		sheet.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(8).setCellValue("lighting_energy");

		sheet.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(9).setCellValue("lighting_avg_powerfactor");

		sheet.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(10).setCellValue("lift_avg_PowerLoad");

		sheet.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(11).setCellValue("power_energy");

		sheet.getRow(0).createCell(12).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(12).setCellValue("power_avg_powerfactor");

		sheet.getRow(0).createCell(13).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(13).setCellValue("power_avg_PowerLoad");

		
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

										
	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,int cellnum10,int cellnum11,int cellnum12,int cellnum13,String site, double main_energy, double main_avg_powerfactor,double mains_avg_PowerLoad,double mains_apparentEnergy,double hvac_energy,double hvac_avg_powerfactor,double hvac_avg_PowerLoad,double lighting_energy,double lighting_avg_powerfactor,double lighting_avg_PowerLoad, double power_energy,double power_avg_powerfactor,double power_avg_PowerLoad)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/RRLEMSData/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(main_energy);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(main_avg_powerfactor);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(mains_avg_PowerLoad);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(mains_apparentEnergy);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(hvac_energy);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(hvac_avg_powerfactor);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(hvac_avg_PowerLoad);
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(lighting_energy);
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(lighting_avg_powerfactor);
			sheet.getRow(rownum).createCell(cellnum10).setCellValue(lighting_avg_PowerLoad);
			sheet.getRow(rownum).createCell(cellnum11).setCellValue(power_energy);
			sheet.getRow(rownum).createCell(cellnum12).setCellValue(power_avg_powerfactor);
			sheet.getRow(rownum).createCell(cellnum13).setCellValue(power_avg_PowerLoad);
			
			
			
			
			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,int cellnum10,String site,double energyGenerated,double energyUnAccounted,double energyAccounted,double billedPF,double co2emission,double carbonEmissionsqft,double enpi, double allocatedBudget,double utilizeddBudget,double utilizeddBudgetpercent)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/RRLEMSData/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(energyGenerated);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(energyUnAccounted);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(energyAccounted);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(billedPF);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(co2emission);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(carbonEmissionsqft);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(enpi);
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(allocatedBudget);
					
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(utilizeddBudget);
			sheet.getRow(rownum).createCell(cellnum10).setCellValue(utilizeddBudgetpercent);	
			
			FileOutputStream fos = new FileOutputStream(excelPath);
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

	public static double sumofEnergyRunHr(double chanel1,double chanel2,double chanel3,double chanel4)
	{
		double sum=0;
		try {
			sum=chanel1+chanel2+chanel3+chanel4;
		}
		catch (Exception e) {
			// TODO: handle exception
			sum=0;
		}
		return sum;
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

	

	public static void create_xlsx_energy_analysis(String opfile_name)
	{

		String excelPath=  System.getProperty("user.dir") + "/RRLEMSData/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sitename);
		sheet.createRow(0);
		
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue("StoreName");
		
		sheet.getRow(0).createCell(1).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(1).setCellValue("energyGenerated");

		sheet.getRow(0).createCell(2).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(2).setCellValue("energyUnAccounted");

		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue("energyAccounted");
		
		sheet.getRow(0).createCell(4).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(4).setCellValue("billedpf");

		sheet.getRow(0).createCell(5).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(5).setCellValue("co2emission");

		sheet.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(6).setCellValue("carbonEmissionsqft");

		sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(7).setCellValue("enpi");

		sheet.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(8).setCellValue("allocatedBudget");

		sheet.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(9).setCellValue("utilizeddBudget");
			
		sheet.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(10).setCellValue("utilizeddBudgetpercent");
		
		
		
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

	public static void create_xlsx_dgMaintenance(String opfile_name)
	{

		String excelPath=  System.getProperty("user.dir") + "/RRLEMSData/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sitename);
		sheet.createRow(0);
		
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue("StoreName");
		
		sheet.getRow(0).createCell(1).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(1).setCellValue("energyGenerated");

		sheet.getRow(0).createCell(2).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(2).setCellValue("enpi");
		
		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue("dgc_event_ops_hour");

		sheet.getRow(0).createCell(4).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(4).setCellValue("event_fuel_fill_count");

		sheet.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(5).setCellValue("event_total_fuel_filled");

		sheet.getRow(0).createCell(6).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(6).setCellValue("event_total_fuel_removed");

		sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(7).setCellValue("start_fuel_in_liters");

		sheet.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(8).setCellValue("end_fuel_in_liters");

		sheet.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(9).setCellValue("total_fuelConsumed_site");

		sheet.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(10).setCellValue("dg_efficiency");
			
		sheet.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(11).setCellValue("co2emission");
		
		sheet.getRow(0).createCell(12).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(12).setCellValue("co2persqft");
		
		sheet.getRow(0).createCell(13).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(13).setCellValue("dg_opertionalhours");
		
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
			//System.out.println("deviceId :"+deviceId);
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
			//System.out.println("Asset code : "+assetCode);

		}
		catch(Exception e)
		{

		}
		sa.assertAll();		

	}

	
		
	public void getStoredat(String storename)
	{

		sa= new SoftAssert();
		uri = base_uri+user_id+"/acls";
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("Eid",eid)
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.when()
				.get(uri)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("ACLs retrieved successfully for user.", response.jsonPath().getString("message"));
		List isStore= response.jsonPath().getList("data.dat.metadata.isStore");
		//System.out.println(isStore);
		List storeName= response.jsonPath().getList("data.dat.name");
		List storeArea= response.jsonPath().getList("data.dat.metadata.areaOfStore");
		List fuelTankCapacitylist=response.jsonPath().getList("data.dat.metadata.fuelTankCapacity");
		List fuelPricelist=response.jsonPath().getList("data.dat.metadata.fuelPrice");
		List pricePerUnitlist=response.jsonPath().getList("data.dat.metadata.tariff_details.pricePerUnit");

		List pfThresholdValuelist= response.jsonPath().getList("data.dat.metadata.pfThresholdValue");
		List workinghrsFromList=response.jsonPath().getList("dat.metadata.workingHours.from");
		//System.out.println(workinghrsFromList);
		List workinghrsToList=response.jsonPath().getList("data.dat.metadata.workingHours.to");
		//System.out.println(workinghrsToList);
		List workingDaysList=response.jsonPath().getList("data.dat.metadata.workingDays");
		//System.out.println(workingDaysList);

		//System.out.println(storeName);
		List storedatPath= response.jsonPath().getList("data.dat.path");
		//System.out.println(storedatPath);
		for(int i =0;i<isStore.size();i++)
		{
			String isstore=isStore.get(i).toString();
			//if(isstore.equalsIgnoreCase("false"))
			//{
			if(storeName.get(i).toString().equalsIgnoreCase(storename))
			{
				storeDatpath= storedatPath.get(i).toString();
				areaOfStore=storeArea.get(i).toString();
				pfThresholdValue=pfThresholdValuelist.get(i).toString();
				
				//fuelTankCapacity=fuelTankCapacitylist.get(i).toString();
				//fuelPrice=fuelPricelist.get(i).toString();
				//pricePerUnitist=pricePerUnitlist.get(i).toString();
				//workingHrsFrom=workinghrsFromList.get(i).toString();
				//workingHrsTo=workinghrsToList.get(i).toString();
				//workingDays=workingDaysList.get(i).toString();
				//System.out.println(workingHrsFrom);
				//System.out.println(workingHrsTo);
				//System.out.println(workingDays);
				//System.out.println("storeDatpath : "+storeDatpath);
				System.out.println("areaOfStore : "+areaOfStore);
				System.out.println("pfThresholdValue : "+pfThresholdValue);
				//System.out.println("fuelTankCapacity : "+fuelTankCapacity);
				//System.out.println("fuelPrice : "+fuelPrice);
				//System.out.println("pricePerUnitist : "+pricePerUnitist);

				break;

			}
			//}

		}
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



public void energyComparissionDates()
{
	
		
	time(-2,-0);
	from_date_yesterday=getDate+" "+start_time;
	timeInMillis = timetomillisecondsforEnergyCalc(from_date_yesterday);
	//System.out.println(from_date_yesterday);
	yesterdayDate_start_millis=timeInMillis+"000";
	//System.out.println("yesterdayDate_start_millis : "+yesterdayDate_start_millis);
	

	time(-1,-0,0);
	to_date_yesterday=getDate+" "+getTime;
	//System.out.println("to_date_yesterday:"+to_date_yesterday);
	String time=istUtc(to_date_yesterday);
	//System.out.println("time :"+time);
	timeInMillis=timetomillisecondsforEnergyCalc(time);
	yesterdayDate_end_millis=timeInMillis+"999";
	//System.out.println("yesterdayDate_end_millis : "+yesterdayDate_end_millis);
	
	 
	time(-1,-0);
	from_date_today=getDate+" "+start_time;
	timeInMillis = timetomillisecondsforEnergyCalc(from_date_today);
	//System.out.println(from_date_today);
	todaysDate_start_millis=timeInMillis+"000";
	//System.out.println("todaysDate_start_millis : "+todaysDate_start_millis);
	
	time(0,-0,0);
	to_date_today=getDate+" "+getTime;
	//System.out.println("to_date_today :"+to_date_today);
	time=istUtc(to_date_today);
	timeInMillis = timetomillisecondsforEnergyCalc(time);
	//System.out.println(to_date_today);
	todaysDate_end_millis=timeInMillis+"999";
	//System.out.println("todaysDate_end_millis : "+todaysDate_end_millis);
	
}


public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,String name,double dgenergy,double enpi,double opshour)
{
	try {	
		String excelPath=  System.getProperty("user.dir") + "/RRLEMSData/"+opfile_name+".xlsx";
		FileInputStream file= new FileInputStream(excelPath);
		workbook = new XSSFWorkbook(file);
		sheet = workbook.getSheet(sitename);
		sheet.createRow(rownum);
		sheet.getRow(rownum).createCell(cellnum0).setCellValue(name);
		sheet.getRow(rownum).createCell(cellnum1).setCellValue(dgenergy);
		sheet.getRow(rownum).createCell(cellnum2).setCellValue(enpi);
		sheet.getRow(rownum).createCell(cellnum3).setCellValue(opshour);
		FileOutputStream fos = new FileOutputStream(excelPath);
		workbook.write(fos);
		workbook.close();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}

public static void update_to_excel(String opfile_name,int rownum,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,int cellnum10, double event_fuel_fill_count,double event_total_fuel_filled,double event_total_fuel_removed,String start_fuel_in_liters,String end_fuel_in_liters,double total_fuelConsumed_site,double dg_efficiency,double co2emission,double co2persqft,String dg_events_hms)
{
	try {	
		String excelPath=  System.getProperty("user.dir") + "/RRLEMSData/"+opfile_name+".xlsx";
		FileInputStream file= new FileInputStream(excelPath);
		workbook = new XSSFWorkbook(file);
		sheet = workbook.getSheet(sitename);

		sheet.getRow(rownum).createCell(cellnum1).setCellValue(event_fuel_fill_count);
		sheet.getRow(rownum).createCell(cellnum2).setCellValue(event_total_fuel_filled);
		sheet.getRow(rownum).createCell(cellnum3).setCellValue(event_total_fuel_removed);
		sheet.getRow(rownum).createCell(cellnum4).setCellValue(start_fuel_in_liters);
		sheet.getRow(rownum).createCell(cellnum5).setCellValue(end_fuel_in_liters);
		sheet.getRow(rownum).createCell(cellnum6).setCellValue(total_fuelConsumed_site);
		sheet.getRow(rownum).createCell(cellnum7).setCellValue(dg_efficiency);
		sheet.getRow(rownum).createCell(cellnum8).setCellValue(co2emission);
		sheet.getRow(rownum).createCell(cellnum9).setCellValue(co2persqft);
		sheet.getRow(rownum).createCell(cellnum10).setCellValue(dg_events_hms);
				
		FileOutputStream fos = new FileOutputStream(excelPath);
		workbook.write(fos);
		workbook.close();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}

									

}


