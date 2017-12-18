package com.ca.steps.dto;

import java.util.List;

public class StepMetadata {

	private String className;
	private List<String> mandatoryFields;
	private List<String> optionalFields;
	private List<String> supportedMethods;
	private List<String> stepDependencies;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<String> getMandatoryFields() {
		return mandatoryFields;
	}

	public void setMandatoryFields(List<String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}

	public List<String> getOptionalFields() {
		return optionalFields;
	}

	public void setOptionalFields(List<String> optionalFields) {
		this.optionalFields = optionalFields;
	}

	public List<String> getSupportedMethods() {
		return supportedMethods;
	}

	public void setSupportedMethods(List<String> supportedMethods) {
		this.supportedMethods = supportedMethods;
	}

	public List<String> getStepDependencies() {
		return stepDependencies;
	}

	public void setStepDependencies(List<String> stepDependencies) {
		this.stepDependencies = stepDependencies;
	}

}
