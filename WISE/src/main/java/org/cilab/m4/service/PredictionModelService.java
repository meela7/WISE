package org.cilab.m4.service;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.PredictionModel;

public interface PredictionModelService {
	
	/**
	 * Class Name:	PredictionService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.07.05
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(PredictionModel pred);
	public PredictionModel readInstance(int predID);
	public boolean updateInstance(PredictionModel pred);
	public boolean deleteInstance(int predID);	
	public List<PredictionModel> readCollection();
	
	public List<PredictionModel> search(Map<String, String> map);
	public List<PredictionModel> listSearch(Map<String, List<String>> map);

}
