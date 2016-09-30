package org.cilab.s4rm.dao;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Tag;

public interface TagDAO {
	
	/**
	 * Class Name:	TagDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(Tag tag);
	public Tag read(int tagID);
	public boolean update(Tag tag);
	public boolean delete(int tagID);
	
	public List<Tag> list();
	public Tag getByUniqueKey(String name);
	public List<Tag> search(Map<String, String> map);
	public List<Tag> listSearch(Map<String, List<String>> map);

}
