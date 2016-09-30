package org.cilab.wise;

import java.util.List;

import org.cilab.s4rm.model.Value;
import org.cilab.s4rm.service.ValueService;
import org.cilab.s4rm.service.impl.ValueServiceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(ServiceTest.class);
	private ValueService valueService = new ValueServiceImpl();
	
	@Test
	public void testService() {
		
		try {
			List<Value> values = valueService.search("af828ceb-f396-46fd-800f-e5f9ae50b8f6", "2014-06-23", "2014-06-25");
		
			for(Value val: values){
				logger.debug(" ===== StreamID: {}, DateTime: {}, Value: {} ===== ", val.getStreamID(), val.getDateTime(),
						val.getValue());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
