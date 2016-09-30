package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cilab.m4.model.PredictionModel;
import org.cilab.m4.service.PredictionModelService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import io.swagger.annotations.Api;

@Api(value = "models")
@RestController
public class ModelController {

	/**
	 * Class Name: ModelController.java Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.07.06
	 * @version 1.2
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

	@Autowired
	private PredictionModelService modelService;

	// -------------------- Read and Search PredictionModel Collection Resource
	// --------------------
	@RequestMapping(value = "/models", method = RequestMethod.GET)
	public ResponseEntity<List<PredictionModel>> list(
			@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read PredictionModel collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading PredictionModel Collection Resource ...");
			List<PredictionModel> predList = modelService.readCollection();
			if (predList.isEmpty()) {
				logger.info("No PredictionModels found.");
				return new ResponseEntity<List<PredictionModel>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<PredictionModel>>(predList, HttpStatus.OK);
		}
		// search PredictionModel collection resource with parameters.
		else {
			logger.info("Searching PredictionModel Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(PredictionModel.class);
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
					// set forceEncodingFilter in the web.xml, therefore need
					// decode every value.
					for (String value : params.get(key)) {
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
				return new ResponseEntity<List<PredictionModel>>(HttpStatus.BAD_REQUEST);
			} else {
				List<PredictionModel> predList = this.modelService.listSearch(map);
				if (predList.isEmpty() || predList == null)
					return new ResponseEntity<List<PredictionModel>>(predList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<PredictionModel>>(predList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a PredictionModel Instance Resource
	// ------------------
	@RequestMapping(value = "/models/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody PredictionModel model) {
		// check if PredictionModel contains the Not Null field in the database.
		model.setMethodType("prediction");
		boolean createdID = modelService.newInstance(model);
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// ===== Read a PredictionModel Instance Resource =====
	@RequestMapping(value = "/models/{id}", method = RequestMethod.GET)
	public ResponseEntity<PredictionModel> read(@PathVariable("id") int modelID) {
		logger.info("Reading PredictionModel Instance Resource of ID: {} ...", modelID);
		PredictionModel model = this.modelService.readInstance(modelID);
		if (model == null) {
			logger.info("PredictionModel Instance Resource of ID: {}, not found.", modelID);
			return new ResponseEntity<PredictionModel>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<PredictionModel>(model, HttpStatus.OK);
	}

	// ===== Upload training data file of a PredictionModel Instance Resource
	// =====
	@ResponseStatus(value = HttpStatus.OK) // because he returned "void", say
											// nothing, should return "OK"
											// message.
	@RequestMapping(value = "/models/{id}/trainingData", method = RequestMethod.POST)
	public void uploadTrainingData(@RequestParam MultipartFile file, @PathVariable("id") int id,
			HttpServletRequest request) throws IOException {

		if (file.isEmpty()) {
			logger.debug(" ===== File is Empty ===== ");
		}

		String rootPath = request.getSession().getServletContext().getRealPath("WEB-INF/classes");
		File dir = new File(rootPath + "/predictionModel/trainingData");

		if (!dir.exists()) {
			dir.mkdirs();
		}
		logger.info(" ===== AbstractPath : {} =====", dir.getAbsolutePath());
		File serverFile = new File(dir.getAbsolutePath() + File.separator + id + ".csv"); // file.getOriginalFilename());

		List<String[]> content = new ArrayList<String[]>();
		CSVReader reader = null;
		CSVWriter writer = null;
		try {
			InputStream in = file.getInputStream();
			reader = new CSVReader(new InputStreamReader(in));
			content = reader.readAll();
			writer = new CSVWriter(new FileWriter(serverFile));

			for (String[] data : content) {
				writer.writeNext(data);
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}
	}

	// ===== Download training data file of a PredictionModel Instance Resource
	// =====
	@RequestMapping(value = "/models/{id}/trainingData", method = RequestMethod.GET)
	public void downloadTrainingData(HttpServletResponse response, @PathVariable("id") int id) throws IOException {

		String newFileName = "predictionModel" + id + "-trainingData.csv";
		String fileName = "\\predictionModel\\trainingData\\" + id + ".csv";
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream(fileName);

		List<String[]> content = new ArrayList<String[]>();
		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(in));
			content = reader.readAll();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
			}
		}

		response.setContentType("application/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + newFileName + "\"");

		CSVWriter writer = new CSVWriter(response.getWriter());

		for (String[] data : content) {
			writer.writeNext(data);
		}

		writer.close();
	}

	// ===== Download training data file of a PredictionModel Instance Resource
	// =====
	@RequestMapping(value = "/models/{id}/script", method = RequestMethod.GET)
	public void downloadScript(HttpServletResponse response, @PathVariable("id") int id) throws IOException {

		String newFileName = "predictionModel" + id + "-script.txt";

		response.setContentType("application/text");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + newFileName + "\"");

		PredictionModel model = this.modelService.readInstance(id);

		OutputStream writer = response.getOutputStream();
		writer.write(model.getScriptCode().getBytes());
		writer.close();
	}

	// ------------------ Update a PredictionModel Instance Resource
	// ------------------
	@RequestMapping(value = "/models/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody PredictionModel model, @PathVariable("id") int modelID) {

		PredictionModel oldPredictionModel = this.modelService.readInstance(modelID);
		if (oldPredictionModel == null) {
			logger.info("PredictionModel Instance Resource of ID: {}, not found.", modelID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}

		// set the null of PredictionModel with oldPredictionModel

		Boolean res = this.modelService.updateInstance(model);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a PredictionModel Instance Resource
	// ------------------
	@RequestMapping(value = "/models/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int modelID) {
		logger.info("Reading & Deleting PredictionModel Instance Resource of ID: {} ...", modelID);
		PredictionModel model = this.modelService.readInstance(modelID);
		if (model == null) {
			logger.info("Unable to delete. PredictionModel Instance Resource of ID: {}, not found.", modelID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.modelService.deleteInstance(modelID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Search for PredictionModel Resource
	// --------------------
	@RequestMapping(value = "/models", method = RequestMethod.POST)
	public ResponseEntity<List<PredictionModel>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching PredictionModel Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(PredictionModel.class);
		List<String> variables = new ArrayList<String>();
		for (PropertyDescriptor desc : params) {
			variables.add(desc.getName());
		}

		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String key : reqMap.keySet()) {
			if (variables.contains(key)) {
				String param = key.substring(0, 1).toUpperCase();
				param = param + key.substring(1);
				map.put(param, reqMap.get(key));
			} else
				logger.info("Unexpected Parameter :{} has been removed.", key);
		}
		List<PredictionModel> predList = this.modelService.listSearch(map);
		if (predList.isEmpty() || predList == null)
			return new ResponseEntity<List<PredictionModel>>(predList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<PredictionModel>>(predList, HttpStatus.OK);
	}

}
