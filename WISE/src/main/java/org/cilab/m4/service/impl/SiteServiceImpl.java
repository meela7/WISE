package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.SiteDAO;
import org.cilab.m4.model.Site;
import org.cilab.m4.service.SiteService;

public class SiteServiceImpl implements SiteService {

	/**
	 * Class Name:	SiteServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	SiteDAO siteDao;
	
	public void setSiteDao(SiteDAO siteDao) throws Exception {
		this.siteDao = siteDao;
	}
	@Override
	public void newInstance(Site site) throws Exception {
		this.siteDao.create(site);
	}

	@Override
	public Site readInstance(int siteID) throws Exception {
		return this.siteDao.read(siteID);
	}

	@Override
	public void updateInstance(Site site) throws Exception {
		this.siteDao.update(site);
	}

	@Override
	public void deleteInstance(int siteID) throws Exception {
		this.siteDao.delete(siteID);
	}

	@Override
	public List<Site> readCollection() throws Exception {
		return this.siteDao.list();
	}

	@Override
	public boolean isInstanceExist(String name) throws Exception {
		if(this.siteDao.getByUniqueKey(name) != null)
			return true;
		return false;
	}

	@Override
	public List<Site> search(Map<String, String> map) throws Exception {
		return this.siteDao.search(map);
	}

	@Override
	public List<Site> listSearch(Map<String, List<String>> map) throws Exception {
		return this.siteDao.listSearch(map);
	}

}
