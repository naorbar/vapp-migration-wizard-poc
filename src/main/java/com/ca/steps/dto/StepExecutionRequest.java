package com.ca.steps.dto;

import java.util.Map;

public class StepExecutionRequest {

	private String id;
	private String className;
	private Map<String, String> members;
	private ExecutionType requestType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, String> getMembers() {
		return members;
	}

	public void setMembers(Map<String, String> members) {
		this.members = members;
	}

	public ExecutionType getRequestType() {
		return requestType;
	}

	public void setRequestType(ExecutionType requestType) {
		this.requestType = requestType;
	}

}
