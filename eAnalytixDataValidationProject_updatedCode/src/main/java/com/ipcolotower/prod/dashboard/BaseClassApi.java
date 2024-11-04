package com.ipcolotower.prod.dashboard;

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
	public static String eid="pempilot";
	public static String env_prd="prd";
//	public static String env_sit="sit";
//	public static String env_ppd="ppd";
//	public static String env_sit_uri="https://sit.eanalytix.jio.com/";
	public static String env_prod_uri="https://eanalytix.jio.com/";
//	public static String env_ppd_uri="https://pp.eanalytix.jvts.net/";
	
	public static String eid_uri=env_prod_uri;
//	public static String sit_input_tower_sheet="sit_towerid_input.xlsx";
	public static String prod_input_tower_sheet="prd_towerid_input.xlsx";
	public static String inputsheet=prod_input_tower_sheet;
	public static String assettype="";
	public static String start_date="2024/10/09";
	public static String end_date="2024/10/10";
	

	public static String userid="poonam.chauhan@ril.com";
	public static String password="P00n@m@01";
	
	public static String adminuserid="jiotadmin@jio.io";
	public static String adminpassword="jeVaaPPaDm@41";
	
//	public static String adminuserid="jiotadmin@jio.io";
//	public static String adminpassword="jiotadmin";
	
	public static String assetModelName="";
	
	
	public static String store_dat_path="";
	public static String BearerToken="";
	public static String adminBearerToken="";
	public static String user_id="";
	public static String adminuser_id="";
	

	
	//public static String sitename="IP COLO";
	//public static String sitename="India";
	//public static String sitename="Pune-1 MP";
	

//	public static String sitename="I-MH-MWAL-ENB-I010";
	//public static String sitename="I-MH-JNNR-ENB-I009";
	//public static String sitename="I-MH-JNNR-ENB-I009";
	//public static String sitename="I-MH-PUNE-ENB-I179";
	public static String sitename="I-MH-MWAL-ENB-I010";
//	public static String sitename="I-MH-PUNE-ENB-A033";
//		public static String sitename="I-MH-AMGN-ENB-A002";
//		public static String sitename="I-MH-AMGN-ENB-A001";
//		public static String sitename="I-DL-FDBD-ENB-G010";
//	public static String sitename="GTL";

	
	
	public static String sheet1_name="test";
	public static String sheet2_name="test";
	
	public static String ac_filename="ac_outputfile";
	public static String dc_filename="dc_outputfile";
	
	
	
	public static String apiKey="42e94c60-e79e-49b1-81dd-67f044550735";

	public static String prod_uri="https://eanalytix.jio.com/";
	public static String sit_uri="https://sit.eanalytix.jio.com/";
	public static String exec_uri=prod_uri;
	public static String base_uri=exec_uri+"accounts/api/users/";
	public static String login_api="v2/login";
	public static String uri_events=exec_uri+"accounts/api/events/v2/stats/billing/";
	public static String uri_generic_api=exec_uri+"accounts/api/";
	public static String uri_devicestatus=uri_generic_api+"devicestatuses/search";
	public static String base_event_uri=exec_uri+"accounts/api/events/v2/stats/";
	public static String generic_events_uri=exec_uri+"accounts/api/events/search?skip=0&limit=500";
	public static String generic_get_device=exec_uri+"accounts/api/devices/";
	

	public static String admin_login_uri = exec_uri+"accounts/api/adminusers/v2/login";
	public static String admin_assetModel_uri = exec_uri+"accounts/api/assettypemodels/search";
	public static String admin_assetModelBycode_uri = exec_uri+"accounts/api/assettypemodels/byCode";
		
	public static String kpa_stats_energy_dcrunhr_uri=uri_generic_api+"ipcolo-stats/energy";
	public static String kpa_stats_jioTariff=uri_generic_api+"ipcolo-stats/jioTariff";
	public static String kpa_stats_energy_acKwhRunHr_uri=uri_generic_api+"ipcolo-stats/acKwhRunHr";
	public static String kpa_stats_energy_jioDcLoad_uri=uri_generic_api+"ipcolo-stats/jioDcLoad";
	
	
	public static String kpa_critical_energy_totalKwhEB="0";
	public static String kpa_critical_energy_totalKwhDG="0";
	public static String kpa_critical_energy_totalKwhBattery="0";
	public static String kpa_critical_energy_totalKwhOthers="0";
	public static String kpa_critical_energy_totalKwh="0";
	
	public static String kpa_noncritical_energy_totalKwhEB="0";
	public static String kpa_noncritical_energy_totalKwhDG="0";
	public static String kpa_noncritical_energy_totalKwhBattery="0";
	public static String kpa_noncritical_energy_totalKwhOthers="0";
	public static String kpa_noncritical_energy_totalKwh="0";
	
	public static String kpa_others_energy_totalKwhEB="0";
	public static String kpa_others_energy_totalKwhDG="0";
	public static String kpa_others_energy_totalKwhBattery="0";
	public static String kpa_others_energy_totalKwhOthers="0";
	public static String kpa_others_energy_totalKwh="0";
	
	public static String kpa_smpsop_energy_totalKwhEB="0";
	public static String kpa_smpsop_energy_totalKwhDG="0";
	public static String kpa_smpsop_energy_totalKwhBattery="0";
	public static String kpa_smpsop_energy_totalKwhOthers="0";
	public static String kpa_smpsop_energy_totalKwh="0";
	
	public static String kpa_critical_runhr_totalRunHourEB ="0";
	public static String kpa_critical_runhr_totalRunHourDG ="0";
	public static String kpa_critical_runhr_totalRunHourBattery ="0";
	public static String kpa_critical_runhr_totalRunHourOthers ="0";
	public static String kpa_critical_runhr_totalRunHour ="0";
	
	public static String kpa_noncritical_runhr_totalRunHourEB ="0";
	public static String kpa_noncritical_runhr_totalRunHourDG ="0";
	public static String kpa_noncritical_runhr_totalRunHourBattery ="0";
	public static String kpa_noncritical_runhr_totalRunHourOthers ="0";
	public static String kpa_noncritical_runhr_totalRunHour ="0";
	
	public static String kpa_others_runhr_totalRunHourEB ="0";
	public static String kpa_others_runhr_totalRunHourDG ="0";
	public static String kpa_others_runhr_totalRunHourBattery ="0";
	public static String kpa_others_runhr_totalRunHourOthers ="0";
	public static String kpa_others_runhr_totalRunHour ="0";
	
	public static String kpa_smpsop_runhr_totalRunHourEB ="0";
	public static String kpa_smpsop_runhr_totalRunHourDG ="0";
	public static String kpa_smpsop_runhr_totalRunHourBattery ="0";
	public static String kpa_smpsop_runhr_totalRunHourOthers ="0";
	public static String kpa_smpsop_runhr_totalRunHour ="0";
		
	public static String kpa_tariff_jioTariffEB ="0";
	public static String kpa_tariff_jioTariffDG ="0";
	public static String kpa_tariff_totalTariff ="0";
	
	public static String kpa_acrunhr_totalRunHourEB="0";
	public static String kpa_acrunhr_totalRunHourDG="0";
	public static String kpa_acrunhr_totalKwhEB="0";
	public static String kpa_acrunhr_totalKwhDG="0";
	public static String kpa_acrunhr_totalRunHour="0";
	public static String kpa_acrunhr_totalKwh="0";
	
	public static String kpa_jioDcLoad="0";
	public static String kpa_totalDcLoad="0";
	
	public static String assetCode="0";
	
	
	
	public SoftAssert sa;
	public static String uri;
	public static String start_time="18:30:00";
//	public static String end_time="18:29:59";
	public static String end_time="18:30:59";
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
	public static Workbook workbook1;
	public static Workbook workbook2;
	public static Sheet	sheet;
	public static Sheet	sheet1;
	public static Sheet	sheet2;

	public static String ec_ops_outputfile="ipcolo_outputfile";
	public static String ec_ops_kpioutputfile="kpi_ipcolo_outputfile";
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


	
	//public static double energy_generated = 0.000;
	public static double opshours = 0;	

	public static  double cph_value = 1.02;
	
	//For Indus
	public static  double eb_tariff = 8.54;
	//For ATC
//	public static  double eb_tariff = 8.73;

	//For Indus
	public static  double dg_tariff = 92.54;
	//For ATC
//	public static  double dg_tariff = 92.36;

	
	public static double dg_kva =0;
	public static double dg_powerfactor = 0.8;
	public static double chp_value_from_OEM = 0;
	/*	
	 public static double dg_kva =25;
	public static double dg_powerfactor = 0.8;
	public static double chp_value_from_OEM = 4.47;
	*/
	
	public static String admin_assetModelcode="";
	public static double modelStaticParamsValue_kvaParam=0;
	public static double modelStaticParamsValue_pf=0;
	public static double modelStaticParamsValue_oem=0;
	public static String modelStaticParamsname="";
	public static String modelStaticParamsname_kvaParam="kvaParam";
	public static String modelStaticParamsname_powerFactor="powerFactor";
	public static double modelStaticParamsname_oem_capcity=70.5;
	
	
	@BeforeSuite
	public void beforesuite()
	{
		create_xlsx(ec_ops_outputfile);
		create_kpixlsx(ec_ops_kpioutputfile);
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
		//System.out.println(requestpayload);
		//System.out.println(base_uri+login_api);
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
				.header("Eid", eid)
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
//		to_date_in_milli=x.substring(0,10).concat("000");


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
				.header("Eid", eid)
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
		int size = 300;
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
				System.out.println("last_event_value is : " +last_event_value);
				System.out.println("first_event_value is : " +first_event_value);

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
			String dir = System.getProperty("user.dir")+"\\IPCOLOData\\output_file\\uidir.bat";
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


//	@Test
	public void test()
	{
		get_device_id("I-MH-MWAL-ENB-I010");
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
				.header("Eid", eid)
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
	
	public void admin_login()
	{

		String requestpayload = "{\"email\":\""+adminuserid+"\",\"password\":\""+adminpassword+"\"}";
		//System.out.println(requestpayload);
		//System.out.println(admin_login_uri);
		sa = new SoftAssert();
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("Eid", eid)
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
	
	
	public static void create_xlsx(String opfile_name,String SAPID,String channel1_nonCriticalEnergy, String channel1_nonCriticalRunHr, String channel2_criticalEnergy,String channel2_criticalRunhr,String channel3_otherEnergy,String channel3_otherRunhr,String channel4_smpsEnergy,String channel4_smpsRunhr,String eb_charges_for_jio,String disel_charges_for_jio, String ac_dgEnergy,String ac_dgrunhr,String jio_dc_loadKw,String total_jio_dc_loadKw, String estimated_bill_for_jio)
	{
		
		String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sitename);
		sheet.createRow(0);
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue(SAPID);

		sheet.getRow(0).createCell(1).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(1).setCellValue(channel1_nonCriticalEnergy);

		sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(2).setCellValue(channel1_nonCriticalRunHr);

		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue(channel2_criticalEnergy);

		sheet.getRow(0).createCell(4).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(4).setCellValue(channel2_criticalRunhr);

		sheet.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(5).setCellValue(channel3_otherEnergy);

		sheet.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(6).setCellValue(channel3_otherRunhr);

		sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(7).setCellValue(channel4_smpsEnergy);

		sheet.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(8).setCellValue(channel4_smpsRunhr);

		sheet.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(9).setCellValue(eb_charges_for_jio);

		sheet.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(10).setCellValue(disel_charges_for_jio);
		
		sheet.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(11).setCellValue(ac_dgEnergy);
		
		sheet.getRow(0).createCell(12).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(12).setCellValue(ac_dgrunhr);
	
		sheet1.getRow(0).createCell(13).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(13).setCellValue(jio_dc_loadKw);
		
		sheet1.getRow(0).createCell(14).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(14).setCellValue(total_jio_dc_loadKw);
		
		
		sheet1.getRow(0).createCell(15).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(15).setCellValue(estimated_bill_for_jio);
		
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
	public void test34()
	{
		create_xlsx(ec_ops_outputfile);
	}
	
	
	public static void create_xlsx_dc(String opfile_name,String sheet1_name)
	{
		String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
		workbook1 = new XSSFWorkbook();
		sheet1 = workbook1.createSheet(sheet1_name);
		sheet1.createRow(0);
		sheet1.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet1.getRow(0).createCell(0).setCellValue("SAPID");

		sheet1.getRow(0).createCell(1).setCellType(CellType.STRING);
		sheet1.getRow(0).createCell(1).setCellValue("channel1_nonCriticalEnergy");

		sheet1.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(2).setCellValue("channel1_nonCriticalRunHr");

		sheet1.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(3).setCellValue("channel2_criticalEnergy");

		sheet1.getRow(0).createCell(4).setCellType(CellType.STRING);
		sheet1.getRow(0).createCell(4).setCellValue("channel2_criticalRunhr");

		sheet1.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(5).setCellValue("channel3_otherEnergy");

		sheet1.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(6).setCellValue("channel3_otherRunhr");

		sheet1.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(7).setCellValue("channel4_smpsEnergy");

		sheet1.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(8).setCellValue("channel4_smpsRunhr");

		sheet1.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(9).setCellValue("jio_dc_loadKw");

		sheet1.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(10).setCellValue("total_jio_dc_loadKw");
		
		sheet1.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(11).setCellValue("disel_charges_for_jio");
				
		try {
			File file = new File(excelPath);
			FileOutputStream fos = new FileOutputStream(file);
			workbook1.write(fos);
			workbook1.close();
		
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void create_xlsx_ac(String opfile_name,String sheet_name)
	{
		String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
    	workbook2 = new XSSFWorkbook();
		sheet2 = workbook2.createSheet(sheet2_name);
		sheet2.createRow(0);
		
		sheet2.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet2.getRow(0).createCell(0).setCellValue("SAPID");

		sheet2.getRow(0).createCell(1).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(1).setCellValue("ac_ebEnergy");

		sheet2.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(2).setCellValue("ac_ebRunhr");

		sheet2.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(3).setCellValue("ac_dgEnergy");

		sheet2.getRow(0).createCell(4).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(4).setCellValue("ac_dgrunhr");

		sheet2.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(5).setCellValue("ac_energy");
		
		sheet2.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(6).setCellValue("ac_runHr");
		
		sheet2.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(7).setCellValue("ac_dg_powerKw");
		
		sheet2.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(8).setCellValue("ac_eb_powerKw");
					
		sheet2.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(9).setCellValue("eb_charges_for_jio");
				
			
		try {
			File file = new File(excelPath);
			FileOutputStream fos = new FileOutputStream(file);
			workbook2.write(fos);
			workbook2.close();
			}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	public static void create_xlsx(String opfile_name,String sheet1_name,String sheet2_name)
	{
		String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
		workbook1 = new XSSFWorkbook();
		sheet1 = workbook1.createSheet(sheet1_name);
		sheet1.createRow(0);
		sheet1.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet1.getRow(0).createCell(0).setCellValue("SAPID");

		sheet1.getRow(0).createCell(1).setCellType(CellType.STRING);
		sheet1.getRow(0).createCell(1).setCellValue("channel1_nonCriticalEnergy");

		sheet1.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(2).setCellValue("channel1_nonCriticalRunHr");

		sheet1.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(3).setCellValue("channel2_criticalEnergy");

		sheet1.getRow(0).createCell(4).setCellType(CellType.STRING);
		sheet1.getRow(0).createCell(4).setCellValue("channel2_criticalRunhr");

		sheet1.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(5).setCellValue("channel3_otherEnergy");

		sheet1.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(6).setCellValue("channel3_otherRunhr");

		sheet1.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(7).setCellValue("channel4_smpsEnergy");

		sheet1.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(8).setCellValue("channel4_smpsRunhr");

		sheet1.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(9).setCellValue("jio_dc_loadKw");

		sheet1.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(10).setCellValue("total_jio_dc_loadKw");
		/*
		sheet1.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(11).setCellValue(eb_charges_for_jio);
		
		sheet1.getRow(0).createCell(12).setCellType(CellType.NUMERIC);
		sheet1.getRow(0).createCell(12).setCellValue(disel_charges_for_jio);
		*/
		
		workbook2 = new XSSFWorkbook();
		sheet2 = workbook2.createSheet(sheet2_name);
		sheet2.createRow(0);
		
		sheet2.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet2.getRow(0).createCell(0).setCellValue("SAPID");

		sheet2.getRow(0).createCell(1).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(1).setCellValue("ac_ebEnergy");

		sheet2.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(2).setCellValue("ac_ebRunhr");

		sheet2.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(3).setCellValue("ac_dgEnergy");

		sheet2.getRow(0).createCell(4).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(4).setCellValue("ac_dgrunhr");

		sheet2.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(5).setCellValue("ac_energy");
		
		sheet2.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(6).setCellValue("ac_runHr");
		
		sheet2.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(7).setCellValue("ac_dg_powerKw");
		
		sheet2.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(8).setCellValue("ac_eb_powerKw");
					
		sheet2.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
		sheet2.getRow(0).createCell(9).setCellValue("eb_charges_for_jio");
				
			
		try {
			File file = new File(excelPath);
			FileOutputStream fos = new FileOutputStream(file);
			workbook2.write(fos);
			workbook1.write(fos);
			workbook2.close();
			workbook1.close();
		
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	
	
	public static void create_xlsx(String opfile_name,String SAPID,String jio_dc_loadKw,String total_jio_dc_loadKw,String eb_charges_for_jio, String disel_charges_for_jio)
	{
		//String excelPath=  System.getProperty("user.dir") + "/Testdata/Daily_History_Data.xlsx";


		String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(sitename);
		sheet.createRow(0);
		sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
		sheet.getRow(0).createCell(0).setCellValue(SAPID);

		sheet.getRow(0).createCell(1).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(1).setCellValue(jio_dc_loadKw);

		sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(2).setCellValue(total_jio_dc_loadKw);
		
		sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(3).setCellValue(eb_charges_for_jio);
		
		sheet.getRow(0).createCell(4).setCellType(CellType.NUMERIC);
		sheet.getRow(0).createCell(4).setCellValue(disel_charges_for_jio);
		
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
		//create_xlsx_dc(ec_ops_outputfile,sheet1_name);
		create_xlsx_ac("dc_outputfile",sheet1_name);
		create_xlsx_ac("ac_outputfile",sheet2_name);
		
		//create_xlsx(ec_ops_outputfile,"SAPID","channel1_nonCriticalEnergy","channel1_nonCriticalRunHr", "channel2_criticalEnergy","channel2_criticalRunhr","channel3_otherEnergy","channel3_otherRunhr","channel4_smpsEnergy", "channel4_smpsRunhr","jio_dc_loadKw","total_jio_dc_loadKw","eb_charges_for_jio","disel_charges_for_jio");
	}

	
	

	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,int cellnum10,int cellnum11,int cellnum12,int cellnum13,int cellnum14,int cellnum15,int cellnum16,int cellnum17,int cellnum18,int cellnum19, String sapid, double channel1_nonCriticalEnergy,double channel1_nonCriticalRunHr,double channel2_criticalEnergy,double channel2_criticalRunhr,double channel3_otherEnergy,double channel3_otherRunhr,double channel4_smpsEnergy,double channel4_smpsRunhr,double eb_charges_for_jio,double disel_charges_for_jio, double ac_dgEnergy,double ac_dgrunhr,double ac_ebEnergy,double ac_ebRunhr,double ac_energy,double ac_runhr,double jio_dc_loadKw,double total_jio_dc_loadKw, double estimated_bill_for_jio)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(sapid);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(channel1_nonCriticalEnergy);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(channel1_nonCriticalRunHr);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(channel2_criticalEnergy);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(channel2_criticalRunhr);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(channel3_otherEnergy);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(channel3_otherRunhr);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(channel4_smpsEnergy);
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(channel4_smpsRunhr);
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(eb_charges_for_jio);
			sheet.getRow(rownum).createCell(cellnum10).setCellValue(disel_charges_for_jio);
			sheet.getRow(rownum).createCell(cellnum11).setCellValue(ac_dgEnergy);
			sheet.getRow(rownum).createCell(cellnum12).setCellValue(ac_dgrunhr);
			sheet.getRow(rownum).createCell(cellnum13).setCellValue(ac_ebEnergy);
			sheet.getRow(rownum).createCell(cellnum14).setCellValue(ac_ebRunhr);
			sheet.getRow(rownum).createCell(cellnum15).setCellValue(ac_energy);
			sheet.getRow(rownum).createCell(cellnum16).setCellValue(ac_runhr);
			sheet.getRow(rownum).createCell(cellnum17).setCellValue(jio_dc_loadKw);
			sheet.getRow(rownum).createCell(cellnum18).setCellValue(total_jio_dc_loadKw);
			sheet.getRow(rownum).createCell(cellnum19).setCellValue(estimated_bill_for_jio);

			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,String sapid, String kpa_tariff_totalTariff2,String kpa_acrunhr_totalRunHourEB2,String kpa_acrunhr_totalRunHourDG2,String kpa_acrunhr_totalKwhEB2,String kpa_acrunhr_totalKwhDG2, String kpa_jioDcLoad2,String kpa_totalDcLoad2)
	{
		
		 
		try {	
			String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(sapid);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(kpa_tariff_totalTariff2);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(kpa_acrunhr_totalRunHourEB2);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(kpa_acrunhr_totalRunHourDG2);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(kpa_acrunhr_totalKwhEB2);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(kpa_acrunhr_totalKwhDG2);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(kpa_jioDcLoad2);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(kpa_totalDcLoad2);
			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	                   
	public static void write_to_excel_ac(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,String sapid, String ac_ebEnergy, String ac_ebRunhr,String ac_dgEnergy,String ac_dgrunhr,String ac_energy,String ac_runHr,String ac_dg_powerKw,String ac_eb_powerKw,String eb_charges_for_jio)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sheet1_name);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(sapid);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(ac_ebEnergy);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(ac_ebRunhr);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(ac_dgEnergy);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(ac_dgrunhr);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(ac_energy);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(ac_runHr);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(ac_dg_powerKw);
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(ac_eb_powerKw);
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(eb_charges_for_jio);
			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
					//	write_to_excel(dc_filename,count,0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,sapid,channel1_nonCriticalEnergy,channel1_nonCriticalRunhr, channel2_criticalEnergy,channel2_criticalRunhr,channel3_otherEnergy,channel3_otherRunhr,channel4_smpsEnergy, channel4_smpsRunhr,jio_dc_loadKw,total_jio_dc_loadKw,disel_charges_for_jio);	
	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,int cellnum10,String sapid, double channel1_nonCriticalEnergy,double channel1_nonCriticalRunHr,double channel2_criticalEnergy,double channel2_criticalRunhr,double channel3_otherEnergy,double channel3_otherRunhr,double channel4_smpsEnergy,double channel4_smpsRunhr,double dgenergy,double opshour,double jio_dc_loadKw,double total_jio_dc_loadKw)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(sapid);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(channel1_nonCriticalEnergy);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(channel1_nonCriticalRunHr);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(channel2_criticalEnergy);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(channel2_criticalRunhr);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(channel3_otherEnergy);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(channel3_otherRunhr);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(channel4_smpsEnergy);
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(channel4_smpsRunhr);
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(jio_dc_loadKw);
			sheet.getRow(rownum).createCell(cellnum10).setCellValue(total_jio_dc_loadKw);
			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	
	public static void write_to_excel_dc(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4,int cellnum5,int cellnum6,int cellnum7,int cellnum8,int cellnum9,int cellnum10,int cellnum11,String sapid, double channel1_nonCriticalEnergy,double channel1_nonCriticalRunHr,double channel2_criticalEnergy,double channel2_criticalRunhr,double channel3_otherEnergy,double channel3_otherRunhr,double channel4_smpsEnergy,double channel4_smpsRunhr,double jio_dc_loadKw,double total_jio_dc_loadKw,double disel_charges_for_jio)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sheet2_name);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(sapid);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(channel1_nonCriticalEnergy);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(channel1_nonCriticalRunHr);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(channel2_criticalEnergy);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(channel2_criticalRunhr);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(channel3_otherEnergy);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(channel3_otherRunhr);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(channel4_smpsEnergy);
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(channel4_smpsRunhr);
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(jio_dc_loadKw);
			sheet.getRow(rownum).createCell(cellnum10).setCellValue(total_jio_dc_loadKw);
			sheet.getRow(rownum).createCell(cellnum11).setCellValue(disel_charges_for_jio);
			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	

	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4,String sapid,double jio_dc_loadKw,double total_jio_dc_loadKw,double eb_charges_for_jio,double disel_charges_for_jio)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(sapid);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(jio_dc_loadKw);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(total_jio_dc_loadKw);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(eb_charges_for_jio);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(disel_charges_for_jio);
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

public static double jioLoadCalcKW(double dcload,double totaldcload) 
{
	double sum=0;
	if((dcload==0 || totaldcload==0))
	{
		sum=0;
	}
	else	
	try {
	sum=dcload/totaldcload;
	}
	catch (ArithmeticException e) {
		// TODO: handle exception
		sum=0;
	}
	
	return sum;
}

public static double loading_of_dg_total_load(double dcload,double totaldcload) 
{
	double sum=0;
	if((dcload==0 || totaldcload==0))
	{
		sum=0;
	}
	else
		
	try {
	sum=(dcload/totaldcload)*100;
	}
	catch (ArithmeticException e) {
		// TODO: handle exception
		System.out.println(e);
		sum=0;
	}
	return sum;
}

public static double ebCPHforJio(double ac_eb_powerKw,double dc_loadPercentage) 
{
	double eb_cph=0;
	
	if((ac_eb_powerKw==0 || dc_loadPercentage==0))
	{
		eb_cph=0;
	}
	else
	
	
	try {
		eb_cph=ac_eb_powerKw*dc_loadPercentage*cph_value;
	}
	catch (ArithmeticException e) {
		// TODO: handle exception
		eb_cph=0;
	}
	return eb_cph;
}

public static double ebchargesforJio(double ac_eb_powerKw,double dc_loadPercentage) 
{
	double eb_cp_jio=0;

	
	if((ac_eb_powerKw==0 || dc_loadPercentage==0))
	{
		eb_cp_jio=0;
	}
	else
		
	try {
		eb_cp_jio=ac_eb_powerKw*dc_loadPercentage*eb_tariff;
	}
	catch (ArithmeticException e) {
		// TODO: handle exception
		eb_cp_jio=0;
	}
	return eb_cp_jio;
}

public static double dgcapacity(double dg_kv,double dg_kva_pf) 
{
	double dg_capcity=0;
	dg_kv=dg_kva;
	dg_kva_pf=dg_powerfactor;
	//dg_kv=modelStaticParamsValue_kvaParam;
	//dg_kva_pf=modelStaticParamsValue_pf;
	try {
		dg_capcity=dg_kv*dg_kva_pf;
	}
	catch (ArithmeticException e) {
		// TODO: handle exception
		dg_capcity=0;
	}
	return dg_capcity;
}


public static double dgcphforJio(double dg_lp,double dg_oem_value) 
{
	double dg_cph_value=0;
	dg_oem_value=chp_value_from_OEM;
	try {
		dg_cph_value=dg_lp*dg_oem_value;
	}
	catch (ArithmeticException e) {
		// TODO: handle exception
		dg_cph_value=0;
	}
	return dg_cph_value;
}

public static double diesel_charges_for_jio(double dg_runhr,double cph,double dg_tarif) 
{
	double diesel_charges_for_jio=0;
	dg_tarif=dg_tariff;
	try {
		diesel_charges_for_jio=dg_runhr*dg_tarif*cph;
	}
	catch (ArithmeticException e) {
		// TODO: handle exception
		diesel_charges_for_jio=0;
	}
	return diesel_charges_for_jio;

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



public static void create_xlsx(String opfile_name)
{
	
	String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";

	workbook = new XSSFWorkbook();
	sheet = workbook.createSheet(sitename);
	sheet.createRow(0);
	sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
	sheet.getRow(0).createCell(0).setCellValue("SAPID");

	sheet.getRow(0).createCell(1).setCellType(CellType.STRING);
	sheet.getRow(0).createCell(1).setCellValue("channel1_nonCriticalEnergy");

	sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(2).setCellValue("channel1_nonCriticalRunHr");

	sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(3).setCellValue("channel2_criticalEnergy");

	sheet.getRow(0).createCell(4).setCellType(CellType.STRING);
	sheet.getRow(0).createCell(4).setCellValue("channel2_criticalRunhr");

	sheet.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(5).setCellValue("channel3_otherEnergy");

	sheet.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(6).setCellValue("channel3_otherRunhr");

	sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(7).setCellValue("channel4_smpsEnergy");

	sheet.getRow(0).createCell(8).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(8).setCellValue("channel4_smpsRunhr");

	sheet.getRow(0).createCell(9).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(9).setCellValue("eb_charges_for_jio");

	sheet.getRow(0).createCell(10).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(10).setCellValue("disel_charges_for_jio");
	
	sheet.getRow(0).createCell(11).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(11).setCellValue("ac_dgEnergy");
	
	sheet.getRow(0).createCell(12).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(12).setCellValue("ac_dgrunhr");
	

	sheet.getRow(0).createCell(13).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(13).setCellValue("ac_ebEnergy");

	sheet.getRow(0).createCell(14).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(14).setCellValue("ac_ebrunhr");
	
	sheet.getRow(0).createCell(15).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(15).setCellValue("ac_Energy");
	
	sheet.getRow(0).createCell(16).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(16).setCellValue("ac_runhr");
	
	sheet.getRow(0).createCell(17).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(17).setCellValue("jio_dc_loadKw");
	
	sheet.getRow(0).createCell(18).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(18).setCellValue("total_jio_dc_loadKw");
	
	
	
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


public static void create_kpixlsx(String opfile_name)
{
	
	String excelPath=  System.getProperty("user.dir") + "/IPCOLOData/output_file/"+opfile_name+".xlsx";

	workbook = new XSSFWorkbook();
	sheet = workbook.createSheet(sitename);
	sheet.createRow(0);
	sheet.getRow(0).createCell(0).setCellType(CellType.STRING);
	sheet.getRow(0).createCell(0).setCellValue("SAPID");

	sheet.getRow(0).createCell(1).setCellType(CellType.STRING);
	sheet.getRow(0).createCell(1).setCellValue("kpa_tariff_totalTariff");

	sheet.getRow(0).createCell(2).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(2).setCellValue("kpa_acrunhr_totalRunHourEB");

	sheet.getRow(0).createCell(3).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(3).setCellValue("kpa_acrunhr_totalRunHourDG");

	sheet.getRow(0).createCell(4).setCellType(CellType.STRING);
	sheet.getRow(0).createCell(4).setCellValue("kpa_acrunhr_totalKwhEB");

	sheet.getRow(0).createCell(5).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(5).setCellValue("kpa_acrunhr_totalKwhDG");

	sheet.getRow(0).createCell(6).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(6).setCellValue("kpa_jioDcLoad");
	
	sheet.getRow(0).createCell(7).setCellType(CellType.NUMERIC);
	sheet.getRow(0).createCell(7).setCellValue("kpa_totalDcLoad");
	
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







public static void getOEMcphvalue(String dgAssetcode,double dg_loadpercent) 
{
	double dg_kva_15=15;
	double dg_kva_25=25;
	double dg_kva_40=40;
	double dg_percent=dg_loadpercent;
	assetCode=dgAssetcode;
	double cph=0;
	double dg_capacity=0;
	String name_eicher="eicher";
//	String name_kirloskar="kirloskar";

	String name_mahindra="mahindra";
	System.out.println(assetCode);
	while(assetCode==null)
	{
		
	}
//	else
	if((assetCode.contains("25")) & (assetCode.contains(name_mahindra)))
	{

		if((dg_percent>10) & (dg_percent<=25))
		{
			cph=2.45;
			System.out.println(cph);
		}
		else if((dg_percent>25) & (dg_percent<=30))
		{
			cph=2.67;
			System.out.println(cph);
		}
		else if((dg_percent>30) & (dg_percent<=40))
		{
			cph=3.1;
			System.out.println(cph);
		}
		else if((dg_percent>40) & (dg_percent<=50))
		{
			cph=3.53;
			System.out.println(cph);
		}
		else if((dg_percent>50) & (dg_percent<=60))
		{
			cph=3.99;
			System.out.println(cph);
		}
		else if((dg_percent>60) & (dg_percent<=70))
		{
			cph=4.46;
			System.out.println(cph);
		}
		else if((dg_percent>70) & (dg_percent<=75))
		{
			cph=4.69;
			System.out.println(cph);
		}
		else if((dg_percent>75) & (dg_percent<=80))
		{
			cph=4.99;
			System.out.println(cph);
		}
		else if((dg_percent>80) & (dg_percent<=90))
		{
			cph=5.58;
			System.out.println(cph);
		}
		else if((dg_percent>90) & (dg_percent<=100))
		{
			cph=6.18;
			System.out.println(cph);
		}
		chp_value_from_OEM=cph;
	}
	else if((assetCode.contains("40")) & (assetCode.contains(name_mahindra)))

	{
		dg_kva=dg_kva_40;
		if((dg_percent>10) & (dg_percent<=25))
		{
			cph=4.5;
			System.out.println(cph);
		}
		else if((dg_percent>25) & (dg_percent<=30))
		{
			cph=4.67;
			System.out.println(cph);
		}
		else if((dg_percent>30) & (dg_percent<=40))
		{
			cph=5;
			System.out.println(cph);
		}
		else if((dg_percent>40) & (dg_percent<=50))
		{
			cph=5.34;
			System.out.println(cph);
		}
		else if((dg_percent>50) & (dg_percent<=60))
		{
			cph=6.14;
			System.out.println(cph);
		}
		else if((dg_percent>60) & (dg_percent<=70))
		{
			cph=6.93;
			System.out.println(cph);
		}
		else if((dg_percent>70) & (dg_percent<=75))
		{
			cph=7.33;
			System.out.println(cph);
		}
		else if((dg_percent>75) & (dg_percent<=80))
		{
			cph=7.84;
			System.out.println(cph);
		}
		else if((dg_percent>80) & (dg_percent<=90))
		{
			cph=8.87;
			System.out.println(cph);
		}
		else if((dg_percent>90) & (dg_percent<=100))
		{
			cph=9.9;
			System.out.println(cph);
		}
		chp_value_from_OEM=cph;
	}
	else if((assetCode.contains("25")) & (assetCode.contains(name_eicher)))
	{

		dg_kva=dg_kva_25;
		if((dg_percent>10) & (dg_percent<=25))
		{
			cph=3.58;
			System.out.println(cph);
		}
		else if((dg_percent>25) & (dg_percent<=30))
		{
			cph=3.58;
			System.out.println(cph);
		}
		else if((dg_percent>30) & (dg_percent<=40))
		{
			cph=3.58;
			System.out.println(cph);
		}
		else if((dg_percent>40) & (dg_percent<=50))
		{
			cph=5.38;
			System.out.println(cph);
		}
		else if((dg_percent>50) & (dg_percent<=60))
		{
			cph=4.02;
			System.out.println(cph);
		}
		else if((dg_percent>60) & (dg_percent<=70))
		{
			cph=4.47;
			System.out.println(cph);
		}
		else if((dg_percent>70) & (dg_percent<=75))
		{
			cph=4.69;
			System.out.println(cph);
		}
		else if((dg_percent>75) & (dg_percent<=80))
		{
			cph=4.99;
			System.out.println(cph);
		}
		else if((dg_percent>80) & (dg_percent<=90))
		{
			cph=5.58;
			System.out.println(cph);
		}
		else if((dg_percent>90) & (dg_percent<=100))
		{
			cph=6.18;
			System.out.println(cph);
		}
		chp_value_from_OEM=cph;
	}
	else if((assetCode.contains("15")) & (assetCode.contains(name_eicher)))

	{
		dg_kva=dg_kva_40;
		System.out.println(dg_kva);
		System.out.println(dg_kva);
		if((dg_percent>10) & (dg_percent<=25))
		{
			cph=2.2;
			System.out.println(cph);
		}
		else if((dg_percent>25) & (dg_percent<=30))
		{
			cph=2.2;
			System.out.println(cph);
		}
		else if((dg_percent>30) & (dg_percent<=40))
		{
			cph=2.2;
			System.out.println(cph);
		}
		else if((dg_percent>40) & (dg_percent<=50))
		{
			cph=2.2;
			System.out.println(cph);
		}
		else if((dg_percent>50) & (dg_percent<=60))
		{
			cph=2.52;
			System.out.println(cph);
		}
		else if((dg_percent>60) & (dg_percent<=70))
		{
			cph=2.52;
			System.out.println(cph);
		}
		else if((dg_percent>70) & (dg_percent<=75))
		{
			cph=3;
			System.out.println(cph);
		}
		else if((dg_percent>75) & (dg_percent<=80))
		{
			cph=3.16;
			System.out.println(cph);
		}
		else if((dg_percent>80) & (dg_percent<=90))
		{
			cph=3.48;
			System.out.println(cph);
		}
		else if((dg_percent>90) & (dg_percent<=100))
		{
			cph=3.8;
			System.out.println(cph);
		}

		chp_value_from_OEM=cph;
	}
		
		
//		else if((assetCode.contains("25")) & (assetCode.contains(name_kirloskar)))
//
//		{
//			dg_kva=dg_kva_40;
//			System.out.println(dg_kva);
//		
//		if((dg_percent>10) & (dg_percent<=20))
//		{
//			cph=2.5;
//			System.out.println(cph);
//		}
//		else if((dg_percent>20) & (dg_percent<=25))
//		{
//			cph=2.8;
//			System.out.println(cph);
//		}
//		else if((dg_percent>25) & (dg_percent<=30))
//		{
//			cph=3.2;
//			System.out.println(cph);
//		}
//		else if((dg_percent>30) & (dg_percent<=40))
//		{
//			cph=5.3;
//			System.out.println(cph);
//		}
//	else if((dg_percent>40) & (dg_percent<=50))
//	{
//		cph=5.3;
//		System.out.println(cph);
//	}
//	else if((dg_percent>50) & (dg_percent<=60))
//	{
//		cph=5.3;
//		System.out.println(cph);
//	}
//		else if((dg_percent>60) & (dg_percent<=70))
//		{
//			cph=6.8;
//			System.out.println(cph);
//		}
//	else if((dg_percent>70) & (dg_percent<=75))
//	{
//		cph=6.8;
//		System.out.println(cph);
//	}
//	else if((dg_percent>75) & (dg_percent<=80))
//	{
//		cph=6.8;
//		System.out.println(cph);
//	}
//		else if((dg_percent>80) & (dg_percent<=90))
//		{
//			cph=8.9;
//			System.out.println(cph);
//		}
	
//	else if((dg_percent>90) & (dg_percent<=100))
//	{
//		cph=8.9;
//		System.out.println(cph);
//	}
//		
//		
//		chp_value_from_OEM=cph;
//	}		

	System.out.println("chp_value_from_OEM : "+chp_value_from_OEM);
}


public static void getDgkva(String dgAssetcode) 
{
	double dg_kva_15=15;
	double dg_kva_25=25;
	double dg_kva_40=40;
	assetCode=dgAssetcode;
	double cph=0;
	double dg_capacity=0;
	String name_eicher="eicher";
	String name_mahindra="mahindra";
//	String name_kirolskar="kirolskar";

	System.out.println(assetCode);

	if((assetCode.contains("25")) & (assetCode.contains(name_mahindra)))
	{
		dg_kva=dg_kva_25;


	}
	else if((assetCode.contains("40")) & (assetCode.contains(name_mahindra)))

	{
		dg_kva=dg_kva_40;
	}
	else if((assetCode.contains("25")) & (assetCode.contains(name_eicher)))
	{

		dg_kva=dg_kva_25;

	}
	else if((assetCode.contains("15")) & (assetCode.contains(name_eicher)))

	{
		dg_kva=dg_kva_15;

	}		


//else if((assetCode.contains("40")) & (assetCode.contains(name_kirolskar)))
//
//{
//	dg_kva=dg_kva_40;
//
//}		
	System.out.println(dg_kva);
}


public String getDeviceAssetCodecount(String macid)
{
	String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+macid+"\"}";
//	String payload ="{\"from\":1715625000000,\"to\":1715711399999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"6618c839ddb3da111ecfacd8\"}}";
	//System.out.println(payload);
	//System.out.println(jsonpath);
	Response response = given()
			.header("Content-type", "application/json")
			.and()
			.header("Eid", eid)
			.and()
			.header("apiKey", apiKey)
			.and()
			.header("Authorization", BearerToken)
			.and()
			.body(payload)
			.when()
//			.post(generic_events_uri)
			.post(generic_events_uri)
			.then()
			.extract().response();
	sa.assertEquals(response.statusCode(),200);
	List<Object> deviceList=response.getBody().jsonPath().getList("data.device");
	String count = response.jsonPath().getString("count"); 
	return count;
	// checking the count of the assets in debug view, if 0 assets are there count is 0
}
public void  getDeviceAssetCode(String macid)
{
	String totalcount=getDeviceAssetCodecount(macid);
	System.out.println(totalcount+"totalcount");
	String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+macid+"\"}";
//	String payload ="{\"from\":1715625000000,\"to\":1715711399999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"6618c839ddb3da111ecfacd8\"}}";
	//System.out.println(payload);
	//System.out.println(jsonpath);
	Response response = given()
			.header("Content-type", "application/json")
			.and()
			.header("Eid", eid)
			.and()
			.header("apiKey", apiKey)
			.and()
			.header("Authorization", BearerToken)
			.and()
			.body(payload)
			.when()
			.post(generic_events_uri)
//			.post(generic_events_uri+"?skip=0&limit="+totalcount)
			.then()
			.extract().response();
	sa.assertEquals(response.statusCode(),200);
	List<Object> deviceList=response.getBody().jsonPath().getList("data.device");
	int count = response.jsonPath().getInt("count"); // checking the count of the assets in debug view, if 0 assets are there count is 0

	String deviceId="";
	try {
		System.out.println("deviceId :"+deviceId);
		deviceId=deviceList.get(0).toString();
		System.out.println("Device Id : "+deviceId);
		System.out.println(generic_get_device+deviceId);
	    
//		System.out.println("First packet : " +response.prettyPrint());
		System.out.println("First packet Count : " +count);


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
			.header("Eid", eid)
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


}


