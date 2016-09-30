package org.cilab.m4.service;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Prediction;

public interface PredictionService {
	
	/**
	 * Class Name:	PredictionService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Prediction model);
	public Prediction readInstance(int predID);
	public boolean updateInstance(Prediction pred);
	public boolean deleteInstance(int predID);	
	public List<Prediction> readCollection();
	
	public List<Prediction> search(Map<String, String> map);
	public List<Prediction> listSearch(Map<String, List<String>> map);

}
