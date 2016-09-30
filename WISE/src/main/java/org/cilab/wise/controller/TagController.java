package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Tag;
import org.cilab.s4rm.service.TagService;
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

@Api(value = "tags")
@RestController
public class TagController {

	/**
	 * Class Name: TagController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(TagController.class);

	@Autowired
	private TagService tagService;

	// -------------------- Read and Search Tag Collection Resource
	// --------------------
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public ResponseEntity<List<Tag>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Tag collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Tag Collection Resource ...");
			List<Tag> tags = tagService.readCollection();
			if (tags.isEmpty()) {
				logger.info("No Tags found.");
				return new ResponseEntity<List<Tag>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Tag>>(tags, HttpStatus.OK);
		}
		// search Tag collection resource with parameters.
		else {
			logger.info("Searching Tag Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Tag.class);
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
				return new ResponseEntity<List<Tag>>(HttpStatus.BAD_REQUEST);
			} else {
				List<Tag> tags = this.tagService.listSearch(map);
				if (tags.isEmpty() || tags == null)
					return new ResponseEntity<List<Tag>>(HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Tag>>(tags, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Tag Instance Resource ------------------
	@RequestMapping(value = "/tags/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Tag tag) {
		logger.info("Creating Tag Instance Resource of Name: {} ..." + tag.getName());
		// check if tag contains the Not Null field in the database.
		if (tag.getName() == null)
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);

		if (tagService.isInstanceExist(tag.getName())) {
			logger.info("A Tag with name {} already exist.", tag.getName());
			return new ResponseEntity<Boolean>(HttpStatus.CONFLICT);
		}

		boolean createRes = tagService.newInstance(tag);
		return new ResponseEntity<Boolean>(createRes, HttpStatus.CREATED);
	}

	// -------------------- Read a Tag Instance Resource --------------------
	@RequestMapping(value = "/tags/{id}", method = RequestMethod.GET)
	public ResponseEntity<Tag> read(@PathVariable("id") int tagID) {
		logger.info("Reading Tag Instance Resource of ID: {} ...", tagID);
		Tag tag = this.tagService.readInstance(tagID);
		if (tag == null) {
			logger.info("Tag Instance Resource of ID: {}, not found.", tagID);
			return new ResponseEntity<Tag>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Tag>(tag, HttpStatus.OK);
	}

	// -------------------- Update a Tag Instance Resource ------------------
	@RequestMapping(value = "/tags/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Tag tag, @PathVariable("id") int tagID) {
		logger.info("Updating Tag Instance Resource of ID: {} ...", tag.getTagID());

		if (tagID != tag.getTagID()) {
			logger.info("Tag Instance Resource of ID: {} , {} doesn't match.", tagID, tag.getTagID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Tag oldTag = this.tagService.readInstance(tagID);
			if (oldTag == null) {
				logger.info("Tag Instance Resource of ID: {}, not found.", tagID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Tag with oldTag

		Boolean res = this.tagService.updateInstance(tag);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Tag Instance Resource ------------------
	@RequestMapping(value = "/tags/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int tagID) {
		logger.info("Reading & Deleting Tag Instance Resource of ID: {} ...", tagID);
		Tag tag = this.tagService.readInstance(tagID);
		if (tag == null) {
			logger.info("Unable to delete. Tag Instance Resource of ID: {}, not found.", tagID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.tagService.deleteInstance(tagID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); // when Tag has existing related Sites
	}

	// -------------------- Search for Tag Resource --------------------
	@RequestMapping(value = "/tags", method = RequestMethod.POST)
	public ResponseEntity<List<Tag>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Tag Resource ...");

		// remove the parameters which doesn't match with column in the list
		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Tag.class);
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
		List<Tag> tags = this.tagService.listSearch(map);
		if (tags.isEmpty() || tags == null)
			return new ResponseEntity<List<Tag>>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Tag>>(tags, HttpStatus.OK);
	}

}
