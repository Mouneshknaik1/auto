package com.solar.dev.dashboard;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.*; 
import java.util.stream.*; 
import java.util.function.*; 

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.commons.math3.util.Precision;
import org.openxmlformats.schemas.drawingml.x2006.main.impl.STLineEndTypeImpl;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender.Size;


public class SolarDashboardDataValidation extends BaseClassApi {
	public static List currentdatList=null;
	public static List acldatList=null;
	public static List runHourList=null;
	public static List<String> getcurrentdatList;
	public static List<String> getcurrentmacList;
	public static List<String> getdeviceidList;
	public static int inverterCount=0;
	public static Response response=null;
	String datapath;
	public static int writecount=1;
	public static int writecount_live=1;
	public static int writecount_avg=1;
	public static int dataPacketCount = 0;

	/**************************/
	public static double dcCapacity=0;
	public static double acCapacity=0;
	public static String deviceid_sensor=null;
	public static double cumulative_dccapacity=0;
	public static double cumulative_accapacity=0;

	public static double energyToday=0;
	public static double energyTillDate=0;
	public static double peakPower=0;
	public static List<Double> enegyTilldateList;
	public static List<Double> peakPowerList;
	public static List<Double> presentPowerList;
	public static double presentPower=0;

	public static double cumulative_energyToday=0;
	public static double cumulative_energyTillDate=0;
	public static double cumulative_peakPower=0;
	public static double cumulative_presentPower=0;
	public static double solarIrradiance=0;
	public static double weatherdeatails_windSpeed=0;
	public static double weatherdeatails_humidity=0;
	public static double weatherdeatails_moduleTemperature=0;

	public static double totalActiveEnergyImport=0;
	public static long totalActiveEnergyExport=0;
	public static double lifeTime_active_EnergyImport=0;
	public static long lifeTime_active_EnergyExport=0;
	public static double carbonemissioncalc=0;

	public static double capacityUtilizationFactor=0;
	public static double dcCapacityUtilizationFactor=0;
	public static double acCapacityUtilizationFactor=0;
	public static List<Long> timeSeriesStartList;
	public static Long startTime=null;
	public static List<Long> timeSeriesEndList;
	public static Long endTime=null;
	public static List<String> timeSeriesGenerationList;

	public static String SolartimeGeneration_StartTime=null;
	public static String SolartimeGeneration_EndTime=null;
	public static String SolartimeGeneration=null;

	public static List<String> convertNullToZeroList;
	public static List<Long> convertStringToLongList=null;
	public static String skipDGC="";

	public static long expectedEnergy=0;
	public static long expectedPerformanceRatio=0;
	
	public static long energyAvailableforConsumption=0;
	public static long bayendimportdeduction=0;
	public static long bayendexmportvalue=0;
	public static long transmissionLoss=0;
	public static double wheelingcharges=0;
	public static double bankingcharges=0;
	public static double netEnergyavailableforConsumption=0;
	public static long surplusDeficit=0;
	
	public static double transmissionLossFixed=2.764;
	public static double  bayendimportFixed=975;
	public static long 	bayendimportdeductionFixed=115;
	public static double wheelingchargesFixed=2.93;
	public static double bankingchargesFixed=8.0;
	public static double facilitiesAverageConsumptionFixed=116257;
	
	
	

	@Test
	public void solarTodaysData() {
		String site="";
		site=sitename;
		long value=0;
		uri = base_uri + user_id + "/acls";
		SoftAssert sa = new SoftAssert();
		enegyTilldateList= new ArrayList<>();
		peakPowerList = new ArrayList<>();
		presentPowerList = new ArrayList<>();
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
		cumulative_dccapacity=0;
		cumulative_accapacity=0;
		cumulative_energyToday=0;
		cumulative_energyTillDate=0;
		cumulative_peakPower=0;
		for(String ab:getcurrentdatList)
		{
			String macid=getcurrentmacList.get(c).toString();
			String sitedat= acldatList.get(i).toString();
			//System.out.println(acldatList.indexOf(ab));
			i=acldatList.indexOf(ab);
			String site1= name.get(i).toString();
			//System.out.println(/************************************/);
			//System.out.println(site1+" : "+macid);	
			//System.out.println(macid);
			String deviceid=get_device_id(macid);
			//System.out.println(deviceid);
			deviceCapacityCalc(deviceid);
			cumulative_dccapacity=cumulativecapacity(dcCapacity, cumulative_dccapacity);
			cumulative_accapacity=cumulativecapacity(acCapacity, cumulative_accapacity);
			//System.out.println(/************************************/);
			energyCalculation(deviceid,todaysDate_start_millis,todaysDate_end_millis);
			cumulative_energyToday=cumulativecapacity(energyToday, cumulative_energyToday);
			cumulative_energyTillDate=cumulativecapacity(energyTillDate, cumulative_energyTillDate);
			/*
			try {
				double value=0;
				value=cumulative_energyTillDate;
				value=Precision.round(cumulativecapacity(energyTillDate, cumulative_energyTillDate), 2);


				cumulative_energyTillDate=value/1000;
			}
			catch (Exception e) {
				// TODO: handle exception
				cumulative_energyTillDate=0;
			}
			 */
			c++;
		}

		//System.out.println("peakPowerList : "+peakPowerList);
		//System.out.println("presentPowerList : "+presentPowerList);
		cumulative_peakPower=cumulativevalue(peakPowerList);
		cumulative_presentPower=cumulativevalue(presentPowerList);
		//cumulative_peakPower=maxValue(peakPowerList);
		System.out.println("++++++++++ Todays Data("+to_date_today+") ++++++++++++++++");
		System.out.println("cumulative_dccapacity(kW) : "+cumulative_dccapacity);
		System.out.println("cumulative_accapacity(kW) : "+cumulative_accapacity);
		System.out.println("cumulative_energyToday(kW) : "+cumulative_energyToday);
		importExport(weatherSensor_sitename,todaysDate_start_millis,todaysDate_end_millis);
		System.out.println("cumulative_peakPower(kW) : "+cumulative_peakPower);
		System.out.println("cumulative_presentPower(kW) : "+cumulative_presentPower);
		solarIrradiance(weatherSensor_sitename,todaysDate_start_millis,todaysDate_end_millis);
		value=(long) cumulative_energyTillDate;
		cumulative_energyTillDate=value/1000;
		System.out.println("cumulative_energyTillDate(MWh) : "+cumulative_energyTillDate);
		//System.out.println(cumulative_energyTillDate/1000);
		carbonemissioncalc(cumulative_energyTillDate);
		System.out.println("Life time Co2 Reduction(kt) : "+carbonemissioncalc);
		//cufCalculation(cumulative_energyToday,cumulative_dccapacity,1);
		//System.out.println("capacityUtilizationFactor(%) : "+capacityUtilizationFactor);
		System.out.println("+++++++++++++++++++++++++++++++++++++++");
		sa.assertAll();
	}


	public void getcurrentDat(String datapath,List acldatList)
	{
		try {
			String payload="{\"flags\":{\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetType\":true},\"startsWith\":{\"datRegex\":\""+datapath+"\"},\"assetType\":{\"code\":\"pv_inverter\"}}";
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


	public void deviceCapacityCalc(String deviceId)
	{

		dcCapacity=0;
		acCapacity=0;
		String deviceDetailsUri=generic_get_device+deviceId;
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
				.get(deviceDetailsUri)
				.then()
				.extract().response();
		sa.assertEquals(response.statusCode(),200);
		//System.out.println(response.statusCode());
		String json_dcCapacity = "data.dc_capacity"; 
		String json_acCapacity = "data.ac_capacity";

		String dcCap=response.getBody().jsonPath().getString(json_dcCapacity);
		if(dcCap==null)
		{
			dcCapacity=0;
		}
		else
		{
			dcCapacity=response.getBody().jsonPath().getDouble(json_dcCapacity);
		}

		String acCap=response.getBody().jsonPath().getString(json_acCapacity);
		if(acCap==null)
		{
			acCapacity=0;
		}
		else
		{
			acCapacity=response.getBody().jsonPath().getDouble(json_acCapacity);
		}




		//System.out.println("dcCapacity : "+dcCapacity);
		//System.out.println("acCapacity : "+acCapacity);
	}

	public void energyCalculation(String deviceid,String stratdate,String enddate)
	{
		energyToday=0;
		energyTillDate=0;
		String payload ="{\"from\":"+stratdate+",\"to\":"+enddate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
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
		//System.out.println(response.statusCode());
		String json_energyToday = "data.meter.energyToday"; 
		String json_energyTillDate = "data.meter.energyTillDate";
		String json_peakpower = "data.meter.outputPower";
		String json_tms = "data.meter.tms";
		String json_time = "data.time";
		List<String> energyTodayList=response.getBody().jsonPath().getList(json_energyToday);
		List<String>  energyTillDateList=response.getBody().jsonPath().getList(json_energyTillDate);
		List<Double>  peakpowerList=response.getBody().jsonPath().getList(json_peakpower);
		List<Double> energyList = new ArrayList<>();
		//System.out.println("energyTodayList : "+energyTodayList);
		//System.out.println("energyTillDateList : "+energyTillDateList);
		energyList.clear();
		if(peakpowerList.size()!=0)
		{
			while(peakpowerList.remove(null))
			{
			}
			for(Object obj:peakpowerList)
			{
				energyList.add((double)((Number) obj).doubleValue());

			}
			presentPower=0;
			peakPower=0;
			peakPower=maxValue(energyList);
			peakPowerList.add(peakPower);
			presentPower=energyList.get(0);
			presentPowerList.add(presentPower);
			//System.out.println("presentPowerList : "+presentPowerList);
			//System.out.println("peakPowerList"+peakPowerList);
		}
		else
		{
			presentPower=0;
			peakPower=0;
			peakPowerList.add(peakPower);
			presentPowerList.add(presentPower);
		}

		energyList.clear();
		if(energyTodayList.size()!=0)
		{
			while(energyTodayList.remove(null))
			{
			}

			for(Object obj:energyTodayList)
			{
				energyList.add((double)((Number) obj).doubleValue());

			}

			//System.out.println("energyList :"+energyList);
			//energy=energyList.get(0);

			energyToday=energyList.get(0);

		}
		else
		{
			energyToday=0;
		}
		energyList.clear();
		if(energyTillDateList.size()!=0)
		{
			while(energyTillDateList.remove(null))
			{
			}

			for(Object obj:energyTillDateList)
			{
				energyList.add((double)((Number) obj).doubleValue());

			}


			//energy=energyList.get(0);
			energyTillDate=energyList.get(0);

		}
		else
		{
			energyTillDate=0;
		}

		//System.out.println("energyToday :"+energyToday);
		//System.out.println("energyTillDate : "+energyTillDate);

	}


	public double cumulativecapacity(double individualcapacity,double cumulativecapacity)
	{
		double totalCapacity=Precision.round((individualcapacity+cumulativecapacity),2);
		return totalCapacity;

	}

	public double cumulativevalue(List list)
	{
		double totalCapacity=0;

		try {
			if(list.size()!=0)
			{
				for(int i=0;i<list.size();i++)
				{
					totalCapacity=(double)(list.get(i))+totalCapacity;

				}
				totalCapacity=totalCapacity/1000;
			}
		}

		catch (Exception e) {
			// TODO: handle exception
			totalCapacity=0;
		}


		return totalCapacity;

	}

	public double maxValue(List list)
	{
		double maxvalue=0;
		if(list.size()==0)
		{
			maxvalue=0;
		}
		else
		{
			maxvalue=(double) Collections.max(list);

		}

		return maxvalue;

	}

	public void solarIrradiance(String weatherSensorsitename,String fromdate,String todate)
	{		

		//getDeviceid(weatherSensorsitename);
		//deviceid_sensor="652673bb268453661bdac707";
		getDeviceid(weatherSensor_sitename,assetType);
		String deviceid=deviceid_sensor;
		String json_solarIrradiance="data.meter.RadiationTemperature";
		String json_pvPanelTemperature="data.meter.pvPanelTemperature";
		String json_windSpeed="data.meter.windSpeed";
		String json_humidity="data.meter.humidity";
		solarIrradiance=0;
		String payload ="{\"from\":"+fromdate+",\"to\":"+todate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
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
		List<String> solarIrradianceList=response.getBody().jsonPath().getList(json_solarIrradiance);
		List<String> pvPanelTemperatureList=response.getBody().jsonPath().getList(json_pvPanelTemperature);
		List<String> humidityList=response.getBody().jsonPath().getList(json_humidity);
		List<String> windSpeedList=response.getBody().jsonPath().getList(json_windSpeed);
		solarIrradiance=getValue(solarIrradianceList);
		System.out.println("solarIrradiance(W/m 2) : "+solarIrradiance);
		weatherdeatails_moduleTemperature=getValue(pvPanelTemperatureList);
		System.out.println("weatherdeatails_moduleTemperature(C) : "+weatherdeatails_moduleTemperature);
		weatherdeatails_humidity=getValue(humidityList);
		System.out.println("weatherdeatails_humidity(%) : "+weatherdeatails_humidity);
		weatherdeatails_windSpeed=getValue(windSpeedList);
		System.out.println("weatherdeatails_windSpeed(km/h) : "+weatherdeatails_windSpeed);
	}


	public void getDeviceid(String site,String assettype)
	{
		deviceid_sensor=null;
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
		//System.out.println(response.jsonPath().getString("message"));
		sa.assertEquals("ACLs retrieved successfully for user.", response.jsonPath().getString("message"));
		List<String> name = response.jsonPath().getList("data.dat.name");
		List<String> acldatList = response.jsonPath().getList("data.dat.path");


		for (int i = 0; i < name.size(); i++) {
			String s = name.get(i).toString();
			if (s.equals(site)) {
				datapath = acldatList.get(i).toString();
				//System.out.println("weatherSensor_sitename name :"+site);
				//System.out.println("weatherSensor_sitename path :"+datapath);
				break;

			}

		}
		deviceid_sensor=get_device_id(assettype,datapath);
		//System.out.println("deviceid_sensor : "+deviceid_sensor);
	}


	public void importExport(String mfm,String fromdate,String todate)
	{		

		getDeviceid(weatherSensor_sitename,assetType);
		String deviceid=deviceid_sensor;
		totalActiveEnergyImport=0;
		totalActiveEnergyExport=0;
		//getDeviceid(weatherSensorsitename);
		double lastPacket=0;
		double firstPacket=0;
		//deviceid_sensor="6527e8fb7e6ed52ef7eb8cb3";
		//String deviceid=deviceid_sensor;
		String json_totalActiveEnergyImport="data.meter.totalActiveEnergyImport";
		String json_totalActiveEnergyExport="data.meter.totalActiveEnergyExport";
		solarIrradiance=0;
		String payload ="{\"from\":"+fromdate+",\"to\":"+todate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
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
		sa.assertEquals(response.statusCode(),200);
		List<String> totalActiveEnergyImportList=response.getBody().jsonPath().getList(json_totalActiveEnergyImport);
		List<String> totalActiveEnergyExportList=response.getBody().jsonPath().getList(json_totalActiveEnergyExport);
		//System.out.println("totalActiveEnergyImportList"+totalActiveEnergyImportList);
		//System.out.println("totalActiveEnergyExportList"+totalActiveEnergyExportList);

		List<Double> wb = new ArrayList<>();

		try {
			wb.clear();

			if(totalActiveEnergyImportList.size()!=0)
			{
				lastPacket=0;
				firstPacket=0;
				while(totalActiveEnergyImportList.remove(null))
				{

				}
				for(Object obj:totalActiveEnergyImportList)
				{
					wb.add((double)((Number) obj).floatValue());

				}
				lifeTime_active_EnergyImport=wb.get(0);
				firstPacket=wb.get(wb.size()-1);
				totalActiveEnergyImport=lifeTime_active_EnergyImport-firstPacket;
			}
			else
			{
				lastPacket=0;
				firstPacket=0;
				totalActiveEnergyImport=0;

			}


		}
		catch (Exception e) {
			totalActiveEnergyImport=0;
			//System.out.println("solarIrradiance : "+solarIrradiance);
			// TODO: handle exception
		}

		try {
			wb.clear();
			if(totalActiveEnergyExportList.size()!=0)
			{
				lastPacket=0;
				firstPacket=0;
				while(totalActiveEnergyExportList.remove(null))
				{

				}
				for(Object obj:totalActiveEnergyExportList)
				{
					wb.add((double)((Number) obj).floatValue());

				}
				lifeTime_active_EnergyExport=(long) Precision.round((wb.get(0)),2);
				firstPacket=Precision.round((wb.get(wb.size()-1)),2);
				totalActiveEnergyExport=(long) (lifeTime_active_EnergyExport-firstPacket);
				//System.out.println("Present_solarIrradiance : "+lastPacket);


			}
			else
			{
				lastPacket=0;
				firstPacket=0;
				totalActiveEnergyExport=0;
				//System.out.println("Present_solarIrradiance : "+lastPacket);
			}


		}
		catch (Exception e) {
			totalActiveEnergyExport=0;
			//System.out.println("totalActiveEnergyExport : "+totalActiveEnergyExport);
			// TODO: handle exception
		}

		System.out.println("totalActiveEnergyExport(kW) : "+totalActiveEnergyExport);
		System.out.println("totalActiveEnergyImport(kW) : "+totalActiveEnergyImport);


		lifeTime_active_EnergyExport=kwhtoMwhConversion(lifeTime_active_EnergyExport);
		System.out.println("lifeTime_active_EnergyExport(MWh)  : "+lifeTime_active_EnergyExport);
		lifeTime_active_EnergyImport=kwhtoMwhConversion(lifeTime_active_EnergyImport);
		System.out.println("lifeTime_active_EnergyImport(MWh) : "+lifeTime_active_EnergyImport);

	}

	public void carbonemissioncalc(double energygenerated)
	{		
		carbonemissioncalc=0;
		//getDeviceid(weatherSensorsitename);
		try
		{
			carbonemissioncalc=Precision.round((energygenerated*0.83),2);
		}
		catch (Exception e) {
			carbonemissioncalc=0;
			// TODO: handle exception
		}

	}


	public void cumulativeIrradianceCalculation(String weathersensor)
	{		
		getDeviceid(weatherSensor_sitename,assetType);
		String deviceid=deviceid_sensor;

		String json_solarIrradiance="data.meter.RadiationTemperature";
		String json_time="data.meter.pvPanelTemperature";


		solarIrradiance=0;
		String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
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
		List<String> solarIrradianceList=response.getBody().jsonPath().getList(json_solarIrradiance);
		List<String> timeList=response.getBody().jsonPath().getList(json_time);
		//System.out.println("solarIrradianceList : "+solarIrradianceList);
		//System.out.println("timeList : "+timeList);


	}


	public double cufCalculation(double energy, double dcCapacity,int days)
	{
		capacityUtilizationFactor=0;
		double value=0;
		try {
			value=(energy)/((24*days*dcCapacity))*100;
			capacityUtilizationFactor=Precision.round(value, 2);
		}
		catch (Exception e) {
			capacityUtilizationFactor=0;
			// TODO: handle exception
		}
		return capacityUtilizationFactor;
	}

	@Test
	public void solarSelectedDateData() {
		
		timeSeriesStartList = new ArrayList<>();
		timeSeriesEndList = new ArrayList<>();
		String site="";
		site=sitename;
		uri = base_uri + user_id + "/acls";
		SoftAssert sa = new SoftAssert();
		peakPowerList = new ArrayList<>();
		presentPowerList = new ArrayList<>();
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
				System.out.println("SiteName : "+s);
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
		cumulative_dccapacity=0;
		cumulative_accapacity=0;
		cumulative_energyToday=0;
		cumulative_energyTillDate=0;
		cumulative_peakPower=0;
		inverterCount=getcurrentmacList.size();
		System.out.println("InverterCount : "+inverterCount);
		for(String ab:getcurrentdatList)
		{


			String macid=getcurrentmacList.get(c).toString();
			//System.out.println("macid: "+macid);
			String sitedat= acldatList.get(i).toString();
			//System.out.println(acldatList.indexOf(ab));
			i=acldatList.indexOf(ab);
			String site1= name.get(i).toString();
			//System.out.println(/************************************/);
			//System.out.println(site1+" : "+macid);	
			//System.out.println(macid);
			String deviceid=get_device_id(macid);
			//System.out.println(deviceid);

			deviceCapacityCalc(deviceid);
			cumulative_dccapacity=cumulativecapacity(dcCapacity, cumulative_dccapacity);
			cumulative_accapacity=cumulativecapacity(acCapacity, cumulative_accapacity);

			//System.out.println(/************************************/);
			energyCalculation(deviceid,from_date_in_milli,to_date_in_milli);
			cumulative_energyToday=cumulativecapacity(energyToday, cumulative_energyToday);
			cumulative_energyTillDate=cumulativecapacity(energyTillDate, cumulative_energyTillDate);

			
			
			try {
				energyGenerationTimeCalculation(deviceid,from_date_in_milli,to_date_in_milli);

				//System.out.println(convertStringToLongList.get(number));
				//System.out.println(convertStringToLongList.size());

				//System.out.println("timeSeriesStartList :"+timeSeriesStartList);
				//System.out.println("timeSeriesEndList :"+timeSeriesEndList);
				long starttime=Collections.min(timeSeriesStartList);
				//System.out.println("starttime : "+starttime);
				long endtime=Collections.min(timeSeriesEndList);
				//System.out.println("endtime : "+endtime);

				String start_Time = milliTodays(starttime);
				//System.out.println("start_Time : "+start_Time);
				String end_Time = milliTodays(endtime);
				//System.out.println("end_Time : "+end_Time);

				long duration=0;
				duration=endtime-starttime;
				//System.out.println("duration : "+duration);
				SolartimeGeneration_StartTime= start_Time;
				SolartimeGeneration_EndTime=end_Time;
				SolartimeGeneration=milliTodaysUTC((endtime-starttime));
				//System.out.println("SolartimeGeneration : "+SolartimeGeneration);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}




			/*	try {
				double value=0;
				cumulative_energyTillDate=0;
				value=Precision.round(cumulativecapacity(energyTillDate, cumulative_energyTillDate),2);
				//cumulative_energyTillDate=Precision.round(value, 2);

				cumulative_energyTillDate=value/1000;
			}
			catch (Exception e) {
				// TODO: handle exception
				cumulative_energyTillDate=0;
			}
			 */
			c++;
		}
		//System.out.println("peakPowerList : "+peakPowerList);
		//System.out.println("presentPowerList : "+presentPowerList);
		System.out.println("+++++++++"+from_date+" to : "+to_date);
		cumulative_peakPower=cumulativevalue(peakPowerList);
		cumulative_presentPower=cumulativevalue(presentPowerList);
		cumulative_peakPower=maxValue(peakPowerList);
		//System.out.println(peakPowerList);
		//System.out.println("cumulative_dccapacity(kW) : "+cumulative_dccapacity);
		//System.out.println("cumulative_accapacity(kW) : "+cumulative_accapacity);
		System.out.println("cumulative_energyToday(kW) : "+cumulative_energyToday);
		importExport(weatherSensor_sitename,from_date_in_milli,to_date_in_milli);
		System.out.println("cumulative_peakPower(kW) : "+cumulative_peakPower);
		//System.out.println("cumulative_presentPower(kW) : "+cumulative_presentPower);
		//solarIrradiance(weatherSensor_sitename,from_date_in_milli,to_date_in_milli);
		//System.out.println("cumulative_energyTillDate(kWh) : "+cumulative_energyTillDate);
		carbonemissioncalc(cumulative_energyToday);
		System.out.println("Carbon Saving(kt) : "+carbonemissioncalc);
		dcCapacityUtilizationFactor = cufCalculation(cumulative_energyToday,cumulative_dccapacity,1);
		System.out.println("DC capacityUtilizationFactor(%) : "+dcCapacityUtilizationFactor);
		acCapacityUtilizationFactor=cufCalculation(cumulative_energyToday,cumulative_accapacity,1);
		System.out.println("AC capacityUtilizationFactor(%) : "+acCapacityUtilizationFactor);
		
		
		System.out.println("Solar Power Generation Start Time : "+SolartimeGeneration_StartTime);
		System.out.println("Solar Power Generation End Time : "+SolartimeGeneration_EndTime);
		System.out.println("Solar Power Generation Time : "+SolartimeGeneration);

		cumulativeIrradianceCalculation();
		energyAvailableforConsumptionCalc((long) cumulative_energyToday,eEBBayendimportfixedvalue);	
		System.out.println("energyAvailableforConsumption : "+energyAvailableforConsumption);
		surplusdeficitCalc(cumulative_energyToday);
		System.out.println("surplus/Deficit : "+surplusDeficit);
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		sa.assertAll();
	}

	public double getValue(List getList)
	{
		double getValue=0;
		genericgetList = new ArrayList<>();
		genericgetList.clear();
		try {

			if(getList.size()!=0)
			{

				for(Object obj:getList)
				{
					if(obj==null)
					{
						genericgetList.add((long) 0);
					}
					else
					{
						genericgetList.add((long)((Number) obj).longValue());
					}

				}

				//getValue=Precision.round((double) genericgetList.get(0),2);
				getValue=Precision.round((double) genericgetList.get(0),2);
				//System.out.println("Present_moduleTemperature(c) : "+weatherdeatails_moduleTemperature);

			}
			else
			{
				getValue=0;
				//"Present_moduleTemperature : "+getValue);
			}
		}
		catch (Exception e) {
			getValue=0;
			//System.out.println("getValue : "+getValue);
			// TODO: handle exception
		}
		return getValue;
	}



	public void energyCalc(List<Long> irradianceList,List<Long> timeList)
	{
		List<Float> energyList =new ArrayList<>();
		float energy=0;
		cumulativeIrradiance=0;
		float radiationTemperature=0;
		float timeDifferenceInSeconds=0;

		try {
			if(irradianceList.size()==timeList.size())
			{
				//System.out.println("Radiance");
				for(int i=0;i<irradianceList.size();i++)
				{
					energy=0;
					timeDifferenceInSeconds=0;
					radiationTemperature=0;
					radiationTemperature=irradianceList.get(i);
					timeDifferenceInSeconds=timeList.get(i);

					energy = ((radiationTemperature)*(timeDifferenceInSeconds))/3600;
					energyList.add((float) energy);
					//System.out.println(energy);
					//System.out.println(timeDifferenceInSeconds);
					cumulativeIrradiance = cumulativeIrradiance +energy;
					//System.out.println("radiationTemperature"+ i +" :"+radiationTemperature);
					//System.out.println("timeDifferenceInSeconds"+ i +" :"+timeDifferenceInSeconds);
					//System.out.println("cumulativeIrradiance"+ i +" :"+energy);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		/*
		if(energyList.isEmpty())
		{
			cumulativeIrradiance=0;
		}
		else
		{
		for(int i=0;i<energyList.size();i++)
		{
			cumulativeIrradiance=cumulativeIrradiance+energyList.get(i).doubleValue();
		}
		 */
		try {
			cumulativeIrradiance=cumulativeIrradiance/1000;
		}
		catch (Exception e) {
			cumulativeIrradiance=0;
		}

		//System.out.println("Instantaneous Cumulative radiation List : "+energyList);

	}

	public void cumulativeIrradianceCalculation()
	{
		getDeviceid(weatherSensor_sitename,assetType);
		String deviceid=deviceid_sensor;
		String json_solarIrradiance="data.meter.RadiationTemperature";
		String json_pvPanelTemperature="data.meter.pvPanelTemperature";
		String json_time="data.time";

		float solarIrradiance=0;
		float pvPanelTemperature=0;

		String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"eventCode\":\"300\",\"device\":{\"_id\":\""+deviceid+"\"}}";
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
		List<Long> solarIrradianceList=response.getBody().jsonPath().getList(json_solarIrradiance);
		//System.out.println("solarIrradianceList"+solarIrradianceList);
		getValue(solarIrradianceList);
		solarIrradianceList=genericgetList;

		List<Long> timeList=response.getBody().jsonPath().getList(json_time);

		List<Long> pvPanelTemperatureList=response.getBody().jsonPath().getList(json_pvPanelTemperature);
		getValue(pvPanelTemperatureList);
		pvPanelTemperatureList.clear();
		pvPanelTemperatureList=genericgetList;
		List<Long> timeDifferenceInSecondsList = new ArrayList<>();
		List<Long> numberofDataPointsPerHourList = new ArrayList<>();
		for(int i=0;i<timeList.size();i++)
		{
			int j=i+1;

			if(j<timeList.size())
			{
				long currentpacketTime=timeList.get(i);
				long previousacketTime=timeList.get(j);

				long numberofDataPointsPerHour=0;
				long timeDifferenceInSeconds=0;
				try {
					timeDifferenceInSeconds=(currentpacketTime-previousacketTime)/1000;
					numberofDataPointsPerHour=Math.round((60 * 60 * 60) / (timeDifferenceInSeconds * 60));
					numberofDataPointsPerHourList.add(numberofDataPointsPerHour);
					timeDifferenceInSecondsList.add(timeDifferenceInSeconds);
				}
				catch (Exception e) {
					timeDifferenceInSeconds=0;
					numberofDataPointsPerHour=0;
					// TODO: handle exception
				}
			}

			else
			{
				numberofDataPointsPerHourList.add((long) 0);
				timeDifferenceInSecondsList.add((long) 0);
			}

		}
		int diffValue =timeList.size()-numberofDataPointsPerHourList.size();
		if((timeList.size()-numberofDataPointsPerHourList.size())>1)
		{

			for(int i=0;i<diffValue;i++)
			{
				numberofDataPointsPerHourList.add(i,(long) 0);
				timeDifferenceInSecondsList.add((long) 0);
			}
		}

		//System.out.println("TimeListDiff : "+numberofDataPointsPerHourList);
		//System.out.println(diffTimeList.size());
		//Collections.reverse(numberofDataPointsPerHourList);
		//Collections.reverse(timeDifferenceInSecondsList);
		energyCalc(solarIrradianceList,timeDifferenceInSecondsList);
		System.out.println("cumulativeIrradiance(kWh/m 2) :"+cumulativeIrradiance);

		energyCalc(solarIrradianceList,pvPanelTemperatureList,numberofDataPointsPerHourList);
		System.out.println("cumulativeInsulation :"+cumulativeInsulation);


		performanceRatioCalc(cumulative_energyToday, cumulative_dccapacity, cumulativeInsulation);
		System.out.println("performanceRatio(%) : "+performanceRatio );
	
		
		expectedEnergyPerformanceRatioCalculation(lossFactor,cumulative_dccapacity,cumulativeInsulation,cumulativeIrradiance);
	    System.out.println("expectedEnergy :"+expectedEnergy);
	    System.out.println("expectedPerformanceRatio :"+expectedPerformanceRatio);

	}
		

	public void energyCalc(List<Long> irradianceList,List<Long> pvPanelTemperatureList, List<Long> timeList)
	{
		List<Double> energyList =new ArrayList<>();
		double energy=0;
		cumulativeInsulation=0;
		float radiationTemperature=0;
		float pvPanelTemperature=0;
		float numberofDataPointsPerHour=0;
		if(irradianceList.size()==timeList.size())
		{
			for(int i=0;i<irradianceList.size();i++)
			{
				double value=0;
				radiationTemperature=irradianceList.get(i);
				numberofDataPointsPerHour=timeList.get(i);
				pvPanelTemperature=pvPanelTemperatureList.get(i);
				try {
					value=(double) (radiationTemperature * (0.001) * ((1 - (0.0039 * (pvPanelTemperature - 25))) / numberofDataPointsPerHour));
					//cumulativeInsulation= value+cumulativeInsulation;
					if(value<0)
					{
						value=0;
					}
				}
				catch (Exception e) {
					// TODO: handle exception
					value=0;

					//cumulativeInsulation= value+cumulativeInsulation;
				}

				//System.out.println("Cumulative Insulation value : "+value);
				//cumulativeInsulation= value+cumulativeInsulation;
				energyList.add(value);

			}
		}
		if(energyList.size()>2)
		{
			//System.out.println(energyList.get(0));
			//System.out.println(energyList.get(energyList.size()-2));
			//cumulativeInsulation=energyList.get(energyList.size()-2);

			for(int i=0;i<energyList.size()-2;i++)
			{
				double value=0;

				if(Double.isNaN(energyList.get(i))) {
					value=0;
					cumulativeInsulation=cumulativeInsulation+value;
				}
				else
				{
					cumulativeInsulation=cumulativeInsulation+energyList.get(i).doubleValue();
				}
			}
		}
		else
		{
			cumulativeInsulation=0;	
		}

		//System.out.println("Cumulative Insulation List : "+energyList);
		//System.out.println("Cumulative Insulation value : "+cumulativeInsulation);

	}

	public void performanceRatioCalc(double energy, double dc_capacity, double radiance )
	{
		performanceRatio=0;
		try
		{
			performanceRatio=((energy)/((radiance)*(dc_capacity)))*100;
			//System.out.println(performanceRatio);
		}
		catch (Exception e) {
			// TODO: handle exception
			performanceRatio=0;
			e.printStackTrace();
		}
	}


	public void energyGenerationTimeCalculation(String deviceid,String stratdate,String enddate)
	{
		energyToday=0;
		energyTillDate=0;
		String payload ="{\"from\":"+stratdate+",\"to\":"+enddate+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceid+"\"}}";
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
		//System.out.println(response.statusCode());
		String json_energyToday = "data.meter.energyToday"; 
		String json_energyTillDate = "data.meter.energyTillDate";
		String json_tms = "data.meter.tms";
		String json_time = "data.time";
		List<String> energyTodayList=response.getBody().jsonPath().getList(json_energyToday);
		//System.out.println("energyTodayList :"+energyTodayList);

		List<Long>  timeListintime=response.getBody().jsonPath().getList(json_time);
		//System.out.println("timeListintime :"+timeListintime);
		List<String>  tmsListmilliseconds=response.getBody().jsonPath().getList(json_tms);

		try {

			convertNullToZeroList(energyTodayList);
			convertStringToLongList(convertNullToZeroList);
			int number =indexCount(convertStringToLongList);
			//int starttime_number =indexStartGenCount(convertStringToLongList);
			int starttime_number =indexStartCount(convertStringToLongList);

			if(energyTodayList.indexOf(null)!=energyTodayList.lastIndexOf(null))
			{
				int indexoffirstEnergynull=0;

				indexoffirstEnergynull=energyTodayList.indexOf(null);
				//System.out.println("starttime_number :"+starttime_number);
				//System.out.println("timeListintime :"+timeListintime.size());
				startTime=timeListintime.get((starttime_number));
				String endenergy=null;
				endenergy=convertNullToZeroList.get(number).toString();

				endTime=timeListintime.get(number);
				//System.out.println("Solar endtime :"+endTime);
				//System.out.println(endtime);


			}
			else if(energyTodayList.indexOf(null)==energyTodayList.lastIndexOf(null))
			{
				startTime=timeListintime.get(energyTodayList.size()-1);
				//System.out.println("indexoffirstEnergynull :"+timeListintime.get(indexoffirstEnergynull));
				//System.out.println(Collections.max(energyTodayList));
				//System.out.println("Solar startTime :"+startTime);
				String endenergy=convertNullToZeroList.get(number).toString();
				//System.out.println(endenergy);
				endTime=timeListintime.get(number);
				//System.out.println("Solar endtime :"+endTime);
				//System.out.println(endtime);

			}	

			else	
			{
				startTime=(long) 0;
				//System.out.println("Solar startTime :"+startTime);
				endTime=(long) 0;
				//System.out.println("Solar endtime :"+endTime);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		timeSeriesStartList.add(startTime);
		timeSeriesEndList.add(endTime);
	}	

	public static void convertNullToZeroList(List getList)
	{
		//convertNullToZeroList.clear();
		convertNullToZeroList = new ArrayList<>();

		try
		{
			for(int i=0;i<getList.size();i++)

			{
				if((getList.get(i)==null))
				{
					convertNullToZeroList.add("0");
				}
				else
				{
					convertNullToZeroList.add(getList.get(i).toString());
				}

			}
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		//System.out.println(convertNullToZeroList);
	}

	public static void convertStringToLongList(List<String> getList)
	{
		//convertStringToLongList.clear();
		convertStringToLongList = new ArrayList<>();
		List removedotList = new ArrayList<>();

		//System.out.println("convertStringToLongList getList : "+getList);
		for(String value:getList)
		{
			String number=null;
			if(value.contains("."))
			{
				number=value.replace(".","" );
				removedotList.add(number);
			}
			else
			{
				number=value.trim()+"0";
				removedotList.add(number);
			}
			//String number=value.replace(".","" );
			//removedotList.add(number);
		}
		//System.out.println(removedotList);

		try {
			for(Object obj:removedotList)
			{

				Long value=Long.valueOf((String) obj.toString());

				//long value=Long.parseLong((String) obj);
				//convertStringToLongList.add((Long)((Number) obj).longValue());
				//convertStringToLongList.add((long)((Number) obj).longValue());

				convertStringToLongList.add(value);
			}
		}
		catch (Exception e) {
			long value=0;
			// TODO: handle exception
			convertStringToLongList.add(value);
			//e.printStackTrace();
		}
		//System.out.println("convertStringToLongList :"+convertStringToLongList);

		//endTime=Collections.max(convertStringToLongList);
		//System.out.println("endTime : "+endTime);


	}


	public static long kwhtoMwhConversion(long energyvalue)
	{
		long value=0;
		try {
			value=(long) energyvalue/1000;
		}
		catch(Exception e)
		{
			value=0;
		}
		return value;
	}

	public static long kwhtoMwhConversion(double energyvalue)
	{
		long value=0;
		try {
			value=(long) energyvalue/1000;
		}
		catch(Exception e)
		{
			value=0;
		}
		return value;
	}


	public int indexCount(List getList)
	{
		//System.out.println("getList: "+getList);
		//System.out.println("getList Size: "+getList.size());
		int indexCount=0;
		List difflist = new ArrayList<>();
		for(int i=0;i<getList.size();i++)
		{
			if(i+1<getList.size())
			{
				Long current=(Long) getList.get(i);
				Long succeeding=(Long) getList.get(i+1);
				Long diff=current-succeeding;
				if(diff==0)
				{
					difflist.add(diff);

				}
				else
				{

					indexCount++;
					break;
				}
			}   		 
		}
		//System.out.println("difflist :"+difflist);
		int num= difflist.size();
		//System.out.println(num);
		return num;
	}

	public int indexStartGenCount(List getList)
	{
		//System.out.println("getList: "+getList);
		//System.out.println("getList Size: "+getList.size());
		int indexCount=0;
		List difflist = new ArrayList<>();
		for(int i=0;i<getList.size();i++)
		{
			if(i+1<getList.size())
			{
				Long current=(Long) getList.get(i);
				Long succeeding=(Long) getList.get(i+1);
				Long sum=current+succeeding;
				if(sum==0)
				{

					break;
				}
				else
				{
					difflist.add(sum);
					indexCount++;

				}
			}   		 
		}
		//System.out.println("Sumlist :"+difflist);
		int num= difflist.size();
		//System.out.println(num);
		return num;
	}

	public int indexendtGenCount(List getList)
	{
		//System.out.println("getList: "+getList);
		//System.out.println("getList Size: "+getList.size());
		int indexCount=0;
		List difflist = new ArrayList<>();
		for(int i=0;i<getList.size();i++)
		{
			if(i+1<getList.size())
			{
				Long current=(Long) getList.get(i);
				Long succeeding=(Long) getList.get(i+1);
				Long sum=current+succeeding;
				if(sum==0)
				{

					break;
				}
				else
				{
					difflist.add(sum);
					indexCount++;

				}
			}   		 
		}
		//System.out.println("Sumlist :"+difflist);
		int num= difflist.size();
		//System.out.println(num);
		return num;
	}


	public int indexStartCount(List getList)
	{
		//System.out.println("getList: "+getList);
		//System.out.println("getList Size: "+getList.size());
		int indexCount=0;
		//System.out.println("getList : "+getList);
		Collections.reverse(getList);
		//System.out.println("after revesre getList : "+getList);
		//System.out.println("after revesre getList size : "+getList.size());
		List difflist = new ArrayList<>();
		for(int i=0;i<getList.size();i++)
		{
			if(i+1<getList.size())
			{
				Long current=(Long) getList.get(i);
				Long succeeding=(Long) getList.get(i+1);
				Long diff=current-succeeding;
				if(diff==0||succeeding==0)
				{
					difflist.add(diff);


				}
				else
				{

					indexCount++;
					break;
				}
			}   		 
		}
		//System.out.println("difflist Size :"+difflist.size());
		int num= getList.size()-difflist.size()-1;
		//System.out.println(num);
		return num;
	}

	public void expectedEnergyPerformanceRatioCalculation(double lossFactor,double dc_capacity,double avgCumulativeInsulation, double avgCumulativeIrradiance)
	{
		expectedEnergy=0;
		expectedPerformanceRatio=0;

		try {
			
			expectedEnergy = (long) (lossFactor * dc_capacity * avgCumulativeInsulation);
		}
		catch (Exception e) {
			expectedEnergy=0;
		}
		try {
			expectedPerformanceRatio = (long) ((expectedEnergy / (dc_capacity * avgCumulativeIrradiance)) * 100);
		}
		catch(Exception e)
		{
			expectedPerformanceRatio=0;
		}
	}
	
	public void energyAvailableforConsumptionCalc(double cumulative_energyToday,double bayendimportFixed )
	{
		energyAvailableforConsumption=0;
		energyAvailableforConsumption=(long) (cumulative_energyToday+bayendimportFixed);
	}
	
	
	public void bayendimportdeductionCalc(long energy,long value)
	{
		bayendimportdeduction=0;
		try {
			bayendimportdeduction=energy*(value/100);
		}
		catch (Exception e) {
			// TODO: handle exception
			bayendimportdeduction=0;
		}
	}
	
	public void bayendexportCalc(long energy,long value)
	{
		bayendexmportvalue=0;
		try {
			bayendexmportvalue=energy-value;
		}
		catch (Exception e) {
			// TODO: handle exception
			bayendexmportvalue=0;
		}
	}

	public void transmissionLoss(long energy,double transmissionLossFixed2)
	{
		transmissionLoss=0;
		try {
			transmissionLoss=(long) (energy*(transmissionLossFixed2/100));
		}
		catch (Exception e) {
			// TODO: handle exception
			transmissionLoss=0;
		}
	}
	
	
	public void wheelingCharges(long bayendExportvalue,long transmissionlossvalue,double wheelingchargesvalue)
	{
		wheelingcharges=0;
		try {
			wheelingcharges=(bayendExportvalue-transmissionlossvalue)*wheelingchargesvalue;
		}
		catch (Exception e) {
			// TODO: handle exception
			wheelingcharges=0;
		}
	}
	
	
	public void bankingCharges(long bayendExportvalue,long transmissionlossvalue,double wheelingchargesvalue,double bankingChargesfixed)
	{
		bankingcharges=0;
		try {
			bankingcharges=(bayendExportvalue-transmissionlossvalue-wheelingchargesvalue)/bankingChargesfixed;
		}
		catch (Exception e) {
			// TODO: handle exception
			bankingcharges=0;
		}
	}
	
	public void netEnergyavailableforConsumptionCalc(long bayendExportvalue,long transmissionlossvalue,double wheelingchargesvalue,double bankingcharges)
	{
		netEnergyavailableforConsumption=0;
		try {
			netEnergyavailableforConsumption=bayendExportvalue+transmissionlossvalue+wheelingchargesvalue+bankingcharges;
		}
		catch (Exception e) {
			// TODO: handle exception
			netEnergyavailableforConsumption=0;
		}
	}
	
	
	public void surplusdeficitCalc(double energy)
	{
		getfixedCharges();
		energyAvailableforConsumptionCalc(cumulative_energyToday,bayendimportFixed);
		bayendexportCalc(1000,bayendimportdeductionFixed);
		bayendimportdeductionCalc(bayendimportdeduction,energyAvailableforConsumption);
		transmissionLoss(bayendexmportvalue,transmissionLossFixed);
		wheelingCharges(bayendexmportvalue,transmissionLoss,wheelingchargesFixed);
		bankingCharges(bayendexmportvalue,transmissionLoss,wheelingcharges,bankingchargesFixed);
		netEnergyavailableforConsumptionCalc(bayendexmportvalue,transmissionLoss,wheelingcharges,bankingcharges);
		surplusDeficit=0;
		try {
			surplusDeficit=(long) (netEnergyavailableforConsumption-facilitiesAverageConsumptionFixed);
		}
		catch (Exception e) {
			// TODO: handle exception
			surplusDeficit=0;
		}
	}
	
	
	
	public void getfixedCharges()
	{
		get_path(storecodeName);
		String payload ="{\"type\":\"dat\",\"dat\":{\"code\":\""+store_code+"\"},\"actions\":[\"create\",\"read\",\"update\",\"delete\"],\"flags\":{\"isPopulateUser\":true}}";
		//System.out.println(payload);
		String uri = base_uri + user_id + "/acls/search";
		//System.out.println(uri);
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
		
		//System.out.println(response.prettyPrint());
		 transmissionLossFixed=0;
		  bayendimportFixed=0;
		 bayendimportdeductionFixed=0;
		 wheelingchargesFixed=0;
		 bankingchargesFixed=0;
		 facilitiesAverageConsumptionFixed=0;
		
		if(response.statusCode()==200)
		{
			
			 acl_eBImportConstant=response.getBody().jsonPath().getString(json_eBImportConstant);
			 acl_transmissionLossasPerAgreement=response.getBody().jsonPath().getString(json_transmissionLossasPerAgreement);
			 acl_importDeductionasPerWBagreement=response.getBody().jsonPath().getString(json_importDeductionasPerWBagreement);
			 acl_wheelingCharges=response.getBody().jsonPath().getString(json_wheelingCharges);
			 acl_bankingCharges=response.getBody().jsonPath().getString(json_bankingCharges);
			 acl_estimatedConsumption=response.getBody().jsonPath().getString(json_estimatedConsumption);
			 transmissionLossFixed=Double.parseDouble(acl_transmissionLossasPerAgreement);
			 
			 try {
			 transmissionLossFixed=Double.parseDouble(acl_transmissionLossasPerAgreement);
			 bayendimportFixed=Double.parseDouble(acl_eBImportConstant);
			 bayendimportdeductionFixed=Long.parseLong(acl_importDeductionasPerWBagreement);
			 wheelingchargesFixed=Double.parseDouble(acl_wheelingCharges);
			 bankingchargesFixed=Double.parseDouble(acl_bankingCharges);
			 facilitiesAverageConsumptionFixed=Double.parseDouble(acl_estimatedConsumption);
			 }
			 catch(Exception e)
			 {
				 e.printStackTrace();
				 transmissionLossFixed=0;
				 bayendimportFixed=0;
				 bayendimportdeductionFixed=0;
				 wheelingchargesFixed=0;
				 bankingchargesFixed=0;
				 facilitiesAverageConsumptionFixed=0;
				 
			 }
			
		}
		else
		{
			 acl_eBImportConstant="";
			 acl_transmissionLossasPerAgreement="";
			 acl_importDeductionasPerWBagreement="";
			 acl_wheelingCharges="";
			 acl_bankingCharges="";
			 acl_estimatedConsumption="";
			 transmissionLossFixed=0;
			 bayendimportFixed=0;
			 bayendimportdeductionFixed=0;
			 wheelingchargesFixed=0;
			 bankingchargesFixed=0;
			 facilitiesAverageConsumptionFixed=0;
		}
		/*
		System.out.println("acl_eBImportConstant :"+acl_eBImportConstant);
		System.out.println("acl_transmissionLossasPerAgreement :"+acl_transmissionLossasPerAgreement);
		System.out.println("acl_importDeductionasPerWBagreement :"+acl_importDeductionasPerWBagreement);
		System.out.println("acl_wheelingCharges :"+acl_wheelingCharges);
		System.out.println("acl_bankingCharges :"+acl_bankingCharges);
		System.out.println("acl_estimatedConsumption :"+acl_estimatedConsumption);
		
		
		System.out.println("transmissionLossFixed :"+transmissionLossFixed);
		System.out.println("bayendimportFixed :"+bayendimportFixed);
		System.out.println("bayendimportdeductionFixed :"+bayendimportdeductionFixed);
		System.out.println("wheelingchargesFixed :"+wheelingchargesFixed);
		System.out.println("bankingchargesFixed :"+bankingchargesFixed);
		System.out.println("facilitiesAverageConsumptionFixed :"+facilitiesAverageConsumptionFixed);
		*/
		}
		
	}
	
	
	

