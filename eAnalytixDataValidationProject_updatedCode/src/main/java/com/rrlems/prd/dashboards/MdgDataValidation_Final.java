package com.rrlems.prd.dashboards;

import static io.restassured.RestAssured.given;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import io.restassured.response.Response;



public class MdgDataValidation_Final extends RrlBaseClass{
	public static String eid="ems";
	public static String userid="Madhusudhana.sv@ril.com";
	public static String password="Jio@1234";
	public static String sitename="Vehicle 4382";
	
	public static String datapath="";
	public static String storeCode="";

	public static String imei_id="";
	public static int rowcount=1;

	public static List mac_id_list;

	public static List name_id_list;

	public static double total_fuelConsumed_site=0;

	public static String dg_generator="";

	public  float event_total_fuel_filled=0.00f;
	public float event_total_fuel_removed=0.00f;
	public  float event_total_fuel_consumed=0.00f;
	public  int event_fuel_removal_count=0;
	public  int event_fuel_fill_count=0;
	public  int event_event_fuel_power_on_count=0;
	public  int event_event_fuel_power_off_count=0;
	public static long dg_events_ops=0;
	public static long site_dg_events_ops=0;
	
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

	public static double events_carbonperarea=0;
	public static double events_carbonEmissionCoefficient=0;

	public static String start_active_energy = null;
	public static String end_active_energy = null;
	public static float energy_generated = 0.00f;
	public static double dg_efficiency = 0.00f;
	public static double dgc_enpi = 0.00f;
	public static int dg_power_on_count = 0;
	public static double areaofStore=100;
	public static double co2emission=0;
	public static double co2persqft=0;
	public static double enpi=0;
	public static double co2_calfixed=2.653;
	
	
	public static double total_site_energy = 0.00f;
	public static int total_site_event_ops_hour=0;
	
	//public static String dg_events_hms="00:00:00";

	@BeforeSuite
	public void login()
	{
		String requestpayload = "{\"eid\":\""+eid+"\",\"email\":\""+userid+"\",\"type\":\"supervisor\",\"password\":\""+password+"\"}";
		sa = new SoftAssert();
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("Eid",eid)
				.and()
				
				.header("apiKey", apiKey)
				.and()
				.body(requestpayload)
				.when()
				.post(base_uri+login_api)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		//sa.assertEquals("User login successful.", response.jsonPath().getString("message"));
		String token = response.jsonPath().getString("data.token");
		user_id = response.jsonPath().getString("data._id");
		/*
		 * System.out.println("user_id:"+user_id);
		 * System.out.println(response.jsonPath().getString("message"));
		 * System.out.println(token);
		 */
		BearerToken= "Bearer "+ token;
		//System.out.println(BearerToken);
		sa.assertAll();  

	}
	
	
	

	@org.testng.annotations.Test(priority = 1)
	public void getSitedatPath()
	{
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
		List<String> name= response.jsonPath().getList("data.dat.name");
		List<String> path= response.jsonPath().getList("data.dat.path");
		List<String> storecode =response.jsonPath().getList("data.dat.metadata.storeCode");
		//System.out.println(lpath);
		//System.out.println(name);
		for(int i=0;i<name.size();i++)
		{
			String s= name.get(i).toString();
			if(s.equalsIgnoreCase(sitename))
			{
				getStoredat(sitename);
				//System.out.println(s);
				
				//System.out.println(path.get(i).toString());
				datapath=path.get(i).toString();
				storeCode=storecode.get(i).toString();
				
				//System.out.println("datapath:"+datapath);

			}

		}
		sa.assertAll();
	}

	@org.testng.annotations.Test(dependsOnMethods = "getSitedatPath")
	public void getmdgData()
	{
		int mdgcount=0;
		String dg_controller="";
		String dg="";
		String vehicle_name="";
		String payload="{\"flags\":{\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetType\":true},\"startsWith\":{\"datRegex\":\""+datapath+"\"}}";
		//System.out.println("payload :"+payload);
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
		List<String> lpath= response1.jsonPath().getList("data.device.mac");
		List<String> lname= response1.jsonPath().getList("data.device.name");

		System.out.println(lpath);
		total_site_energy = 0.00f;
		total_site_event_ops_hour=0;
		try
		{
			mdgcount=lpath.size()/2;
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		uri = generic_events_uri;

		try {
			if(lpath.size()==0)
			{
				System.out.println("No devices onboarded");
			}
			else 

				for(int j=0;j<lpath.size();j++)
				{

					String event_ops_hour_start_reading="";
					String event_ops_hour_end_reading="";
					int event_ops_hour=0;
					String start_active_energy = null;
					String end_active_energy = null;
					double energy_generated = 0.00f;

					String s= lpath.get(j).toString();
					String name=lname.get(j).toString();
					String deviceid=null;
					if(s.contains("_1"))
					{
						deviceid=null;

						System.out.println(+rowcount+" : DG Controller : "+s);
						
						dg_controller=s;
						vehicle_name=name;
						deviceid=get_device_id(s);
						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"electricity_metering_data\",\"device\":{\"_id\":\""+deviceid+"\"}}";
						System.out.println(payload);

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
						sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));


						if(count==0)
						{
							energy_generated=0;
							event_ops_hour=0;

							System.out.println("energy_generated : "+energy_generated);
							System.out.println("event_ops_hour : "+event_ops_hour);
							//System.out.println("event_ops_hour : "+event_ops_hour);
						}
						else {

							List activeEnergy = response.getBody().jsonPath().getList("data.meter.activeEnergy");
							List hourMeterReading = response.getBody().jsonPath().getList("data.meter.hourMeterReading");
							List<Float> wb1 = new ArrayList<>();
							System.out.println(activeEnergy);
							System.out.println(activeEnergy.size());
							System.out.println(hourMeterReading);
							System.out.println(activeEnergy.size());
							while(activeEnergy.remove(null))
							{
							}
							if(activeEnergy.size()==0)
							{
								energy_generated=0;
								event_ops_hour=0;
								System.out.println("energy_generated : "+energy_generated);
								System.out.println("event_ops_hour : "+event_ops_hour);
							}
							else
							{
								for(Object obj:activeEnergy)
								{
									wb1.add((float)((Number) obj).floatValue());

								}


								//System.out.println(wb1);
								for(int i=0;i<wb1.size();i++)
								{
									if(wb1.get(i)!=0)	
									{
										start_active_energy=wb1.get(i).toString();
										//System.out.println("start_active_energy :"+start_active_energy);
										break;
									}
									else
									{
										start_active_energy="0";
									}
								}
								//System.out.println("start_active_energy:"+start_active_energy);
								for(int i=wb1.size()-1;i>0;i--)
								{
									if(wb1.get(i)!=0)	
									{
										end_active_energy=wb1.get(i).toString();
										//System.out.println("end_active_energy:"+end_active_energy);
										break;
									}
									else
									{
										end_active_energy="0";
									}
								}

								List<Integer> wb2 = new ArrayList<>();
								//System.out.println(is);
								while(hourMeterReading.remove(null))
								{
								}
								for(Object obj:hourMeterReading)
								{
									wb2.add((int)((Number) obj).floatValue());

								}

								//System.out.println("Hour Meter :"+wb2);
								for(int i=0;i<wb2.size();i++)
								{
									if(wb2.get(i)!=0)	
									{
										event_ops_hour_end_reading=wb2.get(i).toString();
										//System.out.println("start_active_energy :"+start_active_energy);
										break;
									}
									else
									{
										event_ops_hour_end_reading="0";
									}
								}

								//System.out.println("event_ops_hour_end_reading:"+event_ops_hour_end_reading);



								for(int i=wb2.size()-1;i>0;i--)
								{
									if(wb2.get(i)!=0)	
									{
										event_ops_hour_start_reading=wb2.get(i).toString();
										//System.out.println("end_active_energy:"+end_active_energy);
										break;
									}
									else
									{
										event_ops_hour_start_reading="0";
									}
								}
								//System.out.println("end_active_energy:"+end_active_energy);
								energy_generated = Float.parseFloat(start_active_energy)-Float.parseFloat(end_active_energy);
								System.out.println("energy_generated:"+energy_generated);
								//System.out.println("event_ops_hour_start_reading:"+event_ops_hour_start_reading);
								event_ops_hour = Integer.parseInt(event_ops_hour_end_reading)-Integer.parseInt(event_ops_hour_start_reading);
								System.out.println("event_ops_hour in milliseconds :"+event_ops_hour);
								
								try {
									enpi=0;
									enpi=(energy_generated/areaofStore)*1/1000;
									System.out.println("enpi : "+enpi );
									}
								    catch (Exception e) {
								    	enpi=0;
										// TODO: handle exception
								    	e.printStackTrace();
									}
							}
							
								
								
							}
						}
						write_to_excel(ec_ops_outputfile_dg,rowcount,0,1,2,3,vehicle_name,energy_generated,enpi,event_ops_hour);
						rowcount++;
						total_site_energy=total_site_energy+energy_generated;
						total_site_event_ops_hour+=event_ops_hour;
					}

					//total_site_energy=total_site_energy+energy_generated;
					//total_site_event_ops_hour+=event_ops_hour;
					

			System.out.println("Total MDG count : "+mdgcount);
			System.out.println("total_site_energy : "+total_site_energy);
			System.out.println("total_site_event_ops_hour : "+total_site_event_ops_hour);

		}
		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}
		//sa.assertAll();
	}
	/*
	//@org.testng.annotations.Test(dependsOnMethods = "getSitedatPath")
	public void kpa_dashboard_data()
	{
		try {
			String payload="{\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"dat\":\""+datapath+"\"}";
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
					.post(kpa_mdgfuel_consumed_uri)
					.then()
					.extract().response();
			sa.assertEquals(response.statusCode(),200);
			sa.assertEquals(response.jsonPath().getString("message"),"MDG fuel consumed retrieved successfully.");
			kpa_mdgfuel_consumed = response.getBody().jsonPath().getString("data.fuelConsumed");
			System.out.println("kpa_mdgfuel_consumed :"+kpa_mdgfuel_consumed);

			String payload_mdgcount="{\"datpath\":\""+datapath+"\"}";
			response = given()
					.header("Content-type", "application/json")
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.body(payload_mdgcount)
					.when()
					.post(kpa_mdgcount_uri)
					.then()
					.extract().response();
			sa.assertEquals(response.statusCode(),200);
			sa.assertEquals(response.jsonPath().getString("message"),"MDG count dashboard Report retrieved successfully." );
			kpa_mdgcount = response.getBody().jsonPath().getString("data.count");
			System.out.println("kpa_mdgcount :"+kpa_mdgcount);


			String kpa_energy_consumed_payload="{\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"dat\":\""+datapath+"\",\"category\":\"energyconsumed\"}";

			response = given()
					.header("Content-type", "application/json")
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.body(kpa_energy_consumed_payload)
					.when()
					.post(kpa_energyconsumed_uri)
					.then()
					.extract().response();
			sa.assertEquals(response.statusCode(),200);
			sa.assertEquals(response.jsonPath().getString("message"),"Energy Consumed data retrieved successfully." );
			kpa_energyconsumed = response.getBody().jsonPath().getString("data.energy");
			System.out.println("kpa_energyconsumed :"+kpa_energyconsumed);


			String kpa_mdgfueloperation_payload_lowfuel="{\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"dat\":\""+datapath+"\",\"type\":\"fuel_level_low\"}";
			response = given()
					.header("Content-type", "application/json")
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.body(kpa_mdgfueloperation_payload_lowfuel)
					.when()
					.post(kpa_mdgfueloperation_uri)
					.then()
					.extract().response();
			kpa_mdgfueloperation_fuel_fuelLowCount = response.getBody().jsonPath().getString("data.fuelLowCount");

			String kpa_mdgfueloperation_payload_fuel_fill="{\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"dat\":\""+datapath+"\",\"type\":\"fuel_fill\"}";
			response = given()
					.header("Content-type", "application/json")
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.body(kpa_mdgfueloperation_payload_fuel_fill)
					.when()
					.post(kpa_mdgfueloperation_uri)
					.then()
					.extract().response();
			kpa_mdgfueloperation_fuel_fill = response.getBody().jsonPath().getString("data.fuelFillTotal");

			String kpa_mdgfueloperation_payload_fuel_removed="{\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"dat\":\""+datapath+"\",\"type\":\"fuel_removed\"}";
			response = given()
					.header("Content-type", "application/json")
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.body(kpa_mdgfueloperation_payload_fuel_removed)
					.when()
					.post(kpa_mdgfueloperation_uri)
					.then()
					.extract().response();
			kpa_mdgfueloperation_fuel_removed = response.getBody().jsonPath().getString("data.fuelRemoved");


			String kpa_mdgfueloperation_payload_Opshour="{\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"dat\":\""+datapath+"\",\"types\":[\"power_on\",\"power_off\"]}";



			response = given()
					.header("Content-type", "application/json")
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.body(kpa_mdgfueloperation_payload_Opshour)
					.when()
					.post(kpa_mdgfueloperation_uri)
					.then()
					.extract().response();
			kpa_mdgfueloperation_fuel_operationalHour = response.getBody().jsonPath().getString("data.operationalHour");
			sa.assertEquals(response.statusCode(),200);
			sa.assertEquals(response.jsonPath().getString("message"),"MDG operational hour retrieved successfully." );
			System.out.println("kpa_mdgfueloperation_fuel_fill :"+kpa_mdgfueloperation_fuel_fill);
			System.out.println("kpa_mdgfueloperation_fuel_removed :"+kpa_mdgfueloperation_fuel_removed);
			System.out.println("kpa_mdgfueloperation_fuel_fuelLowCount :"+kpa_mdgfueloperation_fuel_fuelLowCount);
			System.out.println("kpa_mdgfueloperation_fuel_operationalHour :"+kpa_mdgfueloperation_fuel_operationalHour);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("No status found for this dat.");
		}

		sa.assertAll();
	}


	@org.testng.annotations.Test(dependsOnMethods = "getSitedatPath")
	public void kpa_dashboard_mdgenergychart()
	{
		try {
			String payload="{\"time\":{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+"},\"dat\":\""+datapath+"\"}";
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
					.post(kpa_mdgenergychart_uri)
					.then()
					.extract().response();
			sa.assertEquals(response.statusCode(),200);
			sa.assertEquals(response.jsonPath().getString("message"),"Energy consumed retrieved successfully.");
			kpa_mdgRunHour = response.getBody().jsonPath().getString("data.mdgRunHour");
			kpa_mdgEnergy = response.getBody().jsonPath().getString("data.mdgEnergy");
			kpa_totalRunHour = response.getBody().jsonPath().getString("data.totalRunHour");
			kpa_totalEnergy=response.getBody().jsonPath().getString("data.totalEnergy");
		}
		catch (Exception e) {
			// TODO: handle exception
		}


		System.out.println("kpa_totalRunHour : "+kpa_totalRunHour);
		System.out.println("kpa_totalEnergy : "+kpa_totalEnergy);
		sa.assertAll();

	}

*/
	//@org.testng.annotations.Test(dependsOnMethods = "dg_events_fuel_events")
	public void dg_events_status()
	{
		String start_fuel_in_liters="";
		String end_fuel_in_liters="";
String s=null;
		String deviceid=get_device_id(s);
		uri = generic_events_uri;
		//String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+dg_generator+"\"}";
		String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"electricity_metering_data\",\"device\":{\"_id\":\""+deviceid+"\"}}";
		//System.out.println(payload);
		System.out.println(generic_events_uri);
		sa = new SoftAssert();
		try {
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
			sa.assertEquals(response.jsonPath().getString("message"),"Events retrieved successfully.");
			List fuelInLitres_list = response.getBody().jsonPath().getList("data.meter.fuelInLitres");
			List<Float> wb1 = new ArrayList<>();
			//System.out.println(fuelInLitres_list);
			if(fuelInLitres_list.isEmpty())
			{
				event_total_fuel_consumed=0;
				end_fuel_in_liters="0";
				start_fuel_in_liters="0";
				System.out.println("fuel_consumed:"+event_total_fuel_consumed);
			}
			else
			{
				while(fuelInLitres_list.remove(null))
				{
				}
				for(Object obj:fuelInLitres_list)
				{
					wb1.add((float)((Number) obj).floatValue());

				}

				if(wb1.size()==0)
				{
					event_total_fuel_consumed=0;
					System.out.println("fuel_consumed:"+event_total_fuel_consumed);
				}
				else	
				{

					//System.out.println(wb1);
					for(int i=0;i<wb1.size();i++)
					{
						if(wb1.get(i)!=0)	
						{
							end_fuel_in_liters=wb1.get(i).toString();
							System.out.println("end_fuel_in_liters :"+end_fuel_in_liters);
							break;
						}
						else
						{
							end_fuel_in_liters="0";
						}
					}

					for(int i=wb1.size()-1;i>0;i--)
					{
						if(wb1.get(i)!=0)	
						{
							start_fuel_in_liters=wb1.get(i).toString();
							System.out.println("start_fuel_in_liters :"+start_fuel_in_liters);
							break;
						}
						else
						{
							start_fuel_in_liters="0";
						}
					}
					event_total_fuel_consumed = Math.abs((Float.parseFloat(start_fuel_in_liters)-Float.parseFloat(end_fuel_in_liters)+event_total_fuel_filled-event_total_fuel_removed));
					System.out.println("fuel_consumed:"+event_total_fuel_consumed);
				}

			}		
		}
		catch(Exception e)
		{
			event_total_fuel_consumed=0;
			System.out.println("fuel_consumed:"+event_total_fuel_consumed);
		}
		sa.assertAll();
	}

	//@Test	
	//public void dg_events_fuel_events(String dg)
	public void dg_events_fuel_events()
	{


		String dg_generator="PI5VV1LCLTxenIg";
		uri = generic_events_uri;
		String payload;

		payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_fill_type+"\",\"meterId\":\""+dg_generator+"\"}";
		//System.out.println(payload);

		//payload="{\"from\":1688841000000,\"to\":1688927399999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"fuel_fill\",\"meterId\":\"PI5VV1LCLTxenIg\"}";
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
				.post(uri)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		//sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
		event_fuel_fill_count=response.getBody().jsonPath().getInt("count");
		System.out.println("event_fuel_fill_count : "+event_fuel_fill_count);
		try {


			if(event_fuel_fill_count==0)
			{
				event_total_fuel_filled=0;
			}
			else
			{
				List<String> is = response.getBody().jsonPath().getList("data.meter.flr");


				List<Float> wb1 = new ArrayList<>();
				for(Object obj:is)
				{
					wb1.add((float)((Number) obj).floatValue());

				}

				System.out.println(wb1);
				for(int i=0;i<wb1.size();i++)
				{
					event_total_fuel_filled=wb1.get(i)+event_total_fuel_filled;	
				}

			}
			System.out.println("event_total_fuel_filled :"+event_total_fuel_filled);

		}

		catch (Exception e) {
			// TODO: handle exception
		}
		payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_removed_type+"\",\"meterId\":\""+dg_generator+"\"}";
		//System.out.println(payload);
		response = given()
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
		//sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
		event_fuel_removal_count=response.getBody().jsonPath().getInt("count");
		System.out.println("event_fuel_removal_count :"+event_fuel_removal_count);

		try {
			if(event_fuel_removal_count==0)
			{
				event_total_fuel_removed=0;
				//System.out.println("event_total_fuel_removed :"+event_total_fuel_removed);
			}

			else
			{
				List<String> is = response.getBody().jsonPath().getList("data.meter.flr");
				List<Float> wb1 = new ArrayList<>();
				for(Object obj:is)
				{
					wb1.add((float)((Number) obj).floatValue());

				}
				System.out.println(wb1);
				for(int i=0;i<wb1.size();i++)
				{
					event_total_fuel_removed=wb1.get(i)+event_total_fuel_removed;	
				}


			}
			System.out.println("event_total_fuel_removed :"+event_total_fuel_removed);
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		sa.assertAll();

		//0.85* Total Energy Supplied by the Mains + 2.653* Total fue

	}


	@org.testng.annotations.Test(dependsOnMethods = "getSitedatPath")
	public void getdevicelists() {


		int mdgcount=0;
		String dg_controller="";
		String dg="";
		String vehicle_name="";
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
		try {
			mac_id_list= response1.jsonPath().getList("data.device.mac");
			name_id_list= response1.jsonPath().getList("data.device.name");
			System.out.println(mac_id_list);
			System.out.println(name_id_list);

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		sa.assertAll();

	}


	@Test(dependsOnMethods = {"getdevicelists","getmdgData"})
	//@Test()
	public void mdgFuelData()
	{
       String deviceid=null;

		int rowcount=1;
		if(mac_id_list.size()==0)
		{
			System.out.println("No devices onboarded");		
		}
		else

			for(int j=0;j<mac_id_list.size();j++)
			{
				String dg_events_hms="00:00:00";
				double event_total_fuel_filled=0.00f;
				double event_total_fuel_removed=0.00f;
				double event_total_fuel_consumed=0.00f;
				double event_total_fuel_theft=0.00f;
				double event_fuel_removal_count=0;
				double event_fuel_fill_count=0;

				//event_total_fuel_consumed=0;
				String start_fuel_in_liters = null;
				String end_fuel_in_liters = null;


				String macid= mac_id_list.get(j).toString();
				String name=name_id_list.get(j).toString();
				
				if(macid.contains("_1"))
				{
					deviceid=null;
					dg_generator=macid.replace("_1", "");
					deviceid=get_device_id(dg_generator);
					System.out.println(+rowcount+" : dg_generator : "+dg_generator);




					/*************************/

					/*************dg omr***/

					//uri = generic_events_uri;
					List<Integer> poweronlist;
					List<Integer> powerofflist;
					//String dg_generator="PALE1VrLIyEF8laI";
					deviceid=get_device_id(dg_generator);
					System.out.println("deviceid :"+deviceid);
					
					
					String payload_poweron="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_power_on_type+"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
					String payload_poweroff="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_power_off_type+"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
					
					//String payload_poweron="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_power_on_type+"\",\"meterId\":\""+dg_generator+"\"}";
					//String payload_poweroff="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_power_off_type+"\",\"meterId\":\""+dg_generator+"\"}";
					
					
					//System.out.println("payload_poweron"+payload_poweron);
					//System.out.println("payload_poweroff"+payload_poweroff);
					Response response_poweron = given()
							.header("Content-type", "application/json")
							.and()
							.header("Eid",eid)
							.and()
							.header("apiKey", apiKey)
							.and()
							.header("Authorization", BearerToken)
							.and()
							.body(payload_poweron)
							.when()
							.post(uri)
							.then()
							.extract().response();
					sa.assertEquals(200, response_poweron.statusCode());


					Response response_poweroff = given()
							.header("Content-type", "application/json")
							.and()
							.header("Eid",eid)
							.and()
							.header("apiKey", apiKey)
							.and()
							.header("Authorization", BearerToken)
							.and()
							.body(payload_poweroff)
							.when()
							.post(uri)
							.then()
							.extract().response();
					sa.assertEquals(200, response_poweroff.statusCode());

					//sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
					event_event_fuel_power_on_count=response_poweron.getBody().jsonPath().getInt("count");
					event_event_fuel_power_off_count=response_poweroff.getBody().jsonPath().getInt("count");
					//System.out.println("event_event_fuel_power_on_count : "+event_event_fuel_power_on_count);
					//System.out.println("event_event_fuel_power_off_count : "+event_event_fuel_power_off_count);

					try {
						if(((event_event_fuel_power_on_count) & (event_event_fuel_power_off_count))==0)
						{
							event_event_fuel_power_on_count=0;
							event_event_fuel_power_off_count=0;
							event_ops_hour=0;

						}
						else
						{
							poweronlist = response_poweron.getBody().jsonPath().getList("data.time");
							powerofflist = response_poweroff.getBody().jsonPath().getList("data.time");
							List<Long> wb_on = new ArrayList<>();
							List<Long> wb_off = new ArrayList<>();
							for(Object obj:poweronlist)
							{
								wb_on.add((long)((Number) obj).longValue());

							}
							for(Object obj1:powerofflist)
							{
								wb_off.add((long)((Number) obj1).longValue());

							}

							//System.out.println("poweronlist : "+wb_on);
							//System.out.println("powerofflist : "+wb_off);
							//float event_total_fuel_removed1=0;
							long diff=0;
							long dg_events_ops_ind=0;
							for(int i=0;i<wb_on.size();i++)
							{
								diff=wb_off.get(i)-wb_on.get(i);	
								//diff=Math.abs(wb2.get(i))-Math.abs(wb12.get(i));
								//System.out.println(diff);
								dg_events_ops_ind=diff+dg_events_ops_ind;
							}
							//dg_events_ops=diff+dg_events_ops;
							dg_events_ops=dg_events_ops_ind/2;
							//dg_generator
							System.out.println("dg_generator : "+dg_generator);
							//System.out.println("dg ops : "+dg_events_ops);

							dg_events_hms=millitohms(dg_events_ops);
							System.out.println("dg_events_hms : "+dg_events_hms);
							site_dg_events_ops=site_dg_events_ops+dg_events_ops;			

							//event_total_fuel_removed=event_total_fuel_removed1/2;
						}
						//System.out.println("event_total_fuel_removed :"+event_total_fuel_removed);
					}
					catch (Exception e) {
						dg_events_ops=0;
						dg_events_hms="00:00:00";
						site_dg_events_ops=0;
						// TODO: handle exception
					}								





					/********ends***********/




					/******************************/
					uri = generic_events_uri;

//					String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_fill_type+"\",\"meterId\":\""+dg_generator+"\"}";
					//System.out.println(payload);
					String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_fill_type+"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
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
					sa.assertEquals(response.statusCode(),200);
					//sa.assertEquals( response.jsonPath().getString("message"),"Events retrieved successfully.");
					event_fuel_fill_count=response.getBody().jsonPath().getInt("count");

					System.out.println("event_fuel_fill_count : "+event_fuel_fill_count);
					try {
						if(event_fuel_fill_count==0)
						{
							event_total_fuel_filled=0;
						}
						else
						{
							List<String> is = response.getBody().jsonPath().getList("data.meter.flr");
							List<Float> wb1 = new ArrayList<>();
							for(Object obj:is)
							{
								wb1.add((float)((Number) obj).floatValue());


							}

							//System.out.println("fuel_fill: "+wb1);
							float event_total_fuel_filled1=0;
							for(int i=0;i<wb1.size();i++)
							{
								event_total_fuel_filled1=wb1.get(i)+event_total_fuel_filled1;	

							}
							event_total_fuel_filled=event_total_fuel_filled1;
						}
						System.out.println("event_total_fuel_filled :"+event_total_fuel_filled);

					}

					catch (Exception e) {
						// TODO: handle exception
						event_total_fuel_filled=0;
					}




					//payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_removed_type+"\",\"meterId\":\""+dg_generator+"\"}";
					payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_removed_type+"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
					//System.out.println(payload);
					response = given()
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
					//sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
					
					String s=response.getBody().jsonPath().getString("count");
					if(s!=null)
					{
					
					event_fuel_removal_count=response.getBody().jsonPath().getInt("count");
					}
					else
					{
						event_fuel_removal_count=0;
					}

					try {
						/*	
						if(((event_fuel_removal_count) & (event_event_fuel_power_on_count))==0)
						{
							event_total_fuel_removed=0;
						}
						else 
						 */
						if((event_fuel_removal_count==0))
						{
							event_total_fuel_removed=0;
						}
						else
						{
							List<String> is = response.getBody().jsonPath().getList("data.meter.flr");
							List<Float> wb1 = new ArrayList<>();
							for(Object obj:is)
							{
								wb1.add((float)((Number) obj).floatValue());

							}

							//System.out.println("fuel_removed : "+wb1);
							float event_total_fuel_removed1=0;
							for(int i=0;i<wb1.size();i++)
							{
								event_total_fuel_removed1=wb1.get(i)+event_total_fuel_removed1;	
							}

							event_total_fuel_removed=event_total_fuel_removed1;
						}
						System.out.println("event_total_fuel_removed :"+event_total_fuel_removed);
					}
					catch (Exception e) {
						event_total_fuel_removed=0;
						// TODO: handle exception
					}


					
					
					 //payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"electricity_metering_data\",\"meterId\":\""+dg_generator+"\"}";
					 payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"electricity_metering_data\",\"device\":{\"_id\":\""+deviceid+"\"}}";
					System.out.println(payload);
					System.out.println(generic_events_uri);
					sa = new SoftAssert();
					try {
						response = given()
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
						sa.assertEquals(response.jsonPath().getString("message"),"Events retrieved successfully.");
						List fuelInLitres_list = response.getBody().jsonPath().getList("data.meter.fuelInLitres");
						List<Float> wb1 = new ArrayList<>();
						//System.out.println("fuelInLitres_list"+fuelInLitres_list);
						if(fuelInLitres_list.isEmpty())
						{
							event_total_fuel_consumed=0;
							end_fuel_in_liters="0";
							start_fuel_in_liters="0";
							System.out.println("fuel_consumed:"+event_total_fuel_consumed);
						}
						else
						{
							while(fuelInLitres_list.remove(null))
							{
							}
							for(Object obj:fuelInLitres_list)
							{
								wb1.add((float)((Number) obj).floatValue());

							}

							if(wb1.size()==0)
							{
								event_total_fuel_consumed=0;
								end_fuel_in_liters="0";
								start_fuel_in_liters="0";
								System.out.println("fuel_consumed:"+event_total_fuel_consumed);
							}
							else	
							{

								//System.out.println(wb1);
								try {
									for(int i=0;i<wb1.size();i++)
									{
										if(wb1.get(i)!=0)	
										{
											end_fuel_in_liters=wb1.get(i).toString();
											System.out.println("end_fuel_in_liters :"+end_fuel_in_liters);
											break;
										}
										else
										{
											end_fuel_in_liters="0";
										}
									}
								}
								catch (Exception e) {
									end_fuel_in_liters="0";
								}

								try {

									for(int i=wb1.size()-1;i>0;i--)
									{
										if(wb1.get(i)!=0)	
										{
											start_fuel_in_liters=wb1.get(i).toString();
											System.out.println("start_fuel_in_liters :"+start_fuel_in_liters);
											break;
										}
										else
										{
											start_fuel_in_liters="0";
										}

									}
								}
								catch (Exception e) {
									//System.out.println("1009");
									start_fuel_in_liters="0";
								}

								// TODO: handle exception


								if(event_event_fuel_power_on_count==0)
								{
									event_total_fuel_consumed=0;
									event_total_fuel_removed=Math.abs((Float.parseFloat(start_fuel_in_liters)-Float.parseFloat(end_fuel_in_liters)+event_total_fuel_filled-event_total_fuel_removed));
									//event_total_fuel_theft=Math.abs((Float.parseFloat(start_fuel_in_liters)-Float.parseFloat(end_fuel_in_liters)+event_total_fuel_filled+event_total_fuel_removed));
									//event_total_fuel_removed=0;
									System.out.println("DG Fuel_consumed for : "+sitename+" in Liters "+event_total_fuel_consumed);
									System.out.println("DG Fuel Remove : "+sitename+" in Liters "+event_total_fuel_removed);
									dg_efficiency=0;
									System.out.println("dg_efficiency : "+dg_efficiency);
								}
								else
								{

									event_total_fuel_consumed = Math.abs((Float.parseFloat(start_fuel_in_liters)-Float.parseFloat(end_fuel_in_liters)+event_total_fuel_filled-event_total_fuel_removed));
									System.out.println("DG Fuel_consumed for : "+sitename+" in Liters "+event_total_fuel_consumed);
									

						}
							}
						}
					}
						
						catch(Exception e)
						{
							event_total_fuel_consumed=0;
							start_fuel_in_liters="0";
							end_fuel_in_liters="0";
							event_total_fuel_filled=0;
							event_total_fuel_removed=0;
							dg_events_ops=0;
							dg_events_hms="00:00:00";
							System.out.println("event_total_fuel_consumed:"+event_total_fuel_consumed);
							//update_to_excel(ec_ops_outputfile_dg, rowcount,4,5,6,7,8,9,10,11,12,13,event_fuel_fill_count, event_total_fuel_filled, event_total_fuel_removed, start_fuel_in_liters, end_fuel_in_liters, total_fuelConsumed_site, dg_efficiency, co2emission, co2persqft, dg_events_hms);
							//rowcount++;
						}
						//update_to_excel(ec_ops_outputfile, rowcount,4,5,6,7,8,9,10,11,dg_generator,start_fuel_in_liters,end_fuel_in_liters,event_total_fuel_filled,event_total_fuel_removed,event_total_fuel_consumed,dg_events_hms,event_total_fuel_theft);
					rowcount=2;
					
						total_fuelConsumed_site=total_fuelConsumed_site+event_total_fuel_consumed;
						String total_site_dg_events_ops=millitohms(site_dg_events_ops);
						System.out.println("total_fuelConsumed_site : "+total_fuelConsumed_site);
						System.out.println("total_site_dg_events_ops : "+total_site_dg_events_ops);

						try {
							dg_efficiency=total_site_energy/total_fuelConsumed_site;
							System.out.println("dg_efficiency : "+dg_efficiency);
							}
							catch(Exception e)
							{
								dg_efficiency=0;
								System.out.println("dg_efficiency : "+dg_efficiency);
							}
							
						}
						
						try {
						
						co2emission= (.85*total_site_energy)+(co2_calfixed*total_fuelConsumed_site);
						co2emission=co2emission/1000;
						System.out.println("co2emission :"+co2emission);

						co2persqft=co2emission/(Integer.parseInt((areaOfStore)));

						System.out.println("co2persqft :"+co2persqft);
						}
						catch(Exception e)
						{
							co2emission=0;
							co2persqft=0;
							System.out.println("co2emission :"+co2emission);
							System.out.println("co2persqft :"+co2persqft);	
						}
						update_to_excel(ec_ops_outputfile_dg, rowcount,4,5,6,7,8,9,10,11,12,13,event_fuel_fill_count, event_total_fuel_filled, event_total_fuel_removed, start_fuel_in_liters, end_fuel_in_liters, event_total_fuel_consumed, dg_efficiency, co2emission, co2persqft, dg_events_hms);	
						rowcount++;					


					}	
						
						
				
				}

		//@Test(priority=100,dependsOnMethods ="getSitedatPath" )
		public void power_on_off()
		{

			uri = generic_events_uri;
			List<Integer> poweronlist;
			List<Integer> powerofflist;
			String dg_generator="PALE1VrLIyEF8laI";
			String payload_poweron="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_power_on_type+"\",\"meterId\":\""+dg_generator+"\"}";
			String payload_poweroff="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\""+event_fuel_power_off_type+"\",\"meterId\":\""+dg_generator+"\"}";
			//System.out.println(payload_poweron);
			Response response_poweron = given()
					.header("Content-type", "application/json")
					.and()
					.header("Eid",eid)
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.body(payload_poweron)
					.when()
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response_poweron.statusCode());


			Response response_poweroff = given()
					.header("Content-type", "application/json")
					.and()
					.header("Eid",eid)
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.body(payload_poweroff)
					.when()
					.post(uri)
					.then()
					.extract().response();
			sa.assertEquals(200, response_poweroff.statusCode());

			//sa.assertEquals("Events retrieved successfully.", response.jsonPath().getString("message"));
			event_event_fuel_power_on_count=response_poweron.getBody().jsonPath().getInt("count");
			System.out.println("event_event_fuel_power_on_count :"+event_event_fuel_power_on_count);
			event_event_fuel_power_off_count=response_poweroff.getBody().jsonPath().getInt("count");

			try {
				if(((event_event_fuel_power_on_count) & (event_event_fuel_power_off_count))==0)
				{
					event_event_fuel_power_on_count=0;
					event_event_fuel_power_off_count=0;
					event_ops_hour=0;

				}
				else
				{
					poweronlist = response_poweron.getBody().jsonPath().getList("data.time");
					powerofflist = response_poweroff.getBody().jsonPath().getList("data.time");
					List<Integer> wb1 = new ArrayList<>();
					List<Integer> wb2 = new ArrayList<>();
					for(Object obj:poweronlist)
					{
						wb1.add((Integer)((Number) obj).intValue());

					}
					for(Object obj1:powerofflist)
					{
						wb2.add((Integer)((Number) obj1).intValue());

					}

					System.out.println("poweronlist : "+wb1);
					System.out.println("powerofflist : "+wb2);
					//float event_total_fuel_removed1=0;
					int diff=0;
					for(int i=0;i<wb1.size();i++)
					{
						diff=wb2.get(i)-wb1.get(i);	
						System.out.println(diff);
						dg_events_ops=diff+dg_events_ops;
					}
					//dg_events_ops=diff+dg_events_ops;
					dg_events_ops=dg_events_ops/2;

					System.out.println("dg ops"+dg_events_ops);
					//MILLISECONDS.toMinutes(milliseconds);
					long hours = TimeUnit.MILLISECONDS.toHours(dg_events_ops);
					System.out.println("Total minutes : "+hours);


					//event_total_fuel_removed=event_total_fuel_removed1/2;
				}
				//System.out.println("event_total_fuel_removed :"+event_total_fuel_removed);
			}
			catch (Exception e) {
				dg_events_ops=0;
				// TODO: handle exception
			}



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
			//System.out.println(response.prettyPrint());
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

		
		public void getEntitydata(String storename)
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
					//pfThresholdValue=pfThresholdValuelist.get(i).toString();
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
					//System.out.println("areaOfStore : "+areaOfStore);
					//System.out.println("fuelTankCapacity : "+fuelTankCapacity);
					//System.out.println("fuelPrice : "+fuelPrice);
					//System.out.println("pricePerUnitist : "+pricePerUnitist);

					break;

				}
				//}

			}
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
			//List workinghrsFromList=response.jsonPath().getList("dat.metadata.workingHours.from");
			//System.out.println(workinghrsFromList);
		//	List workinghrsToList=response.jsonPath().getList("data.dat.metadata.workingHours.to");
			//System.out.println(workinghrsToList);
		//	List workingDaysList=response.jsonPath().getList("data.dat.metadata.workingDays");
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
					//pfThresholdValue=pfThresholdValuelist.get(i).toString();
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
					//System.out.println("fuelTankCapacity : "+fuelTankCapacity);
					//System.out.println("fuelPrice : "+fuelPrice);
					//System.out.println("pricePerUnitist : "+pricePerUnitist);

					break;

				}
				//}

			}
		}

		
		

	}

