package org.cilab.m4.dao;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.PredictionModel;


public interface PredictionModelDAO {
	
	/**
	 * Class Name:	PredictionModelDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(PredictionModel model);
	public PredictionModel read(int modelID);
	public boolean update(PredictionModel model);
	public boolean delete(int modelID);
	
	public List<PredictionModel> list();
	public List<PredictionModel> search(Map<String, String> map);
	public List<PredictionModel> listSearch(Map<String, List<String>> map);

}
