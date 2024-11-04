package com.rrlems.prd.dashboards;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import io.restassured.response.Response;



public class RrlSubmeter extends RrlBaseClass {
	public static List currentdatList=null;
	public static List acldatList=null;
	public static List runHourList=null;
	public static List<String> getcurrentdatList;
	public static List<String> getcurrentmacList;
	public static List<String> getdeviceidList;
	public static List<String> getdevicenameList;
	public static Response response=null;
	public static String datapath=null;
	public static int writecount=1;
	public static int writecount_live=1;
	public static int writecount_avg=1;
	public static int sumofAchours = 0;
	public static int dataPacketCount = 0;
	public String assetDataMissingvalue="";
	public static int dataPacketCount_day = 144;
	public static int dataPacketCount_week = 0;
	public static int dataPacketCount_month = 0;
	public static List<String> siteDatList=null;
	public static List<String> siteNameList =null;
	public static double avg_powerfactor = 0;

	public static int avg_calc_days = 90;

	public static double avg_value = 0;
	public static double acrunhr_avg_value = 0;
	public static double dg_efficiency_avg_value = 0;
	public static double dg_fuel_consumed_avg_value = 0;
	public static int days=89;
	public static int no_events_days_count=0;

	public static String json_path_activeEnergy="data.meter.activeEnergy";
	public static String jsonpath_avg_powerLoad="data.meter.activePower";
	public static String json_path_pf="data.meter.powerFactor";
	public static String json_path_apparentEnergy="data.meter.apparentEnergy";


	public static double changeinEfficiency=0;
	public static double diffinEfficiency=0;

	public static double energyGenerated=0;
	public static double energyAccounted=0;
	public static double energyUnAccounted=0;
	public static double billedPF=0;

	public static double carbonEmission=0;
	public static double carbonEmissionsqft=0;


	public static double areaofStore=1000;
	public static double enpi=0;
	public static double co2_calfixed=2.653;


	public static double allocatedBudget=0;
	public static double utilizeddBudgetpercent=0;


	@Test
	public void submeter_calc() {
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
		//System.out.println(acldatList);
		//System.out.println(name);
		for (int i = 0; i < name.size(); i++) {
			String s = name.get(i).toString();
			//System.out.println("s:"+s);
			//if (s.equalsIgnoreCase("Ludo Mall"))
			if (s.startsWith(sitename))
			{
				//System.out.println(s);
				//System.out.println(acldatList.get(i).toString());
				datapath = acldatList.get(i).toString();
				//System.out.println("datapath : "+datapath);
				getcurrentDat(datapath,acldatList);
				//break;

			}

		}
		//	System.out.println(getcurrentdatList.size());
		//for(int c=0;c<acldatList.size();c++)
		int i=0;
		int c=0;
		double main_energy=0;
		double main_avg_powerfactor=0;
		double mains_avg_PowerLoad=0;
		double mains_apparentEnergy=0;

		double lighting_energy=0;
		double lighting_avg_powerfactor=0;
		double lighting_avg_PowerLoad=0;

		double power_energy=0;
		double power_avg_powerfactor=0;
		double power_avg_PowerLoad=0;

		double lift_energy=0;
		double lift_avg_powerfactor=0;
		double lift_avg_PowerLoad=0;

		double hvac_energy=0;
		double hvac_avg_powerfactor=0;
		double hvac_avg_PowerLoad=0;
		
		double ac_energy=0;
		double ac_avg_powerfactor=0;
		double ac_avg_PowerLoad=0;
		
		double unknown_device_energy=0;
		double unknown_device_avg_powerfactor=0;
		double unknown_device_avg_PowerLoad=0;
		
		
		
		for(String ab:getcurrentdatList)
		{
			String macid=getcurrentmacList.get(c).toString();
			String devicename=getdevicenameList.get(c).toString();
			//String deviceid=getdeviceidList.get(c).toString();
			String sitedat= acldatList.get(i).toString();
			i=acldatList.indexOf(ab);
			String site1= name.get(i).toString();

			//System.out.println(site1);	
			//System.out.println(macid);
			//System.out.println(devicename);
			String deviceid=get_device_id(macid);
			//System.out.println("deviceid for : "+macid+" : "+deviceid);
			//energyComparision(site1,deviceid);


			//System.out.println(/************************************/);
			System.out.println(/************************************/);
			System.out.println("Device Name : "+devicename);
			if(devicename.contains("Main"))
			{
				main_energy=Math.round(testenergyCalculation(deviceid,"Test",json_path_activeEnergy));
				System.out.println("main_energy : "+main_energy);

				main_avg_powerfactor=Math.round(avgPfCalculation(deviceid));
				System.out.println("main_avg_powerfactor : "+main_avg_powerfactor);

				mains_avg_PowerLoad=avgCalculation(deviceid,jsonpath_avg_powerLoad);
				System.out.println("Avg Power Load for Mains : "+mains_avg_PowerLoad);


				mains_apparentEnergy=Math.round(testenergyCalculation(deviceid,"Test",json_path_apparentEnergy));
				System.out.println("mains_apparentEnergy for Mains : "+mains_apparentEnergy);

			}
			else if(devicename.contains("Light"))
			{

				{
					lighting_energy=Math.round(testenergyCalculation(deviceid,"Test",json_path_activeEnergy));
					System.out.println("lighting_energy : "+lighting_energy);

					lighting_avg_powerfactor=Math.round(avgPfCalculation(deviceid));
					System.out.println("lighting_avg_powerfactor : "+lighting_avg_powerfactor);

					lighting_avg_PowerLoad=avgCalculation(deviceid,jsonpath_avg_powerLoad);
					System.out.println("Avg Power Load for Lighting : "+lighting_avg_PowerLoad);
				}
			}
			else if(devicename.contains("Power"))
			{
				power_energy=Math.round(testenergyCalculation(deviceid,"Test",json_path_activeEnergy));
				System.out.println("power_energy : "+power_energy);

				power_avg_powerfactor=Math.round(avgPfCalculation(deviceid));
				System.out.println("power_avg_powerfactor : "+power_avg_powerfactor);

				power_avg_PowerLoad=avgCalculation(deviceid,jsonpath_avg_powerLoad);
				System.out.println("Avg Power Load for Power : "+power_avg_PowerLoad);

			}
			else if(devicename.contains("Lift"))
			{
				lift_energy=Math.round(testenergyCalculation(deviceid,"Test",json_path_activeEnergy));
				System.out.println("lift_energy : "+lift_energy);

				lift_avg_powerfactor=Math.round(avgPfCalculation(deviceid));
				System.out.println("lift_avg_powerfactor : "+lift_avg_powerfactor);

				lift_avg_PowerLoad=avgCalculation(deviceid,jsonpath_avg_powerLoad);
				System.out.println("Avg Power Load for Lift : "+lift_avg_PowerLoad);

			}
			else if(devicename.contains("HVAC Submeter") || (devicename.contains("Split AC Panel")))
			{
				hvac_energy=Math.round(testenergyCalculation(deviceid,"Test",json_path_activeEnergy));
				System.out.println("hvac_energy : "+hvac_energy);

				hvac_avg_powerfactor=Math.round(avgPfCalculation(deviceid));
				System.out.println("hvac_avg_powerfactor : "+hvac_avg_powerfactor);

				hvac_avg_PowerLoad=avgCalculation(deviceid,jsonpath_avg_powerLoad);
				System.out.println("Avg Power Load for hvac : "+hvac_avg_PowerLoad);

			}
		/*	else if(devicename.contains("Split AC"))
			{
				hvac_energy=Math.round(testenergyCalculation(deviceid,"Test",json_path_activeEnergy));
				System.out.println("ac_energy : "+ac_energy);

				hvac_avg_powerfactor=Math.round(avgPfCalculation(deviceid));
				System.out.println("ac_powerfactor : "+ac_avg_powerfactor);

				hvac_avg_PowerLoad=avgCalculation(deviceid,jsonpath_avg_powerLoad);
				System.out.println("Avg Power Load for ac : "+ac_avg_PowerLoad);

			}
			*/
			else 
			{
				unknown_device_energy=Math.round(testenergyCalculation(deviceid,"Test",json_path_activeEnergy));
				System.out.println("unknown_device_energy : "+unknown_device_energy);

				unknown_device_avg_powerfactor=Math.round(avgPfCalculation(deviceid));
				System.out.println("AVG unknown_device_powerfactor : "+unknown_device_avg_powerfactor);

				unknown_device_avg_PowerLoad=avgCalculation(deviceid,jsonpath_avg_powerLoad);
				System.out.println("AVG unknown_device_avg_PowerLoad : "+unknown_device_avg_PowerLoad);

			}
			


			c++;
		}

		try
		{
			billedPF=main_energy/mains_apparentEnergy;
			//System.out.println("mains billedPF : "+billedPF);
		}
		catch (Exception e) {
			// TODO: handle exception
			billedPF=0;

		}

	



		try {

			carbonEmission= (.85*main_energy);
			carbonEmission=carbonEmission/1000;
			carbonEmissionsqft=carbonEmission/(Integer.parseInt((areaOfStore)));

		}
		catch(Exception e)
		{
			carbonEmission=0;
			carbonEmission=0;
			//System.out.println("co2emission :"+carbonEmission);
			//System.out.println("carbonEmissionsqft :"+carbonEmissionsqft);	
		}

		try
		{
			
			enpi=main_energy/(Integer.parseInt((areaOfStore)));
			//System.out.println("enpi : "+enpi);
		}
		catch (Exception e) {
			enpi=0;
			//System.out.println("enpi : "+enpi);
			// TODO: handle exception
		}
		
		energyGenerated=main_energy;
		energyAccounted=hvac_energy+lift_energy+power_energy+lighting_energy;
		energyUnAccounted=main_energy-energyAccounted;

		budget(energyGenerated);

	

		System.out.println("energyGenerated : "+energyGenerated);
		System.out.println("energyUnAccounted : "+energyUnAccounted);
		System.out.println("energyAccounted : "+energyAccounted);
		System.out.println("mains billedpf : "+billedPF);
		System.out.println("co2emission :"+carbonEmission);
		System.out.println("carbonEmissionsqft :"+carbonEmissionsqft);
		System.out.println("enpi : "+enpi);
		write_to_excel(ec_ops_outputfile_submeter,writecount_live,0,1,2,3,4,5,6,7,8,9,10,11,12,13,sitename,main_energy, main_avg_powerfactor,mains_avg_PowerLoad,mains_apparentEnergy,hvac_energy,hvac_avg_powerfactor,hvac_avg_PowerLoad,lighting_energy,lighting_avg_powerfactor,lighting_avg_PowerLoad,power_energy,power_avg_powerfactor,power_avg_PowerLoad);
		write_to_excel(ec_ops_outputfile_energyanalysis,writecount_live,0,1,2,3,4,5,6,7,8,9,10,sitename, energyGenerated, energyUnAccounted, energyAccounted, billedPF, carbonEmission, carbonEmissionsqft, enpi,  allocatedBudget, energyGenerated, utilizeddBudgetpercent);
		
		sa.assertAll();
	}

	public void getcurrentDat(String datapath,List acldatList)
	{
		try {
			//System.out.println("datapath:"+datapath);
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
			getcurrentdatList= new ArrayList();
			getcurrentmacList=new ArrayList();
			getdeviceidList=new ArrayList();
			getdevicenameList=new ArrayList();

			for(int i=0;i<acldatList1.size();i++)
			{
				String s=acldatList1.get(i).toString();

				for(int x=0;x<currentdat.size();x++)
				{
					String cs=currentdat.get(x).toString();
					String name=nameList.get(x).toString();
					String macid=macIdList.get(x).toString();
					if(cs.equalsIgnoreCase(s))
					{
						getcurrentdatList.add(cs);
						getcurrentmacList.add(macid);
						getdevicenameList.add(name);

					}
				}

			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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

			payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"electricity_metering_data\",\"device\":{\"_id\":\""+macid+"\"}}";

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
					.post(generic_events_uri) 	
					.then()
					.extract().response();
			//System.out.println(response.prettyPrint());
			sa.assertEquals(response.statusCode(),200);
			int count = response.jsonPath().getInt("count");
			//System.out.println(count);

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
				//System.out.println("energylist : "+energylist);
				List<Float> wb1 = new ArrayList<>();
				while(energylist.remove(null))
				{
				}

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
					//System.out.println("last_active_packet_value:"+last_active_packet_value);
					for(int i=wb1.size()-1;i>0;i--)
					{
						if(wb1.get(i)!=0)	
						{
							first_active_packet_value=wb1.get(i).toString();
							//	System.out.println("first_active_packet_value:"+first_active_packet_value);
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
		catch (Exception e) {
			//e.printStackTrace();

			//System.out.println(macid+" : "+"No events found that match with the search keys.");
			energy_generated=0;
			opshours=0;
		}

		sa.assertAll();
		return energy_generated;

	}

	public double avgPfCalculation(String macid)

	{

		avg_powerfactor=0;
		String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"electricity_metering_data\",\"device\":{\"_id\":\""+macid+"\"}}";
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

		List powerFactor = response.getBody().jsonPath().getList(json_path_pf);
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
		//System.out.println("powerFactor:"+wb2);
		for(int i=0;i<wb2.size();i++)
		{
			if(wb2.get(i)!=0)
			{
				//total=total+wb2.get(i);
				total=total+Math.abs(wb2.get(i));
				avg_powerfactor = total /count;   
			}

			else
			{
				avg_powerfactor=0.00;
			}
		}
		return avg_powerfactor;
	}

	public double avgCalculation(String macid,String jsonpath)
	{
		avg_value=0;
		double average=0;
		String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"electricity_metering_data\",\"device\":{\"_id\":\""+macid+"\"}}";
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
					avg_value = average; 
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

	public void testMainCalcultion(String store,String id)

	{

		double tankcapcity=0;
		double fuelprice=0;
		double storeareavalue=0;
		getStoredat(store);
		tankcapcity=parseStringvalue(fuelTankCapacity);
		fuelprice=parseStringvalue(fuelPrice);
		storeareavalue=parseStringvalue(areaOfStore);
		String macid=id;

		//	double eb_energy=Math.round(testenergyCalculation(macid,"Test",json_path_sebLoadManager1ActiveEnergyKWH));
		//System.out.println("eb_energy : "+eb_energy);


		//write_to_excel(ec_ops_outputfile,writecount,0,1,2,3,4,5,6,7,8,9,10,11,12,13,store, eb_energy, dg_energy,energy,dgFuelConsumed,dgrunhr,EnergyConsumedperSqftvalue,dgFuelefficiencyvalue,costInCured,runhrs,acEnergyEffeciency,dataPacketCount,gap,avg_powerfactor);

	}

	public double testenergyCalculation(String id,String chaneltype,String jsonpath)
	{
		double energy_generated = 0;
		String start_active_energy = null;
		String end_active_energy = null;
		String macid=id;

		SoftAssert sa = new SoftAssert();
		try {



			String payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"electricity_metering_data\",\"device\":{\"_id\":\""+macid+"\"}}";
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

			if(count==0)
			{
				energy_generated=0;


				System.out.println("energy_generated : "+energy_generated);
				//
			}
			else {

				List energylist = response.getBody().jsonPath().getList(jsonpath);

				List<Float> wb1 = new ArrayList<>();
				//System.out.println(energylist);
				while(energylist.remove(null))
				{
				}
				while(energylist.remove("0"))
				{
				}
				if(energylist.size()==0)
				{
					energy_generated=0;

					//System.out.println("energy_generated : "+energy_generated);
				}
				else
				{
					for(Object obj:energylist)
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

				}


			}
			try {
				energy_generated = Double.parseDouble(start_active_energy)-Double.parseDouble(end_active_energy);
				if(energy_generated<=0)
				{
					energy_generated=0;
				}
			}catch (NumberFormatException e){
				//e.printStackTrace();
				energy_generated=0;
				opshours=0;

				System.out.println("not a number"); 
			} 

		}
		catch (Exception e) {
			//e.printStackTrace();

			System.out.println(macid+" : "+"No events found that match with the search keys.");
			energy_generated=0;
			opshours=0;
		}

		sa.assertAll();

		//System.out.println("energy_generated : "+energy_generated);
		return energy_generated;

	}

	public void budget(double mainenergy)
	{
		allocatedBudget=0;
		utilizeddBudgetpercent=0;

		//System.out.println(datapath);
		String payload="{\"dat\":\""+datapath+"\",\"time\":{\"from\":"+budget_from_date_in_milli+",\"to\":"+budget_to_date_in_milli+"},\"granularityType\":\"month\"}";
		String json_allocatedbudget="data.allocatedBudget";
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
				.post(generic_dashboard_budget)
				.then()
				.extract().response();
		sa.assertEquals(response.statusCode(),200);
		//sa.assertEquals(response.jsonPath().getString("message"),"Events retrieved successfully.");
		try {
		allocatedBudget=response.jsonPath().getDouble(json_allocatedbudget);
		}
		catch (Exception e) {
			allocatedBudget=0;
		}
		System.out.println("allocatedBudget : "+allocatedBudget);
		try {
		utilizeddBudgetpercent=((mainenergy)/(allocatedBudget))*100;
		}
		catch (Exception e) {
			utilizeddBudgetpercent=0;
			// TODO: handle exception
		}
		System.out.println("utilizeddBudget :"+mainenergy);
		System.out.println("utilizeddBudgetpercent(%) :"+utilizeddBudgetpercent);

	}



}
