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

public class UploadWaterQuality {

	/**
	 * Class Name: WatershedUpload.java Description:
	 * 
	 * @author Meilan Jiang
	 * @since 2016.01.30
	 * @version 1.0
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */

	private static final Logger logger = LoggerFactory.getLogger(UploadWaterQuality.class);

	public static void main(String[] args) throws BiffException, IOException, ParseException {

		Workbook book = Workbook.getWorkbook(new File("M:\\WISE\\2014-2016 센서데이터.xls"));
		Sheet sheet = book.getSheet(0);

		List<String> streamIDList = new ArrayList<String>();
		// 여수대교
//		streamIDList.add("e2f24086-de23-4803-b8f0-608f7b80a031"); 	// water_temperature	3
//		streamIDList.add("97166cfb-6568-4b83-9ae3-c0f52c582fb1"); 	// pH					4 
//		streamIDList.add("c7edf034-dbbf-4fa9-8fa8-c64a41239e6d"); 	// SpCond				5 
//		streamIDList.add("575efddd-9629-4453-92cf-f3969df998da");	// Sal					6 
//		streamIDList.add("f26f5f33-fae8-4863-9f64-ecb2440f3362");	// TDS					7 
//		streamIDList.add("172b419e-5427-4cd2-94e1-89b779779d6e"); 	// TurbSC				8 
//		streamIDList.add("2d9146b6-e770-49aa-81c4-8b040d27ede7"); 	// LDO(%)				9 
//		streamIDList.add("b5ed8410-9190-496b-8ed2-60a6a1f17e25");	// LDO					10 
//		streamIDList.add("af828ceb-f396-46fd-800f-e5f9ae50b8f6"); 	// CHL					11
		
		// 너부대교
		streamIDList.add("8e4bc020-c16f-4180-9c15-74eb955e67fc"); 	// water_temperature	3
		streamIDList.add("060939be-b38c-4dac-94e2-cdf5547bf201"); 	// pH					4		ok
		streamIDList.add("33b62f81-58dd-4406-8437-e768a9ee20b3"); 	// SpCond 				5		ok
		streamIDList.add("daf14268-0dcb-4055-8f66-5928c74f75c7");	// Sal					6		ok
		streamIDList.add("f5d9ef35-0484-46d6-b15a-a7c70f511dfe");	// TDS					7	5	ok
		streamIDList.add("7a8e29b3-b003-4ed3-ba82-1221e941843b"); 	// TurbSC				8	6	ok		
		streamIDList.add("4fd08233-8dfc-4498-99d1-99ee629a04b5"); 	// LDO(%)				9	7	ok
		streamIDList.add("22db7bc4-c479-40c7-a722-6a3a2e7499e4");	// LDO					10	8	ok
		streamIDList.add("4397b17c-a4bd-486d-9476-a475a3bb08ef"); 	// CHL					11	9	ok

		// ======================================
		// Step 1. Read Value
		// ======================================
		List<Value> values = new ArrayList<Value>();
		for (int i = 2; i < sheet.getRows(); i++) {	//			sheet.getRows()
			Cell[] row = sheet.getRow(i);
			if(row.length == 0 || row[0].getContents().isEmpty())
				logger.debug(" ===== No content in {} th Row. ===== ", i);
			else{ 
				
				String date = row[0].getContents() + " " + row[1].getContents();
				if (date.equals("소계") || date.equals("구   분") || date.isEmpty())
					logger.debug(" ===== Removed {} from List ===== ", row[2].getContents());
				else {
					// 1st column is site which already read.
//					logger.debug(" ===== {} th Row. =====", i+1);
//					for (int j = 3; j < row.length-5; j++) {
						String value = row[9].getContents();		// =============================================
						
	
						if (value.isEmpty() || value.equals("오류"))
							logger.debug(" ===== Removed {} from List ===== ", value);
						else {
							Value val = new Value();
							val.setStreamID(streamIDList.get(9 - 3));	// =============================================
							val.setDateTime(date);
							val.setValue(Double.parseDouble(value));
							values.add(val);
							logger.debug(" ===== StreamID: {}, DateTime: {}, Value: {} ===== ", 
									 val.getStreamID(), val.getDateTime(), val.getValue());
//						}
	
					}
				}			
			}

		}
//		MyDB.createWaterQualityValueSet(values);
		// ======================================
		// Step 1. Done.
		// ======================================
	}

}
