package org.cilab.s4rm.upload;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.cilab.s4rm.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class UploadMosquitos {

	/**
	 * Class Name: WatershedUpload.java Description:
	 * 
	 * @author Meilan Jiang
	 * @since 2016.01.30
	 * @version 1.0
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */

	private static final Logger logger = LoggerFactory.getLogger(UploadMosquitos.class);

	public static void main(String[] args) {
		
//		List<String> dateList = new ArrayList<String>();
		List<String> streamIDList  = new ArrayList<String>();
		
		streamIDList.add("5ff88f49-e191-4226-a0c4-2fa7d5dd0295");		//1		ok
		streamIDList.add("3c9aa7b1-08c5-485b-adf7-d770b8ebdca7");		//2		ok
		streamIDList.add("759481bd-af20-4aa1-94d6-1f75856222b9");		//3		ok
		streamIDList.add("ea696124-3488-4f93-8bb4-22c0f7a237dc");		//4		ok
		streamIDList.add("e913e331-0fbf-4ce0-bc5c-285800d39118");		//5		ok
		
		streamIDList.add("d8065050-9fa9-44f9-9c73-07a0470031fc");		//6		ok
		streamIDList.add("eac60e13-b5d1-4c4a-8ded-49abc202ed5b");		//7		ok
		streamIDList.add("aed7fc2a-815c-4381-b65f-07aa3ac57a5c");		//8 	ok
		streamIDList.add("2387bfbe-5a76-4da5-93a5-66743475203d");		//9			2014년 순서.
		streamIDList.add("fc504eec-0f61-40c2-8160-d957d79828fa");		//10	ok
		streamIDList.add("f08e4636-aaea-4b64-9643-6fe4d5b873b9");		//11	ok
		
		streamIDList.add("0850d603-c46c-4d31-8998-219ef29846f5");		//11	ok
		streamIDList.add("550294a3-1fcd-434e-9d82-ffdc82966d05");		//12	ok
		streamIDList.add("f77695c6-4a69-47a4-87a6-f494bb4defc0");		//13	ok
		streamIDList.add("7d8d41bc-d369-40cd-ba24-9915f745d61b");		//14	ok
		streamIDList.add("d9c17ed1-ed98-4df6-b0e8-d2d284b76342");		//15	ok
		
		streamIDList.add("d2c49d64-3329-48b7-8c3c-470e29f38cbe");		//16	ok
		streamIDList.add("7c54bfeb-ad61-4149-b9e3-942645d8fadf");		//			2014년 추가
		streamIDList.add("de780958-ec7a-43a3-84b2-6b3c777b026f");		//17	ok
		streamIDList.add("387d3dad-de74-4583-84fd-f2186173f2a8");		//18	ok
		streamIDList.add("946ec114-cb5f-4b88-8eaa-6536a589315c");		//			2014년 추가
		
		streamIDList.add("e49e874b-bec6-461d-863f-78f40d44244e");		//19
		streamIDList.add("b3b3b61e-baae-4c81-bea7-966048845a7f");		//20
		
		try {
			upload2014Data(streamIDList);
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void upload2014Data(List<String> streamIDList)  throws IOException, ParseException, BiffException{
		
		Workbook book = Workbook.getWorkbook(new File("M:\\WISE\\영등포구_모기_2011-2014_3.xls"));
		Sheet sheet = book.getSheet(6);
		
		// ======================================
		//  Read Value
		// ======================================
		List<Value> values = new ArrayList<Value>();
		for (int i=2; i<sheet.getRows(); i++){
			Cell[] row = sheet.getRow(i);
			
			String date = row[0].getContents();
			if(date.equals("소계") || date.equals("구   분") || date.isEmpty() )
				logger.debug(" ===== Removed {} from List ===== ", row[0].getContents());
			else{
				// 1st column is site which already read.
//				for(int j=1; j<row.length; j++){
					String value = row[41].getContents();				// j start with 1
					Value val = new Value();
					val.setStreamID(streamIDList.get((41-1)/2));				// j start with 1
					val.setDateTime(date);
					
					if(value.isEmpty() || value.equals("모기"))
						logger.debug(" ===== Removed {} from List ===== ", value);
					else{
						
						val.setValue(Double.parseDouble(value));
						values.add(val);
						logger.debug(" ===== {}th Row - StreamID: {}, DateTime: {}, Value: {} ===== ",i, val.getStreamID(), val.getDateTime(), val.getValue());
//					}
				}
			}	
			
		}
//		MyDB.createValueSet(values);
		// ======================================
		// Step 3. Done.
		// ======================================
	}
	
	public static void upload2015Data(List<String> streamIDList) throws IOException, ParseException, BiffException{
		
		Workbook book = Workbook.getWorkbook(new File("M:\\dms-all-2015.xls"));
		Sheet sheet = book.getSheet(0);
		// ======================================
		// Step 3. Read Value
		// ======================================
		List<Value> values = new ArrayList<Value>();
		for (int i=2; i<sheet.getRows(); i++){
			Cell[] row = sheet.getRow(i);
			
			String date = row[0].getContents();
			if(date.equals("소계") || date.equals("구   분") || date.isEmpty() )
				logger.debug(" ===== Removed {} from List ===== ", row[0].getContents());
			else{
				// 1st column is site which already read.
//				for(int j=1; j<row.length; j++){
					String value = row[20].getContents();				// j start with 1
					Value val = new Value();
					val.setStreamID(streamIDList.get(20-1));				// j start with 1
					val.setDateTime(date);
					
					if(value.isEmpty() || value.equals("ERROR"))
						logger.debug(" ===== Removed {} from List ===== ", value);
					else{
						
						val.setValue(Double.parseDouble(value));
						values.add(val);
						logger.debug(" ===== {}th Row - StreamID: {}, DateTime: {}, Value: {} ===== ",i, val.getStreamID(), val.getDateTime(), val.getValue());
//					}
				}
			}	
			
		}
//		MyDB.createValueSet(values);
		// ======================================
		// Step 3. Done.
		// ======================================
	}
}
