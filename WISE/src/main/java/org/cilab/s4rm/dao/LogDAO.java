package org.cilab.s4rm.dao;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Log;

public interface LogDAO {
	/**
	 * Class Name:	LogDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(Log log);
	public Log read(int logID);
	public boolean update(Log log);
	public boolean delete(int logID);
	
	public List<Log> list();
	public Log getByUniqueKey(String startTime, String streamID);
	public List<Log> search(Map<String, String> map);
	public List<Log> listSearch(Map<String, List<String>> map);
}
