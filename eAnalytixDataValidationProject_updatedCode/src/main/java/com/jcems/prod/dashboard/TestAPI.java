package com.jcems.prod.dashboard;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;



public class TestAPI extends BaseClassApi{
	
//	public static String apiKey = "42e94c60-e79e-49b1-81dd-67f044550735";
//	public static String eid = "jcems";
//	public static String prod_uri="https://eanalytix.jvts.net/";
//	public static String exec_uri=prod_uri;
//	public static String base_uri=exec_uri+"accounts/api/users/";
//	
//	public static String uri;
//	public static String BearerToken="";
//	public static String storename="";
//	
//	public static String fuelTankCapacity="";
//	public static String fuelPrice="";
//	public static String areaOfStore="";
	String site="Malleswaram";

	

	public void getStoredat1(String storename)
	 
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
 
				fuelTankCapacity=fuelTankCapacitylist.get(i).toString();
 
				fuelPrice=fuelPricelist.get(i).toString();
 
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
		List storeName= response.jsonPath().getList("data.dat.name");
		List storedatPath= response.jsonPath().getList("data.dat.path");
		//System.out.println(storedatPath);
		for(int i =0;i<storeName.size();i++)
		{

			if(storeName.get(i).toString().equalsIgnoreCase(storename))
			{
				storeDatpath= storedatPath.get(i).toString();
				break;

			}


		}
		//System.out.println("storeDatpath : "+storeDatpath);
	}

	
	//To get the values of totalEnergyConsumed and totalCostIncurred
	@Test
	public void energyandcost()
	{
		getStoredat1(site);

		site=sitename;
		String path = storeDatpath;
		System.out.println(storeDatpath);
		SoftAssert sa = new SoftAssert();
		String url = "https://eanalytix.jvts.net/accounts/api/events/v3/jcems/kpi/energy-and-cost\r\n"
				+ "";
		String requestpayload = "{\"time\":{\"from\":1710959400000,\"to\":1711045799999},\"granularityType\":\"day\",\"isStatsEnabled\":true,\"limit\":5,\"dat\":\"64f82a7514e88d81dce8ab57/south47zkzj/karnatakanjag4uh/bangalore1cciy0m/malleswarambeklxt\"}";
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
				.header("Authorization", BearerToken)
				
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(url)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		
		sa.assertEquals("JCEMS total energy consumption and its total cost", response.jsonPath().getString("message"));
		
		String total_energy_consumed = response.jsonPath().getString("data.totalEnergyConsumed");
		String totalCostIncurred = response.jsonPath().getString("data.totalCostIncurred");
		System.out.println("Total energy Consumed value is : " +total_energy_consumed);
		System.out.println("Total cost incurred value is : " +totalCostIncurred);

		sa.assertAll();  

	}
	

	//To get the values of total ac runhour
	@Test
	public void total_ac_runHour()
	{
		getStoredat1(site);

		site=sitename;
		String path = storeDatpath;
		System.out.println(storeDatpath);
		SoftAssert sa = new SoftAssert();
		String url = "https://eanalytix.jvts.net/accounts/api/events/v4/stats/dashboard/total-acRunHour\r\n"
				+ "";
		String requestpayload = "{\"time\":{\"from\":1710959400000,\"to\":1711045799999},\"granularityType\":\"day\",\"isStatsEnabled\":true,\"dat\":\"64f82a7514e88d81dce8ab57/south47zkzj/karnatakanjag4uh/bangalore1cciy0m/malleswarambeklxt\"}";

		sa = new SoftAssert();
		Response response = given()
				
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)				
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(url)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("Jc dashboard stats for Ac run hour retrieved successfully.", response.jsonPath().getString("message"));

		
		String dat_value = response.jsonPath().getString("data");
		System.out.println("Dat value is : " +dat_value);

		sa.assertAll();  

	}
	
	
	//check for values(not getting)
	@Test
	public void total_dgrunhour()
	{
		getStoredat1(site);

		site=sitename;
		String path = storeDatpath;
		System.out.println(storeDatpath);
		SoftAssert sa = new SoftAssert();
		String url = "https://eanalytix.jvts.net/accounts/api/events/v4/stats/dashboard/total-dgrunhour\r\n"
				+ "";
		String requestpayload = "{\"time\":{\"from\":1710959400000,\"to\":1711045799999},\"granularityType\":\"day\",\"isStatsEnabled\":true,\"dat\":\"64f82a7514e88d81dce8ab57/south47zkzj/karnatakanjag4uh/bangalore1cciy0m/malleswarambeklxt\"}";

		sa = new SoftAssert();
		Response response = given()
				
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)				
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(url)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		
		sa.assertEquals("Jc dashboard stats for dg run hour retrieved successfully.", response.jsonPath().getString("message"));

		String dat_value = response.jsonPath().getString("data");
		System.out.println("Dat value is : " +dat_value);

		sa.assertAll();  

	}
	
	
	//To get the values of totalAcRunHour & totalDgRunHour
	@Test
	public void total_runhour_comparison()
	{
		getStoredat1(site);

		site=sitename;
		String path = storeDatpath;
		System.out.println(storeDatpath);
		SoftAssert sa = new SoftAssert();
		String url = "https://eanalytix.jvts.net/accounts/api/events/v4/stats/dashboard/total-runhour-comparision\r\n"
				+ "";
		String requestpayload = "{\"time\":{\"from\":1711099800000,\"to\":1711106999999},\"dat\":\"64f82a7514e88d81dce8ab57/south47zkzj/karnatakanjag4uh/bangalore1cciy0m/malleswarambeklxt\"}";
		
		sa = new SoftAssert();
		Response response = given()
				
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)				
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(url)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		
		sa.assertEquals("Jc dashboard stats for total Ac and dg run hour comparision retrieved successfully.", response.jsonPath().getString("message"));

		String today_totalacRunHour = response.jsonPath().getString("data.today.totalAcRunHour");
		String today_totalDgRunHour = response.jsonPath().getString("data.today.totalDgRunHour");
		
		String yesterday_totalacRunHour = response.jsonPath().getString("data.yesterday.totalAcRunHour");
		String yesterday_totalDgRunHour = response.jsonPath().getString("data.yesterday.totalDgRunHour");

		System.out.println("Today total acRunHour value is : " +today_totalacRunHour);
		System.out.println("Today total DgRunHour value is : " +today_totalDgRunHour);

		System.out.println("Yesterday total acRunHour value is : " +yesterday_totalacRunHour);
		System.out.println("Yesterday total DgRunHour value is : " +yesterday_totalDgRunHour);

		sa.assertAll();  

	}
	
	//To get the values of total fuel consumed
	@Test
	public void total_fuel_consumed()
	{
		getStoredat1(site);

		site=sitename;
		String path = storeDatpath;
		System.out.println(storeDatpath);
		SoftAssert sa = new SoftAssert();
		String url = "https://eanalytix.jvts.net/accounts/api/events/v4/stats/dashboard/fuel-consumed\r\n"
				+ "";
		String requestpayload = "{\"time\":{\"from\":1709490600000,\"to\":1709576999999},\"granularityType\":\"day\",\"isStatsEnabled\":true,\"dat\":\"64f82a7514e88d81dce8ab57/south47zkzj/karnatakanjag4uh/bangalore1cciy0m/malleswarambeklxt\"}";
		
		sa = new SoftAssert();
		Response response = given()
				
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)				
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(url)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		
		sa.assertEquals("Jc dashboard stats for total fuel consumed retrieved successfully.", response.jsonPath().getString("message"));

		String totalDgFuelLevelConsumed = response.jsonPath().getString("data.totalDgFuelLevelConsumed");


		System.out.println("totalDgFuelLevelConsumed value is : " +totalDgFuelLevelConsumed);


		sa.assertAll();  

	}

	
	//To get the values of today and yesterday totalConsumeSEBActiveEnergy and totalConsumeDGActiveEnergy
	@Test
	public void total_consumption_energy_comparision()
	{
		getStoredat1(site);

		site=sitename;
		String path = storeDatpath;
		System.out.println(storeDatpath);
		SoftAssert sa = new SoftAssert();
		String url = "https://eanalytix.jvts.net/accounts/api/events/v4/stats/dashboard/total-consumption-energy-comparision\r\n"
				+ "";
		String requestpayload = "{\"time\":{\"from\":1711114200000,\"to\":1711121399999},\"dat\":\"64f82a7514e88d81dce8ab57/south47zkzj/karnatakanjag4uh/bangalore1cciy0m/malleswarambeklxt\"}";
		
		sa = new SoftAssert();
		Response response = given()
				
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)				
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(url)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		
		sa.assertEquals("Jc dashboard stats for total seb and dg consumptions energy comparision retrieved successfully.", response.jsonPath().getString("message"));
		
		String today_totalConsumeSEBActiveEnergy = response.jsonPath().getString("data.today.totalConsumeSEBActiveEnergy");
		String today_totalConsumeDGActiveEnergy = response.jsonPath().getString("data.today.totalConsumeDGActiveEnergy");
		
		String yesterday_totalConsumeSEBActiveEnergy = response.jsonPath().getString("data.yesterday.totalConsumeSEBActiveEnergy");
		String yesterday_totalConsumeDGActiveEnergy = response.jsonPath().getString("data.yesterday.totalConsumeDGActiveEnergy");

		System.out.println("Today today_totalConsumeSEBActiveEnergy value is : " +today_totalConsumeSEBActiveEnergy);
		System.out.println("Today today_totalConsumeDGActiveEnergy value is : " +today_totalConsumeDGActiveEnergy);

		System.out.println("Yesterday today_totalConsumeSEBActiveEnergy value is : " +yesterday_totalConsumeSEBActiveEnergy);
		System.out.println("Yesterday yesterday_totalConsumeDGActiveEnergy value is : " +yesterday_totalConsumeDGActiveEnergy);


		sa.assertAll();  

	}	
	
	
	//To get the values of totalConsumeSEBActiveEnergy, totalConsumeDGActiveEnergy, AverageEnergy
	@Test
	public void average_consumption_energy()
	{
		getStoredat1(site);

		site=sitename;
		String path = storeDatpath;
		System.out.println(storeDatpath);
		SoftAssert sa = new SoftAssert();
		String url = "https://eanalytix.jvts.net/accounts/api/events/v4/stats/dashboard/average-consumption-energy\r\n"
				+ "";
		String requestpayload = "{\"time\":{\"from\":1703269800000,\"to\":1711045800000},\"isStatsEnabled\":true,\"dat\":\"64f82a7514e88d81dce8ab57/south47zkzj/karnatakanjag4uh/bangalore1cciy0m/malleswarambeklxt\"}";
		
		sa = new SoftAssert();
		Response response = given()
				
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)				
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(url)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		
		sa.assertEquals("Jc dashboard stats for Average consumptions energy retrieved successfully.", response.jsonPath().getString("message"));
		
		String totalConsumeSEBActiveEnergy = response.jsonPath().getString("data.totalConsumeSEBActiveEnergy");
		String totalConsumeDGActiveEnergy = response.jsonPath().getString("data.totalConsumeDGActiveEnergy");
		
//		String no_of_days = response.jsonPath().getString("data.No. of days");
		String AverageEnergy = response.jsonPath().getString("data.AverageEnergy");

		System.out.println("totalConsumeSEBActiveEnergy value is : " +totalConsumeSEBActiveEnergy);
		System.out.println("totalConsumeDGActiveEnergy value is : " +totalConsumeDGActiveEnergy);

//		System.out.println("no_of_days value is : " +no_of_days);
		System.out.println("AverageEnergy value is : " +AverageEnergy);


		sa.assertAll();  

	}	
	
	
	
	//To get the values of data missing(Expected data count, Actual data count)
	@Test
	public void data_missing()
	{
		getStoredat1(site);

		site=sitename;
		String path = storeDatpath;
		System.out.println(storeDatpath);
		SoftAssert sa = new SoftAssert();
		String url = "https://eanalytix.jvts.net/accounts/api/events/v3/kpi-stats/JC/data-missing\r\n"
				+ "";
		String requestpayload = "{\"time\":{\"from\":1710959400000,\"to\":1711045799999},\"granularityType\":\"day\",\"isStatsEnabled\":true,\"dat\":\"64f82a7514e88d81dce8ab57/south47zkzj/karnatakanjag4uh/bangalore1cciy0m/malleswarambeklxt\"}";
		sa = new SoftAssert();
		Response response = given()
				
				.and()
				.header("User-Agent", "PostmanRuntime/7.32.2")
				.and()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)				
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(url)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		
		sa.assertEquals("JC missing data stats retrieved successfully.", response.jsonPath().getString("message"));
		
		String actualDataCount = response.jsonPath().getString("data.actualDataCount");
		String expectedDataCount = response.jsonPath().getString("data.expectedDataCount");
		


		System.out.println("actualDataCount value is : " +actualDataCount);
		System.out.println("expectedDataCount value is : " +expectedDataCount);


		sa.assertAll();  

	}	
	
}
