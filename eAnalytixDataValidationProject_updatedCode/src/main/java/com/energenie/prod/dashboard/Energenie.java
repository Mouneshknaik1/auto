package com.energenie.prod.dashboard;
import java.time.Instant;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import static io.restassured.RestAssured.given;

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


public class Energenie extends BaseClassApi {

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
	public static double TotalSum = 0;
	public static List<Float> voltage = null;
	public static Double sum_voltage=0.0;
	public static Double sum_current=0.0;
	public static Double totalenergysum=0.0;
	public static Double sum_voltage_dispensed=0.0;
	public static Double sum_current_dispensed=0.0;
	public static Double totalenergysum_dispensed=0.0;

	public static Double totalcapacitysum=0.0;
	public static Double totalcapacitysum_dispensed=0.0;

	public static Double totalenergysum_kwh;
	public static Double totalenergysum_kwh_dispensed;

	public static Double Energy_consumed_for_Charging=0.0;
	public static Double Energy_consumed_for_DisCharging=0.0;
	public static List<Float> time= null;
	public static List<Long> timeDifferencesSeconds;
	public static List<Long> timeDifferenceMillis;


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
				.header("apiKey", apiKey)
				.and()
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
			System.out.println("slavmac : " +slavmac);

			//			get_device_id(macid);
			//			if("PAKDwUxCcrkFUu5Y_3".equals(slavmac)) 
			//			{
			//				energenieData();

			//				energenieData_dispensed();

			Energy_Consumed_Calcultion(site1,slavmac);
			Energy_Dispensed_Calcultion(site1,slavmac);
			//			TimeDifferenceCalculator();

			//			}

			System.out.println(/************************************/);
			c++;
		}

		sa.assertAll();
	}


	//	@org.testng.annotations.Test(dependsOnMethods = "getSitedatPath")
	public void energenieData()
	{
		String device="";
		sum=0;
		sum_voltage=0.0;
		sum_current=0.0;
		totalenergysum=0.0;
		totalcapacitysum=0.0;
		totalenergysum_kwh=0.0;


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
					if(Slave_id.contains("_"))
					{
						//						device=s;
						//						System.out.println(+rowcount+" : Slave Ids are : "+s);
						//						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+dg_controller+"\"}";
						//						payload="{\"from\":1718649000000,\"to\":1718735399999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceidis+"\"}";
						//						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"jc_periodic_data\",\"meterId\":\""+dg_controller+"\"}";
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
						List voltage = new ArrayList();
						List battery_status =new ArrayList();
						List current = new ArrayList();
						List timelist= new ArrayList();
						List timestamplist = new ArrayList();
						List filtertimelist=new ArrayList();
						List filtervoltage=new ArrayList();
						List filterbattery_status=new ArrayList();
						List filtercurrent=new ArrayList();
						List filtertimestamplist=new ArrayList();

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
							List voltageinner = responsnew.getBody().jsonPath().getList("data.meter.voltage");
							List battery_statusinner = responsnew.getBody().jsonPath().getList("data.meter.battery_status");
							List currentinner = responsnew.getBody().jsonPath().getList("data.meter.current");
							List timeinner = responsnew.getBody().jsonPath().getList("data.time");
							List timestamp = responsnew.getBody().jsonPath().getList("data.meter.tms");


							filtervoltage.addAll(voltageinner);
							filterbattery_status.addAll(battery_statusinner);
							filtercurrent.addAll(currentinner);
							filtertimelist.addAll(timeinner);
							filtertimestamplist.addAll(timestamp);

							//							System.out.println(payload);
							//							System.out.println(dyanmicuri);
							//							System.out.println(responsnew.prettyPrint());
							//							System.out.println("voltage:"+voltageinner.size()+"--"+voltageinner);
							//							System.out.println("battery_status:"+battery_statusinner.size()+"--"+battery_statusinner);
						}

						for(int i=0;i<filterbattery_status.size();i++) {
							if(filterbattery_status.get(i) != null) {
								voltage.add(filtervoltage.get(i));
								battery_status.add(filterbattery_status.get(i));
								current.add(filtercurrent.get(i));
								timelist.add(filtertimelist.get(i));
								timestamplist.add(filtertimestamplist.get(i));
							}
						}
						System.out.println("voltage:"+voltage.size()+"--"+voltage);
						System.out.println("battery_status:"+battery_status.size()+"--"+battery_status);
						System.out.println("current:"+current.size()+"---"+current);
						System.out.println("timelist:"+timelist.size()+"---"+timelist);
						System.out.println("timestamplist:"+timestamplist.size()+"---"+timestamplist);

						List battery_statuslist=new ArrayList();
						List filteredCurrent=new ArrayList();
						List filteredVoltage=new ArrayList();
						List filteredTime=new ArrayList();
						List filteredTimestamp=new ArrayList();


						for(int i=0;i<battery_status.size();i++) {
							//		
							if("CHARGING".equals(battery_status.get(i))) {


								battery_statuslist.add(battery_status.get(i));
								filteredVoltage.add(voltage.get(i));
								filteredCurrent.add(current.get(i));
								//				                filteredTime.add(convertEpochToISO((Long) timelist.get(i)));
								filteredTime.add(timelist.get(i));
								//				                filteredTime.add(convertEpochToISO((Long) timelist.get(i+1)));
								filteredTime.add(timelist.get(i+1));
								//								excludelist.add((Float) energylist.get(i));


								filteredTimestamp.add(Instant.parse((CharSequence) timestamplist.get(i)).atZone(ZoneOffset.UTC));

								filteredTimestamp.add(Instant.parse((CharSequence) timestamplist.get(i+1)).atZone(ZoneOffset.UTC));

							}else{
								//								System.out.println(energylist.get(i));
								//								if((energylistold.get(i) != null)) {
								//								voltagelist.add(voltagelist.get(i));
								//								battery_status.add(battery_status.get(i));

								//								}
							}
						}
						//						}
						System.out.println("filteredVoltage:"+filteredVoltage.size()+"--"+filteredVoltage);
						System.out.println("battery_statuslist:"+battery_statuslist.size()+"--"+battery_statuslist);
						System.out.println("filteredCurrent:"+filteredCurrent.size()+"---"+filteredCurrent);
						System.out.println("filteredTime:"+filteredTime.size()+"---"+filteredTime);
						System.out.println("filteredTimestamp:"+filteredTimestamp.size()+"---"+filteredTimestamp);


						List<Long> timeDiffFinal=timeDiffCall(filteredTime);
						//						System.out.println("sec diff:"+timeDiffFinal);



						System.out.println("sec diff:"+timeDiffFinal.size()+"---"+timeDiffFinal);


						List capacity =new ArrayList();
						List energyList =new ArrayList();


						// Check that timeDiffFinal size is the same as filteredVoltage and filteredCurrent
						int minSize = Math.min(Math.min(filteredVoltage.size(), filteredCurrent.size()), timeDiffFinal.size());


						for (int i = 0; i < minSize; i++) {

							if (filteredVoltage.get(i) != null) { // Ensure the value is not null before adding
								//			                    sum_voltage += (Float) filteredVoltage.get(i);
								//			                    sum_current += (Float) filteredCurrent.get(i);
							}
							float v = filteredVoltage.get(i) instanceof Float ? (Float) filteredVoltage.get(i) : ((Integer) filteredVoltage.get(i)).floatValue();
							float c = filteredCurrent.get(i) instanceof Float ? (Float) filteredCurrent.get(i) : ((Integer) filteredCurrent.get(i)).floatValue();
							long timeDiff = (long) timeDiffFinal.get(i);

							float calcap = (c * timeDiff) / 3600;
							float calen = calcap * v;

							capacity.add(calcap);
							energyList.add(calen);
						}
						//						float totalenergysum=0;
						for(int i=0;i<capacity.size();i++) {
							if (capacity.get(i) != null) { // Ensure the value is not null before adding
								totalcapacitysum += Math.abs((Float) capacity.get(i));

							}
						}

						for(int i=0;i<energyList.size();i++) {
							if (energyList.get(i) != null) { // Ensure the value is not null before adding
								totalenergysum += Math.abs((Float) energyList.get(i));

								totalenergysum_kwh = Math.abs(totalenergysum/1000);

							}
						}


						System.out.println("capacity List:"+capacity);
						System.out.println("Energy List:"+energyList);
						System.out.println("total Energy:"+totalenergysum);
						System.out.println("total Energy kwh :"+totalenergysum_kwh);
						System.out.println("total capacity:"+totalcapacitysum);
						System.out.println("total Energy_consumed_for_Charging:"+Energy_consumed_for_Charging);


						Energy_consumed_for_Charging +=totalenergysum_kwh;

						System.out.println("sum voltage-"+sum_voltage);
						System.out.println("sum voltage-"+sum_current);
					}
				}
		}

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}

		//		return sum;
		//		sa.assertAll();
	}



	public void energenieData_dispensed()
	{
		String device="";
		sum=0;
		sum_voltage_dispensed=0.0;
		sum_current_dispensed=0.0;
		totalenergysum_dispensed=0.0;
		totalcapacitysum_dispensed=0.0;
		totalenergysum_kwh_dispensed=0.0;


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
					int totalCount = 1500; 
					int pageSize = 100;


					String s= lpath.get(j).toString();
					String name=lname.get(j).toString();
					String Slave_id=slavmac;
					if(Slave_id.contains("_"))
					{
						//						device=s;
						System.out.println(+rowcount+" : Slave Ids are : "+s);
						//						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+dg_controller+"\"}";
						//						payload="{\"from\":1718649000000,\"to\":1718735399999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceidis+"\"}";
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

						List voltage_dis = new ArrayList();
						List battery_status_dis =new ArrayList();
						List current_dis = new ArrayList();
						List timelist_dis= new ArrayList();
						List timestamplist_dis = new ArrayList();

						List filtervoltage_dis = new ArrayList();
						List filterbattery_status_dis =new ArrayList();
						List filtercurrent_dis = new ArrayList();
						List filtertimelist_dis= new ArrayList();
						List filtertimestamplist_dis = new ArrayList();




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
							List voltageinner_dis = responsnew.getBody().jsonPath().getList("data.meter.voltage");
							List battery_statusinner_dis = responsnew.getBody().jsonPath().getList("data.meter.battery_status");
							List currentinner_dis = responsnew.getBody().jsonPath().getList("data.meter.current");
							List timeinner_dis = responsnew.getBody().jsonPath().getList("data.time");
							List timestamp_dis = responsnew.getBody().jsonPath().getList("data.meter.tms");

							filtervoltage_dis.addAll(voltageinner_dis);
							filterbattery_status_dis.addAll(battery_statusinner_dis);
							filtercurrent_dis.addAll(currentinner_dis);
							filtertimelist_dis.addAll(timeinner_dis);
							filtertimestamplist_dis.addAll(timestamp_dis);



							//							System.out.println(payload);
							//							System.out.println(dyanmicuri);
							//							System.out.println(responsnew.prettyPrint());
							//							System.out.println("voltage:"+voltageinner.size()+"--"+voltageinner);
							//							System.out.println("battery_status:"+battery_statusinner.size()+"--"+battery_statusinner);
						}
						System.out.println("voltage:"+voltage_dis.size()+"--"+voltage_dis);
						System.out.println("battery_status:"+battery_status_dis.size()+"--"+battery_status_dis);
						System.out.println("current:"+current_dis.size()+"---"+current_dis);
						System.out.println("timelist:"+timelist_dis.size()+"---"+timelist_dis);
						System.out.println("timestamplist:"+timestamplist_dis.size()+"---"+timestamplist_dis);



						for(int i=0;i<filterbattery_status_dis.size();i++) {
							if(filterbattery_status_dis.get(i) != null) {
								voltage_dis.add(filtervoltage_dis.get(i));
								battery_status_dis.add(filterbattery_status_dis.get(i));
								current_dis.add(filtercurrent_dis.get(i));
								timelist_dis.add(filtertimelist_dis.get(i));
								timestamplist_dis.add(filtertimestamplist_dis.get(i));
							}
						}
						System.out.println("voltage_dis:"+voltage_dis.size()+"--"+voltage_dis);
						System.out.println("battery_status_dis:"+battery_status_dis.size()+"--"+battery_status_dis);
						System.out.println("current_dis:"+current_dis.size()+"---"+current_dis);
						System.out.println("timelist_dis:"+timelist_dis.size()+"---"+timelist_dis);
						System.out.println("timestamplist_dis:"+timestamplist_dis.size()+"---"+timestamplist_dis);




						List battery_statuslist_dis=new ArrayList();
						List filteredCurrent_dis=new ArrayList();
						List filteredVoltage_dis=new ArrayList();
						List filteredTime_dis=new ArrayList();
						List filteredTimestamp_dis=new ArrayList();



						for(int i=0;i<battery_status_dis.size();i++) {

							if("DISCHARGING".equals(battery_status_dis.get(i))) {


								battery_statuslist_dis.add(battery_status_dis.get(i));
								filteredVoltage_dis.add(voltage_dis.get(i));
								filteredCurrent_dis.add(current_dis.get(i));
								//				                filteredTime.add(convertEpochToISO((Long) timelist.get(i)));
								filteredTime_dis.add(timelist_dis.get(i));
								//				                filteredTime.add(convertEpochToISO((Long) timelist.get(i+1)));
								//				                filteredTime_dis.add(timelist.get(i+1));


								//				                filteredTime_dis.add(timelist_dis.get(i+1));
								//								excludelist.add((Float) energylist.get(i));


								//				                filteredTimestamp_dis.add(Instant.parse((CharSequence) timestamplist_dis.get(i)).atZone(ZoneOffset.UTC));

								//				                filteredTimestamp_dis.add(Instant.parse((CharSequence) timestamplist_dis.get(i+1)).atZone(ZoneOffset.UTC));


								if (i < battery_status_dis.size() - 1) {
									filteredTime_dis.add(timelist_dis.get(i + 1));
									filteredTimestamp_dis.add(Instant.parse((CharSequence) timestamplist_dis.get(i)).atZone(ZoneOffset.UTC));
									filteredTimestamp_dis.add(Instant.parse((CharSequence) timestamplist_dis.get(i + 1)).atZone(ZoneOffset.UTC));
								} else {
									// Handle the last element separately
									filteredTimestamp_dis.add(Instant.parse((CharSequence) timestamplist_dis.get(i)).atZone(ZoneOffset.UTC));
									if ("DISCHARGING".equals(battery_status_dis.get(i))) {
										filteredTimestamp_dis.add(Instant.now().plusSeconds(60).atZone(ZoneOffset.UTC));
									}

								}
							}else{
								//								System.out.println(energylist.get(i));
								//								if((energylistold.get(i) != null)) {
								//								voltagelist.add(voltagelist.get(i));
								//								battery_status.add(battery_status.get(i));

								//								}
							}
						}
						//						}
						System.out.println("voltage_dis:"+filteredVoltage_dis.size()+"--"+filteredVoltage_dis);
						System.out.println("battery_status_dis:"+battery_statuslist_dis.size()+"--"+battery_statuslist_dis);
						System.out.println("current_dis:"+filteredCurrent_dis.size()+"---"+filteredCurrent_dis);
						System.out.println("filteredTime_dis:"+filteredTime_dis.size()+"---"+filteredTime_dis);
						System.out.println("filteredTimestamp_dis:"+filteredTimestamp_dis.size()+"---"+filteredTimestamp_dis);


						List timeDiffFinal_dis=timeDiffCall(filteredTime_dis);
						System.out.println("sec diff _dis:"+timeDiffFinal_dis);

						List capacity_dis =new ArrayList();
						List energyList_dis =new ArrayList();
						for(int i=0;i<filteredVoltage_dis.size();i++) {
							if (i < timeDiffFinal_dis.size()) {

								if (filteredVoltage_dis.get(i) != null) { // Ensure the value is not null before adding
									//			                    sum_voltage_dispensed += (Float) filteredVoltage_dis.get(i);
									//			                    sum_current_dispensed += (Float) filteredCurrent_dis.get(i);
								}

								float v = filteredVoltage_dis.get(i) instanceof Float ? (Float) filteredVoltage_dis.get(i) : ((Integer) filteredVoltage_dis.get(i)).floatValue();

								float c = filteredCurrent_dis.get(i) instanceof Float ? (Float) filteredCurrent_dis.get(i) : ((Integer) filteredCurrent_dis.get(i)).floatValue();
								long t=(long) timeDiffFinal_dis.get(i);
								float calcap_dis= (c*t)/3600;

								float calen_dis=Math.abs(calcap_dis*v);

								capacity_dis.add(calcap_dis);
								energyList_dis.add(calen_dis);
							}
						}


						for(int i=0;i<capacity_dis.size();i++) {
							if (capacity_dis.get(i) != null) { // Ensure the value is not null before adding
								totalcapacitysum_dispensed += Math.abs((Float) capacity_dis.get(i));

							}
						}

						for(int i=0;i<energyList_dis.size();i++) {
							if (energyList_dis.get(i) != null) { // Ensure the value is not null before adding
								totalenergysum_dispensed += Math.abs((Float) energyList_dis.get(i));
								totalenergysum_kwh_dispensed = Math.abs(totalenergysum_dispensed/1000);

							}
						}


						System.out.println("capacity_dis List:"+capacity_dis);
						System.out.println("Energy_dis List:"+energyList_dis);
						System.out.println("total Energy:"+totalenergysum_dispensed);
						System.out.println("total Energy kwh :"+totalenergysum_kwh_dispensed);
						System.out.println("total capacity:"+totalcapacitysum_dispensed);
						System.out.println("total Energy_consumed_for_DisCharging:"+Energy_consumed_for_DisCharging);


						Energy_consumed_for_DisCharging +=Math.abs(totalenergysum_kwh_dispensed);

						System.out.println("sum voltage-"+sum_voltage_dispensed);
						System.out.println("sum voltage-"+sum_current_dispensed);
					}
				}
		}		

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}

		//		return sum;
		//		sa.assertAll();
	}





	public List timestamp(List timeList) {
		List diftimeList=new ArrayList();
		List timeDifferencesMillis = new ArrayList();
		//        List timeDifferencesSeconds = new ArrayList();
		//        List timeDifferencesMinutes = new ArrayList();

		for (int t = 0; t <timeList.size()-1; t++) {
			//        	if(t%2==0){
			long timeDifferenceMillis = (long)timeList.get(t)-(long)timeList.get(t+1);
			//            long timeDifferenceSeconds = timeDifferenceMillis / 1000;
			//            long timeDifferenceMinutes = timeDifferenceSeconds / 60;

			timeDifferencesMillis.add(timeDifferenceMillis);
			//            timeDifferencesSeconds.add(timeDifferenceSeconds);
			//            timeDifferencesMinutes.add(timeDifferenceMinutes);
			//            }
		}
		return timeDifferenceMillis;
	}


	public List timeDiffCall(List timeList) {
		List diftimeList=new ArrayList();
		//	    List timeDifferencesMillis = new ArrayList();
		//        List timeDifferencesSeconds = new ArrayList();
		List timeDifferencesMinutes = new ArrayList();

		for (int t = 0; t <timeList.size()-1; t++) {
			if(t%2==0){

				//            long timeDifferenceMillis = (long)timeList.get(t)-(long)timeList.get(t+1);
				long timeDifferenceMillis = minuteConverter((long)(timeList.get(t)))-minuteConverter((long)(timeList.get(t+1)));
				//        		System.out.println("timeDifferenceMillis-"+timeDifferenceMillis);
				long Seconds = timeDifferenceMillis / 1000;
				long Minutes = Seconds / 60;

				//            timeDifferencesMillis.add(timeDifferenceMillis);
				//            timeDifferencesSeconds.add(timeDifferenceSeconds);
				timeDifferencesMinutes.add(Seconds);

				//            System.out.println("timeDifferenceMillis:" +timeDifferenceMillis);
				//            System.out.println("timeDifferencesMinutes:" +timeDifferencesMinutes);

			}
		}
		//        System.out.println("timestamp_value-"+timeDifferencesMinutes);
		return timeDifferencesMinutes;
	}

	public long minuteConverter(long timestamp_value)
	{

		long timestamp = timestamp_value/10000;
		long trimmedTimestamp = (long) Math.floor(timestamp)*10000;

		return trimmedTimestamp;

	}
	public void TimeDifferenceCalculator() {

		String device="";
		sum=0;
		sum_voltage=0.0;
		sum_current=0.0;
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
					int totalCount = 1500; 
					int pageSize = 100;


					String s= lpath.get(j).toString();
					String name=lname.get(j).toString();
					String dg_controller=slavmac;
					if(dg_controller.contains("_"))
					{
						//						device=s;
						System.out.println(+rowcount+" : Slave Ids are : "+s);
						//						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+dg_controller+"\"}";
						//						payload="{\"from\":1718649000000,\"to\":1718735399999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\""+deviceidis+"\"}";
						payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+dg_controller+"\"}";
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
						List voltage = new ArrayList();
						List battery_status =new ArrayList();
						List current = new ArrayList();
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
							List<Long> time = responsnew.getBody().jsonPath().getList("data.time");

							System.out.println("Time is : " +time);

							// Calculate time differences
							List<Long> timeDifferencesMillis = new ArrayList<>();
							List<Long> timeDifferencesSeconds = new ArrayList<>();
							List<Long> timeDifferencesMinutes = new ArrayList<>();

							for (int t = 0; t < time.size() - 1; t++) {
								long timeDifferenceMillis = time.get(t)-time.get(t + 1);
								long timeDifferenceSeconds = timeDifferenceMillis / 1000;
								long timeDifferenceMinutes = timeDifferenceSeconds / 60;

								timeDifferencesMillis.add(timeDifferenceMillis);
								timeDifferencesSeconds.add(timeDifferenceSeconds);
								timeDifferencesMinutes.add(timeDifferenceMinutes);
							}

							// Output the results

							for (int t1 = 0; t1 < timeDifferencesSeconds.size(); t1++) {
								float timeDifferencesSeconds_value = time.get(t1);
							}
							System.out.println("Time differences in milliseconds: " + timeDifferencesMillis);
							System.out.println("Time differences in seconds: " + timeDifferencesSeconds);
							System.out.println("Time differences in minutes: " + timeDifferencesMinutes);


						}

					}
				}
		}

		catch (Exception e) {

			e.printStackTrace();// TODO: handle exception
		}


	}


	public static String convertEpochToISO(Long object) {
		// Convert epoch milliseconds to LocalDateTime
		LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(object), ZoneId.systemDefault());
		// Format the date-time in ISO 8601 format
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		return dateTime.format(formatter);
	}


	public static void write_to_excel_toupdate(String opfile_name,int rownum,int cellnum0,String string)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/EnergenieData/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			//			sheet.getRow(rownum).createCell(cellnum0).setCellValue(filteredVoltage);
			System.out.println("filteredVoltage" +filteredVoltage);
			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void write_to_excel_new(String opfile_name,int rownum,int cellnum0,int cellnum1,String site,List<Float> filteredVoltage)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/EnergenieData/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			//			sheet.getRow(rownum).createCell(cellnum1).setCellValue(filteredVoltage);

			if (filteredVoltage != null) {

				for (int i = 0; i < filteredVoltage.size(); i++) {
					sheet.getRow(rownum).createCell(cellnum1 + i).setCellValue(filteredVoltage.get(i));
				}


				System.out.println("Excel filtered voltage :" +filteredVoltage);

				FileOutputStream fos = new FileOutputStream(excelPath);
				workbook.write(fos);
				workbook.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void write_to_excel_energy_consumed(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4, String site, String device_mac_id,double sumofenergy,double totalenergysum_kwh, double chargin_Energy_consumed)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/EnergenieData/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);

			sheet = workbook.getSheet(sitename);

			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(device_mac_id);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(sumofenergy);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(totalenergysum_kwh);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(chargin_Energy_consumed);


			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void write_to_excel_updated(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4, String site, String device_mac_id, double sumofenergy,double energy_kwh, double chargin_Energy_consumed)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/EnergenieData/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(sitename);
			//			rownum=voltage.size();
			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(device_mac_id);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(sumofenergy);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(energy_kwh);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(chargin_Energy_consumed);
			//			write_to_excel_updated(ec_ops_outputfile,writecount,0,1,2,3,4,store,id,sumofenergy,energy_kwh,chargin_Energy_consumed);


			FileOutputStream fos = new FileOutputStream(excelPath);
			workbook.write(fos);
			workbook.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}




	public static void write_to_excel_energy_dispensed(String opfile_name,int rownum,int cellnum0,int cellnum1,int cellnum2,int cellnum3,int cellnum4, String site, String device_mac_id,double sumofenergy_dispensed,double energy_kwh_dispensed, double dischargin_Energy_consumed)
	{
		try {	
			String excelPath=  System.getProperty("user.dir") + "/EnergenieData/"+opfile_name+".xlsx";
			FileInputStream file= new FileInputStream(excelPath);
			workbook = new XSSFWorkbook(file);

			sheet = workbook.getSheet(sitename);

			sheet.createRow(rownum);
			sheet.getRow(rownum).createCell(cellnum0).setCellValue(site);
			sheet.getRow(rownum).createCell(cellnum1).setCellValue(device_mac_id);
			sheet.getRow(rownum).createCell(cellnum2).setCellValue(sumofenergy_dispensed);
			sheet.getRow(rownum).createCell(cellnum3).setCellValue(energy_kwh_dispensed);
			sheet.getRow(rownum).createCell(cellnum4).setCellValue(dischargin_Energy_consumed);


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
		energenieData();
		double sumofvoltage=sum_voltage;
		double sumofcurrent=sum_current;
		double sumofcapacity=totalcapacitysum;
		double sumofenergy=totalenergysum;		        
		double energy_kwh = totalenergysum_kwh;
		double chargin_Energy_consumed = Energy_consumed_for_Charging;
		String macid=id;





		write_to_excel_updated(ec_ops_outputfile_energy_consumed,writecount,0,1,2,3,4,store,id,sumofenergy,energy_kwh,chargin_Energy_consumed);
		writecount++;
	}

	public void Energy_Dispensed_Calcultion(String store,String id)

	{


		energenieData_dispensed();
		double sumofenergy_dispensed=totalenergysum_dispensed;
		double energy_kwh_dispensed = totalenergysum_kwh_dispensed;

		double sumofvoltage_dispensed=sum_voltage_dispensed;
		double sumofcurrent_dispensed=sum_current_dispensed;
		double sumofcapacity_dispensed=totalcapacitysum_dispensed;
		double dischargin_Energy_consumed = Energy_consumed_for_DisCharging;



		write_to_excel_energy_dispensed(ec_ops_outputfile_energy_dispensed,writecount,0,1,2,3,4,store,id,sumofenergy_dispensed,energy_kwh_dispensed,dischargin_Energy_consumed);
		writecount++;
	}
}
