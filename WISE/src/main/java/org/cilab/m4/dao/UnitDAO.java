package org.cilab.m4.dao;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Unit;

public interface UnitDAO {
	/**
	 * Class Name:	UnitDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.0
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(Unit unit);
	public Unit read(int unitID);
	public boolean update(Unit unit);
	public boolean delete(int unitID);
	
	public List<Unit> list();
	public Unit getByUniqueKey(String name);
	public List<Unit> search(Map<String, String> map);
	public List<Unit> listSearch(Map<String, List<String>> map);
}
