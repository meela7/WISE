package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Site;
import org.cilab.m4.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "sites")
@RestController
public class SiteController {

	/**
	 * Class Name: SiteController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SiteController.class);

	@Autowired
	private SiteService siteService;


	// -------------------- Read and Search Site Collection Resource --------------------
	@RequestMapping(value = "/sites", method = RequestMethod.GET)
	public ResponseEntity<List<Site>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Method collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Site Collection Resource ...");
			List<Site> siteList = null;
			try {
				siteList = siteService.readCollection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (siteList.isEmpty()) {
				logger.info("No Site found.");
				return new ResponseEntity<List<Site>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Site>>(siteList, HttpStatus.OK);
		}
		// search Method collection resource with parameters.
		else {
			logger.info("Searching Site Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Site.class);
			List<String> variables = new ArrayList<String>();
			for (PropertyDescriptor desc : props) {
				variables.add(desc.getName());
			}
			
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String key : params.keySet()) {
				if(variables.contains(key)){
					// uppercase first letter of property name
					String param = key.substring(0,1).toUpperCase();
					param = param + key.substring(1);				
				
					List<String> values = new ArrayList<String>();
					// set forceEncodingFilter in the web.xml, therefore need decode every value.
					for(String value: params.get(key)){
						try {
							values.add(new String(value.getBytes("8859_1"), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					map.put(param, values);			
				}
				else
					logger.info("Unexpected Parameter :{} has been removed.", key);				
			}
			if(map.keySet().size() == 0){
				return new ResponseEntity<List<Site>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Site> siteList = null;
				try {
					siteList = this.siteService.listSearch(map);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (siteList.isEmpty() || siteList == null)
					return new ResponseEntity<List<Site>>(siteList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Site>>(siteList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Site Instance Resource ------------------
	@RequestMapping(value = "/sites/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Site site){
		logger.info("Creating Site Instance Resource of Name: {} ..." + site.getSiteName());
		
		if (site.getSiteName() == null || site.getLatitude() == null || site.getLongitude() == null ) 
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		
		try {
			if (siteService.isInstanceExist(site.getSiteName())) {
				logger.info("A Site with name {} already exist.", site.getSiteName());
				return new ResponseEntity<Boolean>(HttpStatus.CONFLICT);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			siteService.newInstance(site) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
	}

	// -------------------- Read a Site Instance Resource --------------------
	@RequestMapping(value = "/sites/{id}", method = RequestMethod.GET)
	public ResponseEntity<Site> read(@PathVariable("id") int siteID) {
		logger.info("Reading Method Instance Resource of ID: {} ...", siteID);
		Site site = null;
		try {
			site = this.siteService.readInstance(siteID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (site == null) {
			logger.info("Site Instance Resource of ID: {}, not found.", siteID);
			return new ResponseEntity<Site>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Site>(site, HttpStatus.OK);
	}

	// -------------------- Update a Site Instance Resource ------------------
	@RequestMapping(value = "/sites/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Site site, @PathVariable("id") int siteID) {
		logger.info("Updating Site Instance Resource of ID: {} ...", site.getSiteID());

		if (siteID != site.getSiteID()) {
			logger.info("Site Instance Resource of ID: {} , {} doesn't match.", siteID, site.getSiteID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Site oldSite = null;
			try {
				oldSite = this.siteService.readInstance(siteID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (oldSite == null) {
				logger.info("Site Instance Resource of ID: {}, not found.", siteID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Method with oldMethod
		try {
			this.siteService.updateInstance(site);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
		}	
			
	}

	// -------------------- Delete a Site Instance Resource ------------------
	@RequestMapping(value = "/sites/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int siteID) {
		logger.info("Reading & Deleting Site Instance Resource of ID: {} ...", siteID);
		Site site = null;
		try {
			site = this.siteService.readInstance(siteID);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (site == null) {
			logger.info("Unable to delete. Site Instance Resource of ID: {}, not found.", siteID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		try {
			this.siteService.deleteInstance(siteID);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT); 
		}			
	}

	// -------------------- Search for Site Resource --------------------
	@RequestMapping(value = "/sites", method = RequestMethod.POST)
	public ResponseEntity<List<Site>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Site Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Site.class);
		List<String> variables = new ArrayList<String>();
		for (PropertyDescriptor desc : params) {
			variables.add(desc.getName());
		}
			
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String key : reqMap.keySet()) {
			if(variables.contains(key)){
				String param = key.substring(0,1).toUpperCase();
				param = param + key.substring(1);
				map.put(param, reqMap.get(key));
			}
			else
				logger.info("Unexpected Parameter :{} has been removed.", key);
		}
		List<Site> siteList = null;
		try {
			siteList = this.siteService.listSearch(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (siteList.isEmpty() || siteList == null)
			return new ResponseEntity<List<Site>>(siteList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Site>>(siteList, HttpStatus.OK);
	}

}
