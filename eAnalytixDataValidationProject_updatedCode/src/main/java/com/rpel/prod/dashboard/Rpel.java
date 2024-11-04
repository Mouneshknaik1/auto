package com.rpel.prod.dashboard;
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

import io.restassured.response.Response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class Rpel extends BaseClassApi {

	public static List currentdatList=null;
	public static List acldatList=null;
	public static List<String> getcurrentdatList;
	public static List<String> getcurrentmacList;
	public static List<String> getdeviceidList;
	public static Response response=null;
	String datapath;
	public static int writecount=1;
	public static int rowcount=1;

	public static int writecount_live=1;
	public static int writecount_avg=1;

	public static List<String> siteDatList=null;
	public static List<String> siteNameList =null;
	public static Double firstTimestamp=0.0;
	public static Double lastTimestamp=0.0;
	
	public static Double firstimportValue=0.0;
	public static Double lastimportValue=0.0;
	public static Double firstexportValue=0.0;
	public static Double lastexportValue=0.0;
	public static Double totalimportValue=0.0;

	

	public static String slavmac="";



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

			//System.out.println(acldatList.indexOf(ab));
			i=acldatList.indexOf(ab);
			String site1= name.get(i).toString();
			System.out.println(/************************************/);
			System.out.println("site1 : " +site1);	
			System.out.println("macid : " +macid);
			slavmac=macid;
			String deviceid=get_device_id(macid);
			System.out.println("deviceid : " +deviceid);

			//			get_device_id(macid);
			//			if("PAKDwUxCcrkFUu5Y_3".equals(slavmac)) 
			//			{
			Energy_Consumed_Calcultion(site1,slavmac);


			//			}

			System.out.println(/************************************/);
			c++;
		}

		sa.assertAll();
	}


	//	@org.testng.annotations.Test(dependsOnMethods = "getSitedatPath")
	public void rpelDataCalculation()
	{
		String device="";
		firstTimestamp=0.0;
		lastTimestamp=0.0;
		firstimportValue=0.0;
//		importValueDifference=0.0;
	    totalimportValue=(Double) 0.0;
		
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

					String start_voltage = null;
					String end_voltage = null;
					int pageSize = 100;


					String s= lpath.get(j).toString();
					String name=lname.get(j).toString();
					String Slave_id=slavmac;
					//					if(Slave_id.contains("_"))
					//					{
					//						device=s;
					payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+Slave_id+"\"}";

					System.out.println("New payload : " +payload);
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
					List bessSOCkWh = new ArrayList();
					List<Double> bessExportedEnergy =new ArrayList();
					List bessImportedEnergy = new ArrayList();
					List timelist= new ArrayList();

					List timestamplist = new ArrayList();
					List filtertimelist=new ArrayList();
					List filterbessSOCkWh=new ArrayList();
					List filterbessExportedEnergy=new ArrayList();
					List filterbessImportedEnergy=new ArrayList();
					List filtertimestamplist=new ArrayList();

					List filterbessSOCkWh_value = new ArrayList();
					List filterbessExportedEnergy_value=new ArrayList();
					List filterbessImportedEnergy_value=new ArrayList();
					List filtertimelist_value=new ArrayList();


					List filteredbessSOCkWh = new ArrayList();
					List filteredbessExportedEnergy=new ArrayList();
					List filteredbessImportedEnergy=new ArrayList();
					List filteredtimelist=new ArrayList();


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
						List bessSOCkWh_value = responsnew.getBody().jsonPath().getList("data.meter.bessSOCkWh");
						List bessExportedEnergy_value = responsnew.getBody().jsonPath().getList("data.meter.bessExportedEnergy");
						List bessImportedEnergy_value = responsnew.getBody().jsonPath().getList("data.meter.bessImportedEnergy");
						List timeinner = responsnew.getBody().jsonPath().getList("data.time");


						bessSOCkWh.addAll(bessSOCkWh_value);
						bessExportedEnergy.addAll(bessExportedEnergy_value);
						bessImportedEnergy.addAll(bessImportedEnergy_value);
						timelist.addAll(timeinner);

						System.out.println("bessSOCkWh:"+bessSOCkWh.size()+"--"+bessSOCkWh);
						System.out.println("bessExportedEnergy:"+bessExportedEnergy.size()+"--"+bessExportedEnergy);
						System.out.println("bessImportedEnergy:"+bessImportedEnergy.size()+"--"+bessImportedEnergy);
						System.out.println("timelist:"+timelist.size()+"--"+timelist);

					}




					for(int i=0;i<bessExportedEnergy.size();i++) {
						if(bessExportedEnergy.get(i) != null) {

							filterbessSOCkWh_value.add(bessSOCkWh.get(i));
							filterbessExportedEnergy_value.add(bessExportedEnergy.get(i));
							filterbessImportedEnergy_value.add(bessImportedEnergy.get(i));
							filtertimelist_value.add(timelist.get(i));
							
						}
					}

					System.out.println("filterbessSOCkWh:"+filterbessSOCkWh.size()+"--"+filterbessSOCkWh_value);
					System.out.println("filterbessExportedEnergy:"+filterbessExportedEnergy.size()+"--"+filterbessExportedEnergy_value);
					System.out.println("filterbessImportedEnergy:"+filterbessImportedEnergy.size()+"--"+filterbessImportedEnergy_value);
					System.out.println("filtertimelist:"+filtertimelist.size()+"--"+filtertimelist_value);


					for(int i=0;i<filterbessExportedEnergy_value.size();i++) {
						//		

						filteredbessSOCkWh.add(filterbessSOCkWh_value.get(i));
						filteredbessExportedEnergy.add(filterbessExportedEnergy_value.get(i));
						filteredbessImportedEnergy.add(filterbessImportedEnergy_value.get(i));
						filteredtimelist.add(filtertimelist_value.get(i));

					}


					System.out.println("filteredbessSOCkWh:"+filteredbessSOCkWh.size()+"--"+filteredbessSOCkWh);
					System.out.println("filteredbessExportedEnergy:"+filteredbessExportedEnergy.size()+"--"+filteredbessExportedEnergy);
					System.out.println("filteredbessImportedEnergy:"+filteredbessImportedEnergy.size()+"--"+filteredbessImportedEnergy);
					System.out.println("filteredtimelist:"+filteredtimelist.size()+"--"+filteredtimelist);


					ArrayList<String> istTimestamps = convertToIST(filteredtimelist);

					// Loop through each hour for a 24-hour period
					for (int hour = 0; hour < 24; hour++) {
						// Format hour to HH:mm:ss
						String startTime = String.format("%02d:00:00", hour);
						String endTime = String.format("%02d:00:00", (hour + 1) % 24); // The next hour



						// Variables to store the first and last timestamp in the given range
						String firstTimestamp = null;
						String lastTimestamp = null;
						int i = 0;
						int k = 0;
						int firstindex = 0;
						int lastindex = 0;

						// Loop through the list and find the first and last timestamps in the desired range
						for (String timestamp : istTimestamps) {
							//System.out.println("timestamp----------"+timestamp);


							// Extract the time part (HH:mm:ss) from the timestamp
							String timePart = timestamp.substring(11, 19);



							// Compare timePart to see if it falls between one hour
							if (timePart.compareTo(startTime) >= 0 && timePart.compareTo(endTime) < 0) {
								// Set the first timestamp if it's the first match
								if (firstTimestamp == null) {
									firstindex = i;
									firstTimestamp = timestamp;
								}
								// Always update the last timestamp (the last match will be stored)
								lastTimestamp = timestamp;
								lastindex = k;
							}
							i++;
							k++;
						}

						List firstValue = new ArrayList<>();
						List lastValue = new ArrayList<>();
						List<Float> firstimportValue = new ArrayList<>();
						List<Float> firstexportValue = new ArrayList<>();
						List<Float> lastimportValue = new ArrayList<>();
						List<Float> lastexportValue = new ArrayList<>();


						// Print the first and last timestamp in the range for this hour
						if (firstTimestamp != null && lastTimestamp != null) {
							System.out.println("Hour: " + startTime + " to " + endTime);

							firstValue.add(firstTimestamp);
							lastValue.add(lastTimestamp);
							firstimportValue.add((Float) filteredbessImportedEnergy.get(firstindex));
							firstexportValue.add((Float) filteredbessExportedEnergy.get(firstindex));
							lastimportValue.add((Float) filteredbessImportedEnergy.get(lastindex));
							lastexportValue.add((Float) filteredbessExportedEnergy.get(lastindex));


							System.out.println("First timestamp: " + firstTimestamp);
							System.out.println("Last timestamp: " + lastTimestamp);


							System.out.println("First filteredbessImportedEnergy:"+firstimportValue.size()+"--"+firstimportValue);
							System.out.println("First filteredbessExportedEnergy:"+firstexportValue.size()+"--"+firstexportValue);
							System.out.println("Last filteredbessImportedEnergy:"+lastimportValue.size()+"--"+lastimportValue);
							System.out.println("Last filteredbessExportedEnergy:"+lastexportValue.size()+"--"+lastexportValue);
							
							
						    // Subtract the values to find the difference
						    if (!firstimportValue.isEmpty() && !lastimportValue.isEmpty()) {
						        float importValueDifference = Math.abs(lastimportValue.get(0) - firstimportValue.get(0));
						        
								   totalimportValue += Math.abs(importValueDifference);

						        System.out.println("Difference in imported energy: " + importValueDifference);
						        System.out.println("Total imported energy: " + totalimportValue);

						    }

						    if (!firstexportValue.isEmpty() && !lastexportValue.isEmpty()) {
						    	float exportValueDifference = Math.abs(lastexportValue.get(0) - firstexportValue.get(0));
						    	
								   totalexportValue += Math.abs(exportValueDifference);

						        System.out.println("Difference in exported energy: " + exportValueDifference);
						        System.out.println("Total exported energy: " + totalexportValue);

						    }

						    System.out.println();
						    
						    System.out.println();
						} else {
							System.out.println("Hour: " + startTime + " to " + endTime);
							System.out.println("No timestamps found.\n");
						}
				
					}
					
				}
		}




		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}

		//		return sum;
		//		sa.assertAll();


	}



	public static void write_to_excel_updated(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3, String site, String device_mac_id, double totalimport, double totalexport)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/RPELData/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			//			rownum=voltage.size();
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(device_mac_id);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(totalimport);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(totalexport);

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



	public void Energy_Consumed_Calcultion(String store,String id)

	{
		rpelDataCalculation();

		double totalimport=totalimportValue;
		double totalexport=totalexportValue;

		String macid=id;


		write_to_excel_updated(ec_ops_outputfile_rpel,writecount,0,1,2,3,store,id,totalimport,totalexport);
		writecount++;
	}


}
