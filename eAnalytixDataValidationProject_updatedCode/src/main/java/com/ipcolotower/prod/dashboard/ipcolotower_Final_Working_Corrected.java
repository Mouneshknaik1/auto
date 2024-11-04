package com.ipcolotower.prod.dashboard;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import io.restassured.response.Response;

public class ipcolotower_Final_Working_Corrected extends BaseClassApi {
	public static String datapath = "";
	public static String imei_id = "";
	public static int rowcount = 1;
	public static List mac_id_list;
	public static List name_id_list;

	public static String admin_assetModelcode = "";
	public static double modelStaticParamsValue_kvaParam = 0;
	public static double modelStaticParamsValue_pf = 0;
	public static double modelStaticParamsValue_oem = 0;
	public static String modelStaticParamsname = "";
	public static String modelStaticParamsname_kvaParam = "kvaParam";
	public static String modelStaticParamsname_powerFactor = "powerFactor";
	public static double modelStaticParamsname_oem_capcity = 70.5;

	public static String jsonpath_ebEnergy_noncritical = "data.meter.channel_1EbCumulativeKwh";
	public static String jsonpath_ebEnergy_critical = "data.meter.channel_2EbCumulativeKwh";
	public static String jsonpath_ebEnergy_others = "data.meter.channel_3EbCumulativeKwh";
	public static String jsonpath_ebEnergy_battery = "data.meter.channel_4EbCumulativeKwh";

	public static String jsonpath_dgEnergy_noncritical = "data.meter.channel_1DgCumulativeKwh";
	public static String jsonpath_dgEnergy_critical = "data.meter.channel_2DgCumulativeKwh";
	public static String jsonpath_dgEnergy_others = "data.meter.channel_3DgCumulativeKwh";
	public static String jsonpath_dgEnergy_battery = "data.meter.channel_4DgCumulativeKwh";

	public static String jsonpath_smpsEnergy_noncritical = "data.meter.channel_1CumulativeBatKwh";
	public static String jsonpath_smpsEnergy_critical = "data.meter.channel_2CumulativeBatKwh";
	public static String jsonpath_smpsEnergy_others = "data.meter.channel_3CumulativeBatKwh";
	public static String jsonpath_smpsEnergy_battery = "data.meter.channel_4CumulativeBatKwh";

	public static String jsonpath_otherEnergy_noncritical = "data.meter.channel_1CumulativeOtherKwh";
	public static String jsonpath_otherEnergy_critical = "data.meter.channel_2CumulativeOtherKwh";
	public static String jsonpath_otherEnergy_others = "data.meter.channel_3CumulativeOtherKwh";
	public static String jsonpath_otherEnergy_battery = "data.meter.channel_4CumulativeOtherKwh";

	public static String jsonpath_ebRunhr_noncritical = "data.meter.channel_1EbCumulativeRunhrs";
	public static String jsonpath_ebRunhr_critical = "data.meter.channel_2EbCumulativeRunhrs";
	public static String jsonpath_ebRunhr_others = "data.meter.channel_3EbCumulativeRunhrs";
	public static String jsonpath_ebRunhr_battery = "data.meter.channel_4EbCumulativeRunhrs";

	public static String jsonpath_dgRunhr_noncritical = "data.meter.channel_1DgCumulativeRunhrs";
	public static String jsonpath_dgRunhr_critical = "data.meter.channel_2DgCumulativeRunhrs";
	public static String jsonpath_dgRunhr_others = "data.meter.channel_3DgCumulativeRunhrs";
	public static String jsonpath_dgRunhr_battery = "data.meter.channel_4DgCumulativeRunhrs";

	public static String jsonpath_smpsRunhr_noncritical = "data.meter.channel_1CumulativeBatRunhrs";
	public static String jsonpath_smpsRunhr_critical = "data.meter.channel_2CumulativeBatRunhrs";
	public static String jsonpath_smpsRunhr_others = "data.meter.channel_3CumulativeBatRunhrs";
	public static String jsonpath_smpsRunhr_battery = "data.meter.channel_4CumulativeBatRunhrs";

	public static String jsonpath_otherRunhr_noncritical = "data.meter.channel_1CumulativeOtherRunhrs";
	public static String jsonpath_otherRunhr_critical = "data.meter.channel_2CumulativeOtherRunhrs";
	public static String jsonpath_otherRunhr_others = "data.meter.channel_3CumulativeOtherRunhrs";
	public static String jsonpath_otherRunhr_battery = "data.meter.channel_4CumulativeOtherRunhrs";
	
	public static String jsonpath_aceb_commActiveEnergy = "data.meter.commActiveEnergy";
	public static String jsonpath_aceb_hour_timer_imported = "data.meter.hour_timer_imported";
	public static String jsonpath_commActiveEnergy = "data.meter.commActiveEnergy";
	public static String jsonpath_acsubmeter_hour_timer_imported = "data.meter.hour_timer_imported";
	
	
	public static String jsonpath_ac_ebEnergy = "";
	public static String jsonpath_ac_ebRunhr = "";

	public static String jsonpath_ac_dgEnergy = "";
	public static String jsonpath_ac_dgrunhr = "";
	

	/*
	public static String jsonpath_ac_ebEnergy = "data.meter.ebActiveEnergy";
	public static String jsonpath_ac_ebRunhr = "data.meter.hour_timer_imported";

	public static String jsonpath_ac_dgEnergy = "data.meter.dgActiveEnergy";
	public static String jsonpath_ac_dgrunhr = "data.meter.hour_timer_imported";
	*/
	/*
	public static double jio_dc_loadKw = 0;
	public static double total_jio_dc_loadKw = 0;
	public static double eb_cph_jio = 0;
	public static double eb_charges_for_jio = 0;
	public static double disel_charges_for_jio = 0;
	 */

	public static int getEafwv=0;
	
	public static double disel_charges_for_jio=0;
	public static double eb_charges_for_jio=0;
	public static double estimated_bill_for_jio=0;
	

	String payload_dc = "";
	String payload_ac_dg = "";
	String payload_ac_eb = "";

	@org.testng.annotations.Test(priority = 1)
	public void getSitedatPath() {
		uri = base_uri + user_id + "/acls";
		//System.out.println(uri);
		Response response = given().header("Content-type", "application/json").and().header("apiKey", apiKey).and().header("Eid", eid).and()
				.header("Authorization", BearerToken).and().when().get(uri).then().extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("ACLs retrieved successfully for user.", response.jsonPath().getString("message"));
		List<String> name = response.jsonPath().getList("data.dat.name");
		List<String> path = response.jsonPath().getList("data.dat.path");
		//System.out.println(lpath);
		System.out.println(name);
		for (int i = 0; i < name.size(); i++) {
			String s = name.get(i).toString();
			if (s.equalsIgnoreCase(sitename)) {
				// System.out.println(s);
				// System.out.println(path.get(i).toString());
				datapath = path.get(i).toString();
				System.out.println("last_event_value is : " +last_event_value);
				System.out.println("first_event_value is : " +first_event_value);

			}

		}
		sa.assertAll();
	}

	public double testenergyCalculation(String id,String chaneltype,String jsonpath)
	{
		double energy_generated = 0;
		String start_active_energy = null;
		String end_active_energy = null;
		String macid=id;	
		SoftAssert sa = new SoftAssert();
		try {


			String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+macid+"\"}";
//			String payload = "{\"from\":1715625000000,\"to\":1715711399999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"6618c839ddb3da111ecfacd8\"}}";
			//payload=chaneltype;
			System.out.println("Payload : " +payload);
			//System.out.println(jsonpath);
			Response response = given()
					.header("Content-type", "application/json")
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.header("Eid", eid)
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
			System.out.println(	"Count of API: "+response.jsonPath().getString("count"));

//			int size = 300;

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
//					for(int i=0;i<wb1.size();i++)
					for(int i=0;i<wb1.size();i++)

					{
						if(wb1.get(i)!=0)	
						{
							start_active_energy=wb1.get(i).toString();
							System.out.println("start_active_energy :"+start_active_energy);
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
							System.out.println("end_active_energy:"+end_active_energy);
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

	@Test(dependsOnMethods = "getSitedatPath")
	public void towertest()
	{

		getDeviceAssetCode("PAKVVYoOwlEs4oW4");
		jsonpath_ac_ebEnergy = "data.meter.ebActiveEnergy";
		jsonpath_ac_ebRunhr = "data.meter.hour_timer_imported";

		jsonpath_ac_dgEnergy = "data.meter.dgActiveEnergy";
		jsonpath_ac_dgrunhr = "data.meter.hour_timer_imported";
		
		double dc_eb_critical_energy=0;
		double dc_eb_noncritical_energy=0;
		double dc_eb_otherl_energy=0;
		double dc_eb_smps_energy=0;

		double dc_dg_energy=0;
		double dc_other_energy=0;
		double dc_battery_energy=0;

		double dc_eb_runhour=0;	
		double dc_dg_runhour=0;
		double dc_other_runhour=0;
		double dc_battery_runhour=0;

		double ebEnergy_noncritical =0; 
		double ebEnergy_critical =0;
		double ebEnergy_others =0; 
		double ebEnergy_battery =0;

		double dgEnergy_noncritical =0; 
		double dgEnergy_critical =0; 
		double dgEnergy_others =0; 
		double dgEnergy_battery =0;

		double smpsEnergy_noncritical =0; 
		double smpsEnergy_critical =0;
		double smpsEnergy_others =0; 
		double smpsEnergy_battery =0;

		double otherEnergy_noncritical =0; 
		double otherEnergy_critical =0; 
		double otherEnergy_others =0;
		double otherEnergy_battery =0; 


		double ebRunhr_noncritical =0;
		double ebRunhr_critical =0;
		double ebRunhr_others =0;
		double ebRunhr_battery =0;

		double dgRunhr_noncritical =0;
		double dgRunhr_critical =0;
		double dgRunhr_others =0;
		double dgRunhr_battery =0;

		double smpsRunhr_noncritical =0; 
		double smpsRunhr_critical =0;
		double smpsRunhr_others =0; 
		double smpsRunhr_battery =0;

		double otherRunhr_noncritical =0; 
		double otherRunhr_critical =0;
		double otherRunhr_others =0;
		double otherRunhr_battery =0;

		double ac_ebEnergy =0;
		double ac_ebRunhr = 0;

		double ac_dgEnergy = 0;
		double ac_dgrunhr = 0;

		double channel1_nonCriticalEnergy=0;
		double channel2_criticalEnergy=0;
		double channel3_otherEnergy=0;
		double channel4_smpsEnergy=0;

		double channel1_nonCriticalRunhr=0;
		double channel2_criticalRunhr=0;
		double channel3_otherRunhr=0;
		double channel4_smpsRunhr=0;

		double ac_energy=0;
		double ac_runHr=0;

		double jio_dc_load=0;
		double total_jio_dc_load=0;

		double jio_dc_load_runHr=0;
		double total_jio_dc_load_runHr=0;



		double dc_loadPercentage=0;

		double ac_dg_powerKw=0;
		double ac_eb_powerKw=0;



		double dg_capacity=0;

		double loading_of_dg_total_load=0;

		double dg_cph_for_jio=0;

		String macid="";
		String dg_macid="";

		//double disel_charges_for_jio=0;
		//double eb_charges_for_jio=0;
		double jio_dc_loadKw=0;
		double total_jio_dc_loadKw=0;
		double eb_cph_jio=0;


		String sapid="";
		String vehicle_name="";
		String payload="{\"flags\":{\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetType\":true},\"startsWith\":{\"datRegex\":\""+datapath+"\"}}";
		Response response1 = given()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.header("Eid", eid)
				.and()
				.body(payload)
				.when()
				.post(uri_devicestatus)
				.then()
				.extract().response();
		sa.assertEquals(200, response1.statusCode());
		sa.assertEquals("Devicestatuses retrieved successfully.", response1.jsonPath().getString("message"));
		List<String> macid_list= response1.jsonPath().getList("data.device.mac");
		List<String> name_list= response1.jsonPath().getList("data.device.name");
		List<String> meterType_list= response1.jsonPath().getList("data.device.meterType");
		List<String> loadType_list= response1.jsonPath().getList("data.device.loadtype");
		List<String> sapid_list= response1.jsonPath().getList("data.device.sapid");
		System.out.println(loadType_list);
		//List<String> assetTypeModelCode_list= response1.jsonPath().getList("data.assetTypeModel.code");


		System.out.println("macid_list size : "+macid_list);
		System.out.println(meterType_list);
		/*
		System.out.println(macid_list);
		System.out.println(meterType_list);
		System.out.println(loadType_list);
		System.out.println(sapid_list);
		//System.out.println(assetTypeModelCode_list);
		 */
		int count=1;
		rowcount=1;
		int i =0;
		for(i =0;i<macid_list.size();i++)
		{
			/*
			channel1_nonCriticalEnergy=0;
			channel1_nonCriticalRunhr=0;
			channel2_criticalEnergy=0;
			channel2_criticalRunhr=0;
			channel3_otherEnergy=0;
			channel3_otherRunhr=0;
			channel4_smpsEnergy=0;
			channel4_smpsRunhr=0;
			jio_dc_loadKw=0;
			total_jio_dc_loadKw=0;
			eb_charges_for_jio=0;
			disel_charges_for_jio=0;
			 */

			try {
				String metertype=meterType_list.get(i);
				String loadtype=loadType_list.get(i);
				String name=name_list.get(i);
				sapid=sapid_list.get(i);
				name=name_list.get(i).toString();
				if(metertype==null)
				{
					continue;
				}
				
				else if(metertype.contains("iot_device"))
				{
					continue;
				}
				else
					if(metertype.equalsIgnoreCase("ackwhmeter"))
					{
						//if((loadtype.equalsIgnoreCase("EB") & (name.contains("EB"))))
						if((loadtype.equalsIgnoreCase("EB")))
						{
							ac_ebEnergy=0;
							ac_ebRunhr=0;

							count++;
							System.out.print(count+" : ");
							System.out.print(metertype+" : ");
							macid=macid_list.get(i).toString();
							System.out.println(sapid);
							System.out.println(macid);
							getEafwv(macid);
							//if(sapid.equalsIgnoreCase("I-MH-MWAL-ENB-I010") | sapid.equalsIgnoreCase("I-MH-RGNG-ENB-I012") | sapid.equalsIgnoreCase("I-MH-BHOR-ENB-I011"))
							if(getEafwv>=20400)
							{
								jsonpath_ac_ebEnergy="data.meter.commActiveEnergy";
								System.out.println(jsonpath_ac_ebEnergy); 
								System.out.println("++++++++++++++++++AC EB Device Data+++++++++++++++");
								ac_ebEnergy = testenergyCalculation(macid,payload_ac_eb,jsonpath_ac_ebEnergy);
								System.out.println("ac_ebEnergy : "+ac_ebEnergy);
								ac_ebRunhr=testenergyCalculation(macid,payload_ac_eb,jsonpath_ac_ebRunhr);
								System.out.println("ac_ebRunhr : "+ac_ebRunhr);

								ac_eb_powerKw=jioLoadCalcKW(ac_ebEnergy,ac_ebRunhr);
								System.out.println("ac_eb_powerKw : "+ac_eb_powerKw);
								
								
							}
							else {

							System.out.println("++++++++++++++++++AC EB Device Data+++++++++++++++");
							jsonpath_ac_ebEnergy="data.meter.ebActiveEnergy";
							ac_ebEnergy = testenergyCalculation(macid,payload_ac_eb,jsonpath_ac_ebEnergy);
							System.out.println("ac_ebEnergy : "+ac_ebEnergy);
							ac_ebRunhr=testenergyCalculation(macid,payload_ac_eb,jsonpath_ac_ebRunhr);
							System.out.println("ac_ebRunhr : "+ac_ebRunhr);

							ac_eb_powerKw=jioLoadCalcKW(ac_ebEnergy,ac_ebRunhr);
							System.out.println("ac_eb_powerKw : "+ac_eb_powerKw);
							}
						}
						else if(loadtype.equalsIgnoreCase(("DG")))
						{
							ac_dgEnergy=0;
							ac_dgrunhr=0;

							//getsapiddatSitePath(sapid);
							//getAssetTypeModeldata();
							//getassetmodeldata();
							//getassetmodelStaticParams();


							count++;
							System.out.print(count+" : ");
							System.out.print(metertype+" : ");
							dg_macid=macid_list.get(i).toString();
							//String dgmodel=assetTypeModelCode_list.get(i).toString();
							//System.out.println("dgmodelcode : "+dgmodel);
							//System.out.println(sapid);
							//System.out.println(macid);
							getDeviceAssetCode(dg_macid);
							getDgkva(assetCode);
							
							getEafwv(dg_macid);
							//if(sapid.equalsIgnoreCase("I-MH-MWAL-ENB-I010") | sapid.equalsIgnoreCase("I-MH-RGNG-ENB-I012") | sapid.equalsIgnoreCase("I-MH-BHOR-ENB-I011"))
							if(getEafwv>=20400)
							{
								
								jsonpath_ac_dgEnergy="data.meter.commActiveEnergy";
								System.out.println(jsonpath_ac_dgEnergy);
								
								System.out.println("++++++++++++++++++AC DG Device Data+++++++++++++++");
								ac_dgEnergy=testenergyCalculation(dg_macid,payload_ac_dg,jsonpath_ac_dgEnergy);
								System.out.println("ac_dgEnergy : "+ac_dgEnergy);

								ac_dgrunhr=testenergyCalculation(dg_macid,payload_ac_dg,jsonpath_ac_dgrunhr);
								System.out.println("ac_dgrunhr : "+ac_dgrunhr);

								ac_dg_powerKw = jioLoadCalcKW(ac_dgEnergy,ac_dgrunhr);
								System.out.println("ac_dg_powerKw : "+ac_dg_powerKw);
							}
							else {
							
							
							System.out.println("++++++++++++++++++AC DG Device Data+++++++++++++++");
							jsonpath_ac_dgEnergy = "data.meter.dgActiveEnergy";
							ac_dgEnergy=testenergyCalculation(dg_macid,payload_ac_dg,jsonpath_ac_dgEnergy);
							System.out.println("ac_dgEnergy : "+ac_dgEnergy);

							ac_dgrunhr=testenergyCalculation(dg_macid,payload_ac_dg,jsonpath_ac_dgrunhr);
							System.out.println("ac_dgrunhr : "+ac_dgrunhr);

							ac_dg_powerKw = jioLoadCalcKW(ac_dgEnergy,ac_dgrunhr);
							System.out.println("ac_dg_powerKw : "+ac_dg_powerKw);
							}
						}
						else if(loadtype.isEmpty())
						{
							continue;
						}

						ac_energy=sumofEnergyRunHr(ac_ebEnergy,ac_dgEnergy,0,0);
						System.out.println("ac_energy  : "+ac_energy);

						ac_runHr=sumofEnergyRunHr(ac_ebRunhr,ac_dgrunhr,0,0);
						System.out.println("ac_runHr  : "+ac_runHr);



					}
					else if(metertype.contains("kwh_meter"))
					{

						count++;
						System.out.print(count+" : ");
						System.out.print(metertype+" : ");
						macid=macid_list.get(i).toString();
						System.out.println(sapid);
						System.out.println(macid);

						System.out.println("++++++++++++++++++DC Device Data Critical+++++++++++++++");

						dgEnergy_critical=testenergyCalculation(macid,payload_dc,jsonpath_dgEnergy_critical);
						System.out.println("dgEnergy_critical : "+dgEnergy_critical);

						ebEnergy_critical=testenergyCalculation(macid,payload_dc,jsonpath_ebEnergy_critical);
						System.out.println("ebEnergy_critical : "+ebEnergy_critical);

						otherEnergy_critical=testenergyCalculation(macid,payload_dc,jsonpath_otherEnergy_critical);
						System.out.println("otherEnergy_critical : "+otherEnergy_critical);

						smpsEnergy_critical=testenergyCalculation(macid,payload_dc,jsonpath_smpsEnergy_critical);
						System.out.println("smpsEnergy_critical : "+smpsEnergy_critical);


						dgRunhr_critical=testenergyCalculation(macid,payload_dc,jsonpath_dgRunhr_critical);
						System.out.println("dgRunhr_critical : "+dgRunhr_critical);

						ebRunhr_critical=testenergyCalculation(macid,payload_dc,jsonpath_ebRunhr_critical);
						System.out.println("ebRunhr_critical : "+ebRunhr_critical);

						otherRunhr_critical=testenergyCalculation(macid,payload_dc,jsonpath_otherRunhr_critical);
						System.out.println("otherRunhr_critical : "+otherRunhr_critical);

						smpsRunhr_critical=testenergyCalculation(macid,payload_dc,jsonpath_smpsRunhr_critical);
						System.out.println("smpsRunhr_critical : "+smpsRunhr_critical);

						channel2_criticalEnergy=sumofEnergyRunHr(smpsEnergy_critical,otherEnergy_critical,ebEnergy_critical,dgEnergy_critical);
						System.out.println("channel2_criticalEnergy  : "+channel2_criticalEnergy);

						channel2_criticalRunhr=sumofEnergyRunHr(dgRunhr_critical,ebRunhr_critical,otherRunhr_critical,smpsRunhr_critical);
						System.out.println("channel2_criticalRunhr  : "+channel2_criticalRunhr);


						System.out.println("++++++++++++++++++DC Device Data +++++++++++++++");

						System.out.println("++++++++++++++++++DC Device Data non-Crtical+++++++++++++++");

						dgEnergy_noncritical=testenergyCalculation(macid,payload_dc,jsonpath_dgEnergy_noncritical);
						System.out.println("dgEnergy_noncritical : "+dgEnergy_noncritical);

						ebEnergy_noncritical=testenergyCalculation(macid,payload_dc,jsonpath_ebEnergy_noncritical);
						System.out.println("ebEnergy_noncritical : "+ebEnergy_noncritical);

						otherEnergy_noncritical=testenergyCalculation(macid,payload_dc,jsonpath_otherEnergy_noncritical);
						System.out.println("otherEnergy_noncritical : "+otherEnergy_noncritical);

						smpsEnergy_noncritical=testenergyCalculation(macid,payload_dc,jsonpath_smpsEnergy_noncritical);
						System.out.println("smpsEnergy_noncritical : "+smpsEnergy_noncritical);


						dgRunhr_noncritical=testenergyCalculation(macid,payload_dc,jsonpath_dgRunhr_noncritical);
						System.out.println("dgRunhr_noncritical : "+dgRunhr_noncritical);

						ebRunhr_noncritical=testenergyCalculation(macid,payload_dc,jsonpath_ebRunhr_noncritical);
						System.out.println("ebRunhr_noncritical : "+ebRunhr_noncritical);

						otherRunhr_noncritical=testenergyCalculation(macid,payload_dc,jsonpath_otherRunhr_noncritical);
						System.out.println("otherRunhr_noncritical : "+otherRunhr_noncritical);

						smpsRunhr_noncritical=testenergyCalculation(macid,payload_dc,jsonpath_smpsRunhr_noncritical);
						System.out.println("smpsRunhr_noncritical : "+smpsRunhr_noncritical);

						channel1_nonCriticalEnergy=sumofEnergyRunHr(dgEnergy_noncritical,ebEnergy_noncritical,otherEnergy_noncritical,smpsEnergy_noncritical);
						System.out.println("channel1_nonCriticalEnergy  : "+channel1_nonCriticalEnergy);

						channel1_nonCriticalRunhr=sumofEnergyRunHr(dgRunhr_noncritical,ebRunhr_noncritical,otherRunhr_noncritical,smpsRunhr_noncritical);
						System.out.println("channel1_nonCriticalRunhr  : "+channel1_nonCriticalRunhr);



						System.out.println("++++++++++++++++++DC Device Data +++++++++++++++");


						System.out.println("++++++++++++++++++DC Device Data others+++++++++++++++");

						dgEnergy_others=testenergyCalculation(macid,payload_dc,jsonpath_dgEnergy_others);
						System.out.println("dgEnergy_others : "+dgEnergy_others);

						ebEnergy_others=testenergyCalculation(macid,payload_dc, jsonpath_ebEnergy_others);
						System.out.println("ebEnergy_others : "+ebEnergy_others);

						otherEnergy_others=testenergyCalculation(macid,payload_dc,jsonpath_otherEnergy_others);
						System.out.println("otherEnergy_others : "+ otherEnergy_others);

						smpsEnergy_others =testenergyCalculation(macid,payload_dc,jsonpath_smpsEnergy_others);
						System.out.println("otherEnergy_battery  : "+otherEnergy_battery );


						dgRunhr_others=testenergyCalculation(macid,payload_dc,jsonpath_dgRunhr_others);
						System.out.println("dgRunhr_others : "+dgRunhr_others);

						ebRunhr_others=testenergyCalculation(macid,payload_dc, jsonpath_ebRunhr_others);
						System.out.println("ebRunhr_others : "+ebRunhr_others);

						otherRunhr_others=testenergyCalculation(macid,payload_dc,jsonpath_otherRunhr_others);
						System.out.println("otherRunhr_others : "+ otherRunhr_others);

						smpsRunhr_others =testenergyCalculation(macid,payload_dc,jsonpath_smpsRunhr_others);
						System.out.println("otherRunhr_battery  : "+otherRunhr_battery );

						channel3_otherEnergy=sumofEnergyRunHr(dgEnergy_others,ebEnergy_others,otherEnergy_others,otherEnergy_battery);
						System.out.println("channel3_otherEnergy  : "+channel3_otherEnergy);

						channel3_otherRunhr=sumofEnergyRunHr(dgRunhr_others,ebRunhr_others,otherRunhr_others,smpsRunhr_others);
						System.out.println("channel3_otherRunhr  : "+channel3_otherRunhr);

						System.out.println("++++++++++++++++++DC Device Data +++++++++++++++");
						System.out.println("++++++++++++++++++DC Device Data smps+++++++++++++++");
						dgEnergy_battery=testenergyCalculation(macid,payload_dc,jsonpath_dgEnergy_battery);
						System.out.println("dgEnergy_battery : "+dgEnergy_battery);

						ebEnergy_battery=testenergyCalculation(macid,payload_dc, jsonpath_ebEnergy_battery);
						System.out.println("ebEnergy_battery : "+ebEnergy_battery);

						otherEnergy_battery=testenergyCalculation(macid,payload_dc,jsonpath_otherEnergy_battery);
						System.out.println("otherEnergy_battery : "+ otherEnergy_battery);

						smpsEnergy_battery =testenergyCalculation(macid,payload_dc,jsonpath_smpsEnergy_battery);
						System.out.println("smpsEnergy_battery  : "+smpsEnergy_battery );

						dgRunhr_battery=testenergyCalculation(macid,payload_dc,jsonpath_dgRunhr_battery);
						System.out.println("dgRunhr_battery : "+dgRunhr_battery);

						ebRunhr_battery=testenergyCalculation(macid,payload_dc, jsonpath_ebRunhr_battery);
						System.out.println("ebRunhr_battery : "+ebRunhr_battery);

						otherRunhr_battery=testenergyCalculation(macid,payload_dc,jsonpath_otherRunhr_battery);
						System.out.println("otherRunhr_battery : "+ otherRunhr_battery);

						smpsRunhr_battery =testenergyCalculation(macid,payload_dc,jsonpath_smpsRunhr_battery);
						System.out.println("smpsRunhr_battery  : "+smpsRunhr_battery );

						channel4_smpsEnergy=sumofEnergyRunHr(dgEnergy_battery,ebEnergy_battery,otherEnergy_battery,smpsEnergy_battery);
						System.out.println("channel4_smpsEnergy  : "+channel4_smpsEnergy);

						channel4_smpsRunhr=sumofEnergyRunHr(dgRunhr_battery,ebRunhr_battery,otherRunhr_battery,smpsRunhr_battery);
						System.out.println("channel4_smpsRunhr  : "+channel4_smpsRunhr);
						System.out.println("++++++++++++++++++DC Device Data +++++++++++++++");

						jio_dc_load=sumofEnergyRunHr(channel1_nonCriticalEnergy,channel2_criticalEnergy,channel3_otherEnergy,0);
						System.out.println("jio_dc_load : "+jio_dc_load);
						System.out.println("total_jio_dc_load : "+channel4_smpsEnergy);

						jio_dc_load_runHr=sumofEnergyRunHr(channel1_nonCriticalRunhr,channel2_criticalRunhr,channel3_otherRunhr,0);
						System.out.println("jio_dc_load_runHr : "+jio_dc_load_runHr);
						System.out.println("total_jio_dc_load_runHr : "+channel4_smpsRunhr);

						jio_dc_loadKw=jioLoadCalcKW(jio_dc_load,channel4_smpsRunhr);
						System.out.println("jio_dc_loadKw : "+jio_dc_loadKw);

						total_jio_dc_loadKw=jioLoadCalcKW(channel4_smpsEnergy,channel4_smpsRunhr);
						System.out.println("total_jio_dc_loadKw : "+total_jio_dc_loadKw);

						dc_loadPercentage=jioLoadCalcKW(jio_dc_loadKw,total_jio_dc_loadKw);
						System.out.println("dc_loadPercentage : "+dc_loadPercentage);


					}

				jio_dc_loadKw=jioLoadCalcKW(jio_dc_load,channel4_smpsRunhr);
				System.out.println("jio_dc_loadKw : "+jio_dc_loadKw);

				total_jio_dc_loadKw=jioLoadCalcKW(channel4_smpsEnergy,channel4_smpsRunhr);
				System.out.println("total_jio_dc_loadKw : "+total_jio_dc_loadKw);

				eb_cph_jio=ebCPHforJio(ac_eb_powerKw,dc_loadPercentage);
				System.out.println("eb_cph_jio : "+eb_cph_jio);

				eb_charges_for_jio=ebchargesforJio(eb_cph_jio,ac_ebRunhr);
				System.out.println("eb_cph_jio : "+eb_cph_jio);
				System.out.println("ac_ebRunhr : "+ac_ebRunhr);

				System.out.println("eb_charges_for_jio : "+eb_charges_for_jio);

				//getDeviceAssetCode(dg_macid);
				//getDgkva(assetCode);

				dg_capacity=dgcapacity(dg_kva,dg_powerfactor);
				System.out.println("dg_capacity : "+dg_capacity);

				loading_of_dg_total_load=loading_of_dg_total_load(ac_dg_powerKw,dg_capacity);
				System.out.println("loading_of_dg_total_load : "+loading_of_dg_total_load);

				//getDeviceAssetCode(dg_macid);
				//getDgkva(assetCode);
				getOEMcphvalue(assetCode,loading_of_dg_total_load);
				dg_cph_for_jio=dgcphforJio(dc_loadPercentage,chp_value_from_OEM);
				System.out.println("dg_cph_for_jio : "+dg_cph_for_jio);

				disel_charges_for_jio=diesel_charges_for_jio(ac_dgrunhr,dg_cph_for_jio,dg_tariff);
				System.out.println("disel_charges_for_jio : "+disel_charges_for_jio);
				System.out.println("write to excel starts count : "+count);
				
				estimated_bill_for_jio = eb_charges_for_jio+disel_charges_for_jio;
				System.out.println("estimated_bill_for_jio : " +estimated_bill_for_jio);

				write_to_excel(ec_ops_outputfile,count,0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13,14,15,16,17,18, 19, sapid,channel1_nonCriticalEnergy,channel1_nonCriticalRunhr, channel2_criticalEnergy,channel2_criticalRunhr,channel3_otherEnergy,channel3_otherRunhr,channel4_smpsEnergy, channel4_smpsRunhr,eb_charges_for_jio, disel_charges_for_jio,ac_dgEnergy,ac_dgrunhr,ac_ebEnergy,ac_ebRunhr,ac_energy,ac_runHr,jio_dc_loadKw,total_jio_dc_loadKw, estimated_bill_for_jio);
				System.out.println("write to excel end count : "+count);
				/*			

				  if(macid.contains("_")) 
				  { 
					  write_to_excel(ec_ops_outputfile,count,0, 1, 2, 3,  4,sapid,jio_dc_loadKw,total_jio_dc_loadKw,eb_charges_for_jio,
				  disel_charges_for_jio); 
				  }
				 else
				  {

				  }
				 */
				//write_to_excel(ec_ops_outputfile,count,0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, sapid,channel1_nonCriticalEnergy,channel1_nonCriticalRunhr, channel2_criticalEnergy,channel2_criticalRunhr,channel3_otherEnergy,channel3_otherRunhr,channel4_smpsEnergy, channel4_smpsRunhr,jio_dc_loadKw,total_jio_dc_loadKw,eb_charges_for_jio,disel_charges_for_jio,ac_dgEnergy,ac_dgrunhr);
			}

			catch(Exception e)
			{
				ac_dgrunhr =0;
				ac_dg_powerKw =0;
				ac_energy  =0;
				ac_runHr  =0;
				eb_cph_jio =0;
				eb_charges_for_jio=0;
				dg_capacity =0;
				loading_of_dg_total_load =0;
				dg_cph_for_jio =0;
				disel_charges_for_jio =0;

				eb_cph_jio=0;
				eb_charges_for_jio=0;

				e.printStackTrace();


				//getDeviceAssetCode(dg_macid);
				//getDgkva(assetCode);
				//getOEMcphvalue(assetCode,loading_of_dg_total_load);



				//getDeviceAssetCode(dg_macid);
				//getDgkva(assetCode);
				//getOEMcphvalue(assetCode,loading_of_dg_total_load);


				dg_cph_for_jio=dgcphforJio(dc_loadPercentage,chp_value_from_OEM);
				System.out.println("dg_cph_for_jio : "+dg_cph_for_jio);

				disel_charges_for_jio=diesel_charges_for_jio(ac_dgrunhr,dg_cph_for_jio,dg_tariff);
				System.out.println("disel_charges_for_jio : "+disel_charges_for_jio);
				System.out.println("write to excel starts count : "+count);
				
				estimated_bill_for_jio = eb_charges_for_jio+disel_charges_for_jio;
				System.out.println("estimated_bill_for_jio : " +estimated_bill_for_jio);
				
				write_to_excel(ec_ops_outputfile,count,0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13,14,15,16,17,18,19, sapid,channel1_nonCriticalEnergy,channel1_nonCriticalRunhr, channel2_criticalEnergy,channel2_criticalRunhr,channel3_otherEnergy,channel3_otherRunhr,channel4_smpsEnergy, channel4_smpsRunhr,eb_charges_for_jio, disel_charges_for_jio,ac_dgEnergy,ac_dgrunhr,ac_ebEnergy,ac_ebRunhr,ac_energy,ac_runHr,jio_dc_loadKw,total_jio_dc_loadKw,estimated_bill_for_jio);
				System.out.println("write to excel end count : "+count);

			}


		}

		jio_dc_loadKw=jioLoadCalcKW(jio_dc_load,channel4_smpsRunhr);
		System.out.println("jio_dc_loadKw : "+jio_dc_loadKw);

		total_jio_dc_loadKw=jioLoadCalcKW(channel4_smpsEnergy,channel4_smpsRunhr);
		System.out.println("total_jio_dc_loadKw : "+total_jio_dc_loadKw);

		eb_cph_jio=ebCPHforJio(ac_eb_powerKw,dc_loadPercentage);
		System.out.println("eb_cph_jio : "+eb_cph_jio);

		eb_charges_for_jio=ebchargesforJio(eb_cph_jio,ac_ebRunhr);
		System.out.println("eb_charges_for_jio : "+eb_charges_for_jio);

		dg_capacity=dgcapacity(dg_kva,dg_powerfactor);
		System.out.println("dg_capacity : "+dg_capacity);

		System.out.println("ac_dg_powerKw : "+ac_dg_powerKw);
		loading_of_dg_total_load=loading_of_dg_total_load(ac_dg_powerKw,dg_capacity);
		System.out.println("loading_of_dg_total_load : "+loading_of_dg_total_load);

		getOEMcphvalue(assetCode,loading_of_dg_total_load);

		dg_cph_for_jio=dgcphforJio(dc_loadPercentage,chp_value_from_OEM);
		System.out.println("dg_cph_for_jio : "+dg_cph_for_jio);

		disel_charges_for_jio=diesel_charges_for_jio(ac_dgrunhr,dg_cph_for_jio,dg_tariff);
		System.out.println("disel_charges_for_jio : "+disel_charges_for_jio);
				
		estimated_bill_for_jio = eb_charges_for_jio+disel_charges_for_jio;
		System.out.println("estimated_bill_for_jio : " +estimated_bill_for_jio);
		
		System.out.println("write to excel final write starts count : "+count);
		write_to_excel(ec_ops_outputfile,count,0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13,14,15,16,17,18,19, sapid,channel1_nonCriticalEnergy,channel1_nonCriticalRunhr, channel2_criticalEnergy,channel2_criticalRunhr,channel3_otherEnergy,channel3_otherRunhr,channel4_smpsEnergy, channel4_smpsRunhr,eb_charges_for_jio, disel_charges_for_jio,ac_dgEnergy,ac_dgrunhr,ac_ebEnergy,ac_ebRunhr,ac_energy,ac_runHr,jio_dc_loadKw,total_jio_dc_loadKw, estimated_bill_for_jio);
		System.out.println("write to excel final write end count : "+count);
		rowcount++;


	} 

	public void getAssetTypeModeldata()
	{

		String payload="{\"flags\":{\"isPopulateAssetType\":true,\"isExactMatchDatCode\":true,\"isSkipAutoAssignUser\":true,\"isPopulateAssetTypeModel\":true,\"isSortRequired\":true},\"startsWith\":{\"datRegex\":\""+datapath+"\"},\"device\":{\"model\":\"\",\"name\":\"\",\"mac\":\"\",\"assetCode\":\"ackwhmeter\",\"created\":{\"from\":\"\",\"to\":\"\"}},\"assetType\":{\"code\":\"ackwhmeter\"}}";
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.header("Eid", eid)
				.and()
				.body(payload)
				.when()
				.post(uri_devicestatus)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("Devicestatuses retrieved successfully.", response.jsonPath().getString("message"));
		List<String> assetTypeModelList= response.jsonPath().getList("data.assetTypeModel.name");
		//System.out.println("assetTypeModelList : "+assetTypeModelList);
		for(int i=0;i<assetTypeModelList.size();i++)
		{
			String name=assetTypeModelList.get(i).toString();
			System.out.println("name : "+name);
			try {
				if(name.contains("DG"))
				{
					assetModelName=assetTypeModelList.get(i).toString();
					System.out.println("assetModelName : "+assetModelName);
					break;
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				System.out.println("Unable to find Asset");
			}

		}

		sa.assertAll();
	}
	public void getsapiddatSitePath(String sapid)
	{
		uri = base_uri+user_id+"/acls";
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", BearerToken)
				.and()
				.header("Eid", eid)
				.and()
				.when()
				.get(uri)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals("ACLs retrieved successfully for user.", response.jsonPath().getString("message"));
		List<String> name= response.jsonPath().getList("data.dat.name");
		List<String> path= response.jsonPath().getList("data.dat.path");
		//System.out.println(lpath);
		//System.out.println(name);
		for(int i=0;i<name.size();i++)
		{
			String s= name.get(i).toString();
			if(s.equalsIgnoreCase(sapid))
			{
				//System.out.println(s);
				//System.out.println(path.get(i).toString());
				datapath=path.get(i).toString();

			}

		}
		sa.assertAll();
	}

	public void getassetmodeldata()
	{

		String requestpayload = "{\"name\":\""+assetModelName+"\",\"code\":null,\"makeModel\":null,\"limit\":1000,\"skip\":0}";
		//System.out.println(requestpayload);
		sa = new SoftAssert();
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", adminBearerToken)
				.and()
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(admin_assetModel_uri)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals(response.jsonPath().getString("message"),"Asset type models retrived successfully.");
		List codeList = response.jsonPath().getList("data.code");
		admin_assetModelcode = codeList.get(0).toString();
		sa.assertAll();  


	}

	//@Test(dependsOnMethods = "getassetmodeldata")
	public void getassetmodelStaticParams()
	{
		//admin_login();
		//getSitedatPath();
		//getassetmodeldata();

		String requestpayload = "{\"code\":\""+admin_assetModelcode+"\"}";
		//System.out.println(requestpayload);
		sa = new SoftAssert();
		Response response = given()
				.header("Content-type", "application/json")
				.and()
				.header("apiKey", apiKey)
				.and()
				.header("Authorization", adminBearerToken)
				.and()
				.header("Eid", eid)
				.and()
				.body(requestpayload)
				.when()
				.post(admin_assetModelBycode_uri)
				.then()
				.extract().response();
		sa.assertEquals(200, response.statusCode());
		sa.assertEquals(response.jsonPath().getString("message"),"Asset type model retrieved successfully.");
		List modelStaticParamsNameList = response.jsonPath().getList("data.modelStaticParams.name");
		List modelStaticParamsValueList = response.jsonPath().getList("data.modelStaticParams.value");
		for(int i =0;i<modelStaticParamsNameList.size();i++)
		{
			String modelStaticParamsName=modelStaticParamsNameList.get(i).toString();
			double modelStaticParamsvalue= Double.valueOf(modelStaticParamsValueList.get(i).toString());
			try {
				if(modelStaticParamsName.equalsIgnoreCase(modelStaticParamsname_kvaParam))
				{
					String value=modelStaticParamsValueList.get(i).toString();
					modelStaticParamsValue_kvaParam=Double.valueOf(value);
					System.out.println("modelStaticParamsValue_kvaParam :  "+modelStaticParamsValue_kvaParam);
				}
				else if(modelStaticParamsName.equalsIgnoreCase(modelStaticParamsname_powerFactor))
				{
					String value=modelStaticParamsValueList.get(i).toString();
					modelStaticParamsValue_pf=Double.valueOf(value);
					System.out.println("modelStaticParamsValue_pf :  "+modelStaticParamsValue_pf);
				}
				else if(modelStaticParamsName.equalsIgnoreCase(modelStaticParamsname_powerFactor))
				{
					String value=modelStaticParamsValueList.get(i).toString();
					modelStaticParamsValue_pf=Double.valueOf(value);
					System.out.println("modelStaticParamsValue_pf :  "+modelStaticParamsValue_pf);
				}
				else if(Double.valueOf(modelStaticParamsName)>=modelStaticParamsname_oem_capcity)
				{
					String value=modelStaticParamsValueList.get(i).toString();
					modelStaticParamsValue_oem=Double.valueOf(value);

					System.out.println("modelStaticParamsValue_oem :  "+modelStaticParamsValue_oem);
					break;
				}

			}
			catch (Exception e) {
				System.out.println("Data not found");
				// TODO: handle exception
			}

		}
	}


	public void  getEafwv(String id)
	{

		SoftAssert sa = new SoftAssert();
		getEafwv=0;
		String macid=id;
		String jsonpath="data.meter.eafwv";
		String payload ="{\"from\":"+from_date_in_milli+",\"to\":"+to_date_in_milli+",\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"meterId\":\""+macid+"\"}";
//		String payload ="{\"from\":1715625000000,\"to\":1715711399999,\"sort\":{\"location.timestamp\":-1},\"flags\":{\"isSortRequired\":true,\"isTotalCount\":true},\"type\":\"\",\"device\":{\"_id\":\"6618c839ddb3da111ecfacd8\"}}";

		try {
			Response response = given()
					.header("Content-type", "application/json")
					.and()
					.header("apiKey", apiKey)
					.and()
					.header("Authorization", BearerToken)
					.and()
					.header("Eid", eid)
					.and()
					.body(payload)
					.when()
					.post(generic_events_uri)
					.then()
					.extract().response();
			sa.assertEquals(response.statusCode(),200);
			//String test = response.getBody().jsonPath().getString("data[0].meter.eafwv");

			int count = response.jsonPath().getInt("count");

			if(count==0)
			{
				getEafwv=0;
			}
			else
			{
				List energylist = response.getBody().jsonPath().getList(jsonpath);
				//System.out.println(energylist);
				while(energylist.remove(null))
				{
				}
				if(energylist.size()==0)
				{
					getEafwv=0;

					//System.out.println("energy_generated : "+energy_generated);
				}
				else
				{
					String value= energylist.get(0).toString();
					String s=value.replace(".", "");
					getEafwv= Integer.valueOf(s);
					System.out.println(getEafwv);

				}
			}

		}
		catch(Exception e)
		{
			System.out.println("No events");
		}
	}
	}