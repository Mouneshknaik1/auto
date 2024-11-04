package com.ldc.prod.dashboard;
import java.time.Instant;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import static io.restassured.RestAssured.given;
import java.text.ParseException;

import java.awt.Event;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Objects;

import com.DataValidation.operations.PageOperations;

import io.restassured.response.Response;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class LDC extends BaseClassApi {

	public static List currentdatList=null;
	public static List acldatList=null;
	public static List<String> getcurrentdatList;
	public static List<String> getcurrentmacList;
	public static List<String> getdeviceidList;
	public static List<String> getdevicenameList;

	public static Response response=null;
	String datapath;
	public static int writecount=1;
	public static int rowcount=1;

	public static int writecount_live=1;
	public static int writecount_avg=1;

	public static List<String> siteDatList=null;
	public static List<String> siteNameList =null;

	public static String slavmac="";
	public static int dataPacketCount = 0;
	public static String jsonactiveenergyblockload="data.meter.ACTIVEENERGY_BLOCKLOAD";
	public static float activeenergyblockload_diff=0.0000000f;

	public static List activeenergylist=null;
	public static int sumofactiveenergyblockload = 0;

	public static Double activeenergyblockloadsum;
	public static Double totalenergysum=0.0;
	public static float totalSum = 0.00000f;
	public static Double current_sum = 0.0;
	public static float totalBatterysum = 0.00000f;

	public static float totalActiveEnergyBlockLoad_test;
	public static float battery_charged_value;
	public static float battery1_charged_value;
	public static float battery2_charged_value;
	public static float sumof_battery1;
	public static float avgof_battery1;
	public static float sumof_battery2;
	public static float avgof_battery2;
	public static float alltime_battery_charged_Value;
	public static float appliances_value;
	public static double test_value;
	public static double battery_perc;
	public static float battery1_total_input_energy_first;
	public static float battery2_total_input_energy_first;
	public static float battery1_total_discharge_energy_first;
	public static float battery2_total_discharge_energy_first;
	public static double total_Discharge_kwh;
	public static float battery1_total_load_energy_first;
	public static float battery2_total_load_energy_first;
	public static double total_load_energy_kwh;
	public static float battery1_total_charge_energy_first;
	public static float battery2_total_charge_energy_first;
	public static double total_charge_energy_kwh;

	public static double cost_of_loss_kwh;

	public static double battery_soc;
	public static float battery1_soc;
	public static float battery2_soc;

	public static float kpi_current_sum = 0.00000f;
	public static float kpi_totalSum = 0.00000f;
	public static float kpi_battery_charged_value;
	public static float kpi_battery1_charged_value;
	public static float kpi_battery2_charged_value;
	public static float kpi_appliances_value;
	public static float kpi_alltime_battery_charged_Value;
	public static double kpi_total_Discharge_kwh;
	public static double kpi_total_load_energy_kwh;
	public static double kpi_total_charge_energy_kwh;
	public static double kpi_cost_of_loss_kwh;
	public static float currentConsumption;
	public static float energyConsumption;

	public static float total_Charge_energy;
	public static float total_load_energy;
	public static float total_input_energy;
	public static float total_discharge_energy;
	public static float energy_loss;


	@Test
	public void getSitedatPath() {
		String site="";

		site=sitename;
		uri = base_uri + user_id + "/acls";
		SoftAssert sa = new SoftAssert();
		response = given().
				header("Content-type", "application/json")
				.and()
				.header("Eid",eid)
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("apiKey", apiKey).
				and()
				.header("Authorization", BearerToken)
				.and()
				.when()
				.get(uri)
				.then()
				.extract()
				.response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("ACLs retrieved successfully for user.", response.jsonPath().getString("message"));
		List<String> name = response.jsonPath().getList("data.dat.name");
		List<String> acldatList = response.jsonPath().getList("data.dat.path");
		//System.out.println(path);
		//System.out.println(name);
		for (int i = 0; i < name.size(); i++) {
			String s = name.get(i).toString();
			if (s.equalsIgnoreCase(site)) {
				//System.out.println(s);
				//System.out.println(acldatList.get(i).toString());
				datapath = acldatList.get(i).toString();
				//System.out.println(datapath);
				getcurrentDat(datapath,acldatList);
				//break;

			}

		}
		//				System.out.println(getcurrentdatList);
		//		//for(int c=0;c<acldatList.size();c++)
		int i=0;
		int c=0;
		for(String ab:getcurrentdatList)
		{

			String macid=getcurrentmacList.get(c).toString();
			String sitedat= acldatList.get(i).toString();

			//System.out.println(acldatList.indexOf(ab));
			i=acldatList.indexOf(ab);
			String site1= name.get(i).toString();
			System.out.println(/************************************/);
			System.out.println("site1 : " +site1);	
			System.out.println("macid : " +macid);
			slavmac=macid;
			String deviceid=get_device_id(macid);
//			System.out.println("deviceid : " +deviceidis);

			////			get_device_id(macid);
			////			if("PAKDwUxCcrkFUu5Y_3".equals(slavmac)) 
			////			{

//			smartMeterConsumption_day();
//			smartMeterConsumption();
//			alltime();
//			alltime_energy_consumption();
//			battery();
//			overview();
			LDCDataCalculation(site1, slavmac);
//			test(site1, slavmac);
//			battery1_charge();
//			battery2_charge();			
//			overview_calculation_battery1();
//			overview_calculation_battery2();
			//			}

			System.out.println(/************************************/);
			c++;
		}

		sa.assertAll();
	}


	//		@org.testng.annotations.Test(dependsOnMethods = "getSitedatPath")
	public void smartMeterConsumption_day()
	{
		String device="";
		String Slave_id=slavmac;

		uri = exec_uri+"accounts/api/events/getSmartmeterConsumption";

		try {
			//			String payload="{\"timestamp\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"granularityType\":\"day\",\"meterId\":\"IHM10050192\"}";

			String payload="{\"timestamp\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"granularityType\":\"day\",\"meterId\":\"IHM10050192\"}";

			System.out.println("smartMeterConsumption_day payload : " +payload);
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
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			sa.assertEquals("Devicestatuses retrieved successfully.", response.jsonPath().getString("message"));


			List<Float> totalActiveEnergyBlockLoad = response.getBody().jsonPath().getList("data.totalActiveEnergyBlockLoad");
			List<Float> currentConsumptions = response.getBody().jsonPath().getList("data.currentConsumptions");

			currentConsumption = currentConsumptions.get(0);
			energyConsumption = totalActiveEnergyBlockLoad.get(0);

			System.out.println("totalActiveEnergyBlockLoad grid imported yesterday kwh:"+totalActiveEnergyBlockLoad.size()+"--"+totalActiveEnergyBlockLoad);
			System.out.println("currentConsumptions day:"+currentConsumptions.size()+"--"+currentConsumptions);

		}

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}


	public void smartMeterConsumption()
	{
		String device="";
		String Slave_id=slavmac;

		uri = exec_uri+"accounts/api/events/getSmartmeterConsumption";


		try {
			String payload="{\"timestamp\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"meterId\":\"IHM10050192\"}";

			//			String payload="{\"timestamp\":{\"from\":"+from_date_yesterday+",\"to\":"+to_date_yesterday+"},\"meterId\":\"IHM10050192\"}";

			System.out.println("smartMeterConsumption payload : " +payload);
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
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			sa.assertEquals("Devicestatuses retrieved successfully.", response.jsonPath().getString("message"));


			List totalActiveEnergyBlockLoad = response.getBody().jsonPath().getList("data.totalActiveEnergyBlockLoad");
			List currentConsumptions = response.getBody().jsonPath().getList("data.currentConsumptions");


			for(int i=0;i<totalActiveEnergyBlockLoad.size();i++)
			{
				if(totalActiveEnergyBlockLoad!=null)
				{
					totalActiveEnergyBlockLoad_test = (float) totalActiveEnergyBlockLoad.get(0);
				}
				else
				{
					totalActiveEnergyBlockLoad_test = 0.0f;
				}
			}

			System.out.println("totalActiveEnergyBlockLoad_test-----------------"+totalActiveEnergyBlockLoad_test);
			System.out.println("currentConsumptions:"+currentConsumptions.size()+"--"+currentConsumptions);

		}


		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}



	public void alltime()
	{
		String device="";
		String uri = exec_uri+"accounts/api/events/restore/alltime\r\n"
				+ "";


		try {
			//			String payload="{\"dat\":\"66910c9a2d2bde2b9b650fba\",\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"}}";
			String payload="{\"dat\":\"657037f6881e0054497cd4ec\",\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"}}";
//			String payload="{\"dat\":\"657037f6881e0054497cd4ec\",\"time\":{\"from\":1727029800000,\"to\":1729698370032}}";
			System.out.println("Alltime Payload : " +payload);
			
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
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			sa.assertEquals("All time details retrieved successfully.", response.jsonPath().getString("message"));


			List TotalChargeEnergy = response.getBody().jsonPath().getList("data.TotalChargeEnergy");
			List<Number> TodayLoadEnergy = response.getBody().jsonPath().getList("data.TodayLoadEnergy", Number.class);
			List<Number> TotalInputEnergy = response.getBody().jsonPath().getList("data.TotalInputEnergy", Number.class);
			List<Number> TotalDischargeEnergy = response.getBody().jsonPath().getList("data.TotalDischargeEnergy", Number.class);
			List<Number> EnergyLoss = response.getBody().jsonPath().getList("data.EnergyLoss", Number.class);


//			total_Charge_energy = TotalChargeEnergy.get(0).floatValue();
//			total_load_energy = TodayLoadEnergy.get(0).floatValue();
//			total_input_energy = TotalInputEnergy.get(0).floatValue();
//			total_discharge_energy = TotalDischargeEnergy.get(0).floatValue();
//			energy_loss = EnergyLoss.get(0).floatValue();

			System.out.println("TotalChargeEnergy:"+TotalChargeEnergy.size()+"--"+TotalChargeEnergy);
			System.out.println("TodayLoadEnergy:"+TodayLoadEnergy.size()+"--"+TodayLoadEnergy);
			System.out.println("TodayLoadEnergy:"+TodayLoadEnergy.size()+"--"+TodayLoadEnergy);
			System.out.println("TotalInputEnergy battery charged from Grid :"+TotalInputEnergy.size()+"--"+TotalInputEnergy);
			System.out.println("TotalDischargeEnergy:"+TotalDischargeEnergy.size()+"--"+TotalDischargeEnergy);
			System.out.println("EnergyLoss:"+EnergyLoss.size()+"--"+EnergyLoss);

		}



		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}


	public void alltime_energy_consumption()
	{
		String device="";
		String uri = exec_uri+"accounts/api/events/restore/alltime-energy-consumption";

		System.out.println("alltime_energy_consumption payload : " +uri);
		String Slave_id=slavmac;

		try {
			//			String payload="{\"timestamp\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"granularityType\":\"day\",\"meterId\":\""+Slave_id+"\"}";
			String payload="{\"timestamp\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"granularityType\":\"day\",\"meterId\":\"IHM10050192\"}";

			System.out.println("alltime_energy_consumption payload : " +payload);
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
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			sa.assertEquals("Energy Consumtion retirved successfully.", response.jsonPath().getString("message"));


			List allTimeActiveEnergyDailyLoad = response.getBody().jsonPath().getList("data.allTimeActiveEnergyDailyLoad");



			System.out.println("allTimeActiveEnergyDailyLoad grid imported:"+allTimeActiveEnergyDailyLoad.size()+"--"+allTimeActiveEnergyDailyLoad);


		}

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}



	public void battery()
	{
		String device="";
		String uri = exec_uri+"accounts/api/events/restore/battery\r\n"
				+ "";


		try {
			//			String payload="{\"dat\":\"66910c9a2d2bde2b9b650fba\",\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"}}";
			String payload="{\"dat\":\"657037f6881e0054497cd4ec\",\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"}}";
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
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			sa.assertEquals("Battery details retrieved successfully.", response.jsonPath().getString("message"));


			List TodayChargeEnergy = response.getBody().jsonPath().getList("data.TodayChargeEnergy");
			List TodayLoadEnergy = response.getBody().jsonPath().getList("data.TodayLoadEnergy");
			List TodayInputEnergy = response.getBody().jsonPath().getList("data.TodayInputEnergy");
			List AvailableBatteryEnergy = response.getBody().jsonPath().getList("data.AvailableBatteryEnergy");



			System.out.println("TodayChargeEnergy:"+TodayChargeEnergy.size()+"--"+TodayChargeEnergy);
			System.out.println("TodayLoadEnergy:"+TodayLoadEnergy.size()+"--"+TodayLoadEnergy);
			System.out.println("TodayInputEnergy:"+TodayInputEnergy.size()+"--"+TodayInputEnergy);
			System.out.println("AvailableBatteryEnergy:"+AvailableBatteryEnergy.size()+"--"+AvailableBatteryEnergy);


		}

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}

	public void overview()
	{
		String device="";
		String uri = exec_uri+"accounts/api/events/restore/overview\r\n"
				+ "";


		try {
			//			String payload="{\"dat\":\"66910c9a2d2bde2b9b650fba\",\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"}}";
			String payload="{\"dat\":\"657037f6881e0054497cd4ec\",\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"}}";

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
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			sa.assertEquals("Overview details retrieved successfully.", response.jsonPath().getString("message"));


			List TodayChargeEnergy = response.getBody().jsonPath().getList("data.TodayChargeEnergy");
			List TodayLoadEnergy = response.getBody().jsonPath().getList("data.TodayLoadEnergy");
			List TodayEnergyConsumedFromGridByRestore = response.getBody().jsonPath().getList("data.TodayEnergyConsumedFromGridByRestore");
			List SelfpoweredSolarEnergy = response.getBody().jsonPath().getList("data.SelfpoweredSolarEnergy");
			List AppliancesCount = response.getBody().jsonPath().getList("data.AppliancesCount");



			System.out.println("TodayChargeEnergy:"+TodayChargeEnergy.size()+"--"+TodayChargeEnergy);
			System.out.println("TodayLoadEnergy:"+TodayLoadEnergy.size()+"--"+TodayLoadEnergy);
			System.out.println("TodayEnergyConsumedFromGridByRestore:"+TodayEnergyConsumedFromGridByRestore.size()+"--"+TodayEnergyConsumedFromGridByRestore);
			System.out.println("SelfpoweredSolarEnergy:"+SelfpoweredSolarEnergy.size()+"--"+SelfpoweredSolarEnergy);
			System.out.println("AppliancesCount:"+AppliancesCount.size()+"--"+AppliancesCount);

		}

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}




	public void overview_calculation_battery1()
	{
		String device="";
		String uri = exec_uri+"accounts/api/events/search";


		try {
			String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"657041b135644b53ef83931e\"}}";
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
					.body(payload)
					.when()
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			int count = response.getBody().jsonPath().getInt("count");
			System.out.println("Count : " +count);
			sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
			//			int step=500;

			List activeenergy = new ArrayList();
			List battery_status =new ArrayList();



			List<Float> total_charge_energy = response.getBody().jsonPath().getList("data.meter.total_charge_energy", Float.class);
			List<Float> total_discharge_energy = response.getBody().jsonPath().getList("data.meter.total_discharge_energy", Float.class);
			List<Float> total_load_energy = response.getBody().jsonPath().getList("data.meter.total_load_energy", Float.class);


			System.out.println("total_charge_energy:"+total_charge_energy.size()+"--"+total_charge_energy);
			System.out.println("total_discharge_energy:"+total_discharge_energy.size()+"--"+total_discharge_energy);
			System.out.println("total_load_energy:"+total_load_energy.size()+"---"+total_load_energy);

			Float total_charge_energy_first=0.0f;
			Float total_charge_energy_last=0.0f;

			if (total_charge_energy != null && !total_charge_energy.isEmpty()) {
				total_charge_energy_first = total_charge_energy.get(0);
			} else {
				System.out.println("No data found for total_input_energy.");
			}

			if (total_charge_energy != null && !total_charge_energy.isEmpty()) {
				total_charge_energy_last = total_charge_energy.get(total_charge_energy.size()-1);
			} else {
				System.out.println("No data found for total_input_energy.");
			}
			
//			if (total_discharge_energy != null && !total_discharge_energy.isEmpty()) {
//				total_charge_energy_last = (float) (total_discharge_energy.size()-1);
//			} else {
//				System.out.println("No data found for total_discharge_energy.");
//			}


			//			        if (total_load_energy != null && !total_load_energy.isEmpty()) {
			//			        	total_load_energy_first = total_load_energy.get(0);
			//			        } else {
			//			            System.out.println("No data found for total_discharge_energy.");
			//			        }



			//					Float total_charge_energy_first = total_charge_energy.get(0);
			//					Float total_charge_energy_last = total_charge_energy.get(total_charge_energy.size()-1);

			battery1_charged_value = total_charge_energy_first-total_charge_energy_last;

			System.out.println("battery1_charged_value : " +battery1_charged_value);
		}

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}




	public void overview_calculation_battery2()
	{
		String device="";
		String uri = exec_uri+"accounts/api/events/search";


		try {
			String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"657041eda365d153f0871d3d\"}}";
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
					.body(payload)
					.when()
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			int count = response.getBody().jsonPath().getInt("count");
			System.out.println("Count : " +count);
			sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
			//			int step=500;

			List activeenergy = new ArrayList();
			List battery_status =new ArrayList();



			List<Float> total_charge_energy = response.getBody().jsonPath().getList("data.meter.total_charge_energy", Float.class);
			List<Float> total_discharge_energy = response.getBody().jsonPath().getList("data.meter.total_discharge_energy", Float.class);
			List total_load_energy = response.getBody().jsonPath().getList("data.meter.total_load_energy");



			System.out.println("total_charge_energy:"+total_charge_energy.size()+"--"+total_charge_energy);
			System.out.println("total_discharge_energy:"+total_discharge_energy.size()+"--"+total_discharge_energy);
			System.out.println("total_load_energy:"+total_load_energy.size()+"---"+total_load_energy);

			//				Float total_charge_energy_first = total_charge_energy.get(0);
			//				Float total_charge_energy_last = total_charge_energy.get(total_charge_energy.size()-1);
			//
			//				battery2_charged_value = total_charge_energy_first-total_charge_energy_last;
			//				
			//				System.out.println("total_charge_energy_first : " +total_charge_energy_first);
			//				System.out.println("total_charge_energy_last : " +total_charge_energy_last);
			//
			//				System.out.println("battery2_charged_value : " +battery2_charged_value);



			Float total_charge_energy_first=0.0f;
			Float total_charge_energy_last=0.0f;

			if (total_charge_energy != null && !total_charge_energy.isEmpty()) {
				total_charge_energy_first = total_charge_energy.get(0);
			} else {
				System.out.println("No data found for total_input_energy.");
			}

			if (total_charge_energy != null && !total_charge_energy.isEmpty()) {
				total_charge_energy_last = total_charge_energy.get(total_charge_energy.size()-1);
			} else {
				System.out.println("No data found for total_discharge_energy.");
			}
			



			//			        if (total_load_energy != null && !total_load_energy.isEmpty()) {
			//			        	total_load_energy_first = total_load_energy.get(0);
			//			        } else {
			//			            System.out.println("No data found for total_discharge_energy.");
			//			        }



			//					Float total_charge_energy_first = total_charge_energy.get(0);
			//					Float total_charge_energy_last = total_charge_energy.get(total_charge_energy.size()-1);

			battery2_charged_value = total_charge_energy_first-total_charge_energy_last;

			System.out.println("battery2_charged_value : " +battery2_charged_value);


			//			}
		}

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}





	public static void write_to_excel(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4, int cellnum5, int cellnum6, int cellnum7, int cellnum8, int cellnum9, int cellnum10, int cellnum11, int cellnum12, String site, String device_mac_id,double current_consumption,double energy_consumption, double battery1_charged_value, double battery2_charged_value, double battery_charged_value, double appliances_value, double alltime_battery_charged_Value, double total_Discharge_kwh, double total_load_energy_kwh, double total_charge_energy_kwh, double cost_of_loss_kwh)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/LDC/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);

			sheet = workbook.getSheet(sitename);

			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(device_mac_id);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(current_consumption);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(energy_consumption);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(battery1_charged_value);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(battery2_charged_value);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(battery_charged_value);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(appliances_value);			
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(alltime_battery_charged_Value);			
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(total_Discharge_kwh);			
			sheet.getRow(rownum).createCell(cellnum10).setCellValue(total_load_energy_kwh);
			sheet.getRow(rownum).createCell(cellnum11).setCellValue(total_charge_energy_kwh);			
			sheet.getRow(rownum).createCell(cellnum12).setCellValue(cost_of_loss_kwh);


			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void write_to_excel_kpi(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4, int cellnum5, int cellnum6, int cellnum7, int cellnum8, int cellnum9, int cellnum10, int cellnum11, int cellnum12, String site, String device_mac_id,double kpi_current_consumption,double kpi_energy_consumption, double kpi_battery1_charged_value, double kpi_battery2_charged_value, double kpi_battery_charged_value, double kpi_appliances_value, double kpi_alltime_battery_charged_Value, double kpi_total_Discharge_kwh, double kpi_total_load_energy_kwh, double kpi_total_charge_energy_kwh, double kpi_cost_of_loss_kwh)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/LDC/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);

			sheet = workbook.getSheet(sitename);

			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(device_mac_id);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(kpi_current_consumption);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(kpi_energy_consumption);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(kpi_battery1_charged_value);
			sheet.getRow(rownum).createCell(cellnum5).setCellValue(kpi_battery2_charged_value);
			sheet.getRow(rownum).createCell(cellnum6).setCellValue(kpi_battery_charged_value);
			sheet.getRow(rownum).createCell(cellnum7).setCellValue(kpi_appliances_value);			
			sheet.getRow(rownum).createCell(cellnum8).setCellValue(kpi_alltime_battery_charged_Value);			
			sheet.getRow(rownum).createCell(cellnum9).setCellValue(kpi_total_Discharge_kwh);			
			sheet.getRow(rownum).createCell(cellnum10).setCellValue(kpi_total_load_energy_kwh);
			sheet.getRow(rownum).createCell(cellnum11).setCellValue(kpi_total_charge_energy_kwh);			
			sheet.getRow(rownum).createCell(cellnum12).setCellValue(kpi_cost_of_loss_kwh);


			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}



	public void getcurrentDat(String datapath,List acldatList)
	{
		try {
			String payload="{\"flags\":{\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetType\":true},\"startsWith\":{\"datRegex\":\""+datapath+"\"}}";
			Response response1 = given()
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
			sa.assertEquals(200, response1.statusCode());
			sa.assertEquals("Devicestatuses retrieved successfully.", response1.jsonPath().getString("message"));
			List<String> macIdList= response1.jsonPath().getList("data.device.mac");
			List<String> nameList= response1.jsonPath().getList("data.device.name");
			List<String> currentdat = response1.jsonPath().getList("data.currentDat");
			List<String> acldatList1 = response.jsonPath().getList("data.dat.path");
			List<String> deviceIdList=response.jsonPath().getList("data.device._id");
			//System.out.println("acldatList1 : "+acldatList1.size());
			getcurrentdatList= new ArrayList();
			getcurrentmacList=new ArrayList();
			getdeviceidList=new ArrayList();
			//for(String s:acldatList1)
			for(int i=0;i<acldatList1.size();i++)
			{
				String s=acldatList1.get(i).toString();
				//System.out.println(s);
				//for(String cs:currentdat)
				for(int x=0;x<currentdat.size();x++)
				{
					String cs=currentdat.get(x).toString();
					String macid=macIdList.get(x).toString();
					//String deviceid=deviceIdList.get(x).toString();
					if(cs.equalsIgnoreCase(s))
					{
						//System.out.println(cs);
						getcurrentdatList.add(cs);
						getcurrentmacList.add(macid);

					}
				}

			}
			//System.out.println(getcurrentdatList);
			//System.out.println(getcurrentmacList);
			//System.out.println(macIdList);
			//System.out.println(nameList);
			//System.out.println(currentdat);
			//System.out.println(acldatList);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}




	public double parseStringvalue(String value)
	{
		double parsedValue=0;
		try {

			parsedValue=Double.valueOf(value);

		}
		catch (Exception e) {
			// TODO: handle exception
			parsedValue=0;
		}
		return parsedValue;

	}






	public void LDCDataCalculation(String store,String id)
	{
		String device="";

		//getStoredat(store);
		String macid=id;
		System.out.println("mac id is : " +macid);

		//		String payload="{\"flags\":{\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetType\":true},\"startsWith\":{\"datRegex\":\""+datapath+"\"}}";
		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true},\"startsWith\":{\"datRegex\":\""+datapath+"\"},\"device\":{\"model\":\"\",\"mac\":\"\",\"assetCode\":\"\",\"created\":{\"from\":\"\",\"to\":\"\"}},\"assetType\":{\"code\":\"\"}}";

		Response response1 = given()
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
				.post(uri_devicestatus+"?skip=1&limit=1")
				.then()
				.extract().response();
		sa.assertEquals(200, response1.statusCode());
		sa.assertEquals("Devicestatuses retrieved successfully.", response1.jsonPath().getString("message"));
		List<String> lpath= response1.jsonPath().getList("data.device.mac");
		List<String> lname= response1.jsonPath().getList("data.device.name");
		List<String> deviceidis= response1.jsonPath().getList("data.device._id");

		System.out.println("deviceidis : " +deviceidis);
		System.out.println("lname : " +lname);
		System.out.println("lpath : " +lpath);



		uri = generic_events_uri;


		System.out.println("API : " +uri);
		try {
			if(lpath.size()==0)
			{
				System.out.println("No devices onboarded");
			}
			else 

				for(int j=0;j<lpath.size();j++)
				{

					//					String deviceid= device_mac_id_list.get(j).toString();

					String s= lpath.get(j).toString();
					System.out.println("S -------------- " +s);
					String name=lname.get(j).toString();
					String Slave_id=slavmac;
					System.out.println("Slave_id -------------- " +Slave_id);
//					String deviceid=get_device_id(macid);
					String deviceid=get_device_id(macid);
					System.out.println("deviceid : " +deviceid);

					List<String> activeenergyblockload = new ArrayList();
					if(Slave_id.contains(device))
					{
						//						device=s;
						//						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+Slave_id+"\"}";
						//					payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"669112ae33fcc32b95541c2e\"}}";
						//						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+macid+"\"}}";
						//						payload="{"from":1728930600000,"to":1729016999999,"sort":{"location.timestamp":-1},"flags":{"isSortRequired":true,"isTotalCount":true},"type":"","device":{"_id":"65703ea3a365d153f087138d"}}";

//						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"66fbc5ca894d8edeb5c96a73\"}}";

						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceid+"\"}}";

						//						payload="{\"from\":1728930600000,\"to\":1729016999999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"66fbc5ca894d8edeb5c96a73\"}}";

						System.out.println("New payload : " +payload);
						System.out.println("Deviceid -------------- " +deviceid);

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
								.body(payload)
								.when()
								.post(uri)
								.then()
								.extract().response();
						sa.assertEquals(200, response.statusCode());
						int count = response.getBody().jsonPath().getInt("count");
						System.out.println("Count : " +count);
						sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
						int step=500;
						List activeenergy = new ArrayList();
						List battery_status =new ArrayList();


						for(int i=0;i<count;i+=step) {
							System.out.println(i+"-step");
							sa = new SoftAssert();
							String dyanmicuri=uri+"?skip="+i+"&limit="+step;
							System.out.println("dyanmicuri--"+dyanmicuri);
							Response responsnew = given()
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
									.post(dyanmicuri)
									.then()
									.extract().response();
							sa.assertEquals(200, responsnew.statusCode());
							sa.assertEquals("Events retrieved successfully.", responsnew.jsonPath().getString("message"));
							List total_charge_energy = responsnew.getBody().jsonPath().getList("data.meter.total_charge_energy");
							List total_discharge_energy = responsnew.getBody().jsonPath().getList("data.meter.total_discharge_energy");
							List total_load_energy = responsnew.getBody().jsonPath().getList("data.meter.total_load_energy");

							activeenergyblockload = responsnew.getBody().jsonPath().getList("data.meter.ACTIVEENERGY_BLOCKLOAD");
							//List<Float> activeenergydailyload = responsnew.getBody().jsonPath().getList("data.meter.ACTIVEENERGY_DAILYLOAD");
							List<String> current_ir = responsnew.getBody().jsonPath().getList("data.meter.CURRENT_IR");
							List<String> current_iy = responsnew.getBody().jsonPath().getList("data.meter.CURRENT_IY");
							List<String> current_ib = responsnew.getBody().jsonPath().getList("data.meter.CURRENT_IB");

							String current_ir_value = current_ir.get(0);

							String current_iy_value = current_iy.get(0);
							String current_ib_value = current_ib.get(0);

							// Convert the string values to Double
							double current_ir_value_double = nullchekerString(current_ir_value);
							double current_iy_value_double = nullchekerString(current_iy_value);
							double current_ib_value_double = nullchekerString(current_ib_value);

							current_sum = (current_ir_value_double)+(current_iy_value_double)+(current_ib_value_double);


							//							System.out.println("total_charge_energy:"+total_charge_energy.size()+"--"+total_charge_energy);
							//							System.out.println("total_discharge_energy:"+total_discharge_energy.size()+"--"+total_discharge_energy);
							//							System.out.println("total_load_energy:"+total_load_energy.size()+"---"+total_load_energy);
														System.out.println("activeenergyblockload:"+activeenergyblockload.size()+"---"+activeenergyblockload);
							//							//System.out.println("activeenergy:"+activeenergy.size()+"---"+activeenergy);
							//							System.out.println("current_ir:"+current_ir.size()+"---"+current_ir_value);
							//							System.out.println("current_iy:"+current_iy.size()+"---"+current_iy_value);
							//							System.out.println("current_ib:"+current_ib.size()+"---"+current_ib_value);

							System.out.println("current_sum:"+"---"+current_sum);




							//							System.out.println("Error parsing value: " + value);
						}


					}
					totalSum=0;
					for (String value : activeenergyblockload) {
						if (value != null) {
							try {
								totalSum += Float.parseFloat(value); // Parse String to Float
							} catch (NumberFormatException e) {

								System.out.println("Error parsing value: " + value);

							}

						}

					}
					System.out.println("Total sum of active energy block load: " + totalSum);
					//
					//					write_to_excel(ec_ops_outputfile_ldc,writecount,0,1,2,3,4,store,id,current_sum,totalSum,totalActiveEnergyBlockLoad_test);
					//					writecount++;
				}			

		}
		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}

		//		return sum;
		//		sa.assertAll();
	}



	private Double nullchekerString(String value) {
		if(value!=null) {
			return Double.parseDouble(value);
		}else {
			return 0.00;
		}
	
	}
	public void battery1_charge()
	{
		String device="";
		String uri = exec_uri+"accounts/api/events/search";


		try {
			String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"657041b135644b53ef83931e\"}}";
			//			String payload="{\"from\":1728930600000,\"to\":1729016999999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"657041eda365d153f0871d3d\"}}";
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
					.body(payload)
					.when()
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			int count = response.getBody().jsonPath().getInt("count");
			System.out.println("Count : " +count);
			sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
			//			int step=500;

			List activeenergy = new ArrayList();
			List battery_status =new ArrayList();



			List<Float> total_input_energy = response.getBody().jsonPath().getList("data.meter.total_input_energy");
			List<Float> total_discharge_energy = response.getBody().jsonPath().getList("data.meter.total_discharge_energy");
			List<Float> total_load_energy = response.getBody().jsonPath().getList("data.meter.total_load_energy");
			List<Float> total_charge_energy = response.getBody().jsonPath().getList("data.meter.total_charge_energy");
			List<Integer> battery_soc_value = response.getBody().jsonPath().getList("data.meter.battery_soc");



			//				sum of last packet of both batteries.

			//				System.out.println("total_input_energy:"+total_input_energy.size()+"--"+total_input_energy);
			//				System.out.println("total_discharge_energy:"+total_discharge_energy.size()+"--"+total_discharge_energy);
			//				System.out.println("total_load_energy:"+total_load_energy.size()+"---"+total_load_energy);
			//				System.out.println("battery_soc_value:"+battery_soc_value.size()+"---"+battery_soc_value);

			//				battery1_total_input_energy_first = total_input_energy.get(0);
			//				battery1_total_discharge_energy_first = total_discharge_energy.get(0);
			//				battery1_total_load_energy_first = total_load_energy.get(0);
			//				battery1_total_charge_energy_first = total_charge_energy.get(0);
			//				battery1_soc = battery_soc_value.get(0);

			System.out.println("battery1_soc : " +battery1_soc);


			//--------------------

			if (total_input_energy != null && !total_input_energy.isEmpty()) {
				battery1_total_input_energy_first = total_input_energy.get(0);
			} else {
				System.out.println("No data found for total_input_energy.");
			}

			if (total_discharge_energy != null && !total_discharge_energy.isEmpty()) {
				battery1_total_discharge_energy_first = total_discharge_energy.get(0);
			} else {
				System.out.println("No data found for total_discharge_energy.");
			}

			if (total_load_energy != null && !total_load_energy.isEmpty()) {
				battery1_total_load_energy_first = total_load_energy.get(0);
			} else {
				System.out.println("No data found for total_load_energy.");
			}

			if (total_charge_energy != null && !total_charge_energy.isEmpty()) {
				battery1_total_charge_energy_first = total_charge_energy.get(0);
			} else {
				System.out.println("No data found for total_charge_energy.");
			}

			if (battery_soc_value != null && !battery_soc_value.isEmpty()) {
				battery1_soc = battery_soc_value.get(0);
				System.out.println("battery1_soc : " + battery1_soc);
			} else {
				System.out.println("No data found for battery_soc_value.");
			}


		}


		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
	}



	public void battery2_charge()
	{
		String device="";
		String uri = exec_uri+"accounts/api/events/search";


		try {
			String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"657041eda365d153f0871d3d\"}}";
			//			String payload="{\"from\":1728930600000,\"to\":1729016999999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"657041b135644b53ef83931e\"}}";

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
					.body(payload)
					.when()
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response.statusCode());
			int count = response.getBody().jsonPath().getInt("count");
			System.out.println("Count : " +count);
			sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
			//			int step=500;

			List activeenergy = new ArrayList();
			List battery_status =new ArrayList();



			List<Float> total_input_energy = response.getBody().jsonPath().getList("data.meter.total_input_energy", Float.class);
			List<Float> total_discharge_energy = response.getBody().jsonPath().getList("data.meter.total_discharge_energy");
			List<Float> total_load_energy = response.getBody().jsonPath().getList("data.meter.total_load_energy");
			List<Float> total_charge_energy = response.getBody().jsonPath().getList("data.meter.total_charge_energy");
			List<Integer> battery_soc_value = response.getBody().jsonPath().getList("data.meter.battery_soc");


			//				System.out.println("total_input_energy:"+total_input_energy.size()+"--"+total_input_energy);				
			//				System.out.println("total_discharge_energy:"+total_discharge_energy.size()+"--"+total_discharge_energy);
			//				System.out.println("total_load_energy:"+total_load_energy.size()+"---"+total_load_energy);
			//				System.out.println("battery_soc_value:"+battery_soc_value.size()+"---"+battery_soc_value);


			//				battery2_total_input_energy_first = total_input_energy.get(0);
			//				battery2_total_discharge_energy_first = total_discharge_energy.get(0);
			//				battery2_total_load_energy_first = total_load_energy.get(0);
			//				battery2_total_charge_energy_first = total_charge_energy.get(0);
			//				battery2_soc = battery_soc_value.get(0);





			if (total_input_energy != null && !total_input_energy.isEmpty()) {
				battery2_total_input_energy_first = total_input_energy.get(0);
			} else {
				System.out.println("No data found for total_input_energy.");
			}

			if (total_discharge_energy != null && !total_discharge_energy.isEmpty()) {
				battery2_total_discharge_energy_first = total_discharge_energy.get(0);
			} else {
				System.out.println("No data found for total_discharge_energy.");
			}

			if (total_load_energy != null && !total_load_energy.isEmpty()) {
				battery2_total_load_energy_first = total_load_energy.get(0);
			} else {
				System.out.println("No data found for total_load_energy.");
			}

			if (total_charge_energy != null && !total_charge_energy.isEmpty()) {
				battery2_total_charge_energy_first = total_charge_energy.get(0);
			} else {
				System.out.println("No data found for total_charge_energy.");
			}

			if (battery_soc_value != null && !battery_soc_value.isEmpty()) {
				battery2_soc = battery_soc_value.get(0);
				System.out.println("battery2_soc : " + battery2_soc);
			} else {
				System.out.println("No data found for battery_soc_value.");
			}

			System.out.println("battery2_soc : " +battery2_soc);



		}

		catch (Exception e) {

			e.printStackTrace();// 




		}
	}



	public void test(String store, String id)
	{

		//
		battery_charged_value = battery1_charged_value + battery2_charged_value;
		System.out.println("battery_charged_value : " +battery_charged_value);

		//Grid value-Battery value
		appliances_value = Math.abs(totalSum - battery_charged_value);
		System.out.println("appliances_value : " +appliances_value);

		alltime_battery_charged_Value = battery1_total_input_energy_first + battery2_total_input_energy_first;
		System.out.println("alltime_battery_charged_Value : " +alltime_battery_charged_Value);

		
		total_Discharge_kwh = battery1_total_discharge_energy_first + battery2_total_discharge_energy_first;
		System.out.println("total_Discharge_kwh : " +total_Discharge_kwh);

		total_load_energy_kwh = battery1_total_load_energy_first + battery2_total_load_energy_first;
		System.out.println("total_load_energy_kwh : " +total_load_energy_kwh);

		
		total_charge_energy_kwh = battery1_total_charge_energy_first + battery2_total_charge_energy_first;
		System.out.println("total_charge_energy_kwh : " +total_charge_energy_kwh);

		cost_of_loss_kwh = ((alltime_battery_charged_Value-total_charge_energy_kwh)+(total_Discharge_kwh-total_load_energy_kwh))*10;
		System.out.println("cost_of_loss_kwh : " +cost_of_loss_kwh);

		//Battery pecentage
		battery_soc = (battery1_soc + battery2_soc)/2;
		System.out.println("battery_soc : " +battery_soc);

		kpi_current_sum = currentConsumption;
		kpi_totalSum = energyConsumption;

		System.out.println("kpi_current_sum : " +kpi_current_sum);
		System.out.println("kpi currentConsumptions : " +currentConsumption);




		kpi_battery_charged_value = total_Charge_energy;
		kpi_total_load_energy_kwh = total_load_energy;
		kpi_alltime_battery_charged_Value = total_input_energy;
		kpi_total_Discharge_kwh = total_discharge_energy;
		kpi_cost_of_loss_kwh = energy_loss;


		//		test_value = 261892;
		write_to_excel(ec_ops_outputfile_ldc,writecount,0,1,2,3,4,5,6,7,8,9,10,11,12, store, id, current_sum, totalSum, battery1_charged_value, battery2_charged_value, battery_charged_value, appliances_value, alltime_battery_charged_Value, total_Discharge_kwh, total_load_energy_kwh, total_charge_energy_kwh, cost_of_loss_kwh);

		//		writecount++;

		write_to_excel_kpi(kpi_ops_outputfile_ldc,writecount,0,1,2,3,4,5,6,7,8,9,10,11,12, store, id, kpi_current_sum, kpi_totalSum, kpi_battery1_charged_value, kpi_battery2_charged_value, kpi_battery_charged_value, kpi_appliances_value, kpi_alltime_battery_charged_Value, kpi_total_Discharge_kwh, kpi_total_load_energy_kwh, kpi_total_charge_energy_kwh, kpi_cost_of_loss_kwh);
		//		writecount++;


		//		rowcount++;
		//
		//		write_to_excel_test(ec_ops_outputfile_ldc,writecount,0,1,2, store, id, test_value);

		//		double eb_energy=Math.round(testenergyCalculation("657041eda365d153f0871d3d","Test","data.meter.total_charge_energy"));
		//		System.out.println("eb_energy :=------------------" +eb_energy);
	}
}
