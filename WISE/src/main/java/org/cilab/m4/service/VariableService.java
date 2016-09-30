package org.cilab.m4.service;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Variable;

public interface VariableService {
	
	/**
	 * Class Name:	VariableService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Variable variable);
	public Variable readInstance(int variableID);
	public boolean updateInstance(Variable variable);
	public boolean deleteInstance(int variableID);	
	public List<Variable> readCollection();
	
	public boolean isInstanceExist(String name, int unitID);
	public List<Variable> search(Map<String, String> map);
	public List<Variable> listSearch(Map<String, List<String>> map);

}
