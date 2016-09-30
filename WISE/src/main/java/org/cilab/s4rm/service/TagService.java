package org.cilab.s4rm.service;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Tag;

public interface TagService {
	
	/**
	 * Class Name:	TagService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Tag tag);
	public Tag readInstance(int tagID);
	public boolean updateInstance(Tag tag);
	public boolean deleteInstance(int tagID);	
	public List<Tag> readCollection();
	
	public boolean isInstanceExist(String name);
	public List<Tag> search(Map<String, String> map);
	public List<Tag> listSearch(Map<String, List<String>> map);
}
