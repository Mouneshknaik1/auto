package com.jcems.prod.dashboard;

import static io.restassured.RestAssured.given;

import java.awt.Event;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;


public class JCEMS_Cluster_id extends BaseClassApi {
	public static List currentdatList=null;
	public static List acldatList=null;
	public static List runHourList=null;
	public static List<String> getcurrentdatList;
	public static List<String> getcurrentmacList;
	public static List<String> getdeviceidList;
	public static Response response=null;
	String datapath;
	public static int writecount=1;
	public static int writecount_live=1;
	public static int writecount_avg=1;
	public static int sumofAchours = 0;
	public static int dataPacketCount = 0;
	public String assetDataMissingvalue="";
	public static int dataPacketCount_day = 144;
	public static int dataPacketCount_week = 1008;
	public static int dataPacketCount_month = 4464;
	public static int dataPacketCount_year = 0;
	public static List<String> siteDatList=null;
	public static List<String> siteNameList =null;
	public static double avg_powerfactor = 0;
	public static double TotalSum = 0;

	public static int avg_calc_days = 90;

	public static double avg_value = 0;
	public static double acrunhr_avg_value = 0;
	public static double dg_efficiency_avg_value = 0;
	public static double dg_fuel_consumed_avg_value = 0;
	public static int days=89;
	public static int no_events_days_count=0;

	public static String fuell_fill_event_type="jc_dg_fuel_fill";
	public static String fuell_theft_event_type="jc_dg_fuel_theft";
	public static String fuell_fill_json="data.meter.dg1FuelFillInPercentage";
	public static String fuell_theft_json="data.meter.dg1FuelTheftInPercentage";


	public static String json_path_sebLoadManager1ActiveEnergyKWH="data.meter.sebLoadManager1ActiveEnergyKWH";
	public static String json_path_dg1CumulativeEnergykWh="data.meter.dg1CumulativeEnergykWh";
	public static String json_path_eventTag = "data.meter.eventTag";
	public static String json_path_dg1RunHours="data.meter.dg1RunHours";
	public static String json_path_dg1FuelLevel="data.meter.dg1FuelLevel";
	public static String json_path_pf="data.meter.sebLoadManager1PowerFactor";

	public static double changeinEfficiency=0;
	public static double diffinEfficiency=0;


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
		//		System.out.println(getcurrentdatList.size());
		//for(int c=0;c<acldatList.size();c++)
		int i=0;
		int c=0;
		for(String ab:getcurrentdatList)
		{

			String macid=getcurrentmacList.get(c).toString();
			String sitedat= acldatList.get(i).toString();

//			System.out.println("sitedat : " +sitedat);
			i=acldatList.indexOf(ab);
			String site1= name.get(i).toString();
			System.out.println(/************************************/);
			System.out.println(site1);	
			System.out.println(macid);
			String deviceid=get_device_id(macid);
			System.out.println(deviceid);
			testMainCalcultion(site1,deviceid);
			//			acRunHrCalc();
			//energyComparision(site1,deviceid);
			//energy_90_days_avg(site1,deviceid);
			System.out.println(/************************************/);
			c++;
		}

		sa.assertAll();
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


	public void energyComparision(String site,String id)
	{

		double tankcapcity=0;
		double fuelprice=0;
		double storeareavalue=0;
		getStoredat(site);
		String start_today=todaysDate_start_millis;
		String end_today=todaysDate_end_millis;
		String start_yesterday=yesterdayDate_start_millis;
		String end_yesterday=yesterdayDate_end_millis;
		tankcapcity=parseStringvalue(fuelTankCapacity);
		fuelprice=parseStringvalue(fuelPrice);
		storeareavalue=parseStringvalue(areaOfStore);
		String macid=id;

		System.out.println(" SEB energy Consumption Comparison for :"+yesterdayDate_end_millis);
		//double yesterday_energy_seb=0;
		double todays_energy_seb=Math.round(testenergyCalculation(macid,todaysDate_start_millis,todaysDate_end_millis,json_path_sebLoadManager1ActiveEnergyKWH));
		System.out.println("todays_energy_seb : "+todays_energy_seb);


		double yesterday_energy_seb=Math.round(testenergyCalculation(macid,yesterdayDate_start_millis,yesterdayDate_end_millis,json_path_sebLoadManager1ActiveEnergyKWH));
		System.out.println("yesterday_energy_seb : "+yesterday_energy_seb);

		System.out.println(" +++++++++++++++++++++++++++++++++");

		System.out.println(" DG energy Consumption Comparison");
		double todays_energy_dg=Math.round(testenergyCalculation(macid,todaysDate_start_millis,todaysDate_end_millis,json_path_dg1CumulativeEnergykWh));
		System.out.println("todays_energy_dg : "+todays_energy_dg);

		double yesterday_energy_dg=Math.round(testenergyCalculation(macid,yesterdayDate_start_millis,yesterdayDate_end_millis,json_path_dg1CumulativeEnergykWh));
		System.out.println("yesterday_energy_dg : "+yesterday_energy_dg);

		System.out.println(" +++++++++++++++++++++++++++++++++");


		System.out.println(" DG Run Hour Comparison");
		double yesterday_runhr_dg=Math.round(testenergyCalculation(macid,yesterdayDate_start_millis,yesterdayDate_end_millis,json_path_dg1RunHours));
		System.out.println("yesterday_runhr_dg : "+yesterday_runhr_dg);
		double todays_runhr_dg=Math.round(testenergyCalculation(macid,todaysDate_start_millis,todaysDate_end_millis,json_path_dg1RunHours));
		System.out.println("todays_runhr_dg : "+todays_runhr_dg);
		System.out.println(" +++++++++++++++++++++++++++++++++");

		System.out.println(" AC Run Hour Comparison");
		double yesterday_runhr_ac=acrunhrs(macid,yesterdayDate_start_millis,yesterdayDate_end_millis);
		System.out.println("yesterday_runhr_ac : "+yesterday_runhr_ac);
		double todays_runhr_ac=acrunhrs(macid,todaysDate_start_millis,todaysDate_end_millis);
		//System.out.println("todaysDate_end_millis :"+todaysDate_end_millis);
		System.out.println("todays_runhr_ac : "+todays_runhr_ac);
		//double avg_90days_runhr_ac=Math.round(avgCalculation(macid,from_date_90day_avg_millis,todaysDate_end_millis,json_path_dg1RunHours));
		//System.out.println("avg_90days_runhr_dg : "+avg_90days_runhr_dg);

		System.out.println(" +++++++++++++++++++++++++++++++++");

		System.out.println("AC Efficiency Comparison");
		double todays_acrunhrs=Double.valueOf(todays_runhr_ac);
		double todays_acEfficiency= acEnergyEffeciencyCalc(todays_energy_seb,todays_acrunhrs);
		System.out.println("todays_acEfficiency : "+todays_acEfficiency);


		double yesterday_acrunhrs=Double.valueOf(yesterday_runhr_ac);
		double yesterdays_acEfficiency= acEnergyEffeciencyCalc(yesterday_energy_seb,yesterday_acrunhrs);
		System.out.println("yesterdays_acEfficiency : "+yesterdays_acEfficiency);
		System.out.println(" +++++++++++++++++++++++++++++++++");

		diffEfficiencyCalc(yesterdays_acEfficiency,todays_acEfficiency);
		double ac_diffinEfficiency=diffinEfficiency;
		double ac_changeinEfficiency=changeinEfficiency;
		System.out.println("AC Efficiency usage : "+ac_diffinEfficiency);
		System.out.println("AC Efficiency usage in % : "+ac_changeinEfficiency);
		System.out.println(" +++++++++++++++++++++++++++++++++");

		System.out.println("Dg Efficiency Comparison");
		double yesterdays_dgFuelConsumed=Math.round(testenergyCalculation(macid,yesterdayDate_start_millis,yesterdayDate_end_millis,json_path_dg1FuelLevel));
		//System.out.println("yesterdays_dgFuelConsumed : "+yesterdays_dgFuelConsumed);

		double yesterday_fuelfill=fuelCalculation(macid,yesterdayDate_start_millis,yesterdayDate_end_millis,fuell_fill_event_type,fuell_fill_json);
		//System.out.println("yesterday_fuelfill : "+yesterday_fuelfill);

		double yesterday_fuelremoved=fuelCalculation(macid,yesterdayDate_start_millis,yesterdayDate_end_millis,fuell_theft_event_type,fuell_theft_json);
		//System.out.println("yesterday_fuelremoved : "+yesterday_fuelremoved);

		double yesterday_fuel_addremoved_difference=Math.abs(yesterday_fuelfill-yesterday_fuelremoved);
		//System.out.println("yesterday_fuel_addremoved_difference : "+yesterday_fuel_addremoved_difference);

		double yesterdays_dgFuelConsumedvalue=Math.round(dgFuelConsumedValueCalc(tankcapcity,yesterdays_dgFuelConsumed,yesterday_fuel_addremoved_difference));
		System.out.println("yesterdays_dgFuelConsumedvalue : "+yesterdays_dgFuelConsumedvalue);

		double yesterday_dgFuelefficiencyvalue= dgFuelefficiencyCalc(yesterday_energy_dg,yesterdays_dgFuelConsumedvalue);
		System.out.println("Yesterdays_dgFuelefficiencyvalue : "+yesterday_dgFuelefficiencyvalue);

		double todays_dgFuelConsumed=Math.round(testenergyCalculation(macid,todaysDate_start_millis,todaysDate_end_millis,json_path_dg1FuelLevel));
		//System.out.println("todays_dgFuelConsumed : "+todays_dgFuelConsumed);

		double todays_fuelfill=fuelCalculation(macid,todaysDate_start_millis,todaysDate_end_millis,fuell_fill_event_type,fuell_fill_json);
		//System.out.println("todays_fuelfill : "+todays_fuelfill);

		double todays_fuelremoved=fuelCalculation(macid,todaysDate_start_millis,todaysDate_end_millis,fuell_theft_event_type,fuell_theft_json);
		//System.out.println("todays_fuelremoved : "+todays_fuelremoved);

		double todays_fuel_addremoved_difference=Math.abs(todays_fuelfill-todays_fuelremoved);
		//System.out.println("todays_fuel_addremoved_difference : "+todays_fuel_addremoved_difference);

		double todays_dgFuelConsumedvalue=Math.round(dgFuelConsumedValueCalc(tankcapcity,todays_dgFuelConsumed,todays_fuel_addremoved_difference));
		System.out.println("todays_dgFuelConsumedvalue : "+todays_dgFuelConsumedvalue);

		double todays_dgFuelefficiencyvalue= dgFuelefficiencyCalc(todays_energy_dg,todays_dgFuelConsumedvalue);
		System.out.println("todays_dgFuelefficiencyvalue : "+todays_dgFuelefficiencyvalue);

		diffEfficiencyCalc(yesterday_dgFuelefficiencyvalue,todays_dgFuelefficiencyvalue);
		double dg_diffinEfficiency=diffinEfficiency;
		double dg_changeinEfficiency=changeinEfficiency;

		System.out.println("DG Efficiency usage : "+dg_diffinEfficiency);
		System.out.println("DG Efficiency usage in % : "+dg_changeinEfficiency);
		System.out.println(" +++++++++++++++++++++++++++++++++");

		System.out.println(" DG Fuel Cosumption Comparission");
		diffEfficiencyCalc(yesterdays_dgFuelConsumedvalue,todays_dgFuelConsumedvalue);
		double dgfuelconsumption_diff=diffinEfficiency;
		double dgfuelconsumption_changeinvalue=changeinEfficiency;

		System.out.println("DG Fuel consumption Efficiency usage : "+dgfuelconsumption_diff);
		System.out.println("DG Fuel consumption Efficiency usage in % : "+dgfuelconsumption_changeinvalue);
		System.out.println(" +++++++++++++++++++++++++++++++++");
		write_to_excel(ec_ops_outputfile_live,writecount_live,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,site,yesterday_energy_seb,todays_energy_seb,yesterday_energy_dg,todays_energy_dg,yesterday_runhr_dg,todays_runhr_dg,yesterday_runhr_ac,todays_runhr_ac,todays_acEfficiency,ac_changeinEfficiency,todays_dgFuelConsumedvalue,yesterdays_dgFuelConsumedvalue,dgfuelconsumption_diff,dgfuelconsumption_changeinvalue,todays_dgFuelefficiencyvalue,yesterday_dgFuelefficiencyvalue,dg_diffinEfficiency,dg_changeinEfficiency);
		//write_to_excel(ec_ops_outputfile_live,writecount_live,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,site,yesterday_energy_seb,todays_energy_seb,yesterday_energy_dg,todays_energy_dg,yesterday_runhr_dg,todays_runhr_dg,yesterday_runhr_ac,todays_runhr_ac,																																																																	                                                                                                                     																												
		writecount_live++;

	}

	public int acrunhrs(String id)
	{
		int runhrs=0;
		int acrunhrslast=acRunHrCalc("last", id);
		System.out.println(runHourList);
		System.out.println("acrunhrslast :"+acrunhrslast);
		int acrunhrsfirst=acRunHrCalc("first", id);
		System.out.println(runHourList);
		System.out.println("acrunhrsfirst :"+acrunhrsfirst);
		runhrs=acrunhrsfirst-acrunhrslast;
		return runhrs;
	}

	public double acrunhrs(String id,String from,String to)
	{

		double runhrs=0;

		double acrunhrslast=acRunHrCalc("last",from,to,id);
		System.out.println("acrunhrslast :"+acrunhrslast);
		double acrunhrsfirst=acRunHrCalc("first",from,to,id);
		System.out.println("acrunhrsfirst :"+acrunhrsfirst);
		runhrs=acrunhrsfirst-acrunhrslast;
		return runhrs;
	}



	public double dgFuelConsumedValueCalc(double tankcapacity, double fuelconsumed,double addedremoveddiff)
	{
		double dgFuelConsumedValue=0;
		try {
			dgFuelConsumedValue=((fuelconsumed/100)*tankcapacity)+((addedremoveddiff/100)*tankcapacity);
			//System.out.println("DgFuelConsumedValue : "+dgFuelConsumedValue);
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		return dgFuelConsumedValue;
	}

	public double dgFuelefficiencyCalc(double energy, double fuel)
	{
		double dgefficiency=0;
		try {
			dgefficiency=energy/fuel;
			//System.out.println("dgefficiency : "+dgefficiency);
		}
		catch (Exception e) {
			dgefficiency=0;
			// TODO: handle exception
		}

		return dgefficiency;
	}


	public double dgEnergyConsumedperSqftCalc(double energy, String areaOfStore)
	{
		double dgenergyConsumepersqft=0;
		double area=Double.valueOf(areaOfStore);
		try {
			dgenergyConsumepersqft=energy/area;
			//System.out.println("dgenergyConsumepersqft : "+dgenergyConsumepersqft);
		}
		catch (Exception e) {
			dgenergyConsumepersqft=0;
			// TODO: handle exception
		}

		return dgenergyConsumepersqft;
	}


	public double acEnergyEffeciencyCalc(double energy, double acrunhrs)
	{
		double acTariffunit=0.7;
		double acEnergyEffeciency=0;
		double area=Double.valueOf(areaOfStore);
		try {
			acEnergyEffeciency=(energy/acrunhrs)*acTariffunit;

		}
		catch (Exception e) {
			acEnergyEffeciency=0;
			// TODO: handle exception
		}

		return acEnergyEffeciency;
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
	public double testenergyCalculation(String id,String day,String jsonpath)
	{
		double energy_generated = 0;
		String first_packet_value = null;
		String last_packet_value = null;
		String macid=id;	
		List<Float> energylistnonzero = new ArrayList<>();
		SoftAssert sa = new SoftAssert();
		try {


			String payload ="";
			if(day.contains("currentDate"))
			{
				//payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
				payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";
//								payload="{\"from\":1715452200000,\"to\":1716056999999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"64eded45b57a06739bdb482b\"}}";
				System.out.println("IF payload : " +payload);

			}
			else
			{
				//payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
				payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";
//				payload = "{\"from\":1715452200000,\"to\":1716056999999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"64eded45b57a06739bdb482b\"}}";
				System.out.println("Else payload : " +payload);
			}

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
			
			
			int count = response.getBody().jsonPath().getInt("count");
			System.out.println("Count is : " +count);

			int skip=count-50;
			
			Response first_packet_response = given()
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
//					.post(generic_events_uri)

					.post(generic_events_uri+"?skip="+skip+"&limit=100")
					.then()
					.extract().response();
			sa.assertEquals(first_packet_response.statusCode(),200);
			
			
			//			List eventTag = response.getBody().jsonPath().getList(jsonpath);
			System.out.println("jsonpath" +jsonpath);
			System.out.println("first_packet_response : " +first_packet_response);

			List eventtag = response.getBody().jsonPath().getList("data.meter.eventTag");
			List energylistold = response.getBody().jsonPath().getList(jsonpath);				
			List energylist = new ArrayList();
			String searchElement = "anomaly";
			System.out.println("Event Tag values are : " +eventtag);
			for(int i=0;i<eventtag.size();i++) {
//					System.out.println(eventtag.get(i) == null);
//				 if (eventtag.get(i).equals(searchElement)) {
//		                System.out.println("Anomaly found at index: " + i);
////		                break; // Stop searching after finding the first occurrence
//		            }
				if((eventtag.get(i) != null)) {
//					excludelist.add((Float) energylist.get(i));
				}else{
//					System.out.println(energylist.get(i));
//					if((energylistold.get(i) != null)) {
						energylist.add(energylistold.get(i));
//					}
				}
			}

			System.out.println("event list:"+energylistold);
			System.out.println("include:"+energylist);


			System.out.println("energylist updated: "+energylist);


			if(count==0)
			{
				energy_generated=0;
				dataPacketCount=0;

				//System.out.println("energy_generated : "+energy_generated);
				

			}
			else {
				dataPacketCount=count;


				System.out.println("energylist updated: "+energylist);


				List<Float> wb1 = new ArrayList<>();


				while(energylist.remove(null))
				{
				}

				/*
				for(int x=0;x<energylist.size();x++)
				{
					Float value=(Float) energylist.get(x);
					if(value!=0)
					{
						energylistnonzero.add(value);
					}
				}
				 */
				if(energylist.size()==0)
				{
					energy_generated=0;
				}
				else
				{
					for(Object obj:energylist)
					{


						wb1.add((float)((Number) obj).floatValue());

					}
					System.out.println(wb1);
					for(int i=0;i<wb1.size();i++)
					{
						if(wb1.get(i)!=0)	
						{
							last_packet_value=wb1.get(i).toString();
							break;
						}
						else
						{
							last_packet_value="0";
						}
					}
					System.out.println("last_packet_value:"+last_packet_value);
					for(int i=wb1.size()-1;i>0;i--)
					{
						if(wb1.get(i)!=0)	
						{
							first_packet_value=wb1.get(i).toString();
							System.out.println("first_packet_value:"+first_packet_value);
							break;
						}
						else
						{
							first_packet_value="0";
						}
					}

				}


			}
			try {
				energy_generated = Math.abs(Double.parseDouble(last_packet_value)-Double.parseDouble(first_packet_value));
				if(energy_generated<=0)
				{
					energy_generated=0;
				}
			}catch (NumberFormatException e){
				e.printStackTrace();
				energy_generated=0;
				opshours=0;

				System.out.println("not a number"); 
			} 

			//			break;
			//			}//added
		}
		catch (Exception e) {
			e.printStackTrace();

			//System.out.println(macid+" : "+"No events found that match with the search keys.");
			energy_generated=0;

		}

		sa.assertAll();
		return energy_generated;

	}
	
	
	public double testenergyCalculationDGfuelconsumption(String id, String day, String jsonpath) {
        double energy_generated = 0;
        String first_packet_value = null;
        String last_packet_value = null;
        
        Integer firstPacket = 0 ;
        Integer lastPacket = 0;	
        String macid = id;
        List<Float> energylistnonzero = new ArrayList<>();
        SoftAssert sa = new SoftAssert();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            // Define the date range
            String dateRangeStart = start_date;
            String dateRangeEnd = end_date;

            System.out.println("startDate ex:"+dateRangeStart);
            System.out.println("endDate ex:"+dateRangeEnd);
            
            // Parse the start and end dates
            Date startDate = dateFormat.parse(dateRangeStart);
            Date endDate = dateFormat.parse(dateRangeEnd);

            System.out.println("startDate :"+startDate);
            System.out.println("endDate :"+endDate);

            
            // Calendar to iterate through each day
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1); // Adding one day to start date
            startDate = calendar.getTime();

            System.out.println("Adjusted startDate :" + startDate);
            System.out.println("endDate :" + endDate);
            
            calendar.setTime(startDate);

            
            List<Float> firstAndLastPackets = new ArrayList<>();

			while(firstAndLastPackets.remove(null))
			{
			}
			
            System.out.println("firstAndLastPackets: " +firstAndLastPackets);
//            Double TotalSum=0.0;
            while (!calendar.getTime().after(endDate)) {
                Date currentDate = calendar.getTime();
                String formattedDate = dateFormat.format(currentDate);

                // Set from and to timestamps for the current day
                long from_date_in_milli = currentDate.getTime();
                long to_date_in_milli = from_date_in_milli + (24 * 60 * 60 * 1000) - 1; // End of the day

                long to_date_in_milli1 = from_date_in_milli + (24 * 60 * 60 * 1000) - 1; // End of the day

                System.out.println("from_date_in_milli :" +from_date_in_milli);
                System.out.println("to_date_in_milli1 :" +to_date_in_milli1);

                String payload = "{\"from\":" + from_date_in_milli + ",\"to\":" + to_date_in_milli1 + ",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"" + macid + "\"}}";
                System.out.println("Payload for date " + formattedDate + ": " + payload);

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
                        .then()
                        .extract().response();
                sa.assertEquals(response.statusCode(), 200);

                List<Integer> packets = response.getBody().jsonPath().getList("data.meter.dg1FuelLevel");

                System.out.println("packets List :" +packets);
                Integer dgfuelpacket_difference=0;
                if (packets != null && !packets.isEmpty()) {
                	firstPacket =packets.get(packets.size() - 1);
                	lastPacket =packets.get(0);

//                    firstAndLastPackets.add(firstPacket);
//                    firstAndLastPackets.add(lastPacket);
                    System.out.println(firstPacket+"-"+lastPacket);
                    dgfuelpacket_difference = Math.abs(firstPacket)- Math.abs(lastPacket);
                    System.out.println("Fuel Difference : " +dgfuelpacket_difference);
                    
                    
                    

                }
                else
                {
                	firstPacket =0 ;
                	lastPacket = 0;
                	dgfuelpacket_difference = 0;
                }
                double fuelfill=fuelCalculation(macid,fuell_fill_event_type,fuell_fill_json,from_date_in_milli,to_date_in_milli1);
        		System.out.println("fuelfill: "+fuelfill);
        		double fueltheft=fuelCalculation(macid,fuell_theft_event_type,fuell_theft_json,from_date_in_milli,to_date_in_milli1);
        		System.out.println("fueltheft : "+fueltheft);
                calendar.add(Calendar.DATE, 1);
                double fuel_difference = Math.abs(fuelfill)-Math.abs(fueltheft);
                double totalFuleconsumed=Math.abs((fuel_difference)+(dgfuelpacket_difference));
                System.out.println("Difference in fuel: " +fuel_difference);
                System.out.println("totalFuleconsumed-"+totalFuleconsumed);
                System.out.println("pricePerUnitlist----"+pricePerUnitlist);
                TotalSum+=Math.abs((totalFuleconsumed*250)/100);

            }

            // Print the results
            System.out.println("TotalSum:"+TotalSum);
//            for (Float packet : firstAndLastPackets) {
//                System.out.println(packet);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return energy_generated;
    }

	
	
	public double testenergyCalculationDGfuel(String id,String day,String jsonpath)
	{
		double energy_generated = 0;
		String first_packet_value = null;
		String last_packet_value = null;
		String macid=id;	
		List<Float> energylistnonzero = new ArrayList<>();
		SoftAssert sa = new SoftAssert();
		try {


			String payload ="";
			if(day.contains("currentDate"))
			{
				//payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
//				payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";
//								payload="{\"from\":1715452200000,\"to\":1716056999999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"64eded45b57a06739bdb482b\"}}";
				payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+macid+"\"}}";

				System.out.println("IF payload : " +payload);

			}
			else
			{
				//payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
//				payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";
				payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+macid+"\"}}";

				//				payload = "{\"from\":1715452200000,\"to\":1716056999999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"64eded45b57a06739bdb482b\"}}";
				System.out.println("Else payload : " +payload);
			}

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
			
			
			int count = response.getBody().jsonPath().getInt("count");
			System.out.println("Count is : " +count);

			int skip=count-50;
			
			Response first_packet_response = given()
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
					.post(generic_events_uri+"?skip="+skip+"&limit=100")
					.then()
					.extract().response();
			sa.assertEquals(first_packet_response.statusCode(),200);
			
			
			//			List eventTag = response.getBody().jsonPath().getList(jsonpath);
			System.out.println("jsonpath" +jsonpath);
			System.out.println("first_packet_response : " +first_packet_response);

			List eventtag = response.getBody().jsonPath().getList("data.meter.eventTag");
			List energylistold = response.getBody().jsonPath().getList(jsonpath);				
			 List energylist = new ArrayList();
			  String searchElement = "anomaly";
			System.out.println("Event Tag values are : " +eventtag);
			for(int i=0;i<eventtag.size();i++) {
//					System.out.println(eventtag.get(i) == null);
//				 if (eventtag.get(i).equals(searchElement)) {
//		                System.out.println("Anomaly found at index: " + i);
////		                break; // Stop searching after finding the first occurrence
//		            }
				if((eventtag.get(i) != null)) {
//					excludelist.add((Float) energylist.get(i));
				}else{
//					System.out.println(energylist.get(i));
//					if((energylistold.get(i) != null)) {
						energylist.add(energylistold.get(i));
//					}
				}
			}

			System.out.println("event list:"+energylistold);
			System.out.println("include:"+energylist);


			System.out.println("energylist updated: "+energylist);


			if(count==0)
			{
				energy_generated=0;
				dataPacketCount=0;


				//System.out.println("energy_generated : "+energy_generated);
				

			}
			else {
				dataPacketCount=count;


				System.out.println("energylist updated: "+energylist);


				List<Float> wb1 = new ArrayList<>();


				while(energylist.remove(null))
				{
				}

				/*
				for(int x=0;x<energylist.size();x++)
				{
					Float value=(Float) energylist.get(x);
					if(value!=0)
					{
						energylistnonzero.add(value);
					}
				}
				 */
				if(energylist.size()==0)
				{
					energy_generated=0;
				}
				else
				{
					for(Object obj:energylist)
					{


						wb1.add((float)((Number) obj).floatValue());

					}
					System.out.println(wb1);
					for(int i=0;i<wb1.size();i++)
					{
						if(wb1.get(i)!=0)	
						{
							last_packet_value=wb1.get(i).toString();
							break;
						}
						else
						{
							last_packet_value="0";
						}
					}
					System.out.println("last_packet_value:"+last_packet_value);
					for(int i=wb1.size()-1;i>0;i--)
					{
						if(wb1.get(i)!=0)	
						{
							first_packet_value=wb1.get(i).toString();
							System.out.println("first_packet_value:"+first_packet_value);
							break;
						}
						else
						{
							first_packet_value="0";
						}
					}

				}


			}
			try {
				energy_generated = Math.abs(Double.parseDouble(last_packet_value)-Double.parseDouble(first_packet_value));
				if(energy_generated<=0)
				{
					energy_generated=0;
				}
			}catch (NumberFormatException e){
				e.printStackTrace();
				energy_generated=0;
				opshours=0;

				System.out.println("not a number"); 
			} 

			//			break;
			//			}//added
		}
		catch (Exception e) {
			e.printStackTrace();

			//System.out.println(macid+" : "+"No events found that match with the search keys.");
			energy_generated=0;

		}

		sa.assertAll();
		return energy_generated;

	}


	public double testenergyCalculation(String id,String fromDate,String toDate,String jsonpath)
	{


		double energy_generated = 0;
		String last_active_packet_value = null;
		String first_active_packet_value = null;
		String macid=id;	
		List<Float> energylistnonzero = new ArrayList<>();
		SoftAssert sa = new SoftAssert();
		try {

			String payload ="";

			payload="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";

			System.out.println(payload);

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
			System.out.println(response.prettyPrint());
			sa.assertEquals(response.statusCode(),200);
			int count = response.jsonPath().getInt("count");

			

			System.out.println("response : " +response.prettyPrint());

			System.out.println("Count of testenergy " +count);
			
			if(count==0)
			{
				energy_generated=0;
				dataPacketCount=0;

				//System.out.println("energy_generated : "+energy_generated);
				//
			}
			else {
				dataPacketCount=count;
				List energylist = response.getBody().jsonPath().getList(jsonpath);

				System.out.println("energylist : "+energylist);
							
				List<Float> wb1 = new ArrayList<>();


				while(energylist.remove(null))
				{
				}


				/*
				for(int x=0;x<energylist.size();x++)
				{
					Float value=(Float) energylist.get(x);
					if(value!=0)
					{
						energylistnonzero.add(value);
					}
				}
				 */
				//System.out.println("energylistnonzero"+energylistnonzero);

				if(energylist.size()==0)
				{
					energy_generated=0;

				}
				else
				{
					for(Object obj:energylist)
					{
						wb1.add((float)((Number) obj).floatValue());

					}
					//System.out.println("wb1"+wb1);
					for(int i=0;i<wb1.size();i++)
					{
						if(wb1.get(i)!=0)	
						{
							last_active_packet_value=wb1.get(i).toString();
							break;
						}
						else
						{
							last_active_packet_value="0";
						}
					}
					System.out.println("last_active_packet_value:"+last_active_packet_value);
					for(int i=wb1.size()-1;i>0;i--)
					{
						if(wb1.get(i)!=0)	
						{
							first_active_packet_value=wb1.get(i).toString();
								System.out.println("first_active_packet_value:"+first_active_packet_value);
							break;
						}
						else
						{
							first_active_packet_value="0";
						}
					}

				}


			}
			try {
				energy_generated = Math.abs(Double.parseDouble(last_active_packet_value)-Double.parseDouble(first_active_packet_value));
				if(energy_generated<=0)
				{
					energy_generated=0;
				}
			}catch (NumberFormatException e){
				e.printStackTrace();
				energy_generated=0;


				System.out.println("not a number"); 
			} 

		}
		//		}//775
		catch (Exception e) {
			//e.printStackTrace();

			//System.out.println(macid+" : "+"No events found that match with the search keys.");
			energy_generated=0;
			opshours=0;
		}

		sa.assertAll();
		return energy_generated;

	}


	//	@Test
	public int acRunHrCalc(String packet,String id)
	{
		runHourList=null;
		runHourList = new ArrayList<>();
		try {
			sumofAchours=0;
			int ac_run_hr_value=0;
			String macid=id;
			//String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+macid+"\"}";
			//String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
			String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";
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
			//sa.assertEquals(response.jsonPath().getString("message"),"Events retrieved successfully.");
			//System.out.println(	"Count : "+response.jsonPath().getString("count"));
			int count = response.jsonPath().getInt("count");

			//System.out.println("packet count : "+count);
			String jsonpath="";
			for(int i=1;i<=ac_count;i++)
			{

				int sumofhrs=0;
				String jsontext="";
				String acrhrvalue="";
				int acrhvalue=0;
				String s="ac"+i+"RunHour";
				int lastpacket=count-1;
				int firstpacket=0;
				if(packet.contains("last"))
				{
					jsonpath="data["+lastpacket+"].meter."+s;
					System.out.println("jsonpath : " +jsonpath);
				}
				else if(packet.contains("first"))
				{
					jsonpath="data["+firstpacket+"].meter."+s;
					System.out.println("jsonpath : " +jsonpath);

				}
				if(count==0)
				{
					sumofAchours=0;


					//System.out.println("Run Hours : "+sumofAchours);
					//
				}
				else {

					acrhrvalue = response.getBody().jsonPath().getString(jsonpath);

					if((acrhrvalue==null))
					{
						acrhvalue=0;
					}
					else
					{

						acrhvalue=Integer.valueOf(acrhrvalue);
						runHourList.add(acrhvalue);
						sumofAchours=acrhvalue+sumofAchours;
						//System.out.println(sumofAchours);
					}

				}
			}
			//System.out.println("sumofAchours : "+sumofAchours);
		}
		catch (Exception e) {
			sumofAchours=0;
			//System.out.println(sumofAchours);
		}
		return sumofAchours;
	}

	public int acRunHrCalc(String packet,String fromDate,String toDate,String id)
	{
		try {
			runHourList=null;
			runHourList = new ArrayList<>();
			sumofAchours=0;
			int ac_run_hr_value=0;
			String macid=id;
			//String payload ="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
			String payload="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";

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
			//sa.assertEquals(response.jsonPath().getString("message"),"Events retrieved successfully.");
			//System.out.println(	"Count : "+response.jsonPath().getString("count"));
			int count = response.jsonPath().getInt("count");
			//System.out.println("packet count : "+count);
			String jsonpath="";
			for(int i=1;i<=ac_count;i++)
			{

				int sumofhrs=0;
				String jsontext="";
				String acrhrvalue="";
				int acrhvalue=0;
				String s="ac"+i+"RunHour";
				int lastpacket=count-1;
				int firstpacket=0;
				if(packet.contains("last"))
				{
					jsonpath="data["+lastpacket+"].meter."+s;

				}
				else if(packet.contains("first"))
				{
					jsonpath="data["+firstpacket+"].meter."+s;
				}
				if(count==0)
				{
					sumofAchours=0;


					//System.out.println("Run Hours : "+sumofAchours);
					//
				}
				else {

					acrhrvalue = response.getBody().jsonPath().getString(jsonpath);

					if((acrhrvalue==null))
					{
						acrhvalue=0;
					}
					else
					{

						acrhvalue=Integer.valueOf(acrhrvalue);
						runHourList.add(acrhvalue);
						sumofAchours=acrhvalue+sumofAchours;
						//System.out.println(sumofAchours);
					}

				}
			}
			//System.out.println("sumofAchours : "+sumofAchours);
		}
		catch (Exception e) {
			sumofAchours=0;
			//System.out.println(sumofAchours);
		}
		return sumofAchours;
	}

	public void testMainCalcultion(String store,String id)

	{

		double tankcapcity=0;
		double fuelprice=0;
		double storeareavalue=0;
		double avgTariff=8;
		getStoredat(store);
//		getStoredat(pricePerUnitlist);
//		System.out.println("pricePerUnitlist current : " +pricePerUnitlist);

		tankcapcity=parseStringvalue(fuelTankCapacity);
		fuelprice=parseStringvalue(fuelPrice);
//		avgTariff=parseStringvalue(pricePerUnitist);
//		System.out.println("avgTariff --------: " +avgTariff);
//		storeareavalue=parseStringvalue(areaOfStore);
		String macid=id;

		double eb_energy=Math.round(testenergyCalculation(macid,"Test",json_path_sebLoadManager1ActiveEnergyKWH));
		System.out.println("eb_energy : "+eb_energy);

		System.out.println("Tank capacity is : " +tankcapcity);
		System.out.println("fuelprice is  : " +fuelprice);
		System.out.println("avgTariff is  : " +avgTariff);

		double dg_energy=Math.round(testenergyCalculation(macid,"Test",json_path_dg1CumulativeEnergykWh));
		System.out.println("dg_energy : "+dg_energy);

		double energy=eb_energy+dg_energy;
		System.out.println("Cumulative Energy : "+energy);

//		double dgFuelConsumed=testenergyCalculation(macid,"Test",json_path_dg1FuelLevel);
//		System.out.println("dgFuelConsumed : "+dgFuelConsumed);
		
//		double dgFuelConsumed=testenergyCalculationDGfuel(macid,"Test",json_path_dg1FuelLevel);
//		System.out.println("dgFuelConsumed : "+dgFuelConsumed);
		
		double dgFuelConsumed=testenergyCalculationDGfuelconsumption(macid,"Test",json_path_dg1FuelLevel);
		System.out.println("dgFuelConsumed : "+dgFuelConsumed);
		
		double fuelfill=fuelCalculation(macid,fuell_fill_event_type,fuell_fill_json);
		System.out.println("fuelfill : "+fuelfill);

		double fuelremoved=fuelCalculation(macid,fuell_theft_event_type,fuell_theft_json);
		System.out.println("fuelremoved : "+fuelremoved);
		
//		double fuelfill=fuelCalculation(macid,fuell_fill_event_type,fuell_fill_json);
//		System.out.println("fuelfill : "+fuelfill);
//
//		double fuelremoved=fuelCalculation(macid,fuell_theft_event_type,fuell_theft_json);
//		System.out.println("fuelremoved : "+fuelremoved);

		double fuel_addremoved_difference=fuelfill-fuelremoved;
		System.out.println("fuel_addremoved_difference : "+fuel_addremoved_difference);

		double dgrunhr=testenergyCalculation(macid,"Test",json_path_dg1RunHours);
		System.out.println("dgrunhr : "+dgrunhr);

		double fuel=Math.abs(Math.round(dgFuelConsumedValueCalc(tankcapcity,dgFuelConsumed,fuel_addremoved_difference)));
		System.out.println("DG Fuel Consumed : "+fuel);

		double EnergyConsumedperSqftvalue=dgEnergyConsumedperSqftCalc(energy,areaOfStore);
		System.out.println("EnergyConsumedperSqftvalue : "+EnergyConsumedperSqftvalue);

		double dgFuelefficiencyvalue= dgFuelefficiencyCalc(energy,fuel);
		System.out.println("dgFuelefficiencyvalue : "+dgFuelefficiencyvalue);

//		double costInCured=(energy*avgTariff)+(fuel*fuelprice);
//		System.out.println("costInCured : "+costInCured);
		
		double costInCured=(energy*avgTariff)+(TotalSum*fuelprice);
		System.out.println("costInCured : "+costInCured);
		
		//int runhrs=acrunhrs(id);
		//System.out.println("AC Runhrs : "+runhrs);


		int runhrs=acRunHrList(id);
		System.out.println("AC Runhrs : "+runhrs);

		double acEnergyEffeciency= acEnergyEffeciencyCalc(energy,runhrs);
		System.out.println("acEnergyEffeciency : "+acEnergyEffeciency);

		int assetDataMissingcalc=dataPacketCount_day-dataPacketCount;
		double gap=0.000;
		if(assetDataMissingcalc==0)
		{
			assetDataMissingvalue="No";
			gap=0;
			System.out.println("Data Missing Gap : "+gap);
			System.out.println("assetDataMissingvalue "+assetDataMissingvalue );

		}
		else
		{
			assetDataMissingvalue="Yes";
			gap = ((assetDataMissingcalc*100)/(dataPacketCount_day));
			System.out.println("Data Missing Gap : "+gap);
			System.out.println("assetDataMissingvalue "+assetDataMissingvalue );
		}
		System.out.println("assetDataMissingvalue : "+assetDataMissingvalue);
		System.out.println("Data Missing Gap : "+gap);
		avgPfCalculation(macid);
		avg_powerfactor=Math.round(avg_powerfactor);
		System.out.println("avg_powerfactor : "+avg_powerfactor);

		write_to_excel(ec_ops_outputfile,writecount,0,1,2,3,4,5,6,7,8,9,10,11,12,13,store, eb_energy, dg_energy,energy,TotalSum,dgrunhr,EnergyConsumedperSqftvalue,dgFuelefficiencyvalue,costInCured,runhrs,acEnergyEffeciency,dataPacketCount,gap,avg_powerfactor);
		writecount++;
	}
	public double avgPfCalculation(String macid)
	{
		avg_powerfactor=0;
		//String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
		String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";
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
		int count = response.jsonPath().getInt("count");

		List powerFactor = response.getBody().jsonPath().getList("data.meter.sebLoadManager1PowerFactor");
		List<Double> wb2 = new ArrayList<>();
		//System.out.println(powerFactor);
		while(powerFactor.remove(null))
		{
		}
		for(Object obj:powerFactor)
		{
			wb2.add((Double)((Number) obj).doubleValue());

		}
		double total=0;
		//System.out.println(wb2);
		for(int i=0;i<wb2.size();i++)
		{
			if(wb2.get(i)!=0)
			{
				total=total+wb2.get(i);
				avg_powerfactor = total /wb2.size();   
			}

			else
			{
				avg_powerfactor=0.00;
			}
		}
		return avg_powerfactor;
	}

	public double avgCalculation(String macid,String fromDate,String toDate,String jsonpath,int days)
	{
		avg_value=0;
		double average=0;
		//String payload ="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
		String payload="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";
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
		int count = response.jsonPath().getInt("count");

		List powerFactor = response.getBody().jsonPath().getList(jsonpath);
		List<Double> wb2 = new ArrayList<>();
		//System.out.println(powerFactor);
		while(powerFactor.remove(null))
		{
		}
		for(Object obj:powerFactor)
		{
			wb2.add((Double)((Number) obj).doubleValue());

		}
		double total=0;
		//System.out.println(wb2);
		for(int i=0;i<wb2.size();i++)
		{
			if(wb2.get(i)!=0)
			{
				total=total+wb2.get(i);
				try
				{
					average=total /powerFactor.size();
					avg_value = average/days; 
				}
				catch (Exception e) {
					// TODO: handle exception
				}



			}

			else
			{
				avg_value=0.00;
			}
		}
		return avg_value;
	}


	//edited
	public double fuelCalculation(String macid, String eventType, String jsontype,long from_date_in_milli, long to_date_in_milli)
	{
//		System.out.println(from_date_in_milli+'-'+to_date_in_milli);
		
		double event_total=0;
		String payload ="";
		//payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+eventType+"\",\"meterId\":\""+macid+"\"}";
		payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+eventType+"\",\"device\":{\"_id\":\""+macid+"\"}}";

		System.out.println("fuelCalculation payload" +payload);

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
		int count = response.jsonPath().getInt("count");
		//System.out.println(count);
		List<String> fuel=response.getBody().jsonPath().getList(jsontype);
		System.out.println("Fuel list fuel: " +fuel);

		if(count==0)
		{
			event_total=0;
		}
		else
		{

			List<String> is = response.getBody().jsonPath().getList(jsontype);
			System.out.println("Fuel list jsontype: " +is);
			while(is.remove(null))
			{
			}

			List<Double> wb1 = new ArrayList<>();
			for(Object obj:is)
			{
				wb1.add((double)((Number) obj).floatValue());
//				System.out.println("fuel consumed : " +wb1);

			}

			//System.out.println(wb1);
			for(int i=0;i<wb1.size();i++)
			{
				event_total=wb1.get(i)+event_total;	
			}

		}
		System.out.println("event_total_fuel_filled :"+event_total);

		return event_total;
	}
	public double fuelCalculation(String macid, String eventType, String jsontype)
	{
//		System.out.println(from_date_in_milli+'-'+to_date_in_milli);
		
		double event_total=0;
		String payload ="";
		//payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+eventType+"\",\"meterId\":\""+macid+"\"}";
		payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+eventType+"\",\"device\":{\"_id\":\""+macid+"\"}}";

		System.out.println("fuelCalculation payload" +payload);

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
		int count = response.jsonPath().getInt("count");
		//System.out.println(count);
		List<String> fuel=response.getBody().jsonPath().getList(jsontype);
		System.out.println("Fuel list fuel: " +fuel);

		if(count==0)
		{
			event_total=0;
		}
		else
		{

			List<String> is = response.getBody().jsonPath().getList(jsontype);
			System.out.println("Fuel list jsontype: " +is);
			while(is.remove(null))
			{
			}

			List<Double> wb1 = new ArrayList<>();
			for(Object obj:is)
			{
				wb1.add((double)((Number) obj).floatValue());
//				System.out.println("fuel consumed : " +wb1);

			}

			//System.out.println(wb1);
			for(int i=0;i<wb1.size();i++)
			{
				event_total=wb1.get(i)+event_total;	
			}

		}
		System.out.println("event_total_fuel_filled :"+event_total);

		return event_total;
	}
	public void diffEfficiencyCalc(double yesterdaysEfficiency,double todaysEfficiency)
	{
		changeinEfficiency=0;
		diffinEfficiency=0;

		//diffinEfficiency=Math.abs(yesterdaysEfficiency-todaysEfficiency);
		diffinEfficiency=yesterdaysEfficiency-todaysEfficiency;
		try {
			changeinEfficiency=(diffinEfficiency/yesterdaysEfficiency)*100;
		}
		catch (Exception e) {
			changeinEfficiency=0;

			// TODO: handle exception
		}
	}
	
	@Test
	public double fuelCalculation(String macid, String fromDate,String toDate,String eventType, String jsontype )
	{
		double event_total=0;
		String payload ="";
		//payload ="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+eventType+"\",\"meterId\":\""+macid+"\"}";
		//ayload ="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+eventType+"\",\"meterId\":\""+macid+"\"}";
		payload="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+eventType+"\",\"device\":{\"_id\":\""+macid+"\"}}";
		System.out.println("fuelCalculation payload" +payload);

		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
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
		int count = response.jsonPath().getInt("count");
		//System.out.println(count);
		List<String> fuel=response.getBody().jsonPath().getList(jsontype);
		System.out.println("fuel list : " +fuel);

		if(count==0)
		{
			event_total=0;
		}
		else
		{
			try {

				List<String> is = response.getBody().jsonPath().getList(jsontype);
				System.out.println("fuelCalculation list is : " +is);
				while(is.remove(null))
				{
				}

				List<Double> wb1 = new ArrayList<>();
				for(Object obj:is)
				{
					wb1.add((double)((Number) obj).floatValue());

				}

				//System.out.println(wb1);
				for(int i=0;i<wb1.size();i++)
				{
					event_total=wb1.get(i)+event_total;	
				}
			}
			catch (Exception e) {
				event_total=0;
				// TODO: handle exception
			}
		}
		System.out.println("event_total_fuel_filled :"+event_total);

		return event_total;
	}


	//@org.testng.annotations.Test
	public void testavg()
	{
		String macid="I-AS-BGGN-JCO-0001";
		testavgcalc(macid,json_path_sebLoadManager1ActiveEnergyKWH);
		double seb_avg_value=avg_value;
		System.out.println("seb_avg_value : "+seb_avg_value);

		testavgcalc(macid,json_path_dg1CumulativeEnergykWh);
		double dg_avg_value=avg_value;
		System.out.println("dg_avg_value : "+dg_avg_value);
		double cumulative_energy_avg_value=seb_avg_value+dg_avg_value;
		System.out.println("cumulative_energy_avg_value : "+cumulative_energy_avg_value);

		testavgcalc(macid,json_path_dg1RunHours);
		double dgrunhr_avg_value=avg_value;
		System.out.println("dgrunhr_avg_value : "+dgrunhr_avg_value);
		//System.out.println("no_events_days_count : "+no_events_days_count);

		testavgcalc_ac(macid);
		double runhrs= acrunhr_avg_value;
		System.out.println("AC runhr avg value : "+acrunhr_avg_value);
	}

	public  void testavgcalc(String id,String path)
	{
		List diffvalue=null;
		diffvalue= new ArrayList<>();
		double sum=0;
		avg_value = 0;
		String macid=id;
		String jsonpath=path;
		for(int i =0;i<=days;i++)
		{
			int j=i+1;
			time(-j,0);
			String from_date_avg=getDate+" "+start_time;
			timeInMillis = timetomillisecondsforEnergyCalc(from_date_avg);
			//System.out.println("from_date_avg : "+from_date_avg);
			String from_date_avg_millis=timeInMillis+"000";
			//System.out.println("from_date_avg_millis : "+from_date_avg_millis);

			time(-i,0);
			String to_date_avg=getDate+" "+end_time;
			timeInMillis = timetomillisecondsforEnergyCalc(to_date_avg);
			//System.out.println("to_date_avg : "+to_date_avg);
			String toDate_avg_millis=timeInMillis+"999";
			//System.out.println("toDate_avg_millis : "+toDate_avg_millis);

			double energy=Math.round(testenergyCalculation(macid,from_date_avg_millis,toDate_avg_millis,jsonpath));
			diffvalue.add(energy);
			sum=sum+energy;	
			//System.out.println(sum);
		}
		//System.out.println("sum : "+sum);
		if(sum==0)
		{
			avg_value=0;
		}
		else
		{
			try {
				//avg=sum/(days-no_events_days_count);
				avg_value=sum/(days+1);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		System.out.println("List : "+diffvalue);
		//System.out.println("List Size : "+diffvalue.size());
		//System.out.println(sum);
		//System.out.println("avg :"+avg_value);

	}

	public double testavgcalc_ac(String id)
	{
		List acdiffvalue=null;
		acdiffvalue= new ArrayList<>();
		double sum=0;
		acrunhr_avg_value = 0;
		String macid=id;
		//days=90;

		for(int i =0;i<=days;i++)
		{
			int j=i+1;
			time(-j,0);
			String from_date_avg=getDate+" "+start_time;
			timeInMillis = timetomillisecondsforEnergyCalc(from_date_avg);
			//System.out.println("from_date_avg : "+from_date_avg);
			String from_date_avg_millis=timeInMillis+"000";
			//System.out.println("from_date_avg_millis : "+from_date_avg_millis);

			time(-i,0);
			String to_date_avg=getDate+" "+end_time;
			timeInMillis = timetomillisecondsforEnergyCalc(to_date_avg);
			//System.out.println("to_date_avg : "+to_date_avg);
			String toDate_avg_millis=timeInMillis+"999";
			//System.out.println("toDate_avg_millis : "+toDate_avg_millis);

			//double runhrs = acrunhrs(macid,from_date_avg_millis,toDate_avg_millis);

			double runhrs=acRunHrList(from_date_avg_millis,toDate_avg_millis,macid);
			//
			acdiffvalue.add(runhrs);
			sum=sum+runhrs;	
			//System.out.println("Sum of runhr for avg :"+sum);
		}

		if(sum==0)
		{
			acrunhr_avg_value=0;
		}
		else
		{
			try {
				//avg=sum/(days-no_events_days_count);
				acrunhr_avg_value=sum/(days+1);

			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		System.out.println("AC Runhrs List : "+acdiffvalue);
		System.out.println("Cumulative Ac runhr for avg :"+sum);
		//System.out.println("ac_avg_value :"+ac_avg_value);
		return acrunhr_avg_value;
	}

	public  void dg_avgcalc(String id,double tankcapcity,double dgenergy)
	{
		List diffvalue=null;
		diffvalue= new ArrayList<>();
		double sum=0;
		avg_value = 0;
		dg_fuel_consumed_avg_value=0;
		dg_efficiency_avg_value = 0;
		double dgFuelConsumedvalue=0;
		String macid=id;
		//String jsonpath=path;
		//for(int i =0;i<=1;i++)
		for(int i =0;i<=days;i++)
		{
			int j=i+1;
			time(-j,0);
			String from_date_avg=getDate+" "+start_time;
			timeInMillis = timetomillisecondsforEnergyCalc(from_date_avg);
			//System.out.println("from_date_avg : "+from_date_avg);
			String from_date_avg_millis=timeInMillis+"000";
			//System.out.println("from_date_avg_millis : "+from_date_avg_millis);

			time(-i,0);
			String to_date_avg=getDate+" "+end_time;
			timeInMillis = timetomillisecondsforEnergyCalc(to_date_avg);
			//System.out.println("to_date_avg : "+to_date_avg);
			String toDate_avg_millis=timeInMillis+"999";
			//System.out.println("toDate_avg_millis : "+toDate_avg_millis);

			double dgFuelConsumed=Math.round(testenergyCalculation(macid,from_date_avg_millis,toDate_avg_millis,json_path_dg1FuelLevel));
			//System.out.println("yesterdays_dgFuelConsumed : "+yesterdays_dgFuelConsumed);

			double fuelfill=fuelCalculation(macid,from_date_avg_millis,toDate_avg_millis,fuell_fill_event_type,fuell_fill_json);
			//System.out.println("yesterday_fuelfill : "+yesterday_fuelfill);

			double fuelremoved=fuelCalculation(macid,from_date_avg_millis,toDate_avg_millis,fuell_theft_event_type,fuell_theft_json);
			//System.out.println("yesterday_fuelremoved : "+yesterday_fuelremoved);

			double fuel_addremoved_difference=Math.abs(fuelfill-fuelremoved);
			//System.out.println("yesterday_fuel_addremoved_difference : "+yesterday_fuel_addremoved_difference);

			dgFuelConsumedvalue=Math.round(dgFuelConsumedValueCalc(tankcapcity,dgFuelConsumed,fuel_addremoved_difference));
			//System.out.println("dgFuelConsumedvalue : "+dgFuelConsumedvalue);
			diffvalue.add(dgFuelConsumedvalue);
			sum=sum+dgFuelConsumedvalue;
			//System.out.println(sum);
		}
		//System.out.println("sum : "+sum);
		if(sum==0)
		{
			dg_fuel_consumed_avg_value=0;
		}
		else
		{
			try {
				//avg=sum/(days-no_events_days_count);
				dg_fuel_consumed_avg_value=sum/(days+1);


			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		System.out.println("List : "+diffvalue);
		System.out.println("List Size : "+diffvalue.size());
		System.out.println("90 days cumulative dgFuelConsumedvalue : "+sum);

		dg_efficiency_avg_value= dgFuelefficiencyCalc(dgenergy,dgFuelConsumedvalue);
		System.out.println("90 days avg dgFuelefficiencyvalue : "+dg_efficiency_avg_value);

		//System.out.println("avg :"+avg_value);

	}

	public static int acRunHrList(String fromDate,String toDate,String id)
	{
		List<Integer> runhrList=new ArrayList();
		List<Integer> nonzerorunhrList=new ArrayList();
		//runhrList=null;
		SoftAssert sa = new SoftAssert();
		try {
			sumofAchours=0;
			int ac_run_hr_value=0;
			String macid=id;
			//String payload ="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
			String payload="{\"from\":"+fromDate+",\"to\":"+toDate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";

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
			//sa.assertEquals(response.jsonPath().getString("message"),"Events retrieved successfully.");
			//System.out.println(	"Count : "+response.jsonPath().getString("count"));
			int count = response.jsonPath().getInt("count");
			//System.out.println("packet count : "+count);
			String jsonpath="";

			if(count==0)
			{
				sumofAchours=0;
			}
			else
			{
				for(int j=0;j<count;j++) {

					int acsumofAchours=0;
					for(int i=1;i<=ac_count;i++)
					{

						int sumofhrs=0;
						String jsontext="";
						String acrhrvalue="";
						int acrhvalue=0;
						String s="ac"+i+"RunHour";
						int lastpacket=count-1;
						int firstpacket=0;
						jsonpath="data["+j+"].meter."+s;
						acrhrvalue = response.getBody().jsonPath().getString(jsonpath);
						System.out.println("acRunHourlist lastpacket : " +lastpacket);
						System.out.println("acRunHourlist firstpacket: " +firstpacket);

						if((acrhrvalue==null))
						{
							acrhvalue=0;
						}
						else
						{
							acrhvalue=Integer.valueOf(acrhrvalue);

							//System.out.println(sumofAchours);

						}
						acsumofAchours=acrhvalue+acsumofAchours;
					}


					runhrList.add(acsumofAchours);
				}

				//runhrList.add(sumofAchours);
			}
			//System.out.println("runhr List : "+runhrList);
			for(int x=0;x<runhrList.size();x++)
			{
				int value=runhrList.get(x);
				if(value!=0)
				{
					nonzerorunhrList.add(value);
				}
			}
			//System.out.println("runhr List : "+nonzerorunhrList);
			try {
				if(nonzerorunhrList.size()==0)
				{
					sumofAchours=0;
				}
				else
				{

					int size= nonzerorunhrList.size()-1;
					int last =(int) nonzerorunhrList.get(size);
					int first=(int) nonzerorunhrList.get(0);
					sumofAchours=first-last;
					//System.out.println(sumofAchours);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		catch (Exception e) {
			sumofAchours=0;
			e.printStackTrace();
			//System.out.println(sumofAchours);
		}
		return sumofAchours;
	}


	public static int acRunHrList(String id)
	{
		List<Integer> runhrList=new ArrayList();
		List<Integer> nonzerorunhrList=new ArrayList();
		//runhrList=null;
		SoftAssert sa = new SoftAssert();
		try {
			sumofAchours=0;
			int ac_run_hr_value=0;
			String macid=id;
			//String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+macid+"\"}";
			String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"device\":{\"_id\":\""+macid+"\"}}";
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
			//sa.assertEquals(response.jsonPath().getString("message"),"Events retrieved successfully.");
			//System.out.println(	"Count : "+response.jsonPath().getString("count"));
			int count = response.jsonPath().getInt("count");
			//System.out.println("packet count : "+count);
			String jsonpath="";

			if(count==0)
			{
				sumofAchours=0;
			}
			else
			{
				for(int j=0;j<count;j++) {

					int acsumofAchours=0;
					for(int i=1;i<=ac_count;i++)
					{

						int sumofhrs=0;
						String jsontext="";
						String acrhrvalue="";
						int acrhvalue=0;
						String s="ac"+i+"RunHour";
						int lastpacket=count-1;
						int firstpacket=0;
						jsonpath="data["+j+"].meter."+s;
						acrhrvalue = response.getBody().jsonPath().getString(jsonpath);

						if((acrhrvalue==null))
						{
							acrhvalue=0;
						}
						else
						{
							acrhvalue=Integer.valueOf(acrhrvalue);

							//System.out.println(sumofAchours);

						}
						acsumofAchours=acrhvalue+acsumofAchours;
					}


					runhrList.add(acsumofAchours);
				}

				System.out.println("sumofAchours runhourlist : " +sumofAchours);
			}
			//System.out.println("runhr List : "+runhrList);
			for(int x=0;x<runhrList.size();x++)
			{
				System.out.println("runhrList.size() is : "+runhrList.size());
				int value=runhrList.get(x);
				
				//value is the sum of all AC run hours in each packet
				//runhrList.get(x) is the number of data packets
				if(value!=0)
				{
					
					nonzerorunhrList.add(value);
					System.out.println("Value : "+nonzerorunhrList);

				}
			}
			
			System.out.println("runhr List : "+nonzerorunhrList);
			try {
				if(nonzerorunhrList.size()==0)
				{
					sumofAchours=0;
				}
				else
				{

					int size= nonzerorunhrList.size()-1;
					int last =(int) nonzerorunhrList.get(size);
					int first=(int) nonzerorunhrList.get(0);
					sumofAchours=first-last;
					System.out.println("sumofAchours : " +sumofAchours);
					System.out.println("first : " +first);
					System.out.println("last : " +last);

				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		catch (Exception e) {
			sumofAchours=0;
			e.printStackTrace();
			//System.out.println(sumofAchours);
		}
		return sumofAchours;
	}

	


	public void energy_90_days_avg(String site,String id)
	{
		double tankcapcity=0;
		double fuelprice=0;
		double storeareavalue=0;
		getStoredat(site);
		tankcapcity=parseStringvalue(fuelTankCapacity);
		fuelprice=parseStringvalue(fuelPrice);
		storeareavalue=parseStringvalue(areaOfStore);
		String macid=id;

		System.out.println("******************90 Days Avg Calculation********************");
		testavgcalc(macid,json_path_sebLoadManager1ActiveEnergyKWH);
		double seb_avg_value=avg_value;
		System.out.println("seb_avg_value : "+seb_avg_value);

		testavgcalc(macid,json_path_dg1CumulativeEnergykWh);
		double dg_avg_value=avg_value;
		System.out.println("dg_avg_value : "+dg_avg_value);
		double cumulative_energy_avg_value=seb_avg_value+dg_avg_value;
		System.out.println("cumulative_energy_avg_value :"+cumulative_energy_avg_value);

		testavgcalc(macid,json_path_dg1RunHours);
		double dgrunhr_avg_value=avg_value;
		System.out.println("dgrunhr_avg_value : "+dgrunhr_avg_value);
		//System.out.println("no_events_days_count : "+no_events_days_count);

		testavgcalc_ac(macid);
		double acrunhrs= acrunhr_avg_value;
		System.out.println("AC runhr avg value : "+acrunhr_avg_value);

		System.out.println("90 days AC Average efficiency");
		double avg_90days_acEfficiency= acEnergyEffeciencyCalc(seb_avg_value,acrunhrs);
		System.out.println("avg_90days_acEfficiency : "+avg_90days_acEfficiency);

		//double dg_avg_value=2.8666666666666667;
		System.out.println("90 days DG Average efficiency");
		dg_avgcalc(id,tankcapcity,dg_avg_value);
		System.out.println("avg_90days_dg_efficiency_avg_value  : "+dg_efficiency_avg_value);
		System.out.println("avg_90days_dg_fuel_consumed_avg_value  : "+dg_fuel_consumed_avg_value);
		write_to_excel(ec_ops_outputfile_avg,writecount_avg,0,1,2,3,4,5,site,cumulative_energy_avg_value, dgrunhr_avg_value,acrunhrs,dg_efficiency_avg_value,dg_fuel_consumed_avg_value);
		writecount_avg++;



	}


}
