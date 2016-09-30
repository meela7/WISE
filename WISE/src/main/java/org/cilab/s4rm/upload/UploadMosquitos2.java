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

public class UploadMosquitos2 {

	/**
	 * Class Name: WatershedUpload.java Description:
	 * 
	 * @author Meilan Jiang
	 * @since 2016.01.30
	 * @version 1.0
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */

	private static final Logger logger = LoggerFactory.getLogger(UploadMosquitos2.class);

	public static void main(String[] args) throws BiffException, IOException, ParseException {

//		Workbook book = Workbook.getWorkbook(new File("M:\\WISE\\여의도고등학교2015.xls"));
//		Workbook book = Workbook.getWorkbook(new File("M:\\WISE\\윤중초등학교2015.xls"));
		Workbook book = Workbook.getWorkbook(new File("M:\\WISE\\문래동빗물펌프장2015.xls"));
		Sheet sheet = book.getSheet(0);
		
//		List<String> dateList = new ArrayList<String>();
//		String streamID  = "7c54bfeb-ad61-4149-b9e3-942645d8fadf"; //여의도고등학교
//		String streamID  = "946ec114-cb5f-4b88-8eaa-6536a589315c"; //윤중초등학교
		String streamID  = "2387bfbe-5a76-4da5-93a5-66743475203d"; //문래동빗물펌프장

		List<Value> values = new ArrayList<Value>();
		// ======================================
		// Step 1. Read Value
		// ======================================
		
		for (int i=0; i<sheet.getRows(); i++){
			Cell[] row = sheet.getRow(i);
			String date = row[0].getContents();

			String value = row[1].getContents();
			Value val = new Value();
			val.setStreamID(streamID);
			val.setDateTime(date);
			
			if(value.isEmpty() || value.equals("ERROR"))
				logger.debug(" ===== Removed {} from List ===== ", value);
			else{
				val.setValue(Double.parseDouble(value));
				logger.debug(" ===== {}th Row. StreamID: {}, DateTime: {}, Value: {} ===== ",i+1 , val.getStreamID(), val.getDateTime(), val.getValue());
				values.add(val);
			}			
		}
//		MyDB.createValueSet(values);
		// ======================================
		// Step 1. Done.
		// ======================================
	}
}
