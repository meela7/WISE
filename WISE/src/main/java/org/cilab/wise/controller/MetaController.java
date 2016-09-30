package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Metadata;
import org.cilab.s4rm.service.MetaService;
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

@Api(value = "metas")
@RestController
public class MetaController {

	/**
	 * Class Name: MetadataController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MetaController.class);

	@Autowired
	private MetaService metaService;

	// -------------------- Read and Search Metadata Collection Resource
	// --------------------
	@RequestMapping(value = "/metas", method = RequestMethod.GET)
	public ResponseEntity<List<Metadata>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Metadata collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Metadata Collection Resource ...");
			List<Metadata> metas = metaService.readCollection();
			if (metas.isEmpty()) {
				logger.info("No Metadatas found.");
				return new ResponseEntity<List<Metadata>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Metadata>>(metas, HttpStatus.OK);
		}
		// search Metadata collection resource with parameters.
		else {
			logger.info("Searching Metadata Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Metadata.class);
			List<String> variables = new ArrayList<String>();
			for (PropertyDescriptor desc : props) {
				variables.add(desc.getName());
			}
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String key : params.keySet()) {
				if (variables.contains(key)) {
					// uppercase first letter of property name
					String param = key.substring(0, 1).toUpperCase();
					param = param + key.substring(1);

					List<String> values = new ArrayList<String>();
					for(String value: params.get(key)){
						// decode parameters
						try {
							values.add(new String(value.getBytes("8859_1"), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					map.put(param, values);
				} else
					logger.info("Unexpected Parameter :{} has been removed.", key);
			}
			if (map.keySet().size() == 0) {
				return new ResponseEntity<List<Metadata>>(HttpStatus.BAD_REQUEST);
			} else {
				List<Metadata> metas = this.metaService.listSearch(map);
				if (metas.isEmpty() || metas == null)
					return new ResponseEntity<List<Metadata>>(HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Metadata>>(metas, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Metadata Instance Resource ------------------
	@RequestMapping(value = "/metas/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Metadata meta) {
		logger.info("Creating Metadata Instance Resource of Key: {}, Value: {}.", meta.getKey(), meta.getValue());
		// check if meta contains the Not Null field in the database.
		if (meta.getKey() == null || meta.getValue() == null)
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);

		if (metaService.isInstanceExist(meta.getKey(), meta.getValue())) {
			logger.info("A Metadata with key: {}, and value: {} already exist.", meta.getKey(), meta.getValue());
			return new ResponseEntity<Boolean>(HttpStatus.CONFLICT);
		}

		boolean createRes = metaService.newInstance(meta);
		return new ResponseEntity<Boolean>(createRes, HttpStatus.CREATED);
	}

	// -------------------- Read a Metadata Instance Resource --------------------
	@RequestMapping(value = "/metas/{id}", method = RequestMethod.GET)
	public ResponseEntity<Metadata> read(@PathVariable("id") int metaID) {
		logger.info("Reading Metadata Instance Resource of ID: {} ...", metaID);
		Metadata meta = this.metaService.readInstance(metaID);
		if (meta == null) {
			logger.info("Metadata Instance Resource of ID: {}, not found.", metaID);
			return new ResponseEntity<Metadata>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Metadata>(meta, HttpStatus.OK);
	}

	// -------------------- Update a Metadata Instance Resource ------------------
	@RequestMapping(value = "/metas/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Metadata meta, @PathVariable("id") int metaID) {
		logger.info("Updating Metadata Instance Resource of ID: {} ...", meta.getMetadataID());

		if (metaID != meta.getMetadataID()) {
			logger.info("Metadata Instance Resource of ID: {} , {} doesn't match.", metaID, meta.getMetadataID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Metadata oldMetadata = this.metaService.readInstance(metaID);
			if (oldMetadata == null) {
				logger.info("Metadata Instance Resource of ID: {}, not found.", metaID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Metadata with oldMetadata

		Boolean res = this.metaService.updateInstance(meta);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Metadata Instance Resource ------------------
	@RequestMapping(value = "/metas/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int metaID) {
		logger.info("Reading & Deleting Metadata Instance Resource of ID: {} ...", metaID);
		Metadata meta = this.metaService.readInstance(metaID);
		if (meta == null) {
			logger.info("Unable to delete. Metadata Instance Resource of ID: {}, not found.", metaID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.metaService.deleteInstance(metaID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); // when Metadata has existing related Sites
	}

	// -------------------- Search for Metadata Resource --------------------
	@RequestMapping(value = "/metas", method = RequestMethod.POST)
	public ResponseEntity<List<Metadata>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Metadata Resource ...");

		// remove the parameters which doesn't match with column in the list
		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Metadata.class);
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
		List<Metadata> metas = this.metaService.listSearch(map);
		if (metas.isEmpty() || metas == null)
			return new ResponseEntity<List<Metadata>>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Metadata>>(metas, HttpStatus.OK);
	}

}
