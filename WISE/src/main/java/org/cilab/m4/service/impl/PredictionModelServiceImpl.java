package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.PredictionModelDAO;
import org.cilab.m4.model.PredictionModel;
import org.cilab.m4.service.PredictionModelService;

public class PredictionModelServiceImpl implements PredictionModelService {

	/**
	 * Class Name:	PredictionModelServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.07.06
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	PredictionModelDAO modelDao;
	
	public void setModelDao(PredictionModelDAO modelDao){
		this.modelDao = modelDao;
	}
	
	@Override
	public boolean newInstance(PredictionModel model) {
		return modelDao.create(model);
	}

	@Override
	public PredictionModel readInstance(int modelID) {
		PredictionModel model = modelDao.read(modelID);
		return model;
	}

	@Override
	public boolean updateInstance(PredictionModel model) {
		return modelDao.update(model);
	}

	@Override
	public boolean deleteInstance(int modelID) {
		return modelDao.delete(modelID);
	}

	@Override
	public List<PredictionModel> readCollection() {
		return modelDao.list();
	}


	@Override
	public List<PredictionModel> search(Map<String, String> map) {
		return modelDao.search(map);
	}

	@Override
	public List<PredictionModel> listSearch(Map<String, List<String>> map) {
		return modelDao.listSearch(map);
	}
}
