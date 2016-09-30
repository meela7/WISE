package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.FishDAO;
import org.cilab.m4.model.Fish;
import org.cilab.m4.service.FishService;

public class FishServiceImpl implements FishService {

	/**
	 * Class Name:	FishServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.02.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	FishDAO fishDao;
	public void setFishDao(FishDAO fishDao){
		this.fishDao = fishDao;
	}
	
	@Override
	public boolean newInstance(Fish fish) {
		return fishDao.create(fish);
	}

	@Override
	public Fish readInstance(int fishID) {
		Fish fish = fishDao.read(fishID);
		return fish;
	}

	@Override
	public boolean updateInstance(Fish fish) {
		
		return fishDao.update(fish);
	}

	@Override
	public boolean deleteInstance(int fishID) {
		return fishDao.delete(fishID);
	}

	@Override
	public List<Fish> readCollection() {
		return fishDao.list();
	}

	@Override
	public List<Fish> search(Map<String, String> map) {
		return fishDao.search(map);
	}
	@Override
	public List<Fish> listSearch(Map<String, List<String>> map) {
		
		return fishDao.listSearch(map);
	}

}
