package org.cilab.m4.model;

public class PredictionModel extends Method{
	/**
	 * Class Name: Prediction.java 
	 * Description: Hibernate Mapping pojo File
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.09
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */

	private String ModelingMethod;
	private String ExecuteEnvironment;
	private String PredictType;
	private String Script;
	private String ScriptCode;		
	private String InputData;		// json format
	private String OutputData;		// json format
	private String TrainingData; //file name
	private String Description;

	public String getModelingMethod() {
		return ModelingMethod;
	}
	public void setModelingMethod(String modelingMethod) {
		ModelingMethod = modelingMethod;
	}
	public String getExecuteEnvironment() {
		return ExecuteEnvironment;
	}
	public void setExecuteEnvironment(String executeEnvironment) {
		ExecuteEnvironment = executeEnvironment;
	}
	public String getPredictType() {
		return PredictType;
	}
	public void setPredictType(String predictType) {
		PredictType = predictType;
	}
	public String getScript() {
		return Script;
	}
	public void setScript(String script) {
		Script = script;
	}
	public String getScriptCode() {
		return ScriptCode;
	}
	public void setScriptCode(String scriptCode) {
		ScriptCode = scriptCode;
	}
	public String getInputData() {
		return InputData;
	}
	public void setInputData(String inputData) {
		InputData = inputData;
	}
	public String getOutputData() {
		return OutputData;
	}
	public void setOutputData(String outputData) {
		OutputData = outputData;
	}
	public String getTrainingData() {
		return TrainingData;
	}
	public void setTrainingData(String trainingData) {
		TrainingData = trainingData;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}

	
}
