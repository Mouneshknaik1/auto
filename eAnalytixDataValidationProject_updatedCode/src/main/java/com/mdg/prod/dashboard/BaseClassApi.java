package com.mdg.prod.dashboard;

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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
//	public static String eid="tower";
	public static String eid="mdgtower";

	public static String env_prd="prd";
//	public static String env_sit="sit";
//	public static String env_sit_uri="https://sit.eanalytix.jio.com/";
	public static String env_prod_uri="https://eanalytix.jio.com/";
	public static String eid_uri=env_prod_uri;
	public static String sit_input_tower_sheet="sit_towerid_input.xlsx";
	public static String prod_input_tower_sheet="prd_towerid_input.xlsx";
	public static String inputsheet=prod_input_tower_sheet;
	public static String assettype="";
	public static String start_date="2024/10/07";
	public static String end_date="2024/10//08";
	public static String assetCode="0";
	//SIT
	//public static String userid="neeraj.chaunkar@ril.com";
	//public static String password="Jio@1234";
	
	/**Prod***/
	public static String userid="poonam.chauhan@ril.com";
	public static String password="P00n@m@01";
//	public static String userid="aishwarya.mr@ril.com";
//	public static String password="Jio@12345";
	public static String store_dat_path="";
	public static String BearerToken="";
	public static String user_id="";
	public static String sheet_name="Today";
//	public static String sitename="Bangalore-2 Peenya SAG2";
	public static String asset_code_fuel_sensor="dg_fuel_sensor";
//	public static String asset_code_fuel_sensor="dg_set";

	//public static String sitename="Karnataka";
	//public static String sitename="Pune-1";
	public static String sitename="India";
	//public static String sitename="Bangalore-3";
	
	
	

	public static String apiKey="42e94c60-e79e-49b1-81dd-67f044550735";
	
	public static List device_deviceid_list;
	
	public static String prod_uri="https://eanalytix.jio.com/";
	public static String sit_uri="https://sit.eanalytix.jio.com/";
	public static String exec_uri=prod_uri;
	public static String base_uri=exec_uri+"accounts/api/users/";
	public static String login_api="v2/login";
	public static String uri_events=exec_uri+"accounts/api/events/v2/stats/billing/";
	public static String uri_generic_api=exec_uri+"accounts/api/";
	public static String uri_devicestatus=uri_generic_api+"devicestatuses/search";
	public static String base_event_uri=exec_uri+"accounts/api/events/v2/stats/";
	public static String generic_events_uri=exec_uri+"accounts/api/events/search";
	public static String generic_events_uri_last=exec_uri+"accounts/api/events/search?skip=1848&limit=1";

	
	public static String kpa_mdgfuel_consumed_uri=uri_generic_api+"kpa/mdgfuelconsumed";
	public static String kpa_mdgcount_uri=uri_generic_api+"kpa/mdgcount";
	public static String kpa_energyconsumed_uri=uri_generic_api+"kpa/energyconsumed";
	public static String kpa_mdgfueloperation_uri=uri_generic_api+"kpa/mdgfueloperation";
	public static String kpa_mdgenergychart_uri=uri_generic_api+"kpa/mdg-energy-chart";
	
	public static String kpa_mdgRunHour="0";
	public static String kpa_mdgEnergy="0";
	public static String kpa_totalRunHour="0";
	public static String kpa_totalEnergy="0";
	
	
	public static String kpa_mdgfuel_consumed="";
	public static String kpa_mdgcount="";
	public static String kpa_energyconsumed="";
	public static String kpa_mdgfueloperation="";
	
	public static String kpa_mdgfueloperation_fuel_fill="";
	public static String kpa_mdgfueloperation_fuel_removed ="";
	public static String kpa_mdgfueloperation_fuel_fuelLowCount ="";
	public static String kpa_mdgfueloperation_fuel_operationalHour="";
	public static String generic_get_device=exec_uri+"accounts/api/devices/";
	 
	public SoftAssert sa;
	public static String uri;
	public static String start_time="18:30:00";
	public static String end_time="18:29:59";
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

	public static float last_event_value=0.00000f;
	public static float first_event_value=0.00000f;

	public static Workbook workbook;
	public static Sheet	sheet;

	public static String ec_ops_outputfile="mdg_energy_ops_outputfile";
	public static String tower_inputfile="tower_inputfile";
	public static String firmware_outputfile="Firmwareverion_outputfile";
	
	/*********Dg Fuel code********************/
	public static String event_fuel_fill_type="fuel_fill";
	public static String event_fuel_removed_type="fuel_removed";
	public static String event_fuel_theft_type="fuel_theft";
	public static String event_fuel_power_on_type="power_on";
	public static String event_fuel_power_off_type="power_off";



	
	public static String event_ops_hour_start_reading="";
	public static String event_ops_hour_end_reading="";
	public static int event_ops_hour=0;

	public static String end_fuel_in_liters_energy="";
	public static String start_fuel_in_liters_energy="";


	public static String start_active_energy = null;
	public static String end_active_energy = null;
	public static float energy_generated = 0.00f;
	public static float dg_efficiency = 0.00f;
	public static double dgc_enpi = 0.00f;

	public static int dg_power_on_count = 0;

	

	@BeforeSuite
	public void beforesuite()
	{
		create_xlsx(ec_ops_outputfile,"Vehicle Number","DG Controller","Dg Energy(DGC)", "DG Operational Hours(DGC)","DG","Start_fuel_level","end_fuel_leve","Fuel Filled", "Fuel Removed","Fuel Consumed","dg_events_ops","event_total_fuel_theft");
		//create_xlsx();
		//create_xlsx(firmware_outputfile, "Tower_id", "Total_Record", "Mismatch", "Match", "Status");
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
		Response response = given()
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
		System.out.println(user_id);
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
		List lpath= response.jsonPath().getList("data.dat.path");
		List lname= response.jsonPath().getList("data.dat.name");
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

		//System.out.println(lname);
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
				//System.out.println(last_event_value);
				//System.out.println(first_event_value);

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
		sheet = workbook.createSheet(sitename);
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
		sheet.getRow(0).createCell(5).setCellValue("Events_CumulativeEnergyConsumption");

		sheet.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(6).setCellValue("Events_CumulativeOpsRunhr");

		sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(7).setCellValue("Kpi_EnergyConsumption");

		sheet.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(8).setCellValue("Kpi_OpsRunhr");
		
		sheet.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(9).setCellValue("Kpi_ebEnergy");

		sheet.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(10).setCellValue("Kpi_batteryEnergy");

		sheet.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(11).setCellValue("Consumption_difference");

		sheet.getRow(0).createCell(12).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(12).setCellValue("EnergyConsumption_Staus");

		sheet.getRow(0).createCell(13).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(13).setCellValue("OpsRunHr_difference");

		sheet.getRow(0).createCell(14).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(14).setCellValue("Ops_Data_Staus");

		sheet.getRow(0).createCell(15).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(15).setCellValue("Tower_Event_Count");
		
		sheet.getRow(0).createCell(16).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(16).setCellValue("Asset_events_api_responsetime");

		sheet.getRow(0).createCell(17).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(17).setCellValue("Api_dashboard_api_responsetime");

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


	public static void write_to_excel(String filename,int rownum,int cellnum0,int cellnum1_11, int cellnum1_1,int cellnum1_2,int cellnum1_3, int cellnum1,int cellnum2,int cellnum3,int cellnum3_1,int cellnum3_2,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,int cellnum10,int cellnum11,String towerid, String value1_11,float value1_1,float value1_2,float value1_3,float value1,float value2,float value3,float value3_1,float value3_2,float value4,float value5,String status1, float value6,String status2, String count,long assetapiresponsetime,long kpidashboardapiresponsetime)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/TowerData/output_file/"+filename+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet("Sheet");
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(towerid);
			sheet.getRow(rownum).createCell(cellnum1_11).setCellValue(value1_11);
			sheet.getRow(rownum).createCell(cellnum1_1).setCellValue(value1_1);
			sheet.getRow(rownum).createCell(cellnum1_2).setCellValue(value1_2);
			sheet.getRow(rownum).createCell(cellnum1_3).setCellValue(value1_3);
			
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


	public static void update_to_excel(String opfile_name,int rownum,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8, String value1,String value2,String value3,double value4,double value5,double value6,String value7,double value8)
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
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(value8);
			
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
		//else
			//JiopowerPageOperations.rename_test_report("Out","");
	}


	public void renamereport() throws IOException
	{

		String replace=to_date.replace("/", "");
		try {
			String dir = System.getProperty("user.dir")+"\\MdgData\\output_file\\uidir.bat";
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
		//imei_id ="I-MH-CWAD-ENB-0734";

		String payload = "{\"device\":{\"model\":\"\",\"name\":null,\"mac\":\""+imei_id+"\",\"tagId\":null,\"created\":{\"from\":null,\"to\":null}},\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true},\"assetType\":{},\"startsWith\":{\"datRegex\":\""+user_id+"\"}}";
		//System.out.println(payload);
		Response response = given()
				.header("Content-type", "application/json")
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


	
	
	
	public static void create_xlsx(String opfile_name,String number,String dgController_imei, String dgEnegry, String OperationalHours,String dg_imei,String startFuellevel,String endFuellevel,String fuellfilled,String fuelremoved,String FuelConsumed,String dgops, String event_total_fuel_theft)
	{
		//String excelPath=  System.getProperty("user.dir") + "/Testdata/Daily_History_Data.xlsx";


		String excelPath=  System.getProperty("user.dir") + "/MdgData/output_file/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sitename);
		sheet.createRow(0);
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue(number);

		sheet.getRow(0).createCell(1).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(1).setCellValue(dgController_imei);
		
		sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(2).setCellValue(dgEnegry);

		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue(OperationalHours);
		
		sheet.getRow(0).createCell(4).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(4).setCellValue(dg_imei);
		
		sheet.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(5).setCellValue(startFuellevel);
		
		sheet.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(6).setCellValue(endFuellevel);
		
		sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(7).setCellValue(fuellfilled);
		
		sheet.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(8).setCellValue(fuelremoved);
		
		sheet.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(9).setCellValue(FuelConsumed);
		
		sheet.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(10).setCellValue(dgops);
		
		sheet.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(11).setCellValue(event_total_fuel_theft);
		
		
		
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


//@Test
	public void test1()
{
	//create_xlsx(ec_ops_outputfile,"Vehicle number","DG Controller","Dg Energy", "DG Operational Hours");
}
	
	
	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,String name,String dgc_imei, double dgenergy,double opshour)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/MdgData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(name);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(dgc_imei);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(dgenergy);
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

		assetCode=null;
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
	
	public static String get_device_bymacid(String imei_id,String assetCode)
	{
		SoftAssert sa= new SoftAssert();
	
		String device_id=null;
//		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true},\"startsWith\":{\"datRegex\":\"\"},\"device\":{\"model\":\"\",\"name\":\"\",\"mac\":\""+imei_id+"\",\"assetCode\":\"\",\"created\":{\"from\":null,\"to\":null}},\"assetType\":{\"code\":\""+assetCode+"\"}}";
		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true},\"startsWith\":{\"datRegex\":\"\"},\"device\":{\"model\":\"\",\"name\":\"\",\"mac\":\""+imei_id+"\",\"assetCode\":\"\",\"created\":{\"from\":null,\"to\":null}},\"assetType\":{\"code\":\""+assetCode+"\"}}";
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
		System.out.println(response.statusCode());
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
			//Asset name 
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
					System.out.println(device_id);
					System.out.println("device_mac_id :"+device_mac_id);
					System.out.println("device_asset_code :"+device_asset_code);
					break;
				}
			}


		}
		sa.assertAll();
		return device_id;
	}

	

}