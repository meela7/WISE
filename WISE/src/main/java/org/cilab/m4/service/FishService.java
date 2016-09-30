package org.cilab.m4.service;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Fish;

public interface FishService {
	
	/**
	 * Class Name:	FishService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Fish fish);
	public Fish readInstance(int fishID);
	public boolean updateInstance(Fish fish);
	public boolean deleteInstance(int fishID);	
	public List<Fish> readCollection();
	
//	public boolean isInstanceExist(String name);
	public List<Fish> search(Map<String, String> map);
	public List<Fish> listSearch(Map<String, List<String>> map);

}
