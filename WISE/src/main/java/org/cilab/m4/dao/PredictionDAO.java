package org.cilab.m4.dao;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Prediction;


public interface PredictionDAO {
	
	/**
	 * Class Name:	PredictionDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(Prediction pred);
	public Prediction read(int predID);
	public boolean update(Prediction pred);
	public boolean delete(int predID);
	
	public List<Prediction> list();
	public List<Prediction> search(Map<String, String> map);
	public List<Prediction> listSearch(Map<String, List<String>> map);

}
