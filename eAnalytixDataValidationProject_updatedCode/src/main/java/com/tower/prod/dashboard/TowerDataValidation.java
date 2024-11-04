package com.tower.prod.dashboard;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;


public class TowerDataValidation extends BaseClassApi {

	public static String base_event_uri=eid_uri+"accounts/api/events/v2/stats/";
	public static String generic_events_uri=eid_uri+"accounts/api/events/search?skip=0&limit=235";
	public static String dashboard_home_runhour_chart_tower="devices/home-runhour-chart-tower";


	public SoftAssert sa;
	public static String uri;
	public static String day="";
	public static String payload="";
	public static List deviceidList;
	public static String event_count="";
	public static int asset_count=0;

	@Test
	public void getassetdata()  throws IOException,SSLException
	{
		//setTimeout();
		RestAssured.config=RestAssured.config().httpClient(HttpClientConfig.httpClientConfig()
				.setParam("http.connection.timeout",300000)
				.setParam("http.socket.timeout",300000)
				.setParam("http.connection-manager.timeout",300000));
		String excelPath=  System.getProperty("user.dir") + "/TowerData/input_file/"+inputsheet;
		FileInputStream inputStream = new FileInputStream(excelPath);
		XSSFWorkbook wb=new XSSFWorkbook(inputStream);
		XSSFSheet sheet=wb.getSheet(sheet_name);
		int rowCount=sheet.getLastRowNum()-sheet.getFirstRowNum();
		System.out.println("Total number of Tower:"+rowCount);
		for(int i=1;i<=rowCount;i++){
			//for(int i=279;i<=384;i++){
			String event_count="";
			String event_code="390";
			float mainscumkwh_diff=0.0000000f;
			float mainscumRunH_diff=0000000f;
			float batCumKwh_diff=0.0000000f;
			float batCumRunH_diff=0.000000f;
			float energykwh_diff=0.0000000f;
			float dgRunH_diff=0000000f;
			float mppttotal_diff=0.0000000f;

			float kpi_energy_consumed=0.000000f;
			float kpi_ops_run_hr=0.000000f;
			float asset_event_energy_consumed_value =0.000000f;
			float asset_event_ops_hr_value =0.000000f;
			float energy_diff=0.000000f;
			float opsrunhr_diff=0.000000f;
			String diff_status_mathcing="Matching";
			String diff_status_not_matching="Not_Matching";
			String status_consumed="";
			String status_opshr="";
			String tower_id="";

			float kpi_energy_ebEnergy=0.000000f;
			float kpi_energy_batteryEnergy=0.000000f;
			long kpi_dashboard_api_responsetime=0;
			long asset_events_api_responsetime_smps=0;
			long asset_events_api_responsetime_tdg=0;
			long asset_events_api_responsetime=0;
			float cumulative_asset_event_energy_consumed_value =0.000000f;
			float cumulative_asset_event_ops_hr_value =0.000000f;
			float cumulative_asset_event_eb_value =0.000000f;



			int cellcount=sheet.getRow(i).getLastCellNum();
			for(int j=0;j<cellcount;j++){

				try {
					CellType celltype=sheet.getRow(i).getCell(j).getCellType();
					if(celltype.toString().equals("NUMERIC"))
					{
						tower_id = sheet.getRow(i).getCell(j).getRawValue();
						++j;
						//assettype = sheet.getRow(i).getCell(j).getRawValue();
					}
					else
					{
						tower_id = sheet.getRow(i).getCell(j).getStringCellValue();
						++j;
						//assettype = sheet.getRow(i).getCell(j).getStringCellValue();
					}

					System.out.print("Tower ID:"+i);
					System.out.println(":"+tower_id);
					//System.out.print("Asset Type:"+i);
					//System.out.println(":"+assettype);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				get_path(tower_id);
				get_device_id(tower_id);
				
				System.out.println(device_mac_id_list);
				System.out.println(device_asset_code_list);
				System.out.println(device_asset_imei_list);
				
				
				asset_count=device_mac_id_list.size();
				System.out.println("asset_count :"+asset_count);
				if(device_mac_id_list.size()!=0)
				{
					for(int c=0;c<device_mac_id_list.size();c++)
					{
						String deviceid= device_mac_id_list.get(c).toString();
						String device_imei=device_asset_imei_list.get(c).toString();
						/**************** solar *********************/
						if(deviceid.contains("battery"))
						{
							continue;
						}
						/**************** battery *********************/
						else if(deviceid.contains("solar"))
						{
							payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+deviceid+"\"}";
							//System.out.println(payload);	
							sa = new SoftAssert();
							Response response = given()
									.and()
									.config(RestAssured.config)
									.and()
									.header("User-Agent", "PostmanRuntime/7.32.2")
									.and()
									.header("Content-type", "application/json")
									.and()
									.header("eid", eid)
									.and()
									.relaxedHTTPSValidation()
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
							//System.out.println(response.statusCode());
							sa.assertEquals(200, response.statusCode());
							event_count=response.jsonPath().getString("count");
							asset_events_api_responsetime_tdg=response.getTimeIn(TimeUnit.SECONDS);		
							try {
								List mppttotalList = response.getBody().jsonPath().getList("data.meter.mppttotal");
								//System.out.println(mainsconsumed_list);
								//List energyKwhrunhrList = response.getBody().jsonPath().getList("data.meter.dgRunHr");
								//System.out.println(mainsconsumed_list);
								get_event_values(mppttotalList);
								mppttotal_diff = last_event_value-first_event_value;
								System.out.println("mppttotal_diff : "+mppttotal_diff);
								//get_event_values(energyKwhrunhrList);
								//dgRunH_diff = last_event_value-first_event_value;
								//System.out.println("dgRunH_diff : "+dgRunH_diff);
							}
							catch(Exception e)
							{
								System.out.println("No events found that match with the search keys.");
								e.printStackTrace();

							}
						}

						/**************** smps *********************/
						else if(deviceid.contains("smps")) {
							payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+deviceid+"\"}";


							System.out.println(payload);	
							sa = new SoftAssert();
							Response response = given()
									.and()
									.config(RestAssured.config)
									.and()
									.header("User-Agent", "PostmanRuntime/7.32.2")
									.and()
									.header("eid", eid)
									.and()
									.header("Content-type", "application/json")
									.and()
									.relaxedHTTPSValidation()
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
							//System.out.println(response.statusCode());
							sa.assertEquals(200, response.statusCode());
							event_count=response.jsonPath().getString("count");
							asset_events_api_responsetime_smps=response.getTimeIn(TimeUnit.SECONDS);		
							try {
								List mainsconsumed_list = response.getBody().jsonPath().getList("data.meter.mainsCumKwh");
								List mainsconsumedrunhr_list = response.getBody().jsonPath().getList("data.meter.mainsCumRunH");
								List batCumKwh_list = response.getBody().jsonPath().getList("data.meter.batCumKwh");
								List batCumRunH_list = response.getBody().jsonPath().getList("data.meter.batCumRunH");

								System.out.println(mainsconsumed_list);
								get_event_values(mainsconsumed_list);
								mainscumkwh_diff = last_event_value-first_event_value-energykwh_diff;
								System.out.println("mainscumkwh_diff : "+mainscumkwh_diff);

								get_event_values(mainsconsumedrunhr_list);
								mainscumRunH_diff = last_event_value-first_event_value-dgRunH_diff;
								System.out.println("mainscumRunH_diff : "+mainscumRunH_diff);

								get_event_values(batCumKwh_list);
								batCumKwh_diff = last_event_value-first_event_value;
								System.out.println("batCumKwh_diff : "+batCumKwh_diff);

								get_event_values(batCumRunH_list);
								batCumRunH_diff = last_event_value-first_event_value;
								System.out.println("batCumRunH_diff : "+batCumRunH_diff);

								asset_event_energy_consumed_value =mainscumkwh_diff+batCumKwh_diff;
								asset_event_ops_hr_value =mainscumRunH_diff+batCumRunH_diff+dgRunH_diff;
								/*System.out.println("+++++Energy consumption and Operational Hour for Tower id from asset events :"+tower_id+"+++++++");
								System.out.println("Energy Consumption : "+asset_event_energy_consumed_value);
								System.out.println("Operational Hour : "+asset_event_ops_hr_value);
								 */
							}
							catch(Exception e)
							{
								System.out.println("No events found that match with the search keys.");
								e.printStackTrace();

							}
						}
						/**************** DG calculation *********************/
						else if(deviceid.contains("tdg")){

							payload="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+deviceid+"\"}";
							//System.out.println(payload);payload);	
							sa = new SoftAssert();
							Response response = given()
									.and()
									.config(RestAssured.config)
									.and()
									.header("User-Agent", "PostmanRuntime/7.32.2")
									.and()
									.header("Content-type", "application/json")
									.and()
									.header("eid", eid)
									.and()
									.relaxedHTTPSValidation()
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

							//System.out.println(response.statusCode());
							sa.assertEquals(200, response.statusCode());
							event_count=response.jsonPath().getString("count");
							asset_events_api_responsetime_tdg=response.getTimeIn(TimeUnit.SECONDS);		
							try {
								List energyKwhList = response.getBody().jsonPath().getList("data.meter.energyKwh");
								//System.out.println(mainsconsumed_list);
								List energyKwhrunhrList = response.getBody().jsonPath().getList("data.meter.dgRunHr");
								//System.out.println(mainsconsumed_list);
								get_event_values(energyKwhList);
								energykwh_diff = last_event_value-first_event_value;
								System.out.println("energykwh_diff : "+energykwh_diff);
								get_event_values(energyKwhrunhrList);
								dgRunH_diff = last_event_value-first_event_value;
								System.out.println("dgRunH_diff : "+dgRunH_diff);
							}
							catch(Exception e)
							{
								System.out.println("No events found that match with the search keys.");
								e.printStackTrace();

							}
						}

						cumulative_asset_event_energy_consumed_value=asset_event_energy_consumed_value+energykwh_diff+mppttotal_diff;
						cumulative_asset_event_ops_hr_value=asset_event_ops_hr_value+dgRunH_diff;
						cumulative_asset_event_eb_value=mainscumkwh_diff-energykwh_diff;
						asset_events_api_responsetime=asset_events_api_responsetime_smps+asset_events_api_responsetime_tdg;

						System.out.println("+++++Energy consumption and Operational Hour for Tower id from asset events : " +tower_id+ "+++++++");
						System.out.println("Energy Consumption : cumulative_asset_event_energy_consumed_value : "+cumulative_asset_event_energy_consumed_value);
						System.out.println("Operational Hour : cumulative_asset_event_ops_hr_value : "+cumulative_asset_event_ops_hr_value);
						System.out.println("EB : cumulative_asset_event_eb_value : "+cumulative_asset_event_eb_value);
						System.out.println("asset_events_api_responsetime : cumulative_asset_events_api_responsetime : "+asset_events_api_responsetime);

					}

				}

			}

			if(store_dat_path=="")
			{
				kpi_energy_consumed=0;
				kpi_ops_run_hr=0;
				kpi_energy_ebEnergy=0;
				kpi_energy_batteryEnergy=0;
			}
			else {

				payload="{\"startDate\":"+from_date_in_milli+",\"endDate\":"+kpi_to_date_in_milli+",\"datpath\":\""+store_dat_path+"\"}";

				//System.out.println(uri_generic_api+dashboard_home_runhour_chart_tower);
				//System.out.println(payload);

				//sa = new SoftAssert();
				Response response1 = given()
						.config(RestAssured.config)
						.and()
						.header("User-Agent", "PostmanRuntime/7.32.2")
						.and()
						.header("Content-type", "application/json")
						.and()
						.header("eid", eid)
						.and()
						.relaxedHTTPSValidation()
						.and()
						.header("apiKey", apiKey)
						.and()
						.header("Authorization", BearerToken)
						.and()
						.body(payload)
						.when()
						.post(uri_generic_api+dashboard_home_runhour_chart_tower)
						.then()
						.extract().response();
				sa.assertEquals(200, response1.statusCode());
				kpi_dashboard_api_responsetime=response1.getTimeIn(TimeUnit.SECONDS);
				sa.assertEquals(response1.jsonPath().getString("message"),"RunHour retrieved successfully.");
				System.out.println("KPI Dashboard Response time :"+response1.getTimeIn(TimeUnit.SECONDS));
				//System.out.println(response.prettyPrint());
				try {
					System.out.println("+++++Energy consumption and Operational Hour for Tower id from dashboard api :"+tower_id+"+++++++");
					System.out.println("Energy Consumption :"+response1.getBody().jsonPath().getString("data.totalEnergy"));
					System.out.println("Operational Hour : "+response1.getBody().jsonPath().getString("data.totalRunHour"));
					System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					kpi_energy_consumed=response1.getBody().jsonPath().getFloat("data.totalEnergy");
					kpi_ops_run_hr=response1.getBody().jsonPath().getFloat("data.totalRunHour");

					kpi_energy_ebEnergy=response1.getBody().jsonPath().getFloat("data.ebEnergy");
					kpi_energy_batteryEnergy=response1.getBody().jsonPath().getFloat("data.batteryEnergy");

					energy_diff=Math.abs(kpi_energy_consumed-cumulative_asset_event_energy_consumed_value);
					opsrunhr_diff=Math.abs(kpi_ops_run_hr-cumulative_asset_event_ops_hr_value);

					if(energy_diff<=1)
					{
						System.out.println("energy_diff :"+energy_diff);
						System.out.println("energy_consumed value is matching");
						status_consumed=diff_status_mathcing;
					}
					else
					{
						System.out.println("energy_diff :"+energy_diff);
						System.out.println("energy_consumed value doesn't match");
						status_consumed=diff_status_not_matching;

					}
					if(opsrunhr_diff<=1)
					{
						System.out.println("opsrunhr_diff :"+opsrunhr_diff);
						System.out.println("ops_hr_value is matching");
						status_opshr=diff_status_mathcing;
					}
					else
					{
						System.out.println("opsrunhr_diff :"+opsrunhr_diff);
						System.out.println("ops_hr_value is doesn't match");
						status_opshr=diff_status_not_matching;
					}	

				}
				catch (Exception e) {

					e.printStackTrace();
					// TODO: handle exception
				}
			}
			write_to_excel(ec_ops_outputfile,i,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,tower_id,deviceOnboarded,mainscumkwh_diff,batCumKwh_diff,energykwh_diff,mppttotal_diff,cumulative_asset_event_energy_consumed_value,cumulative_asset_event_ops_hr_value,kpi_energy_consumed,kpi_ops_run_hr,kpi_energy_ebEnergy,kpi_energy_batteryEnergy, energy_diff,status_consumed,opsrunhr_diff,status_opshr,event_count,asset_events_api_responsetime,kpi_dashboard_api_responsetime);
			sa.assertAll();
		}


	}}