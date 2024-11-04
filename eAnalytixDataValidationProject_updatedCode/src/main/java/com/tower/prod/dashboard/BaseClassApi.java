package com.tower.prod.dashboard;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
//	public static String eid_oldinfra="rlwtower";
	public static String eid_newinfra="jiotower";
	public static String eid=eid_newinfra;
	public static String env_prd="prd";
//	public static String env_sit="sit";
	public static String env_sit_uri="https://sit.eanalytix.jio.com/";
//	public static String env_prod_olduri="https://eanalytix.jvts.net/";
//	public static String env_prod_newuri="https://eanalytix.jio.com/";
	public static String eid_uri=env_sit_uri;
	public static String sit_input_tower_sheet="sit_towerid_input.xlsx";
//	public static String prod_input_tower_sheet="prd_towerid_input.xlsx";
	public static String inputsheet=sit_input_tower_sheet;
	public static String assettype="";
	
//	public static String userid="aishwarya.mr@ril.com";
	public static String userid="poonam.chauhan@ril.com";
	public static String password="P00n@m@01";

//	public static String password="Jio@1234";
	public static String store_dat_path="";
	public static String BearerToken="";
	public static String user_id="";
	public static String sheet_name="oct";
//	public static String sheet_name="Naveen";

	//public static String sheet_name="Test1";
//	public static String imei_no="I-GJ-UPLT-ENB-9026";

	public static String apiKey="42e94c60-e79e-49b1-81dd-67f044550735";
	public static String base_uri=eid_uri+"accounts/api/users/";
	public static String login_api="v2/login";
	public static String uri_events=eid_uri+"accounts/api/events/v2/stats/billing/";
	public static String uri_generic_api=eid_uri+"accounts/api/";
	public static String uri_devicestatus="devicestatuses/search";
	public SoftAssert sa;
	public static String uri;
	public static String start_time="18:30:00";
	public static String end_time="18:29:59";
	public static String start_date="2024/08/24";
	public static String end_date="2024/08/25";
	
	public static String from_date=start_date+" "+start_time;
	public static String to_date=end_date+" "+end_time;
	public static String to_date_kpi=end_date+" "+start_time;
	public static String granularity="day";
	public static String from_date_in_milli="";
	public static String to_date_in_milli="";
	public static String kpi_to_date_in_milli="";
	public static String device_mac_id="";
	public static String device_asset_code="";
	public static List device_mac_id_list;
	public static List device_asset_code_list;
	public static List device_asset_imei_list;
	public static String deviceOnboarded="";
	public static String sheetname="";

	public static float last_event_value=0.00000f;
	public static float first_event_value=0.00000f;

	public static Workbook workbook;
	public static Sheet	sheet;

	public static String ec_ops_outputfile="Tower_Ec_Ops_outputfile";
	public static String tower_inputfile="tower_inputfile";
	public static String firmware_outputfile="Firmwareverion_outputfile";

	@BeforeSuite
	public void beforesuite()
	{
		sheetname();
		create_xlsx();
		create_xlsx(firmware_outputfile, "Tower_id", "Total_Record", "Mismatch", "Match", "Status");
		enddate_in_milli(to_date);
		from_date_in_milli(from_date);
		kpi_enddate_in_milli(to_date_kpi);
		login();

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
		sa = new SoftAssert();
		Response response =  given()
				.and()
				.config(RestAssured.config)
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.relaxedHTTPSValidation()
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
		/*
		 * System.out.println("user_id:"+user_id);
		 * System.out.println(response.jsonPath().getString("message"));
		 * System.out.println(token);
		 */
		BearerToken= "Bearer "+ token;
		sa.assertAll();  

	}


	public void get_path(String imei_no)
	{
		uri = base_uri+user_id+"/acls";
		sa = new SoftAssert();
		Response response = given()
				.and()
				.config(RestAssured.config)
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("eid",eid)
				.and()
				.relaxedHTTPSValidation()
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
		List lpath= response.jsonPath().getList("data.dat.path");
		List lname= response.jsonPath().getList("data.dat.name");
		String Storepath="";
		for(int i =0;i<lname.size();i++)
		{
			String storename=lname.get(i).toString();
			//System.out.println(storename);
			if(imei_no.equalsIgnoreCase(storename) )
			{
				Storepath= lpath.get(i).toString();
				//System.out.println(store_dat_path);
				break;
			}
		}
		store_dat_path=Storepath;
//		System.out.println(store_dat_path);
		//System.out.println(lpath);
		/*
		String get_path = response.jsonPath().getString("data[0].dat.path");
		String get_name = response.jsonPath().getString("data[0].dat.name");
		String get_code = response.jsonPath().getString("data[0].dat.code");
		System.out.println(get_path);
		System.out.println(get_name);
		System.out.println(get_code);
		 */
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
		Response response =  given()
				.and()
				.config(RestAssured.config)
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("eid", eid)
				.and()
				.relaxedHTTPSValidation()
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

	public void get_event_values(List getlist)
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
				System.out.println("last_event_value : " +last_event_value);
				System.out.println("first_event_value : "+first_event_value);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}


	public static void create_xlsx()
	{
		//String excelPath=  System.getProperty("user.dir") + "/Testdata/Daily_History_Data.xlsx";

		String attach_date_op_file=to_date.replace("/", "");
		String excelPath=  System.getProperty("user.dir") + "/TowerData/output_file/"+ec_ops_outputfile+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sheetname);
		sheet.createRow(0);
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue("Tower_ID");
		
		sheet.getRow(0).createCell(1).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(1).setCellValue("DeviceOnboarded");

		sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(2).setCellValue("Events_ebEnergy");

		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue("Events_batteryEnergy");

		sheet.getRow(0).createCell(4).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(4).setCellValue("Events_dgEnergy");
		
		sheet.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(5).setCellValue("Events_mppttotal");
		
		sheet.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(6).setCellValue("Events_CumulativeEnergyConsumption");

		sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(7).setCellValue("Events_CumulativeOpsRunhr");

		sheet.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(8).setCellValue("Kpi_Dashboard_EnergyConsumption");

		sheet.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(9).setCellValue("Kpi_Dashboard_OpsRunhr");
		
		sheet.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(10).setCellValue("Kpi_Dashboard_ebEnergy");

		sheet.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(11).setCellValue("Kpi_Dashboard_batteryEnergy");

		sheet.getRow(0).createCell(12).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(12).setCellValue("Consumption_difference");

		sheet.getRow(0).createCell(13).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(13).setCellValue("EnergyConsumption_Staus");

		sheet.getRow(0).createCell(14).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(14).setCellValue("OpsRunHr_difference");

		sheet.getRow(0).createCell(15).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(15).setCellValue("Ops_Data_Staus");

		sheet.getRow(0).createCell(16).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(16).setCellValue("Tower_Event_Count");
		
		sheet.getRow(0).createCell(17).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(17).setCellValue("Asset_events_api_responsetime");

		sheet.getRow(0).createCell(18).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(18).setCellValue("Api_dashboard_api_responsetime");

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


	public static void write_to_excel(String filename,int rownum,int cellnum0,int cellnum1_11, int cellnum1_1,int cellnum1_2,int cellnum1_3,int cellnum1_4, int cellnum1,int cellnum2,int cellnum3,int cellnum3_1,int cellnum3_2,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,int cellnum10,int cellnum11,String towerid, String value1_11,float value1_1,float value1_2,float value1_3,float value1_4,float value1,float value2,float value3,float value3_1,float value3_2,float value4,float value5,String status1, float value6,String status2, String count,long assetapiresponsetime,long kpidashboardapiresponsetime)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/TowerData/output_file/"+filename+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sheetname);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(towerid);
			sheet.getRow(rownum).createCell(cellnum1_11).setCellValue(value1_11);
			sheet.getRow(rownum).createCell(cellnum1_1).setCellValue(value1_1);
			sheet.getRow(rownum).createCell(cellnum1_2).setCellValue(value1_2);
			sheet.getRow(rownum).createCell(cellnum1_3).setCellValue(value1_3);
			sheet.getRow(rownum).createCell(cellnum1_4).setCellValue(value1_4);
			//mppttotal_diff
			
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(value1);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(value2);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(value3);
			sheet.getRow(rownum).createCell(cellnum3_1).setCellValue(value3_1);
			sheet.getRow(rownum).createCell(cellnum3_2).setCellValue(value3_2);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(value4);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(value5);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(status1);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(value6);
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(status2);
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(count);
			sheet.getRow(rownum).createCell(cellnum10).setCellValue(assetapiresponsetime);
			sheet.getRow(rownum).createCell(cellnum11).setCellValue(kpidashboardapiresponsetime);

			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void update_to_excel(String filename,int rownum,int cellnum1,int cellnum2,String value1,Float value2)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/Testdata/Dailydata/"+filename+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet("Sheet");

			int rowCount = sheet.getLastRowNum();
			//System.out.println("rowCount:"+rowCount);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(value1);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(value2);
			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void create_xlsx(String opfile_name,String Tower_ID, String totalrecord, String mismatch, String match, String state  )
	{
		//String excelPath=  System.getProperty("user.dir") + "/Testdata/Daily_History_Data.xlsx";


		String excelPath=  System.getProperty("user.dir") + "/TowerData/output_file/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Sheet");
		sheet.createRow(0);
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue("Tower_ID");

		sheet.getRow(0).createCell(1).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(1).setCellValue(totalrecord);

		sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(2).setCellValue(mismatch);


		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue(match);

		sheet.getRow(0).createCell(4).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(4).setCellValue(state);

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

	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4, String Tower_ID, int totalrecord, int mismatch, int match, String state)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/TowerData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet("Sheet");
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(Tower_ID);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(totalrecord);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue( mismatch);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(match);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(state);

			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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


	public void renamereport() throws IOException
	{

		String replace=to_date.replace("/", "");
		try {
			String dir = System.getProperty("user.dir")+"\\TowerData\\output_file\\uidir.bat";
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
	public void test()
	{
		get_device_id("I-GJ-UPLT-ENB-9005");
	}
	
	public String get_device_id(String imei_id)
	{
		sa= new SoftAssert();
		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true},\"startsWith\":{\"datRegex\":\""+store_dat_path+"\"}}";
		Response response =  given()
				.and()
				.config(RestAssured.config)
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("eid", eid)
				.and()
				.header("Content-type", "application/json")
				.and()
				.relaxedHTTPSValidation()
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
		sa.assertEquals(200, response.statusCode());
		//System.out.println(response.statusCode());
		int count= response.jsonPath().getInt("count");
		if(count==0)
		{
			deviceOnboarded="No";
			System.out.println("No device found");
			device_asset_imei_list= response.jsonPath().getList("data.device.imei");
			device_mac_id_list= response.jsonPath().getList("data.device.mac");
			device_asset_code_list= response.jsonPath().getList("data.assetType.code");
	
	}
	
	else
	{
	
		
		deviceOnboarded="Yes";
		sa.assertEquals("Devicestatuses retrieved successfully.", response.jsonPath().getString("message"));
		device_asset_imei_list= response.jsonPath().getList("data.device.imei");
		device_mac_id_list= response.jsonPath().getList("data.device.mac");
		device_asset_code_list= response.jsonPath().getList("data.assetType.code");
		//System.out.println(imei);
		//System.out.println(mac);
		for(int i =0;i<device_mac_id_list.size();i++)
		{
			String towername=device_asset_imei_list.get(i).toString();
			if(towername.equalsIgnoreCase(imei_id))
			{
				device_mac_id= device_mac_id_list.get(i).toString();
				device_asset_code=device_asset_code_list.get(i).toString();
				//System.out.println("device_mac_id :"+device_mac_id);
				//System.out.println("device_asset_code :"+device_asset_code);
				//break;
			}
		}

		
		
	}
		sa.assertAll();
		return device_mac_id;
	}
	
	
	public void sheetname()
	{
		
		sheetname=end_date.replace("/", "");
		
	}
	
}