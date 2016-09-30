package org.cilab.s4rm.service;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Log;

public interface LogService {
	
	/**
	 * Class Name:	StreamService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.20
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	public boolean newInstance(Log log);
	public Log readInstance(int logID);
	public boolean updateInstance(Log log);
	public boolean deleteInstance(int logID);	
	public List<Log> readCollection();
	
	public boolean isInstanceExist(String startTime, String streamID);
	public List<Log> search(Map<String, String> map);
	public List<Log> listSearch(Map<String, List<String>> map);

}
