package org.cilab.s4rm.dao;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Metadata;

public interface MetaDAO {
	
	/**
	 * Class Name:	MetaDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.17
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(Metadata meta);
	public Metadata read(int metaID);
	public boolean update(Metadata meta);
	public boolean delete(int metaID);
	
	public List<Metadata> list();
	public Metadata getByUniqueKey(String key, String value);
	public List<Metadata> search(Map<String, String> map);
	public List<Metadata> listSearch(Map<String, List<String>> map);

}
