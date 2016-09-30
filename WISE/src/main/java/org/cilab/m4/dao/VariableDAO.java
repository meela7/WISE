package org.cilab.m4.dao;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Variable;

public interface VariableDAO {
	/**
	 * Class Name:	VariableDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(Variable variable);
	public Variable read(int variableID);
	public boolean update(Variable variable);
	public boolean delete(int variableID);
	
	public List<Variable> list();
	public Variable getByUniqueKey(String name, int unitID);
	public List<Variable> search(Map<String, String> map);
	public List<Variable> listSearch(Map<String, List<String>> map);
}
