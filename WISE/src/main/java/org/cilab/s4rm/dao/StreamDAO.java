package org.cilab.s4rm.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Stream;

public interface StreamDAO {
	/**
	 * Class Name:	StreamDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	public boolean create(Stream stream) throws SQLException;
	public Stream read(String streamID) throws SQLException;
	public boolean update(Stream stream) throws SQLException;
	public boolean delete(String streamID) throws SQLException;	
	public List<Stream> list() throws SQLException;	
	
	public Stream getByUniqueKey(String createdAt, String sensorID) throws SQLException;
	public List<Stream> search(Map<String, String> map) throws SQLException;
	public List<Stream> listSearch(Map<String, List<String>> map) throws SQLException;
}
