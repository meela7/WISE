package org.cilab.s4rm.service;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Value;

public interface ValueService {
	
	/**
	 * Class Name:	ValueService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.24
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Value value)  throws Exception;
	public Value readInstance(String valueID)  throws Exception;
	public boolean updateInstance(Value value)  throws Exception;
	public boolean deleteInstance(String valueID)  throws Exception;	
	public List<Value> readCollection()  throws Exception;
	
	public boolean isInstanceExist(String streamID)  throws Exception;
	public List<Value> search(String streamID, String startDate, String endDate)  throws Exception;
	public Map<String, List<Value>> listSearch(Map<String, List<String>> map)  throws Exception;

}
